<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>课程学习目标管理</title>
<style>
#nostyle td {
	height: 8px
}
</style>
<script type="text/javascript">   
	$(function() {	  		
  		KE.init({//初始化编辑器
	      id : 'courseLearningGuidContent',	         
	      imageUploadJson:baseUrl+'/edu3/framework/filemanage/editor/upload.html?textAreaId=courseLearningGuidContent&storePath=users,${storeDir},images',//一定要跟storePath参数，指定文件存储的位置	       
	      allowFileManager:true,
	      fileManagerJson:baseUrl+'/edu3/framework/filemanage/editor/browser.html' ,
	      rootPath:'users,${storeDir},images',//默认文件浏览根目录,请使用,号间隔，如abc\d\e\f 则为 abc,d,e,f	
	       afterCreate : function(id) {
				KE.util.focus(id);
			}          
  		});     	        
    });
</script>
</head>
<body>
	<div class="page">
		<div class="pageContent">
			<gh:resAuth parentCode="RES_METARES_COURSEWARE_MANAGE"
				pageType="cglist"></gh:resAuth>
			<table class="table" layouth="25%">
				<thead>
					<tr>
						<th width="10%"><input type="checkbox" name="checkall"
							id="check_all_courselearningguid"
							onclick="checkboxAll('#check_all_courselearningguid','resourceid','#courseLearningGuidBody')" /></th>
						<th width="20%">所属知识节点</th>
						<th width="30%">类型</th>
						<th width="20%">填写人</th>
						<th width="20%">填写日期</th>
					</tr>
				</thead>
				<tbody id="courseLearningGuidBody">
					<c:forEach items="${resultList.result}" var="guid" varStatus="vs">
						<tr>
							<td><input type="checkbox" name="resourceid"
								value="${guid.resourceid }" autocomplete="off"
								${(guid.resourceid eq courseLearningGuid.resourceid)?'checked':'' } /></td>
							<td>${guid.syllabus.syllabusName}</td>
							<td>${ghfn:dictCode2Val('CodeCourseLearningGuidType',guid.type) }</td>
							<td>${guid.fillinMan }</td>
							<td>${guid.fillinDate }</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<gh:page page="${resultList}"
				goPageUrl="${baseUrl}/edu3/framework/metares/list.html"
				condition="${condition }" targetType="localArea"
				localArea="mateTabContent${condition['currentIndex'] }"
				pageType="sys" />

			<c:if
				test="${(not empty condition['syllabusId'])or (not empty courseLearningGuid.resourceid)}">
				<div class="pageContent">
					<form method="post"
						action="${baseUrl}/edu3/teaching/courselearningguid/save.html"
						class="pageForm"
						onsubmit="return validateCallback(this,myMateAjaxDone);">
						<div class="formBar">
							<span style="font-weight: bold;">${(empty courseReference.resourceid)?'新增':'编辑' }学习目标</span>
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
							value="${courseLearningGuid.resourceid }" /> <input type="hidden"
							name="parentSyllabusId" value="${condition['syllabusId'] }" /> <input
							type="hidden" name="courseId" value="${condition['courseId'] }" />
						<div class="pageFormContent" layoutH="45%">
							<table class="form">
								<tr>
									<td style="width: 12%">所属知识节点:</td>
									<td style="width: 88%" colspan="3"><select
										name="syllabusId" class="required">
											<c:forEach items="${syllabusList}" var="syb" varStatus="vs">
												<option value="${syb.resourceid }"
													<c:if test="${syb.resourceid eq courseLearningGuid.syllabus.resourceid}"> selected </c:if>>
													<c:forEach var="seconds" begin="1"
														end="${syb.syllabusLevel}" step="1">&nbsp;&nbsp;</c:forEach>
													${syb.syllabusName }
												</option>
											</c:forEach>
									</select> <span style="color: red;">*</span></td>
								</tr>
								<tr>
									<td>类型:</td>
									<td><gh:select name="type"
											value="${courseLearningGuid.type}"
											dictionaryCode="CodeCourseLearningGuidType"
											style="width:135px" classCss="required" choose="N" /><span
										style="color: red;">*</span></td>
								</tr>
								<tr>
									<td>内容:</td>
									<td id="nostyle"><textarea name="content"
											id="courseLearningGuidContent" rows="5" cols=""
											style="width: 700px; height: 400px; visibility: hidden;"
											class="required">${courseLearningGuid.content }</textarea></td>
								</tr>
							</table>
						</div>
					</form>
				</div>
			</c:if>
			<script type="text/javascript"> 
		//创建编辑器
		 function loadEditor(){ 
		     KE.create('courseLearningGuidContent');
	    } 	     
	    window.setTimeout(loadEditor,1000);
		//新增
		function addCourseLearningGuid(){
			var syllabusId = "${condition['syllabusId']}";
			var courseId = "${condition['courseId']}";
			mateAddHandle(syllabusId,courseId,"0",'${resultList.pageNum}','${resultList.pageSize}');
		}		
		//修改
		function modifyCourseLearningGuid(){
			var syllabusId = "${condition['syllabusId']}"; 		
			var courseId = "${condition['courseId']}";
			mateModifyHandle(syllabusId,courseId,"#courseLearningGuidBody","0",'${resultList.pageNum}','${resultList.pageSize}');
		}			
		//删除
		function deleteCourseLearningGuid(){	
			var syllabusId = "${condition['syllabusId']}";
			var courseId = "${condition['courseId']}";
			var postUrl = "${baseUrl}/edu3/teaching/courselearningguid/delete.html?syllabusId="+syllabusId+"&courseId="+courseId;
			mateRemoveHandle("您确定要删除这些记录吗？",postUrl,"#courseLearningGuidBody",myMateAjaxDone);
		}			
		
	</script>
		</div>
</body>
</html>