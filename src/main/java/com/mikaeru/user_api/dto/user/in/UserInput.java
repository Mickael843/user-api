package com.mikaeru.user_api.dto.user.in;

import com.mikaeru.user_api.domain.model.phone.Phone;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@AllArgsConstructor
public class UserInput implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    private UUID externalId;

    @NotBlank
    private String firstname;

    @NotBlank
    private String lastName;

    @NotBlank
    private String username;

    @NotBlank
    private String password;

    @Email
    private String email;

    private List<Phone> phones;
}
