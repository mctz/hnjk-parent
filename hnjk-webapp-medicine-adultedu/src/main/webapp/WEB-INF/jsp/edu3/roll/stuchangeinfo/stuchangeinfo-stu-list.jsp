<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>学籍异动</title>
<script type="text/javascript">
	//新增
	function addInfoChangeApply(){
		var url = "${baseUrl}/edu3/register/stuchangeinfo/edit.html";
		navTab.openTab('_blank', url, '新增学籍异动');
	}
	//删除
	function stuApplyDel(){	
	//	pageBarHandle("您确定要删除这些记录吗？","${baseUrl}/edu3/roll/graduation/student/stuApplyDel.html","#stuChangeBody");
	}
	
	function _viewChangeInfoApply(resourceid,wfid){
		$.pdialog.open('${baseUrl}/edu3/register/stuchangeinfo/edit.html?resourceid='+resourceid+'&wf_id='+wfid, 'RES_STUDENT_APPLY_INFOCHANGE_VIEW', '查看学籍异动申请', {width: 800, height: 600});
		
	}
	
</script>
</head>
<body>

	<div class="page">
		<div class="pageHeader">
			<form onsubmit="return navTabSearch(this);"
				action="${baseUrl }/edu3/register/stuchangeinfo/stuchange-list.html"
				method="post">
				<!--  学生查看不需要条件，考虑加上按申核结果查询
		<div class="searchBar">			
			<ul class="searchContent">				
				<li>
					<label>学籍状态：</label><gh:select name="stuStatus" value="${condition['stuStatus']}" dictionaryCode="CodeStudentStatus" style="width:120px" />
				</li>
				<li>
					<label>异动类型：</label><gh:select name="stuChange" value="${condition['stuChange']}" dictionaryCode="CodeStudentStatusChange" style="width:120px" />
				</li>				
			</ul>
			<div class="subBar">
				<ul>
					<li><div class="buttonActive"><div class="buttonContent"><button type="submit"> 查 询 </button></div></div></li>					
				</ul>
			</div>
		</div>
		-->
			</form>
		</div>
		<div class="pageContent">
			<gh:resAuth parentCode="RES_STUDENT_APPLY_INFOCHANGE" pageType="list"></gh:resAuth>
			<table class="table" layouth="89">
				<thead>
					<tr>
						<th width="5%"><input type="checkbox" name="checkall"
							id="check_all_stuchange"
							onclick="checkboxAll('#check_all_stuchange','resourceid','#stuChangeBody')" /></th>
						<th width="10%">学号</th>
						<th width="9%">姓名</th>
						<th width="6%">民族</th>
						<th width="10%">身份证号</th>
						<th width="10%">教学站</th>
						<th width="9%">年级</th>
						<th width="9%">层次</th>
						<th width="9%">专业</th>
						<th width="10%">申请时间</th>
						<th width="8%">异动类型</th>
						<th width="8%">申请结果</th>
					</tr>
				</thead>
				<tbody id="stuChangeBody">
					<c:forEach items="${stulist.result}" var="stu" varStatus="vs">
						<tr>
							<td><input type="checkbox" name="resourceid"
								value="${stu.resourceid }" autocomplete="off" /></td>
							<td>${stu.studentInfo.studyNo }</td>
							<td><a href="##"
								onclick="_viewChangeInfoApply('${stu.resourceid}','${stu.wf_id}')">${stu.studentInfo.studentName }</a></td>
							<td>${ghfn:dictCode2Val('CodeNation',stu.studentInfo.studentBaseInfo.nation) }</td>
							<td>${stu.studentInfo.studentBaseInfo.certNum }</td>
							<td>${stu.studentInfo.branchSchool }</td>
							<td>${stu.studentInfo.grade }</td>
							<td>${stu.studentInfo.classic }</td>
							<td>${stu.studentInfo.major }</td>
							<td><fmt:formatDate value="${stu.applicationDate }"
									pattern="yyyy-MM-dd HH:mm:ss" /></td>
							<td>${ghfn:dictCode2Val('CodeStudentStatusChange',stu.changeType) }</td>
							<td>${ghfn:dictCode2Val('CodeCheckStatus',stu.finalAuditStatus) }</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>

			<gh:page page="${stulist}"
				goPageUrl="${baseUrl }/edu3/register/stuchangeinfo/stuchange-list.html"
				pageType="sys" condition="${condition}" />
		</div>
	</div>

</body>
</html>