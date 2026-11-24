# Portfolio Project Data — Grosir Sembako Jaya Makmur POS

- **Category:** `desktop-mobile` (`Desktop App`)
- **Category Label:** `Desktop App`
- **Accent Color:** `amber`
- **Target Assets Folder:** `public/images/projects/grosir_sembako_jaya_makmur/`
- **Required Local Images:** `banner.webp`, `image01.webp`, `image02.webp`, `image03.webp`, `image04.webp`, `image05.webp`

## TypeScript Data (`ProjectCaseStudy`)

```typescript
{
  "id": "grosir-sembako-jaya-makmur-pos",
  "code": "PRO-11 // APP-07",
  "title": "Grosir Sembako Jaya Makmur POS",
  "category": "desktop-mobile",
  "categoryLabel": "Desktop App",
  "year": "2022",
  "status": "Completed",
  "accentColor": "amber",
  "repoUrl": "https://github.com/weztcy/Grosir-Sembako-Jaya-Makmur-POS",
  "license": "MIT License",
  "banner": "/images/projects/grosir_sembako_jaya_makmur/banner.webp",
  "summary": "Desktop Java Swing POS for wholesale grocery retail featuring tiered markup pricing, 11% VAT, credit interest, souvenirs, and JSON storage.",
  "projectDescription": "A Java Swing and FlatLaf desktop point-of-sale application tailored for wholesale grocery operations. It automates five-tier capital markup pricing, 11% VAT, credit tempo interest, cash souvenir rewards, stock tracking, CSV reporting, and local JSON persistence.",
  "architectureSummary": "MainFrame Tabs (Swing + FlatLaf) -> PricingCalculator Engine -> Synchronized DataRepository Singleton -> Gson UTF-8 JSON Storage",
  "techStack": [
    "Java",
    "Java Swing",
    "Java AWT",
    "FlatLaf",
    "Gson",
    "JSON",
    "PowerShell",
    "Windows Batch"
  ],
  "telemetryMatrix": {
    "projectType": "Java Swing Desktop POS Application",
    "targetPlatform": "Windows Desktop (JVM)",
    "primaryDomain": "Wholesale Grocery Retail & Inventory",
    "architecturePipeline": "Swing UI -> Service -> Gson JSON",
    "testingAndAudit": "11/11 Logic Unit Tests Passed",
    "projectStatus": "Completed",
    "projectYear": "2022"
  },
  "desktopMobileProjectInfo": {
    "applicationType": "Native JVM Desktop POS Application",
    "targetPlatform": "Windows Desktop",
    "developmentStatus": "Completed",
    "developmentYear": "2022",
    "applicationCategory": "Wholesale Retail & Inventory Management"
  },
  "desktopMobileBackground": "Wholesale grocery counters frequently struggle with manual price calculations across varied capital tiers, inconsistent credit term interest enforcement, and untracked inventory deductions. Paper receipts and spreadsheets also slow down reconciliation, prompting a unified offline-first desktop POS system.",
  "desktopMobileProblemStatements": [
    {
      "title": "Error-Prone Tiered Margin Calculations",
      "description": "Manual markup computation across five wholesale capital brackets (from 10% for high-value bulk goods up to 50% for low-cost staples) leads to inconsistent pricing and margin leakage at checkout.",
      "issueCode": "ISSUE_01"
    },
    {
      "title": "Complex Tax, Credit Tempo, & Gift Rules",
      "description": "Applying flat 11% VAT, enforcing 50% credit interest only beyond two-week tempo periods, and evaluating cash-exclusive souvenir tiers manually slows counter throughput and causes billing disputes.",
      "issueCode": "ISSUE_02"
    },
    {
      "title": "Disconnected Inventory & Receipt Tracking",
      "description": "Without atomic stock updates during checkout and invoice cancellation, wholesale SKU counts drift out of sync while paper receipts lack structured digital history or CSV export.",
      "issueCode": "ISSUE_03"
    }
  ],
  "desktopMobileObjectives": [
    {
      "title": "Deterministic Business Pricing Engine",
      "description": "Centralize five-tier markup rounding, 11% VAT, credit interest thresholds, and cash souvenir rewards inside a pure, unit-tested PricingCalculator service.",
      "targetTag": "TARGET_01"
    },
    {
      "title": "Zero-Dependency Offline Persistence",
      "description": "Maintain product catalogs and historical invoices in local UTF-8 JSON files via a thread-safe Gson repository with automatic default data seeding.",
      "targetTag": "TARGET_02"
    },
    {
      "title": "Ergonomic Enterprise Desktop UI",
      "description": "Deliver a responsive multi-tabbed Java Swing interface styled with FlatLightLaf, custom Java2D vector icons, live KPI cards, and one-click receipt printing or CSV export.",
      "targetTag": "TARGET_03"
    }
  ],
  "desktopMobileFeatures": [
    {
      "title": "POS Cashier & Live Cart Checkout",
      "description": "Generates sequential daily invoice codes, enforces stock ceilings per SKU, computes real-time totals for cash or credit tempo payments, and displays next-tier souvenir upsell prompts.",
      "badge": "POS CHECKOUT"
    },
    {
      "title": "Master Product & Stock Alert Console",
      "description": "Tracks total SKU count, physical units, capital asset value, and low-stock warnings alongside CRUD modals featuring one-click tiered selling price recommendations.",
      "badge": "INVENTORY"
    },
    {
      "title": "Sales Analytics & Invoice Cancellation",
      "description": "Summarizes six financial KPIs, filters invoices by keyword, payment type, and settlement status, inspects itemized breakdowns, restores stock on cancellation, and exports CSV reports.",
      "badge": "REPORTING"
    },
    {
      "title": "Monospaced Receipt & OS Print Integration",
      "description": "Generates structured wholesale receipts upon checkout or from history with native Windows print dialog support and plain-text (.txt) file archiving.",
      "badge": "RECEIPT I/O"
    }
  ],
  "desktopMobileInterfaceShowcase": [
    {
      "imageUrl": "/images/projects/grosir_sembako_jaya_makmur/image01.webp"
    },
    {
      "imageUrl": "/images/projects/grosir_sembako_jaya_makmur/image02.webp"
    },
    {
      "imageUrl": "/images/projects/grosir_sembako_jaya_makmur/image03.webp"
    },
    {
      "imageUrl": "/images/projects/grosir_sembako_jaya_makmur/image04.webp"
    },
    {
      "imageUrl": "/images/projects/grosir_sembako_jaya_makmur/image05.webp"
    }
  ],
  "desktopMobileUserFlow": {
    "summary": "Cashiers launch the tabbed desktop workspace, select wholesale items with live stock validation, choose cash or credit tempo payment to trigger automatic tax, interest, and souvenir rules, finalize the transaction for persistent storage, and print or export receipts.",
    "steps": [
      {
        "step": "01",
        "title": "Launch Workspace & Load Local Catalog",
        "description": "MainFrame initializes FlatLightLaf theming on the Swing Event Dispatch Thread while DataRepository loads 24 products and transaction history from local JSON files.",
        "actorOrNode": "Swing EDT / DataRepository"
      },
      {
        "step": "02",
        "title": "Build Wholesale Cart & Validate Stock",
        "description": "Operator enters customer details, selects SKUs, and adds quantities to the cart while TransactionPanel prevents quantities from exceeding available inventory.",
        "actorOrNode": "Cashier / TransactionPanel"
      },
      {
        "step": "03",
        "title": "Compute Tax, Tempo Interest, & Souvenir",
        "description": "Selecting Cash or Credit Tempo updates due dates, 11% VAT, 50% interest for >2 weeks, cash souvenir eligibility, and real-time change or shortfall indicators.",
        "actorOrNode": "PricingCalculator Service"
      },
      {
        "step": "04",
        "title": "Commit Sale & Print or Export Output",
        "description": "Finalizing checkout atomically deducts product stock, persists the invoice to JSON, opens ReceiptDialog for printing or TXT export, and refreshes reporting KPIs.",
        "actorOrNode": "DataRepository / ReceiptDialog"
      }
    ]
  },
  "desktopMobileArchitecture": {
    "summary": "The application follows a modular three-tier desktop architecture separating FlatLaf-styled Swing presentation panels, a deterministic business-rule pricing engine, domain POJO models, and a synchronized singleton Gson repository backed by local UTF-8 JSON storage.",
    "layers": [
      {
        "name": "Presentation & Dialog Layer",
        "role": "Renders the four-tab desktop shell, KPI dashboards, split-pane tables, CRUD forms, and receipt dialogs using FlatLightLaf and custom Java2D vector glyphs.",
        "tech": "Java Swing & FlatLaf",
        "components": [
          "MainFrame",
          "TransactionPanel",
          "ProductPanel",
          "ReportPanel",
          "ProductDialog",
          "ReceiptDialog",
          "UIHelper",
          "VectorIcon"
        ]
      },
      {
        "name": "Business Logic Service Layer",
        "role": "Encapsulates stateless wholesale markup tier formulas, 11% VAT computation, credit tempo interest rules, and cash souvenir tier evaluation.",
        "tech": "Java Service",
        "components": [
          "PricingCalculator",
          "TestBusinessLogic"
        ]
      },
      {
        "name": "Domain & Persistence Layer",
        "role": "Defines wholesale domain entities and manages synchronized in-memory collections serialized to local UTF-8 JSON files with fallback default seeding.",
        "tech": "Gson & JSON",
        "components": [
          "DataRepository",
          "DataSeeder",
          "Product",
          "Transaction",
          "TransactionItem",
          "PaymentType"
        ]
      }
    ]
  },
  "desktopMobileDataManagement": {
    "storageType": "Local UTF-8 JSON Files via Gson",
    "stateEngine": "Synchronized Singleton In-Memory Lists",
    "offlineSyncStrategy": "100% Offline-First Auto-Commit Persistence",
    "cachingMechanism": "In-Memory Catalog & History Cache with On-Demand Tab Reload"
  },
  "desktopMobilePlatformImplementation": [
    {
      "title": "Swing Event Dispatch Thread & Live Clock Timer",
      "description": "Bootstraps FlatLightLaf UI defaults before instantiating MainFrame on SwingUtilities.invokeLater and runs a 1-second javax.swing.Timer for the live header timestamp.",
      "badge": "JVM THREADING"
    },
    {
      "title": "Resolution-Independent Java2D Vector Icons",
      "description": "Implements custom Icon glyphs in VectorIcon using Graphics2D anti-aliasing and Path2D geometry, preventing missing Unicode glyph boxes across Windows fonts.",
      "badge": "GRAPHICS2D"
    },
    {
      "title": "Native OS Print & File Chooser Integration",
      "description": "Connects ReceiptDialog and ReportPanel directly to Windows print pipelines via JTextArea.print() and local filesystem exports (.txt receipts and .csv reports) via JFileChooser.",
      "badge": "OS INTEGRATION"
    }
  ],
  "desktopMobileTechSpecs": {
    "programmingLanguage": "Java",
    "framework": "Java Swing & FlatLaf",
    "localStorage": "Gson JSON Storage"
  },
  "desktopMobilePlatformSpecs": {
    "targetPlatform": "Windows Desktop JVM",
    "packageFormat": "Compiled Bytecode & JARs",
    "targetArchitecture": "JVM x64 Runtime",
    "buildFootprint": "1.28 MB Compiled Bundle"
  }
}
```
