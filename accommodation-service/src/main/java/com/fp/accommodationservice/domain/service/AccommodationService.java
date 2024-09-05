package com.fp.accommodationservice.domain.service;

import static com.fp.accommodationservice.domain.entity.type.RoomType.STANDARD;

import com.fp.accommodationservice.domain.dto.response.AccommodationResponse;
import com.fp.accommodationservice.domain.exception.ErrorType;
import com.fp.accommodationservice.domain.repository.AccommodationImageRepository;
import com.fp.accommodationservice.domain.dto.response.AccommodationDetailResponse;
import com.fp.accommodationservice.domain.entity.Accommodation;
import com.fp.accommodationservice.domain.entity.AccommodationImage;
import com.fp.accommodationservice.domain.exception.AccommodationException;
import com.fp.accommodationservice.domain.repository.AccommodationRepository;
import com.fp.accommodationservice.feign.client.RoomClient;
import com.fp.accommodationservice.feign.dto.response.room.RoomDetailResponse;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AccommodationService {

    private final AccommodationRepository accommodationRepository;
    private final AccommodationImageRepository accommodationImageRepository;
    private final RoomClient roomClient;
    private static final int PAGE_SIZE = 8;

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
                int price = roomClient.getRoomDetailList(
                        accommodation.getId(), checkInDate, checkOutDate, personNumber).stream()
                    .filter(room -> STANDARD.getName().equalsIgnoreCase(room.getType()))
                    .mapToInt(RoomDetailResponse::getPrice)
                    .findFirst()
                    .orElse(0);
                String imageUrl = accommodationImageRepository
                    .findByAccommodationId(accommodation.getId()).get(0).getImageUrl();

                return AccommodationResponse.from(accommodation, imageUrl, price);
            })
            .toList();
    }

    public AccommodationDetailResponse findAccommodation(Long accommodationId) {
        Accommodation accommodation = accommodationRepository.findById(accommodationId)
            .orElseThrow(() -> new AccommodationException(ErrorType.NOT_FOUND));

        List<String> imageUrlList = accommodationImageRepository.findByAccommodationId(
                accommodationId).stream().map(AccommodationImage::getImageUrl)
            .toList();

        return AccommodationDetailResponse.from(accommodation, imageUrlList);
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

    private boolean hasValidRooms(Accommodation accommodation, LocalDate checkInDate,
        LocalDate checkOutDate, Integer personNumber) {
        List<RoomDetailResponse> roomEntityList = roomClient.getRoomDetailList(
            accommodation.getId(), checkInDate, checkOutDate, personNumber);

        return roomEntityList.stream()
            .anyMatch(room -> room.getRoomCount() > 0);
    }

    private LocalDate validCheckInDate(LocalDate checkIn) {
        return checkIn == null ? LocalDate.now() : checkIn;
    }

    private LocalDate validCheckOutDate(LocalDate checkOut) {
        return checkOut == null ? LocalDate.now().plusDays(1) : checkOut;
    }
}
