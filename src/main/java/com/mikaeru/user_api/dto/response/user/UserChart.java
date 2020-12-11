package com.mikaeru.user_api.dto.response.user;

import java.io.Serializable;
import java.util.Objects;

public class UserChart implements Serializable {

    private static final long serialVersionUID = 1L;

    private String name;
    private String salary;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserChart userChart = (UserChart) o;
        return Objects.equals(name, userChart.name) &&
                Objects.equals(salary, userChart.salary);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, salary);
    }
}
