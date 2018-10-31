<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>通讯录</title>
<gh:loadCom components="treeView" />
<script type="text/javascript">
jQuery(document).ready(function(){
		jQuery("#addressbookleftree").treeview({
			persist: "location",			
			unique: false
		});	
		if($('._addressbookLeftTree')){//如果有左侧树，则赋值为自适应高度		
			$('._addressbookLeftTree').height($("#container .tabsPageContent").height()-5);
		}	
	})
	

	function goSelected(unitid){
		navTab.openTab('RES_PERSON_ADDRESSBOOK', '${baseUrl}/edu3/person/info/addressbook.html?unitId='+unitid);
	}
	
	function sendMsg(type,touser){
		if(type==0){
			$.pdialog.open('${baseUrl }/edu3/framework/message/dailog.html?touser='+touser,'selector','发送消息',{mask:true,width:750,height:500});
		}else{
			alertMsg.warn("该功能暂时未开通!");
		}
	}
	
</script>

</head>
<body>
	<div style="float: left; width: 19%">
		<div class="_addressbookLeftTree"
			style="display: block; overflow: auto; padding-bottom: 6px; border: solid 1px #CCC; line-height: 21px;">
			<gh:treeview nodes="${unitTree}" id="addressbookleftree" css="folder" />
		</div>
	</div>
	<div class="page" style="float: left; width: 81%">
		<div class="pageHeader">
			<form id="userForm" onsubmit="return navTabSearch(this);"
				action="${baseUrl }/edu3/person/info/addressbook.html" method="post">
				<div class="searchBar">
					<ul class="searchContent">
						<li><label>用户中文名：</label> </label><input type="text" name="cnName"
							id="cnName" value="${condition['cnName'] }" /> <input
							type="hidden" name="unitId" id="unitId"
							value="${condition['unitId'] }" /></li>
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
			<table class="table" layouth="112">
				<thead>
					<tr>
						<th width="10%">姓名</th>
						<th width="10%">单位</th>
						<th width="10%">邮编</th>
						<th width="25%">通信地址</th>
						<th width="10%">办公电话</th>
						<th width="10%">手机</th>
						<th width="10%">电子邮件</th>
						<th width="15%">操作</th>
					</tr>
				</thead>
				<tbody id="userBody">
					<c:forEach items="${userList.result}" var="user" varStatus="vs">
						<tr>
							<td><a
								href="${baseUrl }/edu3/framework/edumanager/view.html?userId=${user.resourceid }"
								target="dialog" title="${user.cnName }" rel="edumanager_view"
								width="700" height="500">${user.cnName }</a></td>
							<td>${user.orgUnit.unitName }</td>
							<td>${user.postCode }</td>
							<td>${user.homeAddress }</td>
							<td>${user.officeTel }</td>
							<td>${user.mobile }</td>
							<td>${user.email }</td>
							<td><a href="###" onclick="sendMsg(0,'${user.username }');">发消息</a>
								<a href="###" onclick="sendMsg(1,'${user.email }');">发邮件</a> <a
								href="###" onclick="sendMsg(2,'${user.mobile }');">发短信</a></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>

			<gh:page page="${userList}"
				goPageUrl="${baseUrl }/edu3/person/info/addressbook.html"
				condition="${condition }" pageType="sys" />
		</div>
	</div>
</body>
</html>