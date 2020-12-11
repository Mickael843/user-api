package com.mikaeru.user_api.domain.model.user.profession;

import com.mikaeru.user_api.domain.model.user.User;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
public class Profession implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String description;

//    @OneToMany(mappedBy = "profession")
//    private List<User> users;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
