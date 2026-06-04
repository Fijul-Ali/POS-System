"use client";
import React, { useState } from 'react';
import { useRouter } from 'next/navigation';
import axios from 'axios';

function Login({}) {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState('');
    const [loading, setLoading] = useState(false);
    const router = useRouter();

    const handleLogin = async (e) => {
        e.preventDefault();
        setError('');
        setLoading(true);

        try {

            const response = await axios.post('http://localhost:8080/api/authenticate',{
                username, 
                password
            })
            const result = response.data;
            debugger

            if (result.token && result.token !== 'Error') {
                localStorage.setItem('token', result.token);
                localStorage.setItem('username', username);
                router.push("/home");
            } else {
                setError('Invalid username or password');
            }
        } catch (err) {
            setError('Unable to connect to server. Please try again.');
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className='min-h-screen bg-gradient-to-br from-blue-50 to-indigo-100 flex items-center justify-center px-4'>
            <div className='bg-white rounded-2xl shadow-xl p-10 w-full max-w-md'>
                <div className='text-center mb-8'>
                    <h1 className='text-3xl font-extrabold text-gray-800'>Welcome Back</h1>
                    <p className='text-gray-500 text-sm mt-2'>Sign in to your POS account</p>
                </div>

                {error && (
                    <div className='bg-red-50 text-red-500 text-sm text-center px-4 py-3 rounded-xl mb-4'>
                        {error}
                    </div>
                )}

                <form onSubmit={handleLogin} className='flex flex-col gap-5'>
                    <div className='flex flex-col gap-1'>
                        <label className='text-sm font-semibold text-gray-600'>Email</label>
                        <input
                            type='email'
                            placeholder='Enter your email'
                            value={username}
                            onChange={(e) => setUsername(e.target.value)}
                            required
                            className='border border-gray-300 rounded-xl px-4 py-3 text-sm outline-none focus:border-indigo-500 focus:ring-2 focus:ring-indigo-100 transition text-gray-700'
                        />
                    </div>
                    <div className='flex flex-col gap-1'>
                        <label className='text-sm font-semibold text-gray-600'>Password</label>
                        <input
                            type='password'
                            placeholder='Enter your password'
                            value={password}
                            onChange={(e) => setPassword(e.target.value)}
                            required
                            className='border border-gray-300 rounded-xl px-4 py-3 text-sm outline-none focus:border-indigo-500 focus:ring-2 focus:ring-indigo-100 transition text-gray-700'
                        />
                    </div>
                    <button
                        type='submit'
                        disabled={loading}
                        className='bg-indigo-600 text-white rounded-xl py-3 font-bold text-sm hover:bg-indigo-700 transition mt-2 disabled:opacity-50 disabled:cursor-not-allowed'
                    >
                        {loading ? 'Signing in...' : 'Sign In'}
                    </button>
                    <div className='text-center'>
                        <span className='text-gray-500 text-sm'>Don't have an account? </span>
                        <button
                            type='button'
                            onClick={() => router.push('/register')}
                            className='text-indigo-600 text-sm font-semibold hover:underline'
                        >
                            Register
                        </button>
                    </div>
                </form>
            </div>
        </div>
    );
}

export default Login;