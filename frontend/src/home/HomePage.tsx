import { useState, type ReactNode } from "react";
import {
  ArrowRightIcon,
  BrandLogo,
  CheckIcon,
  Container,
  DownArrowIcon,
  Eyebrow,
  FieldPattern,
  PinIcon,
  lightOutlineButtonClass,
  primaryButtonClass,
  softOutlineButtonClass,
} from "./ui";
import {
  COFFEE_IMAGE,
  FARMER_IMAGE,
  HERO_IMAGE,
  MAIZE_IMAGE,
  TEFF_IMAGE,
  WHEAT_IMAGE,
} from "./images";

/* ------------------------------------------------------------------ */
/*  Content data                                                       */
/* ------------------------------------------------------------------ */

const NAV_LINKS = [
  { label: "Home", href: "#home" },
  { label: "Marketplace", href: "#marketplace" },
  { label: "How It Works", href: "#how-it-works" },
  { label: "For Farmers", href: "#for-farmers" },
  { label: "For Buyers", href: "#for-buyers" },
  { label: "Logistics", href: "#logistics" },
  { label: "About", href: "#about" },
];

const HERO_ROLES = [
  { icon: "🌾", label: "Farmers" },
  { icon: "🛒", label: "Buyers" },
  { icon: "🚚", label: "Logistics" },
];

const HERO_FLOW = ["Ethiopian Farm", "Market", "Buyer", "Logistics"];

const VALUE_CHAIN = [
  {
    icon: "🌾",
    title: "Farmers",
    body: "Showcase your harvest, reach buyers, and grow your market.",
    cta: "Join as a Farmer",
    href: "#auth",
    tile: "bg-brand-gold/15 ring-brand-gold/30",
  },
  {
    icon: "🛒",
    title: "Buyers",
    body: "Discover quality Ethiopian agricultural products from local growers.",
    cta: "Explore Products",
    href: "#marketplace",
    tile: "bg-brand-green/10 ring-brand-green/25",
  },
  {
    icon: "🚚",
    title: "Logistics",
    body: "Connect agricultural orders with reliable transportation and delivery.",
    cta: "Find Logistics",
    href: "#logistics",
    tile: "bg-brand-orange/15 ring-brand-orange/30",
  },
];

type ShowcaseItem = {
  key: string;
  name: string;
  location: string;
  image: string;
  alt: string;
  listPrice: number;
  listUnit: string;
};

const SHOWCASE_ITEMS: ShowcaseItem[] = [
  {
    key: "teff",
    name: "Teff",
    location: "Amhara, Ethiopia",
    image: TEFF_IMAGE,
    alt: "Farmers harvesting teff in northern Ethiopia",
    listPrice: 4850,
    listUnit: "kg",
  },
  {
    key: "wheat",
    name: "Wheat",
    location: "Oromia, Ethiopia",
    image: WHEAT_IMAGE,
    alt: "Ripe golden wheat ears ready for harvest",
    listPrice: 3650,
    listUnit: "kg",
  },
  {
    key: "coffee",
    name: "Coffee",
    location: "Sidama, Ethiopia",
    image: COFFEE_IMAGE,
    alt: "Ethiopian coffee beans from Sidama",
    listPrice: 1450,
    listUnit: "kg",
  },
  {
    key: "maize",
    name: "Maize",
    location: "Amhara, Ethiopia",
    image: MAIZE_IMAGE,
    alt: "Maize field ready for harvest",
    listPrice: 2980,
    listUnit: "kg",
  },
];

export type ShowcaseProduct = {
  name: string;
  slug?: string;
  price: number;
  currency: string;
  unit: string;
  stockQuantity: number;
};

const FARMER_BENEFITS = [
  "Reach more buyers",
  "Showcase your harvest",
  "Manage product listings",
  "Build trusted connections",
];

const STEPS = [
  {
    number: "01",
    title: "List",
    body: "Farmers list agricultural products with quantity, quality, location, and price.",
  },
  {
    number: "02",
    title: "Connect",
    body: "Buyers discover products and connect with growers.",
  },
  {
    number: "03",
    title: "Order",
    body: "Buyers place and manage orders through the marketplace.",
  },
  {
    number: "04",
    title: "Deliver",
    body: "Logistics partners move products from farm to destination.",
  },
];

const JOURNEY = [
  { icon: "🌾", label: "Farm" },
  { icon: "📦", label: "Pickup" },
  { icon: "🚚", label: "In Transit" },
  { icon: "📍", label: "Destination" },
];

const ETHIOPIA_VIEWBOX = { width: 1000, height: 787.4 };

