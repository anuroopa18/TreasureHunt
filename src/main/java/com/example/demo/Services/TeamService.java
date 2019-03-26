package com.example.demo.Services;

import com.example.demo.Entity.QuestEntity;
import com.example.demo.Entity.TeamEntity;

import java.util.HashSet;
import java.util.Set;

public class TeamService {
    String[] names = {"Team A", "Team B","Team C", "Team D", "Team E", "Team F", "Team G", "Team H", "Team I",
        "Team J", "Team K", "Team L", "Team M", "Team N", "Team O", "Team P", "Team Q", "Team R", "Team S", "Team T",
        "Team U", "Team V", "Team W", "Team X", "Team Y", "Team Z"
    };
    public Set<TeamEntity> createTeams(QuestEntity quest) {
        Set<TeamEntity> teams = new HashSet<>();
        if (quest.getClues().size() == 0)
            return teams;

        int noOfTeams = quest.getNoOfTeams();
        for (int i = 0; i < noOfTeams; i++) {
            teams.add(new TeamEntity(names[i]));
        }

        return teams;
    }
}
