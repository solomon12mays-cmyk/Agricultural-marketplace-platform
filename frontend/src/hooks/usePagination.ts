import { useState } from "react";

/** Zero-based pagination state shared by list screens. */
export function usePagination(initialSize = 20, initialTotal = 0) {
  const [page, setPage] = useState(0);
  const [size, setSize] = useState(initialSize);
  const [totalElements, setTotalElements] = useState(initialTotal);

  const totalPages = size > 0 ? Math.ceil(totalElements / size) : 0;

  function next() {
    setPage((current) => Math.min(current + 1, Math.max(0, totalPages - 1)));
  }

  function previous() {
    setPage((current) => Math.max(current - 1, 0));
  }

  function reset() {
    setPage(0);
  }

  return {
    page,
    size,
    totalElements,
    totalPages,
    setPage,
    setSize,
    setTotalElements,
    next,
    previous,
    reset,
  };
}

export default usePagination;
