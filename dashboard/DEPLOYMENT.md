# Deployment Guide

Complete guide to deploying the Monte Carlo Dashboard and Backend to production.

## Architecture

```
┌──────────────────────────────────────────────────────────────┐
│                     Your Users                               │
└──────────────────┬───────────────────────────────────────────┘
                   │
                   ▼
        ┌──────────────────────┐
        │  Vercel (Frontend)   │
        │  monte-carlo-dash    │
        │  .vercel.app         │
        └──────────┬───────────┘
                   │ HTTPS
                   ▼
        ┌──────────────────────────┐
        │  Render (Backend)        │
        │  monte-carlo-api         │
        │  .onrender.com           │
        └──────────────────────────┘
```

## Step 1: Deploy Backend to Render

### 1.1 Create Render Account
- Go to [render.com](https://render.com)
- Sign up with GitHub

### 1.2 Create Web Service
1. Click "New +" → "Web Service"
2. Connect GitHub account
3. Select `monte-carlo-simulation-engine` repository
4. Fill in details:
   - **Name:** `monte-carlo-api` (or your choice)
   - **Environment:** `Java`
   - **Build Command:** `mvn clean package`
   - **Start Command:** `java -jar target/mcengine-0.0.1-SNAPSHOT.jar`
   - **Instance Type:** `Free` (for portfolio)

### 1.3 Environment Variables (Optional)
```
SERVER_PORT=8080
```

### 1.4 Deploy
- Click "Create Web Service"
- Wait for build & deployment (~5-10 min)
- You'll get a URL like: `https://monte-carlo-api.onrender.com`

**Keep this URL — you'll need it for the frontend.**

## Step 2: Deploy Frontend to Vercel

### 2.1 Create Vercel Account
- Go to [vercel.com](https://vercel.com)
- Sign up with GitHub

### 2.2 Import Project
1. Click "Import Project"
2. Select GitHub → `monte-carlo-dashboard`
3. Fill in Project Name: `monte-carlo-dashboard`

### 2.3 Environment Variables
Add the backend URL:
```
NEXT_PUBLIC_API_URL=https://monte-carlo-api.onrender.com
```

### 2.4 Deploy
- Click "Deploy"
- Wait for build (~2 min)
- You'll get a URL like: `https://monte-carlo-dashboard.vercel.app`

## Step 3: Verify Everything Works

### Test the Frontend
1. Open `https://monte-carlo-dashboard.vercel.app`
2. Click "Launch Dashboard"
3. Run a simulation
4. Check that results appear

### Test the API Directly
```bash
curl -X POST https://monte-carlo-api.onrender.com/api/v1/simulate \
  -H "Content-Type: application/json" \
  -d '{"domain":"buffon","samples":5000,"replications":10,"threads":1,"seed":42}'
```

## Troubleshooting

### "Failed to run simulation" in Dashboard
**Problem:** Frontend can't reach backend  
**Solution:**
1. Check `NEXT_PUBLIC_API_URL` in Vercel settings
2. Verify backend is running on Render (check logs)
3. Ensure backend has public URL (not private)

### Backend taking too long to start
**Problem:** Render's free tier spins down after 15 min inactivity  
**Solution:** 
- First request takes ~30 sec (cold start)
- Subsequent requests are fast
- For production, upgrade to paid tier

### CORS errors
**Problem:** Frontend blocked by CORS policy  
**Solution:**
- Backend already has CORS enabled (Spring Boot default)
- If issues persist, add to Spring Boot:
```java
@Configuration
public class CorsConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedOrigins("*");
    }
}
```

## Production Checklist

- [ ] Backend deployed to Render with public URL
- [ ] Frontend deployed to Vercel
- [ ] `NEXT_PUBLIC_API_URL` set in Vercel
- [ ] Test simulation works end-to-end
- [ ] Custom domain configured (optional)
- [ ] Link on GitHub profile updated
- [ ] Share with recruiters! 🚀

## Cost Estimation

| Service | Free Tier | Cost |
|---------|-----------|------|
| Vercel | Yes | $0/month |
| Render | Yes (with limits) | $0/month |
| **Total** | | **$0/month** |

## Custom Domain (Optional)

### Add Domain to Vercel
1. In Vercel project settings
2. Add Domain
3. Follow DNS setup instructions

### Example
Instead of `monte-carlo-dashboard.vercel.app`, use:
- `monte-carlo.yourname.com` (if you own the domain)

## Performance Optimization

### Frontend (Vercel)
- Automatic image optimization
- Code splitting
- CDN caching

### Backend (Render)
- For better performance, upgrade from free tier
- Free tier: Cold start ~30s
- Paid tier: Always-on, <100ms start time

## Monitoring

### Vercel Dashboard
- View logs: Project → Deployments → Details
- Check build history

### Render Dashboard
- View logs: Service → Logs
- Check resource usage

## Next Steps

1. Share your dashboard URL on:
   - GitHub profile
   - Resume/CV
   - LinkedIn
   - Interview portfolio

2. Example share text:
   > "Built a Monte Carlo simulation engine with 5× parallel speedup and an interactive dashboard. Deployed end-to-end with REST API and live at: https://monte-carlo-dashboard.vercel.app"

## Support

For issues:
1. Check Vercel/Render logs
2. Test API directly with curl
3. Check browser console (F12)
4. Read error messages carefully

Good luck! 🚀
