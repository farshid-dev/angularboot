package com.avasyspod.angularboot.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.Enumeration;

@RestController
public class ServiceAuthenticate {
	
	@RequestMapping(value = "/user")
	  public Principal user(Principal user, HttpServletRequest request) {

		System.out.println("I am inside of ServiceAuthenticate...");

		Enumeration<String> headerNames = request.getHeaderNames();
		while (headerNames.hasMoreElements()) {
			String headerName = headerNames.nextElement();
			String headerValue = request.getHeader(headerName);
			System.out.println("HttpServletRequest content is as : " + headerName + ": " + headerValue);
		}

	    return user;
	  }

}
