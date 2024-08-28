package com.fp.roomservice.repository;

import com.fp.roomservice.entity.RoomOption;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomOptionRepository extends JpaRepository<RoomOption, Long> {

    Optional<RoomOption> findByRoomId(Long roomId);
}
