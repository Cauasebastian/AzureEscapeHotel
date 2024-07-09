import React from 'react'
import { Container, Row,Col } from 'react-bootstrap'
import Header from './Header'
import { FaClock, FaCocktail, FaParking, FaSnowflake, FaUtensils, FaWifi } from 'react-icons/fa'
import Card from 'react-bootstrap/Card'

const HotelService = () => {
  return (
    <>
    <Container className='mb-2'>
        <Header title={'Our Services'} />
        <Row>
            <h4 className='text-center'>
                Services at <span className='hotel-color'>Azure Escape Hotel</span>
                <span className='gap-2'>
                    <FaClock /> - 24/7 Room Service
                </span>
            </h4>
        </Row>
        <hr />
        <Row xs={1} md={2} lg={3} className='g-4 mt-2'>
            <Col>
                <Card>
                    <Card.Body>
                        <Card.Title className='hotel-color'>
                            <FaWifi /> - Wi-Fi
                        </Card.Title>
                        <Card.Text>
                            Stay connected with our high-speed Wi-Fi
                        </Card.Text>
                    </Card.Body>
                </Card>
            </Col>
            <Col>
                <Card>
                    <Card.Body>
                        <Card.Title className='hotel-color'>
                            <FaUtensils /> BreakFast
                        </Card.Title>
                        <Card.Text>
                            Enjoy our complimentary breakfast
                        </Card.Text>
                    </Card.Body>
                </Card>
            </Col>
            <Col>
                <Card>
                    <Card.Body>
                        <Card.Title className='hotel-color'>
                            <FaCocktail /> - Bar
                        </Card.Title>
                        <Card.Text>
                            Relax at our bar with a wide range of drinks
                        </Card.Text>
                    </Card.Body>
                </Card>
            </Col>
            <Col>
                <Card>
                    <Card.Body>
                        <Card.Title className='hotel-color'>
                            <FaParking /> - Parking
                        </Card.Title>
                        <Card.Text>
                            Park your vehicle safely with our parking facility
                        </Card.Text>
                    </Card.Body>
                </Card>
            </Col>
            <Col>
                <Card>
                    <Card.Body>
                        <Card.Title className='hotel-color'>
                            <FaSnowflake /> - Air Conditioning
                        </Card.Title>
                        <Card.Text>
                            Stay cool with our air conditioning facility
                        </Card.Text>
                    </Card.Body>
                </Card>
            </Col>
        </Row>
    </Container>

    </>
  )
}

export default HotelService