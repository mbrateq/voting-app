package com.mbr.voting.voter.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateVoterCommand(@NotBlank String firstName, @NotBlank String lastName) {
}
