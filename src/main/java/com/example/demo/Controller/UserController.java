package com.example.demo.Controller;

import com.example.demo.Entity.TeamEntity;
import com.example.demo.Entity.UserEntity;
import com.example.demo.Repository.TeamRepository;
import com.example.demo.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;

@RestController
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TeamRepository teamRepository;

    @GetMapping("/api/users")
    public Iterable<UserEntity> findAllUsers() {
        return userRepository.findAll();
    }

    @GetMapping("/api/users/{id}")
    public UserEntity findUserById(@PathVariable("id") int id) {
        return userRepository.findById(id).orElseThrow(() ->
            new EntityNotFoundException("User is not found" + id));
    }

    @PostMapping("/api/users")
    public UserEntity createUser(@RequestBody (required = false) UserEntity user) {
        return userRepository.save(user);
    }

    @DeleteMapping("/api/users/{id}")
    public void deleteUser(@PathVariable("id") Integer id) {
        userRepository.deleteById(id);
    }

    @PutMapping("/api/users/{id}")
    public UserEntity updateUser(@RequestBody (required = false) UserEntity newUser,
                                 @PathVariable("id") Integer id) {
        return userRepository.findById(id)
            .map(user -> {
                if (newUser.getUsername() != user.getUsername())
                    user.setUsername(newUser.getUsername());
                if (newUser.getFirstName() != user.getFirstName())
                    user.setFirstName(newUser.getFirstName());
                if (newUser.getLastName() != user.getLastName())
                    user.setLastName(newUser.getLastName());
                if (newUser.getPassword() != user.getPassword())
                    user.setPassword(newUser.getPassword());
                return userRepository.save(user);
            })
            .orElseGet(() -> {
                newUser.setId(id);
                return userRepository.save(newUser);
            });
    }

    @PutMapping("/api/users/{user_id}/team/{team_id}")
    public UserEntity addUserToTeam (@PathVariable("user_id") Integer user_id,
                                     @PathVariable("team_id") Integer team_id) {
        TeamEntity team = teamRepository.findById(team_id).orElseThrow(() ->
            new EntityNotFoundException("Team is not found " + team_id));

        // checking number of players in quest and in this team.
        if (team.getQuest().getNoOfPlayers() <= team.getUsers().size())
            throw new Error("Team is full");
        UserEntity user = userRepository.findById(user_id).orElseThrow(() ->
            new EntityNotFoundException("User is not found " + user_id));
        team.addUser(user);
        user.getTeams().add(team);

        return userRepository.save(user);
    }
}
