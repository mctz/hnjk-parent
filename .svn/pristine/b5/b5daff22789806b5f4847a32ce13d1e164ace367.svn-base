<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>试题导入</title>
<style type="text/css">
#explanationSection li, #explanationSection p,
	#courseExamImportTemplate_ul li {
	line-height: 22px;
	width: 100%;
	height: auto;
	padding: 0;
}

#explanationSection ._bolder {
	font-weight: bold;
}
</style>
<script type="text/javascript">
		$(function() {	  		
	  		$("#uploadify_courseexam_form").uploadify({ //初始化附件组件
                'script'         : baseUrl+'/edu3/filemanage/upload.html?storePath=importfiles', //上传URL
                'auto'           : true, //自动上传
                'multi'          : false, //多文件上传
                'scriptData'     :{fillinName:'${currentUser.cnName}',fillinNameId:'${currentUser.resourceid}',formType:'CourseExamImport'},
                'fileDesc'       : '支持格式:rtf/doc',  //限制文件上传格式描述
                'fileExt'        : '*.rtf;*.doc;', //限制文件上传的类型,必须有fileDesc这个性质
                'sizeLimit'      : 31457280, //限制单个文件上传大小30M 
                'buttonImg'      : baseUrl+'/jscript/jquery.uploadify/images/icon_upload.png', //按钮图片
                onComplete: function (event, queueID, fileObj, response, data) { //上传成功回调函数
                	$("#courseexam_uploadifyQueue").html("<li id='"+response.split("|")[0]+"'><img src='"+baseUrl+"/jscript/jquery.uploadify/images/attach.png' style='cursor:pointer;height:10px'>&nbsp;&nbsp;<a href='#' onclick='downloadAttachFile(\""+response.split("|")[0]+"\")'>"+fileObj.name+"&nbsp;</a>&nbsp;&nbsp;<img src='"+baseUrl+"/jscript/jquery.uploadify/images/cancel.png' onClick='deleteAttachFile(\""+response.split("|")[0]+"\");' style='cursor:pointer;height:10px'></li>")
					$("#courseexam_hideFileId").html("<input type='hidden' id='hideFileId_"+response.split("|")[0]+"' name='uploadfileid' value='"+response.split("|")[0]+"'>"); //添加隐藏域与业务建立关系
				},  
				onError: function(event, queueID, fileObj) {  //上传失败回调函数
				    alertMsg.warn("文件:" + fileObj.name + "上传失败"); 
				    $('#uploadify_courseexam_form').uploadifyClearQueue(); //清空上传队列
				}
	       });   		   	       	        
    	});
    	
    	//附件下载
	   function downloadAttachFile(attid){
	   		var url = baseUrl+"/edu3/framework/filemanage/download.html?id="+attid;
	   		var elemIF = document.createElement("iframe");  
			elemIF.src = url;  
			elemIF.style.display = "none";  
			document.body.appendChild(elemIF); 
	   }
	   //附件删除
	   function deleteAttachFile(attid){   
		   	alertMsg.confirm("确定要删除这个附件？", {
				okCall: function(){
					var url = baseUrl+"/edu3/framework/filemanage/delete.html"; 
					$.get(url,{fileid:attid},function(data){
						$("#courseexam_uploadifyQueue > li[id='"+attid+"']").remove();
						$("#hideFileId_"+attid).remove();
					});		
				}
			}); 
	   } 
	   
	   function courseExamImportValidateCallback(form){
		   var isEnrolExam = $(form).find("[name='isEnrolExam']").val();
		   var courseId = $(form).find("[name='courseId']").val();
		   if(courseId==""){
			   alertMsg.warn("课程不能为空！");
			   return false;
		   }
		   var uploadfileid = $(form).find("[name='uploadfileid']").size();
		   if(uploadfileid<1){
			   alertMsg.warn("附件不能为空！");
			   return false;
		   }
		   return validateCallback(form,function (json){
			   DWZ.ajaxDone(json);
				if (json.statusCode == 200){
					if (json.navTabId){
						navTab.reload(json.reloadUrl, {}, json.navTabId);
					}
					$.pdialog.closeCurrent();
				}
		   });
	   }
	   //下载导入模板
	   function downloadCourseExamTemplate(course,fileName){
		   var url = "${baseUrl}/edu3/framework/courseexam/template/download.html?course="+course+"&fileName="+encodeURIComponent(fileName);
			downloadFileByIframe(url,"downloadCourseExamImportTemplateIframe"); 
	   }
	</script>
