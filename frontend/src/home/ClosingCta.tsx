import {
  ArrowRightIcon,
  BrandLogo,
  Container,
  Eyebrow,
  lightOutlineButtonClass,
  primaryButtonClass,
} from "./ui";
import { DUSK_WHEAT_IMAGE, PHOTO_CREDIT_URL } from "./images";

export function ClosingCta() {
  return (
    <section className="relative isolate overflow-hidden bg-brand-deep py-24 lg:py-28">
      <img
        src={DUSK_WHEAT_IMAGE}
        alt=""
        aria-hidden="true"
        loading="lazy"
        className="absolute inset-0 h-full w-full object-cover"
      />
      <div className="absolute inset-0 bg-brand-deep/85" aria-hidden="true" />
      <div
        className="absolute inset-0 bg-gradient-to-t from-brand-deep via-brand-deep/70 to-brand-deep/90"
        aria-hidden="true"
      />
      <div
        className="absolute inset-x-0 bottom-0 h-40 bg-gradient-to-t from-brand-gold/25 to-transparent"
        aria-hidden="true"
      />

      <Container className="relative mx-auto max-w-3xl text-center">
        <Eyebrow className="text-brand-gold">Grow with HarvestLink</Eyebrow>
        <h2 className="mt-5 font-display text-[34px] font-extrabold leading-[1.1] tracking-tight text-white sm:text-[44px] lg:text-[54px]">
          Let's grow the future of <span className="text-brand-gold">Ethiopian agriculture</span>.
        </h2>
        <p className="mx-auto mt-6 max-w-2xl text-[16px] leading-8 text-white/80 lg:text-[18px]">
          Whether you grow, buy, or move agricultural products, HarvestLink helps you
          connect with the opportunities around every harvest.
        </p>
        <div className="mt-9 flex flex-wrap items-center justify-center gap-4">
          <a href="#marketplace" className={primaryButtonClass}>
            Explore Marketplace
            <ArrowRightIcon />
          </a>
          <a href="#auth" className={lightOutlineButtonClass}>
            Join HarvestLink
          </a>
        </div>
      </Container>
    </section>
  );
}

const FOOTER_COLUMNS = [
  {
    heading: "Platform",
    links: [
      { label: "Marketplace", href: "#marketplace" },
      { label: "How It Works", href: "#how-it-works" },
      { label: "Farmers", href: "#for-farmers" },
      { label: "Buyers", href: "#for-buyers" },
      { label: "Logistics", href: "#logistics" },
    ],
  },
  {
    heading: "Company",
    links: [
      { label: "About", href: "#about" },
      { label: "Contact", href: "#about" },
      { label: "Help", href: "#about" },
    ],
  },
  {
    heading: "Account",
    links: [
      { label: "Login", href: "#auth" },
      { label: "Register", href: "#auth" },
    ],
  },
];

export function SiteFooter() {
  return (
    <footer className="bg-brand-deep text-white">
      <Container className="grid gap-12 py-16 lg:grid-cols-[1.5fr_1fr_1fr_1fr]">
        <div>
          <BrandLogo light />
          <p className="mt-5 max-w-xs text-[15px] leading-7 text-white/65">
            Connecting Ethiopia's agricultural community from farm to market.
          </p>
          <div className="mt-6 flex flex-wrap gap-2 text-[12px] font-semibold text-white/70">
            {["🌾 Farmers", "🛒 Buyers", "🚚 Logistics"].map((role) => (
              <span
                key={role}
                className="rounded-full border border-white/15 bg-white/5 px-3 py-1.5"
              >
                {role}
              </span>
            ))}
          </div>
        </div>

        {FOOTER_COLUMNS.map((column) => (
          <nav key={column.heading} aria-label={`${column.heading} links`}>
            <h3 className="text-[12px] font-bold uppercase tracking-[0.22em] text-brand-gold">
              {column.heading}
            </h3>
            <ul className="mt-5 space-y-3">
              {column.links.map((link) => (
                <li key={link.label}>
                  <a
                    href={link.href}
                    className="text-[15px] text-white/70 transition hover:text-white"
                  >
                    {link.label}
                  </a>
                </li>
              ))}
            </ul>
          </nav>
        ))}
      </Container>

      <div className="border-t border-white/10">
        <Container className="flex flex-col gap-3 py-6 text-[13px] text-white/50 sm:flex-row sm:items-center sm:justify-between">
          <span>© 2026 HarvestLink Ethiopia. All rights reserved.</span>
          <span>
            Photography:{" "}
            <a
              href={PHOTO_CREDIT_URL}
              target="_blank"
              rel="noreferrer"
              className="underline transition hover:text-white/80"
            >
              Wikimedia Commons contributors
            </a>
            {" · "}
            <a
              href="https://commons.wikimedia.org/wiki/File:Ethiopia_Gheralta_FarmerHarvestingTef.jpg"
              target="_blank"
              rel="noreferrer"
              className="underline transition hover:text-white/80"
            >
              Gheralta teff harvest
            </a>
            {" · "}
            <a
              href="https://commons.wikimedia.org/wiki/File:Ethiopian_farmer_at_work_on_his_land.jpg"
              target="_blank"
              rel="noreferrer"
              className="underline transition hover:text-white/80"
            >
              Farmer at work
            </a>
          </span>
        </Container>
      </div>

    </footer>
  );
}
