# Frontend — Agricultural Marketplace Platform

React 19 + TypeScript + Vite + Tailwind CSS single-page application.

## Layout

```
src/
├── main.tsx            entry point
├── App.tsx             application shell (providers + router + initializer)
├── index.css           Tailwind entry + theme imports
├── app/
│   ├── AppRouter.tsx   route table rendering
│   ├── AppProviders.tsx router/store providers
│   ├── AppInitializer.tsx startup bootstrap
│   ├── store/          zustand stores (auth, notification, ui)
│   └── config/         app/api/routes configuration
├── api/                axios client, interceptors, error handling
│   └── endpoints/      one module per backend domain
├── components/
│   ├── common/         Button, Input, Modal, DataTable, ...
│   ├── layout/         AppLayout, Sidebar, Header, ...
│   ├── navigation/     role-specific navigation definitions
│   └── feedback/       Toast, Alert, NotificationPanel
├── features/           one folder per bounded context (pages, components,
│                       hooks, services, schemas, types, utils)
├── layouts/            route group layouts per role
├── routes/             route table, guards (Protected/Role/Permission)
├── hooks/              shared hooks (useAuth, usePagination, ...)
├── types/              shared TypeScript contracts
├── schemas/            zod validation schemas
├── utils/              formatting, validation, storage helpers
├── constants/          roles, permissions, state machines labels
└── styles/             CSS variables and component classes
tests/                  feature test folders
public/                 static assets (logo, icons, images)
```

## Commands

```bash
npm ci
npm run dev        # dev server with /api proxy to localhost:8081
npm run build      # typecheck (tsc -b) + production build
npm test -- --run  # vitest suite
npm run lint       # eslint
```

## Environment

Copy `.env.example` to `.env` and adjust `VITE_API_BASE_URL` if the API is
not served through the dev proxy.

## Status

The structure follows the platform's bounded contexts. Pages, services and
API endpoint modules are compilable skeletons with TODO bodies; shared
primitives (components, hooks, utils, routing, stores, api client) are
implemented.
