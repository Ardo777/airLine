package com.example.airlineproject.mapper;

import com.example.airlineproject.dto.CreateTicketDto;
import com.example.airlineproject.dto.TicketResponseFewDetailsDto;
import com.example.airlineproject.entity.Ticket;
import org.mapstruct.Mapper;




@Mapper(componentModel = "spring")
public interface TicketMapper {
   TicketResponseFewDetailsDto mapToTicketResponseFewDetailsDto(Ticket ticket);
   Ticket  mapToTicket(CreateTicketDto createTicketDto);

}
