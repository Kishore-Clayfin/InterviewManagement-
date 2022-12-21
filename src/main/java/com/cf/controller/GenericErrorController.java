package com.cf.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class GenericErrorController implements ErrorController {
	  @RequestMapping("/error")
//	  @ResponseBody
public String error() {
	return "login.html";
}
//@Override
//public String getErrorPath() {
//  return "/error";
//}


}
