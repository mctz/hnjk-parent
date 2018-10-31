<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>课程论坛回复帖管理</title>
<script type="text/javascript">
	//新增
	function addBbsReply(){
		var url = "${baseUrl}/edu3/metares/topicreply/bbsreply/input.html";
		navTab.openTab('RES_METARES_BBS_RREPLY_EDIT', url+"?bbsTopicId="+$("#bbsReplyForm input[name='bbsTopicId']").val(), '新增回复帖');
	}
	
	//修改
	function editBbsReply(){
		var url = "${baseUrl}/edu3/metares/topicreply/bbsreply/input.html";
		if(isCheckOnlyone('resourceid','#bbsReplyBody')){
			navTab.openTab('RES_METARES_BBS_RREPLY_EDIT', url+'?resourceid='+$("#bbsReplyBody input[@name='resourceid']:checked").val(), '编辑回复帖');
		}			
	}
		
	//删除
	function removeBbsReply(){	
		pageBarHandle("您确定要删除这些记录吗？","${baseUrl}/edu3/metares/topicreply/bbsreply/remove.html","#bbsReplyBody");
	}		
</script>
</head>
<body>
	<div class="page">
		<div class="pageHeader">
			<form id="bbsReplyForm" onsubmit="return navTabSearch(this);"
				action="${baseUrl }/edu3/metares/topicreply/bbsreply/list.html"
				method="post">
				<div class="searchBar">
					<ul class="searchContent">
						<li><label>回复人：</label><input type="text" name="replyMan"
							value="${condition['replyMan'] }" /> <input type="hidden"
							name="bbsTopicId" value="${condition['bbsTopicId'] }" /></li>
						<li><label>回复时间：</label> 从<input type="text"
							name="replyDateStartStr"
							value="${condition['replyDateStartStr']}" class="Wdate"
							id="rbeginTime" onFocus="WdatePicker()" /></li>
						<li>到 &nbsp;&nbsp;&nbsp;<input type="text"
							name="replyDateEndStr" value="${condition['replyDateEndStr']}"
							class="Wdate"
							onFocus="WdatePicker({minDate:'#F{$dp.$D(\'rbeginTime\')}'})" />
						</li>
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
			<gh:resAuth parentCode="RES_METARES_BBS_TOPICREPLY"
				pageType="sublist"></gh:resAuth>
			<table class="table" layouth="138">
				<thead>
					<tr>
						<th width="5%"><input type="checkbox" name="checkall"
							id="check_all_bbsreply"
							onclick="checkboxAll('#check_all_bbsreply','resourceid','#bbsReplyBody')" /></th>
						<th width="30%">回复帖子</th>
						<th width="30%">回复人</th>
						<th width="25%">回复日期</th>
						<th width="10%">排序号</th>
					</tr>
				</thead>
				<tbody id="bbsReplyBody">
					<c:forEach items="${bbsReplyListPage.result }" var="bbsReply"
						varStatus="vs">
						<tr>
							<td><input type="checkbox" name="resourceid"
								value="${bbsReply.resourceid }" autocomplete="off" /></td>
							<td><a href="javascript:;"
								onclick='window.open("${baseUrl }/edu3/learning/bbs/topic.html?topicId=${bbsReply.bbsTopic.resourceid }&courseId=${bbsReply.bbsTopic.course.resourceid }&pageNum=<fmt:formatNumber type='number' value='${(bbsReply.showOrder-bbsReply.showOrder%30)/30+1 }' maxFractionDigits='0' />#${bbsReply.resourceid }","course_bbs")'>Re:
									${bbsReply.bbsTopic.title }</a></td>
							<td>${bbsReply.replyMan }</td>
							<td>${bbsReply.replyDate }</td>
							<td>${bbsReply.showOrder }</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<gh:page page="${bbsReplyListPage}"
				goPageUrl="${baseUrl}/edu3/metares/topicreply/bbsreply/list.html"
				condition="${condition }" pageType="sys" />

		</div>
	</div>
</body>
</html>