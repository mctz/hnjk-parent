<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>${school}${schoolConnectName}- 论坛</title>
<gh:loadCom components="bbs-defaultcss,jquery" />
<style type="text/css">
<!--
.STYLE1 {
	color: #FFFFFF;
	font-weight: bold;
}

.STYLE2 {
	color: #999999
}

#topicbody .nav_topic {
	height: 270px;
}
-->
</style>
<script type="text/javascript">
		function NewsSpanBar() {
			this.f = 1;
			this.event = "onmouseover";
			this.titleid = "#topic_bot";
			this.bodyid = "#topicbody";
			this.class_dis = "dis";
			this.class_undis = "undis";
			this.class_hiton = "tabgroup_on";
			this.class_hitno = "tabgroup";
			var Tags, TagsCnt, len, flag;
			var BClassName;
			this.load = function() {			
				if (!$(this.titleid) || !$(this.bodyid)) {
					return false;
				}
				flag = this.f;				
				BClassName = [this.class_dis, this.class_undis, this.class_hiton,
						this.class_hitno];				
				Tags = $(this.titleid).find("p").get();
				TagsCnt = $(this.bodyid).find("dl").get();				
				len = Tags.length;				
				for (var i = 0; i < len; i++) {
					Tags[i].value = i;
					if (this.event != "click") {					
						Tags[i].onmouseover = function() {
							changeNav(this.value)
						};
					} else {
						Tags[i].onclick = function() {
							changeNav(this.value)
						};
					}
					TagsCnt[i].className = BClassName[1]; // display:none
				}
				Tags[flag].className = BClassName[3];
				TagsCnt[flag].className = BClassName[0]; // display:block
			}
			function changeNav(v) {
				Tags[flag].className = BClassName[2];
				TagsCnt[flag].className = BClassName[1]; // 把上一步显示的标签隐掉
				flag = v;
				Tags[v].className = BClassName[3];
				TagsCnt[v].className = BClassName[0];
			}
		}
		$(function (){
			ajaxGetDigestAndNewTopic();
			
			var newtopictab = new NewsSpanBar();
		    newtopictab.f=0;
		    newtopictab.load();
		    
			//$("#topicbody .nav_topic").height(270);	
		});		
		//搜索
		function searchBbsTopics(){
			var keyword = $("#searchTopicKeyword").val();			
			if($.trim(keyword)==""){
				alert("关键字不能为空！");
				return false;
			} 
			return true;
		}
		
		function ajaxGetDigestAndNewTopic(){			
			var url = "${baseUrl}/edu3/${course.isQualityResource eq 'Y' ? 'course':'learning' }/bbs/topic/ajax.html";
			jQuery.post(url,{courseId:'${course.resourceid}'},function (json){
				var topictypes = ['digestTopics','hotTopics','newTopics'];
				var search = ['digest','hot','new'];
				var courseId = '${course.resourceid }';
				for(var i in topictypes){					
					var $ul = $("<ul></ul>");
					eval("var topicmap=json."+topictypes[i]);
					for(var index in topicmap){
						var obj = topicmap[index];
						var $li = $("<li></li>");
						var $title = obj.title;
						if(obj.groupName){
							$title += "("+obj.groupName+")";
						} 
						$title += "("+obj.cnname+")";						
						var url1 = "${baseUrl }/edu3/${course.isQualityResource eq 'Y' ? 'course':'learning' }/bbs/topic.html?topicId="+obj.resourceid;
						if(courseId!=""){
							url1 += "&courseId="+courseId; 
						}
						$li.append("<a title='"+$title+"' href='"+url1+"' target='_blank'>"+obj.title+"</a>("+obj.cnname+")<br/>");
						$ul.append($li);
					}					
					$("#"+topictypes[i]).append($ul);
					if(topicmap.length>0){
						var url2 = "${baseUrl }/edu3/${course.isQualityResource eq 'Y' ? 'course':'learning' }/bbs/search.html?search="+search[i];
						if(courseId!=""){
							url2 += "&courseId="+courseId; 
						}
						$("#"+topictypes[i]).append("<li style='text-align: right; padding-right: 10px;'><a href='"+url2+"' target='_blank'>&gt;&gt;更多...</a></li>");
					}
				}
			},"json");
		}
	</script>
