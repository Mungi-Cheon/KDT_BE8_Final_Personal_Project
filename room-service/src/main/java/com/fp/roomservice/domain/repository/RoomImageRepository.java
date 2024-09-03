package com.fp.roomservice.domain.repository;

import com.fp.roomservice.domain.entity.RoomImage;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomImageRepository extends JpaRepository<RoomImage, Long> {

    List<RoomImage> findAllByRoomId(Long roomId);
}
