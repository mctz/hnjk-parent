package com.opensymphony.module.propertyset.hibernate3;

import java.util.Collection;

public interface HibernatePropertySetDAO {

	/**
	 * Save the implementation of a PropertySetItem.
	 * 
	 * @param item
	 * @param isUpdate 
	 * 			  -	Boolean indicating whether or not this item already exists
	 */
	public void setImpl(PropertySetItem item, boolean isUpdate);

	/**
	 * Return a list of property set objects
	 * 
	 * @param entityName
	 *            - name of property set entity
	 * @param entityId
	 *            - id of property set
	 * @param prefix
	 *            - parameter added to selection query
	 * @param type
	 *            - property set key_type
	 * @return Collection
	 */
	public Collection<String> getKeys(String entityName, Long entityId, String prefix, int type);

	/**
	 * Return a new PropertySetItem; this is object construction only.
	 * 
	 * @param entityName
	 *            - name of property set entity
	 * @param entityId
	 *            - id for property set
	 * @param key
	 *            - key
	 * @return - PropertySetItem
	 */
	public PropertySetItem create(String entityName, long entityId, String key);

	/**
	 * Return a PropertySetItem from persistent storage. Since there is a
	 * composite key on the os_propertyset table the selection requires the
	 * entityName, entityId, and the key.
	 * 
	 * @param entityName
	 *            - entity name
	 * @param entityId
	 *            - the entity id
	 * @param key
	 *            - the key
	 * @return PropertySetItem
	 */
	public PropertySetItem findByKey(String entityName, Long entityId, String key);

	/**
	 * Remove a PropertySetItem from persistent storage
	 * 
	 * @param entityName
	 *            - entity name
	 * @param entityId
	 *            - entity id
	 * @param key
	 *            - the key
	 */
	public void remove(String entityName, Long entityId, String key);

	/**
	 * Remove a PropertySetItem from persistent storage
	 * 
	 * @param entityName
	 *            - entity name
	 * @param entityId
	 *            - enting id
	 */
	public void remove(String entityName, Long entityId);
}
