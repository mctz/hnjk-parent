<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>选择用户</title>
</head>
<body>

	<div class="page">
		<div class="pageHeader">
			<form id="examStaff_sysUser_dialog_form"
				onsubmit="return dialogSearch(this);"
				action="${baseUrl }/edu3/framework/examstaff/sysuser/rel.html"
				method="post">
				<input type="hidden" id="examStaff_sysUser_formid" name="formid"
					value="${condition['formid'] }" /> <input type="hidden"
					id="examStaff_sysUser_sysUserId" name="sysUserId"
					value="${condition['sysUserId'] }" />
				<div class="searchBar">
					<ul class="searchContent">
						<c:if test="${not brschool }">
							<li><label>所属单位：</label> <gh:brSchoolAutocomplete
									name="unitId" tabindex="3" id="examStaff_sysUser_search"
									defaultValue="${condition['unitId'] }" displayType="code"
									scope="all"></gh:brSchoolAutocomplete></li>
						</c:if>
						<li><label>姓名：</label> <input type="text" name="cnName"
							value="${condition['cnName'] }" /></li>
						<li><label>用户名：</label> <input type="text" name="userName"
							value="${condition['userName'] }" /></li>
					</ul>
					<div class="subBar">
						<ul style="float: left;">
							<li><div class="buttonActive">
									<div class="buttonContent">
										<button type="button" onclick="queryExamStaffSysUser()">
											确 定</button>
									</div>
								</div></li>
						</ul>
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
			<table class="table" layouth="142">
				<thead>
					<tr>
						<th width="5%">&nbsp;</th>
						<th width="10%">用户名</th>
						<th width="10%">姓名</th>
						<th width="11%">联系电话</th>
						<th width="11%">邮件</th>
						<th width="11%">身份证号</th>
						<th width="12%">所属单位</th>
						<th width="10%">性别</th>
						<th width="10%">出生年月</th>
						<th width="10%">学历</th>
					</tr>
				</thead>
				<tbody id="examStaff_sysUser_dialog_tbody">
					<c:forEach items="${userlist.result}" var="u" varStatus="vs">
						<tr>
							<td id="td_${u.resourceid }"><input type="radio"
								name="resourceid" value="${u.resourceid }" autocomplete="off"
								<c:if test="${u.resourceid eq condition['sysUserId'] }">checked="checked"</c:if> />
								<input type="hidden" name="cnName" value="${u.cnName }" /> <input
								type="hidden" name="telelphone" value="${u.officeTel }" /> <input
								type="hidden" name="email" value="${u.email }" /> <input
								type="hidden" name="idcardNum" value="${u.certNum }" /> <input
								type="hidden" name="orgUnitId" value="${u.unitId }" /> <input
								type="hidden" name="orgUnitName" value="${u.orgUnit }" /> <input
								type="hidden" name="gender" value="${u.gender }" /> <input
								type="hidden" name="bornDay" value="${u.birthday }" /> <input
								type="hidden" name="education" value="${u.educationalLevel }" />
							</td>
							<td>${u.username }</td>
							<td>${u.cnName }</td>
							<td>${u.officeTel }</td>
							<td>${u.email }</td>
							<td>${u.certNum }</td>
							<td>${u.orgUnit }</td>
							<td>${ghfn:dictCode2Val('CodeSex',u.gender) }</td>
							<td><fmt:formatDate value="${u.birthday }"
									pattern="yyyy-MM-dd" /></td>
							<td>${ghfn:dictCode2Val('CodeEducation',u.educationalLevel) }</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>

			<gh:page page="${userlist}"
				goPageUrl="${baseUrl }/edu3/framework/examstaff/sysuser/rel.html"
				pageType="sys" targetType="dialog" condition="${condition}" />
		</div>
	</div>
	<script type="text/javascript">
	function queryExamStaffSysUser(){
		var sysUser = $("#examStaff_sysUser_dialog_tbody input[@name='resourceid']:checked");
		if(sysUser.size()>0){
			var formid = $("#examStaff_sysUser_formid").val();
			$("#"+formid+" input[name='sysUserId']").val(sysUser.val());
			
			var $td = $("#td_"+sysUser.val());
			$("#"+formid+" input[name='name']").val($td.find("input[name='cnName']").val());
			$("#"+formid+" input[name='telelphone']").val($td.find("input[name='telelphone']").val());
			$("#"+formid+" input[name='email']").val($td.find("input[name='email']").val());
			$("#"+formid+" input[name='idcardNum']").val($td.find("input[name='idcardNum']").val());
			$("#"+formid+" input[name='orgUnitId']").val($td.find("input[name='orgUnitId']").val());
			$("#examStaff_form_orgUnitId_flexselect").val($td.find("input[name='orgUnitName']").val());
			$("#"+formid+" select[name='gender']").val($td.find("input[name='gender']").val());
			$("#"+formid+" input[name='bornDay']").val($td.find("input[name='bornDay']").val());
			$("#"+formid+" select[name='education']").val($td.find("input[name='education']").val());
			
			$.pdialog.closeCurrent(); 
		} else {
			alertMsg.warn("请选择一条记录！");
			return;
		}	
	}
</script>
</body>
</html>