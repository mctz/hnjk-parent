<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>打印准考证</title>
</head>
<body>

	<div class="page">
		<div class="pageHeader">
			<form id="printExamCardForm" onsubmit="return navTabSearch(this);"
				action="${baseUrl}/edu3/teaching/exam/examPrint/printExamCard-list.html"
				method="post">
				<div class="searchBar">
					<ul class="searchContent">
						<c:if test="${!isBrschool}">
							<li><label>教学站：</label> <gh:brSchoolAutocomplete
									name="branchSchool" tabindex="1" id="print_examcard_brSchoolId"
									displayType="code" defaultValue="${condition['branchSchool']}"
									style="width:53%" /> <font color="red">*</font></li>
						</c:if>
						<li><label>考试批次：</label> <gh:selectModel id="examSub"
								name="examSub" bindValue="resourceid" displayValue="batchName"
								style="width:55%" value="${condition['examSub'] }"
								modelClass="com.hnjk.edu.teaching.model.ExamSub"
								condition="batchType='exam'" orderBy="batchName desc" />
							<%-- yearInfo.resourceid='${defaultGrade.yearInfo.resourceid }',term='${defaultGrade.term }',--%>
							<font color="red">*</font></li>
						<li><label>年级：</label>
						<gh:selectModel id="gradeid" name="gradeid" bindValue="resourceid"
								displayValue="gradeName"
								modelClass="com.hnjk.edu.basedata.model.Grade"
								value="${condition['gradeid']}" orderBy="gradeName desc"
								style="width:55%" /></li>
					</ul>
					<ul class="searchContent">
						<li><label>层 次：</label> <gh:selectModel name="classic"
								bindValue="resourceid" displayValue="classicName"
								modelClass="com.hnjk.edu.basedata.model.Classic"
								value="${condition['classic']}" style="width:55%" /></li>
						<li><label>专 业：</label> <gh:selectModel name="major"
								bindValue="resourceid" displayValue="majorCodeName"
								modelClass="com.hnjk.edu.basedata.model.Major"
								value="${condition['major']}" orderBy="majorCode"
								style="width:55%" /></li>
						<li><label>姓名：</label><input type="text" name="stuName"
							value="${condition['name']}" style="width: 53%" /></li>
					</ul>
					<ul class="searchContent">
						<li><label>学号：</label><input type="text" name="studyNo"
							value="${condition['studyNo']}" style="width: 53%" /></li>
						<c:if test="${!isBrschool}">
							<span class="tips">提示：教学站、考试批次为必选条件</span>
						</c:if>
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
		<gh:resAuth parentCode="RES_TEACHING_EXAM_EXAMCARD" pageType="list"></gh:resAuth>
		<div class="pageContent">
			<table class="table" layouth="187" width="100%">
				<thead>
					<tr>
						<th width="5%"><input type="checkbox" name="checkall"
							id="check_all_print_exam_card"
							onclick="checkboxAll('#check_all_print_exam_card','resourceid','#printExamCardBody')" /></th>
						<th width="20%">批次</th>
						<th width="10%">学号</th>
						<th width="8%">姓名</th>
						<th width="13%">教学站</th>
						<th width="8%">层次</th>
						<th width="10%">年级</th>
						<th width="26%">专业</th>
					</tr>
				</thead>
				<tbody id="printExamCardBody">
					<c:forEach items="${objPage.result}" var="vo" varStatus="vs">
						<tr>
							<td><input title="${vo[2] }" type="checkbox"
								name="resourceid" value="${vo[0] }" autocomplete="off" /></td>
							<td title="${vo[7] }">${vo[7] }</td>
							<td title="${vo[1] }">${vo[1] }</td>
							<td title="${vo[2] }">${vo[2] }</td>
							<td title="${vo[3] }">${vo[3] }</td>
							<td title="${vo[4] }">${vo[4] }</td>
							<td title="${vo[5] }">${vo[5] }</td>
							<td title="${vo[6] }">${vo[6] }</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<gh:page page="${objPage}"
				goPageUrl="${baseUrl }/edu3/teaching/exam/examPrint/printExamCard-list.html"
				pageType="sys" condition="${condition}" />

		</div>
	</div>
	<script type="text/javascript">
	function _checkPrintExamCardPostForm(obj){
		if($('#printExamCardForm #print_examcard_brSchoolId')){
			alertMsg.warn("请选择一个教学站！")
			return false;
		}
		
		if($('#printExamCardForm #examSub')){
			alertMsg.warn("请选择一个考试批次！")
			return false;
		}
		
		return navTabSearch(obj);
	}
	
	function printExamCard(){
		
		var examSubId    = $("#printExamCardForm select[name='examSub']").val();
		var gradeid      = $("#printExamCardForm select[name='gradeid']").val();
		var classic      = $("#printExamCardForm select[name='classic']").val();
		var major        = $("#printExamCardForm select[name='major']").val();
		var branchSchool = $("#printExamCardForm input[name='branchSchool']").val();
		var stuName      = $("#printExamCardForm input[name='stuName']").val();
		var studyNo      = $("#printExamCardForm input[name='studyNo']").val();
		if("" == branchSchool){
			alertMsg.warn("请选择一个教学站！")
			return false;
		}
		if (""==examSubId){
			alertMsg.warn("请选择一个考试批次！")
			return false;
		}
		var studentResourceid = new Array();
		jQuery("#printExamCardBody input[name='resourceid']:checked").each(function(){
			studentResourceid.push(jQuery(this).val());
		});
		var url  = "${baseUrl}/edu3/teaching/exam/examPrint/printExamCard-view.html";
			url += "?examSub="+examSubId+"&gradeid="+gradeid+"&major="+major+"&classic="+classic;
			url += "&branchSchool="+branchSchool+"&stuName="+stuName+"&studyNo="+studyNo+"&stuId="+studentResourceid.toString();
			
		$.pdialog.open(url,'RES_TEACHING_EXAM_EXAMCARD_PRINT_VIEW','打印预览',{height:600, width:800});
	}
</script>
</body>
</html>