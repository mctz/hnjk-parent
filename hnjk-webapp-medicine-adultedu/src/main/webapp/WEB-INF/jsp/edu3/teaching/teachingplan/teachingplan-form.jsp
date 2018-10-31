<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>教学计划</title>
</head>
<body>
	<script type="text/javascript">
	function _addTeachingCourse(){//增加课程
		var teachingPlanId = $('#_teachingPlanForm_teachingPlanId').val();
		$.pdialog.open('${baseUrl }/edu3/framework/teaching/teachingplan/teachingcourse/edit.html?teachingPlanId='+teachingPlanId,
				'teachingPlanCourse','新增教学计划课程',{width:650,height:400});
	}
	
	function _editTeachingCourse(){//修改课程
		if(!isChecked('teachingCourseId','#_teachingCourseBody')){
 			alertMsg.warn('请选择一条要操作记录！');
			return false;
 		}
		var teachingPlanId = $('#_teachingPlanForm_teachingPlanId').val();
		var teachingCourseId = $("#_teachingCourseBody input[@name='teachingCourseId']:checked").val();
		$.pdialog.open('${baseUrl }/edu3/framework/teaching/teachingplan/teachingcourse/edit.html?teachingPlanId='+teachingPlanId+'&teachingCourseId='+teachingCourseId,
				'teachingPlanCourse','编辑教学计划课程',{width:650,height:400});
	}
	
	function _deleteTeachingCourse(){//删除课程
		var teachingPlanId = $('#_teachingPlanForm_teachingPlanId').val();;		
		if(!isChecked('teachingCourseId','#_teachingCourseBody')){
 			alertMsg.warn('请选择一条要操作记录！');
			return false;
 		}
		alertMsg.confirm("您确定要删除这些记录吗？", {
				okCall: function(){//执行			
					var res = "";
					var k = 0;
					var num  = $("#_teachingCourseBody input[name='teachingCourseId']:checked").size();
					$("#_teachingCourseBody input[@name='teachingCourseId']:checked").each(function(){
	                        res+=$(this).val();
	                        if(k != num -1 ) res += ",";
	                        k++;
	                    })
	                
					$.post('${baseUrl}/edu3/framework/teaching/teachingplan/teachingcourse/delete.html',{teachingCourseId:res,teachingPlanId:teachingPlanId}, _teachingPlannavTabAjaxDone, "json");
				}
		});	
		
	}
	
	function _teachingPlannavTabAjaxDone(json){
		DWZ.ajaxDone(json);		
		if (json.statusCode == 200){
			if(json.dialog){
				$.pdialog.close(json.dialog);
			}
			if ("forward" == json.callbackType) {
				navTab.reload(json.forwardUrl,null,json.navTabId);
			}
		}
	}
	
	//关联教材
	function _relTeachingCourseBook(){
		var teachingPlanId = $('#_teachingPlanForm_teachingPlanId').val();
		var teachingCourseId = $("#_teachingCourseBody input[@name='teachingCourseId']:checked").val();
		if(isCheckOnlyone('teachingCourseId','#_teachingCourseBody')){ 		
			$.pdialog.open('${baseUrl }/edu3/framework/teaching/teachingplan/teachingcourse/rel.html?teachingPlanId='+teachingPlanId+'&teachingCourseId='+teachingCourseId,
					'teachingPlanCourseBook','编辑关联课程',{width:800,height:600});
 		}
		return false;
		
	}
	//清空指定的教学站
	function clearnBrschool(){
		$("#brSchoolId").val("");
		$("#brSchoolName").val("");
	}
	//修改教学计划版本
	function editNum(){
		var id=$("#_teachingPlanForm_teachingPlanId").val();
		var num=$("#vserion").val();
		$.ajax({
	          type:"POST",
	          url:"${baseUrl}/edu3/framework/teaching/teachingplan/editNum.html",
	          data:{id:id,num:num},
	          dataType:'json',       
	          success:function(date){   
	        	  
	        	  if(date.success=='Y'){
	        		  $("#vserion").attr("value",date.sum);
	        	  }
	        	  alertMsg.correct(date.msg);
	         		        
	          }            
		});
		
	}
	$(document).ready(function(){
		$("select[class*=flexselect]").flexselect();
	});
	</script>
	<h2 class="contentTitle">
		编辑教学计划
		<c:if test="${not empty teachingPlan.resourceid }">: ${teachingPlan.major } - ${teachingPlan.classic } (<input
				type="text" value="${teachingPlan.versionNum}" id="vserion" size=2
				name="vserion">)<input type="button" onclick="editNum()"
				value="确定">
		</c:if>
	</h2>
	<div class="page">
		<div class="pageContent">
			<form method="post"
				action="${baseUrl}/edu3/teaching/teachingplan/save.html"
				class="pageForm" onsubmit="return validateCallback(this);">
				<input type="hidden" name="resourceid"
					id="_teachingPlanForm_teachingPlanId"
					value="${teachingPlan.resourceid }" /> <input type="hidden"
					name="fillinDate"
					value="<fmt:formatDate value="${teachingPlan.fillinDate }" pattern="yyyy-MM-dd"/>" />
				<input type="hidden" name="versionNum"
					value="${teachingPlan.versionNum }" />

				<div class="pageFormContent" layoutH="97">
					<div class="tabs"
						<c:if test="${tabIndex == 1 }">currentIndex=1</c:if>>
						<div class="tabsHeader">
							<div class="tabsHeaderContent">
								<ul>
									<li><a href="#"><span>计划信息</span></a></li>
									<c:if test="${ not empty teachingPlan.resourceid }">
										<li class="#"><a href="#"><span>课程信息</span></a></li>
									</c:if>
								</ul>
							</div>
						</div>
						<div class="tabsContent" style="height: 100%;">
							<!-- 1 -->
							<div>
								<table class="form">
									<tr>
										<td width="15%">办学模式:</td>
										<td><gh:select name="schoolType"
												value="${teachingPlan.schoolType}"
												dictionaryCode="CodeTeachingType" /></td>
										<td width="15%">计划名称:</td>
										<td width="35%"><input type="text" name="planName"
											style="width: 50%" value="${teachingPlan.planName }"
											class="required" /></td>
										<!--							<td width="15%">教学中心：</td>-->
										<!--							<td width="35%">-->
										<!--							<input type="hidden" id="brSchoolId" name="brSchoolId" value="${teachingPlan.orgUnit.resourceid}"/>								-->
										<!--							<input type="text" id="brSchoolName" readonly="true" value="${teachingPlan.orgUnit.unitName}" style="width: 50%;"/>&nbsp;&nbsp;-->
										<!--								<c:if test="${isBrschool != 'brSchool' }">-->
										<!--								<a href="#" class="button" onclick="javascript:$.pdialog.open('${baseUrl }/edu3/framework/selector/org.html?parentUnitCode=${parentUnitCode }&checkBoxType=radio&idsN=brSchoolId&namesN=brSchoolName&checkedValues='+$('#brSchoolId').val(),'selector','选择组织',{mask:true,width:700,height:500});"><span>选择教学站</span></a>-->
										<!--								<a href="#" class="button" onclick="clearnBrschool()"><span>清空</span></a>	-->
										<!--								</c:if>-->
										<!--							</td>						-->
									</tr>
									<tr>
										<td width="15%">专业:</td>
										<td width="35%"><gh:selectModel name='majorId'
												bindValue='resourceid' displayValue='majorCode,majorName'
												style='width:80%' classCss='flexselect'
												modelClass='com.hnjk.edu.basedata.model.Major'
												value='${teachingPlan.major.resourceid }'
												condition='isDeleted=0' orderBy='majorCode' /></td>
										<td width="15%">层次:</td>
										<td width="35%"><gh:selectModel name='classicId'
												bindValue='resourceid' displayValue='classicName'
												style='width:80%'
												modelClass='com.hnjk.edu.basedata.model.Classic'
												value='${teachingPlan.classic.resourceid }'
												condition='isDeleted=0' /></td>
									</tr>
									<tr>
										<td width="15%">毕业论文申请最低学分：</td>
										<td width="35%"><input type="text"
											name="applyPaperMinResult" style="width: 50%"
											value="<fmt:formatNumber value="${teachingPlan.applyPaperMinResult }" pattern="#.#"/>"
											class="required decimal" /> <font color="green">专升本适用</font>
										</td>
										<td width="15%">毕业最低学分:</td>
										<td width="35%"><input type="text" name="minResult"
											style="width: 50%"
											value="<fmt:formatNumber value="${teachingPlan.minResult }" pattern="#.#"/>"
											class="required decimal" /></td>
									</tr>
									<tr>
										<td width="15%">选修课修读门数：</td>
										<td width="35%"><input type="text"
											name="optionalCourseNum" style="width: 50%"
											value="${teachingPlan.optionalCourseNum }" class="digits" />
											<font color="green">专升本适用</font></td>
										<td width="15%">学位授予:</td>
										<td width="35%"><gh:select name="degreeName"
												value="${teachingPlan.degreeName}"
												dictionaryCode="CodeDegree" /> <font color="green">专升本适用</font>
										</td>
									</tr>
									<tr id="degreeConditionTR"
										<c:if test="${ (empty teachingPlan.degreeName) or (teachingPlan.degreeName eq '') }"> style="display:none" </c:if>>
										<td>学位授予条件:</td>
										<td colspan="3">

											<p>
												<input type="checkbox" name="checkall"
													id="check_all_degreecondition"
													onclick="checkboxAll('#check_all_degreecondition','degreeRules','#_degreeconditionBody')" />
												全选
											</p>
											<div id="_degreeconditionBody">
												<c:forEach items="${degreeList }" var="degreeCondition"
													varStatus="vs">
													<span style="padding-right: 20px"><input
														type="checkbox" name="degreeRules"
														value="${degreeCondition.dictValue }"
														<c:forTokens items="${teachingPlan.degreeRules}" delims="," var="degreeRule" varStatus="status">
										<c:if test="${degreeRule eq degreeCondition.dictValue }">checked</c:if>
									 </c:forTokens> />
														${ degreeCondition.dictName}</span>
												</c:forEach>
											</div>
										</td>
									</tr>
									<tr>
										<td>培养目标:</td>
										<td colspan="3"><textarea name="trainingTarget" rows="5"
												cols="" style="width: 50%" class="required">${teachingPlan.trainingTarget }</textarea>
										</td>
									</tr>
									<tr>
										<td>学制:</td>
										<td><input type="text" name="eduYear" style="width: 50%"
											value="${teachingPlan.eduYear }" class="required number" /></td>
										<td>结业最低分:</td>
										<td><input type="text" name="theGraduationScore"
											style="width: 50%"
											value="<fmt:formatNumber value="${teachingPlan.theGraduationScore }" pattern="#.#"/>"
											class="required decimal" /></td>
									</tr>
									<tr>
										<td>主干课程:</td>
										<td colspan="3">${teachingPlan.mainCourse }</td>
									</tr>
									<tr>
										<td>修读说明:</td>
										<td colspan="3"><textarea name="learningDescript"
												rows="5" cols="" style="width: 50%" class="required">${teachingPlan.learningDescript }</textarea>
										</td>
									</tr>
									<tr>
										<td>备注:</td>
										<td colspan="3"><textarea name="memo" rows="5" cols=""
												style="width: 50%">${teachingPlan.memo }</textarea></td>
									</tr>
								</table>
							</div>
							<!-- 2 -->
							<c:if test="${ not empty teachingPlan.resourceid }">
								<div>
									<div class="pageContent">
										<span class="buttonActive"><div class="buttonContent">
												<button type="button" onclick="_addTeachingCourse()">增加课程</button>
											</div></span> <span class="buttonActive"><div class="buttonContent">
												<button type="button" onclick="_editTeachingCourse()">编辑课程</button>
											</div></span> <span class="buttonActive"><div class="buttonContent">
												<button type="button" onclick="_deleteTeachingCourse()">删除课程</button>
											</div></span> <span class="buttonActive"><div class="buttonContent">
												<button type="button" onclick="_relTeachingCourseBook()">管理教材</button>
											</div></span>
										<table class="table" layouth="138">
											<thead>
												<tr>
													<th style="width: 4%">&nbsp;</th>
													<th style="width: 6%">课程类型</th>
													<th style="width: 6%">课程编码</th>
													<th style="width: 12%">课程名称</th>
													<th style="width: 7%">课程类别</th>
