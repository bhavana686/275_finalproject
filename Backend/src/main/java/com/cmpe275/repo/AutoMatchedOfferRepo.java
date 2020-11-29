package com.cmpe275.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.cmpe275.entity.AutoMatchedOffer;
import com.cmpe275.entity.CounterOffer;

import java.util.Optional;

@Transactional
@Repository
public interface AutoMatchedOfferRepo extends JpaRepository<AutoMatchedOffer, Long> {

	public Optional<AutoMatchedOffer> getById(long id);
	
	public Optional<AutoMatchedOffer> getByCounter(CounterOffer counterOffer);

}