package com.project.hotel_svc.repository;

import com.project.hotel_svc.entity.RoomInventory;
import java.time.LocalDate;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomInventoryRepository extends JpaRepository<RoomInventory, Long> {
    Optional<RoomInventory> findByRoomIdAndAvailableDate(Long roomId, LocalDate availableDate);
}
