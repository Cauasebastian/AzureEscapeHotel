import React from 'react';
import { useLocation } from 'react-router-dom';
import Header from '../Common/Header';

const BookingSuccess = () => {
    const location = useLocation();
    const { success, message } = location.state || {};

    return (
        <div className='container'>
            <Header title='Booking Status'/>
            <div className='mt-5'>
                {success ? (
                    <div>
                        <h3 className='text-success'>Booking Successful</h3>
                        <p className='text-success'>{message}</p>
                    </div>
                ) : (
                    <div>
                        <h3 className='text-danger'>Error booking room</h3>
                        <p className='text-danger'>{message}</p>
                    </div>
                )}
            </div>
        </div>
    );
};

export default BookingSuccess;