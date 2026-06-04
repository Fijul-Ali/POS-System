"use client";
import React,{useState} from "react";
import Add from "@/app/components/add";
import SingleDropdown from "@/app/components/singledropdown";
import MultiDropdown from "@/app/components/multidropdown";

function ProductAdd() {

    const [unit, setUnit] = useState('');
    const [categories, setCategories] = useState([]);
    const [brand, setBrand] = useState('');
    const [model, setModel] = useState('');

    const extraFields = [
        { key: 'unit',
          type:'custom',
          component: (
            <SingleDropdown
            value={unit}
              name="unit"
              label="Unit"
              apiPath="unit"
              required
              onChange={(val) => setUnit(val)}
            />
          )
        },
        {key: 'categories', type:'custom',
          component: (
            <MultiDropdown
              value={categories}
              name="categories"
              label="Categories"
              apiPath="category"
              required
              onChange={(val) => setCategories(val)}
            />
          )
        },
        {key: 'brand', type:'custom',
          component: (
            <SingleDropdown
              value={brand}
              name="brand"
              label="Brand"
              apiPath="brand"
              required
              onChange={(val) => setBrand(val)}
            />
          )
        },
        {key: 'model', type:'custom',
          component: (
            <SingleDropdown
              value={model}
              name="model"
              label="Model"
              apiPath="models"
              required
              onChange={(val) => setModel(val)}
            />
          )
        }
    ];

    return (
        <Add
            title='Product'
            apiPath='product'
            extraFields={extraFields}
            extraData={{ unit, categories, brand, model }}
        />
    );
}

export default ProductAdd;