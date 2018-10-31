<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>试题管理</title>
<style>
.nostyle td {
	height: 4px
}
</style>
<script type="text/javascript"> 
	$(function() {
		var ids = ['courseExam_question','courseExam_answer','courseExam_parser'];
		$("#unitCourseExamChildsForm [id^='unit_question']").each(function (){
			ids.push(this.id);
		});
		for(var i in ids){
			KE.init({//初始化编辑器
		      id : ids[i],	         
		      imageUploadJson:baseUrl+'/edu3/framework/filemanage/editor/upload.html?textAreaId='+ids[i]+'&storePath=users,${storeDir},images',//一定要跟storePath参数，指定文件存储的位置	       
		      allowFileManager:true,
		      fileManagerJson:baseUrl+'/edu3/framework/filemanage/editor/browser.html' ,
		      rootPath:'users,${storeDir},images',         
		      afterCreate : function(id) {
					KE.util.focus(id);
				}	
	  		});
		}
    });  
	
	function examTypeChange(examType){
		if(examType=='5'){
	    	 KE.create('courseExam_answer');
	    } else {
	    	 KE.remove('courseExam_answer');
	    }
		if(examType=="" || examType=="1" || examType=="2"){
			$("#courseExam_answerOptionNum_tr").show();
		} else {
			$("#courseExam_answerOptionNum_tr").hide();
		}
	}

	
	function relateExercise(type){
		var objForm = $("#relateActiveExamForm");
		var courseId = $("#courseExamForm_CourseId").val();		
		var examType = $("#courseExamEditForm select[name='examType']").val();	
		if(type==0){	
			if(courseId==""){
	   		   alertMsg.warn("请选择一门课程!");
	   		   return false;
			} if(examType=="" || examType=="4" || examType=="5"){
				alertMsg.warn("题型必须为选择题或判断题!");
		   		return false;
			} else {	
				var checkurl = "${baseUrl}/edu3/framework/syllabus/list.html";
				jQuery.post(checkurl,{courseId:courseId},function(json){
					var str = "<option value=''>请选择</option>";
					 for(var i in json){
						 var spaces = "";
						 for(var k=1;k<=json[i].syllabusLevel;k++){
							 spaces += "&nbsp;&nbsp;";
						 }
						 str += "<option value='"+json[i].resourceid+"'>"+spaces+json[i].syllabusName+"</option>";
					 }
					 $("#courseExamForm_SyllabusId").html(str);
					 objForm.show();
				},"json");
			}						
		} else {
			objForm.hide();
		}
	}
	
	function myFormCallback1(json){		
		DWZ.ajaxDone(json);
		if (json.statusCode == 200){
			if ("closeCurrent" == json.callbackType) {
				setTimeout(function(){navTab.closeCurrentTab();}, 100);
			} else {
				navTab.reload(json.reloadUrl);
			}
		}
	}
