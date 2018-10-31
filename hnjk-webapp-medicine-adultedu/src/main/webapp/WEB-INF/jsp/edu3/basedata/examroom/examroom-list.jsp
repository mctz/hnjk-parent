<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>考场教室</title>
<script type="text/javascript">
	//新增
	function addExamroom(){
		navTab.openTab('navTab', '${baseUrl}/edu3/sysmanager/examroom/edit.html', '新增考室');
	}
	
	//修改
	function modifyExamroom(){
		var url = "${baseUrl}/edu3/sysmanager/examroom/edit.html";
		if(isCheckOnlyone('resourceid','#examBody')){
			navTab.openTab('_blank', url+'?resourceid='+$("#examBody input[@name='resourceid']:checked").val(), '编辑考室');
		}			
	}
		
	//删除
	function deleteExamroom(){	
		pageBarHandle("您确定要删除这些记录吗？","${baseUrl}/edu3/sysmanager/examroom/delete.html","#examBody");
	}
</script>
</head>
<body>
	<div class="page">
		<div class="pageHeader">
			<form onsubmit="return navTabSearch(this);"
				action="${baseUrl }/edu3/sysmanager/examroom/list.html"
				method="post">
				<div class="searchBar">
					<ul class="searchContent">
						<li><label>考室名称：</label><input type="text"
							name="examroomName" value="${condition['examroomName']}" /></li>
						<c:if test="${not brschool }">
							<li><label>教学站：</label> <gh:brSchoolAutocomplete
									name="schoolId" tabindex="1" id="examroom_brSchoolId"
									defaultValue="${condition['schoolId']}" /></li>
							<!-- 
				<li>
					<label>是否机房：</label>
					<select name="isComputerRoom">
						<option value="">请选择</option>
						<option value="Y" <c:if test="${condition['isComputerRoom'] eq 'Y'}">selected</c:if>>是</option>
						<option value="N" <c:if test="${condition['isComputerRoom'] eq 'N'}">selected</c:if>>否</option>
					</select>
				</li>
				 -->
						</c:if>
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
			<gh:resAuth parentCode="RES_BASEDATA_EXAMROOM" pageType="list"></gh:resAuth>
			<table class="table" layouth="138">
				<thead>
					<tr>
						<th width="5%"><input type="checkbox" name="checkall"
							id="check_all_exam"
							onclick="checkboxAll('#check_all_exam','resourceid','#examBody')" /></th>
						<th width="20%">考室名称</th>
						<th width="20%">考室容量</th>
						<th width="20%">所属教学站</th>
						<th width="15%">单隔位容量</th>
						<th width="15%">双隔位容量</th>
						<th width="5%">是否机房</th>
					</tr>
				</thead>
				<tbody id="examBody">
					<c:forEach items="${examroomList.result}" var="exam" varStatus="vs">
						<tr>
							<td><input type="checkbox" name="resourceid"
								value="${exam.resourceid }" autocomplete="off" /></td>
							<td align="left">${exam.examroomName }</td>
							<td align="left">${exam.examroomSize }</td>
							<td align="left">${exam.branchSchool }</td>
							<td align="left">${exam.singleSeatNum }</td>
							<td align="left">${exam.doubleSeatNum }</td>
							<td align="left"><c:if test="${exam.isComputerRoom=='Y'}">是</c:if>
								<c:if test="${exam.isComputerRoom!='Y'}">否</c:if></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<gh:page page="${examroomList}"
				goPageUrl="${baseUrl }/edu3/sysmanager/examroom/list.html"
				pageType="sys" condition="${condition}" />
		</div>
	</div>

</body>
</html>