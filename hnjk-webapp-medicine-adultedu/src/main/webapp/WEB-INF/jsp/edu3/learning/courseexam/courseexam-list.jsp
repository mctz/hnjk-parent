<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>题库试题管理</title>
<script type="text/javascript">
	//新增
	function addCourseExam(){
		var url = "${baseUrl}/edu3/metares/courseexam/input.html";
		navTab.openTab('courseExamInput', url, '新增试题');
	}
	//修改
	function modifyCourseExam(){
		var url = "${baseUrl}/edu3/metares/courseexam/input.html";
		if(isCheckOnlyone('resourceid','#courseExamBody')){
			navTab.openTab('courseExamInput', url+'?resourceid='+$("#courseExamBody input[@name='resourceid']:checked").val(), '编辑试题');
		}			
	}		
	//删除
	function removeCourseExam(){	
		pageBarHandle("您确定要删除这些试题吗？","${baseUrl}/edu3/metares/courseexam/remove.html","#courseExamBody");
	}
	
	//导入
	function importCourseExam(){	
		$.pdialog.open("${baseUrl}/edu3/framework/courseexam/upload.html?isEnrolExam=N", "courseexamimport", "导入习题", {width:800,height:500,mask:true});
	}
	
	function courseexam_navTabSearch(form){
		var showOrder = $(form).find("[name='showOrder']").val();
		if($.trim(showOrder)!=""){
			if(!$.trim(showOrder).isInteger()){
				alertMsg.warn("序号必须为整数!");
				return false;
			}
		}
		return navTabSearch(form);
	}	
	//转换base64图片
	function convertBase64Image(){
		alertMsg.confirm("你要把试题中的base64图片数据转为图片文件吗?", {
			okCall: function(){		
				$.post("${baseUrl}/edu3/metares/courseexam/image/convert.html",{}, navTabAjaxDone, "json");
			}
		});			
	}
	
	//导入试题excel
	function importCourseExamByExcel(){	
		$.pdialog.open("${baseUrl}/edu3/framework/courseexam/uploadExcel.html", "courseexamimport", "导入习题(excel)", {width:800,height:500,mask:true});
	}
</script>
</head>
<body>
	<div class="page">
		<div class="pageHeader">
			<form onsubmit="return courseexam_navTabSearch(this);"
				action="${baseUrl }/edu3/metares/courseexam/list.html" method="post">
				<div class="searchBar">
					<ul class="searchContent">
						<li><label>课程:</label> <input type="hidden"
							name="isEnrolExam" value="${condition['isEnrolExam']}" /> <select
							id="courseexam_courseExamCourseId" name="courseId"
							style="width: 130px;">
								<option value=""></option>
								<c:forEach items="${courseList }" var="c">
									<option value="${c.resourceid }"
										<c:if test="${condition['courseId'] eq c.resourceid }">selected="selected"</c:if>>${c.courseCode }-${c.courseName }</option>
								</c:forEach>
						</select> <script type="text/javascript">
					$(document).ready(function(){
						$("#courseexam_courseExamCourseId").flexselect();
				    });
					</script></li>
						<li><label>类别：</label>
						<gh:select name="examNodeType"
								value="${condition['examNodeType']}"
								dictionaryCode="CodeExamNodeType" /></li>
						<li><label>题型：</label>
						<gh:select name="examType" value="${condition['examType']}"
								dictionaryCode="CodeExamType" /></li>
						
					</ul>
					<ul class="searchContent">
						<li><label>考试形式：</label>
						<gh:select id="courseExams_examform" dictionaryCode="CodeExamform"
								name="examform" value="${condition['examform'] }" choose="N"
								filtrationStr="unit_exam,online_exam,final_exam" style="width:120px;"/></li>
						<li><label>关键字：</label><input type="text" name="keywords"
							value="${condition['keywords'] }" style="width: 130px;"/></li>
						<li><label>难度：</label>
						<gh:select name="difficult" value="${condition['difficult']}"
								dictionaryCode="CodeExamDifficult" /></li>
						<li><label>序号：</label><input type="text" name="showOrder"
							value="${condition['showOrder']}" size="3" /></li>
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
			<gh:resAuth parentCode="RES_METARES_COURSEEXAM" pageType="list"></gh:resAuth>
			<table class="table" layouth="184">
				<thead>
					<tr>
						<th width="5%"><input type="checkbox" name="checkall"
							id="check_all_courseexam"
							onclick="checkboxAll('#check_all_courseexam','resourceid','#courseExamBody')" /></th>
						<th width="10%">序号</th>
						<th width="20%">课程</th>
						<th width="10%">类别</th>
						<th width="10%">题型</th>
						<th width="10%">难度</th>
						<th width="10%">考试要求</th>
						<th width="15%">关键字</th>
						<th width="10%">&nbsp;</th>
					</tr>
				</thead>
				<tbody id="courseExamBody">
					<c:forEach items="${courseExamList.result }" var="exam"
						varStatus="vs">
						<tr>
							<td><input type="checkbox" name="resourceid"
								value="${exam.resourceid }" autocomplete="off" /></td>
							<td>${exam.showOrder }</td>
							<td>${exam.course.courseName }</td>
							<td>${ghfn:dictCode2Val('CodeExamNodeType',exam.examNodeType)}</td>
							<td>${ghfn:dictCode2Val('CodeExamType',exam.examType)}</td>
							<td>${ghfn:dictCode2Val('CodeExamDifficult',exam.difficult)}</td>
							<td>${ghfn:dictCode2Val('CodeTeachingRequest',exam.requirement)}</td>
							<td>${exam.keywords }</td>
							<td><a href="javascript:;"
								onclick="javascript:$.pdialog.open('${baseUrl }/edu3/metares/courseexam/view.html?courseExamId=${exam.resourceid }','selector','查看试题',{width:600,height:400});">查看</a>
							</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<gh:page page="${courseExamList}"
				goPageUrl="${baseUrl}/edu3/metares/courseexam/list.html"
				condition="${condition }" pageType="sys" />

		</div>
	</div>
</body>
</html>