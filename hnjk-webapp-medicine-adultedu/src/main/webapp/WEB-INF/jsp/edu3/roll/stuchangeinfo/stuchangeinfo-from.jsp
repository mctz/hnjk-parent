<%@page contentType="text/html;charset=UTF-8"%>
<%@include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>学籍异动</title>
<script type="text/javascript">	 
	jQuery(document).ready(function(){    
		  //判断显示区域
		  /*
		  停学	06
		休学	11
		复学	12
		退学	13
		转专业	23
		更改学习方式	81
		更改层次	83
		更改年级	84
		其他	99
		更改教学点	82
		*/
		  var changeType = '${stuChangeInfo.changeType}';
		  if(changeType == '23'){
			  $("#changeTab #33").show();
		  }else if(changeType == '81'){
			  $("#changeTab #66").show();
		  }else if(changeType == '83'){
			  $("#changeTab #44").show();
		  }else if(changeType == '82'){
			  $("#changeTab #55").show();
		  }
	  });
	function saveChange(){
		jQuery("#CM").val("0");
	}
	
	function submitChange(){
		jQuery("#CM").val("1");
	}
	
	function stuchangeinfo_submitForm(type,callback){
		var f =  document.getElementById("stuchangeinfoForm");
		f.action += "?CM="+type;	;
		return validateCallback(f,callback);
	}
	
	function stuchangeinfo_getBack(){
		
	}	
	
	
	function queryStuInfo(){
		var stuNum = jQuery("#changeTab").find("#stuNum").val();
		if(stuNum!=""){
			jQuery.ajax({
				data:"stuNum="+stuNum,
				dataType:"json",
				url:"${baseUrl}/edu3/schoolroll/awardspun/querystuinfo.html",
				success:function(data){
					if(data.error && data.error!=""){
						alertMsg.warn(data.error);
					}else{
						jQuery("#changeTab input[name='stuName']").val(data.studentName);
						jQuery("#changeTab input[name='stuCenter']").val(data.schoolCenter);
						jQuery("#changeTab input[name='major']").val(data.major);
						jQuery("#changeTab input[name='classic']").val(data.classic);
						jQuery("#changeTab input[name='grade']").val(data.grade);
						jQuery("#changeTab input[name='gradeId']").val(data.gradeId);					
						jQuery("#changeTab input[name='changeBeforeLearingStyle']").val(data.learingStyle);
						jQuery("#changeTab input[name='changeBeforeTeachingGuidePlanId']").val(data.teachingPlan);
						jQuery("#changeTab input[name='changeBeforeBrSchoolId']").val(data.brSchool);
					}
					
				}
			})
		}else{
			alertMsg.warn("请输入学生学号.");
		}
	}
	function choiceChange(){
		var obj = document.getElementById('changeType');
		if(obj.value!='23' || obj.value!='83' || obj.value!='82' || obj.value!='81' || obj.value!='98'){
			$("#changeTab #33,#44,#55,#66").hide();
		}
		if(obj.value=='23'){
			$("#changeTab #33").show();
			$("#changeTab #44,#55,#66").hide();
		}
		if(obj.value=='83'){
			$("#changeTab #44").show();
			$("#changeTab #33,#55,#66").hide();
		}
		if(obj.value=='82'){
			$("#changeTab #55").show();
			$("#changeTab #33,#44,#66").hide();
		}
		if(obj.value=='81'){
			$("#changeTab #66").show();
			$("#changeTab #33,#44,#55").hide();
		}
		if(obj.value=='98'){
			$("#changeTab #33,#44,#55,#66").show();
		}
	}
	
	</script>
