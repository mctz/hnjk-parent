<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>学生反馈管理</title>
<script type="text/javascript">
	function replyFeedback(){
		if(isCheckOnlyone('resourceid','#refeedbackBody')){
			navTab.openTab('RES_TEACHING_ESTAB_FEEDBACK_ADD', "${baseUrl}/edu3/teacher/feedback/input.html?resourceid="+$("#refeedbackBody input[@name='resourceid']:checked").val(), '回复学生反馈');
		}		
	}
	
	function viewStuInfoFeedback(id){
		if(id!=""){
			var url = "${baseUrl}/edu3/framework/studentinfo/view.html";
			$.pdialog.open(url+'?resourceid='+id, 'RES_SCHOOL_SCHOOLROLL_MANAGER_VIEW', '查看学籍', {width: 800, height: 600});
		}		
	}
	//导出
	function exportFeedback(){
		var url = "${baseUrl}/edu3/teacher/feedback/export.html?"+$("#teacherReplyFeedbackForm").serialize();
		downloadFileByIframe(url,"teacherReplyFeedbackIframe");
	}
</script>
</head>
<body>
	<div class="page">
		<div class="pageHeader">
			<form onsubmit="return navTabSearch(this);"
				id="teacherReplyFeedbackForm"
				action="${baseUrl }/edu3/teacher/feedback/list.html" method="post">
				<div class="searchBar">
					<ul class="searchContent">
						<li><label>反馈类型：</label> <gh:select
								dictionaryCode="CodeFacebookType"
								value="${condition['facebookType'] }" id="teacher_facebookType"
								name="facebookType" /></li>
						<li><label>反馈人：</label> <input type="text" name="fillinMan"
							value="${condition['fillinMan'] }" /></li>
						<li><label>是否回复：</label> <select name="isAnswered">
								<option value="">请选择</option>
								<option value="0"
									<c:if test="${condition['isAnswered'] eq '0' }">selected="selected"</c:if>>未回复</option>
								<option value="1"
									<c:if test="${condition['isAnswered'] eq '1' }">selected="selected"</c:if>>已回复</option>
						</select></li>
					</ul>
					<ul class="searchContent">
						<li><label>开始时间：</label> <input type="text"
							name="fillinDateStartStr"
							value="${condition['fillinDateStartStr']}" class="Wdate"
							id="fillinDateStartStrbeginTime"
							onFocus="WdatePicker({isShowWeek:true,maxDate:'#F{$dp.$D(\'fillinDateStartStrendTime\')}'})" />

						</li>
						<li><label>结束时间：</label> <input type="text"
							name="fillinDateEndStr" value="${condition['fillinDateEndStr']}"
							class="Wdate" id="fillinDateStartStrendTime"
							onFocus="WdatePicker({isShowWeek:true,minDate:'#F{$dp.$D(\'fillinDateStartStrbeginTime\')}'})" />
						</li>
					</ul>
					<div class="subBar">
						<ul>
							<li><span class="tips">提示：更多查询条件请点击高级查询</span></li>
							<li><div class="buttonActive">
									<div class="buttonContent">
										<button type="submit">查 询</button>
									</div>
								</div></li>
							<li><a class="button"
								href="${baseUrl }/edu3/teacher/feedback/list.html?con=advance"
								target="dialog" rel="RES_METARES_BBS_FEEBACKTOPIC" title="查询条件"><span>高级查询</span></a></li>
						</ul>
					</div>
				</div>
			</form>
		</div>
		<div class="pageContent">
			<gh:resAuth parentCode="RES_TEACHING_ESTAB_FEEDBACK" pageType="list"></gh:resAuth>
			<table class="table" layouth="161">
				<thead>
					<tr>
						<th width="5%"><input type="checkbox" name="checkall"
							id="check_all_refeedback"
							onclick="checkboxAll('#check_all_refeedback','resourceid','#refeedbackBody')" /></th>
						<th width="25%">反馈问题</th>
						<th width="10%">反馈类型</th>
						<th width="10%">反馈学生</th>
						<th width="10%">学生帐号</th>
						<th width="15%">反馈时间</th>
						<th width="10%">回复人</th>
						<th width="15%">回复日期</th>
					</tr>
				</thead>
				<tbody id="refeedbackBody">
					<c:forEach items="${feedbackPage.result }" var="bbsTopic"
						varStatus="vs">
						<tr>
							<td><input type="checkbox" name="resourceid"
								value="${bbsTopic.resourceid }" autocomplete="off" /></td>
							<td><a href="javascript:;"
								onclick="javascript:$.pdialog.open('${baseUrl }/edu3/teacher/feedback/input.html?act=view&resourceid=${bbsTopic.resourceid}','viewFeedback','查看反馈',{width:700,height:400});">${bbsTopic.title }</a>
							</td>
							<td>${ghfn:dictCode2Val('CodeFacebookType',bbsTopic.facebookType)}</td>
							<td><a href="javascript:void(0)"
								onclick="viewStuInfoFeedback('${bbsTopic.bbsUserInfo.sysUser.userExtends['defalutrollid'].exValue }')">${bbsTopic.fillinMan}</a></td>
							<td>${bbsTopic.bbsUserInfo.sysUser.username}</td>
							<td><fmt:formatDate value="${bbsTopic.fillinDate }"
									pattern="yyyy-MM-dd HH:mm:ss" /></td>
							<td><c:if test="${bbsTopic.replyCount gt 0 }">${bbsTopic.lastReplyMan }</c:if></td>
							<td><c:if test="${bbsTopic.replyCount gt 0 }">
									<fmt:formatDate value="${bbsTopic.lastReplyDate }"
										pattern="yyyy-MM-dd HH:mm:ss" />
								</c:if></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<gh:page page="${feedbackPage}"
				goPageUrl="${baseUrl}/edu3/teacher/feedback/list.html"
				condition="${condition }" pageType="sys" />

		</div>
	</div>
</body>
</html>