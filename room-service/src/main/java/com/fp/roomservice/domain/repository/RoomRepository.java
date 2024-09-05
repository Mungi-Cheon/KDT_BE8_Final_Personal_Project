package com.fp.roomservice.domain.repository;

import com.fp.roomservice.domain.entity.Room;
import com.fp.roomservice.global.exception.type.RoomErrorType;
import com.fp.roomservice.domain.exception.RoomException;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RoomRepository extends JpaRepository<Room, Long> {

    List<Room> findAllByAccommodationId(Long accommodationId);

    Optional<Room> findByIdAndAccommodationId(Long id, Long accommodationId);

    @Query("SELECT r FROM Room r "
        + "WHERE r.accommodationId = :accommodationId "
        + "AND r.standardNumber <= :personNumber "
        + "AND r.maximumNumber >= :personNumber"
    )
    List<Room> findAllByAccommodationIdAndStandardNumber(Long accommodationId,
        Integer personNumber);

    @Query("SELECT r FROM Room r "
        + "LEFT JOIN RoomOption ro ON ro.roomId = r.id "
        + "LEFT JOIN RoomImage ri ON ri.roomId = r.id "
        + "WHERE r.id = :roomId "
        + "AND r.accommodationId = :accommodationId")
    Optional<Room> findByAccommodationIdAndRoomIdJoinImagesAndOption(
        @Param("accommodationId") Long accommodationId, @Param("roomId") Long roomId);

    default Room getByAccommodationIdAndRoomIdJoinImagesAndOption(
        Long accommodationId, Long roomId) {
        return findByAccommodationIdAndRoomIdJoinImagesAndOption(accommodationId, roomId)
            .orElseThrow(() -> new RoomException(RoomErrorType.NOT_FOUND));
    }
}