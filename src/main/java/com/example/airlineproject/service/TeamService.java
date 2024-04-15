package com.example.airlineproject.service;

import com.example.airlineproject.dto.TeamDto;
import com.example.airlineproject.entity.Company;
import com.example.airlineproject.entity.TeamMember;
import com.example.airlineproject.entity.enums.Profession;
import com.example.airlineproject.security.SpringUser;

import java.util.List;

public interface TeamService {

    TeamMember save(TeamDto teamDto, SpringUser springUser);

    List<TeamMember> findTeamMemberByCompanyAndActive(Company company);

    TeamMember findById(int id);

    void changeTeamMember(int id, String name, String surname, Profession profession);
}
