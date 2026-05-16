package com.project.hotel_svc.service;

import com.project.hotel_svc.entity.Room;
import com.project.hotel_svc.model.RoomDto;
import java.util.List;

public interface RoomService {

    void createRoom(RoomDto roomDto);

    List<Room> getRoomsByHotelId(Long hotelId);

}

