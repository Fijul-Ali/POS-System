"use client";
import Add from '@/app/components/add';
import MultiDropdown from '@/app/components/multidropdown';
import { useState } from 'react';

function UserAdd() {
    const [roles, setRoles] = useState([]);

    const extraFields = [
        { key: 'roles', type: 'custom',
            component: (
                <MultiDropdown
                    value={roles}
                    apiPath='role'
                    onChange={(val) => setRoles(val)}
                    label='Roles'
                    required
                />               
            )
        },
        { key: 'name', type: 'text', label: 'Name'},
        { key: 'password', type: 'password', label: 'Password'},
        { key: 'phoneNo', type: 'text', label: 'Phone Number', maxLen: '10'},
        { key: 'username', type: 'email', label: 'E-Mail'}
    ];

return (
    <Add
        title='User'
        apiPath='user'
        extraFields={extraFields}
        extraData={{ roles }}
        />
);
}

export default UserAdd;