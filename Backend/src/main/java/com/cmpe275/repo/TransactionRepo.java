package com.cmpe275.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.cmpe275.entity.Transaction;
import java.util.Optional;

@Transactional
@Repository
public interface TransactionRepo extends JpaRepository<Transaction, Long> {

	public Optional<Transaction> getById(long id);

}
