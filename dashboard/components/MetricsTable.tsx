import React from 'react';
import { SimulationResult } from '../pages/dashboard';

interface MetricsTableProps {
  results: SimulationResult[];
}

export default function MetricsTable({ results }: MetricsTableProps) {
  return (
    <div className="bg-slate-700 border border-slate-600 rounded-lg overflow-hidden">
      <div className="overflow-x-auto">
        <table className="w-full text-sm">
          <thead>
            <tr className="bg-slate-600 border-b border-slate-500">
              <th className="px-4 py-3 text-left text-white font-semibold">Estimator</th>
              <th className="px-4 py-3 text-right text-white font-semibold">Estimate</th>
              <th className="px-4 py-3 text-right text-white font-semibold">Std Error</th>
              <th className="px-4 py-3 text-right text-white font-semibold">Bias</th>
              <th className="px-4 py-3 text-right text-white font-semibold">MSE</th>
              <th className="px-4 py-3 text-center text-white font-semibold">CI</th>
            </tr>
          </thead>
          <tbody>
            {results.map((result, idx) => (
              <tr
                key={idx}
                className={`border-b border-slate-600 ${
                  idx % 2 === 0 ? 'bg-slate-700' : 'bg-slate-750'
                }`}
              >
                <td className="px-4 py-3">
                  <span className="text-white font-semibold">{result.estimator}</span>
                </td>
                <td className="px-4 py-3 text-right text-blue-400 font-mono">
                  {result.pointEstimate.toFixed(6)}
                </td>
                <td className="px-4 py-3 text-right text-slate-300 font-mono">
                  {result.standardError.toFixed(6)}
                </td>
                <td className="px-4 py-3 text-right text-slate-300 font-mono">
                  {result.bias.toFixed(6)}
                </td>
                <td className="px-4 py-3 text-right text-slate-300 font-mono">
                  {result.mse.toFixed(6)}
                </td>
                <td className="px-4 py-3 text-center">
                  {result.containsTruth ? (
                    <span className="text-green-400 font-semibold">✓</span>
                  ) : (
                    <span className="text-red-400 font-semibold">✗</span>
                  )}
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
}
