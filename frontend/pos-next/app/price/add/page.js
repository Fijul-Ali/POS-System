"use client";
import React,{useState} from "react";
import Add from "@/app/components/add";
import SingleDropdown from "@/app/components/singledropdown";

function PriceAdd() {

    const [product, setProduct] = useState('');

    const extraFields = [
        { key: 'product', type:'custom',
            component: (
                <SingleDropdown
                    value={product}
                    label="Product"
                    apiPath="product"
                    onChange={(val) => setProduct(val)}
                    required
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
        <Add
            title='Price'
            apiPath='price'
            extraFields={extraFields}
            extraData={{ product }}
        />
    );
}

export default PriceAdd;