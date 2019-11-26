package com.diviso.graeshoppe.repository;

import com.diviso.graeshoppe.domain.UniqueCustomerID;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the UniqueCustomerID entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UniqueCustomerIDRepository extends JpaRepository<UniqueCustomerID, Long> {

}
