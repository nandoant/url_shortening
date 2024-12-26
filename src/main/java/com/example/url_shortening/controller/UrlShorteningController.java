package com.example.url_shortening.controller;

import com.example.url_shortening.service.UrlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class UrlShorteningController {

    @Autowired
    private UrlService urlService;


}
