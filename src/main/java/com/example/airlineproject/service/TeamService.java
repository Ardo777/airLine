package com.example.airlineproject.service;

import com.example.airlineproject.dto.TeamDto;
import com.example.airlineproject.entity.TeamMember;
import com.example.airlineproject.security.SpringUser;

public interface TeamService {
    TeamMember save(TeamDto teamDto, SpringUser springUser);
}
