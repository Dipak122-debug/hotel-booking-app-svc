package com.project.hotel_svc.repository;

import com.project.hotel_svc.entity.Hotel;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface HotelRepository extends JpaRepository<Hotel, Long> {
    @Query("SELECT h FROM Hotel h WHERE (:city IS NULL OR h.city = :city) " +
           "AND (:minPrice IS NULL OR h.rating >= :minRating) " +
           "AND (:maxPrice IS NULL OR h.rating <= :maxRating)")
    List<Hotel> searchHotels(@Param("city") String city,
                            @Param("minPrice") BigDecimal minPrice,
                            @Param("maxPrice") BigDecimal maxPrice,
                            @Param("minRating") BigDecimal minRating,
                            @Param("maxRating") BigDecimal maxRating);
}
