package com.example.airlineproject.mapper;

import com.example.airlineproject.dto.TeamDto;
import com.example.airlineproject.entity.TeamMember;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-04-16T08:27:35+0400",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 17.0.10 (Amazon.com Inc.)"
)
@Component
public class TeamMapperImpl implements TeamMapper {

    @Override
    public TeamDto map(TeamMember teamMember) {
        if ( teamMember == null ) {
            return null;
        }

        TeamDto.TeamDtoBuilder teamDto = TeamDto.builder();

        teamDto.name( teamMember.getName() );
        teamDto.surname( teamMember.getSurname() );
        teamDto.profession( teamMember.getProfession() );

        return teamDto.build();
    }

    @Override
    public TeamMember map(TeamDto teamDto) {
        if ( teamDto == null ) {
            return null;
        }

        TeamMember teamMember = new TeamMember();

        teamMember.setName( teamDto.getName() );
        teamMember.setSurname( teamDto.getSurname() );
        teamMember.setProfession( teamDto.getProfession() );

        return teamMember;
    }
}
