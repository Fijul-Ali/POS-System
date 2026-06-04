"use client";
import React, { useEffect, useState } from 'react';
import apiClient from '@/utils/apiClient';
import Layout from './layout';
import { useRouter } from 'next/navigation';

function List({ title, apiPath, columns, editPath, addPath }) {
    const router = useRouter();
    const [data, setData] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState('');
    const [page, setPage] = useState(0);
    const [totalPages, setTotalPages] = useState(0);
    const [totalRecords, setTotalRecords] = useState(0);
    const sizePerPage = 3;

    useEffect(() => {
        fetchData(page);
    }, [page]);

    const fetchData = async (currentPage) => {
        try {
            setLoading(true);
            const response = await apiClient.post(`/api/${apiPath}/list`, {
                    page: currentPage,
                    sizePerPage,
                    sortDirection: 'ASC',
                    sortField: 'id'
                })
            if (!response) return;
            const result = response.data;
            setData(result.dtoList || []);
            setTotalPages(result.totalPages || 0);
            setTotalRecords(result.totalRecords || 0);
        } catch (err) {
            setError('Failed to load data');
        } finally {
            setLoading(false);
        }
    };

    const handleDelete = async (identifier) => {
        if (!window.confirm(`Delete ${identifier}?`)) return;
        try {
            const response = await apiClient.get(`/api/${apiPath}/delete?identifier=${identifier}`);
            if (!response) return;
            const result = response.data;
            if (result) {
                setData(prev => prev.filter(item => item.identifier !== identifier));
                setTotalRecords(prev => prev - 1);
            }
        } catch (err) {
            setError('Failed to delete');
        }
    };

    const handleStatusChange = async (identifier, currentStatus) => {
        try {
            const response = await apiClient.post(`/api/${apiPath}/changestatus`, {
                 identifier, status: !currentStatus })
            if (!response) return;
            const result = response.data;
            if (result) {
                setData(prev => prev.map(item =>
                    item.identifier === identifier ? { ...item, status: !currentStatus } : item
                ));
            }
        } catch (err) {
            setError('Failed to update status');
        }
    };

    if (loading) return (
        <Layout>
            <div className='flex justify-center items-center h-64'>
                <p className='text-gray-400 text-sm'>Loading {title}...</p>
            </div>
        </Layout>
    );

    return (
        <Layout>
            <div className='p-6'>
                {/* Header */}
                <div className='flex justify-between items-center mb-4'>
                    <div>
                        <h1 className='text-xl font-bold text-gray-800'>{title}</h1>
                        <p className='text-sm text-gray-500 mt-0.5'>
                            {totalRecords} total records — Page {page + 1} of {totalPages}
                        </p>
                    </div>
                    <button
                        onClick={() => router.push(addPath)}
                        className='bg-blue-600 hover:bg-blue-700 text-white text-sm font-semibold py-2 px-4 rounded-lg transition'
                    >
                        + Add {title}
                    </button>
                </div>

                {error && (
                    <div className='bg-red-50 text-red-600 text-sm px-4 py-2 rounded-lg mb-4 border border-red-200'>
                        {error}
                    </div>
                )}

                {data.length === 0 ? (
                    <div className='bg-white border border-gray-200 rounded-lg p-12 text-center'>
                        <p className='text-gray-400 text-sm'>No {title} records found</p>
                    </div>
                ) : (
                    <>
                        <div className='bg-white border border-gray-200 rounded-lg overflow-hidden'>
                            <div className='overflow-x-auto'>
                                <table className='w-full text-sm'>
                                    <thead>
                                        <tr className='bg-gray-50 border-b border-gray-200'>
                                            {columns.map((col) => (
                                                <th key={col.key} className='px-4 py-3 text-left text-xs font-semibold text-gray-500 uppercase tracking-wide'>
                                                    {col.label}
                                                </th>
                                            ))}
                                            <th className='px-4 py-3 text-left text-xs font-semibold text-gray-500 uppercase tracking-wide'>Status</th>
                                            <th className='px-4 py-3 text-left text-xs font-semibold text-gray-500 uppercase tracking-wide'>Actions</th>
                                        </tr>
                                    </thead>
                                    <tbody className='divide-y divide-gray-100'>
                                        {data.map((item) => (
                                            <tr key={item.identifier} className='hover:bg-gray-50 transition'>
                                                {columns.map((col) => (
                                                    <td key={col.key} className='px-4 py-3 text-gray-700'>
                                                        {Array.isArray(item[col.key]) ? item[col.key].join(', ') : item[col.key] ?? '-'}
                                                    </td>
                                                ))}
                                                <td className='px-4 py-3'>
                                                    <button
                                                        onClick={() => handleStatusChange(item.identifier, item.status)}
                                                        className={`relative inline-flex items-center w-10 h-5 rounded-full transition-colors duration-200 focus:outline-none ${
                                                            item.status ? 'bg-blue-600' : 'bg-gray-300'
                                                        }`}
                                                    >
                                                        <span className={`inline-block w-3.5 h-3.5 bg-white rounded-full shadow transform transition-transform duration-200 ${
                                                            item.status ? 'translate-x-5' : 'translate-x-0.5'
                                                        }`} />
                                                    </button>
                                                </td>
                                                <td className='px-4 py-3'>
                                                    <div className='flex gap-2'>
                                                        <button
                                                            onClick={() => router.push(`${editPath}/${item.identifier}`)}
                                                            className='text-xs font-semibold text-blue-600 hover:text-blue-800 transition'
                                                        >
                                                            Edit
                                                        </button>
                                                        <span className='text-gray-300'>|</span>
                                                        <button
                                                            onClick={() => handleDelete(item.identifier)}
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

                        {/* Pagination */}
                        <div className='flex justify-between items-center mt-4'>
                            <p className='text-xs text-gray-500'>
                                Showing {page * sizePerPage + 1}–{Math.min((page + 1) * sizePerPage, totalRecords)} of {totalRecords}
                            </p>
                            <div className='flex gap-1'>
                                <button
                                    onClick={() => setPage(0)}
                                    disabled={page === 0}
                                    className='px-2 py-1 text-xs font-medium text-gray-600 bg-white border border-gray-200 rounded hover:bg-gray-50 disabled:opacity-40 disabled:cursor-not-allowed transition'
                                >
                                    «
                                </button>
                                <button
                                    onClick={() => setPage(prev => prev - 1)}
                                    disabled={page === 0}
                                    className='px-3 py-1 text-xs font-medium text-gray-600 bg-white border border-gray-200 rounded hover:bg-gray-50 disabled:opacity-40 disabled:cursor-not-allowed transition'
                                >
                                    Prev
                                </button>

                                {/* Page numbers */}
                                {Array.from({ length: totalPages }, (_, i) => i)
                                    .filter(i => i === 0 || i === totalPages - 1 || Math.abs(i - page) <= 1)
                                    .map((i, idx, arr) => (
                                        <React.Fragment key={i}>
                                            {idx > 0 && arr[idx - 1] !== i - 1 && (
                                                <span className='px-2 py-1 text-xs text-gray-400'>...</span>
                                            )}
                                            <button
                                                onClick={() => setPage(i)}
                                                className={`px-3 py-1 text-xs font-medium border rounded transition ${
                                                    page === i
                                                        ? 'bg-blue-600 text-white border-blue-600'
                                                        : 'text-gray-600 bg-white border-gray-200 hover:bg-gray-50'
                                                }`}
                                            >
                                                {i + 1}
                                            </button>
                                        </React.Fragment>
                                    ))
                                }

                                <button
                                    onClick={() => setPage(prev => prev + 1)}
                                    disabled={page >= totalPages - 1}
                                    className='px-3 py-1 text-xs font-medium text-gray-600 bg-white border border-gray-200 rounded hover:bg-gray-50 disabled:opacity-40 disabled:cursor-not-allowed transition'
                                >
                                    Next
                                </button>
                                <button
                                    onClick={() => setPage(totalPages - 1)}
                                    disabled={page >= totalPages - 1}
                                    className='px-2 py-1 text-xs font-medium text-gray-600 bg-white border border-gray-200 rounded hover:bg-gray-50 disabled:opacity-40 disabled:cursor-not-allowed transition'
                                >
                                    »
                                </button>
                            </div>
                        </div>
                    </>
                )}
            </div>
        </Layout>
    );
}

export default List;