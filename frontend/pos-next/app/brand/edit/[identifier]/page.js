"use client";
import React from 'react';
import Edit from '@/app/components/edit';

function BrandEdit() {
    return (
        <Edit
            title='Brand'
            apiPath='brand'
            extraFields={[]}
        />
    );
}

export default BrandEdit;