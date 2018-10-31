<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>课程概况管理</title>
<style>
#nostyle td {
	height: 8px
}
</style>
<script type="text/javascript">
	//新增
	function addCourseOverview(){
		//navTab.openTab('RES_TEACHING_COURSEOVERVIEW', '${baseUrl}/edu3/teaching/courseoverview/input.html?courseId='+$("#courseOverviewForm input[name='courseId']").val(), '新增课程概况');
		navTab.openTab('courseOverview', '${baseUrl }/edu3/teaching/courseoverview/list.html?courseId='+$("#courseOverviewCourseId").val(), '课程概况管理');
	}
	
	//修改
	function modifyCourseOverview(){
		var url = "${baseUrl }/edu3/teaching/courseoverview/list.html";
		if(isCheckOnlyone('resourceid','#courseOverviewBody')){
			navTab.openTab('courseOverview', url+'?resourceid='+$("#courseOverviewBody input[@name='resourceid']:checked").val()+"&courseId="+$("#courseOverviewCourseId").val(), '课程概况管理');
		}			
	}
		
	//删除
	function deleteCourseOverview(){	
		pageBarHandle("您确定要删除这些记录吗？","${baseUrl}/edu3/teaching/courseoverview/delete.html","#courseOverviewBody");
	}
	//
	function editCourseOverview(res,courseId){
		var url = "${baseUrl }/edu3/teaching/courseoverview/list.html";
		navTab.openTab('courseOverview', url+'?resourceid='+res+"&courseId="+courseId, '课程概况管理');
	}
	
	function viewCourseOverview(){
		var courseId = $("#courseOverviewCourseId").val();
		var url = "${baseUrl }/edu3/learning/interactive/main.html?courseId="+courseId;	
		var isQualityResource = "${course.isQualityResource}";
		if(isQualityResource=='Y'){
			url = "${baseUrl }/resource/course/index.html?courseId="+courseId;	
		} 
		window.open(url,"interactive");
	}
	
	$(function() {	
  		KE.init({
	      id : 'overviewContent',	         
	      imageUploadJson:baseUrl+'/edu3/framework/filemanage/editor/upload.html?textAreaId=overviewContent&storePath=users,${storeDir},images',//一定要跟storePath参数，指定文件存储的位置	       
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
			<input type="hidden" id="courseOverviewCourseId"
				value="${condition['courseId']}" />
			<gh:resAuth parentCode="RES_METARES_COURSEWARE_MANAGE"
				pageType="cvlist"></gh:resAuth>
			<table class="table" layouth="20%">
				<thead>
					<tr>
						<th width="10%"><input type="checkbox" name="checkall"
							id="check_all_courseoverview"
							onclick="checkboxAll('#check_all_courseoverview','resourceid','#courseOverviewBody')" /></th>
						<th width="20%">所属课程</th>
						<th width="30%">类型</th>
						<th width="20%">填写人</th>
						<th width="20%">填写日期</th>
					</tr>
				</thead>
				<tbody id="courseOverviewBody">
					<c:forEach items="${courseOverviewPage.result}" var="overview"
						varStatus="vs">
						<tr>
							<td><input type="checkbox" name="resourceid"
								value="${overview.resourceid }" autocomplete="off"
								${(courseOverview.resourceid eq overview.resourceid)?'checked':'' } /></td>
							<td>${overview.course.courseName}</td>
							<td><a
								onclick="editCourseOverview('${overview.resourceid }','${overview.course.resourceid }');"
								href="javascript:;">${ghfn:dictCode2Val('CodeCourseOverviewType',overview.type) }</a></td>
							<td>${overview.fillinMan }</td>
							<td>${overview.fillinDate }</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>

			<gh:page page="${courseOverviewPage}"
				goPageUrl="${baseUrl }/edu3/teaching/courseoverview/list.html"
				pageType="sys" condition="${condition}" />
		</div>

		<h2 class="contentTitle">${(empty courseOverview.resourceid)?'新增':'编辑' }课程概况</h2>
		<div class="pageContent">
			<form method="post"
				action="${baseUrl}/edu3/teaching/courseoverview/save.html"
				class="pageForm" onsubmit="return validateCallback(this);">
				<div class="formBar">
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
					value="${courseOverview.resourceid }" />
				<div class="pageFormContent" layoutH="55%">
					<table class="form">
						<tr>
							<td width="10%">所属课程:</td>
							<td><input type="text" size="30" style="width: 50%"
								value="${courseOverview.course.courseName }" readonly="readonly" />
								<input type="hidden" name="courseId"
								value="${courseOverview.course.resourceid }" /></td>
						</tr>
						<tr>
							<td>类型:</td>
							<td><gh:select name="type" value="${courseOverview.type}"
									dictionaryCode="CodeCourseOverviewType" style="width:135px"
									classCss="required" /><span style="color: red;">*</span><span
								style="color: green;">(一个类型只能创建一项)</span></td>
						</tr>
						<tr>
							<td>内容:</td>
							<td id="nostyle"><textarea name="content"
									id="overviewContent" rows="5" cols=""
									style="width: 100%; height: 400px; visibility: hidden;"
									class="required">${courseOverview.content }</textarea></td>
						</tr>
					</table>
				</div>
			</form>
		</div>
		<script type="text/javascript">
	 	 //创建编辑器
	 	 function loadEditor(){ 
		     KE.create('overviewContent');
	     } 	     
	     window.setTimeout(loadEditor,1000);
	</script>
	</div>

</body>
</html>