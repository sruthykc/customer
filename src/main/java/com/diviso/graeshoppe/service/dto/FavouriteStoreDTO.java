package com.diviso.graeshoppe.service.dto;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the FavouriteStore entity.
 */
public class FavouriteStoreDTO implements Serializable {

    private Long id;

    @NotNull
    private Long storeId;


    private Long customerId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getStoreId() {
        return storeId;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        FavouriteStoreDTO favouriteStoreDTO = (FavouriteStoreDTO) o;
        if (favouriteStoreDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), favouriteStoreDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "FavouriteStoreDTO{" +
            "id=" + getId() +
            ", storeId=" + getStoreId() +
            ", customer=" + getCustomerId() +
            "}";
    }
}
