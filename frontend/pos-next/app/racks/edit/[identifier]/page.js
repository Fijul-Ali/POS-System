"use client";
import Edit from "@/app/components/edit";
import React, { useState } from "react";
import MultiDropdown from "@/app/components/multidropdown";

function RacksEdit() {
    const [shelves, setShelves] = useState([]);

    const handleLoad = (data) => {
        setShelves(data.shelves || []);
    };

    const extraFields = [
        { key: 'shelves', type: 'custom',
            component: (
                <MultiDropdown
                    value={shelves}
                    onChange={(val) => setShelves(val)}
                    apiPath="shelfs"
                    label="Shelves"
                />
            )
        }
    ];

    return (
        <Edit
            title='Rack'
            apiPath='racks'
            extraFields={extraFields}
            extraData={{ shelves }}
            onLoad={handleLoad}
        />
    );
}

export default RacksEdit;