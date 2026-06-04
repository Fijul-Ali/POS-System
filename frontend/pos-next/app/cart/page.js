"use client";
import React, { useEffect, useState } from 'react';
import { useRouter } from 'next/navigation';
import apiClient from '@/utils/apiClient';
import Layout from '@/app/components/layout';

function CartList() {
    const router = useRouter();
    const [data, setData] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState('');
    const [page, setPage] = useState(0);
    const [totalPages, setTotalPages] = useState(0);
    const [totalRecords, setTotalRecords] = useState(0);
    const sizePerPage = 5;

    // Modal state
    const [showModal, setShowModal] = useState(false);
    const [customers, setCustomers] = useState([]);
    const [selectedCustomer, setSelectedCustomer] = useState('');
    const [initializing, setInitializing] = useState(false);
    const [modalError, setModalError] = useState('');

    useEffect(() => {
        fetchData(page);
    }, [page]);

    const fetchData = async (currentPage) => {
        try {
            setLoading(true);
            const response = await apiClient.post('/api/cart/list', {
                page: currentPage,
                sizePerPage,
                sortDirection: 'ASC',
                sortField: 'id'
            });
            if (!response) return;
            const result = response.data;
            setData(result.dtoList || []);
            setTotalPages(result.totalPages || 0);
            setTotalRecords(result.totalRecords || 0);
        } catch (err) {
            setError('Failed to load carts');
        } finally {
            setLoading(false);
        }
    };

    const openModal = async () => {
        setModalError('');
        setSelectedCustomer('');
        setShowModal(true);
        try {
            const response = await apiClient.post('/api/customer/list', {
                page: 0,
                sizePerPage: 100,
                sortDirection: 'ASC',
                sortField: 'id'
            });
            if (!response) return;
            setCustomers(response.data.dtoList || []);
        } catch (err) {
            setModalError('Failed to load customers');
        }
    };

    const handleInitialize = async () => {
        if (!selectedCustomer) {
            setModalError('Please select a customer');
            return;
        }
        setInitializing(true);
        setModalError('');
        try {
            const response = await apiClient.post('/api/cart/add', {
                identifier: selectedCustomer
            });
            if (!response) return;
            setShowModal(false);
            fetchData(page);
        } catch (err) {
            setModalError('Failed to initialize cart. Customer may already have one.');
        } finally {
            setInitializing(false);
        }
    };

    const handleDelete = async (identifier) => {
        if (!window.confirm(`Delete cart for ${identifier}?`)) return;
        try {
            const response = await apiClient.post('/api/cart/delete', { identifier });
            if (!response) return;
            setData(prev => prev.filter(item => item.identifier !== identifier));
            setTotalRecords(prev => prev - 1);
        } catch (err) {
            setError('Failed to delete cart');
        }
    };

    if (loading) return (
        <Layout>
            <div className='flex justify-center items-center h-64'>
                <p className='text-gray-400 text-sm'>Loading Carts...</p>
            </div>
        </Layout>
    );

    return (
        <Layout>
            <div className='p-6'>
                <div className='flex justify-between items-center mb-4'>
                    <div>
                        <h1 className='text-xl font-bold text-gray-800'>Carts</h1>
                        <p className='text-sm text-gray-500 mt-0.5'>
                            {totalRecords} total records — Page {page + 1} of {totalPages}
                        </p>
                    </div>
                    <button
                        onClick={openModal}
                        className='bg-blue-600 hover:bg-blue-700 text-white text-sm font-semibold py-2 px-4 rounded-lg transition'
                    >
                        + Initialize Cart
                    </button>
                </div>

                {error && (
                    <div className='bg-red-50 text-red-600 text-sm px-4 py-2 rounded-lg mb-4 border border-red-200'>
                        {error}
                    </div>
                )}

                {data.length === 0 ? (
                    <div className='bg-white border border-gray-200 rounded-lg p-12 text-center'>
                        <p className='text-gray-400 text-sm'>No carts found</p>
                    </div>
                ) : (
                    <>
                        <div className='bg-white border border-gray-200 rounded-lg overflow-hidden'>
                            <div className='overflow-x-auto'>
                                <table className='w-full text-sm'>
                                    <thead>
                                        <tr className='bg-gray-50 border-b border-gray-200'>
                                            <th className='px-4 py-3 text-left text-xs font-semibold text-gray-500 uppercase tracking-wide'>Customer</th>
                                            <th className='px-4 py-3 text-left text-xs font-semibold text-gray-500 uppercase tracking-wide'>Total Price</th>
                                            <th className='px-4 py-3 text-left text-xs font-semibold text-gray-500 uppercase tracking-wide'>Discount</th>
                                            <th className='px-4 py-3 text-left text-xs font-semibold text-gray-500 uppercase tracking-wide'>Original Price</th>
                                            <th className='px-4 py-3 text-left text-xs font-semibold text-gray-500 uppercase tracking-wide'>Status</th>
                                            <th className='px-4 py-3 text-left text-xs font-semibold text-gray-500 uppercase tracking-wide'>Actions</th>
                                        </tr>
                                    </thead>
                                    <tbody className='divide-y divide-gray-100'>
                                        {data.map((cart) => (
                                            <tr key={cart.identifier} className='hover:bg-gray-50 transition'>
                                                <td className='px-4 py-3 text-gray-700'>{cart.identifier}</td>
                                                <td className='px-4 py-3 text-gray-700'>
                                                    {cart.totalPrice != null ? `₹${cart.totalPrice.toLocaleString()}` : '-'}
                                                </td>
                                                <td className='px-4 py-3 text-gray-700'>
                                                    {cart.totalDiscount != null ? `₹${cart.totalDiscount.toLocaleString()}` : '-'}
                                                </td>
                                                <td className='px-4 py-3 text-gray-700'>
                                                    {cart.originalPrice != null ? `₹${cart.originalPrice.toLocaleString()}` : '-'}
                                                </td>
                                                <td className='px-4 py-3'>
                                                    <span className={`text-xs font-semibold px-2 py-0.5 rounded ${cart.status ? 'bg-green-50 text-green-700' : 'bg-gray-100 text-gray-500'}`}>
                                                        {cart.status ? 'Active' : 'Inactive'}
                                                    </span>
                                                </td>
                                                <td className='px-4 py-3'>
                                                    <div className='flex gap-2'>
                                                        <button
                                                            onClick={() => router.push(`/cart/manage/${cart.identifier}`)}
                                                            className='text-xs font-semibold text-blue-600 hover:text-blue-800 transition'
                                                        >
                                                            Manage
                                                        </button>
                                                        <span className='text-gray-300'>|</span>
                                                        <button
                                                            onClick={() => handleDelete(cart.identifier)}
                                                            className='text-xs font-semibold text-red-500 hover:text-red-700 transition'
                                                        >
                                                            Delete
                                                        </button>
                                                    </div>
                                                </td>
                                            </tr>
                                        ))}
                                    </tbody>
                                </table>
                            </div>
                        </div>

                        <div className='flex justify-between items-center mt-4'>
                            <p className='text-xs text-gray-500'>
                                Showing {page * sizePerPage + 1}–{Math.min((page + 1) * sizePerPage, totalRecords)} of {totalRecords}
                            </p>
                            <div className='flex gap-1'>
                                <button onClick={() => setPage(0)} disabled={page === 0} className='px-2 py-1 text-xs font-medium text-gray-600 bg-white border border-gray-200 rounded hover:bg-gray-50 disabled:opacity-40 disabled:cursor-not-allowed transition'>«</button>
                                <button onClick={() => setPage(prev => prev - 1)} disabled={page === 0} className='px-3 py-1 text-xs font-medium text-gray-600 bg-white border border-gray-200 rounded hover:bg-gray-50 disabled:opacity-40 disabled:cursor-not-allowed transition'>Prev</button>
                                {Array.from({ length: totalPages }, (_, i) => i)
                                    .filter(i => i === 0 || i === totalPages - 1 || Math.abs(i - page) <= 1)
                                    .map((i, idx, arr) => (
                                        <React.Fragment key={i}>
                                            {idx > 0 && arr[idx - 1] !== i - 1 && <span className='px-2 py-1 text-xs text-gray-400'>...</span>}
                                            <button
                                                onClick={() => setPage(i)}
                                                className={`px-3 py-1 text-xs font-medium border rounded transition ${page === i ? 'bg-blue-600 text-white border-blue-600' : 'text-gray-600 bg-white border-gray-200 hover:bg-gray-50'}`}
                                            >
                                                {i + 1}
                                            </button>
                                        </React.Fragment>
                                    ))
                                }
                                <button onClick={() => setPage(prev => prev + 1)} disabled={page >= totalPages - 1} className='px-3 py-1 text-xs font-medium text-gray-600 bg-white border border-gray-200 rounded hover:bg-gray-50 disabled:opacity-40 disabled:cursor-not-allowed transition'>Next</button>
                                <button onClick={() => setPage(totalPages - 1)} disabled={page >= totalPages - 1} className='px-2 py-1 text-xs font-medium text-gray-600 bg-white border border-gray-200 rounded hover:bg-gray-50 disabled:opacity-40 disabled:cursor-not-allowed transition'>»</button>
                            </div>
                        </div>
                    </>
                )}
            </div>

            {/* Initialize Cart Modal */}
            {showModal && (
                <div className='fixed inset-0 bg-black bg-opacity-40 flex items-center justify-center z-50'>
                    <div className='bg-white rounded-xl shadow-lg p-6 w-full max-w-sm mx-4'>
                        <h2 className='text-base font-bold text-gray-800 mb-1'>Initialize Cart</h2>
                        <p className='text-xs text-gray-500 mb-4'>Select a customer to create a new cart</p>

                        {modalError && (
                            <div className='bg-red-50 text-red-600 text-sm px-3 py-2 rounded-lg mb-3 border border-red-200'>
                                {modalError}
                            </div>
                        )}

                        <label className='text-xs font-semibold text-gray-500 uppercase tracking-wide mb-1 block'>Customer</label>
                        <select
                            value={selectedCustomer}
                            onChange={(e) => setSelectedCustomer(e.target.value)}
                            className='border border-gray-300 rounded-lg px-3 py-2.5 text-sm outline-none focus:border-blue-500 focus:ring-1 focus:ring-blue-200 transition w-full mb-4'
                        >
                            <option value=''>Select a customer</option>
                            {customers.map((c) => (
                                <option key={c.identifier} value={c.identifier}>
                                    {c.name ? `${c.name} (${c.identifier})` : c.identifier}
                                </option>
                            ))}
                        </select>

                        <div className='flex gap-3'>
                            <button
                                onClick={() => setShowModal(false)}
                                className='flex-1 bg-gray-100 text-gray-700 rounded-lg py-2.5 text-sm font-semibold hover:bg-gray-200 transition'
                            >
                                Cancel
                            </button>
                            <button
                                onClick={handleInitialize}
                                disabled={initializing}
                                className='flex-1 bg-blue-600 text-white rounded-lg py-2.5 text-sm font-semibold hover:bg-blue-700 transition disabled:opacity-50'
                            >
                                {initializing ? 'Initializing...' : 'Initialize'}
                            </button>
                        </div>
                    </div>
                </div>
            )}
        </Layout>
    );
}

export default CartList;