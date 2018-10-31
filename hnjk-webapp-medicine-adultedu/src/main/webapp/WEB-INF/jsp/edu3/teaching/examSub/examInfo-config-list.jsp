<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>考试课程设置</title>
</head>
<body>
	<div class="page">
		<div class="pageHeader">
			<form id="examInfoConfigSearchForm"
				onsubmit="return navTabSearch(this);"
				action="${baseUrl}/edu3/teaching/exam/examinfo-config-page.html"
				method="post">
				<input name="fromPage" value="Y" type="hidden" /> <input name="type"
					value="${pageType }" type="hidden" /> <input name="resourceid"
					value="${resourceid }" type="hidden" />
				<div class="searchBar">
					<ul class="searchContent">
						<li><label>课程：</label>
						<gh:courseAutocomplete name="courseId" tabindex="1"
								id="examInfoConfig_courseId" value="${courseId}"
								displayType="code" isFilterTeacher="Y"
								taskCondition="yearInfo.resourceid='${examSub.yearInfo.resourceid }',term='${examSub.term }'"
								style="width:53%" /></li>
					</ul>
					<ul class="searchContent">
						<div class="buttonActive" style="float: right">
							<div class="buttonContent">
								<button id="searchGData" type="submit">查 询</button>
							</div>
						</div>
					</ul>
				</div>
			</form>
		</div>
		<div class="pageContent">
			<div class="tabs"
				<c:if test="${not empty currentIndex }">currentIndex=${currentIndex }</c:if>>
				<div class="tabsHeader">
					<div class="tabsHeaderContent">
						<ul>
							<li class="#"><a href="#"><span>网授考试课程信息</span></a></li>
							<c:if test="${isHasOnlineExamInfo }">
								<li class="#"><a href="#"><span>网考课程信息</span></a></li>
							</c:if>
							<c:if test="${isHasFaceExamInfo }">
								<li class="#"><a href="#"><span>面授考试课程信息</span></a></li>
							</c:if>
							<c:if test="${isHasFaceAndNetExamInfo }">
								<li class="#"><a href="#"><span>面授+网授考试课程信息</span></a></li>
							</c:if>
						</ul>
					</div>
				</div>
				<div class="tabsContent" style="height: 100%;">
					<%---------------------------------------------------------------- 考试课程信息 ----------------------------------------------------------------%>
					<div>
						<table>
							<tr>
								<td colspan="8"><span class="buttonActive"><div
											class="buttonContent">
											<button type="button"
												onclick="examInfoConfingShowPage('0','examInfoConfig_0_Body','0');">设置成绩比例</button>
										</div></span></td>
							</tr>
						</table>
						<table class="table" id="examinfoConfig_0" layoutH="80"
							width="100%">
							<thead>
								<tr>
									<th style="width: 5%"><input type="checkbox"
										name="checkall" id="check_all_examinfo_config_0"
										onclick="checkboxAll('#check_all_examinfo_config_0','resourceid','#examInfoConfig_0_Body')" /></th>
									<th style="width: 10%; text-align: center;"><strong>考试课程编号</strong></th>
									<th style="width: 20%;"><strong>课程名称</strong></th>
									<th style="width: 10%; text-align: center;"><strong>考试形式</strong></th>
									<th style="width: 10%; text-align: center;"><strong>考试课程类型</strong></th>
									<th style="width: 20%; text-align: center;"><strong>考试时间</strong></th>
									<th style="width: 15%; text-align: center;"><strong>成绩比例</strong></th>
									<th style="width: 10%; text-align: center;"><strong>成绩类型</strong></th>
								</tr>
							</thead>
							<tbody id="examInfoConfig_0_Body">
								<c:forEach items="${examSub.examInfo }" var="c" varStatus="vs">
									<c:if
										test="${c.course.examType ne 6 and c.examCourseType ne 1 and c.examCourseType ne 2}">
										<tr>
											<td><input type='checkbox' name="resourceid"
												value='${c.resourceid }' title="${c.course.courseName }" /></td>
											<td style="text-align: center;">${c.examCourseCode }</td>
											<td>${c.course.courseName }</td>
											<td style="text-align: center;">${ghfn:dictCode2Val('CodeCourseExamType',c.course.examType)}</td>
											<td style="text-align: center;">${ghfn:dictCode2Val('CodeExamInfoCourseType',c.examCourseType)}
											</td>
											<td style="text-align: center;"><fmt:formatDate
													value="${c.examStartTime }" pattern="yyyy年MM月dd日 HH:mm:ss" />&nbsp;-&nbsp;
												<fmt:formatDate value="${c.examEndTime }" pattern="HH:mm:ss" />
											</td>
											<td style="text-align: center;"><c:if
													test="${not empty c.studyScorePer }">
										卷面：<fmt:formatNumber value="${c.studyScorePer }" pattern="#" />%
										平时：<fmt:formatNumber value="${100 - c.studyScorePer }"
														pattern="#" />%
									</c:if></td>
											<td style="text-align: center;">
												${ghfn:dictCode2Val('CodeCourseScoreStyle',c.courseScoreType)}
											</td>
										</tr>
									</c:if>
								</c:forEach>
							</tbody>
						</table>
					</div>
					<%---------------------------------------------------------------- 考试课程信息 ----------------------------------------------------------------%>
					<%---------------------------------------------------------------- 统考课程信息 ----------------------------------------------------------------%>
					<c:if test="${isHasOnlineExamInfo }">
						<div>
							<table>
								<tr>
									<td colspan="8"><span class="buttonActive"><div
												class="buttonContent">
												<button type="button"
													onclick="examInfoConfingShowPage('0','examInfoConfig_1_Body','1');">设置成绩比例</button>
											</div></span></td>
								</tr>
							</table>
							<table class="table" id="examinfoTab_online" layoutH="80">
								<thead>
									<tr>
										<th style="width: 5%"><input type="checkbox"
											name="checkall" id="check_all_examinfo_config_1"
											onclick="checkboxAll('#check_all_examinfo_config_1','resourceid','#examInfoConfig_1_Body')" /></th>
										<th style="width: 20%"><strong>课程名称</strong></th>
										<th style="width: 10%; text-align: center;"><strong>考试形式</strong></th>
										<th style="width: 15%; text-align: center;"><strong>考试开始时间</strong></th>
										<th style="width: 15%; text-align: center;"><strong>考试结束时间</strong></th>
										<th style="width: 10%; text-align: center;"><strong>成卷规则</strong></th>
										<th style="width: 15%; text-align: center;"><strong>成绩比例</strong></th>
										<th style="width: 10%; text-align: center;"><strong>成绩类型</strong></th>
									</tr>
								</thead>
								<tbody id="examInfoConfig_1_Body">
									<c:forEach items="${examSub.examInfo }" var="c" varStatus="vs">
										<c:if test="${c.course.examType eq 6 }">
											<tr>
												<td><input type='checkbox' name="resourceid"
													value='${c.resourceid }' title="${c.course.courseName }" /></td>
												<td>${c.course.courseName }</td>
												<td style="text-align: center;">${ghfn:dictCode2Val('CodeCourseExamType',c.course.examType)}
												</td>
												<td style="text-align: center;"><fmt:formatDate
														value="${c.examStartTime }" pattern="yyyy年MM月dd日 HH:mm:ss" /></td>
												<td style="text-align: center;"><fmt:formatDate
														value="${c.examEndTime }" pattern="yyyy年MM月dd日 HH:mm:ss" /></td>
												<td style="text-align: center;">${c.courseExamRules.courseExamRulesName }</td>
												<td style="text-align: center;"><c:if
														test="${not empty c.studyScorePer }">
										    卷面：<fmt:formatNumber value="${c.studyScorePer }"
															pattern="#" />%
										   平时：<fmt:formatNumber value="${100 - c.studyScorePer }"
															pattern="#" />%
								   </c:if></td>
												<td style="text-align: center;">${ghfn:dictCode2Val('CodeCourseScoreStyle',c.courseScoreType)}</td>
											</tr>
										</c:if>
									</c:forEach>
								</tbody>
							</table>
						</div>
					</c:if>
					<%---------------------------------------------------------------- 网考课程信息 ----------------------------------------------------------------%>
					<%---------------------------------------------------------------- 面授考试课程信息 ----------------------------------------------------------------%>
					<c:if test="${isHasFaceExamInfo }">
						<div>
							<table>
								<tr>
									<td colspan="7" style="text-align: center; font-weight: bold;">
										<span class="buttonActive"><div class="buttonContent">
												<button type="button"
													onclick="examInfoConfingShowPage('1','examInfoConfig_2_Body','2');">设置成绩比例</button>
											</div></span>
									</td>
								</tr>
							</table>
							<table class="table" id="brexaminfoTab" layoutH="80">
								<thead>
									<tr>
										<th style="width: 3%"><input type="checkbox"
											name="checkall" id="check_all_examinfo_config_2"
											onclick="checkboxAll('#check_all_examinfo_config_2','resourceid','#examInfoConfig_2_Body')" /></th>
										<th style="width: 15%"><strong>课程名称</strong></th>
										<th style="width: 6%; text-align: center;"><strong>考试形式</strong></th>
										<th style="width: 6%; text-align: center;"><strong>考试课程类型</strong></th>
										<th style="width: 20%; text-align: center;"><strong>考试时间</strong></th>
										<th style="width: 20%; text-align: center;"><strong>成绩比例</strong></th>
										<th style="width: 10%; text-align: center;"><strong>成绩类型</strong></th>
									</tr>
								</thead>
								<tbody id="examInfoConfig_2_Body">
									<c:forEach items="${examSub.examInfo }" var="c" varStatus="vs">
										<c:if test="${c.examCourseType eq 1 }">
											<tr>
												<td><input type='checkbox' name="resourceid"
													value='${c.resourceid }' title="${c.course.courseName }" /></td>
												<td>${c.course.courseName }</td>
												<td style="text-align: center;">${ghfn:dictCode2Val('CodeCourseExamType',c.course.examType)}</td>
												<td style="text-align: center;">${ghfn:dictCode2Val('CodeExamInfoCourseType',c.examCourseType)}
												</td>
												<td style="text-align: center;"><fmt:formatDate
														value="${c.examStartTime }" pattern="yyyy年MM月dd日 HH:mm:ss" />&nbsp;-&nbsp;
													<fmt:formatDate value="${c.examEndTime }"
														pattern="HH:mm:ss" /></td>
												<td style="text-align: center;">
													卷面：<fmt:formatNumber value="${c.facestudyScorePer }" pattern="#" />%&nbsp;
													平时考核：<fmt:formatNumber value="${c.facestudyScorePer2 }" pattern="#" />%&nbsp;
													<%-- 网上学习：<fmt:formatNumber value="${c.facestudyScorePer3 }" pattern="#" />%&nbsp; --%>
												</td>
												<td style="text-align: center;">${ghfn:dictCode2Val('CodeCourseScoreStyle',c.courseScoreType)}</td>
											</tr>
										</c:if>
									</c:forEach>
								</tbody>
							</table>
						</div>
					</c:if>
					<%---------------------------------------------------------------- 面授考试课程信息 ----------------------------------------------------------------%>
					<%---------------------------------------------------------------- 面授+网授考试课程信息 ----------------------------------------------------------------%>
					<c:if test="${isHasFaceAndNetExamInfo }">
						<div>
							<table>
								<tr>
									<td colspan="8"><span class="buttonActive"><div
												class="buttonContent">
												<button type="button"
													onclick="examInfoConfingShowPage('2','examInfoConfig_3_Body','3');">设置成绩比例</button>
											</div></span></td>
								</tr>
							</table>
							<table class="table" id="faceAndNetExaminfoTab" layoutH="80">
								<thead>
									<tr>
										<th style="width: 5%"><input type="checkbox"
											name="checkall" id="check_all_examinfo_config_3"
											onclick="checkboxAll('#check_all_examinfo_config_3','resourceid','#examInfoConfig_3_Body')" /></th>
										<th style="width: 30%"><strong>课程名称</strong></th>
										<th style="width: 10%; text-align: center;"><strong>考试形式</strong></th>
										<th style="width: 10%; text-align: center;"><strong>考试课程类型</strong></th>
										<th style="width: 20%; text-align: center;"><strong>考试时间</strong></th>
										<th style="width: 15%; text-align: center;">成绩比例</th>
										<th style="width: 10%; text-align: center;">成绩类型</th>
									</tr>
								</thead>
								<tbody id="examInfoConfig_3_Body">
									<c:forEach items="${examSub.examInfo }" var="c" varStatus="vs">
										<c:if test="${c.examCourseType eq 2 }">
											<tr>
												<td><input type='checkbox' name="resourceid"
													value='${c.resourceid }' title="${c.course.courseName }" /></td>
												<td>${c.course.courseName }</td>
												<td style="text-align: center;">${ghfn:dictCode2Val('CodeCourseExamType',c.course.examType)}</td>
												<td style="text-align: center;">${ghfn:dictCode2Val('CodeExamInfoCourseType',c.examCourseType)}
												</td>
												<td style="text-align: center;"><fmt:formatDate
														value="${c.examStartTime }" pattern="yyyy年MM月dd日 HH:mm:ss" />&nbsp;-&nbsp;
													<fmt:formatDate value="${c.examEndTime }"
														pattern="HH:mm:ss" /></td>
												<td style="text-align: center;"><c:if
														test="${not empty c.studyScorePer }">
								     	
										卷面：<fmt:formatNumber value="${c.studyScorePer }" pattern="#" />%
										平时：<fmt:formatNumber value="${100 - c.studyScorePer }"
															pattern="#" />%
									</c:if></td>
												<td style="text-align: center;">${ghfn:dictCode2Val('CodeCourseScoreStyle',c.courseScoreType)}</td>

											</tr>
										</c:if>
									</c:forEach>
								</tbody>
							</table>
						</div>
					</c:if>
					<%---------------------------------------------------------------- 面授+网授考试课程信息 ----------------------------------------------------------------%>
				</div>
				<div class="tabsFooter">
					<div class="tabsFooterContent"></div>
				</div>
			</div>

		</div>
	</div>
	<script type="text/javascript">
		//设置成绩比例
		function examInfoConfingShowPage(examCourseType,tbodyId,currentIndex){
			var res  	   = new Array();
			var examSubId  = "${examSub.resourceid}";
			var url        = "${baseUrl}/edu3/teaching/exam/examinfo-config-page.html?type=setting&currentIndex="+currentIndex+"&examCoureType="+examCourseType+"&resourceid="+examSubId;
			$("#"+tbodyId+" INPUT[name='resourceid']:checked").each(function(ind){
				res.push($(this).val());
			});
			if(res.length>0){
				alertMsg.confirm("确定要设置所选课程的成绩比例吗？", {
					 okCall: function(){
						 $.pdialog.open(url+"&ids="+res.toString(),"RES_TEACHING_EXAM_PLAN_EXAMSCOREPER_CONFIG_SETTING","成绩比例设置",{width:800, height:600,mask:true});	
					 }
				});
			}else{
				alertMsg.warn("请选择要设置的考试课程!");
				return false;
			}
		}
	</script>
</body>
