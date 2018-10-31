<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/edu3/learning/bbssection/bbs-header.jsp"%>
<script type="text/javascript">
		$(function() {
			ajaxUpdateTopicClickCount();
			
	  		KE.init({
		      id : 'replyTextarea',	
		      resizeMode : 0,  		             
		      imageUploadJson:baseUrl+'/edu3/framework/filemanage/editor/upload.html?textAreaId=replyTextarea&storePath=users,${storeDir},images',//一定要跟storePath参数，指定文件存储的位置	       
		      allowFileManager:true,
		      fileManagerJson:baseUrl+'/edu3/framework/filemanage/editor/browser.html' ,
		      rootPath:'users,${storeDir},images',
		      afterCreate : function(id) {
		      		KE.util.focus(id);
					KE.event.ctrl(document, 13, function() {
						KE.util.setData(id);
						$("#bbsRepySubmit").click();
					});
					KE.event.ctrl(KE.g[id].iframeDoc, 13, function() {
						KE.util.setData(id);						
						$("#bbsRepySubmit").click();//按 Ctrl+Enter发布  
					});
				}         
	  		});		  		  		   	       	        
	   	});  
		
		function ajaxUpdateTopicClickCount(){
			var url = "${baseUrl }/edu3/${course.isQualityResource eq 'Y' ? 'course':'learning' }/bbs/topic/update/ajax.html";
			jQuery.post(url,{type:'topic',topicId:'${bbsTopic.resourceid }'});
		}
		
		function fontSize(type,pid){
			var size = parseFloat($("#"+pid).css("font-size"));	
			if(type=='m'){	
				size = (size-2==0)?12:(size-2);									
				$("#"+pid).css({"font-size":size+"px","line-height":size+"px"});
			}	
			if(type=='b'){	
				size = size+2;										
				$("#"+pid).css({"font-size":size+"px","line-height":size+"px"});
			}	
		}
		
		function deleteTopic(topicId){
 			var url = baseUrl+"/edu3/learning/bbs/topic/remove.html";
 			var parentSectionUrl = "${baseUrl }/edu3/${course.isQualityResource eq 'Y' ? 'course':'learning' }/bbs/section.html?courseId=${course.resourceid }&sectionId=${bbsTopic.bbsSection.resourceid }";
 			$.get(url,{resourceid:topicId},function (json){
 				if(json.statusCode == 200){	
 					alert(json.message); 							 					
					window.location.href = parentSectionUrl;
				} else {
					alert(json.message); 
				}										
 			},"json");
 		}
 		function statusTopic(topicId,status){
 			var url = baseUrl+"/edu3/learning/bbs/topic/status.html";
 			$.get(url,{resourceid:topicId,status:status},function (json){
 				if(json.statusCode == 200){
 					var message = "";	
 					switch(status){
 						case -3:
 							message = "帖子已取消置顶!";
 							break;
 						case -1:
 							message = "帖子已锁定!";
 							break;
 						case 0:
 							message = "帖子已还原为普通帖!";
 							break;
 						case 1:
 							message = "帖子已设置为精华!";
 							break;
 						case 3:
 							message = "帖子已置顶!";
 							break;
 					}	
 					alert(json.message+message);						 					
					window.location.reload();
				} else {
					alert(json.message);
				}																						
 			},"json");
 		}
 		
 		function isBest(rid,courseId,isBest,type){
 			var url = baseUrl+"/edu3/learning/bbs/isbest.html";
 			$.get(url,{resourceid:rid,courseId:courseId,isBest:isBest,type:type},function (json){
 				if(json.statusCode == 200){	
 					var m = "";
 					if(isBest=='Y'){
 						m = "帖子已评为优秀帖!";
 					} else {
 						m = "帖子已取消优秀帖!";
 					}
 					alert(json.message + m); 							 					
					window.location.reload();
				} else {
					alert(json.message); 
				}										
 			},"json");
 		}
 		
 		function deleteReply(replyId){
 			var url = baseUrl+"/edu3/learning/bbs/reply/remove.html";
 			$.get(url,{resourceid:replyId},function (json){
 				if(json.statusCode == 200){	
 					alert(json.message);							 					
					window.location.reload();
				} else {
					alert(json.message);
				}									
 			},"json");
 		}
	</script>
