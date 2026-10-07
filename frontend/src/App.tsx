import { useEffect, useState, type FormEvent } from "react";
import HomePage from "./home/HomePage";
import { ClosingCta, SiteFooter } from "./home/ClosingCta";

type AccountSession = {
  loginId: string;
  displayName: string;
  phoneNumber: string | null;
  role: "FARMER" | "OPERATOR";
  mustChangePassword: boolean;
};

type ProvisionedFarmer = {
  fullName: string;
  phoneNumber: string;
  fanNumber: string;
  temporaryPassword: string;
  mustChangePassword: boolean;
};

async function getCsrfToken(): Promise<string> {
  const response = await fetch("/api/v1/auth/csrf", { credentials: "same-origin" });
  if (!response.ok) {
    throw new Error("Unable to start a secure account request.");
  }
  const csrfCookie = document.cookie
    .split(";")
    .map((cookie) => cookie.trim())
    .find((cookie) => cookie.startsWith("XSRF-TOKEN="));
  if (!csrfCookie) {
    throw new Error("The security cookie was not provided by the server.");
  }
  return decodeURIComponent(csrfCookie.slice("XSRF-TOKEN=".length));
}

async function postAuthJson<T>(url: string, body: object): Promise<T> {
  const token = await getCsrfToken();
  const response = await fetch(url, {
    method: "POST",
    credentials: "same-origin",
    headers: {
      "Content-Type": "application/json",
      "X-XSRF-TOKEN": token,
    },
    body: JSON.stringify(body),
  });
  if (!response.ok) {
    let message = `Account request failed (${response.status}).`;
    try {
      const payload = (await response.json()) as { detail?: string; message?: string };
      message = payload.detail ?? payload.message ?? message;
    } catch {
      // Keep the status-based message when the server response is not JSON.
    }
    throw new Error(message);
  }
  return (await response.json()) as T;
}

type ApiHealth = {
  status: string;
};

type ProductCategory = {
  id: number;
  name: string;
  slug: string;
  description?: string;
};

type Product = {
  id: number;
  name: string;
  slug: string;
  description?: string;
  origin: string;
  unit: string;
  price: number;
  currency: string;
  stockQuantity: number;
  categoryName: string;
  imageUrl?: string;
  sellerName?: string;
  sellerLocation?: string;
};

type ProductDetail = Product & {
  status?: string;
  categorySlug?: string;
  sellerRating?: number;
};

type CartLine = {
  productId: number;
  name: string;
  slug: string;
  unit: string;
  price: number;
  currency: string;
  quantity: number;
  sellerName?: string;
};

type CheckoutForm = {
  buyerName: string;
  buyerEmail: string;
  shippingAddress: string;
  notes: string;
};

type CreateOrderRequest = {
  buyerName: string;
  buyerEmail: string;
  shippingAddress: string;
  notes: string;
  paymentStatus?: string;
  items: Array<{
    productId: number;
    name: string;
    slug: string;
    unit: string;
    price: number;
    currency: string;
    quantity: number;
    sellerName?: string;
  }>;
};

type CatalogResponse = {
  categories: ProductCategory[];
  products: Product[];
};

type RecentOrder = {
  id: number;
  buyerName: string;
  status: string;
  paymentStatus?: string;
  totalAmount: number;
  createdAt: string;
  itemCount: number;
};

type Shipment = {
  id: number;
  orderId: number;
  carrier: string;
  trackingCode: string;
  origin: string;
  destination: string;
  status: string;
  eta: string;
  notes?: string;
  createdAt: string;
};

type Seller = {
  id: number;
  name: string;
  slug: string;
  location: string;
  description?: string;
  rating: number;
  productCount?: number;
};

type SellerProduct = {
  id: number;
  name: string;
  slug: string;
  unit: string;
  price: number;
  currency: string;
  categoryName?: string;
};

type SellerDetail = Seller & {
  productCount: number;
  products: SellerProduct[];
};

type SellerInventoryItem = {
  id: number;
  name: string;
  slug: string;
  unit: string;
  price: number;
  currency: string;
  stockQuantity: number;
  status?: string;
  lowStock: boolean;
  categoryName?: string;
};

type Farmer = {
  id: number;
  name: string;
  slug: string;
  region: string;
  cropFocus: string;
  farmSize: string;
  rating: number;
  bio?: string;
  harvestsThisSeason?: number;
};

