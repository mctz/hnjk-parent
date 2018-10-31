<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>专业设置</title>
<script type="text/javascript">
	//新建
	function addMajor(){
		navTab.openTab('navTab', '${baseUrl}/edu3/sysmanager/major/edit.html', '新增专业');
	}
	
	//修改
	function modifyMajor(){
		var url = "${baseUrl}/edu3/sysmanager/major/edit.html";
		if(isCheckOnlyone('resourceid','#yxy_majorBody')){
			navTab.openTab('_blank', url+'?resourceid='+$("#yxy_majorBody input[@name='resourceid']:checked").val(), '编辑专业');
		}			
	}
		
	//删除
	function deleteMajor(){	
		pageBarHandle("您确定要删除这些记录吗？","${baseUrl}/edu3/sysmanager/major/delete.html","#yxy_majorBody");
	}
	//国家专业管理
	function listNationMajor(){
		navTab.openTab('RES_BASEDATA_NATIONMAJOR', '${baseUrl}/edu3/sysmanager/nationmajor/list.html', '国家专业设置');
	}
</script>
</head>
<body>

	<div class="page">
		<div class="pageHeader">
			<form onsubmit="return navTabSearch(this);"
				action="${baseUrl }/edu3/sysmanager/major/list.html" method="post">
				<div class="searchBar">
					<ul class="searchContent">
						<li><label>专业名称：</label><input type="text" name="majorName"
							value="${condition['majorName']}" /></li>
						<li><label>专业编码：</label><input type="text" name="majorCode"
							value="${condition['majorCode']}" /></li>
						<li>
							<label>外语类：</label>
							<gh:select name="isForeignLng" value="${condition['isForeignLng']}" dictionaryCode="yesOrNo" />
						</li>
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
			<gh:resAuth parentCode="RES_BASEDATA_MAJOR" pageType="list"></gh:resAuth>
			<table class="table" layouth="138">
				<thead>
					<tr>
						<th width="5%"><input type="checkbox" name="checkall"
							id="check_all_major"
							onclick="checkboxAll('#check_all_major','resourceid','#yxy_majorBody')" /></th>
						<th width="8%">专业编号</th>
						<th width="17%">专业名称</th>
						<th width="10%">英文名称</th>
						<th width="10%">专业类别</th>
						<th width="10%">所属层次</th>
						<th width="10%">是否外语类</th>
						<th width="10%">国家专业代码/名称</th>
						<th width="10%">教学计划课程图片</th>
						<th width="10%">教材目录图片</th>
					</tr>
				</thead>
				<tbody id="yxy_majorBody">
					<c:forEach items="${majorList.result}" var="major" varStatus="vs">
						<tr>
							<td><input type="checkbox" name="resourceid"
								value="${major.resourceid }" autocomplete="off" /></td>
							<td>${major.majorCode }</td>
							<td>${major.majorName }</td>
							<td>${major.majorEnName }</td>
							<td>${ghfn:dictCode2Val('CodeMajorClass',major.majorClass) }</td>
							<td>${major.classicName }</td>
							<td>${ghfn:dictCode2Val('yesOrNo',major.isForeignLng) }</td>
							<td>${major.nationMajor.nationMajorCode }&nbsp;<c:if
									test="${not empty major.nationMajor }">/</c:if>&nbsp;${major.nationMajor.nationMajorName}
							</td>
							<td><c:choose>
									<c:when test="${ not empty major.picture4Course}"><a href="${baseUrl }/edu3/sysmanager/pictureViewPlancourse.html?resourceid=${major.resourceid }"
								target="dialog" title="查看图片" width="1000" height="800">查看图片</a></c:when>
									<c:otherwise>未上传图片</c:otherwise>
								</c:choose></td>
								<td><c:choose>
									<c:when test="${ not empty major.picture4TextBook}"><a href="${baseUrl }/edu3/sysmanager/pictureViewTextbook.html?resourceid=${major.resourceid }"
								target="dialog" title="查看图片" width="1000" height="800">查看图片</a></c:when>
									<c:otherwise>未上传图片</c:otherwise>
								</c:choose></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<gh:page page="${majorList}"
				goPageUrl="${baseUrl }/edu3/sysmanager/major/list.html"
				pageType="sys" condition="${condition}" />
		</div>
	</div>

</body>
</html>