package com.mikaeru.user_api.dto.user.in;

import lombok.*;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@AllArgsConstructor
public class ReportParam implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotBlank private String startDate;

    @NotBlank private String endDate;
}
