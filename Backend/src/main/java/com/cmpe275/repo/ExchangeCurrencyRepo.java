package com.cmpe275.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cmpe275.entity.ExchangeCurrency;
import com.cmpe275.entity.Enum;

import java.util.List;
import java.util.Optional;



@Transactional
@Repository
public interface ExchangeCurrencyRepo extends JpaRepository<ExchangeCurrency, Long> {
	
   

    
   

	public Optional<ExchangeCurrency> findBySourceCurrencyAndTargetCurrency(Enum.Currency source, Enum.Currency destination);
    
    
    

    
	
}
