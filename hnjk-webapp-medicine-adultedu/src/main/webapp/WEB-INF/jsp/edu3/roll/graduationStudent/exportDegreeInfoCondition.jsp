<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>学位表统计导出条件</title>
<script type="text/javascript">
//学位导出
function exportStatDegreeExcel(){
	var branchSchoolInDegreeExport=$("#branchSchoolInDegreeExport").val();
	var gradeInDegreeExport=$("#gradeInDegreeExport").val();
	//var stuStatusInDegreeExport=$("#stuStatusInDegreeExport").val();
	var classicInDegreeExport=$("#classicInDegreeExport").val();
	var majorInDegreeExport=$("#majorInDegreeExport").val();
	var graduateDateInDegreeExport=$("#graduateDateInDegreeExport").val();
	var confirmGraduateDateb=$("#confirmGraduateDateb_id").val();
	var confirmGraduateDatee=$("#confirmGraduateDatee_id").val();;
	var url = "${baseUrl}/edu3/roll/graduationStudent/exportDegreeInfo.html"
		+"?branchSchoolInDegreeExport="+branchSchoolInDegreeExport
		+"&gradeInDegreeExport="+gradeInDegreeExport
		//+"&stuStatusInDegreeExport="+stuStatusInDegreeExport
		+"&majorInDegreeExport="+majorInDegreeExport
		+"&classicInDegreeExport="+classicInDegreeExport
		+"&graduateDateInDegreeExport="+graduateDateInDegreeExport
		+"&confirmGraduateDateb="+confirmGraduateDateb
		+"&confirmGraduateDatee="+confirmGraduateDatee;
	
	$('#frame_exportDegreeInfos').remove();
	var iframe = document.createElement("iframe");
	iframe.id = "frame_exportDegreeInfos";
	iframe.src = url;
	iframe.style.display = "none";
	//创建完成之后，添加到body中
	document.body.appendChild(iframe);
}
</script>
</head>
<body>

	<div class="page">
		<div class="pageHeader">
			<form id="exportDegreeStatExcelForm" method="post"
				action="${baseUrl}/edu3/roll/graduationStudent/exportDegreeInfoCondition.html"
				class="pageForm" onsubmit="return navTabSearch(this);">
				<div id="graduateDegreeExport" class="searchBar">
					<ul class="searchContent">
						<li><label>教学站：</label> <gh:brSchoolAutocomplete tabindex="1"
								name="branchSchoolInDegreeExport"
								id="branchSchoolInDegreeExport"
								defaultValue="${condition['branchSchoolInDegreeExport']}"
								displayType="code" style="width:120px" /></li>
						<li><label>年级：</label> <gh:selectModel
								name="gradeInDegreeExport" id="gradeInDegreeExport"
								bindValue="resourceid" displayValue="gradeName"
								modelClass="com.hnjk.edu.basedata.model.Grade"
								style="width:120px" value="${condition['gradeInDegreeExport']}" /></li>
						<!-- 
					<li>	<label>学位状态：</label>
							<select name="stuStatusInDegreeExport" id="stuStatusInDegreeExport" style="width:125px" > 
							<option value="" <c:if test="${condition['stuStatusInDegreeExport']==''}">selected="selected"</c:if> >请选择</option>
							<option value="0" <c:if test="${condition['stuStatusInDegreeExport']=='W'}">selected="selected"</c:if>  >待审核</option>
							<option value="1" <c:if test="${condition['stuStatusInDegreeExport']=='Y'}">selected="selected"</c:if>  >已获得</option>
							</select></li> -->

					</ul>
					<ul class="searchContent">
						<li><label>层次：</label> <gh:selectModel
								name="classicInDegreeExport" id="classicInDegreeExport"
								bindValue="resourceid" displayValue="classicName"
								value="${condition['classicInDegreeExport']}"
								modelClass="com.hnjk.edu.basedata.model.Classic"
								style="width:120px" /></li>
						<li><label>专业：</label> <gh:selectModel
								id="majorInDegreeExport" name="majorInDegreeExport"
								bindValue="resourceid" displayValue="majorName"
								value="${condition['majorInDegreeExport']}"
								modelClass="com.hnjk.edu.basedata.model.Major"
								style="width:120px" /></li>
					</ul>
					<ul class="searchContent">
						<li><label>毕业日期：</label> <input type="text"
							id="graduateDateInDegreeExport" name="graduateDateInDegreeExport"
							class="Wdate" value="${condition['graduateDateInDegreeExport']}"
							onfocus="WdatePicker({isShowWeek:true})" /></li>
						<li><label>毕业确认时间:</label> <input type="text"
							id="confirmGraduateDateb_id" style="width: 60px;"
							name="confirmGraduateDateb" class="Wdate"
							value="${condition['confirmGraduateDateb']}"
							onfocus="WdatePicker({onpicked:function(){saveGraduateDate();},isShowWeek:true })" />
							到<input type="text" id="confirmGraduateDatee_id"
							style="width: 60px;" name="confirmGraduateDatee" class="Wdate"
							value="${condition['confirmGraduateDatee']}"
							onfocus="WdatePicker({onpicked:function(){saveGraduateDate();},isShowWeek:true })" />

						</li>
					</ul>
					</ul>
				</div>
				<div class="subBar">
					<ul>
						<li><div class="buttonActive">
								<div class="buttonContent">
									<button type="submit">查询</button>
								</div>
							</div></li>
						<li><div class="buttonActive">
								<div class="buttonContent">
									<button type="button" onclick="exportStatDegreeExcel()">导出</button>
								</div>
							</div></li>
					</ul>
				</div>
			</form>
		</div>
		<div class="pageContent">
			<table class="table" layouth="160">
				<thead>
					<tr>
						<th width="10%">毕业日期</th>
						<th width="10%">层次</th>
						<th width="10%">教学站</th>
						<th width="10%">年级</th>
						<th width="10%">专业</th>
						<th width="10%">总计（人）</th>
						<th width="10%">男（人）</th>
						<th width="10%">女（人）</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${dgInfos1.result }" var="dgInfo1">
						<tr>
							<td width="10%">${dgInfo1.graduatedate}</td>
							<td width="10%">${dgInfo1.classicname}</td>
							<td width="10%">${dgInfo1.unitname}</td>
							<td width="10%">${dgInfo1.gradename}</td>
							<td width="10%">${dgInfo1.majorname}</td>
							<td width="10%">${dgInfo1.total}</td>
							<td width="10%">${dgInfo1.man}</td>
							<td width="10%">${dgInfo1.lady}</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<gh:page page="${dgInfos1}"
				goPageUrl="${baseUrl }/edu3/roll/graduationStudent/exportDegreeInfoCondition.html"
				condition="${condition }" pageType="sys" />
		</div>
	</div>
</html>