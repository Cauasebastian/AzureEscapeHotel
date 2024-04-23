package org.sebastiandev.azureescapehotel.repository;

import org.sebastiandev.azureescapehotel.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RoomRepository extends JpaRepository<Room, Long> {

    @Query("SELECT DISTINCT r.RoomType FROM Room r")
    List<String> findDistinctRoomType();
}
