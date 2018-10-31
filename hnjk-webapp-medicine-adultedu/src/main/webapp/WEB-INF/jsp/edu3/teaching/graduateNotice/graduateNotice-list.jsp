<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>毕业导师公告</title>
<script type="text/javascript">
	//新增
	function graduatePN_add(){
		navTab.openTab('RES_TEACHING_THESIS_PapersNotice_VIEW', '${baseUrl}/edu3/teaching/graduateNotice/edit.html', '新增导师公告');
	}
	
	//修改
	function graduatePN_edit(){
		var url = "${baseUrl}/edu3/teaching/graduateNotice/edit.html";
		if(isCheckOnlyone('resourceid','#gpnBody')){
			navTab.openTab('RES_TEACHING_THESIS_PapersNotice_VIEW', url+'?resourceid='+$("#gpnBody input[@name='resourceid']:checked").val(), '编辑导师公告');
		}
	}
		
	//删除
	function graduatePN_del(){
		pageBarHandle("您确定要删除这些记录吗？","${baseUrl}/edu3/teaching/graduateNotice/delete.html","#gpnBody");
	}
	//浏览
	function _graduatePN_view(id){
		var url = "${baseUrl}/edu3/framework/teaching/graduateNotice/view.html";
		$.pdialog.open(url+'?resourceid='+id, 'RES_TEACHING_THESIS_PapersNotice_VIEW', '查看通知', {width: 800, height: 600});　
	}
	
</script>
</head>
<body>

	<div class="page">
		<c:if test="${condition['isStudent'] eq 'N' }">
			<div class="pageHeader">
				<form onsubmit="return navTabSearch(this);"
					action="${baseUrl }/edu3/teaching/graduateNotice/list.html"
					method="post">
					<div class="searchBar">
						<ul class="searchContent">
							<li><label>论文批次：</label>
							<gh:selectModel name="examSub" bindValue="resourceid"
									displayValue="batchName" style="width:125px"
									modelClass="com.hnjk.edu.teaching.model.ExamSub"
									value="${condition['examSub']}" condition="batchType='thesis'" />
							</li>
							<li><label>标题：</label><input type="text" name="title"
								value="${condition['title']}" style="width: 120px" /></li>
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
			<gh:resAuth parentCode="RES_TEACHING_THESIS_PapersNotice"
				pageType="list"></gh:resAuth>
			<table class="table" layouth="138">
				<thead>
					<tr>
						<th width="5%"><input type="checkbox" name="checkall"
							id="check_all_gpn"
							onclick="checkboxAll('#check_all_gpn','resourceid','#gpnBody')" /></th>
						<th width="25%">毕业论文批次</th>
						<th width="35%">标题</th>
						<th width="15%">指导导师</th>
						<th width="15%">发布时间</th>
					</tr>
				</thead>
				<tbody id="gpnBody">
					<c:forEach items="${noticeList.result}" var="n" varStatus="vs">
						<tr>
							<td><input type="checkbox" name="resourceid"
								value="${n.resourceid }" autocomplete="off" /></td>
							<td>${n.examSub.batchName }</td>
							<td><a href="###"
								onclick="_graduatePN_view('${n.resourceid }')">${n.title }</a></td>
							<td>${n.guidTeacherName }</td>
							<td><fmt:formatDate value="${n.pubTime }"
									pattern="yyyy-MM-dd HH:mm:ss" /></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>

			<gh:page page="${noticeList}"
				goPageUrl="${baseUrl }/edu3/teaching/graduateNotice/list.html"
				pageType="sys" condition="${condition}" />
		</div>
	</div>

</body>
</html>