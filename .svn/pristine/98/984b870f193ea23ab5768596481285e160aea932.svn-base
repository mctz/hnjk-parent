<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>成绩管理</title>


</head>
<body>
	<div class="page">
		<div class="pageHeader">
			<form id="examResultsReviewExamresultsSearchForm"
				onsubmit="return navTabSearch(this);"
				action="${baseUrl}/edu3/teaching/examresult/review-examresults-list.html"
				method="post">
				<input id="examResultsReviewExamresultsSearchForm_examSubId"
					type="hidden" name="examSubId" value="${condition['examSubId'] }" />
				<input id="examResultsReviewExamresultsSearchForm_examInfoId"
					type="hidden" name="examInfoId" value="${condition['examInfoId'] }" />
				<div class="searchBar">
					<ul class="searchContent">
						<li><label>审核状态：</label> <select name="auditStatus">
								<option value="">请选择</option>
								<option value="0"
									<c:if test="${condition['auditStatus'] eq '0' }">selected="selected"</c:if>>待审核</option>
								<option value="1"
									<c:if test="${condition['auditStatus'] eq '1' }">selected="selected"</c:if>>审核通过</option>
						</select></li>
						<div class="buttonActive" style="float: right">
							<div class="buttonContent">
								<button type="submit">查 询</button>
							</div>
						</div>
					</ul>
					<font color="red">
						<p style="font-weight: bolder">XX成绩B 代表 更正前XX成绩</p>
						<p style="font-weight: bolder">XX成绩A 代表 更正后XX成绩</p>
					</font>
				</div>
			</form>
		</div>
		<gh:resAuth parentCode="RES_TEACHING_RESULT_EXAMRESULTS_REVIEW"
			pageType="subList"></gh:resAuth>
		<div class="pageContent">
			<form id="examResultsReviewExamresultsSaveForm"
				action="${baseUrl}/edu3/teaching/examresult/review.html"
				method="post">
				<input id="examResultsReviewExamresultsSaveForm_examSubId"
					type="hidden" name="examSubId" value="${condition['examSubId'] }" />
				<input id="examResultsReviewExamresultsSaveForm_examInfoId"
					type="hidden" name="examInfoId" value="${condition['examInfoId'] }" />
				<table class="table" layouth="138" width="100%">
					<thead>
						<tr>
							<c:choose>
								<c:when test="${info.ismixture eq 'Y'}">
									<th width="4%" style="text-align: center;"><input
										type="checkbox" name="checkall"
										id="check_all_review_examResults"
										onclick="checkboxAll('#check_all_review_examResults','resourceid','#examResultsReviewExamresultsBody')" /></th>
									<th width="8%" style="text-align: center;">教学站</th>
									<th width="8%" style="text-align: center;">学号</th>
									<th width="8%" style="text-align: center;">姓名</th>

									<th width="6%" style="text-align: center;">笔考成绩B</th>
									<th width="6%" style="text-align: center;">机考成绩B</th>
									<th width="6%" style="text-align: center;">平时成绩B</th>
									<th width="6%" style="text-align: center;">综合成绩B</th>

									<th width="6%" style="text-align: center;">笔考成绩A</th>
									<th width="6%" style="text-align: center;">机考成绩A</th>
									<th width="6%" style="text-align: center;">平时成绩A</th>
									<th width="6%" style="text-align: center;">综合成绩A</th>

									<th width="8%" style="text-align: center;">更正人时间</th>
									<th width="8%" style="text-align: center;">复查人时间</th>
									<th width="8%" style="text-align: center;">备注</th>
								</c:when>
								<c:otherwise>
									<th width="4%" style="text-align: center;"><input
										type="checkbox" name="checkall"
										id="check_all_review_examResults"
										onclick="checkboxAll('#check_all_review_examResults','resourceid','#examResultsReviewExamresultsBody')" /></th>
									<th width="8%" style="text-align: center;">教学站</th>
									<th width="8%" style="text-align: center;">学号</th>
									<th width="8%" style="text-align: center;">姓名</th>

									<th width="7%" style="text-align: center;">卷面成绩B</th>
									<th width="7%" style="text-align: center;">平时成绩B</th>
									<th width="7%" style="text-align: center;">综合成绩B</th>

									<th width="7%" style="text-align: center;">卷面成绩A</th>
									<th width="7%" style="text-align: center;">平时成绩A</th>
									<th width="7%" style="text-align: center;">综合成绩A</th>

									<th width="11%" style="text-align: center;">更正人时间</th>
									<th width="11%" style="text-align: center;">复查人时间</th>
									<th width="8%" style="text-align: center;">备注</th>
								</c:otherwise>
							</c:choose>

						</tr>
					</thead>

					<tbody id="examResultsReviewExamresultsBody">
						<c:forEach items="${page.result}" var="audit" varStatus="vs">
							<tr>
								<td style="text-align: center;"><input type="checkbox"
									name="resourceid" value="${audit.resourceid }"
									autocomplete="off" /></td>
								<td style="text-align: center; vertical-align: middle;"
									title="${audit.examResults.studentInfo.branchSchool.unitCode }-${audit.examResults.studentInfo.branchSchool.unitName }">${audit.examResults.studentInfo.branchSchool.unitCode }-${audit.examResults.studentInfo.branchSchool.unitShortName }</td>
								<td style="text-align: center; vertical-align: middle;"
									title="${audit.examResults.studentInfo.studyNo}">${audit.examResults.studentInfo.studyNo}</td>
								<td style="text-align: center; vertical-align: middle;"
									title="${audit.examResults.studentInfo.studentName}">${audit.examResults.studentInfo.studentName}</td>
								<%--如果是混合机考课程则增加笔考成绩列 --%>
								<c:if test="${info.ismixture eq 'Y'}">
									<td
										style="text-align: center; vertical-align: middle; color: blue"></td>
								</c:if>
								<%--如果是混合机考课程则增加笔考成绩列 --%>
								<td
									style="text-align: center; vertical-align: middle; color: blue">
									<c:choose>
										<c:when test="${audit.beforeExamAbnormity eq '0'}"> ${audit.beforeWrittenScore }</c:when>
										<c:otherwise>${ghfn:dictCode2Val('CodeExamAbnormity',audit.beforeExamAbnormity) }</c:otherwise>
									</c:choose>
								</td>
								<td
									style="text-align: center; vertical-align: middle; color: blue">${audit.beforeUsuallyScore }</td>
								<td
									style="text-align: center; vertical-align: middle; color: blue">
									<c:choose>
										<c:when test="${audit.beforeExamAbnormity eq '0'}"> ${audit.beforeIntegratedScore }</c:when>
										<c:otherwise>${ghfn:dictCode2Val('CodeExamAbnormity',audit.beforeExamAbnormity) }</c:otherwise>
									</c:choose>
								</td>
								<%--如果是混合机考课程则增加笔考成绩列 --%>
								<c:if test="${info.ismixture eq 'Y'}">
									<td
										style="text-align: center; vertical-align: middle; color: red"></td>
								</c:if>
								<%--如果是混合机考课程则增加笔考成绩列 --%>
								<td
									style="text-align: center; vertical-align: middle; color: red">
									<c:choose>
										<c:when test="${audit.changedExamAbnormity eq '0'}"> ${audit.changedWrittenScore }</c:when>
										<c:otherwise>${ghfn:dictCode2Val('CodeExamAbnormity',audit.changedExamAbnormity) }</c:otherwise>
									</c:choose>
								</td>
								<td
									style="text-align: center; vertical-align: middle; color: red">${audit.changedUsuallyScore }</td>
								<td
									style="text-align: center; vertical-align: middle; color: red">
									<c:choose>
										<c:when test="${audit.changedExamAbnormity eq '0'}"> ${audit.changedIntegratedScore }</c:when>
										<c:otherwise>${ghfn:dictCode2Val('CodeExamAbnormity',audit.changedExamAbnormity) }</c:otherwise>
									</c:choose>
								</td>
								<td style="text-align: center; vertical-align: middle;"
									title="(${audit.changedMan})<fmt:formatDate value="${audit.changedDate}" pattern="yyyy-MM-dd HH:mm:ss"/>">(${audit.changedMan})<fmt:formatDate
										value="${audit.changedDate}" pattern="yyyy-MM-dd" />
								</td>
								<td style="text-align: center; vertical-align: middle;"><c:if
										test="${not empty audit.auditMan and not empty audit.auditDate}">(${audit.auditMan})${audit.auditDate}</c:if>
								</td>
								<td style="text-align: center; vertical-align: middle;"
									title="${audit.memo}"><input style="width: 95%"
									name="memo_${audit.resourceid }" value="${audit.memo}">
								</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</form>
			<gh:page page="${page}" targetType="navTab"
				goPageUrl="${baseUrl }/edu3/teaching/examresult/review-examresults-list.html"
				pageType="sys" condition="${condition }" />
		</div>
	</div>
	<script type="text/javascript">
	
	//复审
	function examresultsReview(flag){
		var msg ="Y"==flag?"审核通过":"退回成绩审核人员";
		var URL = "${baseUrl}/edu3/teaching/examresult/review.html?flag="+flag;
		var auditExamresoultsResourceid = new Array();
		jQuery("#examResultsReviewExamresultsBody input[name='resourceid']:checked").each(function(){
			auditExamresoultsResourceid.push(jQuery(this).val());
		});
		if(auditExamresoultsResourceid.length>0){
			var $form = $("#examResultsReviewExamresultsSaveForm");
			if (!$form.valid()) {
				alertMsg.error(DWZ.msg["validateFormError"]);
				return false; 
			}
			alertMsg.confirm("确认要将所选成绩记录"+msg+"？", {
                okCall: function(){
                	$.ajax({
						type:'POST',
						url:URL,
						data:$form.serializeArray(),
						dataType:"json",
						cache: false,
						error: DWZ.ajaxError,
						success: function(resultData){
							var success  = resultData['success'];
							var msg      = resultData['msg'];
							if(success==false){
								alertMsg.warn(msg);
							}else{
								alertMsg.info(msg);
								navTab.reload($("#examResultsReviewExamresultsSearchForm").attr("action"), $("#examResultsReviewExamresultsSearchForm").serializeArray());
							}
						}
					});
                }
            });
		}else{
			alertMsg.warn("请选择审核不通过的成绩！");
		}
			
	}
	
</script>
</body>
</html>