<DIV id=topLayout>
	<!--导航-->
	<DIV class=notice>
		<DIV id="top"
			style="PADDING-LEFT: 10px; FLOAT: left; WIDTH: auto; TEXT-ALIGN: left"
			class="STYLE1">
			<A
				href="${baseUrl }/edu3/${course.isQualityResource eq 'Y' ? 'course':'learning' }/bbs/index.html${querys}"
				class="STYLE1"> <c:choose>
					<c:when test="${empty course}">广东学苑在线</c:when>
					<c:otherwise>${course.courseName }课程论坛</c:otherwise>
				</c:choose>
			</A>
			<c:forEach items="${bbsSections }" var="section" varStatus="vs">
						→ <A
					href="${baseUrl }/edu3/${course.isQualityResource eq 'Y' ? 'course':'learning' }/bbs/section.html?sectionId=${section.resourceid }${coursequerystr }"
					class="STYLE1"> ${section.sectionName }</A>
			</c:forEach>
			→
			<c:choose>
				<c:when test="${not empty bbsTopic.bbsGroup }">
					<a class="STYLE1"
						href="${baseUrl }/edu3/${course.isQualityResource eq 'Y' ? 'course':'learning' }/bbs/topic.html?topicId=${bbsTopic.parenTopic.resourceid }${coursequerystr }">${bbsTopic.parenTopic.title }</a>
				 	→ ${bbsTopic.bbsGroup.groupName }
				 	</c:when>
				<c:otherwise>
					<a class="STYLE1"
						href="${baseUrl }/edu3/${course.isQualityResource eq 'Y' ? 'course':'learning' }/bbs/topic.html?topicId=${bbsTopic.resourceid }${coursequerystr }">${bbsTopic.title }</a>
				</c:otherwise>
			</c:choose>
		</DIV>
	</DIV>
</DIV>


<!--用户操作按钮-->
<DIV class=main style="MARGIN-TOP: 4px; LINE-HEIGHT: 28px; HEIGHT: 28px">
	<c:if
		test="${(not empty user) and (empty bbsTopic.endTime) and (bbsTopic.bbsSection.sectionCode ne questionSectionCode) and (user.userType ne 'student' or bbsTopic.bbsSection.isReadonly ne 'Y')}">
		<DIV class=nav>
			<UL>
				<LI class=menu2 onmouseover="this.className='menu1'"
					onmouseout="this.className='menu2'"><A href="javascript:;">&nbsp;&nbsp;我要发表</A>
					<DIV class=postlist>
						<A title=发表一个新帖子
							href="${baseUrl }/edu3/learning/bbs/newtopic.html?sectionId=${bbsTopic.bbsSection.resourceid }${coursequerystr }"><SPAN><IMG
								src="${baseUrl }/style/default/images/new_topic.gif" border=0>
						</SPAN>新的主题</A> <BR>
					</DIV></LI>
			</UL>
		</DIV>
	</c:if>
	<%-- 锁定帖不给回复 --%>
	<c:if
		test="${(bbsTopic.status ne -1) and (not empty user) and ((empty bbsTopic.endTime)or(not empty bbsTopic.bbsGroup)) and (bbsTopic.bbsSection.isReadonly ne 'Y')}">
		<DIV class=post_button
			style="margin-left: ${((not empty bbsTopic.bbsGroup) or (bbsTopic.bbsSection.sectionCode eq questionSectionCode))?'0':'86' }px;">
			<c:set
				value="${(bbsReplyPage.totalCount%bbsReplyPage.pageSize==0)?bbsReplyPage.totalPages+1:bbsReplyPage.totalPages }"
				var="replyPageNum"></c:set>
			<A title=回复贴子
				href="${baseUrl }/edu3/learning/bbs/reply.html?topicId=${bbsTopic.resourceid }${coursequerystr }&pageNum=${(replyPageNum eq -1)?1:replyPageNum}">回复贴子</A>
		</DIV>
	</c:if>
	<c:if
		test="${(bbsTopic.status eq -1) and (not empty bbsTopic.endTime)}">
		<marquee direction=left scrollamount=6 onmouseover="this.stop()"
			onmouseout="this.start()"
			style="color: green; font-size: 16px; line-height: 170%;">话题讨论已经结束！</marquee>
	</c:if>
	<DIV style="FLOAT: right">
		您是本帖的第 <B>${bbsTopic.clickCount }</B> 个阅读者
	</DIV>
</DIV>

<!--帖子内容-->
<DIV class=th>
	<DIV style="TEXT-INDENT: 10px; HEIGHT: 24px; TEXT-ALIGN: left">
		标题： <FONT color=green>${bbsTopic.title }</FONT>
	</DIV>
