package com.cmpe275.controller;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.cmpe275.entity.Offer;
import com.cmpe275.entity.User;
import com.cmpe275.repo.OfferRepo;
import com.cmpe275.repo.UserRepo;
import com.cmpe275.service.TransactionService;
import com.cmpe275.entity.Enum;

@Controller
@CrossOrigin("*")
@RequestMapping(value = "/offer")
public class TransactionController {

	@Autowired
	private TransactionService transactionService;

	@Autowired
	private OfferRepo offerRepo;

	@Autowired
	private UserRepo userRepo;

	@PostMapping("/direct/{id}/accept")
	public ResponseEntity<Object> acceptDirectOffer(HttpServletRequest request, @RequestBody JsonNode body,
			@PathVariable("id") long offerId) {
		return transactionService.processDirectOffer(request, body, (long) offerId);
	}

	@PostMapping("/direct/{id}/counter")
	public ResponseEntity<Object> counterOffer(HttpServletRequest request, @RequestBody JsonNode body,
			@PathVariable("id") long offerId) {
		return transactionService.counterAOffer(request, body, (long) offerId);
	}

	@PostMapping("/automatch/{id}/equal/process")
	public ResponseEntity<Object> processAutoMatchOffer(HttpServletRequest request, @RequestBody JsonNode body,
			@PathVariable("id") long offerId) {
		// Incoming amount in source currency
		return transactionService.processAutoMatchOffer(request, body, (long) offerId);
	}

	@PostMapping("/automatch/{id}/unequal/process")
	public ResponseEntity<Object> processUnEqualAutoMatchOffer(HttpServletRequest request, @RequestBody JsonNode body,
			@PathVariable("id") long offerId) {
		// Incoming amount in destination currency
		return transactionService.processUnEqualAutoMatchOffer(request, body, (long) offerId);
	}

	@PostMapping("/counter/{id}/accept")
	public ResponseEntity<Object> acceptCounterOffer(HttpServletRequest request, @RequestBody JsonNode body,
			@PathVariable("id") long counterId) {
		return transactionService.acceptCounterOffer(request, body, (long) counterId);
	}

	@PostMapping("/automatch/{id}")
	public ResponseEntity<Object> autoMatchOffers(HttpServletRequest request, @PathVariable("id") long offerId) {
		return transactionService.autoMatchOffers(request, (long) offerId);
	}

//	@GetMapping("/create")
//	public ResponseEntity<Object> createOffer(HttpServletRequest request) {
//		Offer offer = new Offer();
//		offer.setDestinationCountry(Enum.Countries.India);
//		offer.setDestinationCurrency(Enum.Currency.INR);
//		offer.setSourceCountry(Enum.Countries.US);
//		offer.setSourceCurrency(Enum.Currency.USD);
//		offer.setAmount(75000);
//		offer.setStatus(Enum.OfferStatuses.open);
//		offer.setExchangeRate(0.014);
//		long id = 2;
//		Optional<User> user = userRepo.getById(id);
////		System.out.println("--------------");
////		System.out.println(user.isEmpty());
////		System.out.println(user.get());
//		offer.setPostedBy(user.get());
//		offerRepo.save(offer);
//		return new ResponseEntity<>("Success", HttpStatus.OK);
//	}

}
