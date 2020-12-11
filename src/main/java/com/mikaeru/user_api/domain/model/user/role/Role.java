package com.mikaeru.user_api.domain.model.user.role;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
@EqualsAndHashCode
@AllArgsConstructor
@Table(name = "role_entity")
public class Role implements GrantedAuthority {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String authority;

    @Override
    public String getAuthority() {
        return authority;
    }
}
