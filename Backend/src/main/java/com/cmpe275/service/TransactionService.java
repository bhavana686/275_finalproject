package com.cmpe275.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.TimeZone;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.cmpe275.Exception.CustomException;
import com.cmpe275.entity.AutoMatchedOffer;
import com.cmpe275.entity.CounterOffer;
import com.cmpe275.entity.Enum;
import com.cmpe275.entity.ExchangeCurrency;
import com.cmpe275.entity.Offer;
import com.cmpe275.entity.Transaction;
import com.cmpe275.entity.TransferRequest;
import com.cmpe275.entity.User;
import com.cmpe275.repo.AutoMatchedOfferRepo;
import com.cmpe275.repo.CounterOfferRepo;
import com.cmpe275.repo.ExchangeCurrencyRepo;
import com.cmpe275.repo.OfferRepo;
import com.cmpe275.repo.TransactionRepo;
import com.cmpe275.repo.TransferRequestRepo;
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

	@Autowired
	private TransferRequestRepo transferRequestRepo;

	@Autowired
	private ExchangeCurrencyRepo exchangeCurrencyRepo;

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

			List<Offer> offersList = new ArrayList<Offer>();
			offersList.add(originalOffer.get());
			offersList.add(newOffer);

			Transaction tran = new Transaction();
//			Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
//			Date date = calendar.getTime();
//			int day = calendar.get(Calendar.DATE);
//			int month = calendar.get(Calendar.MONTH) + 1;
//			int year = calendar.get(Calendar.YEAR);
//			int hour = calendar.get(Calendar.HOUR_OF_DAY);
//			int minute = calendar.get(Calendar.MINUTE);
//			int second = calendar.get(Calendar.SECOND);
			long timestamp = System.currentTimeMillis();
			tran.setExpiry(new Timestamp(timestamp + (10 * 60000)));
			Transaction transaction = transactionRepo.save(tran);
			List<TransferRequest> requests = createTransferRequests(offersList, transaction);
			transaction.setRequests(requests);
			transactionRepo.save(transaction);
			// Send Email to both
			// -------------------------------------------------------------------------------

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
			offer.setStatus(Enum.OfferStatuses.pending);
			offer.setEditable(false);
			offer.setDisplay(false);
			// Update this to handle dynamic rates
			offer.setExchangeRate(1 / originalOffer.getExchangeRate());
			offer.setUsePrevailingRate(originalOffer.isUsePrevailingRate());
			offer.setAmount(originalOffer.getExchangeRate() * originalOffer.getAmount());
			offer.setTransactedAmount(originalOffer.getExchangeRate() * originalOffer.getAmount());
			offer.setCounter(true);
