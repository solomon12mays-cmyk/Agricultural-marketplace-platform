import { useUiStore } from "../app/store/uiStore";

/** Toast helpers bound to the UI store. */
export function useToast() {
  const pushToast = useUiStore((state) => state.pushToast);

  return {
    show: (text: string) => pushToast("info", text),
    success: (text: string) => pushToast("success", text),
    warning: (text: string) => pushToast("warning", text),
    error: (text: string) => pushToast("error", text),
  };
}

export default useToast;
