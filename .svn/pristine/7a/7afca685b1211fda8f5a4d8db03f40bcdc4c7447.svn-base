package com.opensymphony.workflow.spi.hibernate3;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * 上下步骤节点复合主键.
 * @author hzg
 *
 */
@Embeddable
public class StepPrevPK implements java.io.Serializable{

	private static final long serialVersionUID = -7851589514124130303L;
	
	@Column(name="ID",nullable=false)
	private Long id;
	
	@Column(name="PREVIOUS_ID",nullable=false)
    private Long previousId;
    
    
    public StepPrevPK() {}


	public long getId() {
		return id;
	}


	public void setId(long id) {
		this.id = id;
	}


	public long getPreviousId() {
		return previousId;
	}


	public void setPreviousId(long previousId) {
		this.previousId = previousId;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (null == obj) {
			return false;
		}
		if (!(obj instanceof StepPrevPK)) {
			return false;
		}

		final StepPrevPK pro = (StepPrevPK) obj;
		if (!id.equals(pro.getId()) || !previousId.equals(pro.getPreviousId())) {
			return false;
		}
		if (null == id || null == previousId || id.intValue() != pro.getId() || previousId != pro.getPreviousId()) {
			return false;
		}
		return true;
	}


	@Override
	public int hashCode() {
		int result;
		result = id.hashCode();
		result = 29 * result + previousId.hashCode();
		return result;
	}
    
    

}
