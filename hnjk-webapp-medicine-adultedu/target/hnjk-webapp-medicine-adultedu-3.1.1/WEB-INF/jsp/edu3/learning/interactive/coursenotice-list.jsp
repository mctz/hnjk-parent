<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>课程公告管理</title>
</head>
<body>
	<script type="text/javascript">
//显示课程公告
	function showNotice(courseNoticeId,title){
		var url ="${baseUrl }/edu3/framework/coursenotice/view.html?courseNoticeId="+courseNoticeId;
		$.get(url,function(data){	
			var htmldiv = "<div class='alert' id='alertMsgBox' style='top: 150.5px; z-index: 9999;width:"+$(document).width()/3+"px'><div class='alertContent'><div class='confirm'><div class='alertInner'><h1>"+title+"</h1><div class='msg' style='height=100px;overflow:sroll;'>"+data+"</div></div><div class='toolBar'><ul><li><a href='javascript:' onclick='alertMsg.close()' rel='' class='button'><span>确定</span></a></li><li><a href='javascript:' onclick='alertMsg.close()' rel='' class='button'><span>取消</span></a></li></ul></div></div></div><div class='alertFooter'><div class='alertFooter_r'><div class='alertFooter_c'></div></div></div></div>";
			$(htmldiv).appendTo("body").show();	
		});		
	}
</script>
	<div class="page">
		<div class="pageHeader">
			<form onsubmit="return navTabSearch(this);"
				action="${baseUrl }/edu3/learning/interactive/coursenotice/list.html"
				method="post">
				<div class="searchBar">
					<ul class="searchContent">
						<li><label>年度：</label>
						<gh:selectModel name="yearInfoId" bindValue="resourceid"
								displayValue="yearName"
								modelClass="com.hnjk.edu.basedata.model.YearInfo"
								value="${condition['yearInfoId']}" orderBy="firstYear desc" /> <input
							type="hidden" name="courseId" value="${condition['courseId'] }" />
						</li>
						<li><label>学期：</label>
						<gh:select name="term" value="${condition['term']}"
								dictionaryCode="CodeTerm" /></li>
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
						<th width="2%"></th>
						<th width="12%">年度</th>
						<th width="8%">学期</th>
						<th width="8%">类型</th>
						<th width="40%">通知标题</th>
						<th width="10%">发布人</th>
						<th width="20%">发布时间</th>
					</tr>
				</thead>
				<tbody id="courseNoticeBody">
					<c:forEach items="${courseNoticePage.result}" var="courseNotice"
						varStatus="vs">
						<tr>
							<td></td>
							<td>${courseNotice.yearInfo.yearName }</td>
							<td>${ghfn:dictCode2Val('CodeTerm',courseNotice.term) }</td>
							<td>通知</td>
							<td>
								<%-- <a href="#" onclick="showNotice('${courseNotice.resourceid }','${courseNotice.noticeTitle }');">${courseNotice.noticeTitle }</a> --%>
								<a href="javascript:;"
								onclick="javascript:$.pdialog.open('${baseUrl }/edu3/framework/coursenotice/view.html?courseNoticeId=${courseNotice.resourceid }','courseNotice','${courseNotice.noticeTitle }',{width:500,height:350});">${courseNotice.noticeTitle }</a>
							</td>
							<td>${courseNotice.fillinMan }</td>
							<td>${courseNotice.fillinDate }</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>

			<gh:page page="${courseNoticePage}"
				goPageUrl="${baseUrl }/edu3/learning/interactive/coursenotice/list.html"
				pageType="sys" condition="${condition}" />
		</div>
	</div>

</body>
</html>