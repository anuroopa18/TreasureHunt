package com.example.demo.Entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;

@Entity
@Table(name = "quests")
public class QuestEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    private int noOfPlayers;

    private int timeLimit;

    private boolean started;

    private Date startTime;

    private String code;

    private int noOfTeams;

    @OneToMany(mappedBy = "quest", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<TeamEntity> teams;

    @OneToMany(mappedBy = "quest", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @OrderBy()
    private Set<ClueEntity> clues;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNoOfPlayers() {
        return noOfPlayers;
    }

    public void setNoOfPlayers(int noOfPlayers) {
        this.noOfPlayers = noOfPlayers;
    }

    public int getTimeLimit() {
        return timeLimit;
    }

    public void setTimeLimit(int timeLimit) {
        this.timeLimit = timeLimit;
    }

    public boolean isStarted() {
        return started;
    }

    public void setStarted(boolean started) {
        this.started = started;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Set<TeamEntity> getTeams() {
        return teams;
    }

    public void addTeam(TeamEntity team) {
        this.teams.add(team);
    }

    public Set<ClueEntity> getClues() {
        return clues;
    }

    public void setClues(Set<ClueEntity> clues) {
        this.clues = clues;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getNoOfTeams() {
        return noOfTeams;
    }

    public void setNoOfTeams(int noOfTeams) {
        this.noOfTeams = noOfTeams;
    }
}
