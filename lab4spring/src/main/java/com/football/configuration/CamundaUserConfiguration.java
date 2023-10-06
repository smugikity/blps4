package com.football.configuration;

import com.football.service.BetService;
import com.football.service.MatchService;
import com.football.service.TeamService;
import com.football.service.UserService;
import org.camunda.bpm.client.spring.annotation.ExternalTaskSubscription;
import org.camunda.bpm.client.task.ExternalTaskHandler;
import org.camunda.bpm.engine.variable.Variables;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Map;
import com.football.model.*;

@Configuration
public class CamundaUserConfiguration {

    @Autowired
    UserService userService;
    @Autowired
    MatchService matchService;
    @Autowired
    BetService betService;
    @Autowired
    TeamService teamService;

    @Bean
    @ExternalTaskSubscription(topicName = "validation", processDefinitionKey = "footballProcess", lockDuration = 100)
    public ExternalTaskHandler validateAccount() {
        return (externalTask, externalTaskService) ->{
            System.out.println("ExternalTaskHandler validateAccount executed");
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            String password = externalTask.getVariable("password");
            String username = externalTask.getVariable("username");
            Map<String,Object> m =  Variables.createVariables();
            try {
                User user = userService.findByUsername(username);
                if (encoder.matches(password,user.getPassword())) {
                    m.put("accountExisted","true");
                    m.put("id", user.getId());
                    if (user.getRoles().stream().anyMatch(obj -> obj.getName().equals(ERole.ADMIN.getName()))) {
                        m.put("identity","admin");
                    } else {
                        m.put("identity","user");
                    }
                } else {
                    m.put("accountExisted","false");
                }
                System.out.println("validateAccount successfully.");
            } catch (Exception e) {
                m.put("accountExisted","false");
                System.out.println("validateAccount unsuccessfully.");
            }
            m.put("password","");
            externalTaskService.complete(externalTask,m);
        };
    }

    @Bean
    @ExternalTaskSubscription(topicName = "login", processDefinitionKey = "footballProcess", lockDuration = 100)
    public ExternalTaskHandler login(){
        return (externalTask, externalTaskService) ->{
            System.out.println("ExternalTaskHandler login executed");
            Map<String,Object> m =  Variables.createVariables();
            m.put("password","");
            System.out.println("login successfully.");
            externalTaskService.complete(externalTask, m);
        };
    }

    @Bean
    @ExternalTaskSubscription(topicName = "register", processDefinitionKey = "footballProcess", lockDuration = 100)
    public ExternalTaskHandler register(){
        return (externalTask, externalTaskService) ->{
            System.out.println("ExternalTaskHandler register executed");
            Map<String,Object> m =  Variables.createVariables();
            String username = externalTask.getVariable("username");
            String password = externalTask.getVariable("password");
            try {
                User user = userService.create(username,password);
                m.put("id", user.getId());
                System.out.println("register successfully.");
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("register unsuccessfully.");
            }
            externalTaskService.complete(externalTask,m);
        };
    }

    @Bean
    @ExternalTaskSubscription(topicName = "getMatches", processDefinitionKey = "footballProcess", lockDuration = 100)
    public ExternalTaskHandler getMatches(){
        return (externalTask, externalTaskService) -> {
            System.out.println("ExternalTaskHandler getMatches executed");
            Map<String,Object> m =  Variables.createVariables();
            matchService.findAll().forEach(obj -> System.out.println(obj.toString()));
            System.out.println("getMatches successfully.");
            externalTaskService.complete(externalTask,m);
        };
    }

    @Bean
    @ExternalTaskSubscription(topicName = "checkPoints", processDefinitionKey = "footballProcess", lockDuration = 100)
    public ExternalTaskHandler checkPoints(){
        return (externalTask, externalTaskService) -> {
            System.out.println("ExternalTaskHandler checkPoints executed");
            Map<String,Object> m =  Variables.createVariables();
            Long id = externalTask.getVariable("id");
            try {
                int userPoints = userService.findById(id).getPoint();
                int betPoints = externalTask.getVariable("betPoints");
                if (userPoints >= betPoints) {
                    m.put("enoughPoints", "true");
                } else {
                    m.put("enoughPoints", "false");
                }
                System.out.println("checkPoints successfully.");
            } catch (Exception e) {
                e.printStackTrace();
                m.put("enoughPoints", "false");
                System.out.println("checkPoints unsuccessfully.");
            }
            externalTaskService.complete(externalTask,m);
        };
    }

    @Bean
    @ExternalTaskSubscription(topicName = "placeBet", processDefinitionKey = "footballProcess", lockDuration = 100)
    public ExternalTaskHandler placeBet(){
        return (externalTask, externalTaskService) -> {
            System.out.println("ExternalTaskHandler placeBet executed");
            Map<String,Object> m =  Variables.createVariables();
            String username = externalTask.getVariable("username");
            Long teamId = ((Integer) externalTask.getVariable("betTeamId")).longValue();
            Long matchId = ((Integer) externalTask.getVariable("betMatchId")).longValue();
            int betPoints = externalTask.getVariable("betPoints");
            try {
                betService.createBet(username,betPoints,matchId,teamId);
                System.out.println("placeBet successfully.");
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("placeBet unsuccessfully.");
            }
            externalTaskService.complete(externalTask,m);
        };
    }
}
