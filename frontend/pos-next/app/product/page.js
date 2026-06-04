import React from 'react';
import List from '@/app/components/list';

function Product() {
    const columns = [
        { key: 'identifier', label: 'Identifier' },
        { key: 'brand', label: 'Brand' },
        { key: 'model', label: 'Model' },
        { key: 'unit', label: 'Unit' },
        { key: 'categories', label: 'Categories' },
        { key: 'description', label: 'Description' }
    ];

    return (
        <List
            title='Product'
            apiPath='product'
            columns={columns}
            addPath='/product/add'
            editPath='/product/edit'
        />
    );
}

export default Product;