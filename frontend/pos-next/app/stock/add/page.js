"use client";
import Add from "@/app/components/add";
import SingleDropdown from "@/app/components/singledropdown";
import { useState } from "react";

function StockAdd() {
    const [product, setProduct] = useState('');
    const [warehouse, setWarehouse] = useState('');

    const extraFields = [
        { key: 'product', type:'custom',
            component: (
                <SingleDropdown
                    label="Product"
                    value={product}
                    onChange={setProduct}
                    apiPath="product"
                />
            )
        },
        { key: 'warehouse', type:'custom',
            component: (
                <SingleDropdown
                    label="Warehouse"
                    value={warehouse}
                    onChange={setWarehouse}
                    apiPath="warehouse"
                />
            )
        },
        {key : 'quantity', type: 'number', label: 'Quantity'}
    ];

    return (
        <Add
            title='Stock'
            apiPath='stock'
            extraFields={extraFields}
            extraData={{ product, warehouse}}
        />
    );
}

export default StockAdd;