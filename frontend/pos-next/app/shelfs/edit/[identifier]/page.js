"use client";
import Edit from "@/app/components/edit";   

function ShelfsEdit() {
    return (
        <Edit
            title='Shelfs'
            apiPath='shelfs'
            extraFields={[]}
        />
    );
}

export default ShelfsEdit;