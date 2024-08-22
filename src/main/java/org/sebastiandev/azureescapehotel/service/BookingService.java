package org.sebastiandev.azureescapehotel.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.sebastiandev.azureescapehotel.exception.InvalidBookingRequestException;
import org.sebastiandev.azureescapehotel.exception.ResourceNotFoundException;
import org.sebastiandev.azureescapehotel.model.BookedRoom;
import org.sebastiandev.azureescapehotel.model.Room;
import org.sebastiandev.azureescapehotel.repository.BookingRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingService implements IbookingService {

    private final BookingRepository bookingRepository;
    private final IRoomService roomService;


    @Override
    @Cacheable(value = "bookings")
    public List<BookedRoom> getAllBookings() {
        return bookingRepository.findAll();
    }


    @Override
    @Cacheable(value = "bookings", key = "#email")
    public List<BookedRoom> getBookingsByUserEmail(String email) {
        return bookingRepository.findByGuestEmail(email);
    }

    @Override
    @CacheEvict(value = "bookings", key = "#bookingId")
    public void cancelBooking(Long bookingId) {
        bookingRepository.deleteById(bookingId);
    }

    @Override
    @Cacheable(value = "bookings", key = "#roomId")
    public List<BookedRoom> getAllBookingsByRoomId(Long roomId) {
        return bookingRepository.findByRoomId(roomId);
    }
    @Transactional
    @Override
    public String saveBooking(Long roomId, BookedRoom bookingRequest) {
        if (bookingRequest.getCheckOutDate().isBefore(bookingRequest.getCheckInDate())){
            throw new InvalidBookingRequestException("Check-in date must come before check-out date");
        }
        Room room = roomService.getRoomById(roomId).get();
        List<BookedRoom> existingBookings = room.getBookings();
        boolean roomIsAvailable = roomIsAvailable(bookingRequest,existingBookings);
        if (roomIsAvailable){
            room.addBooking(bookingRequest);
            bookingRepository.save(bookingRequest);
        }else{
            throw  new InvalidBookingRequestException("Sorry, This room is not available for the selected dates;");
        }
        return bookingRequest.getBookingConfirmationCode();
    }

    @Override
    @Cacheable(value = "bookings", key = "#confirmationCode")
    public BookedRoom findByBookingConfirmationCode(String confirmationCode) {
        return bookingRepository.findByBookingConfirmationCode(confirmationCode)
                .orElseThrow(() -> new ResourceNotFoundException("No booking found with booking code :"+confirmationCode));

    }


    private boolean roomIsAvailable(BookedRoom bookingRequest, List<BookedRoom> existingBookings) {
        return existingBookings.stream()
                .noneMatch(existingBooking ->
                        bookingRequest.getCheckInDate().equals(existingBooking.getCheckInDate())
                                || bookingRequest.getCheckOutDate().isBefore(existingBooking.getCheckOutDate())
                                || (bookingRequest.getCheckInDate().isAfter(existingBooking.getCheckInDate())
                                && bookingRequest.getCheckInDate().isBefore(existingBooking.getCheckOutDate()))
                                || (bookingRequest.getCheckInDate().isBefore(existingBooking.getCheckInDate())

                                && bookingRequest.getCheckOutDate().equals(existingBooking.getCheckOutDate()))
                                || (bookingRequest.getCheckInDate().isBefore(existingBooking.getCheckInDate())

                                && bookingRequest.getCheckOutDate().isAfter(existingBooking.getCheckOutDate()))

                                || (bookingRequest.getCheckInDate().equals(existingBooking.getCheckOutDate())
                                && bookingRequest.getCheckOutDate().equals(existingBooking.getCheckInDate()))

                                || (bookingRequest.getCheckInDate().equals(existingBooking.getCheckOutDate())
                                && bookingRequest.getCheckOutDate().equals(bookingRequest.getCheckInDate()))
                );
    }
}