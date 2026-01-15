package com.mbr.voting.voter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mbr.voting.exception.GlobalExceptionHandler;
import com.mbr.voting.voter.dto.ChangeVoterStatusCommand;
import com.mbr.voting.voter.dto.CreateVoterCommand;
import jakarta.validation.ValidationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(VoterController.class)
@Import(GlobalExceptionHandler.class)
class VoterControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ManageVoter manageVoter;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldCreateVoter() throws Exception {
        // given
        var givenCreateVoterCommand = new CreateVoterCommand("John", "Smith");

        var voter = new Voter();
        voter.setId(1L);
        voter.setFirstName("John");
        voter.setLastName("Smith");
        voter.setBlocked(false);

        when(manageVoter.create(any(CreateVoterCommand.class)))
                .thenReturn(voter);

        // when then
        mockMvc.perform(post("/api/v1/voters")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(givenCreateVoterCommand)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Smith"))
                .andExpect(jsonPath("$.blocked").value(false));
    }

    @Test
    void shouldChangeVoterStatus() throws Exception {
        // given
        var changeVoterStatusCommand = new ChangeVoterStatusCommand(false);
        Voter voter = new Voter();
        voter.setId(1L);
        voter.setFirstName("John");
        voter.setLastName("Smith");
        voter.setBlocked(true);

        when(manageVoter.changeStatus(eq(1L), any(ChangeVoterStatusCommand.class)))
                .thenReturn(voter);

        // when & then
        mockMvc.perform(patch("/api/v1/voters/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(changeVoterStatusCommand)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.blocked").value(true));
    }

    @Test
    void shouldReturn400WhenVoterNotFound() throws Exception {
        // given
        var command = new ChangeVoterStatusCommand(true);

        when(manageVoter.changeStatus(eq(99L), any(ChangeVoterStatusCommand.class)))
                .thenThrow(new ValidationException("Voter not found"));

        // when & then
        mockMvc.perform(patch("/api/v1/voters/{id}", 99L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").value("Voter not found"));
    }
}
