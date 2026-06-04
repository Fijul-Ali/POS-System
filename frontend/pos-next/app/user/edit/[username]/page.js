"use client";
import React, { useEffect, useState } from 'react';
import { useRouter } from 'next/navigation';
import apiClient from '@/utils/apiClient';
import Layout from '@/app/components/layout';
import { useParams } from 'next/navigation';
import MultiDropdown from '@/app/components/multidropdown';

function UserEdit() {
    const router = useRouter();
    const { username } = useParams();
    const [formData, setFormData] = useState({
        username: '',
        name: '',
        phoneNo: '',
        id:''
    });
    const [roles, setRoles] = useState([]);
    const [error, setError] = useState('');
    const [success, setSuccess] = useState('');
    const [loading, setLoading] = useState(true);
    const [saving, setSaving] = useState(false);

    const inputClass = 'border border-gray-300 rounded-lg px-3 py-2.5 text-sm outline-none focus:border-blue-500 focus:ring-1 focus:ring-blue-200 transition w-full';
    const labelClass = 'text-xs font-semibold text-gray-500 uppercase tracking-wide mb-1 block';

    useEffect(() => {
        const fetchUser = async () => {
            try {
                const response = await apiClient.get(`/api/user/get?username=${username}`);
                if (!response) return;
                const result = response.data;
                setFormData({
                    username: result.username || '',
                    name: result.name || '',
                    phoneNo: result.phoneNo || '',
                    id: result.id || ''
                });
                setRoles(result.roles || []);
            } catch (err) {
                setError('Failed to load user');
            } finally {
                setLoading(false);
            }
        };
        fetchUser();
    }, []);

    const handleChange = (e) => {
        setFormData({ ...formData, [e.target.name]: e.target.value });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setError('');
        setSuccess('');
        setSaving(true);
        try {
            const response = await apiClient.post('/api/user/update',
                { ...formData, roles });
                debugger
            if (!response) return;
            const result = response.data;
            if (result.success) {
                setSuccess('User updated successfully');
                router.push('/user')
            } else {
                setError(data.message || 'Failed to update');
            }
        } catch (err) {
            setError('Unable to connect to server.');
        } finally {
            setSaving(false);
        }
    };

    if (loading) return (
        <Layout>
            <div className='flex justify-center items-center h-64'>
                <p className='text-gray-400 text-sm'>Loading user...</p>
            </div>
        </Layout>
    );

    return (
        <Layout>
            <div className='p-6'>
                <div className='mb-6'>
                    <h1 className='text-xl font-bold text-gray-800'>Edit User</h1>
                    <p className='text-sm text-gray-500 mt-0.5'>Update user details</p>
                </div>

                <div className='bg-white border border-gray-200 rounded-lg p-6 max-w-lg'>
                    {error && <div className='bg-red-50 text-red-600 text-sm px-3 py-2 rounded-lg mb-4 border border-red-200'>{error}</div>}
                    {success && <div className='bg-green-50 text-green-700 text-sm px-3 py-2 rounded-lg mb-4 border border-green-200'>{success}</div>}

                    <form onSubmit={handleSubmit} className='flex flex-col gap-4'>
                        <div>
                            <label className={labelClass}>Email (Username)</label>
                            <input
                                type='email'
                                value={formData.username}
                                readOnly
                                className={`${inputClass} bg-gray-50 text-gray-500 cursor-not-allowed`}
                            />
                        </div>
                        <div>
                            <label className={labelClass}>Full Name</label>
                            <input type='text' name='name' value={formData.name} onChange={handleChange} placeholder='Enter full name' required className={inputClass} />
                        </div>
                        <div>
                            <label className={labelClass}>Phone Number</label>
                            <input
                                type='tel'
                                name='phoneNo'
                                value={formData.phoneNo}
                                onChange={handleChange}
                                placeholder='Enter 10 digit phone number'
                                maxLength={10}
                                onKeyDown={(e) => {
                                    if (!/[0-9]/.test(e.key) && e.key !== 'Backspace' && e.key !== 'Delete' && e.key !== 'Tab') {
                                        e.preventDefault();
                                    }
                                }}
                                required
                                className={inputClass}
                            />
                        </div>
                        <div>
                            <MultiDropdown
                                value={roles}
                                onChange={(val) => setRoles(val)}
                                label='Roles'
                                apiPath='role'
                            />
                        </div>

                        <div className='flex gap-3 pt-2'>
                            <button type='button' onClick={() => router.push('/user')} className='flex-1 bg-gray-100 text-gray-700 rounded-lg py-2.5 text-sm font-semibold hover:bg-gray-200 transition'>Cancel</button>
                            <button type='submit' disabled={saving} className='flex-1 bg-blue-600 text-white rounded-lg py-2.5 text-sm font-semibold hover:bg-blue-700 transition disabled:opacity-50'>{saving ? 'Saving...' : 'Update User'}</button>
                        </div>
                    </form>
                </div>
            </div>
        </Layout>
    );
}

export default UserEdit;