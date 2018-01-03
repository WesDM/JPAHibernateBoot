package com.wesdm.JPAHibernate.controller;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wesdm.JPAHibernate.dao.UserDao;
import com.wesdm.JPAHibernate.model.Address;
import com.wesdm.JPAHibernate.model.User;

@RestController
@RequestMapping("/user")
public class UserController {
	
	private static final Logger LOGGER = LogManager.getLogger(UserController.class);
	
	private UserDao userDao;
	
	@Autowired
	Environment env;
	
	@Autowired
	public UserController(UserDao userDao) {
		this.userDao = userDao;
	}
	
	@PostMapping(consumes="application/json")
	@Transactional
	public void save(@RequestBody User user) {
		userDao.makePersistent(user);
	}
	
	@PostMapping(path="/setup/{index}/{batchSize}", consumes="application/json")
	@Transactional
	public void setup(@PathVariable int index, @PathVariable int batchSize) {
		final long startTime = System.currentTimeMillis();


		for(int i = index; i < index+100; i++) {
			User user = new User();
			user.setUsername("USERNAME"+i);
			user.setBillingAddress(new Address("SESAME ST", "10108", "NY"));
			user.setHomeAddress(new Address("SESAME ST", "10108", "NY"));
			userDao.makePersistent(user);
			if(i > 0 && i % batchSize == 0) {
				userDao.getEntityManager().flush();
				userDao.getEntityManager().clear();
			}
		}
		userDao.getEntityManager().flush();
		userDao.getEntityManager().clear();
		final long endTime = System.currentTimeMillis();

		LOGGER.info("Total execution time: " + (endTime - startTime));
	}
}
