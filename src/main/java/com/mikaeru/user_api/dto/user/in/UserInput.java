package com.mikaeru.user_api.dto.user.in;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mikaeru.user_api.domain.model.user.User;
import com.mikaeru.user_api.domain.validation.user.UserValidationGroup.CreateValidation;
import com.mikaeru.user_api.domain.validation.user.UserValidationGroup.RecoveryValidation;
import com.mikaeru.user_api.domain.validation.user.UserValidationGroup.UpdateValidation;
import com.mikaeru.user_api.dto.phone.PhoneDTO;
import com.mikaeru.user_api.dto.role.RoleDTO;
import lombok.*;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;
import java.util.UUID;

/**
 * Classe que representa o objeto de transferência de dados de Entrada de um Usuário.
 * @author Mickael Luiz
 */
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@AllArgsConstructor
public class UserInput implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull(groups = {CreateValidation.class, UpdateValidation.class})
    private UUID externalId;

    @NotBlank(groups = {CreateValidation.class, UpdateValidation.class})
    private String firstname;

    @NotBlank(groups = {CreateValidation.class, UpdateValidation.class})
    private String lastname;

    @NotBlank(groups = {CreateValidation.class, RecoveryValidation.class})
    private String username;

    @NotBlank(groups = {CreateValidation.class})
    private String password;

    @Email
    @NotBlank(groups = {CreateValidation.class})
    private String email;

    private List<PhoneDTO> phones;

    private List<RoleDTO> authorities;

    public User convertToEntity() {
        ModelMapper mapper = new ModelMapper();
        mapper.addMappings(skipIdFieldsMap);
        return mapper.map(this, User.class);
    }

    @JsonIgnore
    PropertyMap<UserInput, User> skipIdFieldsMap = new PropertyMap<>() {
        @Override
        protected void configure() {
            skip().setId(null);
        }
    };
}
