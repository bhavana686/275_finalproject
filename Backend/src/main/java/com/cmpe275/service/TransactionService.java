package com.cmpe275.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.TimeZone;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.cmpe275.Exception.CustomException;
import com.cmpe275.entity.AutoMatchedOffer;
import com.cmpe275.entity.CounterOffer;
import com.cmpe275.entity.Enum;
import com.cmpe275.entity.Enum.CounterOfferStatuses;
import com.cmpe275.entity.Enum.OfferStatuses;
import com.cmpe275.entity.ExchangeCurrency;
import com.cmpe275.entity.Offer;
import com.cmpe275.entity.Transaction;
import com.cmpe275.entity.TransferRequest;
import com.cmpe275.entity.User;
import com.cmpe275.helper.AutoMatchEntity;
import com.cmpe275.helper.AutoMatchRecommendationOffer;
import com.cmpe275.helper.ResponseBuilder;
import com.cmpe275.repo.AutoMatchedOfferRepo;
import com.cmpe275.repo.CounterOfferRepo;
import com.cmpe275.repo.ExchangeCurrencyRepo;
import com.cmpe275.repo.OfferRepo;
import com.cmpe275.repo.TransactionRepo;
import com.cmpe275.repo.TransferRequestRepo;
import com.cmpe275.repo.UserRepo;
import com.fasterxml.jackson.databind.JsonNode;

