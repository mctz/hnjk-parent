<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>下载成绩单</title>


</head>
<body>
	<div class="page">
		<div class="pageHeader">
			<form id="examInfoListForDownLoadTranscriptsForm"
				onsubmit="return dialogSearch(this);"
				action="${baseUrl}/edu3/teaching/result/downloadtranscripts-examinfo-list.html"
				method="post">
				<input type="hidden" name="examSubId"
					value="${condition['examSubId'] }" />
				<div class="searchBar">
					<ul class="searchContent">
						<li><label>课程：</label> <gh:courseAutocomplete
								name="courseId" tabindex="1" id="examInfoForDownload_courseId"
								value="${condition['courseId']}" isFilterTeacher="Y"
								displayType="code" style="width:55%" /></li>
					</ul>
					<div class="buttonActive" style="float: right">
						<div class="buttonContent">
							<button type="submit">查 询</button>
						</div>
					</div>
				</div>
			</form>
		</div>
		<gh:resAuth parentCode="RES_TEACHING_RESULT_MANAGE"
			pageType="transcriptsSub"></gh:resAuth>
		<div class="pageContent">

			<table class="table" layouth="138">
				<thead>
					<tr>
						<th width="5%"><input type="checkbox" name="checkall"
							id="check_all_examInfoVo"
							onclick="checkboxAll('#check_all_examInfoVo','resourceid','#examInfoVoBody')" /></th>
						<th width="10%">课程编号</th>
						<th width="20%">课程名称</th>
						<th width="5%">计划</th>
						<th width="10%">成绩状态</th>
						<th width="20%">考试开始时间</th>
						<th width="20%">考试结束时间</th>
						<th width="10%">已预约人数</th>
					</tr>
				</thead>
				<tbody id="examInfoVoBody">
					<c:forEach items="${page.result}" var="examInfoVo" varStatus="vs">
						<tr>
							<td><input type="checkbox" name="resourceid"
								value="${examInfoVo.examInfoResourceId }" autocomplete="off" /></td>
							<td>${examInfoVo.examCourseCode}</td>
							<td>${examInfoVo.courseName }</td>

							<td><c:if test="${examInfoVo.isOutplanCourse eq 0}">计划内</c:if>
								<c:if test="${examInfoVo.isOutplanCourse eq 1}">计划外</c:if></td>
							<td><c:choose>
									<c:when test="${examInfoVo.examResultStatus eq '' }">
			            			未提交
			            		</c:when>
									<c:otherwise>
			            			${ghfn:dictCode2Val('CodeExamResultCheckStatus',examInfoVo.examResultStatus)}
			            		</c:otherwise>
								</c:choose></td>
							<td>${examInfoVo.examStartTime }</td>
							<td>${examInfoVo.examEndTime}</td>
							<td>${examInfoVo.orderNumber }</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<gh:page page="${page}" targetType="dialog"
				goPageUrl="${baseUrl }/edu3/teaching/result/downloadtranscripts-examinfo-list.html"
				pageType="sys" condition="${condition}" />
		</div>
	</div>
	<script type="text/javascript">
	function downLoadSub(flag){

		if(isCheckOnlyone('resourceid','#examInfoVoBody')){
			var examInfoId      = jQuery("#examInfoVoBody input[name='resourceid']:checked").val();
			var examSubId       = jQuery("#examInfoListForDownLoadTranscriptsForm input[name='examSubId']:hidden").val();
			
			var url = "${baseUrl }/edu3/teaching/transcripts/download.html?flag="+flag+"&examInfoId="+examInfoId+"&examSubId="+examSubId;
			downloadFileByIframe(url,'examInfoListForDownloadExportIframe');
			//window.location.href=url+"?examInfoId="+examInfoId+"&examSubId="+examSubId;
		}
	}
</script>
</body>
</html>