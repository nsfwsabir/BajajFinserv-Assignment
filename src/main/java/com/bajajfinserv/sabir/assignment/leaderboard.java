package com.bajajfinserv.sabir.assignment;

import java.util.HashMap;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

@SpringBootApplication
public class leaderboard implements CommandLineRunner{

	public static void main(String[] args) {
		SpringApplication.run(leaderboard.class, args);
	}

	@Override
    public void run(String... args) throws Exception {
        String regNo = "RA2311003010484";
        RestTemplate restTemplate = new RestTemplate();
        
        Map<String, Integer> uniqueEvents = new HashMap<>();

        for (int poll = 0; poll <= 9; poll++) {
            String url = "https://devapigw.vidalhealthtpa.com/srm-quiz-task/quiz/messages"
                       + "?regNo=" + regNo + "&poll=" + poll;
            
            QuizResponse response = restTemplate.getForObject(url, QuizResponse.class);
            
            for (Event event : response.getEvents()) {
                String key = event.getRoundId() + "|" + event.getParticipant();
                uniqueEvents.putIfAbsent(key, event.getScore());
            }
            
            if (poll < 9) Thread.sleep(5000); 
        }

        Map<String, Integer> scores = new HashMap<>();
        for (Map.Entry<String, Integer> entry : uniqueEvents.entrySet()) {
            String participant = entry.getKey().split("\\|")[1];
            scores.merge(participant, entry.getValue(), Integer::sum);
        }

        List<LeaderboardEntry> leaderboard = scores.entrySet().stream()
            .map(e -> new LeaderboardEntry(e.getKey(), e.getValue()))
            .sorted((a, b) -> b.getTotalScore() - a.getTotalScore())
            .collect(Collectors.toList());

        System.out.println("\n=== LEADERBOARD ===");
        for (LeaderboardEntry entry : leaderboard) {
            System.out.println(entry.getParticipant() + " → " + entry.getTotalScore());
        }
        int total = leaderboard.stream().mapToInt(LeaderboardEntry::getTotalScore).sum();
        System.out.println("Total score: " + total);

        HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		SubmitRequest submitRequest = new SubmitRequest(regNo, leaderboard);
		HttpEntity<SubmitRequest> request = new HttpEntity<>(submitRequest, headers);

		ResponseEntity<String> result = restTemplate.postForEntity(
				"https://devapigw.vidalhealthtpa.com/srm-quiz-task/quiz/submit",
				request,
				String.class
		);

		System.out.println("\n=== SUBMISSION RESULT ===");
		System.out.println(result.getBody());
		}
	}