</DIV>
<c:if test="${bbsReplyPage.pageNum eq 1 }">
	<DIV class=postlary1>
		<TABLE style="TABLE-LAYOUT: fixed; WIDTH: 100%" cellSpacing=0
			cellPadding=0 border=0>
			<TBODY>
				<TR>
					<TD class=td_a vAlign=top width="22%" rowSpan=2>
						<DIV
							style="PADDING-RIGHT: 0px; PADDING-LEFT: 5px; PADDING-BOTTOM: 0px; LINE-HEIGHT: 30px; PADDING-TOP: 10px; HEIGHT: 30px">
							<DIV
								style="FILTER: glow(color = '#A6BA98', strength = '2'); FLOAT: left; WIDTH: 105px">
								<!-- FONT face=Verdana color=#61b713><B>${bbsTopic.bbsUserInfo.userName}</B>
									</FONT> -->
							</DIV>
							<DIV style="MARGIN-TOP: 3px; FLOAT: left; WIDTH: 23px"></DIV>
							<DIV
								style="MARGIN-TOP: 8px; FLOAT: left; MARGIN-LEFT: 5px; WIDTH: 15px"></DIV>
						</DIV>
						<DIV>
							<c:choose>
								<c:when test="${not empty bbsTopic.bbsUserInfo.userface }">
									<img src="${bbsTopic.bbsUserInfo.userface }" width="55"
										height="55" />
								</c:when>
								<c:otherwise>
									<img src="${baseUrl }/themes/default/images/person.png"
										width="55" height="55" />
								</c:otherwise>
							</c:choose>
						</DIV>
						<DIV>
							<IMG style="MARGIN: 5px 0px"
								src="${baseUrl }/style/default/images/start0.gif">
						</DIV> <c:if test="${not empty bbsTopic.bbsUserInfo.studentInfo}">
							<DIV>学号：${bbsTopic.bbsUserInfo.studentInfo.studyNo}</DIV>
							<DIV>
								年级：${bbsTopic.bbsUserInfo.studentInfo.grade.gradeName}</DIV>
							<DIV>
								专业：${bbsTopic.bbsUserInfo.studentInfo.major.majorName}</DIV>
						</c:if>
						<DIV>姓名：${bbsTopic.bbsUserInfo.sysUser.cnName}</DIV>
						<DIV>发帖数量：${bbsTopic.bbsUserInfo.topicCount}</DIV>
						<DIV>
							教学站：
							<c:choose>
								<c:when test="${not empty bbsUserInfo.studentInfo}">${bbsTopic.bbsUserInfo.studentInfo.branchSchool.unitName}</c:when>
								<c:otherwise>${bbsTopic.bbsUserInfo.sysUser.orgUnit.unitName}</c:otherwise>
							</c:choose>
						</DIV>
					</TD>
					<TD class=td_b style="OVERFLOW: hidden; LINE-HEIGHT: normal"
						vAlign=top width="82%">
						<DIV>
							<DIV class=user_menu_info>
								<c:if test="${bbsTopic.topLevel gt 0 }">
									<font color="green" style="border: 1px solid #B7D3EA;">置顶帖</font>
								</c:if>
								<font color="green" style="border: 1px solid #B7D3EA;"> <c:choose>
										<c:when test="${bbsTopic.status eq 1 }">精华帖</c:when>
										<c:when test="${bbsTopic.status eq -1 }">锁定帖</c:when>
										<c:otherwise>普通帖</c:otherwise>
									</c:choose>
								</font>
								<c:if test="${bbsTopic.isBest eq 'Y' }">
									<font color="green" style="border: 1px solid #B7D3EA;">优秀帖</font>
								</c:if>
								<DIV style="FLOAT: right; COLOR: #333">楼主</DIV>
								<DIV class=text_style style="FLOAT: right">
									<A onclick="fontSize('m','${bbsTopic.resourceid }')"
										href="javascript:void(0);">小</A> <A
										onclick="fontSize('b','${bbsTopic.resourceid }')"
										href="javascript:void(0);">大</A>
								</DIV>
							</DIV>
						</DIV>
						<div id="${bbsTopic.resourceid }"
							style="min-height: 150px; font-size: 9pt; text-indent: 24px; margin-top: 20px;">
							${bbsTopic.content }</div>
					</TD>
				</TR>
				<TR>
					<TD class=td_d vAlign=bottom width="82%"><c:if
							test="${not empty bbsTopic.attachs}">
							<div
								style="margin-left: 20px; border: 1px solid #E6E6E2; margin-bottom: 6px; padding: 0.5em 1em 0.3em 3em;">
								<c:choose>
									<c:when test="${not empty user }">
										<div>附件：</div>
										<c:forEach items="${bbsTopic.attachs}" var="attach"
											varStatus="vs">
											<li><img style="cursor: pointer; height: 10px;"
												src="${baseUrl}/jscript/jquery.uploadify/images/attach.png" />&nbsp;&nbsp;
												<a
												onclick="downloadAttachFile('${attach.resourceid }');return false;"
												href="${baseUrl }/edu3/framework/filemanage/download.html?id=${attach.resourceid }">${attach.attName }&nbsp;(${ghfn:formatFileSize(attach.attSize) })</a>
											</li>
										</c:forEach>
									</c:when>
									<c:otherwise>
										<div style="color: #FF3A00;">无法查看附件，请先登录！</div>
									</c:otherwise>
								</c:choose>
							</div>
						</c:if>
						<DIV id=sigline_1
							style="CLEAR: both; FLOAT: left; OVERFLOW-X: hidden; WIDTH: 85%; TEXT-ALIGN: left">
							<IMG src="${baseUrl }/style/default/images/topic_desin.gif">
							<BR> ${bbsTopic.bbsUserInfo.desinger }
						</DIV> <BR>
						<DIV class=info>
							<UL class=info>
								<LI></LI>
							</UL>
						</DIV></TD>
				</TR>
				<TR height=20>
					<TD style="PADDING-LEFT: 20px" vAlign=center width="22%">
						<DIV style="PADDING-LEFT: 2px; FLOAT: left; TEXT-INDENT: 12px">
							<fmt:formatDate value="${bbsTopic.fillinDate }"
								pattern="yyyy-MM-dd HH:mm:ss" />
						</DIV>
					</TD>
					<TD class=td_c
						style="PADDING-RIGHT: 6px; PADDING-LEFT: 6px; PADDING-BOTTOM: 6px; PADDING-TOP: 6px; TEXT-ALIGN: left"
						vAlign=center width="82%"><c:if test="${not empty user }">
							<%-- 锁定帖与讨论话题父贴不可回复 --%>
							<c:if
								test="${(bbsTopic.status ne -1) and ((empty bbsTopic.endTime)or(not empty bbsTopic.bbsGroup)) and (bbsTopic.bbsSection.isReadonly ne 'Y')}">
								<A style="color: #DD5E10;" title="回复贴子"
									href="${baseUrl }/edu3/learning/bbs/reply.html?topicId=${bbsTopic.resourceid }${coursequerystr }&pageNum=${((empty replyPageNum)or (replyPageNum < 1))?1:replyPageNum}">回复</A>
							</c:if>
							<%-- 版主或发帖人可编辑 --%>
							<c:if
								test="${(fn:contains(bbsTopic.bbsSection.masterId,user.resourceid) or (user.resourceid eq bbsTopic.bbsUserInfo.sysUser.resourceid)) and (empty bbsTopic.bbsGroup) }">
								<A style="color: #DD5E10;" title="编辑主题"
									href="${baseUrl }/edu3/learning/bbs/newtopic.html?topicId=${bbsTopic.resourceid }${coursequerystr }">编辑</A>
							</c:if>
							<%-- 版主或发帖人可编辑 --%>
							<c:if
								test="${fn:contains(bbsTopic.bbsSection.masterId,user.resourceid) }">
								<a style="color: #DD5E10;" title="删除主题" href="javascript:;"
									onclick="deleteTopic('${bbsTopic.resourceid }');return false;">删除主题</a>
								<c:choose>
									<c:when test="${bbsTopic.topLevel eq 0 }">
										<a style="color: #DD5E10;" title="置顶" href="javascript:;"
											onclick="statusTopic('${bbsTopic.resourceid }',3);return false;">置顶</a>
									</c:when>
									<c:otherwise>
										<a style="color: #DD5E10;" title="取消置顶" href="javascript:;"
											onclick="statusTopic('${bbsTopic.resourceid }',-3);return false;">取消置顶</a>
									</c:otherwise>
								</c:choose>
								<c:choose>
									<c:when test="${bbsTopic.status eq -1 }">
										<a style="color: #DD5E10;" title="精华" href="javascript:;"
											onclick="statusTopic('${bbsTopic.resourceid }',1);return false;">精华</a>
										<a style="color: #DD5E10;" title="解除锁定" href="javascript:;"
											onclick="statusTopic('${bbsTopic.resourceid }',0);return false;">解除锁定</a>
									</c:when>
									<c:when test="${bbsTopic.status eq 1 }">
										<a style="color: #DD5E10;" title="撤消精华" href="javascript:;"
											onclick="statusTopic('${bbsTopic.resourceid }',0);return false;">撤消精华</a>
										<a style="color: #DD5E10;" title="锁定" href="javascript:;"
											onclick="statusTopic('${bbsTopic.resourceid }',-1);return false;">锁定</a>
									</c:when>
									<c:otherwise>
										<a style="color: #DD5E10;" title="精华" href="javascript:;"
											onclick="statusTopic('${bbsTopic.resourceid }',1);return false;">精华</a>
										<a style="color: #DD5E10;" title="锁定" href="javascript:;"
											onclick="statusTopic('${bbsTopic.resourceid }',-1);return false;">锁定</a>
									</c:otherwise>
								</c:choose>

								<span style="color: green;">(更多管理操作请到后台进行)</span>

							</c:if>
						</c:if>
						<DIV style="FLOAT: right; MARGIN-RIGHT: 5px; HEIGHT: 26px">
							<a href="#"
								onclick="javascript:window.scrollTo(0,0);return false;"> <IMG
								style="BORDER-TOP-WIDTH: 0px; BORDER-LEFT-WIDTH: 0px; BORDER-BOTTOM-WIDTH: 0px; BORDER-RIGHT-WIDTH: 0px"
								alt=回到顶部
								src="${baseUrl }/style/default/images/topic_backtop.gif">
							</a>
						</DIV> <c:if
							test="${not empty bbsTopic.course and user.userType eq 'edumanager' }">
							<div style="FLOAT: right; MARGIN-RIGHT: 10px;">
								<c:choose>
									<c:when test="${bbsTopic.isBest ne 'Y' }">
										<a style="color: #DD5E10;" title="评优" href="javascript:;"
											onclick="isBest('${bbsTopic.resourceid }','${bbsTopic.course.resourceid }','Y','1');return false;">评优</a>
									</c:when>
									<c:otherwise>
										<a style="color: #DD5E10;" title="取消评优" href="javascript:;"
											onclick="isBest('${bbsTopic.resourceid }','${bbsTopic.course.resourceid }','N','1');return false;">取消评优</a>
									</c:otherwise>
								</c:choose>
							</div>
						</c:if></TD>
				</TR>
			</TBODY>
		</TABLE>
	</DIV>
