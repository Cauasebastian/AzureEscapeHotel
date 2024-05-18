import React, { useEffect, useState } from 'react';
import { getAllRooms, deleteRoom } from '../Utils/ApiFunctions'; // Certifique-se de que esses caminhos estão corretos
import RoomFilter from '../Common/RoomFilter'; // Certifique-se de que esse caminho está correto
import RoomPaginator from '../Common/RoomPaginator'; // Certifique-se de que esse caminho está correto
import { Col } from 'react-bootstrap'; // Certifique-se de que react-bootstrap está instalado
import { FaEdit, FaEye, FaTrashAlt } from 'react-icons/fa'; // Certifique-se de que react-icons/fa está instalado
import { Link } from 'react-router-dom'; // Certifique-se de que react-router-dom está instalado

const ExistingRooms = () => {
    const [rooms, setRooms] = useState([]);
    const [currentPage, setCurrentPage] = useState(1);
    const [roomsPerPage] = useState(8);
    const [isLoading, setIsLoading] = useState(false);
    const [filteredRooms, setFilteredRooms] = useState([]);
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
            setRooms(response);
            setIsLoading(false);
        } catch (error) {
            setErrorMessage(error.message);
            setIsLoading(false);
        }
    };

    useEffect(() => {
        if (selectedRoomType === "") {
            setFilteredRooms(rooms);
        } else {
            const filtered = rooms.filter(room => room.roomType === selectedRoomType);
            setFilteredRooms(filtered);
        }
        setCurrentPage(1);
    }, [selectedRoomType, rooms]);

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
        setTimeout(() => {
            setSuccessMessage("");
            setErrorMessage("");
        }, 5000);
    };

    const calculateTotalPages = (filteredRooms, roomsPerPage, rooms) => {
        const totalRooms = filteredRooms.length > 0 ? filteredRooms.length : rooms.length;
        return Math.ceil(totalRooms / roomsPerPage);
    };

    const indexOfLastRoom = currentPage * roomsPerPage;
    const indexOfFirstRoom = indexOfLastRoom - roomsPerPage;
    const currentRooms = filteredRooms.slice(indexOfFirstRoom, indexOfLastRoom);

    return (
        <>
            {isLoading ? (
                <p>Loading existing Rooms...</p>
            ) : (
                <section className="mt-5 mb-5 container">
                    <div className="d-flex justify-content-center mb-3 mt-5">
                        <h2>Existing Rooms</h2>
                    </div>
                    <Col mb={6} className="mb-3 mb-md-0">
                        <RoomFilter data={rooms} setFiltered={setFilteredRooms} /> {/* Passando a prop correta */}
                    </Col>
                    <table className="table table-bordered table-hover">
                        <thead>
                            <tr className="text-center">
                                <th>ID</th>
                                <th>Room Type</th>
                                <th>Room Price</th>
                                <th>Actions</th>
                            </tr>
                        </thead>
                        <tbody>
                            {currentRooms.map((room) => (
                                <tr key={room.id} className="text-center">
                                    <td>{room.id}</td>
                                    <td>{room.roomType}</td>
                                    <td>{room.roomPrice}</td>
                                    <td className="gap-2">
                                        <Link to={`/edit-room/${room.id}`}>
                                            <span className="btn btn-info btn-sm">
                                                <FaEye />
                                            </span>
                                            <span className="btn btn-warning btn-sm">
                                                <FaEdit />
                                            </span>
                                        </Link>
                                        <button
                                            className="btn btn-danger btn-sm"
                                            onClick={() => handleDelete(room.id)}
                                        >
                                            <FaTrashAlt /> Delete
                                        </button>
                                    </td>
                                </tr>
                            ))}
                        </tbody>
                    </table>
                    <RoomPaginator
                        currentPage={currentPage}
                        totalPages={calculateTotalPages(filteredRooms, roomsPerPage, rooms)}
                        onPageChange={handlePaginationClick}
                    />
                </section>
            )}
        </>
    );
};

export default ExistingRooms;
