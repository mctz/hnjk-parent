<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/common.jsp"%>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=9" />
<title>${school}${schoolConnectName}平台</title>
<meta http-equiv="Cache-Control" content="no-store" />
<meta http-equiv="Pragma" content="no-cache" />
<meta http-equiv="Expires" content="0" />
<gh:loadCom
	components="netedu3-defaultcss,jquery,framework,fileupload,fineUploader,editor,datePicker,cnArea,autocomplete,highcharts,multiselect,colortips,ztree" />
<!--[if IE]>
		<link href="${baseUrl}/themes/css/ieHack.css" rel="stylesheet" type="text/css" />		
	<![endif]-->
<script type="text/template"  id="qq-template">
    <div class="qq-uploader-selector qq-uploader" qq-drop-area-text="拖拽文件到此上传">
        <div class="qq-total-progress-bar-container-selector qq-total-progress-bar-container">
            <div role="progressbar" aria-valuenow="0" aria-valuemin="0" aria-valuemax="100" class="qq-total-progress-bar-selector qq-progress-bar qq-total-progress-bar"></div>
        </div>
        <div class="qq-upload-drop-area-selector qq-upload-drop-area" qq-hide-dropzone>
            <span class="qq-upload-drop-area-text-selector"></span>
        </div>
        <div class="qq-upload-button-selector qq-upload-button">
            <div>上传文件</div>
        </div>
            <span class="qq-drop-processing-selector qq-drop-processing">
                <span>正在拖拽文件...</span>
                <span class="qq-drop-processing-spinner-selector qq-drop-processing-spinner"></span>
            </span>
        <ul class="qq-upload-list-selector qq-upload-list" aria-live="polite" aria-relevant="additions removals">
            <li>
                <div class="qq-progress-bar-container-selector">
                    <div role="progressbar" aria-valuenow="0" aria-valuemin="0" aria-valuemax="100" class="qq-progress-bar-selector qq-progress-bar"></div>
                </div>
                <span class="qq-upload-spinner-selector qq-upload-spinner"></span>
                <span class="qq-upload-file-selector qq-upload-file"></span>
                <span class="qq-edit-filename-icon-selector qq-edit-filename-icon" aria-label="Edit filename"></span>
                <input class="qq-edit-filename-selector qq-edit-filename" tabindex="0" type="text">
                <span class="qq-upload-size-selector qq-upload-size"></span>
                <button type="button" class="qq-btn qq-upload-cancel-selector qq-upload-cancel">取消</button>
                <button type="button" class="qq-btn qq-upload-retry-selector qq-upload-retry">重试</button>
                <button type="button" class="qq-btn qq-upload-delete-selector qq-upload-delete">删除</button>
                <span role="status" class="qq-upload-status-text-selector qq-upload-status-text"></span>
            </li>
        </ul>
<!--
        <dialog class="qq-alert-dialog-selector">
            <div class="qq-dialog-message-selector"></div>
            <div class="qq-dialog-buttons">
                <button type="button" class="qq-cancel-button-selector">close</button>
            </div>
        </dialog>

        <dialog class="qq-confirm-dialog-selector">
            <div class="qq-dialog-message-selector"></div>
            <div class="qq-dialog-buttons">
                <button type="button" class="qq-cancel-button-selector">No</button>
                <button type="button" class="qq-ok-button-selector">Yes</button>
            </div>
        </dialog>

        <dialog class="qq-prompt-dialog-selector">
            <div class="qq-dialog-message-selector"></div>
            <input type="text">
            <div class="qq-dialog-buttons">
                <button type="button" class="qq-cancel-button-selector">取消</button>
                <button type="button" class="qq-ok-button-selector">确定</button>
            </div>
        </dialog>
-->
    </div>
</script>
</head>
 
