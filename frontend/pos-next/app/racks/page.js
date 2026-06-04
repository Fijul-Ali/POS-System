"use client";
import React from "react";
import List from "../components/list";

function RacksList(){
    const columns = [
        { key:'identifier', label:'Identifier'},
        { key: 'shelves', label: 'Shelves'},
        { key: 'description', label: 'Description'}
    ]
    return (
        <List
            title='Racks'
            apiPath='racks'
            columns={columns}
            addPath='/racks/add'
            editPath='/racks/edit'
        />
    );
}

export default RacksList;