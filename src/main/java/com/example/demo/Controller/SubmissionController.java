package com.example.demo.Controller;

import com.example.demo.Entity.*;
import com.example.demo.Repository.ClueRepository;
import com.example.demo.Repository.SubmissionRepository;
import com.example.demo.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@RestController
public class SubmissionController {

    @Autowired
    private SubmissionRepository submissionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ClueRepository clueRepository;

    @GetMapping("/api/submissions")
    public Iterable<SubmissionEntity> findAllSubmissions() {
        return submissionRepository.findAll();
    }

    @GetMapping("/api/submissions/{id}")
    public SubmissionEntity findSubmissionById(@PathVariable("id") int id) {
        return submissionRepository.findById(id).orElseThrow(() ->
            new EntityNotFoundException("Submission is not found" + id));
    }

    @PostMapping("/api/submissions/user/{user_id}/clue/{clue_id}")
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
        return submissionRepository.findById(id)
            .map(submission -> {
                if (newSubmission.getImageStatus() != submission.getImageStatus())
                    submission.setImageStatus(newSubmission.getImageStatus());
                if (newSubmission.getReason() != submission.getReason())
                    submission.setReason(newSubmission.getReason());
                if (!newSubmission.getImage().equals(submission.getImage()))
                    submission.setImage(newSubmission.getImage());
                return submissionRepository.save(submission);
            })
            .orElseGet(() -> {
                newSubmission.setId(id);
                return submissionRepository.save(newSubmission);
            });
    }
}
