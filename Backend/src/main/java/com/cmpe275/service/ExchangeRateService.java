package com.cmpe275.service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.cmpe275.Exception.CustomException;
import com.cmpe275.entity.Enum.Countries;
import com.cmpe275.entity.Enum.Currency;
import com.cmpe275.entity.Enum;
import com.cmpe275.entity.Offer;
import com.cmpe275.entity.User;
import com.cmpe275.models.OfferDeepForm;
import com.cmpe275.models.UserShallowForm;
import com.cmpe275.repo.ExchangeRateRepo;
import com.cmpe275.repo.UserRepo;
import com.fasterxml.jackson.databind.JsonNode;

@Service
public class ExchangeRateService {

	@Autowired
	private ExchangeRateRepo exchangerepo;

	@Autowired
	private UserRepo userrepo;

	public ResponseEntity<String> createOffer(HttpServletRequest request, JsonNode body) {
		System.out.println("create offer");
		Offer offer;
		try {
			// System.out.println(sourceCountry);
			offer = buildofferfromdata(body);

			Offer offer1 = exchangerepo.save(offer);
			System.out.println("success");
			return new ResponseEntity<>("Succesfully Created Offer", HttpStatus.OK);
		}

		catch (Exception e) {
			return new ResponseEntity<>("Invalid Data", HttpStatus.BAD_REQUEST);
		}
	}

	private Offer buildofferfromdata(JsonNode body) throws CustomException {
		Offer offer = new Offer();
		try {
			String expiry = body.get("expiry").asText();
			if (expiry != null) {
				expiry = body.get("expiry").asText();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				Date date = sdf.parse(expiry);
				SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

				String fdate = sdf1.format(date);
				Timestamp ldt = Timestamp.valueOf(fdate);
				offer.setExpiry(ldt);
			}
			String allowCounterOffers = body.get("allowCounterOffers").asText();
			if (allowCounterOffers == "false")
				offer.setAllowCounterOffers(false);
			String allowSplitExchanges = body.get("allowSplitExchanges").asText();
			System.out.print(allowSplitExchanges);
			if (allowSplitExchanges == "false")
				offer.setAllowSplitExchanges(false);
			String usePrevailingRate = body.get("usePrevailingRate").asText();
			if (usePrevailingRate == "false") {
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
			long userid = Long.parseLong(body.get("userid").asText());
			User user = userrepo.getById(userid).get();
			offer.setPostedBy(user);
			return offer;
		} catch (Exception e) {
			throw new CustomException(e.getMessage(), HttpStatus.BAD_REQUEST);
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
			if (amount > 0) {
				exchangeOffer.setAmount(amount);
			}
			Countries destinationCountry = offer.getDestinationCountry();
			if (destinationCountry != null) {
				exchangeOffer.setDestinationCountry(destinationCountry);
			}
			Countries sourceCountry = offer.getSourceCountry();
			if (sourceCountry != null) {
				exchangeOffer.setSourceCountry(sourceCountry);
			}
			Currency destinationcurrency = offer.getDestinationCurrency();
			if (destinationcurrency != null) {
				exchangeOffer.setDestinationCurrency(destinationcurrency);
			}
			Currency sourceCurrency = offer.getSourceCurrency();
			if (sourceCurrency != null) {
				exchangeOffer.setSourceCurrency(sourceCurrency);
			}
			if (!offer.isUsePrevailingRate()) {
				exchangeOffer.setUsePrevailingRate(false);
				if (offer.getExchangeRate() > 0) {
					exchangeOffer.setExchangeRate(offer.getExchangeRate());
				}
			}
			if (!exchangeOffer.isUsePrevailingRate()) {
				if (offer.getExchangeRate() > 0) {
					exchangeOffer.setExchangeRate(offer.getExchangeRate());
				}
			}
			if (!offer.isAllowCounterOffers()) {

				exchangeOffer.setAllowCounterOffers(false);
			}
			if (!offer.isAllowSplitExchanges()) {
				exchangeOffer.setAllowSplitExchanges(false);
			}
			Timestamp expiry = offer.getExpiry();
			if (expiry != null) {
				exchangeOffer.setExpiry(expiry);
			}

		} catch (CustomException e) {
			throw new CustomException(e.getMessage(), e.getErrorCode());
		} catch (Exception e) {
			e.printStackTrace();
			throw new CustomException(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
		return exchangeOffer;
	}

	public ResponseEntity<String> updateOffer(Offer offer, long id) {

		try {
			offer = checkIfOfferExisting(offer, id);
			exchangerepo.save(offer);
			return new ResponseEntity<>("Successfully updated the offer", HttpStatus.OK);
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
			offerDeepForm.setExchangeRate(p.getExchangeRate());
			offerDeepForm.setUsePrevailingRate(p.isUsePrevailingRate());
			offerDeepForm.setExpiry(p.getExpiry());
			offerDeepForm.setAllowCounterOffers(p.isAllowCounterOffers());
			offerDeepForm.setAllowSplitExchanges(p.isAllowSplitExchanges());
			offerDeepForm.setEditable(p.isEditable());

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
		try {
			if (!userrepo.getById(userid).isPresent()) {
				throw new CustomException("user does not exist with given Id", HttpStatus.NOT_FOUND);
			} else {
				list = userrepo.getById(userid).get().getOffers();
			}
			return new ResponseEntity<>(convertOfferObjectToDeepForm(list), HttpStatus.OK);
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

}