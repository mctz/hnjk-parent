<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>用户具体信息</title>
</head>
<body>
	<h2 class="contentTitle" style="border-bottom: medium;">${edumanager.cnName}</h2>
	<div class="page">
		<div class="pageFormContent" layoutH="25">
			<table class="form">
				<tbody>
					<tr>
						<td width="20%">用户中文名：</td>
						<td width="30%">${edumanager.cnName }</td>
						<td width="20%">用户所在组织单位：</td>
						<td width="30%">${edumanager.orgUnit.unitName}</td>
					</tr>
					<tr>
						<td>中文拼音：</td>
						<td>${edumanager.namePY}</td>
						<td>人员编号：</td>
						<td>${edumanager.teacherCode }</td>
					</tr>
					<tr>
						<td>职称：</td>
						<td>${ghfn:dictCode2Val('CodeTitleOfTechnicalCode',edumanager.titleOfTechnical ) }</td>
						<td>职务：</td>
						<td>${ghfn:dictCode2Val('CodeDuty',edumanager.duty)}</td>
					</tr>
					<tr>
						<td>文化程度：</td>
						<td>${ghfn:dictCode2Val('CodeEducation',edumanager.education ) }</td>
						<td>性别：</td>
						<td>${ghfn:dictCode2Val('CodeSex',edumanager.gender) }</td>
					</tr>
					<tr>
						<td>生日：</td>
						<td>${edumanager.birthday}</td>
						<td>电话：</td>
						<td>${edumanager.officeTel }</td>
					</tr>
					<tr>
						<td>家庭电话：</td>
						<td>${edumanager.homeTel }</td>
						<td>手机：</td>
						<td>${edumanager.mobile }</td>
					</tr>
					<tr>
						<td>电子邮件：</td>
						<td>${edumanager.email }</td>
						<td>邮编：</td>
						<td>${edumanager.zipcode }</td>
					</tr>
					<tr>
						<td>文号：</td>
						<td>${edumanager.documentcode }</td>
						<td>教师角色：</td>
						<td><c:forEach items="${edumanager.roles}" var="role"
								varStatus="vss">
		            		${role.roleName }
		            		<c:if test="${not vss.last }">,</c:if>
							</c:forEach></td>
					</tr>
					<tr>
						<td>原单位:</td>
						<td>${edumanager.oldUnitName }</td>
						<td>身份证号码：</td>
						<td>${edumanager.certNum }</td>
					</tr>

					<tr>
						<td>编制：</td>
						<td>${ghfn:dictCode2Val('teacherOrgUnitType',edumanager.orgUnitType ) }</td>
						<td>最高学历：</td>
						<td>${ghfn:dictCode2Val('CodeEducation',edumanager.educationalLevel)}</td>
					</tr>

					<tr>
						<td>毕业时间:</td>
						<td>${edumanager.graduateDate }</td>
						<td>毕业学校 :</td>
						<td>${edumanager.graduateSchool }</td>
					</tr>

					<tr>
						<td>邮编:</td>
						<td>${edumanager.postCode }</td>
						<td>当前担任课程 :</td>
						<td>${edumanager.currentCourse }</td>
					</tr>
					<tr>
						<td>单位通信地址：</td>
						<td colspan="3">${edumanager.homeAddress }</td>
					</tr>
					<tr>
						<td>简介：</td>
						<td colspan="3">${edumanager.introduction }</td>
					</tr>
				</tbody>
			</table>
		</div>
	</div>

</body>
</html>