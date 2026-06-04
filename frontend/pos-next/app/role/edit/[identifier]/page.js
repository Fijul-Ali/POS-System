import Edit from "@/app/components/edit";

function RoleEdit() {
    return (
        <Edit
            title='Role'
            apiPath='role'
            extraFields={[]}
        />
    );
}

export default RoleEdit;