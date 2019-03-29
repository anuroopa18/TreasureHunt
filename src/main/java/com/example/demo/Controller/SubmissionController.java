package com.example.demo.Controller;

import com.example.demo.Entity.*;
import com.example.demo.Repository.ClueRepository;
import com.example.demo.Repository.SubmissionRepository;
import com.example.demo.Repository.TeamRepository;
import com.example.demo.Repository.UserRepository;
import com.example.demo.Services.MainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@RestController
public class SubmissionController {

    @Autowired
    private SubmissionRepository submissionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ClueRepository clueRepository;

    @Autowired
    private TeamRepository teamRepository;

    @GetMapping("/api/submissions")
    public Iterable<SubmissionEntity> findAllSubmissions() {
        return submissionRepository.findAll();
    }

    @GetMapping("/api/submissions/{id}")
    public SubmissionEntity findSubmissionById(@PathVariable("id") int id) {
        return submissionRepository.findById(id).orElseThrow(() ->
            new EntityNotFoundException("Submission is not found" + id));
    }

    @GetMapping("/api/submissions/clue_id/{clueId}/team_id/{teamId}")
    public List<SubmissionEntity> findSubmissionByClueAndTeam(@PathVariable("clueId") int clueId,
                                                        @PathVariable("teamId") int teamId) {
        ClueEntity clue = clueRepository.findById(clueId).orElseThrow(() ->
            new EntityNotFoundException("Clue is not found" + clueId));
        Set<SubmissionEntity> submissions = clue.getSubmissions();
        List<SubmissionEntity> clueTeamSubmissions = new ArrayList();

        for (SubmissionEntity submission : submissions) {
            if (submission.getTeam().getId() == teamId)
                clueTeamSubmissions.add(submission);
        }

        return clueTeamSubmissions;
    }

    @PostMapping("/api/submissions/user/{user_id}/clue/1")
    public SubmissionEntity createSubmission(@RequestBody (required = false) SubmissionEntity submission,
                                             @PathVariable("user_id") Integer user_id,
                                             @PathVariable("clue_id") Integer clue_id) {
        UserEntity user = userRepository.findById(user_id).orElseThrow(() ->
            new EntityNotFoundException("User is not found " + user_id));
        ClueEntity clue = clueRepository.findById(clue_id).orElseThrow(() ->
            new EntityNotFoundException("Clue is not found " + clue_id));

        submission.setUser(user);
        submission.setClue(clue);

        List<TeamEntity> teams = user.getTeams();
        TeamEntity team = teams.get(teams.size() - 1);
        submission.setTeam(team);
        return submissionRepository.save(submission);
    }

    @DeleteMapping("/api/submissions/{id}")
    public void deleteSubmission(@PathVariable("id") Integer id) {
        submissionRepository.deleteById(id);
    }

    @PutMapping("/api/submissions/{id}")
    public SubmissionEntity updateSubmission(@RequestBody (required = false) SubmissionEntity newSubmission,
                                             @PathVariable("id") Integer id) {
        MainService service = new MainService();
        return submissionRepository.findById(id)
            .map(submission -> {
                TeamEntity team = submission.getTeam();
                ClueEntity clue = submission.getClue();
                // image status is different.
                if (newSubmission.getImageStatus() != submission.getImageStatus()) {
                    System.out.println("here ifjbjsbdkjfbkjbfdhjbehjkrbghjbhjr");
                    submission.setImageStatus(newSubmission.getImageStatus());
                    String status = submission.getImageStatus().toUpperCase();
                    if (submission.getImageStatus().equals(status)) {
                        if (team == null) throw new NullPointerException("Team is not found for this submission");
                        ClueEntity nextClue = service.nextClue(team.getQuest(), clue);
                        team.setClue_on(nextClue);
                        teamRepository.save(team);
                    }
                }
                if (submission.getReason() == null ||
                    (newSubmission.getReason() != null && !newSubmission.getReason().equals(submission.getReason())))
                    submission.setReason(newSubmission.getReason());
                if (submission.getImage() == null ||
                    (newSubmission.getImage() != null && !newSubmission.getImage().equals(submission.getImage())))
                    submission.setImage(newSubmission.getImage());
                return submissionRepository.save(submission);
            })
            .orElseGet(() -> {
                newSubmission.setId(id);
                return submissionRepository.save(newSubmission);
            });
    }
}
