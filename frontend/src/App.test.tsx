import { cleanup, fireEvent, render, screen, waitFor } from "@testing-library/react";
import { afterEach, describe, expect, it, vi } from "vitest";
import App from "./App";

describe("marketplace landing page", () => {
  afterEach(() => {
    cleanup();
    vi.unstubAllGlobals();
    document.cookie = "XSRF-TOKEN=; Max-Age=0; Path=/";
  });

  it("introduces the platform and its agricultural community", async () => {
    vi.stubGlobal(
      "fetch",
      vi.fn().mockResolvedValue({
        ok: true,
        json: async () => ({ status: "UP" }),
      }),
    );

    render(<App />);

    expect(
      screen.getByRole("heading", { name: /from ethiopian farms to growing markets/i }),
    ).toBeInTheDocument();
    expect(
      screen.getByRole("heading", { name: /from ethiopian farms to growing markets/i })
        .textContent,
    ).toContain("Ethiopian Farms");
    expect(
      screen.getByText(/connecting farmers, buyers, and logistics partners/i),
    ).toBeInTheDocument();
    expect(await screen.findByText("Platform services online")).toBeInTheDocument();
  });

  it("lets buyers filter the catalog by category", async () => {
    const fetchMock = vi.fn((input: RequestInfo | URL) => {
      const url = String(input);

      if (url === "/api/v1/health") {
        return Promise.resolve({
          ok: true,
          json: async () => ({ status: "UP" }),
        });
      }

      if (url === "/api/v1/catalog") {
        return Promise.resolve({
          ok: true,
          json: async () => ({
            categories: [{ id: 1, name: "Cereals", slug: "cereals" }],
            products: [{
              id: 11,
              name: "Teff",
              slug: "teff",
              description: "Whole grain teff",
              origin: "Amhara",
              unit: "kg",
              price: 4200,
              currency: "ETB",
              stockQuantity: 180,
              categoryName: "Cereals",
            }],
          }),
        });
      }

      if (url === "/api/v1/catalog?category=cereals") {
        return Promise.resolve({
          ok: true,
          json: async () => ({
            categories: [{ id: 1, name: "Cereals", slug: "cereals" }],
            products: [{
              id: 11,
              name: "Teff",
              slug: "teff",
              description: "Whole grain teff",
              origin: "Amhara",
              unit: "kg",
              price: 4200,
              currency: "ETB",
              stockQuantity: 180,
              categoryName: "Cereals",
            }],
          }),
        });
      }

      return Promise.resolve({ ok: true, json: async () => ({}) });
    });

    vi.stubGlobal("fetch", fetchMock);

    render(<App />);

    const cerealsButton = await screen.findByRole("button", { name: "Cereals" });
    fireEvent.click(cerealsButton);

    expect(fetchMock).toHaveBeenCalledWith(
      "/api/v1/catalog?category=cereals",
      expect.objectContaining({ signal: expect.any(AbortSignal) }),
    );
  });

  it("supports searching and sorting the catalog", async () => {
    const fetchMock = vi.fn((input: RequestInfo | URL) => {
      const url = String(input);

      if (url === "/api/v1/health") {
        return Promise.resolve({
          ok: true,
          json: async () => ({ status: "UP" }),
        });
      }

      if (url.includes("/api/v1/catalog")) {
        return Promise.resolve({
          ok: true,
          json: async () => ({
            categories: [{ id: 1, name: "Cereals", slug: "cereals" }],
            products: [{
              id: 11,
              name: "Teff",
              slug: "teff",
              description: "Whole grain teff",
              origin: "Amhara",
              unit: "kg",
              price: 4200,
              currency: "ETB",
              stockQuantity: 180,
              categoryName: "Cereals",
            }],
          }),
        });
      }

      return Promise.resolve({ ok: true, json: async () => ({}) });
    });

    vi.stubGlobal("fetch", fetchMock);

    render(<App />);

    const [searchInput] = await screen.findAllByPlaceholderText("Search products");
    fireEvent.change(searchInput, { target: { value: "teff" } });

    const [sortSelect] = await screen.findAllByLabelText("Sort products");
    fireEvent.change(sortSelect, { target: { value: "price_desc" } });

    expect(fetchMock).toHaveBeenCalledWith(
      "/api/v1/catalog?search=teff&sort=price_desc",
      expect.objectContaining({ signal: expect.any(AbortSignal) }),
    );
  });

  it("adds products to the cart and updates the checkout summary", async () => {
    const fetchMock = vi.fn((input: RequestInfo | URL) => {
      const url = String(input);

      if (url === "/api/v1/health") {
        return Promise.resolve({
          ok: true,
          json: async () => ({ status: "UP" }),
        });
      }

      if (url.includes("/api/v1/catalog")) {
        return Promise.resolve({
          ok: true,
          json: async () => ({
            categories: [{ id: 1, name: "Cereals", slug: "cereals" }],
            products: [{
              id: 11,
              name: "Teff",
              slug: "teff",
              description: "Whole grain teff",
              origin: "Amhara",
              unit: "kg",
              price: 4200,
              currency: "ETB",
              stockQuantity: 180,
              categoryName: "Cereals",
            }],
          }),
        });
      }

      return Promise.resolve({ ok: true, json: async () => ({}) });
    });

    vi.stubGlobal("fetch", fetchMock);

    render(<App />);

    const addToCartButtons = await screen.findAllByRole("button", { name: "Add to cart" });
    fireEvent.click(addToCartButtons[0]);

    expect(screen.getByText(/Cart \(1 items\)/i)).toBeInTheDocument();
    expect(screen.getByText(/Cart total:/i)).toBeInTheDocument();
  });

  it("shows seller operations insight cards for inventory and fulfillment", async () => {
    const fetchMock = vi.fn((input: RequestInfo | URL) => {
      const url = String(input);

      if (url === "/api/v1/health") {
        return Promise.resolve({
          ok: true,
          json: async () => ({ status: "UP" }),
        });
      }

      if (url.includes("/api/v1/catalog")) {
        return Promise.resolve({
          ok: true,
          json: async () => ({
            categories: [{ id: 1, name: "Cereals", slug: "cereals" }],
            products: [{
              id: 11,
              name: "Teff",
              slug: "teff",
              description: "Whole grain teff",
              origin: "Amhara",
              unit: "kg",
              price: 4200,
              currency: "ETB",
              stockQuantity: 18,
              categoryName: "Cereals",
            }],
          }),
        });
      }

      if (url === "/api/v1/orders") {
        return Promise.resolve({
          ok: true,
          json: async () => [{
            id: 101,
            buyerName: "Selam",
            status: "PENDING",
            totalAmount: 4200,
            createdAt: new Date().toISOString(),
            itemCount: 1,
          }],
        });
      }

      if (url === "/api/v1/shipments") {
        return Promise.resolve({
          ok: true,
          json: async () => [{
            id: 21,
            orderId: 101,
            carrier: "EthioPost",
            trackingCode: "ET-1001",
            origin: "Addis Ababa",
            destination: "Dire Dawa",
            status: "IN_TRANSIT",
            eta: "2 days",
            createdAt: new Date().toISOString(),
          }],
        });
      }

      return Promise.resolve({ ok: true, json: async () => ({}) });
    });

    vi.stubGlobal("fetch", fetchMock);

    render(<App />);

    expect(await screen.findByRole("heading", { name: /inventory and fulfillment visibility for growers/i })).toBeInTheDocument();
    expect(screen.getByText(/low-stock alerts/i)).toBeInTheDocument();
    expect(screen.getByText(/active shipments/i)).toBeInTheDocument();
  });

  it("submits farmer registration with FAN, phone, full name, and password", async () => {
    const fetchMock = vi.fn((input: RequestInfo | URL, init?: RequestInit) => {
      const url = String(input);
      if (url === "/api/v1/auth/me") {
        return Promise.resolve({ ok: false, status: 403, json: async () => ({}) });
      }
      if (url === "/api/v1/auth/csrf") {
        return Promise.resolve({ ok: true, json: async () => ({ token: "csrf-token" }) });
      }
      if (url === "/api/v1/auth/register") {
        return Promise.resolve({
          ok: true,
          json: async () => ({ role: "FARMER", mustChangePassword: false }),
          requestInit: init,
        });
      }
      return Promise.resolve({ ok: true, json: async () => ({}) });
    });
    vi.stubGlobal("fetch", fetchMock);
    document.cookie = "XSRF-TOKEN=csrf-token; Path=/";

    render(<App />);
    fireEvent.click(await screen.findByRole("button", { name: "Farmer registration" }));
    fireEvent.change(screen.getByLabelText("Farmer full name"), {
      target: { value: "Abebe Bekele" },
    });
    fireEvent.change(screen.getByLabelText("National ID / FAN number"), {
      target: { value: "FAN-12345" },
    });
    fireEvent.change(screen.getByLabelText("Phone number"), {
      target: { value: "+251911234567" },
    });
    fireEvent.change(screen.getByLabelText("Password"), {
      target: { value: "FarmerPassword2026!" },
    });
    fireEvent.click(screen.getByRole("button", { name: "Create farmer account" }));

    await waitFor(() => {
      expect(fetchMock).toHaveBeenCalledWith(
        "/api/v1/auth/register",
        expect.objectContaining({
          method: "POST",
          headers: expect.objectContaining({ "X-XSRF-TOKEN": "csrf-token" }),
          body: JSON.stringify({
            fullName: "Abebe Bekele",
            fanNumber: "FAN-12345",
            phoneNumber: "+251911234567",
            password: "FarmerPassword2026!",
          }),
        }),
      );
    });
    expect(await screen.findByText(/Farmer account created/i)).toBeInTheDocument();
  });

  it("requires an operator-issued temporary password to be changed after login", async () => {
    const fetchMock = vi.fn((input: RequestInfo | URL, init?: RequestInit) => {
      const url = String(input);
      if (url === "/api/v1/auth/me") {
        return Promise.resolve({ ok: false, status: 403, json: async () => ({}) });
      }
      if (url === "/api/v1/auth/csrf") {
        return Promise.resolve({ ok: true, json: async () => ({ token: "csrf-token" }) });
      }
      if (url === "/api/v1/auth/login") {
        return Promise.resolve({
          ok: true,
          json: async () => ({
            loginId: "+251911234567",
            displayName: "Abebe Bekele",
            phoneNumber: "+251911234567",
            role: "FARMER",
            mustChangePassword: true,
          }),
          requestInit: init,
        });
      }
      if (url === "/api/v1/auth/change-password") {
        return Promise.resolve({
          ok: true,
          json: async () => ({
            loginId: "+251911234567",
            displayName: "Abebe Bekele",
            phoneNumber: "+251911234567",
            role: "FARMER",
            mustChangePassword: false,
          }),
          requestInit: init,
        });
      }
      return Promise.resolve({ ok: true, json: async () => ({}) });
    });
    vi.stubGlobal("fetch", fetchMock);
    document.cookie = "XSRF-TOKEN=csrf-token; Path=/";

    render(<App />);
    fireEvent.change(await screen.findByLabelText("Phone number or operator username"), {
      target: { value: "+251911234567" },
    });
    fireEvent.change(screen.getByLabelText("Password"), {
      target: { value: "TemporaryPassword2026!" },
    });
    fireEvent.click(screen.getByRole("button", { name: "Login to dashboard" }));

    expect(await screen.findByText(/must be changed before continuing/i)).toBeInTheDocument();
    fireEvent.change(screen.getByLabelText("New password"), {
      target: { value: "NewSecureFarmerPass2026!" },
    });
    fireEvent.click(screen.getByRole("button", { name: "Change password and continue" }));

    expect(await screen.findByText("Password updated successfully.")).toBeInTheDocument();
    expect(screen.queryByLabelText("New password")).not.toBeInTheDocument();
    expect(fetchMock).toHaveBeenCalledWith(
      "/api/v1/auth/change-password",
      expect.objectContaining({
        method: "POST",
        body: JSON.stringify({ newPassword: "NewSecureFarmerPass2026!" }),
      }),
    );
  });
});
