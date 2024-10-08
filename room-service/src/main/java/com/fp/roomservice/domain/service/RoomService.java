package com.fp.roomservice.domain.service;

import com.fp.roomservice.domain.dto.response.RoomDetailResponse;
import com.fp.roomservice.domain.dto.response.RoomImageResponse;
import com.fp.roomservice.domain.dto.response.RoomListResponse;
import com.fp.roomservice.domain.dto.response.RoomOptionResponse;
import com.fp.roomservice.domain.dto.response.RoomResponse;
import com.fp.roomservice.domain.dto.response.RoomSimpleResponse;
import com.fp.roomservice.domain.entity.Room;
import com.fp.roomservice.domain.entity.RoomImage;
import com.fp.roomservice.domain.entity.RoomInfo;
import com.fp.roomservice.domain.entity.RoomOption;
import com.fp.roomservice.domain.repository.RoomImageRepository;
import com.fp.roomservice.global.exception.type.RoomErrorType;
import com.fp.roomservice.domain.exception.RoomException;
import com.fp.roomservice.domain.repository.RoomInfoRepository;
import com.fp.roomservice.domain.repository.RoomOptionRepository;
import com.fp.roomservice.domain.repository.RoomRepository;
import com.fp.roomservice.global.feign.client.AccommodationClient;
import com.fp.roomservice.global.feign.dto.response.accommodation.AccommodationDetailResponse;
import com.fp.roomservice.global.redis.RedissonLock;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;
    private final RoomInfoRepository roomInfoRepository;
    private final RoomImageRepository roomImageRepository;
    private final RoomOptionRepository roomOptionRepository;
    private final AccommodationClient accommodationClient;

    @Transactional
    public RoomListResponse getRoomList(Long accommodationId, LocalDate checkIn,
        LocalDate checkOut, int personNumber) {

        LocalDate checkInDate = validCheckInDate(checkIn);
        LocalDate checkOutDate = validCheckOutDate(checkOut);

        validatePersonNumber(personNumber);

        AccommodationDetailResponse accommodation = accommodationClient.getAccommodationDetail(
            accommodationId);

        List<Room> roomList = findAllRooms(accommodationId, personNumber);

        Map<Long, List<RoomInfo>> roomInfoMap = findRoomIdToInfoMap(roomList, checkInDate,
            checkOutDate);

        Map<Long, List<RoomImage>> imageMap = findRoomIdToImageMap(roomList);

        List<RoomResponse> roomResponses = createRoomResponse(roomList,
            roomInfoMap, imageMap);

        return RoomListResponse.from(accommodationId, accommodation.getName(),
            roomResponses, checkInDate, checkOutDate);
    }


    @Transactional(readOnly = true)
    public List<RoomDetailResponse> getRoomDetailList(Long accommodationId, LocalDate checkIn,
        LocalDate checkOut, int personNumber) {
        LocalDate checkInDate = validCheckInDate(checkIn);
        LocalDate checkOutDate = validCheckOutDate(checkOut);
        validatePersonNumber(personNumber);

        AccommodationDetailResponse accommodation = accommodationClient.getAccommodationDetail(
            accommodationId);
        List<Room> roomList = findAllRooms(accommodationId, personNumber);

        return roomList.stream().map(room -> {
            List<RoomInfo> availableRoomInfoList = findRoomInfoList(room.getId(), checkInDate,
                checkOutDate);

            int totalPrice = calculateTotalPrice(availableRoomInfoList);

            long expectedNights = ChronoUnit.DAYS.between(checkInDate, checkOutDate);
            if (availableRoomInfoList.size() < expectedNights) {
                throw new RoomException(RoomErrorType.NOT_FOUND);
            }

            List<String> imageList = roomImageRepository.findAllByRoomId(room.getId())
                .stream().map(RoomImage::getImageUrl).collect(Collectors.toList());

            RoomOption roomOption = roomOptionRepository.findByRoomId(room.getId())
                .orElseThrow(() -> new RoomException(RoomErrorType.NOT_FOUND));

            RoomOptionResponse roomOptionResponse = RoomOptionResponse.from(roomOption);

            int roomMinimumCount = availableRoomInfoList.stream()
                .mapToInt(RoomInfo::getCount)
                .min()
                .orElse(0);

            return RoomDetailResponse.from(room, accommodation.getName(),
                availableRoomInfoList.get(0).getPrice(), totalPrice, (int) expectedNights,
                roomMinimumCount, imageList, roomOptionResponse);
        }).toList();
    }

    @Transactional(readOnly = true)
    public RoomDetailResponse getRoomDetail(Long accommodationId, Long roomId,
        LocalDate checkIn, LocalDate checkOut, int personNumber) {
        LocalDate checkInDate = validCheckInDate(checkIn);
        LocalDate checkOutDate = validCheckOutDate(checkOut);

        validatePersonNumber(personNumber);

        AccommodationDetailResponse accommodation = accommodationClient.getAccommodationDetail(
            accommodationId);

        Room room = roomRepository.findByIdAndAccommodationId(roomId, accommodationId)
            .orElseThrow(() -> new RoomException(RoomErrorType.NOT_FOUND));

        if (personNumber > room.getMaximumNumber()) {
            throw new RoomException(RoomErrorType.INVALID_NUMBER_OF_PEOPLE);
        }

        List<RoomInfo> availableRoomInfoList = findRoomInfoList(roomId, checkInDate,
            checkOutDate);

        int totalPrice = calculateTotalPrice(availableRoomInfoList);

        long expectedNights = ChronoUnit.DAYS.between(checkInDate, checkOutDate);
        if (availableRoomInfoList.size() < expectedNights) {
            throw new RoomException(RoomErrorType.NOT_FOUND);
        }

        int roomMinimumCount = availableRoomInfoList.stream()
            .mapToInt(RoomInfo::getCount)
            .min()
            .orElse(0);

        List<String> imageList = roomImageRepository.findAllByRoomId(roomId)
            .stream().map(RoomImage::getImageUrl).collect(Collectors.toList());

        RoomOption roomOption = roomOptionRepository.findByRoomId(roomId)
            .orElseThrow(() -> new RoomException(RoomErrorType.NOT_FOUND));

        RoomOptionResponse roomOptionResponse = RoomOptionResponse.from(roomOption);

        return RoomDetailResponse.from(room, accommodation.getName(),
            availableRoomInfoList.get(0).getPrice(), totalPrice, (int) expectedNights,
            roomMinimumCount, imageList, roomOptionResponse);
    }

    @Transactional(readOnly = true)
    public List<RoomSimpleResponse> getSearchRoom(Long accommodationId, String keyword) {
        List<Room> roomList = roomRepository.findAllByAccommodationId(accommodationId)
            .stream()
            .filter(room -> room.getName().contains(keyword))
            .toList();
        Map<Long, List<RoomImage>> roomIdToImageMap = findRoomIdToImageMap(roomList);
        return createRoomSimpleResponse(roomList, roomIdToImageMap);
    }

    @Transactional(readOnly = true)
    public RoomImageResponse findRoomImagesByRoomId(Long accommodationId, Long roomId) {
        roomRepository.findByIdAndAccommodationId(roomId, accommodationId)
            .orElseThrow(() -> new RoomException(RoomErrorType.NOT_FOUND));

        List<RoomImage> roomImageList = roomImageRepository.findAllByRoomId(roomId);
        if (roomImageList.isEmpty()) {
            throw new RoomException(RoomErrorType.NOT_FOUND);
        }

        List<String> imageUrlList = roomImageList.stream().map(RoomImage::getImageUrl).toList();
        return RoomImageResponse.from(imageUrlList);
    }

    @Transactional
    @RedissonLock(key = "'reservation_' + #roomId + '_' + #checkIn + '_' + #checkOut")
    public void increaseCountByOne(Long roomId, LocalDate checkIn, LocalDate checkOut) {
        List<RoomInfo> roomInfoList = findRoomInfoList(roomId, checkIn, checkOut);

        for (RoomInfo roomInfo : roomInfoList) {
            roomInfo.increaseCountByOne();
        }
    }

    @Transactional
    @RedissonLock(key = "'reservation_' + #roomId + '_' + #checkIn + '_' + #checkOut")
    public void decreaseCountByOne(Long roomId, LocalDate checkIn, LocalDate checkOut) {

        List<RoomInfo> roomInfoList = findRoomInfoList(roomId, checkIn, checkOut);

        for (RoomInfo roomInfo : roomInfoList) {
            if (roomInfo.getCount() <= 0) {
                throw new RoomException(RoomErrorType.INCLUDES_FULLY_BOOKED_ROOM);
            }
            roomInfo.decreaseCountByOne();
        }
    }

    private List<RoomSimpleResponse> createRoomSimpleResponse(List<Room> roomList,
        Map<Long, List<RoomImage>> imageMap) {
        return roomList.stream()
            .map(room -> {
                List<String> imageUrlList = imageMap.get(room.getId())
                    .stream()
                    .map(RoomImage::getImageUrl)
                    .toList();
                RoomImageResponse roomImageResponse = RoomImageResponse.from(imageUrlList);
                return RoomSimpleResponse.from(room, roomImageResponse);
            }).toList();
    }

    private void validatePersonNumber(int personNumber) {
        if (personNumber < 1) {
            throw new RoomException(RoomErrorType.INVALID_NUMBER_OF_PEOPLE);
        }
    }

    private List<Room> findAllRooms(Long accommodationId, int personNumber) {
        List<Room> roomList = roomRepository.findAllByAccommodationIdAndStandardNumber(
            accommodationId, personNumber);

        if (roomList.isEmpty()) {
            throw new RoomException(RoomErrorType.INVALID_NUMBER_OF_PEOPLE);
        }
        return roomList;
    }

    private Map<Long, List<RoomInfo>> findRoomIdToInfoMap(List<Room> roomList,
        LocalDate checkInDate, LocalDate checkOutDate) {
        return roomList.stream()
            .collect(Collectors.toMap(
                Room::getId,
                room -> roomInfoRepository
                    .findByRoomIdAndDateRange(room.getId(), checkInDate, checkOutDate)
            ));
    }

    private Map<Long, List<RoomImage>> findRoomIdToImageMap(List<Room> roomList) {
        return roomList.stream()
            .collect(Collectors.toMap(
                Room::getId,
                room -> roomImageRepository.findAllByRoomId(room.getId())
            ));
    }

    private List<RoomResponse> createRoomResponse(List<Room> roomList,
        Map<Long, List<RoomInfo>> infoMap, Map<Long, List<RoomImage>> imageMap) {
        return roomList.stream()
            .map(room -> {
                int minCount = getMinimumCount(infoMap, room.getId());
                int price = getMinimumPrice(infoMap, room.getId());
                RoomImageResponse roomImageResponse = createRoomImageResponse(imageMap,
                    room.getId());
                return RoomResponse.from(room, minCount, price, roomImageResponse);
            })
            .collect(Collectors.toList());
    }

    private int getMinimumCount(Map<Long, List<RoomInfo>> infoMap, Long roomId) {
        return infoMap.get(roomId).stream()
            .mapToInt(RoomInfo::getCount)
            .min()
            .orElse(0);
    }

    private int getMinimumPrice(Map<Long, List<RoomInfo>> infoMap, Long roomId) {
        return infoMap.get(roomId).stream()
            .mapToInt(RoomInfo::getPrice)
            .min()
            .orElse(infoMap.get(roomId).get(0).getPrice());
    }

    private RoomImageResponse createRoomImageResponse(Map<Long, List<RoomImage>> imageMap,
        Long roomId) {
        return RoomImageResponse.from(
            imageMap.get(roomId)
                .stream()
                .map(RoomImage::getImageUrl)
                .toList());
    }

    private List<RoomInfo> findRoomInfoList(Long roomId, LocalDate checkInDate,
        LocalDate checkOutDate) {
        return roomInfoRepository
            .findByRoomIdAndDateRange(roomId, checkInDate, checkOutDate);
    }

    private int calculateTotalPrice(List<RoomInfo> roomInfoList) {
        return roomInfoList.stream()
            .mapToInt(RoomInfo::getPrice)
            .sum();
    }

    private LocalDate validCheckInDate(LocalDate checkIn) {
        return checkIn == null ? LocalDate.now() : checkIn;
    }

    private LocalDate validCheckOutDate(LocalDate checkOut) {
        return checkOut == null ? LocalDate.now().plusDays(1) : checkOut;
    }
}