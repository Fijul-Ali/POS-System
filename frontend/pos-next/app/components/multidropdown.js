"use client";
import React, { useEffect, useState } from 'react';
import apiClient from '@/utils/apiClient';

function MultiDropdown({ value, onChange, label, apiPath, required = false }) {
    const [options, setOptions] = useState([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        const fetchOptions = async () => {
            try {
                const response = await apiClient.get(`/api/${apiPath}/active`);
                if (!response) return;
                const result = response.data;
                setOptions(result);
            } catch (err) {
                console.error(`Failed to fetch ${label}:`, err);
            } finally {
                setLoading(false);
            }
        };
        fetchOptions();
    }, [apiPath]);

    if (loading) return <p className='text-sm text-gray-400'>Loading {label}...</p>;

    return (
        <div className='flex flex-col gap-1'>
            <label className='text-sm font-semibold text-gray-600'>{label}</label>
            <select
                multiple
                value={value}
                onChange={(e) => {
                    const selected = Array.from(e.target.selectedOptions, o => o.value);
                    onChange(selected);
                }}
                required={required}
                className='border border-gray-300 rounded-xl px-4 py-3 text-sm outline-none focus:border-indigo-500 focus:ring-2 focus:ring-indigo-100 transition'
            >
                {options.map((item) => (
                    <option key={item.identifier} value={item.identifier}>
                        {item.identifier}
                    </option>
                ))}
            </select>
            <p className='text-xs text-gray-400 mt-1'>Hold Ctrl to select multiple</p>
        </div>
    );
}

export default MultiDropdown;