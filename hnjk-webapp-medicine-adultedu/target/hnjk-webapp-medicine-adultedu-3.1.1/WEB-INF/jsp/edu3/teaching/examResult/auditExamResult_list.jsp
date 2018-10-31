<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>成绩审核</title>


</head>
<body>
	<div class="page">
		<div class="pageHeader">
			<form id="auditExamResultsSearchForm"
				onsubmit="return navTabSearch(this);"
				action="${baseUrl}/edu3/teaching/result/audit-examresults-list.html"
				method="post">
				<input id="auditExamResultsSearchForm_examSubId" type="hidden" name="examSubId" value="${condition['examSub'] }" /> 
				<input id="auditExamResultsSearchForm_examInfoId" type="hidden" name="examInfoId" value="${condition['examInfoId'] }" /> 
				<input id="auditExamResultsSearchForm_pageNum" type="hidden" name="pageNum" value="${objPage.pageNum}" /> 
				<input  id="auditExamResultsSearchForm_pc_teachType" type="hidden" name="pc_teachType" value="${condition['pc_teachType'] }" />
				<input  id="auditExamResultsSearchForm_cs_teachType" type="hidden" name="cs_teachType" value="${condition['cs_teachType'] }" />
				<input id="auditExamResultsSearchForm_courseId" type="hidden" name="courseId" value="${condition['courseId'] }" /> 
				<input id="auditExamResultsSearchForm_classesid" type="hidden" name="classesid" value="${condition['classesid'] }" />
				<input  id="auditExamResultsSearchForm_examCourseType" type="hidden" name="examCourseType" value="${condition['examCourseType'] }" />

				<div class="searchBar">
					<ul class="searchContent">
						<li><label>教学站：</label> <gh:brSchoolAutocomplete
								name="branchSchool" tabindex="1"
								id="audit_examresults_brSchoolId" displayType="code"
								defaultValue="${condition['branchSchool']}" style="width:53%" />
						</li>
						<li><label>年级：</label>
						<gh:selectModel id="gradeid" name="gradeid" bindValue="resourceid"
								displayValue="gradeName"
								modelClass="com.hnjk.edu.basedata.model.Grade"
								value="${condition['gradeid']}" orderBy="gradeName desc"
								style="width:55%" /></li>
						<li><label>层次：</label> <gh:selectModel name="classic"
								bindValue="resourceid" displayValue="classicName"
								modelClass="com.hnjk.edu.basedata.model.Classic"
								value="${condition['classic']}" style="width:55%" /></li>
					</ul>
					<ul class="searchContent">
						<li><label>专业：</label> <gh:selectModel name="major"
								bindValue="resourceid" displayValue="majorCodeName"
								modelClass="com.hnjk.edu.basedata.model.Major"
								orderBy="majorCode asc" value="${condition['major']}"
								style="width:55%" /></li>

						<li><label>学号：</label><input type="text" name="studyNo"
							value="${condition['studyNo']}" style="width: 53%" /></li>
						<li><label>姓名：</label><input type="text" name="name"
							value="${condition['name']}" style="width: 53%" /></li>
					</ul>
					<ul class="searchContent">
						<li><label>成绩状态：</label> <gh:select name="checkStatus"
								dictionaryCode="CodeExamResultCheckStatus" choose="Y"
								value="${condition['checkStatus']}" style="width:55%" /></li>
						<label style="color: red;width: 250px;">${scoreper }</label>
						<div class="buttonActive" style="float: right">
							<div class="buttonContent">
								<button type="submit">查 询</button>
							</div>
						</div>
					</ul>
				</div>
			</form>
		</div>
		<gh:resAuth parentCode="RES_TEACHING_RESULT_MANAGE" pageType="auditSub"></gh:resAuth>
		<div class="pageContent">
			<form id="auditExamResultsSaveForm"
				action="${baseUrl}/edu3/teaching/result/examresults-audit-pass.html" method="post">
				<input id="auditExamResultsSaveForm_examSubId" type="hidden" name="examSubId" value="${condition['examSub'] }" />
                <input id="auditExamResultsSaveForm_examInfoId" type="hidden" name="examInfoId" value="${condition['examInfoId'] }" />
                <%--<input id="auditExamResultsSaveForm_teachType" type="hidden" name="teachType" value="${condition['teachType'] }" />--%>
				<input  id="auditExamResultsSearchForm_pc_teachType" type="hidden" name="pc_teachType" value="${condition['pc_teachType'] }" />
				<input  id="auditExamResultsSearchForm_cs_teachType" type="hidden" name="cs_teachType" value="${condition['cs_teachType'] }" />
                <input id="auditExamResultsSearchForm_courseId" type="hidden" name="courseId" value="${condition['courseId'] }" />
                <input id="auditExamResultsSearchForm_classesid" type="hidden" name="classesid" value="${condition['classesid'] }" />
                <input  id="auditExamResultsSearchForm_examCourseType" type="hidden" name="examCourseType" value="${condition['examCourseType'] }" />
					
				<table class="table" layouth="164" width="100%">
					<thead>
						<tr>
							<c:choose>
								<%--期末混合机考增加 笔考成绩 列--%>
								<c:when test="${condition['examInfo'].ismixture eq 'Y' }">
									<th width="4%"><input type="checkbox" name="checkall"
										id="check_all_examResults_audit_save"
										onclick="checkboxAll('#check_all_examResults_audit_save','resourceid','#examResultsAuditBody')" /></th>
									<th width="10%">教学站</th>
									<th width="6%">年级</th>
									<th width="10%">专业</th>
									<th width="6%">层次</th>
									<th width="8%">学号</th>
									<th width="6%">姓名</th>
									<th width="6%">选考次数</th>
									<th width="6%">平时成绩</th>
									<th width="6%">机考成绩</th>
									<th width="6%">笔考成绩</th>
									<th width="6%">综合成绩</th>
									<th width="6%">成绩异常</th>
									<th width="6%">成绩状态</th>
									<th width="8%">审核意见</th>
								</c:when>
								<c:otherwise>
									<th width="4%"><input type="checkbox" name="checkall"
										id="check_all_examResults_audit_save"
										onclick="checkboxAll('#check_all_examResults_audit_save','resourceid','#examResultsAuditBody')" /></th>
									<th width="10%">教学站</th>
									<th width="6%">年级</th>
									<th width="14%">专业</th>
									<th width="6%">层次</th>
									<th width="8%">学号</th>
									<th width="6%">姓名</th>
									<th width="6%">选考次数</th>
									<th width="6%">平时成绩</th>
									<th width="6%">卷面成绩</th>
									<th width="6%">综合成绩</th>
									<th width="6%">成绩异常</th>
									<th width="6%">成绩状态</th>
									<th width="10%">审核意见</th>
								</c:otherwise>
							</c:choose>
						</tr>
					</thead>
					<tbody id="examResultsAuditBody">
						<c:forEach items="${objPage.result}" var="examResults"
							varStatus="vs">
							<c:choose>
								<c:when
									test="${examResults.checkStatus>=1 and examResults.checkStatus<3}">
									<tr>
										<td><input type="checkbox" name="resourceid"
											value="${examResults.resourceid }" autocomplete="off" /></td>
										<td title="${examResults.studentInfo.branchSchool.unitName }">${examResults.studentInfo.branchSchool.unitCode }-${examResults.studentInfo.branchSchool.unitShortName }</td>
										<td title="${examResults.studentInfo.grade.gradeName }">${examResults.studentInfo.grade.gradeName }</td>
										<td title="${examResults.studentInfo.major.majorName}">${examResults.studentInfo.major.majorName}</td>
										<td title="${examResults.studentInfo.classic.classicName}">${examResults.studentInfo.classic.classicName}</td>
										<td title="${examResults.studentInfo.studyNo}">${examResults.studentInfo.studyNo}</td>
										<td title="${examResults.studentInfo.studentName}">${examResults.studentInfo.studentName}
											<c:if
												test="${examResults.studentInfo.studentStatus eq '16' and 'networkstudy' ne condition['teachType']   }">
												<span style="color: red">[${ghfn:dictCode2Val('CodeStudentStatus',examResults.studentInfo.studentStatus) }]</span>
											</c:if>
										</td>
										<td>${examResults.examCount}</td>
										<%--平时成绩 --%>
										<td><c:choose>
												<c:when test="${examResults.checkStatus eq '2' }">
													<input id="usuallyScore_${examResults.resourceid }"
														type="text" name="usuallyScore_${examResults.resourceid }"
														align="middle" class="number" style="width: 30px" min="0"
														max="100"
														value="${examResults.examResultsAudit.changedUsuallyScore}" />
												</c:when>
												<c:otherwise>
													<input id="usuallyScore_${examResults.resourceid }"
														type="text" name="usuallyScore_${examResults.resourceid }"
														align="middle" class="number" style="width: 30px" min="0"
														max="100" value="${examResults.usuallyScore}" />
												</c:otherwise>
											</c:choose></td>
										<td>
											<%-- 此列显示两种数据 begin--%> <c:choose>
												<%-- A、当考试课程为混合机考的时候显示的是机考成绩--%>
												<c:when test="${condition['examInfo'].ismixture eq 'Y' }">
													<c:choose>
														<c:when test="${examResults.checkStatus eq '2' }">
															<input
																id="writtenMachineScore_${examResults.resourceid }"
																type="text"
																name="writtenMachineScore_${examResults.resourceid }"
																align="middle" class="number" style="width: 30px"
																min="0" max="100"
																value="${examResults.examResultsAudit.changedWrittenMachineScore}" />
														</c:when>
														<c:otherwise>
															<input
																id="writtenMachineScore_${examResults.resourceid }"
																type="text"
																name="writtenMachineScore_${examResults.resourceid }"
																align="middle" class="number" style="width: 30px"
																min="0" max="100"
																value="${examResults.writtenMachineScore}" />
														</c:otherwise>
													</c:choose>
												</c:when>
												<%-- B、否则显示的是卷面成绩 --%>
												<c:otherwise>
													<c:choose>
														<c:when test="${examResults.checkStatus eq '2' }">
															<input id="writtenScore_${examResults.resourceid }"
																type="text"
																name="writtenScore_${examResults.resourceid }"
																align="middle" class="number" style="width: 30px"
																min="0" max="100"
																value="${examResults.examResultsAudit.changedWrittenScore}" />
														</c:when>
														<c:otherwise>
															<input id="writtenScore_${examResults.resourceid }"
																type="text"
																name="writtenScore_${examResults.resourceid }"
																align="middle" class="number" style="width: 30px"
																min="0" max="100" value="${examResults.tempwrittenScore_d}" />
														</c:otherwise>
													</c:choose>
												</c:otherwise>
											</c:choose> <%-- 此列显示两种数据 end--%>
										</td>
										<%-- 期末混合机考增加 笔考成绩 列 --%>
										<c:if test="${condition['examInfo'].ismixture eq 'Y' }">
											<td><c:choose>
													<c:when test="${examResults.checkStatus eq '2' }">
														<input
															id="writtenHandworkScore_${examResults.resourceid }"
															type="text"
															name="writtenHandworkScore_${examResults.resourceid }"
															align="middle" class="number" style="width: 30px"
															range="[0,${examResults.examInfo.mixtrueScorePer }]"
															value="${examResults.examResultsAudit.changedWrittenHandworkScore}" />
													</c:when>
													<c:otherwise>
														<input
															id="writtenHandworkScore_${examResults.resourceid }"
															type="text"
															name="writtenHandworkScore_${examResults.resourceid }"
															align="middle" class="number" style="width: 30px"
															range="[0,${examResults.examInfo.mixtrueScorePer }]"
															value="${examResults.writtenHandworkScore}" />
													</c:otherwise>
												</c:choose></td>
										</c:if>
										<td><c:choose>
												<c:when test="${examResults.checkStatus eq '2' }">
													<input id="integratedScore_${examResults.resourceid }"
														type="text"
														name="integratedScore_${examResults.resourceid }"
														align="middle" class="number" style="width: 30px" min="0"
														max="100"
														value="${examResults.examResultsAudit.changedIntegratedScore}" />
												</c:when>
												<c:otherwise>
													<input id="integratedScore_${examResults.resourceid }"
														type="text"
														name="integratedScore_${examResults.resourceid }"
														align="middle" class="number" style="width: 30px" min="0"
														max="100" value="${examResults.tempintegratedScore_d}" />
												</c:otherwise>
											</c:choose></td>
										<td><c:choose>
												<c:when test="${examResults.checkStatus eq '2' }">
													<gh:select name="examAbnormity_${examResults.resourceid}"
														dictionaryCode="CodeExamAbnormity" choose="N"
														value="${examResults.examResultsAudit.changedExamAbnormity}" />
												</c:when>
												<c:otherwise>
													<gh:select name="examAbnormity_${examResults.resourceid}"
														dictionaryCode="CodeExamAbnormity" choose="N"
														value="${examResults.examAbnormity}" />
												</c:otherwise>
											</c:choose></td>
										<td
											title="${ghfn:dictCode2Val('CodeExamResultCheckStatus',examResults.checkStatus)}"
											<c:choose> <c:when test="${examResults.checkStatus eq '1' }"> style="color: red"  </c:when> <c:when test="${examResults.checkStatus eq '2' }"> style="color:blue"  </c:when> <c:when test="${examResults.checkStatus eq '3' }"> style="color: green"  </c:when></c:choose>>${ghfn:dictCode2Val('CodeExamResultCheckStatus',examResults.checkStatus)}</td>
										<td title="${examResults.checkNotes }"><input type="text"
											name="checkNotes_${examResults.resourceid }"
											style="width: 90%" title="${examResults.checkNotes }"
											value="${examResults.checkNotes }" /></td>
									</tr>
								</c:when>
								<c:otherwise>
									<tr>
										<td></td>
										<td title="${examResults.studentInfo.branchSchool.unitName }">${examResults.studentInfo.branchSchool.unitCode }-${examResults.studentInfo.branchSchool.unitShortName }</td>
										<td title="${examResults.studentInfo.grade.gradeName }">${examResults.studentInfo.grade.gradeName }</td>
										<td title="${examResults.studentInfo.major.majorName}">${examResults.studentInfo.major.majorName}</td>
										<td title="${examResults.studentInfo.classic.classicName}">${examResults.studentInfo.classic.classicName}</td>
										<td title="${examResults.studentInfo.studyNo}">${examResults.studentInfo.studyNo}</td>
										<td title="${examResults.studentInfo.studentName}">${examResults.studentInfo.studentName}</td>
										<td>${examResults.examCount}</td>
										<%--平时成绩 --%>
										<td>${examResults.usuallyScore}</td>
										<td>
											<%-- 此列显示两种数据 --%> <c:choose>
												<%-- A、当考试课程为混合机考的时候显示的是机考成绩--%>
												<c:when test="${condition['examInfo'].ismixture eq 'Y' }">
							            		${examResults.writtenMachineScore}	
							            	</c:when>
												<%-- B、否则显示的是卷面成绩 --%>
												<c:otherwise>
							            		${examResults.writtenScore}
							            	</c:otherwise>
											</c:choose> <%-- 此列显示两种数据 --%>
										</td>
										<%-- 期末混合机考增加 笔考成绩 列 --%>
										<c:if test="${condition['examInfo'].ismixture eq 'Y' }">
											<td>${examResults.writtenHandworkScore}</td>
										</c:if>
										<td><c:choose>
												<c:when
													test="${examResults.checkStatus >= 3 && !(examResults.courseScoreType=='22' || examResults.courseScoreType=='23' || examResults.courseScoreType=='24' || examResults.courseScoreType=='25')}">
						            		${examResults.integratedScore }
						            	</c:when>
												<c:otherwise>
						            		 ${examResults.tempintegratedScore_d}
						            	</c:otherwise>
											</c:choose></td>
										<td>${ghfn:dictCode2Val('CodeExamAbnormity',examResults.examAbnormity) }</td>
										<td
											title="${ghfn:dictCode2Val('CodeExamResultCheckStatus',examResults.checkStatus)}"
											<c:choose> <c:when test="${examResults.checkStatus eq '-1' or examResults.checkStatus  eq '0'  }"> style="color: red"  </c:when> <c:when test="${examResults.checkStatus eq '3' or examResults.checkStatus eq '4' }"> style="color: green"  </c:when></c:choose>>${ghfn:dictCode2Val('CodeExamResultCheckStatus',examResults.checkStatus)}</td>
										<td title="${examResults.checkNotes }">${examResults.checkNotes }</td>
									</tr>
								</c:otherwise>
							</c:choose>
						</c:forEach>
					</tbody>
				</table>
			</form>
			<gh:page page="${objPage}"
				goPageUrl="${baseUrl }/edu3/teaching/result/audit-examresults-list.html"
				pageType="sys" condition="${condition}" />
		</div>
	</div>
	<script type="text/javascript">	

	//审核(将审核通过跟更正合在一起，如果提交未更改的成绩则审核通过，反之进入复审流程)
	function auditPass(){
		var passURL = "${baseUrl}/edu3/teaching/result/examresults-audit-pass.html";
		var auditExamresoultsResourceid = new Array();
		jQuery("#examResultsAuditBody input[name='resourceid']:checked").each(function(){
			auditExamresoultsResourceid.push(jQuery(this).val());
		});
		
		if(auditExamresoultsResourceid.length>0){
			var $form = $("#auditExamResultsSaveForm");
			if (!$form.valid()) {
				alertMsg.error(DWZ.msg["validateFormError"]);
				return false; 
			}
			alertMsg.confirm("确认要审核所选成绩记录？", {
                okCall: function(){
                	$.ajax({
						type:'POST',
						url:passURL,
						data:$form.serializeArray(),
						dataType:"json",
						cache: false,
						error: DWZ.ajaxError,
						success: function(resultData){
							var success  = resultData['success'];
							var msg      = resultData['msg'];
							if(success==false){
								alertMsg.warn(msg);
							}else{
								alertMsg.info(msg);
								navTab.reload($("#auditExamResultsSearchForm").attr("action"), $("#auditExamResultsSearchForm").serializeArray());
							}
						}
					});
                }
            });
		}else{
			alertMsg.warn("请选择需要审核的成绩！");
		}
	}
</script>
</body>
</html>