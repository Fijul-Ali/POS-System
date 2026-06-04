"use client";
import Add from "@/app/components/add";
import MultiDropdown from "@/app/components/multidropdown";
import { useState } from "react";

function RacksAdd() {
    const [shelves, setShelves] = useState([]);

    const extraFields = [
        { key: 'shelves', type: 'custom', component: (
            <MultiDropdown
                value={shelves}
                name="shelves"
                label="Shelves"
                apiPath="shelfs"
                required
                onChange={(val) => setShelves(val)}
            />
        ) }
    ];

    return (
        <Add
            title='Rack'
            apiPath='racks'
            extraFields={extraFields}
            extraData={{ shelves }}
        />
    );
}

export default RacksAdd;