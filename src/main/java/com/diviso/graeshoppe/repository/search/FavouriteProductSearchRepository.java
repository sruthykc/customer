package com.diviso.graeshoppe.repository.search;

import com.diviso.graeshoppe.domain.FavouriteProduct;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the FavouriteProduct entity.
 */
public interface FavouriteProductSearchRepository extends ElasticsearchRepository<FavouriteProduct, Long> {
}
