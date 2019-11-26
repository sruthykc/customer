package com.diviso.graeshoppe.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of FavouriteStoreSearchRepository to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class FavouriteStoreSearchRepositoryMockConfiguration {

    @MockBean
    private FavouriteStoreSearchRepository mockFavouriteStoreSearchRepository;

}