import net.bytebuddy.description.modifier.EnumerationState;

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

	@Autowired
	private ResponseBuilder responseBuilder;

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

			Offer newOffer = createNewCounterOffer(originalOffer.get(), counterUserId, false, 0);

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

	public Offer createNewCounterOffer(Offer originalOffer, long counterUserId, boolean adjust, double amount) throws CustomException {
		try {
			System.out.println("Entering in ");
			Offer offer = new Offer();
			offer.setDestinationCountry(originalOffer.getSourceCountry());
			offer.setDestinationCurrency(originalOffer.getSourceCurrency());
			offer.setSourceCountry(originalOffer.getDestinationCountry());
			offer.setSourceCurrency(originalOffer.getDestinationCurrency());
			offer.setStatus(Enum.OfferStatuses.pending);
			offer.setEditable(false);
			offer.setDisplay(false);
			offer.setExpiry(originalOffer.getExpiry());
			// ----------------------------------------------------------------------------------------------------------------------------
			// Update this to handle dynamic rates
			Optional<ExchangeCurrency> rate = exchangeCurrencyRepo.findBySourceCurrencyAndTargetCurrency(
					originalOffer.getSourceCurrency(), originalOffer.getDestinationCurrency());
			System.out.println(originalOffer.getSourceCurrency() + " " + originalOffer.getDestinationCurrency());
			System.out.println(rate.isEmpty());
			double exchangeRate = rate.get().getExchangeRate();
			if(adjust) {
				if (originalOffer.isUsePrevailingRate()) {
					offer.setUsePrevailingRate(true);
					offer.setAmount(exchangeRate * amount);
				} else {
					offer.setUsePrevailingRate(false);
					offer.setExchangeRate(1 / originalOffer.getExchangeRate());
					offer.setAmount(originalOffer.getExchangeRate() * amount);
				}
			} else {
				if (originalOffer.isUsePrevailingRate()) {
					offer.setUsePrevailingRate(true);
					offer.setAmount(exchangeRate * originalOffer.getAmount());
				} else {
					offer.setUsePrevailingRate(false);
					offer.setExchangeRate(1 / originalOffer.getExchangeRate());
					offer.setAmount(originalOffer.getExchangeRate() * originalOffer.getAmount());
				}
			}
//			offer.setUsePrevailingRate(originalOffer.isUsePrevailingRate());
//			offer.setAmount(originalOffer.getExchangeRate() * originalOffer.getAmount());
//			offer.setTransactedAmount(originalOffer.getExchangeRate() * originalOffer.getAmount());
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
				offer.setStatus(Enum.OfferStatuses.intransaction);
				offer.setEditable(false);
//				offer.setDisplay(false);
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
			System.out.println("Countering " + offerId + "with amount " + counterAmount + "by user " + counterUserId);
			Optional<Offer> sourceOffer = offerRepo.getById(offerId);
			if (sourceOffer.isEmpty())
				throw new CustomException("Offer Id Invalid", HttpStatus.NOT_FOUND);
			if (!sourceOffer.get().getStatus().equals(Enum.OfferStatuses.open))
				throw new CustomException("Offer already taken", HttpStatus.BAD_REQUEST);

			// Creating counter offer
			CounterOffer counter = createCounterForAOffer(sourceOffer.get(), counterAmount, counterUserId);

			// Creating dummy offer and creating a auto match
			Offer newOffer = createNewCounterOffer(sourceOffer.get(), counterUserId, true, counterAmount);
			AutoMatchedOffer autoOffer = new AutoMatchedOffer();
			autoOffer.setCounter(counter);
			autoOffer.setCounteredOffer(sourceOffer.get());
			autoOffer.setOriginalOffer(newOffer);
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
			if (((sourceOffer.getAmount() * 1.1) < counterAmount)
					|| ((sourceOffer.getAmount() * 0.9) > counterAmount)) {
				throw new CustomException("Counter Amount Invalid", HttpStatus.BAD_REQUEST);
			}
			CounterOffer counter = new CounterOffer();
			counter.setCounterAmount(counterAmount);
			counter.setCounteredAgainst(sourceOffer);
			Optional<User> counteredBy = userRepo.findById(counterUserId);
			counter.setCounteredBy(counteredBy.get());
			counter.setOriginalAmount(sourceOffer.getAmount());
			long timestamp = System.currentTimeMillis();
			counter.setExpiry(new Timestamp(timestamp + (5 * 60000)));
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
//			sourceOffer.setStatus(Enum.OfferStatuses.pending);
			sourceOffer.setEditable(false);
//			sourceOffer.setDisplay(false);
			offerRepo.save(sourceOffer);
			return counterOffer;
		} catch (CustomException e) {
			e.printStackTrace();
			throw new CustomException(e.getMessage(), e.getErrorCode());
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
					long offerId = (long) off.get("id").asLong();
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
			e.printStackTrace();
			throw new CustomException(e.getMessage(), e.getErrorCode());
		} catch (Exception e) {
			e.printStackTrace();
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
			offer.setStatus(Enum.OfferStatuses.intransaction);
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
//			originalOffer.get().setDisplay(false);
//			offerRepo.save(originalOffer.get());

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

		try {
			CounterOffer counterOffer = createCounterForAOffer(offers.get(offers.size() - 1), adjustmentAmount,
					counteredUserId);

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

			offer.setEditable(false);
			offer.setDisplay(false);
			offer.setStatus(Enum.OfferStatuses.counterMade);
			offerRepo.save(offer);

			for (Offer subOffer : offers) {
				subOffer.setEditable(false);
//			subOffer.setDisplay(false);
//			subOffer.setStatus(Enum.OfferStatuses.pending);
				offerRepo.save(subOffer);
			}
		} catch (CustomException e) {
			e.printStackTrace();
			throw new CustomException(e.getMessage(), e.getErrorCode());
		} catch (Exception e) {
			e.printStackTrace();
			throw new CustomException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
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

			// Single Matches
			Optional<List<Offer>> offers = offerRepo.getByAmountBetween(Enum.OfferStatuses.open, false, true,
					amount * 0.9, amount * 1.1, offer.getDestinationCurrency(), offer.getSourceCurrency());

			List<AutoMatchEntity> singleMatches = new ArrayList<AutoMatchEntity>();

			if (offers.isEmpty()) {
				return new ResponseEntity<>("No Matching Offers Found", HttpStatus.NOT_FOUND);
			} else {
				List<Offer> matchingOffers = offers.get();
				System.out.println("Single Matches Found " + matchingOffers.size());
				for (Offer o : matchingOffers) {
//					System.out.println("Offer Id: "+ o.getId() + " Amount: "+o.getAmount());
					List<AutoMatchRecommendationOffer> tmp = new ArrayList<>();
					tmp.add(convertOfferToAutoMatchRecommendationOffer(o));
					AutoMatchEntity autoMatch = new AutoMatchEntity();
					autoMatch.setDifference(Math.abs(o.getAmount() - amount));
					autoMatch.setSum(o.getAmount());
					autoMatch.setType(Enum.AutoMatchedOffers.aplha);
					autoMatch.setOffers(tmp);
					autoMatch.setSupportCounter(o.isAllowCounterOffers());
					singleMatches.add(autoMatch);
				}
			}

			Collections.sort(singleMatches, new Comparator<AutoMatchEntity>() {
				@Override
				public int compare(AutoMatchEntity u1, AutoMatchEntity u2) {
					return Double.compare(u1.getDifference(), u2.getDifference());
				}
			});

			List<AutoMatchEntity> splitMatch1 = getSplitMatchOffersCase1(offerId, amount, offer);
			System.out.println("Dual Matched Case 1: " + splitMatch1.size());

			Collections.sort(splitMatch1, new Comparator<AutoMatchEntity>() {
				@Override
				public int compare(AutoMatchEntity u1, AutoMatchEntity u2) {
					return Double.compare(u1.getDifference(), u2.getDifference());
				}
			});

//			List<AutoMatchEntity> splitMatch2 = getSplitMatchOffersCase2(offerId, amount, offer);
//			System.out.println("Dual Matched Case 2: " + splitMatch2.size());

//			List<AutoMatchEntity> dualMatches = Stream.of(splitMatch1, splitMatch2).flatMap(Collection::stream)
//					.collect(Collectors.toList());
//			Collections.sort(dualMatches, new Comparator<AutoMatchEntity>() {
//				@Override
//				public int compare(AutoMatchEntity u1, AutoMatchEntity u2) {
//					return Double.compare(u1.getDifference(), u2.getDifference());
//				}
//			});

			return new ResponseEntity<>(
					Stream.of(singleMatches, splitMatch1).flatMap(Collection::stream).collect(Collectors.toList()),
					HttpStatus.OK);
		} catch (CustomException e) {
			e.printStackTrace();
			return new ResponseEntity<>(e.getMessage(), e.getErrorCode());
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("Invalid Data", HttpStatus.BAD_REQUEST);
		}
	}

	public List<AutoMatchEntity> getSplitMatchOffersCase1(long offerId, double amount, Offer originalOffer)
			throws CustomException {
		try {
			List<AutoMatchEntity> offers = new ArrayList<>();
			Optional<List<Offer>> fetched = offerRepo.getSplitMatches(Enum.OfferStatuses.open, false, true, amount,
					true, originalOffer.getDestinationCurrency(), originalOffer.getSourceCurrency());
			List<Offer> fetchedOffers = fetched.get();
			Collections.sort(fetchedOffers, new Comparator<Offer>() {
				@Override
				public int compare(Offer u1, Offer u2) {
					return Double.compare(u1.getAmount(), u2.getAmount());
				}
			});
			for (int i = 0; i < fetchedOffers.size() - 1; i++) {
				Offer first = fetchedOffers.get(i);
				for (int j = i + 1; j < fetchedOffers.size(); j++) {
					Offer second = fetchedOffers.get(j);
					if (((first.getAmount() + second.getAmount()) > (amount * 0.9))
							&& ((first.getAmount() + second.getAmount()) < (amount * 1.1))) {
						List<AutoMatchRecommendationOffer> combOffer = new ArrayList<>();
						combOffer.add(convertOfferToAutoMatchRecommendationOffer(first));
						combOffer.add(convertOfferToAutoMatchRecommendationOffer(second));
						AutoMatchEntity autoMatch = new AutoMatchEntity();
						autoMatch.setDifference(Math.abs((first.getAmount() + second.getAmount()) - amount));
						autoMatch.setSum(first.getAmount() + second.getAmount());
						autoMatch.setType(Enum.AutoMatchedOffers.beta);
						autoMatch.setOffers(combOffer);
						autoMatch.setSupportCounter(first.isAllowCounterOffers() && second.isAllowCounterOffers());
						offers.add(autoMatch);
//						System.out.println("Dual Match-----------------------");
//						System.out.println("Offer Id: "+ first.getId() + " Amount: "+first.getAmount());
//						System.out.println("Offer Id: "+ second.getId() + " Amount: "+second.getAmount());
					}
					if ((first.getAmount() + second.getAmount()) > (amount * 1.1)) {
						break;
					}
				}
			}
			return offers;
		} catch (Exception e) {
			e.printStackTrace();
			throw new CustomException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	public List<AutoMatchEntity> getSplitMatchOffersCase2(long offerId, double amount, Offer originalOffer)
			throws CustomException {
		try {
			List<AutoMatchEntity> offers = new ArrayList<>();
			Optional<List<Offer>> fetched = offerRepo.getSplitMatches(Enum.OfferStatuses.open, false, true, amount,
					true, originalOffer.getDestinationCurrency(), originalOffer.getSourceCurrency());
			List<Offer> fetchedOffers = fetched.get();
			Collections.sort(fetchedOffers, new Comparator<Offer>() {
				@Override
				public int compare(Offer u1, Offer u2) {
					return Double.compare(u2.getAmount(), u1.getAmount());
				}
			});
			for (int i = 0; i < fetchedOffers.size(); i++) {
				Offer first = fetchedOffers.get(i);
				for (int j = i + 1; j < fetchedOffers.size(); j++) {
					Offer second = fetchedOffers.get(j);
					if (((first.getAmount() - second.getAmount()) > (amount * 0.9))
							&& ((first.getAmount() - second.getAmount()) < (amount * 1.1))) {
						List<AutoMatchRecommendationOffer> combOffer = new ArrayList<>();
						combOffer.add(convertOfferToAutoMatchRecommendationOffer(first));
						combOffer.add(convertOfferToAutoMatchRecommendationOffer(second));
						AutoMatchEntity autoMatch = new AutoMatchEntity();
						autoMatch.setDifference(Math.abs((first.getAmount() - second.getAmount()) - amount));
						autoMatch.setSum(first.getAmount() + second.getAmount());
						autoMatch.setType(Enum.AutoMatchedOffers.gamma);
						autoMatch.setOffers(combOffer);
						offers.add(autoMatch);
//						System.out.println("Dual Match Case 2 -----------------------");
//						System.out.println("Offer Id: "+ first.getId() + " Amount: "+first.getAmount());
//						System.out.println("Offer Id: "+ second.getId() + " Amount: "+second.getAmount());
					}
				}
			}
			return offers;
		} catch (Exception e) {
			e.printStackTrace();
			throw new CustomException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	public AutoMatchRecommendationOffer convertOfferToAutoMatchRecommendationOffer(Offer offer) {
		AutoMatchRecommendationOffer o = new AutoMatchRecommendationOffer();
		o.setId(offer.getId());
		o.setUsername(offer.getPostedBy().getUsername());
		o.setNickname(offer.getPostedBy().getNickname());
		o.setSourceCountry(offer.getSourceCountry());
		o.setSourceCurrency(offer.getSourceCurrency());
		o.setSourceAmount(offer.getAmount());
		o.setDestinationCountry(offer.getDestinationCountry());
		o.setDestinationCurrency(offer.getDestinationCurrency());
		o.setSupportCounter(offer.isAllowCounterOffers());
		o.setExpiry(offer.getExpiry());
		if (offer.isUsePrevailingRate()) {
			Optional<ExchangeCurrency> rate = exchangeCurrencyRepo
					.findBySourceCurrencyAndTargetCurrency(offer.getSourceCurrency(), offer.getDestinationCurrency());
			o.setDestinationAmount(offer.getAmount() * rate.get().getExchangeRate());
			o.setExchangeRate(rate.get().getExchangeRate());
		} else {
			o.setDestinationAmount(offer.getAmount() * offer.getExchangeRate());
			o.setExchangeRate(offer.getExchangeRate());
		}
		return o;
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

			if (counterOffer.getStatus() != Enum.CounterOfferStatuses.open)
				throw new CustomException("Counter Not Open", HttpStatus.BAD_REQUEST);

			long timestamp = System.currentTimeMillis();
			if (counterOffer.getExpiry().getTime() < timestamp) {
				counterOffer.setStatus(Enum.CounterOfferStatuses.expired);
				counterOfferRepo.save(counterOffer);
				throw new CustomException("Counter Expired", HttpStatus.BAD_REQUEST);
			}

			if (counterOffer.getCounteredAgainst().getStatus() == Enum.OfferStatuses.fulfilled
					|| counterOffer.getCounteredAgainst().getStatus() == Enum.OfferStatuses.expired
					|| counterOffer.getCounteredAgainst().getStatus() == Enum.OfferStatuses.intransaction)
				throw new CustomException("Offer Not Open", HttpStatus.BAD_REQUEST);

			Optional<AutoMatchedOffer> autoMatchedOffer = autoMatchedOfferRepo.getByCounter(counterOffer);
			if (autoMatchedOffer.isEmpty())
				throw new CustomException("Auto Match Not found", HttpStatus.NOT_FOUND);

			List<Offer> offers = new ArrayList<>();

			validateOfferIsValid(autoMatchedOffer.get().getOriginalOffer());
			offers.add(autoMatchedOffer.get().getOriginalOffer());
//			offers.add(autoMatchedOffer.get().getCounteredOffer());
			if (autoMatchedOffer.get().getType() == Enum.AutoMatchTypes.direct_counter) {
				validateOfferIsValid(autoMatchedOffer.get().getCounteredOffer());
			} else if (autoMatchedOffer.get().getType() == Enum.AutoMatchTypes.single_counter) {
				validateOfferIsValid(autoMatchedOffer.get().getCounteredOffer());
			} else {
				validateOfferIsValid(autoMatchedOffer.get().getCounteredOffer());
				validateOfferIsValid(autoMatchedOffer.get().getFullyFulfilledOffer());
				offers.add(autoMatchedOffer.get().getFullyFulfilledOffer());
			}

			counterOffer.setStatus(Enum.CounterOfferStatuses.accepted);
			counterOfferRepo.save(counterOffer);

			Transaction tran = new Transaction();
			tran.setExpiry(new Timestamp(timestamp + (10 * 60000)));
			Transaction transaction = transactionRepo.save(tran);

			List<TransferRequest> requests = createTransferRequests(offers, transaction);
			requests.add(createTransferRequestsForASingleOffer(autoMatchedOffer.get().getCounteredOffer(), transaction, counterOffer.getCounterAmount()));
			transaction.setRequests(requests);
			transactionRepo.save(transaction);

			return new ResponseEntity<>("Success", HttpStatus.OK);
		} catch (CustomException e) {
			e.printStackTrace();
			return new ResponseEntity<>(e.getMessage(), e.getErrorCode());
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("Invalid Data", HttpStatus.BAD_REQUEST);
		}
	}

	public void validateOfferIsValid(Offer offer) throws CustomException {
		try {
			System.out.println("Original Offer: " + offer.getId());
			if (offer.getStatus() == Enum.OfferStatuses.fulfilled || offer.getStatus() == Enum.OfferStatuses.expired)
				throw new CustomException(offer.getId() + " Offer Not Valid Anymore", HttpStatus.BAD_REQUEST);
			System.out.println("Original Offer: " + offer.getId());
			// Check if offer is expired
			long timestamp = System.currentTimeMillis();
			System.out.println(offer.getExpiry());
			System.out.println("Original Offer: " + offer.getId());
			if (offer.getExpiry().getTime() < timestamp) {
				System.out.println("Original Offer: " + offer.getId());
				offer.setStatus(Enum.OfferStatuses.expired);
				offerRepo.save(offer);
				System.out.println("Original Offer: " + offer.getId());
				throw new CustomException(offer.getId() + " Offer Expired", HttpStatus.BAD_REQUEST);
			}

		} catch (CustomException e) {
			e.printStackTrace();
			throw new CustomException(e.getMessage(), e.getErrorCode());
		} catch (Exception e) {
			e.printStackTrace();
			throw new CustomException("Invalid Offer State", HttpStatus.BAD_REQUEST);
		}
	}

	/*
	 * Reject a Counter Offer
	 */

	public ResponseEntity<Object> declineCounterOffer(HttpServletRequest request, JsonNode body, long counterId) {
		try {
			Optional<CounterOffer> cOffer = counterOfferRepo.findById(counterId);
			if (cOffer.isEmpty())
				throw new CustomException("Counter Id not found", HttpStatus.NOT_FOUND);
			CounterOffer counterOffer = cOffer.get();

			if (counterOffer.getStatus() != Enum.CounterOfferStatuses.open)
				throw new CustomException("Counter Not Open", HttpStatus.BAD_REQUEST);

			Optional<AutoMatchedOffer> autoMatchedOffer = autoMatchedOfferRepo.getByCounter(counterOffer);
			if (autoMatchedOffer.isEmpty())
				throw new CustomException("Auto Match Not found", HttpStatus.NOT_FOUND);

			AutoMatchedOffer autoMatchOffer = autoMatchedOffer.get();
			autoMatchOffer.setStatus(Enum.AutoMatchOffersState.declined);
			autoMatchedOfferRepo.save(autoMatchOffer);
			
			counterOffer.setStatus(Enum.CounterOfferStatuses.declined);
			counterOfferRepo.save(counterOffer);

			// Check if original Offer got expired

			long timestamp = System.currentTimeMillis();
			if (autoMatchOffer.getOriginalOffer().getExpiry().getTime() < timestamp) {
				Offer orgOffer = autoMatchOffer.getOriginalOffer();
				orgOffer.setStatus(Enum.OfferStatuses.expired);
				offerRepo.save(orgOffer);

			} else {
				Offer orgOffer = autoMatchOffer.getOriginalOffer();
				if(!orgOffer.isCounter()) {
					orgOffer.setStatus(Enum.OfferStatuses.open);
					offerRepo.save(orgOffer);
				}
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

	/*
	 * Accept a Transfer Request
	 */

	public ResponseEntity<Object> acceptTransferRequest(HttpServletRequest request, JsonNode body, long offerId,
			long requestId) {
		try {
			Optional<TransferRequest> transRequest = transferRequestRepo.findById(requestId);
			if (transRequest.isEmpty())
				throw new CustomException("Request Id invalid", HttpStatus.NOT_FOUND);
			TransferRequest transferRequest = transRequest.get();

			long timestamp = System.currentTimeMillis();
			Transaction transaction = transferRequest.getTransaction();

			List<TransferRequest> requests = transaction.getRequests();
			validateTransferRequestOfferIsValid(requests);

			if (transferRequest.getStatus() == Enum.CounterOfferStatuses.expired
					|| transferRequest.getExpiry().getTime() < timestamp) {
				transferRequest.setStatus(Enum.CounterOfferStatuses.expired);
				transferRequestRepo.save(transferRequest);
				transaction.setStatus(Enum.CounterOfferStatuses.expired);
				// Function to move all offers to open when expired
				// Close all other transfer Requests
				moveOffersToOpen(requests, Enum.OfferStatuses.open);
				throw new CustomException("Request Expired", HttpStatus.BAD_REQUEST);
			}
			
			transferRequest.setStatus(Enum.CounterOfferStatuses.accepted);
			transferRequestRepo.save(transferRequest);
			
			if (isAllRequestsAccepted(requests)) {
				System.out.println("All requests accepted. Fulfilling Transaction");
				moveOffersToFulfilled(requests, Enum.OfferStatuses.fulfilled, transaction);
				double totalAmount = 0;
				for (TransferRequest trequest : requests) {
					Offer toffer = trequest.getOffer();
					totalAmount += toffer.getTransactedAmountInUSD();
				}
				transaction.setAmount(totalAmount);
				transaction.setStatus(Enum.CounterOfferStatuses.accepted);
				transactionRepo.save(transaction);
			} 
//			else {
//				transferRequest.setStatus(Enum.CounterOfferStatuses.accepted);
//				transferRequestRepo.save(transferRequest);
//			}
			return new ResponseEntity<>("Success", HttpStatus.OK);
		} catch (CustomException e) {
			e.printStackTrace();
			return new ResponseEntity<>(e.getMessage(), e.getErrorCode());
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("Invalid Data", HttpStatus.BAD_REQUEST);
		}
	}

	public boolean isAllRequestsAccepted(List<TransferRequest> requests) throws CustomException {
		try {
			boolean accepted = true;
			for (TransferRequest request : requests) {
				if (request.getStatus() != Enum.CounterOfferStatuses.accepted) {
					accepted = false;
					break;
				}
			}
			System.out.println("All Accepted the Offer: "+ accepted);
			return accepted;
		} catch (Exception e) {
			e.printStackTrace();
			throw new CustomException("Invalid Offer State", HttpStatus.BAD_REQUEST);
		}
	}

	public void validateTransferRequestOfferIsValid(List<TransferRequest> requests) throws CustomException {
		try {
			for (TransferRequest request : requests) {
				Offer offer = request.getOffer();
				if (offer.getStatus() == Enum.OfferStatuses.fulfilled
						|| offer.getStatus() == Enum.OfferStatuses.expired)
					throw new CustomException(offer.getId() + " Offer Not Valid Anymore", HttpStatus.BAD_REQUEST);

				// Check if offer is expired
				long timestamp = System.currentTimeMillis();
				if (offer.getExpiry().getTime() < timestamp) {
					offer.setStatus(Enum.OfferStatuses.expired);
					offerRepo.save(offer);
					throw new CustomException(offer.getId() + " Offer Expired", HttpStatus.BAD_REQUEST);
				}
			}
		} catch (CustomException e) {
			e.printStackTrace();
			throw new CustomException(e.getMessage(), e.getErrorCode());
		} catch (Exception e) {
			e.printStackTrace();
			throw new CustomException("Invalid Offer State", HttpStatus.BAD_REQUEST);
		}
	}

	public void moveOffersToFulfilled(List<TransferRequest> requests, Enum.OfferStatuses status,
			Transaction transaction) throws CustomException {
		try {
			for (TransferRequest request : requests) {
				Offer offer = request.getOffer();
				if (offer.getStatus() != Enum.OfferStatuses.intransaction)
					throw new CustomException("Offer Status Invalid", HttpStatus.BAD_REQUEST);
				offer.setStatus(Enum.OfferStatuses.fulfilled);
				offer.setFulfilledBy(transaction);
				offer.setTransactedAmount(request.getAmountAdjusted());
				offer.setFullyFulfilled(request.getAmountRequired() == request.getAmountAdjusted());
				if (offer.isUsePrevailingRate()) {
					offer.setExchangedRate(
							responseBuilder.getExchangeRate(offer.getSourceCurrency(), offer.getDestinationCurrency()));
				} else {
					offer.setExchangedRate(offer.getExchangeRate());
				}
				offer.setTransactedAmountInUSD(offer.getSourceCurrency() == Enum.Currency.USD
						? request.getAmountAdjusted()
						: request.getAmountAdjusted()
								* responseBuilder.getExchangeRate(offer.getSourceCurrency(), Enum.Currency.USD));
				offer.setDisplay(true);
				offer.setLastUpdated(new Timestamp(System.currentTimeMillis()));
				offerRepo.save(offer);

				request.setStatus(Enum.CounterOfferStatuses.accepted);
				transferRequestRepo.save(request);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new CustomException("Invalid Offer State", HttpStatus.BAD_REQUEST);
		}
	}

	/*
	 * Decline a Transfer Request
	 */

	public ResponseEntity<Object> declineTransferRequest(HttpServletRequest request, JsonNode body, long offerId,
			long requestId) {
		try {
			Optional<TransferRequest> transRequest = transferRequestRepo.findById(requestId);
			if (transRequest.isEmpty())
				throw new CustomException("Request Id invalid", HttpStatus.NOT_FOUND);

			TransferRequest transferRequest = transRequest.get();

			long timestamp = System.currentTimeMillis();
			Transaction transaction = transferRequest.getTransaction();

			List<TransferRequest> requests = transaction.getRequests();
			validateTransferRequestOfferIsValid(requests);

			if (transferRequest.getStatus() == Enum.CounterOfferStatuses.expired
					|| transferRequest.getExpiry().getTime() < timestamp) {
				transferRequest.setStatus(Enum.CounterOfferStatuses.expired);
				transferRequestRepo.save(transferRequest);
				transaction.setStatus(Enum.CounterOfferStatuses.expired);
				transactionRepo.save(transaction);
				throw new CustomException("Request Expired", HttpStatus.BAD_REQUEST);
			}

			moveOffersToOpen(requests, Enum.OfferStatuses.open);

			return new ResponseEntity<>("Success", HttpStatus.OK);
		} catch (CustomException e) {
			e.printStackTrace();
			return new ResponseEntity<>(e.getMessage(), e.getErrorCode());
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("Invalid Data", HttpStatus.BAD_REQUEST);
		}
	}

	private void moveOffersToOpen(List<TransferRequest> requests, OfferStatuses open) throws CustomException {
		try {
			for (TransferRequest request : requests) {
				Offer offer = request.getOffer();
				offer.setStatus(open);
				offer.setDisplay(true);
				offerRepo.save(offer);

				request.setStatus(Enum.CounterOfferStatuses.declined);
				transferRequestRepo.save(request);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new CustomException("Invalid Offer State", HttpStatus.BAD_REQUEST);
		}
	}
}
