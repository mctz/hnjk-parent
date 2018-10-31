<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta content="text/html; charset=utf-8" http-equiv="Content-Type" />
<title>${school}${schoolConnectName}- <c:if
		test="${not empty course.resourceid }">${course.courseName } - </c:if>
	首页
</title>
<gh:loadCourseCss courseId="${course.resourceid }" />
<gh:loadCom components="jquery,course,popwindow" />
<link type="text/css" rel="stylesheet"
	href="${basePath}/style/default/resources/course_c++.css" />
<style type="text/css">
#teacherFilesTable, #teacherFilesTable td {
	border: 0;
}
</style>
<script type="text/javascript">
$(function (){
	$("#search_box input[name='keyword']").focus(function (){
		if($.trim($(this).val())==$(this).attr('alt')){
			$(this).val('');
		}
	}).blur(function (){
		if($.trim($(this).val())==""){
			$(this).val($(this).attr('alt'));
		}
	});
});
function _resourceCourseSearchOnSubmit(form){
	var $form = $(form);
	var obj = $form.find("input[name='keyword']");
	var kd = $.trim(obj.val());
	if(kd=="" || kd == obj.attr('alt')){
		obj.focus().val('');
		alert("请输入要检索的关键字！");
		return false;
	}
	return true;	
}
</script>
</head>
<body>

	<div id="Area">
		<div id="index_header">
			<div id="search_box">
				<!--检索-->
				<form action="${baseUrl }/resource/course/search.html" method="get"
					onsubmit="return _resourceCourseSearchOnSubmit(this);">
					<input type="hidden" name="courseId" value="${course.resourceid }" />
					<input type="text" id="input1" name="keyword" value="请输入所需的检索"
						alt="请输入所需的检索" /> <input type="image"
						src="${basePath }/style/default/resources/images/search_bt.gif"
						width="67" height="27" class="search_bt" alt="检索" title="检索" />
				</form>
			</div>
			<gh:courseChannel courseId="${course.resourceid }" />
		</div>
		<!--end header-->

		<div id="index_main">
			<div id="index_left">
				<h1>课程简介</h1>
				<p class="indent">${courseOverviewContent }</p>
				<p style="text-align: right">
					<a
						href="${baseUrl }/resource/course/courseoverview.html?courseId=${course.resourceid }">更多>></a>
				</p>
				<H1>每章学习指导</H1>
				<c:if test="${not empty syllabusList  }">
					<div id="Accordion1" class="Accordion" tabindex="0">
						<c:forEach items="${syllabusList }" var="syllabus" varStatus="vs">
							<div class="AccordionPanel">
								<div class="AccordionPanelTab">${syllabus.syllabusName }</div>
								<div class="AccordionPanelContent">
									${courseLearningGuidMap[syllabus.resourceid].content }</div>
							</div>
						</c:forEach>
					</div>
				</c:if>
			</div>
			<!--end left-->

			<div id="con">

				<c:if test="${not empty studyStepList }">
					<div id="TabbedPanels1" class="TabbedPanels">
						<ul class="TabbedPanelsTabGroup">
							<c:forEach items="${studyStepList }" var="stu" varStatus="vs">
								<li class="TabbedPanelsTab" tabindex="0">${stu.channelName }</li>
							</c:forEach>
						</ul>
						<div class="TabbedPanelsContentGroup">
							<c:forEach items="${studyStepList }" var="stu" varStatus="vs">
								<div class="TabbedPanelsContent">
									<p style="text-align: justify;">${fn:replace(stu.channelContent,newLine,'<br/>') }</p>
								</div>
							</c:forEach>
						</div>
					</div>
				</c:if>

				<div>
					<h1>特色资源</h1>
					<c:if test="${not empty teacherFilesList }">
						<table cellpadding="0" cellspacing="0" class="list" width="100%;"
							id="teacherFilesTable">
							<c:forEach items="${teacherFilesList }" var="file" varStatus="vs">
								<tr <c:if test="${vs.index mod 2 eq 0 }">class="odd"</c:if>>
									<td width="21">${vs.index+1 }</td>
									<td>${file.fileName }</td>
									<td width="35"><c:choose>
											<%-- 外部附件 --%>
											<c:when test="${empty file.attach.resourceid }">
												<a href="${file.fileUrl }" title="${file.fileName }">下载</a>
											</c:when>
											<%-- 本地附件 --%>
											<c:otherwise>
												<a href="javascript:void(0)"
													onclick="downloadAttachFile('${file.attach.resourceid }')"
													title="${file.fileName }.${file.attach.attType }">下载</a>
											</c:otherwise>
										</c:choose></td>
								</tr>
							</c:forEach>
							<tr
								<c:if test="${fn:length(teacherFilesList) mod 2 eq 0 }">class="odd"</c:if>>
								<td colspan="3">
									<p style="text-align: right">
										<a
											href="${baseUrl }/resource/course/coursefiles.html?courseId=${course.resourceid}">更多>></a>
									</p>
								</td>
							</tr>
						</table>
					</c:if>
				</div>
			</div>
			<!--end con-->

			<div id="index_right">
				<p class="indent">
					<c:choose>
						<c:when test="${not empty user }">
						您好<span class="t_blue t_strong">${user.cnName }</span>，欢迎学习本课程！本课程理论知识共 127 个。<a
								href="${baseUrl }/edu3/framework/index.html"><span
								class="t_blue t_strong"><c:choose>
										<c:when test="${user.userType eq 'student' }">【进入我的学习空间】</c:when>
										<c:otherwise>【进入网络教学平台】</c:otherwise>
									</c:choose></span></a>
						</c:when>
						<c:otherwise>
						您好，欢迎学习本课程！本课程理论知识共 127 个。 由于您当前的身份为 <span class="t_blue t_strong">游客</span> ， 若想查看您的个人学习信息记录，请<a
								href="#" class="t_red t_strong"
								onclick="login('${course.resourceid}');">登录</a>
						</c:otherwise>
					</c:choose>
				</p>
				<div class="t_xx">
					<p>
						<a href="javascript:void(0)" onclick="goLearningStudy('note');"><img
							src="${baseUrl}/style/default/resources/images/bt_xxbj.gif"
							width="240" height="31" /></a>
					</p>
					<p>
						<a href="javascript:void(0)" onclick="goLearningStudy('trace');"><img
							src="${baseUrl}/style/default/resources/images/bt_xxzd.gif"
							width="240" height="31" /></a>
					</p>
				</div>

				<h1>知识园地</h1>
				<ul style="padding-left: 30px; line-height: 20px;">
					<c:forEach items="${studyCenterFilesList }" var="file"
						varStatus="vs">
						<c:if test="${vs.index lt 20 }">
							<li style="list-style: decimal;"><c:choose>
									<%-- 外部附件 --%>
									<c:when test="${empty file.attach.resourceid }">
										<a href="${file.fileUrl }" target="_blank"
											title="${file.fileName }">${file.fileName }</a>
									</c:when>
									<%-- 本地附件 --%>
									<c:otherwise>
										<a href="javascript:void(0)"
											onclick="downloadAttachFile('${file.attach.resourceid }')"
											title="${file.fileName }.${file.attach.attType }">${file.fileName }</a>
									</c:otherwise>
								</c:choose></li>
						</c:if>
					</c:forEach>
				</ul>
				<c:if test="${not empty studyCenterFilesList }">
					<p style="text-align: right">
						<a
							href="${baseUrl }/resource/course/knowledge.html?courseId=${course.resourceid}">更多>>
						</a>
					</p>
				</c:if>
			</div>
			<!--end right-->

		</div>
		<!--end main-->
		<div id="index_footer">
			<p>&copy; 2012 . ${school}${schoolConnectName}All rights reserved</p>
		</div>
	</div>
	<script type="text/javascript">
