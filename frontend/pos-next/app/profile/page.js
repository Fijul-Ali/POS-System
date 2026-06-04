"use client";
import React, { useEffect, useState } from 'react';
import Layout from '@/app/components/layout';
import apiClient from '@/utils/apiClient';

function Profile() {
    const [user, setUser] = useState(null);

    useEffect(() => {
        const fetchProfile = async () => {
            const username = localStorage.getItem('username');
            const response = await apiClient.get(`/api/user/get?username=${username}`);
            if (!response) return;
            const result = response.data;
            setUser(result);
        };
        fetchProfile();
    }, []);

    if (!user) return (
        <Layout>
            <div className='flex justify-center items-center h-64'>
                <p className='text-gray-400 text-sm'>Loading...</p>
            </div>
        </Layout>
    );

    return (
        <Layout>
            <div className='grid place-items-center'>
                <div className='mb-6 flex-items-center justify-center'>
                    <h1 className='font-bold text-gray-800 text-4xl flex items-center justify-center'>Profile</h1>
                    <p className='text-sm text-gray-500 flex items-center justify-center'>Your account details</p>
                </div>

                <div className='bg-white border border-gray-200 rounded-lg overflow-hidden max-w-lg w-full max-w-md'>
                    <div className='bg-gray-800 px-6 py-5'>
                        <div className='w-12 h-12 bg-blue-600 rounded-full flex items-center justify-center mb-3'>
                            <span className='text-lg font-bold text-white'>
                                {user.name?.charAt(0).toUpperCase()}
                            </span>
                        </div>
                        <h2 className='text-base font-bold text-white'>{user.name}</h2>
                        <p className='text-sm text-gray-400'>{user.username}</p>
                    </div>

                    <div className='divide-y divide-gray-100'>
                        <div className='px-6 py-4'>
                            <p className='text-xs font-semibold text-gray-500 uppercase tracking-wide mb-1'>Full Name</p>
                            <p className='text-sm font-medium text-gray-800'>{user.name}</p>
                        </div>
                        <div className='px-6 py-4'>
                            <p className='text-xs font-semibold text-gray-500 uppercase tracking-wide mb-1'>Email</p>
                            <p className='text-sm font-medium text-gray-800'>{user.username}</p>
                        </div>
                        <div className='px-6 py-4'>
                            <p className='text-xs font-semibold text-gray-500 uppercase tracking-wide mb-1'>Phone</p>
                            <p className='text-sm font-medium text-gray-800'>{user.phoneNo}</p>
                        </div>
                        <div className='px-6 py-4'>
                            <p className='text-xs font-semibold text-gray-500 uppercase tracking-wide mb-1'>Roles</p>
                            <div className='flex gap-2 flex-wrap mt-1'>
                                {user.roles?.map((role, index) => (
                                    <span key={index} className='bg-blue-50 text-blue-700 text-xs font-semibold px-2 py-1 rounded'>
                                        {role}
                                    </span>
                                ))}
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </Layout>
    );
}

export default Profile;