package com.hnjk.edu.teaching.service;

import java.io.File;
import java.sql.Connection;
import java.util.Map;

import net.sf.jasperreports.engine.JasperPrint;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.support.base.service.IBaseService;
import com.hnjk.edu.teaching.model.ExamDistribu;
/**
 * 
 * <code>考场座位按排IExamDistribuService</code><p>
 * 
 * @author：广东学苑教育发展有限公司
 * @since： 2010-10-22 下午02:47:32
 * @see 
 * @version 1.0
 */
public interface IExamDistribuService extends IBaseService<ExamDistribu> {
	
	/**
	 * 分配座位
	 * @param examInfos      考试课程ID串(以逗号分隔)
	 * @param examRoomIds    考场课室ID串(以逗号分隔)
	 * @param reset          当同一个考场按排多门课程座位号需要从1开始
	 * @param examSubId      考试批次ID
	 * @param branchSchool   学习中心ID
	 * @return
	 * @throws ServiceException
	 */
	public Map<String,Object> assignSeat(String examInfos,String examRoomIds,String examSubId,String branchSchool,boolean reset)throws ServiceException;
	/**
	 * 清空考场
	 * @param examResultIds
	 * @return
	 */
	public boolean clearnSeat(String examResultIds);
	/**
	 * 生成JasperPrint对象
	 * @param jasperPrintModelFlie
	 * @param param
	 * @param conn
	 * @return
	 * @throws Exception
	 */
	public JasperPrint doJasperPrint(File jasperPrintModelFlie,Map<String,Object> param,Connection conn)throws Exception;
	
	/**
	 * 按课程分配座位
	 * @param examInfos      考试课程ID串(以逗号分隔)
	 * @param examRoomIds    考场课室ID串(以逗号分隔)
	 * @param reset          当同一个考场按排多门课程座位号需要从1开始
	 * @param examSubId      考试批次ID
	 * @param branchSchool   学习中心ID
	 * @return
	 * @throws ServiceException
	 */
	public  Map<String,Object> assignSeatByCourse(String examInfos, String examRoomIds,String examSubId,String branchSchool,boolean reset) throws ServiceException;
		
}