const ETHIOPIA_PATH =
  "M333.8,0.0 L374.7,31.0 L414.2,14.9 L430.5,29.2 L476.7,30.0 L535.4,57.4 L552.8,80.9 L582.7,102.9 L610.4,142.9 L633.4,165.0 L609.8,195.1 L587.0,227.1 L592.2,246.0 L593.3,266.7 L630.9,267.9 L647.2,263.0 L662.1,275.2 L647.4,299.4 L672.3,337.0 L697.2,369.8 L722.9,394.2 L943.3,475.2 L1000.0,474.8 L809.5,679.6 L721.7,682.6 L661.6,730.7 L618.4,732.0 L600.0,753.5 L553.9,753.5 L526.7,730.4 L465.2,759.0 L445.2,787.4 L400.3,782.0 L385.4,774.2 L369.6,776.0 L348.3,775.3 L262.9,717.4 L216.0,717.4 L193.0,694.9 L193.0,656.6 L158.0,645.2 L118.2,570.9 L87.4,555.1 L75.6,527.8 L41.4,494.5 L0.0,489.6 L23.0,450.8 L58.7,449.1 L68.8,428.2 L67.9,366.9 L87.8,295.5 L119.8,276.4 L126.6,248.5 L155.5,196.3 L196.1,162.5 L223.5,95.3 L234.3,36.7 L312.7,50.9 Z";

type RegionPin = {
  name: string;
  x: number;
  y: number;
  side: "left" | "right";
  capital?: boolean;
};

const REGION_PINS: RegionPin[] = [
  { name: "Tigray", x: 40.75, y: 6.58, side: "right" },
  { name: "Afar", x: 52.21, y: 28.25, side: "right" },
  { name: "Amhara", x: 38.06, y: 27.38, side: "left" },
  { name: "Addis Ababa", x: 39.0, y: 51.39, side: "right", capital: true },
  { name: "Oromia", x: 42.78, y: 59.45, side: "right" },
  { name: "Somali", x: 67.72, y: 75.92, side: "right" },
  { name: "Sidama", x: 37.38, y: 72.46, side: "left" },
  { name: "South Ethiopia", x: 29.29, y: 79.39, side: "left" },
];

const NETWORK_CHAIN = [
  { label: "Farmers", icon: "🌾" },
  { label: "HarvestLink", icon: "link" },
  { label: "Buyers", icon: "🛒" },
  { label: "Logistics", icon: "🚚" },
  { label: "Markets", icon: "🏪" },
];

const TRUST_ITEMS = [
  {
    title: "Trusted Connections",
    body: "Connect with agricultural participants through one platform.",
    icon: "link",
  },
  {
    title: "Transparent Marketplace",
    body: "Clear product and order information.",
    icon: "eye",
  },
  {
    title: "Secure Access",
    body: "Professional account and authentication infrastructure.",
    icon: "shield",
  },
  {
    title: "Local First",
    body: "Designed around Ethiopia's agricultural economy.",
    icon: "leaf",
  },
];

/* ------------------------------------------------------------------ */
/*  Small helpers                                                      */
/* ------------------------------------------------------------------ */

function resolveShowcase(item: ShowcaseItem, products: ShowcaseProduct[]) {
  const match = products.find(
    (product) =>
      product.name.toLowerCase().includes(item.key) ||
      (product.slug ?? "").toLowerCase().includes(item.key),
  );

  if (match) {
    return {
      price: Number(match.price),
      currency: match.currency,
      unit: match.unit,
      available: match.stockQuantity > 0,
    };
  }

  return {
    price: item.listPrice,
    currency: "ETB",
    unit: item.listUnit,
    available: true,
  };
}

function LinkGlyph({ className = "h-5 w-5" }: { className?: string }) {
  return (
    <svg viewBox="0 0 24 24" className={className} fill="none" aria-hidden="true">
      <path
        d="M9.5 14.5 14.5 9.5M8 11l-2.3 2.3a3.6 3.6 0 0 0 5.1 5.1L13 16.2M16 13l2.3-2.3a3.6 3.6 0 0 0-5.1-5.1L11 7.8"
        stroke="currentColor"
        strokeWidth="1.7"
        strokeLinecap="round"
      />
    </svg>
  );
}

function TrustIcon({ name }: { name: string }) {
  if (name === "eye") {
    return (
      <svg viewBox="0 0 24 24" className="h-5 w-5" fill="none" aria-hidden="true">
        <path
          d="M2.8 12S6.4 6 12 6s9.2 6 9.2 6-3.6 6-9.2 6-9.2-6-9.2-6Z"
          stroke="currentColor"
          strokeWidth="1.6"
        />
        <circle cx="12" cy="12" r="2.6" stroke="currentColor" strokeWidth="1.6" />
      </svg>
    );
  }
  if (name === "shield") {
    return (
      <svg viewBox="0 0 24 24" className="h-5 w-5" fill="none" aria-hidden="true">
        <path
          d="M12 3.5 19 6v5.2c0 4.3-2.9 7.6-7 9.3-4.1-1.7-7-5-7-9.3V6l7-2.5Z"
          stroke="currentColor"
          strokeWidth="1.6"
          strokeLinejoin="round"
        />
        <path d="m9 12 2.2 2.2L15.4 10" stroke="currentColor" strokeWidth="1.7" strokeLinecap="round" strokeLinejoin="round" />
      </svg>
    );
  }
  if (name === "leaf") {
    return (
      <svg viewBox="0 0 24 24" className="h-5 w-5" fill="none" aria-hidden="true">
        <path
          d="M19.5 4.5C11 4.5 5.5 8.7 5.5 15.4c0 1.7.5 3 1.4 4 .9-4.9 3.8-8.6 8.3-10.7-3.5 2.4-5.7 5.8-6.4 10.6 1 .6 2.2.9 3.5.9 5.4 0 7.2-6.4 7.2-15.7Z"
          stroke="currentColor"
          strokeWidth="1.5"
          strokeLinejoin="round"
        />
      </svg>
    );
  }
  return <LinkGlyph className="h-5 w-5" />;
}

