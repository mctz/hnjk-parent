<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>课程论坛帖子管理</title>
<script type="text/javascript">
	//回复帖管理
	function listBbsReply(){
		var url = "${baseUrl}/edu3/metares/topicreply/bbsreply/list.html";
		if(isCheckOnlyone('resourceid','#bbsTopicBody')){
			navTab.openTab('RES_METARES_BBS_RREPLY', url+'?bbsTopicId='+$("#bbsTopicBody input[@name='resourceid']:checked").val(), '回复帖管理');
		}	
	}	
	//修改
	function modifyBbsTopic(){
		var url = "${baseUrl}/edu3/metares/topicreply/bbstopic/input.html";
		if(isCheckOnlyone('resourceid','#bbsTopicBody')){
			navTab.openTab('RES_METARES_BBS_TOPIC_EDIT', url+'?resourceid='+$("#bbsTopicBody input[@name='resourceid']:checked").val(), '编辑帖子');
		}			
	}	
	//删除帖子
	function removeBbsTopic(){	
		pageBarHandle("您确定要删除这些帖子吗？","${baseUrl}/edu3/metares/topicreply/bbstopic/remove.html","#bbsTopicBody");
	}
	//加精
	function digestBbsTopic(){	
		pageBarHandle("您确定要设置这些帖子为精华帖吗？","${baseUrl}/edu3/metares/topicreply/bbstopic/status.html?status=1","#bbsTopicBody");
	}
	//锁定
	function lockBbsTopic(){	
		pageBarHandle("您确定要锁定这些帖子吗？","${baseUrl}/edu3/metares/topicreply/bbstopic/status.html?status=-1","#bbsTopicBody");
	}
	//置顶
	function stickBbsTopic(){	
		pageBarHandle("您确定要置顶这些帖子吗？","${baseUrl}/edu3/metares/topicreply/bbstopic/status.html?status=3","#bbsTopicBody");
	}
	//取消置顶
	function unstickBbsTopic(){	
		pageBarHandle("您确定要置顶这些帖子吗？","${baseUrl}/edu3/metares/topicreply/bbstopic/status.html?status=-3","#bbsTopicBody");
	}
	//还原帖子状态
	function revertBbsTopic(){	
		pageBarHandle("您确定要还原这些帖子为普通帖吗？","${baseUrl}/edu3/metares/topicreply/bbstopic/status.html?status=0","#bbsTopicBody");
	}
	//移动帖子
	function moveBbsTopic(){
		if(!isChecked('resourceid','#bbsTopicBody')){
 			alertMsg.warn('请选择一条要操作记录！');
			return false;
 		}	
		var sel = "<label>请选择版块：</label><select name='sectionId' id='sectionId'>"+$('#bbsSectionId').html()+"</select>";
		var res = "";
		var k = 0;
		var num = $("#bbsTopicBody input[name='resourceid']:checked").size();
		$("#bbsTopicBody input[@name='resourceid']:checked").each(function(){
            res+=$(this).val();
            if(k != num -1 ) res += ",";
            k++;
        });
        var postUrl = "${baseUrl}/edu3/metares/topicreply/bbstopic/move.html";
		alertMsg.confirm(sel,{okCall:function(){
			var sid = $("#sectionId").val();
			if(sid==""){
				alertMsg.warn('请选择一个版块！');
				return false;
			}
			$.post(postUrl,{resourceid:res,sectionId:sid}, navTabAjaxDone, "json");
		},okName:'移动帖子'});
	}
