<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>毕业生数据</title>
</head>
<body>
	<h2 class="contentTitle">毕业生数据</h2>
	<div class="page">
		<div class="pageContent">
			<form id="graduate4" method="post"
				action="${baseUrl}/edu3/teaching/graduateData/save.html"
				class="pageForm" onsubmit="return validateCallback(this);">
				<input type="hidden" name="resourceid"
					value="${graduate.resourceid }" />
				<div class="pageFormContent" layoutH="97">
					<table class="form">
						<tr>
							<td width="20%">学生学号:</td>
							<td width="30%"><input type="text" id="stuNum3"
								name="stuNum" style="width: 50%"
								value="${graduate.studentInfo.studyNo }" class="required" /> <span
								class="buttonActive" style="margin-left: 8px"><div
										class="buttonContent">
										<button type="button" onclick="queryStudentInfo9()">查询</button>
									</div></span></td>
							<td width="20%">姓名:</td>
							<td width="30%"><input type="text" id="stuName3"
								name="stuName" style="width: 50%"
								value="${graduate.studentInfo.studentName }" readonly="readonly"
								class="required" /></td>
						</tr>
						<tr>
							<td>教学站:</td>
							<td><input type="text" id="stuCenter3" name="stuCenter"
								style="width: 50%" value="${graduate.studentInfo.branchSchool }"
								readonly="readonly" /></td>
							<td>年级:</td>
							<td><input type="text" id="grade3" name="grade"
								style="width: 50%" value="${graduate.studentInfo.grade }"
								readonly="readonly" /></td>
						</tr>
						<tr>
							<td>专业:</td>
							<td><input type="text" id="major3" name="major"
								style="width: 50%" value="${graduate.studentInfo.major }"
								readonly="readonly" /></td>
							<td>层次:</td>
							<td><input type="text" id="classic3" name="classic"
								style="width: 50%" value="${graduate.studentInfo.classic }"
								readonly="readonly" /></td>
						</tr>
						<tr>
							<td>毕业证书编号:</td>
							<td><input type="text" id="diplomaNum" name="diplomaNum"
								style="width: 50%" value="${graduate.diplomaNum }" /></td>
							<td>学位证书编号:</td>
							<td><input type="text" id="degreeNum" name="degreeNum"
								style="width: 50%" value="${graduate.degreeNum }" /></td>
						</tr>
						<tr>
							<td>学位名称:</td>
							<td><input type="text" id="degreeName" name="degreeName"
								style="width: 50%" value="${graduate.degreeName }" /></td>
							<td>毕业类型:</td>
							<td><gh:select name="graduateType"
									value="${graduate.graduateType }"
									dictionaryCode="CodeGraduateType" style="width:52%" /></td>
						</tr>
						<tr>
							<td>毕业日期:</td>
							<td><input type="text" name="graduateDate"
								style="width: 50%"
								value="<fmt:formatDate value="${graduate.graduateDate}" pattern="yyyy-MM-dd"/>"
								class="Wdate" onclick="WdatePicker({isShowWeek:true})" /></td>
							<td>入学日期:</td>
							<td><input type="text" name="entranceDate"
								style="width: 50%"
								value="<fmt:formatDate value="${graduate.entranceDate}" pattern="yyyy-MM-dd"/>"
								readonly="readonly" /></td>
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
	</div>
</body>
<script type="text/javascript">
<!--
	function queryStudentInfo9(){
		var stuNum = jQuery("#graduate4 #stuNum3").val();
		if(stuNum!=""){
			jQuery.ajax({
				data:"stuNum="+stuNum,
				dataType:"json",
				url:"${baseUrl}/edu3/teaching/graduateNo/queryStudentInfo.html",
				success:function(data){
					jQuery("#stuName3").val(data.studentName);
					jQuery("#stuCenter3").val(data.schoolCenter);
					jQuery("#grade3").val(data.grade);
					jQuery("#major3").val(data.major);
					jQuery("#classic3").val(data.classic);
				}
			})
		}
	}
//-->
</script>
</html>