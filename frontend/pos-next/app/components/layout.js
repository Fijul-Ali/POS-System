"use client";
import React from 'react';
import Sidebar from './sidebar';

function Layout({ children }) {
    return (
        <div className='flex h-screen bg-gray-100'>
            <div className='h-screen sticky top-0'>
                <Sidebar />
            </div>
            <div className='flex-1 overflow-y-auto'>
                {children}
            </div>
        </div>
    );
}

export default Layout;