<!-- 													<th style="width: 7%">教学方式</th> -->
<!-- 													<th style="width: 10%">前置课程名称</th> -->
													<th style="width: 12%">教材</th>
													<th style="width: 5%">总学时</th>
													<th style="width: 5%">面授学时</th>
													<th style="width: 5%">实验学时</th>
													<th style="width: 5%">学分</th>
													<th style="width: 7%">是否主干课程</th>
													<th style="width: 7%">建议学期</th>
												</tr>
											</thead>
											<tbody id="_teachingCourseBody">
												<c:forEach items="${teachingPlan.teachingPlanCourses }"
													var="c" varStatus="vs">
													<tr id='tr${c.courseType}'>
														<td>${vs.index +1}<input type='checkbox'
															name='teachingCourseId' value='${c.resourceid }'
															autocomplete="off" /></td>
														<td>${ghfn:dictCode2Val('CodeCourseType',c.courseType) }</td>
														<td>${c.course.courseCode }</td>
														<td>${c.course.courseName }</td>
														<td>${ghfn:dictCode2Val('courseNature',c.courseNature) }</td>
<%-- 														<td>${ghfn:dictCode2Val('teachType',c.teachType) }</td> --%>
<%-- 														<td>${c.beforeCourse.courseName }</td> --%>
														<td><a href="${baseUrl }/edu3/sysmanager/textbook/viewTextBook.html?resourceid=${c.resourceid}" target="dialog" title="查看教材信息" width="700" >查看</a></td>
														<td>${c.stydyHour }</td>
														<td>${c.faceStudyHour }</td>
														<td>${c.experimentPeriod }</td>
														<td>${c.creditHour }</td>
														<td>${ghfn:dictCode2Val('yesOrNo',c.isMainCourse) }</td>
														<td>${ghfn:dictCode2Val('CodeTermType',c.term) }</td>

													</tr>
												</c:forEach>
												<tr>
													<td>总值:</td>
													<td colspan="6"></td>
													<td>${sum }</td>
													<td>${ms }</td>
													<td>${ep }</td>
													<td>${xf }</td>
													<td colspan="7"></td>
												</tr>
											</tbody>
										</table>
									</div>
								</div>
							</c:if>
						</div>
						<div class="tabsFooter">
							<div class="tabsFooterContent"></div>
						</div>
					</div>
				</div>
				<div class="formBar">
					<ul>
						<li><div class="buttonActive">
								<div class="buttonContent">
									<button type="submit">提交</button>
								</div>
							</div></li>
						<li><div class="button">
								<div class="buttonContent">
									<button type="button" class="close"
										onclick="navTab.closeCurrentTab();">取消</button>
								</div>
							</div></li>
					</ul>
				</div>
			</form>
		</div>
	</div>
