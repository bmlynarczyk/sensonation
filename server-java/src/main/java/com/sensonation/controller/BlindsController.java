package com.sensonation.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BlindsController {

    @RequestMapping(value = "/blinds", method = RequestMethod.GET)
    public void getAll(){

    }

}
