package com.diviso.graeshoppe.service.impl;

import com.diviso.graeshoppe.service.FavouriteProductService;
import com.diviso.graeshoppe.domain.FavouriteProduct;
import com.diviso.graeshoppe.repository.FavouriteProductRepository;
import com.diviso.graeshoppe.repository.search.FavouriteProductSearchRepository;
import com.diviso.graeshoppe.service.dto.FavouriteProductDTO;
import com.diviso.graeshoppe.service.mapper.FavouriteProductMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing FavouriteProduct.
 */
@Service
@Transactional
public class FavouriteProductServiceImpl implements FavouriteProductService {

    private final Logger log = LoggerFactory.getLogger(FavouriteProductServiceImpl.class);

    private final FavouriteProductRepository favouriteProductRepository;

    private final FavouriteProductMapper favouriteProductMapper;

    private final FavouriteProductSearchRepository favouriteProductSearchRepository;

    public FavouriteProductServiceImpl(FavouriteProductRepository favouriteProductRepository, FavouriteProductMapper favouriteProductMapper, FavouriteProductSearchRepository favouriteProductSearchRepository) {
        this.favouriteProductRepository = favouriteProductRepository;
        this.favouriteProductMapper = favouriteProductMapper;
        this.favouriteProductSearchRepository = favouriteProductSearchRepository;
    }

    /**
     * Save a favouriteProduct.
     *
     * @param favouriteProductDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public FavouriteProductDTO save(FavouriteProductDTO favouriteProductDTO) {
        log.debug("Request to save FavouriteProduct : {}", favouriteProductDTO);
        FavouriteProduct favouriteProduct = favouriteProductMapper.toEntity(favouriteProductDTO);
        favouriteProduct = favouriteProductRepository.save(favouriteProduct);
        FavouriteProductDTO result = favouriteProductMapper.toDto(favouriteProduct);
        favouriteProductSearchRepository.save(favouriteProduct);
        return result;
    }

    /**
     * Get all the favouriteProducts.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<FavouriteProductDTO> findAll(Pageable pageable) {
        log.debug("Request to get all FavouriteProducts");
        return favouriteProductRepository.findAll(pageable)
            .map(favouriteProductMapper::toDto);
    }


    /**
     * Get one favouriteProduct by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<FavouriteProductDTO> findOne(Long id) {
        log.debug("Request to get FavouriteProduct : {}", id);
        return favouriteProductRepository.findById(id)
            .map(favouriteProductMapper::toDto);
    }

    /**
     * Delete the favouriteProduct by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete FavouriteProduct : {}", id);        favouriteProductRepository.deleteById(id);
        favouriteProductSearchRepository.deleteById(id);
    }

    /**
     * Search for the favouriteProduct corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<FavouriteProductDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of FavouriteProducts for query {}", query);
        return favouriteProductSearchRepository.search(queryStringQuery(query), pageable)
            .map(favouriteProductMapper::toDto);
    }
}
