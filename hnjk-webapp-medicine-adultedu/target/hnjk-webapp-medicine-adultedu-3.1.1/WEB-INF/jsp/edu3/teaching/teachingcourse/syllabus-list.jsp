<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>知识结构树管理</title>
<gh:loadCom components="treeView" />
<script type="text/javascript">
	$(document).ready(function(){
		$("#_syllabustree").treeview({
			persist: "location",			
			unique: true
		});		
		if($('._syllabusLeftTree')){//如果有左侧树，则赋值为自适应高度		
			$('._syllabusLeftTree').height($("#container .tabsPageContent").height()-5);
		}
		
		var sid = $("#parentsyllabusId").val();
		$("#_syllabustree .itemtext").find("a").removeClass("selected");
		if(sid!=null&&sid!=""){
			$("#_syllabustree span[nodeid='"+sid+"']").find("a").addClass("selected");
		} else {
			$("#_syllabustree .itemtext:first").find("a").addClass("selected");
		}
	});
	//新增
	function addSyllabusNode(){
	 	//var url = "${baseUrl}/edu3/teaching/syllabus/input.html";
	 	var url = "${baseUrl}/edu3/teaching/teachingcourse/addsyllabus.html";
	 	var syllabusId = $('#parentsyllabusId').val();
	 	var courseId = $('#currentcourseId').val();
	 	if(syllabusId == ""){
	 		alertMsg.warn("请选择左边的某个节点！");
	 		return false;
	 	}
	 	navTab.openTab('syllabusList', url+'?courseId='+courseId+'&parentSyllabusId='+syllabusId, '建立知识结构树');
 	}
 	//编辑
 	function modifySyllabusNode(){
		//var url = "${baseUrl}/edu3/teaching/syllabus/input.html";
		var courseId = $('#currentcourseId').val();
		var url = "${baseUrl}/edu3/teaching/teachingcourse/addsyllabus.html";
		if(isCheckOnlyone('resourceid','#syllabusBody')){
			navTab.openTab('syllabusList', url+'?resourceid='+$("#syllabusBody input[@name='resourceid']:checked").val()+'&courseId='+courseId, '建立知识结构树');
		}			
	}	
	//删除
	function deleteSyllabusNode(){	
		pageBarHandle("您确定要删除这些记录吗？","${baseUrl}/edu3/teaching/syllabus/delete.html","#syllabusBody");	
	}
	//左边树的链接
	function goSyllabusSelected(courseId,syllabusId){
		var url = "${baseUrl}/edu3/teaching/teachingcourse/addsyllabus.html";
		navTab.openTab('syllabusList', url+'?courseId='+courseId+'&syllabusId='+syllabusId,'建立知识结构树');
	}
	
	//学习目标
	function listCourseLearningGuid(){
	 	var url = "${baseUrl}/edu3/teaching/courselearningguid/list.html";	 	
	 	if(isCheckOnlyone('resourceid','#syllabusBody')){
	 		if($("#syllabusBody input[@name='resourceid']:checked").attr("rel")!='1'){
	 			alertMsg.warn("学习目标只能加到每一章！");
	 		} else {
	 			navTab.openTab('courseLearningGuid', url+'?syllabusId='+$("#syllabusBody input[@name='resourceid']:checked").val(), '学习目标管理');
	 		}			
		}
 	}
	
