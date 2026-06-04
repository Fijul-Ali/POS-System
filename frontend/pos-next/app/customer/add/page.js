"use client";
import React, { useState } from 'react';
import { useRouter } from 'next/navigation';
import apiClient from '@/utils/apiClient';
import Layout from '@/app/components/layout';

function CustomerAdd() {
    const router = useRouter();
    const [error, setError] = useState('');
    const [success, setSuccess] = useState('');
    const [loading, setLoading] = useState(false);
    const [sameAsBilling, setSameAsBilling] = useState(false);

    const [customer, setCustomer] = useState({
        identifier: '', // 💡 Added Identifier field to state matching DB requirement
        name: '',
        username: '',
        phoneNo: '',
        partyType: 'Customer',
        creditLimit: 0.0,
        balance: 0.0,
        balanceType: 'due',
        billingAddress: { line1: '', city: '', state: '', zipCode: '', country: '' },
        shippingAddress: { line1: '', city: '', state: '', zipCode: '', country: '' }
    });

    const handleGeneralChange = (key, value) => {
        setCustomer(prev => ({ ...prev, [key]: value }));
    };

    const handleAddressChange = (type, key, value) => {
        setCustomer(prev => {
            const updatedAddress = { ...prev[type], [key]: value };
            
            if (sameAsBilling && type === 'billingAddress') {
                return {
                    ...prev,
                    billingAddress: updatedAddress,
                    shippingAddress: updatedAddress
                };
            }
            return { ...prev, [type]: updatedAddress };
        });
    };

    const handleCheckboxChange = (e) => {
        const checked = e.target.checked;
        setSameAsBilling(checked);
        if (checked) {
            setCustomer(prev => ({
                ...prev,
                shippingAddress: { ...prev.billingAddress }
            }));
        }
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setError('');
        setSuccess('');
        setLoading(true);
        try {
            const response = await apiClient.post(`/api/customer/add`, customer);
            if (!response) return;
            const result = response.data;
            
            if (result && result.phoneNo) {
                setSuccess('Customer added successfully');
                router.push('/customer');
            } else {
                setError('Failed to add customer. Check if phone number or identifier already exists.');
            }
        } catch (err) {
            setError('Unable to connect to server.');
        } finally {
            setLoading(false);
        }
    };

    const inputClass = 'border border-gray-300 rounded-lg px-3 py-2.5 text-sm outline-none focus:border-blue-500 focus:ring-1 focus:ring-blue-200 transition w-full';
    const labelClass = 'text-xs font-semibold text-gray-500 uppercase tracking-wide mb-1 block';
    const sectionTitleClass = 'text-xs font-bold text-blue-600 uppercase tracking-wider mb-3 flex items-center gap-1 mt-2';

    return (
        <Layout>
            <div className='grid place-items-center min-h-[80vh] py-6'>
                <div className='mb-6 text-center'>
                    <h1 className='text-4xl font-bold text-gray-800'>Add New Customer</h1>
                    <p className='text-sm text-gray-500 mt-0.5'>Fill in the details below</p>
                </div>

                <div className='bg-white border border-gray-200 rounded-lg p-6 w-full max-w-2xl shadow-sm'>
                    {error && <div className='bg-red-50 text-red-600 text-sm px-3 py-2 rounded-lg mb-4 border border-red-200'>{error}</div>}
                    {success && <div className='bg-green-50 text-green-700 text-sm px-3 py-2 rounded-lg mb-4 border border-green-200'>{success}</div>}

                    <form onSubmit={handleSubmit} className='flex flex-col gap-6'>
                        
                        {/* --- General Info --- */}
                        <div>
                            <div className={sectionTitleClass}>👤 General Information</div>
                            <div className='grid grid-cols-1 md:grid-cols-2 gap-4'>
                                {/* 💡 New Row Segment: Identifier & Full Name Side by Side */}
                                <div>
                                    <label className={labelClass}>Customer Identifier / Code</label>
                                    <input 
                                        type='text' 
                                        placeholder='e.g. CUST-001'
                                        value={customer.identifier} 
                                        onChange={(e) => handleGeneralChange('identifier', e.target.value)} 
                                        required 
                                        className={inputClass} 
                                    />
                                </div>
                                <div>
                                    <label className={labelClass}>Full Name</label>
                                    <input type='text' value={customer.name} onChange={(e) => handleGeneralChange('name', e.target.value)} required className={inputClass} />
                                </div>
                                <div>
                                    <label className={labelClass}>Email (Username)</label>
                                    <input type='email' value={customer.username} onChange={(e) => handleGeneralChange('username', e.target.value)} required className={inputClass} />
                                </div>
                                <div>
                                    <label className={labelClass}>Phone Number</label>
                                    <input type='text' placeholder='+91...' value={customer.phoneNo} onChange={(e) => handleGeneralChange('phoneNo', e.target.value)} required className={inputClass} />
                                </div>
                                <div>
                                    <label className={labelClass}>Party Type</label>
                                    <select value={customer.partyType} onChange={(e) => handleGeneralChange('partyType', e.target.value)} className={inputClass}>
                                        <option value="Customer">Customer</option>
                                        <option value="Dealer">Dealer</option>
                                        <option value="Wholesaler">Wholesaler</option>
                                    </select>
                                </div>
                                <div>
                                    <label className={labelClass}>Credit Limit</label>
                                    <input type='number' step='0.01' value={customer.creditLimit} onChange={(e) => handleGeneralChange('creditLimit', parseFloat(e.target.value) || 0)} className={inputClass} />
                                </div>
                                <div className='grid grid-cols-2 gap-2'>
                                    <div>
                                        <label className={labelClass}>Opening Balance</label>
                                        <input type='number' step='0.01' value={customer.balance} onChange={(e) => handleGeneralChange('balance', parseFloat(e.target.value) || 0)} className={inputClass} />
                                    </div>
                                    <div>
                                        <label className={labelClass}>Balance Type</label>
                                        <select value={customer.balanceType} onChange={(e) => handleGeneralChange('balanceType', e.target.value)} className={inputClass}>
                                            <option value="due">Due</option>
                                            <option value="advance">Advance</option>
                                        </select>
                                    </div>
                                </div>
                            </div>
                        </div>

                        {/* --- Billing Address --- */}
                        <div className='bg-slate-50 p-4 rounded-xl border border-gray-100'>
                            <div className={sectionTitleClass}>📍 Billing Address</div>
                            <div className='grid grid-cols-1 md:grid-cols-3 gap-4'>
                                <div className='md:col-span-3'>
                                    <label className={labelClass}>Address Line</label>
                                    <input type='text' value={customer.billingAddress.line1} onChange={(e) => handleAddressChange('billingAddress', 'line1', e.target.value)} className={inputClass} />
                                </div>
                                <div>
                                    <label className={labelClass}>City</label>
                                    <input type='text' value={customer.billingAddress.city} onChange={(e) => handleAddressChange('billingAddress', 'city', e.target.value)} className={inputClass} />
                                </div>
                                <div>
                                    <label className={labelClass}>State</label>
                                    <input type='text' value={customer.billingAddress.state} onChange={(e) => handleAddressChange('billingAddress', 'state', e.target.value)} className={inputClass} />
                                </div>
                                <div>
                                    <label className={labelClass}>Zip Code</label>
                                    <input type='text' value={customer.billingAddress.zipCode} onChange={(e) => handleAddressChange('billingAddress', 'zipCode', e.target.value)} className={inputClass} />
                                </div>
                                <div>
                                    <label className={labelClass}>Country</label>
                                    <input type='text' value={customer.billingAddress.country} onChange={(e) => handleAddressChange('billingAddress', 'country', e.target.value)} className={inputClass} />
                                </div>
                            </div>
                        </div>

                        {/* --- Shipping Address --- */}
                        <div className='bg-slate-50 p-4 rounded-xl border border-gray-100'>
                            <div className='flex justify-between items-center mb-3'>
                                <div className={`${sectionTitleClass} mb-0`}>🚚 Shipping Address</div>
                                <div className='flex items-center gap-2'>
                                    <input type='checkbox' id='copyCheck' checked={sameAsBilling} onChange={handleCheckboxChange} className='rounded border-gray-300 text-blue-600 focus:ring-blue-500 h-4 w-4' />
                                    <label htmlFor='copyCheck' className='text-xs font-bold text-gray-600 cursor-pointer'>Same as Billing</label>
                                </div>
                            </div>
                            <div className='grid grid-cols-1 md:grid-cols-3 gap-4'>
                                <div className='md:col-span-3'>
                                    <label className={labelClass}>Address Line</label>
                                    <input type='text' value={customer.shippingAddress.line1} onChange={(e) => handleAddressChange('shippingAddress', 'line1', e.target.value)} disabled={sameAsBilling} className={`${inputClass} ${sameAsBilling ? 'bg-gray-100 text-gray-400 cursor-not-allowed' : ''}`} />
                                </div>
                                <div>
                                    <label className={labelClass}>City</label>
                                    <input type='text' value={customer.shippingAddress.city} onChange={(e) => handleAddressChange('shippingAddress', 'city', e.target.value)} disabled={sameAsBilling} className={`${inputClass} ${sameAsBilling ? 'bg-gray-100 text-gray-400 cursor-not-allowed' : ''}`} />
                                </div>
                                <div>
                                    <label className={labelClass}>State</label>
                                    <input type='text' value={customer.shippingAddress.state} onChange={(e) => handleAddressChange('shippingAddress', 'state', e.target.value)} disabled={sameAsBilling} className={`${inputClass} ${sameAsBilling ? 'bg-gray-100 text-gray-400 cursor-not-allowed' : ''}`} />
                                </div>
                                <div>
                                    <label className={labelClass}>Zip Code</label>
                                    <input type='text' value={customer.shippingAddress.zipCode} onChange={(e) => handleAddressChange('shippingAddress', 'zipCode', e.target.value)} disabled={sameAsBilling} className={`${inputClass} ${sameAsBilling ? 'bg-gray-100 text-gray-400 cursor-not-allowed' : ''}`} />
                                </div>
                                <div>
                                    <label className={labelClass}>Country</label>
                                    <input type='text' value={customer.shippingAddress.country} onChange={(e) => handleAddressChange('shippingAddress', 'country', e.target.value)} disabled={sameAsBilling} className={`${inputClass} ${sameAsBilling ? 'bg-gray-100 text-gray-400 cursor-not-allowed' : ''}`} />
                                </div>
                            </div>
                        </div>

                        {/* --- Action Buttons --- */}
                        <div className='flex gap-3 pt-2'>
                            <button type='button' onClick={() => router.push('/customer')} className='flex-1 bg-gray-100 text-gray-700 rounded-lg py-2.5 text-sm font-semibold hover:bg-gray-200 transition'>
                                Cancel
                            </button>
                            <button type='submit' disabled={loading} className='flex-1 bg-blue-600 text-white rounded-lg py-2.5 text-sm font-semibold hover:bg-blue-700 transition disabled:opacity-50'>
                                {loading ? 'Saving...' : 'Save Customer'}
                            </button>
                        </div>
                    </form>
                </div>
            </div>
        </Layout>
    );
}

export default CustomerAdd;