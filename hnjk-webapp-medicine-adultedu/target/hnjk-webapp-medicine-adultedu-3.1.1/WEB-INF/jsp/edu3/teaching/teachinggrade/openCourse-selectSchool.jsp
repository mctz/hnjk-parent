<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>选择要开课的教学点</title>
<script type="text/javascript">
		$(document).ready(function(){
			$("#teachPlan_openCourse_brSchoolid").flexselect();
		});
		// 对某个教学点按年级教学计划开课
		function openCourseByGuiplan(){
			var guiplanIds = $("#openCourseBySuggestTerm_guiplanIds").val();
			var schoolId = $("#teachPlan_openCourse_brSchoolid").val();
			var url = baseUrl+"/edu3/teaching/teachinggrade/openCourseBySuggestTerm.html"; 
			if(!schoolId) {
				alertMsg.warn("请选择一个教学点");
				return false;
			}
			
			$("#openCourseBySuggestTerm_open").attr("disabled","disabled");
			$("#openCourseBySuggestTerm_close").attr("disabled","disabled");
			$.ajax({
				type:'POST',
				url:url,
				data:{guiplanIds:guiplanIds,schoolId:schoolId},
				dataType:"json",
				cache: false,
				error: DWZ.ajaxError,
				success: function(data){
					$("#openCourseBySuggestTerm_open").removeAttr("disabled");
					$("#openCourseBySuggestTerm_close").removeAttr("disabled");
					if(data['resultCode'] == 300){
						alertMsg.error(data['msg']);
				    }else{
			    		alertMsg.correct(data['msg']);
					}
				}
			});
		}
	</script>
</head>
<body>
	<div align="center">
		<div style="margin-top: 60px;"
			id="openCourseBySuggestTerm-selectSchool">
			<div align="left" style="margin-left: 30px; margin-bottom: 10px;">
				教学点： <input type="hidden" id="openCourseBySuggestTerm_guiplanIds"
					value="${guiplanIds }" />
				<gh:brSchoolAutocomplete name="openCourse_brSchoolid" tabindex="1"
					id="teachPlan_openCourse_brSchoolid"
					defaultValue="${condition['schoolId']}" displayType="code"
					style="width:60%" />
				<%--<select  class="flexselect" id="teachPlan_openCourse_brSchoolid" style="width: 60%" tabindex=1 >${selectSchool}</select>--%>
			</div>
		</div>
		<div style="margin-top: 80px; margin-right: 5px;" align="right">
			<button id="openCourseBySuggestTerm_open" type="button"
				onclick="return openCourseByGuiplan();" style="cursor: pointer;">开课</button>
			<button id="openCourseBySuggestTerm_close" type="button"
				class="close" onclick="$.pdialog.closeCurrent();"
				style="cursor: pointer;">取消</button>
		</div>
	</div>
</body>
</html>