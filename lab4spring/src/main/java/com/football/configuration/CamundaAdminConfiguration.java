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
public class CamundaAdminConfiguration {

    @Autowired
    UserService userService;
    @Autowired
    MatchService matchService;
    @Autowired
    BetService betService;
    @Autowired
    TeamService teamService;


    @Bean
    @ExternalTaskSubscription(topicName = "addMatch", processDefinitionKey = "footballProcess", lockDuration = 100)
    public ExternalTaskHandler addMatch(){
        return (externalTask, externalTaskService) -> {
            System.out.println("ExternalTaskHandler addMatch executed");
            try {
                String matchName = externalTask.getVariable("matchName");
                Long team1Id = ((Integer) externalTask.getVariable("team1Id")).longValue();
                Long team2Id = ((Integer) externalTask.getVariable("team2Id")).longValue();
                matchService.create(matchName, team1Id, team2Id);
                System.out.println("Add "+matchName+" match successfully.");
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Add match unsuccessfully.");
                //externalTaskService.handleFailure(externalTask,e.getMessage(), Arrays.toString(e.getStackTrace()),1,1000L);
            }
            externalTaskService.complete(externalTask);

        };
    }

    @Bean
    @ExternalTaskSubscription(topicName = "addTeam", processDefinitionKey = "footballProcess", lockDuration = 100)
    public ExternalTaskHandler addTeam(){
        return (externalTask, externalTaskService) ->{
            System.out.println("ExternalTaskHandler addTeam executed");
            try {
                String teamName = externalTask.getVariable("teamName");
                String teamStadium = externalTask.getVariable("teamStadium");
                String teamCity = externalTask.getVariable("teamCity");
                teamService.create(teamName,teamStadium,teamCity);
                System.out.println("Add "+teamName+" team successfully.");
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Add team unsuccessfully.");
            }
            externalTaskService.complete(externalTask);
        };
    }

}
