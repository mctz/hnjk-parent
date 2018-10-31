<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>随堂练习管理</title>
<gh:loadCom components="treeView" />
<script type="text/javascript">
	$(document).ready(function(){
		$("#_courseexamtree").treeview({
			persist: "location",			
			unique: true
		});		
		if($('._courseExamLeftTree')){//如果有左侧树，则赋值为自适应高度		
			$('._courseExamLeftTree').height($("#container .tabsPageContent").height()-5);
		}
	
	});
	
	//新增
	function addActiveExercise(){
	 	var url = "${baseUrl}/edu3/metares/exercise/activeexercise/input.html";
	 	var syllabusId = $('#courseExamForm #syllabusId').val();
	 	if(syllabusId == ""){
	 		alertMsg.warn("请选择左边的某个知识节点！");
	 		return false;
	 	}
	 	navTab.openTab('RES_METARES_EXERCISE_ACTIVECOURSEEXAM', url+'?syllabusId='+syllabusId, '新增随堂练习');
 	}
 	//编辑
 	function editActiveExercise(){
		var url = "${baseUrl}/edu3/metares/exercise/activeexercise/input.html";
		if(isCheckOnlyone('resourceid','#activeExamBody')){
			navTab.openTab('RES_METARES_EXERCISE_ACTIVECOURSEEXAM', url+'?resourceid='+$("#activeExamBody input[@name='resourceid']:checked").val(), '编辑随堂练习');
		}			
	}
	//删除
	function removeActiveExercise(){
		pageBarHandle("您确定要删除这些记录吗？","${baseUrl}/edu3/metares/exercise/activeexercise/remove.html","#activeExamBody");	
	}
	
	//左边树的链接
	function goActiveCourseExamSelected(courseId,syllabusId){
		var url = "${baseUrl}/edu3/metares/exercise/active/addactivecourseexam.html";
		navTab.openTab('navTab', url+'?courseId='+courseId+'&syllabusId='+syllabusId,'随堂练习管理');
	}
	
	
</script>
</head>
<body>
	<div style="float: left; width: 19%">
		<div class="_courseExamLeftTree"
			style="display: block; overflow: auto; padding-bottom: 6px; border: solid 1px #CCC; line-height: 21px;">
			<gh:treeview nodes="${examtree}" id="_courseexamtree" css="folder"
				expandLevel="100" />
		</div>
	</div>

	<div class="page" style="float: left; width: 81%">
		<div class="pageHeader">
			<form id="courseExamForm" onsubmit="return navTabSearch(this);"
				action="${baseUrl }/edu3/metares/exercise/active/addactivecourseexam.html"
				method="post">
				<div class="searchBar">
					<ul class="searchContent">
						<li><label>排序号：</label><input name="showOrder"
							value="${condition['showOrder']}" /></li>
						<li><label>问题关键字：</label><input type="text" name="question"
							value="${condition['question'] }" /> <input type="hidden"
							id="syllabusId" name="syllabusId"
							value="${condition['syllabusId'] }"> <input type="hidden"
							id="courseId" name="courseId" value="${condition['courseId'] }">
						</li>
						<li><label>题型：</label>
						<gh:select name="questionType"
								value="${condition['questionType']}"
								dictionaryCode="CodeLearningQuestionType" /></li>
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
			<gh:resAuth parentCode="RES_METARES_EXERCISE_ACTIVE"
				pageType="sublist"></gh:resAuth>
			<table class="table" layouth="138">
				<thead>
					<tr>
						<th width="10%"><input type="checkbox" name="checkall"
							id="check_all_ative"
							onclick="checkboxAll('#check_all_ative','resourceid','#activeExamBody')" /></th>
						<th width="20%">排序号</th>
						<th width="25%">所属知识节点</th>
						<th width="25%">题型</th>
						<th width="20%">分值</th>
					</tr>
				</thead>
				<tbody id="activeExamBody">
					<c:forEach items="${activeCourseExamList.result }" var="exercise"
						varStatus="vs">
						<tr>
							<td><input type="checkbox" name="resourceid"
								value="${exercise.resourceid }" autocomplete="off" /></td>
							<td>${exercise.showOrder }</td>
							<td>${exercise.syllabus.syllabusName }</td>
							<td>${ghfn:dictCode2Val('CodeLearningQuestionType',exercise.questionType) }</td>
							<td>${exercise.score }</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<gh:page page="${activeCourseExamList}"
				goPageUrl="${baseUrl}/edu3/metares/exercise/active/addactivecourseexam.html"
				condition="${condition }" pageType="sys" />

		</div>
	</div>
</body>
</html>