<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>选择文章</title>
</head>
<body>
	<script type="text/javascript">	
function selected_article(obj){
			if(obj.checked){
				if($("#${idsN}").val().indexOf(obj.value)<0){//如果默认值为空
					//$("#${idsN}").val("${baseUrl}/portal/site/article/show.html?id="+obj.value);
					$("#${idsN}").val("/portal/site/article/show.html?id="+obj.value);
				}
			}else{
			
			}
	}
	
	function article_selector_search(){
			$.pdialog.reload("${baseUrl}/edu3/framework/chanel/selector/article.html?title="
			+$('#selector_title').val()+
			"&channelId="
			+$('#selector_channelId').val());
	}
</script>
	<div class="page">
		<div class="pageHeader">
			<form onsubmit="return dialogSearch(this);"
				action="${baseUrl }/edu3/framework/chanel/selector/article.html"
				method="post">
				<div class="searchBar">
					<ul class="searchContent">
						<li><label>文章标题：</label> <input type="text"
							id="selector_title" name="title" value="${condition['title']}" />
						</li>
						<li><label>文章栏目：</label> <gh:channel
								channelList="${allChanList }" id="selector_channelId"
								viewType="select" defaultValue="${condition['channelId']}" /></li>
						<input type="hidden" name="idsN" value="${idsN }" />
					</ul>
					<div class="subBar">
						<ul>
							<li><a href="#" class="button"
								onclick="article_selector_search()"><span> 查 询</span> </a></li>
							<li><a href="#" class="button"
								onclick="$.pdialog.closeCurrent();"><span> 确定 </span></a></li>
						</ul>
					</div>
				</div>
			</form>
		</div>
		<div class="pageContent">
			<table class="table" layouth="142">
				<thead>
					<tr>
						<th width="5%"></th>
						<th width="50%">文章标题</th>
						<th width="15%">栏目</th>
						<th width="15%">撰稿人</th>
						<th width="15%">撰稿时间</th>
					</tr>
				</thead>
				<tbody id="selector_articleBody">
					<c:forEach items="${articleList.result}" var="article"
						varStatus="vs">
						<tr>
							<td><input onclick="selected_article(this)" type="radio"
								name="resourceid" value="${article.resourceid }"
								autocomplete="off" /></td>
							<td>${article.title }</td>
							<td>${article.channel.channelName }</td>
							<td>${article.fillinMan }</td>
							<td><fmt:formatDate value="${article.fillinDate }"
									pattern="yyyy-MM-dd" /></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>

			<gh:page page="${articleList}"
				goPageUrl="${baseUrl }/edu3/framework/chanel/selector/article.html"
				pageType="sys" targetType="dialog" condition="${condition }" />
		</div>
	</div>

</body>
</html>