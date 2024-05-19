import axios from 'axios';

export const api = axios.create({
    baseURL: 'http://localhost:9192',
});

export async function addRoom(photo, roomType, roomPrice) {
    const formData = new FormData();
    formData.append('photo', photo);
    formData.append('roomType', roomType);
    formData.append('roomPrice', roomPrice);

    const response = await api.post('/rooms/add/new-room', formData);
    return response.status === 201;
}

export async function getRoomTypes() {
    try {
        const response = await api.get('/rooms/room-types');
        return response.data;
    } catch (error) {
        throw new Error('Error fetching room types');
    }
}

export async function getAllRooms() {
    try {
        const response = await api.get('/rooms/all-rooms');
        return response.data;
    } catch (error) {
        throw new Error('Error fetching rooms');
    }
}

export async function deleteRoom(roomId) {
    try {
        const response = await api.delete(`/rooms/delete/room/${roomId}`);
        return response.data;
    } catch (error) {
        throw new Error('Error deleting room');
    }
}
export async function updateRoom(roomId,roomData){
    try{
        const formData = new FormData();
        formData.append("roomType", roomData.roomType);
        formData.append("roomPrice", roomData.roomPrice);
        formData.append("photo", roomData.photo);
        const response = await api.put(`/rooms/update/${roomId}`);
        return response;
    }catch(error){
        throw new Error('Error updating room');
    }
}
export async function getRoomById(roomId){
    try{
        const result = await api.get(`/rooms/room/${roomId}`);
        return result.data;
    }catch(error){
        throw new Error('Error fetching room');
    }
}