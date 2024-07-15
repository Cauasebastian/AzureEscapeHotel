package org.sebastiandev.azureescapehotel.service;

import lombok.RequiredArgsConstructor;
import org.sebastiandev.azureescapehotel.exception.InvalidBookingRequestException;
import org.sebastiandev.azureescapehotel.model.BookedRoom;
import org.sebastiandev.azureescapehotel.model.Room;
import org.sebastiandev.azureescapehotel.repository.BookingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingService implements IbookingService {

    private final BookingRepository bookingRepository;
    private final RoomService roomService;

    @Override
    public List<BookedRoom> getAllBookingsByRoomId(Long roomId) {
        return bookingRepository.findByRoomId(roomId);
    }
    @Override
    public List<BookedRoom> getAllBookings() {
        return bookingRepository.findAll();
    }

    @Override
    public void cancelBooking(Long bookingId) {
        bookingRepository.deleteById(bookingId);
    }

    @Transactional
    @Override
    public String saveBooking(Long roomId, BookedRoom bookingRequest) {
        if(bookingRequest.getCheckOutDate().isBefore(bookingRequest.getCheckInDate())){
            throw new InvalidBookingRequestException("Check out date cannot be before check in date");
        }
        Room room = roomService.getRoomById(roomId).get();
        List<BookedRoom> bookings = room.getBookings();
        boolean roomIsAvailable = roomIsAvailable(bookingRequest,bookings);
        if(roomIsAvailable){
            room.addBooking(bookingRequest);
            bookingRepository.save(bookingRequest);
        }
        else{
            throw new InvalidBookingRequestException("Room is not available for the requested dates");
        }
        return bookingRequest.getBookingConfirmationCode();
    }

    @Override
    public BookedRoom findBookingByConfirmationCode(String confirmationCode) {
        return bookingRepository.findByBookingConfirmationCode(confirmationCode);
    }
    private boolean roomIsAvailable(BookedRoom bookingRequest, List<BookedRoom> existingBookings) {
        return existingBookings.stream().noneMatch(existingBooking -> {
            boolean checkInDateIsAfterExistingCheckOutDate = bookingRequest.getCheckInDate().isAfter(existingBooking.getCheckOutDate());
            boolean checkOutDateIsBeforeExistingCheckInDate = bookingRequest.getCheckOutDate().isBefore(existingBooking.getCheckInDate());
            boolean checkInDateIsEqualExistingCheckOutDate = bookingRequest.getCheckInDate().isEqual(existingBooking.getCheckOutDate());
            return !checkInDateIsAfterExistingCheckOutDate && !checkOutDateIsBeforeExistingCheckInDate && !checkInDateIsEqualExistingCheckOutDate;
        });
    }
}