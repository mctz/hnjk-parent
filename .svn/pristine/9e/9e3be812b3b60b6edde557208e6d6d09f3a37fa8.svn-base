<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>毕业资格列表</title>

</head>
<body>

	<div class="page">
		<div class="pageHeader">
			<form onsubmit="return navTabSearch(this);"
				action="${baseUrl }/edu3/schoolroll/graduation/qualif/list.html"
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
						<li><label>年级：</label>
						<gh:selectModel name="grade" bindValue="resourceid"
								displayValue="gradeName" value="${condition['gradeid']}"
								modelClass="com.hnjk.edu.basedata.model.Grade"
								orderBy="gradeName desc" style="width:120px" /></li>
					</ul>
					<ul class="searchContent">
						<li><label>学籍状态：</label>
						<gh:select name="stuStatus" value="${condition['stuStatus']}"
								dictionaryCode="CodeStudentStatus" style="width:125px"
								filtrationStr="11,21,25" /></li>
						<li><label>姓名：</label><input type="text" name="name"
							value="${condition['name']}" style="width: 115px" /></li>
						<li><label>学号：</label><input type="text" name="studyNo"
							value="${condition['studyNo']}" style="width: 115px" /></li>
						<li><label>身份证号：</label><input type="text" name="certNum"
							value="${condition['certNum']}" style="width: 115px" /></li>
						<div class="buttonActive" style="float: right">
							<div class="buttonContent">
								<button type="submit">查 询</button>
							</div>
						</div>
					</ul>
				</div>
			</form>
		</div>
		<div class="pageContent">

			<gh:resAuth parentCode="RES_SCHOOL_GRADUATION_QUALIF" pageType="list"></gh:resAuth>
			<table class="table" layouth="138">
				<thead>
					<tr>
						<th width="5%"><input type="checkbox" name="checkall"
							id="check_all_qualif"
							onclick="checkboxAll('#check_all_qualif','resourceid','#qualifBody')" /></th>
						<th width="9%">学号</th>
						<th width="5%">姓名</th>
						<th width="4%">性别</th>
						<th width="8%">年级</th>
						<th width="8%">专业</th>
						<th width="8%">培养层次</th>
						<th width="11%">教学站</th>


						<th width="10%">身份证</th>
						<th width="8%">民族</th>
						<th width="10%">入学日期</th>


						<th width="8%">学籍状态</th>
						<!-- 
			        <th width="6%">学习方式</th>
			        <th width="6%">账号状态</th>
			        -->
					</tr>
				</thead>
				<tbody id="qualifBody">
					<c:forEach items="${graduationQualifList.result}" var="stu"
						varStatus="vs">
						<tr>
							<td><input type="checkbox" name="resourceid"
								value="${stu.resourceid }" autocomplete="off" /></td>
							<td>${stu.studyNo}</td>
							<td><a href="#"
								onclick="viewQualifStuInfo('${stu.resourceid}')" title="点击查看">${stu.studentBaseInfo.name }</a></td>
							<td>${ghfn:dictCode2Val('CodeSex',stu.studentBaseInfo.gender) }</td>
							<td>${stu.grade.gradeName}</td>
							<td>${stu.major.majorName }</td>
							<td>${stu.classic.classicName }</td>
							<td>${stu.branchSchool}</td>
							<td>${stu.studentBaseInfo.certNum }</td>
							<td>${ghfn:dictCode2Val('CodeNation',stu.studentBaseInfo.nation) }</td>
							<td></td>
							<td>${ghfn:dictCode2Val('CodeStudentStatus',stu.studentStatus) }</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<gh:page page="${graduationQualifList}"
				goPageUrl="${baseUrl }/edu3/schoolroll/graduation/qualif/list.html"
				pageType="sys" condition="${condition}" />
		</div>
	</div>

</body>
<script type="text/javascript">
	function viewQualifStuInfo(id){
		var url = "${baseUrl}/edu3/framework/studentinfo/view.html";	
		$.pdialog.open(url+'?act=view&resourceid='+id, 'RES_SCHOOL_SCHOOLROLL_MANAGER_QUALIF_VIEW', '查看学籍', {width: 800, height: 600});
	}
	function refreshGraduationQualified(){
		alertMsg.confirm("您确定要执行刷新毕业资格筛选数据么？如执行此操作，将更新待毕业学生的必修课成绩和限选课修读门数，此过程将执行一段时间。", {
			okCall: function(){//执行			
				refresh();
			}
		});
	}
	function refresh(){
		var postUrl="${baseUrl}/edu3/schoolroll/graduate/qualifier/refreshAuditData.html";	
		$.ajax({
			type:"post",
			url:postUrl,
			dataType:"json",
			success:function(data){
				alertMsg.correct(data['message']);
				if(data['hasError']==true){
				}else{
					navTab.openTab('RES_SCHOOL_GRADUATION_QUALIF', "${baseUrl}/edu3/schoolroll/graduation/qualif/list.html", '毕业资格筛选');
				}
			}
		});
	}

</script>
</html>