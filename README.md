# Voting App

Voting App to prosty system do tworzenia wyborów i głosowania, stworzony w **Spring Boot 3**.  
Celem projektu było pokazanie dobrych praktyk w budowie REST API, pracy z JPA/Hibernate, testowania jednostkowego i
integracyjnego oraz użycia w pamięci bazy danych H2 dla testów i dewelopmentu.

---

## 🛠 Tech Stack

- **Java 17**
- **Spring Boot 3**
    - Spring Web
    - Spring Data JPA
- **H2** (in-memory database) dla testów i lokalnego rozwoju
- **JUnit 5 + Mockito** do testów jednostkowych
- **Maven** do budowania projektu
- **Lombok** do redukcji boilerplate w encjach i DTO

---

## 📦 Struktura projektu

```text
└── com
    └── mbr
        └── voting
            ├── VotingAppApplication.java
            ├── election
            │   ├── CreateElection.java
            │   ├── Election.java
            │   ├── ElectionController.java
            │   ├── ElectionOption.java
            │   ├── ElectionRepository.java
            │   └── dto
            │       └── CreateElectionCommand.java
            ├── exception
            │   ├── BusinessException.java
            │   └── GlobalExceptionHandler.java
            ├── vote
            │   ├── CastVote.java
            │   ├── Vote.java
            │   ├── VoteController.java
            │   ├── VoteRepository.java
            │   └── dto
            │       └── CastVoteCommand.java
            └── voter
                ├── ManageVoter.java
                ├── Voter.java
                ├── VoterController.java
                ├── VoterRepository.java
                └── dto
                    ├── ChangeVoterStatusCommand.java
                    └── CreateVoterCommand.java
```
---

## ⚙️ Konfiguracja środowiska

### Dev

- Baza w pamięci H2
- Hibernate automatycznie tworzy tabele (`ddl-auto: update`)
- URL: `jdbc:h2:mem:votingdb;DB_CLOSE_DELAY=-1;MODE=PostgreSQL`

### Test

- Baza H2 w pamięci (`jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;MODE=PostgreSQL`)
- Profile `test` (`@ActiveProfiles("test")`)
- Hibernate `ddl-auto: create-drop` – baza resetowana dla każdego testu

---

## 🔧 Uruchomienie projektu

1. Repozytorium:

```bash
git clone <repo-url>
cd voting-app
```

2. Budowanie projektu:

```bash
mvn clean install
```

3. Uruchomienie lokalne:

```bash
mvn spring-boot:run
```

4. Api:

```bash
POST /api/v1/elections
GET /api/v1/elections
```

5. Api request body:

 ```json
{
  "name": "Presidential election",
  "options": [
    "Option A",
    "Option B"
  ]
}

```