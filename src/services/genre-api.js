const baseUrl = `${process.env.REACT_APP_API_URL}/api/genre`;

export async function findAll() {

    const init = { method: "GET", credentials: "include" };

    const response = await fetch(baseUrl, init);
    if (response.status === 200) {
        return response.json();
    } else if (response.status === 403) {
        return Promise.reject(403);
    }
    return Promise.reject("Could not fetch genres.");
}

export async function findById(genreId) {

    const init = { method: "GET", credentials: "include" };

    const response = await fetch(`${baseUrl}/${genreId}`, init);
    if (response.status === 200) {
        return response.json();
    } else if (response.status === 403) {
        return Promise.reject(403);
    }
    return Promise.reject("Could not fetch genre.");
}

async function add(genre) {
    const init = {
        method: "POST",
        credentials: "include",
        headers: {
            "Content-Type": "application/json",
            "Accept": "application/json"
        },
        body: JSON.stringify(genre)
    };
    const response = await fetch(baseUrl, init);
    if (response.status === 201) {
        return response.json();
    } else if (response.status === 403) {
        return Promise.reject(403);
    }
    return Promise.reject("Could not add genre.");
}

async function update(genre) {
    const init = {
        method: "PUT",
        credentials: "include",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(genre)
    };
    const response = await fetch(`${baseUrl}/${genre.genreId}`, init);
    if (response.status === 204) {
        return Promise.resolve();
    } else if (response.status === 403) {
        return Promise.reject(403);
    }
    return Promise.reject("Could not update genre.");
}

export async function save(genre) {
    return genre.genreId > 0 ? update(genre) : add(genre);
}

export async function deleteById(genreId) {
    const init = { method: "DELETE", credentials: "include" };
    const response = await fetch(`${baseUrl}/${genreId}`, init);
    if (response.status === 204) {
        return Promise.resolve();
    } else if (response.status === 403) {
        return Promise.reject(403);
    }
    return Promise.reject("Could not delete genre.");
}