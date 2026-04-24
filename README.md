# 🏆 Quiz Leaderboard System — SRM Internship Assignment

A Spring Boot application that polls a quiz API, deduplicates event data, aggregates participant scores, and submits a final leaderboard.

---

## 📸 Output
<img width="1007" height="176" alt="image" src="https://github.com/user-attachments/assets/09fa0be1-0c02-4b9e-bc85-d3ccc0bcac30" />


---

## 🧩 Problem Statement

Build a backend application that:
- Polls a validator API **10 times** to collect quiz round scores
- Handles **duplicate API response data** correctly
- Aggregates scores per participant
- Submits a **sorted leaderboard** once

---

## ⚙️ Tech Stack

- **Java 21**
- **Spring Boot 4.0.6**
- **Lombok** — for boilerplate-free model classes
- **RestTemplate** — for HTTP API calls

---

## 🔄 Approach

### 1. Poll 10 Times
The API is polled with `poll` values from `0` to `9`, with a **mandatory 5-second delay** between each request.

### 2. Deduplicate Events
Each event is uniquely identified by a composite key:
```
key = roundId + "|" + participant
```
`putIfAbsent()` ensures that if the same `roundId + participant` combination appears again in a later poll, it is **silently ignored**.

```
Poll 0 → R1|Alice = 10  → Added ✅
Poll 3 → R1|Alice = 10  → Already exists, ignored ✅
```

### 3. Aggregate Scores
After deduplication, scores are summed per participant using `Map.merge()`.

### 4. Sort & Submit
The leaderboard is sorted by `totalScore` in **descending order** and submitted once via `POST /quiz/submit`.

---

## 📊 Result

| Rank | Participant | Total Score |
|------|-------------|-------------|
| 🥇 1 | Diana       | 470         |
| 🥈 2 | Ethan       | 455         |
| 🥉 3 | Fiona       | 440         |

**Grand Total: 1365**

---

## 🚀 How to Run

### Prerequisites
- Java 21+
- Maven 3.8+

### Steps

```bash
# Clone the repository
git clone https://github.com/your-username/your-repo.git
cd your-repo

# Run the application
mvn spring-boot:run
```

The application will:
1. Poll the API 10 times (~45 seconds total)
2. Print the leaderboard to console
3. Submit and print the final result

---

## 📁 Project Structure

```
src/
└── main/
    └── java/
        └── com/
            └── bajajfinserv/
                └── sabir/
                    └── assignment/
                        ├── leaderboard.java      ← Main app + model classes
                        └── SubmitRequest.java    ← Submit request model
pom.xml
README.md
```

---


## 👤 Author

**Sabir** — SRM Institute of Science and Technology  
Registration No: `RA2311003010484`