<!--
if($("#Accordion1").length>0){
	var Accordion1 = new Spry.Widget.Accordion("Accordion1");
}
if($("#TabbedPanels1").length>0){
	var TabbedPanels1 = new Spry.Widget.TabbedPanels("TabbedPanels1");
}
//-->
</script>
	<script type="text/javascript">
<!--
//下载特色资源
function downloadAttachFile(attid){
	var url = baseUrl+"/edu3/framework/filemanage/${empty user ? 'public/':''}download.html?id="+attid;
	var elemIF = document.createElement("iframe");  
	elemIF.src = url;  
	elemIF.style.display = "none";  
	document.body.appendChild(elemIF); 
}
function goLearningStudy(type){
	var url,title,syllabusid = "${syllabusRoot.resourceid}",courseId="${course.resourceid}",userid="${user.resourceid}",userType="${user.userType}";
	if(userid==""){
		alert("请登录之后再进行操作.");
		return false;
	}
	if(userType!="student"){
		alert("您不是学生,无法操作.");
		return false;
	}
	if(type=='note'){
		if(syllabusid != ""){
			title = "在线笔记";
			var _courseDialogOp = {id:'resource_course_dialog', draggable: true,autosize:false,resizable:true,maximizable:true,autocenter:false, autopos:'fixed',width:800,height:600, onClose: _learningNoteOnClose};
			url = baseUrl+"/resource/course/note.html?syllabusid="+syllabusid;	
			Dialogs.load(url,_courseDialogOp).title("在线笔记");
		}		
	} else {
		if(courseId != ""){
			title = "学习档案";
			var _courseDialogOp2 = {id:'resource_course_trace_dialog', draggable: true,autosize:false,resizable:true,maximizable:true,autocenter:false,autopos:'fixed',width:800,height:600};
			url = baseUrl+"/resource/course/learningtrace.html?courseId="+courseId;
			Dialogs.load(url,_courseDialogOp2).title("学习档案").show();
		}			
	}
}
function _learningNoteOnClose(){
	if($("#orgLearnigNoteContent").length>0){//学习笔记
		if($.trim($("#orgLearnigNoteContent").html()) != $.trim($("#learningNote_content").val())){//关闭学习笔记是内容未保存
			alert("您的学习笔记还未保存,请保存后再关闭.");
			return false;	
		}
	}
	return true;
}
function login(cid){
	var _courseDialogOp2 = {id:'loginForm', draggable: true,autosize:false,resizable:true,maximizable:true,autocenter:false,autopos:'fixed',width:350,height:250};
	var url = baseUrl+"/resource/login.html?courseId="+cid;
	//Dialogs.load(url,_courseDialogOp2).title("登录").show();
	var d = new Dialog(_courseDialogOp2).href(url).show();
}
//-->
</script>
</body>
</html>
