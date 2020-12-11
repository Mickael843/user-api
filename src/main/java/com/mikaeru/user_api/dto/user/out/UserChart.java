package com.mikaeru.user_api.dto.user.out;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@AllArgsConstructor
public class UserChart implements Serializable {

    private static final long serialVersionUID = 1L;

    private String name;
    private String salary;
}
