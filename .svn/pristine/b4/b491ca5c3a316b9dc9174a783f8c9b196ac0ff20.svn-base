<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>选择年级</title>
<script type="text/javascript">
	   // 根据年级同步招生专业信息到联动查询表
		function sysncRecruitMajor(){
		   var gradeId = $("#linkageQuery-selectGrade-gradeId").val();
		   var url = "${baseUrl}/edu3/teaching/linkageQuery/syncLinkageQuery.html";
			$.ajax({
				type:'POST',
				url:url,
				data:{gradeId:gradeId},
				dataType:"json",
				cache: false,
				error: DWZ.ajaxError,
				success: function(data){
					$("#openOrAdjustCourse_open").removeAttr("disabled");
					$("#openOrAdjustCourse_close").removeAttr("disabled");
					if(data['statusCode'] == 300){
						alertMsg.error(data['message']);
				    }else{
			    		alertMsg.correct(data['message']);
			    		navTabPageBreak();
					}
				}
			});
		}
	</script>
</head>
<body>
	<div align="center">
		<div style="margin-top: 40px;" id="linkageQuery-selectGrade">
			<div align="left"
				style="margin-left: 30px; margin-bottom: 10px; color: red;">
				备注：请选择一个年级，将该年级的招生专业信息同步到联动查询表中。</div>
			<div align="left" style="margin-left: 30px; margin-bottom: 10px;">
				<label>年级： </label>
				<gh:selectModel id="linkageQuery-selectGrade-gradeId" name="gradeId"
					bindValue="resourceid" displayValue="gradeName" classCss="required"
					modelClass="com.hnjk.edu.basedata.model.Grade" value=""
					orderBy="yearInfo.firstYear desc" style="width:60%" />

			</div>
		</div>
		<div style="margin-top: 70px; margin-right: 5px;" align="right">
			<button id="linkageQuery_open" type="button"
				onclick="return sysncRecruitMajor();" style="cursor: pointer;">同步</button>
			<button id="linkageQuery_close" type="button" class="close"
				onclick="$.pdialog.closeCurrent();" style="cursor: pointer;">取消</button>
		</div>
	</div>
</body>
</html>