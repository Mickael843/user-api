package com.mikaeru.user_api.dto.request.phone;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Objects;

public class PhoneInput implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    @NotBlank
    private String type;

    @NotBlank
    private String number;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PhoneInput that = (PhoneInput) o;
        return Objects.equals(number, that.number);
    }

    @Override
    public int hashCode() {
        return Objects.hash(number);
    }
}
