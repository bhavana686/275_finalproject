package com.cmpe275.helper;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cmpe275.entity.CounterOffer;
import com.cmpe275.entity.Enum;
import com.cmpe275.entity.ExchangeCurrency;
import com.cmpe275.entity.Offer;
import com.cmpe275.entity.Transaction;
import com.cmpe275.entity.TransferRequest;
import com.cmpe275.entity.User;
import com.cmpe275.models.CounterOfferShallowForm;
import com.cmpe275.models.OfferDeepForm;
import com.cmpe275.models.OfferShallowForm;
import com.cmpe275.models.TransactionDeepForm;
import com.cmpe275.models.TransferRequestDeepForm;
import com.cmpe275.models.TransferRequestShallowForm;
import com.cmpe275.models.UserDeepForm;
import com.cmpe275.models.UserShallowForm;
import com.cmpe275.repo.ExchangeCurrencyRepo;

@Component
public class ResponseBuilder {

	@Autowired
	private ExchangeCurrencyRepo exchangeCurrencyRepo;

	public OfferDeepForm buildOfferDeepForm(Offer offer) throws Exception {
		try {
			OfferDeepForm offerDeepForm = new OfferDeepForm();
			offerDeepForm.setId(offer.getId());
			offerDeepForm.setSourceCountry(offer.getSourceCountry());
			offerDeepForm.setSourceCurrency(offer.getSourceCurrency());
			offerDeepForm.setAmount(offer.getAmount());
			offerDeepForm.setDestinationCountry(offer.getDestinationCountry());
			offerDeepForm.setDestinationCurrency(offer.getDestinationCurrency());
			offerDeepForm.setStatus(offer.getStatus());
			if (offer.isUsePrevailingRate()) {
				offerDeepForm
						.setExchangeRate(getExchangeRate(offer.getSourceCurrency(), offer.getDestinationCurrency()));
			} else {
				offerDeepForm.setExchangeRate(offer.getExchangeRate());
			}
			offerDeepForm.setUsePrevailingRate(offer.isUsePrevailingRate());
			offerDeepForm.setExpiry(offer.getExpiry());
			offerDeepForm.setAllowCounterOffers(offer.isAllowCounterOffers());
			offerDeepForm.setAllowSplitExchanges(offer.isAllowSplitExchanges());
			offerDeepForm.setEditable(offer.isEditable());

			if (offer.getPostedBy() != null) {
				offerDeepForm.setPostedBy(buildUserShallowForm(offer.getPostedBy()));
			}
			offerDeepForm.setCounter(offer.isCounter());
			offerDeepForm.setTransactedAmount(offer.getTransactedAmount());
			offerDeepForm.setFullyFulfilled(offer.isFullyFulfilled());

			if (offer.getTransferRequests() != null) {
				List<TransferRequestShallowForm> list = new ArrayList<TransferRequestShallowForm>();
				for (TransferRequest req : offer.getTransferRequests()) {
					list.add(buildTransferRequestShallowForm(req));
				}
				offerDeepForm.setTransferRequests(list);
			}

			if (offer.getCounterOffers() != null) {
				List<CounterOfferShallowForm> list = new ArrayList<CounterOfferShallowForm>();
				for (CounterOffer co : offer.getCounterOffers()) {
					list.add(buildCounterOfferShallowForm(co));
				}
				offerDeepForm.setCounterOffers(list);
			}

			return offerDeepForm;
		} catch (Exception e) {
			throw new Exception("Error while fetching exchange Rate");
		}
	}
	public UserDeepForm buildUserDeepForm(User user) throws Exception {
		try {
			UserDeepForm u = new UserDeepForm();
			u.setId(user.getId());		
			u.setUsername(user.getUsername());
			u.setNickname(user.getNickname());
			
			if (user.getTransferRequests() != null) {
				List<TransferRequestDeepForm> list = new ArrayList<TransferRequestDeepForm>();
				for (TransferRequest req : user.getTransferRequests()) {
					list.add(buildTransferRequestDeepForm(req));
				}
				u.setTransferRequests(list);
			}
			if (user.getOffers() != null) {
				List<OfferDeepForm> list1 = new ArrayList<OfferDeepForm>();
				for (Offer req : user.getOffers()) {
					list1.add(buildOfferDeepForm(req));
				}
				u.setOffers(list1);
				
			}
			return u;
		} catch (Exception e) {
			throw new Exception("Error while fetching exchange Rate");
		}
	}

	
	public TransferRequestDeepForm buildTransferRequestDeepForm( TransferRequest tr) throws Exception {
		try {
			TransferRequestDeepForm t = new TransferRequestDeepForm();
			t.setId(tr.getId());
			if(tr.getOffer()!=null) {
				t.setOffer(buildOfferDeepForm(tr.getOffer()));
				
			}
			if(tr.getTransaction()!=null) {
				t.setTransaction(buildTransactionDeepForm(tr.getTransaction()));
				
			}
			if(tr.getUser()!=null) {
				t.setUser(buildUserDeepForm(tr.getUser()));
				
			}
			return t;
		} catch (Exception e) {
			throw new Exception("Error while fetching exchange Rate");
		}
	}
	
	
	public TransactionDeepForm buildTransactionDeepForm(Transaction tr) throws Exception {
		try {
			TransactionDeepForm t = new TransactionDeepForm();
			t.setId(tr.getId());
			if(tr.getRequests()!=null) {
				List<TransferRequestDeepForm> list =new ArrayList<TransferRequestDeepForm>();
				for(TransferRequest temp: tr.getRequests())
				{
					list.add(buildTransferRequestDeepForm(temp));
				}
				t.setRequests(list);
				
			}
		
			return t;
		} catch (Exception e) {
			throw new Exception("Error while fetching exchange Rate");
		}
	}
	
	
	


