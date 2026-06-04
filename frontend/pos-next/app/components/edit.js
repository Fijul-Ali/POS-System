"use client";
import React, { useEffect, useState } from 'react';
import { useRouter } from 'next/navigation';
import apiClient from '@/utils/apiClient';
import Layout from './layout';
import { useParams } from 'next/navigation';

function Edit({ title, apiPath, extraFields = [], extraData: externalExtraData = {}, onLoad }) {
    const router = useRouter();
    const { identifier } = useParams();
    const [formData, setFormData] = useState({});
    const [identifier_, setIdentifier] = useState('');
    const [description, setDescription] = useState('');
    const [extraData, setExtraData] = useState({});
    const [error, setError] = useState('');
    const [success, setSuccess] = useState('');
    const [loading, setLoading] = useState(true);
    const [saving, setSaving] = useState(false);

    useEffect(() => { fetchItem(); }, []);

    const fetchItem = async () => {
        try {
            const response = await apiClient.get(`/api/${apiPath}/get?identifier=${identifier}`);
            if (!response) return;
            const result = response.data;
            setIdentifier(result.identifier || '');
            setDescription(result.description || '');
            setFormData(result);
            const extra = {};
            extraFields.forEach(field => {
                if (field.key && result[field.key] !== undefined) extra[field.key] = result[field.key];
            });
            setExtraData(extra);
            if (onLoad) onLoad(result);
        } catch (err) {
            setError('Failed to load data');
        } finally {
            setLoading(false);
        }
    };

    const handleExtraChange = (key, value) => {
        setExtraData(prev => ({ ...prev, [key]: value }));
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setError('');
        setSuccess('');
        setSaving(true);
        try {
            const response = await apiClient.post(`/api/${apiPath}/update`, 
                {...formData, identifier: identifier_, description, ...extraData, ...externalExtraData })
            if (!response) return;
            const result = response.data;
            if (result.success) {
                setSuccess(`${title} updated successfully`);
                router.push(`/${apiPath}`)
            } else {
                setError(data.message || 'Failed to update');
            }
        } catch (err) {
            setError('Unable to connect to server.');
        } finally {
            setSaving(false);
        }
    };

    const inputClass = 'border border-gray-300 rounded-lg px-3 py-2.5 text-sm outline-none focus:border-blue-500 focus:ring-1 focus:ring-blue-200 transition w-full';
    const labelClass = 'text-xs font-semibold text-gray-500 uppercase tracking-wide mb-1 block';

    if (loading) return (
        <Layout>
            <div className='flex justify-center items-center h-64'>
                <p className='text-gray-400 text-sm'>Loading {title}...</p>
            </div>
        </Layout>
    );

    return (
        <Layout>
            <div className='grid place-items-center'>
                <div className='mb-6'>
                    <h1 className='text-4xl font-bold text-gray-800'>Edit {title}</h1>
                    <p className='text-sm text-gray-500 mt-0.5 flex items-center justify-center'>Update the details below</p>
                </div>

                <div className='bg-white border border-gray-200 rounded-lg p-6 max-w-lg w-full max-w-md'>
                    {error && <div className='bg-red-50 text-red-600 text-sm px-3 py-2 rounded-lg mb-4 border border-red-200'>{error}</div>}
                    {success && <div className='bg-green-50 text-green-700 text-sm px-3 py-2 rounded-lg mb-4 border border-green-200'>{success}</div>}

                    <form onSubmit={handleSubmit} className='flex flex-col gap-4'>
                        <div>
                            <label className={labelClass}>Identifier</label>
                            <input type='text' value={identifier_} readOnly className={`${inputClass} bg-gray-50 text-gray-500 cursor-not-allowed`} />
                        </div>
                        <div>
                            <label className={labelClass}>Description</label>
                            <textarea value={description} onChange={(e) => setDescription(e.target.value)} rows={3} className={`${inputClass} resize-none`} />
                        </div>

                        {extraFields.map((field) => (
                            <div key={field.key}>
                                {field.type !== 'custom' && <label className={labelClass}>{field.label}</label>}
                                {field.type === 'custom' ? field.component
                                    : field.type === 'select' ? (
                                        <select value={extraData[field.key] || ''} onChange={(e) => handleExtraChange(field.key, e.target.value)} required={field.required || false} className={inputClass}>
                                            <option value=''>Select {field.label}</option>
                                            {field.options?.map((opt) => <option key={opt.value} value={opt.value}>{opt.label}</option>)}
                                        </select>
                                    ) : field.type === 'multiselect' ? (
                                        <>
                                            <select multiple value={extraData[field.key] || []} onChange={(e) => { const s = Array.from(e.target.selectedOptions, o => o.value); handleExtraChange(field.key, s); }} className={inputClass}>
                                                {field.options?.map((opt) => <option key={opt.value} value={opt.value}>{opt.label}</option>)}
                                            </select>
                                            <p className='text-xs text-gray-400 mt-1'>Hold Ctrl to select multiple</p>
                                        </>
                                    ) : (
                                        <input type={field.type || 'text'} value={extraData[field.key] || ''} placeholder={`Enter ${field.label}`} onChange={(e) => handleExtraChange(field.key, e.target.value)} required={field.required || false} className={inputClass} />
                                    )}
                            </div>
                        ))}

                        <div className='flex gap-3 pt-2'>
                            <button type='button' onClick={() => router.push(`/${apiPath}`)} className='flex-1 bg-gray-100 text-gray-700 rounded-lg py-2.5 text-sm font-semibold hover:bg-gray-200 transition'>
                                Cancel
                            </button>
                            <button type='submit' disabled={saving} className='flex-1 bg-blue-600 text-white rounded-lg py-2.5 text-sm font-semibold hover:bg-blue-700 transition disabled:opacity-50'>
                                {saving ? 'Saving...' : `Update ${title}`}
                            </button>
                        </div>
                    </form>
                </div>
            </div>
        </Layout>
    );
}

export default Edit;