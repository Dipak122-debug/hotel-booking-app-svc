package com.project.booking_svc.repository;

import com.project.booking_svc.model.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long> {

    List<Inventory> findByRoomId(Long roomId);

    Optional<Inventory> findByRoomIdAndDate(Long roomId, LocalDate date);

    List<Inventory> findByDateBetween(LocalDate startDate, LocalDate endDate);
}

