<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>我的学籍异动信息</title>
<script type="text/javascript">
function selfChangeApply(studentid){
	var url = "${baseUrl}/edu3/register/stuchangeinfo/self/edit.html?studentid="+studentid;
	navTab.openTab('RES_SCHOOL_SCHOOLROLL_CHANGE_EDIT', url, '申请学籍异动');
	//给本页加上重载的设定
	navTab.reloadFlag("RES_SCHOOL_SCHOOLROLL_CHANGE");
}
//打印学籍异动审批表
function printApprovalForm(){
	if(!isChecked('resourceid',"#changeBody")){
			alertMsg.warn('请选择一条要操作记录！');
		return false;
 	}
	
	var stuChangeIds = new Array();
	var isAllNoAudit = true;
	$("#changeBody input[name='resourceid']:checked").each(function(){
		stuChangeIds.push($(this).val());
		var audit = $(this).attr("auditStatus");
		// 只要有一个不是未审核的都不能打印
		if(audit != "W"){
			isAllNoAudit = false;
		}
    });
	if(!isAllNoAudit){
		alertMsg.warn('只能打印未审核的记录！');
		return false;
	}
	var alt_msg = "确定要打印所选学生的异动信息吗？";
	var url = "${baseUrl}/edu3/roll/stuchangeinfo/approvalForm-print.html?isPdf=Y&stuChangeIds="+stuChangeIds.toString();
	alertMsg.confirm(alt_msg, {
       okCall: function(){
    	   downloadFileByIframe(url,'stuChangeInfo_exportIframe');
       }
	});
}

//打印退费银行信息
function printRefundInformation(){
	if(!isChecked('resourceid',"#changeBody")){
			alertMsg.warn('请选择一条要操作记录！');
		return false;
 	}
	
	var stuChangeIds = new Array();
	$("#changeBody input[@name='resourceid']:checked").each(function(){
		var changeType = $(this).attr("changeType");
		if(changeType == "13"){
			stuChangeIds.push($(this).val());
		}
    });
	var alt_msg = "确定要打印所选学生的异动信息吗？";
	var url = "${baseUrl}/edu3/roll/stuchangeinfo/approvalForm-print.html?isPdf=Y&isRefundInformation=Y&stuChangeIds="+stuChangeIds.toString();
	alertMsg.confirm(alt_msg, {
       okCall: function(){
    	   downloadFileByIframe(url,'stuChangeInfo_exportIframe');
       }
	});
}
</script>
<style type="text/css">
#my_studentInfo_stuFactFee td {
	text-align: center;
}

