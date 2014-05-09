package com.n11.service;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.n11.domain.User;

/**
 * Service for initializing MongoDB with sample data using {@link MongoTemplate}
 */
public class InitMongoService {
	
	@Autowired
	private MongoTemplate mongoTemplate;

	public void init() {
		// Drop existing collections
		mongoTemplate.dropCollection("user");

		User john = new User();
		john.setId(UUID.randomUUID().toString());
		john.setFirstName("John");
		john.setLastName("Smith");
		john.setPhoneNumber("555 453 45 34");
		
		User jane = new User();
		jane.setId(UUID.randomUUID().toString());
		jane.setFirstName("Jane");
		jane.setLastName("Adams");
		jane.setPhoneNumber("555 917 34 23");
		
		User oguzhan = new User();
		oguzhan.setId(UUID.randomUUID().toString());
		oguzhan.setFirstName("Oguzhan");
		oguzhan.setLastName("Uysal");
		oguzhan.setPhoneNumber("542 665 40 69");
		
		// Insert to db
		mongoTemplate.insert(john, "user");
		mongoTemplate.insert(jane, "user");
		mongoTemplate.insert(oguzhan, "user");
	}
}
