<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>免考申请</title>
<script type="text/javascript">
	//新增
	function ExamApply_add(){
		navTab.openTab('RES_STUDENT_APPLY_NOEXAM_EDIT', '${baseUrl}/edu3/teaching/noexamapply/edit.html', '申请免修免考');
	}

	//删除
	function ExamApply_del(){	
		var obj = $("#neaBody777 input[@name='resourceid']:checked");

		if(obj.attr("title") == '1'){
			alertMsg.warn('学生的申请已审核通过，不能删除！');
			return false;
		}
		
		pageBarHandle("您确定要删除这些记录吗？","${baseUrl}/edu3/teaching/noexamapply/delete.html","#neaBody777");
	}
	
	//修改
	function ExamApply_edit(){
		var url = "${baseUrl}/edu3/teaching/noexamapply/edit.html";
		var obj = $("#neaBody777 input[@name='resourceid']:checked");

		if(obj.attr("title")>0){
			alertMsg.warn('学生的申请已审核通过，不能修改！');
			return false;
		}
		
		if(isCheckOnlyone('resourceid','#neaBody777')){
			navTab.openTab('RES_STUDENT_APPLY_NOEXAM_EDIT', url+'?resourceid='+$("#neaBody777 input[name='resourceid']:checked").val(), '编辑免修免考');
		}		
	}

</script>
</head>
<body>

	<div class="page">
		<div class="pageHeader">
			<form onsubmit="return navTabSearch(this);"
				action="${baseUrl}/edu3/teaching/noexamapply/list.html"
				method="post">
				<div class="searchBar"></div>
			</form>
		</div>
		<div class="pageContent">
			<gh:resAuth parentCode="RES_STUDENT_APPLY_NOEXAMAPPLY"
				pageType="list"></gh:resAuth>
			<table class="table" layouth="84">
				<thead>
					<tr>
						<th width="5%"><input type="checkbox" name="checkall"
							id="check_all_exa"
							onclick="checkboxAll('#check_all_exa','resourceid','#neaBody777')" /></th>
						<th width="10%">教学站</th>
						<th width="10%">年级</th>
						<th width="10%">专业</th>
						<th width="10%">层次</th>
						<th width="10%">学号</th>
						<th width="10%">姓名</th>
						<th width="10%">课程名称</th>
						<th width="9%">免修类型</th>
						<th width="8%">申请时间</th>
						<th width="8%">审核状态</th>
					</tr>
				</thead>
				<tbody id="neaBody777">
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
							<td>${ghfn:dictCode2Val('CodeUnScoreStyle',a.unScore)}</td>
							<td><fmt:formatDate value="${a.subjectTime }"
									pattern="yyyy-MM-dd HH:mm:ss" /></td>
							<td>${ghfn:dictCode2Val('CodeCheckStatus',a.checkStatus)}</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>

			<gh:page page="${applyList}"
				goPageUrl="${baseUrl}/edu3/teaching/noexamapply/list.html"
				pageType="sys" condition="${condition}" />
		</div>
	</div>

</body>
</html>