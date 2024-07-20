package org.sebastiandev.azureescapehotel.service;

import org.sebastiandev.azureescapehotel.model.BookedRoom;

import java.util.List;
@org.springframework.stereotype.Service
public interface IbookingService {
    void cancelBooking(Long bookingId);

    List<BookedRoom> getAllBookingsByRoomId(Long roomId);

    String saveBooking(Long roomId, BookedRoom bookingRequest);

    BookedRoom findByBookingConfirmationCode(String confirmationCode);

    List<BookedRoom> getAllBookings();

    List<BookedRoom> getBookingsByUserEmail(String email);
}
