<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>课程安排表</title>
<style type="text/css">
	th,td{text-align: center;}
</style>
</head>
<script type="text/javascript">
var colspan = 21;
$(document).ready(function(){
	
	var flag = "${flag}";
	var isBrschool = "${isBrschool}";
	if(flag!='export'){
		colspan = colspan-2;
	}
	if(isBrschool=='Y'){
		colspan = colspan-1;
	}
});

	function exportTable(){
		var url = "${baseUrl}/edu3/teaching/teachingplancoursetimetable/exportKCAP.html?flag=export";
		url += "&brSchoolid=${brSchoolid}&gradeid=${gradeid}&classicid=${classicid}&teachingType=${teachingType}";
		url += "&majorid=${majorid}&classesid=${classesid}&courseId=${courseId}&term=${term}&isOpen=${isOpen}&status=${status}";
		downloadFileByIframe(url,'exportKCAP_Ifram',"post");
	}
</script>
<body>
	<div class="page">
		<table class="list" style="width: 99%" border="1">
			<thead>
				<tr>
					<th colspan=
						<c:choose>
							<c:when test="${flag!='export' }">
								<c:if test="${isBrschool ne 'Y' }">"21"</c:if>
								<c:if test="${isBrschool eq 'Y' }">"20"</c:if>
							</c:when>
							<c:otherwise>
								<c:if test="${isBrschool ne 'Y' }">"23"</c:if>
								<c:if test="${isBrschool eq 'Y' }">"22"</c:if>
							</c:otherwise>
						</c:choose>
					>
						<br><font style="font-size: 20px;">${schoolConnectName }${fn:replace(fn:replace(termName,'春季','第一'),'秋季','第二') }课程表</font>
						<!-- <br>开学时间： 2016年2月28日（周日，开课第一天） -->
					<br>上课时间：<label style="color:#ff66cc;">1段</label>：1-3节 上午(9:30-11:50) ；<label style="color:#ff66cc;">2段</label>：4-6节 下午（ 13：00-15:10）；
					<label style="color:#ff66cc;">3段</label>：7-9节 下午（15:40—17:50）；<label style="color:#ff66cc;">4段</label>：10-12节 晚上（19：00--21：15）。
						<br><label style="color: grey;font-size: smaller">备注：此处【1段】、【2段】为上课时间名称，请在【设置上课时间】菜单统一设置！排课内容为 “上课时间段”名称 + “上课日期” + “上课地点”</label>
						<c:if test="${isBrschool eq 'Y' }"><br>&nbsp;<div style="text-align: left;margin-bottom: 5px;">教学单位名称：${schoolName }（盖章）</div></c:if>
					</th>
					<c:if test="${flag!='export' }">
						<th colspan="2">
							<div class="buttonActive">
								<div class="buttonContent"> <button style="text-align: center;" onclick="exportTable()">导 出</button> </div>
							</div>
						</th>
					</c:if>
				</tr>
				<tr>
					<c:if test="${isBrschool ne 'Y' }">
						<th width="2%">外<br>教<br>点</th>
					</c:if>
					<th width="3%">序号<br>(页码)</th>
					<th width="6%">专业班级名称</th>
					<th width="2%">层<br>次</th>
					<th width="2%">形<br>式</th>
					<th width="3%">学生<br>人数</th>
					<th width="6%">课程名称</th>
					<th width="3%">计划<br>学时</th>
					<th width="3%">面授<br>学时</th><%--广外--%>
					<th width="3%">考核<br>类型</th>
					<th width="5%">任课教师</th>
					<th width="4%">职称</th>
					<th width="6%">任课教师<br>电话</th>
					<th width="5%">星期天</th>
					<th width="5%">星期一</th>
					<th width="5%">星期二</th>
					<th width="5%">星期三</th>
					<th width="5%">星期四</th>
					<th width="5%">星期五</th>
					<th width="5%">星期六</th>
					<th width="5%">合班情况</th>
					<th width="4%">上课<br>时间</th>
					<th width="4%">排课人</th>
				</tr>
			</thead>
		</table>
	<div  style="overflow:scroll;position:relative;" layouth="155">
		<table class="list" border="1">
			<tbody>
				<c:forEach items="${mapList}" var="m" varStatus="vs">
					<tr>
						<c:if test="${isBrschool ne 'Y' }">
							<c:if test="${ not empty m.rowspan }"><td width="2%" rowspan="${m.rowspan }">${m.unitname }</td></c:if>
						</c:if>
						<td width="3%">${m.orderNum }</td>
						<td width="6%">${m.classesName }</td>
						<td width="2%">${m.classicName }</td>
						<td width="2%">${ghfn:dictCode2Val('CodeTeachingType',m.teachingType) }</td>
						<td width="3%">${m.stunum }</td>
						<td width="6%">${m.courseName }</td>
						<td width="3%">${m.stydyhour }</td>
						<td width="3%">${m.facestudyhour }</td>
						<td width="3%">${ghfn:dictCode2Val('CodeExamClassType',m.examclasstype) }</td>
						<td width="5%">${fn:replace(m.lecturername,",","<br>") }</td>
						<td width="4%">${fn:replace(ghfn:dictCode2Val('CodeTitleOfTechnicalCode',m.titleoftechnical),",","<br>") }</td>
						<td width="6%">${fn:replace(m.mobile,",","<br>")  }</td>
						<c:forEach begin="0" end="6" step="1" varStatus="index">
			       			<td width="5%">${m.timePeriod[index.count-1]}</td>
			       		</c:forEach>
			       		<td width="5%" title="${m.mergeMemo }">${m.mergeMemo }</td>
						<td width="4%">${m.time }</td>
			       		<td width="4%">${m.operatorName }</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</div>
	</div>
</body>
</html>