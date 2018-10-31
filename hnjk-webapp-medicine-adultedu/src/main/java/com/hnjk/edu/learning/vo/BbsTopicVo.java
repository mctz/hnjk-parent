package com.hnjk.edu.learning.vo;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.apache.lucene.analysis.cn.ChineseAnalyzer;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Where;
import org.hibernate.search.annotations.Analyzer;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.Store;

import com.hnjk.core.annotation.DeleteChild;
import com.hnjk.core.support.base.model.BaseModel;
import com.hnjk.core.support.context.Constants;
import com.hnjk.edu.basedata.model.Course;
import com.hnjk.edu.basedata.model.YearInfo;
import com.hnjk.edu.learning.model.BbsSection;
import com.hnjk.edu.roll.model.Classes;
import com.hnjk.edu.teaching.model.Syllabus;
import com.hnjk.platform.system.model.Attachs;

/**
 * 论坛帖子表
 * <code>BbsTopicVo</code><p>
 */
@Getter(value = AccessLevel.PUBLIC)
@Setter(value = AccessLevel.PUBLIC)
public class BbsTopicVo {

	private String brSchoolName;
	private String courseName;
	private String syllabusName;//章节点
	private String title;//标题
	private String fillinMan;//发帖人
	private String classes;//班级
	private String fillinDate;//提问时间
	private String isAnswered;//是否应答
	private Integer howLongReply;//回复时长
	private String firstReplyMan;//回复人
	private String tags;//是否FAQ
	private String scoreType;//最后回复人
	
}
