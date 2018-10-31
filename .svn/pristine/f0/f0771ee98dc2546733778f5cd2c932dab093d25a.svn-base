<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>课程库管理</title>
<script type="text/javascript">
	$(document).ready(function(){
		 $("#uploadify_course_cover").uploadify({ //初始化图片上传
	        'script'         : baseUrl+'/edu3/filemanage/upload.html', //上传URL
	        'auto'           : true, //自动上传               
	        'multi'          : false, //多文件上传
	        'scriptData'	 : {'isStoreToDatabase':'N','storePath':'covers,${storePath}'},
	        'fileDesc'       : '支持格式:jpg/gif/png',  //限制文件上传格式描述
	        'fileExt'        : '*.JPG;*.GIF;*.PNG;', //限制文件上传的类型,必须有fileDesc这个性质
	        'sizeLimit'      : 307200, //限制单个文件上传大小300KB 
	        'buttonImg'      : baseUrl+'/jscript/jquery.uploadify/images/icon_upload.png', //按钮图片
	        onComplete: function (event, queueID, fileObj, response, data) { //上传成功回调函数	        	
	        	var result = response.split("|"); 
	        	$('#coverImg').attr('src','${rootUrl}covers/${storePath}/'+result[1]);   
	        	        	
	        	$('#coverImg').bind("load", function(){
	        		if(this.width!=492 || this.height!=640){
	        			alertMsg.error("图片"+this.width+"*"+this.height+"尺寸错误,上传的图片的尺寸必须为492*640,否则无法保存");
	        		}else{
	        			$('#cover').val('covers/${storePath}/'+result[1]); 
	        		}
	        	});	
			},  
			onError: function(event, queueID, fileObj) {  //上传失败回调函数
			    //alert("文件" + fileObj.name + "上传失败"); 
			    $('#uploadify').uploadifyClearQueue(); //清空上传队列
			}
		}); 

	       var coverSrc = '${teachingCourse.cover }';
	       if(coverSrc != ''){
	       		$('#coverImg').attr('src','${rootUrl}${teachingCourse.cover }');
	       }            
	})
		//检查唯一性
		function validateOnlyCourseCode(){
	 		var courseCode = $("#courseForm input[name=courseCode]");	
	    	if($.trim(courseCode.val())==""){ alertMsg.warn("请输入课程编码"); courseCode.focus();return false; }
	    	var url = "${baseUrl}/edu3/teaching/teachingcourse/validateCode.html";
	    	$.post(url,{courseCode:courseCode.val()},function(existsCode){
	    		if(existsCode){ alertMsg.warn("编码已存在!"); }else{ alertMsg.correct("恭喜，此编码可用！")}
	    	},"json");
	    }	
	    
	    function changeStopTime(type){
	    	$("#stopTimeArea").css("display",type==2 ? "" : "none");
	    }
	</script>
</head>
<body>
	<h2 class="contentTitle">${(empty teachingCourse.resourceid)?'新增':'编辑' }课程</h2>
	<div class="page">
		<div class="pageContent">
			<form id="courseForm" method="post"
				action="${baseUrl}/edu3/teaching/teachingcourse/save.html"
				class="pageForm" onsubmit="return validateCallback(this);">
				<input type="hidden" name="resourceid"
					value="${teachingCourse.resourceid }" />
				<div class="pageFormContent" layoutH="97">
					<table class="form">
						<tr>
							<td width="20%">课程编码:</td>
							<td width="30%"><input type="text" name="courseCode"
								style="width: 200px;" value="${teachingCourse.courseCode }"
								class="required alphanumeric" alt="请输入字母下划线或数字的组合" /> <a
								class="button" href="javascript:;"
								onclick="validateOnlyCourseCode();"><span>检查唯一性</span></a></td>
							<td width="20%">课程名称:</td>
							<td width="30%"><input type="text" name="courseName"
								style="width: 50%" value="${teachingCourse.courseName }"
								class="required" /></td>
							
						</tr>
<!-- 						<tr> -->
<!-- 							<td>课程英文名称:</td> -->
<!-- 							<td><input type="text" name="courseEnName" -->
<%-- 								style="width: 50%" value="${teachingCourse.courseEnName }" /></td> --%>
<!-- 							<td>课程简称:</td> -->
<!-- 							<td><input type="text" name="courseShortName" -->
<%-- 								style="width: 50%" value="${teachingCourse.courseShortName }" --%>
<!-- 								class="required" /></td> -->
<!-- 						</tr> -->
						<tr>
							<td>课程性质:</td>
							<td><gh:select name="courseType"
									value="${teachingCourse.courseType }" style="width:60px;"
									dictionaryCode="CodeCourseType" classCss="required" size="2" /><font
								color="red">*</font>
							<td>课程状态:</td>
							<td><gh:select name="status"
									value="${teachingCourse.status }"
									dictionaryCode="CodeCourseState" style="width:60px;"
									onchange="changeStopTime(this.value);" classCss="required" /><font
								color="red">*</font> <span id="stopTimeArea"
								<c:if test="${teachingCourse.status!=2 }">style="display:none"</c:if>><input
									type="text" name="stopTime" id="stopTime"
									value='<fmt:formatDate value="${teachingCourse.stopTime }" pattern="yyyy-MM-dd"/>'
									class="Wdate" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd'})" /></span>
							</td>
						</tr>
						<tr>
							<td>是否有课件:</td>
							<td><gh:select name="hasResource"
									value="${teachingCourse.hasResource }" dictionaryCode="yesOrNo" />
							</td>
							<td>随堂问答次数：
										<label ></label>
									</td>
									<td>
										<input type="text" name="topicNum"
											value="${teachingCourse.topicNum==null?1:teachingCourse.topicNum }" class="required digits"
											style="width: 60px;margin-top: 0px;padding-top: 0px;" />
											<label style="color: red;width: 50px;" title="即该门课程应达到的有效帖个数，用于计算学生随堂问答进度（有效帖/有效帖总数）">提示信息</label>
									</td>
