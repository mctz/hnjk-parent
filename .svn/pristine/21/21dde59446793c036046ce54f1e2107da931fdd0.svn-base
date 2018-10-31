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
	href="${basePath}/style/default/resources/course_037/course_037.css" />
<style type="text/css">
#teacherFilesTable, #teacherFilesTable td {
	border: 0;
}

#index_right {
	margin-top: 14px !important;
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
						src="${basePath }/style/default/resources/course_037/images/search_bt.gif"
						width="60" height="20" class="search_bt" alt="检索" title="检索" />
				</form>
			</div>
			<gh:courseChannel courseId="${course.resourceid }" />
		</div>
		<!--end header-->

		<div id="banner">
			<div id="login">
				<c:choose>
					<c:when test="${not empty user }">
						<p class="indent" style="height: 60px;">
							您好，<span class="t_strong">${user.cnName }</span>，欢迎学习本课程！本课程理论知识共
							62 个。<a href="${baseUrl }/edu3/framework/index.html"><span
								class="t_org t_strong"><c:choose>
										<c:when test="${user.userType eq 'student' }">【进入我的学习空间】</c:when>
										<c:otherwise>【进入网络教学平台】</c:otherwise>
									</c:choose></span></a>
						</p>
					</c:when>
					<c:otherwise>
						<p class="indent">
							您好，欢迎学习本课程！本课程理论知识共 62 个。 由于您当前的身份为 <span class="t_strong">游客</span>
							， 若想查看您的个人学习信息记录，请<a href="#" class="t_org t_strong"
								onclick="login('${course.resourceid}');">登录</a>
						</p>
					</c:otherwise>
				</c:choose>
				<div class="t_xx">
					<p>
						<a href="javascript:void(0)" onclick="goLearningStudy('note');"><img
							src="${basePath }/style/default/resources/course_037/images/bn_01.gif"
							width="240" height="31" /></a>
					</p>
					<p>
						<a href="javascript:void(0)" onclick="goLearningStudy('trace');"><img
							src="${basePath }/style/default/resources/course_037/images/bn_02.gif"
							width="240" height="31" /></a>
					</p>
				</div>
				<!--end login-->
			</div>
			<!--end banner-->

			<div id="index_main">
				<div id="index_left">
					<div class="main_title">
						<img
							src="${basePath }/style/default/resources/course_037/images/t_bg02.jpg"
							width="50" height="45" align="absmiddle" />课程简介
					</div>
					<div class="clear"></div>
					<p class="indent">${courseOverviewContent }</p>
					<p style="text-align: right">
						<a
							href="${baseUrl }/resource/course/courseoverview.html?courseId=${course.resourceid }">更多>></a>
					</p>
				</div>
				<!--end left-->

				<div id="con">
					<div class="main_title2">
						<img
							src="${basePath }/style/default/resources/course_037/images/t_bg03.jpg"
							width="50" height="45" align="absmiddle" />每章学习指导
					</div>
					<div style="margin: 26px 0 0 0;">
						<c:if test="${not empty syllabusList  }">
							<c:set var="len" value="${fn:length(syllabusList)}" />
							<c:set var="gp" value="${(len-len%10)/10}" />
							<c:if test="${len%10!=0}">
								<c:set var="gp" value="${gp+1}" />
							</c:if>
							<div id="TabbedPanels2" class="TabbedPanels">
								<ul class="TabbedPanelsTabGroup"
									style="float: right; border: none">
									<c:forEach begin="1" end="${gp }" var="i">
										<li class="TabbedPanelsTab2" tabindex="0">${i }</li>
									</c:forEach>
								</ul>
								<div class="TabbedPanelsContentGroup" style="border: none">
									<c:forEach begin="1" end="${gp }" var="i">
										<div class="TabbedPanelsContent">
											<div id="Accordion${i }" class="Accordion" tabindex="0">
												<c:forEach items="${syllabusList }" var="syllabus"
													varStatus="vs">
													<c:if test="${vs.index ge (i-1)*10 and vs.index lt i*10 }">
														<div class="AccordionPanel">
															<div class="AccordionPanelTab">${syllabus.syllabusName }</div>
															<div class="AccordionPanelContent">
																${courseLearningGuidMap[syllabus.resourceid].content }</div>
														</div>
													</c:if>
												</c:forEach>
											</div>
										</div>
										<!--end TabbedPanelsContent-->
									</c:forEach>
								</div>
								<!--end TabbedPanelsContentGroup-->
							</div>
							<!--end TabbedPanels2-->
						</c:if>
					</div>
				</div>
				<!--end con-->

				<div id="index_right">
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

					<div class="main_title" style="clear: both;">特色资源</div>
					<div style="padding-top: 15px;">
						<c:if test="${not empty teacherFilesList }">
							<table cellpadding="0" cellspacing="0" class="list" width="100%;"
								id="teacherFilesTable">
								<c:forEach items="${teacherFilesList }" var="file"
									varStatus="vs">
									<tr <c:if test="${vs.index mod 2 eq 0 }">class="odd"</c:if>>
										<td width="21">${vs.index+1 }</td>
										<td>${file.fileName }</td>
										<td width="35"><c:choose>
												<%-- 外部附件 --%>
												<c:when test="${empty file.attach.resourceid }">
													<a href="${file.fileUrl }" target="_blank"
														title="${file.fileName }">下载</a>
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
				<!--end right-->

			</div>
			<!--end main-->
			<div id="index_footer">
				<p>&copy; 2012 . ${school}${schoolConnectName}All rights
					reserved</p>
			</div>
		</div>
		<script type="text/javascript">
<!--
if($("#Accordion1").length>0){
	var Accordion1 = new Spry.Widget.Accordion("Accordion1");
}
if($("#Accordion2").length>0){
	var Accordion2 = new Spry.Widget.Accordion("Accordion2");
}
if($("#Accordion3").length>0){
	var Accordion3 = new Spry.Widget.Accordion("Accordion3");
}
if($("#Accordion4").length>0){
	var Accordion4 = new Spry.Widget.Accordion("Accordion4");
}
if($("#TabbedPanels1").length>0){
	var TabbedPanels1 = new Spry.Widget.TabbedPanels("TabbedPanels1");
}
if($("#TabbedPanels2").length>0){
	var TabbedPanels2 = new Spry.Widget.TabbedPanels("TabbedPanels2");
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