/* ------------------------------------------------------------------ */
/*  Header                                                             */
/* ------------------------------------------------------------------ */

function SiteHeader() {
  const [menuOpen, setMenuOpen] = useState(false);

  return (
    <header className="sticky top-0 z-50 border-b border-brand-ink/5 bg-white/95 backdrop-blur-md">
      <Container className="flex h-[68px] items-center justify-between gap-4">
        <a href="#home" aria-label="HarvestLink Ethiopia home" className="shrink-0">
          <BrandLogo />
        </a>

        <nav aria-label="Main navigation" className="hidden items-center gap-6 lg:flex">
          {NAV_LINKS.map((link) => (
            <a
              key={link.label}
              href={link.href}
              className="relative py-1 text-[13.5px] font-medium text-brand-ink/70 transition hover:text-brand-deep after:absolute after:-bottom-0.5 after:left-0 after:h-[2px] after:w-0 after:bg-brand-gold after:transition-all after:duration-300 hover:after:w-full"
            >
              {link.label}
            </a>
          ))}
        </nav>

        <div className="flex items-center gap-3">
          <a
            href="#auth"
            className="hidden text-sm font-semibold text-brand-deep transition hover:text-brand-green sm:inline"
          >
            Login
          </a>
          <a
            href="#auth"
            className="rounded-full bg-brand-deep px-5 py-2.5 text-sm font-semibold text-white shadow-sm transition hover:bg-brand-green"
          >
            Join HarvestLink
          </a>
          <button
            type="button"
            className="grid h-10 w-10 place-items-center rounded-xl border border-brand-ink/10 text-brand-deep lg:hidden"
            aria-expanded={menuOpen}
            aria-label={menuOpen ? "Close navigation menu" : "Open navigation menu"}
            onClick={() => setMenuOpen((open) => !open)}
          >
            <svg viewBox="0 0 20 20" className="h-5 w-5" fill="none" aria-hidden="true">
              {menuOpen ? (
                <path d="m5 5 10 10M15 5 5 15" stroke="currentColor" strokeWidth="1.7" strokeLinecap="round" />
              ) : (
                <path d="M3.5 6h13M3.5 10h13M3.5 14h13" stroke="currentColor" strokeWidth="1.7" strokeLinecap="round" />
              )}
            </svg>
          </button>
        </div>
      </Container>

      {menuOpen ? (
        <div className="border-t border-brand-ink/5 bg-white px-6 py-4 shadow-lg lg:hidden">
          <nav aria-label="Mobile navigation" className="grid gap-1">
            {NAV_LINKS.map((link) => (
              <a
                key={link.label}
                href={link.href}
                onClick={() => setMenuOpen(false)}
                className="rounded-lg px-3 py-2.5 text-sm font-medium text-brand-ink/75 transition hover:bg-brand-cream hover:text-brand-deep"
              >
                {link.label}
              </a>
            ))}
            <a
              href="#auth"
              onClick={() => setMenuOpen(false)}
              className="mt-2 rounded-lg px-3 py-2.5 text-sm font-semibold text-brand-deep transition hover:bg-brand-cream"
            >
              Login
            </a>
          </nav>
        </div>
      ) : null}
    </header>
  );
}

/* ------------------------------------------------------------------ */
/*  Hero                                                               */
/* ------------------------------------------------------------------ */

