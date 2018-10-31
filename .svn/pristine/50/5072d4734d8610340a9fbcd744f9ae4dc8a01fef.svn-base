<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>栏目管理</title>
</head>
<body>
	<script type="text/javascript">
	//新增
	function addHelpChannel(){
		if(isChecked('resourceid','#helpChannelBody')){
			alertMsg.warn('新增操作时,请不要选择记录。');
		}else{
			navTab.openTab('_blank', '${baseUrl}/edu3/portal/manage/helper/channel/input.html?method=add', '新增帮助栏目');
		}
	}
	
	//修改
	function modifyHelpChannel(){
		var url = "${baseUrl}/edu3/portal/manage/helper/channel/input.html";
		if(isCheckOnlyone('resourceid','#helpChannelBody')){
		 	navTab.openTab('_blank', url+'?resourceid='+$("#helpChannelBody input[@name='resourceid']:checked").val(), '编辑帮助栏目');
		}			
	}
	
	//删除
	function deleteHelpChannel(){
		pageBarHandle("您确定要删除这些栏目吗？注意：当您确定删除这些栏目之后，栏目所对应的文章也将删除。","${baseUrl}/edu3/portal/manage/helper/channel/remove.html","#helpChannelBody");
	}
		
	
</script>
	<div class="page">
		<div class="pageHeader">
			<form onsubmit="return navTabSearch(this);"
				action="${baseUrl }/edu3/portal/manage/helper/channel/list.html"
				method="post">
				<div class="searchBar">
					<ul class="searchContent">
						<li><label>栏目名称：</label><input type="text"
							name="helperChannelName" id="helperChannelName"
							value="${condition['helperChannelName'] }" /></li>
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
			<gh:resAuth parentCode="RES_HELP_CHANNEL" pageType="list"></gh:resAuth>
			<table class="table" layouth="138">
				<thead>
					<tr>
						<th width="5%"><input type="checkbox" name="checkall"
							id="helpChannelCheck_all"
							onclick="checkboxAll('#helpChannelCheck_all','resourceid','#helpChannelBody')" /></th>
						<th width="30%">在线帮助栏目名称</th>
						<th width="65%">关联用户类型</th>
					</tr>
				</thead>
				<tbody id="helpChannelBody">
					<c:forEach items="${helperChannelList}" var="helpChannel"
						varStatus="vs">
						<tr>
							<td><c:if test="${helpChannel.resourceid != 0 }">
									<input type="checkbox" name="resourceid"
										value="${helpChannel.resourceid }" autocomplete="off" />
								</c:if></td>
							<td><c:forEach begin="1" end="${ helpChannel.channelLevel}">
		            &nbsp;&nbsp;&nbsp;
		            </c:forEach> ${helpChannel.channelName }</td>
							<td>${rolesNames[vs.index]}</td>

						</tr>
					</c:forEach>
				</tbody>
			</table>

		</div>
	</div>

</body>
</html>
