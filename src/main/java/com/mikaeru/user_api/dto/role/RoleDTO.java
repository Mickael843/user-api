package com.mikaeru.user_api.dto.role;

import lombok.*;

import javax.validation.constraints.NotBlank;

/**
 * Classe que representa o objeto de transferência de dados da classe Role.
 * @author Mickael Luiz
 */
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@AllArgsConstructor
public class RoleDTO {

    @NotBlank
    private String authority;
}
