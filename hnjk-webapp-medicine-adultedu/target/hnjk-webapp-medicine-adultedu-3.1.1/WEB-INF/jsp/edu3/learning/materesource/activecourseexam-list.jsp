<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>随堂练习管理</title>
<script type="text/javascript">	
	//新增
	function addActiveExercise(){
	 	var url = "${baseUrl}/edu3/metares/exercise/activeexercise/input.html";
	 	var syllabusId = "${condition['syllabusId']}";
	 	if(syllabusId == ""){
	 		alertMsg.warn("请选择左边的某个知识节点！");
	 		return false;
	 	}
	 	navTab.openTab('RES_METARES_EXERCISE_ACTIVECOURSEEXAM', url+'?syllabusId='+syllabusId, '新增随堂练习');
 	} 	
	//删除
	function removeActiveExercise(){
		var syllabusId = "${condition['syllabusId']}";
		var courseId = "${condition['courseId']}";
		var postUrl = "${baseUrl}/edu3/metares/exercise/activeexercise/remove.html?syllabusId="+syllabusId+"&courseId="+courseId;
		mateRemoveHandle("您确定要删除这些记录吗？",postUrl,"#activeCourseExamBody1",myMateAjaxDone);
	}	
	//导出习题
	function exportActiveCourseExam(){
		alertMsg.confirm(" 您确定要导出该课程的随堂练习吗？", {
			okCall: function(){		
				var courseId = "${condition['courseId']}";
				var url = "${baseUrl}/edu3/metares/exercise/activeexercise/export.html?courseId="+courseId;
				window.location.href=url;
			}
		});
	}
	//下载模板
	function downloadActiveCourseExam(){
		alertMsg.confirm(" 您确定要下载随堂练习的导入模板吗？", {
			okCall: function(){		
				var courseId = "${condition['courseId']}";
				var url = "${baseUrl}/edu3/metares/exercise/activeexercise/syllabus/export.html?courseId="+courseId;
				downloadFileByIframe(url,"downloadActiveCourseExamTemplateIframe");
			}
		});
	}
	//导入
	function importActiveCourseExam(){	
		$.pdialog.open("${baseUrl}/edu3/framework/activeexercise/upload.html?courseId=${condition['courseId']}", "activecourseexamimport", "导入随堂练习", {width:600,height:400,mask:true});
	}
	function saveActiveExercise(){
		$.ajax({
			type:'POST',
			url:$("#activecourseexam_batcheditform").attr("action"),
			data:$("#activecourseexam_batcheditform").serializeArray(),
			dataType:"json",
			cache: false,
			success: myMateAjaxDone,
			
			error: DWZ.ajaxError
		});
	}
	//预览
	function previewActiveExam(){
		var courseId = "${condition['courseId']}";
		var syllabusId = "${condition['syllabusId']}";
		var url = "${baseUrl}/edu3/metares/exercise/activeexercise/preview.html?courseId="+courseId+"&syllabusId="+syllabusId;
		$.pdialog.open(url, "courseexam_previw", "预览随堂练习", {width:800,height:600});
	}
	
	//修改
	function modifyActiveCourseExam(res){
		var url = "${baseUrl}/edu3/metares/courseexam/input.html?resourceid="+res;
		navTab.openTab('courseExamInput', url, '编辑试题');			
	}
	//移动试题
	function moveActiveCourseExam(){
		var url = "${baseUrl}/edu3/metares/exercise/activeexercise/move.html";
		var courseId = "${condition['courseId']}";
	 	var syllabusId = "${condition['syllabusId']}";
	 	if(syllabusId == ""){
	 		alertMsg.warn("请选择左边的某个知识节点！");
	 		return false;
	 	}
	 	var sel = "<label>移动到：</label><select id='toSyllabusId'>"+$('#moveToSyllabusId').html()+"</select>";
		alertMsg.confirm(sel,{okCall:function(){
			var toid = $("#toSyllabusId").val();
			if(toid==""){
				alertMsg.warn('请选择目标知识节点！');
				return false;
			}
			$.post(url,{fromSyllabusId:syllabusId,toSyllabusId:toid,courseId:courseId}, myMateAjaxDone, "json");
		},okName:'移动随堂练习'});
	}
	function batchsetscore(){
		var setscore = $("#setscore").val();
		if (setscore == "") {
			alertMsg.warn("请输入批量分值！");
			$("#setscore").focus();
			return;
		}
		//alert($("input[name='score']").size());
		$("input[name='score']").each(function(){
		    $(this).val(setscore);
		});
	}
