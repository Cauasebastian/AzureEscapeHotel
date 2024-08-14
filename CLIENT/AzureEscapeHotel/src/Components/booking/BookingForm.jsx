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
  
    const currentUser = localStorage.getItem("userId")

	const [booking, setBooking] = useState({
		guestFullName: "",
		guestEmail: currentUser,
		checkInDate: "",
		checkOutDate: "",
		numOfAdults: "",
		numOfChildren: ""
	})

    const { roomId } = useParams();
    const navigate = useNavigate();

    const[roomInfo, setRoomInfo] = useState({
        photo: '',
        roomType: '',
        roomPrice: '',
    })

    // Function to handle input changes
    const handleInputChange = (e) => {
        const { name, value } = e.target;
        setBooking({
            ...booking,
            [name]: value,
        });
        setErrorMessage('');
        console.log(`handleInputChange: ${name} = ${value}`);
    };

    // Function to fetch room price by room ID
    const getRoomPriceById = async (roomId) => {
        try {
            const room = await getRoomById(roomId);
            setRoomPrice(room.roomPrice);
            console.log(`getRoomPriceById: roomPrice = ${room.roomPrice}`);
        } catch (error) {
            setErrorMessage(error.message);
            console.error(`getRoomPriceById error: ${error.message}`);
        }
    };

    useEffect(() => {
        const fetchRoomData = async () => {
            try {
                const room = await getRoomById(roomId);
                setRoomPrice(room.roomPrice);
            } catch (error) {
                setErrorMessage(error.message);
            }
        };
        fetchRoomData();
    }, [roomId]);

    // Function to calculate payment based on dates and room price
    const calculatePayment = () => {
        const checkInDate = moment(booking.checkInDate);
        const checkOutDate = moment(booking.checkOutDate);
        const diffInDays = checkOutDate.diff(checkInDate, 'days');
        const price = roomPrice ? roomPrice : 0;
        const payment = diffInDays * price;
        console.log(`calculatePayment: payment = ${payment}`);
        return payment;
    };

    // Function to validate guest count
    const isGuestCountValid = () => {
        const adultCount = parseInt(booking.numberOfAdults);
        const childrenCount = parseInt(booking.numberOfChildren);
        const totalCount = adultCount + childrenCount;
        const isValid = totalCount >= 1 && adultCount >= 1;
        console.log(`isGuestCountValid: isValid = ${isValid}`);
        return isValid;
    };

    // Function to validate check-out date is after check-in date
    const isCheckOutDateValid = () => {
        const isValid = moment(booking.checkOutDate).isSameOrAfter(booking.checkInDate);
        if (!isValid) {
            setErrorMessage('Check-out date must be on or after check-in date');
        } else {
            setErrorMessage('');
        }
        console.log(`isCheckOutDateValid: isValid = ${isValid}`);
        return isValid;
    };

    // Function to handle form submission
    const handleSubmit = async (e) => {
        e.preventDefault();
        const form = e.currentTarget;
        const isValid = form.checkValidity() === true && isGuestCountValid() && isCheckOutDateValid();
        if (!isValid) {
            e.stopPropagation();
        } else {
            try {
                setIsSubmitted(true);
                console.log('handleSubmit: Form is valid and submitted');
            } catch (error) {
                setErrorMessage(error.message);
                console.error(`handleSubmit error: ${error.message}`);
            }
        }
        setIsValidated(true);
    };

    // Function to handle booking
    const handleBooking = async () => {
        try {
            const bookingData = {
                guestFullName: booking.guestName,
                guestEmail: booking.guestEmail,
                checkInDate: booking.checkInDate,
                checkOutDate: booking.checkOutDate,
                numOfAdults: parseInt(booking.numberOfAdults),
                numOfChildren: parseInt(booking.numberOfChildren),
                totalNumOfGuest: parseInt(booking.numberOfAdults) + parseInt(booking.numberOfChildren),
            };
            const confirmationCode = await bookRoom(roomId, bookingData);
            setIsSubmitted(true);
            navigate("/booking-success", { state: { message: confirmationCode } })
            console.log(`handleBooking: Booking successful. Confirmation code: ${confirmationCode}`);
        } catch (error) {
			const errorMessage = error.message
			console.log(errorMessage)
			navigate("/booking-success", { state: { error: errorMessage } })
        }
    };    

    return (
        <div className='container'>
            <div className='row'>
                <div className='col-md-6'>
                    <div className='card card-body mt-5'>
                        <h4 className='card-title object-center'>Reservation Details</h4>
                        <Form noValidate validated={isValidated} onSubmit={handleSubmit}>
                            <Form.Group>
                                <Form.Label htmlFor='guestName' className="hotel-color">Full Name</Form.Label>
                                <Form.Control required type='text' name='guestName' id='guestName' value={booking.guestName}
                                    onChange={handleInputChange}
                                    placeholder='Enter your full name' />
                                <Form.Control.Feedback type='invalid'>Please enter your full name</Form.Control.Feedback>
                            </Form.Group>

                            <Form.Group>
                                <Form.Label htmlFor='guestEmail'className="hotel-color">Email</Form.Label>
                                <Form.Control required type='email' name='guestEmail' id='guestEmail' value={booking.guestEmail}
                                    onChange={handleInputChange}
                                    placeholder='Enter your Email'
                                    disabled />
                                <Form.Control.Feedback type='invalid'>Please enter your Email</Form.Control.Feedback>
                            </Form.Group>

                            <fieldset style={{ border: '2px' }}>
                                <h5>Lodging period</h5>
                                <div className='row'>
                                    <div className='col-6'>
                                        <Form.Label htmlFor='checkInDate' className="hotel-color">Check-in Date</Form.Label>
                                        <Form.Control required type='date' name='checkInDate' id='checkInDate' value={booking.checkInDate}
                                            onChange={handleInputChange} />
                                        <Form.Control.Feedback type='invalid'>Please enter your check-in date</Form.Control.Feedback>
                                    </div>
                                    <div className='col-6'>
                                        <Form.Label htmlFor='checkOutDate' className="hotel-color">Check-out Date</Form.Label>
                                        <Form.Control required type='date' name='checkOutDate' id='checkOutDate' value={booking.checkOutDate}
                                            onChange={handleInputChange} />
                                        <Form.Control.Feedback type='invalid'>Please enter your check-out date</Form.Control.Feedback>
                                    </div>
                                    {errorMessage && <p className='text-danger'>{errorMessage}</p>}
                                </div>
                            </fieldset>
                            <fieldset style={{ border: '2px' }}>
                                <h5>Number of guests</h5>
                                <div className='row'>
                                    <div className='col-6'>
                                        <Form.Label htmlFor='numberOfAdults' className="hotel-color">Adults</Form.Label>
                                        <Form.Control required type='number' name='numberOfAdults' id='numberOfAdults' value={booking.numberOfAdults}
                                            onChange={handleInputChange} min='1' placeholder='0' />
                                        <Form.Control.Feedback type='invalid'>Please select at least 1 adult.</Form.Control.Feedback>
                                    </div>
                                    <div className='col-6'>
                                        <Form.Label htmlFor='numberOfChildren'className="hotel-color">Children</Form.Label>
                                        <Form.Control required type='number' name='numberOfChildren' id='numberOfChildren' value={booking.numberOfChildren}
                                            onChange={handleInputChange} min='0' placeholder='0' />
                                        <Form.Control.Feedback type='invalid'>Please enter the number of children</Form.Control.Feedback>
                                    </div>
                                </div>
                            </fieldset>
                            <div className='form-group mt-2 bm-2'>
                                <Button type='submit' className='btn btn-hotel'>Reserve Room</Button>
                            </div>
                        </Form>
                    </div>
                </div>
                <div className='col-md-6'>
                    {isSubmitted && (
                        <BookingSummary booking={booking} payment={calculatePayment()} onConfirm={handleBooking} isFormValid={isValidated} />
                    )}
                </div>
            </div>
        </div>
    );
}

export default BookingForm;
