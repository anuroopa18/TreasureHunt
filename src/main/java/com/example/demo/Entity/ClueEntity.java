package com.example.demo.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Entity
@Table(name = "clues")
public class ClueEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String puzzle;

    private String solution;

    private int points;

    private boolean isDeleted;

    @OneToOne(fetch = FetchType.EAGER, cascade =  CascadeType.ALL)
    @JoinColumn(name = "hint_id")
    private HintEntity hint;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "quest_id", nullable = false)
    private QuestEntity quest;

    @JsonIgnore
    @OneToMany(mappedBy = "clue", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<SubmissionEntity> submissions;

    @JsonIgnore
    @OneToMany(mappedBy = "clue_on", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<TeamEntity> teams;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPuzzle() {
        return puzzle;
    }

    public void setPuzzle(String puzzle) {
        this.puzzle = puzzle;
    }

    public String getSolution() {
        return solution;
    }

    public void setSolution(String solution) {
        this.solution = solution;
    }

    public QuestEntity getQuest() {
        return quest;
    }

    public void setQuest(QuestEntity quest) {
        this.quest = quest;
    }

    public HintEntity getHint() {
        return hint;
    }

    public void setHint(HintEntity hint) {
        this.hint = hint;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public Set<SubmissionEntity> getSubmissions() {
        return submissions;
    }

    public void setSubmissions(Set<SubmissionEntity> submissions) {
        this.submissions = submissions;
    }

    public Set<TeamEntity> getTeams() {
        return teams;
    }

    public void setTeams(Set<TeamEntity> teams) {
        this.teams = teams;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }
}
