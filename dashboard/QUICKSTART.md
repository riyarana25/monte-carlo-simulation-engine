# Quick Start Guide

Get the Monte Carlo Dashboard running in 5 minutes.

## 1. Install & Run (Local)

```bash
# Navigate to the dashboard directory
cd monte-carlo-dashboard

# Install dependencies
npm install

# Start development server
npm run dev
```

Open: `http://localhost:3000`

## 2. Run the Backend API

In a **separate terminal**:

```bash
# Navigate to the engine directory
cd ../monte-carlo-simulation-engine

# Start Spring Boot
mvn spring-boot:run
```

API runs on: `http://localhost:8080`

## 3. Use the Dashboard

1. Go to `http://localhost:3000`
2. Click "Launch Dashboard"
3. Adjust parameters:
   - Domain: Pick one (Buffon, Portfolio VaR, etc.)
   - Samples: 10,000 (default, try 100,000 for accuracy)
   - Replications: 100 (default)
   - Threads: 4 (for parallelization)
   - Seed: 42 (for reproducibility)
4. Click "▶ Run Simulation"
5. Results appear instantly!

## 4. Download Results

- **JSON:** Click "📥 Download JSON" to save raw results
- **CSV:** Click "📊 Download CSV" for spreadsheet

## Common Tasks

### Change API URL
Edit `.env.local`:
```
NEXT_PUBLIC_API_URL=http://localhost:8080
```

### Build for Production
```bash
npm run build
npm start
```

### Deploy to Vercel
```bash
# Make sure your code is on GitHub
git push origin main

# Go to vercel.com → Import Project
# Select this repo → Deploy
```

### Deploy Backend to Render
See `DEPLOYMENT.md` for full instructions.

## File Structure

```
monte-carlo-dashboard/
├── pages/              # Pages (index, dashboard)
├── components/         # React components
├── styles/            # CSS
├── .env.example       # Copy to .env.local
├── README.md          # Full documentation
└── DEPLOYMENT.md      # Deploy guide
```

## Troubleshooting

| Issue | Solution |
|-------|----------|
| "Failed to run simulation" | Make sure `mvn spring-boot:run` is running in another terminal |
| Port 3000 already in use | `npm run dev -- -p 3001` |
| Changes not showing | Hard refresh: `Ctrl+Shift+R` |

## Next: Deploy Live

When ready for production:
1. Read `DEPLOYMENT.md`
2. Deploy backend to Render (5 min)
3. Deploy frontend to Vercel (5 min)
4. Share live URL on GitHub profile!

---

**Need help?** Check README.md or DEPLOYMENT.md for detailed guides.
