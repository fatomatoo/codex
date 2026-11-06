# Testing-Demo: Event-Anmeldung

Kleine Angular-22-Anwendung als Grundlage für den Vortrag **Testing-Pyramide vs. Testing-Trophy**.
Enthält bewusst noch **keine Tests**.

## Starten

Benötigt Node ≥ 24.15 (oder ≥ 22.22.3) und Java 25. Backend und Frontend in zwei Terminals:

```bash
cd backend && ./gradlew bootRun   # http://localhost:8080
```

```bash
npm install
npm start                         # http://localhost:4200, leitet /api per Proxy an :8080 weiter
```

## Seiten

| Route | Inhalt |
|---|---|
| `/register` | Anmeldeformular (Signal Forms): Pflichtfelder, E-Mail-Format, asynchrone Prüfung auf doppelte E-Mail, 1–5 Tickets, AGB-Checkbox, Live-Preisübersicht mit Mengenrabatt |
| `/registrations` | Liste aller Anmeldungen mit Suchfeld, Löschen sowie Lade-, Leer- und Fehlerzustand |

## Aufbau

```
src/app/
  core/            models, price.ts (reine Logik), registration.service.ts,
                   fake-backend.interceptor.ts (In-Memory-"Backend" unter /api)
  register/        register-page (Signal Forms), field-errors
  registrations/   registrations-page (httpResource)
backend/           Spring Boot 4 (Java 25, Gradle), JPA + PostgreSQL (docker-compose.yml)
  .../registration/domain/          Aggregat Registration, Value Objects, Preislogik, Repository-Interface
  .../registration/application/     RegistrationService (Use Cases, E-Mail-Eindeutigkeit)
  .../registration/infrastructure/  JPA-Repository, Converter, Seed-Daten
  .../registration/web/             Controller, DTOs, Fehlerbehandlung
proxy.conf.json    leitet /api an das Backend weiter
```

- **Preislogik** (`core/price.ts`): Standard 49 €, VIP 99 €, ab 3 Tickets 10 % Rabatt.
- **Backend-API** (`backend/`): PostgreSQL (`docker compose up -d postgres`); beim ersten Start werden zwei
  Beispiel-Anmeldungen angelegt (`app.seed-data=false` schaltet das ab).

  | Methode | Pfad | Antwort |
  |---|---|---|
  | GET | `/api/registrations` | Liste, neueste zuerst |
  | GET | `/api/registrations/exists?email=…` | `{ "taken": boolean }` |
  | POST | `/api/registrations` | 201, 400 bei ungültigen Daten, 409 bei doppelter E-Mail |
  | PUT | `/api/registrations/{id}` | aktualisierte Anmeldung, 404, 409 (E-Mail vergeben) |
  | DELETE | `/api/registrations/{id}` | 204, 404 |tran
