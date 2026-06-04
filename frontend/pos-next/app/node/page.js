"use client";
import React from "react";
import List from "../components/list";

function Node() {
    const columns = [
        { key: 'identifier', label: 'Identifier' },
        { key: 'roles', label:'Roles'},
        { key: 'description', label: 'Description' }
    ];

    return (
        <List
            title='Node'
            apiPath='node'
            columns={columns}
            addPath='/node/add'
            editPath='/node/edit'
        />
    );
}

export default Node;