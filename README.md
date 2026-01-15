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

.
└── com
└── mbr
└── voting
├── VotingAppApplication.java
├── election
├── CreateElection.java
├── Election.java
├── ElectionController.java
├── ElectionOption.java
├── ElectionRepository.java
└── dto
└── CreateElectionCommand.java
├── exception
├── BusinessException.java
└── GlobalExceptionHandler.java
├── vote
├── CastVote.java
├── Vote.java
├── VoteController.java
├── VoteRepository.java
└── dto
└── CastVoteCommand.java
└── voter
├── ManageVoter.java
├── Voter.java
├── VoterController.java
├── VoterRepository.java
└── dto
├── ChangeVoterStatusCommand.java
└── CreateVoterCommand.java

- `controller/` – warstwa REST API
- `service/` – logika biznesowa
- `repository/` – warstwa dostępu do danych
- `model/` – encje JPA
- `dto/` – obiekty transferowe, używane w API

---

## 🧪 Test Strategy

Projekt posiada **kompletną strategię testowania**, zgodną z podejściem **unit → slice → integration**.

### 1. Unit tests

- Testy logiki biznesowej w `service/`
- Użycie **Mockito** do mockowania repozytoriów
- Pokrywają najważniejsze reguły biznesowe, np.:
    - nie można utworzyć wyborów bez opcji
    - jeden użytkownik może oddać tylko jeden głos

### 2. Slice tests (WebMvcTest)

- Testy kontrolerów REST API
- Sprawdzają:
    - poprawność endpointów (`200 / 201 / 400 / 404`)
    - mapowanie JSON ⇄ DTO
    - walidację danych wejściowych (`@Valid`)

### 3. Integration tests (`@SpringBootTest`)

- Testy integracyjne pełnej warstwy JPA + Spring Boot
- Użycie **H2 in-memory** jako bazy testowej (`create-drop`)
- Testy CRUD dla encji Election + ElectionOption
- Profile `test` używane poprzez `@ActiveProfiles("test")`
- Dzięki H2 w trybie PostgreSQL możliwe jest użycie `@SequenceGenerator` identycznie jak w Postgresie produkcyjnie

### 4. Test Coverage & Best Practices

- Testy koncentrują się na **decyzjach biznesowych**, nie na getterach/setterach
- Testy izolują logikę od frameworka tam, gdzie to możliwe (unit)
- Testy integracyjne zapewniają, że **REST API, JPA i baza działają razem**

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