package com.diviso.graeshoppe.service.mapper;

import com.diviso.graeshoppe.domain.*;
import com.diviso.graeshoppe.service.dto.FavouriteProductDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity FavouriteProduct and its DTO FavouriteProductDTO.
 */
@Mapper(componentModel = "spring", uses = {CustomerMapper.class})
public interface FavouriteProductMapper extends EntityMapper<FavouriteProductDTO, FavouriteProduct> {

    @Mapping(source = "customer.id", target = "customerId")
    FavouriteProductDTO toDto(FavouriteProduct favouriteProduct);

    @Mapping(source = "customerId", target = "customer")
    FavouriteProduct toEntity(FavouriteProductDTO favouriteProductDTO);

    default FavouriteProduct fromId(Long id) {
        if (id == null) {
            return null;
        }
        FavouriteProduct favouriteProduct = new FavouriteProduct();
        favouriteProduct.setId(id);
        return favouriteProduct;
    }
}
