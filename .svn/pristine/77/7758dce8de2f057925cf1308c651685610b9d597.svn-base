<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>期末机考考试时间设置</title>
</head>
<body>
	<h2 class="contentTitle">期末机考考试时间</h2>
	<div class="page">
		<div class="pageContent">
			<form id="finalExamInfoForm" method="post"
				action="${baseUrl}/edu3/teaching/examinfo/save.html"
				class="pageForm" onsubmit="return validateCallback(this);">
				<input type="hidden" name="resourceid"
					value="${finalExamInfo.resourceid }" />
				<div class="pageFormContent" layoutH="97">
					<table class="form">
						<tr>
							<td width="15%">考试批次:</td>
							<td width="35%">
								<%-- <gh:selectModel id="finalexaminfoform_examSubId" name='examSubId' onchange="searchExamResultChangCourse()" bindValue='resourceid' displayValue='batchName' value="${finalExamInfo.examSub.resourceid}" modelClass='com.hnjk.edu.teaching.model.ExamSub' condition="batchType='exam'" orderBy='yearInfo.firstYear desc,term desc' style="width:60%" classCss="required" />--%>
								<gh:selectModel id="finalexaminfoform_examSubId"
									name='examSubId' bindValue='resourceid'
									displayValue='batchName'
									value="${finalExamInfo.examSub.resourceid}"
									modelClass='com.hnjk.edu.teaching.model.ExamSub'
									condition="batchType='exam'"
									orderBy='yearInfo.firstYear desc,term desc' style="width:60%"
									classCss="required" /> <span style="color: red;">*</span>
							</td>
							<td width="15%">课程:</td>
							<!-- 学院2016修改 -->
							<td width="35%" id="courid"><gh:selectModel bindValue=""
									displayValue="" modelClass="" name="courseId" id="" /></td>
						</tr>
						<tr>
							<td width="15%">考试开始时间:</td>
							<td width="35%"><input type='text' name='examStartTime'
								value='<fmt:formatDate value="${finalExamInfo.examStartTime }" pattern='yyyy-MM-dd HH:mm:ss'/>'
								style='width: 60%' class='required'
								id='finalexaminfoform_examStartTime'
								onFocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',maxDate:'#F{$dp.$D(\'finalexaminfoform_examEndTime\')}'})">
							</td>
							<td width="15%">考试结束时间:</td>
							<td width="35%"><input type='text' name='examEndTime'
								value='<fmt:formatDate value="${finalExamInfo.examEndTime }" pattern='yyyy-MM-dd HH:mm:ss'/>'
								style='width: 60%' class='required'
								id='finalexaminfoform_examEndTime'
								onFocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:'#F{$dp.$D(\'finalexaminfoform_examStartTime\')}'})">
							</td>
						</tr>

						<tr>
							<td width="15%">场次名称:</td>
							<td width="35"><input type="text"
								id="finalexaminfoform_machineExamName" name="machineExamName"
								value="${finalExamInfo.machineExamName }" class='required'
								maxlength="50" style="width: 60%" /></td>
							<td>成卷规则:</td>
							<td><input type="text"
								id="finalexaminfoform_courseExamRulesName"
								value="${finalExamInfo.courseExamRules.courseExamRulesName }"
								readonly="readonly" /> <span style="color: red;">*</span> <input
								type="hidden" id="finalexaminfoform_courseExamRulesId"
								name="courseExamRulesId"
								value="${finalExamInfo.courseExamRules.resourceid }"
								class="required" /> <a href="javascript:void(0);" class="button"
								onclick="selectCourseExamRules('finalexaminfoform_courseExamRulesId','finalexaminfoform_courseExamRulesName');"
								title="选择成卷规则"><span>选择成卷规则</span></a></td>

						</tr>
						<tr>
							<td width="15%">试卷类型:</td>
							<td width="35"><select name="examPaperType"
								style="width: 110px;"
								onchange="examPaperTypeOnchange(this.value)">
									<option value="random">随机试卷</option>
									<option value="fixed"
										<c:if test="${finalExamInfo.examPaperType eq 'fixed' }">selected="selected"</c:if>>固定试卷</option>
							</select> <span style="color: red;">*</span> <span style="color: blue;">(如果选择'固定试卷',请选择一份试卷)</span>
							</td>
							<td><span class="examPaperTypeFiexd"
								<c:if test="${finalExamInfo.examPaperType ne 'fixed' }">style="display:none;"</c:if>>考试试卷:</span></td>
							<td>
								<div class="examPaperTypeFiexd"
									<c:if test="${finalExamInfo.examPaperType ne 'fixed' }">style="display:none;"</c:if>>
									<input type="text" id="finalexaminfoform_courseExamPapersName"
										value="${finalExamInfo.courseExamPapers.paperName }"
										readonly="readonly" /> <input type="hidden"
										id="finalexaminfoform_courseExamPapersId"
										name="courseExamPapersId"
										value="${finalExamInfo.courseExamPapers.resourceid }" /> <input
										type="hidden" id="ei_branchSchoolId" name="ei_branchSchoolId"
										value="${unid }">; <a href="javascript:void(0);"
										class="button"
										onclick="selectCourseExamPapers('finalexaminfoform_courseExamPapersId','finalexaminfoform_courseExamPapersName');"
										title="选择考试试卷"><span>选择考试试卷</span></a>
								</div>
							</td>
						</tr>
						<tr>
							<td width="15%">交卷时是否显示成绩:</td>
							<td width="35"><gh:select dictionaryCode="yesOrNo"
									name="isShowScore" value="${finalExamInfo.isShowScore }"
									choose="N" /></td>
							<td width="15%">是否混合考:</td>
							<td width="35"><gh:select dictionaryCode="yesOrNo"
									name="ismixture"
									value="${empty finalExamInfo.ismixture ? 'N':finalExamInfo.ismixture }"
									choose="N" classCss="required" /><span style="color: red;">*</span>
							</td>

						</tr>
						<tr id="mixtrueScorePer_tr" style="display: none;">
							<td width="15%">混合机考笔考总分:</td>
							<td width="85" colspan="3"><input type="text"
								id="finalexaminfoform_mixtrueScorePer" name="mixtrueScorePer"
								value="<fmt:formatNumber value="${finalExamInfo.mixtrueScorePer }" pattern="##.#" />"
								class="required digits" max="100" min="0" style="width: 3%" /></td>
						</tr>
						<tr>
							<td width="15%">考试类型:</td>
							<td width="85"><gh:select
									dictionaryCode="CodeExamInfoExamType" name="examType"
									value="${finalExamInfo.examType }"
									id="finalexaminfoform_examType" classCss="required" /><span
								style="color: red;">*</span></td>
							<!-- 学院2016修改 -->
							<td><c:if test="${isorunit ne 'Y' }">
						教学站:
						</c:if></td>

							<td id="unid"><c:if test="${isorunit ne 'Y' }">
									<gh:brSchoolAutocomplete name="ei_branchSchoolId" tabindex="1"
										id="ei_branchSchoolId"
										defaultValue="${condition['finalExamInfo.brSchool.resourceid']}" />
								</c:if></td>

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
										onclick="$.pdialog.closeCurrent();">取消</button>
								</div>
							</div></li>
					</ul>
				</div>
			</form>
		</div>
	</div>
	<script type="text/javascript">
	$(document).ready(function(){
		
		var ismixture = "${finalExamInfo.ismixture }";
		$("#finalExamInfoForm select[id='ismixture']").change(function(){
			if($(this).val()=='Y'){
				$("#mixtrueScorePer_tr").show();
				$("#finalexaminfoform_mixtrueScorePer").attr("class","required digits");
			}else{
				$("#mixtrueScorePer_tr").hide();
				$("#finalexaminfoform_mixtrueScorePer").attr("class","");
			}
		});
		if("Y"==ismixture){
			$("#mixtrueScorePer_tr").show();
			$("#finalexaminfoform_mixtrueScorePer").attr("class","required digits");
		} else {
			$("#finalexaminfoform_mixtrueScorePer").removeClass("required digits");
		}
		searchExamResultChangCourse(); //学院2016修改
	});
	function selectCourseExamRules(ids,names){
		var courseId = $("#courseId").val();
		if(courseId==""){
			alertMsg.warn("请选择一门课程");
			return false;
		} else {
			var url = "${baseUrl }/edu3/framework/finalexaminfo/courseexamrule/list.html?courseId="+courseId+"&ids="+ids+"&names="+names+"&vid="+$("#"+ids).val();
			$.pdialog.open(url, "finalexaminfoform_courseExamRules", "选择成卷规则", {mask:true,width:800,height:600});
		}		
	}
	//选择试卷 //学院2016修改
	function selectCourseExamPapers(ids,names){
		var courseId = $("#courseId").val();
		if(courseId==""){
			alertMsg.warn("请选择一门课程");
			return false;
		} else {
			var url = "${baseUrl }/edu3/framework/finalexaminfo/courseexampapers/list.html?courseId="+courseId+"&ids="+ids+"&names="+names+"&vid="+$("#"+ids).val();
			$.pdialog.open(url, "finalexaminfoform_courseExamPapers", "选择考试试卷", {mask:true,width:800,height:600});
		}		
	}
	function examPaperTypeOnchange(examPaperType){
		if(examPaperType!="fixed"){
			$("#finalexaminfoform_courseExamPapersName").val('');
			$("#finalexaminfoform_courseExamPapersId").val('');
			$(".examPaperTypeFiexd").hide();
		} else {
			$(".examPaperTypeFiexd").show();
		}
	}
	//年级-专业 级联 //学院2016修改
	function searchExamResultChangCourse(){
		//var examsub = $("#finalexaminfoform_examSubId").val(); 
		//var unit="${unid}";
		var course="${finalExamInfo.course.resourceid }";
		$.ajax({
			type:"post",
			url:"${baseUrl}/edu3/schoolroll/graduate/audit/list/grade-major-course/page1.html",
			//data:{examsub:examsub,unit:unit,course:course},
			data:{course:course},
			dataType:"json",
			success:function(data){
				$('#courid').html(data['msg']);
				$("select[class*=flexselect]").flexselect();
			}
		});
		
	 }
	</script>
</body>
</html>