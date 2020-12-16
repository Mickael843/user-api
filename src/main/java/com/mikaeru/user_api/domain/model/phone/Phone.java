package com.mikaeru.user_api.domain.model.phone;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mikaeru.user_api.domain.model.user.User;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.UUID;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.AUTO;

/**
 * Classe que representa a entidade Phone
 * @author Mickael Luiz
 */
@Getter
@Setter
@Entity
@NoArgsConstructor
@EqualsAndHashCode
@AllArgsConstructor
public class Phone implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = AUTO)
    private Long id;

    @Column(unique = true, nullable = false)
    private UUID externalId;

    @JsonIgnore
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User owner;

    @Column(name = "phone_type", nullable = false)
    private String type;

    @Column(name = "phone_number", nullable = false, unique = true)
    private String number;
}