	public UserShallowForm buildUserShallowForm(User user) throws Exception {
		try {
			UserShallowForm userShallowForm = new UserShallowForm();
			userShallowForm.setId(user.getId());
			userShallowForm.setUsername(user.getUsername());
			userShallowForm.setNickname(user.getNickname());
			return userShallowForm;
		} catch (Exception e) {
			throw new Exception("Error while fetching exchange Rate");
		}
	}

	public OfferShallowForm buildOfferShallowForm(Offer offer) throws Exception {
		try {
			OfferShallowForm offerShallowForm = new OfferShallowForm();
			offerShallowForm.setId(offer.getId());
			offerShallowForm.setSourceCountry(offer.getSourceCountry());
			offerShallowForm.setSourceCurrency(offer.getSourceCurrency());
			offerShallowForm.setAmount(offer.getAmount());
			offerShallowForm.setDestinationCountry(offer.getDestinationCountry());
			offerShallowForm.setDestinationCurrency(offer.getDestinationCurrency());
			offerShallowForm.setStatus(offer.getStatus());
			if (offer.isUsePrevailingRate()) {
				offerShallowForm
						.setExchangeRate(getExchangeRate(offer.getSourceCurrency(), offer.getDestinationCurrency()));
			} else {
				offerShallowForm.setExchangeRate(offer.getExchangeRate());
			}
			offerShallowForm.setUsePrevailingRate(offer.isUsePrevailingRate());
			offerShallowForm.setExpiry(offer.getExpiry());
			offerShallowForm.setAllowCounterOffers(offer.isAllowCounterOffers());
			offerShallowForm.setAllowSplitExchanges(offer.isAllowSplitExchanges());
			offerShallowForm.setEditable(offer.isEditable());
			offerShallowForm.setCounter(offer.isCounter());
			offerShallowForm.setTransactedAmount(offer.getTransactedAmount());
			offerShallowForm.setFullyFulfilled(offer.isFullyFulfilled());
			return offerShallowForm;
		} catch (Exception e) {
			throw new Exception("Error while fetching exchange Rate");
		}
	}

	public TransferRequestShallowForm buildTransferRequestShallowForm(TransferRequest transferRequest)
			throws Exception {
		try {
			TransferRequestShallowForm transferRequestShallowForm = new TransferRequestShallowForm();
			transferRequestShallowForm.setId(transferRequest.getId());
			transferRequestShallowForm.setAmountAdjusted(transferRequest.getAmountAdjusted());
			transferRequestShallowForm.setAmountRequired(transferRequest.getAmountRequired());
			transferRequestShallowForm.setStatus(transferRequest.getStatus());
			transferRequestShallowForm.setExpiry(transferRequest.getExpiry());
			transferRequestShallowForm.setOffer(buildOfferShallowForm(transferRequest.getOffer()));
			transferRequestShallowForm.setUser(buildUserShallowForm(transferRequest.getUser()));
			return transferRequestShallowForm;
		} catch (Exception e) {
			throw new Exception("Error while fetching exchange Rate");
		}
	}

	public CounterOfferShallowForm buildCounterOfferShallowForm(CounterOffer counterOffer) throws Exception {
		try {
			CounterOfferShallowForm counterOfferShallowForm = new CounterOfferShallowForm();
			counterOfferShallowForm.setId(counterOffer.getId());
			counterOfferShallowForm.setExpiry(counterOffer.getExpiry());
			counterOfferShallowForm.setOriginalAmount(counterOffer.getOriginalAmount());
			counterOfferShallowForm.setCounterAmount(counterOffer.getCounterAmount());
			counterOfferShallowForm.setStatus(counterOffer.getStatus());
			counterOfferShallowForm.setCreatedAt(counterOffer.getCreatedAt());
			counterOfferShallowForm.setCounteredAgainst(buildOfferShallowForm(counterOffer.getCounteredAgainst()));
			counterOfferShallowForm.setCounteredBy(buildUserShallowForm(counterOffer.getCounteredBy()));
			return counterOfferShallowForm;
		} catch (Exception e) {
			throw new Exception("Error while fetching exchange Rate");
		}
	}

	public double getExchangeRate(Enum.Currency source, Enum.Currency destination) throws Exception {
		try {
			Optional<ExchangeCurrency> rate = exchangeCurrencyRepo.findBySourceCurrencyAndTargetCurrency(source,
					destination);
			return rate.get().getExchangeRate();
		} catch (Exception e) {
			throw new Exception("Error while fetching exchange Rate");
		}
	}
}
