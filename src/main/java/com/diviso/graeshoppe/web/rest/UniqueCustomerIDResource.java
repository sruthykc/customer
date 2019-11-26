package com.diviso.graeshoppe.web.rest;
import com.diviso.graeshoppe.service.UniqueCustomerIDService;
import com.diviso.graeshoppe.web.rest.errors.BadRequestAlertException;
import com.diviso.graeshoppe.web.rest.util.HeaderUtil;
import com.diviso.graeshoppe.web.rest.util.PaginationUtil;
import com.diviso.graeshoppe.service.dto.UniqueCustomerIDDTO;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing UniqueCustomerID.
 */
@RestController
@RequestMapping("/api")
public class UniqueCustomerIDResource {

    private final Logger log = LoggerFactory.getLogger(UniqueCustomerIDResource.class);

    private static final String ENTITY_NAME = "customerUniqueCustomerId";

    private final UniqueCustomerIDService uniqueCustomerIDService;

    public UniqueCustomerIDResource(UniqueCustomerIDService uniqueCustomerIDService) {
        this.uniqueCustomerIDService = uniqueCustomerIDService;
    }

    /**
     * POST  /unique-customer-ids : Create a new uniqueCustomerID.
     *
     * @param uniqueCustomerIDDTO the uniqueCustomerIDDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new uniqueCustomerIDDTO, or with status 400 (Bad Request) if the uniqueCustomerID has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/unique-customer-ids")
    public ResponseEntity<UniqueCustomerIDDTO> createUniqueCustomerID(@RequestBody UniqueCustomerIDDTO uniqueCustomerIDDTO) throws URISyntaxException {
        log.debug("REST request to save UniqueCustomerID : {}", uniqueCustomerIDDTO);
        if (uniqueCustomerIDDTO.getId() != null) {
            throw new BadRequestAlertException("A new uniqueCustomerID cannot already have an ID", ENTITY_NAME, "idexists");
        }
        UniqueCustomerIDDTO result = uniqueCustomerIDService.save(uniqueCustomerIDDTO);
        return ResponseEntity.created(new URI("/api/unique-customer-ids/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /unique-customer-ids : Updates an existing uniqueCustomerID.
     *
     * @param uniqueCustomerIDDTO the uniqueCustomerIDDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated uniqueCustomerIDDTO,
     * or with status 400 (Bad Request) if the uniqueCustomerIDDTO is not valid,
     * or with status 500 (Internal Server Error) if the uniqueCustomerIDDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/unique-customer-ids")
    public ResponseEntity<UniqueCustomerIDDTO> updateUniqueCustomerID(@RequestBody UniqueCustomerIDDTO uniqueCustomerIDDTO) throws URISyntaxException {
        log.debug("REST request to update UniqueCustomerID : {}", uniqueCustomerIDDTO);
        if (uniqueCustomerIDDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        UniqueCustomerIDDTO result = uniqueCustomerIDService.save(uniqueCustomerIDDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, uniqueCustomerIDDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /unique-customer-ids : get all the uniqueCustomerIDS.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of uniqueCustomerIDS in body
     */
    @GetMapping("/unique-customer-ids")
    public ResponseEntity<List<UniqueCustomerIDDTO>> getAllUniqueCustomerIDS(Pageable pageable) {
        log.debug("REST request to get a page of UniqueCustomerIDS");
        Page<UniqueCustomerIDDTO> page = uniqueCustomerIDService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/unique-customer-ids");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * GET  /unique-customer-ids/:id : get the "id" uniqueCustomerID.
     *
     * @param id the id of the uniqueCustomerIDDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the uniqueCustomerIDDTO, or with status 404 (Not Found)
     */
    @GetMapping("/unique-customer-ids/{id}")
    public ResponseEntity<UniqueCustomerIDDTO> getUniqueCustomerID(@PathVariable Long id) {
        log.debug("REST request to get UniqueCustomerID : {}", id);
        Optional<UniqueCustomerIDDTO> uniqueCustomerIDDTO = uniqueCustomerIDService.findOne(id);
        return ResponseUtil.wrapOrNotFound(uniqueCustomerIDDTO);
    }

    /**
     * DELETE  /unique-customer-ids/:id : delete the "id" uniqueCustomerID.
     *
     * @param id the id of the uniqueCustomerIDDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/unique-customer-ids/{id}")
    public ResponseEntity<Void> deleteUniqueCustomerID(@PathVariable Long id) {
        log.debug("REST request to delete UniqueCustomerID : {}", id);
        uniqueCustomerIDService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/unique-customer-ids?query=:query : search for the uniqueCustomerID corresponding
     * to the query.
     *
     * @param query the query of the uniqueCustomerID search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/unique-customer-ids")
    public ResponseEntity<List<UniqueCustomerIDDTO>> searchUniqueCustomerIDS(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of UniqueCustomerIDS for query {}", query);
        Page<UniqueCustomerIDDTO> page = uniqueCustomerIDService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/unique-customer-ids");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

}