</c:if>
<c:forEach items="${bbsReplyPage.result }" var="bbsReply" varStatus="vs">
	<DIV class=postlary${(vs.index%2==0)?2:1}>
			<TABLE style="TABLE-LAYOUT: fixed; WIDTH: 100%" cellSpacing=0
				cellPadding=0 border=0>
				<TBODY>
					<TR>
						<TD class=td_a vAlign=top width="22%" rowSpan=2>
							<DIV
								style="PADDING-RIGHT: 0px; PADDING-LEFT: 5px; PADDING-BOTTOM: 0px; LINE-HEIGHT: 30px; PADDING-TOP: 10px; HEIGHT: 30px">
								<DIV
									style="FILTER: glow(color='#A6BA98',strength='2'); FLOAT: left; WIDTH: 105px">
									<!--  FONT face=Verdana color=#61b713><B>${bbsReply.bbsUserInfo.userName }</B>
									</FONT>-->
								</DIV>
								<DIV style="MARGIN-TOP: 3px; FLOAT: left; WIDTH: 23px"></DIV>
								<DIV
									style="MARGIN-TOP: 8px; FLOAT: left; MARGIN-LEFT: 5px; WIDTH: 15px">
								</DIV>
							</DIV>
							<DIV>
								<c:choose>
									<c:when test="${not empty bbsReply.bbsUserInfo.userface }">
										<img src="${bbsReply.bbsUserInfo.userface }" width="55" height="55"/>
									</c:when>
									<c:otherwise>
									<img src="${baseUrl }/themes/default/images/person.png" width="55" height="55"/>
									</c:otherwise>
								</c:choose>	
							</DIV>
							<DIV>
								<IMG style="MARGIN: 5px 0px"
									src="${baseUrl }/style/default/images/start0.gif">
							</DIV>							
							<c:if test="${not empty bbsReply.bbsUserInfo.studentInfo}">
								<DIV>
									学号：${bbsReply.bbsUserInfo.studentInfo.studyNo}
								</DIV>
								<DIV>
									年级：${bbsReply.bbsUserInfo.studentInfo.grade.gradeName}
								</DIV>
								<DIV>
									专业：${bbsReply.bbsUserInfo.studentInfo.major.majorName}
								</DIV>
							</c:if>
							<DIV>
								姓名：${bbsReply.bbsUserInfo.sysUser.cnName}
							</DIV>
							<DIV>
								发帖数量：${bbsReply.bbsUserInfo.topicCount}
							</DIV>							
							<DIV>
								教学站：<c:choose>
											<c:when test="${not empty bbsUserInfo.studentInfo}">${bbsReply.bbsUserInfo.studentInfo.branchSchool.unitName}</c:when>
											<c:otherwise>${bbsReply.bbsUserInfo.sysUser.orgUnit.unitName}</c:otherwise>
										</c:choose>
							</DIV>
						</TD>
						<TD class=td_b style="OVERFLOW: hidden; LINE-HEIGHT: normal"
							vAlign=top width="82%">
							<DIV>
								<DIV class=user_menu_info>
									<c:if test="${bbsReply.isDeleted eq 0 and bbsReply.isBest eq 'Y' }"><font color="green" style="border: 1px solid #B7D3EA;">优秀帖</font>	</c:if>
									<DIV style="FLOAT: right; COLOR: #333" id="${bbsReply.resourceid }">
										第
										<FONT color=red>${bbsReply.showOrder }</FONT> 楼
									</DIV>
									<DIV class=text_style style="FLOAT: right">
										<A onclick="fontSize('m','f${bbsReply.resourceid }')"
											href="javascript:void(0);">小</A><A
											onclick="fontSize('b','f${bbsReply.resourceid }')"
											href="javascript:void(0);">大</A>
									</DIV>
								</DIV>
							</DIV>
							<div id="f${bbsReply.resourceid }" style="min-height: 150px; font-size: 9pt; line-height: 22px; text-indent: 24px; margin-top: 20px;">
								<c:choose>
									<c:when test="${bbsReply.isDeleted eq 0 }">
										${bbsReply.replyContent }
									</c:when>
									<c:otherwise><font color="red"><s>已删除</s></font></c:otherwise>
								</c:choose>
							</div>							
						</TD>
					</TR>
					<TR>
						<TD class=td_d vAlign=bottom width="82%">
							<c:if test="${not empty bbsReply.attachs}">
								<div style="margin-left: 20px;border: 1px solid #E6E6E2;margin-bottom: 6px;padding: 0.5em 1em 0.3em 3em;">
									<c:choose>
										<c:when test="${not empty user }">
											<div>附件：</div>
											<c:forEach items="${bbsReply.attachs}" var="attach" varStatus="vs">									
												<li>
													<img style="cursor: pointer; height: 10px;" src="${baseUrl}/jscript/jquery.uploadify/images/attach.png" />&nbsp;&nbsp;
													<a onclick="downloadAttachFile('${attach.resourceid }');return false;" href="${baseUrl }/edu3/framework/filemanage/download.html?id=${attach.resourceid }">${attach.attName }&nbsp;(${ghfn:formatFileSize(attach.attSize) })</a>
												</li>
											</c:forEach>	
										</c:when>
										<c:otherwise><div style="color: #FF3A00;">无法查看附件，请先登录！</div></c:otherwise>									
									</c:choose>	
								</div>
							</c:if>
							<DIV style="CLEAR: both; FLOAT: left; OVERFLOW-X: hidden; WIDTH: 85%; TEXT-ALIGN: left">
									<IMG src="${baseUrl }/style/default/images/topic_desin.gif">
									<BR>
									${bbsReply.bbsUserInfo.desinger }
								</DIV>
							<BR>
							<DIV class=info>
								<UL class=info>
									<LI></LI>
								</UL>
							</DIV>
						</TD>
					</TR>
					<TR height=20>
						<TD style="PADDING-LEFT: 20px" vAlign=center width="22%">
							<DIV style="PADDING-LEFT: 2px; FLOAT: left; TEXT-INDENT: 12px">
								<fmt:formatDate value="${bbsReply.replyDate }" pattern="yyyy-MM-dd HH:mm:ss"/>
							</DIV>
						</TD>
						<TD class=td_c
							style="PADDING-RIGHT: 6px; PADDING-LEFT: 6px; PADDING-BOTTOM: 6px; PADDING-TOP: 6px; TEXT-ALIGN: left"
							vAlign=center width="82%">
								<c:if test="${not empty user }">
									<c:if test="${(bbsReply.isDeleted eq 0)and(bbsTopic.status ne -1) and ((empty bbsTopic.endTime)or(not empty bbsTopic.bbsGroup)) and (bbsTopic.bbsSection.isReadonly ne 'Y')}">
										<A style="color: #DD5E10;" title=回复贴子 href="${baseUrl }/edu3/learning/bbs/reply.html?topicId=${bbsTopic.resourceid }${coursequerystr }&pageNum=${((empty replyPageNum)or (replyPageNum < 1))?1:replyPageNum}">回复</A>									
										<a style="color: #DD5E10;" title="引用" href="${baseUrl }/edu3/learning/bbs/reply.html?topicId=${bbsTopic.resourceid }${coursequerystr }&quoteId=${bbsReply.resourceid }&pageNum=${(replyPageNum eq -1)?1:replyPageNum}">引用</a>
									</c:if>
									<c:if test="${(bbsReply.isDeleted eq 0 )and (fn:contains(bbsTopic.bbsSection.masterId,user.resourceid) or (user.resourceid eq bbsReply.bbsUserInfo.sysUser.resourceid)) }">
										<A style="color: #DD5E10;" title="编辑贴子" href="${baseUrl }/edu3/learning/bbs/reply.html?replyId=${bbsReply.resourceid }${coursequerystr }&pageNum=${bbsReplyPage.pageNum }">编辑</A>
									</c:if>
								 	<c:if test="${fn:contains(bbsTopic.bbsSection.masterId,user.resourceid) }">
								 		<a style="color: #DD5E10;" title="删除回复" href="javascript:;" onclick="deleteReply('${bbsReply.resourceid }');return false;">删除回复</a>
								 		<span style="color: green;">(更多管理操作请到后台进行)</span>
								 		
								 	</c:if>	
								</c:if>						 							
							<DIV style="FLOAT: right; MARGIN-RIGHT: 5px; HEIGHT: 26px">								
								<a href="#" onclick="javascript:window.scrollTo(0,0);return false;">
								
								<IMG
									style="BORDER-TOP-WIDTH: 0px; BORDER-LEFT-WIDTH: 0px; BORDER-BOTTOM-WIDTH: 0px; BORDER-RIGHT-WIDTH: 0px"
									alt=回到顶部 src="${baseUrl }/style/default/images/topic_backtop.gif">
								</a>
							</DIV>
							<c:if test="${not empty bbsTopic.course }">
								<div style="FLOAT: right;MARGIN-RIGHT: 10px;">
									<c:choose>
										<c:when test="${bbsReply.isBest ne 'Y' }">
											<a style="color: #DD5E10;" title="评优" href="javascript:;" onclick="isBest('${bbsReply.resourceid }','${bbsTopic.course.resourceid }','Y','2');return false;">评优</a>
										</c:when>
										<c:otherwise>
											<a style="color: #DD5E10;" title="取消评优" href="javascript:;" onclick="isBest('${bbsReply.resourceid }','${bbsTopic.course.resourceid }','N','2');return false;">取消评优</a>
										</c:otherwise>
									</c:choose>
								</div>
							</c:if>
						</TD>
					</TR>
				</TBODY>
			</TABLE>
		</div>
