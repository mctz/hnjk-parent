<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>交流区设置</title>
<script type="text/javascript">
	//下一个环节
	function Gmsg_add(){
		//navTab.openTab('navTab', '${baseUrl}/edu3/teaching/graduateMsg/edit.html', '新增交流区');
		pageBarHandle("您确定要进入下一个环节？","${baseUrl}/edu3/teaching/graduateMsg/nextTache.html","#msgBody");
	}
	
	//浏览学生信息
	function _viewStuBaseInfo(id,name){
		var url = "${baseUrl}/edu3/framework/studentinfo/base/view.html";
		$.pdialog.open(url+'?resourceid='+id, 'RES_SCHOOL_SCHOOLROLL_MANAGER_VIEW', name+'的信息', {width: 600, height: 400});　
	}
	
	//修改
	function Gmsg_edit(){
		alertMsg.warn("该功能已禁用！");
		//var url = "${baseUrl}/edu3/teaching/graduateMsg/edit.html";
		///if(isCheckOnlyone('resourceid','#msgBody')){
		//	navTab.openTab('RES_TEACHING_THESIS_MSG_VIEW', url+'?resourceid='+$("#msgBody input[@name='resourceid']:checked").val(), '编辑交流区');
		//}			
	}
		
	//删除
	function Gmsg_del(){
		alertMsg.warn("该功能已禁用！");
		//pageBarHandle("您确定要删除这些记录吗？","${baseUrl}/edu3/teaching/graduateMsg/delete.html","#msgBody");
	}
	
	function Gmsg_sendmsg(){
		if(!isChecked("resourceid","#msgBody")){
			alertMsg.warn('请选择一条要操作记录！');
		return false;
	 	}
		alertMsg.confirm("您确定要给这些学生发消息?", {
				okCall: function(){		
					var res = "";
					var name = "";
					var num  = $("#msgBody input[name='resourceid']:checked").size();
					$("#msgBody input[name='resourceid']:checked").each(function(){
	                        res+=$(this).attr('userid')+",";
	                        name+=$(this).attr('rel')+",";
	                 });
	                
					$.pdialog.open('${baseUrl }/edu3/framework/message/dailog.html?type=batch&userNames='+res+'&cnNames='+encodeURIComponent(name),'selector','发送消息',{mask:true,width:750,height:550});
				}
		});		
	}	
	
	//浏览
	function _viewGruateMsg(msgid){
		var url ="${baseUrl}/edu3/teaching/graduateMsg/edit.html?resourceid="+msgid;
		navTab.openTab('RES_TEACHING_THESIS_MSG_VIEW',url,'毕业论文交流区');
	}
