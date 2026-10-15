interface PaginationProps {
  page: number;
  totalPages: number;
  onPageChange: (page: number) => void;
  disabled?: boolean;
}

/** Zero-based pagination control. */
export function Pagination({ page, totalPages, onPageChange, disabled = false }: PaginationProps) {
  if (totalPages <= 1) {
    return null;
  }

  return (
    <nav aria-label="Pagination" className="flex items-center justify-between gap-3 text-sm">
      <button
        type="button"
        disabled={disabled || page <= 0}
        onClick={() => onPageChange(page - 1)}
        className="rounded-lg border border-slate-300 px-3 py-1.5 font-medium text-slate-600 disabled:opacity-50"
      >
        Previous
      </button>
      <span className="text-slate-500">
        Page {page + 1} of {totalPages}
      </span>
      <button
        type="button"
        disabled={disabled || page + 1 >= totalPages}
        onClick={() => onPageChange(page + 1)}
        className="rounded-lg border border-slate-300 px-3 py-1.5 font-medium text-slate-600 disabled:opacity-50"
      >
        Next
      </button>
    </nav>
  );
}

export default Pagination;
