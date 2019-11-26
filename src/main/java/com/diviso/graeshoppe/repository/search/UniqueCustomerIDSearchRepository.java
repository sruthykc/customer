package com.diviso.graeshoppe.repository.search;

import com.diviso.graeshoppe.domain.UniqueCustomerID;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the UniqueCustomerID entity.
 */
public interface UniqueCustomerIDSearchRepository extends ElasticsearchRepository<UniqueCustomerID, Long> {
}
