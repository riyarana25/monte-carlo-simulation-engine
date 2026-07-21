import React, { useState } from 'react';

interface SimulationFormProps {
  onSubmit: (data: any) => void;
  loading: boolean;
}

export default function SimulationForm({ onSubmit, loading }: SimulationFormProps) {
  const [formData, setFormData] = useState({
    domain: 'buffon',
    samples: 10000,
    replications: 100,
    threads: 4,
    seed: 42,
  });

  const domains = [
    { value: 'buffon', label: '📐 Buffon\'s Needle (π estimation)' },
    { value: 'portfolio_var', label: '💰 Portfolio VaR (95%)' },
    { value: 'barrier_option', label: '📊 Barrier Option Pricing' },
    { value: 'rare_event', label: '⚡ Rare Event (P(Z > 5σ))' },
  ];

  const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) => {
    const { name, value } = e.target;
    setFormData((prev) => ({
      ...prev,
      [name]: name === 'domain' ? value : parseInt(value) || value,
    }));
  };

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    onSubmit(formData);
  };

  return (
    <div className="bg-slate-700 border border-slate-600 rounded-lg p-6">
      <h2 className="text-2xl font-bold text-white mb-6">Configure Simulation</h2>

      <form onSubmit={handleSubmit} className="space-y-5">
        {/* Domain Selection */}
        <div>
          <label className="block text-white font-semibold mb-2">Domain</label>
          <select
            name="domain"
            value={formData.domain}
            onChange={handleChange}
            disabled={loading}
            className="w-full bg-slate-600 text-white px-4 py-2 rounded border border-slate-500 focus:outline-none focus:border-blue-500 disabled:opacity-50"
          >
            {domains.map((d) => (
              <option key={d.value} value={d.value}>
                {d.label}
              </option>
            ))}
          </select>
        </div>

        {/* Samples */}
        <div>
          <label className="block text-white font-semibold mb-2">
            Samples per Replication
          </label>
          <input
            type="number"
            name="samples"
            value={formData.samples}
            onChange={handleChange}
            disabled={loading}
            min={100}
            max={1000000}
            step={1000}
            className="w-full bg-slate-600 text-white px-4 py-2 rounded border border-slate-500 focus:outline-none focus:border-blue-500 disabled:opacity-50"
          />
          <p className="text-xs text-slate-400 mt-1">Range: 100 - 1,000,000</p>
        </div>

        {/* Replications */}
        <div>
          <label className="block text-white font-semibold mb-2">Replications</label>
          <input
            type="number"
            name="replications"
            value={formData.replications}
            onChange={handleChange}
            disabled={loading}
            min={1}
            max={1000}
            className="w-full bg-slate-600 text-white px-4 py-2 rounded border border-slate-500 focus:outline-none focus:border-blue-500 disabled:opacity-50"
          />
          <p className="text-xs text-slate-400 mt-1">Range: 1 - 1,000</p>
        </div>

        {/* Threads */}
        <div>
          <label className="block text-white font-semibold mb-2">Threads</label>
          <input
            type="number"
            name="threads"
            value={formData.threads}
            onChange={handleChange}
            disabled={loading}
            min={1}
            max={16}
            className="w-full bg-slate-600 text-white px-4 py-2 rounded border border-slate-500 focus:outline-none focus:border-blue-500 disabled:opacity-50"
          />
          <p className="text-xs text-slate-400 mt-1">Range: 1 - 16 (parallelization)</p>
        </div>

        {/* Seed */}
        <div>
          <label className="block text-white font-semibold mb-2">Random Seed</label>
          <input
            type="number"
            name="seed"
            value={formData.seed}
            onChange={handleChange}
            disabled={loading}
            className="w-full bg-slate-600 text-white px-4 py-2 rounded border border-slate-500 focus:outline-none focus:border-blue-500 disabled:opacity-50"
          />
          <p className="text-xs text-slate-400 mt-1">For reproducible results</p>
        </div>

        {/* Submit Button */}
        <button
          type="submit"
          disabled={loading}
          className="w-full bg-blue-600 hover:bg-blue-700 disabled:bg-slate-600 text-white font-bold py-3 px-4 rounded transition mt-6"
        >
          {loading ? 'Running...' : '▶ Run Simulation'}
        </button>

        {/* Info Box */}
        <div className="bg-slate-600 border border-slate-500 rounded p-4 text-xs text-slate-300 mt-6">
          <p className="font-semibold mb-2">💡 Tips:</p>
          <ul className="list-disc list-inside space-y-1">
            <li>Larger samples = more accurate but slower</li>
            <li>More threads = faster (if available)</li>
            <li>Same seed = reproducible results</li>
          </ul>
        </div>
      </form>
    </div>
  );
}
