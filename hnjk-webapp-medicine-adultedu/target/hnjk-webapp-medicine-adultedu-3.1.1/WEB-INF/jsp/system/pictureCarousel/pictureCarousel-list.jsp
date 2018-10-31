<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>图片轮播列表</title>
<script type="text/javascript">
	//新增
	function addPictureCarousel(){
		navTab.openTab('navTab', '${baseUrl}/edu3/system/pictureCarousel/edit.html', '图片轮播管理');
	}
	
	//修改
	function modifyPictureCarousel(){
		var url = "${baseUrl}/edu3/system/pictureCarousel/edit.html";
		if(isCheckOnlyone('resourceid','#pictureCarouselBody')){
			navTab.openTab('_blank', url+'?resourceid='+$("#pictureCarouselBody input[@name='resourceid']:checked").val(), '图片轮播管理');
		}			
	}
		
	//删除
	function deletePictureCarousel(){
		pageBarHandle("您确定要删除这些记录吗？","${baseUrl}/edu3/system/pictureCarousel/delete.html","#pictureCarouselBody");
	}
	
</script>
</head>
<body>

	<div class="page">
		<div class="pageHeader">
			<form onsubmit="return navTabSearch(this);"
				action="${baseUrl }/edu3/system/pictureCarousel/list.html"
				method="post">
				<div class="searchBar">
					<ul class="searchContent">
						<li><label>图片名：</label> <input type="text" name="picName"
							style="width: 52%" value="${condition['picName'] }" /></li>
						<li><label>是否显示：</label> <select name="isShow" id="isShow">
								<option value=""
									<c:if test="${empty condition['isShow']}">selected='selected'</c:if>>请选择</option>
								<option value="Y"
									<c:if test="${condition['isShow'] eq 'Y'}">selected='selected'</c:if>>显示</option>
								<option value="N"
									<c:if test="${condition['isShow'] eq 'N'}">selected='selected'</c:if>>不显示</option>
						</select></li>
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
			<gh:resAuth parentCode="RES_SYS_PICTURECAROUSEL" pageType="list"></gh:resAuth>
			<table class="table" layouth="138">
				<thead>
					<tr>
						<th width="5%"><input type="checkbox" name="checkall"
							id="check_all_pictureCarousel"
							onclick="checkboxAll('#check_all_pictureCarousel','resourceid','#pictureCarouselBody')" /></th>
						<th width="15%">图片名</th>
						<th width="20%">图片地址</th>
						<th width="5%">是否显示</th>
						<th width="5%">显示顺序</th>
						<th width="20%">描述</th>
						<th width="15%">创建时间</th>
						<th width="15%">更新时间</th>
					</tr>
				</thead>
				<tbody id="pictureCarouselBody">
					<c:forEach items="${pictureCarouselList.result}"
						var="pictureCarousel" varStatus="vs">
						<tr>
							<td><input type="checkbox" name="resourceid"
								value="${pictureCarousel.resourceid }" autocomplete="off" /></td>
							<td>${pictureCarousel.picName }</td>
							<td>${pictureCarousel.picUrl }</td>
							<td><c:if test="${pictureCarousel.isShow eq 'Y'}">显示</c:if>
								<c:if test="${pictureCarousel.isShow eq 'N'}">不显示</c:if></td>
							<td>${pictureCarousel.showOrder }</td>
							<td>${pictureCarousel.memo }</td>
							<td><fmt:formatDate value="${pictureCarousel.createDate }"
									pattern="yyyy-MM-dd HH:mm:ss" /></td>
							<td><fmt:formatDate value="${pictureCarousel.updateDate }"
									pattern="yyyy-MM-dd HH:mm:ss" /></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<gh:page page="${pictureCarouselList}"
				goPageUrl="${baseUrl }/edu3/system/pictureCarousel/list.html"
				pageType="sys" condition="${condition}" />
		</div>
	</div>

</body>
</html>