function App() {
  const [apiStatus, setApiStatus] = useState<"checking" | "online" | "offline">(
    "checking",
  );
  const [selectedCategory, setSelectedCategory] = useState("all");
  const [searchTerm, setSearchTerm] = useState("");
  const [sortOption, setSortOption] = useState("name_asc");
  const [selectedProduct, setSelectedProduct] = useState<ProductDetail | null>(null);
  const [catalog, setCatalog] = useState<CatalogResponse>({
    categories: [],
    products: [],
  });
  const [farmers, setFarmers] = useState<Farmer[]>([]);
  const [sellers, setSellers] = useState<Seller[]>([]);
  const [selectedSeller, setSelectedSeller] = useState<SellerDetail | null>(null);
  const [sellerInventory, setSellerInventory] = useState<SellerInventoryItem[]>([]);
  const [sellerProfileDraft, setSellerProfileDraft] = useState({
    name: "",
    location: "",
    description: "",
    rating: 4.8,
  });
  const [inventoryEdits, setInventoryEdits] = useState<Record<string, number>>({});
  const [cart, setCart] = useState<CartLine[]>([]);
  const [checkoutVisible, setCheckoutVisible] = useState(false);
  const [checkoutForm, setCheckoutForm] = useState<CheckoutForm>({
    buyerName: "",
    buyerEmail: "",
    shippingAddress: "",
    notes: "",
  });
  const [orderConfirmation, setOrderConfirmation] = useState<string | null>(null);
  const [recentOrders, setRecentOrders] = useState<RecentOrder[]>([]);
  const [shipments, setShipments] = useState<Shipment[]>([]);
  const [authMode, setAuthMode] = useState<"login" | "register">("login");
  const [loginForm, setLoginForm] = useState({ loginId: "", password: "" });
  const [registerForm, setRegisterForm] = useState({
    fullName: "",
    fanNumber: "",
    phoneNumber: "",
    password: "",
  });
  const [passwordDraft, setPasswordDraft] = useState("");
  const [account, setAccount] = useState<AccountSession | null>(null);
  const [operatorFarmer, setOperatorFarmer] = useState({
    fullName: "",
    fanNumber: "",
    phoneNumber: "",
  });
  const [temporaryPassword, setTemporaryPassword] = useState<ProvisionedFarmer | null>(null);
  const [authMessage, setAuthMessage] = useState<string | null>(null);

  useEffect(() => {
    const controller = new AbortController();
    fetch("/api/v1/auth/me", {
      credentials: "same-origin",
      signal: controller.signal,
    })
      .then(async (response) => {
        if (!response.ok) {
          return null;
        }
        return (await response.json()) as AccountSession;
      })
      .then((currentAccount) => {
        if (currentAccount?.loginId && currentAccount.role) {
          setAccount(currentAccount);
        }
      })
      .catch(() => {
        if (!controller.signal.aborted) {
          setAccount(null);
        }
      });
    return () => controller.abort();
  }, []);

  useEffect(() => {
    const controller = new AbortController();

    fetch("/api/v1/health", { signal: controller.signal })
      .then((response) => {
        if (!response.ok) {
          throw new Error(`Health request failed: ${response.status}`);
        }
        return response.json() as Promise<ApiHealth>;
      })
      .then((health) => {
        setApiStatus(health.status === "UP" ? "online" : "offline");
      })
      .catch(() => {
        if (!controller.signal.aborted) {
          setApiStatus("offline");
        }
      });

    return () => controller.abort();
  }, []);

  useEffect(() => {
    const controller = new AbortController();
    const params = new URLSearchParams();

    if (selectedCategory !== "all") {
      params.set("category", selectedCategory);
    }
    if (searchTerm.trim()) {
      params.set("search", searchTerm.trim());
    }
    if (sortOption !== "name_asc") {
      params.set("sort", sortOption);
    }

    const catalogUrl = `/api/v1/catalog${params.size > 0 ? `?${params.toString()}` : ""}`;

    fetch(catalogUrl, { signal: controller.signal })
      .then((response) => {
        if (!response.ok) {
          throw new Error(`Catalog request failed: ${response.status}`);
        }
        return response.json() as Promise<Partial<CatalogResponse>>;
      })
      .then((payload) => {
        setCatalog({
          categories: payload.categories ?? [],
          products: payload.products ?? [],
        });
      })
      .catch(() => {
        if (!controller.signal.aborted) {
          setCatalog({ categories: [], products: [] });
        }
      });

    return () => controller.abort();
  }, [selectedCategory, searchTerm, sortOption]);

  useEffect(() => {
    const controller = new AbortController();

    fetch("/api/v1/farmers", { signal: controller.signal })
      .then((response) => {
        if (!response.ok) {
          throw new Error(`Farmer request failed: ${response.status}`);
        }
        return response.json() as Promise<unknown>;
      })
      .then((payload) => setFarmers(Array.isArray(payload) ? (payload as Farmer[]) : []))
      .catch(() => {
        if (!controller.signal.aborted) {
          setFarmers([]);
        }
      });

    return () => controller.abort();
  }, []);

  useEffect(() => {
    const controller = new AbortController();

    fetch("/api/v1/sellers", { signal: controller.signal })
      .then((response) => {
        if (!response.ok) {
          throw new Error(`Seller request failed: ${response.status}`);
        }
        return response.json() as Promise<unknown>;
      })
      .then((payload) => setSellers(Array.isArray(payload) ? payload as Seller[] : []))
      .catch(() => {
        if (!controller.signal.aborted) {
          setSellers([]);
        }
      });

    return () => controller.abort();
  }, []);

  const fetchRecentOrders = async () => {
    try {
      const response = await fetch("/api/v1/orders");
      if (!response.ok) {
        throw new Error(`Orders request failed: ${response.status}`);
      }
      const payload = (await response.json()) as unknown;
      setRecentOrders(Array.isArray(payload) ? (payload as RecentOrder[]) : []);
    } catch {
      setRecentOrders([]);
    }
  };

  const fetchShipments = async () => {
    try {
      const response = await fetch("/api/v1/shipments");
      if (!response.ok) {
        throw new Error(`Shipments request failed: ${response.status}`);
      }
      const payload = (await response.json()) as unknown;
      setShipments(Array.isArray(payload) ? (payload as Shipment[]) : []);
    } catch {
      setShipments([]);
    }
  };

  useEffect(() => {
    void fetchRecentOrders();
    void fetchShipments();
  }, []);

  const statusLabel = {
    checking: "Checking connection",
    online: "Platform services online",
    offline: "API currently unavailable",
  }[apiStatus];

  const categoryOptions = [{ slug: "all", name: "All products" }, ...catalog.categories];
  const featuredProducts = catalog.products.slice(0, 3);
  const lowStockProducts = catalog.products.filter((product) => product.stockQuantity <= 30).slice(0, 3);
  const activeShipments = shipments.filter((shipment) =>
    ["PENDING", "IN_TRANSIT", "DISPATCHED"].includes(shipment.status),
  );
  const totalRevenue = recentOrders.reduce((sum, order) => sum + order.totalAmount, 0);
  const averagePrice =
    catalog.products.length > 0
      ? catalog.products.reduce((sum, product) => sum + Number(product.price), 0) / catalog.products.length
      : 0;
  const sellerMetrics = {
    listings: catalog.products.length,
    lowStock: lowStockProducts.length,
    revenue: totalRevenue,
    shipments: activeShipments.length,
    avgPrice: averagePrice,
  };

  const fetchProductDetail = async (product: Product) => {
    try {
      const response = await fetch(
        `/api/v1/catalog/products/${encodeURIComponent(product.slug)}`,
      );
      if (!response.ok) {
        throw new Error(`Product request failed: ${response.status}`);
      }
      const detail = (await response.json()) as Partial<ProductDetail>;
      setSelectedProduct({
        ...product,
        ...detail,
        categoryName: detail.categoryName ?? product.categoryName,
      });
    } catch {
      setSelectedProduct({
        ...product,
        status: product.stockQuantity > 0 ? "AVAILABLE" : "OUT_OF_STOCK",
      });
    }
  };

  const addToCart = (product: Product) => {
    setCart((currentCart) => {
      const existingItem = currentCart.find((item) => item.productId === product.id);
      if (existingItem) {
        return currentCart.map((item) =>
          item.productId === product.id
            ? { ...item, quantity: item.quantity + 1 }
            : item,
        );
      }

      return [
        ...currentCart,
        {
          productId: product.id,
          name: product.name,
          slug: product.slug,
          unit: product.unit,
          price: Number(product.price),
          currency: product.currency,
          quantity: 1,
          sellerName: product.sellerName,
        },
      ];
    });
  };

  const changeCartQuantity = (productId: number, delta: number) => {
    setCart((currentCart) =>
      currentCart
        .map((item) =>
          item.productId === productId
            ? { ...item, quantity: Math.max(0, item.quantity + delta) }
            : item,
        )
        .filter((item) => item.quantity > 0),
    );
  };

  const cartSubtotal = cart.reduce(
    (total, item) => total + item.price * item.quantity,
    0,
  );

  const handleLoginSubmit = async (event: FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    setAuthMessage(null);
    try {
      const signedIn = await postAuthJson<AccountSession>("/api/v1/auth/login", loginForm);
      setAccount(signedIn);
      setLoginForm({ loginId: "", password: "" });
      setAuthMessage(
        signedIn.mustChangePassword
          ? "Your operator-issued temporary password must be changed before continuing."
          : `Welcome, ${signedIn.displayName}.`,
      );
    } catch (error) {
      setAuthMessage(error instanceof Error ? error.message : "Unable to sign in.");
    }
  };

  const handleRegisterSubmit = async (event: FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    setAuthMessage(null);
    try {
      await postAuthJson<AccountSession>("/api/v1/auth/register", registerForm);
      setAuthMode("login");
      setLoginForm({ loginId: registerForm.phoneNumber, password: "" });
      setRegisterForm({ fullName: "", fanNumber: "", phoneNumber: "", password: "" });
      setAuthMessage("Farmer account created. Sign in with your phone number and password.");
    } catch (error) {
      setAuthMessage(error instanceof Error ? error.message : "Unable to create the farmer account.");
    }
  };

  const handlePasswordChange = async (event: FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    setAuthMessage(null);
    try {
      const updatedAccount = await postAuthJson<AccountSession>(
        "/api/v1/auth/change-password",
        { newPassword: passwordDraft },
      );
      setAccount(updatedAccount);
      setPasswordDraft("");
      setAuthMessage("Password updated successfully.");
    } catch (error) {
      setAuthMessage(error instanceof Error ? error.message : "Unable to update the password.");
    }
  };

  const handleOperatorProvision = async (event: FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    setAuthMessage(null);
    setTemporaryPassword(null);
    try {
      const token = await getCsrfToken();
      const response = await fetch("/api/v1/operator/farmers", {
        method: "POST",
        credentials: "same-origin",
        headers: {
          "Content-Type": "application/json",
          "X-XSRF-TOKEN": token,
        },
        body: JSON.stringify(operatorFarmer),
      });
      if (!response.ok) {
        let message = `Farmer account request failed (${response.status}).`;
        try {
          const payload = (await response.json()) as { detail?: string; message?: string };
          message = payload.detail ?? payload.message ?? message;
        } catch {
          // Keep the status-based message if the response is not JSON.
        }
        throw new Error(message);
      }
      setTemporaryPassword((await response.json()) as ProvisionedFarmer);
      setOperatorFarmer({ fullName: "", fanNumber: "", phoneNumber: "" });
    } catch (error) {
      setAuthMessage(error instanceof Error ? error.message : "Unable to create the farmer account.");
    }
  };

  const handleLogout = async () => {
    try {
      const token = await getCsrfToken();
      const response = await fetch("/api/v1/auth/logout", {
        method: "POST",
        credentials: "same-origin",
        headers: { "X-XSRF-TOKEN": token },
      });
      if (!response.ok) {
        throw new Error(`Sign out failed (${response.status}).`);
      }
      setAccount(null);
      setTemporaryPassword(null);
      setAuthMessage("You have signed out.");
    } catch (error) {
      setAuthMessage(error instanceof Error ? error.message : "Unable to sign out.");
    }
  };

  const submitOrder = async () => {
    if (cart.length === 0) {
      return;
    }

    const requestBody: CreateOrderRequest = {
      buyerName: checkoutForm.buyerName.trim(),
      buyerEmail: checkoutForm.buyerEmail.trim(),
      shippingAddress: checkoutForm.shippingAddress.trim(),
      notes: checkoutForm.notes.trim(),
      paymentStatus: "PENDING",
      items: cart.map((item) => ({
        productId: item.productId,
        name: item.name,
        slug: item.slug,
        unit: item.unit,
        price: item.price,
        currency: item.currency,
        quantity: item.quantity,
        sellerName: item.sellerName,
      })),
    };

    try {
      const response = await fetch("/api/v1/orders", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(requestBody),
      });

      if (!response.ok) {
        throw new Error(`Order request failed: ${response.status}`);
      }

      const payload = (await response.json()) as {
        id?: number;
        status?: string;
        paymentStatus?: string;
        totalAmount?: number;
        buyerName?: string;
        itemCount?: number;
      };

      const recentOrder: RecentOrder = {
        id: payload.id ?? Date.now(),
        buyerName: payload.buyerName ?? checkoutForm.buyerName.trim(),
        status: payload.status ?? "PENDING",
        paymentStatus: payload.paymentStatus ?? "PENDING",
        totalAmount: payload.totalAmount ?? cartSubtotal,
        createdAt: new Date().toISOString(),
        itemCount: payload.itemCount ?? cart.reduce((count, item) => count + item.quantity, 0),
      };

      setRecentOrders((currentOrders) => [recentOrder, ...currentOrders].slice(0, 5));
      await fetchRecentOrders();
      setOrderConfirmation(
        payload.id
          ? `Order #${payload.id} placed successfully for ${recentOrder.buyerName}.`
          : "Order placed successfully.",
      );
      setCart([]);
      setCheckoutVisible(false);
      setCheckoutForm({ buyerName: "", buyerEmail: "", shippingAddress: "", notes: "" });
    } catch {
      setOrderConfirmation("We couldn't place the order. Please review your basket and try again.");
    }
  };

  const fetchSellerDetail = async (seller: Seller) => {
    try {
      const response = await fetch(
        `/api/v1/sellers/${encodeURIComponent(seller.slug)}`,
      );
      if (!response.ok) {
        throw new Error(`Seller request failed: ${response.status}`);
      }
      const detail = (await response.json()) as Partial<SellerDetail>;
      const nextSeller = {
        ...seller,
        ...detail,
        productCount: detail.productCount ?? seller.productCount ?? 0,
        products: detail.products ?? [],
      };
      setSelectedSeller(nextSeller);
      setSellerProfileDraft({
        name: nextSeller.name,
        location: nextSeller.location,
        description: nextSeller.description ?? "",
        rating: nextSeller.rating ?? 4.8,
      });
      await fetchSellerInventory(seller.slug);
    } catch {
      const fallbackSeller = {
        ...seller,
        productCount: seller.productCount ?? 0,
        products: [],
      };
      setSelectedSeller(fallbackSeller);
      setSellerInventory([]);
    }
  };

  const fetchSellerInventory = async (sellerSlug: string) => {
    try {
      const response = await fetch(
        `/api/v1/sellers/${encodeURIComponent(sellerSlug)}/inventory`,
      );
      if (!response.ok) {
        throw new Error(`Seller inventory request failed: ${response.status}`);
      }
      const payload = (await response.json()) as unknown;
      setSellerInventory(Array.isArray(payload) ? (payload as SellerInventoryItem[]) : []);
    } catch {
      setSellerInventory([]);
    }
  };

  const restockInventory = async (productSlug: string, delta: number) => {
    if (!selectedSeller) {
      return;
    }

    try {
      const response = await fetch(
        `/api/v1/sellers/${encodeURIComponent(selectedSeller.slug)}/inventory/${encodeURIComponent(productSlug)}`,
        {
          method: "PATCH",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify({ delta }),
        },
      );
      if (!response.ok) {
        throw new Error(`Inventory update failed: ${response.status}`);
      }
      const updatedItem = (await response.json()) as SellerInventoryItem;
      setSellerInventory((currentInventory) =>
        currentInventory.map((item) =>
          item.slug === updatedItem.slug
            ? {
                ...item,
                ...updatedItem,
                lowStock: updatedItem.stockQuantity <= 15,
                status: updatedItem.status ?? (updatedItem.stockQuantity <= 0 ? "OUT_OF_STOCK" : updatedItem.stockQuantity <= 15 ? "LOW_STOCK" : "AVAILABLE"),
              }
            : item,
        ),
      );
      setInventoryEdits((currentEdits) => ({
        ...currentEdits,
        [productSlug]: updatedItem.stockQuantity,
      }));
    } catch {
      // Ignore inventory update errors in the browser prototype.
    }
  };

  const updateSellerProfile = async () => {
    if (!selectedSeller) {
      return;
    }

    try {
      const response = await fetch(
        `/api/v1/sellers/${encodeURIComponent(selectedSeller.slug)}/profile`,
        {
          method: "PATCH",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify({
            name: sellerProfileDraft.name.trim(),
            location: sellerProfileDraft.location.trim(),
            description: sellerProfileDraft.description.trim(),
            rating: Number(sellerProfileDraft.rating),
          }),
        },
      );
      if (!response.ok) {
        throw new Error(`Grower profile update failed: ${response.status}`);
      }
      const updatedSeller = (await response.json()) as Seller;
      setSelectedSeller((currentSeller) =>
        currentSeller
          ? {
              ...currentSeller,
              ...updatedSeller,
            }
          : currentSeller,
      );
      setSellers((currentSellers) =>
        currentSellers.map((seller) =>
          seller.slug === updatedSeller.slug ? { ...seller, ...updatedSeller } : seller,
        ),
      );
    } catch {
      // Ignore seller profile update errors in the browser prototype.
    }
  };

  const handleSellerProductSelect = async (product: SellerProduct) => {
    const fallbackProduct: Product = {
      id: product.id,
      name: product.name,
      slug: product.slug,
      description: "Fresh produce from a trusted local seller.",
      origin: selectedSeller?.location ?? "Ethiopia",
      unit: product.unit,
      price: product.price,
      currency: product.currency,
      stockQuantity: 0,
      categoryName: product.categoryName ?? "Marketplace",
      sellerName: selectedSeller?.name,
      sellerLocation: selectedSeller?.location,
    };

    await fetchProductDetail(fallbackProduct);
  };

  return (
    <div className="min-h-screen bg-cream text-slate-900">
      <HomePage products={catalog.products}>
        <section className="bg-white pt-16 lg:pt-20">
          <div className="mx-auto max-w-7xl px-6 lg:px-10">
            <p className="text-sm font-bold uppercase tracking-[0.18em] text-brand-orange">
              The live platform
            </p>
            <h2 className="mt-4 max-w-3xl text-3xl font-semibold tracking-tight text-brand-deep sm:text-4xl">
              See the marketplace in action.
            </h2>
            <p className="mt-4 max-w-2xl leading-7 text-slate-600">
              Create an account, browse the live catalog, place an order, and follow each
              delivery from farm to destination.
            </p>
          </div>
        </section>

        <section id="auth" className="mx-auto max-w-7xl px-6 py-16 lg:px-10 lg:py-20">
          <div className="grid gap-6 lg:grid-cols-[0.9fr_1.1fr]">
            <div className="rounded-[2rem] bg-forest p-8 text-white shadow-xl shadow-forest/20 lg:p-10">
              <p className="text-sm font-bold uppercase tracking-[0.18em] text-emerald-100">
                Access the platform
              </p>
              <h2 className="mt-4 text-3xl font-semibold tracking-tight sm:text-4xl">
                Sign in or create your account.
              </h2>
              <p className="mt-4 max-w-md text-base leading-7 text-emerald-50/90">
                Farmers can create an account with their FAN number and phone. Operators can also provision accounts for farmers who need assistance.
              </p>
              <div className="mt-8 space-y-3 text-sm text-emerald-50/90">
                <div className="flex items-center gap-3">
                  <span aria-hidden="true" className="grid h-7 w-7 place-items-center rounded-full bg-white/15 text-base">
                    ✓
                  </span>
                  Secure account access for local agricultural communities
                </div>
                <div className="flex items-center gap-3">
                  <span aria-hidden="true" className="grid h-7 w-7 place-items-center rounded-full bg-white/15 text-base">
                    ✓
                  </span>
                  Self-registration or operator-assisted onboarding
                </div>
                <div className="flex items-center gap-3">
                  <span aria-hidden="true" className="grid h-7 w-7 place-items-center rounded-full bg-white/15 text-base">
                    ✓
                  </span>
                  Secure sign-in with a required first-use password change
                </div>
              </div>
            </div>

            <div className="rounded-[2rem] border border-slate-200 bg-white p-6 shadow-xl shadow-slate-200/60 lg:p-8">
              {account ? (
                <div>
                  <div className="flex flex-wrap items-start justify-between gap-4">
                    <div>
                      <p className="text-sm font-bold uppercase tracking-[0.18em] text-leaf">
                        {account.role === "OPERATOR" ? "Operator account" : "Farmer account"}
                      </p>
                      <h3 className="mt-2 text-2xl font-semibold text-forest">
                        Welcome, {account.displayName}
                      </h3>
                      <p className="mt-1 text-sm text-slate-500">
                        {account.phoneNumber ?? account.loginId}
                      </p>
                    </div>
                    <button
                      type="button"
                      onClick={handleLogout}
                      className="rounded-full border border-slate-200 px-4 py-2 text-sm font-semibold text-slate-600 hover:border-forest hover:text-forest"
                    >
                      Sign out
                    </button>
                  </div>

                  {account.mustChangePassword ? (
                    <form onSubmit={handlePasswordChange} className="mt-7 space-y-5">
                      <div className="rounded-xl border border-amber-200 bg-amber-50 p-4 text-sm leading-6 text-amber-900">
                        Change the temporary password issued by the operator before using your account.
                      </div>
                      <div>
                        <label htmlFor="new-password" className="mb-2 block text-sm font-medium text-slate-700">
                          New password
                        </label>
                        <input
                          id="new-password"
                          type="password"
                          minLength={10}
                          maxLength={72}
                          required
                          autoComplete="new-password"
                          value={passwordDraft}
                          onChange={(event) => setPasswordDraft(event.target.value)}
                          className="w-full rounded-xl border border-slate-200 bg-slate-50 px-4 py-3 text-sm outline-none transition focus:border-forest focus:bg-white"
                        />
                        <p className="mt-2 text-xs text-slate-500">Use 10 to 72 characters.</p>
                      </div>
                      <button
                        type="submit"
                        className="w-full rounded-xl bg-forest px-4 py-3 text-sm font-semibold text-white transition hover:bg-leaf"
                      >
                        Change password and continue
                      </button>
                    </form>
                  ) : account.role === "OPERATOR" ? (
                    <div className="mt-7">
                      <h4 className="text-lg font-semibold text-forest">Create a farmer account</h4>
                      <p className="mt-2 text-sm leading-6 text-slate-600">
                        The temporary password is shown once. Call or text it to the farmer; they will be required to change it at first sign-in.
                      </p>
                      <form onSubmit={handleOperatorProvision} className="mt-5 space-y-4">
                        <div>
                          <label htmlFor="operator-farmer-name" className="mb-2 block text-sm font-medium text-slate-700">
                            Farmer full name
                          </label>
                          <input
                            id="operator-farmer-name"
                            required
                            maxLength={120}
                            value={operatorFarmer.fullName}
                            onChange={(event) =>
                              setOperatorFarmer((current) => ({ ...current, fullName: event.target.value }))
                            }
                            className="w-full rounded-xl border border-slate-200 bg-slate-50 px-4 py-3 text-sm outline-none transition focus:border-forest focus:bg-white"
                          />
                        </div>
                        <div>
                          <label htmlFor="operator-farmer-fan" className="mb-2 block text-sm font-medium text-slate-700">
                            National ID / FAN number
                          </label>
                          <input
                            id="operator-farmer-fan"
                            required
                            maxLength={80}
                            value={operatorFarmer.fanNumber}
                            onChange={(event) =>
                              setOperatorFarmer((current) => ({ ...current, fanNumber: event.target.value }))
                            }
                            className="w-full rounded-xl border border-slate-200 bg-slate-50 px-4 py-3 text-sm outline-none transition focus:border-forest focus:bg-white"
                          />
                        </div>
                        <div>
                          <label htmlFor="operator-farmer-phone" className="mb-2 block text-sm font-medium text-slate-700">
                            Farmer phone number
                          </label>
                          <input
                            id="operator-farmer-phone"
                            type="tel"
                            required
                            autoComplete="tel"
                            placeholder="+251 9xx xxx xxx"
                            value={operatorFarmer.phoneNumber}
                            onChange={(event) =>
                              setOperatorFarmer((current) => ({ ...current, phoneNumber: event.target.value }))
                            }
                            className="w-full rounded-xl border border-slate-200 bg-slate-50 px-4 py-3 text-sm outline-none transition focus:border-forest focus:bg-white"
                          />
                        </div>
                        <button
                          type="submit"
                          className="w-full rounded-xl bg-forest px-4 py-3 text-sm font-semibold text-white transition hover:bg-leaf"
                        >
                          Create farmer account
                        </button>
                      </form>
                      {temporaryPassword ? (
                        <div className="mt-5 rounded-xl border border-emerald-200 bg-emerald-50 p-4 text-sm text-emerald-950">
                          <p className="font-semibold">Account created for {temporaryPassword.fullName}</p>
                          <p className="mt-1">Phone: {temporaryPassword.phoneNumber}</p>
                          <p className="mt-3">One-time temporary password:</p>
                          <code className="mt-1 block select-all break-all rounded-lg bg-white px-3 py-2 font-mono text-base">
                            {temporaryPassword.temporaryPassword}
                          </code>
                          <p className="mt-3 text-xs leading-5">
                            Share this privately by phone or text. Do not leave it visible or share it with anyone else.
                          </p>
                        </div>
                      ) : null}
                    </div>
                  ) : (
                    <p className="mt-7 rounded-xl bg-emerald-50 p-4 text-sm leading-6 text-emerald-900">
                      You are signed in. Your farmer account is ready.
                    </p>
                  )}
                </div>
              ) : (
                <>
                  <div className="mb-6 flex rounded-full bg-slate-100 p-1">
                    <button
                      type="button"
                      onClick={() => {
                        setAuthMode("login");
                        setAuthMessage(null);
                      }}
                      className={`flex-1 rounded-full px-4 py-2 text-sm font-semibold transition ${
                        authMode === "login" ? "bg-white text-forest shadow-sm" : "text-slate-500"
                      }`}
                    >
                      Login
                    </button>
                    <button
                      type="button"
                      onClick={() => {
                        setAuthMode("register");
                        setAuthMessage(null);
                      }}
                      className={`flex-1 rounded-full px-4 py-2 text-sm font-semibold transition ${
                        authMode === "register" ? "bg-white text-forest shadow-sm" : "text-slate-500"
                      }`}
                    >
                      Farmer registration
                    </button>
                  </div>

                  {authMode === "login" ? (
                    <form onSubmit={handleLoginSubmit} className="space-y-5">
                      <div>
                        <label htmlFor="login-id" className="mb-2 block text-sm font-medium text-slate-700">
                          Phone number or operator username
                        </label>
                        <input
                          id="login-id"
                          type="text"
                          required
                          autoComplete="username"
                          value={loginForm.loginId}
                          onChange={(event) =>
                            setLoginForm((current) => ({ ...current, loginId: event.target.value }))
                          }
                          placeholder="+251 9xx xxx xxx"
                          className="w-full rounded-xl border border-slate-200 bg-slate-50 px-4 py-3 text-sm outline-none transition focus:border-forest focus:bg-white"
                        />
                      </div>
                      <div>
                        <label htmlFor="login-password" className="mb-2 block text-sm font-medium text-slate-700">
                          Password
                        </label>
                        <input
                          id="login-password"
                          type="password"
                          required
                          autoComplete="current-password"
                          value={loginForm.password}
                          onChange={(event) =>
                            setLoginForm((current) => ({ ...current, password: event.target.value }))
                          }
                          placeholder="Enter your password"
                          className="w-full rounded-xl border border-slate-200 bg-slate-50 px-4 py-3 text-sm outline-none transition focus:border-forest focus:bg-white"
                        />
                      </div>
                      <button
                        type="submit"
                        className="w-full rounded-xl bg-forest px-4 py-3 text-sm font-semibold text-white transition hover:bg-leaf"
                      >
                        Login to dashboard
                      </button>
                    </form>
                  ) : (
                    <form onSubmit={handleRegisterSubmit} className="space-y-5">
                      <div>
                        <label htmlFor="register-name" className="mb-2 block text-sm font-medium text-slate-700">
                          Farmer full name
                        </label>
                        <input
                          id="register-name"
                          type="text"
                          required
                          maxLength={120}
                          autoComplete="name"
                          value={registerForm.fullName}
                          onChange={(event) =>
                            setRegisterForm((current) => ({ ...current, fullName: event.target.value }))
                          }
                          placeholder="Abebe Bekele"
                          className="w-full rounded-xl border border-slate-200 bg-slate-50 px-4 py-3 text-sm outline-none transition focus:border-forest focus:bg-white"
                        />
                      </div>
                      <div>
                        <label htmlFor="register-fan" className="mb-2 block text-sm font-medium text-slate-700">
                          National ID / FAN number
                        </label>
                        <input
                          id="register-fan"
                          type="text"
                          required
                          maxLength={80}
                          value={registerForm.fanNumber}
                          onChange={(event) =>
                            setRegisterForm((current) => ({ ...current, fanNumber: event.target.value }))
                          }
                          placeholder="Enter your FAN or national ID"
                          className="w-full rounded-xl border border-slate-200 bg-slate-50 px-4 py-3 text-sm outline-none transition focus:border-forest focus:bg-white"
                        />
                      </div>
                      <div>
                        <label htmlFor="register-phone" className="mb-2 block text-sm font-medium text-slate-700">
                          Phone number
                        </label>
                        <input
                          id="register-phone"
                          type="tel"
                          required
                          autoComplete="tel"
                          value={registerForm.phoneNumber}
                          onChange={(event) =>
                            setRegisterForm((current) => ({ ...current, phoneNumber: event.target.value }))
                          }
                          placeholder="+251 9xx xxx xxx"
                          className="w-full rounded-xl border border-slate-200 bg-slate-50 px-4 py-3 text-sm outline-none transition focus:border-forest focus:bg-white"
                        />
                      </div>
                      <div>
                        <label htmlFor="register-password" className="mb-2 block text-sm font-medium text-slate-700">
                          Password
                        </label>
                        <input
                          id="register-password"
                          type="password"
                          required
                          minLength={10}
                          maxLength={72}
                          autoComplete="new-password"
                          value={registerForm.password}
                          onChange={(event) =>
                            setRegisterForm((current) => ({ ...current, password: event.target.value }))
                          }
                          placeholder="Use 10 to 72 characters"
                          className="w-full rounded-xl border border-slate-200 bg-slate-50 px-4 py-3 text-sm outline-none transition focus:border-forest focus:bg-white"
                        />
                      </div>
                      <button
                        type="submit"
                        className="w-full rounded-xl bg-forest px-4 py-3 text-sm font-semibold text-white transition hover:bg-leaf"
                      >
                        Create farmer account
                      </button>
                    </form>
                  )}
                </>
              )}

              {authMessage ? (
                <p aria-live="polite" className="mt-5 rounded-xl border border-emerald-200 bg-emerald-50 px-4 py-3 text-sm text-emerald-800">
                  {authMessage}
                </p>
              ) : null}
            </div>
          </div>
        </section>

        <section id="marketplace" className="bg-white py-20 lg:py-24">
          <div className="mx-auto max-w-7xl px-6 lg:px-10">
            <div className="flex flex-col gap-6 sm:flex-row sm:items-end sm:justify-between">
              <div className="max-w-2xl">
                <p className="text-sm font-bold uppercase tracking-[0.18em] text-leaf">
                  Marketplace snapshot
                </p>
                <h2 className="mt-4 text-3xl font-semibold tracking-tight text-forest sm:text-4xl">
                  Fresh listings from nearby growers.
                </h2>
              </div>
              <div className="flex flex-wrap items-center gap-3 text-sm text-slate-500">
                <span
                  aria-live="polite"
                  className="inline-flex items-center gap-2 rounded-full border border-slate-200 bg-white px-3 py-1.5 text-xs font-semibold text-slate-600"
                >
                  <span
                    aria-hidden="true"
                    className={`h-2 w-2 rounded-full ${
                      apiStatus === "online"
                        ? "bg-emerald-500"
                        : apiStatus === "offline"
                          ? "bg-amber-500"
                          : "animate-pulse bg-slate-300"
                    }`}
                  />
                  {statusLabel}
                </span>
                <span>
                  {catalog.categories.length > 0
                    ? `${catalog.products.length} product${catalog.products.length === 1 ? "" : "s"} displayed`
                    : "Loading live catalog"}
                </span>
              </div>
            </div>

            <div className="mt-8 flex flex-col gap-4 lg:flex-row lg:items-center lg:justify-between">
              <div className="flex flex-wrap gap-3">
                {categoryOptions.map((category) => (
                  <button
                    key={category.slug}
                    type="button"
                    onClick={() => setSelectedCategory(category.slug)}
                    className={`rounded-full border px-4 py-2 text-sm font-medium transition ${
                      selectedCategory === category.slug
                        ? "border-forest bg-forest text-white"
                        : "border-slate-200 bg-white text-slate-600 hover:border-leaf hover:text-forest"
                    }`}
                  >
                    {category.name}
                  </button>
                ))}
              </div>

              <div className="flex flex-col gap-3 sm:flex-row sm:items-center">
                <label className="sr-only" htmlFor="catalog-search">
                  Search products
                </label>
                <input
                  id="catalog-search"
                  value={searchTerm}
                  onChange={(event) => setSearchTerm(event.target.value)}
                  placeholder="Search products"
                  className="rounded-full border border-slate-200 bg-white px-4 py-2 text-sm text-slate-700 outline-none transition focus:border-forest"
                />
                <label className="sr-only" htmlFor="catalog-sort">
                  Sort products
                </label>
                <select
                  id="catalog-sort"
                  value={sortOption}
                  onChange={(event) => setSortOption(event.target.value)}
                  className="rounded-full border border-slate-200 bg-white px-4 py-2 text-sm text-slate-700 outline-none transition focus:border-forest"
                >
                  <option value="name_asc">Sort: A–Z</option>
                  <option value="price_asc">Price: low to high</option>
                  <option value="price_desc">Price: high to low</option>
                </select>
              </div>
            </div>

            <div className="mt-12 grid gap-6 md:grid-cols-3">
              {featuredProducts.length > 0 ? (
                featuredProducts.map((product) => (
                  <article
                    key={product.id}
                    className="rounded-3xl border border-slate-200 bg-cream p-6 shadow-sm"
                  >
                    <div className="mb-4 flex items-center justify-between gap-3">
                      <span className="rounded-full bg-white px-3 py-1 text-xs font-semibold uppercase tracking-[0.14em] text-leaf">
                        {product.categoryName}
                      </span>
                      <span className="text-xs font-medium text-slate-500">
                        {product.stockQuantity} in stock
                      </span>
                    </div>
                    <h3 className="text-2xl font-semibold text-forest">
                      {product.name}
                    </h3>
                    <p className="mt-3 text-sm leading-6 text-slate-600">
                      {product.description || "Fresh produce sourced from trusted local growers."}
                    </p>
                    <div className="mt-5 flex items-center justify-between text-sm text-slate-500">
                      <span>{product.origin}</span>
                      <span>{product.unit}</span>
                    </div>
                    <div className="mt-4 text-sm text-slate-500">
                      {product.sellerName ? `${product.sellerName} • ${product.sellerLocation ?? "Local supplier"}` : "Trusted local supplier"}
                    </div>
                    <div className="mt-6 flex items-end justify-between gap-3 border-t border-slate-200 pt-4">
                      <div>
                        <p className="text-2xl font-bold text-forest">
                          {product.currency} {product.price.toLocaleString("en-US")}
                        </p>
                        <p className="text-xs uppercase tracking-[0.16em] text-slate-500">
                          per {product.unit}
                        </p>
                      </div>
                      <div className="flex items-center gap-2">
                        <button
                          type="button"
                          onClick={() => addToCart(product)}
                          className="rounded-full bg-white px-3 py-1.5 text-xs font-semibold text-forest ring-1 ring-slate-200 transition hover:bg-emerald-50"
                        >
                          Add to cart
                        </button>
                        <button
                          type="button"
                          onClick={() => fetchProductDetail(product)}
                          className="rounded-full bg-forest px-3 py-1.5 text-xs font-semibold text-white transition hover:bg-leaf"
                        >
                          View details
                        </button>
                      </div>
                    </div>
                  </article>
                ))
              ) : (
                <div className="rounded-3xl border border-dashed border-slate-300 bg-white p-8 text-slate-500 md:col-span-3">
                  Loading product listings from the marketplace API.
                </div>
              )}
            </div>

            {selectedProduct && (
              <div className="mt-12 rounded-[2rem] border border-slate-200 bg-cream p-6 shadow-sm lg:p-8">
                <div className="grid gap-8 lg:grid-cols-[1.25fr_0.75fr]">
                  <div>
                    <p className="text-sm font-bold uppercase tracking-[0.18em] text-leaf">
                      Product detail
                    </p>
                    <h3 className="mt-3 text-3xl font-semibold text-forest">
                      {selectedProduct.name}
                    </h3>
                    <p className="mt-4 max-w-2xl text-base leading-7 text-slate-600">
                      {selectedProduct.description ||
                        "Fresh produce sourced from nearby growers with quality and traceability in mind."}
                    </p>
                    <div className="mt-5 flex flex-wrap gap-3 text-sm text-slate-600">
                      <span className="rounded-full bg-white px-3 py-1.5">
                        Origin: {selectedProduct.origin}
                      </span>
                      <span className="rounded-full bg-white px-3 py-1.5">
                        Category: {selectedProduct.categoryName}
                      </span>
                      <span className="rounded-full bg-white px-3 py-1.5">
                        Unit: {selectedProduct.unit}
                      </span>
                    </div>
                    <div className="mt-5 rounded-2xl border border-slate-200 bg-white p-4">
                      <p className="text-sm font-bold uppercase tracking-[0.18em] text-leaf">
                        Supplier
                      </p>
                      <div className="mt-3 flex items-center justify-between gap-3">
                        <div>
                          <p className="font-semibold text-forest">{selectedProduct.sellerName ?? "Local supplier"}</p>
                          <p className="text-sm text-slate-500">{selectedProduct.sellerLocation ?? "Regional producer"}</p>
                        </div>
                        <span className="rounded-full bg-emerald-50 px-2.5 py-1 text-sm font-semibold text-emerald-700">
                          {selectedProduct.sellerRating != null ? `★ ${selectedProduct.sellerRating.toFixed(1)}` : "★ 4.8"}
                        </span>
                      </div>
                    </div>
                  </div>
                  <div className="rounded-3xl bg-white p-5">
                    <p className="text-sm font-medium uppercase tracking-[0.16em] text-slate-500">
                      Market price
                    </p>
                    <p className="mt-3 text-3xl font-bold text-forest">
                      {selectedProduct.currency} {selectedProduct.price.toLocaleString("en-US")}
                    </p>
                    <p className="mt-1 text-sm text-slate-500">
                      per {selectedProduct.unit}
                    </p>
                    <div className="mt-5 space-y-3 text-sm text-slate-600">
                      <div className="flex items-center justify-between border-b border-slate-200 pb-2">
                        <span>Availability</span>
                        <span className="font-semibold text-forest">
                          {selectedProduct.stockQuantity} units
                        </span>
                      </div>
                      <div className="flex items-center justify-between border-b border-slate-200 pb-2">
                        <span>Status</span>
                        <span className="font-semibold text-forest">
                          {selectedProduct.status ?? (selectedProduct.stockQuantity > 0 ? "AVAILABLE" : "OUT_OF_STOCK")}
                        </span>
                      </div>
                    </div>
                    <button
                      type="button"
                      onClick={() => addToCart(selectedProduct)}
                      className="mt-6 w-full rounded-full bg-forest px-4 py-3 text-sm font-semibold text-white transition hover:bg-leaf"
                    >
                      Add to cart
                    </button>
                  </div>
                </div>
              </div>
            )}

            <div className="mt-12 rounded-[2rem] border border-slate-200 bg-[#f9faf5] p-6 shadow-sm lg:p-8">
              <div className="flex flex-col gap-4 sm:flex-row sm:items-end sm:justify-between">
                <div>
                  <p className="text-sm font-bold uppercase tracking-[0.18em] text-leaf">
                    Cart summary
                  </p>
                  <h3 className="mt-2 text-2xl font-semibold text-forest">
                    {cart.length === 0 ? "Your basket is empty" : `Cart (${cart.reduce((total, item) => total + item.quantity, 0)} items)`}
                  </h3>
                </div>
                <div className="text-2xl font-bold text-forest">
                  Cart total: {cartSubtotal.toLocaleString("en-US", { maximumFractionDigits: 2 })} ETB
                </div>
              </div>

              {cart.length === 0 ? (
                <p className="mt-4 text-sm text-slate-500">
                  Add produce to your cart to start a buyer order.
                </p>
              ) : (
                <div className="mt-6 space-y-4">
                  {cart.map((item) => (
                    <div
                      key={item.productId}
                      className="flex flex-col gap-3 rounded-2xl border border-slate-200 bg-white p-4 sm:flex-row sm:items-center sm:justify-between"
                    >
                      <div>
                        <p className="font-semibold text-forest">{item.name}</p>
                        <p className="text-sm text-slate-500">
                          {item.quantity} × {item.unit} · {item.sellerName ?? "Local supplier"}
                        </p>
                      </div>

                      <div className="flex items-center gap-3">
                        <div className="flex items-center gap-2 rounded-full border border-slate-200 bg-slate-50 px-2 py-1">
                          <button
                            type="button"
                            onClick={() => changeCartQuantity(item.productId, -1)}
                            className="h-7 w-7 rounded-full bg-white text-base font-semibold text-slate-700"
                            aria-label={`Decrease ${item.name}`}
                          >
                            −
                          </button>
                          <span className="min-w-8 text-center text-sm font-semibold text-slate-700">
                            {item.quantity}
                          </span>
                          <button
                            type="button"
                            onClick={() => changeCartQuantity(item.productId, 1)}
                            className="h-7 w-7 rounded-full bg-white text-base font-semibold text-slate-700"
                            aria-label={`Increase ${item.name}`}
                          >
                            +
                          </button>
                        </div>
                        <span className="min-w-20 text-right font-semibold text-forest">
                          {item.currency} {(item.price * item.quantity).toLocaleString("en-US")}
                        </span>
                      </div>
                    </div>
                  ))}

                  <div className="flex flex-col gap-3 pt-2 sm:flex-row sm:items-center sm:justify-between">
                    <div className="text-sm text-slate-500">
                      Ready for checkout and logistics coordination.
                    </div>
                    <button
                      type="button"
                      onClick={() => setCheckoutVisible((current) => !current)}
                      className="rounded-full bg-forest px-5 py-3 text-sm font-semibold text-white transition hover:bg-leaf"
                    >
                      {checkoutVisible ? "Hide checkout" : "Proceed to checkout"}
                    </button>
                  </div>

                  {checkoutVisible && (
                    <div className="mt-6 rounded-2xl border border-slate-200 bg-[#f3f7f1] p-5">
                      <p className="text-sm font-bold uppercase tracking-[0.16em] text-leaf">
                        Buyer details
                      </p>

                      <div className="mt-4 grid gap-4 md:grid-cols-2">
                        <label className="text-sm text-slate-600">
                          <span className="mb-1 block font-medium">Full name</span>
                          <input
                            value={checkoutForm.buyerName}
                            onChange={(event) =>
                              setCheckoutForm((current) => ({
                                ...current,
                                buyerName: event.target.value,
                              }))
                            }
                            className="w-full rounded-xl border border-slate-200 bg-white px-3 py-2.5 outline-none transition focus:border-forest"
                            placeholder="Your full name"
                          />
                        </label>

                        <label className="text-sm text-slate-600">
                          <span className="mb-1 block font-medium">Email</span>
                          <input
                            value={checkoutForm.buyerEmail}
                            onChange={(event) =>
                              setCheckoutForm((current) => ({
                                ...current,
                                buyerEmail: event.target.value,
                              }))
                            }
                            className="w-full rounded-xl border border-slate-200 bg-white px-3 py-2.5 outline-none transition focus:border-forest"
                            placeholder="you@example.com"
                          />
                        </label>
                      </div>

                      <label className="mt-4 block text-sm text-slate-600">
                        <span className="mb-1 block font-medium">Delivery address</span>
                        <textarea
                          value={checkoutForm.shippingAddress}
                          onChange={(event) =>
                            setCheckoutForm((current) => ({
                              ...current,
                              shippingAddress: event.target.value,
                            }))
                          }
                          rows={3}
                          className="w-full rounded-xl border border-slate-200 bg-white px-3 py-2.5 outline-none transition focus:border-forest"
                          placeholder="House, road, city, region"
                        />
                      </label>

                      <label className="mt-4 block text-sm text-slate-600">
                        <span className="mb-1 block font-medium">Order notes</span>
                        <textarea
                          value={checkoutForm.notes}
                          onChange={(event) =>
                            setCheckoutForm((current) => ({
                              ...current,
                              notes: event.target.value,
                            }))
                          }
                          rows={2}
                          className="w-full rounded-xl border border-slate-200 bg-white px-3 py-2.5 outline-none transition focus:border-forest"
                          placeholder="Packing notes or preferred delivery windows"
                        />
                      </label>

                      <div className="mt-5 flex flex-col gap-3 sm:flex-row sm:items-center sm:justify-between">
                        <div className="text-sm text-slate-500">
                          Total due: <span className="font-semibold text-forest">{cartSubtotal.toLocaleString("en-US", { maximumFractionDigits: 2 })} ETB</span>
                        </div>
                        <button
                          type="button"
                          onClick={submitOrder}
                          className="rounded-full bg-forest px-5 py-3 text-sm font-semibold text-white transition hover:bg-leaf"
                        >
                          Place order
                        </button>
                      </div>

                      {orderConfirmation && (
                        <p className="mt-4 rounded-xl border border-emerald-200 bg-emerald-50 px-3 py-2 text-sm text-emerald-700">
                          {orderConfirmation}
                        </p>
                      )}
                    </div>
                  )}
                </div>
              )}
            </div>
          </div>
        </section>

        {(recentOrders.length > 0 || shipments.length > 0) && (
          <section className="bg-[#edf4ee] py-20 lg:py-24">
            <div className="mx-auto max-w-7xl px-6 lg:px-10">
              <div className="max-w-2xl">
                <p className="text-sm font-bold uppercase tracking-[0.18em] text-leaf">
                  Marketplace operations
                </p>
                <h2 className="mt-4 text-3xl font-semibold tracking-tight text-forest sm:text-4xl">
                  Buyer activity and delivery status.
                </h2>
              </div>

              {recentOrders.length > 0 && (
                <div className="mt-8">
                  <h3 className="mb-4 text-xl font-semibold text-forest">Recent orders</h3>
                  <div className="grid gap-4 md:grid-cols-2 xl:grid-cols-3">
                    {recentOrders.map((order) => (
                      <article
                        key={order.id}
                        className="rounded-3xl border border-slate-200 bg-white p-5 shadow-sm"
                      >
                        <div className="flex items-start justify-between gap-3">
                          <div>
                            <p className="text-sm font-medium uppercase tracking-[0.16em] text-leaf">
                              Order #{order.id}
                            </p>
                            <h4 className="mt-2 text-xl font-semibold text-forest">
                              {order.buyerName}
                            </h4>
                          </div>
                          <span className="rounded-full bg-emerald-50 px-2.5 py-1 text-xs font-semibold text-emerald-700">
                            {order.status}
                          </span>
                        </div>
                        <div className="mt-4 space-y-2 text-sm text-slate-600">
                          <p>{order.itemCount} item{order.itemCount === 1 ? "" : "s"}</p>
                          <p>Payment: {order.paymentStatus ?? "PENDING"}</p>
                          <p>{new Date(order.createdAt).toLocaleDateString("en-ET", { dateStyle: "medium" })}</p>
                        </div>
                        <p className="mt-4 text-2xl font-bold text-forest">
                          {order.totalAmount.toLocaleString("en-US", { maximumFractionDigits: 2 })} ETB
                        </p>
                      </article>
                    ))}
                  </div>
                </div>
              )}

              {shipments.length > 0 && (
                <div className="mt-10">
                  <h3 className="mb-4 text-xl font-semibold text-forest">Delivery status</h3>
                  <div className="grid gap-4 md:grid-cols-2 xl:grid-cols-3">
                    {shipments.map((shipment) => (
                      <article
                        key={shipment.id}
                        className="rounded-3xl border border-slate-200 bg-white p-5 shadow-sm"
                      >
                        <div className="flex items-start justify-between gap-3">
                          <div>
                            <p className="text-sm font-medium uppercase tracking-[0.16em] text-leaf">
                              Shipment #{shipment.id}
                            </p>
                            <h4 className="mt-2 text-xl font-semibold text-forest">
                              {shipment.carrier}
                            </h4>
                          </div>
                          <span className="rounded-full bg-amber-50 px-2.5 py-1 text-xs font-semibold text-amber-700">
                            {shipment.status}
                          </span>
                        </div>
                        <div className="mt-4 space-y-2 text-sm text-slate-600">
                          <p>Tracking: {shipment.trackingCode}</p>
                          <p>{shipment.origin} → {shipment.destination}</p>
                          <p>ETA: {shipment.eta}</p>
                        </div>
                        {shipment.notes && (
                          <p className="mt-4 text-sm leading-6 text-slate-600">
                            {shipment.notes}
                          </p>
                        )}
                      </article>
                    ))}
                  </div>
                </div>
              )}
            </div>
          </section>
        )}

        {(catalog.products.length > 0 || recentOrders.length > 0 || shipments.length > 0) && (
          <section className="bg-white py-20 lg:py-24">
            <div className="mx-auto max-w-7xl px-6 lg:px-10">
              <div className="max-w-2xl">
                <p className="text-sm font-bold uppercase tracking-[0.18em] text-leaf">
                  Grower operations
                </p>
                <h2 className="mt-4 text-3xl font-semibold tracking-tight text-forest sm:text-4xl">
                  Inventory and fulfillment visibility for growers.
                </h2>
              </div>

              <div className="mt-8 grid gap-4 md:grid-cols-2 xl:grid-cols-4">
                <article className="rounded-3xl border border-slate-200 bg-cream p-5 shadow-sm">
                  <p className="text-sm font-medium uppercase tracking-[0.14em] text-slate-500">Listings tracked</p>
                  <p className="mt-4 text-3xl font-bold text-forest">{sellerMetrics.listings}</p>
                </article>
                <article className="rounded-3xl border border-slate-200 bg-cream p-5 shadow-sm">
                  <p className="text-sm font-medium uppercase tracking-[0.14em] text-slate-500">Low-stock alerts</p>
                  <p className="mt-4 text-3xl font-bold text-amber-700">{sellerMetrics.lowStock}</p>
                </article>
                <article className="rounded-3xl border border-slate-200 bg-cream p-5 shadow-sm">
                  <p className="text-sm font-medium uppercase tracking-[0.14em] text-slate-500">Active shipments</p>
                  <p className="mt-4 text-3xl font-bold text-forest">{sellerMetrics.shipments}</p>
                </article>
                <article className="rounded-3xl border border-slate-200 bg-cream p-5 shadow-sm">
                  <p className="text-sm font-medium uppercase tracking-[0.14em] text-slate-500">Revenue view</p>
                  <p className="mt-4 text-3xl font-bold text-forest">
                    {sellerMetrics.revenue.toLocaleString("en-US", { maximumFractionDigits: 0 })} ETB
                  </p>
                </article>
              </div>

              <div className="mt-8 grid gap-6 lg:grid-cols-[1fr_1fr]">
                <div className="rounded-[2rem] border border-slate-200 bg-[#edf4ee] p-6">
                  <h3 className="text-xl font-semibold text-forest">Inventory watchlist</h3>
                  {lowStockProducts.length > 0 ? (
                    <ul className="mt-5 space-y-3 text-sm text-slate-600">
                      {lowStockProducts.map((product) => (
                        <li key={product.id} className="flex items-center justify-between border-b border-slate-200 pb-3 last:border-b-0 last:pb-0">
                          <span>{product.name}</span>
                          <span className="font-semibold text-amber-700">{product.stockQuantity} left</span>
                        </li>
                      ))}
                    </ul>
                  ) : (
                    <p className="mt-5 text-sm text-slate-600">No immediate restock warnings in the current catalog.</p>
                  )}
                </div>

                <div className="rounded-[2rem] border border-slate-200 bg-[#edf4ee] p-6">
                  <h3 className="text-xl font-semibold text-forest">Market pulse</h3>
                  <div className="mt-5 space-y-4 text-sm text-slate-600">
                    <div className="flex items-center justify-between border-b border-slate-200 pb-3">
                      <span>Average listing price</span>
                      <span className="font-semibold text-forest">
                        {sellerMetrics.avgPrice.toLocaleString("en-US", { maximumFractionDigits: 0 })} ETB
                      </span>
                    </div>
                    <div className="flex items-center justify-between border-b border-slate-200 pb-3">
                      <span>Fulfillment queue</span>
                      <span className="font-semibold text-forest">{sellerMetrics.shipments} shipments</span>
                    </div>
                    <div className="flex items-center justify-between">
                      <span>Order momentum</span>
                      <span className="font-semibold text-forest">{recentOrders.length} recent orders</span>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </section>
        )}

        <section id="farmers" className="bg-white py-20 lg:py-24">
          <div className="mx-auto max-w-7xl px-6 lg:px-10">
            <div className="max-w-2xl">
              <p className="text-sm font-bold uppercase tracking-[0.18em] text-leaf">
                Farmer module
              </p>
              <h2 className="mt-4 text-3xl font-semibold tracking-tight text-forest sm:text-4xl">
                Grower profiles powering the marketplace.
              </h2>
            </div>

            <div className="mt-12 grid gap-6 md:grid-cols-3">
              {farmers.length > 0 ? (
                farmers.map((farmer) => (
                  <article
                    key={farmer.id}
                    className="rounded-3xl border border-slate-200 bg-cream p-6 shadow-sm transition hover:-translate-y-0.5 hover:border-leaf hover:shadow-md"
                  >
                    <div className="flex items-start justify-between gap-4">
                      <div>
                        <p className="text-sm font-medium uppercase tracking-[0.16em] text-leaf">
                          Grower
                        </p>
                        <h3 className="mt-2 text-2xl font-semibold text-forest">
                          {farmer.name}
                        </h3>
                      </div>
                      <span className="rounded-full bg-emerald-50 px-2.5 py-1 text-sm font-semibold text-emerald-700">
                        ★ {farmer.rating.toFixed(1)}
                      </span>
                    </div>
                    <p className="mt-4 text-sm text-slate-500">{farmer.region}</p>
                    <p className="mt-4 text-sm leading-6 text-slate-600">
                      {farmer.bio || "A dedicated farmer helping build more resilient regional food systems."}
                    </p>
                    <div className="mt-5 space-y-2 border-t border-slate-200 pt-4 text-sm text-slate-500">
                      <p><span className="font-medium text-slate-700">Focus:</span> {farmer.cropFocus}</p>
                      <p><span className="font-medium text-slate-700">Farm size:</span> {farmer.farmSize}</p>
                      <p><span className="font-medium text-slate-700">Season harvests:</span> {farmer.harvestsThisSeason ?? 0}</p>
                    </div>
                  </article>
                ))
              ) : (
                <div className="rounded-3xl border border-dashed border-slate-300 bg-white p-8 text-slate-500 md:col-span-3">
                  Loading farmer profiles from the network.
                </div>
              )}
            </div>
          </div>
        </section>

        <section id="sellers" className="bg-[#edf4ee] py-20 lg:py-24">
          <div className="mx-auto max-w-7xl px-6 lg:px-10">
            <div className="max-w-2xl">
              <p className="text-sm font-bold uppercase tracking-[0.18em] text-leaf">
                Trusted growers
              </p>
              <h2 className="mt-4 text-3xl font-semibold tracking-tight text-forest sm:text-4xl">
                Trusted networks of growers and buyers.
              </h2>
            </div>

            <div className="mt-12 grid gap-6 md:grid-cols-3">
              {sellers.length > 0 ? (
                sellers.map((seller) => (
                  <article
                    key={seller.id}
                    className="rounded-3xl border border-slate-200 bg-white p-6 shadow-sm transition hover:-translate-y-0.5 hover:border-leaf hover:shadow-md"
                  >
                    <div className="flex items-start justify-between gap-4">
                      <div>
                        <p className="text-sm font-medium uppercase tracking-[0.16em] text-leaf">
                          Supplier
                        </p>
                        <h3 className="mt-2 text-2xl font-semibold text-forest">
                          {seller.name}
                        </h3>
                      </div>
                      <span className="rounded-full bg-emerald-50 px-2.5 py-1 text-sm font-semibold text-emerald-700">
                        ★ {seller.rating.toFixed(1)}
                      </span>
                    </div>
                    <p className="mt-4 text-sm text-slate-500">{seller.location}</p>
                    <p className="mt-4 text-sm leading-6 text-slate-600">
                      {seller.description || "A dependable regional supplier building trusted trade relationships."}
                    </p>
                    <div className="mt-5 flex items-center justify-between border-t border-slate-200 pt-4 text-sm text-slate-500">
                      <span>{seller.productCount ?? 0} listings</span>
                      <button
                        type="button"
                        onClick={() => fetchSellerDetail(seller)}
                        className="rounded-full bg-forest px-3 py-1.5 font-semibold text-white transition hover:bg-leaf"
                      >
                        View profile
                      </button>
                    </div>
                  </article>
                ))
              ) : (
                <div className="rounded-3xl border border-dashed border-slate-300 bg-white p-8 text-slate-500 md:col-span-3">
                  Loading trusted growers from the marketplace network.
                </div>
              )}
            </div>

            {selectedSeller && (
              <div className="mt-12 rounded-[2rem] border border-slate-200 bg-white p-6 shadow-sm lg:p-8">
                <div className="grid gap-8 lg:grid-cols-[1.15fr_0.85fr]">
                  <div>
                    <p className="text-sm font-bold uppercase tracking-[0.18em] text-leaf">
                      Grower profile
                    </p>
                    <h3 className="mt-3 text-3xl font-semibold text-forest">
                      {selectedSeller.name}
                    </h3>
                    <p className="mt-3 text-base text-slate-500">
                      {selectedSeller.location}
                    </p>
                    <p className="mt-4 max-w-2xl text-base leading-7 text-slate-600">
                      {selectedSeller.description || "A trusted regional partner connecting farmers with reliable buyers and logistics support."}
                    </p>
                    <div className="mt-5 flex flex-wrap gap-3 text-sm text-slate-600">
                      <span className="rounded-full bg-emerald-50 px-3 py-1.5 text-emerald-700">
                        ★ {selectedSeller.rating.toFixed(1)} rating
                      </span>
                      <span className="rounded-full bg-slate-100 px-3 py-1.5">
                        {selectedSeller.productCount} active listings
                      </span>
                    </div>
                  </div>

                  <div className="rounded-3xl bg-cream p-5">
                    <p className="text-sm font-medium uppercase tracking-[0.16em] text-slate-500">
                      Grower focus
                    </p>
                    <div className="mt-4 space-y-3 text-sm text-slate-600">
                      <div className="flex items-center justify-between border-b border-slate-200 pb-2">
                        <span>Location</span>
                        <span className="font-semibold text-forest">{selectedSeller.location}</span>
                      </div>
                      <div className="flex items-center justify-between border-b border-slate-200 pb-2">
                        <span>Market quality</span>
                        <span className="font-semibold text-forest">Consistent</span>
                      </div>
                      <div className="flex items-center justify-between pb-2">
                        <span>Trust score</span>
                        <span className="font-semibold text-forest">{selectedSeller.rating.toFixed(1)}/5.0</span>
                      </div>
                    </div>
                  </div>
                </div>

                <div className="mt-8 rounded-3xl border border-slate-200 bg-cream p-5">
                  <div className="flex flex-col gap-4 md:flex-row md:items-end md:justify-between">
                    <div>
                      <p className="text-sm font-bold uppercase tracking-[0.18em] text-leaf">
                        Grower profile
                      </p>
                      <h4 className="mt-2 text-xl font-semibold text-forest">Update grower details</h4>
                    </div>
                    <button
                      type="button"
                      onClick={updateSellerProfile}
                      className="rounded-full bg-forest px-4 py-2 text-sm font-semibold text-white transition hover:bg-leaf"
                    >
                      Save profile
                    </button>
                  </div>

                  <div className="mt-5 grid gap-4 md:grid-cols-2">
                    <label className="block text-sm text-slate-600">
                      <span className="mb-1 block font-medium">Grower name</span>
                      <input
                        value={sellerProfileDraft.name}
                        onChange={(event) =>
                          setSellerProfileDraft((current) => ({ ...current, name: event.target.value }))
                        }
                        className="w-full rounded-xl border border-slate-200 bg-white px-3 py-2.5 outline-none transition focus:border-forest"
                      />
                    </label>
                    <label className="block text-sm text-slate-600">
                      <span className="mb-1 block font-medium">Location</span>
                      <input
                        value={sellerProfileDraft.location}
                        onChange={(event) =>
                          setSellerProfileDraft((current) => ({ ...current, location: event.target.value }))
                        }
                        className="w-full rounded-xl border border-slate-200 bg-white px-3 py-2.5 outline-none transition focus:border-forest"
                      />
                    </label>
                    <label className="block text-sm text-slate-600 md:col-span-2">
                      <span className="mb-1 block font-medium">Description</span>
                      <textarea
                        value={sellerProfileDraft.description}
                        onChange={(event) =>
                          setSellerProfileDraft((current) => ({ ...current, description: event.target.value }))
                        }
                        rows={3}
                        className="w-full rounded-xl border border-slate-200 bg-white px-3 py-2.5 outline-none transition focus:border-forest"
                      />
                    </label>
                    <label className="block text-sm text-slate-600">
                      <span className="mb-1 block font-medium">Rating</span>
                      <input
                        type="number"
                        min={0}
                        max={5}
                        step={0.1}
                        value={sellerProfileDraft.rating}
                        onChange={(event) =>
                          setSellerProfileDraft((current) => ({
                            ...current,
                            rating: Number(event.target.value || 0),
                          }))
                        }
                        className="w-full rounded-xl border border-slate-200 bg-white px-3 py-2.5 outline-none transition focus:border-forest"
                      />
                    </label>
                  </div>
                </div>

                <div className="mt-8">
                  <p className="text-sm font-bold uppercase tracking-[0.18em] text-leaf">
                    Featured products
                  </p>
                  <div className="mt-5 grid gap-4 md:grid-cols-2 xl:grid-cols-3">
                    {selectedSeller.products.length > 0 ? (
                      selectedSeller.products.map((product) => (
                        <button
                          key={product.id}
                          type="button"
                          onClick={() => handleSellerProductSelect(product)}
                          className="rounded-2xl border border-slate-200 bg-white p-4 text-left transition hover:border-leaf hover:shadow-sm"
                        >
                          <p className="text-xs font-bold uppercase tracking-[0.14em] text-leaf">
                            {product.categoryName || "Marketplace"}
                          </p>
                          <h4 className="mt-3 text-xl font-semibold text-forest">
                            {product.name}
                          </h4>
                          <p className="mt-2 text-sm text-slate-500">
                            {product.unit} · {product.currency} {product.price.toLocaleString("en-US")}
                          </p>
                        </button>
                      ))
                    ) : (
                      <div className="rounded-2xl border border-dashed border-slate-300 bg-slate-50 p-4 text-sm text-slate-500 md:col-span-2 xl:col-span-3">
                        No product listings available yet for this grower.
                      </div>
                    )}
                  </div>
                </div>

                <div className="mt-10">
                  <p className="text-sm font-bold uppercase tracking-[0.18em] text-leaf">
                    Inventory status
                  </p>
                  <div className="mt-5 grid gap-4 md:grid-cols-2 xl:grid-cols-3">
                    {sellerInventory.length > 0 ? (
                      sellerInventory.map((item) => (
                        <div
                          key={item.id}
                          className="rounded-2xl border border-slate-200 bg-cream p-4"
                        >
                          <div className="flex items-start justify-between gap-3">
                            <div>
                              <h4 className="text-lg font-semibold text-forest">{item.name}</h4>
                              <p className="mt-1 text-sm text-slate-500">
                                {item.categoryName || "Marketplace"}
                              </p>
                            </div>
                            <span
                              className={`rounded-full px-2.5 py-1 text-xs font-semibold ${
                                item.lowStock
                                  ? "bg-amber-100 text-amber-700"
                                  : "bg-emerald-100 text-emerald-700"
                              }`}
                            >
                              {item.lowStock ? "Low stock" : "Healthy"}
                            </span>
                          </div>
                          <p className="mt-4 text-sm text-slate-600">
                            {item.stockQuantity} {item.unit} available
                          </p>
                          <div className="mt-4 flex items-center justify-between gap-3">
                            <span className="text-sm font-medium text-slate-500">
                              {item.currency} {item.price.toLocaleString("en-US")}
                            </span>
                            <button
                              type="button"
                              onClick={() => restockInventory(item.slug, 10)}
                              className="rounded-full bg-forest px-3 py-1.5 text-xs font-semibold text-white transition hover:bg-leaf"
                            >
                              Restock +10
                            </button>
                          </div>
                          <div className="mt-3 flex items-center gap-2">
                            <input
                              type="number"
                              min={0}
                              value={inventoryEdits[item.slug] ?? item.stockQuantity}
                              onChange={(event) =>
                                setInventoryEdits((currentEdits) => ({
                                  ...currentEdits,
                                  [item.slug]: Number(event.target.value || 0),
                                }))
                              }
                              className="w-24 rounded-lg border border-slate-200 bg-white px-2 py-1.5 text-sm outline-none focus:border-forest"
                            />
                            <button
                              type="button"
                              onClick={() => {
                                if (!selectedSeller) return;
                                void fetch(
                                  `/api/v1/sellers/${encodeURIComponent(selectedSeller.slug)}/inventory/${encodeURIComponent(item.slug)}`,
                                  {
                                    method: "PATCH",
                                    headers: { "Content-Type": "application/json" },
                                    body: JSON.stringify({ stockQuantity: inventoryEdits[item.slug] ?? item.stockQuantity }),
                                  },
                                )
                                  .then((response) => {
                                    if (!response.ok) {
                                      throw new Error(`Inventory update failed: ${response.status}`);
                                    }
                                    return response.json() as Promise<SellerInventoryItem>;
                                  })
                                  .then((updatedItem) => {
                                    setSellerInventory((currentInventory) =>
                                      currentInventory.map((currentItem) =>
                                        currentItem.slug === updatedItem.slug
                                          ? {
                                              ...currentItem,
                                              ...updatedItem,
                                              lowStock: updatedItem.stockQuantity <= 15,
                                              status: updatedItem.status ?? (updatedItem.stockQuantity <= 0 ? "OUT_OF_STOCK" : updatedItem.stockQuantity <= 15 ? "LOW_STOCK" : "AVAILABLE"),
                                            }
                                          : currentItem,
                                      ),
                                    );
                                    setInventoryEdits((currentEdits) => ({
                                      ...currentEdits,
                                      [item.slug]: updatedItem.stockQuantity,
                                    }));
                                  })
                                  .catch(() => {
                                    // Ignore inventory update errors in the browser prototype.
                                  });
                              }}
                              className="rounded-full border border-slate-200 bg-white px-3 py-1.5 text-xs font-semibold text-slate-700 transition hover:border-forest hover:text-forest"
                            >
                              Set stock
                            </button>
                          </div>
                        </div>
                      ))
                    ) : (
                      <div className="rounded-2xl border border-dashed border-slate-300 bg-slate-50 p-4 text-sm text-slate-500 md:col-span-2 xl:col-span-3">
                        Inventory data is not available yet for this grower.
                      </div>
                    )}
                  </div>
                </div>
              </div>
            )}
          </div>
        </section>

        <ClosingCta />
      </HomePage>
      <SiteFooter />
    </div>
  );
}

export default App;