function Hero() {
  return (
    <section id="home" className="relative isolate overflow-hidden scroll-mt-20 bg-brand-deep">
      <img
        src={HERO_IMAGE}
        alt="An Ethiopian farmer harvesting teff in the Gheralta highlands"
        className="absolute inset-0 h-full w-full object-cover object-center"
      />
      <div className="absolute inset-0 bg-brand-deep/70" aria-hidden="true" />
      <div
        className="absolute inset-0 bg-gradient-to-r from-brand-deep/95 via-brand-deep/70 to-brand-deep/35"
        aria-hidden="true"
      />
      <div
        className="absolute inset-x-0 bottom-0 h-64 bg-gradient-to-t from-brand-gold/30 via-brand-orange/10 to-transparent"
        aria-hidden="true"
      />
      <FieldPattern
        className="absolute -right-24 top-0 h-full w-[46%] opacity-[0.10]"
      />

      <Container className="relative flex min-h-[540px] flex-col justify-center py-20 sm:min-h-[600px] lg:min-h-[652px] lg:py-24">
        <div className="max-w-3xl">
          <Eyebrow className="flex items-center gap-3 text-brand-gold">
            <span className="h-px w-10 bg-brand-gold/70" aria-hidden="true" />
            From farm to market — one connected network
          </Eyebrow>

          <h1 className="mt-6 font-display text-[42px] font-extrabold leading-[1.06] tracking-tight text-white sm:text-[64px] lg:text-[72px]">
            From <span className="text-brand-gold">Ethiopian Farms</span>{" "}
            <span className="block">to Growing Markets</span>
          </h1>

          <p className="mt-6 max-w-xl text-[16px] leading-8 text-white/85 sm:text-[18px]">
            Connecting farmers, buyers, and logistics partners through one trusted
            agricultural marketplace.
          </p>

          <div className="mt-9 flex flex-wrap items-center gap-4">
            <a href="#marketplace" className={primaryButtonClass}>
              Explore Marketplace
              <ArrowRightIcon />
            </a>
            <a href="#auth" className={lightOutlineButtonClass}>
              Join as a Farmer
            </a>
          </div>

          <div className="mt-10 hidden flex-wrap items-center gap-x-3 gap-y-2 text-[11px] font-bold uppercase tracking-[0.22em] text-white/55 sm:flex">
            {HERO_FLOW.map((step, index) => (
              <span key={step} className="flex items-center gap-3">
                {step}
                {index < HERO_FLOW.length - 1 ? (
                  <span className="text-brand-gold" aria-hidden="true">
                    →
                  </span>
                ) : null}
              </span>
            ))}
          </div>
        </div>
      </Container>

      <div className="absolute bottom-8 right-6 hidden flex-wrap justify-end gap-3 lg:right-10 lg:flex">
        {HERO_ROLES.map((role) => (
          <span
            key={role.label}
            className="flex items-center gap-2 rounded-full border border-white/25 bg-white/10 px-4 py-2 text-sm font-medium text-white backdrop-blur-sm"
          >
            <span aria-hidden="true">{role.icon}</span>
            {role.label}
          </span>
        ))}
      </div>
    </section>
  );
}

/* ------------------------------------------------------------------ */
/*  Section 2 — agricultural value chain                               */
/* ------------------------------------------------------------------ */

function ValueChain() {
  return (
    <section className="bg-white py-20 lg:py-24">
      <Container>
        <div className="mx-auto max-w-2xl text-center">
          <Eyebrow className="text-brand-orange">The HarvestLink network</Eyebrow>
          <h2 className="mt-4 font-display text-[32px] font-extrabold leading-[1.12] tracking-tight text-brand-deep sm:text-[38px] lg:text-[46px]">
            One network. From field to market.
          </h2>
          <p className="mt-5 text-[16px] leading-7 text-brand-ink/70 lg:text-[17px]">
            HarvestLink brings the people who grow, buy, and move agricultural products
            together.
          </p>
        </div>

        <div className="mt-14 grid gap-6 md:grid-cols-3">
          {VALUE_CHAIN.map((card) => (
            <article
              key={card.title}
              className="flex flex-col rounded-3xl border border-brand-ink/5 bg-brand-cream p-7 shadow-card transition duration-300 hover:-translate-y-1 hover:bg-white"
            >
              <span
                className={`grid h-14 w-14 place-items-center rounded-2xl text-2xl ring-1 ${card.tile}`}
                aria-hidden="true"
              >
                {card.icon}
              </span>
              <h3 className="mt-6 text-[13px] font-bold uppercase tracking-[0.22em] text-brand-green">
                {card.title}
              </h3>
              <p className="mt-3 flex-1 text-[16px] leading-7 text-brand-ink/70">
                {card.body}
              </p>
              <a href={card.href} className={`mt-6 self-start ${softOutlineButtonClass}`}>
                {card.cta}
                <ArrowRightIcon className="h-3.5 w-3.5" />
              </a>
            </article>
          ))}
        </div>
      </Container>
    </section>
  );
}

/* ------------------------------------------------------------------ */
/*  Section 3 — wheat + teff marketplace                               */
/* ------------------------------------------------------------------ */

