package com.diviso.graeshoppe.service;

import com.diviso.graeshoppe.service.dto.FavouriteProductDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing FavouriteProduct.
 */
public interface FavouriteProductService {

    /**
     * Save a favouriteProduct.
     *
     * @param favouriteProductDTO the entity to save
     * @return the persisted entity
     */
    FavouriteProductDTO save(FavouriteProductDTO favouriteProductDTO);

    /**
     * Get all the favouriteProducts.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<FavouriteProductDTO> findAll(Pageable pageable);


    /**
     * Get the "id" favouriteProduct.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<FavouriteProductDTO> findOne(Long id);

    /**
     * Delete the "id" favouriteProduct.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the favouriteProduct corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<FavouriteProductDTO> search(String query, Pageable pageable);
}
