package com.example.airlineproject.service;

import com.example.airlineproject.dto.TeamDto;
import com.example.airlineproject.dto.TeamMemberChangeDto;
import com.example.airlineproject.entity.Company;
import com.example.airlineproject.entity.TeamMember;
import com.example.airlineproject.security.SpringUser;

import java.util.List;

public interface TeamService {

    TeamMember save(TeamDto teamDto, SpringUser springUser);

    List<TeamMember> findTeamMemberByCompanyAndActive(Company company);

    TeamMember findById(int id);

    void changeTeamMember(TeamMemberChangeDto teamMemberChangeDto);

    void deleteTeamMember(int id);

    List<TeamMember> getTeamMembersByFilter(String keyword);
}