function MarketShowcase({ products }: { products: ShowcaseProduct[] }) {
  return (
    <section id="for-buyers" className="scroll-mt-20 bg-brand-cream py-20 lg:py-24">
      <Container>
        <div className="flex flex-col gap-6 sm:flex-row sm:items-end sm:justify-between">
          <div className="max-w-2xl">
            <Eyebrow className="text-brand-orange">Marketplace showcase</Eyebrow>
            <h2 className="mt-4 font-display text-[32px] font-extrabold leading-[1.12] tracking-tight text-brand-deep sm:text-[38px] lg:text-[46px]">
              Harvests with real opportunity.
            </h2>
            <p className="mt-5 text-[16px] leading-7 text-brand-ink/70 lg:text-[17px]">
              Discover agricultural products from Ethiopian growers.
            </p>
          </div>
          <a
            href="#marketplace"
            className="inline-flex items-center gap-2 text-sm font-semibold text-brand-deep transition hover:text-brand-orange"
          >
            Browse the full marketplace
            <ArrowRightIcon />
          </a>
        </div>

        <div className="mt-12 grid gap-6 sm:grid-cols-2 lg:grid-cols-4">
          {SHOWCASE_ITEMS.map((item) => {
            const details = resolveShowcase(item, products);

            return (
              <article
                key={item.key}
                className="group flex flex-col overflow-hidden rounded-3xl border border-brand-ink/5 bg-white shadow-card transition duration-300 hover:-translate-y-1"
              >
                <div className="relative aspect-[4/3] overflow-hidden">
                  <img
                    src={item.image}
                    alt={item.alt}
                    loading="lazy"
                    className="h-full w-full object-cover transition duration-500 group-hover:scale-105"
                  />
                  <span
                    className={`absolute left-3 top-3 inline-flex items-center gap-1.5 rounded-full px-2.5 py-1 text-[11px] font-bold uppercase tracking-wider ${
                      details.available
                        ? "bg-white/95 text-brand-green"
                        : "bg-white/95 text-brand-orange"
                    }`}
                  >
                    <span
                      className={`h-1.5 w-1.5 rounded-full ${
                        details.available ? "bg-brand-green" : "bg-brand-orange"
                      }`}
                      aria-hidden="true"
                    />
                    {details.available ? "Available" : "Sold out"}
                  </span>
                </div>

                <div className="flex flex-1 flex-col p-5">
                  <div className="flex items-baseline justify-between gap-3">
                    <h3 className="font-display text-lg font-extrabold uppercase tracking-wide text-brand-deep">
                      {item.name}
                    </h3>
                    <span className="text-[11px] font-semibold uppercase tracking-wider text-brand-ink/45">
                      Grade A
                    </span>
                  </div>

                  <p className="mt-2 flex items-center gap-1.5 text-sm text-brand-ink/60">
                    <PinIcon className="h-4 w-4 text-brand-orange" />
                    {item.location}
                  </p>

                  <div className="mt-4 flex items-end justify-between gap-3 border-t border-brand-ink/5 pt-4">
                    <div>
                      <p className="text-[10px] font-bold uppercase tracking-[0.18em] text-brand-ink/45">
                        Price
                      </p>
                      <p className="mt-1 font-display text-[21px] font-extrabold text-brand-deep">
                        {details.currency}{" "}
                        <span itemProp="price" content={String(details.price)}>
                          {details.price.toLocaleString("en-US")}
                        </span>
                        <span className="ml-1 text-[13px] font-semibold text-brand-ink/50">
                          / {details.unit}
                        </span>
                      </p>
                    </div>
                  </div>

                  <a
                    href="#marketplace"
                    className="mt-5 inline-flex items-center justify-center rounded-full bg-brand-deep px-4 py-2.5 text-sm font-semibold text-white transition hover:bg-brand-green"
                  >
                    View Product
                  </a>
                </div>
              </article>
            );
          })}
        </div>
      </Container>
    </section>
  );
}

/* ------------------------------------------------------------------ */
/*  Section 4 — featured farmer                                        */
/* ------------------------------------------------------------------ */

function FeaturedFarmer() {
  return (
    <section id="for-farmers" className="scroll-mt-20 bg-white py-20 lg:py-24">
      <Container className="grid items-center gap-12 lg:grid-cols-2 lg:gap-16">
        <div className="relative">
          <div
            className="absolute -left-6 -top-6 h-32 w-32 rounded-full bg-brand-gold/25 blur-2xl"
            aria-hidden="true"
          />
          <img
            src={FARMER_IMAGE}
            alt="An Ethiopian farmer working the soil on his land"
            loading="lazy"
            className="relative aspect-[4/3] w-full rounded-[2rem] object-cover shadow-2xl shadow-brand-deep/20"
          />
          <div className="absolute -bottom-5 left-6 flex items-center gap-3 rounded-2xl bg-white px-5 py-3 shadow-xl shadow-brand-deep/10">
            <span className="grid h-9 w-9 place-items-center rounded-xl bg-brand-cream text-lg" aria-hidden="true">
              🌾
            </span>
            <span className="leading-tight">
              <span className="block text-[11px] font-bold uppercase tracking-[0.16em] text-brand-orange">
                Harvest season
              </span>
              <span className="block text-sm font-semibold text-brand-deep">
                Teff &amp; wheat highlands
              </span>
            </span>
          </div>
        </div>

        <div>
          <Eyebrow className="text-brand-orange">Built for Ethiopian farmers</Eyebrow>
          <h2 className="mt-4 font-display text-[32px] font-extrabold leading-[1.12] tracking-tight text-brand-deep sm:text-[38px] lg:text-[44px]">
            Your harvest deserves a bigger market.
          </h2>
          <p className="mt-5 max-w-xl text-[16px] leading-8 text-brand-ink/70 lg:text-[17px]">
            Create a digital presence for your farm, showcase your harvest, connect with
            buyers, and build stronger agricultural relationships.
          </p>

          <ul className="mt-8 grid gap-4 sm:grid-cols-2">
            {FARMER_BENEFITS.map((benefit) => (
              <li key={benefit} className="flex items-center gap-3 text-[15px] font-medium text-brand-ink/80">
                <span className="grid h-7 w-7 shrink-0 place-items-center rounded-full bg-brand-green/10 text-brand-green">
                  <CheckIcon className="h-4 w-4" />
                </span>
                {benefit}
              </li>
            ))}
          </ul>

          <div className="mt-9">
            <a href="#auth" className={primaryButtonClass}>
              Join as a Farmer
              <ArrowRightIcon />
            </a>
          </div>
        </div>
      </Container>
    </section>
  );
}

