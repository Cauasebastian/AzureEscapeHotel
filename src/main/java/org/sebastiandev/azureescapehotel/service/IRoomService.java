package org.sebastiandev.azureescapehotel.service;

import org.sebastiandev.azureescapehotel.model.Room;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

public interface IRoomService {

    Room addNewRoom(MultipartFile photo, String roomType, BigDecimal roomPrice);
}