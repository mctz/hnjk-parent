<%@page contentType="text/html;charset=UTF-8"%>
<%@include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>毕业信息统计</title>
<style type="text/css">
</style>
<script type="text/javascript">	
//导出毕业信息
function exportStatistical(){
	var statistical_branchSchool = $("#statistical_branchSchool").val(); //办学点
	var statistical_classesSelect   = $("#statistical_classesSelect").val();//班级
	var statistical_classic  = $("#statistical_classic").val();//层次
	var statistical_teachingType    = $("#statistical_teachingType").val();//学习形式
	
	var url = "${baseUrl}/edu3/roll/graduationStudent/exportGraduationInfos.html"
		+"?statistical_branchSchool="+statistical_branchSchool
		+"&statistical_classesSelect="+statistical_classesSelect
		+"&statistical_classic="+statistical_classic
		+"&statistical_teachingType="+statistical_teachingType;
	var iframe = document.createElement("iframe");
	iframe.id = "frame_printStatistical";
	iframe.src = url;
	iframe.style.display = "none";
	//创建完成之后，添加到body中
	document.body.appendChild(iframe);
}

function statisticalBrschoolClick(){
		var brschoolId = $('#statistical_branchSchool').val(); //教学站
		$.ajax({
			type:"post",
			url:"${baseUrl}/edu3/schoolroll/graduate/audit/list/grade-major-classes/page1.html",
			data:{gradeId_page1:'',majorId_page1:'',brschoolId_page1:brschoolId,id_page1:'statistical_classesSelect',name_page1:'statistical_classesSelect'},
			dataType:"json",
			success:function(data){
				$('#statistical-gradeToMajorToClasses2').html('<label>班级：</label>'+data['msg']);
				$("select[class*=flexselect]").flexselect();
			}
		});
	 }

</script>
</head>
<body>

	<h2 class="contentTitle">毕业信息统计</h2>
	<div class="page">
		<div class="pageHeader">
			<form id="statGraduationCondition"
				onsubmit="return navTabSearch(this);"
				action="${baseUrl }/edu3/roll/graduationStudent/graduationInfoStat.html"
				method="post">
				<div id="graduateAud" class="searchBar">
					<ul class="searchContent">
						<li><label>办学点：</label> <gh:brSchoolAutocomplete
								name="statistical_branchSchool" tabindex="1"
								id="statistical_branchSchool"
								defaultValue="${condition['statistical_branchSchool']}"
								displayType="code" style="width:120px"
								onchange="statisticalBrschoolClick()" /></li>
						<li id="statistical-gradeToMajorToClasses2"><label>班级名称：</label>
							${statistical_classesSelect }</li>
						<li><label>层次：</label>
						<gh:selectModel name="statistical_classic"
								id="statistical_classic" bindValue="resourceid"
								displayValue="classicName"
								value="${condition['statistical_classic']}"
								modelClass="com.hnjk.edu.basedata.model.Classic"
								style="width:120px" /></li>
						<li><label>形式：</label> <gh:select
								name="statistical_teachingType"
								dictionaryCode="CodeTeachingType" id="statistical_teachingType"
								value="${condition['statistical_teachingType']}"
								style="width:120px;" /></li>
					</ul>
					<div class="subBar">
						<ul>
							<li>
								<div class="buttonActive">
									<div class="buttonContent">
										<button type="submit">查 询</button>
									</div>
								</div>
							</li>
						</ul>
					</div>
				</div>
			</form>
		</div>
		<div class="panelBar">
			<ul class="toolBar">
				<li><a title="按查询条件导出毕业统计信息" class="icon" href="#"
					onclick="exportStatistical()"><span>导出统计信息</span></a></li>
			</ul>
		</div>
		<!-- 统计 -->
		<div id="graduationStus3">
			<div class="pageContent">
				<table class="table" layouth="77%">
					<thead>
						<tr>
							<th width="15%">办学地</th>
							<th width="15%">班级名称</th>
							<th width="10%">学制</th>
							<th width="10%">形式</th>
							<th width="10%">层次</th>
							<th width="10%">原班级总人数</th>
							<th width="10%">毕业生数</th>
							<th width="10%">结业生数</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${statistical_page.result}" var="st"
							varStatus="vs">
							<tr>
								<td>${st['unitname'] }</td>
								<td>${st['classesname'] }</td>
								<td>${st['eduyear'] }</td>
								<td>${ghfn:dictCode2Val("CodeLearningStyle",st['teachingtype'] ) }</td>
								<td>${st['classicname'] }</td>
								<td>${st['classCount'] }</td>
								<td>${st['bycount'] }</td>
								<td>${st['jycount'] }</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
				<gh:page page="${statistical_page}"
					goPageUrl="${baseUrl }/edu3/roll/graduationStudent/graduationInfoStat.html"
					condition="${condition }" pageType="sys" />
			</div>
		</div>

	</div>
</body>
</html>