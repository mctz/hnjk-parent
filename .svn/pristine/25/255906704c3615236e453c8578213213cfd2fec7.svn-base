<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>监巡考人员管理</title>
<script type="text/javascript">
	//新增
	function addExamStaff(){
		var url = "${baseUrl}/edu3/teaching/examstaff/input.html";
		navTab.openTab('RES_TEACHING_EXAM_EXAMSTAFF_EDIT', url, '新增监巡考人员');
	}
	//修改
	function modifyExamStaff(){
		var url = "${baseUrl}/edu3/teaching/examstaff/input.html";
		if(isCheckOnlyone('resourceid','#examStaffBody')){
			navTab.openTab('RES_TEACHING_EXAM_EXAMSTAFF_EDIT', url+'?resourceid='+$("#examStaffBody input[@name='resourceid']:checked").val(), '编辑监巡考人员');
		}			
	}		
	//删除
	function removeExamStaff(){	
		pageBarHandle("您确定要删除这些记录吗？","${baseUrl}/edu3/teaching/examstaff/remove.html","#examStaffBody");
	}
</script>
</head>
<body>

	<div class="page">
		<div class="pageHeader">
			<form onsubmit="return navTabSearch(this);"
				action="${baseUrl }/edu3/teaching/examstaff/list.html" method="post">
				<div class="searchBar">
					<ul class="searchContent">
						<li><label>所属单位：</label> <gh:brSchoolAutocomplete
								name="orgUnitId" tabindex="1" id="examStaff_orgUnitId"
								defaultValue="${condition['orgUnitId'] }" scope="all"
								displayType="code"></gh:brSchoolAutocomplete></li>
						<li><label>姓名：</label> <input type="text" name="name"
							value="${condition['name'] }" /></li>
						<li><label>身份证号：</label> <input type="text" name="idcardNum"
							value="${condition['idcardNum'] }" /></li>
					</ul>
					<ul class="searchContent">
						<li><label style="width: 100px;">是否有过监考：</label> <gh:select
								dictionaryCode="yesOrNo" id="examStaff_hasExamstaff"
								name="hasExamstaff" value="${condition['hasExamstaff'] }" /></li>
						<li><label>工作级别：</label> <gh:select
								dictionaryCode="CodeWorkLevel" id="examStaff_workLevel"
								name="workLevel" value="${condition['workLevel'] }" /></li>
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
			<gh:resAuth parentCode="RES_TEACHING_EXAM_EXAMSTAFF" pageType="list"></gh:resAuth>
			<table class="table" layouth="161">
				<thead>
					<tr class="head_bg">
						<th width="5%"><input type="checkbox" name="checkall"
							id="check_all_examStaff"
							onclick="checkboxAll('#check_all_examStaff','resourceid','#examStaffBody')" /></th>
						<th width="9%">姓名</th>
						<th width="8%">联系电话</th>
						<th width="10%">邮件</th>
						<th width="8%">身份证号</th>
						<th width="10%">所属单位</th>
						<th width="8%">性别</th>
						<th width="10%">出生年月</th>
						<th width="8%">学历</th>
						<th width="8%">身体状况</th>
						<th width="8%">是否有过监考</th>
						<th width="8%">工作级别</th>
					</tr>
				</thead>
				<tbody id="examStaffBody">
					<c:forEach items="${examStaffListPage.result}" var="examStaff"
						varStatus="vs">
						<tr>
							<td><input type="checkbox" name="resourceid"
								value="${examStaff.resourceid }" autocomplete="off" /></td>
							<td>${examStaff.name }</td>
							<td>${examStaff.telelphone }</td>
							<td>${examStaff.email }</td>
							<td>${examStaff.idcardNum }</td>
							<td>${examStaff.orgUnitName }</td>
							<td>${ghfn:dictCode2Val('CodeSex',examStaff.gender) }</td>
							<td><fmt:formatDate value="${examStaff.bornDay }"
									pattern="yyyy-MM-dd" /></td>
							<td>${ghfn:dictCode2Val('CodeEducation',examStaff.education) }</td>
							<td>${ghfn:dictCode2Val('CodeHealth',examStaff.health) }</td>
							<td>${ghfn:dictCode2Val('yesOrNO',examStaff.hasExamstaff) }</td>
							<td>${ghfn:dictCode2Val('CodeWorkLevel',examStaff.workLevel) }</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>

			<gh:page page="${examStaffListPage}"
				goPageUrl="${baseUrl }/edu3/teaching/examstaff/list.html"
				pageType="sys" condition="${condition}" />
		</div>
	</div>

</body>
</html>