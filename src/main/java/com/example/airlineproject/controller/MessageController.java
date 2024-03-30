package com.example.airlineproject.controller;

import com.example.airlineproject.security.SpringUser;
import com.example.airlineproject.service.MessageService;
import com.example.airlineproject.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;

import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@RequiredArgsConstructor
public class MessageController {
    private final MessageService messageService;
    private final UserService userService;


    @GetMapping("/message/chat")
    public String getCurrentlyMessages(@RequestParam(value = "id") int id, ModelMap modelMap, @AuthenticationPrincipal SpringUser springUser) {
        modelMap.put("messages", messageService.getCurrentlyMessages(springUser.getUser(), id));
        modelMap.put("user", userService.findById(id).orElse(null));
        return "/admin/message";
    }

    @PostMapping("/message/chat")
    public String sendMessage(@RequestParam("to") int to,
                              @RequestParam("message") String message,
                             @AuthenticationPrincipal SpringUser springUser
    ) {
        if (!message.isBlank()){
           messageService.save(to,message, springUser.getUser());
        }
        return "redirect:/message/chat?id="+to;
    }
}
