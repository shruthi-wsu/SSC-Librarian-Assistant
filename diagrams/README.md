# Deliverable II — UML Diagrams

All diagrams are in **Draw.io format** (`.drawio`). Open at [app.diagrams.net](https://app.diagrams.net) or import into Lucidchart.

## Files

| File | Diagram | Traces to |
|------|---------|-----------|
| `01_architecture_overview.drawio` | High-Level Architecture (Client-Server + Layered) | All FRs |
| `02_component_user_management.drawio` | UserManagement Component | FR-1 |
| `03_component_book_catalog.drawio` | BookCatalog Component | FR-2 |
| `04_component_circulation.drawio` | CirculationManagement Component | FR-4, FR-5 |
| `05_component_hold_management.drawio` | HoldManagement Component | FR-3 |
| `06_component_frontend_ui.drawio` | Frontend UI Component (React SPA) | FR-1 through FR-6 |

## How to Export PNG for PDF

1. Open a `.drawio` file at [app.diagrams.net](https://app.diagrams.net)
2. **File → Export As → PNG** (set scale to 2x for high resolution)
3. Save to `diagrams/exports/`
4. Insert all PNGs into Google Docs or Word for PDF assembly

## PDF Document Order

1. Title Page
2. `01_architecture_overview` — with pattern justification (Client-Server + Layered/MVC)
3. Component diagrams `02` through `06` — each with design notes
4. Traceability matrix
5. Design reasoning & trade-offs
6. Quality alignment table
