<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>试卷袋标签统计</title>
</head>
<body>
	<script type="text/javascript">

	//生成试卷袋标签
	function genExamPaper(){
		var examSubId = $("#examPaperBag_List_examSub option:selected").val();
		var url = "${baseUrl}/edu3/teaching/exam/paperbag/edit.html?examSubId="+examSubId;
		if(""==examSubId){
			alertMsg.warn("请选择一个考试批次!");
			return false;
		}
		$.pdialog.open(url,"RES_TEACHING_EXAM_PAPER_GEN","生成试卷袋标签", {width:800, height:600});
	}
	//查看试卷袋标签明细
	function showExamPaperBagDetails(examPaperBagId){
		var url = "${baseUrl}/edu3/teaching/exam/paperbagdetails/list.html?examPaperBagId="+examPaperBagId;
		$.pdialog.open(url,"RES_TEACHING_EXAM_PAPER_DETAILS","查看试卷袋标签明细", {width:800, height:600});
	}
	//按课程打印试卷袋标签
	function printExamPaperBagByCourse(){
		
		var examSubId     =$("#examPaperBag_List_examSub option:selected").val();
		var isMachineExam =$("#examPaperBag_List_isMachineExam").val();
		var courseId 	  =$("#examPaperBag_List_courseId").val();
		var brSchoolId 	  =$("#examPaperBag_List_brSchoolId").val();
		
		if(""==examSubId){
			alertMsg.warn("请选择一个考试批次!");
			return false;
		}
		
		var url="${baseUrl}/edu3/teaching/exam/paperbag/print-bycourse.html?flag=printByCours&examSubId="+examSubId+"&isMachineExam="+isMachineExam+"&courseId="+courseId+"&branchSchool="+brSchoolId;
		
		$.pdialog.open(url,"RES_TEACHING_EXAM_PAPERBAG_PRINTBYCOURSE","按课程打印试卷袋标签", {width:800, height:600});
	}
	//按教学站打印试卷袋标签
	function printExamPaperBagByUnit(){
		var examSubId	  =$("#examPaperBag_List_examSub option:selected ").val();
		var isMachineExam =$("#examPaperBag_List_isMachineExam").val();
		var brSchoolId 	  =$("#examPaperBag_List_brSchoolId").val();
		if(""==examSubId){
			alertMsg.warn("请选择一个考试批次!");
			return false;
		}
		var url="${baseUrl}/edu3/teaching/exam/paperbag/print-byschool.html?flag=printByBrschool&examSubId="+examSubId+"&isMachineExam="+isMachineExam+"&branchSchool="+brSchoolId;
		$.pdialog.open(url,"RES_TEACHING_EXAM_PAPERBAG_PRINTBYBRSCHOOL","按教学站打印试卷袋标签", {width:800, height:600});
	}
	//导出试卷袋标签统计表
	function exportExamPaperBagStat(){
		var examSubId     = $("#examPaperBag_List_examSub option:selected").val();
		var examSubName   = $("#examPaperBag_List_examSub option:selected").text();
		var unitId        = $("#examPaperBag_List_brSchoolId").val();
		var unitName      = $("#examPaperBag_List_brSchoolId_flexselect").val();
		var courseId      = $("#examPaperBag_List_courseId").val();
		var courseName    = $("#examPaperBag_List_courseId_flexselect").val();
		var tipInfo       = "导出";
		var isMachineExam = $("#examPaperBag_List_isMachineExam").val();
		var url 	  = "${baseUrl}/edu3/teaching/exam/paperbag/export.html?flag=exportStat&examSubId="+examSubId+"&unitId="+unitId+"&courseId="+courseId+"&isMachineExam="+isMachineExam;
		if(""==examSubId){
			alertMsg.warn("请选择一个考试批次!");
			return false;
		}
		tipInfo += examSubName;
		if(null!=unitName&&""!=unitName){
			tipInfo +=","+unitName;
		}
		if(null!=courseName&&""!=courseName){
			tipInfo +=","+courseName;
		}
		tipInfo += "试卷袋标签统计表";
		alertMsg.confirm(tipInfo,{
			okCall:function(){
				downloadFileByIframe(url,'examPaperBagStatExportIframe');
			}
		});
	}
	//导出试卷袋标签明细表
	function exportExamPaperBagDetails(){
		
		if(isCheckOnlyone('resourceid','#examPaperBagListBody')){
   			var examPaperStatId = $("#examPaperBagListBody input[@name='resourceid']:checked").val();
   			var examPaperInfo   = $("#examPaperBagListBody input[@name='resourceid']:checked").attr("title");
   			var examSubId       = $("#examPaperBag_List_examSub option:selected").val();
   			var examSubName     = $("#examPaperBag_List_examSub option:selected").text();
   			
   			var url 	        = "${baseUrl}/edu3/teaching/exam/paperbag/export.html?flag=exportDetails&examSubId="+examSubId+"&examPaperBagStatId="+examPaperStatId;
   			var tipInfo         = "导出";
   			if(""==examSubId){
   				alertMsg.warn("请选择一个考试批次!");
   				return false;
   			}
   			tipInfo += examSubName;
   			if(null!=examPaperInfo&&""!=examPaperInfo){
   				tipInfo +=","+examPaperInfo;
   			}

   			tipInfo += "试卷袋标签明细表";

   			alertMsg.confirm(tipInfo,{
   				okCall:function(){
   					downloadFileByIframe(url,'examPaperBagDetailsExportIframe');
   				}
   			});
		}			
	}
	//打印试卷袋标签统计表
	function printExamPaperBagStat(){
		var examSubId     = $("#examPaperBag_List_examSub option:selected").val();
		var unitId        = $("#examPaperBag_List_brSchoolId").val();
		var courseId      = $("#examPaperBag_List_courseId").val();
		var isMachineExam =$("#examPaperBag_List_isMachineExam").val();
		var url 	      = "${baseUrl}/edu3/teaching/exam/statpaperbag/print-view.html?flag=printStat&examSubId="+examSubId+"&unitId="+unitId+"&courseId="+courseId+"&isMachineExam="+isMachineExam;
		if(""==examSubId){
			alertMsg.warn("请选择一个考试批次!");
			return false;
		}
		$.pdialog.open(url,"RES_TEACHING_EXAM_PAPERBAG_PRINT_STAT","打印试卷袋标签汇总表", {width:800, height:600});
	}
	
	//打印试卷袋标签明细表
	function printExamPaperBagDetails(){
		if(isCheckOnlyone('resourceid','#examPaperBagListBody')){
			
   			var examPaperStatId = $("#examPaperBagListBody input[@name='resourceid']:checked").val();
   			var examSubId       = $("#examPaperBag_List_examSub option:selected").val();

   			var url 	        = "${baseUrl}/edu3/teaching/exam/statpaperbag/print-view.html?flag=printDetails&examSubId="+examSubId+"&examPaperBagStatId="+examPaperStatId;

   			if(""==examSubId){
   				alertMsg.warn("请选择一个考试批次!");
   				return false;
   			}
   			$.pdialog.open(url,"RES_TEACHING_EXAM_PAPERBAG_PRINT_DETAILS","打印试卷袋标签明细表", {width:800, height:600});
		}	
	}
