<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>选择预约</title>
<script type="text/javascript">
	function ctx777(){
		$.pdialog.reload("${baseUrl}/edu3/teaching/graduateMsg/chooseGmsg.html?teacherName="+$('#teacherName7').val());
	}
		
	function clickThis777(obj){
		if(obj.checked){
			$("#paperOrderId").val(obj.value);
			$("#subject").val(obj.alt);
		}
	}
	
	$(document).ready(function(){
			// 修改高度
			$("#cd_right").css("height",parseInt($(".dialogContent").css("height")));
			
			window.setTimeout(function(){
				var existId = $("#paperOrderId").val();
				$("#gpnBody input[value='"+existId+"']").attr("checked",true);
			},500);
	})
</script>
</head>
<body>

	<div class="page">
		<div class="pageHeader">
			<div class="searchBar">
				<ul class="searchContent">
					<li><label>导师姓名：</label><input type="text" id="teacherName7"
						name="teacherName" value="${condition['teacherName']}" /></li>
				</ul>
				<div class="subBar">
					<ul>
						<a class="button" href="javascript:;" onclick="ctx777()"><span>查
								询</span></a>&nbsp;&nbsp;
						<a class="button" href="javascript:;"
							onclick="$.pdialog.closeCurrent();"><span>确 定</span></a>
					</ul>
				</div>
			</div>
		</div>
		<div class="pageContent">
			<table class="table" layouth="138">
				<thead>
					<tr>
						<th width="5%">&nbsp;</th>
						<th width="15%">毕业论文批次</th>
						<th width="15%">导师姓名</th>
						<th width="25%">标题</th>
						<th width="25%">内容</th>
						<th width="15%">发布时间</th>
					</tr>
				</thead>
				<tbody id="gpnBody">
					<c:forEach items="${noticeList.result}" var="n" varStatus="vs">
						<tr>
							<td><input type="radio" name="resourceid"
								value="${n.resourceid}" onclick="clickThis777(this)"
								alt="${n.title}" /></td>
							<td>${n.examSub.batchName }</td>
							<td>${n.guidTeacherName }</td>
							<td>${n.title }</td>
							<td>${n.content }</td>
							<td><fmt:formatDate value="${n.pubTime }"
									pattern="yyyy-MM-dd" /></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>

			<gh:page page="${noticeList}"
				goPageUrl="${baseUrl }/edu3/teaching/graduateMsg/chooseGmsg.html"
				pageType="sys" targetType="dialog" condition="${condition}" />
		</div>
	</div>

</body>
</html>