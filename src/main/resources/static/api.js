const BASE_URL = '/api';

async function getAllUsers() {
    const response = await fetch(`${BASE_URL}/users`, {
        credentials: 'same-origin'
    });
    if (!response.ok) {
        throw new Error('Не удалось загрузить пользователей');
    }
    return response.json();
}

async function getAllRoles() {
    const response = await fetch(`${BASE_URL}/roles`, {
        credentials: 'same-origin'
    });
    if (!response.ok) {
        throw new Error('Не удалось загрузить роли');
    }
    return response.json();
}

async function createUser(userDto) {
    const response = await fetch(`${BASE_URL}/users`, {
        method: 'POST',
        credentials: 'same-origin',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(userDto)
    });
    if (!response.ok) {
        throw new Error('Не удалось создать пользователя');
    }
    return response.json();
}

async function updateUser(id, userDto) {
    const response = await fetch(`${BASE_URL}/users/${id}`, {
        method: 'PUT',
        credentials: 'same-origin',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(userDto)
    });
    if (!response.ok) {
        throw new Error('Не удалось обновить пользователя');
    }
    return response.json();
}

async function deleteUser(id) {
    const response = await fetch(`${BASE_URL}/users/${id}`, {
        method: 'DELETE',
        credentials: 'same-origin'
    });
    if (!response.ok) {
        throw new Error('Не удалось удалить пользователя');
    }
}