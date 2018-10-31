<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>课程公告管理</title>
<style>
#nostyle td {
	height: 8px
}
</style>
<script type="text/javascript">  
	$(document).ready(function(){
		$("#coursenotice_form_classid").addClass("required");
	});
		$(function() {	  		
	  		KE.init({//初始化编辑器
		      id : 'noticeContent',	         
		      imageUploadJson:baseUrl+'/edu3/framework/filemanage/editor/upload.html?textAreaId=noticeContent&storePath=users,${storeDir},images',//一定要跟storePath参数，指定文件存储的位置	       
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
	<h2 class="contentTitle">编辑课程公告</h2>
	<div class="page">
		<div class="pageContent">
			<form method="post"
				action="${baseUrl}/edu3/teaching/coursenotice/save.html"
				class="pageForm" onsubmit="return validateCallback(this);">
				<input type="hidden" name="resourceid"
					value="${courseNotice.resourceid }" />
				<div class="pageFormContent" layoutH="97">
					<table class="form">
						<tr>
							<td>所属课程:</td>
							<td colspan="3">
								<%-- <input type="text" size="30" style="width:50%" value="${courseNotice.course.courseName }" readonly="readonly"/>
									<input type="hidden" name="courseId" value="${courseNotice.course.resourceid }" />  --%>
								<gh:courseAutocomplete name="courseId" tabindex="3"
									id="courseNotice_form_courseId" displayType="code"
									isFilterTeacher="Y" value="${courseNotice.course.resourceid }"
									classCss="required" style="width:50%;" />
							</td>
							<%-- 
					<td>班级:</td>
					<td><gh:classesAutocomplete name="classesId" id="coursenotice_form_classid" tabindex="1" displayType="code" defaultValue="${courseNotice.classes.resourceid}" exCondition="${classesCondition}" style="width:50%;"></gh:classesAutocomplete></td>
					--%>
						</tr>
						<tr>
							<td width="15%">年度名称:</td>
							<td width="35%"><gh:selectModel name="yearInfoId"
									bindValue="resourceid" displayValue="yearName"
									condition="firstYear<=${year},firstYear>=${startYear}"
									modelClass="com.hnjk.edu.basedata.model.YearInfo"
									value="${courseNotice.yearInfo.resourceid}"
									orderBy="firstYear desc" style="width:70%" classCss="required" />
								<span style="color: red;">*</span></td>
							<td width="15%">学期:</td>
							<td width="35%"><gh:select name="term"
									value="${courseNotice.term}" dictionaryCode="CodeTerm"
									style="width:70%" classCss="required" /><span
								style="color: red;">*</span></td>
						</tr>
						<tr>
							<td>通知标题:</td>
							<td colspan="3"><input type="text" size="30"
								name="noticeTitle" value="${courseNotice.noticeTitle }"
								style="width: 50%" class="required" /></td>
						</tr>
						<tr>
							<td>通知内容:</td>
							<td id="nostyle" colspan="3"><textarea name="noticeContent"
									id="noticeContent" rows="5" cols=""
									style="width: 700px; height: 400px; visibility: hidden;"
									class="required">${courseNotice.noticeContent }</textarea></td>
						</tr>
						<tr>
							<td>附件:</td>
							<td colspan="3"><gh:upload hiddenInputName="uploadfileid"
									baseStorePath="users,${storeDir},attachs" fileSize="10485760"
									uploadType="attach" attachList="${courseNotice.attachs}" />
								<font color="green">(单个文件上传大小不能大于10M)</font></td>
						</tr>
					</table>
				</div>
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
										onclick="navTab.closeCurrentTab();">取消</button>
								</div>
							</div></li>
					</ul>
				</div>
			</form>
		</div>
	</div>
	<script type="text/javascript">
	 	 //创建编辑器
	 	 function createKE(){ 
		     KE.create('noticeContent');
	     } 	     
	     window.setTimeout(createKE,1000);
	</script>
</body>
</html>