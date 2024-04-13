package com.example.airlineproject.mapper;

import com.example.airlineproject.dto.TeamDto;
import com.example.airlineproject.entity.TeamMember;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TeamMapper {

    TeamDto map(TeamMember teamMember);

    TeamMember map(TeamDto teamDto);

}
