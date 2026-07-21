import React from 'react';
import { SimulationResult } from '../pages/dashboard';
import MetricsTable from './MetricsTable';

interface ResultsDisplayProps {
  results: SimulationResult[];
}

export default function ResultsDisplay({ results }: ResultsDisplayProps) {
  if (!results || results.length === 0) {
    return null;
  }

  const domain = results[0]?.domain || 'Unknown';

  return (
    <div className="space-y-6">
      {/* Domain Header */}
      <div className="bg-slate-700 border border-slate-600 rounded-lg p-6">
        <h2 className="text-2xl font-bold text-white">{domain}</h2>
        <p className="text-slate-400 mt-2">
          {results.length} estimator{results.length > 1 ? 's' : ''} evaluated
        </p>
      </div>

      {/* Results Cards */}
      <div className="space-y-4">
        {results.map((result, idx) => (
          <div key={idx} className="bg-slate-700 border border-slate-600 rounded-lg p-6">
            {/* Estimator Header */}
            <div className="flex items-center justify-between mb-4">
              <h3 className="text-xl font-bold text-white">{result.estimator}</h3>
              {result.containsTruth && (
                <span className="bg-green-900 text-green-100 px-3 py-1 rounded text-sm font-semibold">
                  ✓ Contains Truth
                </span>
              )}
              {!result.containsTruth && (
                <span className="bg-red-900 text-red-100 px-3 py-1 rounded text-sm font-semibold">
                  ✗ Outside CI
                </span>
              )}
            </div>

            {/* Main Estimate Display */}
            <div className="bg-slate-600 rounded-lg p-6 mb-6">
              <p className="text-slate-400 text-sm mb-2">Point Estimate</p>
              <div className="text-4xl font-bold text-blue-400 mb-2">
                {result.pointEstimate.toFixed(6)}
              </div>
              <p className="text-slate-400 text-sm">
                ± {result.standardError.toFixed(6)} (std error)
              </p>
              <div className="text-sm text-slate-300 mt-4 border-t border-slate-500 pt-4">
                <p>95% Confidence Interval: [{result.ciLower.toFixed(6)}, {result.ciUpper.toFixed(6)}]</p>
              </div>
            </div>

            {/* Metrics Grid */}
            <div className="grid grid-cols-2 gap-4">
              <div className="bg-slate-600 rounded p-4">
                <p className="text-slate-400 text-xs font-semibold uppercase mb-1">Bias</p>
                <p className="text-xl font-bold text-white">{result.bias.toFixed(6)}</p>
              </div>

              <div className="bg-slate-600 rounded p-4">
                <p className="text-slate-400 text-xs font-semibold uppercase mb-1">MSE</p>
                <p className="text-xl font-bold text-white">{result.mse.toFixed(6)}</p>
              </div>

              {result.varianceReduction !== undefined && (
                <div className="bg-slate-600 rounded p-4">
                  <p className="text-slate-400 text-xs font-semibold uppercase mb-1">Variance Reduction</p>
                  <p className="text-xl font-bold text-green-400">{result.varianceReduction.toFixed(2)}×</p>
                </div>
              )}

              {result.correlation !== undefined && (
                <div className="bg-slate-600 rounded p-4">
                  <p className="text-slate-400 text-xs font-semibold uppercase mb-1">Correlation (ρ)</p>
                  <p className="text-xl font-bold text-yellow-400">{result.correlation.toFixed(4)}</p>
                </div>
              )}

              <div className="bg-slate-600 rounded p-4">
                <p className="text-slate-400 text-xs font-semibold uppercase mb-1">Compute Time</p>
                <p className="text-xl font-bold text-white">{result.computeTimeMs}ms</p>
              </div>
            </div>
          </div>
        ))}
      </div>

      {/* Summary Table */}
      <MetricsTable results={results} />

      {/* Export Button */}
      <div className="flex gap-4">
        <button
          onClick={() => {
            const json = JSON.stringify(results, null, 2);
            const blob = new Blob([json], { type: 'application/json' });
            const url = URL.createObjectURL(blob);
            const a = document.createElement('a');
            a.href = url;
            a.download = `simulation-results-${Date.now()}.json`;
            a.click();
          }}
          className="flex-1 bg-green-600 hover:bg-green-700 text-white font-bold py-2 px-4 rounded transition"
        >
          📥 Download JSON
        </button>

        <button
          onClick={() => {
            const csv = [
              ['Domain', 'Estimator', 'Estimate', 'Std Error', 'Bias', 'MSE', 'Contains Truth'],
              ...results.map((r) => [
                r.domain,
                r.estimator,
                r.pointEstimate,
                r.standardError,
                r.bias,
                r.mse,
                r.containsTruth ? 'Yes' : 'No',
              ]),
            ]
              .map((row) => row.join(','))
              .join('\n');
            const blob = new Blob([csv], { type: 'text/csv' });
            const url = URL.createObjectURL(blob);
            const a = document.createElement('a');
            a.href = url;
            a.download = `simulation-results-${Date.now()}.csv`;
            a.click();
          }}
          className="flex-1 bg-purple-600 hover:bg-purple-700 text-white font-bold py-2 px-4 rounded transition"
        >
          📊 Download CSV
        </button>
      </div>
    </div>
  );
}
