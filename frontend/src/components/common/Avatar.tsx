interface AvatarProps {
  name: string;
  src?: string;
  size?: "sm" | "md" | "lg";
}

const sizeClasses = {
  sm: "h-8 w-8 text-xs",
  md: "h-10 w-10 text-sm",
  lg: "h-14 w-14 text-lg",
} as const;

/** Initials avatar with optional image. */
export function Avatar({ name, src, size = "md" }: AvatarProps) {
  const initials = name
    .split(/\s+/)
    .filter(Boolean)
    .slice(0, 2)
    .map((part) => part[0]?.toUpperCase() ?? "")
    .join("");

  if (src) {
    return <img src={src} alt={name} className={`${sizeClasses[size]} rounded-full object-cover`} />;
  }

  return (
    <span
      aria-label={name}
      className={`${sizeClasses[size]} inline-flex items-center justify-center rounded-full bg-brand-green/15 font-semibold text-brand-deep`}
    >
      {initials}
    </span>
  );
}

export default Avatar;
