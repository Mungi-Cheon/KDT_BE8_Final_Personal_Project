package com.fp.reservationservice.domain.repository;

import com.fp.reservationservice.domain.entity.Reservation;
import com.fp.reservationservice.domain.exception.ErrorType;
import com.fp.reservationservice.domain.exception.ReservationsException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    @Query("SELECT r FROM Reservation r "
        + "WHERE r.memberId =:memberId "
        + "AND r.status <> 'FAILURE'")
    List<Reservation> findByMemberId(@Param("memberId") Long memberId);

    @Query("SELECT r "
        + "FROM Reservation r "
        + "WHERE r.memberId = :memberId "
        + "AND r.roomId = :productId "
        + "AND :checkInDate <= r.checkInDate "
        + "AND :checkOutDate >= r.checkOutDate "
        + "AND r.status <> 'FAILURE' "
        + "AND r.status <> 'CANCEL'")
    List<Reservation> findAlreadyReservation(
        @Param("memberId") Long memberId,
        @Param("productId") Long productId,
        @Param("checkInDate") LocalDate checkInDate,
        @Param("checkOutDate") LocalDate checkOutDate);

    @Modifying
    @Query("UPDATE Reservation r SET r.status = 'SUCCESS' WHERE r.id = :id")
    void updateStatusToSuccessById(Long id);

    @Modifying
    @Query("UPDATE Reservation r SET r.status = 'FAILURE' WHERE r.id = :id")
    void updateStatusToFailureById(Long id);

    Optional<Reservation> findByIdAndMemberId(Long id, Long memberId);

    default Reservation getReservationByIdAndMemberId(Long id, Long memberId) {
        return findByIdAndMemberId(id, memberId).orElseThrow(
            () -> new ReservationsException(ErrorType.NOT_FOUND));
    }
}
