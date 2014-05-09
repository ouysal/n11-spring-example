package com.n11.controller;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.web.client.RestTemplate;

import com.n11.domain.User;
import com.n11.dto.Captcha;
import com.n11.dto.CreateUserDto;

public class UserControllerTest {

	private static final String REST_SERVICE_URL = "http://localhost:8080/spring-example/users";
	private RestTemplate restTemplate = new RestTemplate();

	private User createNewUser() {

		User user = new User();
		user.setFirstName("John");
		user.setLastName("Smith");
		user.setPhoneNumber("555 453 45 34");

		return user;
	}
	
	private Captcha createCaptcha() {
		Captcha captcha = new Captcha();
		captcha.setChallenge("mockChallenge");
		captcha.setResponse("mockResponse");
		return captcha;
	}

	@Test
	public void testFindAll() {

		User newUser = createNewUser();
		restTemplate.postForObject(REST_SERVICE_URL, new CreateUserDto(newUser, createCaptcha()), User.class);

		User[] foundUsers = (User[]) restTemplate.getForObject(REST_SERVICE_URL, User[].class);
		Assert.assertNotNull(foundUsers);
		Assert.assertTrue(foundUsers.length > 0);
	}

	@Test
	public void testCreate() {

		User newUser = createNewUser();
		User createdUser = restTemplate.postForObject(REST_SERVICE_URL, new CreateUserDto(newUser, createCaptcha()), User.class);
		Assert.assertNotNull(createdUser);
		Assert.assertNotNull(createdUser.getId());
	}

	@Test
	public void testFindById() {
		
		User newUser = createNewUser();
		User createdUser = restTemplate.postForObject(REST_SERVICE_URL, new CreateUserDto(newUser, createCaptcha()), User.class);

		String restServiceUrl = REST_SERVICE_URL + "/" + createdUser.getId();
		User foundUser = restTemplate.getForObject(restServiceUrl, User.class);
		Assert.assertNotNull(foundUser);
		Assert.assertEquals(foundUser.getId(), createdUser.getId());
	}

	@Test
	public void testUpdate() {

		User newUser = createNewUser();
		User createdUser = restTemplate.postForObject(REST_SERVICE_URL, new CreateUserDto(newUser, createCaptcha()), User.class);

		createdUser.setFirstName("Jane");

		String restServiceUrl = REST_SERVICE_URL + "/" + createdUser.getId();
		restTemplate.put(restServiceUrl, createdUser);
		
		User modifiedUser = restTemplate.getForObject(restServiceUrl, User.class);
		Assert.assertNotNull(modifiedUser);
		Assert.assertEquals(modifiedUser.getFirstName(),"Jane");
	}

	@Test
	public void testDelete() {

		User newUser = createNewUser();
		User createdUser = restTemplate.postForObject(REST_SERVICE_URL, new CreateUserDto(newUser, createCaptcha()), User.class);

		Assert.assertNotNull(createdUser);
		Assert.assertNotNull(createdUser.getId());

		// it should be found
		String restServiceUrl = REST_SERVICE_URL + "/" + createdUser.getId();
		User foundUser = restTemplate.getForObject(restServiceUrl, User.class);
		Assert.assertNotNull(foundUser);
		Assert.assertEquals(foundUser.getId(), createdUser.getId());

		restTemplate.delete(restServiceUrl);

		// it should not be found
		User deletedUser = restTemplate.getForObject(restServiceUrl, User.class);
		Assert.assertNull(deletedUser);
	}
}