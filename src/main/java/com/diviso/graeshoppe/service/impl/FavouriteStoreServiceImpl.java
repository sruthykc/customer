package com.diviso.graeshoppe.service.impl;

import com.diviso.graeshoppe.service.FavouriteStoreService;
import com.diviso.graeshoppe.domain.FavouriteStore;
import com.diviso.graeshoppe.repository.FavouriteStoreRepository;
import com.diviso.graeshoppe.repository.search.FavouriteStoreSearchRepository;
import com.diviso.graeshoppe.service.dto.FavouriteStoreDTO;
import com.diviso.graeshoppe.service.mapper.FavouriteStoreMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing FavouriteStore.
 */
@Service
@Transactional
public class FavouriteStoreServiceImpl implements FavouriteStoreService {

    private final Logger log = LoggerFactory.getLogger(FavouriteStoreServiceImpl.class);

    private final FavouriteStoreRepository favouriteStoreRepository;

    private final FavouriteStoreMapper favouriteStoreMapper;

    private final FavouriteStoreSearchRepository favouriteStoreSearchRepository;

    public FavouriteStoreServiceImpl(FavouriteStoreRepository favouriteStoreRepository, FavouriteStoreMapper favouriteStoreMapper, FavouriteStoreSearchRepository favouriteStoreSearchRepository) {
        this.favouriteStoreRepository = favouriteStoreRepository;
        this.favouriteStoreMapper = favouriteStoreMapper;
        this.favouriteStoreSearchRepository = favouriteStoreSearchRepository;
    }

    /**
     * Save a favouriteStore.
     *
     * @param favouriteStoreDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public FavouriteStoreDTO save(FavouriteStoreDTO favouriteStoreDTO) {
        log.debug("Request to save FavouriteStore : {}", favouriteStoreDTO);
        FavouriteStore favouriteStore = favouriteStoreMapper.toEntity(favouriteStoreDTO);
        favouriteStore = favouriteStoreRepository.save(favouriteStore);
        FavouriteStoreDTO result = favouriteStoreMapper.toDto(favouriteStore);
        favouriteStoreSearchRepository.save(favouriteStore);
        return result;
    }

    /**
     * Get all the favouriteStores.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<FavouriteStoreDTO> findAll(Pageable pageable) {
        log.debug("Request to get all FavouriteStores");
        return favouriteStoreRepository.findAll(pageable)
            .map(favouriteStoreMapper::toDto);
    }


    /**
     * Get one favouriteStore by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<FavouriteStoreDTO> findOne(Long id) {
        log.debug("Request to get FavouriteStore : {}", id);
        return favouriteStoreRepository.findById(id)
            .map(favouriteStoreMapper::toDto);
    }

    /**
     * Delete the favouriteStore by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete FavouriteStore : {}", id);        favouriteStoreRepository.deleteById(id);
        favouriteStoreSearchRepository.deleteById(id);
    }

    /**
     * Search for the favouriteStore corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<FavouriteStoreDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of FavouriteStores for query {}", query);
        return favouriteStoreSearchRepository.search(queryStringQuery(query), pageable)
            .map(favouriteStoreMapper::toDto);
    }
}
