package com.diviso.graeshoppe.repository.search;

import com.diviso.graeshoppe.domain.FavouriteStore;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the FavouriteStore entity.
 */
public interface FavouriteStoreSearchRepository extends ElasticsearchRepository<FavouriteStore, Long> {
}
