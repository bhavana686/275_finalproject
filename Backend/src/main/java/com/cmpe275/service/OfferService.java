package com.cmpe275.service;

import java.util.*;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.cmpe275.entity.Offer;
import com.cmpe275.repo.OffersRepo;

@Service
public class OfferService {
	@Autowired
	private OffersRepo offersRepo;

	public  ResponseEntity<Object> getOffers(HttpServletRequest req) {
		try {
			List<Offer> offer = offersRepo.getActiveOffers();
			return new ResponseEntity<>(offer, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>("Invalid Data", HttpStatus.BAD_REQUEST);
		}
	}
	
	public  ResponseEntity<Object> getFilteredOffers(HttpServletRequest req) {
		
		 try {
			if(req.getParameter("sourceAmount")=="None")
			{
			List<Offer> offer = offersRepo.getActiveOffersByDestinationAmount(req.getParameter("sourceCurrency"),req.getParameter("destinationCurrency"),Integer.parseInt(req.getParameter("destinationAmount")));
			return new ResponseEntity<>(offer, HttpStatus.OK);
			}
			else
			{
			List<Offer> offer = offersRepo.getActiveOffersBySourceAmount(req.getParameter("sourceCurrency"),req.getParameter("destinationCurrency"),Integer.parseInt(req.getParameter("sourceAmount")));
			return new ResponseEntity<>(offer, HttpStatus.OK);	
			}	
		 } catch (Exception e) {
			return new ResponseEntity<>("Invalid Data", HttpStatus.BAD_REQUEST);
		 }
	
	}
}
