import { useState } from "react";

/** Open/close state for a single modal or drawer. */
export function useModal(initialOpen = false) {
  const [open, setOpen] = useState(initialOpen);

  function open_() {
    setOpen(true);
  }

  function close() {
    setOpen(false);
  }

  function toggle() {
    setOpen((current) => !current);
  }

  return { open, openModal: open_, closeModal: close, toggle };
}

export default useModal;
