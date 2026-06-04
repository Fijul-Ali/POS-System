"use client";
import React, { useState } from 'react';
import { useRouter } from 'next/navigation';
import apiClient from '@/utils/apiClient';
import Layout from './layout';

function Add({ title, apiPath, extraFields = [], extraData: externalExtraData = {}, maxLen, keyDown }) {
    const router = useRouter();
    const [identifier, setIdentifier] = useState('');
    const [description, setDescription] = useState('');
    const [extraData, setExtraData] = useState({});
    const [error, setError] = useState('');
    const [success, setSuccess] = useState('');
    const [loading, setLoading] = useState(false);

    const handleExtraChange = (key, value) => {
        setExtraData(prev => ({ ...prev, [key]: value }));
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setError('');
        setSuccess('');
        setLoading(true);
        try {
            const response = await apiClient.post(`/api/${apiPath}/add`, {
                 identifier, description, ...extraData, ...externalExtraData 
                })
            if (!response) return;
            const result = response.data;
            if (result.success) {
                setSuccess(`${title} added successfully`);
                router.push(`/${apiPath}`)
            } else {
                setError(data.message || 'Failed to add');
            }
        } catch (err) {
            setError(`${title} already exists`);
        } finally {
            setLoading(false);
        }
    };

    const inputClass = 'border border-gray-300 rounded-lg px-3 py-2.5 text-sm outline-none focus:border-blue-500 focus:ring-1 focus:ring-blue-200 transition w-full';
    const labelClass = 'text-xs font-semibold text-gray-500 uppercase tracking-wide mb-1 block';

    return (
        <Layout>
            <div className='grid place-items-center'>
                <div className='mb-6'>
                    <h1 className='text-xl font-bold text-gray-800 flex-items-center text-4xl'>Add {title}</h1>
                    <p className='text-sm text-gray-500 mt-0.5 flex justify-center items-center'>Fill in the details below</p>
                </div>

                <div className='bg-white border border-gray-200 rounded-lg p-6 max-w-lg w-full max-w-md'>
                    {error && <div className='bg-red-50 text-red-600 text-sm px-3 py-2 rounded-lg mb-4 border border-red-200'>{error}</div>}
                    {success && <div className='bg-green-50 text-green-700 text-sm px-3 py-2 rounded-lg mb-4 border border-green-200'>{success}</div>}

                    <form onSubmit={handleSubmit} className='flex flex-col gap-4'>
                        <div>
                            <label className={labelClass}>Identifier</label>
                            <input type='text' placeholder='Enter identifier' value={identifier} onChange={(e) => setIdentifier(e.target.value)} required className={inputClass} />
                        </div>
                        <div>
                            <label className={labelClass}>Description</label>
                            <textarea placeholder='Enter description' value={description} onChange={(e) => setDescription(e.target.value)} rows={3} className={`${inputClass} resize-none`} />
                        </div>

                        {extraFields.map((field) => (
                            <div key={field.key}>
                                {field.type !== 'custom' && <label className={labelClass}>{field.label}</label>}
                                {field.type === 'custom' ? field.component
                                    : field.type === 'select' ? (
                                        <select onChange={(e) => handleExtraChange(field.key, e.target.value)} required={field.required || false} className={inputClass}>
                                            <option value=''>Select {field.label}</option>
                                            {field.options?.map((opt) => <option key={opt.value} value={opt.value}>{opt.label}</option>)}
                                        </select>
                                    ) : field.type === 'multiselect' ? (
                                        <>
                                            <select multiple onChange={(e) => { const s = Array.from(e.target.selectedOptions, o => o.value); handleExtraChange(field.key, s); }} className={inputClass}>
                                                {field.options?.map((opt) => <option key={opt.value} value={opt.value}>{opt.label}</option>)}
                                            </select>
                                            <p className='text-xs text-gray-400 mt-1'>Hold Ctrl to select multiple</p>
                                        </>
                                    ) : (
                                        <input type={field.type || 'text'} placeholder={`Enter ${field.label}`} onChange={(e) => handleExtraChange(field.key, e.target.value)} required={field.required || false} className={inputClass}  maxLength={maxLen} onKeyDown={(e)=>{keyDown}}/>
                                    )}
                            </div>
                        ))}

                        <div className='flex gap-3 pt-2'>
                            <button type='button' onClick={() => router.push(`/${apiPath}`)} className='flex-1 bg-gray-100 text-gray-700 rounded-lg py-2.5 text-sm font-semibold hover:bg-gray-200 transition'>
                                Cancel
                            </button>
                            <button type='submit' disabled={loading} className='flex-1 bg-blue-600 text-white rounded-lg py-2.5 text-sm font-semibold hover:bg-blue-700 transition disabled:opacity-50'>
                                {loading ? 'Saving...' : `Add ${title}`}
                            </button>
                        </div>
                    </form>
                </div>
            </div>
        </Layout>
    );
}

export default Add;