</script>
	<div class="page">
		<div class="pageHeader">
			<form id="examPaperBagSearchForm"
				onsubmit="return navTabSearch(this);"
				action="${baseUrl}/edu3/teaching/exam/paperbag/list.html"
				method="post">
				<div class="searchBar">
					<ul class="searchContent">
						<li><label>预约批次：</label> <gh:selectModel
								id="examPaperBag_List_examSub" name="examSub"
								bindValue="resourceid" displayValue="batchName"
								style="width:55%" value="${condition['examSub'] }"
								modelClass="com.hnjk.edu.teaching.model.ExamSub"
								condition="batchType='exam',examsubStatus='2'"
								orderBy="batchName desc" /><font color="red">*</font></li>
						<c:if test="${!isBrschool}">
							<li><label>教学站：</label> <gh:brSchoolAutocomplete
									name="branchSchool" tabindex="1"
									id="examPaperBag_List_brSchoolId" displayType="code"
									defaultValue="${condition['branchSchool']}" style="width:53%" />
							</li>
						</c:if>
						<li><label>考试课程：</label> <gh:courseAutocomplete
								name="courseId" tabindex="1" id="examPaperBag_List_courseId"
								value="${condition['courseId']}" displayType="code"
								style="width:53%" /></li>
					</ul>
					<ul class="searchContent">
						<li><label>考试形式：</label> <select
							id="examPaperBag_List_isMachineExam" name="isMachineExam"
							style="width: 55%">
								<option value="">请选择</option>
								<option value="Y"
									<c:if test="${condition['isMachineExam'] eq 'Y'}"> selected="selected" </c:if>>机考</option>
								<option value="N"
									<c:if test="${condition['isMachineExam'] eq 'N'}"> selected="selected" </c:if>>笔试</option>
						</select></li>
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
		<gh:resAuth parentCode="RES_TEACHING_EXAM_PAPER_LIST" pageType="list"></gh:resAuth>
		<div class="pageContent">
			<table class="table" layouth="162" width="100%">
				<thead>
					<tr>
						<th width="5%"><input type="checkbox" name="checkall"
							id="check_all_examPaperBag"
							onclick="checkboxAll('#check_all_examPaperBag','resourceid','#examPaperBagListBody')" /></th>
						<th width="6%">考试编号</th>
						<th width="26%">课程名称</th>
						<th width="6%">考试形式</th>
						<th width="13%">考试时间</th>
						<th width="26%">教学站</th>
						<th width="6%">预约人数</th>
						<th width="6%">试卷份数</th>
						<th width="6%">包数</th>
					</tr>
				</thead>
				<tbody id="examPaperBagListBody">
					<c:forEach items="${page.result}" var="examPaperBag" varStatus="vs">
						<tr>
							<td><input type="checkbox" name="resourceid"
								value="${examPaperBag.resourceid}" autocomplete="off"
								title="${examPaperBag.unit.unitName }-${examPaperBag.examInfo.course.courseName }" /></td>
							<td>${examPaperBag.examInfo.examCourseCode }</td>
							<td><a href="javaScript:void(0)"
								onclick="showExamPaperBagDetails('${examPaperBag.resourceid}');">${examPaperBag.examInfo.course.courseName }</a></td>
							<td><c:choose>
									<c:when test="${examPaperBag.examInfo.isMachineExam eq 'Y' }">机考</c:when>
									<c:otherwise>${ghfn:dictCode2Val('CodeCourseExamType',examPaperBag.examInfo.course.examType)}</c:otherwise>
								</c:choose></td>
							<td><fmt:formatDate
									value="${examPaperBag.examInfo.examStartTime }"
									pattern="yyyy-MM-dd HH:mm" />- <fmt:formatDate
									value="${examPaperBag.examInfo.examEndTime }" pattern="HH:mm" />
							</td>
							<td>${examPaperBag.unit.unitName }</td>
							<td>${examPaperBag.orderNum }</td>
							<td>${examPaperBag.paperNum }</td>
							<td>${examPaperBag.bagNum } <c:set var="perPageBagCount"
									value="${perPageBagCount +examPaperBag.bagNum }"></c:set>
							</td>
						</tr>
					</c:forEach>
					<tr>
						<td>合计：</td>
						<td></td>
						<td></td>
						<td></td>
						<td></td>
						<td></td>
						<td></td>
						<td></td>
						<td><c:choose>
								<c:when test="${ null != perPageBagCount}">
									<font color="blue">${perPageBagCount }</font>/<font color="red">${condition['totalBagCount'] }</font>
								</c:when>
								<c:otherwise>0/0</c:otherwise>
							</c:choose></td>
					</tr>
				</tbody>
			</table>
			<gh:page page="${page}"
				goPageUrl="${baseUrl }/edu3/teaching/exam/paperbag/list.html"
				pageType="sys" condition="${condition}" />
		</div>
	</div>

</body>
</html>