</c:forEach>
<%-- 讨论话题子贴 --%>
<c:if test="${not empty bbsTopic.childs }">
	<DIV class=postlary1>
		<table style="width: 100%;">
			<thead>
				<tr>
					<th width="16%">小组</th>
					<th width="8%">组长</th>
					<th width="12%">小组成员</th>
					<th width="20%">评价</th>
					<th width="8%">回复数</th>
					<th width="8%">最后回复人</th>
					<th width="10%">最后回复日期</th>
					<th width="8%">话题权限</th>
					<th width="10%">截止时间</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${bbsTopic.childs }" var="t">
					<tr>
						<td><a style="color: #0B7AC0;"
							href="${baseUrl }/edu3/${course.isQualityResource eq 'Y' ? 'course':'learning' }/bbs/topic.html?courseId=${course.resourceid}&topicId=${t.resourceid }">${t.bbsGroup.groupName }</a></td>
						<td>${t.bbsGroup.leaderName }</td>
						<td><c:forEach items="${t.bbsGroup.groupUsers }" var="u"
								varStatus="i">
	           				${u.studentInfo.studentName }
	           				${i.last?'':',' }
	           				</c:forEach></td>
						<td>XXX</td>
						<td>${t.replyCount }</td>
						<td>${t.lastReplyMan }</td>
						<td><fmt:formatDate value="${t.lastReplyDate }"
								pattern="yyyy-MM-dd HH:mm:ss" /></td>
						<td>${ghfn:dictCode2Val('CodeViewPermiss',t.viewPermiss) }</td>
						<td><fmt:formatDate value="${t.endTime }"
								pattern="yyyy-MM-dd" /></td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</DIV>
