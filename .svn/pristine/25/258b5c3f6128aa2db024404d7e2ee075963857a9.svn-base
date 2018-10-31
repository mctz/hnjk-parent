<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>精品课程栏目</title>
<script type="text/javascript">
	//新增
	function addCourseChannel(){
		$.pdialog.open('${baseUrl }/edu3/resources/coursechannel/input.html?courseId='+$("#courseChannel_courseId").val(), "RES_METARES_COURSECHANNEL_INPUT", "新增精品课程栏目", {width:800,height:600,mask:true});
	}	
	//修改
	function modifyCourseChannel(){
		if(isCheckOnlyone('resourceid','#coursechannelBody')){
			$.pdialog.open('${baseUrl }/edu3/resources/coursechannel/input.html?resourceid='+$("#coursechannelBody input[@name='resourceid']:checked").val()+"&courseId="+$("#courseChannel_courseId").val(), "RES_METARES_COURSECHANNEL_INPUT", "编辑精品课程栏目", {width:800,height:600,mask:true});
		}			
	}		
	//删除
	function removeCourseChannel(){	
		if(!isChecked('resourceid',"#coursechannelBody")){
 			alertMsg.warn('请选择一条要操作记录！');
			return false;
	 	}
		alertMsg.confirm("您确定要删除这些记录吗？", {
			okCall: function(){//执行			
				var res = [];
				$("#coursechannelBody input[@name='resourceid']:checked").each(function(){
                   res.push($(this).val());
                });	                
				$.post("${baseUrl}/edu3/resources/coursechannel/remove.html",{resourceid:res.join(',')}, function (json){
					DWZ.ajaxDone(json);
					if (json.statusCode == 200){
						navTab.reload(null, {}, 'RES_METARES_COURSECHANNEL');
					}
				}, "json");
			}
		});	
	}
</script>
</head>
<body>
	<div class="page">
		<div class="pageHeader">
			<form onsubmit="return navTabSearch(this);"
				action="${baseUrl }/edu3/resources/coursechannel/list.html"
				method="post">
				<div class="searchBar">
					<ul class="searchContent">
						<li><label>栏目名称：</label><input
							value="${condition['channelName']}" name="channelName"
							style="width: 55%;" /> <input type="hidden"
							id="courseChannel_courseId" name="courseId"
							value="${condition['courseId']}" /></li>
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
			<gh:resAuth parentCode="RES_METARES_COURSEWARE_MANAGE"
				pageType="qlist"></gh:resAuth>
			<table class="table" layouth="138">
				<thead>
					<tr>
						<th width="10%"><input type="checkbox" name="checkall"
							id="check_all_coursechannel"
							onclick="checkboxAll('#check_all_coursechannel','resourceid','#coursechannelBody')" /></th>
						<th width="15%">栏目名称</th>
						<th width="15%">栏目内容</th>
						<th width="30%">栏目链接</th>
						<th width="10%">栏目状态</th>
						<th width="15%">栏目位置</th>
						<th width="5%">排序号</th>
					</tr>
				</thead>
				<tbody id="coursechannelBody">
					<c:forEach items="${channelList}" var="channel" varStatus="vs">
						<tr>
							<td><c:if test="${not empty channel.parent}">
									<input type="checkbox" name="resourceid"
										value="${channel.resourceid }" autocomplete="off"
										<c:if test="${channel.resourceid eq courseChannel.resourceid}">selected="selected"</c:if> />
								</c:if></td>
							<td><c:forEach begin="1" end="${channel.channelLevel}">
				            &nbsp;&nbsp;&nbsp;
				            </c:forEach>${channel.channelName }</td>
							<td>${channel.channelContent }</td>
							<td>${channel.channelHref }</td>
							<td>${channel.channelStatus==0 ? "启用": "停用" }</td>
							<td>
								${ghfn:dictCode2Val('channelPosition',channel.channelPosition) }
							</td>
							<td>${channel.showOrder }</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
	</div>
</body>
</html>