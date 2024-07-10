import React, { useState } from 'react';
import { addRoom } from '../Utils/ApiFunctions';
import RoomTypeSelector from '../Common/RoomTypeSelector';
import { Link } from 'react-router-dom';
import imageCompression from 'browser-image-compression';

const AddRoom = () => {
    const [newRoom, setNewRoom] = useState({
        photo: null,
        roomType: "",
        roomPrice: ""
    });

    const [imagePreview, setImagePreview] = useState("");
    const [successMessage, setSuccessMessage] = useState("");
    const [errorMessage, setErrorMessage] = useState("");

    const handleRoomInputChange = (e) => {
        const name = e.target.name;
        let value = e.target.value;
        if (name === "roomPrice") {
            if (!isNaN(value)) {
                value = parseInt(value);
            } else {
                value = "";
            }
        }
        setNewRoom({ ...newRoom, [name]: value });
    };

    const handleImageChange = async (e) => {
        const selectedImage = e.target.files[0];
        
        // Comprimir a imagem antes de configurar no estado
        try {
            const options = {
                maxSizeMB: 5, // Tamanho máximo desejado em MB
                maxWidthOrHeight: 1920, // Resolução máxima desejada
                useWebWorker: true,
            };
            
            const compressedImage = await imageCompression(selectedImage, options);
            setNewRoom({ ...newRoom, photo: compressedImage });
            setImagePreview(URL.createObjectURL(compressedImage));
        } catch (error) {
            console.error('Erro ao comprimir a imagem:', error);
            setErrorMessage('Erro ao comprimir a imagem');
        }
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            const success = await addRoom(newRoom.photo, newRoom.roomType, newRoom.roomPrice);
            if (success) {
                setSuccessMessage("A new room was added to the database");
                setNewRoom({ photo: null, roomType: "", roomPrice: "" });
                setImagePreview("");
                setErrorMessage("");
            } else {
                setErrorMessage("Error adding room");
            }
        } catch (error) {
            setErrorMessage(error.message);
        }
        setTimeout(() => {
            setSuccessMessage("");
            setErrorMessage("");
        }, 5000);
    };

    return (
        <section className='container mt-5 mb-5'>
            <div className='row justify-content-center'>
                <div className='col-md-8 col-lg-6'>
                    <h2 className='mt-5 mb-2'>Add a New Room</h2>
                    {successMessage && (
                        <div className='alert alert-success fade show'>{successMessage}</div>
                    )}
                    {errorMessage && (
                        <div className='alert alert-danger fade show'>{errorMessage}</div>
                    )}
                    <form onSubmit={handleSubmit}>
                        <div className='mb-3'>
                            <label htmlFor='roomType' className='form-label'>
                                Room Type
                            </label>
                            <div>
                                <RoomTypeSelector handleRoomInputChange={handleRoomInputChange} newRoom={newRoom} />
                            </div>
                        </div>
                        <div className='mb-3'>
                            <label htmlFor='roomPrice' className='form-label'>
                                Room Price
                            </label>
                            <input
                                className="form-control"
                                required
                                id="roomPrice"
                                type='number'
                                name='roomPrice'
                                value={newRoom.roomPrice}
                                onChange={handleRoomInputChange}
                            />
                        </div>
                        <div className='mb-3'>
                            <label htmlFor='photo' className='form-label'>
                                Room Photo
                            </label>
                            <input
                                className='form-control'
                                id='photo'
                                name='photo'
                                type='file'
                                onChange={handleImageChange}
                            />
                            {imagePreview && (
                                <img
                                    src={imagePreview}
                                    alt="Preview Room Photo"
                                    style={{ maxWidth: "400px", maxHeight: "400px" }}
                                    className='mb-3'
                                />
                            )}
                        </div>
                        <div className='d-grid d-md-flex mt-2'>
                            <Link to='/existing-rooms' className='btn btn-outline-info ml-5'>
                                Cancel
                            </Link>
                            <button className='btn btn-outline-primary ml-5' type='submit'>
                                Save Room
                            </button>
                        </div>
                    </form>
                </div>
            </div>
        </section>
    );
};

export default AddRoom;