</c:if>

<BR>
<!--分页-->
<gh:page page="${bbsReplyPage}"
	goPageUrl="${baseUrl }/edu3/${course.isQualityResource eq 'Y' ? 'course':'learning' }/bbs/topic.html"
	condition="${condition }" pageType="bbs" />

<%-- 锁定帖不给回复 或 用户没登录时 --%>
<c:if
	test="${(not empty user)and(bbsTopic.status ne -1) and ((empty bbsTopic.endTime)or(not empty bbsTopic.bbsGroup)) and (bbsTopic.bbsSection.isReadonly ne 'Y')}">
	<form id="bbsReplyForm" method="post"
		action="${baseUrl}/edu3/learning/bbs/reply/save.html"
		onsubmit="return validateAndSubmit(this);">
		<div style="margin: 10px auto;" class="postlary2 postlary">
			<input type="hidden" name="bbsTopicId"
				value="${bbsTopic.resourceid }" /> <input type="hidden"
				name="courseId" value="${course.resourceid }" /> <input
				type="hidden" name="pageNum"
				value="${(replyPageNum eq -1)?1:replyPageNum}" />
			<table cellspacing="0" cellpadding="0" border="0"
				style="width: 100%; line-height: normal;">
				<tr>
					<th colspan="2" style="text-indent: 12px; text-align: left;">标题：
						${bbsTopic.title }</th>
				</tr>
				<tr>
					<td
						style="text-align: center; line-height: normal; padding: 0px; width: 15%;"
						class="td_a">回复内容：</td>
					<td style="text-align: left; line-height: normal; padding: 0px;"
						class="td_d"><textarea id="replyTextarea" name="replyContent"
							rows="5" cols=""
							style="width: 100%; height: 300px; visibility: hidden;"></textarea>
					</td>
				</tr>
				<tr>
					<td style="text-align: center; line-height: normal; padding: 0px;"
						class="td_a">&nbsp;</td>
					<td
						style="text-align: left; line-height: normal; padding: 0px; border-bottom: 0px none;"
						class="td_d"><input id="bbsRepySubmit" type="submit"
						style="margin: 3px;" value="OK!发表回复"> <input type="reset"
						onclick="clearReset()" value="清空内容！" style="margin: 3px;">
						<span>[完成后可按 Ctrl+Enter 发布] </span></td>
				</tr>
			</table>
		</div>
	</form>
</c:if>
<script type="text/javascript">		
		function loadEditor(){
			 if(${bbsTopic.status ne -1}){//不为锁定帖时创建
			 	KE.create('replyTextarea'); 
			 }		     		    	     		    
	    } 	        
	    window.setTimeout(loadEditor,1000);
	
		function clearReset(){
			KE.text("replyTextarea","");
		}
		//检查合法性
		function validateAndSubmit(form){				
			if(KE.isEmpty("replyTextarea")){
				alert("内容不能为空");
				return false;
			}
			return validateCallback(form,function(json){				
				if(json.statusCode == 200){										
					if("${bbsReplyPage.pageNum}"=="${(replyPageNum eq -1)?1:replyPageNum}"){
						location.reload();
					} else {
						location.href=json.reloadUrl;
					}
				}
			});
		}
	   //附件下载
	   function downloadAttachFile(attid){
	   		var url = baseUrl+"/edu3/framework/filemanage/download.html?id="+attid;
	   		var elemIF = document.createElement("iframe");  
			elemIF.src = url;  
			elemIF.style.display = "none";  
			document.body.appendChild(elemIF); 
	   }
	</script>
<%@ include file="/WEB-INF/jsp/edu3/learning/bbssection/bbs-footer.jsp"%>