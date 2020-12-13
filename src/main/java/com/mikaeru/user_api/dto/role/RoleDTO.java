package com.mikaeru.user_api.dto.role;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@AllArgsConstructor
public class RoleDTO {

    @NotBlank
    private String authority;
}
