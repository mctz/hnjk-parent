<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>考试课程设置</title>
</head>
<body>
	<div class="page">

		<form method="post"
			action="${baseUrl}/edu3/teaching/exam/examinfo-config.html"
			class="pageForm"
			onsubmit="return validateCallback(this,dialogAjaxDone);">
			<input type="hidden" name="ids" value="${ids }" /> <input
				type="hidden" name="examSubId" value="${examSubId }"> <input
				type="hidden" name="currentIndex" value="${currentIndex }">
			<input type="hidden" name="examCoureType" value="${examCoureType }">
			<div class="pageContent" layoutH="40">
				<table class="form">
					<c:choose>
						<c:when test="${examCoureType eq '0' }">
							<tr>
								<td nowrap="nowrap">网授课程卷面成绩比例：</td>
								<td><input id="networkstudy_examScorePer_writtenScorePer"
									name="studyScorePer" onblur="//checkFacestudyScorePerSum()"
									class="required" onKeyUp="value=value.replace(/[^\d]/g,'') "
									onbeforepaste="clipboardData.setData('text',clipboardData.getData('text').replace(/[^\d]/g,''))"
									style="width: 20%"
									value="<fmt:formatNumber value="${writtenScorePer }" pattern="#"></fmt:formatNumber>" />%
								</td>
								<td nowrap="nowrap">网授课程网上成绩比例：</td>
								<td><input id="networkstudy_examScorePer_usuallyScorePer"
									name="networkstudy_usuallyScorePer"
									onblur="//checkFacestudyScorePerSum()" class="required number"
									min="0" max="100" onKeyUp="value=value.replace(/[^\d]/g,'') "
									onbeforepaste="clipboardData.setData('text',clipboardData.getData('text').replace(/[^\d]/g,''))"
									style="width: 20%"
									value="<fmt:formatNumber value="${netsidestudyScorePer }" pattern="#"></fmt:formatNumber>" />%
								</td>
								<%-- <td nowrap="nowrap">网授课程网上学习成绩比例：</td>
						<td>
							<input id="facestudy_examScorePer_onlineScorePer" name="facestudyScorePer3"  onblur="//checkFacestudyScorePerSum()" class="required number" min="0" max="100" 
							 onKeyUp="value=value.replace(/[^\d]/g,'') " onbeforepaste="clipboardData.setData('text',clipboardData.getData('text').replace(/[^\d]/g,''))"
							 style="width:20%" value="<fmt:formatNumber value="${facestudyScorePer3 }" pattern="#"></fmt:formatNumber>"/>%
						</td> --%>
							</tr>
						</c:when>
						<c:when test="${examCoureType eq '1' }">
							<tr>
								<td nowrap="nowrap">面授课程卷面成绩比例：</td>
								<td><input id="facestudy_examScorePer_writtenScorePer"
									name="facestudyScorePer" onblur="//checkFacestudyScorePerSum()"
									class="required number" min="0" max="100"
									onKeyUp="value=value.replace(/[^\d]/g,'') "
									onbeforepaste="clipboardData.setData('text',clipboardData.getData('text').replace(/[^\d]/g,''))"
									style="width: 20%"
									value="<fmt:formatNumber value="${facestudyScorePer }" pattern="#"></fmt:formatNumber>" />%
								</td>
								<td nowrap="nowrap">面授课程平时考核成绩比例：</td>
								<td><input id="facestudy_examScorePer_usuallyScorePer"
									name="facestudyScorePer2"
									onblur="//checkFacestudyScorePerSum()" class="required number"
									min="0" max="100" onKeyUp="value=value.replace(/[^\d]/g,'') "
									onbeforepaste="clipboardData.setData('text',clipboardData.getData('text').replace(/[^\d]/g,''))"
									style="width: 20%"
									value="<fmt:formatNumber value="${facestudyScorePer2 }" pattern="#"></fmt:formatNumber>" />%
								</td>
								<%-- <td nowrap="nowrap">面授课程网上学习成绩比例：</td>
						<td>
							<input id="facestudy_examScorePer_onlineScorePer" name="facestudyScorePer3"  onblur="//checkFacestudyScorePerSum()" class="required number" min="0" max="100" 
							 onKeyUp="value=value.replace(/[^\d]/g,'') " onbeforepaste="clipboardData.setData('text',clipboardData.getData('text').replace(/[^\d]/g,''))"
							 style="width:20%" value="<fmt:formatNumber value="${facestudyScorePer3 }" pattern="#"></fmt:formatNumber>"/>%
						</td> --%>
							</tr>
						</c:when>
						<%-- <c:when test="${examCoureType eq '2' }">
					<tr>
						<td>网授+面授课程卷面成绩比例：</td>
						<td>
							<input   id="netsidestudy_examScorePer_writtenScorePer" name="studyScorePer" onblur="genUsuallyScoreRatio('netsidestudy')" class="required" 
							 onKeyUp="value=value.replace(/[^\d]/g,'') " onbeforepaste="clipboardData.setData('text',clipboardData.getData('text').replace(/[^\d]/g,''))"
							 style="width:20%" value="<fmt:formatNumber value="60" pattern="#"></fmt:formatNumber>"/>%
						</td>
						<td>网授+面授课程平时成绩比例：</td>
						<td>
							<input id="netsidestudy_examScorePer_usuallyScorePer" name="netsidestudy_usuallyScorePer"  style="width:20%" class="required" readonly="readonly" value="40" />%
						</td>
					</tr>	
				</c:when> --%>
					</c:choose>

					<tr>
						<td>成绩类型</td>
						<td colspan="5"><gh:select name="courseScoreType"
								dictionaryCode="CodeCourseScoreStyle" value="11" /></td>
					</tr>

				</table>
				<table class="form">
					<tr>
						<td colspan="4" style="text-align: center;"><strong>以下为所选课程当前的成绩比例</strong></td>
					</tr>
					<tr>
						<td style="width: 15%"><strong>课程名称</strong></td>
						<td style="width: 20%; text-align: center;" colspan="2">成绩比例</td>
						<td style="width: 10%; text-align: center;"><strong>成绩类型</strong></td>
					</tr>
					<tbody id="examinfoSettingBody">
						<c:forEach items="${infos }" var="c">
							<tr>
								<td>${c.course.courseName }</td>
								<td style="text-align: center;" colspan="2"><c:choose>
										<c:when test="${c.examCourseType==1 }">
								    卷面：<fmt:formatNumber value="${c.facestudyScorePer }"
												pattern="#" />%
								    平时考核：<fmt:formatNumber value="${c.facestudyScorePer2 }"
												pattern="#" />%
								 <%--    网上学习：<fmt:formatNumber value="${c.facestudyScorePer3 }" pattern="#"/>% --%>
										</c:when>
										<c:otherwise>
								   卷面成绩：<fmt:formatNumber value="${c.studyScorePer }"
												pattern="#" />%
								   网上成绩：<fmt:formatNumber value="${c.netsidestudyScorePer }"
												pattern="#" />%
						</c:otherwise>
									</c:choose></td>
								<td style="text-align: center;">${ghfn:dictCode2Val('CodeCourseScoreStyle',c.courseScoreType)}</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>
			<div class="formBar">
				<ul>
					<li><div class="buttonActive">
							<div class="buttonContent">
								<button type="button" onclick="checkForm()">提交</button>
							</div>
						</div></li>
					<li><div class="button">
							<div class="buttonContent">
								<button type="button" class="close"
									onclick="$.pdialog.closeCurrent();">取消</button>
							</div>
						</div></li>
				</ul>
			</div>
		</form>

	</div>
	<script type="text/javascript">
	function checkForm(){
		if (checkFacestudyScorePerSum()) {
			$("form.pageForm").submit();
		}
	}
	function checkFacestudyScorePerSum(){
		var facestudyScorePer = $("#facestudy_examScorePer_writtenScorePer").val();
		var facestudyScorePer2 = $("#facestudy_examScorePer_usuallyScorePer").val();
		//var facestudyScorePer3 = $("#facestudy_examScorePer_onlineScorePer").val();
		var netstudyScorePer  = $("#networkstudy_examScorePer_writtenScorePer").val();
		var netstudyScorePer2 = $("#networkstudy_examScorePer_usuallyScorePer").val();
		var examCoureType = "${examCoureType}";
		if (examCoureType=="1") {
			if( facestudyScorePer != "" && facestudyScorePer2 != "" 
				&& (parseInt(facestudyScorePer) + parseInt(facestudyScorePer2)) != 100){
				alertMsg.warn("面授课程比例之和必须为100！");
				return false;
			}
			//$("button[type='submit']").attr("disabled", "disabled");
			
		}else if(examCoureType=="0") {
			if(netstudyScorePer != "" && netstudyScorePer2 != ""
				&& (parseInt(netstudyScorePer) + parseInt(netstudyScorePer2)) != 100){
				alertMsg.warn("网络课程比例之和必须为100！");
				return false;
			}
		}
		return true;
		//$("button[type='submit']").removeAttr("disabled");
	}
	
	function genUsuallyScoreRatio(type){
		var w_id            = "#"+type+"_examScorePer_writtenScorePer"
		var u_id            = "#"+type+"_examScorePer_usuallyScorePer"
		var writtenScorePer = $(w_id).val();
		if(parseInt(writtenScorePer)>100){
			$(w_id).attr("value","");
			$(u_id).attr("value","");
			alertMsg.warn("请输入一个0-100之间的数字!");
			return false;
		}
		var usuallyScorePer = 100-writtenScorePer;
		$(u_id).val(usuallyScorePer);
	}
</script>
</body>
