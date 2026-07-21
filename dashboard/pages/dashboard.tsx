import React, { useState } from 'react';
import axios from 'axios';
import ResultsDisplay from '../components/ResultsDisplay';
import SimulationForm from '../components/SimulationForm';

export interface SimulationResult {
  domain: string;
  estimator: string;
  pointEstimate: number;
  standardError: number;
  bias: number;
  mse: number;
  ciLower: number;
  ciUpper: number;
  containsTruth: boolean;
  varianceReduction?: number;
  correlation?: number;
  computeTimeMs: number;
}

export default function Dashboard() {
  const [results, setResults] = useState<SimulationResult[]>([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const handleSimulate = async (formData: any) => {
    setLoading(true);
    setError(null);
    setResults([]);

    try {
      const apiUrl = process.env.NEXT_PUBLIC_API_URL || 'http://localhost:8080';
      const response = await axios.post(`${apiUrl}/api/v1/simulate`, formData);
      setResults(response.data);
    } catch (err: any) {
      setError(
        err.response?.data?.message ||
        `Failed to run simulation. Make sure the API is running at ${process.env.NEXT_PUBLIC_API_URL || 'http://localhost:8080'}`
      );
      console.error('Simulation error:', err);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="min-h-screen bg-gradient-to-br from-slate-900 to-slate-800">
      {/* Header */}
      <div className="bg-slate-800 border-b border-slate-700">
        <div className="max-w-6xl mx-auto px-6 py-6">
          <div className="flex items-center justify-between">
            <div>
              <h1 className="text-3xl font-bold text-white">MC Simulation Engine</h1>
              <p className="text-slate-400 mt-1">Interactive dashboard for variance reduction analysis</p>
            </div>
            <a
              href="/"
              className="text-slate-300 hover:text-white transition font-medium"
            >
              ← Back Home
            </a>
          </div>
        </div>
      </div>

      {/* Main Content */}
      <div className="max-w-6xl mx-auto px-6 py-8">
        <div className="grid grid-cols-1 lg:grid-cols-3 gap-8">
          {/* Form Section */}
          <div className="lg:col-span-1">
            <SimulationForm onSubmit={handleSimulate} loading={loading} />
          </div>

          {/* Results Section */}
          <div className="lg:col-span-2">
            {error && (
              <div className="bg-red-900 border border-red-700 text-red-100 px-6 py-4 rounded-lg mb-6">
                <p className="font-bold">Error</p>
                <p className="text-sm mt-1">{error}</p>
              </div>
            )}

            {loading && (
              <div className="bg-slate-700 border border-slate-600 rounded-lg p-8 text-center">
                <div className="inline-block animate-spin rounded-full h-8 w-8 border-b-2 border-blue-500"></div>
                <p className="text-slate-300 mt-4">Running simulation...</p>
              </div>
            )}

            {results.length > 0 && !loading && (
              <ResultsDisplay results={results} />
            )}

            {results.length === 0 && !loading && !error && (
              <div className="bg-slate-700 border border-slate-600 rounded-lg p-8 text-center text-slate-400">
                <p>👈 Fill in the form and click "Run Simulation" to see results</p>
              </div>
            )}
          </div>
        </div>
      </div>
    </div>
  );
}
