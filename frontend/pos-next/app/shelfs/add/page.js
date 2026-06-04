"use client";
import Add from "@/app/components/add";

function ShelfsAdd() {
    return (
        <Add
            title='Shelfs'
            apiPath='shelfs'
            extraFields={[]}
        />
    );
}

export default ShelfsAdd;