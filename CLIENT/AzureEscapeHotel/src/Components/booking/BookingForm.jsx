import React, { useEffect, useState } from 'react';
import { Form, Button } from 'react-bootstrap';
import { useParams, useNavigate } from 'react-router-dom';
import moment from 'moment';
import BookingSummary from './BookingSummary';
import { getRoomById, bookRoom } from '../Utils/ApiFunctions';

const BookingForm = () => {
    const [isValidated, setIsValidated] = useState(false);
    const [isSubmitted, setIsSubmitted] = useState(false);
    const [errorMessage, setErrorMessage] = useState('');
    const [roomPrice, setRoomPrice] = useState(0);
    const [booking, setBooking] = useState({
        guestName: '',
        guestEmail: '',
        checkInDate: '',
        checkOutDate: '',
        numberOfAdults: '',
        numberOfChildren: '',
    });

    const { roomId } = useParams();
    const navigate = useNavigate();

    // Function to handle input changes
    const handleInputChange = (e) => {
        const { name, value } = e.target;
        setBooking({
            ...booking,
            [name]: value,
        });
        setErrorMessage('');
    };

    // Function to fetch room price by room ID
    const getRoomPriceById = async (roomId) => {
        try {
            const room = await getRoomById(roomId);
            setRoomPrice(room.roomPrice);
        } catch (error) {
            setErrorMessage(error.message);
        }
    };

    useEffect(() => {
        getRoomPriceById(roomId);
    }, [roomId]);

    // Function to calculate payment based on dates and room price
    const calculatePayment = () => {
        const checkInDate = moment(booking.checkInDate);
        const checkOutDate = moment(booking.checkOutDate);
        const diffInDays = checkOutDate.diff(checkInDate, 'days');
        const price = roomPrice ? roomPrice : 0;
        return diffInDays * price;
    };

    // Function to validate guest count
    const isGuestCountValid = () => {
        const adultCount = parseInt(booking.numberOfAdults);
        const childrenCount = parseInt(booking.numberOfChildren);
        const totalCount = adultCount + childrenCount;
        return totalCount >= 1; // At least one adult required
    };

    // Function to validate check-out date is after check-in date
    const isCheckOutDateValid = () => {
        if (!moment(booking.checkOutDate).isSameOrAfter(booking.checkInDate)) {
            setErrorMessage('Check-out date must be on or after check-in date');
            return false;
        } else {
            setErrorMessage('');
            return true;
        }
    };

    // Function to handle form submission
    const handleSubmit = async (e) => {
        e.preventDefault();
        const form = e.currentTarget;
        if (form.checkValidity() === false || !isGuestCountValid() || !isCheckOutDateValid()) {
            e.stopPropagation();
        } else {
            try {
                setIsSubmitted(true);
                setIsValidated(true);
            } catch (error) {
                setErrorMessage(error.message);
            }
        }
    };

    // Function to handle booking
    const handleBooking = async () => {
        try {
            const confirmationCode = await bookRoom(roomId, booking);
            navigate('/', { state: { message: `Booking successful. Confirmation code: ${confirmationCode}` } });
        } catch (error) {
            setErrorMessage(error.message);
        }
    };

    return (
        (<>
            <div className='container'>
                <div className='row'>
                    <div className='col-md-6'>
                        <div className='card card-body mt-5'>
                            <h4 className=' card-title object-center'>Reservation Details</h4>
                            <Form noValidate validated={isValidated} onSubmit={handleSubmit}>
                                <Form.Group>
                                    <Form.Label htmlFor='guestName'>Full Name</Form.Label>
                                <Form.Control required type='text' name='guestName' id='guestName' value={booking.guestName}
                                 onChange={handleInputChange}
                                 placeholder='Enter your full name'/>
                                <Form.Control.Feedback type='invalid'>Please enter your full name</Form.Control.Feedback>
                                </Form.Group>
    
                                <Form.Group>
                                    <Form.Label htmlFor='guestEmail'>email</Form.Label>
                                <Form.Control required type='text' name='guestEmail' id='guestEmail' value={booking.guestEmail}
                                 onChange={handleInputChange}
                                 placeholder='Enter your Email'/>
                                <Form.Control.Feedback type='invalid'>Please enter your Email</Form.Control.Feedback>
                                </Form.Group>
    
                                <fieldset style={{border: '2px'}}>
                                <h5> Lodging period</h5>
                                    <div className='row'>
                                        <div className='col-6'>
                                                <Form.Label htmlFor='checkInDate'>Check-in Date</Form.Label>
                                                <Form.Control required type='date' name='checkInDate' id='checkInDate' value={booking.checkInDate}
                                                onChange={handleInputChange}/>
                                                <Form.Control.Feedback type='invalid'>Please enter your check-in date</Form.Control.Feedback>               
                                        </div>
                                        <div className='col-6'>
                                                <Form.Label htmlFor='checkOutDate'>Check-out Date</Form.Label>
                                                <Form.Control required type='date' name='checkOutDate' id='checkOutDate' value={booking.checkOutDate}
                                                onChange={handleInputChange}/>
                                                <Form.Control.Feedback type='invalid'>Please enter your check-out date</Form.Control.Feedback>
                                        </div>
                                        {errorMessage && <p className='text-danger'>{errorMessage}</p>
                                        }    
                                    </div>
                                </fieldset>
                                <fieldset style={{border: '2px'}}>
                                <h5> Number of guests</h5>
                                        <div className='row'>
                                            <div className='col-6'>
                                                <Form.Label htmlFor='numberOfAdults'>Adults</Form.Label>
                                                <Form.Control required type='number' name='numberOfAdults' id='numberOfAdults' value={booking.numberOfAdults}
                                                onChange={handleInputChange} min='1'placeholder='0'/>
                                                <Form.Control.Feedback type='invalid'>Please select at least 1 adult.</Form.Control.Feedback>
                                            </div>
                                            <div className='col-6'>
                                                <Form.Label htmlFor='numberOfChildren'>Children</Form.Label>
                                                <Form.Control required type='number' name='numberOfChildren' id='numberOfChildren' value={booking.numberOfChildren}
                                                onChange={handleInputChange} min='0' placeholder='0'/>
                                                <Form.Control.Feedback type='invalid'>Please enter the number of children</Form.Control.Feedback>
                                            </div>
                                        </div>
                                </fieldset>
                                <div className='form-group mt-2 bm-2'>
                                    <button type='submit' className='btn btn-hotel'>Reserve Room</button>
                                </div>
    
                            </Form>
                        </div>
                    </div>
                    <div className='col-md-6'>
                        {isSubmitted && (<BookingSummary booking={booking} payment={cauculatePayment()} onConfirm={handleBooking}
                         isFormValid={isValidated}/>)
    
                        }
    
                    </div>
                </div>
            </div>
        </>)
      )
    }
    
    export default BookingForm