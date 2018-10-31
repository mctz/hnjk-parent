<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>栏目管理</title>
</head>
<body>
	<script type="text/javascript">
	//新增
	function addChannel(){
		navTab.openTab('_blank', '${baseUrl}/edu3/portal/manage/channel/input.html', '新增栏目');
	}
	
	//修改
	function modifyChannel(){
			var url = "${baseUrl}/edu3/portal/manage/channel/input.html";
			if(isCheckOnlyone('resourceid','#channelBody')){
		 	navTab.openTab('_blank', url+'?resourceid='+$("#channelBody input[@name='resourceid']:checked").val(), '编辑栏目');
		}			
	}
	
	//删除
	function deleteChannel(){	
		pageBarHandle("您确定要删除这些记录吗？","${baseUrl}/edu3/portal/manage/channel/remove.html","#channelBody");
	}
		
	
</script>
	<div class="page">
		<div class="pageHeader">
			<form onsubmit="return navTabSearch(this);"
				action="${baseUrl }/edu3/portal/manage/channel/list.html"
				method="post">
				<div class="searchBar">
					<ul class="searchContent">
						<li><label>栏目名称：</label><input type="text" name="channelName"
							id="channelName" value="${condition['channelName'] }" /></li>
						<li><label>栏目位置：</label> <gh:select name="channelPosition"
								value="${condition['channelPosition'] }"
								dictionaryCode="channelPosition" /></li>
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
			<gh:resAuth parentCode="RES_PORTAL_CHANNEL" pageType="list"></gh:resAuth>
			<table class="table" layouth="138">
				<thead>
					<tr>
						<th width="5%"><input type="checkbox" name="checkall"
							id="check_all"
							onclick="checkboxAll('#check_all','resourceid','#channelBody')" /></th>
						<th width="30%">栏目名称</th>
						<th width="25%">栏目内容</th>
						<th width="10%">栏目状态</th>
						<th width="20%">栏目位置</th>
					</tr>
				</thead>
				<tbody id="channelBody">
					<c:forEach items="${channelList}" var="channel" varStatus="vs">
						<tr>
							<td><c:if test="${channel.resourceid != '0' }">
									<input type="checkbox" name="resourceid"
										value="${channel.resourceid }" autocomplete="off" />
								</c:if></td>
							<td><c:forEach begin="1" end="${ channel.channelLevel}">
		            &nbsp;&nbsp;&nbsp;
		            </c:forEach> ${channel.channelName }</td>
							<td>${channel.channelContent }</td>
							<td>${channel.channelStatus==0 ? "正常": "停用" }</td>
							<td>
								${ghfn:dictCode2Val('channelPosition',channel.channelPosition) }
							</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>

		</div>
	</div>

</body>
</html>
