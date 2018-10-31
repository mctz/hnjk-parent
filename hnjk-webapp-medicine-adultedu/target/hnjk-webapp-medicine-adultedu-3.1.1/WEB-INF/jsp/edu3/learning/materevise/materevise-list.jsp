<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>教师复习总结录像</title>
<script type="text/javascript">
	//新增
	function addMateRevise(){
		navTab.openTab('RES_LEARNING_MATE_REVISE', "${baseUrl }/edu3/learning/materevise/list.html?reviseCourseId=${condition['reviseCourseId']}", '教师复习总结录像');
	}	
	//修改
	function modifyMateRevise(){
		var url = "${baseUrl }/edu3/learning/materevise/list.html";
		if(isCheckOnlyone('resourceid','#mateReviseBody')){
			navTab.openTab('RES_LEARNING_MATE_REVISE', url+'?resourceid='+$("#mateReviseBody input[@name='resourceid']:checked").val()+"&reviseCourseId=${condition['reviseCourseId']}", '教师复习总结录像');
		}			
	}		
	//删除
	function removeMateRevise(){	
		pageBarHandle("您确定要删除这些记录吗？","${baseUrl}/edu3/learning/materevise/remove.html","#mateReviseBody");
	}	
</script>
</head>
<body>
	<div class="page">
		<div class="pageContent">
			<gh:resAuth parentCode="RES_METARES_COURSEWARE_MANAGE"
				pageType="reviselist"></gh:resAuth>
			<table class="table" layouth="35%">
				<thead>
					<tr>
						<th width="8%"><input type="checkbox" name="checkall"
							id="check_all_mateRevise"
							onclick="checkboxAll('#check_all_mateRevise','resourceid','#mateReviseBody')" /></th>
						<th width="8%">排序号</th>
						<th width="19%">课程</th>
						<th width="20%">录像名称</th>
						<th width="25%">录像地址</th>
						<th width="10%">录像时间</th>
						<th width="10%">是否发布</th>
					</tr>
				</thead>
				<tbody id="mateReviseBody">
					<c:forEach items="${reviseList.result}" var="revise" varStatus="vs">
						<tr>
							<td><input type="checkbox" name="resourceid"
								value="${revise.resourceid }" autocomplete="off"
								<c:if test="${mateRevise.resourceid eq revise.resourceid }">checked="checked"</c:if> /></td>
							<td>${revise.showOrder }</td>
							<td>${revise.course.courseName }</td>
							<td>${revise.mateName }</td>
							<td>${revise.mateUrl }</td>
							<td><fmt:formatDate value="${revise.captureDate }"
									pattern="yyyy-MM-dd" /></td>
							<td>${ghfn:dictCode2Val('yesOrNo',revise.isPublished) }</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>

			<gh:page page="${reviseList}"
				goPageUrl="${baseUrl }/edu3/learning/materevise/list.html"
				pageType="sys" condition="${condition}" />
		</div>

		<h2 class="contentTitle">${(empty mateRevise.resourceid)?'新增':'编辑' }教师复习总结录像</h2>
		<div class="pageContent">
			<form method="post"
				action="${baseUrl}/edu3/learning/materevise/save.html"
				class="pageForm" onsubmit="return validateCallback(this);">
				<div class="formBar">
					<ul>
						<li><div class="buttonActive">
								<div class="buttonContent">
									<button type="submit">提交</button>
								</div>
							</div></li>
						<li><div class="button">
								<div class="buttonContent">
									<button type="button" class="close"
										onclick="navTab.closeCurrentTab();">取消</button>
								</div>
							</div></li>
					</ul>
				</div>
				<input type="hidden" name="resourceid"
					value="${mateRevise.resourceid }" /> <input type="hidden"
					name="mateType" value="${mateRevise.mateType }" /> <input
					type="hidden" name="channelType" value="${mateRevise.channelType }" />
				<div class="pageFormContent" layoutH="35%">
					<table class="form">
						<tr>
							<td width="20%">所属课程:</td>
							<td colspan="3"><input type="text" style="width: 50%;"
								value="${mateRevise.course.courseName }" readonly="readonly" />
								<input type="hidden" name="reviseCourseId"
								value="${mateRevise.course.resourceid }" /></td>
						</tr>
						<tr>
							<td width="20%">录像名称:</td>
							<td width="30%"><input type="text" name="mateName"
								value="${mateRevise.mateName }" style="width: 50%;"
								class="required" /></td>
							<td width="20%">序号:</td>
							<td width="30%"><input type="text" name="showOrder"
								value="${mateRevise.showOrder }" style="width: 40%;"
								class="required digits" /></td>
						</tr>
						<tr>
							<td>拍摄时间:</td>
							<td><input type="text" name="captureDate"
								value="<fmt:formatDate value='${mateRevise.captureDate }' pattern='yyyy-MM-dd'/>"
								style="width: 50%;"
								onFocus="WdatePicker({isShowWeek:true,dateFmt:'yyyy-MM-dd'})" /></td>
							<td>是否发布:</td>
							<td><gh:select dictionaryCode="yesOrNo" name="isPublished"
									value="${mateRevise.isPublished }" style="width:40%;" /></td>
						</tr>
						<tr>
							<td>录像地址:</td>
							<td colspan="3"><input type="text" style="width: 75%;"
								name="mateUrl" value="${mateRevise.mateUrl }"
								class="required url"></td>
						</tr>
					</table>
				</div>
			</form>
		</div>
	</div>
</body>
</html>