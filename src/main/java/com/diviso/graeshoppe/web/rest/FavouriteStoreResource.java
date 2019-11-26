package com.diviso.graeshoppe.web.rest;
import com.diviso.graeshoppe.service.FavouriteStoreService;
import com.diviso.graeshoppe.web.rest.errors.BadRequestAlertException;
import com.diviso.graeshoppe.web.rest.util.HeaderUtil;
import com.diviso.graeshoppe.web.rest.util.PaginationUtil;
import com.diviso.graeshoppe.service.dto.FavouriteStoreDTO;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing FavouriteStore.
 */
@RestController
@RequestMapping("/api")
public class FavouriteStoreResource {

    private final Logger log = LoggerFactory.getLogger(FavouriteStoreResource.class);

    private static final String ENTITY_NAME = "customerFavouriteStore";

    private final FavouriteStoreService favouriteStoreService;

    public FavouriteStoreResource(FavouriteStoreService favouriteStoreService) {
        this.favouriteStoreService = favouriteStoreService;
    }

    /**
     * POST  /favourite-stores : Create a new favouriteStore.
     *
     * @param favouriteStoreDTO the favouriteStoreDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new favouriteStoreDTO, or with status 400 (Bad Request) if the favouriteStore has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/favourite-stores")
    public ResponseEntity<FavouriteStoreDTO> createFavouriteStore(@Valid @RequestBody FavouriteStoreDTO favouriteStoreDTO) throws URISyntaxException {
        log.debug("REST request to save FavouriteStore : {}", favouriteStoreDTO);
        if (favouriteStoreDTO.getId() != null) {
            throw new BadRequestAlertException("A new favouriteStore cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FavouriteStoreDTO result = favouriteStoreService.save(favouriteStoreDTO);
        return ResponseEntity.created(new URI("/api/favourite-stores/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /favourite-stores : Updates an existing favouriteStore.
     *
     * @param favouriteStoreDTO the favouriteStoreDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated favouriteStoreDTO,
     * or with status 400 (Bad Request) if the favouriteStoreDTO is not valid,
     * or with status 500 (Internal Server Error) if the favouriteStoreDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/favourite-stores")
    public ResponseEntity<FavouriteStoreDTO> updateFavouriteStore(@Valid @RequestBody FavouriteStoreDTO favouriteStoreDTO) throws URISyntaxException {
        log.debug("REST request to update FavouriteStore : {}", favouriteStoreDTO);
        if (favouriteStoreDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        FavouriteStoreDTO result = favouriteStoreService.save(favouriteStoreDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, favouriteStoreDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /favourite-stores : get all the favouriteStores.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of favouriteStores in body
     */
    @GetMapping("/favourite-stores")
    public ResponseEntity<List<FavouriteStoreDTO>> getAllFavouriteStores(Pageable pageable) {
        log.debug("REST request to get a page of FavouriteStores");
        Page<FavouriteStoreDTO> page = favouriteStoreService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/favourite-stores");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * GET  /favourite-stores/:id : get the "id" favouriteStore.
     *
     * @param id the id of the favouriteStoreDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the favouriteStoreDTO, or with status 404 (Not Found)
     */
    @GetMapping("/favourite-stores/{id}")
    public ResponseEntity<FavouriteStoreDTO> getFavouriteStore(@PathVariable Long id) {
        log.debug("REST request to get FavouriteStore : {}", id);
        Optional<FavouriteStoreDTO> favouriteStoreDTO = favouriteStoreService.findOne(id);
        return ResponseUtil.wrapOrNotFound(favouriteStoreDTO);
    }

    /**
     * DELETE  /favourite-stores/:id : delete the "id" favouriteStore.
     *
     * @param id the id of the favouriteStoreDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/favourite-stores/{id}")
    public ResponseEntity<Void> deleteFavouriteStore(@PathVariable Long id) {
        log.debug("REST request to delete FavouriteStore : {}", id);
        favouriteStoreService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/favourite-stores?query=:query : search for the favouriteStore corresponding
     * to the query.
     *
     * @param query the query of the favouriteStore search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/favourite-stores")
    public ResponseEntity<List<FavouriteStoreDTO>> searchFavouriteStores(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of FavouriteStores for query {}", query);
        Page<FavouriteStoreDTO> page = favouriteStoreService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/favourite-stores");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

}
