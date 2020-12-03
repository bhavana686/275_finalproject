package com.cmpe275.repo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.cmpe275.entity.Enum;
import com.cmpe275.entity.Transaction;
import com.cmpe275.models.*;

import java.util.List;

@Transactional
@Repository

public interface TransactionHistoryRepo extends JpaRepository<Transaction,Long> {
	
	@Query(value = "Select t.date,o.source_currency,o.amount,o.destination_currency,o.exchanged_rate,o.transacted_amount*0.995 as destination_amount, "
			+ "o.transacted_amount*0.005 as service_fee,t.status "
			+ "from transaction t right join transaction_offers too on t.id=too.transaction_id "
			+ "left join offer o on o.id = too.offers_id where o.posted_by_id= :userid and t.status='fulfilled' and "
			+ "t.date BETWEEN DATE_SUB(NOW(), INTERVAL 365 DAY) AND NOW() and EXTRACT(MONTH FROM t.date) = :month ;",nativeQuery = true)
    List<List<String>> getTransactionHistoryByMonth(@Param("userid") long userid,@Param("month") int month);
	
	@Query(value = "Select * from (Select count(*) as Count,sum(amount) as Total,1 as Filter from transaction where status = 'fulfilled' and "
			+ "date BETWEEN DATE_SUB(NOW(), INTERVAL 365 DAY) AND NOW() and EXTRACT(MONTH FROM date) =:month "
			+ "UNION "
			+ "Select count(*) as Count,sum(amount) as Total,2 as Filter from transaction where status != 'fulfilled' and "
			+ "date BETWEEN DATE_SUB(NOW(), INTERVAL 365 DAY) AND NOW() and EXTRACT(MONTH FROM date) =:month) as Stats order by Filter;",nativeQuery = true)
    List<List<String>> getTotalTransactionHistoryByMonth(@Param("month") int month);

}
