import React from 'react';
import Link from 'next/link';

export default function Home() {
  return (
    <div className="min-h-screen bg-gradient-to-br from-slate-900 to-slate-800">
      <div className="max-w-4xl mx-auto px-6 py-20">
        {/* Header */}
        <div className="text-center mb-16">
          <h1 className="text-5xl font-bold text-white mb-4">
            Monte Carlo Simulation Engine
          </h1>
          <p className="text-xl text-slate-300 mb-8">
            Interactive dashboard for variance reduction and empirical validation
          </p>
          <Link
            href="/dashboard"
            className="inline-block bg-blue-600 hover:bg-blue-700 text-white font-bold py-3 px-8 rounded-lg transition"
          >
            Launch Dashboard →
          </Link>
        </div>

        {/* Features Grid */}
        <div className="grid grid-cols-1 md:grid-cols-2 gap-6 mb-12">
          <div className="bg-slate-700 p-6 rounded-lg">
            <h3 className="text-xl font-bold text-white mb-2">📊 4 Domains</h3>
            <p className="text-slate-300">
              Buffon's Needle, Portfolio VaR, Barrier Options, Rare Events
            </p>
          </div>

          <div className="bg-slate-700 p-6 rounded-lg">
            <h3 className="text-xl font-bold text-white mb-2">⚡ 5× Speedup</h3>
            <p className="text-slate-300">
              Parallel execution with deterministic stream-based RNG
            </p>
          </div>

          <div className="bg-slate-700 p-6 rounded-lg">
            <h3 className="text-xl font-bold text-white mb-2">📈 Full Metrics</h3>
            <p className="text-slate-300">
              Bias, MSE, variance reduction, correlation, confidence intervals
            </p>
          </div>

          <div className="bg-slate-700 p-6 rounded-lg">
            <h3 className="text-xl font-bold text-white mb-2">🔬 Validated</h3>
            <p className="text-slate-300">
              Empirical validation against analytical ground truth
            </p>
          </div>
        </div>

        {/* Footer Links */}
        <div className="border-t border-slate-600 pt-8 text-center text-slate-400">
          <p className="mb-4">Built with Next.js + React + Recharts</p>
          <div className="flex justify-center gap-6">
            <a
              href="https://github.com/riyarana25/monte-carlo-simulation-engine"
              className="hover:text-white transition"
            >
              GitHub
            </a>
            <a href="https://linkedin.com/in/riya-rana-55229821a" className="hover:text-white transition">
              LinkedIn
            </a>
            <a href="mailto:riyarana0125@gmail.com" className="hover:text-white transition">
              Contact
            </a>
          </div>
        </div>
      </div>
    </div>
  );
}
