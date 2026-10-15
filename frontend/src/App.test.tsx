import { cleanup, render, screen } from "@testing-library/react";
import { afterEach, describe, expect, it } from "vitest";
import App from "./App";

describe("application shell", () => {
  afterEach(() => {
    cleanup();
    window.history.replaceState(null, "", "/");
  });

  it("renders the public marketplace at the root path", async () => {
    render(<App />);

    expect(
      await screen.findByRole("heading", { name: /marketplace/i }),
    ).toBeInTheDocument();
    expect(screen.getByRole("link", { name: /agricultural marketplace/i })).toBeInTheDocument();
  });

  it("shows the not-found screen for unknown routes", async () => {
    window.history.replaceState(null, "", "/definitely-not-a-route");
    render(<App />);

    expect(await screen.findByRole("heading", { name: /page not found/i })).toBeInTheDocument();
  });
});
