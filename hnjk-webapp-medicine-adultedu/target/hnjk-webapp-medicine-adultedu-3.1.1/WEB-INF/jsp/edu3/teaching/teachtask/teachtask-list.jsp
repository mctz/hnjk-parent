<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>教学任务书</title>
</head>
<body>

	<div class="page">
		<div class="pageHeader">
			<form onsubmit="return navTabSearch(this);"
				action="${baseUrl }/edu3/teaching/teachtask/list.html" method="post">
				<div class="searchBar">
					<ul class="searchContent">
						<li><label>年度：</label>
						<gh:selectModel name="yearInfoid" bindValue="resourceid"
								displayValue="yearName"
								modelClass="com.hnjk.edu.basedata.model.YearInfo"
								value="${condition['yearInfoid']}" orderBy="firstYear desc" />
						</li>
						<li><label>学期：</label> <gh:select name="term"
								value="${condition['term']}" dictionaryCode="CodeTerm" /></li>
						<li><label>课程：</label><input type="text" name='courseName'
							value="${condition['courseName']}" /></li>

					</ul>
					<div class="subBar">
						<ul>
							<li><span class="tips">提示：更多查询条件请点击高级查询 &nbsp;&nbsp;</span></li>
							<li><div class="buttonActive">
									<div class="buttonContent">
										<button type="submit">查 询</button>
									</div>
								</div></li>
							<li><a class="button"
								href="${baseUrl }/edu3/teaching/teachtask/list.html?con=advance"
								target="dialog" rel="RES_TEACHING_ESTAB_TEACHTASK"
								title="教学任务书查询"><span>高级查询</span></a></li>
						</ul>
					</div>
				</div>
			</form>
		</div>
		<div class="pageContent">
			<gh:resAuth parentCode="RES_TEACHING_ESTAB_TEACHTASK" pageType="list"></gh:resAuth>
			<table class="table" layouth="138">
				<thead>
					<tr>
						<th width="5%"><input type="checkbox" name="checkall"
							id="check_all_tk"
							onclick="checkboxAll('#check_all_tk','resourceid','#taskBody')" /></th>
						<th width="10%">年度</th>
						<th width="5%">学期</th>
						<th width="5%">课程编号</th>
						<th width="15%">课程名称</th>
						<th width="10%">主讲老师</th>
						<th width="10%">返回时限</th>
						<th width="10%">实际返回时间</th>
						<th width="10%">任务书状态</th>
						<th width="10%">备注</th>
						<th width="10%">&nbsp;</th>
					</tr>
				</thead>
				<tbody id="taskBody">
					<c:forEach items="${taskList.result}" var="t" varStatus="vs">
						<tr>
							<td><input type="checkbox" name="resourceid"
								value="${t.resourceid }" autocomplete="off"
								rel="${t.taskStatus }" /></td>
							<td>${t.yearInfo }</td>
							<td>${ghfn:dictCode2Val('CodeTerm',t.term) }</td>
							<td>${t.course.courseCode }</td>
							<td>${t.course.courseName }</td>
							<td>${t.teacherName }</td>
							<td><fmt:formatDate value='${t.returnTime}'
									pattern='yyyy-MM-dd' /></td>
							<td><fmt:formatDate value='${t.realReturnTime}'
									pattern='yyyy-MM-dd' /></td>
							<td>${ghfn:dictCode2Val('CodeTaskStatus',t.taskStatus) }</td>
							<td>${t.memo }</td>
							<td><a height="600" width="800" target="dialog"
								rel="_teachTaskView"
								href="${baseUrl }/edu3/teaching/teachtask/view.html?resourceid=${t.resourceid }">查看</a></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>

			<gh:page page="${taskList}"
				goPageUrl="${baseUrl }/edu3/teaching/teachtask/list.html"
				pageType="sys" condition="${condition}" />
		</div>
	</div>
</body>
<script type="text/javascript">
	function addTTask(){
		navTab.openTab('navTab', '${baseUrl}/edu3/teaching/teachtask/edit.html', '新增教学任务书');
	}
	
	function modifyTTask(){
		var url = "${baseUrl}/edu3/teaching/teachtask/edit.html";
		if(isCheckOnlyone('resourceid','#taskBody')){
			var taskStatus = $("#taskBody input[@name='resourceid']:checked").attr("rel");
			if(taskStatus!='0'){
				alertMsg.warn("任务书已经审核发布或已发送给老师！");
			} else {
				navTab.openTab('_blank', url+'?resourceid='+$("#taskBody input[@name='resourceid']:checked").val(), '分配任务');
			}				
		}
	}
	
	function listTeachTaskDetails(){
		var url = "${baseUrl}/edu3/teaching/teachtask/edit.html";
		if(isCheckOnlyone('resourceid','#taskBody')){
			var taskStatus = $("#taskBody input[@name='resourceid']:checked").attr("rel");
			if(taskStatus!='3'){
				alertMsg.warn("任务书未审核发布，不能操作！");
			} else {
				navTab.openTab('_blank', url+'?type=details&resourceid='+$("#taskBody input[@name='resourceid']:checked").val(), '查看与评价');
			}
			
		}
	}
	
	function delTTask(){
		var isSend = true;
		$("#taskBody input[@name='resourceid']:checked").each(function (){
			var obj = $(this);
			if(obj.attr('rel')!='0'){
				isSend = false;
			}
		});
		if(!isSend){
			alertMsg.warn("已审核发布或已完成的任务书不能删除，请重新选择！");
		} else {
			pageBarHandle("您确定要删除这些记录吗？","${baseUrl}/edu3/teaching/teachtask/delete.html","#taskBody");
		}		
	}	
	
	function sendTask(type){
		if(type==2){//退回修改
			var isSend = true;
			$("#taskBody input[@name='resourceid']:checked").each(function (){
				var obj = $(this);
				if(obj.attr('rel')!='2'){
					isSend = false;
				}
			});
			if(!isSend){
				alertMsg.warn("任务书已发布或未完成或已审核，请重新选择!");
				return false;
			}
		}
		pageBarHandle("您确定要发送这些任务书吗？","${baseUrl}/edu3/teaching/teachtask/send.html","#taskBody");
	}
	
	function auditTeachTask(){
		var isSend = true;
		$("#taskBody input[@name='resourceid']:checked").each(function (){
			var obj = $(this);
			if(obj.attr('rel')!='2'){
				isSend = false;
			}
		});
		if(!isSend){
			alertMsg.warn("任务书已发布或未完成或已审核，请重新选择!");
		} else {
			pageBarHandle("您确定要发布这些任务书吗？","${baseUrl}/edu3/teaching/teachtask/audit.html","#taskBody");
		}			
	}
</script>
</html>