//			offer.setFullyFulfilled(true);
			Optional<User> user = userRepo.getById(counterUserId);
			offer.setPostedBy(user.get());
			Offer savedNewOffer = offerRepo.save(offer);
			return savedNewOffer;
		} catch (Exception e) {
			e.printStackTrace();
			throw new CustomException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	public List<TransferRequest> createTransferRequests(List<Offer> offersList, Transaction transaction)
			throws CustomException {
		try {
			List<TransferRequest> req = new ArrayList<>();
			for (Offer offer : offersList) {
				TransferRequest transferRequest = new TransferRequest();
				transferRequest.setOffer(offer);
				transferRequest.setTransaction(transaction);
				transferRequest.setUser(offer.getPostedBy());
				long timestamp = System.currentTimeMillis();
				transferRequest.setExpiry(new Timestamp(timestamp + (10 * 60000)));
				transferRequest.setAmountAdjusted(offer.getAmount());
				transferRequest.setAmountRequired(offer.getAmount());
				// Create transfer request
				TransferRequest transferReq = transferRequestRepo.save(transferRequest);

				// added request to user
//				offer.getPostedBy().getTransferRequests().add(transferReq);
//				userRepo.save(offer.getPostedBy());
				User offerCreator = offer.getPostedBy();
				List<TransferRequest> userTransReqs = offerCreator.getTransferRequests() != null
						? offerCreator.getTransferRequests()
						: new ArrayList<TransferRequest>();
				userTransReqs.add(transferReq);
				offerCreator.setTransferRequests(userTransReqs);
				userRepo.save(offerCreator);

				// added request to offer
				List<TransferRequest> transReqs = offer.getTransferRequests() != null ? offer.getTransferRequests()
						: new ArrayList<TransferRequest>();
				transReqs.add(transferReq);
				offer.setTransferRequests(transReqs);
				offer.setStatus(Enum.OfferStatuses.pending);
				offer.setEditable(false);
				offer.setDisplay(false);
				offerRepo.save(offer);

				// add to list
				req.add(transferReq);
			}
			return req;
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
			double counterAmount = (double) body.get("amount").asDouble();
			Optional<Offer> sourceOffer = offerRepo.getById(offerId);
			if (sourceOffer.isEmpty())
				throw new CustomException("Offer Id Invalid", HttpStatus.NOT_FOUND);
			if (!sourceOffer.get().getStatus().equals(Enum.OfferStatuses.open))
				throw new CustomException("Offer already taken", HttpStatus.BAD_REQUEST);

			// Creating counter offer
			CounterOffer counter = createCounterForAOffer(sourceOffer.get(), counterAmount, counterUserId);

			// Creating dummy offer and creating a auto match
			Offer newOffer = createNewCounterOffer(sourceOffer.get(), counterUserId);
			AutoMatchedOffer autoOffer = new AutoMatchedOffer();
			autoOffer.setCounter(counter);
			autoOffer.setCounteredOffer(newOffer);
			autoOffer.setOriginalOffer(sourceOffer.get());
			autoOffer.setType(Enum.AutoMatchTypes.direct_counter);
			autoMatchedOfferRepo.save(autoOffer);

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
			Optional<User> counteredBy = userRepo.findById(counterUserId);
			counter.setCounteredBy(counteredBy.get());
			counter.setOriginalAmount(sourceOffer.getAmount());
			long timestamp = System.currentTimeMillis();
			counter.setExpiry(new Timestamp(timestamp + (10 * 60000)));
			counter.setCreatedAt(new Timestamp(timestamp));
			CounterOffer counterOffer = counterOfferRepo.save(counter);

			// Adding counter offers to user
			User originalOfferUser = sourceOffer.getPostedBy();
			List<CounterOffer> existingCounterOffers = originalOfferUser.getCounterOffers() != null
					? originalOfferUser.getCounterOffers()
					: new ArrayList<CounterOffer>();
			existingCounterOffers.add(counterOffer);
			originalOfferUser.setCounterOffers(existingCounterOffers);
			userRepo.save(originalOfferUser);

			// Adding Counter offer to source offer
			List<CounterOffer> existingCounterOffersOfOffer = sourceOffer.getCounterOffers() != null
					? sourceOffer.getCounterOffers()
					: new ArrayList<CounterOffer>();
			existingCounterOffersOfOffer.add(counterOffer);
			sourceOffer.setCounterOffers(existingCounterOffersOfOffer);

			// Setting offer to display none
			sourceOffer.setStatus(Enum.OfferStatuses.pending);
			sourceOffer.setEditable(false);
			sourceOffer.setDisplay(false);
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
	 * according to the matched offers amount
	 */

	public ResponseEntity<Object> processAutoMatchOffer(HttpServletRequest request, JsonNode body, long offerId) {
		try {
			// Amount comes in requesting offers currency
			long counteredUserId = (long) body.get("userId").asLong();
			double offerAmount = (double) body.get("offerAmount").asDouble();
			double sumOfMatchedOffers = (double) body.get("sumOfMatchedOffers").asDouble();
			double requestingAmount = (double) body.get("requestingAmount").asDouble();
			JsonNode offersNode = (JsonNode) body.get("offers");
			Optional<Offer> originalOffer = offerRepo.findById(offerId);
			originalOffer.get().setDisplay(false);

			List<Offer> offers = getOffersFromBody(offersNode);

			// Process all offers
			processEqualAutoMatchedOffers(originalOffer.get(), requestingAmount, offers, offerAmount,
					sumOfMatchedOffers, requestingAmount);

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

	public void processEqualAutoMatchedOffers(Offer originalOffer, double fulfilledAmount, List<Offer> offers,
			double offerAmount, double sumOfMatchedOffers, double requestingAmount) throws CustomException {
		try {
			// Creating a transaction
			Transaction tran = new Transaction();
			long timestamp = System.currentTimeMillis();
			tran.setExpiry(new Timestamp(timestamp + (10 * 60000)));
			Transaction transaction = transactionRepo.save(tran);

			double amountAdjusted = requestingAmount == offerAmount ? requestingAmount : sumOfMatchedOffers;
			List<TransferRequest> requests = createTransferRequests(offers, transaction);
			requests.add(createTransferRequestsForASingleOffer(originalOffer, transaction, amountAdjusted));
			transaction.setRequests(requests);
			transactionRepo.save(transaction);

//			List<TransferRequest> transactionOffers = new ArrayList<>();
//			Transaction transaction = new Transaction();

//
//			for (Offer offer : offers) {
//				offer.setFullyFulfilled(true);
//				offer.setTransactedAmount(offer.getAmount());
//				offer.setStatus(Enum.OfferStatuses.fulfilled);
//				offer.setFulfilledBy(transaction);
//				transactionOffers.add(offerRepo.save(offer));
//			}

//			originalOffer.setFullyFulfilled(fulfilledAmount == originalOffer.getAmount());
//			originalOffer.setTransactedAmount(fulfilledAmount);
//			originalOffer.setStatus(Enum.OfferStatuses.fulfilled);
//			originalOffer.setFulfilledBy(transaction);
//			transactionOffers.add(offerRepo.save(originalOffer));
//
//			transaction.setOffers(transactionOffers);
//			transactionRepo.save(transaction);

		} catch (Exception e) {
			e.printStackTrace();
			throw new CustomException("Invalid Data", HttpStatus.BAD_REQUEST);
		}
	}

	public TransferRequest createTransferRequestsForASingleOffer(Offer offer, Transaction transaction,
			double amountAdjusted) throws CustomException {
		try {
			TransferRequest transferRequest = new TransferRequest();
			transferRequest.setOffer(offer);
			transferRequest.setTransaction(transaction);
			transferRequest.setUser(offer.getPostedBy());
			long timestamp = System.currentTimeMillis();
			transferRequest.setExpiry(new Timestamp(timestamp + (10 * 60000)));
			transferRequest.setAmountAdjusted(amountAdjusted);
			transferRequest.setAmountRequired(offer.getAmount());
			// Create transfer request
			TransferRequest transferReq = transferRequestRepo.save(transferRequest);

			// added request to user
			User offerCreator = offer.getPostedBy();
			List<TransferRequest> userTransReqs = offerCreator.getTransferRequests() != null
					? offerCreator.getTransferRequests()
					: new ArrayList<TransferRequest>();
			userTransReqs.add(transferReq);
			offerCreator.setTransferRequests(userTransReqs);
			userRepo.save(offerCreator);

			// added request to offer
			List<TransferRequest> transReqs = offer.getTransferRequests() != null ? offer.getTransferRequests()
					: new ArrayList<TransferRequest>();
			transReqs.add(transferReq);
			offer.setTransferRequests(transReqs);
			offer.setStatus(Enum.OfferStatuses.pending);
			offer.setEditable(false);
			offer.setDisplay(false);
			offerRepo.save(offer);

			return transferReq;
		} catch (Exception e) {
			e.printStackTrace();
			throw new CustomException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/*
	 * Process an Offer where a user picks one of the auto match offers where the
	 * amount doesn't match with his requirement and decides to counter the matched
	 * offers amount
	 */

	public ResponseEntity<Object> processUnEqualAutoMatchOffer(HttpServletRequest request, JsonNode body,
			long offerId) {
		try {
			long counteredUserId = (long) body.get("userId").asLong();
			double offerAmount = (double) body.get("offerAmount").asDouble();
			double sumOfMatchedOffers = (double) body.get("sumOfMatchedOffers").asDouble();
			double adjustmentAmount = (double) body.get("adjustmentAmount").asDouble();
			JsonNode offersNode = (JsonNode) body.get("offers");
			Optional<Offer> originalOffer = offerRepo.findById(offerId);
			originalOffer.get().setDisplay(false);
			offerRepo.save(originalOffer.get());

			List<Offer> offers = getOffersFromBody(offersNode);

			// Adjust Offer
			adjustOffersBasedOnAmount(originalOffer.get(), offerAmount, sumOfMatchedOffers, adjustmentAmount, offers,
					counteredUserId);

			return new ResponseEntity<>("Success", HttpStatus.OK);
		} catch (CustomException e) {
			e.printStackTrace();
			return new ResponseEntity<>(e.getMessage(), e.getErrorCode());
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("Invalid Data", HttpStatus.BAD_REQUEST);
		}
	}

	private void adjustOffersBasedOnAmount(Offer offer, double offerAmount, double sumOfMatchedOffers,
			double adjustmentAmount, List<Offer> offers, long counteredUserId) throws CustomException {

		CounterOffer counterOffer = createCounterForAOffer(offer, adjustmentAmount, counteredUserId);

		AutoMatchedOffer autoOffer = new AutoMatchedOffer();
		autoOffer.setCounter(counterOffer);
		autoOffer.setCounteredOffer(offers.get(offers.size() - 1));
		autoOffer.setOriginalOffer(offer);

		if (offers.size() == 1) {
			autoOffer.setType(Enum.AutoMatchTypes.single_counter);
		} else if (offers.size() == 2) {
			autoOffer.setFullyFulfilledOffer(offers.get(0));
			autoOffer.setType(Enum.AutoMatchTypes.dual_counter);
		}

		autoMatchedOfferRepo.save(autoOffer);

		for (Offer subOffer : offers) {
			subOffer.setEditable(false);
			subOffer.setDisplay(false);
			subOffer.setStatus(Enum.OfferStatuses.pending);
			offerRepo.save(subOffer);
		}

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
			return null;
		} catch (CustomException e) {
			e.printStackTrace();
			return new ResponseEntity<>(e.getMessage(), e.getErrorCode());
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("Invalid Data", HttpStatus.BAD_REQUEST);
		}
	}

	/*
	 * Auto Match Offers
	 */

	public ResponseEntity<Object> autoMatchOffers(HttpServletRequest request, long offerId) {
		try {
			Optional<Offer> autoOffer = offerRepo.findById(offerId);
			if (autoOffer.isEmpty())
				throw new CustomException("Offer Id Invalid", HttpStatus.NOT_FOUND);
			Offer offer = autoOffer.get();
			Enum.Currency currency = offer.getDestinationCurrency();
			double amount = offer.getAmount();
			System.out.println(offer.getAmount());
			System.out.println(offer.getExchangeRate());
			if (offer.isUsePrevailingRate()) {
				Optional<ExchangeCurrency> rate = exchangeCurrencyRepo.findBySourceCurrencyAndTargetCurrency(
						offer.getSourceCurrency(), offer.getDestinationCurrency());
				amount = amount * rate.get().getExchangeRate();
			} else {
				amount = amount * offer.getExchangeRate();
			}
			Optional<List<Offer>> offers = offerRepo.getByAmountBetween(Enum.OfferStatuses.open, false, true,
					amount * 0.9, amount * 1.1);
			System.out.println(offers.isEmpty());
			System.out.println(amount);

			List<List<Offer>> matches = new ArrayList<>();
			if (offers.isEmpty()) {
				return new ResponseEntity<>("No Matching Offers Found", HttpStatus.NOT_FOUND);
			} else {
				List<Offer> matchingOffers = offers.get();
				System.out.println(matchingOffers.size());
//				for (Offer o : matchingOffers) {
//					System.out.println(o.getId());
//					System.out.println(o.getAmount());
//				}
			}
			List<List<Offer>> splitMatch1 = getSplitMatchOffers(offerId, amount);
			return null;
		} catch (CustomException e) {
			e.printStackTrace();
			return new ResponseEntity<>(e.getMessage(), e.getErrorCode());
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("Invalid Data", HttpStatus.BAD_REQUEST);
		}
	}

	public List<List<Offer>> getSplitMatchOffers(long offerId, double amount) throws CustomException {
		try {
			List<List<Offer>> offers = new ArrayList<>();
			Optional<List<Offer>> fetched = offerRepo.getSplitMatches(Enum.OfferStatuses.open, false, true, amount);
			List<Offer> fetchedOffers = fetched.get();
			
			
			return offers;
		} catch (Exception e) {
			e.printStackTrace();
			throw new CustomException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
