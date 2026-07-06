package com.project.booking_svc.repository;

import com.project.booking_svc.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {

    List<Room> findByHotelId(Long hotelId);

    Optional<Room> findByRoomNumber(String roomNumber);

    List<Room> findByStatus(String status);
}

