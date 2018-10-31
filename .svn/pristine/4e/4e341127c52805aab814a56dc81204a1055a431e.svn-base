<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>课程素材资源管理</title>
<script type="text/javascript">
	//新增素材
	function addCourseware(){
		var syllabusId = "${condition['syllabusId']}";
		var courseId = "${condition['courseId']}";
		mateAddHandle(syllabusId,courseId,"1",'${resultList.pageNum}','${resultList.pageSize}');	
 	}
 	//编辑素材
 	function modifyCourseware(){
 		var syllabusId = "${condition['syllabusId']}"; 		
		var courseId = "${condition['courseId']}";
		mateModifyHandle(syllabusId,courseId,"#materesourceBody","1",'${resultList.pageNum}','${resultList.pageSize}');					
	}
	
	//删除
	function deleteCourseware(){
		var syllabusId = "${condition['syllabusId']}";
		var courseId = "${condition['courseId']}";
		var postUrl = "${baseUrl}/edu3/metares/courseware/remove.html?syllabusId="+syllabusId+"&courseId="+courseId;
		mateRemoveHandle("您确定要删除这些记录吗？",postUrl,"#materesourceBody",myMateAjaxDone);
	}
	
	//预览素材
	function viewCourseware(){
		var courseId = "${condition['courseId']}";
		var isQualityResource = "${course.isQualityResource}";
		var url = "${baseUrl }/edu3/learning/interactive/main.html?courseId="+courseId;	
		if(isQualityResource=='Y'){
			url = "${baseUrl }/resource/course/index.html?courseId="+courseId;	
		} 
		window.open(url,"interactive");
	}
	
	//检查唯一性
	function validateOnlyMateCode(){
 		var mateCode = $("#materesourceEditForm input[name='mateCode']");	
    	if(mateCode.val()==""){ alertMsg.warn("请输入材料编码"); mateCode.focus();return false; }
    	var url = "${baseUrl}/edu3/metares/courseware/validateCode.html";
    	$.post(url,{mateCode:mateCode.val()},function(existsCode){
    		if(existsCode){ alertMsg.warn("编码已存在!");mateCode.addClass("error");  }else{ alertMsg.correct("恭喜，此编码可用！");mateCode.removeClass("error");}
    	},"json");
    }
    
    //预览mateUrl
    function preView(name){
    	var url ="${baseUrl }/edu3/metares/courseware/view.html";
    	var mateType = $("#materesourceEditForm select[name='mateType']").val();
    	var mateUrl = $("#materesourceEditForm input[name='"+name+"']").val();
    	if($.trim(mateUrl)==""){
    		alertMsg.warn("请输入URL路径");
    	} else if(!$.trim(mateUrl).isURL()){
    		alertMsg.warn("URL路径不合法！");
    	} else {
    		if(mateType==""){
    			alertMsg.warn("请指定材料类型！");
    		} else {
    			$.pdialog.open(url+"?mateUrl="+mateUrl+"&mateType="+mateType,'preView','预览',{mask:true,height:450,width:500});
    		}
    	}
    }
    