</script>
</head>
<body>

	<div class="page">
		<c:if test="${condition['isStudent'] eq 'N'}">
			<div class="pageHeader">
				<form onsubmit="return navTabSearch(this);"
					action="${baseUrl }/edu3/teaching/graduateMsg/list.html"
					method="post">
					<div class="searchBar">
						<ul class="searchContent">
							<li><label>预约批次：</label> <select name="batchId" size="1"
								style="width: 55%;">
									<option value="">请选择...</option>
									<c:forEach items="${ examSubList}" var="examSub">
										<option value="${examSub.resourceid }"
											<c:if test="${examSub.resourceid eq  condition['batchId']}">selected</c:if>>${examSub.batchName }</option>
									</c:forEach>
							</select></li>
							<li><label>学生姓名：</label><input type="text" name="stuName"
								value="${condition['classicName']}" /></li>
							<li><label>学生学号：</label><input type="text" name="stuStudyNo"
								value="${condition['stuStudyNo']}" /></li>

							<li><security:authorize
									ifNotGranted="ROLE_TEACHER,ROLE_TEACHER_DUTY,ROLE_TEACHER_COACH,ROLE_STUDENT">
									<span class="tips">提示：只能老师和学生才能使用该功能</span>
								</security:authorize></li>
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
		</c:if>

		<div class="pageContent"
			style="border-left: 1px #B8D0D6 solid; border-right: 1px #B8D0D6 solid">
			<gh:resAuth parentCode="RES_TEACHING_THESIS_MSG" pageType="list"></gh:resAuth>
			<table class="table" layouth="138">
				<thead>
					<tr>
						<th width="5%"><input type="checkbox" name="checkall"
							id="check_all_msg"
							onclick="checkboxAll('#check_all_msg','resourceid','#msgBody')" /></th>
						<th width="10%">论文批次</th>
						<c:choose>
							<c:when test="${condition['isStudent'] eq 'Y' }">
								<th width="10%">老师姓名</th>
								<th width="10%">邮箱</th>
								<th width="10%">电话</th>
							</c:when>
							<c:otherwise>
								<th width="8%">学生姓名</th>
								<th width="10%">学生学号</th>
								<th width="10%">教学站</th>
								<th width="8%">学生联系电话</th>
								<th width="10%">学生邮箱</th>
							</c:otherwise>
						</c:choose>

						<th width="10%">专业</th>
						<th width="6%">论文环节</th>
						<th width="8%">截止时间</th>
						<th width="8%">最新留言时间</th>
						<th width="8%">最后留言人</th>
					</tr>
				</thead>
				<tbody id="msgBody">
					<c:forEach items="${msgList.result}" var="msg" varStatus="vs">
						<tr>
							<td><input type="checkbox" name="resourceid"
								value="${msg.resourceid }"
								userid="${msg.studentInfo.sysUser.username}"
								rel="${msg.studentInfo.studentName }" autocomplete="off" /></td>
							<td>${msg.examSub.batchName }</td>
							<c:choose>
								<c:when test="${condition['isStudent'] eq 'Y' }">
									<td>${msg.teacher.cnName }</td>
									<td>${msg.teacher.email }</td>
									<td>${msg.teacher.mobile }</td>
								</c:when>
								<c:otherwise>
									<td><a href="##" title="查看学生信息"
										onclick="_viewStuBaseInfo('${msg.studentInfo.resourceid}','${msg.studentInfo.studentName }')">${msg.studentInfo.studentName }</a></td>
									<td>${ msg.studentInfo.studyNo}</td>
									<td>${ msg.studentInfo.branchSchool.unitName}</td>
									<td>${ msg.studentInfo.studentBaseInfo.mobile}</td>
									<td>${ msg.studentInfo.studentBaseInfo.email}</td>
								</c:otherwise>
							</c:choose>
							<td>${msg.studentInfo.major }</td>
							<td><a href="##" title="点击进入交流区"
								onclick="_viewGruateMsg('${msg.resourceid}')">${ghfn:dictCode2Val('CodeCurrentTache',msg.currentTache ) }</a></td>
							<td><c:if test="${msg.currentTache eq '1' }">
									<fmt:formatDate
										value="${msg.examSub.gradendDate.syllabusEndDate }"
										pattern="yyyy-MM-dd" />
								</c:if> <c:if test="${msg.currentTache eq '2' }">
									<fmt:formatDate
										value="${msg.examSub.gradendDate.firstDraftEndDate }"
										pattern="yyyy-MM-dd" />
								</c:if> <c:if test="${msg.currentTache eq '3' }">
									<fmt:formatDate
										value="${msg.examSub.gradendDate.secondDraftEndDate }"
										pattern="yyyy-MM-dd" />
								</c:if></td>
							<c:set var="array" value="${fn:split(msg.lastUpdateInfo, ',')}" />
							<td>${array[0] }</td>
							<td>${array[1]}</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>

			<gh:page page="${msgList}"
				goPageUrl="${baseUrl }/edu3/teaching/graduateMsg/list.html"
				pageType="sys" condition="${condition}" />
		</div>
	</div>

</body>
</html>