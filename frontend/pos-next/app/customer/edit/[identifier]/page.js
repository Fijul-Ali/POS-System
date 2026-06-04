"use client";
import React, { useEffect, useState, useCallback } from 'react';
import { useRouter, useParams } from 'next/navigation';
import apiClient from '@/utils/apiClient';
import Layout from '@/app/components/layout';

function CustomerEdit() {
    const router = useRouter();
    const { identifier } = useParams(); 
    const [error, setError] = useState('');
    const [success, setSuccess] = useState('');
    const [loading, setLoading] = useState(true);
    const [saving, setSaving] = useState(false);

    const [customer, setCustomer] = useState({
        id: null,
        identifier: '',
        name: '',
        username: '',
        phoneNo: '',
        partyType: 'Customer',
        creditLimit: 0.0,
        balance: 0.0,
        balanceType: 'due',
        billingAddress: { id: null, line1: '', city: '', state: '', zipCode: '', country: '', addressType: 'Billing' },
        shippingAddress: { id: null, line1: '', city: '', state: '', zipCode: '', country: '', addressType: 'Shipping' }
    });

    const fetchCustomerAndAddresses = useCallback(async () => {
        if (!identifier) return;
        
        try {
            setLoading(true);
            setError('');
            
            // 1. Fetch main customer info by identifier
            const customerResponse = await apiClient.get(`/api/customer/get?identifier=${identifier}`);
            if (!customerResponse || !customerResponse.data) {
                throw new Error("Customer profile not found");
            }
            const customerData = customerResponse.data;
            const targetPhone = customerData.phoneNo;

            let billingObj = { line1: '', city: '', state: '', zipCode: '', country: '', addressType: 'Billing' };
            let shippingObj = { line1: '', city: '', state: '', zipCode: '', country: '', addressType: 'Shipping' };

            // 2. Fetch linked addresses from your separate table/endpoint using the phone number
            if (targetPhone) {
                try {
                    // Update this endpoint string to match your exact Spring Boot address query endpoint
                    const addressResponse = await apiClient.get(`/api/customer/findaddr?phoneNumber=${targetPhone}`);
                    
                    if (addressResponse && Array.isArray(addressResponse.data)) {
                        const addresses = addressResponse.data;
                        
                        const bAddress = addresses.find(addr => addr.addressType === 'Billing');
                        if (bAddress) billingObj = bAddress;

                        const sAddress = addresses.find(addr => addr.addressType === 'Shipping');
                        if (sAddress) shippingObj = sAddress;
                    }
                } catch (addrErr) {
                    console.warn("Addresses could not be retrieved, using defaults:", addrErr);
                }
            }

            // 3. Assemble them into unified state
            setCustomer({
                ...customerData,
                creditLimit: customerData.creditLimit ?? 0.0,
                balance: customerData.balance ?? 0.0,
                billingAddress: billingObj,
                shippingAddress: shippingObj
            });

        } catch (err) {
            console.error("Fetch Data Lifecycle Error: ", err);
            setError('Failed to load customer profile details.');
        } finally {
            setLoading(false);
        }
    }, [identifier]);

    useEffect(() => {
        fetchCustomerAndAddresses();
    }, [identifier, fetchCustomerAndAddresses]);

    const handleGeneralChange = (key, value) => {
        setCustomer(prev => ({ ...prev, [key]: value }));
    };

    const handleAddressChange = (type, key, value) => {
        setCustomer(prev => ({
            ...prev,
            [type]: { ...prev[type], [key]: value }
        }));
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setError('');
        setSuccess('');
        setSaving(true);
        try {
            // Sends the combined object back. 
            // Note: Ensure your Spring Boot backend's /update controller maps these arrays/objects correctly or saves them to their respective repositories.
            const response = await apiClient.post('/api/customer/update', customer);
            if (!response) return;
            const result = response.data;
            
            if (result && (result.phoneNo || result.identifier)) {
                setSuccess('Customer updated successfully');
                router.push('/customer');
            } else {
                setError('Failed to update customer payload updates.');
            }
        } catch (err) {
            setError('Unable to connect to server.');
        } finally {
            setSaving(false);
        }
    };

    const inputClass = 'border border-gray-300 rounded-lg px-3 py-2.5 text-sm outline-none focus:border-blue-500 focus:ring-1 focus:ring-blue-200 transition w-full';
    const labelClass = 'text-xs font-semibold text-gray-500 uppercase tracking-wide mb-1 block';
    const sectionTitleClass = 'text-xs font-bold text-blue-600 uppercase tracking-wider mb-3 flex items-center gap-1 mt-2';

    if (loading) return (
        <Layout>
            <div className='flex justify-center items-center h-64'>
                <p className='text-gray-400 text-sm animate-pulse'>Loading Customer Details & Addresses...</p>
            </div>
        </Layout>
    );

    return (
        <Layout>
            <div className='grid place-items-center min-h-[80vh] py-6'>
                <div className='mb-6 text-center'>
                    <h1 className='text-4xl font-bold text-gray-800'>Edit Customer</h1>
                    <p className='text-sm text-gray-500 mt-0.5'>Update the customer data model below</p>
                </div>

                <div className='bg-white border border-gray-200 rounded-lg p-6 w-full max-w-2xl shadow-sm'>
                    {error && <div className='bg-red-50 text-red-600 text-sm px-3 py-2 rounded-lg mb-4 border border-red-200'>{error}</div>}
                    {success && <div className='bg-green-50 text-green-700 text-sm px-3 py-2 rounded-lg mb-4 border border-green-200'>{success}</div>}

                    <form onSubmit={handleSubmit} className='flex flex-col gap-6'>
                        
                        {/* --- General Section --- */}
                        <div>
                            <div className={sectionTitleClass}>👤 General Information</div>
                            <div className='grid grid-cols-1 md:grid-cols-2 gap-4'>
                                <div>
                                    <label className={labelClass}>Customer Identifier</label>
                                    <input type='text' value={customer.identifier} readOnly className={`${inputClass} bg-gray-50 text-gray-500 cursor-not-allowed`} />
                                </div>
                                <div>
                                    <label className={labelClass}>Phone Number (Unique ID)</label>
                                    <input type='text' value={customer.phoneNo} readOnly className={`${inputClass} bg-gray-50 text-gray-500 cursor-not-allowed`} />
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
                            </div>
                        </div>

                        {/* --- Billing --- */}
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

                        {/* --- Shipping --- */}
                        <div className='bg-slate-50 p-4 rounded-xl border border-gray-100'>
                            <div className={sectionTitleClass}>🚚 Shipping Address</div>
                            <div className='grid grid-cols-1 md:grid-cols-3 gap-4'>
                                <div className='md:col-span-3'>
                                    <label className={labelClass}>Address Line</label>
                                    <input type='text' value={customer.shippingAddress.line1} onChange={(e) => handleAddressChange('shippingAddress', 'line1', e.target.value)} className={inputClass} />
                                </div>
                                <div>
                                    <label className={labelClass}>City</label>
                                    <input type='text' value={customer.shippingAddress.city} onChange={(e) => handleAddressChange('shippingAddress', 'city', e.target.value)} className={inputClass} />
                                </div>
                                <div>
                                    <label className={labelClass}>State</label>
                                    <input type='text' value={customer.shippingAddress.state} onChange={(e) => handleAddressChange('shippingAddress', 'state', e.target.value)} className={inputClass} />
                                </div>
                                <div>
                                    <label className={labelClass}>Zip Code</label>
                                    <input type='text' value={customer.shippingAddress.zipCode} onChange={(e) => handleAddressChange('shippingAddress', 'zipCode', e.target.value)} className={inputClass} />
                                </div>
                                <div>
                                    <label className={labelClass}>Country</label>
                                    <input type='text' value={customer.shippingAddress.country} onChange={(e) => handleAddressChange('shippingAddress', 'country', e.target.value)} className={inputClass} />
                                </div>
                            </div>
                        </div>

                        {/* --- Controls --- */}
                        <div className='flex gap-3 pt-2'>
                            <button type='button' onClick={() => router.push('/customer')} className='flex-1 bg-gray-100 text-gray-700 rounded-lg py-2.5 text-sm font-semibold hover:bg-gray-200 transition'>
                                Cancel
                            </button>
                            <button type='submit' disabled={saving} className='flex-1 bg-blue-600 text-white rounded-lg py-2.5 text-sm font-semibold hover:bg-blue-700 transition disabled:opacity-50'>
                                {saving ? 'Saving...' : 'Update Customer'}
                            </button>
                        </div>
                    </form>
                </div>
            </div>
        </Layout>
    );
}

export default CustomerEdit;