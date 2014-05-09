package com.n11.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.n11.domain.User;
import com.n11.repository.UserRepository;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;

	public List<User> findAll() {
		return userRepository.findAll();
	}

	public User create(User user) {
		return userRepository.save(user);
	}

	public User findById(String userId) {
		return userRepository.findOne(userId);
	}

	public void update(User user) {
		userRepository.save(user);
	}

	public void delete(String userId) {
		User user = userRepository.findOne(userId.toString());
		if (user != null) {
			userRepository.delete(user);
		}
	}
}