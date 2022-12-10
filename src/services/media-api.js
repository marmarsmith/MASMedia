const baseUrl = `${process.env.REACT_APP_API_URL}/api/media`;

export async function findAll() {

    const init = { method: "GET", credentials: "include" };

    const response = await fetch(baseUrl, init);
    if (response.status === 200) {
        return response.json();
    } else if (response.status === 403) {
        return Promise.reject(403);
    }
    return Promise.reject("Could not fetch medias.");
}

export async function findById(mediaId) {

    const init = { method: "GET", credentials: "include" };

    const response = await fetch(`${baseUrl}/${mediaId}`, init);
    if (response.status === 200) {
        return response.json();
    } else if (response.status === 403) {
        return Promise.reject(403);
    }
    return Promise.reject("Could not fetch media.");
}

async function add(media) {
    const init = {
        method: "POST",
        credentials: "include",
        headers: {
            "Content-Type": "application/json",
            "Accept": "application/json"
        },
        body: JSON.stringify(media)
    };
    const response = await fetch(baseUrl, init);
    if (response.status === 201) {
        return response.json();
    } else if (response.status === 403) {
        return Promise.reject(403);
    }
    return Promise.reject("Could not add media.");
}

async function update(media) {
    const init = {
        method: "PUT",
        credentials: "include",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(media)
    };
    const response = await fetch(`${baseUrl}/${media.mediaId}`, init);
    if (response.status === 204) {
        return Promise.resolve();
    } else if (response.status === 403) {
        return Promise.reject(403);
    }
    return Promise.reject("Could not update media.");
}

export async function save(media) {
    return media.mediaId > 0 ? update(media) : add(media);
}

export async function deleteById(mediaId) {
    const init = { method: "DELETE", credentials: "include" };
    const response = await fetch(`${baseUrl}/${mediaId}`, init);
    if (response.status === 204) {
        return Promise.resolve();
    } else if (response.status === 403) {
        return Promise.reject(403);
    }
    return Promise.reject("Could not delete media.");
}