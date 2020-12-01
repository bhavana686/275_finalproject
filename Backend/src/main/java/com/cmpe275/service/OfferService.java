package com.cmpe275.service;

import java.util.*;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.cmpe275.entity.Offer;
import com.cmpe275.models.OfferDeepForm;
import com.cmpe275.entity.Enum;
import com.cmpe275.models.UserShallowForm;
import com.cmpe275.repo.OffersRepo;

@Service
public class OfferService {
	@Autowired
	private OffersRepo offersRepo;

	public List<OfferDeepForm> convertOfferObjectToDeepForm(List<Offer> offer) {
		List<OfferDeepForm> offerList = new ArrayList<OfferDeepForm>();
		
		offer.forEach((p) -> {
		OfferDeepForm offerDeepForm = new OfferDeepForm();
		offerDeepForm.setId(p.getId());
		offerDeepForm.setSourceCountry(p.getSourceCountry());
		offerDeepForm.setSourceCurrency(p.getSourceCurrency());
		offerDeepForm.setAmount(p.getAmount());
		offerDeepForm.setDestinationCountry(p.getDestinationCountry());
		offerDeepForm.setDestinationCurrency(p.getDestinationCurrency());
		offerDeepForm.setStatus(p.getStatus());
		offerDeepForm.setExchangeRate(p.getExchangeRate());
		offerDeepForm.setUsePrevailingRate(p.isUsePrevailingRate());
		offerDeepForm.setExpiry(p.getExpiry());
		offerDeepForm.setAllowCounterOffers(p.isAllowCounterOffers());
		offerDeepForm.setAllowSplitExchanges(p.isAllowSplitExchanges());
		
		if (p.getPostedBy() != null) {
			UserShallowForm userShallowForm = new UserShallowForm();
			userShallowForm.setId(p.getPostedBy().getId());
			userShallowForm.setUsername(p.getPostedBy().getUsername());
			userShallowForm.setNickname(p.getPostedBy().getNickname());	
			offerDeepForm.setPostedBy(userShallowForm);
		}
		offerDeepForm.setCounter(p.isCounter());
		offerDeepForm.setTransactedAmount(p.getTransactedAmount());
		offerDeepForm.setFullyFulfilled(p.isFullyFulfilled());
		
		offerList.add(offerDeepForm);
		});
		
		return offerList;
	}
	
	public  ResponseEntity<Object> getOffers(HttpServletRequest req) {
		try {
			List<Offer> offer = offersRepo.getActiveOffers();
			return new ResponseEntity<>(convertOfferObjectToDeepForm(offer), HttpStatus.OK);
		} catch (Exception e) {
			System.out.println(e);
			return new ResponseEntity<>("Invalid Data", HttpStatus.BAD_REQUEST);
		}
	}
	
	public  ResponseEntity<Object> getFilteredOffers(HttpServletRequest req,Enum.Currency sourceCurrency,Enum.Currency destinationCurrency ) {
		
		 try {
			 
			if(req.getParameter("sourceAmount")=="")
			{
			List<Offer> offer = offersRepo.getActiveOffersByDestinationAmount(sourceCurrency,destinationCurrency,Double.parseDouble(req.getParameter("destinationAmount")));
			return new ResponseEntity<>(offer, HttpStatus.OK);
			}
			else
				
			{	
			List<Offer> offer = offersRepo.getActiveOffersBySourceAmount(sourceCurrency,destinationCurrency,Double.parseDouble(req.getParameter("sourceAmount")));
			return new ResponseEntity<>(convertOfferObjectToDeepForm(offer), HttpStatus.OK);	
			}	
		 } catch (Exception e) {
			 System.out.println(e);
			return new ResponseEntity<>("Invalid Data", HttpStatus.BAD_REQUEST);
		 }
	
	}
}
