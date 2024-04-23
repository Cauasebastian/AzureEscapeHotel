package org.sebastiandev.azureescapehotel.service;

import lombok.RequiredArgsConstructor;
import org.sebastiandev.azureescapehotel.model.Room;
import org.sebastiandev.azureescapehotel.repository.RoomRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.math.BigDecimal;
import java.sql.Blob;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RoomService implements IRoomService {

    private final RoomRepository roomRepository;
    private static final Logger logger = LoggerFactory.getLogger(RoomService.class);

    @Override
    @Transactional
    public Room addNewRoom(MultipartFile file, String roomType, BigDecimal roomPrice) {
        Room room = new Room();
        room.setRoomType(roomType);
        room.setRoomPrice(roomPrice);

        try {
            if (!file.isEmpty()) {
                byte[] photoBytes = file.getBytes();
                Blob photoBlob = new SerialBlob(photoBytes);
                room.setPhoto(photoBlob);
            }
        } catch (Exception e) {
            // Log the exception instead of printing stack trace
            logger.error("Error occurred while adding new room", e);
            // Optionally, re-throw the exception if necessary
            throw new RuntimeException("Failed to add new room", e);
        }

        return roomRepository.save(room);
    }

    @Override
    public List<String> getAllRoomTypes() {
        return roomRepository.findDistinctRoomType();
    }
}