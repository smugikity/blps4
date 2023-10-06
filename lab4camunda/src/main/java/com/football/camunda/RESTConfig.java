package com.football.camunda;

import org.camunda.bpm.spring.boot.starter.rest.CamundaJerseyResourceConfig;
import org.springframework.stereotype.Component;

import javax.ws.rs.ApplicationPath;

@Component
@ApplicationPath("/rest")
public class RESTConfig extends CamundaJerseyResourceConfig {

}