</script>
</head>
<body>
	<div class="page">
		<div class="pageHeader">
			<form onsubmit="return navTabSearch(this);"
				action="${baseUrl }/edu3/metares/topicreply/bbstopic/list.html"
				method="post">
				<div class="searchBar">
					<ul class="searchContent">
						<c:if test="${empty condition['sectionCode'] }">
							<li><label>所属论坛版块：</label> <gh:bbsSectionSelect
									id="bbsSectionId" name="bbsSectionId" style="width: 40%;"
									value="${condition['bbsSectionId'] }" /> <%--
					<select id="bbsSectionId" name="bbsSectionId" style="width: 40%;">
						<option value="" ${(empty condition['bbsSectionId'])?'selected':''}>
							选取所有版面
						</option>											
						<c:forEach items="${parentBbsSections}" var="item">
							<c:forEach items="${item.value}" var="section" varStatus="vs">
								<c:choose>
									<c:when test="${vs.first }">
										<option value="${section.resourceid }" ${(condition['bbsSectionId']==section.resourceid)?'selected':''}>
										╋${section.sectionName }
										</option>
									</c:when>
									<c:otherwise>
										<option value="${section.resourceid }" ${(condition['bbsSectionId']==section.resourceid)?'selected':''}>
											<c:forEach begin="1" end="${section.sectionLevel }" step="1">&nbsp;&nbsp;</c:forEach>
											├${section.sectionName }
										</option>
									</c:otherwise>
								</c:choose>
							</c:forEach>
						</c:forEach>		
					</select>
					 --%></li>
						</c:if>
						<li><label>标题：</label><input type="text" name="title"
							value="${condition['title'] }" /></li>
						<li><span class="tips">提示：更多查询条件请点击高级查询</span></li>
					</ul>
					<div class="subBar">
						<ul>
							<li><div class="buttonActive">
									<div class="buttonContent">
										<button type="submit">查 询</button>
									</div>
								</div></li>
							<li><a class="button"
								href="${baseUrl }/edu3/metares/topicreply/bbstopic/list.html?con=advance"
								target="dialog" rel="RES_METARES_BBS_TOPICREPLY" title="查询条件"><span>高级查询</span></a></li>
						</ul>
					</div>
				</div>
			</form>
		</div>

		<div class="pageContent">
			<gh:resAuth parentCode="RES_METARES_BBS_TOPICREPLY" pageType="list"></gh:resAuth>
			<table class="table" layouth="138">
				<thead>
					<tr>
						<th width="5%"><input type="checkbox" name="checkall"
							id="check_all_bbstopic"
							onclick="checkboxAll('#check_all_bbstopic','resourceid','#bbsTopicBody')" /></th>
						<th width="10%">所属论坛版块</th>
						<th width="32%">标题</th>
						<th width="8%">帖子类型</th>
						<th width="10%">发帖人</th>
						<th width="10%">发帖时间</th>
						<th width="5%">回复数</th>
						<th width="10%">最后回复人</th>
						<th width="10%">最后回复日期</th>
					</tr>
				</thead>
				<tbody id="bbsTopicBody">
					<c:forEach items="${bbsTopicListPage.result }" var="bbsTopic"
						varStatus="vs">
						<tr>
							<td><input type="checkbox" name="resourceid"
								value="${bbsTopic.resourceid }" autocomplete="off" /></td>
							<td>${bbsTopic.bbsSection.sectionName }</td>
							<td><strong>${bbsTopic.topLevel>0?'置顶':''}
									${bbsTopic.status != 0 ? ghfn:dictCode2Val('CodeBbsTopicStatus',bbsTopic.status):'' }</strong>
								<a href="javascript:;"
								onclick="window.open('${baseUrl }/edu3/learning/bbs/topic.html?topicId=${bbsTopic.resourceid }&courseId=${bbsTopic.course.resourceid }','course_bbs')">${bbsTopic.title }</a>
							</td>
							<td>${ghfn:dictCode2Val('CodeBbsTopicType',bbsTopic.topicType) }</td>
							<td>${bbsTopic.fillinMan}</td>
							<td><fmt:formatDate value="${bbsTopic.fillinDate }"
									pattern="yyyy-MM-dd HH:mm:ss" /></td>
							<td>${bbsTopic.replyCount }</td>
							<td>${bbsTopic.lastReplyMan }</td>
							<td><fmt:formatDate value="${bbsTopic.lastReplyDate }"
									pattern="yyyy-MM-dd HH:mm:ss" /></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<gh:page page="${bbsTopicListPage}"
				goPageUrl="${baseUrl}/edu3/metares/topicreply/bbstopic/list.html"
				condition="${condition }" pageType="sys" />

		</div>
	</div>
</body>
</html>