<body scroll="no">
	<div id="layout">
		<!-- 头部开始 -->
		<div id="header">
			<div class="headerNav">
				<a href="#" class="logo" onclick="openPortal('${url}')">LOGO</a>
				<ul class="nav">
					<li><a href="##">欢迎,${user.username }</a> <security:authorize
							ifAnyGranted="ROLE_PREVIOUS_ADMINISTRATOR"> / <a
								href="${baseUrl }/j_spring_security_exit_user">切换原用户</a>
						</security:authorize></li>
					<li id="_spped_test_li"><img
						src="${baseUrl }/themes/default/images/netspeed/inco_network_1.png"
						id="_user_net_speed1" title="网络信号差" style="margin-top: -4px" /> <img
						src="${baseUrl }/themes/default/images/netspeed/inco_network_2.png"
						id="_user_net_speed2" title="网络信号若"
						style="margin-top: -4px; display: none" /> <img
						src="${baseUrl }/themes/default/images/netspeed/inco_network_3.png"
						id="_user_net_speed3" title="网络信号好"
						style="margin-top: -4px; display: none" /> <img
						src="${baseUrl }/themes/default/images/netspeed/inco_network_4.png"
						id="_user_net_speed4" title="网络信号极好"
						style="margin-top: -4px; display: none" /></li>
					<li id="fastEntryBox"><a href="#">快速入口▼</a>
						<ul>
							<li><a href="#" onclick="addFastEntry();"><b>+添加...</b></a></li>
						</ul></li>
					<li id="switchEnvBox"><a href="javascript:void(0)">更多▼</a>
						<ul>
							<li><a href="##" onclick="openDownloads();">常用下载</a></li>
							<li><a href="##" onclick="addFastEntry();">我的博客</a></li>
							<li><a href="##" onclick="openBbs();">学院论坛</a></li>
							<li><a href="##" onclick="addFastEntry();">网上图书馆</a></li>
							<li><a href="##" onclick="openPortal('${url}');">学院门户</a></li>
						</ul></li>
					<li><a href="${baseUrl }/edu3/framework/user/setting.html"
						id="_user_setting"
						<c:if test="${isNeedModifyPwd}">title="您的登录密码为初始密码.<br/>出于安全考虑,请及时更改密码."</c:if>
						target="dialog" mask="true" rel="_userInfo_setting">设置</a></li>
					<li><a href="${baseUrl}/edu3/student/feedback/input.html"
						title="新增反馈" target="navTab" rel="RES_STUDENT_FEEDBACK_ADD">反馈</a></li>
					<li><a href="${baseUrl}/attachs/system/helpdoc/jwyczsc.doc"
						target="_blank">帮助</a></li>
					<li><a href="###" onclick="javascript:logout();">退出</a></li>
				</ul>
				<ul class="themeList" id="themeList">
					<li class="t1" theme="default" title="经典蓝色"><div
							class="selected">蓝色</div></li>
					<li class="t2" theme="green" title="活力绿色"><div>绿色</div></li>
					<li class="t4" theme="purple" title="温馨玫瑰"><div>紫色</div></li>
					<li class="t5" theme="silver" title="成熟灰色"><div>银色</div></li>
					<li class="t6" theme="azure" title="动感天蓝"><div>天蓝</div></li>
				</ul>
			</div>
		</div>
		<!-- 头部结束 -->

		<!-- 左侧菜单开始 -->
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
				<div class="toggleCollapse">
					<h2>主菜单</h2>
					<div title="收缩">收缩</div>
				</div>
				<div class="accordion">
					<gh:currentUserSystemMenu />
				</div>
			</div>
		</div>
		<!-- 左侧菜单结束 -->

		<!-- 主内容框架开始 -->
		<div id="container">
			<div id="navTab" class="tabsPage">
				<div class="tabsPageHeader">
					<div class="tabsPageHeaderContent">
						<!-- 显示左右控制时添加 class="tabsPageHeaderMargin" -->
						<ul class="navTab-tab">
							<li tabid="main" class="main"><a href="#"><span><span
										class="home_icon">我的主页</span></span></a></li>
						</ul>
					</div>
					<div class="tabsLeft">left</div>
					<!-- 禁用只需要添加一个样式 class="tabsLeft tabsLeftDisabled" -->
					<div class="tabsRight">right</div>
					<!-- 禁用只需要添加一个样式 class="tabsRight tabsRightDisabled" -->
					<div class="tabsMore">more</div>
				</div>
				<ul class="tabsMoreList">
					<li><a href="javascript:void(0)">我的主页</a></li>
				</ul>
				<!-- 内容显示 -->
				<div class="navTab-panel tabsPageContent">
					<div>
						<div class="accountInfo">
							<div class="alertInfo">
								<c:choose>
									<c:when test="${user.userType=='student' }">
										<p style="color: #666">
											在线时长：<font color="red">${studentInfo.loginLongStr!=''?studentInfo.loginLongStr:'首次登录' }</font>
										</p>
									</c:when>
									<c:otherwise>
										<p style="color: #666">
											在线时长：<font color="red">${user.loginLongStr!=''?user.loginLongStr:'首次登录' }</font>
										</p>
									</c:otherwise>
								</c:choose>
								<!--				
								<h2 id="currentCity"></h2>
								<p style="margin-top:-34px;margin-left:60px" id="todayWeather">									
								 </p>
								 <p style="margin-top:-34px;display:none;margin-left:60px" id="tomorrowWeather">								
								 </p>
								-->
							</div>
							<div class="right">
								<p style="color: #666">
									最近登录：
									<fmt:formatDate value="${user.lastLoginTime }"
										pattern="yyyy/MM/dd  HH:mm" />
								</p>
								<p style="color: #666">
									<c:if test="${currentrole ne 'student' }">待办事项(${dbNum })，</c:if>
									新消息(<span id="_newMsgNum">0</span>)

								</p>

							</div>
							<span
								style="float: left; height: 55px; width: 55px; border: 1px solid #ccc; margin-top: 1px; margin-right: 4px">
								<c:choose>
									<c:when test="${not empty userface }">
										<img src="${rootUrl}users/${user.username}/${userface }"
											width="55" height="55" />
									</c:when>
									<c:otherwise>
										<img src="${baseUrl }/themes/default/images/person.png"
											width="55" height="55" />
									</c:otherwise>
								</c:choose>

							</span>
							<p>
								<span> <c:if test="${nowHour > 0 and nowHour <=8 }">早上好</c:if>
									<c:if test="${nowHour > 8 and nowHour <12 }">上午好</c:if> <c:if
										test="${nowHour >=12 and nowHour <13 }">中午好</c:if> <c:if
										test="${nowHour >= 13 and nowHour <= 18 }">下午好</c:if> <c:if
										test="${nowHour > 18 }">晚上好</c:if> ，${user.cnName } <c:if
										test="${currentrole eq 'student' }">同学</c:if>
								</span>
							</p>
							<p>
								<a href="#"
									style="color: #2f77c5; font-weight: normal; font-size: 10pt"><fmt:formatDate
										value="${nowTime }" pattern="yyyy年MM月dd日" /> ${weekStr }</a></span>
							</p>
						</div>
						<div style="padding-top: 16px"></div>
						<div class="page">
							<div class="pageContent" layoutH="76">


								<!-- new line -->
								<c:choose>
									<c:when test="${currentrole eq 'student' }">
										<div
											style="float: left; display: block; overflow: hidden; width: 44%; margin-bottom: 16px; padding: 0 20px; line-height: 21px;">
											<div class="panel" defH="150">
												<h1>消息与通知</h1>
												<div>
													<ul id='sysMessageList'>

													</ul>
												</div>
											</div>
										</div>
										<div
											style="float: left; display: block; overflow: hidden; width: 44%; margin-bottom: 16px; padding: 0 20px; line-height: 21px;">
											<div class="panel" defH="150">
												<h1>
													<span style="float: left; line-height: 28px;">我的学习日历</span>
												</h1>
												<div>
													<table class="list" width="98%">
														<tr>
															<th width="14.3%">日</th>
															<th width="14.3%">一</th>
															<th width="14.3%">二</th>
															<th width="14.3%">三</th>
															<th width="14.3%">四</th>
															<th width="14.3%">五</th>
															<th width="14.3%">六</th>
														</tr>
														<c:forEach begin="0" end="6" step="1" var="j">
															<tr id="${j }">
																<c:set var="st" value="${j*7 }" />
																<c:set var="ed" value="${(j+1)*7 }" />
																<c:forEach begin="${st }" end="${ed -1}" step="1"
																	var="i">
																	<td
																		<c:if test="${not empty days[i] }">id="CalendarDays${days[i] }"</c:if>>
																		<c:choose>
																			<c:when test="${(i- firstIndex +1) == today }">
																				<font color="red">${days[i] }</font>
																			</c:when>
																			<c:otherwise>
												    					${days[i] } 
												    				</c:otherwise>
																		</c:choose>
																	</td>
																</c:forEach>
															</tr>

														</c:forEach>


													</table>
												</div>
											</div>
										</div>

										<div
											style="float: left; display: block; overflow: hidden; width: 94%; margin-bottom: 16px; padding: 0 20px; line-height: 21px;">
											<div class="panel" defH="100%">

												<div class="tabs" currentIndex="0" eventType="click">
													<div class="tabsHeader">
														<div class="tabsHeaderContent">
															<ul>
																<li class="selected"><a class="j-ajax"
																	href="${baseUrl }/edu3/student/study/mycoursetimetable/list.html?from=main"><span>我的课表</span></a></li>
																<li><a href="javascript:void(0)"><span>我的学习计划</span></a></li>
															</ul>
														</div>
													</div>

													<div class="tabsContent">
														<%-- 我的课表start --%>
														<div></div>
														<%-- 我的课表end --%>
														<%-- 我的学习计划start --%>
														<div>
															<!-- 判断学生是否有教学计划 -->
															<c:choose>
																<c:when test="${noTeachingPlan eq 'Y'}">
																	<div style="text-align: center; padding-bottom: 120px">
																		<div class="tips">
																			<b>您的学籍还未关联教学计划，请联系教务管理人员！</b>
																		</div>
																	</div>
																</c:when>
																<c:otherwise>
																	<div class="tabs" currentIndex="0" eventType="click">
																		<div class="tabsHeader">
																			<div class="tabsHeaderContent">
																				<ul>
																					<%-- 先输出默认学籍，其余的排在默认学籍后 --%>
																					<c:forEach items="${studentInfoList }" var="stu">
																						<c:if test="${defaultStudentId eq stu.resourceid}">
																							<li class="selected"><a
																								href="javascript:void(0)"><span>${stu.teachingPlan.major }-${stu.teachingPlan.classic }-${stu.grade }</span></a></li>
																						</c:if>
																					</c:forEach>
																					<c:forEach items="${studentInfoList }" var="stu">
																						<c:if test="${defaultStudentId ne stu.resourceid}">
																							<%-- 
																			<li><a href="${baseUrl }/edu3/framework/getStuTeachingPlan.html?studetnid=${stu.resourceid}" class="j-ajax"><span>${stu.teachingPlan.major.majorName }-${stu.teachingPlan.classic.classicName }</span></a></li>
																			--%>
																							<li><a
																								href="${baseUrl }/edu3/student/otherStudyplan.html?studentId=${stu.resourceid}"
																								class="j-ajax ajax-once"><span>${stu.teachingPlan.major.majorName }-${stu.teachingPlan.classic.classicName }-${stu.grade }</span></a></li>
																						</c:if>
																					</c:forEach>
																				</ul>
																			</div>
																		</div>

																		<div class="tabsContent">
																			<div>
																				<table id="leanPlanTable" class="list" width="97%">
																					<thead>
																						<tr>
																							<td colspan="10"><strong>学制：</strong>${teachingPlanList['planTitle'].eduYear }
																								<strong>最低毕业学分：</strong>${teachingPlanList['planTitle'].minResult }
																								<strong>已修总学分：</strong>${teachingPlanList['planTitle'].totalScore}
																								<strong>已修必修课总学分：</strong>${teachingPlanList['planTitle'].compulsoryedScore}&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
																								<strong>已修必修课数：</strong>${teachingPlanList['planTitle'].compulsoryed}&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
																								
																							</td>
																						</tr>
																						<c:if test="${isDisplayScore eq 'N' }">
																						<tr><td colspan="12"><strong><span style="color:red;">  (提示： 按学校要求，未有缴费记录或未缴费，则不显示成绩。可以在【我的个人信息】【我的学籍信息】【缴费信息】中查看缴费情况)</span></strong></td></tr>
																						</c:if>
																						<%--
																    <tr>
																    	<td colspan="10">
																    	<c:set var="learning_process" value="${ teachingPlanList['planTitle'].totalScore /teachingPlanList['planTitle'].minResult }"></c:set>
																    		<c:if test="${learning_process >=1}">
																    			<c:set var="learning_process" value="1"/>													    			
																    	</c:if>													    			
																    	<table width="60%">
																    		<tr>
																    			<td width="20%">学习进度(<font color='green'><fmt:formatNumber type='percent' pattern='###%' value='${learning_process }'/></font>)：</td>
																    			
																    			<td><span class="active_optionbar"><span style="width: <fmt:formatNumber type='percent' pattern='###.##%' value='${learning_process }'/>;"></span></span>
																    			
																    			</td>
																    			
																    		</tr>
																    	</table>											    	
																    	
																    	</td>													    	
																    </tr> --%>
																					</thead>
																					<tbody id="tab">
																						<%-- 
																    <tr>
																      <td colspan="11" style="background:#ff6600;color:#fff;font-weight: bold;">													     
																      <marquee direction=left scrollamount=3 onmouseover="this.stop()" onmouseout="this.start()">
																      	 <!-- 下面的foreach是学籍办提出在主页滚动条提示入学资格未通过的提示 -->
																      	 <c:forEach items="${studentInfoList }" var="stu">
																			<c:choose>
																				<c:when test="${defaultStudentId eq stu.resourceid and stu.enterAuditStatus=='N'}" >
																					<font color="blue"><b>入学资格:不通过，请立即对专科证书验国证。</b></font>
																				</c:when>
																			</c:choose>
																		</c:forEach>
																      	 <c:if test="${ not empty stuOrderTime['orderCourse']}">
																	    	 ${stuOrderTime['orderCourse'].settingName } 预约学习时间是：
																	    	  <fmt:formatDate value="${stuOrderTime['orderCourse'].startDate }" pattern="yyyy-MM-dd"/> 至
																	    	  <fmt:formatDate value="${stuOrderTime['orderCourse'].endDate }"   pattern="yyyy-MM-dd"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
																    	 </c:if> 
																    	 <c:if test="${ not empty stuOrderTime['examSub']}">
																	    	 ${stuOrderTime['examSub'].batchName } 预约考试时间是：
																	    	 <fmt:formatDate value="${stuOrderTime['examSub'].startTime }" pattern="yyyy-MM-dd"/> 至 
																	    	 <fmt:formatDate value="${stuOrderTime['examSub'].endTime }"  pattern="yyyy-MM-dd"/> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
																    	 </c:if>
																    	 <c:if test="${ not empty stuOrderTime['thesisSub'] and fn:indexOf(teachingPlanList['planTitle'].teachingPlan_Name,'专科')<0 }">
																	    	 ${stuOrderTime['thesisSub'].batchName } 预约毕业论文时间是：
																	    	 <fmt:formatDate value="${stuOrderTime['thesisSub'].startTime }" pattern="yyyy-MM-dd"/> 至 
																	    	 <fmt:formatDate value="${stuOrderTime['thesisSub'].endTime }" pattern="yyyy-MM-dd"/> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
																     	 </c:if>
																      </marquee> 
																    </td>
																    </tr> --%>
																						<!-- -------------------------------------------学生学习计划------------------------------------------- -->
																						<%-- 
																    <tr>
																      <th width="3%">编号</th>
																      <th width="18%">课程名称</th>
																      <th width="8%">课程性质</th>
																      <th width="8%">教学方式</th>
																      <th width="8%">考试形式</th>
																      <th width="7%">学分</th>
																      <th width="8%">建议学期</th>
																      <th width="8%">状态</th>
																      <th width="8%">预约</th>
																      <th width="5%">成绩</th>
																      <th width="19%">考试时间</th>
																    </tr> --%>
																						<tr>
																							<th width="3%">序号</th>
																							<th width="6%">课程类别</th>
																							<th width="12%">课程名称</th>
																							<th width="6%">开课学期</th>
																							<th width="5%">学分</th>
																							<th width="4%">总学时</th>
																							<th width="4%">学时</th>
																							<th width="6%">考试类别</th>
																							<th width="6%">是否主干课</th>
																							<th width="6%">是否学位课</th>
																							<th width="8%">课程类型</th>
																							<th width="6%">学习状态</th>
																							<th width="4%">成绩</th>
																							<th width="6%">网上学习成绩</th>
																							<th width="21%">教材信息</th>
																						</tr>
																						<c:forEach
																							items="${teachingPlanList['planCourseList']}"
																							var="course" varStatus="vs">
																							<tr>
																								<td>${course.teachingPlan_Course_num }</td>
																								<td>${course.teachingPlan_Course_type }</td>
																								<td><c:choose>
																										<%-- 毕业状态，只给查看，不给操作 --%>
																										<c:when
																											test="${defaultStudentStatus eq '16' }">${course.teachingPlan_Course_name }</c:when>
																										<%-- <c:when test="${  course.teachingPlan_Course_type_code ne 'thesis' && course.isHasResource eq 'Y' }"> --%>
																										<c:when
																											test="${ course.isHasResource eq 'Y' && course.teachingPlan_Course_teachType_code eq 'networkTeach'}">
																											<a href="javascript:void(0)"
																												onclick="goInteractive('${course.course_Id }','${course.teachingPlan_Course_Id }','${(course.teachingPlan_Course_learnStatusInt eq 2 and course.isNeedReExamination eq 'Y')?'Y':''}','${course.teachingPlan_Course_teachType_code}','${course.teachingPlan_Course_term_code }')">${course.teachingPlan_Course_name }</a>
																										</c:when>
																										<c:when
																											test="${course.teachingPlan_Course_learnStatusInt>-1 &&  course.teachingPlan_Course_type_code eq 'thesis' && course.teachingPlan_Course_teachType_code ne 'netsidestudy'}">
																											<a rel="RES_TEACHING_THESIS_MSG"
																												target="navTab" title="个人辅导"
																												href="${baseUrl }/edu3/framework/graduateMsg/main.html">${course.teachingPlan_Course_name }</a>
																										</c:when>
																										<c:otherwise>
																	      			${course.teachingPlan_Course_name }
																	      		</c:otherwise>
																									</c:choose></td>
																								<td>${course.teachingPlan_Course_term }</td>
																								<td>${course.teachingPlan_Course_creditHour }</td>
																								<td>${course.teachingPlan_Course_studyHour}</td>
																								<td>${course.teachingPlan_Course_faceStudyHour }</td>
																								<td>${course.teachingPlan_Course_examClassType }</td>
																								<td>${course.teachingPlan_Course_isMainCourse }</td>
																								<td>${course.teachingPlan_Course_isDegreeCourse }</td>
																								<td
																									<c:if test="${course.teachingPlan_Course_teachType_code eq 'networkTeach' }"> style="color: blue;"</c:if>>${course.teachingPlan_Course_teachType_Name }</td>
																								<%--学习状态 --%>
																								<c:choose>
																									<c:when
																										test="${course.teachingPlan_Course_teachType_code eq 'faceTeach' and course.teachingPlan_Course_learnStatusInt lt 3  or course.teachingPlan_Course_teachType_code eq 'networkTeach' and course.teachingPlan_Course_learnStatusInt lt 2}">
																										<td>不需预约</td>
																									</c:when>
																									<c:when
																										test="${course.teachingPlan_Course_learnStatus eq '正在学习' }">
																										<td><font color='green'>${course.teachingPlan_Course_learnStatus}</font></td>
																									</c:when>
																									<c:when
																										test="${course.teachingPlan_Course_learnStatus eq '已预约考试' }">
																										<td><font color='red'>${course.teachingPlan_Course_learnStatus}</font></td>
																									</c:when>
																									<c:otherwise>
																										<td>${course.teachingPlan_Course_learnStatus}</td>
																									</c:otherwise>
																								</c:choose>
																								<%--学习状态 --%>



																								<td id="_student_course_score"><a href='##'
																									class="course_tips"
																									title="卷面成绩：${fn:replace(course.teachingPlan_Course_WrittenScore,'-1.0','')}
																	      <span style='padding-right:12px'></span>平时成绩：${fn:replace(course.teachingPlan_Course_UsuallyScore,'-1.0','') }"
																									class='blue'
																									<c:if test="${course.teachingPlan_Course_type_code eq 'thesis' and course.teachingPlan_Course_scoreStr eq '不及格' }">style="color:red;"</c:if>>
																										<c:choose>
																											<c:when
																												test="${not empty course.teachingPlan_Course_scoreStr }">
																		      		${course.teachingPlan_Course_scoreStr}
																		      	</c:when>
																											<c:when
																												test="${not empty course.teachingPlan_Course_score }">
																		      		${course.teachingPlan_Course_score}
																		      	</c:when>
																										</c:choose>
																								</a></td>

																								<c:choose>
																									<c:when
																										test="${course.isHasResource=='Y' && course.teachingPlan_Course_teachType_code eq 'networkTeach'}">
																										<td style="cursor: pointer;"
																											onclick="viewStudentOnlineScore2('${course.teachingPlan_Course_Id}','${currentStudent.grade.resourceid }','${currentStudent.branchSchool.resourceid }','${currentStudent.teachingPlan.resourceid }','${currentStudent.resourceid }','${course.course_Id }'),'${course.teachingPlan_Course_teachType_code}'"><a
																											href="javascript:void(0)">查看</a></td>
																									</c:when>
																									<c:otherwise>
																										<td></td>
																									</c:otherwise>
																								</c:choose>

																								<%-- --%>																								
																								<td ><a href="${baseUrl }/edu3/sysmanager/textbook/viewTextBook.html?resourceid=${course.teachingPlan_Course_Id}" target="dialog" title="查看教材信息" width="700" >${course.textBookStr }</a></td>
