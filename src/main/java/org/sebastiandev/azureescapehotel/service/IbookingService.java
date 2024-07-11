package org.sebastiandev.azureescapehotel.service;

import org.sebastiandev.azureescapehotel.model.BookedRoom;

import java.util.List;

public interface IbookingService {
    void cancelBooking(Long bookingId);

    String saveBooking(Long roomId, BookedRoom bookingRequest);

    BookedRoom findBookingByConfirmationCode(String confirmationCode);

    List<BookedRoom> getAllBookingsByRoomId(Long roomId);

    List<BookedRoom> getAllBookings();
}
