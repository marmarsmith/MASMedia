const baseUrl = `${process.env.REACT_APP_API_URL}/api/review`;
const profUrl = `${process.env.REACT_APP_API_URL}/api/profile`;
const searchUrl = `${process.env.REACT_APP_API_URL}/api/media/review`;

// export async function findAll() {

//     const init = { method: "GET", credentials: "include" };

//     const response = await fetch(baseUrl, init);
//     if (response.status === 200) {
//         return response.json();
//     } else if (response.status === 403) {
//         return Promise.reject(403);
//     }
//     return Promise.reject("Could not fetch reviews.");
// }

export async function findbyUsername(username) {

    const init = { method: "GET", credentials: "include" };

    const response = await fetch(`${profUrl}/${username}/`, init);
    if (response.status === 200) {
        return response.json();
    } else if (response.status === 403) {
        return Promise.reject(403);
    }
    return Promise.reject("Could not fetch reviews.");
}

export async function findByMediaId(mediaId) {

    const init = { method: "GET", credentials: "include" };

    const response = await fetch(`${searchUrl}/${mediaId}/`, init);
    if (response.status === 200) {
        return response.json();
    } else if (response.status === 403) {
        return Promise.reject(403);
    }
    return Promise.reject("Could not fetch reviews.");
}

export async function findById(username, reviewId) {

    const init = { method: "GET", credentials: "include" };

    const response = await fetch(`${profUrl}/${username}/${reviewId}`, init);
    if (response.status === 200) {
        return response.json();
    } else if (response.status === 403) {
        return Promise.reject(403);
    }
    return Promise.reject("Could not fetch review.");
}

async function add(review) {
    const init = {
        method: "POST",
        credentials: "include",
        headers: {
            "Content-Type": "application/json",
            "Accept": "application/json"
        },
        body: JSON.stringify(review)
    };
    const response = await fetch(`${baseUrl}/add`, init);
    if (response.status === 201) {
        return response.json();
    } else if (response.status === 403) {
        return Promise.reject(403);
    }
    return Promise.reject("Could not add review.");
}

async function update(review) {
    const init = {
        method: "PUT",
        credentials: "include",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(review)
    };
    const response = await fetch(`${profUrl}/${review.user.username}/${review.reviewId}`, init);
    if (response.status === 204) {
        return Promise.resolve();
    } else if (response.status === 403) {
        return Promise.reject(403);
    }
    return Promise.reject("Could not update review.");
}

export async function save(review) {
    return review.reviewId > 0 ? update(review) : add(review);
}

export async function deleteById(username, reviewId) {
    const init = { method: "DELETE", credentials: "include" };
    const response = await fetch(`${profUrl}/${username}/${reviewId}/delete`, init);
    if (response.status === 204) {
        return Promise.resolve();
    } else if (response.status === 403) {
        return Promise.reject(403);
    }
    return Promise.reject("Could not delete review.");
}