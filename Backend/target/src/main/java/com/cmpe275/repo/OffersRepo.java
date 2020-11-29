package com.cmpe275.repo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.cmpe275.entity.Offer;

import java.util.List;

@Transactional
@Repository
public interface OffersRepo extends JpaRepository<Offer, Long> {
	
	@Query("SELECT id from Offer where status = 'open'")
     List<Offer> getActiveOffers();
}
