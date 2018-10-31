<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>学生讨论小组管理</title>
<script type="text/javascript">
	//新增
	function addBbsGroup(){
		var url = "${baseUrl}/edu3/framework/bbs/bbsgroup/input.html";
		navTab.openTab('RES_METARES_BBSGROUP_EDIT', url, '新增学习小组');
	}
	
	//修改
	function modifyBbsGroup(){
		var url = "${baseUrl}/edu3/framework/bbs/bbsgroup/input.html";
		if(isCheckOnlyone('resourceid','#bbsGroupBody')){
			navTab.openTab('RES_METARES_BBSGROUP_EDIT', url+'?resourceid='+$("#bbsGroupBody input[@name='resourceid']:checked").val(), '编辑学习小组');
		}			
	}
		
	//删除
	function removeBbsGroup(){
		pageBarHandle("您确定要删除这些记录吗？","${baseUrl}/edu3/framework/bbs/bbsgroup/remove.html","#bbsGroupBody");
	}	
	//小组成员
	function listBbsGroupUsers(){
		var classesid = "";
		$("#bbsGroupBody input[name='resourceid']").each(function(){
			if($(this).attr("checked")){
				classesid = $(this).attr("classesid");
				return false;
			}
		});
		//alert(classesid);
		var url = "${baseUrl}/edu3/framework/bbs/bbsgroupusers/list.html";
		if(isCheckOnlyone('resourceid','#bbsGroupBody')){
			navTab.openTab('RES_METARES_BBSGROUPUSERS', url+'?groupId='+$("#bbsGroupBody input[@name='resourceid']:checked").val()+"&classesId="+classesid, '分配小组成员');
		}	
	}	
	//自动分配小组
	function autoBbsGroupUsers(){
		$.pdialog.open("${baseUrl}/edu3/framework/bbs/bbsgroupusers/autoassign.html", "autoassignbbsgroup", "自动分配小组", {width:600,height:400});
	}
</script>
</head>
<body>
	<div class="page">
		<div class="pageHeader">
			<form onsubmit="return navTabSearch(this);"
				action="${baseUrl }/edu3/framework/bbs/bbsgroup/list.html"
				method="post">
				<div class="searchBar">
					<ul class="searchContent">
						<li><label>组名：</label><input type="text" name="groupName"
							value="${condition['groupName'] }" /></li>
						<li><label>课程名称：</label><input type="text" name="courseName"
							value="${condition['courseName'] }" /></li>
						<li><label>状态：</label>
						<gh:select name="status" value="${condition['status'] }"
								dictionaryCode="CodeBbsGroupStatus" /></li>
						<li><label>班级：</label> <gh:classesAutocomplete
								name="classesId" id="bbsgroup_list_classid" tabindex="1"
								displayType="code" defaultValue="${condition['classesId']}"
								exCondition="${classesCondition}"></gh:classesAutocomplete></li>
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
			<gh:resAuth parentCode="RES_METARES_BBSGROUP" pageType="list"></gh:resAuth>
			<table class="table" layouth="138">
				<thead>
					<tr>
						<th width="3%"><input type="checkbox" name="checkall"
							id="check_all_bbsGroup"
							onclick="checkboxAll('#check_all_bbsGroup','resourceid','#bbsGroupBody')" /></th>
						<th width="15%">所属课程</th>
						<th width="15%">班级</th>
						<th width="15%">组名</th>
						<th width="15%">组描述</th>
						<th width="5%">组长</th>
						<th width="5%">组状态</th>
						<th width="5%">小组成员人数</th>
					</tr>
				</thead>
				<tbody id="bbsGroupBody">
					<c:forEach items="${bbsGroupListPage.result }" var="bbsGroup"
						varStatus="vs">
						<tr>
							<td><input type="checkbox" name="resourceid"
								value="${bbsGroup.resourceid }"
								classesid="${bbsGroup.classes.resourceid }" autocomplete="off" /></td>
							<td>${bbsGroup.course.courseName }</td>
							<td>${bbsGroup.classes.classname }</td>
							<td><a href="javascript:;"
								onclick="javascript:$.pdialog.open('${baseUrl }/edu3/framework/bbsgroup/view.html?resourceid=${bbsGroup.resourceid }','viewBbsGroup','${bbsGroup.groupName }',{width:700,height:500});">${bbsGroup.groupName }</a></td>
							<td>${bbsGroup.groupDescript }</td>
							<td>${bbsGroup.leaderName}</td>
							<td>${ghfn:dictCode2Val('CodeBbsGroupStatus',bbsGroup.status)}</td>
							<td>${fn:length(bbsGroup.groupUsers)}</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<gh:page page="${bbsGroupListPage}"
				goPageUrl="${baseUrl}/edu3/framework/bbs/bbsgroup/list.html"
				condition="${condition }" pageType="sys" />

		</div>
	</div>
</body>
</html>