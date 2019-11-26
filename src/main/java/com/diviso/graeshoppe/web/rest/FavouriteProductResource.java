package com.diviso.graeshoppe.web.rest;
import com.diviso.graeshoppe.service.FavouriteProductService;
import com.diviso.graeshoppe.web.rest.errors.BadRequestAlertException;
import com.diviso.graeshoppe.web.rest.util.HeaderUtil;
import com.diviso.graeshoppe.web.rest.util.PaginationUtil;
import com.diviso.graeshoppe.service.dto.FavouriteProductDTO;
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
 * REST controller for managing FavouriteProduct.
 */
@RestController
@RequestMapping("/api")
public class FavouriteProductResource {

    private final Logger log = LoggerFactory.getLogger(FavouriteProductResource.class);

    private static final String ENTITY_NAME = "customerFavouriteProduct";

    private final FavouriteProductService favouriteProductService;

    public FavouriteProductResource(FavouriteProductService favouriteProductService) {
        this.favouriteProductService = favouriteProductService;
    }

    /**
     * POST  /favourite-products : Create a new favouriteProduct.
     *
     * @param favouriteProductDTO the favouriteProductDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new favouriteProductDTO, or with status 400 (Bad Request) if the favouriteProduct has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/favourite-products")
    public ResponseEntity<FavouriteProductDTO> createFavouriteProduct(@Valid @RequestBody FavouriteProductDTO favouriteProductDTO) throws URISyntaxException {
        log.debug("REST request to save FavouriteProduct : {}", favouriteProductDTO);
        if (favouriteProductDTO.getId() != null) {
            throw new BadRequestAlertException("A new favouriteProduct cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FavouriteProductDTO result = favouriteProductService.save(favouriteProductDTO);
        return ResponseEntity.created(new URI("/api/favourite-products/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /favourite-products : Updates an existing favouriteProduct.
     *
     * @param favouriteProductDTO the favouriteProductDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated favouriteProductDTO,
     * or with status 400 (Bad Request) if the favouriteProductDTO is not valid,
     * or with status 500 (Internal Server Error) if the favouriteProductDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/favourite-products")
    public ResponseEntity<FavouriteProductDTO> updateFavouriteProduct(@Valid @RequestBody FavouriteProductDTO favouriteProductDTO) throws URISyntaxException {
        log.debug("REST request to update FavouriteProduct : {}", favouriteProductDTO);
        if (favouriteProductDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        FavouriteProductDTO result = favouriteProductService.save(favouriteProductDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, favouriteProductDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /favourite-products : get all the favouriteProducts.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of favouriteProducts in body
     */
    @GetMapping("/favourite-products")
    public ResponseEntity<List<FavouriteProductDTO>> getAllFavouriteProducts(Pageable pageable) {
        log.debug("REST request to get a page of FavouriteProducts");
        Page<FavouriteProductDTO> page = favouriteProductService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/favourite-products");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * GET  /favourite-products/:id : get the "id" favouriteProduct.
     *
     * @param id the id of the favouriteProductDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the favouriteProductDTO, or with status 404 (Not Found)
     */
    @GetMapping("/favourite-products/{id}")
    public ResponseEntity<FavouriteProductDTO> getFavouriteProduct(@PathVariable Long id) {
        log.debug("REST request to get FavouriteProduct : {}", id);
        Optional<FavouriteProductDTO> favouriteProductDTO = favouriteProductService.findOne(id);
        return ResponseUtil.wrapOrNotFound(favouriteProductDTO);
    }

    /**
     * DELETE  /favourite-products/:id : delete the "id" favouriteProduct.
     *
     * @param id the id of the favouriteProductDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/favourite-products/{id}")
    public ResponseEntity<Void> deleteFavouriteProduct(@PathVariable Long id) {
        log.debug("REST request to delete FavouriteProduct : {}", id);
        favouriteProductService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/favourite-products?query=:query : search for the favouriteProduct corresponding
     * to the query.
     *
     * @param query the query of the favouriteProduct search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/favourite-products")
    public ResponseEntity<List<FavouriteProductDTO>> searchFavouriteProducts(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of FavouriteProducts for query {}", query);
        Page<FavouriteProductDTO> page = favouriteProductService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/favourite-products");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

}
