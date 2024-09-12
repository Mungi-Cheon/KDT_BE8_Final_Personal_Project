package com.fp.roomservice.domain.repository;

import com.fp.roomservice.domain.entity.RoomInfo;
import jakarta.persistence.LockModeType;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RoomInfoRepository extends JpaRepository<RoomInfo, Long> {

    @Query("SELECT r FROM RoomInfo r " +
        "WHERE r.roomId = :roomId "
        + "AND r.date >= :checkInDate "
        + "AND r.date < :checkOutDate")
    List<RoomInfo> findByRoomIdAndDateRange(
        @Param("roomId") Long roomId,
        @Param("checkInDate") LocalDate checkInDate,
        @Param("checkOutDate") LocalDate checkOutDate);

    @Query("SELECT CASE WHEN COUNT(ri) > 0 THEN TRUE ELSE FALSE END " +
        "FROM Room r " +
        "LEFT JOIN RoomInfo ri ON ri.id = r.id " +
        "AND ri.date = :date " +
        "AND ri.count > 0" +
        "WHERE r.id = :roomId ")
    boolean existsByRoomIdAndDate(
        @Param("roomId") Long roomId,
        @Param("date") LocalDate date
    );

    @Query("SELECT MIN(r.count) FROM RoomInfo r " +
        "WHERE r.roomId = :roomId "
        + "AND r.date >= :checkInDate "
        + "AND r.date < :checkOutDate")
    Integer findMinCountByRoomIdAndDateRange(
        @Param("roomId") Long roomId,
        @Param("checkInDate") LocalDate checkInDate,
        @Param("checkOutDate") LocalDate checkOutDate);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT r FROM RoomInfo r "
        + "WHERE r.roomId = :roomId "
        + "AND r.date >= :checkInDate "
        + "AND r.date < :checkOutDate")
    List<RoomInfo> findByRoomIdAndDateRangeWithPessimisticLock(
        @Param("roomId") Long roomId,
        @Param("checkInDate") LocalDate checkInDate,
        @Param("checkOutDate") LocalDate checkOutDate);
}