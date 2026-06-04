"use client";
import Edit from "@/app/components/edit";
import SingleDropdown from "@/app/components/singledropdown";
import { useState } from "react";

function PriceEdit() {

    const [product, setProduct] = useState('');

    const handleLoad = (data) => {
        setProduct(data.product || '');
    };
    const extraFields = [
        { key: 'product', type: 'custom',
            component: (
                <SingleDropdown
                    value={product}
                    label='Product'
                    apiPath='product'
                    onChange={(val) => setProduct(val)}
                />
            )
        },
        { key: 'costPrice', label: 'Cost Price', type: 'number' },
        { key: 'effectiveFrom', label: 'Effective From', type: 'datetime-local' },
        { key: 'effectiveTo', label: 'Effective To', type: 'datetime-local' },
        { key: 'mrp', label: 'MRP', type: 'number' },
        { key: 'sellingPrice', label: 'Selling Price', type: 'number' }
    ];

    return (
        <Edit
            title='Price'
            apiPath='price'
            extraFields={extraFields}
            extraData={{ product }}
            onLoad={handleLoad}
        />
    );
}

export default PriceEdit;