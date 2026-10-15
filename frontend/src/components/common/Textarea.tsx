import { useId, type TextareaHTMLAttributes } from "react";

interface TextareaProps extends TextareaHTMLAttributes<HTMLTextAreaElement> {
  label?: string;
  error?: string;
  hint?: string;
}

export function Textarea({ label, error, hint, id, className = "", ...rest }: TextareaProps) {
  const generatedId = useId();
  const fieldId = id ?? generatedId;

  return (
    <div className="w-full">
      {label ? (
        <label htmlFor={fieldId} className="mb-1.5 block text-sm font-medium text-slate-700">
          {label}
        </label>
      ) : null}
      <textarea
        id={fieldId}
        aria-invalid={error ? true : undefined}
        className={`w-full rounded-lg border bg-white px-3.5 py-2.5 text-sm outline-none transition focus:border-brand-green focus:ring-2 focus:ring-brand-green/20 ${
          error ? "border-red-400" : "border-slate-300"
        } ${className}`}
        {...rest}
      />
      {error ? (
        <p className="mt-1.5 text-xs text-red-600">{error}</p>
      ) : hint ? (
        <p className="mt-1.5 text-xs text-slate-500">{hint}</p>
      ) : null}
    </div>
  );
}

export default Textarea;
