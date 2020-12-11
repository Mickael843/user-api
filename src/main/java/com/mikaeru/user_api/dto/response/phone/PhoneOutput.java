package com.mikaeru.user_api.dto.response.phone;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Objects;

public class PhoneOutput implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotBlank
    private String type;

    @NotBlank
    private String number;

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
        PhoneOutput that = (PhoneOutput) o;
        return Objects.equals(number, that.number);
    }

    @Override
    public int hashCode() {
        return Objects.hash(number);
    }
}
