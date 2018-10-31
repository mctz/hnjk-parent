<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>题库试题选择</title>
<script type="text/javascript">
	function saveActiveExercise1(){			
		pageBarHandle("您确定把这些试题加到知识节点 <b>${syllabus.syllabusName}</b> 下？","${baseUrl}/edu3/metares/exercise/activeexercise/save.html?syllabusId=${syllabus.resourceid}","#activecourseexam_courseExamBody");
	}
</script>
</head>
<body>
	<div class="page">
		<div class="pageHeader">
			<form onsubmit="return navTabSearch(this);"
				action="${baseUrl }/edu3/metares/exercise/activeexercise/input.html"
				method="post">
				<div class="searchBar">
					<input type="hidden" name="syllabusId"
						value="${syllabus.resourceid }" />
					<ul class="searchContent">
						<li><label>题型：</label> <%-- 
					<select name="examType" >
						<option value="">请选择</option>
						<option value="1" ${(condition['examType'] eq '1')?'selected':''} >单选题</option>
						<option value="2" ${(condition['examType'] eq '2')?'selected':''} >多选题</option>
						<option value="3" ${(condition['examType'] eq '3')?'selected':''} >判断题</option>
					</select>
					 --%> <gh:select dictionaryCode="CodeExamType" name="examType"
								id="chooseExamTypes" value="${condition['examType'] }" /></li>
						<li><label>难易度：</label>
						<gh:select name="difficult" value="${condition['difficult']}"
								dictionaryCode="CodeExamDifficult" /></li>
						<li><label>关键字：</label><input type="text" name="keywords"
							value="${condition['keywords'] }" /></li>
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
			<div class="panelBar">
				<ul class="toolBar">
					<li class=""><a onclick="saveActiveExercise1()" href="#"
						class="icon" title="添加试题"><span>添加试题</span></a></li>
				</ul>
			</div>
			<%--
		<div colspan="6" id="activeexam_courseexam_td">
	   		设置默认分值： 
	   		<label>单选题： </label><input name='score1' value='5' size="5"/>
	   		<label>多选题： </label><input name='score2' value='5' size="5"/>
	   		<label>判断题： </label><input name='score3' value='5' size="5"/>		 
	   	</div>
	   	 --%>
			<table class="table" layouth="138">
				<thead>
					<tr>
						<th width="5%"><input type="checkbox" name="checkall"
							id="check_all_activecourseexam_courseexam"
							onclick="checkboxAll('#check_all_activecourseexam_courseexam','resourceid','#activecourseexam_courseExamBody')" /></th>
						<th width="25%">课程</th>
						<th width="15%">题型</th>
						<th width="10%">难度</th>
						<th width="10%">考试要求</th>
						<th width="25%">关键字</th>
						<th width="10%">&nbsp;</th>
					</tr>
				</thead>
				<tbody id="activecourseexam_courseExamBody">
					<c:forEach items="${courseExamList.result }" var="exam"
						varStatus="vs">
						<tr>
							<td><input type="checkbox" name="resourceid"
								value="${exam.resourceid }" autocomplete="off" /></td>
							<td>${exam.course.courseName }</td>
							<td>${ghfn:dictCode2Val('CodeExamType',exam.examType)}</td>
							<td>${ghfn:dictCode2Val('CodeExamDifficult',exam.difficult)}</td>
							<td>${ghfn:dictCode2Val('CodeTeachingRequest',exam.requirement)}</td>
							<td>${exam.keywords }</td>
							<td><a href="javascript:;"
								onclick="javascript:$.pdialog.open('${baseUrl }/edu3/metares/courseexam/view.html?courseExamId=${exam.resourceid }','selector','查看试题',{width:600,height:400});">查看</a></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<gh:page page="${courseExamList}"
				goPageUrl="${baseUrl}/edu3/metares/exercise/activeexercise/input.html"
				condition="${condition }" pageType="sys" />
		</div>
	</div>
</body>
</html>