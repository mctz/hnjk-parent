<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>补考成绩审核</title>


</head>
<body>
	<div class="page">
		<div class="pageHeader">

			<form id="auditMakeupExamResultsSearchForm"
				onsubmit="return navTabSearch(this);"
				action="${baseUrl}/edu3/teaching/result/audit-examresults-makeup-list.html"
				method="post">

				<input id="auditMakeupExamResultsSearchForm_examSub" type="hidden"
					name="examSubId" value="${condition['examSub'] }" /> <input
					id="auditMakeupExamResultsSearchForm_examInfo" type="hidden"
					name="examInfoId" value="${condition['examInfoId'] }" /> <input
					id="auditMakeupExamResultsSearchForm_pageNum" type="hidden"
					name="pageNum" value="${objPage.pageNum}" /> <input
					id="auditMakeupExamResultsSearchForm_course" type="hidden"
					name="courseId" value="${condition['courseId'] }" /> <input
					id="auditMakeupExamResultsSearchForm_class" type="hidden"
					name="classId" value="${condition['classId'] }" /> <input
					id="auditMakeupExamResultsSearchForm_grade" type="hidden"
					name="gradeId" value="${condition['gradeId'] }" /> <input
					id="auditMakeupExamResultsSearchForm_branchSchool" type="hidden"
					name="branchSchool" value="${condition['branchSchool'] }" /> <input
					id="auditMakeupExamResultsSearchForm_major" type="hidden"
					name="major" value="${condition['major'] }" />

				<div class="searchBar">
					<%-- 
			<ul class="searchContent">
			
				<li>
				
					<label>教学站：</label>
					<gh:brSchoolAutocomplete name="branchSchool" tabindex="1" id="audit_makeup_examresults_brSchoolId" displayType="code" defaultValue="${condition['branchSchool']}" style="width:53%" />
				</li>
				<li>
					<label>年级：</label><gh:selectModel id="gradeid" name="gradeId" bindValue="resourceid" displayValue="gradeName" 
														modelClass="com.hnjk.edu.basedata.model.Grade" value="${condition['gradeId']}" 
														orderBy="gradeName desc" style="width:55%" />
				</li>
			</ul>
			<ul class="searchContent">	
				<li>
				
					<label>专业：</label>
					 <gh:selectModel name="major" bindValue="resourceid" displayValue="majorCodeName" modelClass="com.hnjk.edu.basedata.model.Major" orderBy="majorCode asc" value="${condition['major']}" style="width:55%"  />
				</li>
		
				<li>
					<label>学号：</label><input  type="text" name="studyNo" value="${condition['studyNo']}"  style="width:53%"  />
				</li>
				<li>
					<label>姓名：</label><input  type="text" name="name" value="${condition['name']}"  style="width:53%" />
			
				</li>
			</ul>--%>
					<ul class="searchContent">
						<li>
							<%-- 
					<label>成绩状态：</label>
					<gh:select name="checkStatus"  dictionaryCode="CodeExamResultCheckStatus" choose="Y" value="${condition['checkStatus']}" style="width:55%" />
				</li>
					<li>
					<label>排序：</label>
					<select style="width:55%" name="orderBy" id="orderBy">
						<option value="">请选择</option>
						<option value="studyNoAsc" <c:if test="${condition['orderBy'] eq 'studyNoAsc' }">selected="selected"</c:if>>按学号升序</option>
						<option value="studyNoDesc" <c:if test="${condition['orderBy'] eq 'studyNoDesc' }">selected="selected"</c:if>>按学号降序</option>
						<option value="scoreAsc" <c:if test="${condition['orderBy'] eq 'scoreAsc' }">selected="selected"</c:if>>按综合成绩升序</option>
						<option value="scoreDesc" <c:if test="${condition['orderBy'] eq 'scoreDesc' }">selected="selected"</c:if>>按综合成绩降序</option>
						<option value="wscoreAsc" <c:if test="${condition['orderBy'] eq 'wscoreAsc' }">selected="selected"</c:if>>按卷面成绩升序</option>
						<option value="wscoreDesc" <c:if test="${condition['orderBy'] eq 'wscoreDesc' }">selected="selected"</c:if>>按卷面成绩降序</option>
					</select>
					--%>
						</li>
						<%-- 
				<div class="buttonActive" style="float:right"><div class="buttonContent"><button type="submit"> 查 询 </button></div></div>				
			--%>
					</ul>
				</div>

			</form>

		</div>
		<gh:resAuth parentCode="RES_TEACHING_RESULT_MANAGE_MAKEUP"
			pageType="auditSub"></gh:resAuth>
		<div class="pageContent">
			<form id="auditMakeupExamResultsSaveForm"
				action="${baseUrl}/edu3/teaching/result/examresults-audit-pass-makeup.html"
				method="post">
				<input id="auditMakeupExamResultsSaveForm_examSubId" type="hidden"
					name="examSubId" value="${condition['examSub'] }" /> <input
					id="auditMakeupExamResultsSaveForm_examInfoId" type="hidden"
					name="examInfoId" value="${condition['examInfoId'] }" /> <input
					id="auditMakeupExamResultsSearchForm_courseId" type="hidden"
					name="courseId" value="${condition['courseId'] }" /> <input
					id="auditMakeupExamResultsSearchForm_classId" type="hidden"
					name="classId" value="${condition['classId'] }" /> <input
					id="auditMakeupExamResultsSearchForm_branchSchoolId" type="hidden"
					name="branchSchool" value="${condition['branchSchool'] }" /> <input
					id="auditMakeupExamResultsSearchForm_gradeId" type="hidden"
					name="gradeId" value="${condition['gradeId'] }" /> <input
					id="auditMakeupExamResultsSearchForm_majorId" type="hidden"
					name="major" value="${condition['major'] }" />
				<table class="table" layouth="112" width="100%">
					<thead>
						<th width="4%"><input type="checkbox" name="checkall"
							id="check_all_examResults_audit_save"
							onclick="checkboxAll('#check_all_examResults_audit_save','resourceid','#makeupExamResultsAuditBody')" /></th>
						<th width="10%">教学站</th>
						<th width="6%">年级</th>
						<th width="14%">专业</th>
						<th width="6%">层次</th>
						<th width="11%">学号</th>
						<th width="6%">姓名</th>
						<th width="6%">卷面成绩</th>
						<th width="6%">综合成绩</th>
						<th width="6%">成绩异常</th>
						<th width="6%">成绩状态</th>
						<th width="10%">审核意见</th>
						</tr>
					</thead>
					<tbody id="makeupExamResultsAuditBody">
						<c:forEach items="${objPage.result}" var="examResults"
							varStatus="vs">
							<c:choose>
								<c:when
									test="${examResults.checkStatus>=1 and examResults.checkStatus<3 }">
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
										<td>${examResults.writtenScore}</td>
										<td>${examResults.integratedScore}</td>
										<td>
											${ghfn:dictCode2Val('CodeExamAbnormity',examResults.examAbnormity) }
										</td>
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
										<td>${examResults.writtenScore}</td>
										<td>${examResults.integratedScore}</td>
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
				goPageUrl="${baseUrl }/edu3/teaching/result/audit-examresults-makeup-list.html"
				pageType="sys" condition="${condition}" />
		</div>
	</div>
	<script type="text/javascript">	

	//审核(将审核通过跟更正合在一起，如果提交未更改的成绩则审核通过，反之进入复审流程)
	function makeupAuditPass(){
		var passURL = "${baseUrl}/edu3/teaching/result/examresults-audit-pass-makeup.html";
		var auditExamresoultsResourceid = new Array();
		jQuery("#makeupExamResultsAuditBody input[name='resourceid']:checked").each(function(){
			auditExamresoultsResourceid.push(jQuery(this).val());
		});
		
		if(auditExamresoultsResourceid.length>0){
			var $form = $("#auditMakeupExamResultsSaveForm");
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
								navTab.reload($("#auditMakeupExamResultsSearchForm").attr("action"), $("#auditMakeupExamResultsSearchForm").serializeArray());
							}
						}
					});
                }
            });
		}else{
			alertMsg.warn("请选择审核不通过的成绩！");
		}
	}
</script>
</body>
</html>