import Spinner from "./Spinner";

interface LoadingStateProps {
  label?: string;
}

/** Full-area loading placeholder for pages and panels. */
export function LoadingState({ label = "Loading…" }: LoadingStateProps) {
  return (
    <div className="flex flex-col items-center justify-center gap-3 rounded-xl border border-slate-200 bg-white py-14 text-slate-500">
      <Spinner size="lg" label={label} />
      <p className="text-sm">{label}</p>
    </div>
  );
}

export default LoadingState;
