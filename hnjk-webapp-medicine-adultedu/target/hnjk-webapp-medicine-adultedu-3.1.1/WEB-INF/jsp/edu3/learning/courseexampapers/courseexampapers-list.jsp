<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>试卷管理</title>
<script type="text/javascript">
	//新增
	function addCourseExamPapers(){
		var url = "${baseUrl}/edu3/metares/courseexampapers/input.html";
		navTab.openTab('CourseExamPapers', url, '新增试卷');
	}
	//修改
	function modifyCourseExamPapers(){
		var url = "${baseUrl}/edu3/metares/courseexampapers/input.html";
		if(isCheckOnlyone('resourceid','#courseExamPapersBody')){
			navTab.openTab('CourseExamPapers', url+'?resourceid='+$("#courseExamPapersBody input[@name='resourceid']:checked").val(), '编辑试卷');
		}			
	}		
	//删除
	function removeCourseExamPapers(){	
		pageBarHandle("您确定要删除这些试卷吗？","${baseUrl}/edu3/metares/courseexampapers/remove.html","#courseExamPapersBody");
	}	
	//添加试题到试卷
	function listPapersCourseExam(){
		var url = "${baseUrl}/edu3/metares/courseexampapers/courseexam/list.html";
		if(isCheckOnlyone('resourceid','#courseExamPapersBody')){
			var resobj = $("#courseExamPapersBody input[@name='resourceid']:checked");
			$.pdialog.open(url+'?paperId='+resobj.val(), "PapersCourseExam", resobj.attr('rel'), {width: 800, height: 600 });
		}
	}
	function courseExamPapersTypeOnchange(paperType){
		if(paperType=='entrance_exam'){			
			$("#courseExamPapersSearch_Course1").hide();
			$("#courseExamPapersSearch_Course2").show();
		} else {
			$("#courseExamPapersSearch_Course1").show();
			$("#courseExamPapersSearch_Course2").hide();
		}
	}
	//随机抽取试题
	function randomPapersCourseExam(){	
		if(isCheckOnlyone('resourceid','#courseExamPapersBody')){
			var url = "${baseUrl}/edu3/framework/courseexampapers/random.html?paperId="+$("#courseExamPapersBody input[@name='resourceid']:checked").val();
			$.pdialog.open(url, "PapersCourseExamsSelector", "成卷条件", {width: 600, height: 350});
		}		
	}
</script>
</head>
<body>
	<div class="page">
		<div class="pageHeader">
			<form onsubmit="return navTabSearch(this);"
				action="${baseUrl }/edu3/metares/courseexampapers/list.html"
				method="post">
				<div class="searchBar">
					<ul class="searchContent">
						<li><label>试卷类型：</label>
						<gh:select name="paperType" value="${condition['paperType']}"
								dictionaryCode="CodePaperType"
								onchange="courseExamPapersTypeOnchange(this.value)" /></li>
						<li>
							<div id="courseExamPapersSearch_Course1"
								style="display: ${(condition['paperType'] eq 'entrance_exam')?'none':'block' };">
								<label>课程:</label>
								<gh:courseAutocomplete id="courseExamCourseId" name="courseId"
									tabindex="1" value="${condition['courseId']}"
									displayType="code" />
							</div>
							<div id="courseExamPapersSearch_Course2"
								style="display: ${(condition['paperType'] eq 'entrance_exam')?'block':'none' };">
								<label>入学考试课程:</label>
								<gh:select name="courseName" value="${condition['courseName']}"
									dictionaryCode="CodeEntranceExam" />
							</div>
						</li>
					</ul>
					<ul class="searchContent">
						<li><label>试卷名称：</label><input type="text" name="paperName"
							value="${condition['paperName'] }" /></li>
						<li><label>层次：</label>
						<gh:selectModel name="classicid" bindValue="resourceid"
								displayValue="classicName"
								modelClass="com.hnjk.edu.basedata.model.Classic"
								value="${condition['classicid']}" /></li>
						<li><label>是否开放：</label>
						<gh:select name="isOpened" value="${condition['isOpened']}"
								dictionaryCode="yesOrNo" /></li>
					</ul>
					<div class="subBar">
						<span style="color: green;"> &nbsp;&nbsp;&nbsp;&nbsp;*
							查询课程时请先选择相应试卷类型</span>
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
			<gh:resAuth parentCode="RES_METARES_COURSEEXAMPAPERS" pageType="list"></gh:resAuth>
			<table class="table" layouth="161">
				<thead>
					<tr>
						<th width="5%"><input type="checkbox" name="checkall"
							id="check_all_courseexampapers"
							onclick="checkboxAll('#check_all_courseexampapers','resourceid','#courseExamPapersBody')" /></th>
						<th width="25%">课程</th>
						<th width="10%">层次</th>
						<th width="20%">试卷名称</th>
						<th width="15%">试卷类型</th>
						<th width="15%">考试时长(单位:分钟)</th>
						<th width="10%">是否开放</th>
					</tr>
				</thead>
				<tbody id="courseExamPapersBody">
					<c:forEach items="${courseExamPapersPage.result }" var="paper"
						varStatus="vs">
						<tr>
							<td><input type="checkbox" name="resourceid"
								value="${paper.resourceid }" autocomplete="off"
								rel="${paper.paperName }" /></td>
							<td><c:choose>
									<%-- 入学考试 --%>
									<c:when test="${paper.paperType eq 'entrance_exam' }">${ghfn:dictCode2Val('CodeEntranceExam',paper.courseName)}</c:when>
									<c:otherwise>${paper.course.courseName }</c:otherwise>
								</c:choose></td>
							<td>${paper.classic.classicName}</td>
							<td><a href="javascript:void(0)"
								onclick="javascript:$.pdialog.open('${baseUrl }/edu3/metares/courseexampapers/input.html?resourceid=${paper.resourceid }&act=view','vieCourseExamPapers','试卷: ${paper.paperName }',{width:700,height:500});">${paper.paperName }</a></td>
							<td>${ghfn:dictCode2Val('CodePaperType',paper.paperType)}</td>
							<td>${paper.paperTime }</td>
							<td>${ghfn:dictCode2Val('yesOrNo',paper.isOpened)}</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<gh:page page="${courseExamPapersPage}"
				goPageUrl="${baseUrl}/edu3/metares/courseexampapers/list.html"
				condition="${condition }" pageType="sys" />

		</div>
	</div>
</body>
</html>