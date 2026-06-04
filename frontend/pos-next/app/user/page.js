"use client";
import React, { useEffect, useState } from 'react';
import { useRouter } from 'next/navigation';
import apiClient from '@/utils/apiClient';
import Layout from '../components/layout';

function UserList() {
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
            const response = await apiClient.post('/api/user/list', {
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
            setError('Failed to load users');
        } finally {
            setLoading(false);
        }
    };

    const handleDelete = async (username) => {
        if (!window.confirm(`Delete user ${username}?`)) return;
        try {
            const response = await apiClient.get(`/api/user/delete?username=${username}`);
            if (!response) return;
            const result = response.data;
            if (result) {
                setData(prev => prev.filter(item => item.username !== username));
                setTotalRecords(prev => prev - 1);
            }
        } catch (err) {
            setError('Failed to delete');
        }
    };

    const handleStatusChange = async (username, currentStatus) => {
        try {
            const response = await apiClient.post('/api/user/changestatus', 
                { username, status: !currentStatus })
            if (!response) return;
            const result = response.data;
            if (result) {
                setData(prev => prev.map(item =>
                    item.username === username ? { ...item, status: !currentStatus } : item
                ));
            }
        } catch (err) {
            setError('Failed to update status');
        }
    };

    if (loading) return (
        <Layout>
            <div className='flex justify-center items-center h-64'>
                <p className='text-gray-400 text-sm'>Loading Users...</p>
            </div>
        </Layout>
    );

    return (
        <Layout>
            <div className='p-6'>
                <div className='flex justify-between items-center mb-4'>
                    <div>
                        <h1 className='text-xl font-bold text-gray-800'>Users</h1>
                        <p className='text-sm text-gray-500 mt-0.5'>
                            {totalRecords} total records — Page {page + 1} of {totalPages}
                        </p>
                    </div>
                    <button
                        onClick={() => router.push('/user/add')}
                        className='bg-blue-600 hover:bg-blue-700 text-white text-sm font-semibold py-2 px-4 rounded-lg transition'
                    >
                        + Add User
                    </button>
                </div>

                {error && (
                    <div className='bg-red-50 text-red-600 text-sm px-4 py-2 rounded-lg mb-4 border border-red-200'>
                        {error}
                    </div>
                )}

                {data.length === 0 ? (
                    <div className='bg-white border border-gray-200 rounded-lg p-12 text-center'>
                        <p className='text-gray-400 text-sm'>No users found</p>
                    </div>
                ) : (
                    <>
                        <div className='bg-white border border-gray-200 rounded-lg overflow-hidden'>
                            <div className='overflow-x-auto'>
                                <table className='w-full text-sm'>
                                    <thead>
                                        <tr className='bg-gray-50 border-b border-gray-200'>
                                            <th className='px-4 py-3 text-left text-xs font-semibold text-gray-500 uppercase tracking-wide'>Name</th>
                                            <th className='px-4 py-3 text-left text-xs font-semibold text-gray-500 uppercase tracking-wide'>Username</th>
                                            <th className='px-4 py-3 text-left text-xs font-semibold text-gray-500 uppercase tracking-wide'>Phone</th>
                                            <th className='px-4 py-3 text-left text-xs font-semibold text-gray-500 uppercase tracking-wide'>Roles</th>
                                            <th className='px-4 py-3 text-left text-xs font-semibold text-gray-500 uppercase tracking-wide'>Status</th>
                                            <th className='px-4 py-3 text-left text-xs font-semibold text-gray-500 uppercase tracking-wide'>Actions</th>
                                        </tr>
                                    </thead>
                                    <tbody className='divide-y divide-gray-100'>
                                        {data.map((user) => (
                                            <tr key={user.username} className='hover:bg-gray-50 transition'>
                                                <td className='px-4 py-3 text-gray-700'>{user.name || '-'}</td>
                                                <td className='px-4 py-3 text-gray-700'>{user.username}</td>
                                                <td className='px-4 py-3 text-gray-700'>{user.phoneNo || '-'}</td>
                                                <td className='px-4 py-3 text-gray-700'>
                                                    <div className='flex gap-1 flex-wrap'>
                                                        {user.roles?.map((role, i) => (
                                                            <span key={i} className='bg-blue-50 text-blue-700 text-xs font-semibold px-2 py-0.5 rounded'>
                                                                {role}
                                                            </span>
                                                        ))}
                                                    </div>
                                                </td>
                                                <td className='px-4 py-3'>
                                                    <button
                                                        onClick={() => handleStatusChange(user.username, user.status)}
                                                        className={`relative inline-flex items-center w-10 h-5 rounded-full transition-colors duration-200 focus:outline-none ${
                                                            user.status ? 'bg-blue-600' : 'bg-gray-300'
                                                        }`}
                                                    >
                                                        <span className={`inline-block w-3.5 h-3.5 bg-white rounded-full shadow transform transition-transform duration-200 ${
                                                            user.status ? 'translate-x-5' : 'translate-x-0.5'
                                                        }`} />
                                                    </button>
                                                </td>
                                                <td className='px-4 py-3'>
                                                    <div className='flex gap-2'>
                                                        <button
                                                            onClick={() => router.push(`/user/edit/${user.username}`)}
                                                            className='text-xs font-semibold text-blue-600 hover:text-blue-800 transition'
                                                        >
                                                            Edit
                                                        </button>
                                                        <span className='text-gray-300'>|</span>
                                                        <button
                                                            onClick={() => handleDelete(user.username)}
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

export default UserList;