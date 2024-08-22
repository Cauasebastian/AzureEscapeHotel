package org.sebastiandev.azureescapehotel.service;

import lombok.RequiredArgsConstructor;
import org.sebastiandev.azureescapehotel.exception.InternalServerException;
import org.sebastiandev.azureescapehotel.exception.ResourceNotFoundException;
import org.sebastiandev.azureescapehotel.model.Room;
import org.sebastiandev.azureescapehotel.repository.RoomRepository;
import org.sebastiandev.azureescapehotel.utils.ImageCompressor;
import org.sebastiandev.azureescapehotel.utils.ImageDecompressor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.zip.DataFormatException;

@Service
@RequiredArgsConstructor
public class RoomService implements IRoomService {

    private final RoomRepository roomRepository;
    private static final Logger logger = LoggerFactory.getLogger(RoomService.class);
    private final ImageCompressor imageCompressor;
    private final ImageDecompressor imageDecompressor;

    @Override
    @Transactional
    @CachePut(value = "rooms", key = "#room.id")
    public Room addNewRoom(MultipartFile file, String roomType, BigDecimal roomPrice) {
        Room room = new Room();
        room.setRoomType(roomType);
        room.setRoomPrice(roomPrice);

        try {
            if (!file.isEmpty()) {
                byte[] photoBytes = file.getBytes();
                // Compress the image before saving
                byte[] compressedPhotoBytes = imageCompressor.compress(photoBytes);
                Blob photoBlob = new SerialBlob(compressedPhotoBytes);
                room.setPhoto(photoBlob);
            }
        } catch (Exception e) {
            // Log the exception instead of printing stack trace
            logger.error("Error occurred while adding new room", e);
            throw new RuntimeException("Failed to add new room", e);
        }

        return roomRepository.save(room);
    }

    @Override
    @Cacheable(value = "roomTypes")
    public List<String> getAllRoomTypes() {
        return roomRepository.findDistinctRoomType();
    }

    @Override
    @Cacheable(value = "rooms")
    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }

    @Override
    @Cacheable(value = "roomPhotos", key = "#roomId")
    public byte[] getRoomPhotoByRoomId(Long roomId) throws SQLException {
        Optional<Room> room = roomRepository.findById(roomId);
        if (room.isEmpty()) {
            throw new ResourceNotFoundException("Room not found with id: " + roomId);
        }
        Blob photoBlob = room.get().getPhoto();
        if (photoBlob != null) {
            byte[] compressedPhotoBytes = photoBlob.getBytes(1, (int) photoBlob.length());
            // Decompress the image before returning
            try {
                return imageDecompressor.decompress(compressedPhotoBytes);
            } catch (IOException | DataFormatException e) {
                logger.error("Error occurred while decompressing image", e);
                throw new RuntimeException("Failed to decompress image", e);
            }
        }
        return null;
    }

    @Override
    @CacheEvict(value = "rooms", key = "#roomId")
    public void deleteRoom(Long roomId) {
        Optional<Room> room = roomRepository.findById(roomId);
        if (room.isEmpty()) {
            throw new ResourceNotFoundException("Room not found with id: " + roomId);
        } else {
            roomRepository.deleteById(roomId);
        }
    }

    @Override
    @CachePut(value = "rooms", key = "#roomId")
    public Room updateRoom(Long roomId, String roomType, BigDecimal roomPrice, byte[] photoBytes) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found with id: " + roomId));
        if (roomType != null) {
            room.setRoomType(roomType);
        }
        if (roomPrice != null) {
            room.setRoomPrice(roomPrice);
        }
        if (photoBytes != null && photoBytes.length > 0) {
            try {
                // Compress the image before saving
                byte[] compressedPhotoBytes = imageCompressor.compress(photoBytes);
                room.setPhoto(new SerialBlob(compressedPhotoBytes));
            } catch (IOException e) {
                logger.error("Error occurred while compressing image", e);
                throw new RuntimeException("Failed to compress image", e);
            } catch (SQLException e) {
                throw new InternalServerException("Failed to update room photo", e);
            }
        }
        return roomRepository.save(room);
    }

    @Override
    @Cacheable(value = "rooms", key = "#roomId")
    public Optional<Room> getRoomById(Long roomId) {
        return Optional.of(roomRepository.findById(roomId).get());
    }

    @Override
    @Cacheable(value = "rooms")
    public List<Room> getAvailableRooms(LocalDate checkInDate, LocalDate checkOutDate, String roomType) {
        return roomRepository.findAvailableRoomsByDatesAndType(checkInDate, checkOutDate, roomType);
    }
}