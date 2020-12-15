package com.cmpe275.service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.cmpe275.Exception.CustomException;
import com.cmpe275.entity.Enum.Countries;
import com.cmpe275.entity.Enum.Currency;
import com.cmpe275.entity.BankAccount;
import com.cmpe275.entity.Enum;
import com.cmpe275.entity.ExchangeCurrency;
import com.cmpe275.entity.Offer;
import com.cmpe275.entity.User;
import com.cmpe275.models.OfferDeepForm;
import com.cmpe275.models.UserShallowForm;
import com.cmpe275.repo.ExchangeCurrencyRepo;
import com.cmpe275.repo.ExchangeRateRepo;
import com.cmpe275.repo.OfferRepo;
import com.cmpe275.repo.OffersRepo;
import com.cmpe275.repo.UserRepo;
import com.fasterxml.jackson.databind.JsonNode;

@Service
public class ExchangeRateService {

	@Autowired
	private ExchangeRateRepo exchangerepo;

	@Autowired
	private UserRepo userrepo;

	@Autowired
	private OffersRepo offersrepo;
	
	@Autowired
	private ExchangeCurrencyRepo exchangeCurrencyRepo;

	public ResponseEntity<String> createOffer(HttpServletRequest request, JsonNode body) {
		Offer offer;
		try {

			offer = buildofferfromdata(body);
			long userid = Long.parseLong(body.get("userid").asText());
			User user = userrepo.getById(userid).get();
			offer.setPostedBy(user);

//			List<BankAccount> accounts = user.getBankAccounts();
//			Set<Enum.Countries> countries = new HashSet<>();
//			for (BankAccount acc : accounts) {
//				countries.add(acc.getCountry());
//			}
//
//			System.out.println(countries.size());
//			if (countries.size() < 2) {
//				return new ResponseEntity<String>("accounts", HttpStatus.OK);
//
//			}
			
			String str = checkbankaccountsfinal(null,userid,offer);
			if(str.equals("accounts"))
			{
				return new ResponseEntity<>("low accounts", HttpStatus.OK);
			}
			exchangerepo.save(offer);
			return new ResponseEntity<>("created", HttpStatus.OK);
		}

		catch (Exception e) {
			return new ResponseEntity<>("Invalid Data", HttpStatus.BAD_REQUEST);
		}
	}
	
	

