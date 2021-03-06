


import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.cmpe275.entity.Enum;
import com.cmpe275.service.EmailService;
import com.cmpe275.service.UserService;
import com.fasterxml.jackson.databind.JsonNode;

@Controller
@CrossOrigin(origins="http://localhost:3000", allowedHeaders = "*",allowCredentials="true")
@RequestMapping(value = "/user")
public class UserController {

	@Autowired
	private UserService userService;
	
	@Autowired
	private EmailService  emailService;

	@PostMapping()
	public ResponseEntity<Object> userSignup(HttpServletRequest request, @RequestBody JsonNode body) {
		return userService.signUp(request,body);
	}
	
	@GetMapping
	public ResponseEntity<Object> userSignin(HttpServletRequest request) {
		return userService.signIn(request);
	}
	

	
	@GetMapping("/verifyMail")
	public ResponseEntity<Object> verifyEmail(HttpServletRequest request,@RequestParam("username") String username) {
		return userService.verifyMail(request, username);
	}
	

}
