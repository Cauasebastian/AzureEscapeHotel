package org.sebastiandev.azureescapehotel.repository;

import org.sebastiandev.azureescapehotel.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<Room, Long> {
}
