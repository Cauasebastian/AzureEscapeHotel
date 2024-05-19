import React, { useEffect, useState } from 'react';
import { updateRoom, getRoomById } from '../Utils/ApiFunctions';
import { useParams, Link } from 'react-router-dom';

const EditRoom = () => {
  const [Room, setRoom] = useState({
    photo: null,
    roomType: "",
    roomPrice: ""
  });

  const [imagePreview, setImagePreview] = useState("");
  const [successMessage, setSuccessMessage] = useState("");
  const [errorMessage, setErrorMessage] = useState("");

  const { roomId } = useParams();

  const handleImageChange = (e) => {
    const selectedImage = e.target.files[0];
    setRoom({ ...Room, photo: selectedImage });
    setImagePreview(URL.createObjectURL(selectedImage));
  };

  const handleInputChange = (event) => {
    const { name, value } = event.target;
    setRoom({ ...Room, [name]: value });
  }

  useEffect(() => {
    const fetchRoom = async () => {
      try {
        const roomData = await getRoomById(roomId);
        setRoom(roomData);
        setImagePreview(roomData.photo);
      } catch (error) {
        console.error(error);
      }
    };
    fetchRoom();
  }, [roomId]);

  const handleSubmit = async (event) => {
    event.preventDefault();
    try {
      const response = await updateRoom(roomId, Room);
      if (response.status === 200) {
        setSuccessMessage("Room updated successfully");
        const updatedRoomData = await getRoomById(roomId);
        setRoom(updatedRoomData);
        setImagePreview(updatedRoomData.photo);
        setErrorMessage("");
      } else {
        setErrorMessage("Error adding room");
      }
    } catch (error) {
      console.log(error);
      setErrorMessage(error.message);
    }
  }

  return (
    <section className='container mt-5 mb-5'>
      <div className='row justify-content-center'>
        <div className='col-md-8 col-lg-6'>
          <h2 className='mt-5 mb-2'>Update a Room</h2>
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
              <input type="text"
                className='form-control'
                id='roomType'
                name='roomType'
                value={Room.roomType}
                onChange={handleInputChange}
              />
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
                value={Room.roomPrice}
                onChange={handleInputChange}
              />
            </div>
            <div className='mb-3'>
              <label htmlFor='photo' className='form-label'>
                Room Photo
              </label>
              <input className='form-control'
                id='photo'
                name='photo'
                type='file'
                onChange={handleImageChange}
              />
              {imagePreview && (
                <img src={imagePreview} alt="Preview Room Photo"
                  style={{ maxWidth: "400px", maxHeight: "400px" }}
                  className='mt-3'
                />
              )}
            </div>
            <div className='d-grid gap-2 d-md-flex mt-2'>
              <Link to='/existing-rooms' className='btn btn-outline-info ml-5'>
                Back to Rooms
              </Link>
              <button type='submit' className='btn btn-outline-warning'>
                Update Room
              </button>
            </div>
          </form>
        </div>
      </div>
    </section>
  );
}

export default EditRoom;
