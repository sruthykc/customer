package com.diviso.graeshoppe.web.rest;

import com.diviso.graeshoppe.domain.Customer;
import com.diviso.graeshoppe.domain.OTPChallenge;
import com.diviso.graeshoppe.domain.OTPResponse;
import com.diviso.graeshoppe.repository.CustomerRepository;
import com.diviso.graeshoppe.service.CustomerService;
import com.diviso.graeshoppe.web.rest.errors.BadRequestAlertException;
import com.diviso.graeshoppe.web.rest.util.HeaderUtil;
import com.diviso.graeshoppe.web.rest.util.PaginationUtil;
import com.diviso.graeshoppe.service.dto.CustomerDTO;
import com.diviso.graeshoppe.service.mapper.CustomerMapper;

import io.github.jhipster.web.util.ResponseUtil;
import net.sf.jasperreports.engine.JRException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Customer.
 */
@RestController
@RequestMapping("/api")
public class CustomerResource {

	private final Logger log = LoggerFactory.getLogger(CustomerResource.class);

	private static final String ENTITY_NAME = "customerCustomer";

	private final CustomerService customerService;
	
	

	@Autowired
	private CustomerMapper customerMapper;

	@Autowired
	private CustomerRepository customerRepository;

	
	
	
	public CustomerResource(CustomerService customerService) {
		this.customerService = customerService;
	}

	@GetMapping("/findByMobileNumber/{mobileNumber}")
	public ResponseEntity<CustomerDTO> findByMobileNumber(@PathVariable Long mobileNumber) {
		Optional<CustomerDTO> customerDTO=customerService.findByMobileNumber(mobileNumber);
		return ResponseUtil.wrapOrNotFound(customerDTO);
	}
	
