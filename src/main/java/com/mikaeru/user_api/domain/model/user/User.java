package com.mikaeru.user_api.domain.model.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mikaeru.user_api.domain.model.phone.Phone;
import com.mikaeru.user_api.domain.model.role.Role;
import com.mikaeru.user_api.dto.user.out.UserOutput;
import lombok.*;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import static javax.persistence.GenerationType.IDENTITY;

@Getter
@Setter
@Entity
@NoArgsConstructor
@EqualsAndHashCode
@AllArgsConstructor
@Table(name = "user_entity")
public class User implements UserDetails {

    private static final long serialVersionUID = 1L;

    @Id
    @JsonIgnore
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private UUID externalId;

    @Column(nullable = false)
    private String firstname;

    private String lastName;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(name = "user_password", nullable = false)
    private String password;

    private String email;

    private OffsetDateTime createdAt;

    private OffsetDateTime updatedAt;

    @OneToMany(mappedBy = "owner")
    private List<Phone> phones;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_role", uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "role_id"}, name = "uk_user_role"),
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id", table = "user_entity",
                    foreignKey = @ForeignKey(name = "fk_user", value = ConstraintMode.CONSTRAINT)),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id", table = "role_entity",
                    updatable = false, foreignKey = @ForeignKey(name = "fk_role", value = ConstraintMode.CONSTRAINT)) )
    private List<Role> authorities;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public UserOutput convertToDTO() {
        return new ModelMapper().map(this, UserOutput.class);
    }
}