</body>

<script type="text/javascript">

/*
	var trHtml = "<td><select name='course'><option value=''>请选择...</option><c:forEach items='${courseList }' var='course' varStatus='vs'><option value='${course.resourceid }' <c:if test='${course.resourceid eq c.course.resourceid }'> selected </c:if> title='${course.courseName }'><c:choose><c:when test='${fn:length(course.courseName)>10 }'>${fn:substring(course.courseName,0,10)}...</c:when><c:otherwise> ${course.courseName }</c:otherwise></c:choose></option></c:forEach></select></td>"+
    	"<td><gh:select name='courseNature' dictionaryCode='courseNature' style='width:80%' value='${c.courseNature }'/></td>"+
    	"<td><select name='beforeCourse'><option value=''>请选择...</option><c:forEach items='${courseList }' var='course' varStatus='vs'><option value='${course.resourceid }' <c:if test='${course.resourceid eq c.beforeCourse.resourceid }'> selected </c:if> title='${course.courseName }'><c:choose><c:when test='${fn:length(course.courseName)>10 }'>${fn:substring(course.courseName,0,10)}...</c:when><c:otherwise> ${course.courseName }</c:otherwise></c:choose></option></c:forEach></select></td>"+
    	"<td><input type='text' name='stydyHour' value='' style='width:80%'></td>"+
    	"<td><input type='text' name='creditHour' value='' style='width:80%'></td>"+
    	"<td><gh:select name='isMainCourse' dictionaryCode='yesOrNo' style='width:80%'/> </td>"+
    	"<td><gh:select name='term' dictionaryCode='CodeTermType' style='width:80%'/></td>"+
    	"<td><input type='text' name='c_memo' value='' style='width:80%'><input type='hidden' name='hc_id' value=''></td></tr>";
    
    function addCourseTab11(){
    	var html = "<tr id='tr11'><td><input type='checkbox' name='c_id' value=''></td><td><gh:select name='courseType' dictionaryCode='CodeCourseType' style='width:80%' value='11'/></td>"+trHtml;
    	if($("#tr11").size()==0){
    		jQuery("#courseTab").append(html);
    	}else{
    		jQuery("tr[id='tr11']:last").after(html);    		
    	}
    	generateIndex();
    }
    function addCourseTab22(){
    	var html = "<tr id='tr22'><td><input type='checkbox' name='c_id' value=''></td><td><gh:select name='courseType' dictionaryCode='CodeCourseType' style='width:80%' value='22'/></td>"+trHtml;
    	if($("#tr22").size()==0){
    		jQuery("#courseTab").append(html);    		
    	}else{
    		jQuery("tr[id='tr22']:last").after(html);
    	}
    	generateIndex();
    }
    function addCourseTab33(){
    	var html = "<tr id='tr33'><td><input type='checkbox' name='c_id' value=''></td><td><gh:select name='courseType' dictionaryCode='CodeCourseType' style='width:80%' value='33'/></td>"+trHtml;
    	if($("#tr33").size()==0){
    		jQuery("#courseTab").append(html);  
    	}else{
    		jQuery("tr[id='tr33']:last").after(html);
    	}
    	generateIndex();
    }
    function addCourseTab44(){
    	var html = "<tr id='tr44'><td><input type='checkbox' name='c_id' value=''></td><td><gh:select name='courseType' dictionaryCode='CodeCourseType' style='width:80%' value='44'/></td>"+trHtml;
    	if($("#tr44").size()==0){
    		jQuery("#courseTab").append(html);    		
    	}else{
    		jQuery("tr[id='tr44']:last").after(html);
    	}
    	generateIndex();
    }
    function addCourseTab55(){
    	var html = "<tr id='tr55'><td><input type='checkbox' name='c_id' value=''></td><td><gh:select name='courseType' dictionaryCode='CodeCourseType' style='width:80%' value='55'/></td>"+trHtml;
    	if($("#tr55").size()==0){
    		jQuery("#courseTab").append(html);    		
    	}else{
    		jQuery("tr[id='tr55']:last").after(html);
    	}
    	generateIndex();
    }
    function addCourseTab66(){
    	var html = "<tr id='tr66'><td><input type='checkbox' name='c_id' value=''></td><td><gh:select name='courseType' dictionaryCode='CodeCourseType' style='width:80%' value='66'/></td>"+trHtml;
    	if($("#tr66").size()==0){
    		jQuery("#courseTab").append(html);    		
    	}else{
    		jQuery("tr[id='tr66']:last").after(html);
    	}
    	generateIndex();
    }
    function delCourseTab(){
    	$("#courseTab input[name='c_id']:checked").each(function(ind){
		  	var url = "${baseUrl}/edu3/teaching/teachingplan/delete.html";
		  	$.get(url, {c_id:$(this).val()});
		  	$(this).parent().parent().remove();
		});
		generateIndex();
    }
    function generateIndex(){
    	$("#courseTab input[name='c_id']").each(function(ind){
    		$("#courseTab input[name='showOrder']:eq("+ind+")").val(ind+1);
    	})
    }
   */
</script>

</html>