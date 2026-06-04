"use client";
import React, { useEffect, useState } from 'react';
import Layout from '@/app/components/layout';
import apiClient from '@/utils/apiClient';
import { useRouter } from 'next/navigation';

function Home() {
    const [user, setUser] = useState(null);
    const [stats, setStats] = useState({
        totalOrders: 0,
        completedOrders: 0,
        cancelledOrders: 0,
        totalRevenue: 0,
        activeCarts: 0,
    });
    const [recentOrders, setRecentOrders] = useState([]);
    const [loading, setLoading] = useState(true);
    const router = useRouter();

    useEffect(() => {
        fetchAll();
    }, []);

    const fetchAll = async () => {
        setLoading(true);
        try {
            await Promise.all([fetchUser(), fetchOrderStats(), fetchCartStats()]);
        } finally {
            setLoading(false);
        }
    };

    const fetchUser = async () => {
        try {
            const username = localStorage.getItem('username');
            const response = await apiClient.get(`/api/user/get?username=${username}`);
            if (!response) return;
            setUser(response.data);
        } catch (err) {}
    };

    const fetchOrderStats = async () => {
        try {
            const response = await apiClient.post('/api/order/list', {
                page: 0,
                sizePerPage: 100,
                sortDirection: 'DESC',
                sortField: 'id'
            });
            if (!response) return;
            const orders = response.data.dtoList || [];
            const completed = orders.filter(o => o.orderStatus === 'COMPLETED');
            const cancelled = orders.filter(o => o.orderStatus === 'CANCELLED');
            const revenue = completed.reduce((sum, o) => sum + (Number(o.totalAmount) || 0), 0);
            setStats(prev => ({
                ...prev,
                totalOrders: response.data.totalRecords || 0,
                completedOrders: completed.length,
                cancelledOrders: cancelled.length,
                totalRevenue: revenue,
            }));
            setRecentOrders(orders.slice(0, 5));
        } catch (err) {}
    };

    const fetchCartStats = async () => {
        try {
            const response = await apiClient.post('/api/cart/list', {
                page: 0,
                sizePerPage: 100,
                sortDirection: 'ASC',
                sortField: 'id'
            });
            if (!response) return;
            const carts = response.data.dtoList || [];
            setStats(prev => ({
                ...prev,
                activeCarts: carts.filter(c => c.status).length,
            }));
        } catch (err) {}
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

    const quickActions = [
        { label: 'Products', path: '/product' },
        { label: 'Brands', path: '/brand' },
        { label: 'Categories', path: '/category' },
        { label: 'Users', path: '/user' },
        { label: 'Customers', path: '/customer' },
        { label: 'Cart', path: '/cart' },
        { label: 'Orders', path: '/order' },
        { label: 'Stock', path: '/stock' },
    ];

    return (
        <Layout>
            <div className='p-6'>
                {/* Header */}
                <div className='mb-6'>
                    <h1 className='text-xl font-bold text-gray-800'>
                        Welcome, {user?.name || 'User'}
                    </h1>
                    <p className='text-sm text-gray-500 mt-0.5'>
                        {new Date().toLocaleDateString('en-US', { weekday: 'long', year: 'numeric', month: 'long', day: 'numeric' })}
                    </p>
                </div>

                {/* Stat Cards */}
                <div className='grid grid-cols-2 md:grid-cols-4 gap-4 mb-6'>
                    <div className='bg-white border border-gray-200 rounded-lg p-4'>
                        <p className='text-xs font-semibold text-gray-400 uppercase tracking-wide mb-1'>Total Orders</p>
                        <p className='text-2xl font-bold text-gray-800'>{loading ? '—' : stats.totalOrders}</p>
                    </div>
                    <div className='bg-white border border-gray-200 rounded-lg p-4'>
                        <p className='text-xs font-semibold text-gray-400 uppercase tracking-wide mb-1'>Completed</p>
                        <p className='text-2xl font-bold text-green-600'>{loading ? '—' : stats.completedOrders}</p>
                    </div>
                    <div className='bg-white border border-gray-200 rounded-lg p-4'>
                        <p className='text-xs font-semibold text-gray-400 uppercase tracking-wide mb-1'>Active Carts</p>
                        <p className='text-2xl font-bold text-blue-600'>{loading ? '—' : stats.activeCarts}</p>
                    </div>
                    <div className='bg-white border border-gray-200 rounded-lg p-4'>
                        <p className='text-xs font-semibold text-gray-400 uppercase tracking-wide mb-1'>Total Revenue</p>
                        <p className='text-2xl font-bold text-gray-800'>
                            {loading ? '—' : `₹${stats.totalRevenue.toLocaleString()}`}
                        </p>
                    </div>
                </div>

                <div className='grid grid-cols-1 md:grid-cols-3 gap-6'>
                    {/* Recent Orders */}
                    <div className='md:col-span-2 bg-white border border-gray-200 rounded-lg overflow-hidden'>
                        <div className='flex justify-between items-center px-4 py-3 border-b border-gray-100'>
                            <h2 className='text-sm font-bold text-gray-700'>Recent Orders</h2>
                            <button
                                onClick={() => router.push('/order')}
                                className='text-xs text-blue-600 hover:text-blue-800 font-semibold transition'
                            >
                                View all →
                            </button>
                        </div>
                        {loading ? (
                            <div className='p-8 text-center'>
                                <p className='text-gray-400 text-sm'>Loading...</p>
                            </div>
                        ) : recentOrders.length === 0 ? (
                            <div className='p-8 text-center'>
                                <p className='text-gray-400 text-sm'>No orders yet</p>
                            </div>
                        ) : (
                            <table className='w-full text-sm'>
                                <thead>
                                    <tr className='bg-gray-50 border-b border-gray-200'>
                                        <th className='px-4 py-2.5 text-left text-xs font-semibold text-gray-500 uppercase tracking-wide'>Order ID</th>
                                        <th className='px-4 py-2.5 text-left text-xs font-semibold text-gray-500 uppercase tracking-wide'>Customer</th>
                                        <th className='px-4 py-2.5 text-left text-xs font-semibold text-gray-500 uppercase tracking-wide'>Amount</th>
                                        <th className='px-4 py-2.5 text-left text-xs font-semibold text-gray-500 uppercase tracking-wide'>Date</th>
                                        <th className='px-4 py-2.5 text-left text-xs font-semibold text-gray-500 uppercase tracking-wide'>Status</th>
                                    </tr>
                                </thead>
                                <tbody className='divide-y divide-gray-100'>
                                    {recentOrders.map((order) => (
                                        <tr key={order.identifier} className='hover:bg-gray-50 transition'>
                                            <td className='px-4 py-3 text-xs font-mono text-gray-400'>
                                                {order.identifier?.slice(0, 8)}...
                                            </td>
                                            <td className='px-4 py-3 text-gray-700'>{order.customer || '-'}</td>
                                            <td className='px-4 py-3 font-semibold text-gray-800'>
                                                {order.totalAmount != null ? `₹${Number(order.totalAmount).toLocaleString()}` : '-'}
                                            </td>
                                            <td className='px-4 py-3 text-xs text-gray-500'>{formatDate(order.placedAt)}</td>
                                            <td className='px-4 py-3'>{statusBadge(order.orderStatus)}</td>
                                        </tr>
                                    ))}
                                </tbody>
                            </table>
                        )}
                    </div>

                    {/* Right column */}
                    <div className='flex flex-col gap-4'>
                        {/* User Info */}
                        <div className='bg-white border border-gray-200 rounded-lg p-4'>
                            <h2 className='text-sm font-bold text-gray-700 mb-3'>Logged in as</h2>
                            <p className='text-sm font-semibold text-gray-800 mb-1'>{user?.name || '—'}</p>
                            <p className='text-xs text-gray-500 mb-2'>{user?.username}</p>
                            <div className='flex gap-1 flex-wrap'>
                                {user?.roles?.map((role, index) => (
                                    <span key={index} className='bg-blue-50 text-blue-700 text-xs font-semibold px-2 py-0.5 rounded'>
                                        {role}
                                    </span>
                                ))}
                            </div>
                        </div>

                        {/* Quick Access */}
                        <div className='bg-white border border-gray-200 rounded-lg p-4'>
                            <h2 className='text-sm font-bold text-gray-700 mb-3'>Quick Access</h2>
                            <div className='grid grid-cols-2 gap-2'>
                                {quickActions.map((action) => (
                                    <button
                                        key={action.label}
                                        onClick={() => router.push(action.path)}
                                        className='text-xs font-semibold text-gray-600 bg-gray-50 hover:bg-blue-50 hover:text-blue-700 border border-gray-200 rounded-lg py-2.5 px-3 text-left transition'
                                    >
                                        {action.label}
                                    </button>
                                ))}
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </Layout>
    );
}

export default Home;