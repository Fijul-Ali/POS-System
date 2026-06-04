"use client";
import React from "react";
import List from "../components/list";

function WarehouseList() {
    const columns = [
        { key: 'identifier', label: 'Identifier' },
        { key: 'address', label: 'Address' },
        { key: 'contactNumber', label: 'Contact Number' },
        { key: 'description', label: 'Description' }
    ]
    return (
        <List
            title='Warehouse'
            apiPath='warehouse'
            columns={columns}
            addPath='/warehouse/add'
            editPath='/warehouse/edit'
        />
    );
}

export default WarehouseList;