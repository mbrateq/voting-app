package com.mbr.voting;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mbr.voting.election.Election;
import com.mbr.voting.election.dto.CreateElectionCommand;
import com.mbr.voting.vote.dto.CastVoteCommand;
import com.mbr.voting.voter.Voter;
import com.mbr.voting.voter.dto.CreateVoterCommand;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Transactional
class VotingApplicationIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldCreateVoterElectionAndCastVote() throws Exception {
        var createVoterCommand =
                new CreateVoterCommand("John", "Smith");

        var voterResult = mockMvc.perform(post("/api/v1/voters")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createVoterCommand)))
                .andExpect(status().isCreated())
                .andReturn();

        var voter = objectMapper.readValue(
                voterResult.getResponse().getContentAsString(), Voter.class
        );


        var createElectionCommand =
                new CreateElectionCommand(
                        "Presidential election",
                        List.of("Option A", "Option B")
                );

        var electionResult = mockMvc.perform(post("/api/v1/elections")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createElectionCommand)))
                .andExpect(status().isCreated())
                .andReturn();

        var election = objectMapper.readValue(
                electionResult.getResponse().getContentAsString(), Election.class
        );

        var optionId = election.getElectionOptions().getFirst().getId();


        var castVoteCommand =
                new CastVoteCommand(
                        voter.getId(),
                        election.getId(),
                        optionId
                );

        mockMvc.perform(post("/api/v1/votes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(castVoteCommand)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists());
    }
}