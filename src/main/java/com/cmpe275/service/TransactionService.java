package com.cmpe275.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.cmpe275.Exception.CustomException;
import com.cmpe275.entity.CounterOffer;
import com.cmpe275.entity.Enum;
import com.cmpe275.entity.Offer;
import com.cmpe275.entity.Transaction;
import com.cmpe275.entity.User;
import com.cmpe275.repo.CounterOfferRepo;
import com.cmpe275.repo.OfferRepo;
import com.cmpe275.repo.TransactionRepo;
import com.cmpe275.repo.UserRepo;
import com.fasterxml.jackson.databind.JsonNode;

@Service
public class TransactionService {

	@Autowired
	private OfferRepo offerRepo;

	@Autowired
	private UserRepo userRepo;

	@Autowired
	private TransactionRepo transactionRepo;

	@Autowired
	private CounterOfferRepo counterOfferRepo;

	public ResponseEntity<Object> processDirectOffer(HttpServletRequest req, JsonNode body, long offerId) {
		try {
			long counterUserId = (long) body.get("userId").asLong();
			System.out.println("User Requesting: " + counterUserId);
			Optional<Offer> originalOffer = offerRepo.getById(offerId);
			if (originalOffer.isEmpty())
				throw new CustomException("Offer Id Invalid", HttpStatus.NOT_FOUND);
			if (!originalOffer.get().getStatus().equals(Enum.OfferStatuses.open))
				throw new CustomException("Offer already taken", HttpStatus.BAD_REQUEST);

			Offer newOffer = createNewCounterOffer(originalOffer.get(), counterUserId);

			List<Offer> transactionList = new ArrayList<Offer>();
			transactionList.add(originalOffer.get());
			transactionList.add(newOffer);
			Transaction transaction = createTransaction(transactionList);

			originalOffer.get().setFulfilledBy(transaction);
			originalOffer.get().setStatus(Enum.OfferStatuses.fulfilled);
			originalOffer.get().setTransactedAmount(originalOffer.get().getTransactedAmount());
			originalOffer.get().setFullyFulfilled(true);
			offerRepo.save(originalOffer.get());

			newOffer.setFulfilledBy(transaction);
			offerRepo.save(newOffer);

			return new ResponseEntity<>("Success", HttpStatus.OK);
		} catch (CustomException e) {
			e.printStackTrace();
			return new ResponseEntity<>(e.getMessage(), e.getErrorCode());
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("Invalid Data", HttpStatus.BAD_REQUEST);
		}
	}

	public Offer createNewCounterOffer(Offer originalOffer, long counterUserId) throws CustomException {
		try {
			Offer offer = new Offer();
			offer.setDestinationCountry(originalOffer.getSourceCountry());
			offer.setDestinationCurrency(originalOffer.getSourceCurrency());
			offer.setSourceCountry(originalOffer.getDestinationCountry());
			offer.setSourceCurrency(originalOffer.getDestinationCurrency());
			offer.setStatus(Enum.OfferStatuses.fulfilled);
			offer.setExchangeRate(1 / originalOffer.getExchangeRate());
			offer.setUsePrevailingRate(originalOffer.isUsePrevailingRate());
			offer.setAmount(originalOffer.getAmount());
			offer.setTransactedAmount(originalOffer.getAmount());
			offer.setCounter(true);
			offer.setFullyFulfilled(true);
			Optional<User> user = userRepo.getById(counterUserId);
			offer.setPostedBy(user.get());

			Offer savedNewOffer = offerRepo.save(offer);

			return savedNewOffer;
		} catch (Exception e) {
			e.printStackTrace();
			throw new CustomException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	public Transaction createTransaction(List<Offer> transactionList) throws CustomException {
		try {
			Transaction transaction = new Transaction();
			transaction.setOffers(transactionList);
			transaction.setDate(new Timestamp(System.currentTimeMillis()));
			return transactionRepo.save(transaction);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CustomException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	public ResponseEntity<Object> counterAOffer(HttpServletRequest request, JsonNode body, long offerId) {
		try {
			long counterUserId = (long) body.get("userId").asLong();
			double counterAmount = (double) body.get("userId").asDouble();
			Optional<Offer> sourceOffer = offerRepo.getById(offerId);
			if (sourceOffer.isEmpty())
				throw new CustomException("Offer Id Invalid", HttpStatus.NOT_FOUND);
			if (!sourceOffer.get().getStatus().equals(Enum.OfferStatuses.open))
				throw new CustomException("Offer already taken", HttpStatus.BAD_REQUEST);
			
			createCounterForAOffer(sourceOffer.get(), counterAmount, counterUserId);
			return new ResponseEntity<>("Success", HttpStatus.OK);
		} catch (CustomException e) {
			e.printStackTrace();
			return new ResponseEntity<>(e.getMessage(), e.getErrorCode());
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("Invalid Data", HttpStatus.BAD_REQUEST);
		}
	}

	public void createCounterForAOffer(Offer sourceOffer, double counterAmount, long counterUserId)
			throws CustomException {
		try {
			CounterOffer counter = new CounterOffer();
			counter.setCounterAmount(counterAmount);
			counter.setCounteredAgainst(sourceOffer);
			counter.setCounteredBy(counterUserId);
			counter.setOriginalAmount(sourceOffer.getAmount());
			CounterOffer counterOffer = counterOfferRepo.save(counter);
			User originalOfferUser = sourceOffer.getPostedBy();

			List<CounterOffer> existingCounterOffers = originalOfferUser.getCounterOffers();
			existingCounterOffers.add(counterOffer);
			originalOfferUser.setCounterOffers(existingCounterOffers);
			userRepo.save(originalOfferUser);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CustomException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	public ResponseEntity<Object> processAutoMatchOffer(HttpServletRequest request, JsonNode body,
			long offerId) {
		double amount = (double) body.get("amount").asDouble();
		double originalAmount = (double) body.get("originalAmount").asDouble();
		
		return null;
	}
}
