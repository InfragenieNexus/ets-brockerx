package com.log430.brockerx.dto;

import com.log430.brockerx.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRequestDto {

    // Obligatoire
    private String email;
    private String password;

    // Optionnels
    private Optional<String> phone = Optional.empty();
    private Optional<String> firstName = Optional.empty();
    private Optional<String> lastName = Optional.empty();
    private Optional<String> address = Optional.empty();
    private Optional<LocalDate> dateOfBirth = Optional.empty();
    private Optional<User.Status> status = Optional.empty();
    private Optional<String> totpSecret = Optional.empty();
    private Optional<String> kycDocumentHash = Optional.empty();
    private Optional<Boolean> mfaEnabled = Optional.empty();
    private Optional<LocalDateTime> createdAt = Optional.empty();
}
