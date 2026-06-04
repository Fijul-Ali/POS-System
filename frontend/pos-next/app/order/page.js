"use client";
import React, { useEffect, useState } from 'react';
import { useRouter } from 'next/navigation';
import apiClient from '@/utils/apiClient';
import Layout from '@/app/components/layout';

function OrderList() {
    const router = useRouter();
    const [data, setData] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState('');
    const [page, setPage] = useState(0);
    const [totalPages, setTotalPages] = useState(0);
    const [totalRecords, setTotalRecords] = useState(0);
    const sizePerPage = 5;

    useEffect(() => {
        fetchData(page);
    }, [page]);

    const fetchData = async (currentPage) => {
        try {
            setLoading(true);
            const response = await apiClient.post('/api/order/list', {
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
            setError('Failed to load orders');
        } finally {
            setLoading(false);
        }
    };

    const handleCancel = async (identifier) => {
        if (!window.confirm('Cancel this order?')) return;
        try {
            const response = await apiClient.post('/api/order/cancel', { identifier });
            if (!response) return;
            setData(prev => prev.map(item =>
                item.identifier === identifier ? { ...item, orderStatus: 'CANCELLED' } : item
            ));
        } catch (err) {
            setError('Failed to cancel order');
        }
    };

    const statusBadge = (status) => {
        const styles = {
            COMPLETED: 'bg-green-50 text-green-700',
            CANCELLED: 'bg-red-50 text-red-600',
            PENDING: 'bg-yellow-50 text-yellow-700',
        };
        return (
            <span className={`text-xs font-semibold px-2 py-0.5 rounded ${styles[status] || 'bg-gray-100 text-gray-500'}`}>
                {status || '-'}
            </span>
        );
    };

    const formatDate = (placedAt) => {
        if (!placedAt) return '-';
        return new Date(placedAt).toLocaleString('en-IN', {
            day: '2-digit', month: 'short', year: 'numeric',
            hour: '2-digit', minute: '2-digit'
        });
    };

    if (loading) return (
        <Layout>
            <div className='flex justify-center items-center h-64'>
                <p className='text-gray-400 text-sm'>Loading Orders...</p>
            </div>
        </Layout>
    );

    return (
        <Layout>
            <div className='p-6'>
                <div className='flex justify-between items-center mb-4'>
                    <div>
                        <h1 className='text-xl font-bold text-gray-800'>Orders</h1>
                        <p className='text-sm text-gray-500 mt-0.5'>
                            {totalRecords} total records — Page {page + 1} of {totalPages}
                        </p>
                    </div>
                </div>

                {error && (
                    <div className='bg-red-50 text-red-600 text-sm px-4 py-2 rounded-lg mb-4 border border-red-200'>
                        {error}
                    </div>
                )}

                {data.length === 0 ? (
                    <div className='bg-white border border-gray-200 rounded-lg p-12 text-center'>
                        <p className='text-gray-400 text-sm'>No orders found</p>
                    </div>
                ) : (
                    <>
                        <div className='bg-white border border-gray-200 rounded-lg overflow-hidden'>
                            <div className='overflow-x-auto'>
                                <table className='w-full text-sm'>
                                    <thead>
                                        <tr className='bg-gray-50 border-b border-gray-200'>
                                            <th className='px-4 py-3 text-left text-xs font-semibold text-gray-500 uppercase tracking-wide'>Order ID</th>
                                            <th className='px-4 py-3 text-left text-xs font-semibold text-gray-500 uppercase tracking-wide'>Customer</th>
                                            <th className='px-4 py-3 text-left text-xs font-semibold text-gray-500 uppercase tracking-wide'>Cart</th>
                                            <th className='px-4 py-3 text-left text-xs font-semibold text-gray-500 uppercase tracking-wide'>Total Amount</th>
                                            <th className='px-4 py-3 text-left text-xs font-semibold text-gray-500 uppercase tracking-wide'>Discount</th>
                                            <th className='px-4 py-3 text-left text-xs font-semibold text-gray-500 uppercase tracking-wide'>Placed At</th>
                                            <th className='px-4 py-3 text-left text-xs font-semibold text-gray-500 uppercase tracking-wide'>Status</th>
                                            <th className='px-4 py-3 text-left text-xs font-semibold text-gray-500 uppercase tracking-wide'>Actions</th>
                                        </tr>
                                    </thead>
                                    <tbody className='divide-y divide-gray-100'>
                                        {data.map((order) => (
                                            <tr key={order.identifier} className='hover:bg-gray-50 transition'>
                                                <td className='px-4 py-3 text-gray-500 text-xs font-mono'>
                                                    {order.identifier?.slice(0, 8)}...
                                                </td>
                                                <td className='px-4 py-3 text-gray-700'>{order.customer || '-'}</td>
                                                <td className='px-4 py-3 text-gray-700'>{order.cart || '-'}</td>
                                                <td className='px-4 py-3 text-gray-800 font-semibold'>
                                                    {order.totalAmount != null ? `₹${Number(order.totalAmount).toLocaleString()}` : '-'}
                                                </td>
                                                <td className='px-4 py-3 text-green-600'>
                                                    {order.discount != null ? `₹${Number(order.discount).toLocaleString()}` : '-'}
                                                </td>
                                                <td className='px-4 py-3 text-gray-500 text-xs'>{formatDate(order.placedAt)}</td>
                                                <td className='px-4 py-3'>{statusBadge(order.orderStatus)}</td>
                                                <td className='px-4 py-3'>
                                                    {order.orderStatus !== 'CANCELLED' ? (
                                                        <button
                                                            onClick={() => handleCancel(order.identifier)}
                                                            className='text-xs font-semibold text-red-500 hover:text-red-700 transition'
                                                        >
                                                            Cancel
                                                        </button>
                                                    ) : (
                                                        <span className='text-xs text-gray-300'>—</span>
                                                    )}
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
        </Layout>
    );
}

export default OrderList;