/* ------------------------------------------------------------------ */
/*  Section 5 — how it works                                           */
/* ------------------------------------------------------------------ */

function HowItWorks() {
  return (
    <section id="how-it-works" className="scroll-mt-20 bg-brand-cream py-20 lg:py-24">
      <Container>
        <div className="mx-auto max-w-2xl text-center">
          <Eyebrow className="text-brand-orange">How it works</Eyebrow>
          <h2 className="mt-4 font-display text-[32px] font-extrabold leading-[1.12] tracking-tight text-brand-deep sm:text-[38px] lg:text-[46px]">
            From harvest to destination.
          </h2>
        </div>

        <ol className="relative mt-14 grid gap-10 md:grid-cols-4 md:gap-6">
          {STEPS.map((step, index) => (
            <li key={step.number} className="relative">
              {index > 0 ? (
                <span
                  aria-hidden="true"
                  className="absolute -left-6 top-8 hidden h-px w-12 bg-gradient-to-r from-brand-gold/60 to-brand-gold/60 md:block"
                />
              ) : null}
              {index > 0 ? (
                <span
                  aria-hidden="true"
                  className="absolute -left-[9px] top-[26px] hidden text-brand-gold md:block"
                >
                  <svg viewBox="0 0 16 16" className="h-3.5 w-3.5" fill="none">
                    <path d="M3 8h10m0 0L9.5 4.5M13 8l-3.5 3.5" stroke="currentColor" strokeWidth="1.6" strokeLinecap="round" strokeLinejoin="round" />
                  </svg>
                </span>
              ) : null}
              {index > 0 ? (
                <span
                  aria-hidden="true"
                  className="absolute -top-8 left-[22px] h-8 w-px bg-brand-gold/60 md:hidden"
                />
              ) : null}
              {index > 0 ? (
                <span
                  aria-hidden="true"
                  className="absolute -top-[38px] left-[15px] text-brand-gold md:hidden"
                >
                  <DownArrowIcon className="h-4 w-4" />
                </span>
              ) : null}

              <span className="grid h-16 w-16 place-items-center rounded-2xl bg-white font-display text-lg font-extrabold text-brand-gold shadow-card ring-1 ring-brand-gold/30">
                {step.number}
              </span>
              <h3 className="mt-5 text-[13px] font-bold uppercase tracking-[0.22em] text-brand-deep">
                {step.title}
              </h3>
              <p className="mt-3 text-[15px] leading-7 text-brand-ink/70">{step.body}</p>
            </li>
          ))}
        </ol>
      </Container>
    </section>
  );
}

/* ------------------------------------------------------------------ */
/*  Section 6 — logistics                                              */
/* ------------------------------------------------------------------ */

