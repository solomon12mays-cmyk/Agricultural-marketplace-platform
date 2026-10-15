import { isAxiosError, type AxiosError, type AxiosInstance } from "axios";
import { useAuthStore } from "../app/store/authStore";

/** Attaches the bearer token (and correlation headers) to every request. */
export function registerRequestInterceptor(instance: AxiosInstance): void {
  instance.interceptors.request.use((config) => {
    const tokens = useAuthStore.getState().tokens;
    if (tokens?.accessToken) {
      config.headers.Authorization = `${tokens.tokenType ?? "Bearer"} ${tokens.accessToken}`;
    }
    return config;
  });
}

/** Clears credentials on 401 responses. */
export function registerResponseInterceptor(instance: AxiosInstance): void {
  instance.interceptors.response.use(
    (response) => response,
    (error: AxiosError) => {
      if (isAxiosError(error) && error.response?.status === 401) {
        useAuthStore.getState().clearAuth();
      }
      return Promise.reject(error);
    },
  );
}
