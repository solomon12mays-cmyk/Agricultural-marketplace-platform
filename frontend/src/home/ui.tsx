import { useId, type ReactNode } from "react";

export function Container({
  className = "",
  children,
}: {
  className?: string;
  children: ReactNode;
}) {
  return (
    <div className={`mx-auto w-full max-w-[1280px] px-6 lg:px-10 ${className}`}>
      {children}
    </div>
  );
}

export function BrandLogo({ light = false }: { light?: boolean }) {
  return (
    <span className="flex items-center gap-2.5">
      <span
        className={`grid h-10 w-10 shrink-0 place-items-center rounded-xl ${
          light ? "bg-white/10 ring-1 ring-white/25" : "bg-brand-deep"
        }`}
      >
        <svg viewBox="0 0 24 24" className="h-[22px] w-[22px]" fill="none" aria-hidden="true">
          <path d="M12 21.5V12.8" stroke="#D8A83E" strokeWidth="1.8" strokeLinecap="round" />
          <path d="M12 13.6C12 10.2 9.3 7.4 5.7 7.4c0 3.4 2.7 6.2 6.3 6.2Z" fill="#D8A83E" fillOpacity="0.9" />
          <path d="M12 13.6c0-3.4 2.7-6.2 6.3-6.2 0 3.4-2.7 6.2-6.3 6.2Z" fill="#D8A83E" fillOpacity="0.6" />
          <path d="M12 9.2c0-2.3 1-4.2 3-5.4.4 2.7-.6 4.9-3 5.4Z" fill="#D8A83E" />
        </svg>
      </span>
      <span className="leading-none">
        <span
          className={`block font-display text-[17px] font-extrabold tracking-tight ${
            light ? "text-white" : "text-brand-deep"
          }`}
        >
          HarvestLink
        </span>
        <span className="mt-1 block text-[10px] font-bold tracking-[0.32em] text-brand-gold">
          ETHIOPIA
        </span>
      </span>
    </span>
  );
}

export function Eyebrow({
  children,
  className = "",
}: {
  children: ReactNode;
  className?: string;
}) {
  return (
    <p className={`text-[12px] font-bold uppercase tracking-[0.26em] ${className}`}>
      {children}
    </p>
  );
}

export const primaryButtonClass =
  "inline-flex items-center justify-center gap-2 rounded-full bg-gradient-to-r from-brand-gold to-brand-orange px-7 py-3.5 text-[15px] font-semibold text-brand-ink shadow-pill transition duration-200 hover:-translate-y-0.5 hover:brightness-105 focus-visible:outline focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-brand-gold";

export const lightOutlineButtonClass =
  "inline-flex items-center justify-center gap-2 rounded-full border border-white/45 bg-white/10 px-7 py-3.5 text-[15px] font-semibold text-white backdrop-blur-sm transition duration-200 hover:bg-white/20 focus-visible:outline focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-white";

export const deepButtonClass =
  "inline-flex items-center justify-center gap-2 rounded-full bg-brand-deep px-6 py-3 text-[15px] font-semibold text-white transition duration-200 hover:bg-brand-green focus-visible:outline focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-brand-deep";

export const softOutlineButtonClass =
  "inline-flex items-center justify-center gap-2 rounded-full border border-brand-deep/15 bg-white px-5 py-2.5 text-sm font-semibold text-brand-deep transition duration-200 hover:border-brand-deep hover:bg-brand-deep hover:text-white focus-visible:outline focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-brand-deep";

export function ArrowRightIcon({ className = "h-4 w-4" }: { className?: string }) {
  return (
    <svg viewBox="0 0 16 16" className={className} fill="none" aria-hidden="true">
      <path
        d="M2.5 8h11m0 0L9.5 4m4 4-4 4"
        stroke="currentColor"
        strokeWidth="1.6"
        strokeLinecap="round"
        strokeLinejoin="round"
      />
    </svg>
  );
}

export function PinIcon({ className = "h-4 w-4" }: { className?: string }) {
  return (
    <svg viewBox="0 0 16 16" className={className} fill="none" aria-hidden="true">
      <path
        d="M8 14.5s5-4.1 5-8a5 5 0 1 0-10 0c0 3.9 5 8 5 8Z"
        stroke="currentColor"
        strokeWidth="1.4"
        strokeLinejoin="round"
      />
      <circle cx="8" cy="6.4" r="1.8" stroke="currentColor" strokeWidth="1.4" />
    </svg>
  );
}

export function CheckIcon({ className = "h-4 w-4" }: { className?: string }) {
  return (
    <svg viewBox="0 0 16 16" className={className} fill="none" aria-hidden="true">
      <path
        d="m3.5 8.4 3 3 6-6.8"
        stroke="currentColor"
        strokeWidth="1.9"
        strokeLinecap="round"
        strokeLinejoin="round"
      />
    </svg>
  );
}

export function DownArrowIcon({ className = "h-4 w-4" }: { className?: string }) {
  return (
    <svg viewBox="0 0 16 16" className={className} fill="none" aria-hidden="true">
      <path
        d="M8 2.5v11m0 0 4.2-4.2M8 13.5 3.8 9.3"
        stroke="currentColor"
        strokeWidth="1.5"
        strokeLinecap="round"
        strokeLinejoin="round"
      />
    </svg>
  );
}

/** Subtle crop-row pattern used as agricultural texture. */
export function FieldPattern({ className = "" }: { className?: string }) {
  const patternId = `harvest-rows-${useId()}`;

  return (
    <svg className={className} viewBox="0 0 480 480" preserveAspectRatio="xMidYMid slice" aria-hidden="true">
      <defs>
        <pattern id={patternId} width="48" height="48" patternUnits="userSpaceOnUse" patternTransform="rotate(24)">
          <path d="M0 16h48" stroke="#D8A83E" strokeWidth="1.1" />
          <path d="M0 24h48" stroke="#D8A83E" strokeWidth="0.5" strokeDasharray="3 6" />
          <path d="M0 34h48" stroke="#D8A83E" strokeWidth="0.8" strokeOpacity="0.7" />
        </pattern>
      </defs>
      <rect width="480" height="480" fill={`url(#${patternId})`} />
    </svg>
  );
}
