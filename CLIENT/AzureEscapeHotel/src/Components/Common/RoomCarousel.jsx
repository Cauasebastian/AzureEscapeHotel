import React, { useEffect, useState } from 'react';
import { getAllRooms } from '../Utils/ApiFunctions';
import { Carousel } from 'react-bootstrap';
import { Card, Col, Container, Row } from 'react-bootstrap';
import { Link } from 'react-router-dom';

const RoomCarousel = () => {
  const [rooms, setRooms] = useState([{ id: "", roomType: "", roomPrice: "", photo: "" }]);
  const [errorMessage, setErrorMessage] = useState("");
  const [isLoading, setIsLoading] = useState(false);

  useEffect(() => {
    setIsLoading(true);
    getAllRooms()
      .then((data) => {
        setRooms(data);
        setIsLoading(false);
      })
      .catch((error) => {
        setErrorMessage(error.message);
        setIsLoading(false);
      });
  }, []);

  if (isLoading) {
    return <div className='mt-5 mb-5'>Loading...</div>;
  }

  if (errorMessage) {
    return <div className='mt-5 text-danger mb-5'> Error: {errorMessage}</div>;
  }

  return (
    <div>
      <section className='bg-light mb-5 mt-5 shadow'>
        <Link to='/browse-all-rooms' className='hotel-color text-center'>
          Browse All Rooms
        </Link>
        <Container>
          <Carousel indicators={false}>
            {[...Array(Math.ceil(rooms.length / 4))].map((e, i) => (
              <Carousel.Item key={i}>
                <Row>
                  {rooms.slice(i * 4, i * 4 + 4).map((room) => (
                    <Col key={room.id} className='mb-4' xs={12} md={6} lg={3}>
                      <Card>
                        <Link to={`/book-room/${room.id}`}>
                          <Card.Img
                            variant='top'
                            src={`data:image/png;base64, ${room.photo}`}
                            alt='Room Photo'
                            className='w-100'
                            style={{ height: '200px' }}
                          />
                        </Link>
                        <Card.Body>
                          <Card.Title className='hotel-color'>{room.roomType}</Card.Title>
                          <Card.Title className='room-price'>{room.roomPrice}</Card.Title>
                          <div className='flex-shrink-0'>
                            <Link className='btn btn-sm btn-hotel' to={`/book-room/${room.id}`}>
                              View/Book Now
                            </Link>
                          </div>
                        </Card.Body>
                      </Card>
                    </Col>
                  ))}
                </Row>
              </Carousel.Item>
            ))}
          </Carousel>
        </Container>
      </section>
    </div>
  );
};

export default RoomCarousel;
