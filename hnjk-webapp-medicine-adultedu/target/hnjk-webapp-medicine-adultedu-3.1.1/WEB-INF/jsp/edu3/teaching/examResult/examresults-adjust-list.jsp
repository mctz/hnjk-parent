<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>成绩调整列表</title>
<style type="text/css">
#examresult_examinfo_Body div {
	height: auto;
	line-height: 120%;
	white-space: pre-line;
}
</style>
<script type="text/javascript">
//调整分数
function adjustExamResults(){
	if(isCheckOnlyone('resourceid','#examresult_examinfo_Body')){
		var examInfoId = $("#examresult_examinfo_Body input[name='resourceid']:checked").val();
		var oldAdjustLine = $.trim($("#adjustLine"+examInfoId).attr("oldAdjustLine"));
		var adjustLine = $.trim($("#adjustLine"+examInfoId).val());
		if(oldAdjustLine!=""){
			alertMsg.warn("该课程成绩已经调整过了，调整分数线为"+oldAdjustLine);
			return false;
		} else 	if(adjustLine=="" || !adjustLine.isInteger()){
			alertMsg.warn("分数线必须为数字，请输入一个分数线");
			return false;
		}else if(parseInt(adjustLine) < 0 || parseInt(adjustLine)>100){
			alertMsg.warn("请输入一个0-100的调整分数线");
			return false;
		}else {
			$.post("${baseUrl}/edu3/framework/teaching/examresults/adjust/ajax.html",{examInfoId:examInfoId,adjustLine:adjustLine}, function (data){
				if (data.passNumRatio){
					alertMsg.confirm("调整后及格率为 "+data.passNumRatio+"<br/> 你确定要进行成绩调整吗？",{
						okCall:function (){
							$.post("${baseUrl}/edu3/teaching/examinfo/adjust/save.html",{examInfoId:examInfoId,adjustLine:adjustLine}, function (json){
								DWZ.ajaxDone(json);
								if(json.statusCode == 200){
									dialogPageBreak();
								}
							}, "json");
						}
					});
				}
			}, "json");
		}
	}	
}
</script>
</head>
<body>
	<div class="page">

		<div class="pageHeader">

			<form onsubmit="return dialogSearch(this);"
				action="${baseUrl }/edu3/teaching/examinfo/adjust/list.html"
				method="post">
				<div class="searchBar">
					<ul class="searchContent">
						<li><label>课程：</label> <input type="hidden"
							value="${condition['examSubId'] }" name="examSubId" /> <gh:courseAutocomplete
								name="courseId" tabindex="3" id="examresult_examinfo_courseId"
								value="${condition['courseId'] }" isFilterTeacher="Y"
								displayType="code" /></li>
					</ul>
					<div class="subBar">
						<ul>
							<li><div class="buttonActive">
									<div class="buttonContent">
										<button type="submit">查 询</button>
									</div>
								</div></li>
						</ul>
					</div>
				</div>
			</form>

		</div>
		<div class="pageContent">
			<gh:resAuth parentCode="RES_TEACHING_RESULT_MANAGE"
				pageType="adjustlist"></gh:resAuth>
			<table class="table" layouth="138">
				<thead>
					<tr>
						<th width="5%"><input type="checkbox" name="checkall"
							id="check_all_examresult_examinfo"
							onclick="checkboxAll('#check_all_examresult_examinfo','resourceid','#examresult_examinfo_Body')" /></th>
						<th width="15%">考试批次</th>
						<th width="10%">考试课程</th>
						<th width="20%">考试时间</th>
						<th>当前卷面成绩情况</th>
						<th width="10%">调整分数线</th>
					</tr>
				</thead>
				<tbody id="examresult_examinfo_Body">
					<c:forEach items="${examInfoPage.result}" var="examInfo"
						varStatus="vs">
						<tr>
							<td><input type="checkbox" name="resourceid"
								value="${examInfo.resourceid }" autocomplete="off" /></td>
							<td>${examInfo.examSub.batchName }</td>
							<td>${examInfo.course.courseName }<c:if
									test="${examInfo.isMachineExam eq 'Y' }"> (机考) </c:if></td>
							<td><fmt:formatDate value="${examInfo.examStartTime }"
									pattern="yyyy-MM-dd HH:mm:ss" /> - <fmt:formatDate
									value="${examInfo.examEndTime }" pattern="yyyy-MM-dd HH:mm:ss" />
							</td>
							<td>预约${examInfoList[examInfo.resourceid]['ORDERNUM'] }人,
								缺考${examInfoList[examInfo.resourceid]['ABSENTNUM'] }人,实考${examInfoList[examInfo.resourceid]['EXAMNUM'] }人,
								及格${examInfoList[examInfo.resourceid]['DISTRIBUTIONNUM60~100'] }人,不及格${examInfoList[examInfo.resourceid]['DISTRIBUTIONNUM0~59'] }人,
								平均分${examInfoList[examInfo.resourceid]['AVGSCORE'] }分,及格率${examInfoList[examInfo.resourceid]['PASSRATIO'] }
							</td>
							<td><input type="text"
								id="adjustLine${examInfo.resourceid }"
								name="adjustLine${examInfo.resourceid }"
								value="${examInfo.adjustLine }" size="5"
								oldAdjustLine="${examInfo.adjustLine }" /></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<gh:page page="${examInfoPage}"
				goPageUrl="${baseUrl }/edu3/teaching/examinfo/adjust/list.html"
				targetType="dialog" pageType="sys" condition="${condition}" />
		</div>
	</div>
</body>
</html>