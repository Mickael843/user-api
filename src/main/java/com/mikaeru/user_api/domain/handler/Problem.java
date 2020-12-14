package com.mikaeru.user_api.domain.handler;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Objects;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Problem {

    private String title;

    private Integer status;

    private List<Field> fields;

    private OffsetDateTime dateTime;

    public static class Field {
        private String name;
        private String message;

        public Field(String name, String msg) {
            this.name = name;
            this.message = msg;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public List<Field> getFields() {
        return fields;
    }

    public void setFields(List<Field> fields) {
        this.fields = fields;
    }

    public OffsetDateTime getDateTime() {
        return dateTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Problem problem = (Problem) o;
        return Objects.equals(title, problem.title) &&
                Objects.equals(status, problem.status) &&
                Objects.equals(fields, problem.fields) &&
                Objects.equals(dateTime, problem.dateTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, status, fields, dateTime);
    }

    public void setDateTime(OffsetDateTime dateTime) {
        this.dateTime = dateTime;
    }
}
