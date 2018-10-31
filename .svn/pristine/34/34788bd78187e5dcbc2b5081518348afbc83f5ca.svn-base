<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/common.jsp"%>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=7" />
<title>${school}${schoolConnectName}平台- 学习互动中心</title>
<gh:loadCom
	components="netedu3-defaultcss,jquery,framework,fileupload,editor,datePicker,autocomplete,ztree" />
<meta http-equiv="Cache-Control" content="no-store" />
<meta http-equiv="Pragma" content="no-cache" />
<meta http-equiv="Expires" content="0" />
<%--[if IE]>
		<link href="${baseUrl}/themes/css/ieHack.css" rel="stylesheet" type="text/css" />
	<![endif]--%>
<link type="text/css" rel="stylesheet"
	href="${basePath}/themes/css/core.extends.css" />
<style type="text/css">
.scoretip {
	float: left;
}

.optionbar {
	float: left;
	margin-right: 0.5em;
	border: 1px solid #183152;
	height: 8px;
	width: 25px;
}

.optionbar div {
	float: left;
	height: 8px;
	overflow: hidden;
	background-color: #183152;
	background-repeat: repeat-x;
	background-position: 0 100%;
}
</style>
</head>

<body>

	<div id="layout">
		<!-- 头部开始 -->
		<div id="header" style="display:">
			<div class="headerNav">
				<table width="100%" border="0" cellspacing="0" cellpadding="0">
					<tr>
						<td height="110" colspan="2"
							background="${baseUrl }/style/default/learning/logo.png">
							<table width="100%" height="100%" border="0" cellpadding="0"
								cellspacing="0">
								<tr>
									<td height="45" colspan="3" valign="top">
										<div style="float: right">
											<c:choose>
												<c:when test="${teachType ne 'faceTeach' }">
													<table width="748" border="0" cellpadding="0"
														cellspacing="0" class="stl1">
														</c:when>
														<c:otherwise>
															<table width="430" border="0" cellpadding="0"
																cellspacing="0" class="stl1">
																</c:otherwise>
																</c:choose>

																<tr>
																	<td height="24"
																		background="${baseUrl }/style/default/learning/menu2.gif">
																		<ul class="nav">
																			<c:choose>
																				<c:when test="${currentrole ne 'student' }">
																					<li>欢迎<b>${user.cnName }</b></li>
																				</c:when>
																				<c:otherwise>
																					<li><div>
																							欢迎 <b>${user.cnName }</b> 同学,
																						</div></li>
																					<c:if
																						test="${planCourse.teachType ne 'netsidestudy' }">
																						<%-- 																	<li><div class="scoretip">网络辅导：</div><div class="optionbar" title="0"><div id="bbsResults"></div></div></li> --%>
																						<!-- <li><div class="scoretip">随堂问答：</div><div class="optionbar" title="0"><div id="askQuestionResults"></div></div></li> -->
																						<li><div class="scoretip">随堂问答：</div>
																							<div class="optionbar"
																								title="${learningInfo.effectiveProgress>100?100:learningInfo.effectiveProgress }">
																								<div
																									style="width: ${(learningInfo.effectiveProgress>100?100:learningInfo.effectiveProgress)/4 }px;"></div>
																							</div></li>
																					</c:if>
																					<li><div class="scoretip">随堂练习：</div>
																						<div class="optionbar" title="0">
																							<div id="courseExamResults"></div>
																						</div></li>
																					<li><div class="scoretip">学习进度：</div>
																						<div class="optionbar"
																							title="${learningInfo.stuprogress>100?100:learningInfo.stuprogress }">
																							<div
																								style="width: ${(learningInfo.stuprogress>100?100:learningInfo.stuprogress)/4 }px;"></div>
																						</div></li>
																					<%-- --%>
																					<c:if
																						test="${planCourse.teachType ne 'netsidestudy' }">
																						<li><div class="scoretip">作业：</div>
																							<div class="optionbar" title="0">
																								<div id="exerciseResults"></div>
																							</div></li>
																					</c:if>
																					<li><div class="scoretip">总评分：</div>
																						<div class="optionbar" title="0">
																							<div id="usualResults"></div>
																						</div></li>
																				</c:otherwise>
																			</c:choose>

																			<li><a target="dialog"
																				href="${baseUrl }/edu3/student/feedback/input.html?from=interactive&courseId=${course.resourceid }"
																				style="color: blue" width="800" height="600"
																				rel="studentFeedback" title="反馈">反馈</a></li>
																			<li><a href="#" style="color: blue">帮助</a></li>
																		</ul>
																	</td>
																</tr>
															</table>
															</div>
															</td>
															</tr>
															<tr>
																<td height="30" colspan="3">&nbsp;</td>
															</tr>
															<tr>
																<td width="1%" height="46"
																	background="${baseUrl }/style/default/learning/index_03.jpg">
																	<img src="${baseUrl }/style/default/learning/left.jpg"
																	width="22" height="46" />
																</td>
																<td width="98%" height="46"
																	background="${baseUrl }/style/default/learning/index_03.jpg">
																	<ul id="learning_nav">
																		<li><a
																			href="${baseUrl }/edu3/framework/index.html">返回我的主页</a>
																		</li>
																		<li><a href="#">课程介绍▼</a>
																			<ul>
																				<c:forEach items="${courseOverviews }"
																					var="courseOverview" varStatus="vs">
																					<li><a href="#"
																						onclick="goCourseOverview('${courseOverview.resourceid }','${ghfn:dictCode2Val('CodeCourseOverviewType',courseOverview.type)}')">${ghfn:dictCode2Val('CodeCourseOverviewType',courseOverview.type)}</a></li>
																				</c:forEach>
																				<%-- 
													<li><a href="${baseUrl}/edu3/learning/interactive/coursereference/list.html?courseId=${course.resourceid}" target="navTab" rel="CourseReference" title="课程参考资料">课程参考资料</a></li>
													<li><a href="${baseUrl}/edu3/learning/interactive/coursereference/list.html?courseId=${course.resourceid}&type=special" target="navTab" rel="CourseReferenceSpecial" title="特色栏目">特色栏目</a></li>
													--%>
																			</ul></li>
																		<li><a href="#"
																			onclick="goQuizExercisesDistribution()">随堂练习分布</a></li>
																		<c:if test="${teachType ne 'faceTeach' }">
																			<li><a href="#" onclick="goExerciseBatch()">课程作业</a>
																				<%-- 									<ul>
													<li><a href="#" > 本课程作业</a></li> 
													<li><a href="#" onclick="goStudentExercise('isTypical','典型批改')"> 典型批改</a></li>
													<li><a href="#" onclick="goStudentExercise('isExcell','优秀作业')"> 优秀作业</a></li>
											</ul>  --%></li>
																			<li><a href="#">学习互动▼</a>
																				<ul>
																					<%-- 
													<li><a href="#" target="mainFrame2"> 专题讨论</a></li>
													--%>
																					<li><a
																						href="${baseUrl }/edu3/learning/bbs/section.html?courseId=${course.resourceid }&sectionId=${learningGroupSection.resourceid }"
																						target="_blank"> 学习小组讨论</a></li>
																					<%-- 
													<li><a href="#" target="mainFrame2"> 学以致用</a></li>
													 --%>
																					<li><a
																						href="${baseUrl }/edu3/learning/bbs/section.html?courseId=${course.resourceid }&sectionId=${learningDiscussSection.resourceid }"
																						target="_blank"> 主题讨论</a></li>
																					<%-- 												<li><a href="#" onclick="goFAQ()"> FAQ问题 </a></li> --%>
																				</ul></li>

																			<li><a href="#"
																				onclick="window.open('${baseUrl }/edu3/learning/bbs/index.html?courseId=${course.resourceid }','course_bbs');">课程论坛</a></li>
																		</c:if>
																		<li><a href="#">复习总结▼</a>
																			<ul>
																				<li><a
																					href="${baseUrl }/edu3/learning/interactive/materevise/list.html?reviseCourseId=${course.resourceid}"
																					target="navTab" rel="mateRevise" title="教师复习总结录像">教师复习总结录像</a>
																				</li>
																				<%-- 
													<li><a href="javascript:void(0)" onclick="alertMsg.info('功能尚未开放，谢谢关注！');"> 我的复习材料</a></li>
													 --%>
																				<li><a href="#" onclick="goCourseMockTest();">
																						模拟试题</a></li>
																				<%-- 
													<li><a href="${baseUrl }/edu3/learning/interactive/studentcourseexam/list.html?courseId=${course.resourceid}" target="navTab" rel="historyStudentCourseExam" title="随堂练习历史"> 随堂练习历史 </a></li>
													 --%>
																			</ul></li>
																	</ul>
																</td>
																<td width="1%" height="46" align="right"
																	background="${baseUrl }/style/default/learning/index_03.jpg">
																	<img src="${baseUrl }/style/default/learning/right.jpg"
																	width="18" height="46" />
																</td>
															</tr>
													</table>
									</td>
								</tr>
								<tr>
									<td height="14">&nbsp;</td>
									<td width="100%" valign="top"></td>
								</tr>
							</table>
							</div>
							</div> <!-- 头部结束 --> <!-- 左侧菜单开始 -->
							<div id="leftside">
								<!-- 分割 -->
								<div id="sidebar_s" style="display: none;">
									<div class="collapse">
										<div class="toggleCollapse">
											<div></div>
										</div>
									</div>
								</div>
								<div id="sidebar">
									<div class="toggleCollapse"
										style="height: 46px; background: #ccc">
										<h2>${course.courseName }</h2>
										<div title="收缩">收缩</div>
									</div>
									<div class="accordion">
										<div class="accordionContent"
											style="display: block; height: 100%; width: 100%;">
											<ul id="syllabusTree" class="ztree"></ul>
										</div>
									</div>
								</div>
							</div> <!-- 左侧菜单结束 --> <!-- 主内容框架开始 -->
							<div id="container">
								<div id="navTab" class="tabsPage">
									<div class="tabsPageHeader" style="width: 98%">
										<div class="tabsPageHeaderContent">
											<!-- 显示左右控制时添加 class="tabsPageHeaderMargin" -->
											<ul class="navTab-tab">
												<li tabid="main" class="main"><a href="#"><span><span
															class="home_icon">课程学习</span> </span> </a></li>
											</ul>
										</div>
										<div class="tabsLeft">left</div>
										<!-- 禁用只需要添加一个样式 class="tabsLeft tabsLeftDisabled" -->
										<div class="tabsRight">right</div>
										<!-- 禁用只需要添加一个样式 class="tabsRight tabsRightDisabled" -->
										<div class="tabsMore">more</div>
									</div>
									<div
										style="float: right; width: 20px; margin-right: -4px; margin-top: -20px;">
										<img id="hiddenTop"
											src="${baseUrl }/themes/default/images/fullscreen.png"
											title="全屏查看" style="CURSOR: pointer" onclick="fullScreen()" />
									</div>
									<ul class="tabsMoreList">
										<li><a href="javascript:void(0)">课程学习</a></li>
									</ul>
									<!-- 内容显示 -->
									<div class="navTab-panel tabsPageContent">
										<div class="page">
											<div class="pageContent" layoutH="0" style="margin: 8px">
												<div class="tabs" currentIndex="0" eventType="click"
													style="width: 86%; float: left">
													<div class="tabsHeader">
														<div class="tabsHeaderContent">
															<ul>
																<li><a id="interactive_tab1"
																	href="${baseUrl}/edu3/learning/interactive/courselearningguid/list.html"
																	class="j-ajax"><span>学习目标</span></a></li>
																<li><a id="interactive_tab2"
																	href="${baseUrl}/edu3/learning/interactive/materesource/list.html"
																	class="j-ajax"><span>学习材料</span></a></li>
																<li><a id="interactive_tab3"
																	href="${baseUrl}/edu3/learning/interactive/activecourseexam/list.html?teachType=${teachType}"
																	class="j-ajax"><span>随堂练习</span></a></li>
																<li><a id="interactive_tab4"
																	href="${baseUrl}/edu3/learning/interactive/coursereference/list.html"
																	class="j-ajax"><span>参考资料</span></a></li>
																<li><a id="interactive_tab5"
																	href="${baseUrl}/edu3/learning/interactive/bbstopic/list.html?teachType=${teachType}&courseId=${course.resourceid}"
																	class="j-ajax"><span>随堂问答</span></a></li>
																<%-- 							
											<li>
												<a href="javascript:;"><span>资料上传</span></a>
											</li> --%>
															</ul>
														</div>
													</div>
													<div class="tabsContent" layoutH="40"
														style="height: 520px; background: #fff;">
														<div id="interactive_tab1Content"></div>
														<div id="interactive_tab2Content" style="width: 100%;">
															<%-- 								
										<!-- 左侧材料列表 -->
										<div id="matesList"
											style="float:left;width:25%;overflow:auto;padding-bottom:6px; border-right:solid 1px #CCC; line-height:21px;">
											
										</div>
										<div id="matesContent" style="float:left;width:73%">																					
											 <iframe id='webContentFrame' width='100%' height='100%' frameborder='0' src=''></iframe>											 
										</div>
										 --%>
														</div>
														<div id="interactive_tab3Content"></div>
														<div id="interactive_tab4Content"></div>
														<div id="interactive_tab5Content"></div>
														<%-- 						
									<div>
									<p style="color:green">该功能暂时还未开放...</p>
									</div>
									 --%>
													</div>
													<div class="tabsFooter">
														<div class="tabsFooterContent"></div>
													</div>
												</div>
												<!-- 右侧工具条 -->
												<div class="panel collapse" defH="340"
													style="float: left; width: 10%; margin-left: 6px; line-height: 21px;">
													<h1>互动区</h1>
													<div style="background: #fff">
														<ul>
															<li style="padding-top: 6px; text-align: center"><img
																src="${baseUrl }/themes/default/images/learning/hd_notice.png" /><br />
																<a href="#" onclick="openCourseNotice();"
																title="老师发布的课程公告">课程公告<span id="_courseNoticeNum"></span></a>
															</li>
															<li style="padding-top: 10px; text-align: center"><img
																src="${baseUrl }/themes/default/images/learning/hd_tips.png" /><br />
																<a href="#" onclick="openCourseTips();" title="该课程的学习提醒">温馨提示</a>
															</li>
															<li style="padding-top: 10px; text-align: center"><img
																src="${baseUrl }/themes/default/images/learning/hd_bbs.png" /><br />
																<a href="#" onclick="openCourseBbs();" title="课程论坛">论坛</a>|<a
																href="#" onclick="openCourseGroup();" title="课程讨论小组">小组</a>
															</li>
															<li style="padding-top: 10px; text-align: center"><img
																src="${baseUrl }/themes/default/images/learning/hd_ask.png" /><br />
																<a href="#" onclick="openCourseAsk();"
																title="向老师提出学习中的问题">我要提问</a></li>
															<%--  									<li style="padding-top:10px;text-align:center"> --%>
															<%-- 										<img src="${baseUrl }/themes/default/images/learning/hd_facebook.png"/><br/> --%>
															<%-- 										<a target="dialog" href="${baseUrl }/edu3/student/feedback/input.html?from=interactive&courseId=${course.resourceid }" style="color:blue" width="800" height="600" rel="studentFeedback" title="反馈学习中的问题"><font color="blue">课件问题反馈</font></a>												 --%>
															<%--  									</li>								 --%>
														</ul>
													</div>
												</div>

											</div>
										</div>
									</div>
								</div>
							</div> <!-- 页脚开始 -->
							<div id="taskbar" style="left: 0px; display: none;">
								<div class="taskbarContent">
									<ul></ul>
								</div>
								<div class="taskbarLeft taskbarLeftDisabled"
									style="display: none;">taskbarLeft</div>
								<div class="taskbarRight" style="display: none;">
									taskbarRight</div>
							</div>
							<div id="splitBar"></div>
							<div id="splitBarProxy"></div>
							</div>
							<div id="footer">
								技术支持：广东学苑教育发展有限公司
								<gh:version />
							</div> <!--拖动效果-->
							<div class="resizable"></div> <!--阴影-->
							<div class="shadow"
								style="width: 508px; top: 148px; left: 296px;">
								<div class="shadow_h">
									<div class="shadow_h_l"></div>
									<div class="shadow_h_r"></div>
									<div class="shadow_h_c"></div>
								</div>
								<div class="shadow_c">
									<div class="shadow_c_l" style="height: 296px;"></div>
									<div class="shadow_c_r" style="height: 296px;"></div>
									<div class="shadow_c_c" style="height: 296px;"></div>
								</div>
								<div class="shadow_f">
									<div class="shadow_f_l"></div>
									<div class="shadow_f_r"></div>
									<div class="shadow_f_c"></div>
								</div>
							</div> <!--遮盖屏幕-->
							<div id="alertBackground" class="alertBackground"></div>
							<div id="dialogBackground" class="alertBackground"></div>

							<div id='background' class='background'></div>
							<div id='progressBar' class='progressBar'>数据加载中，请稍等...</div> <input
							type="hidden" name="syllabusId" id="syllabusId" value="" /> <script
								type="text/javascript">
	if ($.browser.msie) {//如果是IE，则定时回收内存
		window.setInterval("CollectGarbage();", 10000);
	}
	
	$(document).ready(function(){
		DWZ.init("${baseUrl}/jscript/framework/dwz.plugin.xml", {				
				callback:function(){
					initEnv();
					$("#themeList").theme({themeBase:"themes"});
				}
			});		
			resizeLayout();
			
			//菜单
			menuFix();
			//初始化Ztree
		    var zTree1;
			var setting = {	checkable: false,expandSpeed:"",callback: {click:	zTreeOnClick }	};
			
		 	var zNodes = [${syllabusTree}];	
			$(document).ready(function(){					
				zTree1 = $("#syllabusTree").zTree(setting, zNodes);
			});
				
			function zTreeOnClick(event, treeId, treeNode) {
				var exAttribute = treeNode.exAttribute || {};
				//学习目标
				$("#interactive_tab1,#interactive_tab1Content").toggle(exAttribute['guid']=='Y');	
				
				//学习材料
				$("#interactive_tab2,#interactive_tab2Content").toggle(exAttribute['mate']=='Y');				
				
				//随堂练习
				$("#interactive_tab3,#interactive_tab3Content").toggle(exAttribute['exam']=='Y');
				
				//参考资料
				$("#interactive_tab4,#interactive_tab4Content").toggle(exAttribute['ref']=='Y');
								
				//if(treeNode.parentId!=""){
					$("#syllabusId").val(treeNode.id.split(",")[0]);	
					//if(treeNode.id.split(",")[1]==1){ 		
						$("#interactive_tab1").attr('href',"${baseUrl}/edu3/learning/interactive/courselearningguid/list.html?teachType=${teachType}&syllabusId="+treeNode.id.split(",")[0]);
					//}
					$("#interactive_tab2").attr('href',"${baseUrl}/edu3/learning/interactive/materesource/list.html?courseId=${course.resourceid }&syllabusId="+treeNode.id.split(",")[0]);
					$("#interactive_tab3").attr('href',"${baseUrl}/edu3/learning/interactive/activecourseexam/list.html?teachType=${teachType}&syllabusId="+treeNode.id.split(",")[0]);		
					$("#interactive_tab4").attr('href',"${baseUrl}/edu3/learning/interactive/coursereference/list.html?syllabusId="+treeNode.id.split(",")[0]);
					$("#interactive_tab5").attr('href',"${baseUrl}/edu3/learning/interactive/bbstopic/list.html?teachType=${teachType}&courseId=${course.resourceid }&syllabusId="+treeNode.id.split(",")[0]);		
					$("[id^=interactive_tab]:visible:first").click();
					navTab._switchTab(0);	
					$("#webContentFrame").attr("src","");		//清空视频				
				//} 
				
			}
			
			//获取平时成绩积分
			getUsualResults();			
			
			window.setTimeout(function(){		
				if($("#syllabusTree_2_a")){ //第一章				
					$("#syllabusTree_2_a").click();
				}
			},500);
			
		window.setTimeout(getCourseNotice,3000);//获取课程通知数量等信息
		
		//getOnlieExam();
	});	
	//var sidebarh;
	//全屏查看
	function fullScreen(){		
		if($("#header").height() == 118){	
			//sidebarh=$("#sidebar .accordionContent").height();
			$("#sidebar .toggleCollapse div").click();		
			$("#header").hide();
			resizeLayout1();
			$("#hiddenTop").attr({ title: "退出全屏查看"} );
			$(window).resize();	
			$("#webContentFrame").height($(".tabsContent").height());
			//$(".zTreeDemoBackground").height($("#sidebar_s").height()-46);		
		}else{
			$("#sidebar_s .toggleCollapse div").click();
			$("#header").show();								
			resizeLayout();								
			$("#hiddenTop").attr({ title: "全屏查看"} );	
			$(window).resize();		
			//$("#sidebar .accordionContent").height(sidebarh);
			//$(".zTreeDemoBackground").height($("#sidebar_s").height()-46);
			$("#sidebar .accordionContent").height($("#sidebar_s").height()-46);
			$("#webContentFrame").height($(".tabsContent").height());
		}	
		$("#matesList").height($(".tabsContent").height()-10);
	}
	
	function resizeLayout(){//重新调整框架布局
		$("#header").height("118px");	
		$("#sidebar").css({width:250});	
		$("#leftside").css({top:118});
		$("#container").css({top:118,left:260});
		$("#splitBar, #splitBarProxy").css({top:118,left:255});	
		
		//$(".zTreeDemoBackground").height($("#container").height()-46);	
	}	
	
	function resizeLayout1(){//重新调整框架布局1
		$("#header").height(0);	
		$("#sidebar").css({width:250});	
		$("#leftside").css({top:0});
		$("#container").css({top:0,left:260});
		$("#splitBar, #splitBarProxy").css({top:0,left:255});	
		
		//$(".zTreeDemoBackground").height($("#sidebar_s").height()-46);	
	}
	var teachType = "${planCourse.teachType}";
	//重新登录
	function relogin(){		
		$.pdialog.open('${baseUrl }/edu3/relogin.html?user=${user.username }','relogin','重新登录',{mask:true,width:400,height:180});
	}
	
	//课程概况
	function goCourseOverview(overviewId,typename){
		var url = "${baseUrl}/edu3/learning/interactive/courseoverview/view.html?overviewId="+overviewId;
		navTab.openTab("navTab"+overviewId, url, typename);
	}
	//打开温馨提示
	function openCourseTips(){
		navTab.openTab("navTabCourseTips","${baseUrl}/edu3/learning/interactive/message/list.html?courseId=${course.resourceid}","温馨提示");
	}	
	//互动区-打开课程公告
	function openCourseNotice(){
		navTab.openTab("navTabCourseNotice","${baseUrl}/edu3/learning/interactive/coursenotice/list.html?courseId=${course.resourceid}","课程公告");
	}
	//互动区-打开课程小组讨论
	function openCourseGroup(){
		if(teachType!='netsidestudy'){
			window.open("${baseUrl }/edu3/learning/bbs/section.html?courseId=${course.resourceid }&sectionId=${learningGroupSection.resourceid }","newwindow");
		}else{
			alertMsg.warn("当前课程部分学习内容需面授，不允许进入此功能。");
		}
		
	}
	//互动区-打开课程论坛
	function openCourseBbs(){
		if(teachType!='netsidestudy'){
			window.open('${baseUrl }/edu3/learning/bbs/index.html?courseId=${course.resourceid }','course_bbs');
		}else{
			alertMsg.warn("当前课程部分学习内容需面授，不允许进入此功能。");
		}
	}
	//互动区-打开我要提问
	function openCourseAsk(){	
		//$("#interactive_tab5").click();
		if(teachType!='netsidestudy'){
			$.pdialog.open(baseUrl+"/edu3/learning/interactive/bbstopic/ask.html?courseId=${course.resourceid }&syllabusId="+$('#syllabusId').val(), "dialogQuestion", "我要提问", {height:550,width:700});
		}else{
			alertMsg.warn("当前课程部分学习内容需面授，不允许进入此功能。");
		}
	}
	//显示FAQ问题
	function goFAQ(){
		navTab.openTab("FAQ","${baseUrl }/edu3/learning/interactive/faq/list.html?courseId=${course.resourceid }","FAQ问题");
	}
	//显示随堂练习分布以及完成情况
	function goQuizExercisesDistribution(){
		navTab.openTab("exerciseBatch","${baseUrl }/edu3/learning/exercise/practice-list.html?courseId=${course.resourceid }&flag=learn","随堂练习分布");
	}
	//显示课程作业
	function goExerciseBatch(){
		navTab.openTab("exerciseBatch","${baseUrl }/edu3/learning/interactive/exercisebatch/list.html?courseId=${course.resourceid }","课程作业");
	}
	//典型批改和优秀作业
	function goStudentExercise(type,name){
		navTab.openTab(type,"${baseUrl }/edu3/learning/interactive/studentexercise/list.html?courseId=${course.resourceid }&type="+type,name);
	}	
	//模拟试题
	function goCourseMockTest(){
		var url = "${baseUrl}/edu3/learning/interactive/coursemocktest/view.html?courseId=${course.resourceid }";
		navTab.openTab("CouseMockTest", url, "模拟试题");
	}
	//获取平时成绩积分
	function getUsualResults(){		
		$.ajax({
		   type: "POST",
		   url: "${baseUrl }/edu3/learning/interactive/usualresults/ajax.html",	
		   data: "courseId=${course.resourceid}&teachType=${planCourse.teachType}",
		   dataType: "json",	    
		   global:false,
		   success: function(data){	 
			   $("#bbsResults").css("width",data.bbsResults+"%").parent().attr("title",data.bbsResults);	
			   $("#askQuestionResults").css("width",data.askQuestionResults+"%").parent().attr("title",data.askQuestionResults);
			   $("#exerciseResults").css("width",data.exerciseResults+"%").parent().attr("title",data.exerciseResults);
			   $("#courseExamResults").css("width",data.courseExamResults+"%").parent().attr("title",data.courseExamResults);
			   $("#usualResults").css("width",data.usualResults+"%").parent().attr("title",data.usualResults);
		   		/*
			   if(data.exerciseShow=='N'){
		   		 $("#exerciseResults").parent().parent().hide();
		   		} else {
		   		 $("#exerciseResults").parent().parent().show();
		   		}*/
		   }
		});
	}
	//下拉菜单
	function menuFix() { 
		var sfEls = document.getElementById("learning_nav").getElementsByTagName("li"); 
		for (var i=0; i<sfEls.length; i++) { 
			sfEls[i].onmouseover=function() { 
			this.className+=(this.className.length>0? " ": "") + "sfhover"; 
		} 
		sfEls[i].onMouseDown=function() { 
			this.className+=(this.className.length>0? " ": "") + "sfhover"; 
		} 
		sfEls[i].onMouseUp=function() { 
			this.className+=(this.className.length>0? " ": "") + "sfhover"; 
		} 
		sfEls[i].onmouseout=function() { 
		this.className=this.className.replace(new RegExp("( ?|^)sfhover\\b"),		
		""); 
		} 
		} 
	}
	
	//获取通知及其他数量
	function getCourseNotice(){
		$.ajax({
			   type: "POST",
			   url: "${baseUrl }/edu3/framework/learning/getmsgcount.html",	
			   data: "courseId=${course.resourceid}",
			   dataType: "json",	   
			   global:false,
			   success: function(data){	 
				   $("#_courseNoticeNum").html("("+data.couseNoticeNum+")");
			   }
			});
	}
	/*function getOnlieExam(){
		$.ajax({
			   type: "POST",
			   url: "${baseUrl }/edu3/framework/learning/onlieexam/ajax.html",	
			   data: "courseId=${course.resourceid}",
			   dataType: "json",	    
			   success: function(data){	 
				   if(data.isOpen && data.isOpen=='Y'){
					   var examName = "在线考试";
					   if(data.isMachineExam=='Y'){
						   examName = "期末考试";
					   }
					   if(data.isMachineExam!='Y'){
						   $("#learning_nav").append("<li><a href='javascript:void(0)' onclick=\"goOnlineExam('"+data.examUrl+"')\">"+examName+"</a></li>");   
					   }					      
				   }				  
			   }
		});
	}
	function goOnlineExam(examUrl){
		var fromNet = '${user.fromNet}';
		var url = "http://";
		if(fromNet=="pub"){
			url += examUrl.split("|")[0]+"/edu3-exam";
		} else {
			url += examUrl.split("|")[1]+"/edu3-exam";
		}
		window.open(url+"/exam/main.html?sid=${user.userExtends['defalutrollid'].exValue}&cid=${course.resourceid}","onlie_exam","height="+screen.availHeight+",width="+screen.availWidth+",channelmode=yes,fullscreen=yes,top=0,left=0,toolbar=no,menubar=no,scrollbars=yes, resizable=no,location=no, status=no");
	}*/
    </script>
</body>
</html>
