<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<title>课程学习</title>
<gh:loadCom components="iframeAutoHeight,popwindow,editor,flowplayer" />
<script type="text/javascript">
$(function (){
	$(".menu").jCourseMenu();	 
});
</script>
</head>
<body>
	<div id="main">
		<div id="left">
			<div class="studyLearn"></div>
			<!-- <img src="${basePath }/style/default/resources/images/t_kcxx.jpg" width="187" height="105" /> -->
			<div class="box">
				<ul class="menu">
					<c:forEach items="${syllabusList }" var="s">
						<li class="level1">
							<h4 <c:if test="${s.showMenu }">class="cur"</c:if>>
								<a
									href="${baseUrl }/resource/course/materesource.html?resType=${resType }&syllabusid=${s.resourceid}"
									title="${s.syllabusName }">${s.syllabusName }</a>
							</h4>
							<ul class="level2"
								<c:if test="${s.showMenu }">style="display:block;"</c:if>>
								<c:forEach items="${s.childs }" var="child">
									<li>
										<h5
											<c:if test="${child.resourceid eq syllabus.resourceid }">class="cur1"</c:if>>
											<a
												href="${baseUrl }/resource/course/materesource.html?resType=${resType }&syllabusid=${child.resourceid}"
												title="${child.syllabusName }">${child.syllabusName }</a>
										</h5> <c:if test="${not empty child.childs }">
											<ul class="level3"
												<c:if test="${child.showMenu }">style="display:block;"</c:if>>
												<c:forEach items="${child.childs }" var="c">
													<li><a
														<c:if test="${c.resourceid eq syllabus.resourceid }">class="cur2"</c:if>
														href="${baseUrl }/resource/course/materesource.html?resType=${resType }&syllabusid=${c.resourceid}"
														title="${c.syllabusName }">${c.syllabusName }</a></li>
												</c:forEach>
											</ul>
										</c:if>
									</li>
								</c:forEach>
							</ul>
						</li>
					</c:forEach>
				</ul>
			</div>
			<!--end box-->
		</div>
		<!--end left-->

		<div id="right">
			<div class="position">
				当前位置：课程学习 >
				<c:if test="${syllabus.syllabusLevel eq 3 }">${syllabus.parent.parent.syllabusName } > </c:if>
				<c:if
					test="${syllabus.syllabusLevel eq 2 or syllabus.syllabusLevel eq 3 }">${syllabus.parent.syllabusName } > </c:if>${syllabus.syllabusName }</div>
			<div class="tip">
				<c:if test="${syllabus.syllabusLevel ne 1 }">
					<c:choose>
						<c:when test="${resType eq '0' }">
							<c:if test="${courseSyllabusVo.mateCount gt 0 }">
								<a
									href="${baseUrl }/resource/course/materesource.html?resType=1&syllabusid=${syllabus.resourceid}">视频</a>
							</c:if>
							<c:if test="${courseSyllabusVo.courseExamCount gt 0 }">
								<a
									href="${baseUrl }/resource/course/materesource.html?resType=2&syllabusid=${syllabus.resourceid}">习题</a>
							</c:if>
						</c:when>
						<c:when test="${resType eq '1' }">
							<c:if test="${courseSyllabusVo.handoutCount gt 0 }">
								<a
									href="${baseUrl }/resource/course/materesource.html?resType=0&syllabusid=${syllabus.resourceid}">讲义</a>
							</c:if>
							<c:if test="${courseSyllabusVo.courseExamCount gt 0 }">
								<a
									href="${baseUrl }/resource/course/materesource.html?resType=2&syllabusid=${syllabus.resourceid}">习题</a>
							</c:if>
						</c:when>
						<c:otherwise>
							<c:if test="${courseSyllabusVo.handoutCount gt 0 }">
								<a
									href="${baseUrl }/resource/course/materesource.html?resType=0&syllabusid=${syllabus.resourceid}">讲义</a>
							</c:if>
							<c:if test="${courseSyllabusVo.mateCount gt 0 }">
								<a
									href="${baseUrl }/resource/course/materesource.html?resType=1&syllabusid=${syllabus.resourceid}">视频</a>
							</c:if>
						</c:otherwise>
					</c:choose>
				</c:if>
			</div>
			<div class="clear"></div>
			<div id="content">
				<c:choose>
					<%-- 学习目标 --%>
					<c:when test="${syllabus.syllabusLevel eq 1 }">
						<c:set var="tmpType" value="" />
						<c:forEach items="${courseLearningGuidList }" var="c"
							varStatus="vs">
							<c:if test="${c.type ne courseLearningGuidType }">
								<c:if test="${c.type ne tmpType }">
									<c:set var="tmpType" value="${c.type }" />
									<h2>${ghfn:dictCode2Val('CodeCourseLearningGuidType',c.type) }</h2>
								</c:if>
								<div style="line-height: 150%; font-size: 14px; padding: 5px;">${c.content }</div>
							</c:if>
						</c:forEach>
					</c:when>
					<%-- 随堂练习 --%>
					<c:when test="${resType eq '2' }">
						<div class="stlx_tab">
							<div class="stlx_tab_over">随堂练习</div>
							<c:if test="${not empty mateTypeList }">
								<c:forEach items="${mateTypeList }" var="m">
									<div class="stlx_tab_out">
										<a
											href="${baseUrl }/resource/course/materesource.html?resType=3&mt=${m }&syllabusid=${syllabus.resourceid}&courseId=${course.resourceid }">${ghfn:dictCode2Val('CodeMateType',m) }</a>
									</div>
								</c:forEach>
							</c:if>
						</div>
						<div class="clear"></div>
						<%@ include
							file="/WEB-INF/jsp/edu3/resources/site/activecourseexam.jsp"%>
					</c:when>
					<%-- 练一练 --%>
					<c:when test="${resType eq '3' }">
						<div class="stlx_tab">
							<div class="stlx_tab_out">
								<a
									href="${baseUrl }/resource/course/materesource.html?resType=2&syllabusid=${syllabus.resourceid}&courseId=${course.resourceid }">随堂练习</a>
							</div>
							<c:if test="${not empty mateTypeList }">
								<c:forEach items="${mateTypeList }" var="m">
									<c:choose>
										<c:when test="${mt eq m }">
											<div class="stlx_tab_over">${ghfn:dictCode2Val('CodeMateType',m) }</div>
										</c:when>
										<c:otherwise>
											<div class="stlx_tab_out">
												<a
													href="${baseUrl }/resource/course/materesource.html?resType=3&mt=${m }&syllabusid=${syllabus.resourceid}&courseId=${course.resourceid }">${ghfn:dictCode2Val('CodeMateType',m) }</a>
											</div>
										</c:otherwise>
									</c:choose>
								</c:forEach>
							</c:if>
						</div>
						<div>
							<c:forEach items="${testMateList }" var="t" varStatus="vs">
								<c:if test="${t.mateType eq mt }">
									<iframe id="_testMateIframe${t.resourceid }"
										name="_testMateIframe${t.resourceid }"
										src="${baseUrl }/resource/course/transfer.html?url=${t.mateUrl }"
										scrolling="no" frameborder="0" width="100%" height="100%"></iframe>
								</c:if>
							</c:forEach>
						</div>
					</c:when>
					<%-- 学习材料 --%>
					<c:otherwise>
						<c:forEach items="${resPage.result }" var="mate">
							<c:choose>
								<%-- 网页、视频(网页) --%>
								<c:when test="${mate.mateType eq '6' or mate.mateType eq '8' }">
									<iframe id="_contentIframe" name="_contentIframe"
										src="${baseUrl }/resource/course/transfer.html"
										rel="${baseUrl }/resource/course/transfer.html?url=${mate.mateUrl }"
										scrolling="no" frameborder="0" width="100%"></iframe>
								</c:when>
								<%-- 视频 --%>
								<c:when
									test="${mate.mateType eq '2' or mate.mateType eq '3' or mate.mateType eq '4' }">
									<div style="text-align: center;">
										<%-- 如果是旧的视频格式，采用旧的播放方式 --%>
										<embed width='382' height='330' hidden='no' autostart='true'
											src='${mate.mateUrl }' />
									</div>
								</c:when>
								<c:when test="${mate.mateType eq '10' }">
									<%-- mp4视频播放 --%>
									<div style="text-align: center; padding-top: 20px;">
										<c:choose>
											<c:when test="${isIPadOrIphone eq 'Y' }">
												<a class="button" href="${mate.mateUrl }" target="_blank"
													title="${mate.mateName }"><span>播放</span></a>
											</c:when>
											<c:otherwise>
												<div style="display: block; width: 99%; height: 480px;"
													id="player2"></div>
												<script>
									  flowplayer("player2", "${baseUrl }/jscript/flowplayer/flowplayer-3.2.7.swf", {					
										  // configure clip to use "lighthttpd" plugin for providing video data
										  clip: {
											url: '${mate.mateUrl }',
											autoPlay:false , 
											url: '${mate.mateUrl }',
										    provider: 'lighttpd'
										  },					
										  // streaming plugins are configured normally under the plugins node
										  plugins: {
											  lighttpd: {
												  url: '${baseUrl }/jscript/flowplayer/flowplayer.pseudostreaming-3.2.7.swf'
											  }
										  }
									  });					     
							        </script>
											</c:otherwise>
										</c:choose>
									</div>
								</c:when>
								<%-- 其他文档 --%>
								<c:otherwise>
									<a href="${mate.mateUrl }" title="${mate.mateName }">${mate.mateName }</a>
								</c:otherwise>
							</c:choose>
						</c:forEach>
						<gh:page condition="${condition }" pageType="resource"
							page="${resPage }" />
					</c:otherwise>
				</c:choose>
			</div>
		</div>
		<!--end right-->
	</div>
	<div id="footer_tip">
		<div class="footer_sub1">
			<c:choose>
				<c:when test="${resType eq '0' }">
					<span class="footer_sub1_over">讲义</span>
					<a
						href="${baseUrl }/resource/course/materesource.html?resType=1&syllabusid=${syllabus.resourceid}&courseId=${course.resourceid }">授课</a>
					<a
						href="${baseUrl }/resource/course/materesource.html?resType=2&syllabusid=${syllabus.resourceid}&courseId=${course.resourceid }">习题</a>
				</c:when>
				<c:when test="${resType eq '1' }">
					<a
						href="${baseUrl }/resource/course/materesource.html?resType=0&syllabusid=${syllabus.resourceid}&courseId=${course.resourceid }">讲义</a>
					<span class="footer_sub1_over">授课</span>
					<a
						href="${baseUrl }/resource/course/materesource.html?resType=2&syllabusid=${syllabus.resourceid}&courseId=${course.resourceid }">习题</a>
				</c:when>
				<c:otherwise>
					<a
						href="${baseUrl }/resource/course/materesource.html?resType=0&syllabusid=${syllabus.resourceid}&courseId=${course.resourceid }">讲义</a>
					<a
						href="${baseUrl }/resource/course/materesource.html?resType=1&syllabusid=${syllabus.resourceid}&courseId=${course.resourceid }">授课</a>
					<span class="footer_sub1_over">习题</span>
				</c:otherwise>
			</c:choose>
		</div>
	</div>
	<!--右侧菜单-->
	<div id="floatTools" class="float0831">
		<div class="floatL">
			<a style="display: none;" id="aFloatTools_Show" class="btnOpen"
				title="学习工具" onclick="floatToolsBehavior('show');"
				href="javascript:void(0);">展开</a> <a id="aFloatTools_Hide"
				class="btnCtn" title="关闭学习工具" onclick="floatToolsBehavior('hide');"
				href="javascript:void(0);">收缩</a>
		</div>
		<div id="divFloatToolsView" class="floatR">
			<div class="tp"></div>
			<div class="cn">
				<ul>
					<li><span class="icoZx"><a href="javascript:void(0)"
							onclick="goCourseStudy(this,'note');">在线笔记</a></span></li>
					<li><span class="icoZx"><a href="javascript:void(0)"
							onclick="goCourseStudy(this,'ask');">随堂问答</a></span></li>
					<li><span class="icoZx"><a href="javascript:void(0)"
							onclick="goCourseStudy(this,'faq');">常见问题</a></span></li>
					<li><span class="icoZx"><a href="javascript:void(0)"
							onclick="goCourseStudy(this,'trace');">学习档案</a></span></li>
					<li><span class="icoZx"><a href="javascript:void(0)"
							onclick="goCourseStudy(this,'help');">帮助系统</a></span></li>
					<li><span class="icoZx"><a href="javascript:void(0)"
							onclick="goCourseStudy(this,'feedback');">反馈意见</a></span></li>
					<li><a class="icoTc" href="javascript:void(0)"
						onclick="goCourseStudy(this,'online');">在线答疑</a></li>
				</ul>
			</div>
		</div>
	</div>
	<!--end 右侧菜单-->
	<script type="text/javascript">
	function floatToolsBehavior(act){
		$('#divFloatToolsView').animate({width: act, opacity: act}, 'normal',function(){
			if(act=='show'){
				$('#divFloatToolsView').show();
			} else {
				$('#divFloatToolsView').hide();
			}
			$.cookie('RightFloatShown',(act=='show'?0:1));
		});
		if(act=='show'){
			$('#aFloatTools_Show').hide();
			$('#aFloatTools_Hide').show();
		} else {
			$('#aFloatTools_Show').show();
			$('#aFloatTools_Hide').hide();
		}		
	}
	
	$(function (){
		var cStatus = $.cookie('RightFloatShown');
		if(cStatus==0){
			$('#aFloatTools_Show').hide();
			$('#aFloatTools_Hide').show();	
			$('#divFloatToolsView').show();
		} else if(cStatus==1) {
			$('#aFloatTools_Show').show();
			$('#aFloatTools_Hide').hide();	
			$('#divFloatToolsView').hide();
		}
	});	
	function _learningNoteOnClose(){
		if($("#orgLearnigNoteContent").length>0){//学习笔记
			if($.trim($("#orgLearnigNoteContent").html()) != $.trim($("#learningNote_content").val())){//关闭学习笔记是内容未保存
				alert("您的学习笔记还未保存,请保存后再关闭.");
				return false;	
			}
		}
		return true;
	}
	function goCourseStudy(obj,type){
		var url,syllabusid = "${syllabus.resourceid}",courseId="${course.resourceid}",userid="${user.resourceid}",userType="${user.userType}";
		var title = $(obj).text();
		if(userid==""){
			alert("请登录之后再进行操作.");
			return false;
		}
		if(userType!="student"){
			alert("您不是学生,无法操作.");
			return false;
		}
		var _courseDialogOp = {id:'resource_course_dialog', draggable: true,autosize:false,resizable:true,maximizable:true,autocenter:false, autopos:'fixed',width:800,height:600, onClose: _learningNoteOnClose};
		var _courseDialogOp2 = {id:'resource_course_trace_dialog', draggable: true,autosize:false,resizable:true,maximizable:true,autocenter:false,autopos:'fixed',width:800,height:600};
		switch (type) {
		case 'note':
			if(syllabusid != ""){
				url = baseUrl+"/resource/course/note.html?syllabusid="+syllabusid;	
				Dialogs.load(url,_courseDialogOp).title("在线笔记");
			}
			break;
		case 'trace':
			if(courseId != ""){
				url = baseUrl+"/resource/course/learningtrace.html?courseId="+courseId;
				Dialogs.load(url,_courseDialogOp2).title("学习档案").show();
			}			
			break;
		case 'ask':
		case 'faq':	
		case 'feedback':
			if(type == 'ask' && syllabusid != ""){
				url = baseUrl+"/resource/course/ask.html?type=ask&syllabusid="+syllabusid;				
			} else 	if(type != 'ask' && courseId != ""){
				url = baseUrl+"/resource/course/ask.html?type="+type+"&courseId="+courseId;
			}
			if(url!=""){
				Dialogs.load(url,_courseDialogOp).title(title);
			}
			break;
		case 'help':
			window.open(baseUrl+"/portal/help/index.html", "v3_help");
			break;
		case 'online':
			var _courseop = {id:'onlinetip', draggable: true,autosize:false,resizable:false,maximizable:false,autocenter:false,autopos:'fixed',width:300,height:100};
			var tip = new Dialog(_courseop).html("<div style='text-align: left;text-indent: 28px;line-height:25px;font-size:14px;padding:5px;'>如需咨询，请添加<span class='t_blue t_strong'> QQ：1670615081</span>，添加时请注明教学点班级和姓名，谢谢。</div>").title(title).show();
			break;
		default:
			break;
		}
	}		
</script>
	<c:if
		test="${not empty user.resourceid and user.userType eq 'student' and (resType eq '0' or resType eq '1') and not empty syllabus.resourceid and syllabus.syllabusLevel ge 2 and not empty resPage.result }">
		<script type="text/javascript">
	$(function (){
		$.post(baseUrl+"/resource/course/learningtrace/log.html?resType=${resType}&syllabusid=${syllabus.resourceid}");	 
	});
</script>
	</c:if>
	<script type="text/javascript">
	if($('#_contentIframe').length>0){
		setTimeout(function (){
			$('#_contentIframe').attr('src',$('#_contentIframe').attr('rel')).iframeAutoHeight({minHeight:100});	 
		},1000);
	}	
</script>
	<script type="text/javascript">
if($("iframe[id^='_testMateIframe']").length>0){
	$("iframe[id^='_testMateIframe']").iframeAutoHeight();	
}
</script>
</body>
</html>
