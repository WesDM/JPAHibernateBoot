package com.wesdm.JPAHibernate.dao;

import org.springframework.stereotype.Repository;

import com.wesdm.JPAHibernate.model.User;

@Repository
public class UserDaoImpl extends GenericDaoImpl<User, Long> implements UserDao {

	public UserDaoImpl() {
		super(User.class);
	}
}
