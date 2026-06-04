"use client";
import React, { useState } from 'react';
import Edit from '@/app/components/edit';
import MultiDropdown from '@/app/components/multidropdown';

function NodeEdit() {
    const [roles, setRoles] = useState([]);

    const handleLoad = (data) => {
        setRoles(data.roles || []);
    };

    const extraFields = [
        {
            key: 'roles',
            type: 'custom',
            component: (
                <MultiDropdown
                    value={roles}
                    onChange={(val) => setRoles(val)}
                    label='Roles'
                    apiPath='role'
                />
            )
        }
    ];

    return (
        <Edit
            title='Node'
            apiPath='node'
            extraFields={extraFields}
            extraData={{ roles }}
            onLoad={handleLoad}
        />
    );
}

export default NodeEdit;