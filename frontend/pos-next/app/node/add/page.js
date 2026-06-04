"use client";
import React, { useState } from 'react';
import Add from '@/app/components/add';
import MultiDropdown from '@/app/components/multidropdown';

function NodeAdd() {
    const [roles, setRoles] = useState([]);

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
                    required
                />
            )
        }
    ];

    return (
        <Add
            title='Node'
            apiPath='node'
            extraFields={extraFields}
            extraData={{ roles }}
        />
    );
}

export default NodeAdd;