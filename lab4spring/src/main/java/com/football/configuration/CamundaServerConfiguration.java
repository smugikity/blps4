package com.football.configuration;

import com.football.service.BetService;
import com.football.service.MatchService;
import com.football.service.TeamService;
import com.football.service.UserService;
import org.camunda.bpm.client.spring.annotation.ExternalTaskSubscription;
import org.camunda.bpm.client.task.ExternalTaskHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CamundaServerConfiguration {
    @Autowired
    UserService userService;
    @Autowired
    MatchService matchService;
    @Autowired
    BetService betService;
    @Autowired
    TeamService teamService;

    @Bean
    @ExternalTaskSubscription(topicName = "addPointToAllUsers", processDefinitionKey = "Timer", lockDuration = 100)
    public ExternalTaskHandler addPointToAllUsers(){
        return (externalTask, externalTaskService) ->{
            System.out.println("ExternalTaskHandler addPointToAllUsers executed");
            try {
                userService.addPointsToAllUsers(100);
                System.out.println("addPointToAllUsers successfully.");
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("addPointToAllUsers unsuccessfully.");
            }
            externalTaskService.complete(externalTask);
        };
    }

    @Bean
    @ExternalTaskSubscription(topicName = "updateMatchScore", processDefinitionKey = "footballProcess", lockDuration = 100)
    public ExternalTaskHandler updateMatchScore(){
        return (externalTask, externalTaskService) ->{
            System.out.println("ExternalTaskHandler updateMatchScore executed");
            Long matchId = externalTask.getVariable("matchId");
            Byte team1Score = ((Integer) externalTask.getVariable("team1Score")).byteValue();
            Byte team2Score = ((Integer) externalTask.getVariable("team2Score")).byteValue();
            try {
                matchService.updateScore(matchId,team1Score,team2Score);
                System.out.println("updateMatchScore successfully.");
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("updateMatchScore unsuccessfully.");
            }
            externalTaskService.complete(externalTask);
        };
    }

    @Bean
    @ExternalTaskSubscription(topicName = "updateUsersPoints", processDefinitionKey = "footballProcess", lockDuration = 100)
    public ExternalTaskHandler updateUsersPoints(){
        return (externalTask, externalTaskService) ->{
            System.out.println("ExternalTaskHandler updateUsersPoints executed");
            Long matchId = externalTask.getVariable("matchId");
            try {
                betService.updateUsersPoints(matchId);
                System.out.println("updateUsersPoints successfully.");
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("updateUsersPoints unsuccessfully.");
            }
            externalTaskService.complete(externalTask);
        };
    }

}
