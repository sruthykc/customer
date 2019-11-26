package com.diviso.graeshoppe.service.dto;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the FavouriteProduct entity.
 */
public class FavouriteProductDTO implements Serializable {

    private Long id;

    @NotNull
    private Long productId;


    private Long customerId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
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

        FavouriteProductDTO favouriteProductDTO = (FavouriteProductDTO) o;
        if (favouriteProductDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), favouriteProductDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "FavouriteProductDTO{" +
            "id=" + getId() +
            ", productId=" + getProductId() +
            ", customer=" + getCustomerId() +
            "}";
    }
}
