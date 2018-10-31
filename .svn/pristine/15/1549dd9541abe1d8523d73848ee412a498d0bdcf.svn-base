<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>考试批次</title>
<script type="text/javascript">
	//新增
	function addExamplan(){
		navTab.openTab('RES_TEACHING_EXAM_PLANSETTING_ADD', '${baseUrl}/edu3/teaching/exam/plan/input.html', '新增考试批次');
	}
	//修改
	function modifyExamplan(){
		var url = "${baseUrl}/edu3/teaching/exam/plan/input.html";
		if(isCheckOnlyone('resourceid','#examSubBody')){
			navTab.openTab('RES_TEACHING_EXAM_PLANSETTING_MODIFY', url+'?resourceid='+$("#examSubBody input[@name='resourceid']:checked").val(), '编辑考试批次');
		}			
	}	
	//删除
	function deleteExamplan(){	
		pageBarHandle("您确定要删除这些记录吗？","${baseUrl}/edu3/teaching/exam/plan/remove.html","#examSubBody");
	}
	//关闭
	function closeExamplan(){
		pageBarHandle("您确定要关闭这些记录吗？","${baseUrl}/edu3/teaching/exam/plan/close.html","#examSubBody");
	}
	//设定考试时间
    function setExamplanCourse(){
    	var url = "${baseUrl}/edu3/teaching/exam/plan/setting.html";
   		if(isCheckOnlyone('resourceid','#examSubBody')){
   			navTab.openTab('RES_TEACHING_EXAM_PLANSETTING_SETTTIME', url+'?resourceid='+$("#examSubBody input[@name='resourceid']:checked").val(), '设定考试时间');
   		}		
    }
	//设置成绩比例
	function examInfoConfig(){
		var url = "${baseUrl}/edu3/teaching/exam/examinfo-config-page.html";
   		if(isCheckOnlyone('resourceid','#examSubBody')){
   			navTab.openTab('RES_TEACHING_EXAM_PLAN_EXAMSCOREPER_CONFIG', url+'?type=list&resourceid='+$("#examSubBody input[@name='resourceid']:checked").val(), '考试课程设置');
   		}	
	}
	//开放批次
	function openExamSub(){
		if(isCheckOnlyone('resourceid','#examSubBody')){
			pageBarHandle("您确定要开放所选考试批次吗？","${baseUrl}/edu3/teaching/exam/plan/open.html","#examSubBody");
		}
	}
	//导出考试课程信息(时间、课程编号)
	function exportExamCoursInfo(){
		if(isCheckOnlyone('resourceid','#examSubBody')){
			var examSubId       = $("#examSubBody input[name='resourceid']:checked").val();
			window.location.href= "${baseUrl}/edu3/teaching/exam/examExport/export-examcourse.html?examSubId="+examSubId;
		}
	}
	//打印考试课程信息(时间、课程编号)
	function printExamCoursInfo(){
		if(isCheckOnlyone('resourceid','#examSubBody')){
			var examSubId = $("#examSubBody input[name='resourceid']:checked").val();
			var url 	  = "${baseUrl}/edu3/teaching/exam/examPrint/print-examcourse-view.html?examSubId="+examSubId;
			$.pdialog.open(url,"RES_TEACHING_EXAM_PLAN_EXAMCOURSE_PRINT","打印考试课程信息", {width:800, height:400});
			//pageBarHandle("确定要打印所选批次的考试课程信息吗？","${baseUrl}/edu3/teaching/exam/examPrint/print-examcourse-view.html","#examSubBody");
		}
	}
	//生成考试课程编号 
	function examCourseCodeGen(){
		if(isCheckOnlyone('resourceid','#examSubBody')){
			var examSubId       = $("#examSubBody input[name='resourceid']:checked").val();
			var url             = "${baseUrl}/edu3/teaching/exam/examcode/gen.html";
			alertMsg.confirm("确定要为有预约考试记录、考试形式为开卷、闭卷生非实践性质课程生成考试课程编号吗？", {
				 okCall: function(){
				 	jQuery.post(url,{resourceid:examSubId},function(returnData){	
						var msg 	   = returnData['message'];
					  	var statusCode =  returnData['statusCode'];
					  	if(statusCode==200){
					  		alertMsg.info(msg);
						}else{
							alertMsg.warn(msg);
						}
					},"json");
				 }
			});
		}
	}
