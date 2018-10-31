<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>成绩发布</title>


</head>
<body>
	<div class="page">
		<div class="pageHeader">
			<form id="publishExamResultsSearchForm"
				onsubmit="return dialogSearch(this);"
				action="${baseUrl}/edu3/teaching/result/publish-examresults-list.html"
				method="post">
				<input type="hidden" name="examSubId"
					value="${condition['examSub'] }" />
				<div class="searchBar">
					<ul class="searchContent">
						<li><label>教学站：</label> <!-- 
					<input type="hidden" id="publish_examresults_brSchoolId" name="branchSchool" size="36" value="${condition['branchSchool']}"/>	
					<input type="text" id="publish_examresults_brSchoolName" name="branchSchoolName" value="${condition['branchSchoolName']}" style="width:120px"/>
					--> <gh:brSchoolAutocomplete name="branchSchool" tabindex="1"
								id="publish_examresults_brSchoolId"
								defaultValue="${condition['branchSchool']}" /></li>
						<li><label>年级：</label>
						<gh:selectModel id="gradeid" name="gradeid" bindValue="resourceid"
								displayValue="gradeName"
								modelClass="com.hnjk.edu.basedata.model.Grade"
								value="${condition['gradeid']}" orderBy="gradeName desc"
								style="width:120px" /></li>
						<li><label>课程名称：</label> <input type="text" name="courseName"
							value="${condition['courseName']}" style="width: 120px" /></li>
						<li><label>课程：</label> <gh:selectModel name="courseId"
								bindValue="resourceid" displayValue="courseName"
								modelClass="com.hnjk.edu.basedata.model.Course"
								value="${condition['courseId']}" condition="status.status='1'"
								style="width:120px" /></li>

						<li><label>学号：</label><input type="text" name="studyNo"
							value="${condition['studyNo']}" style="width: 120px" /></li>

						<li><label>专 业：</label> <gh:selectModel name="major"
								bindValue="resourceid" displayValue="majorCodeName"
								modelClass="com.hnjk.edu.basedata.model.Major"
								value="${condition['major']}" style="width:120px" /></li>
						<li><label>姓名：</label><input type="text" name="stuName"
							value="${condition['name']}" style="width: 120px" /></li>
						<li><label>层 次：</label> <gh:selectModel name="classic"
								bindValue="resourceid" displayValue="classicName"
								modelClass="com.hnjk.edu.basedata.model.Classic"
								value="${condition['classic']}" style="width:120px" /></li>
						<li><label>成绩状态：</label> <gh:select name="checkStatus"
								dictionaryCode="CodeExamResultCheckStatus" choose="Y"
								value="${condition['checkStatus']}" style="width:120px" /></li>
					</ul>
					<div class="subBar">
						<ul>
							<li><div class="buttonActive">
									<div class="buttonContent">
										<button type="submit">查 询</button>
									</div>
								</div></li>
						</ul>
					</div>
				</div>

			</form>
		</div>
		<gh:resAuth parentCode="RES_TEACHING_RESULT_MANAGE"
			pageType="publishSub"></gh:resAuth>
		<div class="pageContent">
			<form id="publishExamResultsSaveForm"
				action="${baseUrl}/edu3/teaching/result/publish-examresults-save.html"
				method="post">
				<input type="hidden" name="examSubId"
					value="${condition['examSub'] }" />
				<table class="table" layouth="195">
					<thead>
						<tr>
							<%--<th width="5%"><input type="checkbox" name="checkall" id="check_all_examResults_publish_save" onclick="checkboxAll('#check_all_examResults_publish_save','resourceid','#examResultsPublishBody')"/></th>
		        	--%>
							<th width="13%">课程名称</th>
							<th width="10%">年级</th>
							<th width="15%">专业</th>
							<th width="10%">层次</th>
							<th width="10%">姓名</th>
							<th width="7%">选考次数</th>
							<th width="7%">卷面成绩</th>
							<th width="7%">平时成绩</th>
							<th width="7%">综合成绩</th>
							<th width="7%">成绩异常</th>
							<th width="7%">成绩状态</th>
						</tr>
					</thead>
					<tbody id="examResultsPublishBody">
						<c:forEach items="${objPage.result}" var="examResults"
							varStatus="vs">
							<tr>
								<%--<td><input type="checkbox" name="resourceid" value="${examResults.resourceid }" autocomplete="off" /></td>
			            --%>
								<td title="${examResults.course.courseName }">${examResults.course.courseName }</td>
								<td title="${examResults.studentInfo.grade.gradeName }">${examResults.studentInfo.grade.gradeName }</td>
								<td title="${examResults.studentInfo.major.majorName}">${examResults.studentInfo.major.majorName}</td>
								<td title="${examResults.studentInfo.classic.classicName}">${examResults.studentInfo.classic.classicName}</td>
								<td title="${examResults.studentInfo.studentName}">${examResults.studentInfo.studentName}</td>
								<td>${examResults.examCount}</td>
								<td>${examResults.writtenScore}</td>
								<td>${examResults.usuallyScore}</td>
								<td>${examResults.integratedScore}</td>
								<td>${ghfn:dictCode2Val('CodeExamAbnormity',examResults.examAbnormity) }</td>
								<td
									title="${ghfn:dictCode2Val('CodeExamResultCheckStatus',examResults.checkStatus)}">${ghfn:dictCode2Val('CodeExamResultCheckStatus',examResults.checkStatus)}</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
				<gh:page page="${objPage}"
					goPageUrl="${baseUrl }/edu3/teaching/result/publish-examresults-list.html"
					pageType="sys" condition="${condition}" />
			</form>
		</div>
	</div>
	<script type="text/javascript">	
	//发布成绩
	function publishExamResults(){
	
	   var examSubId  = $("#publishExamResultsSearchForm input[name='examSubId']").val();
	   var remindurl  = "${baseUrl}/edu3/teaching/result/publish-examresults-remindinfo.html?examSubId="+examSubId;	
	   var publishurl = "${baseUrl}/edu3/teaching/result/publish-examresults-save.html?examSubId="+examSubId;
	   var dwnloadurl = "${baseUrl }/edu3/teaching/examresults/download-file.html?attId="
       $.ajax({
				type:'POST',
				url:remindurl,
				dataType:"json",
				cache: false,
				success: function(resultData){
				   var  remindsuccess   = resultData['success'];
				   var  remindmsg       = resultData['msg'];
				   if(remindsuccess == true){
					   	  alertMsg.confirm(remindmsg,{
		            			 okCall: function(){ 
		            			 	 $.ajax({
		            			 	 		type:'POST',
											url:publishurl,
											dataType:"json",
											cache: false,
											success: function(publishData){
												var returnAttId = publishData['returnAttId'];
												var success     = publishData['success'];
												var msg         = publishData['msg'];
												
												if(success==true && ""!=returnAttId){
													alertMsg.confirm("需要导出发布的成绩吗？", {
		            									 okCall: function(){ 
		            									 	window.open(dwnloadurl+returnAttId);
		            									 }
		            								});
												}else{
													alertMsg.warn(msg);
												}
		
											}
		            			 	 });
		            			 }
	            	   	  });
				   }else{
				   		alertMsg.warn(remindmsg);
				   }
				  
				}
	   });
	   
	}
</script>
</body>
</html>