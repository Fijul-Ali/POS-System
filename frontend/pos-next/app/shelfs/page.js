"use client";
import React from "react";
import List from "../components/list";

function ShelfsList(){
    const columns = [
        { key:'identifier', label:'Identifier'},
        { key: 'description', label: 'Description'}
    ]
    return (
        <List
            title='Shelfs'
            apiPath='shelfs'
            columns={columns}
            addPath='/shelfs/add'
            editPath='/shelfs/edit'
        />
    );
}

export default ShelfsList;