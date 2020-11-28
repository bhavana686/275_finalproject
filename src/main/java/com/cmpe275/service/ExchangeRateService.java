package com.cmpe275.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
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

	public  ResponseEntity<Object> createOffer(Offer offer) {
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
		return new ResponseEntity<>(exchangeOffer, HttpStatus.OK);
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
	

}