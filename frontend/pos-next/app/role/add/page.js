"use client";
import Add from "@/app/components/add";

function RoleAdd() {
    return (
        <Add
            title='Role'
            apiPath='role'
            extraFields={[]}
        />
    );
}

export default RoleAdd;