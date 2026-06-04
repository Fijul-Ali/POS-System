"use client";
import Add from "@/app/components/add";

function WarehouseAdd() {
    const extraFields = [
        {key: "address", type: "address", label: "Address"},
        {key: "contactNumber", type: "phone", label: "Contact Number"},
    ];

    return (
        <Add
            title='Warehouse'
            apiPath='warehouse'
            extraFields={extraFields}
        />
    );
}

export default WarehouseAdd;