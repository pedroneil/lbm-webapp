package com.logbookmanager.data.repository.hibernate;

/**
 * protected Logger log;
 * this.log = LoggerFactory.getLogger(getClass());
 */
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import com.logbookmanager.data.repository.UserRepository;
import com.logbookmanager.data.support.hibernate.HibernateRepository;
import com.logbookmanager.domain.model.security.RegisteredUser;
import com.logbookmanager.domain.support.UserName;

@Repository
public class UserRepositoryImpl extends HibernateRepository<RegisteredUser, Long> implements UserRepository {

	/**
	 * the super must be called otherwise the Generics do not work
	 */
	public UserRepositoryImpl(SessionFactory sessionFactory) {
		super(sessionFactory, RegisteredUser.class, Long.class);
	}

	/**
	 */
	public RegisteredUser getUserByUsername(UserName username) {
		RegisteredUser eg = new RegisteredUser(username);
		return getUser(eg);
	}

	public RegisteredUser getActiveUserByUsername(UserName username) {
		RegisteredUser eg = new RegisteredUser(username);
		eg.setActive(true);
		return getUser(eg);
	}

	public RegisteredUser getActiveUser(RegisteredUser registeredUser) {
		RegisteredUser result = null;
		registeredUser.setActive(true);
		result = getUser(registeredUser);
		return result;
	}

	public RegisteredUser getUser(RegisteredUser registeredUser) {
		RegisteredUser result = findOneByExample(registeredUser);
		return result;
	}

	/**
	 */
	public RegisteredUser saveUser(RegisteredUser registeredUser) {
		if (this.log.isDebugEnabled()) {
			this.log.debug("user's username: " + registeredUser.getUsername());
		}
		makePersistent(registeredUser);

		// we have to flush to enable the propogation of a
		// DataIntegrityViolation thrown by Spring.
		getSessionFactory().getCurrentSession().flush();

		return registeredUser;
	}

	/**
	 */
	public RegisteredUser removeUser(RegisteredUser registeredUser) {
		return delete(registeredUser);
	}

}
