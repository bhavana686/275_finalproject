package com.cmpe275.service;
import java.sql.Date;
import java.sql.Timestamp;

import javax.servlet.http.HttpServletRequest;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.cmpe275.Exception.CustomException;
import com.cmpe275.entity.Enum.Countries;
import com.cmpe275.entity.Enum.Currency;
import com.cmpe275.entity.Offer;
import com.cmpe275.entity.User;
import com.cmpe275.repo.ExchangeRateRepo;
import com.cmpe275.repo.UserRepo;

@Service
public class ExchangeRateService {

	@Autowired
	private ExchangeRateRepo exchangerepo;
	
	@Autowired
	private UserRepo userrepo;

	public  ResponseEntity<String> createOffer(Offer offer) {
		Offer exchangeOffer;
		try {
//			HttpSession session = req.getSession();
//			if(session.getAttribute("user") == null)
//			{
//				throw new CustomException("User is not Authenticated",HttpStatus.UNAUTHORIZED);
//			}
		User usr = new User();
		usr.setId(1);
		usr.setUsername("laxmi");
		offer.setPostedBy(usr);
	    exchangeOffer = exchangerepo.save(offer);
		return new ResponseEntity<>("Succesfully Created Offer", HttpStatus.OK);
		}
		
//		catch(CustomException e)
//		{
//			return new ResponseEntity<>(e.getMessage(), e.getErrorCode());
//
//		}
		catch (Exception e) {
			return new ResponseEntity<>("Invalid Data", HttpStatus.BAD_REQUEST);
		}
        }
	
	public Offer checkIfOfferExisting(Offer offer, long id) throws CustomException {
		Offer exchangeOffer = new Offer();
		try {
			if (!exchangerepo.getById(id).isPresent()) {
				throw new CustomException("Offer does not exist with given Id", HttpStatus.NOT_FOUND);
			}
			
			exchangeOffer = exchangerepo.getById(id).get();
			double amount = offer.getAmount();
			if(amount > 0)
			{
				exchangeOffer.setAmount(amount);
			}
			Countries destinationCountry = offer.getDestinationCountry();
			if(destinationCountry != null)
			{
				exchangeOffer.setDestinationCountry(destinationCountry);
			}
			Countries sourceCountry = offer.getSourceCountry();
			if(sourceCountry != null)
			{
				exchangeOffer.setSourceCountry(sourceCountry);
			}
			Currency destinationcurrency = offer.getDestinationCurrency();
			if(destinationcurrency != null)
			{
				exchangeOffer.setDestinationCurrency(destinationcurrency);
			}
			Currency sourceCurrency = offer.getSourceCurrency();
			if(sourceCurrency != null)
			{
				exchangeOffer.setSourceCurrency(sourceCurrency);
			}
			if(!offer.isUsePrevailingRate())
			{
				exchangeOffer.setUsePrevailingRate(false);
				if(offer.getExchangeRate() > 0)
				{
				exchangeOffer.setExchangeRate(offer.getExchangeRate());
				}
//				else
//				{
//					throw new CustomException("Exchange Rate not Mentioned", HttpStatus.BAD_REQUEST);
//				}
			}
			if(!exchangeOffer.isUsePrevailingRate())
			{
				if(offer.getExchangeRate() > 0)
				{
				exchangeOffer.setExchangeRate(offer.getExchangeRate());
				}
			}
			if(!offer.isAllowCounterOffers())
			{
				
				exchangeOffer.setAllowCounterOffers(false);
			}
			if(!offer.isAllowSplitExchanges())
			{
				exchangeOffer.setAllowSplitExchanges(false);
			}
		    Timestamp expiry = offer.getExpiry();
		    if(expiry != null)
		    {
		    	exchangeOffer.setExpiry(expiry);
		    }
		    
		     
					}
		catch (CustomException e) {
			throw new CustomException(e.getMessage(), e.getErrorCode());
		} catch (Exception e) {
			e.printStackTrace();
			throw new CustomException(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
		return exchangeOffer;
	}

	
	public  ResponseEntity<String> updateOffer(Offer offer, long id)
	{
	
		try {
			offer = checkIfOfferExisting(offer, id);
			exchangerepo.save(offer);
			return new ResponseEntity<>("Successfully updated the offer", HttpStatus.OK);
		} 
		catch(CustomException e)
		{
			return new ResponseEntity<>(e.getMessage(), e.getErrorCode());

		}
		catch(Exception e)
		{
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);

		}
	}
	

}