package com.ltms.backend.usuario;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UpdatePasswordDTO {
    @NotEmpty(message = "El actual password es obligatorio")
    @NotBlank(message = "El actual password es obligatorio")
    private String currentPassword;
    @NotEmpty(message = "El nuevo password es obligatorio")
    @NotBlank(message = "El nuevo password es obligatorio")
    private String newPassword;
//    @NotEmpty(message = "El confirPassword es obligatorio")
//    @NotBlank(message = "El confirmPassword es obligatorio")
//    private String confirmNewPassword;
}
