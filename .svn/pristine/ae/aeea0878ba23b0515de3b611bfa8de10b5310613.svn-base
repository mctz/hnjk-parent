package com.hnjk.core.support.schedule.quartz;

import java.util.List;

/**
 * Quartz调度管理接口.
 * @author hzg
 *
 */
public interface ScheduleManager {

	/**
	 * 调度
	 * @param quartzTrigger
	 */
	void schedule(QuartzTrigger quartzTrigger);
	
	/**
	 * 获取所有调度的工作
	 * @return
	 */
	List<QuartzTrigger> getAllJobs();
	
	/**
	 * 移除工作
	 * @param jobName
	 * @param groupName
	 */
	void remoteSchudle(String jobName,String groupName);
	
	/**
	 * 重新调度
	 * @param quartzTrigger
	 */
	void resetSchudle(QuartzTrigger quartzTrigger);
}
