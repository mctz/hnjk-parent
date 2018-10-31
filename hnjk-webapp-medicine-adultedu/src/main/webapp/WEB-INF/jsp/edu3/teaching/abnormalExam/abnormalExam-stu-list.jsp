<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>缓考申请</title>
<script type="text/javascript">
	// 申请
	function abnormalExam_apply(){
		navTab.openTab('RES_STUDENT_ABNORMALEXAMLIST_APPLY', '${baseUrl}/edu3/teaching/abnormalExam/saveOrEdit.html', '申请缓考');
	}

	// 撤销
	function abnormalExam_revoke(){	
        var check = true;
		$("#stuABExamList input[@name='resourceid']:checked").each(function(){
			if($(this).attr("title") > 0){
				check = false;
			}
		});
        
		if(!check){
			alertMsg.warn('学生的申请已审批过，不能撤销！');
			return false;
		}
		
		pageBarHandle("您确定要撤销这些记录吗？","${baseUrl}/edu3/teaching/abnormalExam/revoke1.html","#stuABExamList");
	}
	
	//修改
	function abnormalExam_edit(){
		var url = "${baseUrl}/edu3/teaching/abnormalExam/saveOrEdit.html";
		
		if(isCheckOnlyone('resourceid','#stuABExamList')){
			var obj = $("#stuABExamList input[@name='resourceid']:checked");
			if(obj.attr("title")>0){
				alertMsg.warn('学生的申请已审批过，不能修改！');
				return false;
			}
			navTab.openTab('RES_STUDENT_ABNORMALEXAMLIST_EDIT', url+'?resourceid='+$("#stuABExamList input[name='resourceid']:checked").val(), '编辑免修免考');
		} else {
			alertMsg.warn('只能选择一条记录！');
		}		
	}

</script>
</head>
<body>

	<div class="page">
		<div class="pageHeader">
			<form onsubmit="return navTabSearch(this);"
				action="${baseUrl}/edu3/teaching/abnormalExam/list.html"
				method="post">
				<div class="searchBar"></div>
			</form>
		</div>
		<div class="pageContent">
			<gh:resAuth parentCode="RES_STUDENT_ABNORMALEXAMLIST" pageType="list"></gh:resAuth>
			<table class="table" layouth="84">
				<thead>
					<tr>
						<th width="4%"><input type="checkbox" name="checkall"
							id="check_all_stuabExam"
							onclick="checkboxAll('#check_all_stuabExam','resourceid','#stuABExamList')" /></th>
						<th width="10%">教学站</th>
						<th width="5%">年级</th>
						<th width="10%">专业</th>
						<th width="5%">层次</th>
						<th width="10%">学号</th>
						<th width="6%">姓名</th>
						<th width="9%">课程名称</th>
						<th width="6%">申请类型</th>
						<th width="8%">申请时间</th>
						<th width="5%">申请人</th>
						<th width="8%">审核时间</th>
						<th width="8%">审核人</th>
						<th width="8%">审核状态</th>
					</tr>
				</thead>
				<tbody id="stuABExamList">
					<c:forEach items="${applyList.result}" var="a" varStatus="vs">
						<tr>
							<td><input type="checkbox" name="resourceid"
								value="${a.resourceid }" autocomplete="off"
								title="${a.checkStatus }" /></td>
							<td>${a.studentInfo.branchSchool }</td>
							<td>${a.studentInfo.grade }</td>
							<td>${a.studentInfo.major }</td>
							<td>${a.studentInfo.classic }</td>
							<td>${a.studentInfo.studyNo }</td>
							<td>${a.studentInfo.studentName }</td>
							<td>${a.course.courseName }</td>
							<td>${ghfn:dictCode2Val('CodeAbnormalType',a.abnormalType)}</td>
							<td><fmt:formatDate value="${a.applyDate }"
									pattern="yyyy-MM-dd HH:mm:ss" /></td>
							<td>${a.applyManName }</td>
							<td><fmt:formatDate value="${a.checkDate }"
									pattern="yyyy-MM-dd HH:mm:ss" /></td>
							<td>${a.checkManName }</td>
							<td>${ghfn:dictCode2Val('CodeCheckStatus',a.checkStatus)}</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>

			<gh:page page="${applyList}"
				goPageUrl="${baseUrl}/edu3/teaching/abnormalExam/list.html"
				pageType="sys" condition="${condition}" />
		</div>
	</div>

</body>
</html>