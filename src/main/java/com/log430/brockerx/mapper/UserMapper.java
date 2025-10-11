package com.log430.brockerx.mapper;

import com.log430.brockerx.dto.UserRequestDto;
import com.log430.brockerx.dto.UserResponseDto;
import com.log430.brockerx.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface UserMapper {

    // ================== Entity → Response DTO ==================
    UserResponseDto toDto(User user);

    // ================== Response DTO → Entity ==================
    User toEntity(UserResponseDto dto);

    // ================== Request DTO → Entity ==================
    @Mapping(target = "firstName", source = "firstName", qualifiedByName = "optionalString")
    @Mapping(target = "lastName", source = "lastName", qualifiedByName = "optionalString")
    @Mapping(target = "phone", source = "phone", qualifiedByName = "optionalString")
    @Mapping(target = "address", source = "address", qualifiedByName = "optionalString")
    @Mapping(target = "dateOfBirth", source = "dateOfBirth", qualifiedByName = "optionalValue")
    @Mapping(target = "status", source = "status", qualifiedByName = "optionalValue")
    @Mapping(target = "totpSecret", source = "totpSecret", qualifiedByName = "optionalString")
    @Mapping(target = "kycDocumentHash", source = "kycDocumentHash", qualifiedByName = "optionalString")
    @Mapping(target = "mfaEnabled", source = "mfaEnabled", qualifiedByName = "optionalBoolean")
    @Mapping(target = "createdAt", source = "createdAt", qualifiedByName = "optionalValue") User toEntity(
            UserRequestDto dto);

    // ================== Helpers pour Optional ==================
    @Named("optionalString") default String mapOptionalString(java.util.Optional<String> value) {
        return value.orElse(null);
    }

    @Named("optionalBoolean") default Boolean mapOptionalBoolean(java.util.Optional<Boolean> value) {
        return value.orElse(null);
    }

    @Named("optionalValue") default <T> T mapOptionalValue(java.util.Optional<T> value) {
        return value.orElse(null);
    }
}
