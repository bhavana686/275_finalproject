package com.cmpe275.service;

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
import com.cmpe275.entity.Offer;
import com.cmpe275.entity.TransferRequest;
import com.cmpe275.entity.User;
import com.cmpe275.helper.ResponseBuilder;
import com.cmpe275.models.CounterOfferShallowForm;
import com.cmpe275.models.TransferRequestShallowForm;
import com.cmpe275.repo.AutoMatchedOfferRepo;
import com.cmpe275.repo.CounterOfferRepo;
import com.cmpe275.repo.ExchangeCurrencyRepo;
import com.cmpe275.repo.OfferRepo;
import com.cmpe275.repo.TransactionRepo;
import com.cmpe275.repo.TransferRequestRepo;
import com.cmpe275.repo.UserRepo;

@Service
public class OfferResponseService {

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

	public ResponseEntity<Object> fetchOfferDeepForm(HttpServletRequest request, long offerId) {
		try {
			Optional<Offer> offer = offerRepo.findById(offerId);
			if (offer.isEmpty())
				throw new CustomException("Offer id Invalid", HttpStatus.NOT_FOUND);
			return new ResponseEntity<>(responseBuilder.buildOfferDeepForm(offer.get()), HttpStatus.OK);
		} catch (CustomException e) {
			e.printStackTrace();
			return new ResponseEntity<>(e.getMessage(), e.getErrorCode());
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("Invalid Data", HttpStatus.BAD_REQUEST);
		}
	}

	public ResponseEntity<Object> fetchCounterOffers(HttpServletRequest request, long userId) {
		try {
			Optional<User> user = userRepo.findById(userId);
			if (user.isEmpty())
				throw new CustomException("User id Invalid", HttpStatus.NOT_FOUND);
			List<CounterOfferShallowForm> shallow = new ArrayList<CounterOfferShallowForm>();
			if (user.get().getCounterOffers() != null) {
				for (CounterOffer c : user.get().getCounterOffers()) {
					shallow.add(responseBuilder.buildCounterOfferShallowForm(c));
				}
			}
			return new ResponseEntity<>(shallow, HttpStatus.OK);
		} catch (CustomException e) {
			e.printStackTrace();
			return new ResponseEntity<>(e.getMessage(), e.getErrorCode());
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("Invalid Data", HttpStatus.BAD_REQUEST);
		}
	}

	public ResponseEntity<Object> fetchTransferRequests(HttpServletRequest request, long userId) {
		try {
			Optional<User> user = userRepo.findById(userId);
			if (user.isEmpty())
				throw new CustomException("User id Invalid", HttpStatus.NOT_FOUND);
			List<TransferRequestShallowForm> shallow = new ArrayList<TransferRequestShallowForm>();
			if (user.get().getTransferRequests() != null) {
				for (TransferRequest t : user.get().getTransferRequests()) {
					shallow.add(responseBuilder.buildTransferRequestShallowForm(t));
				}
			}
			return new ResponseEntity<>(shallow, HttpStatus.OK);
		} catch (CustomException e) {
			e.printStackTrace();
			return new ResponseEntity<>(e.getMessage(), e.getErrorCode());
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("Invalid Data", HttpStatus.BAD_REQUEST);
		}
	}

}
