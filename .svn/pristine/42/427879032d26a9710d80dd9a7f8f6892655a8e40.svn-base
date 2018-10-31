<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>查看详细信息</title>
</head>

<body>
	<div class="page">
		<div class="pageContent">
			<div class="pageFormContent" layoutH="97">
				<table class="form">
					<tr>
						<td width="20%">年度：</td>
						<td width="30%"> ${cadreInfo.yearInfo.yearName }</td>
						<td width="20%">学期：</td>
						<td width="30%">${ghfn:dictCode2Val('CodeTerm',cadreInfo.term ) }</td>
					</tr>
					<tr>
						<td>学号：</td>
						<td>${cadreInfo.studyNo}</td>
						<td>姓名：</td>
						<td>${cadreInfo.studentName}</td>
					</tr>
					<tr>
						<td>教学点：</td>
						<td>${cadreInfo.studentInfo.branchSchool.unitName}</td>
						<td>年级：</td>
						<td>${cadreInfo.studentInfo.grade.gradeName}</td>
					</tr>
					<tr>
						<td>专业：</td>
						<td>${cadreInfo.studentInfo.major.majorName}</td>
						<td>班级：</td>
						<td>${cadreInfo.studentInfo.classes.classname}</td>
					</tr>
					<tr>
						<td>现任部门：</td>
						<td>${ghfn:dictCode2Val('Code.WorkManage.department',cadreInfo.department_current ) }</td>
						<td>现任职位：</td>
						<td>${ghfn:dictCode2Val('Code.WorkManage.position',cadreInfo.position_current ) }</td>
					</tr>
					<tr>
						<td>组织：</td>
						<td>${ghfn:dictCode2Val('Code.WorkManage.organization',cadreInfo.organization ) }</td>
						<td>状态：</td>
						<td>${ghfn:dictCode2Val('Code.WorkManage.positionStatus',cadreInfo.status ) }</td>
					</tr>
					<tr>
						<td>竞选部门：</td>
						<td>${ghfn:dictCode2Val('Code.WorkManage.department',cadreInfo.department ) }</td>
						<td>竞选职位：</td>
						<td>${ghfn:dictCode2Val('Code.WorkManage.position',cadreInfo.position ) }</td>
					</tr>
					<tr>
						<td>调剂职位：</td>
						<td>${ghfn:dictCode2Val('Code.WorkManage.position',cadreInfo.position_adjust ) }</td>
						<td>任职时间</td>
						<td> <fmt:formatDate value="${cadreInfo.jobTime }" pattern="yyyy-MM-dd"/></td>
					</tr>
					<tr>
						<td>第一学期平均分：</td>
						<td>${cadreInfo.avgScore1 }</td>
						<td>第二学期平均分：</td>
						<td>${cadreInfo.avgScore2 }</td>
					</tr>
					<tr>
						<td>是否候选人：</td>
						<td>${ghfn:dictCode2Val('yesOrNo',cadreInfo.isCandidate ) }</td>
						<td>是否任用：</td>
						<td>${ghfn:dictCode2Val('yesOrNo',cadreInfo.isAppoint ) }</td>
					</tr>
					<tr>
						<td>在校获奖情况：</td>
						<td colspan="3"><textarea name="memo" style="width: 70%" rows="3" readonly="readonly">${cadreInfo.awards }</textarea></td>
					</tr>
					<tr>
						<td>主要学生工作经历：</td>
						<td colspan="3"><textarea name="memo" style="width: 70%" rows="3" readonly="readonly">${cadreInfo.workExperience }</textarea></td>
					</tr>
					<tr>
						<td>对竞聘职务认识及工作设想：</td>
						<td colspan="3"><textarea name="memo" style="width: 70%" rows="5" readonly="readonly">${cadreInfo.intention }</textarea></td>
					</tr>
					<tr>
						<td>备注：</td>
						<td colspan="3"><textarea name="memo" style="width: 70%" rows="2" readonly="readonly">${cadreInfo.memo }</textarea></td>
					</tr>
				</table>
			</div>
		</div>
	</div>
</body>
</html>