</head>
<body>

	<h2 class="contentTitle">编辑学籍异动</h2>
	<div class="page">
		<div class="pageContent">
			<form method="post"
				action="${baseUrl}/edu3/register/stuchangeinfo/save.html"
				class="pageForm" id="stuchangeinfoForm">
				<input type="hidden" id="resourceid" name="resourceid"
					value="${stuChangeInfo.resourceid }" /> <input type=hidden
					name=wf_id value="${stuChangeInfo.wf_id }" /> <input type=hidden
					name=APP_WF_ID value="${stuChangeInfo.wf_id }" /> <input
					type="hidden" name="applicationDate"
					value="<fmt:formatDate value="${stuChangeInfo.applicationDate }" pattern="yyyy-MM-dd HH:mm:ss"/>" />
				<input type="hidden" id="CM" name="CM" value="">
				<div class="pageFormContent" layoutH="97">
					<div class="tabs">
						<div class="tabsHeader">
							<div class="tabsHeaderContent">
								<ul>
									<li class="selected"><a href="#"><span>异动信息</span></a></li>
									<li><a href="#"><span>流程跟踪</span></a></li>
								</ul>
							</div>
						</div>
						<div class="tabsContent" style="height: 100%;">
							<div>
								<table class="form" id="changeTab">
									<tr>
										<td width="20%">学生学号:</td>
										<td width="40%">
											<!-- 异动前信息 --> <input type="hidden"
											id="changeBeforeLearingStyle" name="changeBeforeLearingStyle"
											value="${stuChangeInfo.changeBeforeLearingStyle }" /> <input
											type="hidden" id="changeBeforeTeachingGuidePlan"
											name="changeBeforeTeachingGuidePlanId"
											value="${stuChangeInfo.changeBeforeTeachingGuidePlan.resourceid }" />
											<input type="hidden" id="changeBeforeBrSchool"
											name="changeBeforeBrSchoolId"
											value="${stuChangeInfo.changeBeforeBrSchool.resourceid }" />
											<input type="text" id="stuNum" name="stuNum"
											style="width: 50%"
											value="${stuChangeInfo.studentInfo.studyNo }"
											class="required" /> <c:if test="${isManager}">
												&nbsp;&nbsp;
												<div class="button">
													<div class="buttonContent">
														<button type="button" onclick="queryStuInfo();">获取学员信息</button>
													</div>
												</div>
												<br>
												<span style="color: red;">注：请填写学号后，点击“获取学员信息”获取学员信息</span>
											</c:if>
										</td>
										<td width="20%">年级:</td>
										<td width="30%"><input type="hidden" id="gradeId"
											name="gradeId"
											value="${stuChangeInfo.studentInfo.grade.resourceid }" /> <input
											type="text" name="grade" readonly="readonly"
											value="${stuChangeInfo.studentInfo.grade.gradeName }" /></td>
									</tr>
									<tr>
										<td width="20%">姓名:</td>
										<td width="30%"><input type="text" id="stuName"
											name="stuName" style="width: 50%"
											value="${stuChangeInfo.studentInfo.studentName}"
											readonly="readonly" class="required" /></td>
										<td width="20%">教学站:</td>
										<td width="30%"><input type="text" id="stuCenter"
											name="stuCenter" style="width: 50%"
											value="${stuChangeInfo.studentInfo.branchSchool.unitName }"
											readonly="readonly" /></td>
									</tr>
									<tr>
										<td width="20%">专业:</td>
										<td width="30%"><input type="text" id="major"
											name="major" style="width: 50%"
											value="${stuChangeInfo.studentInfo.major.majorName }"
											readonly="readonly" /></td>
										<td width="20%">层次:</td>
										<td width="30%"><input type="text" id="classic"
											name="classic" style="width: 50%"
											value="${stuChangeInfo.studentInfo.classic.classicName }"
											readonly="readonly" /></td>
									</tr>
									<tr>
										<td width="20%">异动类型:</td>
										<td colspan="3"><gh:select id="changeType"
												name="changeType" value="${stuChangeInfo.changeType }"
												dictionaryCode="CodeStudentStatusChange"
												filtrationStr="23，82，83'" style="width:185px;"
												classCss="required" onchange="choiceChange();" /> <font
											color=red>*</font></td>
									</tr>
									<tr id="33" style="display: none">
										<td width="20%">更改专业:</td>
										<td width="30%"><select name="majorid" id="majorid"
											size="1">
												<option value="">请选择</option>
												<c:forEach items="${majors}" var="major" varStatus="vs">
													<option value="${major.resourceid }"
														<c:if test="${stuChangeInfo.changeMajor.resourceid eq  major.resourceid }"> selected </c:if>>${major.majorName }</option>
												</c:forEach>
										</select> <font color='green'>说明：‘专业+A’的为网络成人直属班专业.</font></td>
										<td width="20%">&nbsp;</td>
										<td width="30%">&nbsp;</td>
									</tr>
									<tr id="44" style="display: none">
										<td width="20%">更改层次:</td>
										<td width="30%"><gh:selectModel name="classicid"
												bindValue="resourceid" displayValue="classicName"
												modelClass="com.hnjk.edu.basedata.model.Classic"
												value="${stuChangeInfo.changeClassicId.resourceid }"
												style="width:52%" /></td>
										<td width="20%">&nbsp;</td>
										<td width="30%">&nbsp;</td>
									</tr>
									<tr id="55" style="display: none">
										<td width="20%">更改教学站:</td>
										<td width="30%"><gh:selectModel name="schoolCenterid"
												bindValue="resourceid" displayValue="unitName"
												modelClass="com.hnjk.security.model.OrgUnit"
												condition=" unitType='brSchool' and  isDeleted = 0 and status = 'normal' "
												orderBy=" unitName asc "
												value="${stuChangeInfo.changeBrschool.resourceid }"
												style="width:52%" /></td>
										<td width="20%">&nbsp;</td>
										<td width="30%">&nbsp;</td>
									</tr>
									<tr id="66" style="display: none">
										<td width="20%">更改学习方式:</td>
										<td width="30%"><gh:select name="change_to_style"
												dictionaryCode="CodeLearningStyle"
												value="${stuChangeInfo.changeLearningStyle }"
												style="width:52%" /></td>
										<td width="20%">&nbsp;</td>
										<td width="30%">&nbsp;</td>
									</tr>
									<!--  
										<tr>
											<td width="20%">原因:</td>
											<td width="80%" colspan="3">											
												<textarea rows="4" cols="70%" name="reason" class="required" >${stuChangeInfo.reason }</textarea>
											</td>
										</tr>
										-->
									<tr>
										<td width="20%">意见:</td>
										<td width="80%" colspan="3"><wf:opinion
												appType="stuchangeinfo" appWfId="${stuChangeInfo.wf_id }"
												appFrom="${param.APP_FROM}" /></td>
									</tr>
									<tr>
										<td width="20%">&nbsp;</td>
										<td width="80%" colspan="3">
											<!-- 流程标签	--> <wf:availableAction appType="stuchangeinfo"
												appWfId="${stuChangeInfo.wf_id }"
												appFrom="${param.APP_FROM}" />

										</td>
									</tr>
								</table>
							</div>
							<!--  -->
							<div>
								<wf:trace appType="stuchangeinfo"
									appWfId="${stuChangeInfo.wf_id }" appFrom="${param.APP_FROM}" />
							</div>

						</div>

						<div class="tabsFooter">
							<div class="tabsFooterContent"></div>
						</div>
					</div>
				</div>

				<div class="formBar">
					<ul>
						<!-- <li><div class="buttonActive"><div class="buttonContent"><button type="submit" onclick="saveChange()">保存</button></div></div></li> 
					<li><div class="buttonActive"><div class="buttonContent"><button type="submit" onclick="submitChange()">提交</button></div></div></li>-->
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
</html>