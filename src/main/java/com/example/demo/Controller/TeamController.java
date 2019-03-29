package com.example.demo.Controller;

import com.example.demo.Entity.ClueEntity;
import com.example.demo.Entity.TeamEntity;
import com.example.demo.Repository.ClueRepository;
import com.example.demo.Repository.QuestRepository;
import com.example.demo.Repository.TeamRepository;
import com.example.demo.Repository.UserRepository;
import com.example.demo.Services.MainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.util.Date;

@RestController
public class TeamController {
    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private QuestRepository questRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ClueRepository clueRepository;


    @GetMapping("/api/teams")
    public Iterable<TeamEntity> findAllTeams() {
        return teamRepository.findAll();
    }

    @GetMapping("/api/teams/{id}")
    public TeamEntity findTeamById(@PathVariable("id") int id) {
        return teamRepository.findById(id).orElseThrow(() ->
            new EntityNotFoundException("Team is not found " + id));
    }

    @DeleteMapping("/api/teams/{id}")
    public void deleteTeam(@PathVariable("id") Integer id) {
        teamRepository.deleteById(id);
    }

    @PutMapping("/api/team/{id}")
    public TeamEntity updateTeam(@RequestBody TeamEntity newTeam, @PathVariable("id") Integer id) {
        return teamRepository.findById(id)
            .map(team -> {
                if (newTeam.getName() != team.getName())
                    team.setName(newTeam.getName());
                if (newTeam.getEndTimeQuest() != team.getEndTimeQuest())
                    team.setEndTimeQuest(newTeam.getEndTimeQuest());
                return teamRepository.save(team);
            })
            .orElseGet(() -> {
                newTeam.setId(id);
                return teamRepository.save(newTeam);
            });
    }

    @PutMapping("/api/teams/{id}/clue_on/{clue_id}")
    public TeamEntity updateTeamClue(@PathVariable("id") Integer id,
                                 @PathVariable("clue_id") Integer clue_id) {
        TeamEntity team = teamRepository.findById(id).orElseThrow(() ->
            new EntityNotFoundException("Team is not found " + id));

        if (team.getClue_on().getId() == clue_id)
            return team;

        ClueEntity clue = clueRepository.findById(clue_id).orElseThrow(() ->
            new EntityNotFoundException("Clue is not found " + id));
        team.setClue_on(clue);
        return teamRepository.save(team);
    }

    @PutMapping("/api/teams/{id}/skipClue")
    public TeamEntity skipTeamClue(@PathVariable("id") Integer id) {
        TeamEntity team = teamRepository.findById(id).orElseThrow(() ->
            new EntityNotFoundException("Team is not found " + id));

        ClueEntity clue = team.getClue_on();

        MainService service = new MainService();
        ClueEntity nextClue = service.nextClue(team.getQuest(), clue);

        // no next clue, updating end time quest.
        if (nextClue == null) {
            team.setEndTimeQuest(new Date());
            return teamRepository.save(team);
        }
        team.setClue_on(nextClue);
        return teamRepository.save(team);
    }
}