</script>
</head>
<body>

	<div class="page">
		<div class="pageHeader">
			<form onsubmit="return navTabSearch(this);"
				action="${baseUrl }/edu3/teaching/exam/plan/list.html" method="post">
				<div class="searchBar">
					<ul class="searchContent">
						<li><label>年度：</label> <gh:selectModel id="yearId" style="width: 120px;"
								name="yearId" bindValue="resourceid" displayValue="yearName"
								modelClass="com.hnjk.edu.basedata.model.YearInfo"
								value="${condition['yearId']}" /></li>
						<li><label>学期：</label> <gh:select name="term" style="width: 120px;"
								dictionaryCode="CodeTerm" value="${condition['term']}" /></li>
						<li><label>考试类型：</label> <gh:select name="examType" style="width: 120px;"
								dictionaryCode="ExamResult" value="${condition['examType']}" /></li>
						<li><label>批次状态：</label> <gh:select name="examsubStatus" style="width: 120px;"
								dictionaryCode="CodeExamSubscribeState"
								value="${condition['status'] }" /></li>
					</ul>
					<ul class="searchContent">
						<li><label>预约开始时间：</label> <input type="text" id="startTime"
							name="startTime" class="required" style="width: 50%"
							value="${condition['startTime']}"
							onclick="WdatePicker({isShowWeek:true,dateFmt:'yyyy-MM-dd HH:mm:ss',maxDate:'#F{$dp.$D(\'endTime\')}'})" />
						</li>
						<li><label>预约结束时间：</label> <input type="text" id="endTime"
							name="endTime" class="required" style="width: 50%"
							value="${condition['endTime']}"
							onclick="WdatePicker({isShowWeek:true,dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:'#F{$dp.$D(\'startTime\')}'})" />
						</li>
						<div class="buttonActive" style="float: right;">
							<div class="buttonContent">
								<button type="submit">查 询</button>
							</div>
						</div>
					</ul>
				</div>
			</form>
		</div>
		<div class="pageContent">
			<gh:resAuth parentCode="RES_TEACHING_EXAM_PLAN" pageType="list"></gh:resAuth>
			<table class="table" layouth="135">
				<thead>
					<tr>
						<th style="width: 4%; text-align: center;"><input
							type="checkbox" name="checkall" id="check_all_tp"
							onclick="checkboxAll('#check_all_tp','resourceid','#examSubBody')" /></th>
						<th style="width: 26%; text-align: center;">批次名称</th>
						<th style="width: 10%; text-align: center;">年度</th>
						<th style="width: 5%; text-align: center;">学期</th>
						<th style="width: 20%; text-align: center;">预约时间</th>
						<%-- <th width="13%">预约截止时间</th> --%>
						<th style="width: 15%; text-align: center;">添加预约信息截止时间</th>
						<th style="width: 6%; text-align: center;">预约状态</th>
						<th style="width: 8%; text-align: center;">是否发布座位</th>
						<%-- <th style="width:6%;text-align: center;">批次类型</th>--%>
						<th style="width: 6%; text-align: center;">考试类型</th>
					</tr>
				</thead>
				<tbody id="examSubBody">
					<c:forEach items="${examSubList.result}" var="examSub"
						varStatus="vs">
						<tr>
							<td style="text-align: center;"><input type="checkbox"
								name="resourceid" value="${examSub.resourceid }"
								autocomplete="off" /></td>
							<td title="${examSub.batchName }">${examSub.batchName }</td>
							<td style="text-align: center;"
								title="${examSub.yearInfo.yearName }">${examSub.yearInfo.yearName }</td>
							<td style="text-align: center;">${ghfn:dictCode2Val('CodeTerm',examSub.term)}</td>
							<td style="text-align: center;"
								title="<fmt:formatDate value="${examSub.startTime }" pattern="yyyy/MM/dd HH:mm:ss"/>-<fmt:formatDate value="${examSub.endTime }" pattern="yyyy/MM/dd HH:mm:ss"/>">
								<fmt:formatDate value="${examSub.startTime }"
									pattern="yyyy/MM/dd HH:mm:ss" />&nbsp;-&nbsp; <fmt:formatDate
									value="${examSub.endTime }" pattern="yyyy/MM/dd HH:mm:ss" />
							</td>
							<%--<td ><fmt:formatDate value="${examSub.endTime }" pattern="yyyy-MM-dd HH:mm:ss"/></td> --%>
							<td style="text-align: center;"><fmt:formatDate
									value="${examSub.examsubEndTime }"
									pattern="yyyy/MM/dd/ HH:mm:ss" /></td>
							<td style="text-align: center;">${ghfn:dictCode2Val('CodeExamSubscribeState',examSub.examsubStatus)}</td>
							<td style="text-align: center;">${ghfn:dictCode2Val('yesOrNo',examSub.isseatPublished )}</td>
							<td style="text-align: center;">${ghfn:dictCode2Val('ExamResult',examSub.examType )}</td>
							<%-- <td style="text-align: center;">
			        		<c:if test="${examSub.batchType eq 'exam'}">考试批次</c:if>
			        		<c:if test="${examSub.batchType eq 'thesis'}">论文批次</c:if>
			        	</td>--%>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<gh:page page="${examSubList}"
				goPageUrl="${baseUrl }/edu3/teaching/exam/plan/list.html"
				pageType="sys" condition="${condition }" />
		</div>
	</div>

</body>
