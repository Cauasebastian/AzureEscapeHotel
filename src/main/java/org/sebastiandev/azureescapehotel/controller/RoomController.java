package org.sebastiandev.azureescapehotel.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.codec.binary.Base64;
import org.sebastiandev.azureescapehotel.exception.ResourceNotFoundException;
import org.sebastiandev.azureescapehotel.model.BookedRoom;
import org.sebastiandev.azureescapehotel.model.Room;
import org.sebastiandev.azureescapehotel.response.BookingResponse;
import org.sebastiandev.azureescapehotel.response.RoomResponse;
import org.sebastiandev.azureescapehotel.service.BookingService;
import org.sebastiandev.azureescapehotel.service.IRoomService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequiredArgsConstructor
@RequestMapping("/rooms")
public class RoomController {
    private final IRoomService roomService;
    private final BookingService bookingService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @PostMapping("/add/new-room")
    public ResponseEntity<RoomResponse> addNewRoom(
            @RequestParam("photo") MultipartFile photo,
            @RequestParam("roomType") String roomType,
            @RequestParam("roomPrice") BigDecimal roomPrice) throws IOException {
        Room savedRoom = roomService.addNewRoom(photo, roomType, roomPrice);
        RoomResponse response = new RoomResponse(savedRoom.getId(), savedRoom.getRoomType(),
                savedRoom.getRoomPrice());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/room-types")
    public List<String> getRoomTypes() {
        return roomService.getAllRoomTypes();
    }

    @GetMapping("/all-rooms")
    public ResponseEntity<List<RoomResponse>> getAllRooms() throws IOException {
        List<Room> rooms = roomService.getAllRooms();
        List<RoomResponse> roomResponses = new ArrayList<>();
        for (Object room : rooms) {  // Note o uso de Object aqui para lidar com LinkedHashMap
            Room convertedRoom = convertToRoom(room);
            RoomResponse roomResponse = getRoomResponse(convertedRoom);
            roomResponses.add(roomResponse);
        }
        return ResponseEntity.ok(roomResponses);
    }

    @DeleteMapping("/delete/room/{roomId}")
    public ResponseEntity<Void> deleteRoom(@PathVariable Long roomId) {
        roomService.deleteRoom(roomId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/update/{roomId}")
    public ResponseEntity<RoomResponse> updateRoom(@PathVariable Long roomId,
                                                   @RequestParam(required = false) String roomType,
                                                   @RequestParam(required = false) BigDecimal roomPrice,
                                                   @RequestParam(required = false) MultipartFile photo) throws IOException, SQLException {
        byte[] photoBytes = (photo != null && !photo.isEmpty()) ?
                photo.getBytes() : roomService.getRoomPhotoByRoomId(roomId);

        Room updatedRoom = roomService.updateRoom(roomId, roomType, roomPrice, photoBytes);
        RoomResponse roomResponse = getRoomResponse(convertToRoom(updatedRoom));
        return ResponseEntity.ok(roomResponse);
    }

    @GetMapping("/room/{roomId}")
    public ResponseEntity<Optional<RoomResponse>> getRoomById(@PathVariable Long roomId) {
        Optional<Room> theroom = roomService.getRoomById(roomId);
        if (theroom.isEmpty()) {
            throw new ResourceNotFoundException("Room not found with id: " + roomId);
        }

        Room room = convertToRoom(theroom.get());
        RoomResponse roomResponse = getRoomResponse(room);
        return ResponseEntity.ok(Optional.of(roomResponse));
    }

    @GetMapping("/available-rooms")
    public ResponseEntity<List<RoomResponse>> getAvailableRooms(
            @RequestParam("checkInDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkInDate,
            @RequestParam("checkOutDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkOutDate,
            @RequestParam("roomType") String roomType) throws IOException {
        List<Room> availableRooms = roomService.getAvailableRooms(checkInDate, checkOutDate, roomType);
        List<RoomResponse> roomResponses = new ArrayList<>();
        for (Object room : availableRooms) {  // Note o uso de Object aqui para lidar com LinkedHashMap
            Room convertedRoom = convertToRoom(room);
            RoomResponse roomResponse = getRoomResponse(convertedRoom);
            roomResponses.add(roomResponse);
        }
        if (roomResponses.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(roomResponses);
        }
    }

    private Room convertToRoom(Object object) {
        // Se o objeto for um LinkedHashMap, converte para Room, caso contrário, retorna o próprio objeto
        if (object instanceof Room) {
            return (Room) object;
        }
        return objectMapper.convertValue(object, Room.class);
    }

    private RoomResponse getRoomResponse(Room room) {
        List<BookedRoom> bookings = getAllBookingsByRoomId(room.getId());
        List<BookingResponse> bookingInfo = bookings
                .stream()
                .map(booking -> new BookingResponse(booking.getBookingId(),
                        booking.getCheckInDate(),
                        booking.getCheckOutDate(), booking.getBookingConfirmationCode())).toList();

        byte[] photoBytes = room.getRoomImage().getImage();
        String base64Photo = photoBytes != null ? Base64.encodeBase64String(photoBytes) : null;

        return new RoomResponse(room.getId(),
                room.getRoomType(), room.getRoomPrice(),
                room.isBooked(), base64Photo, bookingInfo);
    }

    private List<BookedRoom> getAllBookingsByRoomId(Long roomId) {
        return bookingService.getAllBookingsByRoomId(roomId);
    }
}
