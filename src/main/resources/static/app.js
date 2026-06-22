let usersCache = [];

document.addEventListener('DOMContentLoaded', async () => {
    await loadUsersAndRoles();
});

async function loadUsersAndRoles() {
    try {
        const [users, roles] = await Promise.all([
            getAllUsers(),
            getAllRoles()
        ]);

        usersCache = users;
        renderUsersTable(users);

        renderRoleOptions(roles, document.getElementById('roles'));
        renderRoleOptions(roles, document.getElementById('edit-roles'));
        renderRoleOptions(roles, document.getElementById('delete-roles'));
    } catch (error) {
        console.error('Ошибка загрузки данных:', error);
        alert('Не удалось загрузить данные. Попробуйте обновить страницу.');
    }
}

function findUserById(id) {
    return usersCache.find(user => user.id === Number(id));
}

// Открытие модалки редактирования — подставляем данные нужного юзера
document.getElementById('editModal').addEventListener('show.bs.modal', (event) => {
    const button = event.relatedTarget;
    const userId = button.dataset.userId;
    const user = findUserById(userId);
    if (user) {
        fillEditModal(user);
    }
});

// Открытие модалки удаления — то же самое
document.getElementById('deleteModal').addEventListener('show.bs.modal', (event) => {
    const button = event.relatedTarget;
    const userId = button.dataset.userId;
    const user = findUserById(userId);
    if (user) {
        fillDeleteModal(user);
    }
});

// Форма добавления пользователя
document.getElementById('add-user-form').addEventListener('submit', async (event) => {
    event.preventDefault();

    const form = event.target;
    const roleIds = Array.from(form.querySelector('#roles').selectedOptions)
        .map(option => ({
            id: Number(option.value),
            name: option.text
        }));

    const userDto = {
        firstName: form.querySelector('#firstName').value,
        lastName: form.querySelector('#lastName').value,
        age: Number(form.querySelector('#age').value),
        email: form.querySelector('#email').value,
        password: form.querySelector('#password').value,
        roles: roleIds
    };

    try {
        await createUser(userDto);
        await loadUsersAndRoles();
        form.reset();
    } catch (error) {
        console.error('Ошибка при создании пользователя:', error);
        alert('Не удалось создать пользователя');
    }
});

// Форма редактирования пользователя
document.getElementById('edit-user-form').addEventListener('submit', async (event) => {
    event.preventDefault();

    const form = event.target;
    const id = form.querySelector('#edit-id-hidden').value;

    const roleIds = Array.from(form.querySelector('#edit-roles').selectedOptions)
        .map(option => ({
            id: Number(option.value),
            name: option.text
        }));

    const userDto = {
        firstName: form.querySelector('#edit-firstName').value,
        lastName: form.querySelector('#edit-lastName').value,
        age: Number(form.querySelector('#edit-age').value),
        email: form.querySelector('#edit-email').value,
        password: form.querySelector('#edit-password').value,
        roles: roleIds
    };

    try {
        await updateUser(id, userDto);
        await loadUsersAndRoles();
        bootstrap.Modal.getInstance(document.getElementById('editModal')).hide();
    } catch (error) {
        console.error('Ошибка при обновлении пользователя:', error);
        alert('Не удалось обновить пользователя');
    }
});

// Форма удаления пользователя
document.getElementById('delete-user-form').addEventListener('submit', async (event) => {
    event.preventDefault();

    const form = event.target;
    const id = form.querySelector('#delete-id-hidden').value;

    try {
        await deleteUser(id);
        await loadUsersAndRoles();
        bootstrap.Modal.getInstance(document.getElementById('deleteModal')).hide();
    } catch (error) {
        console.error('Ошибка при удалении пользователя:', error);
        alert('Не удалось удалить пользователя');
    }
});