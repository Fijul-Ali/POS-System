"use client";
import Edit from "@/app/components/edit";

function WarehouseEdit() {

    const handleLoad = (data) => {

    }
    const extraFields = [
        { key: 'address', label: 'Address', type: 'address' },
        { key: 'contactNumber', label: 'Contact Number', type: 'phone' }
    ];

    return (
        <Edit
            title='Warehouse'
            apiPath='warehouse'
            extraFields={extraFields}
        />
    );
}

export default WarehouseEdit;