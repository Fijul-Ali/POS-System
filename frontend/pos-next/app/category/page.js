"use client";
import React from "react";
import List from "../components/list";

function CategoryList() {
    const columns = [
        { key: 'identifier', label: 'Identifier' },
        { key: 'superCategory', label: 'Super Category' },
        { key: 'description', label: 'Description' }
    ]

    return (
        <List
            title='Category'
            apiPath='category'
            columns={columns}
            addPath='/category/add'
            editPath='/category/edit'
        />
    );
}

export default CategoryList;