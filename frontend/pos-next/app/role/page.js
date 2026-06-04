"use client";
import React from "react";
import List from "../components/list";

function RoleList(){
    const columns = [
        {key:'identifier', label:'Identifier'},
        {key:'description', label: 'Description'}
    ]
    return(
        <List
            title='Role'
            apiPath='role'
            columns={columns}
            addPath='/role/add'
            editPath='/role/edit'
        />
    )
}

export default RoleList;