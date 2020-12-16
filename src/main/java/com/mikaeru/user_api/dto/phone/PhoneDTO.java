package com.mikaeru.user_api.dto.phone;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.UUID;

/**
 * Classe que representa o objeto de transferência de dados de um Telefone.
 * @author Mickael Luiz
 */
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@AllArgsConstructor
public class PhoneDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    private UUID externalId;

    @NotBlank
    private String type;

    @NotBlank
    private String number;
}
