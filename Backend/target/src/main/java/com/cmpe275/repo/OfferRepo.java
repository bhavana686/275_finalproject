package com.cmpe275.repo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.cmpe275.entity.Offer;
import com.cmpe275.entity.User;

import java.util.Optional;

@Transactional
@Repository
public interface OfferRepo extends JpaRepository<Offer, Long> {
	
    public Optional<Offer> getById(long id);
    
    public Optional<Offer> getByPostedBy(User user);
	
}

