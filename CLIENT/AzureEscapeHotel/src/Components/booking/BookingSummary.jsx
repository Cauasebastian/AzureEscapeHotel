import { Button } from 'bootstrap'
import moment from 'moment'
import React, { useEffect } from 'react'
import { useState } from 'react'
import { useNavigate } from 'react-router-dom'

const BookingSummary = ({booking,payment,isFormValid,onConfirm}) => {
    const checkInDate = moment(booking.checkInDate)
    const checkOutDate = moment(booking.checkOutDate)
    const numOfDays = checkOutDate.diff(checkInDate, 'days')
    const[isBookingConfirmed, setIsBookingConfirmed] = useState(false)
    const[isProcessingPayment, setIsProcessingPayment] = useState(false)

    const navigate  = useNavigate()

    const handleConfirmBooking = async () => {
        setIsProcessingPayment(true)
        setTimeout(() => {
            setIsProcessingPayment(false)
            setIsBookingConfirmed(true)
            onConfirm()
        }, 3000)
    }
    useEffect(() => {
        if(isBookingConfirmed){
            navigate('/booking-success')
        }
    }, [isBookingConfirmed, navigate])

  return (
    <div className='card card-body mt-5'>
            <h4>Reservation Summary</h4>
            <p>FullName: <strong>{booking.guestName}</strong></p>
            <p>Email: <strong>{booking.guestEmail}</strong></p>
            <p>Check-in Date: <strong>{moment(booking.checkInDate).format('MMMM Do YYYY')}</strong></p>
            <p>Check-out Date: <strong>{moment(booking.checkOutDate).format('MMMM Do YYYY')}</strong></p>
            <p>Number of Days: <strong>{numOfDays}</strong></p>
            <div>
                <h5>Number of Guests</h5>
                <p>Adults: <strong>{booking.numberOfAdults > 1 ? "s" : ""} : {booking.numberOfAdults}</strong></p>
                <p>Children: <strong>{booking.numberOfChildren}</strong></p>  
            </div>
            {payment > 0 ? (
                <>
                    <p>Total Payment: <strong>{payment}</strong></p>
                    {isFormValid && !isBookingConfirmed ? (
                        <Button variant='success' onClick={handleConfirmBooking}>
                            {isProcessingPayment ? (
                                <>
                                    <span className='spinner-border spinner-border-sm mr-2' role='status' aria-hidden='true'></span>
                                    Booking Confirmed, redirecting to payment gateway...
                                </>
                            ) : (
                                "Confirm Booking and Proceed to Payment"
                            )}
                        </Button>
                    ) : isBookingConfirmed ? (
                        <div className='d-flex justify-content-center align-items-center'>
                            <div className='spinner-border text-primary' role='status'>
                                <span className='sr-only'>Loading...</span>
                            </div>
                        </div>
                    ) : null}
                </>
            ) : (
                <p className='text-danger'>Check-out date must be after check-in date</p>
            )}
        </div>
    );
};

export default BookingSummary;