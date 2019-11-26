package com.diviso.graeshoppe.repository;

import com.diviso.graeshoppe.domain.FavouriteProduct;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the FavouriteProduct entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FavouriteProductRepository extends JpaRepository<FavouriteProduct, Long> {

}
