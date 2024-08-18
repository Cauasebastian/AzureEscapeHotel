package org.sebastiandev.azureescapehotel.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.sebastiandev.azureescapehotel.model.Room;
import org.sebastiandev.azureescapehotel.response.RoomResponse;
import org.sebastiandev.azureescapehotel.service.BookingService;
import org.sebastiandev.azureescapehotel.service.IRoomService;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class RoomControllerTest {

    private MockMvc mockMvc;

    @Mock
    private IRoomService roomService;

    @Mock
    private BookingService bookingService;

    @InjectMocks
    private RoomController roomController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(roomController).build();
    }

    @Test
    void addNewRoom_Success() throws Exception {
        MockMultipartFile photo = new MockMultipartFile("photo", "photo.jpg", MediaType.IMAGE_JPEG_VALUE, "photo".getBytes());
        Room room = new Room();
        room.setId(1L);
        room.setRoomType("Deluxe");
        room.setRoomPrice(BigDecimal.valueOf(100.00));

        when(roomService.addNewRoom(any(), anyString(), any(BigDecimal.class))).thenReturn(room);

        mockMvc.perform(multipart("/rooms/add/new-room")
                        .file(photo)
                        .param("roomType", "Deluxe")
                        .param("roomPrice", "100.00"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.roomType").value("Deluxe"))
                .andExpect(jsonPath("$.roomPrice").value(100.00));
    }

    @Test
    void getRoomTypes_Success() throws Exception {
        when(roomService.getAllRoomTypes()).thenReturn(List.of("Deluxe", "Standard"));

        mockMvc.perform(get("/rooms/room-types"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").value("Deluxe"))
                .andExpect(jsonPath("$[1]").value("Standard"));
    }

    @Test
    void getAllRooms_Success() throws Exception {
        Room room = new Room();
        room.setId(1L);
        room.setRoomType("Deluxe");
        room.setRoomPrice(BigDecimal.valueOf(100.00));

        when(roomService.getAllRooms()).thenReturn(List.of(room));
        when(roomService.getRoomPhotoByRoomId(1L)).thenReturn("photo".getBytes());

        mockMvc.perform(get("/rooms/all-rooms"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].roomType").value("Deluxe"))
                .andExpect(jsonPath("$[0].roomPrice").value(100.00))
                .andExpect(jsonPath("$[0].photo").isNotEmpty());
    }

    @Test
    void deleteRoom_Success() throws Exception {
        doNothing().when(roomService).deleteRoom(1L);

        mockMvc.perform(delete("/rooms/delete/room/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void updateRoom_Success() throws Exception {
        MockMultipartFile photo = new MockMultipartFile("photo", "photo.jpg", MediaType.IMAGE_JPEG_VALUE, "photo".getBytes());
        Room room = new Room();
        room.setId(1L);
        room.setRoomType("Deluxe");
        room.setRoomPrice(BigDecimal.valueOf(100.00));

        when(roomService.updateRoom(anyLong(), anyString(), any(BigDecimal.class), any())).thenReturn(room);

        mockMvc.perform(multipart("/rooms/update/1")
                        .file(photo)
                        .param("roomType", "Deluxe")
                        .param("roomPrice", "100.00"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.roomType").value("Deluxe"))
                .andExpect(jsonPath("$.roomPrice").value(100.00));
    }

    @Test
    void getRoomById_Success() throws Exception {
        Room room = new Room();
        room.setId(1L);
        room.setRoomType("Deluxe");
        room.setRoomPrice(BigDecimal.valueOf(100.00));

        when(roomService.getRoomById(1L)).thenReturn(Optional.of(room));

        mockMvc.perform(get("/rooms/room/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.roomType").value("Deluxe"))
                .andExpect(jsonPath("$.roomPrice").value(100.00));
    }

    @Test
    void getRoomById_NotFound() throws Exception {
        when(roomService.getRoomById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/rooms/room/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getAvailableRooms_Success() throws Exception {
        Room room = new Room();
        room.setId(1L);
        room.setRoomType("Deluxe");
        room.setRoomPrice(BigDecimal.valueOf(100.00));

        when(roomService.getAvailableRooms(any(), any(), anyString())).thenReturn(List.of(room));
        when(roomService.getRoomPhotoByRoomId(1L)).thenReturn("photo".getBytes());

        mockMvc.perform(get("/rooms/available-rooms")
                        .param("checkInDate", "2023-10-01")
                        .param("checkOutDate", "2023-10-10")
                        .param("roomType", "Deluxe"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].roomType").value("Deluxe"))
                .andExpect(jsonPath("$[0].roomPrice").value(100.00))
                .andExpect(jsonPath("$[0].photo").isNotEmpty());
    }

    @Test
    void getAvailableRooms_NoContent() throws Exception {
        when(roomService.getAvailableRooms(any(), any(), anyString())).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/rooms/available-rooms")
                        .param("checkInDate", "2023-10-01")
                        .param("checkOutDate", "2023-10-10")
                        .param("roomType", "Deluxe"))
                .andExpect(status().isNoContent());
    }
}