function Logistics() {
  return (
    <section
      id="logistics"
      className="relative isolate scroll-mt-20 overflow-hidden bg-brand-deep py-20 text-white lg:py-24"
    >
      <div
        className="absolute -right-32 -top-32 h-96 w-96 rounded-full bg-brand-gold/15 blur-3xl"
        aria-hidden="true"
      />
      <FieldPattern className="absolute inset-y-0 right-0 w-1/3 opacity-[0.07]" />

      <Container className="relative grid items-center gap-12 lg:grid-cols-[0.9fr_1.1fr] lg:gap-16">
        <div>
          <Eyebrow className="text-brand-gold">Logistics network</Eyebrow>
          <h2 className="mt-4 font-display text-[32px] font-extrabold leading-[1.12] tracking-tight sm:text-[38px] lg:text-[44px]">
            Move agricultural products with confidence.
          </h2>
          <p className="mt-5 max-w-xl text-[16px] leading-8 text-white/75 lg:text-[17px]">
            Connect farmers and buyers with logistics partners and keep delivery progress
            visible.
          </p>
          <div className="mt-8">
            <a href="#marketplace" className={primaryButtonClass}>
              Explore Logistics
              <ArrowRightIcon />
            </a>
          </div>
        </div>

        <div className="rounded-[2rem] border border-white/10 bg-white/5 p-6 backdrop-blur-sm sm:p-8">
          <ol className="relative grid gap-8 sm:grid-cols-4 sm:gap-4">
            <span
              aria-hidden="true"
              className="absolute left-0 right-0 top-7 hidden border-t border-dashed border-brand-gold/50 sm:block"
            />
            {JOURNEY.map((stop, index) => (
              <li key={stop.label} className="relative flex items-center gap-4 sm:flex-col sm:gap-3 sm:text-center">
                {index > 0 ? (
                  <>
                    <span
                      aria-hidden="true"
                      className="absolute -left-4 top-1/2 hidden h-px w-4 bg-brand-gold/50 sm:block"
                    />
                    <span
                      aria-hidden="true"
                      className="absolute -left-7 top-1/2 -translate-y-1/2 text-brand-gold sm:hidden"
                    >
                      <DownArrowIcon className="h-4 w-4" />
                    </span>
                    <span
                      aria-hidden="true"
                      className="absolute -top-4 left-[21px] h-4 w-px bg-brand-gold/50 sm:hidden"
                    />
                  </>
                ) : null}

                <span className="relative grid h-14 w-14 shrink-0 place-items-center rounded-full border border-brand-gold/40 bg-brand-deep text-xl shadow-lg">
                  <span aria-hidden="true">{stop.icon}</span>
                </span>
                <span className="text-[11px] font-bold uppercase tracking-[0.18em] text-white/80">
                  {stop.label}
                </span>
              </li>
            ))}
          </ol>

          <div className="mt-8 flex flex-col gap-4 border-t border-white/10 pt-6 sm:flex-row sm:items-center sm:justify-between">
            <p className="text-sm leading-6 text-white/70">
              Track agricultural deliveries from origin to destination.
            </p>
            <span className="inline-flex items-center gap-2 rounded-full bg-white/10 px-4 py-2 text-[13px] font-semibold text-brand-gold">
              <span aria-hidden="true">🚚</span>
              Origin → destination visibility
            </span>
          </div>
        </div>
      </Container>
    </section>
  );
}

/* ------------------------------------------------------------------ */
/*  Section 7 — Ethiopia                                               */
/* ------------------------------------------------------------------ */

