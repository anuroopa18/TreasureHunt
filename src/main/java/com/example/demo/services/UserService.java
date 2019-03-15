package com.example.demo.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.models.Quest;
import com.example.demo.models.User;

@RestController
public class UserService {
	
	@GetMapping(value="/api/users")
	public List<User> findAllUsers() {
		List<User> users = new ArrayList<User>();
		users.add(new User("Batman","Robin","Bruce","Wayne"));
		return users;
	}
	
	@PostMapping("api/questDetails")
	public Quest createQuest(@RequestBody Quest quest) {
		
		return quest;
	}
	
	@PostMapping("api/upload")
	public int upload() {
		return 0;
	}

}