	private Offer buildofferfromdata(JsonNode body) throws CustomException {
		Offer offer = new Offer();
		try {
			long userid = Long.parseLong(body.get("userid").asText());
			User user = userrepo.getById(userid).get();
			offer.setPostedBy(user);

			String expiry = body.get("expiry").asText();
			if (expiry != null) {
				expiry = body.get("expiry").asText();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
				Date date = sdf.parse(expiry);
				SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

				String fdate = sdf1.format(date);
				Timestamp ldt = Timestamp.valueOf(fdate);
				offer.setExpiry(ldt);
			}
			String allowCounterOffers = body.get("allowCounterOffers").asText();
			if (allowCounterOffers.equals("false"))
				offer.setAllowCounterOffers(false);
			String allowSplitExchanges = body.get("allowSplitExchanges").asText();
			if (allowSplitExchanges.equals("false"))
				offer.setAllowSplitExchanges(false);
			String usePrevailingRate = body.get("usePrevailingRate").asText();
			if (usePrevailingRate.equals("false")) {
				offer.setUsePrevailingRate(false);
			} else {
				offer.setUsePrevailingRate(true);
			}
			String amount = body.get("amount").asText();
			if (amount != null)
				offer.setAmount(Double.parseDouble(amount));
			for (Countries c : Enum.Countries.values()) {
				if (c.toString().equals(body.get("destinationCountry").asText())) {
					offer.setDestinationCountry(c);
					break;
				}
			}
			for (Currency c : Enum.Currency.values()) {
				if (c.toString().equals(body.get("destinationCurrency").asText())) {
					offer.setDestinationCurrency(c);
					break;
				}
			}
			for (Countries c : Enum.Countries.values()) {
				if (c.toString().equals(body.get("sourceCountry").asText())) {
					offer.setSourceCountry(c);
					break;
				}
			}
			for (Currency c : Enum.Currency.values()) {
				if (c.toString().equals(body.get("sourceCurrency").asText())) {
					offer.setSourceCurrency(c);
					break;
				}
			}

			if (!offer.isUsePrevailingRate()) {
				offer.setExchangeRate(Double.parseDouble(body.get("exchangeRate").asText()));
			}

			return offer;
		} catch (Exception e) {
			throw new CustomException(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	public Offer checkIfOfferExisting(JsonNode body, long id) throws CustomException {
		Offer offer = new Offer();
		try {
			if (!exchangerepo.getById(id).isPresent()) {
				throw new CustomException("Offer does not exist with given Id", HttpStatus.NOT_FOUND);
			}

			offer = exchangerepo.getById(id).get();
			double amount = Double.parseDouble(body.get("amount").asText());
			if (amount > 0) {
				offer.setAmount(amount);
			}

			for (Countries c : Enum.Countries.values()) {
				if (c.toString().equals(body.get("sourceCountry").asText())) {
					offer.setSourceCountry(c);
					break;
				}
			}

			for (Currency c : Enum.Currency.values()) {
				if (c.toString().equals(body.get("sourceCurrency").asText())) {
					offer.setSourceCurrency(c);
					break;
				}
			}

			for (Countries c : Enum.Countries.values()) {
				if (c.toString().equals(body.get("destinationCountry").asText())) {
					offer.setDestinationCountry(c);
					break;
				}
			}

			for (Currency c : Enum.Currency.values()) {
				if (c.toString().equals(body.get("destinationCurrency").asText())) {
					offer.setDestinationCurrency(c);
					break;
				}
			}
			String allowCounterOffers = body.get("allowCounterOffers").asText();
			if (allowCounterOffers.equals("false"))
				offer.setAllowCounterOffers(false);
			String allowSplitExchanges = body.get("allowSplitExchanges").asText();
			if (allowSplitExchanges.equals("false"))
				offer.setAllowSplitExchanges(false);
			String usePrevailingRate = body.get("usePrevailingRate").asText();
			if (usePrevailingRate.equals("false")) {
				offer.setUsePrevailingRate(false);
			} else {
				offer.setUsePrevailingRate(true);
			}
			
			if (!offer.isUsePrevailingRate()) {
				offer.setExchangeRate(Double.parseDouble(body.get("exchangeRate").asText()));
			}
			String expiry = body.get("expiry").asText();
			if (expiry != null) {
				expiry = body.get("expiry").asText();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
				Date date = sdf.parse(expiry);
				SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

				String fdate = sdf1.format(date);
				Timestamp ldt = Timestamp.valueOf(fdate);
				offer.setExpiry(ldt);
			}
			
			
		} catch (CustomException e) {
			throw new CustomException(e.getMessage(), e.getErrorCode());
		} catch (Exception e) {
			e.printStackTrace();
			throw new CustomException(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
		return offer;
	}

	public ResponseEntity<String> updateOffer(JsonNode body, long id) {

		Offer offer = new Offer();
		try {
			String userid = body.get("username").asText();
			offer = checkIfOfferExisting(body, id);
			String str = checkbankaccountsfinal(userid,id,offer);
			if(str.equals("accounts"))
			{
				return new ResponseEntity<>("low accounts", HttpStatus.OK);
			}
			exchangerepo.save(offer);
			return new ResponseEntity<>("updated", HttpStatus.OK);
		} catch (CustomException e) {
			return new ResponseEntity<>(e.getMessage(), e.getErrorCode());

		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);

		}
	}

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
			
			offerDeepForm.setUsePrevailingRate(p.isUsePrevailingRate());
			offerDeepForm.setExpiry(p.getExpiry());
			offerDeepForm.setAllowCounterOffers(p.isAllowCounterOffers());
			offerDeepForm.setAllowSplitExchanges(p.isAllowSplitExchanges());
			offerDeepForm.setEditable(p.isEditable());
			
			if (p.isUsePrevailingRate()) {
				Optional<ExchangeCurrency> rate = exchangeCurrencyRepo.findBySourceCurrencyAndTargetCurrency(
						p.getSourceCurrency(), p.getDestinationCurrency());
				offerDeepForm.setExchangeRate(rate.get().getExchangeRate());
			} else {
				offerDeepForm.setExchangeRate(p.getExchangeRate());
			}

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

	public ResponseEntity<Object> getOffersByUserId(long userid) {
		List<Offer> list;
		User user = userrepo.getById(userid).get();
		try {
			if (!userrepo.getById(userid).isPresent()) {
				throw new CustomException("user does not exist with given Id", HttpStatus.NOT_FOUND);
			} else {
				list = userrepo.getById(userid).get().getOffers();
			}
			LinkedList<Offer> l1 = new LinkedList<>();
			LinkedList<Offer> l2 = new LinkedList<>();
			for(Offer offr : list)
			{
				if(offr.getStatus() == Enum.OfferStatuses.open)
				{
					l1.add(offr);
				}
				else
				{
					l2.add(offr);
				}
			}
			l1.addAll(l2);
			return new ResponseEntity<>(convertOfferObjectToDeepForm(l1), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}

	}

	public ResponseEntity<Object> getofferbyofferid(long id) {
		Offer list;
		try {
			if (!exchangerepo.getById(id).isPresent()) {
				throw new CustomException("offer does not exist with given Id", HttpStatus.NOT_FOUND);
			} else {
				list = exchangerepo.getById(id).get();
			}
			List<Offer> offerlist = new ArrayList<Offer>();
			offerlist.add(list);
			return new ResponseEntity<>(convertOfferObjectToDeepForm(offerlist), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}



	public ResponseEntity<String> checkbankaccounts(long userid) {
	
		User user = userrepo.getById(userid).get();

		List<BankAccount> accounts = user.getBankAccounts();
		Set<Enum.Countries> countries = new HashSet<>();
		for (BankAccount acc : accounts) {
			countries.add(acc.getCountry());
		}

		if (countries.size() < 2) {
			return new ResponseEntity<String>("lowaccounts", HttpStatus.OK);

		}
		else
		{
			return new ResponseEntity<String>("approved", HttpStatus.OK);

		}
	}
	
	
	public String checkbankaccountsfinal(String userid,long id, Offer offer) {
		
		User user;
		if(userid!=null)
		{
			 user = userrepo.getByUsername(userid).get();
		}
		else
		{
	    user = userrepo.getById(id).get();
		}
		List<BankAccount> accounts = user.getBankAccounts();
		Set<Enum.Countries> countries = new HashSet<>();
		for (BankAccount acc : accounts) {
			
	
			if(offer.getDestinationCountry().equals(acc.getCountry()) && (acc.getAccountType().equals("receiving") || acc.getAccountType().equals("both")))
			   {
				countries.add(acc.getCountry());
				}
			else if(offer.getSourceCountry().equals(acc.getCountry()) && (acc.getAccountType().equals("sending") || acc.getAccountType().equals("both")))
			{
				countries.add(acc.getCountry());
			}
		}

		if (countries.size() < 2) {
			return "accounts";

		}
		else
		{
			return "approved";

		}
	}

}