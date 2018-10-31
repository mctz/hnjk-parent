<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>随堂练习回答情况统计</title>
<script type="text/javascript">
$(document).ready(function(){
	$("select[class*=flexselect]").flexselect();
	activeCourseExamQueryBegin();
});
//打开页面或者点击查询（即加载页面执行）
function activeCourseExamQueryBegin() {
	var defaultValue = "${condition['unitid']}";
	var schoolId = "${activecourseexamstatUnit}";
	var gradeId = "${condition['gradeid']}";
	var classicId = "${condition['classicid']}";
	var teachingType = "${condition['teachingtype']}";
	
	
	var selectIdsJson = "{unitId:'activecourseexam_unitId',gradeId:'activecourseexam_gradeid',classicId:'activecourseexam_classicid',teachingType:'activecourseexam_teachingtype'}";
	cascadeQuery("begin", defaultValue, schoolId, gradeId, classicId,teachingType, "", "", selectIdsJson);
}
//选择教学点
function activeCourseExamQueryUnit() {
	var defaultValue = $("#activecourseexam_unitId").val();
	var selectIdsJson = "{gradeId:'activecourseexam_gradeid',classicId:'activecourseexam_classicid',teachingType:'activecourseexam_teachingtype'}";
	cascadeQuery("unit", defaultValue, "", "", "", "", "", "",selectIdsJson);
}

// 选择年级
function activeCourseExamQueryGrade() {
	var defaultValue = $("#activecourseexam_unitId").val();
	var gradeId = $("#activecourseexam_gradeid").val();
	var selectIdsJson = "{classicId:'activecourseexam_classicid',teachingType:'activecourseexam_teachingtype'}";
	cascadeQuery("grade", defaultValue, "", gradeId, "", "", "", "",selectIdsJson);
}

// 选择层次
function activeCourseExamQueryClassic() {
	var defaultValue = $("#activecourseexam_unitId").val();
	var gradeId = $("#activecourseexam_gradeid").val();
	var classicId = $("#activecourseexam_classicid").val();
	var selectIdsJson = "{teachingType:'activecourseexam_teachingtype'}";
	cascadeQuery("classic", defaultValue, "", gradeId, classicId, "", "", "", selectIdsJson);
}
</script>
</head>
<body>

	<div class="page">
		<div class="pageHeader">
			<form onsubmit="return navTabSearch(this);"
				action="${baseUrl }/edu3/metares/exercise/activeexercise/stat.html"
				method="post">
				<div class="searchBar">
					<ul class="searchContent">
						
						<%-- <c:if test="${empty activecourseexamstatUnit }"> --%>
							<li class="custom-li"><label>教学站：</label> <span
								sel-id="activecourseexam_unitId" sel-name="unitid"
								sel-onchange="activeCourseExamQueryUnit()"
								sel-classs="flexselect"></span></li>
						<%-- </c:if> --%>
						<li><label>年级：</label> <span
							sel-id="activecourseexam_gradeid" sel-name="gradeid"
							sel-onchange="activeCourseExamQueryGrade()"
							sel-style="width: 120px"></span></li>
						<li><label>层次：</label> <span
							sel-id="activecourseexam_classicid" sel-name="classicid"
							sel-onchange="activeCourseExamQueryClassic()"
							sel-style="width: 120px"></span></li> 
						<li><label>教学模式：</label> <span
							sel-id="activecourseexam_teachingtype" sel-name="teachingtype"
							dictionaryCode="CodeTeachingType" sel-style="width: 100px"></span>
						</li>
					</ul>
					<ul class="searchContent">
						<li class="custom-li"><label>课程：</label>
							 ${activecourseexamstat }<font color="red"> *</font></li>
					</ul>
					<div class="subBar">
						<ul>
							<li><div class="buttonActive">
									<div class="buttonContent">
										<button type="submit">查 询</button>
									</div>
								</div></li>
						</ul>
					</div>
				</div>
			</form>
		</div>
		<div class="pageContent">
			<table class="table" layouth="138">
				<thead>
					<tr>
						<th width="3%"></th>
						<th width="10%">课程</th>
						<th width="10%">教学站</th>
						<th width="8%">年级</th>
						<th width="8%">层次</th>
						<th width="8%">教学模式</th>
						<th width="17%">知识节点</th>
						<th width="18%">答题正确率</th>
						<th width="18%">答题错误率</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${activecourseexamStatList }" var="stat">
						<tr>
							<td>&nbsp;</td>
							<td>${stat.coursename }</td>
							<td>${stat.unitname }</td>
							<td>${stat.gradename }</td>
							<td>${stat.classicname }</td>
							<td>${ghfn:dictCode2Val('CodeTeachingType',stat.teachingType)}</td>
							<td>${stat.nodename }</td>
							<td><span class="active_optionbar"
								title="<fmt:formatNumber type='percent' pattern='###.##%' value='${stat.correctper }'/>"><span
									style="width: <fmt:formatNumber type='percent' pattern='###.##%' value='${stat.correctper }'/>;"></span></span>&nbsp;<fmt:formatNumber
									type="percent" pattern="###.##%" value="${stat.correctper }" /></td>
							<td><span class="active_optionbar"
								title="<fmt:formatNumber type='percent' pattern='###.##%' value='${stat.mistakeper }'/>"><span
									style="width: <fmt:formatNumber type='percent' pattern='###.##%' value='${stat.mistakeper }'/>;"></span></span>&nbsp;<fmt:formatNumber
									type="percent" pattern="###.##%" value="${stat.mistakeper }" /></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
	</div>
</body>
</html>