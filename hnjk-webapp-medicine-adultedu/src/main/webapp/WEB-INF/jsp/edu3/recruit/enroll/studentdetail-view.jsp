<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>学生报名详细信息</title>
<script type="text/javascript">
 </script>
</head>
<body>
	<h2 class="contentTitle">学生报名详细信息</h2>
	<div class="page">
		<div class="pageContent">
			<form method="post"
				action="${baseUrl}/edu3/recruit/recruitplan/saveplan.html"
				class="pageForm" onsubmit="return validateCallback(this);">
				<div class="pageFormContent" layoutH="97">
					<table class="form">
						<tr>
							<td style="width: 12%">姓名:</td>
							<td style="width: 38%">${student.name }</td>
							<td style="width: 12%; text-align: center;" rowspan="5">相片：</td>
							<td style="width: 38%" rowspan="5"><c:if
									test="${not empty student.photoPath}">
									<img
										src="${baseUrl}/attachs/common/students/${student.photoPath}"
										width="100" height="200">
								</c:if></td>
						</tr>
						<tr>
							<td style="width: 12%">性别:</td>
							<td style="width: 38%">${ghfn:dictCode2Val('CodeSex',student.gender)}</td>

						</tr>
						<tr>
							<td style="width: 12%">证件类别:</td>
							<td style="width: 38%">${ghfn:dictCode2Val('CodeCertType',student.certType)}</td>

						</tr>
						<tr>
							<td style="width: 12%">证件号码：</td>
							<td style="width: 38%">${student.certNum }</td>
						</tr>
						<tr>
							<td style="width: 12%">准考证号:</td>
							<td style="width: 38%">${enrolleeinfo.examCertificateNo }</td>
						</tr>
						<tr>
							<td style="width: 12%">招生批次:</td>
							<td style="width: 38%">
								${enrolleeinfo.recruitMajor.recruitPlan.recruitPlanname}</td>
							<td style="width: 12%">教学站:</td>
							<td style="width: 38%">
								${enrolleeinfo.branchSchool.unitName}</td>
						</tr>
						<tr>
							<td style="width: 12%">招生专业:</td>
							<td style="width: 38%">
								${enrolleeinfo.recruitMajor.recruitMajorName}</td>
							<td style="width: 12%">电子邮件：</td>
							<td style="width: 38%">${student.email }</td>
						</tr>
						<tr>
							<td style="width: 12%">联系电话:</td>
							<td style="width: 38%">${student.contactPhone }</td>
							<td style="width: 12%">手机号码:</td>
							<td style="width: 38%">${student.mobile }</td>
						</tr>
						<tr>
							<td style="width: 12%">地址：</td>
							<td style="width: 38%">${student.contactAddress }</td>
							<td style="width: 12%">邮编:</td>
							<td style="width: 38%">${student.contactZipcode }</td>
						</tr>
						<tr>
							<td style="width: 12%">出生日期:</td>
							<td style="width: 38%">${student.bornDay }</td>
							<td style="width: 12%">婚否:</td>
							<td style="width: 38%">
								${ghfn:dictCode2Val('CodeMarriage',student.marriage)}</td>
						</tr>
						<tr>
							<td style="width: 12%">籍贯:</td>
							<td>${student.homePlace }</td>
							<td style="width: 12%">户口所在地:</td>
							<td style="width: 38%">${student.residence }</td>
						</tr>
						<tr>
							<td style="width: 12%">民族:</td>
							<td style="width: 38%">
								${ghfn:dictCode2Val('CodeNation',student.nation)}</td>
							<td style="width: 12%">政治面目:</td>
							<td style="width: 38%">
								${ghfn:dictCode2Val('CodePolitics',student.politics)}</td>
						</tr>
						<tr>


							<td style="width: 12%">工作单位:</td>
							<td style="width: 38%" colspan="3">${student.officeName }</td>
						</tr>
						<tr>
							<td style="width: 12%">职位职称:</td>
							<td style="width: 38%">${student.title}</td>
							<td style="width: 12%">单位电话:</td>
							<td style="width: 38%">${student.officePhone }</td>
						</tr>



						<tr>
							<td style="width: 12%">最后学历:</td>
							<td style="width: 38%">
								${ghfn:dictCode2Val('CodeEducation',ei.educationalLevel)}</td>
							<td style="width: 12%">入学前学校代码</td>
							<td style="width: 38%">${enrolleeinfo.graduateSchoolCode }</td>
						</tr>
						<tr>
							<td style="width: 12%">毕业学校:</td>
							<td style="width: 38%">${enrolleeinfo.graduateSchool }</td>
							<td style="width: 12%">毕业学校编号:</td>
							<td style="width: 38%">${enrolleeinfo.graduateSchoolCode }</td>
						</tr>
						<tr>
							<td style="width: 12%">毕业证书号:</td>
							<td style="width: 38%">${enrolleeinfo.graduateId }</td>
							<td style="width: 12%">毕业专业:</td>
							<td style="width: 38%">${enrolleeinfo.graduateMajor }</td>
						</tr>
						<tr>
							<td style="width: 12%">毕业时间:</td>
							<td style="width: 38%">${enrolleeinfo.graduateDate}</td>
							<td></td>
							<td></td>
						</tr>
						<tr>
							<td style="width: 12%">备注说明:</td>
							<td colspan="3">${student.memo }</td>
						</tr>

					</table>
					</br>
					<table class="form">
						<thead>
							<strong> 个人简历:</strong>
						</thead>
						<tr>
							<th>编号</th>
							<th>开始年月</th>
							<th>结束年月</th>
							<th>工作单位</th>
							<th>职务</th>
						</tr>
						<c:forEach items="${enrolleeinfo.studentBaseInfo.studentResume}"
							var="resume" varStatus="vs">
							<tr>
								<td><q>${vs.index+1}</q></td>
								<td>开始年: <gh:select name="startYear"
										dictionaryCode="CodeYear" value="${resume.startYear}" /> 开始月:
									<gh:select name="startMonth" dictionaryCode="CodeMonth"
										value="${resume.startMonth}" />
								</td>
								<td>截止年: <gh:select name="endYear"
										dictionaryCode="CodeYear" value="${resume.endYear}" /> 截止月: <gh:select
										name="endMonth" dictionaryCode="CodeMonth"
										value="${resume.endMonth}" />
								</td>
								<td>工作单位: <input type="text" name="company" size="34"
									value="${resume.company}" />
								</td>
								<td>职务: <input type="text" name="resumetitle" size="12"
									value="${resume.title}" />
								</td>
							</tr>
						</c:forEach>
					</table>
				</div>
				<div class="formBar">
					<ul>
						<li><div class="button">
								<div class="buttonContent">
									<button type="button" class="close"
										onclick="navTab.closeCurrentTab();">关闭</button>
								</div>
							</div></li>
					</ul>
				</div>
			</form>
</body>
</html>