package com.diviso.graeshoppe.service;

import com.diviso.graeshoppe.service.dto.FavouriteStoreDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing FavouriteStore.
 */
public interface FavouriteStoreService {

    /**
     * Save a favouriteStore.
     *
     * @param favouriteStoreDTO the entity to save
     * @return the persisted entity
     */
    FavouriteStoreDTO save(FavouriteStoreDTO favouriteStoreDTO);

    /**
     * Get all the favouriteStores.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<FavouriteStoreDTO> findAll(Pageable pageable);


    /**
     * Get the "id" favouriteStore.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<FavouriteStoreDTO> findOne(Long id);

    /**
     * Delete the "id" favouriteStore.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the favouriteStore corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<FavouriteStoreDTO> search(String query, Pageable pageable);
}
