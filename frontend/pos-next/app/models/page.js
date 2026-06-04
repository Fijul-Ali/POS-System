"use client";
import React from "react";
import List from "../components/list";

function ModelsList() {
    const columns = [
        { key: 'identifier', label: 'Identifier' },
        { key: 'description', label: 'Description' }
    ]
    return (
        <List
            title='Models'
            apiPath='models'
            columns={columns}
            addPath='/models/add'
            editPath='/models/edit'
        />
    );
}

export default ModelsList;