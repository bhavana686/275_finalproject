package com.cmpe275.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.cmpe275.entity.TransferRequest;

import java.util.Optional;

@Transactional
@Repository
public interface TransferRequestRepo extends JpaRepository<TransferRequest, Long> {

	public Optional<TransferRequest> getById(long id);

}
