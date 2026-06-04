"use client";
import Edit from "@/app/components/edit";
import SingleDropdown from "@/app/components/singledropdown";
import { useState } from "react";
import MultiDropdown from "@/app/components/multidropdown";

function ProductEdit() {

    const [unit, setUnit] = useState('');
    const [categories, setCategories] = useState([]);
    const [brand, setBrand] = useState('');
    const [model, setModel] = useState('');

    const handleLoad = (data) => {
        setUnit(data.unit || '');
        setCategories(data.categories || []);
        setBrand(data.brand || '');
        setModel(data.model || '');
    };

    const extraFields = [
        { key: 'unit',type:'custom',
            component: (
                <SingleDropdown
                    value={unit}
                    onChange={(val) => setUnit(val)}
                    apiPath="unit"
                    label="Unit"
                />
            )
        },
        {key: 'categories', type:'custom',
            component: (
                <MultiDropdown
                    value={categories}
                    onChange={(val) => setCategories(val)}
                    apiPath="category"
                    label="Categories"
                />
            )
        },
        {key: 'brand', type:'custom',
            component: (
                <SingleDropdown
                    value={brand}
                    onChange={(val) => setBrand(val)}
                    apiPath="brand"
                    label="Brand"
                />
            )
        },
        {key: 'model', type:'custom',
            component: (
                <SingleDropdown
                    value={model}
                    onChange={(val) => setModel(val)}
                    apiPath="models"
                    label="Model"
                />
            )
        }
    ];

    return (
        <Edit
            title='Product'
            apiPath='product'
            extraFields={extraFields}
            extraData={{ unit, categories, brand, model }}
            onLoad={handleLoad}
        />
    );
}

export default ProductEdit;