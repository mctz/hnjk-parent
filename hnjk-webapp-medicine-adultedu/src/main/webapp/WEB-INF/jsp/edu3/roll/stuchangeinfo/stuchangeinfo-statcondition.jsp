<%@page contentType="text/html;charset=UTF-8"%>
<%@include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>学生学籍异动条数统计</title>
<script type="text/javascript">	

function exportStuChangeStatExcel(){
	//以免每次点击下载都创建一个iFrame，把上次创建的删除
	$('#frame_exportStudentChangeStat').remove();
	var iframe = document.createElement("iframe");
	iframe.id = "frame_exportStudentChangeStat";
	var statClass = $("#statClass option:selected").val();
	var auditDate = $("#auditDay").val();
	//如果没有选择统计方式
	if(""==statClass) {
		alert('请您选择统计的方式。');
		iframe.style.display = "none";
		return false;
	}
	//如果选择了按名字统计的方式
	if(6==statClass){
		var students="";
		$("input[name='stuUnitCheckbox']").each(function(){
			if($(this).attr("checked")){
				students += "'"+$(this).attr("value")+"',";
			}
		});
		if(""!=students){	
			students = students.substring(0,students.length-1);
			iframe.src = "${baseUrl }/edu3/register/stuchangeinfo/excel/stuChangeStatExport.html?act=default&statClass="
				+statClass+"&auditDate="+auditDate+"&students="+students;
		}else{
			alert("请选择需要导出的学生的姓名");
		}
	}else{//其他统计方式
		iframe.src = "${baseUrl }/edu3/register/stuchangeinfo/excel/stuChangeStatExport.html?act=default&statClass="
			+statClass+"&auditDate="+auditDate;
	}
	iframe.style.display = "none";
	//创建完成之后，添加到body中
	document.body.appendChild(iframe);
} 
function selectorChange(){
	var statClass = $("#statClass option:selected").val();
	var auditDate = $("#auditDay").val();
	if(statClass=="6"){
		$("#chooseStus1").show();
		$("#chooseStus2").show();
	}else{
		$("#chooseStus1").hide();
		$("#chooseStus2").hide();
	}
	$("#statTypeRec").val(statClass);
	$("#auditDateRec").val(auditDate);
}
	
function checkAllUnits(obj){
	$("input[name='stuUnitCheckbox']").each(function(){
		$(this).attr("checked",$(obj).attr("checked"));
	});
}
</script>
</head>
<body>

	<h2 class="contentTitle">学生学籍异动条数统计</h2>
	<div class="page">
		<div class="pageHeader">
			<table id="changeTab">
				<tr>
					<td>统计方式类别:</td>
					<td><select id="statClass" onchange="selectorChange()">
							<option
								<c:if test="${condition['statType']=='' }">selected="selected"</c:if>
								value="">--请选择--</option>
							<option
								<c:if test="${condition['statType']=='0' }">selected="selected"</c:if>
								value="0">按学习形式统计</option>
							<option
								<c:if test="${condition['statType']=='1' }">selected="selected"</c:if>
								value="1">按专业统计</option>
							<option
								<c:if test="${condition['statType']=='2' }">selected="selected"</c:if>
								value="2">按年级统计</option>
							<option
								<c:if test="${condition['statType']=='3' }">selected="selected"</c:if>
								value="3">按教学站统计</option>
							<option
								<c:if test="${condition['statType']=='4' }">selected="selected"</c:if>
								value="4">按层次统计</option>
							<option
								<c:if test="${condition['statType']=='5' }">selected="selected"</c:if>
								value="5">按学号统计</option>
							<option
								<c:if test="${condition['statType']=='6' }">selected="selected"</c:if>
								value="6">按姓名统计</option>
							<option
								<c:if test="${condition['statType']=='7' }">selected="selected"</c:if>
								value="7">按异动类型统计</option>
					</select></td>
				</tr>
				<tr>
					<td>审核时间:</td>
					<td><input type="text" name="auditDay" id="auditDay"
						class="Wdate" value="${condition['auditDate'] }"
						onfocus="WdatePicker({isShowWeek:true})"
						onchange="selectorChange()" /></td>
				</tr>
				<tr>
					<td colspan="2">
						<div class="buttonActive" style="float: left">
							<div class="buttonContent">
								<button type="button" onclick="exportStuChangeStatExcel()">确定导出</button>
							</div>
						</div>
					</td>
				</tr>
			</table>

			<div id="chooseStus1"
				<c:if test="${condition['isSubmitted']!=1 }">style="display:none;"</c:if>>
				<form id="chooseStusCondition" onsubmit="return navTabSearch(this);"
					action="${baseUrl }/edu3/register/stuchangeinfo/stuchange-chooseStus.html"
					method="post">
					<input type="hidden" id="statTypeRec" name="statTypeRec"
						value="${condition['statType']}" /> <input type="hidden"
						id="auditDateRec" name="auditDateRec"
						value="${condition['auditDate']}" />
					<table id="selectCondition">
						<tr>
							<td><label>学生姓名：</label></td>
							<td><input id="sName" name="sName"
								value="${condition['sName']}" style="width: 115px"></td>
						</tr>
						<tr>
							<td><label>教学站：</label></td>
							<td><gh:brSchoolAutocomplete name="brSchool" tabindex="1"
									id="brSchool" defaultValue="${condition['brSchool']}"
									style="width:120px" /></td>
						</tr>
						<tr>
							<td colspan="2">
								<div class="buttonActive" style="float: left">
									<div class="buttonContent">
										<button type="submit">查 询</button>
									</div>
								</div>
							</td>
						</tr>
					</table>
				</form>
			</div>
		</div>
		<div id="chooseStus2"
			<c:if test="${condition['isSubmitted']!=1 }">style="display:none;"</c:if>>
			<div class="pageContent">
				<table id="stusShows" class="table" layouth="250">
					<thead>
						<tr>
							<th width="5%"><input type="checkbox"
								id="stuUnitsCheckboxAll" onclick="checkAllUnits(this)" /></th>
							<th width="20%">学生姓名</th>
							<th width="30%">所属教学站</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${stus.result }" var="stu">
							<tr>
								<td><input type="checkbox" name="stuUnitCheckbox"
									value="${stu.studentInfo.resourceid }"
									rel="${stu.studentInfo.studentBaseInfo.name}" /></td>
								<td width="20%">${stu.studentInfo.studentBaseInfo.name}</td>
								<td width="30%">${stu.studentInfo.branchSchool.unitName}</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
				<gh:page page="${stus}"
					goPageUrl="${baseUrl }/edu3/register/stuchangeinfo/stuchange-chooseStus.html"
					pageType="sys" condition="${condition}" />
			</div>
		</div>
	</div>
</body>
</html>