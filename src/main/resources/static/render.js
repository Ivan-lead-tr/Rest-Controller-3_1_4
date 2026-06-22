function renderUsersTable(users) {
    const tbody = document.getElementById('users-table-body');

    tbody.innerHTML = users.map(user => {
        const roleNames = user.roles.map(role => role.name).join(' ');

        return `
            <tr>
                <td>${user.id}</td>
                <td>${user.firstName}</td>
                <td>${user.lastName}</td>
                <td>${user.email}</td>
                <td>${user.age}</td>
                <td>${roleNames}</td>
                <td>
                    <button type="button"
                            class="btn btn-info btn-sm"
                            data-bs-toggle="modal"
                            data-bs-target="#editModal"
                            data-user-id="${user.id}">
                        Редактировать
                    </button>
                </td>
                <td>
                    <button type="button"
                            class="btn btn-danger btn-sm"
                            data-bs-toggle="modal"
                            data-bs-target="#deleteModal"
                            data-user-id="${user.id}">
                        Удалить
                    </button>
                </td>
            </tr>
        `;
    }).join('');
}

function renderRoleOptions(roles, selectElement) {
    selectElement.innerHTML = roles.map(role =>
        `<option value="${role.id}">${role.name}</option>`
    ).join('');
}

function fillEditModal(user) {
    document.getElementById('edit-id').value = user.id;
    document.getElementById('edit-id-hidden').value = user.id;
    document.getElementById('edit-firstName').value = user.firstName;
    document.getElementById('edit-lastName').value = user.lastName;
    document.getElementById('edit-email').value = user.email;
    document.getElementById('edit-age').value = user.age;
    document.getElementById('edit-password').value = '';

    const roleIds = user.roles.map(role => String(role.id));
    document.querySelectorAll('#edit-roles option').forEach(opt => {
        opt.selected = roleIds.includes(opt.value);
    });
}

function fillDeleteModal(user) {
    document.getElementById('delete-id').value = user.id;
    document.getElementById('delete-id-hidden').value = user.id;
    document.getElementById('delete-firstName').value = user.firstName;
    document.getElementById('delete-lastName').value = user.lastName;
    document.getElementById('delete-email').value = user.email;
    document.getElementById('delete-age').value = user.age;

    const roleIds = user.roles.map(role => String(role.id));
    document.querySelectorAll('#delete-roles option').forEach(opt => {
        opt.selected = roleIds.includes(opt.value);
    });
}