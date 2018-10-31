<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>随堂练习预览</title>
<style>
#nostyle td {
	height: 4px
}

.list div {
	line-height: 170%;
}

.list td {
	word-wrap: break-word
}

.list p {
	width: 100%
}
</style>
</head>
<body>
	<script type="text/javascript">
//调分
function markscoreCourseExam(){
	var postUrl = "${baseUrl}/edu3/metares/exercise/activeexercise/markscore.html";
	_previewCourseExam_pageBarHandle("您确定要对这些随堂练习进行调分吗？",postUrl,"#preCourseExamBody",_preCourseExam_dialogDone);
}
//审核发布
function publishCourseExam(isPublished){
	var postUrl = "${baseUrl}/edu3/metares/exercise/activeexercise/publish.html?isPublished="+isPublished;
	_previewCourseExam_pageBarHandle("您确定要发布这些随堂练习吗？",postUrl,"#preCourseExamBody",_preCourseExam_dialogDone);
}
function _previewCourseExam_pageBarHandle(msg,url,bodyName,func){
	if(!isChecked('resourceid',bodyName)){
		alertMsg.warn('请选择一条要操作记录！');
	return false;
	}
	alertMsg.confirm(msg, {
			okCall: function(){//执行			
				var res = "";
				var k = 0;
				var num  = $(bodyName+" input[name='resourceid']:checked").size();
				$(bodyName+" input[@name='resourceid']:checked").each(function(){
	                    res+=$(this).val();
	                    if(k != num -1 ) res += ",";
	                    k++;
	             });
	            
				$.post(url,{resourceid:res}, _preCourseExam_dialogDone, "json");
			}
	});	
}
function _preCourseExam_dialogDone(json){
	DWZ.ajaxDone(json);
	if (json.statusCode == 200){
		dialogPageBreak();
	}
}
//移动随堂练习
function moveActiveCourseExam1(){
	if(!isChecked('resourceid',"#preCourseExamBody")){
		alertMsg.warn('请选择一条要操作记录！');
		return false;
	}
	var url = "${baseUrl}/edu3/metares/exercise/activeexercise/move.html";
	var sel = "<label>移动这些随堂练习到：</label><br/><select id='dialog_toSyllabusId'>"+$('#courseexam_dialogSearch_syllabusId').html()+"</select>";
	alertMsg.confirm(sel, {
			okCall: function(){//执行			
				var res = "";
				var k = 0;
				var num  = $("#preCourseExamBody input[name='resourceid']:checked").size();
				$("#preCourseExamBody input[@name='resourceid']:checked").each(function(){
	                    res+=$(this).val();
	                    if(k != num -1 ) res += ",";
	                    k++;
	             });
				var toid = $("#dialog_toSyllabusId").val();
				if(toid==""){
					alertMsg.warn('请选择目标知识节点！');
					return false;
				}
				$.post(url,{resourceid:res,toSyllabusId:toid}, _preCourseExam_dialogDone, "json");
			},
			okName:'移动随堂练习'
	});	
}
</script>
	<div class="page">
		<div class="pageHeader">
			<form onsubmit="return dialogSearch(this);"
				action="${baseUrl }/edu3/metares/exercise/activeexercise/preview.html"
				method="post">
				<div class="searchBar">
					<ul class="searchContent">
						<li><label>知识节点:</label> <input type="hidden"
							value="${condition['courseId'] }" name="courseId" /> <select
							id="courseexam_dialogSearch_syllabusId" name="syllabusId"
							style="width: 125px;">
								<option value="">请选择</option>
								<c:forEach items="${syllabusList}" var="syb" varStatus="vs">
									<option value="${syb.resourceid }"
										<c:if test="${syb.resourceid eq condition['syllabusId']}"> selected </c:if>>
										<c:forEach var="seconds" begin="1" end="${syb.syllabusLevel}"
											step="1">&nbsp;&nbsp;</c:forEach> ${syb.syllabusName }
									</option>
								</c:forEach>
						</select></li>
						<li><label>是否发布:</label> <gh:select dictionaryCode="yesOrNo"
								name="isPublished" id="preview_courseexam_isPublished"
								value="${condition['isPublished'] }" /></li>
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
		<gh:resAuth parentCode="RES_METARES_COURSEWARE_MANAGE"
			pageType="previewlist"></gh:resAuth>
		<div class="pageContent" layouth="138">
			<table class="list" style="width: 100%; table-layout: fixed;">
				<thead>
					<tr>
						<th width="10%"><input type="checkbox" name="checkall"
							id="check_all_ativepreview"
							onclick="checkboxAll('#check_all_ativepreview','resourceid','#preCourseExamBody')" /></th>
						<th>试题内容</th>
						<th width="20%">审核状态</th>
						<th width="8%">编辑</th>
					</tr>
				</thead>
				<tbody id="preCourseExamBody">
					<c:forEach items="${activeCourseExamList.result }" var="exam"
						varStatus="vs">
						<c:if
							test="${syllabusExam[exam.syllabus.resourceid] eq exam.resourceid }">
							<tr>
								<td></td>
								<td colspan="3">
									<div style="font-weight: bold;">${exam.syllabus.syllabusName }</div>
								</td>
							</tr>
						</c:if>
						<tr>
							<td><input type="checkbox" name="resourceid"
								value="${exam.resourceid }" autocomplete="off" /> <br />
							<span style="font-weight: bold;">${exam.showOrder }.
									(${ghfn:dictCode2Val('CodeExamType',exam.courseExam.examType) })</span>
							</td>
							<td>
								<div>${exam.courseExam.question }</div>
								<div tyle="font-weight: bold;">答案：${exam.courseExam.answer }</div>
							</td>
							<td><c:choose>
									<c:when test="${exam.isPublished eq 'Y'}">
			   						已发布<br />
										<c:if test="${not empty exam.auditMan }">
			   						审核人:${exam.auditMan } <br />
			   						审核日期:<fmt:formatDate value="${exam.auditDate }"
												pattern="yyyy-MM-dd HH:mm:ss" />
										</c:if>
									</c:when>
									<c:otherwise>未发布</c:otherwise>
								</c:choose></td>
							<td><a target="navTab" rel="courseExamInput" title="编辑试题"
								href="${baseUrl}/edu3/metares/courseexam/input.html?resourceid=${(empty exam.courseExam.parent)?exam.courseExam.resourceid:exam.courseExam.parent.resourceid}">编辑</a>
							</td>
						</tr>
						<tr>
							<td colspan="4">&nbsp;</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
		<gh:page page="${activeCourseExamList}"
			goPageUrl="${baseUrl}/edu3/metares/exercise/activeexercise/preview.html"
			targetType="dialog" condition="${condition }" pageNumShown="5"
			pageType="sys" />
	</div>
</body>
</html>