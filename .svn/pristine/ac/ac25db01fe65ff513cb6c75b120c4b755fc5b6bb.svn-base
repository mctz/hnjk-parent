<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>讨论小组话题</title>
<style type="text/css">
.nostyle td {
	height: 4px;
}
</style>
<script type="text/javascript">
		$(function() {
	  		KE.init({
		      id : 'bbsgrouptopicContent',	         
		      imageUploadJson:baseUrl+'/edu3/framework/filemanage/editor/upload.html?textAreaId=bbsgrouptopicContent&storePath=users,${storeDir},images',//一定要跟storePath参数，指定文件存储的位置	       
		      allowFileManager:true,
		      fileManagerJson:baseUrl+'/edu3/framework/filemanage/editor/browser.html' ,
		      rootPath:'users,${storeDir},images',//默认文件浏览根目录,请使用,号间隔，如abc\d\e\f 则为 abc,d,e,f	          
		      afterCreate : function(id) {
					KE.util.focus(id);
				}
	  		});	
	  			  		
	  		getCourses();
	  		getGroups('${bbsTopic.course.resourceid }','${bbsTopic.resourceid }');  		
    	});  
    	$("#groupTopicEditForm select[name='courseId']").change(function() {
    	  	var courseId = $(this).val();
    	  	var parenTopicId = $("#groupTopicEditForm input[name='resourceid']").val();
		  	getGroups(courseId,parenTopicId);
		}); 
		//获取课程
		function getCourses(){
			var url = "${baseUrl}/edu3/framework/getCourse.html";
		  	$.post(url,{}, function (json){		  		
		  		var html = "";		  					  		
		  		$(json).each(function(i){  
		  		    var x = json[i]; 
		  		    var c = "";
		  		    if(x.resourceid=='${bbsTopic.course.resourceid }'){
		  		    	c = " selected='selected' ";
		  		    }
		  		    html += "<option "+c+" value='"+x.resourceid+"'>"+x.courseName+"</option>";
		  		});	
		  		$("#groupTopicEditForm select[name='courseId']").append(html);
		  	}, "json");
		}
		//获取课程对应小组
		function getGroups(courseId,parenTopicId){
			var url = "${baseUrl}/edu3/framework/bbs/bbsgrouptopic/getGroup.html";
		  	$.post(url,{courseId:courseId,parenTopicId:parenTopicId}, function (json){
		  		var html = "";		  		
		  		if(json==null||json==""){
		  			html += "<font color='red'>请先分配好小组!</font>";
		  		} else {		  					  		
			  		$(json).each(function(i){  
			  		    var x = json[i]; 
			  		    var c = "";
			  		    if(x[2]=='Y'){
			  		    	c = " checked='checked' disabled='disabled' "
			  		    }
			  		    html += "<input type='checkbox' name='groupIds' "+c+" value='"+x[0]+"' />"+x[1]+"<br/>";
			  		});			  		
		  		}
		  		$("#groupTopicEditForm div[id='groupIds']").html(html);
		  	}, "json");
		} 		
		
		function validateGroupTopicCallback(form) {
			var courseId = $("#groupTopicEditForm select[name='courseId']").val();
			if(courseId==""){
				alertMsg.warn("请选择课程和小组!");
				return false;
			}
			var obj = $("#groupTopicEditForm input[name='groupIds']");
			if(obj.size()==0){
				alertMsg.warn("请先分配好小组!");
				return false;
			} 
			if($("#groupTopicEditForm input[name='groupIds']:checked").size()==0){
				alertMsg.warn("请选择小组!");
				return false;
			}
			return validateCallback(form);
		}
	</script>
</head>
<body>
	<h2 class="contentTitle">${(empty bbsTopic.resourceid)?'新增':'编辑' }小组话题</h2>
	<div class="page">
		<div class="pageContent">
			<form id="groupTopicEditForm" method="post"
				action="${baseUrl}/edu3/framework/bbs/bbsgrouptopic/save.html"
				class="pageForm" onsubmit="return validateGroupTopicCallback(this);">
				<input type="hidden" name="resourceid"
					value="${bbsTopic.resourceid }" />
				<div class="pageFormContent" layoutH="97">
					<table class="form">
						<tr>
							<td>标题:</td>
							<td colspan="3"><input type="text" name="title"
								value="${bbsTopic.title }" style="width: 50%;" class="required" />
							</td>
						</tr>
						<tr class="nostyle">
							<td>内容:</td>
							<td colspan="3"><textarea id="bbsgrouptopicContent"
									name="content" rows="5" cols="" class="required"
									style="width: 700px; height: 400px; visibility: hidden;">${bbsTopic.content }</textarea>
							</td>
						</tr>
						<tr>
							<td width="10%">讨论话题权限:</td>
							<td width="40%"><gh:select name="viewPermiss"
									value="${bbsTopic.viewPermiss}"
									dictionaryCode="CodeViewPermiss" /></td>
							<td width="10%">讨论截止时间:</td>
							<td width="40%"><input type="text" name="endTime"
								value="<fmt:formatDate value='${bbsTopic.endTime}' pattern='yyyy-MM-dd'/>"
								class="required" onFocus="WdatePicker({isShowWeek:true})">
							</td>
						</tr>
						<tr>
							<td>指定课程小组:</td>
							<td colspan="3"><select name="courseId" id="courseId"
								style="width: 120px" class="required"><option value=''>请选择</option></select>
								<div id="groupIds"></div> <span style="color: green;">(先选择课程,再选择一个或多个小组)</span>
							</td>
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
 	 function loadEditor(){
	     KE.create('bbsgrouptopicContent'); 		    
     } 	        
     window.setTimeout(loadEditor,1000);
</script>
</body>
</html>