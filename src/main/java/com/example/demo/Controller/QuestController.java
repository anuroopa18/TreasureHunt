package com.example.demo.Controller;

import com.example.demo.Entity.QuestEntity;
import com.example.demo.Repository.QuestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.util.Date;

@RestController
public class QuestController {

    @Autowired
    private QuestRepository questRepository;


    @GetMapping("/api/quests")
    public Iterable<QuestEntity> findAllQuests() {
        return questRepository.findAll();
    }

    @GetMapping("/api/quests/{id}")
    public QuestEntity findQuestById(@PathVariable("id") int id) {
        return questRepository.findById(id).orElseThrow(() ->
			new EntityNotFoundException("Quest is not found" + id));
    }

    @PostMapping("/api/quests")
    public QuestEntity createQuest(@RequestBody (required = false) QuestEntity quest) {
        if (quest.isStarted())
            this.startQuest(quest);
        if (quest.getNoOfTeams() > 26)
            throw new Error("Number od teams cannot be greater than 26");
        return questRepository.save(quest);
    }

    @DeleteMapping("/api/quests/{id}")
    public void deleteQuest(@PathVariable("id") Integer id) {
        questRepository.deleteById(id);
    }

    @PutMapping("/api/quests/{id}")
    public QuestEntity updateQuest(@RequestBody (required = false) QuestEntity newQuest,
                                   @PathVariable("id") Integer id) {
        return questRepository.findById(id)
            .map(quest -> {
                if (newQuest.getName() != null)
                    quest.setName(newQuest.getName());
                if (newQuest.getNoOfPlayers() != 0)
                    quest.setNoOfPlayers(newQuest.getNoOfPlayers());
                if (!quest.isStarted() && newQuest.isStarted())
                    this.startQuest(quest);
                if (newQuest.getClues() != null)
                    quest.setClues(newQuest.getClues());
                if (newQuest.getTimeLimit() != 0)
                    quest.setTimeLimit(newQuest.getTimeLimit());
                System.out.println(newQuest.getNoOfTeams());
                if (newQuest.getNoOfTeams() != quest.getNoOfTeams())
                    quest.setNoOfTeams(newQuest.getNoOfTeams());
                return questRepository.save(quest);
            })
            .orElseGet(() -> {
                newQuest.setId(id);
                return questRepository.save(newQuest);
            });
    }

    public String getAlphaNumericString() {
        int n = 5;
        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
            + "0123456789"
            + "abcdefghijklmnopqrstuvxyz";

        StringBuilder sb = new StringBuilder(n);
        for (int i = 0; i < n; i++) {
            int index
                = (int)(AlphaNumericString.length()
                * Math.random());

            sb.append(AlphaNumericString
                .charAt(index));
        }

        return sb.toString();
    }

    public void startQuest(QuestEntity quest) {
        quest.setStarted(true);
        quest.setStartTime(new Date());
        quest.setCode(this.getAlphaNumericString());
    }
}
