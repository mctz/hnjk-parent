<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>申请缓考</title>


</head>
<body>
	<div class="page">
		<div class="pageHeader">
			<form id="delayExamSearchForm" onsubmit="return navTabSearch(this);"
				action="${baseUrl}/edu3/teaching/exam/delayExam-list.html"
				method="post">
				<div class="searchBar">
					<ul class="searchContent">
						<c:if test="${condition['isBranchSchool'] !=true}">
							<li><label>教学站：</label> <%-- 
								<gh:selectModel id="branchSchool" name="branchSchool" bindValue="resourceid" displayValue="unitName" style="width:120px"
								modelClass="com.hnjk.security.model.OrgUnit" value="${condition['branchSchool']}"  condition="unitType='brSchool'"/>--%>
								<gh:brSchoolAutocomplete name="branchSchool" tabindex="1"
									id="examDelay_list_brSchoolName"
									defaultValue="${condition['branchSchool']}" displayType="code"
									style="width:55%" /></li>
						</c:if>
						<li><label>年级：</label>
						<gh:selectModel id="gradeid" name="gradeid" bindValue="resourceid"
								displayValue="gradeName"
								modelClass="com.hnjk.edu.basedata.model.Grade"
								value="${condition['gradeid']}" orderBy="gradeName desc"
								style="width:55%" /></li>
						<li><label>专业：</label>
						<gh:selectModel id="major" name="major" bindValue="resourceid"
								displayValue="majorCodeName" value="${condition['major']}"
								modelClass="com.hnjk.edu.basedata.model.Major"
								orderBy="majorCode" style="width:55%" /></li>
					</ul>
					<ul class="searchContent">
						<li><label>层次：</label>
						<gh:selectModel id="classic" name="classic" bindValue="resourceid"
								displayValue="classicName"
								modelClass="com.hnjk.edu.basedata.model.Classic"
								value="${condition['classic']}" style="width:55%" /></li>
						<li><label>姓名：</label><input type="text" id="name"
							name="name" value="${condition['name']}" style="width: 55%" /></li>
						<li><label>学号：</label><input type="text" id="studyNo"
							name="studyNo" value="${condition['studyNo']}" style="width: 55%" />
						</li>

					</ul>
					<div class="buttonActive" style="float: right">
						<div class="buttonContent">
							<button type="submit">查 询</button>
						</div>
					</div>
				</div>
			</form>
		</div>
		<div class="pageContent">
			<gh:resAuth parentCode="RES_TEACHING_EXAM_ORDER" pageType="list"></gh:resAuth>
			<table class="table" layouth="161">
				<thead>
					<tr>
						<th width="5%"><input type="checkbox" name="checkall"
							id="check_all_examdelay_info"
							onclick="checkboxAll('#check_all_examdelay_info','resourceid','#examDelayBody')" /></th>
						<th width="10%">姓名</th>
						<th width="10%">学号</th>
						<th width="15%">教学站</th>
						<th width="10%">培养层次</th>
						<th width="5%">年级</th>
						<th width="10%">专业</th>
						<th width="13%">课程名称</th>
						<th width="17%">考试时间</th>
						<th width="5%">是否缓考</th>
					</tr>
				</thead>

				<tbody id="examDelayBody">
					<c:forEach items="${resultPage.result}" var="examResult"
						varStatus="vs">
						<tr>
							<td><input type="checkbox" name="resourceid"
								value="${examResult.resourceid }" autocomplete="off" /></td>
							<td><a href="#"
								onclick="viewStuInfo2('${examResult.studentInfo.resourceid}')"
								title="点击查看">${examResult.studentInfo.studentName }</a></td>
							<td>${examResult.studentInfo.studyNo}</td>
							<td>${examResult.studentInfo.branchSchool.unitName}</td>
							<td>${examResult.studentInfo.classic.classicName}</td>
							<td>${examResult.studentInfo.grade.gradeName}</td>
							<td>${examResult.studentInfo.major.majorName }</td>
							<td>${examResult.course.courseName}</td>
							<td><font color="green"><fmt:formatDate
										value="${examResult.examStartTime}"
										pattern="yyyy-MM-dd HH:mm:ss" type="date" /></font> - <font
								color="red"><fmt:formatDate
										value="${examResult.examEndTime}" pattern="HH:mm:ss"
										type="date" /></font></td>
							<td><c:if test="${examResult.isDelayExam eq 'Y' }">是</c:if>
								<c:if test="${examResult.isDelayExam eq 'N' }">否</c:if></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<gh:page page="${resultPage}"
				goPageUrl="${baseUrl }/edu3/teaching/exam/delayExam-list"
				pageType="sys" condition="${condition}" />
		</div>

	</div>
	<script type="text/javascript">
	//设定缓考
	function delayExamorder(){
		var branchSchool = jQuery("#delayExamSearchForm #branchSchool").val();
		var gradeid      = jQuery("#delayExamSearchForm #gradeid").val();
		var major        = jQuery("#delayExamSearchForm #major").val();
		var classic      = jQuery("#delayExamSearchForm #classic").val();
		var name         = jQuery("#delayExamSearchForm #name").val();
		var studyNo      = jQuery("#delayExamSearchForm #studyNo").val();

		var ids = new Array();
		jQuery("#examDelayBody input[name='resourceid']:checked").each(function(){
			ids.push(jQuery(this).val());
		})
		if(ids.length==0){
			 alertMsg.warn("请选择要缓考的课程!"); 
		}else{
			var url="${baseUrl}/edu3/teaching/exam/delayExam-save.html"
			
			alertMsg.confirm("确定要为所选择的学员申请缓考吗？", {
                okCall: function(){
                	
                	jQuery.ajax({
        				type:"post",
        				data:"examResultIds="+ids.toString()+"&operaterType=examDelay"+"&branchSchool="+branchSchool
        									  +"&gradeid="+gradeid+"&major="+major+"&classic="+classic
        									  +"&studyNo="+studyNo+"&name="+name,
        				url:url,
        				dataType :"json",
        				success:function(resultDate){
        					if(resultDate['isSuccess']==true){
        						navTab.reload("${baseUrl}/edu3/teaching/exam/delayExam-list.html?branchSchool="+resultDate['branchSchool']
        									  +"&gradeid="+resultDate['gradeid']+"&major="+resultDate['major']+"&classic="+resultDate['classic']
        									  +"&name="+resultDate['name']+"&studyNo="+resultDate['studyNo']);
        						alertMsg.correct("操作成功！");
        						
        					}else{
        						alertMsg.warn(resultDate['msg']);
        					}
        				}
        			});
                }
			});
			
		}	 
	}
	//解除缓考
	function undelayExamorder(){
		var branchSchool = jQuery("#delayExamSearchForm #branchSchool").val();
		var gradeid      = jQuery("#delayExamSearchForm #gradeid").val();
		var major        = jQuery("#delayExamSearchForm #major").val();
		var classic      = jQuery("#delayExamSearchForm #classic").val();
		var name         = jQuery("#delayExamSearchForm #name").val();
		var studyNo      = jQuery("#delayExamSearchForm #studyNo").val();
		
		var ids = new Array();
		jQuery("#examDelayBody input[name='resourceid']:checked").each(function(){
			ids.push(jQuery(this).val());
		})
		if(ids.length==0){
			 alertMsg.warn("请选择要解除缓考的课程!"); 
		}else{
			var url="${baseUrl}/edu3/teaching/exam/delayExam-save.html"
			
			alertMsg.confirm("确定要为所选择的学员解除缓考吗？", {
                okCall: function(){
                	
                	jQuery.ajax({
        				type:"post",
        				data:"examResultIds="+ids.toString()+"&operaterType=disExamDelay"+"&branchSchool="+branchSchool
        									  +"&gradeid="+gradeid+"&major="+major+"&classic="+classic
        									  +"&studyNo="+studyNo+"&name="+name,
        				url:url,
        				dataType :"json",
        				success:function(resultDate){
        					if(resultDate['isSuccess']==true){
        						navTab.reload("${baseUrl}/edu3/teaching/exam/delayExam-list.html?branchSchool="+resultDate['branchSchool']
        									  +"&gradeid="+resultDate['gradeid']+"&major="+resultDate['major']+"&classic="+resultDate['classic']
        									  +"&name="+resultDate['name']+"&studyNo="+resultDate['studyNo']);
        						alertMsg.correct("操作成功！");
        					}else{
        						alertMsg.warn(resultDate['msg']);
        					}
        				}
        			});
                }
			});
			
			
		}	
	}
	//查看学生信息
	function viewStuInfo2(id){
		var url = "${baseUrl}/edu3/framework/studentinfo/view.html";
		$.pdialog.open(url+'?act=view&resourceid='+id, 'RES_SCHOOL_SCHOOLROLL_MANAGER_VIEW', '查看学籍', {width: 800, height: 600});　
	}

</script>
</body>
</html>