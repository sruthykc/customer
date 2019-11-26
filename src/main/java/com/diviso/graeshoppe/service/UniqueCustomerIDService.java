package com.diviso.graeshoppe.service;

import com.diviso.graeshoppe.service.dto.UniqueCustomerIDDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing UniqueCustomerID.
 */
public interface UniqueCustomerIDService {

    /**
     * Save a uniqueCustomerID.
     *
     * @param uniqueCustomerIDDTO the entity to save
     * @return the persisted entity
     */
    UniqueCustomerIDDTO save(UniqueCustomerIDDTO uniqueCustomerIDDTO);

    /**
     * Get all the uniqueCustomerIDS.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<UniqueCustomerIDDTO> findAll(Pageable pageable);


    /**
     * Get the "id" uniqueCustomerID.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<UniqueCustomerIDDTO> findOne(Long id);

    /**
     * Delete the "id" uniqueCustomerID.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the uniqueCustomerID corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<UniqueCustomerIDDTO> search(String query, Pageable pageable);
}
