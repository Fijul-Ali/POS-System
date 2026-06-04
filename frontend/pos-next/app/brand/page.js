"use client";
import React from "react";
import List from "../components/list";

function BrandList() {
    const columns = [
        { key: 'identifier', label: 'Identifier' },
        { key: 'description', label: 'Description' }
    ]

    return (
        <List
            title='Brand'
            apiPath='brand'
            columns={columns}
            addPath='/brand/add'
            editPath='/brand/edit'
        />
    );
}

export default BrandList;