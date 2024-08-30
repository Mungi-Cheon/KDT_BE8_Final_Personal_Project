package com.fp.accommodationservice.repository;

import com.fp.accommodationservice.entity.AccommodationImage;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccommodationImageRepository extends JpaRepository<AccommodationImage, Long> {

    List<AccommodationImage> findByAccommodationId(Long accommodationId);
}
