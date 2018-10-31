<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>招生预约报读信息</title>
</head>
<body>
	<h2 class="contentTitle">编辑招生预约报读信息</h2>
	<div class="page">
		<div class="pageContent">
			<form method="post"
				action="${baseUrl}/edu3/enrollment/booking/save.html" class="pageForm"
				onsubmit="return validateCallback(this);">
				<input type="hidden" name="resourceid" value="${info.resourceid }" />
				<div class="pageFormContent" layoutH="97">
					<table class="form">
						<tr>
							<td width="15%">姓名:</td>
							<td width="85%">
								<input type="text"  name="studentName" style="width: 40%" value="${info.studentName }"  class="required" />
							</td>
						</tr>
						<tr>
							<td width="15%">身份证号:</td>
							<td width="85%">
								<input type="text"  name="certNum" style="width: 40%" value="${info.certNum }"  class="required idnumber" />
							</td>
						</tr>
						<tr>
							<td width="15%">预报读年级:</td>
							<td width="85%">
								<gh:selectModel name="gradeId" bindValue="resourceid" displayValue="gradeName"
											orderBy="yearInfo.firstYear desc,term desc,resourceid desc"
											modelClass="com.hnjk.edu.basedata.model.Grade" style="width:40%;"  
											value="${info.grade.resourceid}" classCss="required" />
							</td>
						</tr>
						<tr>
							<td width="15%">预报读层次:</td>
							<td width="85%">
								<gh:selectModel name="classicId" bindValue="resourceid" displayValue="classicName"
											value="${classes.classic.resourceid}" style="width:40%;"  
											modelClass="com.hnjk.edu.basedata.model.Classic" classCss="required" /> 
							</td>
						</tr>
						<tr>
							<td width="15%">预报读专业:</td>
							<td width="85%">
								<gh:selectModel name="majorId" bindValue="resourceid"
											displayValue="majorCodeName" classCss="required"
											modelClass="com.hnjk.edu.basedata.model.Major"
											value="${info.major.resourceid}" orderBy="majorCode,majorName" style="width:40%;" />
							</td>
						</tr>
						<tr>
							<td width="15%">联系电话:</td>
							<td width="85%">
								<input type="text"  name="phone" style="width: 40%" value="${info.phone }" class="required" />
							</td>
						</tr>
						<c:if test="${isAdmin eq 'Y' }">
							<tr>
								<td width="15%">所属教学点:</td>
								<td width="85%">
									<gh:brSchoolAutocomplete name="unitId"  tabindex="2"
											id="bookingInfo_form_unitId"
											defaultValue="${info.unit.resourceid}" displayType="code" style="width:40%;" />
									<span style="color: red;">*</span>
								</td>
							</tr>
						</c:if>
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
</html>