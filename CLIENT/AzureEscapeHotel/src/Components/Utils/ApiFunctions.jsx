import axios from "axios"

export const api = axios.create({
    baseURL : "http://localhost:9192"
})
//this function add new rooms in your database
export async function AddRoom(photo,roomType,roomPrice){
    const formData = new FormData()
    formData.append("photo",photo)
    formData.append("roomType",roomType)
    formData.append("roomPrice",roomPrice)

    const response = await api.post("/rooms/add/new-room", formData)
    
    if(response.status === 201){
        return true
    }else{
        return false
    }
}
//this func get all room types in your database
export async function getRoomTypes(){
    try{
    const response = await api.get("/rooms/room-types")
    return response.data
    }
    catch(error){
        throw new error("Error fetching room types")
    }
}