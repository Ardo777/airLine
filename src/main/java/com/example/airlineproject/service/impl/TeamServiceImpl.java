package com.example.airlineproject.service.impl;

import com.example.airlineproject.dto.TeamDto;
import com.example.airlineproject.entity.Company;
import com.example.airlineproject.entity.TeamMember;
import com.example.airlineproject.entity.User;
import com.example.airlineproject.entity.enums.Profession;
import com.example.airlineproject.mapper.TeamMapper;
import com.example.airlineproject.repository.TeamRepository;
import com.example.airlineproject.security.SpringUser;
import com.example.airlineproject.service.TeamService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

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
                teamMember.setActive(true);
                TeamMember savedTeamMember = teamRepository.save(teamMember);
                log.info("Saved team member with ID: {}", savedTeamMember.getId());
                return savedTeamMember;
            } else {
                log.warn("Company is null for user {}", user.getId());
            }
        } else {
            log.warn("User is null in SpringUser object");
        }
        return null;
    }

    @Override
    public List<TeamMember> findTeamMemberByCompanyAndActive(Company company) {
        return teamRepository.findTeamMemberByCompanyAndActive(company, true);
    }

    @Override
    public TeamMember findById(int id) {
        return teamRepository.findById(id).orElse(null);
    }

    @Override
    public void changeTeamMember(int id, String name, String surname, Profession profession) {
        TeamMember byId = findById(id);
        byId.setName(name);
        byId.setSurname(surname);
        byId.setProfession(profession);
        teamRepository.save(byId);
    }
}