<!-- 							<td>是否精品课程:</td> -->
<%-- 							<td><gh:select dictionaryCode="yesOrNo" --%>
<%-- 									id="course_isQualityResource" name="isQualityResource" --%>
<%-- 									value="${teachingCourse.isQualityResource }" /></td> --%>
						</tr>
						<%-- <tr>
							<td>是否统考课程:</td> 
							<td><gh:select name="isUniteExam"
									value="${teachingCourse.isUniteExam }" dictionaryCode="yesOrNo" 
									style="width:60px;" classCss="required" /><font color="red">*</font></td> 
							<td>是否学位统考课程:</td>
							<td><gh:select name="isDegreeUnitExam"
									value="${teachingCourse.isDegreeUnitExam }"
									dictionaryCode="yesOrNo" style="width:60px;"
									classCss="required" /><font color="red">*</font></td>
						</tr> --%>
 						<%-- <tr> 
 							<td>课程考试形式:</td> 
							<td><gh:select name="examType"
									value="${teachingCourse.examType }"
									dictionaryCode="CodeCourseExamType" style="width:60px;" /></td>
 							<td>课程考试编码:</td> 
 							<td><input name="examCode" -->
								value="${teachingCourse.examCode }" /></td>
						</tr>  --%>
							<!-- 汕大选修课学时和学分预留字段 -->
							<c:if test="${schoolCode eq '10560' }">
								<tr>
	 								<td>计划外学时:</td>
		 							<td><input type="text" name="planoutStudyHour"
		 								value="${teachingCourse.planoutStudyHour }"
		 								class="required digits" /></td>
		 							<td>计划外学分:</td>
									<td><input type="text" name="planoutCreditHour"
		 								value="${teachingCourse.planoutCreditHour }"
		 								class="required number" /></td>
								</tr>
							</c:if>
						<%-- <tr>
							<c:choose>
								<c:when test="${teachingCourse.isUniteExam eq 'Y' }">
 									<td>是否实践课:</td> -->
									<td><gh:select name="isPractice"
											value="${teachingCourse.isPractice }"
											dictionaryCode="yesOrNo" style="width:60px;" /></td>
									<td>统考成绩模板排序:</td> -->
									<td><input type="text" name="uniteExamTemplatesOrder" 
										value="${teachingCourse.uniteExamTemplatesOrder }"
 										class="required digits" /></td> 
								</c:when>
								<c:otherwise>
									<td>是否实践课:</td> 
									<td><gh:select name="isPractice"
											value="${teachingCourse.isPractice }"
											dictionaryCode="yesOrNo" style="width:60px;" /></td>
									
								</c:otherwise>
							</c:choose>
						</tr> --%>
					<%-- 	<tr> 
						<td>课程中文简介:</td> 
 							<td colspan="3"><textarea name="chsIntroduction" rows="5" 
									cols="" style="width: 50%">${teachingCourse.chsIntroduction }</textarea>
							</td> 
 						</tr> 
 						<tr> 
							<td>课程英文简介:</td> 
							<td colspan="3"><textarea name="enIntroduction" rows="5" 
									cols="" style="width: 50%">${teachingCourse.enIntroduction }</textarea>
 							</td> 
						</tr>  --%>
						<tr>
							<td>备注:</td>
							<td ><textarea name="memo" rows="2" cols="3"
									style="width: 90%">${teachingCourse.memo }</textarea></td>
							<td>是否学位统考课程:</td>
							<td>
							<gh:select name="isDegreeUnitExam" value="${teachingCourse.isDegreeUnitExam }" dictionaryCode="yesOrNo" style="width:60px;"/>
							</td>
						</tr>
						<tr>
						<td>封面:</td>
						<td colspan="3"><input
								name="uploadify_course_cover" id="uploadify_course_cover"
								type="file" /> <input type="hidden" name="cover" id="cover"
								value="${teachingCourse.cover }" /> 说明:小于300KB的gif/jpg/png类型图片,上传的图片的尺寸必须为492*640 <img id="coverImg" height="640px"
								src="${rootUrl}covers/default/cover.jpg" />
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
</body>
</html>