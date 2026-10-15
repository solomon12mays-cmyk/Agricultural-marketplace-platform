/** Envelope returned by successful API responses. */
export interface ApiResponse<T> {
  data: T;
  metadata?: {
    timestamp?: string;
    correlationId?: string;
  };
}

/** Error payload returned by the backend's global exception handler. */
export interface ApiErrorShape {
  code: string;
  message: string;
  fieldErrors?: Record<string, string>;
  timestamp?: string;
}

/** Page slice returned by list endpoints. */
export interface PageResponse<T> {
  content: T[];
  page: number;
  size: number;
  totalElements: number;
  totalPages: number;
  last: boolean;
}

export interface ListQueryParams {
  page?: number;
  size?: number;
  sort?: string;
  q?: string;
}
