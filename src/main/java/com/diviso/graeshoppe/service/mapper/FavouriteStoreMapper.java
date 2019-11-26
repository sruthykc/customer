package com.diviso.graeshoppe.service.mapper;

import com.diviso.graeshoppe.domain.*;
import com.diviso.graeshoppe.service.dto.FavouriteStoreDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity FavouriteStore and its DTO FavouriteStoreDTO.
 */
@Mapper(componentModel = "spring", uses = {CustomerMapper.class})
public interface FavouriteStoreMapper extends EntityMapper<FavouriteStoreDTO, FavouriteStore> {

    @Mapping(source = "customer.id", target = "customerId")
    FavouriteStoreDTO toDto(FavouriteStore favouriteStore);

    @Mapping(source = "customerId", target = "customer")
    FavouriteStore toEntity(FavouriteStoreDTO favouriteStoreDTO);

    default FavouriteStore fromId(Long id) {
        if (id == null) {
            return null;
        }
        FavouriteStore favouriteStore = new FavouriteStore();
        favouriteStore.setId(id);
        return favouriteStore;
    }
}
