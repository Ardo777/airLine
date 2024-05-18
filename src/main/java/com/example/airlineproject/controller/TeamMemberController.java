package com.example.airlineproject.controller;

import com.example.airlineproject.dto.TeamMemberChangeDto;
import com.example.airlineproject.entity.TeamMember;
import com.example.airlineproject.security.SpringUser;
import com.example.airlineproject.service.TeamService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/manager")
@Slf4j
public class TeamMemberController {

    private final TeamService teamService;

    @GetMapping("/teamMembers")
    public String teamMembersPage(@AuthenticationPrincipal SpringUser springUser,
                                  ModelMap modelMap) {
        modelMap.addAttribute("teamMembers", teamService.findTeamMemberByCompanyAndActive(springUser.getUser().getCompany()));
        return "/manager/teamMembers";
    }

    @GetMapping("/teamMembers/delete/{id}")
    public String deleteTeamMember(@PathVariable("id") int id) {
        teamService.deleteTeamMember(id);
        log.info("Team Member deleted successfully,active is false");
        return "redirect:/manager/teamMembers";
    }

    @GetMapping("/teamMembers/change/{id}")
    public String changeTeamMemberPage(@PathVariable("id") int id,
                                       ModelMap modelMap) {
        TeamMember teamMember = teamService.findById(id);
        if (teamMember != null) {
            if (teamMember.isActive()) {
                modelMap.addAttribute("teamMember", teamMember);
                return "teamMemberUpdate";
            }
        }
        return "/manager/teamMembers";
    }

    @PostMapping("/teamMembers/change")
    public String changeTeamMember(@ModelAttribute TeamMemberChangeDto teamMemberChangeDto) {
        log.info("Changing team member with ID {}, name {}, surname {}, profession {}", teamMemberChangeDto.getId(), teamMemberChangeDto.getName(), teamMemberChangeDto.getSurname(), teamMemberChangeDto.getProfession());

        teamService.changeTeamMember(teamMemberChangeDto);
        return "redirect:/manager/teamMembers";
    }

}
