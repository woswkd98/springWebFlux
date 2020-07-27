package com.project.backend.routerEx;

import org.springframework.context.annotation.Configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import com.project.backend.handlers.RequestHandler;
import com.project.backend.handlers.TagHandler;

@Configuration
public class TagController {
    
    @Autowired
    TagHandler tagHandler;
   
}