#my_studentInfo_stuFactFee th {
	text-align: center;
}
</style>
</head>
<body>

	<div class="page">
		<div class="pageContent">
			<div class="pageFormContent" layouth="21">
				<!-- tabs -->
				<div class="tabs">
					<div class="tabsHeader">
						<div class="tabsHeaderContent">
							<ul>
								<c:forEach items="${stuChangeInfoList }" var="stuChange">
									<li
										<c:if test="${stuChange['isDefaultStudentId'] eq 'Y' }">class="selected"</c:if>><a
										href="javascript:void(0)"><span>${stuChange['key'] }</span></a></li>
								</c:forEach>
							</ul>
						</div>
					</div>
					<div class="tabsContent" style="height: 100%;">
						<c:forEach items="${stuChangeInfoList }" var="stuChange2">
							<div class="tabs">
							
								<div class="tabsHeader">
								
									<!-- 功能点 -->
									<ul>
										<li><div class="buttonActive"><div class="buttonContent">
											<button onclick="selfChangeApply('${stuChange2['studentid']}')">申请学籍异动</button>
										</div></div></li>
										<li><div class="buttonActive"><div class="buttonContent">
											<button onclick="printApprovalForm()">打印申请表</button>
										</div></div></li>
										<li><div class="buttonActive"><div class="buttonContent">
											<button onclick="printRefundInformation()">打印退费银行信息</button>
										</div></div></li>
										<%-- <lable>${stuChange2['studentid']}</lable> --%>
									</ul> 
								</div>
								<div class="tabsContent" style="height: 100%;">
									<div>
										<table class="form">
											<thead>
											<tr>
												<th class="idCol" id="idCol" width="3%"><input
													type="checkbox" name="checkall" id="check_all_change"
													onclick="checkboxAll('#check_all_change','resourceid','#changeBody')" /></th>
												<th style="width: 4%">序号</th>
												<th style="width: 8%">申请时间</th>
												<th style="width: 6%">异动类型</th>
												<th style="width: 6%">异动原因</th>
												<th style="width: 20%">异动前信息</th>
												<th style="width: 20%">异动后信息</th>
												<th style="width: 6%">申请人</th>
												<th style="width: 8%">申请时间</th>
												<th style="width: 6%">审核人</th>
												<th style="width: 8%">审核时间</th>
												<th style="width: 8%">审核结果</th>
											</tr>
											</thead>
											<tbody id="changeBody">
											<c:forEach items="${stuChange2['stuChangeInfoList']}"
												var="stu" varStatus="vs">
												<tr
													<c:if test="${stu.resourceid eq stuchangeinfoid }">id="jumpto" </c:if>>
													<td class="idCol"><input type="checkbox" name="resourceid"
														value="${stu.resourceid }" autocomplete="off"
														auditStatus="${stu.finalAuditStatus }"
														changeType="${ stu.changeType}"
														studentId="${stu.studentInfo.resourceid}"
														brSchool="${stu.changeBrschool.resourceid}" /></td>
													<td>${vs.index +1}<c:if
															test="${stu.resourceid eq stuchangeinfoid }">
															<blink>
																<font style="background-color: red" color="yellow">本条</font>
															</blink>
														</c:if></td>
													<td><fmt:formatDate value="${stu.applicationDate }"
															pattern="yyyy-MM-dd" /></td>
													<td>
														${ghfn:dictCode2Val("CodeStudentStatusChange",stu.changeType) }</td>
													<td>${stu.reason}</td>
													<td>
														<!-- 转专业--> <c:if
															test="${stu.changeType eq '23' or stu.changeType eq '12'}">
																【年级】${stu.changeBeforeTeachingGuidePlan.grade }<br> 
																【教学计划名称】${stu.changeBeforeTeachingGuidePlan.teachingPlan.teachingPlanName }<br> 																
																【办学单位】${stu.changeBeforeBrSchool.unitName}<br>
																【班级】${stu.changeBeforeClass.classname}
																</c:if> <!-- 转办学单位 --> <c:if
															test="${empty stu.changeBeforeTeachingGuidePlan and stu.changeType eq '82' }">
																【办学单位】${stu.changeBeforeBrSchool.unitName }
																</c:if> <c:if
															test="${not empty stu.changeBeforeTeachingGuidePlan and stu.changeType eq '82' }">
																【年级】${stu.changeBeforeTeachingGuidePlan.grade }<br> 
																【教学计划名称】${stu.changeBeforeTeachingGuidePlan.teachingPlan.teachingPlanName }<br> 
																【办学单位】${stu.changeBeforeBrSchool.unitName}<br>
																【班级】${stu.changeBeforeClass.classname}
																</c:if> <!-- 转学习方式 --> <c:if
															test="${empty stu.changeBeforeTeachingGuidePlan and stu.changeType eq '81' }">
																【学习形式】${ghfn:dictCode2Val('CodeTeachingType',stu.changeBeforeLearingStyle)}
																</c:if> <c:if
															test="${not empty stu.changeBeforeTeachingGuidePlan and stu.changeType eq '81' }">
																【年级】${stu.changeBeforeTeachingGuidePlan.grade }<br> 
																【教学计划名称】${stu.changeBeforeTeachingGuidePlan.teachingPlan.teachingPlanName }<br> 
																【学习形式】${ghfn:dictCode2Val('CodeTeachingType',stu.changeBeforeTeachingGuidePlan.teachingPlan.schoolType) }<br>
																【办学单位】${stu.changeBeforeBrSchool.unitName}<br>
																【班级】${stu.changeBeforeClass.classname}
																</c:if> <!-- 有异动前学籍状态记录 --> <c:if
															test="${not empty stu.changeBeforeStudentStatus}">【学籍状态】${ghfn:dictCode2Val('CodeStudentStatus',stu.changeBeforeStudentStatus)}
																</c:if> <c:if test="${ stu.changeType eq 'C_24' }">
																【原学校】${stu.changeBeforeSchoolName}<br> 
																【原层次】${stu.changeBeforeClassicName }<br>
																【原形式】${ghfn:dictCode2Val('CodeTeachingType',stu.changeBeforeLearingStyle)}<br>
																【原专业】${stu.changeBeforeMajorName }
																【学籍状态】无
																</c:if>
													</td>
													<td>
														<!-- 转专业--> <c:if
															test="${empty stu.changeTeachingGuidePlan and  (stu.changeType eq '23'  or stu.changeType eq '12' )}">
																	【年级】${stu.studentInfo.grade }<br>  																
																	【专业】${stu.changeMajor.majorName }<br> 
																	【层次】${stu.studentInfo.classic.classicName }<br> 
																	【办学单位】${stu.changeBrschool.unitName}<br> 
																	【班级】${stu.changeClass.classname}
																</c:if> <c:if
															test="${not empty stu.changeTeachingGuidePlan and  (stu.changeType eq '23'  or stu.changeType eq '12') }">
																	【年级】${stu.changeTeachingGuidePlan.grade.gradeName }<br>  																
																	【教学计划名称】${stu.changeTeachingGuidePlan.teachingPlan.teachingPlanName}<br>
																	【办学单位】${stu.changeBrschool.unitName}<br> 
																	【班级】${stu.changeClass.classname}
																</c:if> <!-- 转办学单位 --> <c:if
															test="${empty stu.changeTeachingGuidePlan and stu.changeType eq '82' }">
																	【办学单位】${stu.changeBrschool.unitName }
																</c:if> <c:if
															test="${not empty stu.changeTeachingGuidePlan and stu.changeType eq '82' }">
																	【年级】${stu.changeTeachingGuidePlan.grade.gradeName }<br>  																
																	【教学计划名称】${stu.changeTeachingGuidePlan.teachingPlan.teachingPlanName}<br>
																	【办学单位】${stu.changeBrschool.unitName}<br> 
																	【班级】${stu.changeClass.classname}
																</c:if> <!-- 转学习方式 --> <c:if
															test="${empty stu.changeTeachingGuidePlan and stu.changeType eq '81' }">
																	【学习形式】${ghfn:dictCode2Val('CodeTeachingType',stu.studentInfo.teachingType} }
																</c:if> <c:if
															test="${not empty stu.changeTeachingGuidePlan and stu.changeType eq '81' }">
																	【年级】${stu.changeTeachingGuidePlan.grade.gradeName }<br>  																
																	【教学计划名称】${stu.changeTeachingGuidePlan.teachingPlan.teachingPlanName}<br>
																	【学习形式】${ghfn:dictCode2Val('CodeTeachingType',stu.changeTeachingGuidePlan.teachingPlan.schoolType) }<br> 
																	【办学单位】${stu.changeBrschool.unitName}<br> 
																	【班级】${stu.changeClass.classname}
																</c:if> <c:if test="${ stu.changeType eq 'C_24' }">
																【办学单位】${stu.changeBrschool.unitName}<br> 
																【层次】${stu.changeTeachingGuidePlan.teachingPlan.classic.classicName }<br>
																【形式】${ghfn:dictCode2Val('CodeTeachingType',stu.changeTeachingType)}<br>
																【专业】${stu.changeTeachingGuidePlan.teachingPlan.major.majorName }<br>
																【学籍状态】在学<br>
																【年级】${stu.changeTeachingGuidePlan.grade.gradeName }<br>
																【班级】${stu.changeClass }<br>
														</c:if>
													</td>
													<td>${stu.applicationMan }</td>
													<td><fmt:formatDate value="${stu.applicationDate }"
															pattern="yyyy-MM-dd" /></td>
													<td>${stu.auditMan }</td>
													<td><fmt:formatDate value="${stu.auditDate }"
															pattern="yyyy-MM-dd" /></td>
													<td>${stu.finalAuditStatus == 'Y' ?"<font color='blue'>通过</font>":(stu.finalAuditStatus == 'N' ?"<font color='red'>未通过</font>":"待审核")}</td>
												</tr>
											</c:forEach>
											</tbody>
										</table>
									</div>
								</div>
							</div>
							<!-- end tabs -->
						</c:forEach>
					</div>
					<div class="tabsFooter"></div>
				</div>
				<label style="width: 100%;color: red;">备注：申请学籍异动之后请打印纸质文件，签字并提交，教务员审核后才生效！</label>
				<!-- end tabs -->
			</div>
		</div>
	</div>
</body>
</html>