package com.fp.reservationservice.domain.repository;

import com.fp.reservationservice.domain.entity.Reservation;
import com.fp.reservationservice.domain.exception.ErrorType;
import com.fp.reservationservice.domain.exception.ReservationsException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    @Query("SELECT r FROM Reservation r WHERE r.memberId =:memberId")
    List<Reservation> findByMemberId(@Param("memberId") Long memberId);

    @Query("SELECT r "
        + "FROM Reservation r "
        + "WHERE r.memberId = :memberId "
        + "AND r.roomId = :productId "
        + "AND :checkInDate <= r.checkInDate "
        + "AND :checkOutDate >= r.checkOutDate")
    List<Reservation> findAlreadyReservation(
        @Param("memberId") Long memberId,
        @Param("productId") Long productId,
        @Param("checkInDate") LocalDate checkInDate,
        @Param("checkOutDate") LocalDate checkOutDate);

    Optional<Reservation> findByIdAndMemberId(Long id, Long memberId);


    default Reservation getReservationByIdAndMemberId(Long id, Long memberId) {
        return findByIdAndMemberId(id, memberId).orElseThrow(
            () -> new ReservationsException(ErrorType.NOT_FOUND));
    }

    default Reservation getReservationById(Long id) {
        return findById(id).orElseThrow(
            () -> new ReservationsException(ErrorType.NOT_FOUND));
    }
}
