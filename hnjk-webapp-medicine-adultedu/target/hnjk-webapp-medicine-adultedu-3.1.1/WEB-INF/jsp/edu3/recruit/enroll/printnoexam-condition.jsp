<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>报名信息打印</title>


</head>
<body>
	<div class="page">
		<div class="pageHeader">
			<div class="searchBar">
				<form id="f1" method="post" action=${baseUrl }
					/edu3/recruit/enroll/printnoexam-view.html" class="pageForm">
					<ul class="searchContent">
						<li style="width: 250px"><label>招生批次：</label> <gh:selectModel
								id="_printexamexcused_recruitPlan" name="recruitPlan"
								bindValue="resourceid" displayValue="recruitPlanname"
								isSubOptionText="Y"
								modelClass="com.hnjk.edu.recruit.model.RecruitPlan"
								value="${condition['recruitPlan']}"
								orderBy="yearInfo.firstYear desc" choose="Y"
								style="width: 150px" /> *</li>
						<li style="width: 250px"><label>专业：</label> <gh:selectModel
								id="_printexamexcused_major" name="major" bindValue="resourceid"
								displayValue="majorName" isSubOptionText="Y"
								modelClass="com.hnjk.edu.basedata.model.Major"
								value="${condition['major']}" style="width: 150px" /></li>
						<li style="width: 250px"><label>层次：</label> <gh:selectModel
								id="_printexamexcused_classic" name="classic"
								bindValue="resourceid" displayValue="classicName"
								isSubOptionText="Y"
								modelClass="com.hnjk.edu.basedata.model.Classic"
								value="${condition['classic']}" style="width: 150px" /></li>
						<li style="width: 250px"><label>办学模式：</label>
						<gh:select id="_printexamexcused_teachingType" name="teachingType"
								dictionaryCode="CodeTeachingType"
								value="${condition['teachingType']}" isSubOptionText="Y"
								style="width: 150px" /></li>
						<li style="width: 250px"><label>报名类型：</label>
						<gh:select id="_printexamexcused_enrollType" name="enrollType"
								dictionaryCode="CodeEnrolleeType"
								value="${condition['enrollType']}" isSubOptionText="Y"
								style="width: 150px" /></li>
						<li style="width: 250px"><label>证件号码：</label><input
							type="text" id="_printexamexcused_certNum" style="width: 150px">
						</li>
					</ul>
				</form>
			</div>
		</div>
		<div class="buttonActive" style="float: right;">
			<div class="buttonContent">
				<button id="b2" type="button" onclick="printExamForm();">
					打印</button>
			</div>
		</div>
	</div>

	<script type="text/javascript">
	function printExamForm(){
		if($("#_printexamexcused_recruitPlan").val()==""){
			alert("请选择批次");
			return;
		}
		var recruitPlan	=jQuery("#_printexamexcused_recruitPlan").val();
		var major		=jQuery("#_printexamexcused_major").val();
		var classic		=jQuery("#_printexamexcused_classic").val();
		var teachingType=jQuery("#_printexamexcused_teachingType").val();
		var enrollType	=jQuery("#_printexamexcused_enrollType").val();
		var certNum 	=jQuery("#_printexamexcused_certNum").val();
		$.pdialog.closeCurrent();
		var url = "${baseUrl}/edu3/recruit/enroll/printnoexam-view.html?recruitPlan="+recruitPlan+"&major="+major+"&classic="+classic+"&teachingType="+teachingType+"&enrollType="+enrollType+"&certNum="+certNum;
		$.pdialog.open(url,'RES_RECRUIT_NOEXAMAUDIT_PRINT_VIEW','打印预览',{height:600, width:800});
	}
</script>
</body>
</html>