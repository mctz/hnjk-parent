<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>学位审核列表</title>

</head>
<body>

	<div class="page">
		<div class="pageHeader">
			<form onsubmit="return navTabSearch(this);"
				action="${baseUrl }/edu3/schoolroll/graduation/degree/list.html"
				method="post">
				<div class="searchBar">
					<ul class="searchContent">
						<li><label>教学站：</label> <gh:brSchoolAutocomplete
								name="branchSchool" tabindex="1" id="eiinfo_brSchoolName"
								defaultValue="${condition['branchSchool']}" displayType="code"
								style="width:120px" /></li>
						<li><label>专业：</label>
						<gh:selectModel name="major" bindValue="resourceid"
								displayValue="majorName" value="${condition['major']}"
								modelClass="com.hnjk.edu.basedata.model.Major"
								style="width:120px" /></li>
						<li><label>层次：</label>
						<gh:selectModel name="classic" bindValue="resourceid"
								displayValue="classicName" value="${condition['classic']}"
								modelClass="com.hnjk.edu.basedata.model.Classic"
								style="width:120px" /></li>
					</ul>
					<ul class="searchContent">
						<li><label>学籍状态：</label>
						<gh:select name="stuStatus" value="${condition['stuStatus']}"
								dictionaryCode="CodeStudentStatus" style="width:125px"
								disabled="true" /></li>
						<li><label>姓名：</label><input type="text" name="name"
							value="${condition['name']}" style="width: 115px" /></li>
						<li><label>学号：</label><input type="text" name="studyNo"
							value="${condition['studyNo']}" style="width: 115px" /></li>
						<li><label>年级：</label>
						<gh:selectModel name="gradeInDegree" bindValue="resourceid"
								displayValue="gradeName" value="${condition['gradeid']}"
								modelClass="com.hnjk.edu.basedata.model.Grade"
								orderBy="gradeName desc" style="width:120px" /></li>
						<div class="buttonActive" style="float: right">
							<div class="buttonContent">
								<button type="submit">查 询</button>
							</div>
						</div>
					</ul>
					<ul>
						<li><label>毕业时间(导):</label><input type="text"
							name="graduateDateInDegree" id="graduateDateInDegree"
							class="Wdate" value="${condition['graduateDate']}"
							onfocus="WdatePicker({isShowWeek:true})" /> <font color="red">该毕业时间作为导出学位名单的条件</font>
						</li>
					</ul>
				</div>
			</form>
		</div>
		<div class="pageContent">

			<gh:resAuth parentCode="RES_SCHOOL_GRADUATION_DGREE" pageType="list"></gh:resAuth>
			<table class="table" layouth="161">
				<thead>
					<tr>
						<th width="5%"><input type="checkbox" name="checkall"
							id="check_all_degree"
							onclick="checkboxAll('#check_all_degree','resourceid','#degreeBody')" />
						</th>
						<th width="9%">学号</th>
						<th width="5%">姓名</th>
						<th width="3%">性别</th>
						<th width="4%">年级</th>
						<th width="8%">专业</th>
						<th width="5%">培养层次</th>
						<th width="11%">教学站</th>
						<th width="10%">身份证</th>
						<th width="4%">民族</th>
						<th width="9%">入学日期</th>
						<th width="8%">学籍状态</th>
						<th width="4%">学位状态</th>
					</tr>
				</thead>
				<tbody id="degreeBody">
					<c:forEach items="${graduationQualifList.result}" var="stu"
						varStatus="vs">
						<tr>
							<td><input type="checkbox" name="resourceid"
								value="${stu.resourceid }" autocomplete="off" /></td>
							<td>${stu.studyNo}</td>
							<td><a href="#" onclick="viewStuInfo2('${stu.resourceid}')"
								title="点击查看">${stu.studentBaseInfo.name }</a></td>
							<td>${ghfn:dictCode2Val('CodeSex',stu.studentBaseInfo.gender) }</td>
							<td>${stu.grade.gradeName}</td>
							<td>${stu.major.majorName }</td>
							<td>${stu.classic.classicName }</td>
							<td>${stu.branchSchool}</td>
							<td>${stu.studentBaseInfo.certNum }</td>
							<td>${ghfn:dictCode2Val('CodeNation',stu.studentBaseInfo.nation) }</td>
							<td></td>
							<td>${ghfn:dictCode2Val('CodeStudentStatus',stu.studentStatus) }</td>
							<td><c:if test="${stu.degreeStatus=='Y'}">通过</c:if> <c:if
									test="${stu.degreeStatus=='N'}">不通过</c:if> <c:if
									test="${stu.degreeStatus=='W'}">待审核</c:if></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<gh:page page="${graduationQualifList}"
				goPageUrl="${baseUrl }/edu3/schoolroll/graduation/degree/list.html"
				pageType="sys" condition="${condition}" />
		</div>
	</div>

</body>
<script type="text/javascript">
	function exportDegreeList(){
		$('#frame_exportStudentChange').remove();
		var iframe = document.createElement("iframe");
		iframe.id = "frame_exportStudentChange";
		var graduateDateInDegree = $('#graduateDateInDegree').val();
		iframe.src = "${baseUrl }/edu3/teaching/graduateaudit/export.html?act=default&graduateDateInDegree="
				+graduateDateInDegree;
		iframe.style.display = "none";
		document.body.appendChild(iframe);
	}
	
	function auditdegree(){
	var ids = new Array();
	jQuery("#degreeBody input[name='resourceid']:checked").each(function(){
		ids.push("'"+jQuery(this).val()+"'");
	});
	//if(ids.length==0||ids.length>1){alertMsg.warn("请选择一条记录！");return false};
	if(ids.length==0){alertMsg.warn("请至少选择一条记录！");return false};
	var url = "${baseUrl}/edu3/teaching/graduateaudit/auditdegree.html?resourceid="+ids.toString();
	$.pdialog.open(url, 'RES_SCHOOL_GRADUATION_DGREE1', '学位审核管理', {width: 800, height: 600});
	}


	function viewStuInfo2(id){
		var url = "${baseUrl}/edu3/framework/studentinfo/view.html";
		//navTab.openTab('_blank', url+'?resourceid='+id, '修改学籍');
		$.pdialog.open(url+'?act=view&resourceid='+id, 'RES_SCHOOL_SCHOOLROLL_MANAGER_VIEW', '查看学籍', {width: 800, height: 600});　
	}
	
	function modifyStuInfo(){
		var url = "${baseUrl}/edu3/register/studentinfo/editstu.html";
		if(isCheckOnlyone('resourceid','#infoBody')){
			navTab.openTab('RES_SCHOOL_SCHOOLROLL_MANAGER_EDIT', url+'?resourceid='+$("#infoBody input[@name='resourceid']:checked").val(), '修改学籍');
		}			
	}


</script>
</html>