</script>
</head>
<body>
	<div class="page">
		<div class="pageContent">
			<gh:resAuth parentCode="RES_METARES_COURSEWARE_MANAGE"
				pageType="sublist"></gh:resAuth>
			<table class="table" layouth="25%" id="materesourceTable">
				<thead>
					<tr>
						<th width="10%"><input type="checkbox" name="checkall"
							id="check_all_materesource"
							onclick="checkboxAll('#check_all_materesource','resourceid','#materesourceBody')" /></th>
						<th width="15%">所属课程知识节点</th>
						<th width="20%">材料类型</th>
						<th width="25%">材料名称</th>
						<th width="20%">URL路径</th>
						<th width="10%">是否发布</th>
					</tr>
				</thead>
				<tbody id="materesourceBody">
					<c:forEach items="${resultList.result }" var="mateRes"
						varStatus="vs">
						<tr>
							<td><input type="checkbox" name="resourceid"
								value="${mateRes.resourceid }" autocomplete="off"
								${(mateRes.resourceid eq mateResource.resourceid)?'checked':'' } />${mateRes.showOrder }</td>
							<td>${mateRes.syllabus.syllabusName }</td>
							<td>${ghfn:dictCode2Val('CodeMateType',mateRes.mateType) }</td>
							<td>${mateRes.mateName }</td>
							<td>${mateRes.mateUrl }</td>
							<td>${ghfn:dictCode2Val('yesOrNo',mateRes.isPublished) }</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<gh:page page="${resultList}"
				goPageUrl="${baseUrl}/edu3/framework/metares/list.html"
				condition="${condition }" targetType="localArea"
				localArea="mateTabContent${condition['currentIndex'] }"
				pageType="sys" />
		</div>

		<c:if
			test="${(not empty condition['syllabusId'])or (not empty mateResource.resourceid)}">
			<div class="pageContent">
				<form id="materesourceEditForm" method="post"
					action="${baseUrl}/edu3/metares/courseware/save.html"
					class="pageForm"
					onsubmit="return validateCallback(this,myMateAjaxDone);">
					<div class="formBar">
						<span style="font-weight: bold;">${(empty mateResource.resourceid)?'新增':'编辑' }课程素材资源</span>
						<ul>
							<li><div class="buttonActive">
									<div class="buttonContent">
										<button type="submit">提交</button>
									</div>
								</div></li>
							<li><div class="button">
									<div class="buttonContent">
										<button type="button" class="close"
											onclick="navTab.closeCurrentTab();">关闭</button>
									</div>
								</div></li>
						</ul>
					</div>
					<input type="hidden" name="resourceid"
						value="${mateResource.resourceid }" /> <input type="hidden"
						name="parentSyllabusId" value="${condition['syllabusId'] }" /> <input
						type="hidden" name="courseId" value="${condition['courseId'] }" />
					<input type="hidden" name="channelType"
						value="${mateResource.channelType }" />
					<div class="pageFormContent" layoutH="35%">
						<table class="form">
							<tr>
								<td style="width: 12%">所属知识节点:</td>
								<td style="width: 88%" colspan="3"><select
									name="syllabusId">
										<c:forEach items="${syllabusList}" var="syb" varStatus="vs">
											<option value="${syb.resourceid }"
												<c:if test="${syb.resourceid eq mateResource.syllabus.resourceid}"> selected </c:if>>
												<c:forEach var="seconds" begin="1"
													end="${syb.syllabusLevel}" step="1">&nbsp;&nbsp;</c:forEach>
												${syb.syllabusName }
											</option>
										</c:forEach>
								</select></td>
							</tr>
							<tr>
								<td>材料类型:</td>
								<td><gh:select name="mateType"
										value="${mateResource.mateType }"
										dictionaryCode="CodeMateType" style="width:128px"
										classCss="required" /><font color="red">*</font></td>
								<td>排序号:</td>
								<td><input type="text" name="showOrder"
									value="${mateResource.showOrder}" class="digits" /></td>
							</tr>
							<tr>
								<td style="width: 12%">材料名称:</td>
								<td style="width: 38%"><input type="text" name="mateName"
									style="width: 50%" value="${mateResource.mateName }"
									class="required" /></td>
								<td style="width: 12%">是否发布:</td>
								<td style="width: 38%"><gh:select name="isPublished"
										value="${mateResource.isPublished }" dictionaryCode="yesOrNo"
										style="width:128px" /></td>
							</tr>
							<tr>
								<td>公网URL路径:</td>
								<td colspan="3"><input type="text" name="mateUrl"
									value="${mateResource.mateUrl }" style="width: 80%"
									class="required url" /> <a href="javascript:;"
									onclick="preView('mateUrl');" class="button"><span>预览</span></a>
								</td>
							</tr>
							<%-- 
				<tr>
					<td>教育网URL路径:</td>
					<td colspan="3"><input type="text" name="mateEdunetUrl" value="${mateResource.mateEdunetUrl }" style="width: 80%" class="required"/>
						<a href="javascript:;" onclick="preView('mateEdunetUrl');" class="button"><span>预览</span></a>
					</td>	
				</tr>		
				 --%>
						</table>
					</div>
				</form>
			</div>
		</c:if>
	</div>
</body>
</html>