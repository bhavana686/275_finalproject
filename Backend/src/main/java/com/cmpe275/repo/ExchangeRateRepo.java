package com.cmpe275.repo;

import java.util.Optional;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.cmpe275.entity.Offer;


@Transactional
@Repository
public interface ExchangeRateRepo extends JpaRepository<Offer, Long> {
	
    public Optional<Offer> getById(long id);
    
}