import axios from 'axios';

export const api = axios.create({
    baseURL: 'http://localhost:9192',
});

export async function addRoom(photo, roomType, roomPrice) {
    const formData = new FormData();
    formData.append('photo', photo);
    formData.append('roomType', roomType);
    formData.append('roomPrice', roomPrice);

    try {
        const response = await api.post('/rooms/add/new-room', formData);
        return response.status === 201;
    } catch (error) {
        if (error.response && error.response.status === 413) {
            throw new Error('File too large! Maximum upload size is 20MB.');
        } else {
            throw new Error('Error adding room');
        }
    }
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
export async function updateRoom(roomId, roomData) {
    try {
        const formData = new FormData();
        formData.append("roomType", roomData.roomType);
        formData.append("roomPrice", roomData.roomPrice);
        if (roomData.photo) {
            formData.append("photo", roomData.photo);
        }
        const response = await api.put(`/rooms/update/${roomId}`, formData, {
            headers: {
                'Content-Type': 'multipart/form-data'
            }
        });
        return response;
    } catch (error) {
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
export async function bookRoom(roomId, booking){
    try{
        const response = await api.post(`/bookings/room/${roomId}/booking`, booking);
        return response.data;
    }catch(error){
        if(error.response && error.response.status === 409){
            throw new Error(error.response.data);
        }
        else{
            throw new Error(`error booking room: ${error.message}`); 
        }
    }
}
export async function getAllBookings(){
    try{
        const response = await api.get('/bookings/all-bookings');
        return response.data;
    }catch(error){
        throw new Error(`error fetching bookings: ${error.message}`);
    }
}
export async function getBookingByConfirmationCode(confirmationCode){
    try{
        const response = await api.get(`/bookings/confirmation/${confirmationCode}`);
        return response.data;
    }catch(error){
        if(error.response && error.response.status === 404){
            throw new Error(`Booking with confirmation code ${confirmationCode} not found`);
        }
        else{
            throw new Error(`error fetching booking: ${error.message}`);
        }
    }
}
export async function cancelBooking(bookingId){
    try{
        const response = await api.delete(`/bookings/booking/${bookingId}/delete`);
        return response.data;
    }catch(error){
        if(error.response && error.response.status === 404){
            throw new Error(`Booking with ID ${bookingId} not found`);
        }
        else{
            throw new Error(`error cancelling booking: ${error.message}`);
        }}
}