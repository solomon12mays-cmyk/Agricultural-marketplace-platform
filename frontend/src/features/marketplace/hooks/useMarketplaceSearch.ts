import { useCallback, useState } from "react";
import { marketplaceApi } from "../../../api/endpoints/marketplaceApi";
import { useDebounce } from "../../../hooks/useDebounce";

interface SearchState {
  keyword: string;
  region?: string;
  category?: string;
}

/**
 * Search state for the marketplace, debounced for query execution.
 *
 * TODO: fetch results from `marketplaceApi` whenever the debounced query
 * changes and expose loading/error status.
 */
export function useMarketplaceSearch(initial: SearchState = { keyword: "" }) {
  const [query, setQuery] = useState<SearchState>(initial);
  const debouncedKeyword = useDebounce(query.keyword, 350);

  const setKeyword = useCallback((keyword: string) => {
    setQuery((current) => ({ ...current, keyword }));
  }, []);

  const setRegion = useCallback((region?: string) => {
    setQuery((current) => ({ ...current, region }));
  }, []);

  const setCategory = useCallback((category?: string) => {
    setQuery((current) => ({ ...current, category }));
  }, []);

  return {
    query,
    debouncedKeyword,
    setKeyword,
    setRegion,
    setCategory,
    basePath: marketplaceApi.basePath,
  };
}

export default useMarketplaceSearch;
