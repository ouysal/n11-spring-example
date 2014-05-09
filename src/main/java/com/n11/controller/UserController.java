package com.n11.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.tanesha.recaptcha.ReCaptcha;
import net.tanesha.recaptcha.ReCaptchaResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.n11.domain.User;
import com.n11.dto.CreateUserDto;
import com.n11.service.UserService;

@Controller
@RequestMapping(value = "/users")
public class UserController {  

	@Autowired
	private UserService userService;
	
	@Autowired
	private ReCaptcha reCaptcha;
	
	@RequestMapping(method = RequestMethod.GET)
	@ResponseBody
	public List<User> findAll() {
		return userService.findAll();
	}

	@RequestMapping(method = RequestMethod.POST)
	public @ResponseBody User create(HttpServletRequest request, @RequestBody CreateUserDto createUserDto) throws Exception {
		
		ReCaptchaResponse reCaptchaResponse = reCaptcha.checkAnswer(request.getRemoteAddr(),
				createUserDto.getCaptcha().getChallenge(),
				createUserDto.getCaptcha().getResponse());
		
		if (!reCaptchaResponse.isValid()) {
			throw new Exception("Captcha is incorrect.");
		}
		
		return userService.create(createUserDto.getUser());
	}

	@RequestMapping(value = "/{userId}", method = RequestMethod.GET)
	public @ResponseBody User findById(@PathVariable(value = "userId") String userId) {
		return userService.findById(userId);
	}

	@RequestMapping(value = "/{userId}", method = RequestMethod.PUT)
	public @ResponseBody User update(@PathVariable(value = "userId") String userId,
			@RequestBody User user) {
		userService.update(user);
		return user;
	}

	@RequestMapping(value = "/{userId}", method = RequestMethod.DELETE)
	public @ResponseBody Boolean delete(@PathVariable(value = "userId") String userId) {
		userService.delete(userId);
		return true;
	}
	
	@ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public String handleClientErrors(Exception ex) {
        return ex.getMessage();
    }
	
	@ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public String handleServerErrors(Exception ex) {
        return ex.getMessage();
    }
}