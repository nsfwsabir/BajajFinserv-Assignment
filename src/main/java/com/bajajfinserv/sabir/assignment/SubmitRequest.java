package com.bajajfinserv.sabir.assignment;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
@Data
class Event {
    private String roundId;
    private String participant;
    private int score;
}

@Data
class QuizResponse {
    private String regNo;
    private String setId;
    private int pollIndex;
    private List<Event> events;
}

@Data
@AllArgsConstructor
class LeaderboardEntry {
    private String participant;
    private int totalScore;
}

@Data
@AllArgsConstructor
class SubmitRequest {
    private String regNo;
    private List<LeaderboardEntry> leaderboard;
}