const API_BASE_URL = '/api/geo-objects';

export async function getAllGeoObjects() {
    const response = await fetch(API_BASE_URL)
    if (response.status === 204) return []
    return await response.json()
}

export async function createGeoObject(payload) {
    return await fetch(API_BASE_URL, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(payload)
    })
}

export async function updateGeoObject(id, payload) {
    const response = await fetch(`${API_BASE_URL}/${id}`, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(payload)
    });
    
    if (!response.ok) {
        throw new Error('Ошибка при обновлении объекта');
    }

    return null;
}

export async function deleteGeoObject(id) {
    const response = await fetch(`${API_BASE_URL}/${id}`, {
        method: 'DELETE'
    });
    
    if (!response.ok) {
        throw new Error('Ошибка при удалении объекта');
    }
    
    return null;
} 