const baseUrl = process.env.REACT_APP_API_URL;

function hasAuthority(...authorities) {
    for (const authority of authorities) {
        if (this.authorities.includes(authority)) {
            return true;
        }
    }
    return false;
}

export async function login(credentials) {
    const init = {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            "Accept": "application/json"
        },
        credentials: "include",
        body: JSON.stringify(credentials)
    };

    const response = await fetch(`${baseUrl}/authenticate`, init);

    if (response.status === 200) {
        const body = await response.json();
        body.hasAuthority = hasAuthority;
        return Promise.resolve(body);
    }

    return Promise.reject("bad credentials");
}

export async function logout() {
    const init = { method: "POST", credentials: "include" };
    await fetch(`${baseUrl}/expire_token`, init);
}

export async function refresh() {
    const init = {
        method: "POST",
        headers: {
            "Accept": "application/json"
        },
        credentials: "include"
    };

    const response = await fetch(`${baseUrl}/refresh_token`, init);

    if (response.status === 200) {
        const body = await response.json();
        body.hasAuthority = hasAuthority;
        return Promise.resolve(body);
    }

    return Promise.reject("Information is incorrect");
}