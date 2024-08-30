package com.fp.roomservice.service;

import com.fp.roomservice.entity.Room;
import com.fp.roomservice.entity.RoomImage;
import com.fp.roomservice.entity.RoomInfo;
import com.fp.roomservice.entity.RoomOption;
import com.fp.roomservice.exception.ErrorType;
import com.fp.roomservice.exception.RoomException;
import com.fp.roomservice.repository.RoomImageRepository;
import com.fp.roomservice.repository.RoomInfoRepository;
import com.fp.roomservice.repository.RoomOptionRepository;
import com.fp.roomservice.repository.RoomRepository;
import com.fp.roomservice.response.RoomDetailResponse;
import com.fp.roomservice.response.RoomImageResponse;
import com.fp.roomservice.response.RoomListResponse;
import com.fp.roomservice.response.RoomOptionResponse;
import com.fp.roomservice.response.RoomResponse;
import com.fp.roomservice.response.RoomSimpleResponse;
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

    @Transactional
    public RoomListResponse getRoomList(Long accommodationId, LocalDate checkIn,
        LocalDate checkOut, int personNumber) {

        LocalDate checkInDate = validCheckInDate(checkIn);
        LocalDate checkOutDate = validCheckOutDate(checkOut);

        validatePersonNumber(personNumber);

        // TODO kafka 이벤트 통신 room service <--> accommodation service
        String accommodationName = "";

        List<Room> roomList = findAllRooms(accommodationId, personNumber);

        Map<Long, List<RoomInfo>> roomInfoMap = findRoomIdToInfoMap(roomList, checkInDate,
            checkOutDate);

        Map<Long, List<RoomImage>> imageMap = findRoomIdToImageMap(roomList);

        List<RoomResponse> roomResponses = createRoomResponse(roomList,
            roomInfoMap, imageMap);

        return RoomListResponse.from(accommodationId, accommodationName,
            roomResponses, checkInDate, checkOutDate);
    }

    @Transactional(readOnly = true)
    public RoomDetailResponse getRoomDetail(Long accommodationId, Long roomId,
        LocalDate checkIn, LocalDate checkOut, int personNumber) {
        LocalDate checkInDate = validCheckInDate(checkIn);
        LocalDate checkOutDate = validCheckOutDate(checkOut);

        validatePersonNumber(personNumber);

        // TODO kafka 이벤트 통신 room service <--> accommodation service
        String accommodationName = "";

        Room room = roomRepository.findByIdAndAccommodationId(roomId, accommodationId)
            .orElseThrow(() -> new RoomException(ErrorType.NOT_FOUND));

        if (personNumber > room.getMaximumNumber()) {
            throw new RoomException(ErrorType.INVALID_NUMBER_OF_PEOPLE);
        }

        List<RoomInfo> availableRoomInfoList = findAvailableRoomInfo(roomId, checkInDate,
            checkOutDate);

        int totalPrice = calculateTotalPrice(availableRoomInfoList);

        long expectedNights = ChronoUnit.DAYS.between(checkInDate, checkOutDate);
        if (availableRoomInfoList.size() < expectedNights) {
            throw new RoomException(ErrorType.NOT_FOUND);
        }

        List<String> imageList = roomImageRepository.findAllByRoomId(roomId)
            .stream().map(RoomImage::getImageUrl).collect(Collectors.toList());

        RoomOption roomOption = roomOptionRepository.findByRoomId(roomId)
            .orElseThrow(() -> new RoomException(ErrorType.NOT_FOUND));

        RoomImageResponse roomImageResponse = RoomImageResponse.from(imageList);
        RoomOptionResponse roomOptionResponse = RoomOptionResponse.from(roomOption);

        return RoomDetailResponse.from(room, accommodationName,
            availableRoomInfoList.get(0).getPrice(), totalPrice, (int) expectedNights,
            roomImageResponse, roomOptionResponse);
    }

    @Transactional(readOnly = true)
    public List<RoomSimpleResponse> getSearchRoom(Long accommodationId, String keyword) {
        List<Room> roomList = roomRepository.findAllByAccommodationId(accommodationId)
            .stream()
            .filter(room -> room.getName().contains(keyword))
            .toList();
        Map<Long, List<RoomImage>> roomIdToImageMap = findRoomIdToImageMap(roomList);
        return createRoomSimpleResponse(roomList, roomIdToImageMap, keyword);
    }

    private List<RoomSimpleResponse> createRoomSimpleResponse(List<Room> roomList,
        Map<Long, List<RoomImage>> imageMap, String keyword) {
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
            throw new RoomException(ErrorType.INVALID_NUMBER_OF_PEOPLE);
        }
    }

    private List<Room> findAllRooms(Long accommodationId, int personNumber) {
        List<Room> roomList = roomRepository.findAllByAccommodationIdAndStandardNumber(
            accommodationId, personNumber);

        if (roomList.isEmpty()) {
            throw new RoomException(ErrorType.INVALID_NUMBER_OF_PEOPLE);
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

    private List<RoomInfo> findAvailableRoomInfo(Long roomId, LocalDate checkInDate,
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