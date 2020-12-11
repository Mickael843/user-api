package com.mikaeru.user_api.domain.model.phone;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mikaeru.user_api.domain.model.user.User;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@Entity
@NoArgsConstructor
@EqualsAndHashCode
@AllArgsConstructor
public class Phone implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User owner;

    @Column(name = "phone_type", nullable = false)
    private String type;

    @Column(name = "phone_number", nullable = false, unique = true)
    private String number;
}
