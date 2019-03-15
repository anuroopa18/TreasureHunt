package com.example.demo.models;

public class Quest {
	
	private String name;
	private int numberOfPlayers;
	private int timeLimit;
	
	public Quest(String name, int numberOfPlayers, int timeLimit) {
		this.name = name;
		this.numberOfPlayers = numberOfPlayers;
		this.timeLimit = timeLimit;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getNumberOfPlayers() {
		return numberOfPlayers;
	}

	public void setNumberOfPlayers(int numberOfPlayers) {
		this.numberOfPlayers = numberOfPlayers;
	}

	public int getTimeLimit() {
		return timeLimit;
	}

	public void setTimeLimit(int timeLimit) {
		this.timeLimit = timeLimit;
	}

}
