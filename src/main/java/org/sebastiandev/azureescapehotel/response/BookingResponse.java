package org.sebastiandev.azureescapehotel.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.sebastiandev.azureescapehotel.model.Room;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingResponse {
    private Long id;
    private Long bookingId;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private String guestFullName;
    private String guestEmail;
    private int numOfAdults;
    private int numOfChildren;
    private int totalNumOfGuest;
    private String bookingConfirmationCode;
    private RoomResponse roomResponse;

    public BookingResponse(Long id, LocalDate checkInDate, LocalDate checkOutDate, String bookingConfirmationCode) {
        this.id = id;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.bookingConfirmationCode = bookingConfirmationCode;
    }

    public BookingResponse(Long id, LocalDate checkInDate, LocalDate checkOutDate, String guestFullName, String guestEmail, int numOfAdults, int numOfChildren, int totalNumOfGuest, String bookingConfirmationCode, RoomResponse room) {
        this.id = id;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.guestFullName = guestFullName;
        this.guestEmail = guestEmail;
        this.numOfAdults = numOfAdults;
        this.numOfChildren = numOfChildren;
        this.totalNumOfGuest = totalNumOfGuest;
        this.bookingConfirmationCode = bookingConfirmationCode;
        this.roomResponse = room;
    }
}