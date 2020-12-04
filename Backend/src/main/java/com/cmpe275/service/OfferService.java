package com.cmpe275.service;

import java.util.*;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.cmpe275.entity.Offer;
import com.cmpe275.helper.ResponseBuilder;
import com.cmpe275.models.OfferDeepForm;
import com.cmpe275.entity.Enum;
import com.cmpe275.entity.ExchangeCurrency;
import com.cmpe275.models.UserShallowForm;
import com.cmpe275.repo.ExchangeCurrencyRepo;
import com.cmpe275.repo.OffersRepo;

@Service
public class OfferService {
	@Autowired
	private OffersRepo offersRepo;

	@Autowired
	ExchangeCurrencyRepo exchangeCurrencyRepo;

	@Autowired
	ResponseBuilder responseBuilder;

	public List<OfferDeepForm> convertOfferObjectToDeepForm(List<Offer> offer) throws Exception{
		List<OfferDeepForm> offerList = new ArrayList<OfferDeepForm>();
		try {
			

			offer.forEach((p) -> {
				OfferDeepForm offerDeepForm = new OfferDeepForm();
				offerDeepForm.setId(p.getId());
				offerDeepForm.setSourceCountry(p.getSourceCountry());
				offerDeepForm.setSourceCurrency(p.getSourceCurrency());
				offerDeepForm.setAmount(p.getAmount());
				offerDeepForm.setDestinationCountry(p.getDestinationCountry());
				offerDeepForm.setDestinationCurrency(p.getDestinationCurrency());
				offerDeepForm.setStatus(p.getStatus());
				if (p.isUsePrevailingRate()) {
					Optional<ExchangeCurrency> rate = exchangeCurrencyRepo
							.findBySourceCurrencyAndTargetCurrency(p.getSourceCurrency(), p.getDestinationCurrency());
					offerDeepForm.setExchangeRate(rate.get().getExchangeRate());
				} else {
					offerDeepForm.setExchangeRate(p.getExchangeRate());
				}
				offerDeepForm.setUsePrevailingRate(p.isUsePrevailingRate());
				offerDeepForm.setExpiry(p.getExpiry());
				offerDeepForm.setAllowCounterOffers(p.isAllowCounterOffers());
				offerDeepForm.setAllowSplitExchanges(p.isAllowSplitExchanges());

				if (p.getPostedBy() != null) {
					UserShallowForm userShallowForm = new UserShallowForm();
					userShallowForm.setId(p.getPostedBy().getId());
					userShallowForm.setUsername(p.getPostedBy().getUsername());
					userShallowForm.setNickname(p.getPostedBy().getNickname());
					try {
						userShallowForm.setRating(responseBuilder.calculateRating(p.getPostedBy()));
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					offerDeepForm.setPostedBy(userShallowForm);
				}
				offerDeepForm.setCounter(p.isCounter());
				offerDeepForm.setTransactedAmount(p.getTransactedAmount());
				offerDeepForm.setFullyFulfilled(p.isFullyFulfilled());
				offerList.add(offerDeepForm);
			});

			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return offerList;
	}

	public ResponseEntity<Object> getOffers(HttpServletRequest req) {
		try {
			int userId = (int) Integer.parseInt(req.getParameter("userId"));
			List<Offer> offer = offersRepo.getActiveOffers(userId);
			return new ResponseEntity<>(convertOfferObjectToDeepForm(offer), HttpStatus.OK);
		} catch (Exception e) {
			System.out.println(e);
			return new ResponseEntity<>("Invalid Data", HttpStatus.BAD_REQUEST);
		}
	}

	public ResponseEntity<Object> getFilteredOffers(HttpServletRequest req, Enum.Currency sourceCurrency,
			Enum.Currency destinationCurrency) {

		try {

			if (req.getParameter("destinationAmount") != null
					|| (req.getParameter("destinationAmount") != null && req.getParameter("sourceAmount") != null)) {
				List<Offer> offer = offersRepo.getActiveOffersByDestinationAmount(sourceCurrency.toString(),
						destinationCurrency.toString(), Double.parseDouble(req.getParameter("destinationAmount")));
				return new ResponseEntity<>(convertOfferObjectToDeepForm(offer), HttpStatus.OK);
			} else

			{
				List<Offer> offer = offersRepo.getActiveOffersBySourceAmount(sourceCurrency.toString(),
						destinationCurrency.toString(), Double.parseDouble(req.getParameter("sourceAmount")));
				return new ResponseEntity<>(convertOfferObjectToDeepForm(offer), HttpStatus.OK);
			}

		} catch (Exception e) {
			System.out.println(e);
			return new ResponseEntity<>("Invalid Data", HttpStatus.BAD_REQUEST);
		}

	}
}