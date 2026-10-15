import Button from "./Button";

interface ErrorStateProps {
  message: string;
  onRetry?: () => void;
}

/** Shown when loading or an action fails. */
export function ErrorState({ message, onRetry }: ErrorStateProps) {
  return (
    <div
      role="alert"
      className="flex flex-col items-center justify-center gap-3 rounded-xl border border-red-200 bg-red-50 py-10 text-center"
    >
      <p className="text-sm font-medium text-red-700">{message}</p>
      {onRetry ? (
        <Button variant="secondary" size="sm" onClick={onRetry}>
          Try again
        </Button>
      ) : null}
    </div>
  );
}

export default ErrorState;