<%-- 																								<td id="_student_exam_time"><c:choose> --%>
<%-- 																										<c:when --%>
<%-- 																											test="${ not empty course.teachingPlan_Course_examStartTime and not empty course.teachingPlan_Course_examEndTime and (course.teachingPlan_Course_examType_code eq '3' or course.teachingPlan_Course_examType_code eq '6')}"> --%>
<!-- 																											<a href="javascript:void(0)" -->
<!-- 																												class="_student_exam_time" -->
<%-- 																												title="${course.teachingPlan_Course_examStartTime}至${course.teachingPlan_Course_examEndTime }"> --%>
<!-- 																												查看考试时间</a> -->
<%-- 																										</c:when> --%>
<%-- 																										<c:when --%>
<%-- 																											test="${not empty course.teachingPlan_Course_examStartTime and course.teachingPlan_Course_examEndTime != ''}"> --%>
<%-- 																		      			${course.teachingPlan_Course_examStartTime}-${course.teachingPlan_Course_examEndTime } --%>
<%-- 																		      		</c:when> --%>
<%-- 																										<c:when --%>
<%-- 																											test="${not empty course.teachingPlan_Course_examStartTime and empty course.teachingPlan_Course_examEndTime}"> --%>
<%-- 																		      			${course.teachingPlan_Course_examStartTime} --%>
<%-- 																		      		</c:when> --%>
<%-- 																										<c:otherwise> --%>
<!-- 																		      			&nbsp; -->
<%-- 																		      		</c:otherwise> --%>
<%-- 																									</c:choose></td> --%>

																							</tr>
																						</c:forEach>
																						<!-- -------------------------------------------学生学习计划------------------------------------------- -->

																						<!-- -------------------------------------------学生学习计划(计划外课程)--不显示----------------------------------------- -->
																						<%-- <c:forEach items="${teachingPlanList['planOutCourseList']}" var="course" varStatus="vs">
																	    <tr>
																	      <td>${course.teachingPlan_Course_num }</td>
																	       <td style="color: blue">计划外</td>
																	      <td>
																	      	<c:choose>
																	      		<c:when test="${defaultStudentStatus eq '16' }">${course.teachingPlan_Course_name }</c:when>
																	      		<c:when test="${course.teachingPlan_Course_learnStatusInt>-1 &&  course.teachingPlan_Course_type_code != 'thesis' && course.isHasResource eq 'Y' }">
																	      			<a href="javascript:void(0)" onclick="goInteractive('${course.course_Id }','${course.teachingPlan_Course_Id }','${(course.teachingPlan_Course_learnStatusInt eq 2 and course.isNeedReExamination eq 'Y')?'Y':'${course.teachingPlan_Course_teachType_code}'}')">${course.teachingPlan_Course_name }</a>
																	      		</c:when>
																	      		<c:otherwise>
																	      			${course.teachingPlan_Course_name }
																	      		</c:otherwise>
																	      	</c:choose>
																	    	  
																	      </td>	
																	      <td>${course.teachingPlan_Course_term }</td>	
																	      <td>${course.teachingPlan_Course_creditHour }</td>
																	      <td>${course.teachingPlan_Course_studyHour}</td>
																	      <td>${course.teachingPlan_Course_faceStudyHour }</td>												      
																	      <td>${course.teachingPlan_Course_examClassType }</td>
																	      <td>${course.teachingPlan_Course_isMainCourse }</td>
																	      <td>${course.teachingPlan_Course_isDegreeCourse }</td>
																	      		      
																	     学习状态
																	      <c:choose>
																	      		<c:when test="${course.teachingPlan_Course_learnStatus eq '正在学习' }"><td><font color='green'>${course.teachingPlan_Course_learnStatus}</font></td></c:when>
																	      		<c:when test="${course.teachingPlan_Course_learnStatus eq '已预约考试' }"><td><font color='red'>${course.teachingPlan_Course_learnStatus}</font></td></c:when>
																	      		<c:otherwise><td>${course.teachingPlan_Course_learnStatus}</td></c:otherwise>
																	      </c:choose>
																	      学习状态
																	      
																	      预约状态
																	      <c:choose>  
																	      		<c:when test="${defaultStudentStatus eq '16' }"><td > ${course.teachingPlan_Course_bookingStatus}</td></c:when> 
																		      	预约考试 
																		      	<c:when test="${course.teachingPlan_Course_learnStatusInt ==2 && course.teachingPlan_Course_learnStatus eq '已预约学习' }">
																		      		<td><a href='javascript:void(0)' style='color:Blue;font-weight:normal;font-size:10pt' onclick="changePlanOutCourseStatus('${course.course_Id}','${defaultStudentId}','orderExam','确定要预约此科目的考试吗？')">${course.teachingPlan_Course_bookingStatus}</a></td>
																		      	</c:when>
																		      	预约考试 
																		      	
																		      	重考
																		   		<c:when test="${course.teachingPlan_Course_learnStatusInt ==2 && course.isNeedReExamination eq 'Y' }">
																		      		<td><a href='javascript:void(0)' style='color:Blue;font-weight:normal;font-size:10pt' onclick="changeReExamStudyStatus('Y','${course.course_Id}','确定要重考此科目吗？','${defaultStudentId}','orderExam')">${course.teachingPlan_Course_bookingStatus}</a></td>
																		      	</c:when>
																		      	重考
																		  
																		      	重修 
																		      	<c:when test="${course.teachingPlan_Course_learnStatusInt ==3 &&  course.teachingPlan_Course_bookingStatus ne '成绩录入中'}">															 
																		      		<td title="点击重修此科目"> <a href='javascript:void(0)' onclick="changeReExamStudyStatus('Y','${course.course_Id}','确定要重修此科目吗？','${defaultStudentId}','orderExam')"> ${course.teachingPlan_Course_bookingStatus} </a></td>
																		      	</c:when>
																		        重修 
																		      	<c:otherwise>
																		      		<td > ${course.teachingPlan_Course_bookingStatus}</td>
																		      	</c:otherwise>
																	      </c:choose>
																	      预约状态											      
																	       <td  id="_student_course_score">														     
																	     	 <a href='##' class="course_tips" title="卷面成绩：${course.teachingPlan_Course_WrittenScore}<span style='padding-right:12px'></span>平时成绩：${course.teachingPlan_Course_UsuallyScore }" class='blue'> ${course.teachingPlan_Course_scoreStr}</a>
																	     
																		     
																		      <c:choose>
																		      	<c:when test="${not empty course.teachingPlan_Course_scoreStr }">
																		      		${course.teachingPlan_Course_scoreStr}
																		      	</c:when>
																		      	<c:when test="${not empty course.teachingPlan_Course_score }">
																		      		${course.teachingPlan_Course_score}
																		      	</c:when>
																		      </c:choose> 
																	      </td>
																	     
																	      <td></td>
																	      <td>
																	      	   <c:choose>
																		      		<c:when test="${not empty course.teachingPlan_Course_examStartTime and course.teachingPlan_Course_examEndTime != ''}">
																		      			${course.teachingPlan_Course_examStartTime}-${course.teachingPlan_Course_examEndTime }
																		      		</c:when>
																		      		<c:when test="${not empty course.teachingPlan_Course_examStartTime and empty course.teachingPlan_Course_examEndTime}">
																		      			${course.teachingPlan_Course_examStartTime}
																		      		</c:when>
																		      		<c:otherwise>
																		      			&nbsp;
																		      		</c:otherwise>	
																	     	   </c:choose>
																	       </td> 
																	    </tr>
																	</c:forEach> --%>
																						<!-- -------------------------------------------学生学习计划(计划外课程)------------------------------------------- -->
																					</tbody>
																					<tfoot>
																					</tfoot>
																				</table>
																			</div>
																			<c:forEach items="${studentInfoList }" var="stu">
																				<div></div>
																			</c:forEach>
																			<div class="tabsFooter">
																				<div class="tabsFooterContent"></div>
																			</div>
																		</div>
																	</div>

																</c:otherwise>
															</c:choose>
														</div>
														<%-- 我的学习计划end --%>
														<div class="tabsFooter">
															<div class="tabsFooterContent"></div>
														</div>
													</div>
												</div>
											</div>
										</div>

									</c:when>
									<c:otherwise>
										<!-- 第一个drag div -->
										<div class="sortDrag" selector=".dragSelector"
											style="float: left; display: block; overflow: hidden; height: 95%; width: 44%; margin-bottom: 16px; padding: 0 20px; line-height: 21px;">
											<div style="margin-bottom: 16px;">
												<div class="panel" defH="150">
													<h1 class="dragSelector">消息与通知</h1>
													<div>
														<ul id='sysMessageList'>

														</ul>
													</div>
												</div>
											</div>
											<div style="margin-bottom: 16px;">
												<div class="panel" defH="150">
													<h1 class="dragSelector">我的待办</h1>
													<div>
														<ul>
															<c:choose>
																<c:when test="${not empty  currentWorks.result }">
																	<c:forEach items="${currentWorks.result }" var="dbwork"
																		varStatus="vs">
																		<li style="padding-top: 6px">来自<font color='blue'>${ghfn:ids2Names(fn:split(dbwork.owner,'_')[0],fn:split(dbwork.owner,'_')[1]) }
																		</font>： <a
																			style="color: #2f77c5; font-weight: normal; font-size: 10pt"
																			ref="${ghfn:wfAttr(dbwork,'wftype') }"
																			target="navTab"
																			href='${baseUrl }${ghfn:wfAttr(dbwork,'
																			wfurl')}?APP_FROM=DB&wf_id=${dbwork.entry.id}'>${ghfn:wfAttr(dbwork,'wfcnname')}-${ghfn:wfAttr(dbwork,'curname')}</a>
																			<span class="time">[<fmt:formatDate
																					value="${dbwork.startDate }"
																					pattern="yyyy年MM月dd日 HH时mm分" />]
																		</span><span class="font_12_red">&nbsp; <a
																				ref="${ghfn:wfAttr(dbwork,'wftype') }"
																				target="navTab"
																				href='${baseUrl }${ghfn:wfAttr(dbwork,'
																				wfurl')}?APP_FROM=DB&wf_id=${dbwork.entry.id}'>处理</a></span>
																		</li>
																	</c:forEach>
																</c:when>
																<c:otherwise>
																	<li style="padding-top: 6px">暂无待办事项.</li>
																</c:otherwise>
															</c:choose>
														</ul>
													</div>
												</div>
											</div>

										</div>
										<!-- 第二个drag div -->
										<div class="sortDrag" selector=".dragSelector"
											style="float: left; display: block; overflow: hidden; height: 95%; width: 44%; margin-bottom: 16px; padding: 0 20px; line-height: 21px;">
											<div style="margin-bottom: 16px;">
												<div class="panel" defH="150">
													<h1>
														<span class="dragSelector"
															style="line-height: 28px; display: block; width: 100%;">我的教学管理日历</span>
														<c:if test="${currentrole ne 'student' }">
															<input type="hidden" value="0" id="variable_months" />
															<span
																style="line-height: 23px; height: 23px; display: block; width: 135px; position: absolute; top: 5px; right: 4px; z-index: 1;"><a
																href="##" onclick="getTeachCalendar(0)" title="今天">
																	今天 </a><a href="##" onclick="getTeachCalendar(-1)"
																title="上一月"> &lt;&lt; </a><span
																id="current_variable_date"> ${current_date } </span><a
																href="##" onclick="getTeachCalendar(1)" title="下一月">
																	&gt;&gt; </a></span>
														</c:if>
													</h1>
													<div>
														<table class="list" width="98%">
															<tr>
																<th width="14.3%">日</th>
																<th width="14.3%">一</th>
																<th width="14.3%">二</th>
																<th width="14.3%">三</th>
																<th width="14.3%">四</th>
																<th width="14.3%">五</th>
																<th width="14.3%">六</th>
															</tr>
															<c:forEach begin="0" end="6" step="1" var="j">
																<tr id="${j }">
																	<c:set var="st" value="${j*7 }" />
																	<c:set var="ed" value="${(j+1)*7 }" />
																	<c:forEach begin="${st }" end="${ed -1}" step="1"
																		var="i">
																		<td id="CalendarDays${i }"><c:choose>
																				<c:when test="${(i- firstIndex +1) == today }">
																					<font color="red">${days[i] }</font>
																				</c:when>
																				<c:otherwise>
													    					${days[i] } 
													    				</c:otherwise>
																			</c:choose></td>
																	</c:forEach>
																</tr>

															</c:forEach>


														</table>
													</div>
												</div>
											</div>
											<div style="margin-bottom: 16px;">
												<div class="panel" defH="150">
													<h1 class="dragSelector">我的已办</h1>
													<div>
														<ul>
															<c:choose>
																<c:when test="${not empty  historyWorks.result }">
																	<c:forEach items="${historyWorks.result}" var="dbwork"
																		varStatus="vs">
																		<li style="padding-top: 6px">来自<font color='blue'>${ghfn:ids2Names(fn:split(dbwork.owner,'_')[0],fn:split(dbwork.owner,'_')[1]) }</font>：
																			<a ref="${ghfn:wfAttr(dbwork,'wftype') }"
																			target="navTab"
																			href='${baseUrl }${ghfn:wfAttr(dbwork,'
																			wfurl')}?APP_FROM=DB&wf_id=${dbwork.entry.id}'>${ghfn:wfAttr(dbwork,'wfcnname')}-${ghfn:wfAttr(dbwork,'spname')}</a>
																			<span class="time">[<fmt:formatDate
																					value="${dbwork.startDate }"
																					pattern="yyyy年MM月dd日 HH时mm分" />]
																		</span><span class="font_12_red">&nbsp; <a
																				ref="${ghfn:wfAttr(dbwork,'wftype') }"
																				target="navTab"
																				href='${baseUrl }${ghfn:wfAttr(dbwork,'
																				wfurl')}?APP_FROM=DB&wf_id=${dbwork.entry.id}'>查看</a></span>
																		</li>
																	</c:forEach>
																</c:when>
																<c:otherwise>
																	<li style="padding-top: 6px">暂无已办事项.</li>
																</c:otherwise>
															</c:choose>
														</ul>
													</div>
												</div>
											</div>
										</div>
									</c:otherwise>

								</c:choose>

							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
		<!-- 页脚开始 -->
		<div id="taskbar" style="left: 0px; display: none;">
			<div class="taskbarContent">
				<ul></ul>
			</div>
			<div class="taskbarLeft taskbarLeftDisabled" style="display: none;">taskbarLeft</div>
			<div class="taskbarRight" style="display: none;">taskbarRight</div>
		</div>
		<div id="splitBar"></div>
		<div id="splitBarProxy"></div>
	</div>
	<div id="footer">
		技术支持：广东学苑教育发展有限公司
		<gh:version />
	</div>
	<!--拖动效果-->
	<div class="resizable"></div>
	<!--阴影-->
	<!-- <div class="shadow" style="width: 508px; top: 148px; left: 296px;">
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
	</div> -->
	<!--遮盖屏幕-->
	<div id="alertBackground" class="alertBackground"></div>
	<div id="dialogBackground" class="alertBackground"></div>

	<div id='background' class='background'></div>
	<div id='progressBar' class='progressBar'>数据加载中，请稍等...</div>
	<div id="_weather_info" style="display: none"></div>
	<IMG id="_netspeedTest" style="display: none; width: 0px; height: 0px"
		src="">
	<script type="text/javascript">
	/*~~~~~~~~~~~~~~1.如果是IE，则定时回收内存~~~~~~~~~~~~~~~~~~~~*/
	if ($.browser.msie) {
		window.setInterval("CollectGarbage();", 10000);
	}
	var st = new Date();
	/*~~~~~~~~~~~~~~2.框架预加载项~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/	
	$(document).ready(function(){
		//框架初始化
		DWZ.init("${baseUrl}/jscript/framework/dwz.plugin.xml", {				
				callback:function(){
					initEnv();
					$("#themeList").theme({themeBase:"themes"});
				}
		});	
		//设置天气信息
// 		window.setTimeout(getCityWeather,3000);//获取天气信息
		window.setTimeout(getMessage, 1000);//获取用户消息
		//getLearnPlan();	//获取学生教学计划列表	
		window.setTimeout(updateUserLoginInfo,2000);
		if($("#variable_months")){
			$("#variable_months").val("0");
		}
		if($('#_student_course_score')){//学生成绩 tips
			$('.course_tips').tipsy({opacity:1.0,fade:true,html:true,gravity:'w'});
		}
		if($('#_student_exam_time')){//学生考试时间 tips
			$('._student_exam_time').tipsy({opacity:1.0,fade:true,html:true,gravity:'e'});
		}
		$('._calendar_tips').tipsy({opacity:1.0,fade: true, html:true});		
		if('${isNeedModifyPwd}'){
			$('#_user_setting').tipsy({trigger: 'focus',opacity:1.0,fade: true, html:true,gravity:'ne'});
		}
		$('#_user_setting').trigger('focus');
		//网络信号差时提示用户
		var _user_net_speed = $('#_user_net_speed_1');
		_user_net_speed.bind('click',function(){
			_user_net_speed.tipsy('show');
		});		
		_user_net_speed.tipsy({trigger: 'manual'});
		//判断用户浏览器分辨率是否满足要求
		//window.setTimeout(alertClientBrowserScreen,5000);
		//测速
		//window.setTimeout(alertClientNetspeedToslow,1000);
		//window.setInterval(alertClientNetspeedToslow,10000);
		if('${currentrole}'=='student'){
			//window.setTimeout(getStudentCalendar, 1000);//获取学生学习日历
		}
		//是否提示修改初始化密码
		if('${isTipUpdateInitPassword}'=='Y' && '${isNeedModifyPwd}'=='true'){
			setTimeout(tipUpdatePassword, 300 );
		}
		
		//增加首次登陆的确认信息
		if('${user.userType}'=='student' &&'${firstLogin}'=='Y'){
			if('${currentStudent.firstComfirmStatus}'=='0'){
				//$('#dialogBackground').show();
				//$('#dialogBackground').bind("click",function(){
				//	 $.pdialog.open('${baseUrl }/edu3/confirmMsgView.html','confirmStuInfo','首次登陆确认登陆信息',{mask:true,width:380,height:345,close:closeDialog});	
			//	});
				setTimeout(openConfirmInfo, 500 );
			}else if('${isQuestionnaire}'=='Y'){
				setTimeout(openQuestionnaire, 500 );
			}
		}else if('${user.userType}'=='student' && '${isQuestionnaire}'=='Y'){
			setTimeout(openQuestionnaire, 500 );
		}
		// 触发点击事件
		//$('#dialogBackground').trigger("click");
		//setTimeout("$('#dialogBackground').unbind('click')", 650 );
		setTimeout(getNoExmaresultExamInfo,5000);
	});  
	
	function tipUpdatePassword(){
		$.pdialog.open('${baseUrl }/edu3/sys/tipUpdatePassword.html','ipUpdatePassword','温馨提示',{mask:true,width:500,height:200 });
	}
	
	function openQuestionnaire(){
		var studentInfoid='${defaultStudentId}';
		$.pdialog.open('${baseUrl }/edu3/teaching/quality/evaluation/stuQuestionnaire/view.html?studentInfoid='+studentInfoid,'stuQuestionnaire','课堂教学质量问卷调查表',{mask:true,width:600,height:450,close:closeDialog});
	}
	function openConfirmInfo(){
		$.pdialog.open('${baseUrl }/edu3/confirmMsgView.html','confirmStuInfo','首次登录确认基本信息',{mask:true,width:380,height:450,close:closeDialog});	
	}
    
	//确认信息取消回调方法,不确认则注销
	function closeDialog(){
		window.location.href="${basePath}/j_spring_security_logout";
		// $('#dialogBackground').unbind("click");
		return true;
	}
	
	/*~~~~~~~~~~~~~~~~3.框架页面功能函数定义~~~~~~~~~~~~~~~~~~~*/
	//从新登录
	function relogin(){		
		$.pdialog.open('${baseUrl }/edu3/relogin.html?user=${user.username }','relogin','重新登录',{mask:true,width:400,height:180});
	}	
	//添加快速入口
	function addFastEntry(){
		alertMsg.warn("功能尚未开放，谢谢关注！")
	}
	
	//提醒用户调整分辨率
	//function alertClientBrowserScreen(){	
	//	if($(window).width()<1152 || $(window).height()<620){
	//		alertMsg.warn("您的电脑分辨率过小，可能会影响部分功能使用。<br/>请将分辨率调整到1280*800或以上.");
	//	}
		
	//}
	//网速提示
	function alertClientNetspeedToslow(){
		var testImgsrc = '${baseServerUrl}/edu3/attachs/common/students/speed-test.png?now='+new Date().getTime();
		var testImg = $('#_netspeedTest');
		if(testImg){
			st = new Date();
			testImg.attr('src',testImgsrc);
			testImg.load(function(){
				 var filesize =8;    //measured in KB   
			     var et = new Date();  
			     var speed = Math.round(filesize*1000)/(et - st);
			   var user_net_speed =  $('#_user_net_speed1');	    
			    		   
			    var close = "<span style='padding-left:4px'><a href='###' id='_close_user_net_speed' title='关闭'><b>x</b></a></span>";
			    if(speed>0 && speed<=30){			    	
			    	//user_net_speed.attr({'src':'${baseUrl }/themes/default/images/netspeed/inco_network_1.png','title':'您当前的网络状况较差，可能会导致部分操作失败.'+close});		    	
			    	//TODO 
			    	_switch_spped("_user_net_speed1");
			    	user_net_speed.trigger('click');
			    	 var close_user_net_speed = $('#_close_user_net_speed');
					    if(close_user_net_speed){
					    	close_user_net_speed.unbind('click');
					    	close_user_net_speed.bind('click',function(){
								user_net_speed.tipsy('hide');
							})
						}
			    }else if(speed>30 && speed<=60){
			    	user_net_speed.tipsy('hide');
			    	_switch_spped("_user_net_speed2");
			    }else if(speed>60 && speed<=90){
			    	user_net_speed.tipsy('hide');
			    	_switch_spped("_user_net_speed3");
			    }else if(speed>90){
			    	user_net_speed.tipsy('hide');
			    	_switch_spped("_user_net_speed4");			    	
			    }
			});
			testImg.unload();
		}
	}
	
	function _switch_spped(showImg){
		$("#_spped_test_li").find("img").each(function(){
			if(showImg == this.id){
				$(this).css({display:"block"});
			}else{
				$(this).css({display:"none"});
			}
		});
	}
	
	//改变预约状态--计划外课程
	function changePlanOutCourseStatus(courseId,studentId,orderType,msg){
		if(null==msg || ""==msg ||undefined==msg){
			msg = "确定要执行此操作吗？";
		}
		alertMsg.confirm(msg,{okCall:function(){
			var url = "${basePath}/edu3/teaching/courseorder/outplancourse.html?courseId="+courseId+"&studentId="+studentId+"&orderType="+orderType;
			$.ajax({
				type:"post",
				url:url,
				dataType:"json",
				success:function(returnData){	
					var operatingStatus =  returnData['operatingStatus'];

				  	if(operatingStatus==true){
				  		window.location.href="${baseUrl}/edu3/framework/index.html";	
					}else{
						var msgList  = returnData['msg'];
						var msg 	 = "";
						for(i=0;i<msgList.length;i++){
							msg+=(i+1)+"、"+msgList[i]+'</br>';
						}
						alertMsg.warn(msg);
					}
				}
			});
		}});
	}
	//预约毕业论文时先确认联系方式
	function thesisChangeTeachingPlanCourseStatus(status,courseId,planId,msg){
		if(status=='thesis'){
			/*$.post("${baseUrl}/edu3/framework/studentinfo/ajax.html",{userid:'${user.resourceid}'}, function (json){
				if(json.success && json.success=='Y'){
					var msginfo = "<b>${user.cnName}</b>同学,你当前的联系信息如下:<br/>联系电话:"+json.mobile+"<br/>邮箱地址:"+json.email;
					if(json.mobile==""||json.email==""){
						msginfo += "<br/><b>补全联系信息请点击\"修改\"按钮</b>";
					}
					alertMsg.confirm(msginfo,{okName:'确定',okCall:function(){
						changeTeachingPlanCourseStatus(status,courseId,planId,msg);
					},cancelName:'修改',cancelCall:function(){
						$.pdialog.open("${baseUrl}/edu3/framework/user/setting.html", "student_info_setting", "设置", {mask:true});
						return false;
					}});
				} else {
					 changeTeachingPlanCourseStatus(status,courseId,planId,msg);
				}				
			}, "json");*/
			changeTeachingPlanCourseStatus(status,courseId,planId,msg);
		}
	}
	//改变预约状态--计划内课程
	function changeTeachingPlanCourseStatus(status,courseId,planId,msg){	
		if(null==msg || ""==msg ||undefined==msg){
			msg = "确定要执行此操作吗？";
		}
		alertMsg.confirm(msg,{okCall:function(){
			var url = "${basePath}/edu3/framework/setStuTeachingPlanStatus.html?teachingPlanId="+planId+"&teachingPlanCourseId="+courseId+"&teachingPlanCourseStatus="+status;
			$.ajax({
				type:"post",
				url:url,
				dataType:"json",
				success:function(resultDate){	
				 	  
					 if(resultDate['operateError']==true){
				 			var msgList = resultDate['msg'];
							var msg = "";
							for(i=0;i<msgList.length;i++){
								msg+=(i+1)+"、"+msgList[i]+'\n';
							}
							alertMsg.warn(msg);
	
					 }else if(resultDate['operateType']=='orderCourse'){
						 	if(resultDate['orderCourseStatus']==false){
						 		var isNeedChooseCourse  = resultDate['isNeedChooseCourse'];
								var msgList 			= resultDate['msg'];
								var msg     			= "";
								
								for(i=0;i<msgList.length;i++){
									msg +="<strong>"+(i+1)+"、</strong>"+msgList[i]+'</br>';
								}
								//当有考试冲突时弹出确认框供用户选择是否需要调整预约课程
								if(true===isNeedChooseCourse){
									var conFlictCourseChooseURL = "${basePath}/edu3/teaching/courseorder/student-order-manager.html?operateType=orderCourse";
									
									alertMsg.confirm(msg+"</br>是否需要重新选择本次的预约课程?",{okCall:function(){
										navTab.openTab('RES_STUDENT_PLAN', conFlictCourseChooseURL, '调整我的学习计划');
									}});
									return false;
								}
								alertMsg.warn(msg);
							}else{
								
								 alertMsg.correct('预约成功，请刷新本页面查看预约状态!');
								 window.location.href="${baseUrl}/edu3/framework/index.html";
		 
							}
					 }else if(resultDate['operateType']=='orderExam'){
						 	if(resultDate['orderExamStatus']==false){
								var msgList = resultDate['msg'];
								var isNeedChooseCourse  = resultDate['isNeedChooseCourse'];
								var msg = "";
								for(i=0;i<msgList.length;i++){
									msg+=(i+1)+"、"+msgList[i]+'</br>';
								}
								//当有考试冲突时弹出确认框供用户选择是否需要调整预约课程
								if(true===isNeedChooseCourse){
									var conFlictCourseChooseURL = "${basePath}/edu3/teaching/courseorder/student-order-manager.html?operateType=orderExam";
									alertMsg.confirm(msg+"</br>是否需要重新选择本次的考试预约?",{okCall:function(){
										navTab.openTab('RES_STUDENT_PLAN_EXAMORDER_CHOOSE', conFlictCourseChooseURL, '考试预约调整');
									}});
									return false;
								}
								alertMsg.warn(msg);
							}else{
								alertMsg.correct('预约成功，请刷新本页面查看预约状态!');
								window.location.href="${baseUrl}/edu3/framework/index.html";	
							}
					 }else if(resultDate['operateType']=='orderGraduatePaper'){
						 
						 	if(resultDate['orderGraduatePaperStatus']==false){
								var msgList = resultDate['msg'];
								var msg = "";
								for(i=0;i<msgList.length;i++){
									msg+=(i+1)+"、"+msgList[i]+'</br>';
								}
								alertMsg.warn(msg);
							}else{
								alertMsg.correct('预约成功，请刷新本页面查看预约状态!');
								window.location.href="${baseUrl}/edu3/framework/index.html";	
							}
					 }
				}
			});
		}});
	}
		
	//从系统注销
	function logout(){
		alertMsg.confirm("确定要退出系统吗！", {
			okCall: function(){				
				window.location.href="${basePath}/j_spring_security_logout";
				return false;
			}
		});
	}		
	
	//切换天气
	function _switchWeather(htmlId){
		 var tdObj = $("#todayWeather");  
         var tmObj = $("#tomorrowWeather");
         if(htmlId == "todayWeather"){
         	tdObj.show();
         	tmObj.hide();
         }else if(htmlId =="tomorrowWeather" ){
        	 tdObj.hide();
         	 tmObj.show();
         }
	}
	
	//打开门户
	function openPortal(url){
		window.open(url);
	}
	//bbs
	function openBbs(){
		window.open("${baseUrl }/edu3/learning/bbs/index.html","bbspage");
	}
	//openDownloads
	function openDownloads(){
		window.open("${baseUrl }/simpleDownloads.html","downloads");
	}
	//重考先检查是否沿用或重新累积平时分，再执行预约操作
	function changeReExamStudyStatus(isPlanOutCourse,courseId,msg,planIdOrStuId,statusOrType,tcourseId){			
		$.ajax({
	 		url: baseUrl+"/edu3/learning/interactive/course/hasresource/ajax.html",
	 		type: 'POST',
	 		async:false,
	 		dataType: 'json', 	
	 		data:{courseId:courseId,hasResource:'Y',isNeedReExamination:'Y',from:'exam'},//参数设置 	
	 		success: function(json){
	 			if(json.hasResource && json.hasResource=='Y' && json.isRedoCourseExam && json.isRedoCourseExam=='Y'&& json.isHasUsualResults &&json.isHasUsualResults=='Y'){
					var msgTip = "您这学期需要重考，";
					if(json.usualResults){
						msgTip += "上学期的平时成绩为"+json.usualResults+"分。";
					}
					msgTip += "您是否沿用上学期的平时成绩或重新累积平时成绩？";
					alertMsg.confirm2(msgTip,{
						okName: '沿用',
						okCall:function () {
							doSaveUsualResults('N',isPlanOutCourse,courseId,msg,statusOrType,planIdOrStuId,tcourseId);						
						},
						cancelName:'重新累积',
						cancelCall:function (){
							doSaveUsualResults('Y',isPlanOutCourse,courseId,msg,statusOrType,planIdOrStuId,tcourseId);						
						}
					});	
				} else {
					if(isPlanOutCourse == 'N'){
		 				changeTeachingPlanCourseStatus(statusOrType,tcourseId,planIdOrStuId,msg);
		 			} else {
		 				changePlanOutCourseStatus(courseId,planIdOrStuId,statusOrType,msg);
		 			}
				} 
	 		}
		});		
	}
	function doSaveUsualResults(isRedoCourseExam,isPlanOutCourse,courseId,msg,statusOrType,planIdOrStuId,tcourseId){
		$.ajax({
	 		url: baseUrl+"/edu3/learning/interactive/isredocourseexam/save.html",
	 		type: 'POST',
	 		async:false,
	 		dataType: 'json', 	
	 		data:{courseId:courseId,isRedoCourseExam:isRedoCourseExam},
	 		success: function(res){
	 			if(isPlanOutCourse == 'N'){
	 				changeTeachingPlanCourseStatus(statusOrType,tcourseId,planIdOrStuId,msg);
	 			} else {
	 				changePlanOutCourseStatus(courseId,planIdOrStuId,statusOrType,msg);
	 			}						 			
	 		}
		});	
	}
		
	//进入教学站
	function goInteractive(courseId,tCourseId,isNeedReExamination,teachType,term){
		var currentTerm = "${term}";
		var flag = parseInt(term)>parseInt(currentTerm)?true:false;
		if(flag){
			alertMsg.warn("该门课程不是当前学期网络课程，无法进入学习。");
		}else{
			$.ajax({
		 		url: baseUrl+"/edu3/learning/interactive/course/hasresource/ajax.html",
		 		type: 'POST',
		 		async:false,
		 		dataType: 'json', 	
		 		data:{courseId:courseId,hasResource:'Y',isNeedReExamination:isNeedReExamination,from:'study'},//参数设置 	
		 		success: function(json){
		 			if(json.hasResource && json.hasResource=='N'){	
						 //alertMsg.warn("该课程暂时没有课件资源。<br/>您可以点击右上角反馈按钮进行反馈。");	
						alertMsg.confirm("这门课程没有网上课件资源，请根据教材及其他相关资料在线下学习！",{
							okName: '问题反馈',
							okCall:function () {
								navTab.openTab('RES_STUDENT_FEEDBACK_ADD', "${baseUrl}/edu3/student/feedback/input.html?courseId="+courseId, '新增反馈');
							}
						});	
					} else {
						window.open("${baseUrl }/edu3/learning/interactive/main.html?courseId="+courseId+"&teachType="+teachType,"interactive");
					}
		 		}
			});	
		}
	}
	function redoCourseExam(courseId,isRedoCourseExam){
		$.ajax({
	 		url: baseUrl+"/edu3/learning/interactive/isredocourseexam/save.html",
	 		type: 'POST',
	 		async:false,
	 		dataType: 'json', 	
	 		data:{courseId:courseId,isRedoCourseExam:isRedoCourseExam}, 	
	 		success: function(json){
	 			if(json.statusCode==200){
					window.open("${baseUrl }/edu3/learning/interactive/main.html?courseId="+courseId,"interactive");
				}	
	 		}
		});			
	}
	//弹出窗口
	function popWin(title,content){	
		$('#loft_win_head').html(title);
		$('#msgcontent').html(content);
		pop.show();
	}	
	
	//更新用户状态
	function updateUserLoginInfo(){
	 $.ajax({
		   type: "GET",
		   url: "${baseUrl }/edu3/framework/system/org/user/updateinfo.html",	
		   dataType: "json",
		   global:false,
		   success: function(data){	 
		   	if(data.display == "ok"){			   	   
		   	}else{			   		
		   	}
		   }
		 });
	}
	
	
	//天气信息	
	function getCityWeather(){
		var htmlStr = "";	
		$.ajax({
			  url: "${baseUrl}/portal/service/weather/service.html?city="+encodeURIComponent('${city}')+"&jsoncallback=?",
			  cache: true,
			  dataType:"jsonp",
			  jsonp:"jsoncallback",
			  global:false,
			  success: function(data){
				  if(data.forecasts){
					  $('#currentCity').html(data.name);					   	
				   		htmlStr+= "<img src='"+data.forecasts[0].image_small+"' />";
				   		htmlStr+= "<b> "+data.forecasts[0].low+"°- "+data.forecasts[0].high+"°,"+data.forecasts[0].text+"</b>";
				   		htmlStr+= "<a href='#' style='margin-left:8px' onclick=_switchWeather('tomorrowWeather')>明天>></a>";
				   		$("#todayWeather").html(htmlStr);
				   		htmlStr = "";
				   		htmlStr+= "<img src='"+data.forecasts[1].image_small+"' />";
				   		htmlStr+= "<b> "+data.forecasts[1].low+"°- "+data.forecasts[1].high+"°,"+data.forecasts[1].text+"</b>";	   	
				   		htmlStr+= "<a href='#'style='margin-left:8px' onclick=_switchWeather('todayWeather')><<今天</a>";
				   		$("#tomorrowWeather").html(htmlStr);
				  }else{
// 					  $("#todayWeather").html("获取天气信息失败...<a href='###' onclick='getCityWeather();'>重试</a>");
				  }
				  				 
			  }
			});
	}
	
	//系统消息
	function getMessage(){
		var htmlStr = "";
		$.ajax({
		   type: "GET",
		   url: "${baseUrl }/edu3/framework/getMessage.html",	
		   dataType: "json",
		   global:false,
		   success: function(msgs){	 
			   if ( $.browser.msie && /msie 6\.0/i.test(navigator.userAgent) ) {//判断用户浏览器是否为IE6,如果是则提示更新
				   htmlStr += "<li style='padding-top:6px;color:#ff4F00'>";		       	   
		           htmlStr += "[温馨提示] 您的浏览器为IE6,请<a href='${baseServerUrl }${rootUrl}system/software/IE8-WindowsXP-x86-CHS.zip' target='_blank'>升级到IE8</a>,或使用<a href='${baseServerUrl }/edu3/attachs/system/software/FirefoxSetup3.6.20.zip' target='_blank'>火狐浏览器</a>";		         
		           htmlStr += "</li>";
			   }
			   var newMsgNum = 0;
		       for(i=0;i<msgs.length;i++){
		           htmlStr += "<li style='padding-top:6px;";
		       	   if(!msgs[i].isRead){
		       	   	  htmlStr += "font-weight: bold;";
		       	   	  newMsgNum++;
		       	   }		       	   
		           htmlStr += "'><span> ["+msgs[i].msgType+"] </span><span class='time'> ["+msgs[i].sendTime+"] </span>";
		           htmlStr += "来自<font color='blue'>"+msgs[i].sender+"</font>：";
		           //htmlStr += "<a href='${baseUrl }/edu3/framework/message/show.html?msgId="+msgs[i].resourceid+"&type=inbox' height='600' width='800' target='dialog' title='查看消息'>"+msgs[i].msgTitle+"</a>";
		           htmlStr += "<a href='${baseUrl }/edu3/framework/message/show.html?msgId="+msgs[i].resourceid+"&type=inbox' onclick='showMessage(this);return false;' title='查看消息'>"+msgs[i].msgTitle+"</a>";
		           htmlStr += "</li>";
		       }
		       if(htmlStr==""){
		       	   htmlStr += "<li style='padding-top:6px;'>暂无未读消息</li>";
		       } 
		       if(newMsgNum>0){
		    	   $("#_newMsgNum").html(newMsgNum);
		       }
		       $("#sysMessageList").append(htmlStr);
		   }
		});
	}
	//查看消息窗口
	function showMessage(obj){
		$.pdialog.open(obj.href, "view_message", "查看消息", {width: 800, height: 600});
	}
	//获取教学日历
	function getTeachCalendar(num){
		var m = $("#variable_months");
		var months = num;
		if(num!=0){
			months = months + parseInt(m.val());
		}
		$.ajax({
		   type: "POST",
		   url: "${baseUrl }/edu3/framework/getteachcalendar.html",	
		   data: "months="+months,
		   dataType: "json",	
		   global:false,
		   success: function(data){	 
			   m.val(months);
			   $("#current_variable_date").html(data.current_date);
			   var days = data.days;
			   for(i=0;i<days.length;i++){
				   $("#CalendarDays"+i).html(days[i]);
			   }
			   $('._calendar_tips').tipsy({opacity:1.0,fade: true, html:true});			 
		   }
		});
	}
	//获取学生学习日历
	function getStudentCalendar(){
		$.ajax({
		   type: "POST",
		   url: "${baseUrl }/edu3/framework/teachingcalendar/list.html",	
		   dataType: "json",
		   global:false,
		   success: function(data){	 
			   var calObj,dayObj,dayObjContent;
			   for (var i in data) {
				   calObj = data[i];
				   if(calObj['calSize']>=0){
					   dayObj = $("#CalendarDays"+i);	
					   dayObjContent = dayObj.html();
					   dayObj.html("<b>"+dayObjContent+"</b><a href='##' id='student_calendar_tips_"+i+"' style='font-weight:bold;' class='_student_calendar_tips'>("+calObj['calSize']+")</a><div class='student_calendar_tips_content' style='display:none;'>"+calObj['calContent']+"</div>");
					   $("#student_calendar_tips_"+i).tipsy({gravity:'ne',opacity:1.0,fade: true, html:true,title:function (){return $(this).next('.student_calendar_tips_content').html();}});
				   }				   
			   }			 
		   }
		});
	}
	//获取帮助文档
	function getHelpdoc(){		
		window.open("${baseUrl}/portal/help/index.html", "v3_help");
	}
	
	// 查看某个学生某门课程的网上学习成绩
	function viewStudentOnlineScore2(planCourseId,gradeId,schoolId,planId,studentId,courseId,teachType) {
		var url = "${baseUrl }/edu3/teaching/usualresults/viewOnlineScore_student.html";
		$.pdialog.open(url+"?planCourseId="+planCourseId+"&gradeId="+gradeId+"&schoolId="+schoolId+"&planId="+planId+"&studentId="+studentId+"&courseId="+courseId+"&teachType="+teachType,"","查看网上学习成绩",{mask:true,height:400,width:600});
	}
	
	function getNoExmaresultExamInfo(){
		if("${examInfo}"!=null && "${examInfo}"!="" && "11078"=="${schoolCode}"){//广大
			var option = {resizable:true,drawable:true};
			//alertMsg.confirm("${examInfo}"+"${courseInfo}");
			//$.messager.lays(300, 200);  
			//$.messager.show('<font color=red>消息提示</font>','${examInfo}',0); //自己点击关闭才能关闭  
			//$.messager.show(0, '一秒钟关闭消息', 1000);
		}
	}
 </script>
</body>
</html>
