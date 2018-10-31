<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>试卷成卷规则管理</title>
<script type="text/javascript">
 function courseExamRuleIsEnrolExamOnchange(isEnrolExam){
	 if(isEnrolExam=='Y'){			
		$("#courseExamRules_form_course_tr2").hide();
		$("#courseExamRules_form_course_tr1").show();
	 } else {
		$("#courseExamRules_form_course_tr2").show();
		$("#courseExamRules_form_course_tr1").hide();
	 }
 }
</script>
</head>
<body>
<body>
	<h2 class="contentTitle">${(empty courseExamRules.resourceid)?'新增':'修改' }试卷成卷规则</h2>
	<div class="page">
		<div class="pageContent">
			<form method="post"
				action="${baseUrl}/edu3/learning/courseexamrules/save.html"
				class="pageForm"
				onsubmit="return _checkCourseExamRulesValidateCallback(this);">
				<div class="pageFormContent" layoutH="97">
					<input type="hidden" name="resourceid"
						value="${courseExamRules.resourceid }" />
					<table class="form">
						<tr>
							<td style="width: 15%">是否入学考：</td>
							<td style="width: 35%"><gh:select name="isEnrolExam"
									id="courseExamRules_form_isEnrolExam"
									value="${courseExamRules.isEnrolExam }" choose="N"
									classCss="required" dictionaryCode="yesOrNo"
									disabled="${(isTeacher or (not empty courseExamRules.resourceid))?'disabled':'' }"
									onchange="courseExamRuleIsEnrolExamOnchange(this.value)" /> <c:if
									test="${isTeacher or (not empty courseExamRules.resourceid) }">
									<input type="hidden" name="isEnrolExam"
										value="${courseExamRules.isEnrolExam}" />
								</c:if> <span style="color: red">*</span></td>
							<td style="width: 15%">考试时长(分钟):</td>
							<td style="width: 35%"><input
								id="courseExamRules_form_examTimeLong" name="examTimeLong"
								value="${courseExamRules.examTimeLong }" class="required digits" />
							</td>
						</tr>
						<tr id="courseExamRules_form_course_tr1"
							<c:if test="${isTeacher or courseExamRules.isEnrolExam eq 'N' }">style="display:none"</c:if>>
							<td style="width: 15%">课程:</td>
							<td colspan="3"><gh:select
									id="courseExamRules_form_courseName" name="courseName"
									value="${courseExamRules.courseName }"
									dictionaryCode="CodeEntranceExam" /> <span style="color: red">*</span>
							</td>
						</tr>
						<tr id="courseExamRules_form_course_tr2"
							<c:if test="${courseExamRules.isEnrolExam eq 'Y' }">style="display:none"</c:if>>
							<td style="width: 15%">课程:</td>
							<td><gh:courseAutocomplete
									id="courseExamRules_form_CourseId" name="courseId" tabindex="1"
									value="${courseExamRules.course.resourceid }"
									isFilterTeacher="Y" /></td>
							<td style="width: 15%">试题来源:</td>
							<td><gh:checkBox name="paperSourse"
									id="courseExamRules_form_paperSourse"
									dictionaryCode="CodeExamform"
									value="${courseExamRules.paperSourse}"
									filtrationStr="unit_exam,online_exam,final_exam" /></td>
						</tr>
					</table>
					<table class="form">
						<tr>
							<td>
								<div class="formBar">
									<ul>
										<li><div class="buttonActive">
												<div class="buttonContent">
													<button type="button" onclick="addCourseExamRuleDetails()">
														新 增</button>
												</div>
											</div></li>
										<li><div class="buttonActive">
												<div class="buttonContent">
													<button type="button" onclick="delCourseExamRuleDetails()">
														删 除</button>
												</div>
											</div></li>
									</ul>
								</div>
							</td>
						</tr>
					</table>
					<table class="form">
						<thead>
							<tr>
								<th width="8%"><input type="checkbox" name="checkall"
									id="check_all_courseExamRules_form"
									onclick="checkboxAll('#check_all_courseExamRules_form','courseExamRulesDetailsId','#courseExamRules_form_tbody')" /></th>
								<th width="8%">序号</th>
								<th width="22%">类型</th>
								<th width="22%">题型</th>
								<th width="20%">试题数</th>
								<th width="20%">试题分数</th>
							</tr>
						</thead>
						<tbody id="courseExamRules_form_tbody">
							<c:forEach items="${courseExamRules.courseExamRulesDetails}"
								var="detail" varStatus="vs">
								<tr>
									<td><input type="checkbox" name="courseExamRulesDetailsId"
										value="${detail.resourceid }" autocomplete="off" /><input
										type='hidden' name='detailsId' value='${detail.resourceid }' /></td>
									<td><input type="text" name="showOrder"
										value="${detail.showOrder }" class="required digits" size='5' /></td>
									<td><gh:select name='examNodeType'
											dictionaryCode='CodeExamNodeType'
											value="${detail.examNodeType }" style='width:80%' /></td>
									<td><gh:select name='examType'
											dictionaryCode='CodeExamType' value="${detail.examType }"
											style='width:80%' classCss='required' /><span
										style="color: red">*</span></td>
									<td><input type='text' name='examNum' size='5'
										value='${detail.examNum }' class='digits' /></td>
									<td><input type='text' name='examValue' size='5'
										value='${detail.examValue }' class='number' /></td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
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
	<script type="text/javascript">
	function addCourseExamRuleDetails(){
		var courseName = $("#courseExamRules_form_courseName").val();
		var courseId = $("#courseExamRules_form_CourseId").val();
		var isEnrolExam = $("#courseExamRules_form_isEnrolExam").val();
		if(isEnrolExam=="Y" && courseName=="" || isEnrolExam=="N" && courseId==""){
			alertMsg.warn("请选择一门课程");
			return;
		}				
		var rowNum = jQuery("#courseExamRules_form_tbody").get(0).rows.length;
		var htmlStr = "<tr>";
		htmlStr += "<td><input type='checkbox' name='courseExamRulesDetailsId' value='' autocomplete='off' /><input type='hidden' name='detailsId' value=''/></td>";
		htmlStr += "<td><input type='text' name='showOrder' value='"+(rowNum+1)+"' class='required digits' size='5'/></td>";
		htmlStr += "<td><gh:select name='examNodeType' dictionaryCode='CodeExamNodeType' style='width:80%'/></td>";
		htmlStr += "<td><gh:select name='examType' dictionaryCode='CodeExamType' style='width:80%' classCss='required'/><span style='color:red'>*</span></td>";
		htmlStr += "<td><input type='text' name='examNum' size='5' value='0' class='digits'/></td>";  
	    htmlStr += "<td><input type='text' name='examValue' size='5' value='0' class='number'/></td></tr>"; 	
		$("#courseExamRules_form_tbody").append(htmlStr);
	}
	
	function delCourseExamRuleDetails(){
		if(!isChecked('courseExamRulesDetailsId',"#courseExamRules_form_tbody")){
	 			alertMsg.warn('请选择一条要操作记录！');
				return false;
	 	}
		alertMsg.confirm("您确定要删除这些记录吗？", {
				okCall: function(){//执行			
					var res = new Array();
					$("#courseExamRules_form_tbody input[@name='resourceid']:checked").each(function(){
	                    if($(this).val()!="")  {
	                    	res.push($(this).val());
	                    }
					});
					if(res.length>0){						
						var postUrl = baseUrl+"/edu3/framework/courseexamruledetails/remove.html";
						$.post(postUrl,{resourceid:res.join(",")}, function (json){
							if(json.statusCode==200){
								$("#courseExamRules_form_tbody input[@name='resourceid']:checked").each(function(){
									$(this).parent().parent().remove();
								});
							}						
						}, "json");
					} else {
						$("#courseExamRules_form_tbody input[@name='resourceid']:checked").each(function(){
		                    $(this).parent().parent().remove();
						});
					}
					
				}
		});	
	}
	
	function _checkCourseExamRulesValidateCallback(form){
		var $form = $(form);
		if($form.find("input[name='courseExamRulesDetailsId']").size()==0){
			alertMsg.warn("请填写规则");
			return false;
		}
		var isValid = true;
		$form.find("input[name='examNum']").each(function (){
			var num = $.trim($(this).val());
			if(!num.isInteger() || parseInt(num)<=0){
				isValid = false;
				return false;
			}
		});
		if(!isValid){
			alertMsg.warn("题目数必须为大于0的正整数");
			return false;
		}
		
		isValid = true;
		$form.find("input[name='examValue']").each(function (){
			var nScore = $.trim($(this).val());
			if(!nScore.isNumber() || parseFloat(nScore)<=0){
				isValid = false;
				return false;
			}
		});
		if(!isValid){
			alertMsg.warn("每个类型的题目分值必须为大于0的数值");
			return false;
		}
		var isEnrolExam = $("#courseExamRules_form_isEnrolExam").val();
		if(isEnrolExam=="N"&& $form.find("input[name='paperSourse']:checked").size()==0){
			alertMsg.warn("请选择题库来源");
			return false;
		}
		return validateCallback(form);
	}
	</script>
</body>
</html>