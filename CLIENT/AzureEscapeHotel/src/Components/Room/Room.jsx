import React, { useState, useEffect } from 'react';
import { Col, Container, Row } from 'react-bootstrap';
import { getAllRooms, deleteRoom } from '../Utils/ApiFunctions';
import RoomCard from './RoomCard';
import RoomPaginator from '../Common/RoomPaginator';
import RoomFilter from '../Common/RoomFilter';
import { FaEdit, FaEye, FaPlus, FaTrashAlt } from 'react-icons/fa';
import { Link } from 'react-router-dom';

const Room = () => {
  const [data, setData] = useState([]);
  const [error, setError] = useState(null);
  const [isLoading, setIsLoading] = useState(false);
  const [currentPage, setCurrentPage] = useState(1);
  const [roomsPerPage] = useState(6);
  const [filteredData, setFilteredData] = useState([]);
  const [selectedRoomType, setSelectedRoomType] = useState("");
  const [successMessage, setSuccessMessage] = useState("");
  const [errorMessage, setErrorMessage] = useState("");

  useEffect(() => {
    fetchRooms();
  }, []);

  const fetchRooms = async () => {
    setIsLoading(true);
    try {
      const response = await getAllRooms();
      setData(response);
      setFilteredData(response);
      setIsLoading(false);
    } catch (error) {
      setErrorMessage(error.message);
      setIsLoading(false);
    }
  };

  useEffect(() => {
    if (selectedRoomType === "") {
      setFilteredData(data);
    } else {
      const filtered = data.filter(room => room.roomType === selectedRoomType);
      setFilteredData(filtered);
    }
    setCurrentPage(1);
  }, [selectedRoomType, data]);

  const handlePaginationClick = (pageNumber) => {
    setCurrentPage(pageNumber);
  };

  const handleDelete = async (roomId) => {
    try {
      const response = await deleteRoom(roomId);
      if (response === "") {
        setSuccessMessage("Room deleted successfully");
        fetchRooms();
      } else {
        setErrorMessage("Error deleting room");
      }
    } catch (error) {
      setErrorMessage(error.message);
    }
  };

  const calculateTotalPages = (filteredData, roomsPerPage) => {
    return Math.ceil(filteredData.length / roomsPerPage);
  };

  const indexOfLastRoom = currentPage * roomsPerPage;
  const indexOfFirstRoom = indexOfLastRoom - roomsPerPage;
  const currentRooms = filteredData.slice(indexOfFirstRoom, indexOfLastRoom);

  if (isLoading) {
    return <div>Loading...</div>;
  }
  if (error) {
    return <div className='text-danger'>An error occurred: {error}</div>;
  }

  return (
    <Container>
      <div className="container col-md-8 col-lg-6 col-xl-4">
        {successMessage && <p className="alert alert-success">{successMessage}</p>}
        {errorMessage && <p className="alert alert-danger">{errorMessage}</p>}
      </div>
      <section className="mt-5 mb-5 container">
        <div className="d-flex justify-content-between mb-3 mt-5">
          <h2>Existing Rooms</h2>
          <Link to="/add-room">
            <FaPlus /> Add Room
          </Link>
        </div>
        <Row>
          <Col md={6} className='mb-3 mb-md-0'>
            <RoomFilter data={data} setFiltered={setFilteredData} />
          </Col>
          <Col md={6} className='d-flex align-items-center justify-content-end'>
            <RoomPaginator totalPages={calculateTotalPages(filteredData, roomsPerPage)} onPageChange={handlePaginationClick} currentPage={currentPage} />
          </Col>
        </Row>
        <Row>
          {currentRooms.map((room) => (
            <RoomCard key={room.id} room={room} />
          ))}
        </Row>
        <Row>
          <Col md={6} className='d-flex align-items-center justify-content-end'>
            <RoomPaginator totalPages={calculateTotalPages(filteredData, roomsPerPage)} onPageChange={handlePaginationClick} currentPage={currentPage} />
          </Col>
        </Row>
      </section>
    </Container>
  );
};

export default Room;
