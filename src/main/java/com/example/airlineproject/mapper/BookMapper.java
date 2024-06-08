package com.example.airlineproject.mapper;

import com.example.airlineproject.dto.ResponseBusinessBookingDto;
import com.example.airlineproject.entity.Booking;
import org.mapstruct.Mapper;

import java.util.List;


@Mapper(componentModel = "spring",uses = {TicketMapper.class})
public interface BookMapper {
  List<ResponseBusinessBookingDto> mapToResponseBusinessBookDtoList(List<Booking> bookings);
}
