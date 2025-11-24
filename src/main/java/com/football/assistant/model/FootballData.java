package com.football.assistant.model;

public class FootballData {
    private String teamName;
    private String playerName;
    private String statistic;
    private String information;
    
    public FootballData() {}
    
    public FootballData(String teamName, String playerName, String statistic, String information) {
        this.teamName = teamName;
        this.playerName = playerName;
        this.statistic = statistic;
        this.information = information;
    }
    
    // Getters and Setters
    public String getTeamName() {
        return teamName;
    }
    
    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }
    
    public String getPlayerName() {
        return playerName;
    }
    
    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }
    
    public String getStatistic() {
        return statistic;
    }
    
    public void setStatistic(String statistic) {
        this.statistic = statistic;
    }
    
    public String getInformation() {
        return information;
    }
    
    public void setInformation(String information) {
        this.information = information;
    }
}
