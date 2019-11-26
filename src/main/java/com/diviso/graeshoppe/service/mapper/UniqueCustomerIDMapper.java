package com.diviso.graeshoppe.service.mapper;

import com.diviso.graeshoppe.domain.*;
import com.diviso.graeshoppe.service.dto.UniqueCustomerIDDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity UniqueCustomerID and its DTO UniqueCustomerIDDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface UniqueCustomerIDMapper extends EntityMapper<UniqueCustomerIDDTO, UniqueCustomerID> {



    default UniqueCustomerID fromId(Long id) {
        if (id == null) {
            return null;
        }
        UniqueCustomerID uniqueCustomerID = new UniqueCustomerID();
        uniqueCustomerID.setId(id);
        return uniqueCustomerID;
    }
}
