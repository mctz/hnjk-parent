<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>补预约权限表单</title>

</head>
<body>
	<h2 class="contentTitle">
		<c:choose>
			<c:when test="${setting != null }">编辑补预约权限</c:when>
			<c:otherwise>新增补预约权限</c:otherwise>
		</c:choose>
	</h2>
	<div class="page">
		<form id="reCourseOrderForm" method="post"
			action="${baseUrl}/edu3/teaching/recourseorder/save.html"
			class="pageForm" onsubmit="return validateReCourseOrder(this);">
			<input type="hidden" name="resourceid" value="${setting.resourceid }" />
			<div class="pageContent" layoutH="77">
				<table id="reCourseOrderFormTable" class="form">
					<tr>
						<td width="12%">补预约类型:</td>
						<td width="88%" colspan="3"><select name="reOrderType"
							class="required" id="reOrderType">
								<option value="">请选择</option>
								<option value="1"
									<c:if test="${setting.reOrderType == 1 }"> selected="selected" </c:if>>预约学习</option>
								<option value="2"
									<c:if test="${setting.reOrderType == 2 }"> selected="selected" </c:if>>预约考试</option>
						</select><font color="red">*</font></td>
					</tr>
					<tr>
						<td width="10%">补预约开始时间:</td>
						<td width="40%"><input type="text"
							id="_brSchoolExaminfo_form_startTime" name="startTime"
							style="width: 55%" value="${setting.startTime}" class="required"
							onclick="WdatePicker({isShowWeek:true,dateFmt:'yyyy-MM-dd HH:mm:ss',maxDate:'#F{$dp.$D(\'_brSchoolExaminfo_form_endTime\')}'})" />
						</td>
						<td width="10%">补预约结束时间:</td>
						<td width="40%"><input type="text"
							id="_brSchoolExaminfo_form_endTime" name="endTime"
							style="width: 55%" value="${setting.endTime}" class="required"
							onclick="WdatePicker({isShowWeek:true,dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:'#F{$dp.$D(\'_brSchoolExaminfo_form_startTime\')}'})" />
						</td>
					</tr>
					<c:if test="${null!=setting }">
						<c:choose>
							<c:when test="${null!=setting.orderCourseSetting }">
								<tr id='orderCourseTR'>
									<td width='12%''>预约学习批次</td>
									<td width='88%' colspan='3'><gh:selectModel
											id='reCourseOrderForm_orderCourseSetting'
											name='orderCourseSetting' bindValue='resourceid'
											displayValue='settingName'
											modelClass='com.hnjk.edu.teaching.model.OrderCourseSetting'
											orderBy='startDate desc' choose='Y' classCss='required'
											value="${setting.orderCourseSetting.resourceid }"
											style='width:30%' /><font color='red'>*</font></td>
								</tr>
							</c:when>
							<c:when test="${null!=setting.examSub }">
								<tr id='orderExamTR'>
									<td width='12%''>预约考试批次</td>
									<td width='88%' colspan='3'><gh:selectModel
											id='reCourseOrderForm_examSub' name='examSub'
											bindValue='resourceid' displayValue='batchName'
											modelClass='com.hnjk.edu.teaching.model.ExamSub'
											orderBy='batchName desc' choose='Y' classCss='required'
											value="${setting.examSub.resourceid }" style='width:30%'
											condition="batchType='exam'" /><font color='red'>*</font>
									</td>
								</tr>
							</c:when>
						</c:choose>

					</c:if>
					<tr id="brSchoolTR">
						<td width="10%">教学站：</td>
						<td colspan="3" width="90%"><c:choose>
								<c:when test="${setting != null }">
									<gh:brSchoolAutocomplete name="branchSchoolms2side__dx"
										tabindex="1" id="reOrderSetting_Form_brSchoolName"
										defaultValue="${setting.brSchool.resourceid}"
										displayType="code" />
								</c:when>
								<c:otherwise>
									<select name="branchSchool" id='reCourseOrderForm_branchSchool'
										multiple='multiple' size='10' style="width: auto;">
										<c:if test="${setting != null}">
											<option title="${setting.brSchool.unitName }"
												value="${setting.brSchool.resourceid }" SELECTED>
												${setting.brSchool.unitName }</option>
										</c:if>
										<c:forEach items="${brSchoolList }" var="brSchool">
											<option title="${brSchool.unitName }"
												value="${brSchool.resourceid }">${brSchool.unitName }</option>
										</c:forEach>
									</select>
								</c:otherwise>
							</c:choose></td>
					</tr>
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
	<script type="text/javascript">
		$().ready(function(){
	 		var resourceid = "${setting.resourceid }"
	 		if($("#reCourseOrderForm_branchSchool")){
	 			$("#reCourseOrderForm_branchSchool").multiselect2side({
					selectedPosition: 'right',
					moveOptions: false,
					labelsx: '',
					labeldx: ''
				});
	 		}
	 		
	 		
	 		$("#reCourseOrderForm").find("#reOrderType").change(function(){
				var trHTML   = "";
				var reOrderType = $(this).val();
				$("#reCourseOrderFormTable tr[id=orderCourseTR]").html("");
				$("#reCourseOrderFormTable tr[id=orderExamTR]").html("");
				if(""==reOrderType){
					alertMsg.warn("请选择一个补预约类型!");
					return false;
				}else{
					if("1"==reOrderType){
						trHTML +="<tr id='orderCourseTR'><td width='12%''>预约学习批次</td><td width='88%' colspan='3'><gh:selectModel id='reCourseOrderForm_orderCourseSetting' name='orderCourseSetting' bindValue='resourceid'  displayValue='settingName' modelClass='com.hnjk.edu.teaching.model.OrderCourseSetting'  orderBy='startDate desc' choose='Y' classCss='required' condition="isOpened='Y'" style='width:30%'/><font color='red'>*</font></td></tr>";
						
					}else if("2" == reOrderType ){
						trHTML +="<tr id='orderExamTR'><td width='12%''>预约考试批次</td><td width='88%' colspan='3'><gh:selectModel id='reCourseOrderForm_examSub' name='examSub' bindValue='resourceid'  displayValue='batchName' modelClass='com.hnjk.edu.teaching.model.ExamSub'  orderBy='batchName desc' choose='Y' classCss='required' style='width:30%' condition="examsubStatus='2',batchType='exam'"   /><font color='red'>*</font></td></tr>";
					}
				}
				$("#reCourseOrderForm").find("#reCourseOrderFormTable tr[id='brSchoolTR']").before(trHTML);
			});
		});
	
	    //合法性检查
	    function validateReCourseOrder(form){	 
	    	
	    	var reOrderType		   = $("#reCourseOrderForm #reOrderType").val();
	    	var resourceid 		   = "${setting.resourceid }";
	    	var examSub    		   = $("#reCourseOrderForm #reCourseOrderForm_examSub option:selected").val();
	    	var orderCourseSetting = $("#reCourseOrderForm #reCourseOrderForm_orderCourseSetting option:selected").val();
	    	var brSchool   		   = "";
			
	    	if("1"==reOrderType&&(undefined==orderCourseSetting||null==orderCourseSetting||""==orderCourseSetting)){
	    		alertMsg.warn("预约学习批次是必填项！");
				return false;
	    	}
	    	if("2"==reOrderType&&(undefined==examSub||null==examSub||""==examSub)){
	    		alertMsg.warn("预约考试批次是必填项！");
				return false;
	    	}
	    	
	    	if(resourceid != ""){
	    		brSchool     = $("#reCourseOrderForm input[name='branchSchoolms2side__dx']").val();
	    	}else{
	    		brSchool     = $("#reCourseOrderForm #reCourseOrderForm_branchSchool option:selected").val();
	    	}
			if(brSchool == ""){
				alertMsg.warn("教学站是必填项！");
				return false;
			}
	    	return validateCallback(form);
	    }
	   
	</script>
</body>