</head>
<body>
	<h2 class="contentTitle">试题导入</h2>
	<div class="page">
		<div class="pageContent">
			<form method="post"
				action="${baseUrl}/edu3/metares/courseexam/import.html"
				class="pageForm"
				onsubmit="return courseExamImportValidateCallback(this);">
				<input type="hidden" name="isEnrolExam" value="${isEnrolExam }" />
				<div class="pageFormContent" layoutH="97">
					<table class="form">
						<tr>
							<td width="20%">课程:</td>
							<td colspan="3"><c:choose>
									<c:when test="${isEnrolExam eq 'N' }">
										<gh:courseAutocomplete name="courseId" tabindex="1"
											isFilterTeacher="Y" classCss="required"
											id="courseexamimport_courseId" displayType="code"
											style="width:50%;" />
									</c:when>
									<c:otherwise>
										<gh:select name="courseId" value=""
											dictionaryCode="CodeEntranceExam" classCss="required" />
										<span style="color: red;">*</span>
									</c:otherwise>
								</c:choose></td>
						</tr>
						<c:if test="${isEnrolExam eq 'N' }">
							<tr>
								<td width="20%">试题考试形式:</td>
								<td><gh:select dictionaryCode="CodeExamform"
										classCss="required" name="examform" choose="N"
										filtrationStr="online_exam,unit_exam,final_exam" /> <span
									style="color: red;">*</span></td>
							</tr>
						</c:if>
						<tr>
							<td>试题文件:</td>
							<td colspan="3"><font color="green">(单个文件上传大小不能大于30M)</font><br />
								<input type="file" name="uploadify"
								id="uploadify_courseexam_form" />
								<div id="courseexam_uploadifyQueue" class="uploadifyQueue"></div>
								<div id="courseexam_hideFileId" style="display: none"></div></td>
						</tr>
						<tr>
							<td>导入模板:</td>
							<td colspan="3">
								<ul id="courseExamImportTemplate_ul">
									<c:forEach items="${courseList }" var="course">
										<c:choose>
											<c:when
												test="${isEnrolExam eq 'N' and (course eq 'GDSX' or course eq 'GZSX') }"></c:when>
											<c:when
												test="${isEnrolExam eq 'Y' and (course eq 'NET' or course eq 'JSJYYJC') }"></c:when>
											<c:otherwise>
												<li><img style="cursor: pointer; height: 10px"
													src="${baseUrl }/jscript/jquery.uploadify/images/attach.png"><a
													href="##"
													onclick="downloadCourseExamTemplate('${course}',this.title);"
													title="${dictMap[course]['courseName'] }试题导入模板">${dictMap[course]['courseName'] }试题导入模板.doc</a></li>
											</c:otherwise>
										</c:choose>
									</c:forEach>
									<li><img style="cursor: pointer; height: 10px"
										src="${baseUrl }/jscript/jquery.uploadify/images/attach.png"><a
										href="##"
										onclick="downloadCourseExamTemplate('Illustration',this.title);"
										style="color: blue;" title="试题导入模板说明">试题导入模板说明.doc</a></li>
								</ul>
							</td>
						</tr>
						<tr id="explanationSection">
							<td colspan="4">
								<div>
									附件格式说明：
									<ul>
										<li>（1）试题题型：以“◎题型：”开始，独立一行，分"单选题","多选题","判断题","填空题","论述题"和"材料题"。</li>
										<li>（2）试题类型：紧跟在题型之后,以代码形式或名称形式标注在中括号"[""]"中，类型可以为空。例如：
											语文的"基础知识"，"现代文阅读"和"文言文阅读",英语的"词汇语法"，"阅读理解(5小题)"，"阅读理解(10小题)"，"完形填空"和"翻译"等。
											已有试题类型请看下表:<a href="#courseExamImportExamNodeType"
											style="font-weight: bold;">试题类型表</a>
										</li>
										<li>（3）在线作答非客观题：在类型后加上<span style="font-weight: bold;">[在线做题]</span>
											如<a href="#example6" style="font-weight: bold;">示例6</a></li>
										<li>（4）每道题格式：题目分问题和答案，答案以"【"和"】"包裹。题目间以换行符分隔。</li>
										<li>
											<div>（5）试题导入示例</div>
											<div style="color: green; margin-left: 20px;">
												<p>
													<font color="blue">示例1：</font>
												</p>
												<p class="_bolder">◎题型：单选题</p>
												<p>影响度是指电子商务对国民经济和（ ）产生的影响</p>
												<p>A．社会发展 B．企业发展</p>
												<p>C．市场 D．人民生活</p>
												<p>【A】</p>
												<p>
													<font color="blue">示例2：</font>
												</p>
												<p class="_bolder">◎题型：多选题</p>
												<p>企业制定电子商务战略遵循的基本原则是（ ）</p>
												<p>A．信息择优原则</p>
												<p>B．网络广告原则</p>
												<p>C．电子商务回报原则</p>
												<p>D．零售与机会均等原则</p>
												<p>【ABCD】</p>
												<p>
													<font color="blue">示例3：</font>
												</p>
												<p class="_bolder">◎题型:判断题</p>
												<p>网格计算无须在意具体的执行和服务过程。（ ）</p>
												<p>【对】</p>
												<p>
													<font color="blue">示例4：</font>
												</p>
												<p class="_bolder">
													<span title="题型">◎题型：单选题</span><span style="color: red;">[基础知识题]</span>
												</p>
												<p>下列句中“之”是实词，当“到”讲的是 （ ）</p>
												<p>A、所操之术多异故也。 B、夫子欲之。</p>
												<p>C、大军不知广所之。 D、今我睹子之难穷也。</p>
												<p>【C】</p>
												<p>
													<font color="blue">示例5：</font>
												</p>
												<p class="_bolder">
													<span title="题型">◎题型：材料题</span><span style="color: red;">[阅读理解(5小题)]</span>
												</p>
												<p>Most People's jobs are likely to be affected by
													computers in one way or another. Teacher, for example, can
													have computer terminals to use, which can pose problems and
													ask questions, and the computer can inspect and check the
													pupil?s replies. But could a computer ever replace teachers
													or do any job a man or woman can do? The short answer is
													that this is very likely. Computers are only effective when
													problems are clearly described in advance. They are next to
													useless when problems are not clearly described. For
													example, an airplane can fly automatically most of the
													time, but there is always a human pilot in case something
													goes wrong. The human can react to any situation, some of
													which he may never have imagined. At the moment most
													computer programs need to know everything that might happen
													in advance, and what to do if it does happen. Such programs
													can be written if the computer is only playing backgammon,
													but they can not be written for a nurse, an athlete, or any
													number of other professions. Some people say that computers
													can never have mind of their own because they need a
													program, which is created by a human, to tell them what to
													do. This is perfectly true.</p>
												<p class="_bolder" title="材料题题目题型">T题型：单选题</p>
												<p>1) A computer is said to be useful in the classroom
													because ________.</p>
												<p>A) computers can ask question for pupils to answer B)
													pupils can ask questions using computers</p>
												<p>C) computers can check pupils? learning progress D)
													computers can take the place of teachers</p>
												<p>【A】</p>
												<p>2) A computer is said to be useful in the classroom
													because ________.</p>
												<p>A) computers can ask question for pupils to answer B)
													pupils can ask questions using computers</p>
												<p>C) computers can check pupils? learning progress D)
													computers can take the place of teachers</p>
												<p>【A】</p>
												<p>3) A computer is said to be useful in the classroom
													because ________.</p>
												<p>A) computers can ask question for pupils to answer B)
													pupils can ask questions using computers</p>
												<p>C) computers can check pupils? learning progress D)
													computers can take the place of teachers</p>
												<p>【A】</p>
												<p>4) What's the main idea of this passage?</p>
												<p>A) Computers will eventually take the place of human
													beings.</p>
												<p>B) Computers can always work better than human
													beings.</p>
												<p>C) Computers can never replace human beings.</p>
												<p>D) Human beings will never allow computers to replace
													them.</p>
												<p>【C】</p>
												<p>5) What's the main idea of this passage?</p>
												<p>A) Computers will eventually take the place of human
													beings.</p>
												<p>B) Computers can always work better than human
													beings.</p>
												<p>C) Computers can never replace human beings.</p>
												<p>D) Human beings will never allow computers to replace
													them.</p>
												<p>【C】</p>
												<p id="example6">
													<font color="blue">示例6：</font>
												</p>
												<p class="_bolder">◎题型：论述题[名词解释][在线做题]</p>
												<p>局域网</p>
												<p>【一种覆盖一座或几座大楼、一个校园或者一个厂区等地理区域的小范围的计算机网。】</p>
											</div>
										</li>
									</ul>
								</div>
							</td>
						</tr>
						<tr id="courseExamImportExamNodeType">
							<td colspan="4">
								<table width="90%">
									<thead>
										<tr>
											<th colspan="2"
												style="font-weight: bold; text-align: center;">试题类型表</th>
										</tr>
										<tr>
											<th width="50%"
												style="font-weight: bold; text-align: center;">试题类型名称</th>
											<th width="50%"
												style="font-weight: bold; text-align: center;">试题类型值</th>
										</tr>
									</thead>
									<tbody>
										<c:forEach items="${courseList }" var="course">
											<c:choose>
												<c:when
													test="${isEnrolExam eq 'N' and (course eq 'GDSX' or course eq 'GZSX') }"></c:when>
												<c:when
													test="${isEnrolExam eq 'Y' and (course eq 'NET' or course eq 'JSJYYJC') }"></c:when>
												<c:otherwise>
													<tr>
														<td colspan="2"
															style="font-weight: bold; text-align: center;">${dictMap[course]['courseName'] }</td>
													</tr>
													<c:forEach items="${dictMap[course]['dictList'] }"
														var="dict">
														<tr>
															<td>${dict.name }</td>
															<td>${dict.value }</td>
														</tr>
													</c:forEach>
												</c:otherwise>
											</c:choose>
										</c:forEach>
									</tbody>
								</table>
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
										onclick="$.pdialog.closeCurrent();">取消</button>
								</div>
							</div></li>
					</ul>
				</div>
			</form>
		</div>
	</div>
</body>
</html>