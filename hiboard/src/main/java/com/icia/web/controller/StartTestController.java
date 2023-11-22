package com.icia.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.icia.web.util.CookieUtil;

@Controller("startTestController")
public class StartTestController {
   
   private static Logger logger = LoggerFactory.getLogger(StartTestController.class);
   
   @RequestMapping(value = "/start", method=RequestMethod.GET) 
   public String start(HttpServletRequest request, HttpServletResponse response)
   {
      return "/start";
   }

}