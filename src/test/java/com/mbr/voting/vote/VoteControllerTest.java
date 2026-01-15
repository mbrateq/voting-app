package com.mbr.voting.vote;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mbr.voting.exception.BusinessException;
import com.mbr.voting.exception.GlobalExceptionHandler;
import com.mbr.voting.vote.dto.CastVoteCommand;
import jakarta.validation.ValidationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(VoteController.class)
@Import(GlobalExceptionHandler.class)
class VoteControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CastVote castVote;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldCastVoteSuccessfully() throws Exception {
        // given
        var castVoteCommand = new CastVoteCommand(1L, 10L, 100L);

        Vote vote = new Vote();
        vote.setId(1L);

        when(castVote.execute(any(CastVoteCommand.class)))
                .thenReturn(vote);

        // when & then
        mockMvc.perform(post("/api/v1/votes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(castVoteCommand)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void shouldReturn403WhenVoterIsBlocked() throws Exception {
        // given
        CastVoteCommand command = new CastVoteCommand(1L, 10L, 100L);

        when(castVote.execute(any(CastVoteCommand.class)))
                .thenThrow(new BusinessException("Voter is blocked"));

        // when & then
        mockMvc.perform(post("/api/v1/votes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$").value("Voter is blocked"));
    }

    @Test
    void shouldReturn403WhenVoterAlreadyVoted() throws Exception {
        // given
        CastVoteCommand command = new CastVoteCommand(1L, 10L, 100L);

        when(castVote.execute(any(CastVoteCommand.class)))
                .thenThrow(new BusinessException("Voter already voted in this election"));

        // when & then
        mockMvc.perform(post("/api/v1/votes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$")
                        .value("Voter already voted in this election"));
    }

    @Test
    void shouldReturn400WhenElectionNotFound() throws Exception {
        // given
        CastVoteCommand command = new CastVoteCommand(1L, 10L, 100L);

        when(castVote.execute(any(CastVoteCommand.class)))
                .thenThrow(new ValidationException("Election not found"));

        // when & then
        mockMvc.perform(post("/api/v1/votes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").value("Election not found"));
    }
}

