package com.fp.accommodationservice.repository;

import com.fp.accommodationservice.entity.Accommodation;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AccommodationRepository extends JpaRepository<Accommodation, Long> {

    @Query(value = "SELECT a FROM Accommodation a " +
        "WHERE (:lastAccommodationId IS NULL OR a.id > :lastAccommodationId) " +
        "ORDER BY a.id ASC")
    List<Accommodation> findAccommodations(
        @Param("lastAccommodationId") Long lastAccommodationId);

    @Query(value = "SELECT a FROM Accommodation a " +
        "WHERE (a.category = :category) " +
        "AND (:lastAccommodationId IS NULL OR a.id > :lastAccommodationId) " +
        "ORDER BY a.id ASC")
    List<Accommodation> findAccommodationsByCategory(
        @Param("category") String category,
        @Param("lastAccommodationId") Long lastAccommodationId);


}
