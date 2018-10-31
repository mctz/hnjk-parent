<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<title>课程随堂问答提问</title>
<style>
#nostyleMore td {
	height: 8px
}
</style>
<script type="text/javascript">
		$(function() {
	  		KE.init({
		      id : 'topicContentMore',	         
		      imageUploadJson:baseUrl+'/edu3/framework/filemanage/editor/upload.html?textAreaId=topicContentMore&storePath=users,${storeDir},images',//一定要跟storePath参数，指定文件存储的位置	       
		      allowFileManager:true,
		      fileManagerJson:baseUrl+'/edu3/framework/filemanage/editor/browser.html' ,
		      rootPath:'users,${storeDir},images',//默认文件浏览根目录,请使用,号间隔，如abc\d\e\f 则为 abc,d,e,f	
		      afterCreate : function(id) {
				KE.util.focus(id);
			  }  
	  		});	  		   	       	        
    	});    		
		//清空编辑框
		function resetContent(){
			$("#topicMoreTable input").val("");
			KE.text("topicContentMore","");
		}
		//检查合法性
		function validateSubmit(form){
			if($.trim($("#topicMoreTable input[name=title]").val())==""){
				alertMsg.warn("问题不能为空");
				return false;
			}
			if(KE.isEmpty("topicContentMore")){
				alertMsg.warn("内容不能为空");
				return false;
			}
			return validateCallback(form,function(json){
				DWZ.ajaxDone(json);
				if (json.statusCode == 200){
					if (json.questiontype=="more"){
						navTab.reload(null, {},"bbsTopicOnline");
					} 
					
					if(json.from == "sidebar"){
					//NOthing
					}else{
						$("#interactive_tab5").click();
					}
					$.pdialog.closeCurrent();
				}
			});
		}
		
		//找一找
		function queryQuestionForCourse(){
			var keywords = $.trim($("#topicMoreTable input[name=title]").val());
			if(keywords ==""){
				alertMsg.warn("问题不能为空");
				return false;
			}		
			//最小化当前窗口				
			navTab.openTab("bbsTopicOnline",baseUrl+"/edu3/learning/interactive/bbstopic/list.html?type=more&courseId="+$('#query_courseId').val()+"&keywords="+encodeURIComponent(keywords),"随堂问答");
			//$("div.shadow").hide();
			//$("a.minimize",$.pdialog._current).click();
			
		}
		
	</script>
</head>
<body>
	<div>
		<div>
			<table style="line-height: 20px; width: 100%;">
				<tr>
					<td>
						<h2 class="contentTitle">
							<c:choose>
								<c:when test="${ not empty syllabus.course.resourceid }">${syllabus.course.courseName }</c:when>
								<c:otherwise>${course.courseName }</c:otherwise>
							</c:choose>
							- ${ syllabus.syllabusName}
						</h2>
						<div>
							<div style="line-height: 170%; color: green">提示：提问题前，您可以先找一找历史的问题，可以节省不少时间.</div>
							<form method="post" style="width: 100%"
								action="${baseUrl}/edu3/learning/interactive/bbstopic/save.html?from=sidebar"
								style="overflow: hidden;"
								onsubmit="return validateSubmit(this);">
								<input type="hidden" name="courseId" id="query_courseId"
									value="<c:choose>
								<c:when test="${ not empty syllabus.course.resourceid }">${syllabus.course.resourceid }</c:when>
								<c:otherwise>${course.resourceid }</c:otherwise>
							</c:choose>" />

								<input type="hidden" name="syllabusId"
									value="${syllabus.resourceid }" /> <input type="hidden"
									name="questiontype" value="${questiontype }" />
								<div class="">
									<table class="form" id="topicMoreTable">
										<tr>
											<td width="10%">你的问题:</td>
											<td width="90%">
												<table border="0" width="100%">
													<tr>
														<td width="70%"><input type="text" name="title"
															style="width: 90%" value="" class="textInput" /></td>
														<td width="30%"><span class="buttonActive"
															style="margin-left: 8px;"><div
																	class="buttonContent">
																	<button type="button"
																		onclick="queryQuestionForCourse();">找一找</button>
																</div></span></td>
													</tr>
												</table>
											</td>
										</tr>
										<tr>
											<td>内容:</td>
											<td id="nostyleMore"><textarea id="topicContentMore"
													name="content" rows="5" cols=""
													style="width: 600px; height: 320px; visibility: hidden;"></textarea></td>
										</tr>
										<tr>
											<td colspan="2"><span class="buttonActive"
												style="margin-right: 8px"><div class="buttonContent">
														<button type="submit">发表提问</button>
													</div></span> <span class="buttonActive" style="margin-left: 8px"><div
														class="buttonContent">
														<button type="reset" onclick="resetContent()">清空内容</button>
													</div></span></td>
										</tr>
									</table>
								</div>
							</form>
						</div>
					</td>
				</tr>
			</table>
		</div>
		<div>
			<script type="text/javascript">	 
 	 function loadEditor(){
	     KE.create('topicContentMore'); 		    
     } 	        
     window.setTimeout(loadEditor,1000);
</script>
</body>
</html>
