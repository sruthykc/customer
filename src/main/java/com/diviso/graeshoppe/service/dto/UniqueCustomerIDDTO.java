package com.diviso.graeshoppe.service.dto;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the UniqueCustomerID entity.
 */
public class UniqueCustomerIDDTO implements Serializable {

    private Long id;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        UniqueCustomerIDDTO uniqueCustomerIDDTO = (UniqueCustomerIDDTO) o;
        if (uniqueCustomerIDDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), uniqueCustomerIDDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "UniqueCustomerIDDTO{" +
            "id=" + getId() +
            "}";
    }
}
