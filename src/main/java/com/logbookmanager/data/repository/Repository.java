package com.logbookmanager.data.repository;

import java.io.Serializable;
import java.util.List;

import org.hibernate.criterion.Criterion;

import com.logbookmanager.support.BaseObject;

public interface Repository<T extends BaseObject<T>, ID extends Serializable> {

	T findById(ID id);

	List<T> findAll();

	List<T> findByExample(T exampleInstance);

	T findUniqueByExample(T exampleInstance);

	T findByNaturalId(T exampleInstance);

	T findByNaturalId(T exampleInstance, Criterion naturalIdRestriction);

	T makePersistent(T entity);

	/**
	 * 
	 * @param entity
	 *            The entity in the session which should be removed from the
	 *            datastore
	 * @return the transient version of the entity
	 */
	T delete(T entity);

	/**
	 * 
	 * @param id
	 *            The id of the entity to be removed
	 * @return the transient version of the entity
	 */
	T delete(ID id);

}
