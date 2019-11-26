package com.diviso.graeshoppe.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import java.nio.ByteBuffer;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.diviso.graeshoppe.avro.Customer.Builder;

import com.diviso.graeshoppe.client.SMS.SMSResourceApiIN;
import com.diviso.graeshoppe.client.SMS.SMSResourceApiUK;
import com.diviso.graeshoppe.config.MessageBinderConfiguration;
import com.diviso.graeshoppe.domain.Customer;
import com.diviso.graeshoppe.domain.OTPChallenge;
import com.diviso.graeshoppe.domain.OTPResponse;

import com.diviso.graeshoppe.repository.ContactRepository;
import com.diviso.graeshoppe.repository.CustomerRepository;
import com.diviso.graeshoppe.repository.search.CustomerSearchRepository;
import com.diviso.graeshoppe.service.CustomerService;
import com.diviso.graeshoppe.service.UniqueCustomerIDService;
import com.diviso.graeshoppe.service.dto.CustomerDTO;
import com.diviso.graeshoppe.service.dto.UniqueCustomerIDDTO;
import com.diviso.graeshoppe.service.mapper.CustomerMapper;
import com.diviso.graeshoppe.service.mapper.avro.CustomerAvroMapper;


import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

/**
 * Service Implementation for managing Customer.
 */
@Service
@Transactional
public class CustomerServiceImpl implements CustomerService {

	private final Logger log = LoggerFactory.getLogger(CustomerServiceImpl.class);

	private final CustomerRepository customerRepository;

	private final CustomerMapper customerMapper;

	private final CustomerAvroMapper customerAvroMapper;
	
	private final CustomerSearchRepository customerSearchRepository;
	
	
	@Autowired
	private MessageBinderConfiguration messageChannel;

	@Value("${smsgateway.credentials.in-apiKey}")
	private String apiKey_IN;

	@Value("${smsgateway.in-sender}")
	private String SMSsender_IN;

	@Value("${smsgateway.credentials.uk-apiKey}")
	private String apiKey_UK;

	@Value("${smsgateway.uk-sender}")
	private String SMSsender_UK;

	@Value("${app.customerId-prefix}")
	private String uniqueIdPrefix;
	@Autowired
	private ContactRepository contactRepository;

	@Autowired
	private SMSResourceApiUK smsResourceApiUK;
	@Autowired
	private SMSResourceApiIN smsResourceApiIN;
	@Autowired
	private UniqueCustomerIDService uniqueIdService;
	@Autowired
	private JavaMailSender sender;

	@Autowired
	DataSource dataSource;


	public CustomerServiceImpl(CustomerRepository customerRepository, CustomerMapper customerMapper,
			CustomerAvroMapper customerAvroMapper,CustomerSearchRepository customerSearchRepository) {
		this.customerRepository = customerRepository;
		this.customerMapper = customerMapper;
		this.customerAvroMapper = customerAvroMapper;
		this.customerSearchRepository = customerSearchRepository;
	}

	/**
	 * Save a customer.
	 *
	 * @param customerDTO the entity to save
	 * @return the persisted entity
	 */
	@Override
	public CustomerDTO save(CustomerDTO customerDTO) {
		
		log.debug("Request to save Customer : {}", customerDTO);
		UniqueCustomerIDDTO uniqueId=uniqueIdService.save(new UniqueCustomerIDDTO());
		customerDTO.setCustomerUniqueId(uniqueIdPrefix+""+uniqueId.getId());
		Customer customer = customerMapper.toEntity(customerDTO);
		customer = customerRepository.save(customer);
		CustomerDTO result = customerMapper.toDto(customer);
		customerSearchRepository.save(customer);
		String status="create";
        boolean publishstatus=createPublishMesssage(customer,status);
        
        log.debug("------------------------------------------published"+publishstatus);
        return result;
	}
	
	
	@Override
	public boolean createPublishMesssage(Customer customer, String status) {
		
        log.debug("------------------------------------------publish method"+status);

		/*
		 * Builder customerAvro = com.diviso.graeshoppe.avro.Customer.newBuilder()
		 * .setId(customer.getId()) .setName(customer.getName()) .setStatus(status);
		 */
	//	Builder customerAvro  = customerAvroMapper.modelToAvro(customer);
		//com.diviso.graeshoppe.avro.Customer message =customerAvro.build();
		com.diviso.graeshoppe.avro.Customer message =customerAvroMapper.modelToAvro(customer);
		message .setStatus(status);
		/*
		 * com.diviso.graeshoppe.avro.Customer c; c.getPhoto().get
		 */
		
		return messageChannel.customerOut().send(MessageBuilder.withPayload(message).build());

	}
	
	/**
	 * Update a customer.
	 *
	 * @param customerDTO the entity to update 
	 * @return the persisted entity
	 */
	@Override
	public CustomerDTO update(CustomerDTO customerDTO) {
		log.debug("Request to save Customer : {}", customerDTO);
		Customer customer = customerMapper.toEntity(customerDTO);
		customer = customerRepository.save(customer);
		CustomerDTO result = customerMapper.toDto(customer);
		customerSearchRepository.save(customer);
		String status="update";
        boolean publishstatus=updatePublishMesssage(customer,status);
        log.debug("------------------------------------------published"+publishstatus);
        return result;
	}


