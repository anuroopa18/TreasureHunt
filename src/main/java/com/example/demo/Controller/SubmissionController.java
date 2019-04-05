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

    @PostMapping("/api/submissions/team/{team_id}/clue/{clue_id}")
    public SubmissionEntity createSubmission(@RequestBody (required = false) SubmissionEntity submission,
                                             @PathVariable("team_id") Integer team_id,
                                             @PathVariable("clue_id") Integer clue_id) {
        TeamEntity team = teamRepository.findById(team_id).orElseThrow(() ->
            new EntityNotFoundException("Team is not found " + team_id));
        ClueEntity clue = clueRepository.findById(clue_id).orElseThrow(() ->
            new EntityNotFoundException("Clue is not found " + clue_id));

        submission.setClue(clue);
        submission.setTeam(team);
        return submissionRepository.save(submission);
    }

    @DeleteMapping("/api/submissions/{id}")
    public void deleteSubmission(@PathVariable("id") Integer id) {
        submissionRepository.deleteById(id);
    }

    @PutMapping("/api/submissions/players/{id}")
    public SubmissionEntity updateSubmission(@RequestBody (required = false) SubmissionEntity newSubmission,
                                             @PathVariable("id") Integer id) {
        return submissionRepository.findById(id)
            .map(submission -> {
                // only update image.
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

    @PutMapping("/api/submissions/organizer/{id}")
    public SubmissionEntity updateSubmissionsOrganizer(
        @RequestBody (required = false) SubmissionEntity newSubmission,
        @PathVariable("id") Integer id) {
        MainService service = new MainService();
        return submissionRepository.findById(id)
            .map(submission -> {
                TeamEntity team = submission.getTeam();
                ClueEntity clue = submission.getClue();

                // image status is different. ("ACCEPTED OR REJECTED").
                if (newSubmission.getImageStatus() != submission.getImageStatus()) {
                    String status = newSubmission.getImageStatus().toUpperCase();
                    submission.setImageStatus(status);

                    // team move to next clue.
                    if (status.equals("ACCEPTED")) {
                        if (team == null) throw new NullPointerException("Team is not found for this submission");
                        ClueEntity nextClue = service.nextClue(team.getQuest(), clue);
                        team.setClue_on(nextClue);
                        teamRepository.save(team);
                    }
                }

                // changing reason.
                if (submission.getReason() == null ||
                    (newSubmission.getReason() != null && !newSubmission.getReason().equals(submission.getReason())))
                    submission.setReason(newSubmission.getReason());

                return submissionRepository.save(submission);
            })
            .orElseThrow(() ->
                new EntityNotFoundException("Submission is not found " + id));
    }
}
