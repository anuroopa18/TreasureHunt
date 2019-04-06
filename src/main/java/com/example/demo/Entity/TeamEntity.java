package com.example.demo.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "teams")
public class TeamEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    private Date endTimeQuest;

    private Integer score;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "clue_id")
    private ClueEntity clue_on;

    @JsonIgnore
    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<SubmissionEntity> submissions;

    @ManyToMany(mappedBy = "teams")
    private Set<UserEntity> users;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "quest_id")
    private QuestEntity quest;

    public TeamEntity() {}

    public TeamEntity(String name) {
        this.name = name;
        this.endTimeQuest = null;
        this.score = 0;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public QuestEntity getQuest() {
        return quest;
    }

    public void setQuest(QuestEntity quest) {
        this.quest = quest;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ClueEntity getClue_on() {
        return clue_on;
    }

    public void setClue_on(ClueEntity clue_on) {
        this.clue_on = clue_on;
    }

    public Date getEndTimeQuest() {
        return endTimeQuest;
    }

    public void setEndTimeQuest(Date endTimeQuest) {
        this.endTimeQuest = endTimeQuest;
    }

    public Set<SubmissionEntity> getSubmissions() {
        return submissions;
    }

    public void setSubmissions(Set<SubmissionEntity> submissions) {
        this.submissions = submissions;
    }

    public Set<UserEntity> getUsers() {
        return users;
    }

    public void setUsers(Set<UserEntity> users) {
        this.users = users;
    }

    public void addUser(UserEntity user) {
        users.add(user);
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }
}