	@Override
	public boolean updatePublishMesssage(Customer customer, String status) {
        log.debug("------------------------------------------updatepublish method");

		Builder customerAvro = com.diviso.graeshoppe.avro.Customer.newBuilder()
				.setId(customer.getId())
				.setName(customer.getName())
				.setStatus(status);
		com.diviso.graeshoppe.avro.Customer message =customerAvro.build();
		return messageChannel.customerOut().send(MessageBuilder.withPayload(message).build());
	}

	/**
	 * Get all the customers.
	 *
	 * @param pageable the pagination information
	 * @return the list of entities
	 */
	@Override
	@Transactional(readOnly = true)
	public Page<CustomerDTO> findAll(Pageable pageable) {
		log.debug("Request to get all Customers");
		return customerRepository.findAll(pageable).map(customerMapper::toDto);
	}

	/**
	 * Get one customer by id.
	 *
	 * @param id the id of the entity
	 * @return the entity
	 */
	@Override
	@Transactional(readOnly = true)
	public Optional<CustomerDTO> findOne(Long id) {
		log.debug("Request to get Customer : {}", id);
		return customerRepository.findById(id).map(customerMapper::toDto);
	}

	/**
	 * Delete the customer by id.
	 *
	 * @param id the id of the entity
	 */
	@Override
	public void delete(Long id) {
		log.debug("Request to delete Customer : {}", id);
		//Customer customer=customerRepository.findById(id).get();
		customerRepository.deleteById(id);
		
		customerSearchRepository.deleteById(id);
		
		/*
		 * if((customerRepository.existsById(id))==false) { String status="deleted";
		 * deleteMesssage(customer, status); }
		 */
		
	}
	
	
	@Override
	public boolean deleteMesssage(Customer customer, String status) {
        log.debug("------------------------------------------updatepublish method");

		Builder customerAvro = com.diviso.graeshoppe.avro.Customer.newBuilder()

				.setStatus(status);
		com.diviso.graeshoppe.avro.Customer message =customerAvro.build();
		return messageChannel.customerOut().send(MessageBuilder.withPayload(message).build());
	}
	
	
	
	
	

	/**
	 * Search for the customer corresponding to the query.
	 *
	 * @param query    the query of the search
	 * @param pageable the pagination information
	 * @return the list of entities
	 */
	@Override
	@Transactional(readOnly = true)
	public Page<CustomerDTO> search(String query, Pageable pageable) {
		log.debug("Request to search for a page of Customers for query {}", query);
		return customerSearchRepository.search(queryStringQuery(query), pageable).map(customerMapper::toDto);
	}


	/**
	 * Send email notification to registered customer
	 *
	 * @param email the mobileNumber to send email
	 * @return the email sending status
	 */
	@Override
	public String sendEmail(String email) {

		MimeMessage message = sender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);

		try {
			helper.setTo(email);
			helper.setText("Thank you for registering with us!!!");
			helper.setSubject("Mail From Graeshoppe");
		} catch (MessagingException e) {
			e.printStackTrace();
			return "Error while sending mail ..";
		}

		sender.send(message);
		return "Mail Sent Success!";

	}

	/**
	 * Get customersReport.
	 * 
	 * @return the byte[]
	 * @throws JRException
	 */
	@Override
	public byte[] getPdfAllCustomers() throws JRException {

		log.debug("Request to pdf of all customers");

		JasperReport jr = JasperCompileManager.compileReport("CustomerDetails.jrxml");

		// Preparing parameters
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("customer", jr);

		Connection conn = null;
		try {
			conn = dataSource.getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		JasperPrint jp = JasperFillManager.fillReport(jr, parameters, conn);

		// JasperExportManager.exportReportToPdfFile(jp, "UserNeeds.pdf");

		return JasperExportManager.exportReportToPdf(jp);
	}

	@Override
	public OTPResponse sendSMS(Long numbers) {
		if (numbers.toString().substring(0, 2).equals("91")) {
			log.info("it is an indian number");
			String message = "Dear User, Enter your OTP to complete registration. OTP to verify your Mobile is ";
			return smsResourceApiIN.sendSMS(message, apiKey_IN, numbers, SMSsender_IN);
		} else {
			log.info("it is not an indian number");
			String message = "Dear User, Enter your OTP to complete registration. OTP to verify your Mobile is ";
			return smsResourceApiUK.sendSMS(message, apiKey_UK, numbers, SMSsender_UK);
		}

	}

	@Override
	public OTPChallenge verifyOTP(Long numbers, String code) {
		if (numbers.toString().substring(0, 2).equals("91")) {
			return smsResourceApiIN.verifyOTP(numbers, code, apiKey_IN);
		} else {
			return smsResourceApiUK.verifyOTP(numbers, code, apiKey_UK);

		}
	}

	@Override
	public Customer findByIdpCode(String idpCode) {
		return customerRepository.findByIdpCode(idpCode).get();
	}

	@Override
	public Optional<CustomerDTO> findByMobileNumber(Long mobileNumber) {

		return customerRepository.findByContact_MobileNumber(mobileNumber).map(customerMapper::toDto);
	}

	@Override
	public Boolean checkUserExists(String idpCode) {
		Boolean result= false;
		if(customerRepository.findByIdpCode(idpCode).isPresent()) {
			result =  true;
		}
		
		return result;
	}

}
