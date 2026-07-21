# Monte Carlo Simulation Dashboard

Beautiful, interactive web dashboard for the Monte Carlo Simulation Engine. Built with Next.js, React, and Tailwind CSS.

## Features

- 🎨 **Clean, Modern UI** — Built with Tailwind CSS and responsive design
- ⚡ **Real-time Results** — Run simulations and see results instantly
- 📊 **Full Metrics Display** — Bias, MSE, variance reduction, correlation
- 💾 **Export Results** — Download as JSON or CSV
- 🔗 **API Integration** — Connects to Spring Boot REST API
- 📱 **Responsive** — Works on desktop, tablet, and mobile

## Quick Start

### Prerequisites
- Node.js 18+ and npm
- Monte Carlo Spring Boot API running on `http://localhost:8080`

### Local Development

1. **Install dependencies:**
```bash
npm install
```

2. **Set up environment:**
```bash
cp .env.example .env.local
# Edit .env.local if your API is on a different URL
```

3. **Start development server:**
```bash
npm run dev
```

4. **Open in browser:**
```
http://localhost:3000
```

## Project Structure

```
monte-carlo-dashboard/
├── pages/
│   ├── index.tsx              # Home page
│   ├── dashboard.tsx          # Main dashboard
│   └── _app.tsx              # Next.js app wrapper
├── components/
│   ├── SimulationForm.tsx     # Parameter input form
│   ├── ResultsDisplay.tsx     # Results visualization
│   └── MetricsTable.tsx       # Results table
├── styles/
│   └── globals.css            # Tailwind CSS
├── public/                    # Static assets
└── package.json              # Dependencies
```

## Available Domains

1. **Buffon's Needle** — Estimate π using geometric probability
2. **Portfolio VaR** — 95% Value-at-Risk for correlated portfolio
3. **Barrier Option** — Down-and-out call pricing
4. **Rare Event** — P(Z > 5σ) estimation

## Configuration

### Environment Variables

**Local development (`.env.local`):**
```
NEXT_PUBLIC_API_URL=http://localhost:8080
```

**Production Render (`.env.production.local`):**
```
NEXT_PUBLIC_API_URL=https://your-render-backend.onrender.com
```

## Building

### Production Build
```bash
npm run build
npm start
```

## Deployment

### Deploy to Vercel (Recommended for Frontend)

1. Push to GitHub
2. Go to [vercel.com](https://vercel.com)
3. Import this repository
4. Set environment variable:
   ```
   NEXT_PUBLIC_API_URL=https://your-render-backend.onrender.com
   ```
5. Deploy (automatic on push)

### Deploy Backend (Spring Boot) to Render

1. Go to [render.com](https://render.com)
2. Create New → Web Service
3. Connect to `monte-carlo-simulation-engine` repo
4. Build: `mvn clean package`
5. Start: `java -jar target/mcengine-0.0.1-SNAPSHOT.jar`
6. Deploy

## API Integration

The dashboard calls the Spring Boot REST API at:
```
POST /api/v1/simulate
```

**Request:**
```json
{
  "domain": "buffon|portfolio_var|barrier_option|rare_event",
  "samples": 10000,
  "replications": 100,
  "threads": 4,
  "seed": 42
}
```

**Response:**
```json
[{
  "domain": "Buffon's Needle",
  "estimator": "Naive",
  "pointEstimate": 3.143606,
  "standardError": 0.004392,
  "bias": 0.002014,
  "mse": 0.001933,
  "ciLower": 3.134997,
  "ciUpper": 3.152215,
  "containsTruth": true,
  "computeTimeMs": 245
}]
```

## Performance Tips

- **Fewer samples** = faster results but less accurate
- **More threads** = faster (if available)
- **Same seed** = reproducible results

## Troubleshooting

### "Failed to run simulation"
- Make sure the Spring Boot API is running on `http://localhost:8080`
- Check the API URL in `.env.local`
- Verify the API is responding: `curl http://localhost:8080/api/v1/simulate/health`

### Port already in use
```bash
# Change Next.js port
npm run dev -- -p 3001
```

## Stack

- **Frontend Framework:** Next.js 14
- **UI Library:** React 18
- **Styling:** Tailwind CSS 3
- **HTTP Client:** Axios
- **Charts:** Recharts (optional, can be added)
- **Language:** TypeScript

## License

MIT

## Author

Riya Rana
- Email: riyarana0125@gmail.com
- GitHub: [riyarana25](https://github.com/riyarana25)
- LinkedIn: [linkedin.com/in/riya-rana-55229821a](https://www.linkedin.com/in/riya-rana-55229821a)
