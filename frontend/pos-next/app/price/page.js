"use client";
import React from "react";
import List from "../components/list";

function PriceList() {
    const columns = [
        { key: 'identifier', label: 'Identifier' },
        { key: 'costPrice', label: 'Cost Price' },
        { key: 'effectiveFrom', label: 'Effective From' },
        { key: 'effectiveTo', label: 'Effective To' },
        { key: 'mrp', label: 'MRP' },
        { key: 'sellingPrice', label: 'Selling Price' },
        { key: 'description', label: 'Description' },
        { key: 'product', label: 'Product'}
    ];

    return (
        <List
            title='Price'
            apiPath='price'
            columns={columns}
            addPath='/price/add'
            editPath='/price/edit'
        />
    );
}

export default PriceList;