"use client";
import Edit from "@/app/components/edit";

function UnitEdit() {
    return (
        <Edit
            title='Unit'
            apiPath='unit'
            extraFields={[]}
        />
    );
}

export default UnitEdit;