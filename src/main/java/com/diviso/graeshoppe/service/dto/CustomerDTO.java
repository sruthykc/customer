package com.diviso.graeshoppe.service.dto;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Lob;

/**
 * A DTO for the Customer entity.
 */
public class CustomerDTO implements Serializable {

    private Long id;

    @NotNull
    private String idpCode;

    private String name;

    private String idpSub;

    @NotNull
    private String customerUniqueId;

    @NotNull
    private String imageLink;

    @Lob
    private byte[] image;

    private String imageContentType;

    private Long contactId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIdpCode() {
        return idpCode;
    }

    public void setIdpCode(String idpCode) {
        this.idpCode = idpCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdpSub() {
        return idpSub;
    }

    public void setIdpSub(String idpSub) {
        this.idpSub = idpSub;
    }

    public String getCustomerUniqueId() {
        return customerUniqueId;
    }

    public void setCustomerUniqueId(String customerUniqueId) {
        this.customerUniqueId = customerUniqueId;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getImageContentType() {
        return imageContentType;
    }

    public void setImageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
    }

    public Long getContactId() {
        return contactId;
    }

    public void setContactId(Long contactId) {
        this.contactId = contactId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CustomerDTO customerDTO = (CustomerDTO) o;
        if (customerDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), customerDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "CustomerDTO{" +
            "id=" + getId() +
            ", idpCode='" + getIdpCode() + "'" +
            ", name='" + getName() + "'" +
            ", idpSub='" + getIdpSub() + "'" +
            ", customerUniqueId='" + getCustomerUniqueId() + "'" +
            ", imageLink='" + getImageLink() + "'" +
            ", image='" + getImage() + "'" +
            ", contact=" + getContactId() +
            "}";
    }
}