</script>
</head>
<body>
	<h2 class="contentTitle">${(empty courseExam.resourceid)?'新增':'编辑' }试题</h2>
	<div class="page">
		<div class="pageContent">
			<form id="courseExamEditForm" method="post"
				action="${baseUrl}/edu3/metares/courseexam/save.html"
				class="pageForm"
				onsubmit="return validateCallback(this,myFormCallback1);">
				<input type="hidden" name="resourceid"
					value="${courseExam.resourceid }" /> <input type="hidden"
					name="isEnrolExam" value="N" /> <input type="hidden"
					name="fillinMan" value="${courseExam.fillinMan }" /> <input
					type="hidden" name="fillinManId" value="${courseExam.fillinManId }" />
				<input type="hidden" name="fillinDate"
					value="<fmt:formatDate value='${courseExam.fillinDate }' pattern='yyyy-MM-dd HH:mm:ss'/>" />
				<div class="pageFormContent" layoutH="97">
					<table class="form">
						<tr>
							<td>课程:</td>
							<td><gh:courseAutocomplete id="courseExamForm_CourseId"
									name="courseId" tabindex="1"
									value="${courseExam.course.resourceid}" style="width:80%;"
									classCss="required" /></td>
							<td width="10%">考试形式:</td>
							<td><gh:select dictionaryCode="CodeExamform"
									classCss="required" name="examform"
									value="${courseExam.examform}"
									filtrationStr="unit_exam,online_exam,final_exam" /> <span
								style="color: red;">*</span></td>
						</tr>
						<tr>
							<td style="width: 10%">题型:</td>
							<td style="width: 40%"><gh:select name="examType"
									value="${courseExam.examType}" style="width: 50%;"
									dictionaryCode="CodeExamType" classCss="required"
									onchange="examTypeChange(this.value)" /><font color="red">*</font></td>
							<td style="width: 10%">类别:</td>
							<td style="width: 40%"><gh:select name="examNodeType"
									value="${courseExam.examNodeType}" style="width: 50%;"
									dictionaryCode="CodeExamNodeType" /></td>
						</tr>
						<tr>
							<td style="width: 10%">难度:</td>
							<td style="width: 40%"><gh:select name="difficult"
									value="${courseExam.difficult}" style="width: 50%;"
									dictionaryCode="CodeExamDifficult" /></td>
							<td>考试要求:</td>
							<td><gh:select name="requirement"
									value="${courseExam.requirement}"
									dictionaryCode="CodeTeachingRequest" style="width: 50%;" /></td>
						</tr>
						<tr>
							<td>关键字:</td>
							<td colspan="3"><input type="text" name="keywords"
								value="${courseExam.keywords}" style="width: 40%;" /><span
								style="color: green;"> (多个关键字使用,号分隔!) </span></td>
						</tr>
						<tr>
							<td>是否在线做题:</td>
							<td colspan="3"><gh:select name="isOnlineAnswer"
									value="${courseExam.isOnlineAnswer}" dictionaryCode="yesOrNo"
									style="width: 120px;" /></td>
						</tr>
						<tr class="nostyle">
							<td>${(courseExam.examType ne '6')?'问题':'文章' }:</td>
							<td colspan="3"><textarea id="courseExam_question"
									name="question" rows="5" cols=""
									style="width: 80%; height: 250px; visibility: hidden;"
									class="required">${courseExam.question }</textarea></td>
						</tr>
						<c:if test="${courseExam.examType ne '6' }">
							<tr id="courseExam_answerOptionNum_tr"
								<c:if test="${not (courseExam.examType eq '1' or courseExam.examType eq '2') }">style="display:none;"</c:if>>
								<td style="width: 10%">答题选项数:</td>
								<td style="width: 90%" colspan="3"><select
									name="answerOptionNum" style="width: 80px;">
										<c:forEach begin="${answerOptionNum }" end="16" var="num">
											<option value="${num }"
												<c:if test="${courseExam.answerOptionNum eq num }">selected="selected"</c:if>>${num }</option>
										</c:forEach>
								</select> <span class="tips">学生答题时显示的选项数，默认为5个选项</span></td>
							</tr>
							<tr class="nostyle">
								<td>答案:</td>
								<td colspan="3"><textarea id="courseExam_answer"
										name="answer" rows="2" cols=""
										style="width: 80%; height: 50px;" class="required">${courseExam.answer }</textarea></td>
							</tr>
							<tr class="nostyle">
								<td>解析:</td>
								<td colspan="3"><textarea id="courseExam_parser"
										name="parser" rows="5" cols=""
										style="width: 80%; height: 250px; visibility: hidden;">${courseExam.parser }</textarea></td>
							</tr>
						</c:if>
					</table>
					<c:if test="${courseExam.examType eq '6' }">
						<table class="form" id="unitCourseExamChildsForm">
							<c:forEach items="${courseExam.childs }" var="child"
								varStatus="vs">
								<tr class="nostyle">
									<td width="10%">问题${vs.index+1 }:</td>
									<td><textarea id="unit_question${child.resourceid }"
											name="question${child.resourceid }" rows="3" cols=""
											style="width: 80%; height: 250px; visibility: hidden;"
											class="required">${child.question }</textarea></td>
								</tr>
								<c:if test="${child.examType eq '1' or child.examType eq '2' }">
									<tr>
										<td>答题选项数:</td>
										<td colspan="3"><select
											name="answerOptionNum${child.resourceid }"
											style="width: 80px;">
												<c:forEach begin="${answerOptionNum }" end="16" var="num">
													<option value="${num }"
														<c:if test="${child.answerOptionNum eq num }">selected="selected"</c:if>>${num }</option>
												</c:forEach>
										</select></td>
									</tr>
								</c:if>
								<tr>
									<td>答案:</td>
									<td><textarea id="unit_answer${child.resourceid }"
											name="answer${child.resourceid }" rows="1" cols=""
											style="width: 80%; height: 50px;" class="required">${child.answer }</textarea></td>
								</tr>
							</c:forEach>
						</table>
					</c:if>
					<table class="form" style="display: none"
						<c:if test="${courseExam.examType eq '6' }">style="display:none"</c:if>>
						<tr>
							<td><a href="javascript:;" class="button"
								onclick="relateExercise(0)"><span>关联随堂练习</span></a> <a
								href="javascript:;" class="button" onclick="relateExercise(1)"><span>取消关联</span></a>
							</td>
						</tr>
					</table>
					<table id="relateActiveExamForm" class="form"
						style="display: none;">
						<tr>
							<td>知识节点:</td>
							<td colspan="3"><select id="courseExamForm_SyllabusId"
								name="syllabusId"
								onchange="courseExamSyllabusChange(this.value)">

							</select></td>
						</tr>
						<tr>
							<td style="width: 10%">排序号:</td>
							<td style="width: 40%"><input type="text" name="showOrder"
								value="1" /></td>
							<td style="width: 10%">分值:</td>
							<td style="width: 40%"><input type="text" name="score"
								value="5" /></td>
						</tr>
						<tr>
							<td>相关知识节点:</td>
							<td colspan="3"><input type="hidden"
								id="referSyllabusTreeIds" name="referSyllabusTreeIds" value="" />
								<input type="text" id="referSyllabusTreeNames"
								name="referSyllabusTreeNames" value="" readonly="readonly"
								style="width: 80%;" /> <a href="javascript:;" class="button"
								onclick="selectSyllabusDialog()"><span>选择知识节点</span></a></td>
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
	 	 function loadCourseExamEditor(){
	 		var ids = ['courseExam_question'];
			$("#unitCourseExamChildsForm [id^='unit_question']").each(function (){
				ids.push(this.id);
			});
		     var examType = "${courseExam.examType}";
		     if(examType!='6'){
		    	 ids.push('courseExam_parser');		    	 
		     } 
		     if(examType=='5'){
		    	 ids.push('courseExam_answer');
		     }
		     for(var k in ids){
		    	 KE.create(ids[k]);	
		     }	
	     } 	     
	     window.setTimeout(loadCourseExamEditor,1000);	    
	     
	     function selectSyllabusDialog(){
	    	 var courseId = $("#courseExamForm_CourseId").val();
	    	 if(courseId==""){
	    		 alertMsg.warn("请选择一门课程!");
	    		 return false;
	    	 } else {
	    		 $.pdialog.open('${baseUrl }/edu3/metares/exercise/activeexercise/syllabustree.html?courseId='+courseId+'&idsN=referSyllabusTreeIds&namesN=referSyllabusTreeNames&checkedIds='+$('#referSyllabusTreeIds').val(),'selector','选择知识节点',{mask:true,width:500,height:450});
	    	 }
	    }
	     
	     function courseExamSyllabusChange(syllabusId){
	    	 if(syllabusId!=""){
	    		var checkurl = "${baseUrl}/edu3/framework/acitvecourseexam/showorder.html";
	 			jQuery.post(checkurl,{syllabusId:syllabusId},function(showOrder){
	 				 $("#relateActiveExamForm input[name='showOrder']").val(showOrder);
	 			},"json");	
	    	 }
	     }
	</script>
</body>
</html>