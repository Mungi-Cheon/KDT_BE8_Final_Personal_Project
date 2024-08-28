package com.fp.accommodationservice.service;

import com.fp.accommodationservice.dto.response.AccommodationResponse;
import com.fp.accommodationservice.entity.Accommodation;
import com.fp.accommodationservice.entity.RoomInfo;
import com.fp.accommodationservice.exception.AccommodationException;
import com.fp.accommodationservice.exception.ErrorType;
import com.fp.accommodationservice.repository.AccommodationImageRepository;
import com.fp.accommodationservice.repository.AccommodationRepository;
import com.fp.accommodationservice.repository.RoomRepository;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AccommodationService {

    private final AccommodationRepository accommodationRepository;
    private final RoomRepository roomRepository;
    private final AccommodationImageRepository accommodationImageRepository;
    private static final int PAGE_SIZE = 8;

    //    @Cacheable(value = "accommodations", keyGenerator = "customKeyGenerator")
    @Transactional(readOnly = true)
    public List<AccommodationResponse> getAvailableAccommodations(
        String category, LocalDate checkIn, LocalDate checkOut, int personNumber,
        Long lastAccommodationId) {

        LocalDate checkInDate = validCheckInDate(checkIn);
        LocalDate checkOutDate = validCheckOutDate(checkOut);
        validPersonNumber(personNumber);

        List<Accommodation> accommodations = findAccommodations(category, lastAccommodationId);
        List<Accommodation> validAccommodations = accommodations.stream()
            .filter(accommodation
                -> hasValidRooms(accommodation, checkInDate, checkOutDate, personNumber))
            .limit(PAGE_SIZE)
            .toList();

        if (validAccommodations.isEmpty()) {
            throw new AccommodationException(ErrorType.NOT_FOUND);
        }

        return validAccommodations.stream()
            .map(accommodation -> {

                // TODO kafka 이벤트 통신 accommodation service <--> room service
//                int price = roomRepository.findAllByAccommodationId(accommodation.getId()).stream()
//                    .filter(room -> STANDARD.getName().equalsIgnoreCase(room.getType()))
//                    .flatMap(
//                        room -> findRoomBetweenDates(room.getId(), checkInDate,
//                            checkOutDate).stream())
//                    .mapToInt(RoomInfo::getPrice)
//                    .findFirst()
//                    .orElse(0);
                int price = 0;
                String imageUrl = accommodationImageRepository
                    .findByAccommodationId(accommodation.getId()).get(0).getImageUrl();

                return AccommodationResponse.from(accommodation, imageUrl, price);
            })
            .toList();
    }

    private List<Accommodation> findAccommodations(String category, Long lastAccommodationId) {
        if (category.isEmpty()) {
            return accommodationRepository.findAccommodations(lastAccommodationId);
        } else {
            return accommodationRepository.findAccommodationsByCategory(category,
                lastAccommodationId);
        }
    }

    private void validPersonNumber(int personNumber) {
        if (personNumber < 1) {
            throw new AccommodationException(ErrorType.INVALID_NUMBER_OF_PEOPLE);
        }
    }

    private boolean hasValidRooms(Accommodation accommodation, LocalDate checkIn,
        LocalDate checkOut, Integer personNumber) {
        // TODO kafka 이벤트 통신 accommodation service <--> room service
//        List<Room> roomEntityList = roomRepository.findAllByAccommodationId(
//            accommodation.getId());
//        return roomEntityList.stream()
//            .filter(room -> room.getMaximumNumber() >= personNumber)
//            .anyMatch(room -> areAllDatesAvailable(room.getId(), checkIn, checkOut));
        return true;
    }

//    private boolean areAllDatesAvailable(Long roomId, LocalDate checkIn, LocalDate checkOut) {
//        List<RoomInfo> perNights = findRoomBetweenDates(roomId, checkIn, checkOut);
//
//        return perNights.stream()
//            .allMatch(perNight -> perNight.getCount() > 0);
//    }

    private List<RoomInfo> findRoomBetweenDates(Long roomId, LocalDate checkIn,
        LocalDate checkOut) {
        // TODO kafka 이벤트 통신 accommodation service <--> room service
//        return roomInfoRepository.findByRoomIdAndDateRange(roomId, checkIn, checkOut);
        return new ArrayList<>();
    }

    private LocalDate validCheckInDate(LocalDate checkIn) {
        return checkIn == null ? LocalDate.now() : checkIn;
    }

    private LocalDate validCheckOutDate(LocalDate checkOut) {
        return checkOut == null ? LocalDate.now().plusDays(1) : checkOut;
    }
}
