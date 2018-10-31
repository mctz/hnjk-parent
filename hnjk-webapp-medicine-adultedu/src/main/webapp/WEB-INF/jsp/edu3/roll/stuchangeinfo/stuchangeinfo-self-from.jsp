<%@page contentType="text/html;charset=UTF-8"%>
<%@include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>替学生学籍异动-班主任管理</title>
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
延期 100
过期 101
开除学籍 42
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
		  }else if(changeType == '13'){
			  $("#changeTab #77").show();
			  $("#changeTab select[name='change_reason']").attr("class","required");
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
	
	
	function _selectMajor(obj){
		var majorType = $(obj).val();		
		var brSchoolId= $("#changeBeforeBrSchoolId").val();
		var gradeId   = $("#changeTab input[name='gradeId']").val();	
		
		var html 	  = "<option value=''>请选择...</option>";
		$("#_majorid").html(html);
		
		if((""==brSchoolId || ""==gradeId)||(null==brSchoolId || null==gradeId)){
			alertMsg.warn("请选择一个要异动的学生");
			return false;
		}
		if(""==majorType){
			alertMsg.warn("请选择所转专业的教学模式");
			return false;
		}
		
		jQuery.ajax({
			data:"teachType="+majorType+"&gradeId="+gradeId+"&brSchoolId="+brSchoolId,
			dataType:"json",
			url:"${baseUrl}/edu3/framework/register/stuchangeinfo/getmajor.html",
			success:function(data){
				
				if(data.error && data.error!=""){
					alertMsg.warn(data.error);
					
				}else{				
					for(var i=0;i<data.majorList.length;i++){
						html += "<option value='"+data.majorList[i]['RESOURCEID']+"'>"+data.majorList[i]['MAJORNAME']+"</option>"
					}
					$("#_majorid").html(html);
				}
			}
		})
	}
	
	function _selectMajor_new(){
		
		var gradeId   = $("#changeTab input[name='gradeId']").val();
		var classicId = $("#changeTab input[name='classicId']").val();
		
		var brSchoolId = $("#changeMajor_brschoolid").val(); 
		var teachType  = $("#changeMajor_TeachingType").val();
		
		var html 	  = "<option value=''>请选择...</option>";
		$("#_majorid").html(html);
		
		if((""==brSchoolId || ""==gradeId)||(null==brSchoolId || null==gradeId)){
			alertMsg.warn("请选择一个要异动的学生");
			return false;
		}
		
		if(""==teachType){
			//alertMsg.warn("请选择所转专业的教学模式");
			return false;
		}
		
		jQuery.ajax({
			data:"teachType="+teachType+"&gradeId="+gradeId+"&brSchoolId="+brSchoolId+"&classicId="+classicId,
			dataType:"json",
			url:"${baseUrl}/edu3/framework/register/stuchangeinfo/getInfosmajor.html",
			success:function(data){
				cleanhtml = "<option value=''>请选择...</option>";
				$("#changeMajor_classesid").html(cleanhtml);
				if(data.error && data.error!=""){
					alertMsg.warn(data.error);
					
				}else{				
					for(var i=0;i<data.majorList.length;i++){
						html += "<option value='"+data.majorList[i]['RESOURCEID']+"'>"+data.majorList[i]['MAJORNAME']+"</option>"
					}
					$("#_majorid").html(html);
				}
			}
		});
	}
	function _selectClasses_new(obj){
		var gradeId   = $("#changeTab input[name='gradeId']").val();
		var classicId = $("#changeTab input[name='classicId']").val();
		
		var brSchoolId = $("#changeMajor_brschoolid").val(); 
		var teachType  = $("#changeMajor_TeachingType").val();
		var marjorId   = "";
		if(null==obj){
			majorId = $("#_majorid").val();
		}else{
			majorId = $(obj).val();
		}
		var html 	  = "<option value=''>请选择...</option>";
		$("#changeMajor_classesid").html(html);
		
		if((""==brSchoolId || ""==gradeId)||(null==brSchoolId || null==gradeId)){
			alertMsg.warn("请选择一个要异动的学生");
			return false;
		}
		if(""==teachType){
			alertMsg.warn("请选择所转专业的教学模式");
			return false;
		}
		jQuery.ajax({
			data:{teachType:teachType,gradeId:gradeId,brSchoolId:brSchoolId,classicId:classicId,majorId:majorId},
			dataType:"json",
			url:"${baseUrl}/edu3/framework/register/stuchangeinfo/getClasses.html",
			success:function(data){
				
				if(data.error && data.error!=""){
					alertMsg.warn(data.error);
					
				}else{				
					for(var i=0;i<data.classesList.length;i++){
						html += "<option value='"+data.classesList[i]['RESOURCEID']+"'>"+data.classesList[i]['CLASSESNAME']+"</option>"
					}
					$("#changeMajor_classesid").html(html);
				}
			}
		})
	}
	
	function queryStuInfo(){
		var stuNum = $("#stuChangeForStu input[id='stuNum']").val();
		if(stuNum!=""){
			$.ajax({
				data:"stuNum="+stuNum,
				dataType:"json",
				url:"${baseUrl}/edu3/schoolroll/awardspun/querystuinfo.html",
				success:function(data){
					if(data.error && data.error!=""){
						alertMsg.warn(data.error);
					}else{
						$("#changeTab input[name='stuName']").val(data.studentName);
						$("#changeTab input[name='stuCenter']").val(data.schoolCenter);
						$("#changeTab input[name='major']").val(data.major);
						$("#changeTab input[name='majorId']").val(data.majorId);
						$("#changeTab input[name='classic']").val(data.classic);
						$("#changeTab input[name='classicId']").val(data.classicId);
						$("#changeTab input[name='grade']").val(data.grade);
						$("#changeTab input[name='gradeId']").val(data.gradeId);					
						$("#changeTab input[name='changeBeforeLearingStyle']").val(data.learingStyle);
						$("#changeTab input[name='changeBeforeTeachingGuidePlanId']").val(data.teachingPlan);
						$("#changeTab input[name='changeBeforeBrSchoolId']").val(data.brSchool);
						$("#changeTab input[name='schoolType']").val(data.schoolType);
						$("#changeMajor_classicName").html(data.classic);
						$("#changeMajor_gradeName").html(data.grade);
					}
				}
			})
		}else{
			alertMsg.warn("请输入学生学号.");
		}
	}
	function choiceChange(){
		var obj = document.getElementById('changeType');
		$("#changeTab select[name='change_reason']").attr("class","");
		$("#changeTab #77").hide();
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
		if(obj.value=='13'){
			$("#changeTab #77").show();
			$("#changeTab select[name='change_reason']").attr("class","required");
		}
	}
	//更改层次，选定层次后，异步查询是否有多条年度教学计划
	function choiceChangeClassic(){
		
		var classic = jQuery("#classicid_id").val();
		var gradeId = jQuery("#gradeId").val();
		var major = jQuery("#majorId").val();
		var schooltype  = jQuery("#schoolType").val();
		if(""!=classic && ""!=gradeId && ""!=major&&""!=schooltype){
			jQuery.ajax({
				type:"post",
				url: "${baseUrl}/edu3/teaching/teachingGuidePlan/getTeachingGuidePlan.html",
				data:"classic="+classic+"&gradeId="+gradeId+"&major="+major+"&schooltype="+schooltype,
				dataType:"json",
				success:function(resultData){
					jQuery("#teachingGuidePlanId").children().remove();
					jQuery("#teachingGuidePlanId").attr("class","required");
					var insertHTML ="<option value=''>--请选择年度教学计划--</option>";
					var teachingGuidePlanList = resultData;
					for(var i=0;i<teachingGuidePlanList.length;i++){	
						insertHTML += '<option value="'+resultData[i].id+'">'+resultData[i].value+'</option>';
					}
					jQuery("#teachingGuidePlanId").html(insertHTML);
				}
			});
		}
	}
	</script>
</head>
<body>

	<h2 class="contentTitle">编辑学籍异动</h2>
	<div class="page">
		<div class="pageContent">
			<form method="post"
				action="${baseUrl}/edu3/register/stuchangeinfo/self/save.html"
				class="pageForm" id="stuchangeinfoForm"
				onsubmit="return validateCallback(this);">
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
									<!-- <li><a href="#"><span>流程跟踪</span></a></li>	 -->
								</ul>
							</div>
						</div>
						<div class="tabsContent" style="height: 100%;">
							<div>
								<table class="form" id="changeTab">
									<tr>
										<td width="20%">学生学号:</td>
										<td width="30%">
											<!-- 异动前信息 --> <input type="hidden"
											id="changeBeforeLearingStyle" name="changeBeforeLearingStyle"
											value="${stuChangeInfo.changeBeforeLearingStyle }" /> <input
											type="hidden" id="changeBeforeTeachingGuidePlanId"
											name="changeBeforeTeachingGuidePlanId"
											value="${stuChangeInfo.changeBeforeTeachingGuidePlan.resourceid }" />
											<input type="hidden" id="changeBeforeBrSchoolId"
											name="changeBeforeBrSchoolId"
											value="${stuChangeInfo.changeBeforeBrSchool.resourceid }" />
											<input type="hidden" id="schoolType" name="schoolType"
											value="" />

											<div id="stuChangeForStu">
												<input type="text" id="stuNum" name="stuNum"
													style="width: 50%"
													value="${stuChangeInfo.studentInfo.studyNo }"
													class="required" />
											</div> <c:if test="${isManager}">
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
											type="text" name="grade" class="required" readonly="readonly"
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
											class="required" name="stuCenter" style="width: 50%"
											value="${stuChangeInfo.studentInfo.branchSchool.unitName }"
											readonly="readonly" /></td>
									</tr>
									<tr>
										<td width="20%">专业:</td>
										<td width="30%"><input type="hidden" id="majorId"
											name="majorId"
											value="${stuChangeInfo.studentInfo.major.resourceid }" /> <input
											type="text" id="major" name="major" style="width: 50%"
											value="${stuChangeInfo.studentInfo.major.majorName }"
											readonly="readonly" /></td>
										<td width="20%">层次:</td>
										<td width="30%"><input type="hidden" id="classicId"
											name="classicId"
											value="${stuChangeInfo.studentInfo.classic.resourceid }" /> <input
											type="text" class="required" id="classic" name="classic"
											style="width: 50%"
											value="${stuChangeInfo.studentInfo.classic.classicName }"
											readonly="readonly" /></td>
									</tr>
									<tr>
										<td width="20%">异动类型:</td>
										<td>
											<%--<gh:select id="changeType" name="changeType"  dictionaryCode="CodeStudentStatusChange" style="width:185px;" classCss="required" onchange="choiceChange();"/>--%>
											<gh:select id="changeType" name="changeType"
												value="${stuChangeInfo.changeType }"
												dictionaryCode="CodeStudentStatusChange"
												filtrationStr="23，12" style="width:185px;"
												classCss="required" onchange="choiceChange();" /> <font
											color=red>*</font>
										</td>
										<c:choose>
											<c:when test="${isManager }">
												<td width="20%"></td>
												<td width="30%"></td>
											</c:when>
											<c:otherwise>
												<td></td>
												<td></td>
											</c:otherwise>
										</c:choose>

									</tr>
									<tr id="33" style="display: none">
										<td width="20%">更改专业:</td>
										<td colspan="3" width="80%">新办学单位:<gh:selectModel
												condition=" unitType='brSchool' "
												name="changeMajor_brschool" style="float:none;"
												id="changeMajor_brschoolid" bindValue="resourceid"
												displayValue="unitName"
												modelClass="com.hnjk.security.model.OrgUnit"
												onchange="_selectMajor_new()" /> 年级:<span
											id="changeMajor_gradeName">${stuChangeInfo.studentInfo.grade.gradeName}</span>
											层次:<span id="changeMajor_classicName">${stuChangeInfo.studentInfo.classic.classicName}</span>
											<br> 学习形式:<gh:select dictionaryCode="CodeTeachingType"
												name="changeMajor_TeachingType" style="float:none;"
												onchange="_selectMajor_new()" /> 专业:<select name="majorid"
											id="_majorid" size="1" style="float: none;" class="required"
											onchange="_selectClasses_new(this)"><option value="">请选择</option></select>
											班级:<select name="changeMajor_classesid"
											id="changeMajor_classesid" size="1" class="required"
											style="float: none;"><option value="">请选择</option></select>
										</td>

									</tr>
									<tr id="44" style="display: none">
										<td width="20%">更改层次:</td>
										<td width="30%"><gh:selectModel name="classicid"
												id="classicid_id" bindValue="resourceid"
												displayValue="classicName"
												modelClass="com.hnjk.edu.basedata.model.Classic"
												value="${stuChangeInfo.changeClassicId.resourceid }"
												style="width:52%" onchange="choiceChangeClassic()" /></td>
										<td width="20%">选择年度教学计划:</td>
										<td width="30%"><select id="teachingGuidePlanId"
											name="teachingGuidePlan">
												<option value="">请选择年度教学计划</option>
										</select> <font color="red">*</font></td>
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
									<tr id="77" style="display: none">
										<td width="20%">退学原因:</td>
										<td width="30%"><select name="change_reason">
												<option value="">--请选择--</option>
												<option
													<c:if test="${stuChangeInfo.reason=='患病'}"> selected="selected"</c:if>
													value="患病">患病</option>
												<option
													<c:if test="${stuChangeInfo.reason=='停学实践'}"> selected="selected"</c:if>
													value="停学实践">停学实践</option>
												<option
													<c:if test="${stuChangeInfo.reason=='贫困'}"> selected="selected"</c:if>
													value="贫困">贫困</option>
												<option
													<c:if test="${stuChangeInfo.reason=='学习成绩不好'}"> selected="selected"</c:if>
													value="学习成绩不好">学习成绩不好</option>
												<option
													<c:if test="${stuChangeInfo.reason=='出国'}"> selected="selected"</c:if>
													value="出国">出国</option>
												<option
													<c:if test="${stuChangeInfo.reason=='其他'}"> selected="selected"</c:if>
													value="其他">其他</option>
										</select></td>
										<td width="20%">&nbsp;</td>
										<td width="30%">&nbsp;</td>
									</tr>
									<tr>
										<td width="20%">备注:</td>
										<td width="80%" colspan="3"><textarea rows="4" cols="70%"
												name="memo">${stuChangeInfo.memo }</textarea></td>
									</tr>
									<tr>
										<td width="20%">&nbsp;</td>
										<td width="80%" colspan="3">
											<!-- 流程标签									 
												<wf:availableAction appType="stuchangeinfo" appWfId="${stuChangeInfo.wf_id }" appFrom="${param.APP_FROM}"/>
												 -->
										</td>
									</tr>
								</table>
							</div>
							<!-- 
							<div>							
								<wf:trace appType="stuchangeinfo" appWfId="${stuChangeInfo.wf_id }" appFrom="${param.APP_FROM}"/>
							</div>
							 -->
						</div>

						<div class="tabsFooter">
							<div class="tabsFooterContent"></div>
						</div>
					</div>
				</div>

				<div class="formBar">
					<ul>
						<!-- <li><div class="buttonActive"><div class="buttonContent"><button type="submit" onclick="saveChange()">保存</button></div></div></li> -->
						<c:if test="${ empty stuChangeInfo.resourceid }">
							<li><div class="buttonActive">
									<div class="buttonContent">
										<button type="submit">提交</button>
									</div>
								</div></li>
						</c:if>
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