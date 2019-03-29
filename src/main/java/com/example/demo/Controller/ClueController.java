package com.example.demo.Controller;

import com.example.demo.Entity.ClueEntity;
import com.example.demo.Entity.HintEntity;
import com.example.demo.Entity.QuestEntity;
import com.example.demo.Entity.TeamEntity;
import com.example.demo.Repository.ClueRepository;
import com.example.demo.Repository.HintRepository;
import com.example.demo.Repository.QuestRepository;
import com.example.demo.Repository.TeamRepository;
import com.example.demo.Services.MainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.util.Set;

@RestController
public class ClueController {
    @Autowired
    private ClueRepository clueRepository;

    @Autowired
    private QuestRepository questRepository;

    @Autowired
    private HintRepository hintRepository;

    @Autowired
    private TeamRepository teamRepository;

    @GetMapping("/api/clues")
    public Iterable<ClueEntity> findAllHints() {
        return clueRepository.findAll();
    }

    @GetMapping("/api/clues/{id}")
    public ClueEntity findCluesById(@PathVariable("id") int id) {
        return clueRepository.findById(id).orElseThrow(() ->
            new EntityNotFoundException("Clue is not found" + id));
    }

    @PostMapping("/api/clues/questId/{quest_id}/hintId/{hint_id}")
    public ClueEntity createClues(@RequestBody (required = false) ClueEntity clue,
                                  @PathVariable("quest_id") Integer quest_id,
                                  @PathVariable("hint_id") Integer hint_id) {
        QuestEntity quest = questRepository.findById(quest_id).orElseThrow(() ->
            new EntityNotFoundException("Quest is not found" + quest_id));

        HintEntity hint = hintRepository.findById(hint_id).orElse(null);

        clue.setHint(hint);
        clue.setQuest(quest);
        quest.getClues().add(clue);

        clue = clueRepository.save(clue);

        //CREATING TEAMS: -------> move this in future.
        if (quest.getNoOfTeams() > 0 && quest.getTeams().size() == 0) {
            ClueEntity firstClue = clue;
            MainService service = new MainService();
            Set<TeamEntity> teams = service.createTeams(quest);
            for(TeamEntity team: teams) {
                team.setClue_on(firstClue);
                team.setQuest(quest);
                teamRepository.save(team);
            }
        }

        return clue;
    }

    @DeleteMapping("/api/clues/{id}")
    public void deleteClues(@PathVariable("id") Integer id) {
        clueRepository.deleteById(id);
    }

    @PutMapping("/api/clues/{id}")
    public ClueEntity updateClues(@RequestBody (required = false) ClueEntity newClue, @PathVariable("id") Integer id) {
        return clueRepository.findById(id)
            .map(clue -> {
                if (newClue.getPuzzle() != clue.getPuzzle())
                    clue.setPuzzle(newClue.getPuzzle());
                if (newClue.getSolution() != clue.getSolution())
                    clue.setSolution(newClue.getSolution());
                if (newClue.getPoints() != clue.getPoints())
                    clue.setPoints(newClue.getPoints());
                return clueRepository.save(clue);
            })
            .orElseGet(() -> {
                newClue.setId(id);
                return clueRepository.save(newClue);
            });
    }
}