</script>
</head>
<body>
	<div style="display: none;">
		<select id="moveToSyllabusId">
			<option value="">请选择</option>
			<c:forEach items="${syllabusList}" var="syb" varStatus="vs">
				<option value="${syb.resourceid }"
					<c:if test="${syb.resourceid eq condition['syllabusId']}"> selected </c:if>>
					<c:forEach var="seconds" begin="1" end="${syb.syllabusLevel}"
						step="1">&nbsp;&nbsp;</c:forEach> ${syb.syllabusName }
				</option>
			</c:forEach>
		</select>
	</div>
	<div>
		<input type="text" id="setscore" value="" size="5" /> <input
			type="button" value="批量设置分值" size="5" onclick="batchsetscore();" />
	</div>
	<div class="page">
		<div class="pageContent">
			<gh:resAuth parentCode="RES_METARES_COURSEWARE_MANAGE"
				pageType="asublist"></gh:resAuth>
			<form id="activecourseexam_batcheditform" method="post"
				action="${baseUrl}/edu3/metares/exercise/activeexercise/save.html"
				class="pageForm" onsubmit="return false;">
				<input type="hidden" name="syllabusId"
					value="${condition['syllabusId']}" /> <input type="hidden"
					name="from" value="batchedit" />
				<table class="table" layouth="138">
					<thead>
						<tr>
							<th width="5%"><input type="checkbox" name="checkall"
								id="check_all_ative"
								onclick="checkboxAll('#check_all_ative','resourceid','#activeCourseExamBody1')" /></th>
							<th width="8%">排序号</th>
							<th width="11%">所属知识节点</th>
							<th width="9%">题型</th>
							<th width="7%">难度</th>
							<th width="7%">关键字</th>
							<th width="7%">分值</th>
							<th width="6%">是否发布</th>
							<th width="30%">关联知识点</th>
							<th width="10%">&nbsp;</th>
						</tr>
					</thead>
					<tbody id="activeCourseExamBody1">
						<c:forEach items="${resultList.result }" var="exercise"
							varStatus="vs">
							<tr>
								<td><input type="checkbox" name="resourceid"
									value="${exercise.resourceid }" autocomplete="off" /><input
									type="hidden" name="examId" value="${exercise.resourceid }" /></td>
								<td><input type="text" name="showOrder"
									value="${exercise.showOrder }" size="5" /></td>
								<td>${exercise.syllabus.syllabusName }</td>
								<td>${ghfn:dictCode2Val('CodeExamType',exercise.courseExam.examType) }</td>
								<td>${ghfn:dictCode2Val('CodeExamDifficult',exercise.courseExam.difficult) }</td>
								<td>${exercise.courseExam.keywords }</td>
								<td><input type="text" name="score"
									value="${exercise.score }" size="5" /></td>
								<td>${(exercise.isPublished eq 'Y')?'已发布':'未发布' }</td>
								<td><input type="text"
									id="referSyllabusTreeNames_${exercise.resourceid }"
									name="referSyllabusTreeNames"
									value="${exercise.referSyllabusTreeNames }" readonly="readonly" />
									<input type="hidden"
									id="referSyllabusTreeIds_${exercise.resourceid }"
									name="referSyllabusTreeIds"
									value="${exercise.referSyllabusTreeIds }" /> <a
									href="javascript:;"
									onclick="javascript:$.pdialog.open('${baseUrl }/edu3/metares/exercise/activeexercise/syllabustree.html?courseId=${exercise.syllabus.course.resourceid }&idsN=referSyllabusTreeIds_${exercise.resourceid }&namesN=referSyllabusTreeNames_${exercise.resourceid }&checkedIds='+$('#referSyllabusTreeIds_${exercise.resourceid }').val(),'selector','选择知识节点',{mask:true,width:500,height:450});">选择知识点</a>
								</td>
								<td><c:choose>
										<c:when test="${not empty exercise.courseExam.parent }">
											<a href="javascript:;"
												onclick="javascript:$.pdialog.open('${baseUrl }/edu3/metares/courseexam/view.html?courseExamId=${exercise.courseExam.parent.resourceid }','activecourseexam','查看习题',{width:600,height:400});">查看</a>
			   				&nbsp;&nbsp;
			   				<a href="javascript:;"
												onclick="modifyActiveCourseExam('${exercise.courseExam.parent.resourceid }')">编辑试题</a>
										</c:when>
										<c:otherwise>
											<a href="javascript:;"
												onclick="javascript:$.pdialog.open('${baseUrl }/edu3/metares/courseexam/view.html?courseExamId=${exercise.courseExam.resourceid }','activecourseexam','查看习题',{width:600,height:400});">查看</a>
			   				&nbsp;&nbsp;
			   				<a href="javascript:;"
												onclick="modifyActiveCourseExam('${exercise.courseExam.resourceid }')">编辑试题</a>
										</c:otherwise>
									</c:choose></td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</form>
			<gh:page page="${resultList}"
				goPageUrl="${baseUrl}/edu3/framework/metares/list.html"
				condition="${condition }" targetType="localArea"
				localArea="mateTabContent${condition['currentIndex'] }"
				pageType="sys" />
		</div>
	</div>
</body>
</html>