package com.cmpe275.repo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.cmpe275.entity.Enum;
import com.cmpe275.entity.Offer;

import java.util.List;

@Transactional
@Repository
public interface OffersRepo extends JpaRepository<Offer, Long> {
	
	
	@Query("SELECT o from Offer o where o.status = 'open'")
     List<Offer> getActiveOffers();
	
	@Query(value = "Select o.* from offer o left join exchange_currency e on "
			+ " o.destination_currency = e.target_currency and o.source_currency = e.source_currency "
			+ " where o.source_currency= :destinationCurrency and o.destination_currency= :sourceCurrency and o.status = 'open' and o.amount * IF(o.use_prevailing_rate=1,e.exchange_rate,o.exchange_rate) = :sourceAmount "
			+ " UNION Select o.* from offer o left join exchange_currency e on "
		    + " o.destination_currency = e.target_currency and o.source_currency = e.source_currency "
		    + " where o.source_currency= :destinationCurrency and o.destination_currency= :sourceCurrency and o.status = 'open' and o.amount * IF(o.use_prevailing_rate=1,e.exchange_rate,o.exchange_rate) between 0.9*:sourceAmount and 1.1*:sourceAmount "
		    + " UNION Select o.* from offer o left join exchange_currency e on "
		    + " o.destination_currency = e.target_currency and o.source_currency = e.source_currency "
		    + " where o.source_currency= :destinationCurrency and o.destination_currency= :sourceCurrency and o.status = 'open' and o.amount * IF(o.use_prevailing_rate=1,e.exchange_rate,o.exchange_rate) not between 0.9*:sourceAmount and 1.1*:sourceAmount ", 
		    nativeQuery = true)
	List<Offer> getActiveOffersBySourceAmount(
	@Param("sourceCurrency") String sourceCurrency, 
	@Param("destinationCurrency") String destinationCurrency,@Param("sourceAmount") double sourceAmount);
	
	@Query(value = "SELECT * FROM Offer o where o.status = 'open' and o.source_currency = :destinationCurrency and o.destination_currency = :sourceCurrency and  o.amount = :destinationAmount "
			+ "UNION SELECT * FROM Offer o where o.status = 'open' and o.source_currency = :destinationCurrency and o.destination_currency = :sourceCurrency and o.amount != :destinationAmount and (o.amount between 0.9*:destinationAmount and 1.1*:destinationAmount) "
			+ "UNION SELECT * FROM Offer o where o.status = 'open' and o.source_currency = :destinationCurrency and o.destination_currency = :sourceCurrency and o.amount != :destinationAmount and (o.amount not between 0.9*:destinationAmount and 1.1*:destinationAmount)  ;", 
			  nativeQuery = true)
	List<Offer> getActiveOffersByDestinationAmount(
	@Param("sourceCurrency") String sourceCurrency, 
	@Param("destinationCurrency") String destinationCurrency,@Param("destinationAmount") double destinationAmount);
}
