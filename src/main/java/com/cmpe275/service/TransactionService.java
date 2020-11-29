package com.cmpe275.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.cmpe275.Exception.CustomException;
import com.cmpe275.entity.AutoMatchedOffer;
import com.cmpe275.entity.CounterOffer;
import com.cmpe275.entity.Enum;
import com.cmpe275.entity.Offer;
import com.cmpe275.entity.Transaction;
import com.cmpe275.entity.User;
import com.cmpe275.repo.AutoMatchedOfferRepo;
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

	@Autowired
	private AutoMatchedOfferRepo autoMatchedOfferRepo;

	/*
	 * Process an Offer where a user directly picks and offer from list of offers
	 * and Proceeds with one of them
	 */

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

	/*
	 * Process an Offer where a user picks and offer from list of offers and
	 * Proceeds with one of them but decides to counter with a different amount
	 */

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

	public CounterOffer createCounterForAOffer(Offer sourceOffer, double counterAmount, long counterUserId)
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

			sourceOffer.setEditable(false);
			offerRepo.save(sourceOffer);
			return counterOffer;
		} catch (Exception e) {
			e.printStackTrace();
			throw new CustomException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/*
	 * Process an Offer where a user picks one of the auto match offers where the
	 * amount matches with his requirement or he decides to adjust his amount
	 * according to the matched offers amount or decides to counter the matched
	 * offers amount
	 */

	public ResponseEntity<Object> processAutoMatchOffer(HttpServletRequest request, JsonNode body, long offerId) {
		try {
			long counteredUserId = (long) body.get("userId").asLong();
			double offerAmount = (double) body.get("offerAmount").asDouble();
			double sumOfMatchedOffers = (double) body.get("sumOfMatchedOffers").asDouble();
			double requestingAmount = (double) body.get("requestingAmount").asDouble();
			JsonNode offersNode = (JsonNode) body.get("offers");

			Optional<Offer> originalOffer = offerRepo.findById(offerId);
			originalOffer.get().setDisplay(false);
			offerRepo.save(originalOffer.get());

			List<Offer> offers = getOffersFromBody(offersNode);
			if (offerAmount == sumOfMatchedOffers || requestingAmount == sumOfMatchedOffers) {
				// Process all offers
				processEqualAutoMatchedOffers(originalOffer.get(), requestingAmount, offers);
			} else {
				// Adjust Offer
				adjustOffersBasedOnAmount(originalOffer.get(), offerAmount, sumOfMatchedOffers, requestingAmount,
						offers);
			}
			return new ResponseEntity<>("Success", HttpStatus.OK);
		} catch (CustomException e) {
			e.printStackTrace();
			return new ResponseEntity<>(e.getMessage(), e.getErrorCode());
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("Invalid Data", HttpStatus.BAD_REQUEST);
		}
	}

	public List<Offer> getOffersFromBody(JsonNode node) throws CustomException {
		try {
			System.out.println("Processing offers from body");
			List<Offer> offers = new ArrayList<Offer>();
			if (node.isArray()) {
				for (final JsonNode off : node) {
					long offerId = (long) off.get("offerId").asLong();
					System.out.println(offerId);
					Optional<Offer> offer = offerRepo.findById(offerId);
					if (offer.isEmpty())
						throw new CustomException("Offer Doesnt exist", HttpStatus.BAD_REQUEST);
					if (!offer.get().getStatus().equals(Enum.OfferStatuses.open))
						throw new CustomException("Offer already taken", HttpStatus.BAD_REQUEST);
					offers.add(offer.get());
				}
			}

			Collections.sort(offers, new Comparator<Offer>() {
				@Override
				public int compare(Offer u1, Offer u2) {
					return Double.compare(u1.getAmount(), u2.getAmount());
				}
			});

			return offers;
		} catch (CustomException e) {
			throw new CustomException(e.getMessage(), e.getErrorCode());
		} catch (Exception e) {
			throw new CustomException(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	public void processEqualAutoMatchedOffers(Offer originalOffer, double fulfilledAmount, List<Offer> offers)
			throws CustomException {
		try {

			List<Offer> transactionOffers = new ArrayList<>();
			Transaction transaction = createTransaction(transactionOffers);

			for (Offer offer : offers) {
				offer.setFullyFulfilled(true);
				offer.setTransactedAmount(offer.getAmount());
				offer.setStatus(Enum.OfferStatuses.fulfilled);
				offer.setFulfilledBy(transaction);
				transactionOffers.add(offerRepo.save(offer));
			}

			originalOffer.setFullyFulfilled(fulfilledAmount == originalOffer.getAmount());
			originalOffer.setTransactedAmount(fulfilledAmount);
			originalOffer.setStatus(Enum.OfferStatuses.fulfilled);
			originalOffer.setFulfilledBy(transaction);
			transactionOffers.add(offerRepo.save(originalOffer));

			transaction.setOffers(transactionOffers);
			transactionRepo.save(transaction);

		} catch (Exception e) {
			e.printStackTrace();
			throw new CustomException("Invalid Data", HttpStatus.BAD_REQUEST);
		}
	}

	private void adjustOffersBasedOnAmount(Offer offer, double offerAmount, double sumOfMatchedOffers,
			double requestingAmount, List<Offer> offers) throws CustomException {

		offer.setEditable(false);
		offerRepo.save(offer);

		for (Offer subOffer : offers) {
			subOffer.setEditable(false);
			offerRepo.save(subOffer);
		}

		Optional<Offer> countOffer = offerRepo.getById(offers.get(offers.size() - 1).getId());
		Offer counteredOffer = countOffer.get();

		double counterAmount = requestingAmount > sumOfMatchedOffers
				? (counteredOffer.getAmount() + requestingAmount - sumOfMatchedOffers)
				: (counteredOffer.getAmount() - requestingAmount - sumOfMatchedOffers);
		CounterOffer counterOffer = createCounterForAOffer(counteredOffer, counterAmount, offer.getPostedBy().getId());

		AutoMatchedOffer auto = new AutoMatchedOffer();
		auto.setOriginalOffer(offer);
		auto.setCounteredOffer(counteredOffer);
		List<Offer> splittedOffers = new ArrayList<>();
		for (int i = 1; i < offers.size(); i++) {
			splittedOffers.add(offers.get(i));
		}
		auto.setFullyFulfilledOffers(splittedOffers);
		auto.setCounter(counterOffer);
		autoMatchedOfferRepo.save(auto);
	}

	/*
	 * Approve a Counter Offer
	 */

	public ResponseEntity<Object> acceptCounterOffer(HttpServletRequest request, JsonNode body, long counterId) {
		try {
			Optional<CounterOffer> cOffer = counterOfferRepo.findById(counterId);
			if (cOffer.isEmpty())
				throw new CustomException("Counter Id not found", HttpStatus.NOT_FOUND);
			CounterOffer counterOffer = cOffer.get();
			Optional<AutoMatchedOffer> autoMatchedOffer = autoMatchedOfferRepo.getByCounter(counterOffer);
//			if
			
			
			return null;
		} catch (CustomException e) {
			e.printStackTrace();
			return new ResponseEntity<>(e.getMessage(), e.getErrorCode());
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("Invalid Data", HttpStatus.BAD_REQUEST);
		}
	}

}
