package com.mycompany.backend.controller;

import java.net.URI;

import javax.servlet.http.HttpServletResponse;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ErrorHandlerController implements ErrorController{
  @RequestMapping("/error")
  public ResponseEntity<String> error(HttpServletResponse response) {
    int status = response.getStatus();
    if(status == 404) {
      return ResponseEntity.status(HttpStatus.MOVED_PERMANENTLY)//301
          .location(URI.create("/"))
          .body("");
    } else {
      return ResponseEntity.status(status).body("invalide access token");
    }
  }
}
