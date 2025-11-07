package com.dvc.RtLogistics.controller;

import lombok.Generated;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/")
public class HomeController {

    @PreAuthorize("hasAnyRole('USER')")
    @GetMapping("/home")
    public void getHome(){
        System.out.println("Home page it is!");
    }
}
