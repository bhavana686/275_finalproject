package com.cmpe275.controller;

import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.cmpe275.repo.OfferRepo;
import com.cmpe275.repo.UserRepo;
import com.cmpe275.service.OfferResponseService;
import com.cmpe275.service.TransactionService;

@Controller
@CrossOrigin(origins = "*", allowedHeaders = "*", allowCredentials = "true")
@RequestMapping(value = "/offer")
public class TransactionController {

	@Autowired
	private TransactionService transactionService;
	
	@Autowired
	private OfferResponseService offerService;

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
		return transactionService.processAutoMatchOffer(request, body, (long) offerId);
	}

	@PostMapping("/automatch/{id}/unequal/process")
	public ResponseEntity<Object> processUnEqualAutoMatchOffer(HttpServletRequest request, @RequestBody JsonNode body,
			@PathVariable("id") long offerId) {
		return transactionService.processUnEqualAutoMatchOffer(request, body, (long) offerId);
	}

	@PostMapping("/counter/{id}/accept")
	public ResponseEntity<Object> acceptCounterOffer(HttpServletRequest request, @RequestBody JsonNode body,
			@PathVariable("id") long counterId) {
		return transactionService.acceptCounterOffer(request, body, (long) counterId);
	}
	
	@PostMapping("/counter/{id}/decline")
	public ResponseEntity<Object> declineCounterOffer(HttpServletRequest request, @RequestBody JsonNode body,
			@PathVariable("id") long counterId) {
		return transactionService.declineCounterOffer(request, body, (long) counterId);
	}
	
	@PostMapping("/{offerId}/request/{requestId}/accept")
	public ResponseEntity<Object> acceptTransferRequest(HttpServletRequest request, @RequestBody JsonNode body,
			@PathVariable("offerId") long offerId, @PathVariable("requestId") long requestId) {
		return transactionService.acceptTransferRequest(request, body, (long) offerId, (long) requestId);
	}
	
	@PostMapping("/{offerId}/request/{requestId}/decline")
	public ResponseEntity<Object> declineTransferRequest(HttpServletRequest request, @RequestBody JsonNode body,
			@PathVariable("offerId") long offerId, @PathVariable("requestId") long requestId) {
		return transactionService.declineTransferRequest(request, body, (long) offerId, (long) requestId);
	}

	@PostMapping("/automatch/{id}")
	public ResponseEntity<Object> autoMatchOffers(HttpServletRequest request, @PathVariable("id") long offerId) {
		return transactionService.autoMatchOffers(request, (long) offerId);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Object> fetchOfferDeepForm(HttpServletRequest request, @PathVariable("id") long offerId) {
		return offerService.fetchOfferDeepForm(request, (long) offerId);
	}

}
