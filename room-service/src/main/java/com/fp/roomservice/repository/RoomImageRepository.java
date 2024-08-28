package com.fp.roomservice.repository;

import com.fp.roomservice.entity.RoomImage;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomImageRepository extends JpaRepository<RoomImage, Long> {

    List<RoomImage> findAllByRoomId(Long roomId);
}