	/**
	 * POST /customers : Create a new customer.
	 *
	 * @param customerDTO the customerDTO to create
	 * @return the ResponseEntity with status 201 (Created) and with body the new
	 *         customerDTO, or with status 400 (Bad Request) if the customer has
	 *         already an ID
	 * @throws URISyntaxException if the Location URI syntax is incorrect
	 */
	@PostMapping("/customers")
	public ResponseEntity<CustomerDTO> createCustomer(@RequestBody CustomerDTO customerDTO) throws URISyntaxException {

		log.debug("REST request to save Customer : {}", customerDTO);

		if (customerDTO.getId() != null) {
			throw new BadRequestAlertException("A new customer cannot already have an ID", ENTITY_NAME, "idexists");
		}

		List<Customer> customers = customerRepository.findAll();

		for (Customer c : customers) {

			if (customerDTO.getName().equals(c.getName())) {

				throw new BadRequestAlertException("Already have a customer with the same name", ENTITY_NAME,
						"nameexists");
			}
		}

		CustomerDTO result1 = customerService.save(customerDTO);
		if (result1.getId() == null) {
			throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
		}

		CustomerDTO result = customerService.save(result1);

		return ResponseEntity.created(new URI("/api/customers/" + result.getId()))
				.headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString())).body(result);
	}

	/**
	 * PUT /customers : Updates an existing customer.
	 *
	 * @param customerDTO the customerDTO to update
	 * @return the ResponseEntity with status 200 (OK) and with body the updated
	 *         customerDTO, or with status 400 (Bad Request) if the customerDTO is
	 *         not valid, or with status 500 (Internal Server Error) if the
	 *         customerDTO couldn't be updated
	 * @throws URISyntaxException if the Location URI syntax is incorrect
	 */
	@PutMapping("/customers")
	public ResponseEntity<CustomerDTO> updateCustomer(@RequestBody CustomerDTO customerDTO) throws URISyntaxException {
		log.debug("REST request to update Customer : {}", customerDTO);
		if (customerDTO.getId() == null) {
			throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
		}
		CustomerDTO result = customerService.save(customerDTO);
		return ResponseEntity.ok()
				.headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, customerDTO.getId().toString())).body(result);
	}

	/**
	 * GET /customers : get all the customers.
	 *
	 * @param pageable the pagination information
	 * @return the ResponseEntity with status 200 (OK) and the list of customers in
	 *         body
	 */
	@GetMapping("/customers")
	public ResponseEntity<List<CustomerDTO>> getAllCustomers(Pageable pageable) {
		log.debug("REST request to get a page of Customers");
		Page<CustomerDTO> page = customerService.findAll(pageable);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/customers");
		return ResponseEntity.ok().headers(headers).body(page.getContent());
	}

	/**
	 * GET /customers/:id : get the "id" customer.
	 *
	 * @param id the id of the customerDTO to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the
	 *         customerDTO, or with status 404 (Not Found)
	 */
	@GetMapping("/customers/{id}")
	public ResponseEntity<CustomerDTO> getCustomer(@PathVariable Long id) {
		log.debug("REST request to get Customer : {}", id);
		Optional<CustomerDTO> customerDTO = customerService.findOne(id);
		return ResponseUtil.wrapOrNotFound(customerDTO);
	}

	/**
	 * DELETE /customers/:id : delete the "id" customer.
	 *
	 * @param id the id of the customerDTO to delete
	 * @return the ResponseEntity with status 200 (OK)
	 */
	@DeleteMapping("/customers/{id}")
	public ResponseEntity<Void> deleteCustomer(@PathVariable Long id) {
		log.debug("REST request to delete Customer : {}", id);
		customerService.delete(id);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
	}

	/**
	 * SEARCH /_search/customers?query=:query : search for the customer
	 * corresponding to the query.
	 *
	 * @param query    the query of the customer search
	 * @param pageable the pagination information
	 * @return the result of the search
	 */
	@GetMapping("/_search/customers")
	public ResponseEntity<List<CustomerDTO>> searchCustomers(@RequestParam String query, Pageable pageable) {
		log.debug("REST request to search for a page of Customers for query {}", query);
		Page<CustomerDTO> page = customerService.search(query, pageable);
		HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/customers");
		return ResponseEntity.ok().headers(headers).body(page.getContent());
	}

	/**
	 * GET /pdf/customerReport : get the pdf of all the products.
	 * 
	 * @return the byte[]
	 * @return the ResponseEntity with status 200 (OK) and the pdf of customers in
	 *         body
	 */

	@GetMapping("/pdf/customerReport")
	public ResponseEntity<byte[]> getPdfAllCustomers() {

		log.debug("REST request to get a pdf of customers");

		byte[] pdfContents = null;

		try {
			pdfContents = customerService.getPdfAllCustomers();
		} catch (JRException e) {
			e.printStackTrace();
		}
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.parseMediaType("application/pdf"));
		String fileName = "customersDetailsReport.pdf";
		headers.add("content-disposition", "attachment; filename=" + fileName);
		ResponseEntity<byte[]> response = new ResponseEntity<byte[]>(pdfContents, headers, HttpStatus.OK);
		return response;
	}

	@PostMapping("/customer/modelToDto")
	public ResponseEntity<CustomerDTO> modelToDto(@RequestBody Customer customer) {
		log.debug("REST request to convert to DTO");
		return ResponseEntity.ok().body(customerMapper.toDto(customer));
	}
	
	@PostMapping("/customer/sendSMS")
	public ResponseEntity<CustomerDTO> sendSMS(@RequestBody Customer customer) {
		log.debug("REST request to convert to DTO");
		return ResponseEntity.ok().body(customerMapper.toDto(customer));
	}
	
	@PostMapping("/customer/otp_send")
	OTPResponse sendSMS( @RequestParam long numbers) {
    			
		return customerService.sendSMS( numbers);
	}
	
    @PostMapping("/customer/otp_challenge")
	OTPChallenge verifyOTP(@RequestParam long numbers, @RequestParam String code) {
  			
		return customerService.verifyOTP(numbers,code);
	}

	public CustomerRepository getCustomerRepository() {
		return customerRepository;
	}

	@GetMapping("/findByIdpcode/{idpCode}")
	public Customer findByReference(@PathVariable String idpCode) {
		return customerService.findByIdpCode(idpCode);
	}
	
	
	@GetMapping("/checkUserExists/{reference}")
	public Boolean checkUserExists(@PathVariable String reference) {
		return customerService.checkUserExists(reference);
	}
	
	public void setCustomerRepository(CustomerRepository customerRepository) {
		this.customerRepository = customerRepository;
	}

	
	
}
