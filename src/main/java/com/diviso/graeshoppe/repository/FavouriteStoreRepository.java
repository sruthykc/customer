package com.diviso.graeshoppe.repository;

import com.diviso.graeshoppe.domain.FavouriteStore;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the FavouriteStore entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FavouriteStoreRepository extends JpaRepository<FavouriteStore, Long> {

}
