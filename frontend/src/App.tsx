import AppInitializer from "./app/AppInitializer";
import AppProviders from "./app/AppProviders";
import AppRouter from "./app/AppRouter";

export default function App() {
  return (
    <AppProviders>
      <AppInitializer>
        <AppRouter />
      </AppInitializer>
    </AppProviders>
  );
}
