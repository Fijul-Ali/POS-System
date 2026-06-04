"use client";
import Edit from "@/app/components/edit";
import { useState } from "react";
import SingleDropdown from "@/app/components/singledropdown";

function StockEdit() {

    const [product, setProduct] = useState('');
    const [warehouse, setWarehouse] = useState('');

    const handleLoad = (data) => {
        setProduct(data.product || '');
        setWarehouse(data.warehouse || '');
    };

    const extraFields = [
        { key: 'product', type: 'custom',
            component: (
                <SingleDropdown
                    label='Product'
                    value={product}
                    onChange={setProduct}
                    apiPath='product'
                />
            )
        },
        { key: 'warehouse', type: 'custom',
            component: (
                <SingleDropdown
                    label='Warehouse'
                    value={warehouse}
                    onChange={setWarehouse}
                    apiPath='warehouse'
                />
            )
        },
        { key: 'quantity', label: 'Quantity', type: 'number' }
    ];

    return (
        <Edit
            title='Stock'
            apiPath='stock'
            extraFields={extraFields}
            onLoad={handleLoad}
            extraData={{ product, warehouse }}
        />
    );
}

export default StockEdit;