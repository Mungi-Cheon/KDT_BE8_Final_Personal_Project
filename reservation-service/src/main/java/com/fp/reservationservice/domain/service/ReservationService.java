package com.fp.reservationservice.domain.service;

import com.fp.reservationservice.domain.dto.request.ReservationCancelRequest;
import com.fp.reservationservice.domain.dto.request.ReservationRequest;
import com.fp.reservationservice.domain.dto.response.ReservationCancelResponse;
import com.fp.reservationservice.domain.dto.response.ReservationHistoryListResponse;
import com.fp.reservationservice.domain.dto.response.ReservationHistoryResponse;
import com.fp.reservationservice.domain.dto.response.ReservationResponse;
import com.fp.reservationservice.domain.entity.Reservation;
import com.fp.reservationservice.domain.exception.ErrorType;
import com.fp.reservationservice.domain.exception.ReservationsException;
import com.fp.reservationservice.domain.repository.ReservationRepository;
import com.fp.reservationservice.global.feign.client.AccommodationClient;
import com.fp.reservationservice.global.feign.client.RoomClient;
import com.fp.reservationservice.global.feign.dto.response.accommodation.AccommodationDetailResponse;
import com.fp.reservationservice.global.feign.dto.response.room.RoomDetailResponse;
import com.fp.reservationservice.global.feign.dto.response.room.RoomImageResponse;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final AccommodationClient accommodationClient;
    private final RoomClient roomClient;

    @Transactional(readOnly = true)
    public ReservationHistoryListResponse getReservationHistories(Long memberId) {

        List<Reservation> reservations = reservationRepository.findByMemberId(memberId);
        if (reservations.isEmpty()) {
            return ReservationHistoryListResponse.builder()
                .reservationHistoryList(new ArrayList<>()).build();
        }
        List<ReservationHistoryResponse> rhList = createReservationHistoryList(reservations);
        return ReservationHistoryListResponse.builder()
            .reservationHistoryList(rhList)
            .build();
    }

    @CacheEvict(value = "accommodations", allEntries = true)
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public ReservationResponse reserve(ReservationRequest request,
        Long memberId) {
        LocalDate checkInDate = request.getCheckInDate();
        LocalDate checkOutDate = request.getCheckOutDate();
        int nights = calcNight(checkInDate, checkOutDate);

        List<Reservation> alreadyReservationList = reservationRepository.findAlreadyReservation(
            memberId, request.getRoomId(), checkInDate, checkOutDate);

        if (!alreadyReservationList.isEmpty()) {
            throw new ReservationsException(ErrorType.ALREADY_RESERVATION);
        }

        AccommodationDetailResponse accommodation = accommodationClient.getAccommodationDetail(
            request.getAccommodationId());

        RoomDetailResponse room = roomClient.getRoomDetail(request.getAccommodationId(),
            request.getRoomId(), checkInDate, checkOutDate,
            request.getPersonNumber());

        // TODO : kafka 예약 이벤트 발행 -> room service 이벤트 구독

        Reservation reservation = createReservation(
            memberId, request.getAccommodationId(),
            request.getRoomId(), accommodation.getName(),
            room.getName(), request.getPersonNumber(),
            checkInDate, checkOutDate,
            room.getPrice(), nights,
            room.getType(), room.getStandardNumber(),
            room.getMaximumNumber()
        );

        Reservation saved = reservationRepository.save(reservation);
        return ReservationResponse.from(saved);
    }

    private Reservation createReservation(Long memberId, Long accommodationId,
        Long roomId, String accommodationName,
        String roomName, Integer personNumber,
        LocalDate checkInDate, LocalDate checkOutDate,
        int price, int night,
        String roomType, int standardNumber,
        int maximumNumber) {
        return Reservation.builder()
            .memberId(memberId)
            .accommodationId(accommodationId)
            .accommodationName(accommodationName)
            .roomId(roomId)
            .roomName(roomName)
            .personNumber(personNumber)
            .checkInDate(checkInDate)
            .checkOutDate(checkOutDate)
            .price(price)
            .night(night)
            .roomType(roomType)
            .standardNumber(standardNumber)
            .maximumNumber(maximumNumber)
            .build();
    }

    @Transactional()
    public ReservationCancelResponse cancelReservation(
        Long memberId, ReservationCancelRequest request) {
        Reservation reservation = reservationRepository
            .getReservationByIdAndMemberId(request.getId(), memberId);

        reservationRepository.delete(reservation);
        reservationRepository.flush();

        return ReservationCancelResponse.from(
            reservation,
            reservation.getAccommodationName(),
            reservation.getRoomType(),
            reservation.getStandardNumber(),
            reservation.getMaximumNumber(),
            LocalDateTime.now().withNano(0)
        );
    }

    private int calcNight(LocalDate checkInDate, LocalDate checkOutDate) {
        return (int) ChronoUnit.DAYS.between(checkInDate, checkOutDate);
    }

    private List<ReservationHistoryResponse> createReservationHistoryList(
        List<Reservation> reservations) {
        List<ReservationHistoryResponse> rhList = new ArrayList<>();

        reservations.forEach(reservation -> {
            RoomImageResponse roomImage = roomClient.getRoomImagesByRoomIds(
                reservation.getAccommodationId(), reservation.getRoomId());
            rhList.add(
                ReservationHistoryResponse.from(
                    reservation,
                    reservation.getAccommodationId(),
                    reservation.getAccommodationName(),
                    reservation.getRoomType(),
                    reservation.getStandardNumber(),
                    reservation.getMaximumNumber(),
                    roomImage.getImageUrlList()
                )
            );
        });
        return rhList;
    }
}