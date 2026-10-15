import { useCallback, useState } from "react";

export interface MarketplaceFilters {
  location?: string;
  minPrice?: number;
  maxPrice?: number;
  quality?: string;
  availability?: "IN_STOCK" | "PRE_ORDER";
}

/**
 * Active filter set for the marketplace grid.
 *
 * TODO: serialise filters into URL search params so results are shareable.
 */
export function useMarketplaceFilters() {
  const [filters, setFilters] = useState<MarketplaceFilters>({});

  const updateFilter = useCallback(<K extends keyof MarketplaceFilters>(key: K, value: MarketplaceFilters[K]) => {
    setFilters((current) => ({ ...current, [key]: value }));
  }, []);

  const clearFilters = useCallback(() => setFilters({}), []);

  return { filters, updateFilter, clearFilters };
}

export default useMarketplaceFilters;
