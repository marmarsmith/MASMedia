const baseUrl = process.env.REACT_APP_API_URL;

export async function create(user) {
    const init = {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            "Accept": "application/json"
        },
        body: JSON.stringify(user)
    }

    const response = await fetch(`${baseUrl}/user/create`, init);
    if (response.status === 201) {
        return Promise.resolve();
    } else if (response.status === 400) {
        const messages = await response.json();
        return Promise.reject({ status: response.status, messages });
    }

    return Promise.reject({ status: response.status });
}

export async function changePassword(password) {
    const init = {
        method: "PUT",
        credentials: "include",
        headers: {
            "Content-Type": "application/json",
            "Accept": "application/json"
        },
        body: JSON.stringify({ password })
    }

    const response = await fetch(`${baseUrl}/user/password`, init);
    if (response.status === 204) {
        return Promise.resolve();
    } else if (response.status === 400) {
        const messages = await response.json();
        return Promise.reject({ status: response.status, messages });
    }

    return Promise.reject({ status: response.status });
}

export async function changePrivacy(user) {
    const init = {
        method: "PUT",
        credentials: "include",
        headers: {
            "Content-Type": "application/json",
            "Accept": "application/json"
        },
        body: JSON.stringify(user)
    }

    const response = await fetch(`${baseUrl}/user/privacy`, init);
    if (response.status === 204) {
        return Promise.resolve();
    } else if (response.status === 400) {
        const messages = await response.json();
        return Promise.reject({ status: response.status, messages });
    }

    return Promise.reject({ status: response.status });
}

export async function findAll() {
    const init = {
        method: "GET",
        credentials: "include",
        headers: {
            "Accept": "application/json"
        }
    }
    const response = await fetch(`${baseUrl}/user`, init);
    if (response.status === 200) {
        return await response.json();
    } else {
        return Promise.reject(response.status);
    }
}

export async function findAppUserById(appUserId) {
    const init = {
        method: "GET",
        credentials: "include",
        headers: {
            "Accept": "application/json"
        }
    }
    const response = await fetch(`${baseUrl}/user/${appUserId}`, init);
    if (response.status === 200) {
        return await response.json();
    } else {
        return Promise.reject(response.status);
    }
}

export async function findByUsername(username) {
    const init = {
        method: "GET",
        credentials: "include",
        headers: {
            "Accept": "application/json"
        }
    }
    const response = await fetch(`${baseUrl}/user/username/${username}`, init);
    if (response.status === 200) {
        return await response.json();
    } else {
        return Promise.reject(response.status);
    }
}

export async function findRoles() {
    const init = {
        method: "GET",
        credentials: "include",
        headers: {
            "Accept": "application/json"
        }
    }
    const response = await fetch(`${baseUrl}/user/role`, init);
    if (response.status === 200) {
        return await response.json();
    } else {
        return Promise.reject(response.status);
    }
}

export async function update(user) {
    delete user.authorities;
    const init = {
        method: "PUT",
        credentials: "include",
        headers: {
            "Content-Type": "application/json",
            "Accept": "application/json"
        },
        body: JSON.stringify(user)
    }

    const response = await fetch(`${baseUrl}/user/update`, init);
    if (response.status === 204) {
        return Promise.resolve();
    } else if (response.status === 400) {
        const messages = await response.json();
        return Promise.reject({ status: response.status, messages });
    }

    return Promise.reject({ status: response.status });
}