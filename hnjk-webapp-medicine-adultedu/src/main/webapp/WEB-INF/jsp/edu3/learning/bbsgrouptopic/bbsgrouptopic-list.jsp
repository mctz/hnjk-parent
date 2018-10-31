<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>小组话题管理</title>
<script type="text/javascript">	
	//新增话题
	function addBbsGroupTopic(){
		var url = "${baseUrl}/edu3/framework/bbs/bbsgrouptopic/input.html";
		navTab.openTab('RES_METARES_BBSGROUPTOPIC_EDIT', url, '新增话题');
	}	
	//修改话题
	function modifyBbsGroupTopic(){
		var url = "${baseUrl}/edu3/framework/bbs/bbsgrouptopic/input.html";
		if(isCheckOnlyone('resourceid','#bbsGroupTopicBody')){
			navTab.openTab('RES_METARES_BBSGROUPTOPIC_EDIT', url+'?resourceid='+$("#bbsGroupTopicBody input[@name='resourceid']:checked").val(), '编辑话题');
		}			
	}	
	//删除话题
	function removeBbsGroupTopic(){	
		pageBarHandle("您确定要删除这些话题吗？","${baseUrl}/edu3/framework/bbs/bbsgrouptopic/remove.html","#bbsGroupTopicBody");
	}
	
	$("#bbsGroupTopicForm select[name='courseId']").change( function() {
	  	var courseId = $(this).val();
	  	var url = "${baseUrl}/edu3/framework/bbs/bbsgrouptopic/getGroup.html";
	  	$.post(url,{courseId:courseId}, function (json){
	  		var html = "<option value=''>请选择</option>";
	  		$(json).each(function(i){  
	  		    var x = json[i]; 
	  		    html += "<option value='"+x[0]+"'>"+x[1]+"</option>";
	  		});
	  		$("#bbsGroupTopicForm select[name='groupId']").html(html);
	  	}, "json");
	});
	
	function groupNavTabSearch(form){
		if(!$(form).valid()){return false;}
		return navTabSearch(form);
	}	
	 
</script>
</head>
<body>
	<div class="page">
		<div class="pageHeader">
			<form id="bbsGroupTopicForm"
				onsubmit="return groupNavTabSearch(this);"
				action="${baseUrl }/edu3/framework/bbs/bbsgrouptopic/list.html"
				method="post">
				<div class="searchBar">
					<ul class="searchContent">
						<li><label>所属课程：</label> <input type="text"
							value="${condition['courseName'] }" name="courseName" /></li>
						<li><label>所属小组：</label> <input type="text"
							value="${condition['groupName'] }" name="groupName" /></li>
						<li><label>标题：</label><input type="text" name="title"
							value="${condition['title'] }" /></li>
					</ul>
					<ul class="searchContent">
						<li><label>发表时间：</label> 从<input type="text"
							name="fillinDateStartStr"
							value="${condition['fillinDateStartStr']}" class="Wdate date1"
							id="bbsgrouptopicBeginTime"
							onFocus="WdatePicker({isShowWeek:true,maxDate:'#F{$dp.$D(\'bbsgrouptopicEndTime\')}'})" />
						</li>
						<li>到 &nbsp;&nbsp;&nbsp;<input type="text"
							name="fillinDateEndStr" value="${condition['fillinDateEndStr']}"
							class="Wdate date1" id="bbsgrouptopicEndTime"
							onFocus="WdatePicker({isShowWeek:true,minDate:'#F{$dp.$D(\'bbsgrouptopicBeginTime\')}'})" />
						</li>
						<li><label>班级：</label> <gh:classesAutocomplete
								name="classesId" id="interlocution_list_classid" tabindex="1"
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
			<gh:resAuth parentCode="RES_METARES_BBSGROUPTOPIC" pageType="list"></gh:resAuth>
			<table class="table" layouth="163" id="bbsGroupTopicTable">
				<thead>
					<tr>
						<th width="3%"><input type="checkbox" name="checkall"
							id="check_all_bbsgrouptopic"
							onclick="checkboxAll('#check_all_bbsgrouptopic','resourceid','#bbsGroupTopicBody')" /></th>
						<th width="10%">所属课程</th>
						<th width="15%">班级</th>
						<th width="20%">讨论话题</th>
						<th width="5%">发表人</th>
						<th width="10%">发表时间</th>
						<th width="10%">话题权限</th>
						<th width="10%">讨论截止时间</th>
					</tr>
				</thead>
				<tbody id="bbsGroupTopicBody">
					<c:forEach items="${bbsGroupTopicListPage.result }"
						var="bbsGroupTopic" varStatus="vs">
						<tr>
							<td><input type="checkbox" name="resourceid"
								value="${bbsGroupTopic.resourceid }" autocomplete="off" /></td>
							<td>${bbsGroupTopic.course.courseName }</td>
							<td>${bbsGroupTopic.classes.classname}</td>
							<td><a href="javascript:;"
								onclick="window.open('${baseUrl }/edu3/learning/bbs/topic.html?topicId=${bbsGroupTopic.resourceid }&courseId=${bbsGroupTopic.course.resourceid }','course_bbs')">${bbsGroupTopic.title }</a>
								<c:forEach items="${bbsGroupTopic.childs }" var="child"
									varStatus="vs">
				   			${vs.first?'(':'' } 
				   			<a href="javascript:;" style="color: #DD5E10;"
										onclick="window.open('${baseUrl }/edu3/learning/bbs/topic.html?topicId=${child.resourceid }&courseId=${child.course.resourceid }','course_bbs')">${child.bbsGroup.groupName }${vs.last?'':',' }
									</a>
				   			 ${vs.last?')':'' }
				   			</c:forEach></td>
							<td>${bbsGroupTopic.fillinMan}</td>
							<td><fmt:formatDate value="${bbsGroupTopic.fillinDate }"
									pattern="yyyy-MM-dd HH:mm:ss" /></td>
							<td>${ghfn:dictCode2Val('CodeViewPermiss',bbsGroupTopic.viewPermiss) }</td>
							<td><fmt:formatDate value="${bbsGroupTopic.endTime }"
									pattern="yyyy-MM-dd HH:mm:ss" /></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<gh:page page="${bbsGroupTopicListPage}"
				goPageUrl="${baseUrl}/edu3/framework/bbs/bbsgrouptopic/list.html"
				condition="${condition }" pageType="sys" />

		</div>
	</div>
</body>
</html>