</head>
<body>
	<%@ include file="/WEB-INF/jsp/edu3/learning/bbssection/bbs-header.jsp"%>


	<!--right side-->
	<DIV class=nav_top id=centerLayout3>

		<DIV id=right>
			<%-- 登录框
			<c:if test="${empty user }">
			<DIV class=tabtitle>
				<DIV class=search_bot>
					<P class=tab_search>
						登陆
					</P>
					<DIV style="CLEAR: both"></DIV>
				</DIV>
				<DIV class=searchbody>
					<FORM action=login.asp?action=chk method=post>
						<DIV style="LINE-HEIGHT: 20px">
							<TABLE>
								<TBODY>
									<TR>
										<TD style="WIDTH: 100px; TEXT-ALIGN: right">
											用户帐号：
										</TD>
										<TD style="WIDTH: 150px; TEXT-ALIGN: left">
											<INPUT tabIndex=4 size=18 name=username type="text">
										</TD>
									</TR>
									<TR>
										<TD style="WIDTH: 100px; TEXT-ALIGN: right">
											用户密码：
										</TD>
										<TD style="WIDTH: 150px; TEXT-ALIGN: left">
											<INPUT tabIndex=5 type=password size=18 value=""
												name=password>
										</TD>
									</TR>
									<TR>
										<TD style="WIDTH: 100px; TEXT-ALIGN: right">
											验证码：
										</TD>
										<TD style="WIDTH: 150px; TEXT-ALIGN: left">
											<INPUT tabIndex=6 size=4 name=codestr>
											验证码
										</TD>
									</TR>
								</TBODY>
							</TABLE>
							<INPUT tabIndex=8 type=submit value=" 登 录 " name=submit>
							[
							<A href="#">忘记密码</A> ]
							<BR>
						</DIV>
					</FORM>
				</DIV>				
			</DIV>	
			</c:if>		
			 --%>
			<BR>
			<!--登录入口完-->
			<DIV class=tabtitle>
				<DIV class=search_bot id=search_bot>
					<P class=tab_search value="0">搜索</P>
					<DIV style="CLEAR: both"></DIV>
				</DIV>
				<DIV class=searchbody id=searchbody>
					<DL class=dis>
						<form
							action="${baseUrl }/edu3/${course.isQualityResource eq 'Y' ? 'course':'learning' }/bbs/search.html"
							method="get" onsubmit="return searchBbsTopics();">
							<input type="text" id="searchTopicKeyword" name="keyword" /> <input
								type="hidden" name="sType" value="title" /> <input
								type="hidden" name="courseId" value="${course.resourceid }" />
							<BUTTON type="submit">搜 索</BUTTON>
						</form>
					</DL>
				</DIV>
			</DIV>
			<BR>
			<!--搜索完-->
			<DIV class=tabtitle>
				<DIV class=topic_bot id=topic_bot>
					<P class=tabgroup value="0">最新主题</P>
					<P class=tabgroup_on value="1">热门主题</P>
					<P class=tabgroup_on value="2">精华主题</P>
				</DIV>
				<DIV class=topicbody id=topicbody>
					<DL class=dis>
						<DIV class=nav_topic id="newTopics">
							<%-- 
							<UL>
								<c:forEach items="${newTopics }" var="newTopic">
									<LI>
										<A title="${newTopic.title}(${newTopic.fillinMan })" href="${baseUrl }/edu3/learning/bbs/topic.html?courseId=${course.resourceid }&topicId=${newTopic.resourceid }" target=_blank>
											<c:choose>
												<c:when test="${fn:length(newTopic.title)>10}">${fn:substring(newTopic.title,0,10)}...</c:when>
												<c:otherwise>${newTopic.title}</c:otherwise>
											</c:choose>
										</A>
										 (${newTopic.fillinMan })
										<BR>
									</LI>
								</c:forEach>								
							</UL>
							<c:if test="${not empty newTopics }"><li style="text-align: right; padding-right: 10px;"><a href="${baseUrl }/edu3/learning/bbs/search.html?courseId=${course.resourceid }&search=new" target="_blank">&gt;&gt;更多...</a></li></c:if>
							 --%>
						</DIV>
					</DL>
					<DL class=undis>
						<DIV class=nav_topic id="hotTopics">
							<%-- 
							<UL>
								<c:forEach items="${hotTopics }" var="hotTopic">
									<LI>
										<A title="${hotTopic.title}<c:if test="${not empty hotTopic.bbsGroup }">(${hotTopic.bbsGroup.groupName })</c:if>(${hotTopic.fillinMan })" href="${baseUrl }/edu3/learning/bbs/topic.html?courseId=${course.resourceid }&topicId=${hotTopic.resourceid }" target=_blank>
											<c:choose>
												<c:when test="${fn:length(hotTopic.title)>10}">${fn:substring(hotTopic.title,0,10)}...</c:when>
												<c:otherwise>${hotTopic.title}</c:otherwise>
											</c:choose>
										</A>
										(${hotTopic.fillinMan })
										<BR>
									</LI>
								</c:forEach>								
							</UL>
							<c:if test="${not empty hotTopics }"><li style="text-align: right; padding-right: 10px;"><a href="${baseUrl }/edu3/learning/bbs/search.html?courseId=${course.resourceid }&search=hot" target="_blank">&gt;&gt;更多...</a></li></c:if>
							 --%>
						</DIV>
					</DL>
					<DL class=undis>
						<DIV class=nav_topic id="digestTopics">
							<%--  
							<UL>
								<c:forEach items="${digestTopics }" var="digestTopic">
									<LI>
										<A title="${digestTopic.title}<c:if test="${not empty digestTopic.bbsGroup }">(${digestTopic.bbsGroup.groupName })</c:if>(${digestTopic.fillinMan })" href="${baseUrl }/edu3/learning/bbs/topic.html?courseId=${course.resourceid }&topicId=${digestTopic.resourceid }" target=_blank>
											<c:choose>
												<c:when test="${fn:length(digestTopic.title)>10}">${fn:substring(digestTopic.title,0,10)}...</c:when>
												<c:otherwise>${digestTopic.title}</c:otherwise>
											</c:choose>
										</A>
										(${digestTopic.fillinMan }) 
										<BR>
									</LI>
								</c:forEach>
							</UL>
							<c:if test="${not empty digestTopics }"><li style="text-align: right; padding-right: 10px;"><a href="${baseUrl }/edu3/learning/bbs/search.html?courseId=${course.resourceid }&search=digest" target="_blank">&gt;&gt;更多...</a></li></c:if>
							 --%>
						</DIV>
					</DL>
				</DIV>
			</DIV>
			<BR>
			<!--热门帖子完-->

		</DIV>

		<!--帖子版块区-->
		<DIV id=left></DIV>
		<DIV id=center1></DIV>
		<DIV id=center2>
			<c:forEach items="${parentBbsSections }" var="parentbbsSection">
				<!-- 顶级版块开始 -->
				<DIV class=bbs_column2>
					<H1>
						<c:choose>
							<c:when test="${not empty course }">${parentbbsSection.sectionName }版块</c:when>
							<c:otherwise>
								<A title="进入本分类论坛"
									href="${baseUrl }/edu3/${course.isQualityResource eq 'Y' ? 'course':'learning' }/bbs/section.html?sectionId=${parentbbsSection.resourceid }${coursequerystr }"
									target="_blank">${parentbbsSection.sectionName }</A>
							</c:otherwise>
						</c:choose>
					</H1>
					<UL>
						<c:forEach items="${parentbbsSection.childs }" var="bbsSection"
							varStatus="vs">
							<%-- 只显示可见的论坛 --%>
							<c:if test="${bbsSection.isVisible ne 'N' }">
								<!--子版块-->
								<LI style="WIDTH: 48%">
									<DL class="${(bbsSection.todayCount==0)?'today':'todaynew' }">
										<DT>${bbsSection.todayCount }
										<DD>今日帖</DD>
									</DL>
									<DIV>
										<a
											href="${baseUrl }/edu3/${course.isQualityResource eq 'Y' ? 'course':'learning' }/bbs/section.html?sectionId=${bbsSection.resourceid }${coursequerystr }">『
											${bbsSection.sectionName } 』</a>
									</DIV> <SPAN>主题：${bbsSection.statTopicCount }&nbsp;&nbsp;
										|&nbsp;&nbsp;帖子：${bbsSection.invitationCount }<BR> <c:choose>
											<c:when test="${empty bbsSection.masterName }">此版暂无版主</c:when>
											<c:otherwise>
												<c:forEach items="${fn:split(bbsSection.masterId, ',')}"
													var="mid">
													<a
														href="${baseUrl }/edu3/learning/bbs/user.html?userid=${mid}${coursequerystr }"
														target="_blank"
														title="查看版主${ghfn:ids2Names('user',mid)}的资料">${ghfn:ids2Names('user',mid)}</a>
						            		&nbsp;
						            	</c:forEach>
											</c:otherwise>
										</c:choose>
								</SPAN> <c:if test="${vs.last }">
										<!--如果最后一个版块,需要加上以下这句-->
										<DIV style="CLEAR: both"></DIV>
									</c:if>
								</LI>
							</c:if>
							<!--子版块1完-->
						</c:forEach>
					</UL>
				</DIV>
				<DIV style="CLEAR: both"></DIV>
				<!--顶级版块完-->
			</c:forEach>

		</DIV>
	</DIV>

	<DIV style="MARGIN-TOP: 30px; MARGIN-BOTTOM: 10px"></DIV>
	<DIV class=copyright>
		<DIV id=stylemsg style="PADDING-RIGHT: 5px; COLOR: red"></DIV>
		<DIV>
			<DIV>
				Copyright &copy; 2001-2012 ${school}${schoolConnectName}All Rights
				Reserved &nbsp;&nbsp;
				<gh:version />
			</DIV>
			<DIV>
				<BR>
			</DIV>
		</DIV>
	</DIV>
</body>
</html>