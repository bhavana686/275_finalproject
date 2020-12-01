package com.cmpe275.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.cmpe275.entity.Offer;
import com.cmpe275.entity.User;
import com.cmpe275.entity.Enum;

import java.util.List;
import java.util.Optional;

@Transactional
@Repository
public interface OfferRepo extends JpaRepository<Offer, Long> {

	public Optional<Offer> getById(long id);

	public Optional<Offer> getByPostedBy(User user);

	@Query("Select o from Offer o where o.status=?1 and o.isCounter =?2 and o.display=?3 and o.amount between ?4 and ?5 and sourceCurrency=?6 and destinationCurrency=?7")
	public Optional<List<Offer>> getByAmountBetween(Enum.OfferStatuses status, boolean isCounter, boolean display,
			double start, double end, Enum.Currency source, Enum.Currency destination);

	@Query("Select o from Offer o where o.status=?1 and o.isCounter =?2 and o.display=?3 and o.amount<?4 and o.allowSplitExchanges=?5 and sourceCurrency=?6 and destinationCurrency=?7")
	public Optional<List<Offer>> getSplitMatches(Enum.OfferStatuses status, boolean isCounter, boolean display,
			double amount, boolean allowSplitExchanges, Enum.Currency source, Enum.Currency destination);

}
