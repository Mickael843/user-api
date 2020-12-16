package com.mikaeru.user_api.dto.user.out;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.mikaeru.user_api.dto.phone.PhoneDTO;
import com.mikaeru.user_api.dto.role.RoleDTO;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;
import java.util.UUID;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

/**
 * Classe que representa o objeto de transferência de dados de Saída de um Usuário.
 * @author Mickael Luiz
 */
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@AllArgsConstructor
@JsonInclude(NON_NULL)
public class UserOutput implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    private UUID externalId;

    @NotBlank
    private String firstname;

    @NotBlank
    private String lastName;

    @NotBlank
    private String username;

    @Email
    private String email;

    private List<PhoneDTO> phones;

    private List<RoleDTO> roles;
}
