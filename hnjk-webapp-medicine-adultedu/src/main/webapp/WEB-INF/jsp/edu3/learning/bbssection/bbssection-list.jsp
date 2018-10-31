<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>论坛版块管理</title>
<script type="text/javascript">
	//新增
	function addBbsSection(){
		var url = "${baseUrl}/edu3/metares/bbssection/input.html";
		navTab.openTab('RES_METARES_BBS_BBSSECTION_EDIT', url, '新增论坛版块');
	}
	
	//修改
	function editBbsSection(){
		var url = "${baseUrl}/edu3/metares/bbssection/input.html";
		if(isCheckOnlyone('resourceid','#bbsSectionBody')){
			navTab.openTab('RES_METARES_BBS_BBSSECTION_EDIT', url+'?resourceid='+$("#bbsSectionBody input[@name='resourceid']:checked").val(), '编辑论坛版块');
		}			
	}
		
	//删除
	function removeBbsSection(){
		pageBarHandle("您确定要删除这些记录吗？","${baseUrl}/edu3/metares/bbssection/remove.html","#bbsSectionBody");
	}	
</script>
</head>
<body>
	<div class="page">
		<div class="pageHeader">
			<form onsubmit="return navTabSearch(this);"
				action="${baseUrl }/edu3/metares/bbssection/list.html" method="post">
				<div class="searchBar">
					<ul class="searchContent">
						<li><label style="width: 100px;">论坛版块名称：</label><input
							style="width: 120px;" type="text" name="sectionName"
							value="${condition['sectionName'] }" /></li>
						<li><label>版主名：</label><input type="text" name="masterName"
							value="${condition['masterName'] }" /></li>
					</ul>
					<ul class="searchContent">
						<li><label>选择版块：</label> <gh:bbsSectionSelect
								id="bbsSectiuon_bbsSectionId" name="bbsSectionId"
								style="width: 130px;" value="${condition['bbsSectionId'] }" />
						</li>
						<li><label style="width: 100px;">是否课程论坛版块：</label> <gh:select
								dictionaryCode="yesOrNo" id="bbsSectiuon_isCourseSection"
								name="isCourseSection" value="${condition['isCourseSection'] }"
								style="width:100px;" /></li>
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
			<gh:resAuth parentCode="RES_METARES_BBS_BBSSECTION" pageType="list"></gh:resAuth>
			<table class="table" layouth="161">
				<thead>
					<tr>
						<th width="5%"><input type="checkbox" name="checkall"
							id="check_all_bbssection"
							onclick="checkboxAll('#check_all_bbssection','resourceid','#bbsSectionBody')" /></th>
						<th width="10%">论坛版块编码</th>
						<th width="15%">父版块</th>
						<th width="20%">论坛版块名称</th>
						<th width="20%">论坛版块介绍</th>
						<th width="10%">版主名</th>
						<th width="10%">点击量</th>
						<th width="10%">帖子总量</th>
					</tr>
				</thead>
				<tbody id="bbsSectionBody">
					<c:forEach items="${bbsSectionListPage.result }" var="bbsSection"
						varStatus="vs">
						<tr>
							<td><input type="checkbox" name="resourceid"
								value="${bbsSection.resourceid }" autocomplete="off" /></td>
							<td>${bbsSection.sectionCode }</td>
							<td>${bbsSection.parent.sectionName }</td>
							<td>${bbsSection.sectionName }</td>
							<td>${bbsSection.sectionDescript}</td>
							<td>${bbsSection.masterName }</td>
							<td>${bbsSection.clickCount }</td>
							<td>${bbsSection.topicCount }</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<gh:page page="${bbsSectionListPage}"
				goPageUrl="${baseUrl}/edu3/metares/bbssection/list.html"
				condition="${condition }" pageType="sys" />

		</div>
	</div>
</body>
</html>