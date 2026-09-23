# Color Votes

The "Colors / Votes" code test built in **Java 21 + Spring Boot** instead of PHP, with an
embedded **H2** database in place of MySQL.

- The left column comes from the `colors` table, rendered on the server.
- Clicking a color makes an Ajax call (`fetch`) to `GET /api/colors/{id}/votes`. That call
  returns the color's votes summed across all cities, and the page shows the number in the
  right column.
- Clicking **TOTAL** adds up the numbers currently shown, using only JavaScript. There is no
  server call.

## Why H2?

The data is small and read-only. An embedded database means the app runs as a single jar with
no setup and deploys to any free container host. The schema and seed data are ordinary
Flyway migrations (`src/main/resources/db/migration`) written in portable SQL. To switch to
PostgreSQL or MySQL, add the JDBC driver and set `DATABASE_URL`, `DATABASE_USERNAME` and
`DATABASE_PASSWORD`.

## Schema

```
colors(id PK, name UNIQUE, sort_order)
votes (id PK, city, color_id FK -> colors.id, votes >= 0, UNIQUE(city, color_id))
```

`votes` references `colors` by a foreign key instead of repeating the color name. Colors with no
votes (Orange, Green, Indigo) still show `0` because the query uses a `LEFT JOIN`.

## Run locally

Requires JDK 21 and Maven.

```bash
mvn test            # repository + web-layer tests
mvn spring-boot:run # http://localhost:8080
```

Or with Docker:

```bash
docker build -t color-votes .
docker run -p 8080:8080 color-votes
```

## Deploy

Any host that runs Docker works. The server listens on `$PORT`. `render.yaml` sets up a
free Render web service: push this repo to GitHub, then in Render choose
**New → Blueprint** and select the repo. The health check is at `/actuator/health`.

## Layout

| Path | Purpose |
|---|---|
| `ColorRepository` | SQL via Spring `JdbcClient` |
| `PageController` | Renders the page (`templates/index.html`) |
| `ColorApiController` | JSON endpoint; returns 404 for an unknown color and 400 for a non-numeric id |
| `static/app.js` | Ajax loading, loading and error states, client-side total |