function EthiopiaNetwork() {
  const capital = REGION_PINS.find((pin) => pin.capital) ?? REGION_PINS[0];
  const toViewBoxX = (percent: number) => (percent / 100) * ETHIOPIA_VIEWBOX.width;
  const toViewBoxY = (percent: number) => (percent / 100) * ETHIOPIA_VIEWBOX.height;

  return (
    <section id="about" className="scroll-mt-20 overflow-hidden bg-white py-20 lg:py-24">
      <Container>
        <div className="mx-auto max-w-2xl text-center">
          <Eyebrow className="text-brand-orange">Across Ethiopia</Eyebrow>
          <h2 className="mt-4 font-display text-[32px] font-extrabold leading-[1.12] tracking-tight text-brand-deep sm:text-[38px] lg:text-[46px]">
            Connecting agricultural communities across Ethiopia.
          </h2>
        </div>

        <div className="mt-12 grid items-center gap-14 lg:grid-cols-[1.1fr_0.9fr]">
          <div className="relative mx-auto w-full max-w-[560px]">
            <div
              className="absolute inset-0 -z-10 scale-90 rounded-full bg-brand-gold/10 blur-3xl"
              aria-hidden="true"
            />
            <div className="relative aspect-[1000/787] w-full">
              <svg
                viewBox={`0 0 ${ETHIOPIA_VIEWBOX.width} ${ETHIOPIA_VIEWBOX.height}`}
                className="absolute inset-0 h-full w-full"
                role="img"
                aria-label="Outline map of Ethiopia with agricultural connection points"
              >
                <path
                  d={ETHIOPIA_PATH}
                  fill="#2F6B45"
                  fillOpacity="0.08"
                  stroke="#123F32"
                  strokeOpacity="0.5"
                  strokeWidth="3"
                  strokeLinejoin="round"
                />
                {REGION_PINS.filter((pin) => !pin.capital).map((pin) => (
                  <line
                    key={`link-${pin.name}`}
                    x1={toViewBoxX(capital.x)}
                    y1={toViewBoxY(capital.y)}
                    x2={toViewBoxX(pin.x)}
                    y2={toViewBoxY(pin.y)}
                    stroke="#D8A83E"
                    strokeOpacity="0.55"
                    strokeWidth="2"
                    strokeDasharray="6 10"
                    strokeLinecap="round"
                  />
                ))}
              </svg>

              {REGION_PINS.map((pin) => {
                const dot = (
                  <span
                    className={`block h-2.5 w-2.5 shrink-0 rounded-full ring-4 ${
                      pin.capital
                        ? "bg-brand-orange ring-brand-orange/25"
                        : "bg-brand-gold ring-brand-gold/25"
                    }`}
                    aria-hidden="true"
                  />
                );
                const label = (
                  <span
                    className={`whitespace-nowrap rounded-full bg-white/85 px-2 py-0.5 text-[11px] font-semibold shadow-sm ${
                      pin.capital ? "font-bold text-brand-deep" : "text-brand-deep/75"
                    }`}
                  >
                    {pin.name}
                  </span>
                );

                const style =
                  pin.side === "right"
                    ? { left: `${pin.x}%`, top: `${pin.y}%`, transform: "translate(0, -50%)" }
                    : { right: `${100 - pin.x}%`, top: `${pin.y}%`, transform: "translate(0, -50%)" };

                return (
                  <span
                    key={pin.name}
                    className="absolute flex items-center gap-1.5"
                    style={style}
                  >
                    {pin.side === "right" ? (
                      <>
                        {dot}
                        {label}
                      </>
                    ) : (
                      <>
                        {label}
                        {dot}
                      </>
                    )}
                  </span>
                );
              })}
            </div>
          </div>

          <div>
            <h3 className="font-display text-[22px] font-extrabold leading-snug text-brand-deep lg:text-[26px]">
              One connected chain, coast to coast.
            </h3>
            <p className="mt-4 text-[16px] leading-7 text-brand-ink/70">
              Regional connection points link growers, buyers, logistics partners, and
              markets through a single agricultural network.
            </p>

            <ol className="relative mt-8 space-y-4">
              {NETWORK_CHAIN.map((node, index) => (
                <li key={node.label} className="relative flex items-center gap-4">
                  {index < NETWORK_CHAIN.length - 1 ? (
                    <span
                      aria-hidden="true"
                      className="absolute left-6 top-full h-4 w-px bg-brand-gold/60"
                    />
                  ) : null}
                  <span
                    className={`grid h-12 w-12 shrink-0 place-items-center rounded-full text-lg ring-1 ${
                      node.label === "HarvestLink"
                        ? "bg-brand-gold text-brand-ink ring-brand-gold/40 shadow-pill"
                        : "bg-brand-cream text-brand-deep ring-brand-ink/10"
                    }`}
                  >
                    {node.icon === "link" ? (
                      <LinkGlyph className="h-5 w-5" />
                    ) : (
                      <span aria-hidden="true">{node.icon}</span>
                    )}
                  </span>
                  <span
                    className={`rounded-full px-4 py-2 text-sm font-semibold ${
                      node.label === "HarvestLink"
                        ? "bg-brand-deep text-white"
                        : "border border-brand-ink/10 bg-white text-brand-ink/80"
                    }`}
                  >
                    {node.label}
                  </span>
                </li>
              ))}
            </ol>
          </div>
        </div>
      </Container>
    </section>
  );
}

/* ------------------------------------------------------------------ */
/*  Section 8 — trust                                                  */
/* ------------------------------------------------------------------ */

function TrustSection() {
  return (
    <section className="bg-brand-cream py-20 lg:py-24">
      <Container>
        <div className="mx-auto max-w-2xl text-center">
          <Eyebrow className="text-brand-orange">Why HarvestLink</Eyebrow>
          <h2 className="mt-4 font-display text-[32px] font-extrabold leading-[1.12] tracking-tight text-brand-deep sm:text-[38px] lg:text-[46px]">
            Built for a more connected agricultural market.
          </h2>
        </div>

        <div className="mt-12 grid gap-5 sm:grid-cols-2 lg:grid-cols-4">
          {TRUST_ITEMS.map((item) => (
            <article
              key={item.title}
              className="rounded-2xl border border-brand-ink/5 bg-white p-6 shadow-card transition duration-300 hover:-translate-y-1"
            >
              <span className="grid h-11 w-11 place-items-center rounded-xl bg-brand-green/10 text-brand-green">
                <TrustIcon name={item.icon} />
              </span>
              <h3 className="mt-5 text-[17px] font-bold text-brand-deep">{item.title}</h3>
              <p className="mt-2 text-[15px] leading-6 text-brand-ink/65">{item.body}</p>
            </article>
          ))}
        </div>
      </Container>
    </section>
  );
}

/* ------------------------------------------------------------------ */
/*  Page                                                               */
/* ------------------------------------------------------------------ */

export default function HomePage({
  products,
  children,
}: {
  products: ShowcaseProduct[];
  children?: ReactNode;
}) {
  return (
    <>
      <SiteHeader />
      <main>
        <Hero />
        <ValueChain />
        <MarketShowcase products={products} />
        <FeaturedFarmer />
        <HowItWorks />
        <Logistics />
        <EthiopiaNetwork />
        <TrustSection />
        {children}
      </main>
    </>
  );
}
