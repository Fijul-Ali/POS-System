"use client";
import React from "react";
import List from "../components/list";

function StockList() {
    const columns = [
        { key: 'identifier', label: 'Identifier' },
        { key: 'quantity', label: 'Quantity' },
        { key: 'product', label: 'Product' },
        { key: 'warehouse', label: 'Warehouse' },
        { key: 'description', label: 'Description' }
    ]
    return (
        <List
            title='Stock'
            apiPath='stock'
            columns={columns}
            addPath='/stock/add'
            editPath='/stock/edit'
        />
    );
}

export default StockList;