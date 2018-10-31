package com.hnjk.core.support.base.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 提供一个基础的Model. <p>
 * 其他Model均继承自BaseModel.<p>
 * @author： 广东学苑教育发展有限公司.
 * @since： 2009-2-26上午11:46:15
 * @version 1.0
 */
@MappedSuperclass
@Getter(value = AccessLevel.PUBLIC)
@Setter(value = AccessLevel.PUBLIC)
public abstract class BaseModel implements IBaseModel, Serializable {

	/**
	 * ID，必须为"resourceid"
	 */
	@Id
	@Column(length=32)
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	private String resourceid;

	/**
	 * 版本号
	 */
	@Version
	private Long version;

	/**
	 * 是否删除
	 */
	@Column(name="isDeleted")
	private Integer isDeleted  = 0;

}
