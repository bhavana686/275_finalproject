package com.cmpe275.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.cmpe275.entity.CounterOffer;
import java.util.Optional;

@Transactional
@Repository
public interface CounterOfferRepo extends JpaRepository<CounterOffer, Long> {

	public Optional<CounterOffer> getById(long id);

}
