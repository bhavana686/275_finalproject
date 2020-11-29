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
	
	@Query("SELECT o.id FROM Offer o WHERE o.status = 'open' and o.sourceCurrency = ?1 and o.destinationCurrency = ?2 and o.amount *(case when o.usePrevailingRate = false then o.exchangeRate "
			+ "when o.usePrevailingRate = true then 75  end ) = ?3 ")
	List<Offer> getActiveOffersByDestinationAmount(String sourceCurrency,String destinationCurrency,int destinationAmount);
	
	@Query("SELECT o.id FROM Offer o WHERE o.status = 'open' and o.sourceCurrency = ?1 and o.destinationCurrency = ?2 and o.amount = ?3 ")
	List<Offer> getActiveOffersBySourceAmount(String sourceCurrency,String destinationCurrency,int sourceAmount);
}
