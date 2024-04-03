package com.example.airlineproject.service.impl;

import com.example.airlineproject.dto.TeamDto;
import com.example.airlineproject.entity.Company;
import com.example.airlineproject.entity.TeamMember;
import com.example.airlineproject.entity.User;
import com.example.airlineproject.mapper.TeamMapper;
import com.example.airlineproject.repository.TeamRepository;
import com.example.airlineproject.security.SpringUser;
import com.example.airlineproject.service.TeamService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class TeamServiceImpl implements TeamService {
    private final TeamMapper teamMapper;
    private final TeamRepository teamRepository;

    @Override
    public TeamMember save(TeamDto teamDto, SpringUser springUser) {
        User user = springUser.getUser();
        if (user != null) {
            Company company = user.getCompany();
            if (company != null) {
                TeamMember teamMember = teamMapper.map(teamDto);
                teamMember.setCompany(company);
                return teamRepository.save(teamMember);
            }
        }
        return null;
    }
}
