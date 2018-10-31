<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>下载成绩单</title>


</head>
<body>
	<div class="page">
		<div class="pageHeader">
			<form id="examInfoListForAbnormityConfigForm"
				onsubmit="return dialogSearch(this);"
				action="${baseUrl}/edu3/teaching/result/abnormity-config-list.html"
				method="post">
				<input type="hidden" name="examSubId"
					value="${condition['examSubId'] }" />
				<div class="searchBar">
					<ul class="searchContent">
						<li><label>课程：</label> <gh:courseAutocomplete
								name="courseId" tabindex="1"
								id="examInfoForAbnormityConfig_courseId"
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
			pageType="abnormityConfig"></gh:resAuth>
		<div class="pageContent">

			<table class="table" layouth="138">
				<thead>
					<tr>
						<th width="5%"><input type="checkbox" name="checkall"
							id="check_all_examInfo_forabnormity_config"
							onclick="checkboxAll('#check_all_examInfo_forabnormity_config','resourceid','#check_all_examInfo_forabnormity_configBody')" /></th>
						<th width="10%">课程编号</th>
						<th width="60%">课程名称</th>
						<th width="15%">异常成绩状态</th>
					</tr>
				</thead>
				<tbody id="check_all_examInfo_forabnormity_configBody">
					<c:forEach items="${page.result}" var="examInfoVo" varStatus="vs">
						<tr>
							<td><input type="checkbox" name="resourceid"
								value="${examInfoVo.examInfoResourceId }" autocomplete="off" /></td>
							<td>${examInfoVo.examCourseCode}</td>
							<td>${examInfoVo.courseName }</td>

							<c:choose>
								<c:when test="${examInfoVo.isAbnormityEnd eq 'Y'}">
									<td style="color: green">已录完</td>
								</c:when>
								<c:otherwise>
									<td style="color: red">未录完</td>
								</c:otherwise>
							</c:choose>

						</tr>
					</c:forEach>
				</tbody>
			</table>
			<gh:page page="${page}" targetType="dialog"
				goPageUrl="${baseUrl }/edu3/teaching/result/abnormity-config-list.html"
				pageType="sys" condition="${condition}" />
		</div>
	</div>
	<script type="text/javascript">
	//设置异常成绩录入状态
	function abnormityInputStatus(status){
		if(isCheckOnlyone('resourceid','#check_all_examInfo_forabnormity_configBody')){
			var examInfoId      = $("#check_all_examInfo_forabnormity_configBody input[name='resourceid']:checked").val();
			var url             = "${baseUrl}/edu3/teaching/result/abnormity-status.html";
			jQuery.post(url,{examInfoId:examInfoId,status:status},function(returnData){	
				var msg 	   = returnData['message'];
			  	var statusCode =  returnData['statusCode'];
			  	if(statusCode==200){
			  		alertMsg.info(msg);
			  		$.pdialog.reload($("#examInfoListForAbnormityConfigForm").attr("action"), $("#examInfoListForAbnormityConfigForm").serializeArray());
				}else{
					alertMsg.warn(msg);
				}
			},"json");
		}	
	}
</script>
</body>
</html>