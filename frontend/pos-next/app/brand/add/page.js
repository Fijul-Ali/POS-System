"use client";
import React from 'react';
import Add from '@/app/components/add';

function BrandAdd() {
    return (
            <Add
                title='Brand'
                apiPath='brand'
                extraFields={[]}
            />
    );
}

export default BrandAdd;