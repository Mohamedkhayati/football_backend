package com.football.assistant.tools;

import dev.langchain4j.agent.tool.Tool;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class FootballStatsTool {
  /*  
    private final Map<String, String> footballRules;
    private final Map<String, String> teamStats;
    
    public FootballStatsTool() {
        // Initialize with basic football knowledge
        footballRules = new HashMap<>();
        footballRules.put("offside", "A player is in an offside position if they are nearer to the opponent's goal line than both the ball and the second-last opponent when the ball is played to them.");
        footballRules.put("penalty", "A penalty kick is awarded when a foul is committed inside the penalty area.");
        footballRules.put("red card", "A red card results in the player being sent off the field and cannot be replaced.");
        footballRules.put("var", "Video Assistant Referee (VAR) is used to review decisions made by the head referee.");
        
        teamStats = new HashMap<>();
        teamStats.put("real madrid", "Real Madrid has won 14 UEFA Champions League titles, the most by any club.");
        teamStats.put("barcelona", "FC Barcelona is known for its tiki-taka playing style and La Masia academy.");
        teamStats.put("manchester united", "Manchester United has won 20 English league titles.");
    }
    
    @Tool("Get information about football rules and regulations")
    public String getFootballRule(String ruleName) {
        String rule = footballRules.get(ruleName.toLowerCase());
        return rule != null ? rule : "Rule not found. Please ask about offside, penalty, red card, or VAR.";
    }
    
    @Tool("Get statistics and information about football teams")
    public String getTeamStats(String teamName) {
        String stats = teamStats.get(teamName.toLowerCase());
        return stats != null ? stats : "Team information not found in the database.";
    }
    
    @Tool("Calculate player statistics like goals per game")
    public String calculatePlayerStats(int goals, int games) {
        if (games == 0) {
            return "Cannot calculate stats with zero games.";
        }
        double average = (double) goals / games;
        return String.format("Goals per game: %.2f (Total: %d goals in %d games)", average, goals, games);
    }*/
}