</script>
</head>
<body>
	<div style="float: left; width: 19%">
		<div class="_syllabusLeftTree"
			style="display: block; overflow: auto; padding-bottom: 6px; border: solid 1px #CCC; line-height: 21px;">
			<gh:treeview nodes="${syllabustree}" id="_syllabustree" css="folder"
				expandLevel="100" />
		</div>
	</div>

	<div class="page" style="float: left; width: 81%">
		<input type="hidden" id="parentsyllabusId" name="syllabusId"
			value="${condition['syllabusId'] }"> <input type="hidden"
			id="currentcourseId" name="courseId"
			value="${condition['courseId'] }">
		<div class="pageContent">
			<gh:resAuth parentCode="RES_METARES_COURSEWARE_MANAGE"
				pageType="sylist"></gh:resAuth>
			<table class="table" layoutH="15%">
				<thead>
					<tr>
						<th width="5%"><input type="checkbox" name="checkall"
							id="check_all_syllabus"
							onclick="checkboxAll('#check_all_syllabus','resourceid','#syllabusBody')" /></th>
						<%-- <th width="10%">课程名称</th> --%>
						<th width="30%">节点名称</th>
						<th width="10%">节点类型</th>
						<th width="25%">节点内容</th>
						<%-- 
               		<th width="10%">教学要求度</th>
               		<th width="10%">能力目标</th> 
               		--%>
						<th width="10%">学时分配</th>
						<th width="20%">备注</th>
					</tr>
				</thead>
				<tbody id="syllabusBody">
					<c:forEach items="${syllabuslist.result }" var="syb" varStatus="vs">
						<tr>
							<td><c:if test="${syb.syllabusLevel != 0 }">
									<input type="checkbox" name="resourceid"
										value="${syb.resourceid }" rel="${syb.syllabusLevel }"
										autocomplete="off"
										${(syb.resourceid eq syllabus.resourceid)?'checked':'' } />
								</c:if></td>
							<%-- <td>${syb.course.courseName }</td> --%>
							<td>${syb.syllabusName }</td>
							<td>${ghfn:dictCode2Val('CodeTeachingNodeType',syb.syllabusType) }</td>
							<td>${syb.syllabusContent }</td>
							<%-- 
			   			<td>${ghfn:dictCode2Val('CodeTeachingRequest',syb.required) }</td>
			   			<td>${ghfn:dictCode2Val('CodeTeachingAbilityTarget',syb.abilityTarget) }</td> 
			   			--%>
							<td>${syb.provideStydyHour }</td>
							<td>${syb.memo }</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<gh:page page="${syllabuslist}"
				goPageUrl="${baseUrl}/edu3/teaching/teachingcourse/addsyllabus.html"
				condition="${condition }" pageType="sys" />

			<c:if test="${(not empty syllabus) and (not empty syllabus.parent) }">
				<form method="post"
					action="${baseUrl}/edu3/teaching/syllabus/save.html"
					id="syllabusForm" class="pageForm"
					onsubmit="return validateCallback(this);">
					<div class="formBar">
						<span style="font-weight: bold;">${(empty syllabus.resourceid)?'新增':'编辑' }知识节点</span>
						<ul>
							<li><div class="buttonActive">
									<div class="buttonContent">
										<button type="submit">提交</button>
									</div>
								</div></li>
							<li><div class="button">
									<div class="buttonContent">
										<button type="button" class="close"
											onclick="navTab.closeCurrentTab();">取消</button>
									</div>
								</div></li>
						</ul>
					</div>
					<input type="hidden" name="resourceid"
						value="${syllabus.resourceid }" />
					<div class="pageFormContent" layoutH="55%">
						<table class="form">
							<tr>
								<td style="width: 10%">课程名称:</td>
								<td style="width: 40%"><input type="text"
									style="width: 50%" value="${syllabus.course.courseName }"
									readonly="readonly" /> <input type="hidden" name="courseId"
									value="${syllabus.course.resourceid }" /></td>
								<td>父节点:</td>
								<td><select name="parentId" class="required">
										<c:forEach items="${syllabusList}" var="syb" varStatus="vs">
											<option value="${syb.resourceid }"
												<c:if test="${syb.resourceid eq syllabus.parent.resourceid}"> selected </c:if>>
												<c:forEach var="seconds" begin="1"
													end="${syb.syllabusLevel}" step="1">&nbsp;&nbsp;</c:forEach>
												${syb.syllabusName }
											</option>
										</c:forEach>
								</select> <font color="red">*</font></td>
							</tr>
							<tr>
								<td style="width: 10%">知识节点名称:</td>
								<td style="width: 40%"><input type="text"
									name="syllabusName" style="width: 50%"
									value="${syllabus.syllabusName }" class="required" /></td>
								<td>节点类型:</td>
								<td><gh:select name="syllabusType"
										value="${syllabus.syllabusType}"
										dictionaryCode="CodeTeachingNodeType" classCss="required" /> <font
									color="red">*</font></td>

							</tr>
							<%-- 
				<tr>
					<td>教学要求度:</td>
					<td><gh:select name="required" value="${syllabus.required}" dictionaryCode="CodeTeachingRequest" style="width:52%"/></td>
					<td>能力目标:</td>
					<td><gh:select name="abilityTarget" value="${syllabus.abilityTarget}" dictionaryCode="CodeTeachingAbilityTarget"/></td>
				</tr>
				 --%>
							<tr>
								<td>学时分配:</td>
								<td><input type="text" name="provideStydyHour"
									style="width: 50%" value="${syllabus.provideStydyHour }"
									class="number" /></td>
								<td>排序号:</td>
								<td><input type="text" name="showOrder"
									value="${syllabus.showOrder }" class="number" /></td>
							</tr>
							<tr>
								<td>节点内容:</td>
								<td colspan="3"><textarea name="syllabusContent" rows="5"
										cols="" style="width: 50%">${syllabus.syllabusContent }</textarea></td>
							</tr>
							<tr>
								<td>备注:</td>
								<td colspan="3"><textarea name="memo" rows="5" cols=""
										style="width: 50%">${syllabus.memo }</textarea></td>
							</tr>
						</table>
					</div>
				</form>
		</div>
		</c:if>
	</div>
</body>
</html>