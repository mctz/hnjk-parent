/*
 * Copyright (c) 2002-2003 by OpenSymphony
 * All rights reserved.
 */
package com.opensymphony.module.propertyset.hibernate3;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.hibernate.HibernateException;
import org.hibernate.Query;

import com.opensymphony.module.propertyset.PropertyException;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Iterator;

/**
 * @author $author$
 * @version $Revision: 1.1 $
 */
public class HibernatePropertySetDAOImpl extends HibernateDaoSupport implements HibernatePropertySetDAO {
	private Log log = LogFactory.getLog(HibernatePropertySetDAOImpl.class);

	public HibernatePropertySetDAOImpl() {
		super();
	}

	/**
	 * @see com.tricision.maas.dao.IHibernatePropertySetDAO#setImpl(PropertySetItem, boolean)
	 */
	@Override
	public void setImpl(PropertySetItem item, boolean isUpdate) {
		this.log.debug("TOP of setImpl... "); 
		try {
			this.log.debug("(setImpl) Have an open sessionFactory");

			if (isUpdate) {
				this.log.debug("(setImpl) Attempting update");
				this.getSession().update(item);
			} else {
				this.log.debug("(setImpl) Attempting save");
				this.getSession().save(item);
			}
			this.log.debug("(setImpl) Attempting flush");
		} catch (HibernateException he) {
			throw new PropertyException("Could not save key '" + item.getPropertyPK().getKey() + "':" + he.getMessage()); //$NON-NLS-1$
		}
	}

	/**
	 * @see com.tricision.maas.dao.IHibernatePropertySetDAO#getKeys(java.lang.String, java.lang.Long, java.lang.String, int)
	 */
	@Override
	public Collection<String> getKeys(String entityName, Long entityId, String prefix, int type) {
		this.log.debug("Top of getKeys() with entityName: " + entityName);
		List<String> list = null;
		try {
			list = getKeysImpl(entityName, entityId, prefix, type);
		} catch (HibernateException e) {
			list = Collections.emptyList();
		}
		return list;
	}

	/**
	 * @see com.tricision.maas.dao.IHibernatePropertySetDAO#create(String, long, String)
	 */
	@Override
	public PropertySetItem create(String entityName, long entityId, String key) {
		PropertySetItem propertySetItem = new PropertySetItem();
		PropertyPK propertyPK = new PropertyPK();
		propertyPK.setEntityId(entityId);
		propertyPK.setEntityName(entityName);
		propertyPK.setKey(key);
		propertySetItem.setPropertyPK(propertyPK);
		return propertySetItem;
	}

	/**
	 * @see com.tricision.maas.dao.IHibernatePropertySetDAO#findByKey(String, Long, String)
	 */
	@Override
	public PropertySetItem findByKey(String entityName, Long entityId, String key) {
		this.log.debug(this.getClass().getName() + " Top of findByKey with entityName: " + entityName + " entityId: " + entityId + " and Key: " + key);
		PropertySetItem item = null;
		try {
			this.log.debug("Looking for propertysetitem: " + entityName + " with entityId: " + entityId + " and key: " + key);
			item = getItem(entityName, entityId, key);
		} catch (HibernateException e) {
			this.log.error("Item not found in persistent storage. " + e.getMessage());
			return null;
		}
		return item;
	}

	/**
	 * @see com.tricision.maas.dao.IHibernatePropertySetDAO#remove(String, Long)
	 */
	@Override
	public void remove(String entityName, Long entityId) {
		try {
			// REFACTOR
			Collection<String> keys = getKeys(entityName, entityId, null, 0);
			Iterator<String> iter = keys.iterator();
			while (iter.hasNext()) {
				String key = iter.next();
				remove(entityName, entityId, key);
			}
		} catch (HibernateException e) {
			throw new PropertyException("Could not remove all keys: " + e.getMessage()); //$NON-NLS-1$
		}
	}

	/**
	 * @see com.tricision.maas.dao.IHibernatePropertySetDAO#remove(String, Long, String)
	 */
	@Override
	public void remove(String entityName, Long entityId, String key) {
		try {
			this.getSession().delete(getItem(entityName, entityId, key));
		} catch (HibernateException e) {
			throw new PropertyException("Could not remove key '" + key + "': " + e.getMessage());
		}
	}

	/**
	 * Get the propertyset item from persistent storage.
	 * 
	 * @param entityName
	 *            - the name of the entity
	 * @param entityId
	 *            - the id of the entity
	 * @param key
	 *            - the string that is the key for the entity
	 * @return - PropertySetItem
	 * @throws HibernateException
	 */
	public PropertySetItem getItem(final String entityName, final Long entityId, final String key) throws HibernateException {
		// Propertly construct the object before the find.
		PropertySetItem findItem = new PropertySetItem();
		PropertyPK propertyPK = new PropertyPK();
		propertyPK.setEntityId(entityId);
		propertyPK.setEntityName(entityName);
		propertyPK.setKey(key);
		findItem.setPropertyPK(propertyPK);
		

		// getHibernateTemplate().load will throw an ObjectNotFound exception,
		// get() will
		// just return null for the object.
		return (PropertySetItem) this.getSession().get(PropertySetItem.class, findItem);
	}

	/**
	 * This is the body of the getKeys() method, so that you can reuse it
	 * wrapped by your own session management.
	 * 
	 * @param entityName
	 * @param entityId
	 * @param prefix
	 * @param type
	 * @return
	 * @throws HibernateException
	 */
	@SuppressWarnings("unchecked")
	public List<String> getKeysImpl(String entityName, Long entityId, String prefix, int type) throws HibernateException {
		Query query;

		if ((prefix != null) && (type > 0)) {
			query = this.getSession().getNamedQuery("all_keys_with_type_like");
			query.setString("like", prefix + '%');
			query.setInteger("type", type);
		} else if (prefix != null) {
			query = this.getSession().getNamedQuery("all_keys_like");
			query.setString("like", prefix + '%');
		} else if (type > 0) {
			query = this.getSession().getNamedQuery("all_keys_with_type");
			query.setInteger("type", type);
		} else {
			query = this.getSession().getNamedQuery("all_keys");
		}

		query.setString("entityName", entityName);
		query.setLong("entityId", entityId.longValue());
		return query.list();
	}
}
