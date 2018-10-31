<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>考试批次</title>
</head>
<script type="text/javascript">
	$(document).ready(function(){
		var modifyScoreType = "${modifyScoreType}";
		if(modifyScoreType=="N"){
			$("#facestudyScorePer").attr("readonly","readonly");
			$("#facestudyScorePer2").attr("readonly","readonly");
			$("#writtenScorePer").attr("readonly","readonly");
			$("#netsidestudyScorePer").attr("readonly","readonly");
		}
	});
	var changType = "${AddOrUpdate}";
	$("#yearId1").click(function(){
		if(changType=="add"){
			setTime();
		}
	});
	$("#term1").click(function(){
		if(changType=="add"){
			setTime();
		}
	});
	function setTime(){
		var year=$("#yearId1 :selected").text().substr(0,4);
		var term=$("#term1 :selected").val();
		
		if(term=='1'){		
			$("#YYstartTime").val(year+"-03-01 00:00:00"); //预约开始时间
			$("#YYendTime").val(year+"-09-01 00:00:00");//预约截止时间
			$("#examinputStartTime").val(year+"-03-01 00:00:00"); //成绩录入开始时间
			$("#examinputEndTime").val(year+"-09-01 00:00:00"); //成绩录入截止时间
			$("#usualScoreInputStartTime").val(year+"-03-01 00:00:00"); //平时成绩录入开始时间
			$("#usualScoreInputEndTime").val(year+"-09-01 00:00:00"); //平时成绩录入截止时间
			$("#examsubEndTime").val(year+"-03-01 00:00:00"); //添加预约信息截止时间
		}else{
			
			$("#YYstartTime").val(year+"-09-01 00:00:00"); //预约开始时间
			
			$("#examinputStartTime").val(year+"-09-01 00:00:00"); //成绩录入开始时间
			$("#usualScoreInputStartTime").val(year+"-09-01 00:00:00"); //平时成绩录入开始时间
			$("#examsubEndTime").val(year+"-09-01 00:00:00"); //添加预约信息截止时间
			year=year-1+2;
			$("#examinputEndTime").val(year+"-03-01 00:00:00"); //成绩录入截止时间
			$("#YYendTime").val(year+"-03-01 00:00:00");//预约截止时间
			$("#usualScoreInputEndTime").val(year+"-03-01 00:00:00"); //平时成绩录入截止时间
			
		}
	}
	function checkForm(){
		if (checkFacestudyScorePerSum()) {
			$("form.pageForm").submit();
		}
		//$("form.pageForm").submit();
	}
	function checkFacestudyScorePerSum(){
		var facestudyScorePer = $("#facestudyScorePer").val();
		var facestudyScorePer2 = $("#facestudyScorePer2").val();
		//var facestudyScorePer3 = $("#facestudyScorePer3").val();
		var netstudyScorePer  = $("#writtenScorePer").val();
		var netstudyScorePer2 = $("#netsidestudyScorePer").val();
		if (facestudyScorePer != "" && facestudyScorePer2 != "" 
				&& (parseInt(facestudyScorePer) + parseInt(facestudyScorePer2)) != 100) {
			alertMsg.warn("面授课程比例之和必须为100！");
			//$("button[type='submit']").attr("disabled", "disabled");
			return false;
		}
		if (netstudyScorePer != "" && netstudyScorePer2 != "" 
			&& (parseInt(netstudyScorePer) + parseInt(netstudyScorePer2)) != 100) {
			alertMsg.warn("网络课程比例之和必须为100！");
			//$("button[type='submit']").attr("disabled", "disabled");
			return false;
		}
		//$("button[type='submit']").removeAttr("disabled");
		return true;
	}
	
	//设置成绩比例
	function examInfoConfingShowPage(examCourseType,tbodyId,currentIndex){
		var res  	   = new Array();
		var examSubId  = "${examSub.resourceid}";
		var url        = "${baseUrl}/edu3/teaching/exam/examinfo-config-page.html?type=setting&currentIndex="
				+currentIndex+"&examCoureType="+examCourseType+"&resourceid="+examSubId+"&navTabId=examSub";
		$("#"+tbodyId+" INPUT[name='c_id']:checked").each(function(ind){
			res.push($(this).val());
		});
		if (res.length > 200) {
			alertMsg.warn("请不要选择超过200门课程！");
			return false;
		}
		if(res.length>0){
			alertMsg.confirm("确定要设置所选课程的成绩比例吗？", {
				 okCall: function(){
					 $.pdialog.open(url+"&ids="+res.toString(),"RES_TEACHING_EXAM_PLAN_EXAMSCOREPER_CONFIG_SETTING","成绩比例设置",{width:800, height:600,mask:true});	
				 }
			});
		}else{
			alertMsg.warn("请选择要设置的考试课程!");
			return false;
		}
	}
	function genUsuallyScoreRatio(){
		var writtenScorePer = $("#writtenScorePer").val();
		if(parseInt(writtenScorePer)>100){
			$("#writtenScorePer").attr("value","");
			$("#usuallyScorePer").children().remove();
			alertMsg.warn("请输入一个0-100之间的数字!");
			return false;
		}
		var usuallyScorePer = 100-writtenScorePer;
		var appendHTML = "<option value='"+usuallyScorePer.toFixed(2)+"' >"+usuallyScorePer+"%</option>";
		$("#usuallyScorePer").html(appendHTML);
	}	

     function delExaminfoTabwangsou(endfix){
    	$("#examinfoTab"+endfix+" input[name='c_id']:checked").each(function(ind){
		  	var url    = "${baseUrl}/edu3/teaching/exam/plan/remove.html";
		  	var infoId = $(this).val();
		  	var delTD  = "#examinfoTab"+endfix+"  input[value='"+infoId+"']";
		  	$.get(url, {c_id:$(this).val()},function(data){
		  		var message    = data['message']
		  		var statusCode = data['statusCode']
		  		if("200"==statusCode){
		  			$(delTD).parent().parent().remove();
		  		}else{
		  			alertMsg.warn(message);
		  		}
		  	},"json");
		  
		});
    }
	function editExaminfoForNetCourseTabmianshou(tbodyId){
    	if(isCheckOnlyone('c_id','#'+tbodyId)){
    		$.pdialog.open('${baseUrl }/edu3/framework/teaching/netcourse/input.html?c_id='+$("#"+tbodyId+" input[name='c_id']:checked").val(),
					'examinfo_for_netcourse_edit','编辑考试课程',{width:600,height:400});	
    	}
    }
    function addExaminfoForNetCourseTab(){
    	$.pdialog.open('${baseUrl }/edu3/framework/teaching/netcourse/input.html?examSubId=${examSub.resourceid}','examinfo_for_netcourse_add','新增考试课程',{width:600,height:400});	
    }
    function generateIndex(){
    	$("input[name='c_id']").each(function(ind){
    		$("input[name='showOrder']:eq("+ind+")").val(ind+1);
    	})
    }
  
</script>
<body>
	<h2 class="contentTitle">编辑考试批次</h2>
	<div class="page">
		<div class="pageContent">
			<form method="post"
				action="${baseUrl}/edu3/teaching/exam/plan/save.html"
				class="pageForm" onsubmit="return validateCallback(this);">
				<input type="hidden" name="resourceid"
					value="${examSub.resourceid }" />
				<div class="pageFormContent" layoutH="97">
					<div class="tabs"
						<c:if test="${not empty currentIndex }">currentIndex=${currentIndex }</c:if>>
						<div class="tabsHeader">
							<div class="tabsHeaderContent">
								<ul>
									<li><a href="#"><span>考试批次</span></a></li>
									<li class="#"><a href="#"><span>网授考试课程信息</span></a></li>
									<li class="#"><a href="#"><span>面授考试课程信息</span></a></li>
								</ul>
							</div>
						</div>
						<div class="tabsContent" style="height: 100%;">
							<!-- 考试/毕业论文批次预约表 -->
							<div>
								<table class="form">
									<tr>
										<td><strong>批次名称:</strong></td>
										<td colspan="3"><input type="text" name="batchName"
											style="width: 18%" value="${examSub.batchName }"
											class="required" /></td>
									</tr>
									<tr>
										<td><strong>年度:</strong></td>
										<td><gh:selectModel id="yearId1" onclick="setTime()"
												name="yearId" bindValue="resourceid" displayValue="yearName"
												style="width:53%"
												modelClass="com.hnjk.edu.basedata.model.YearInfo"
												value="${examSub.yearInfo.resourceid}" choose="N" /></td>
										<td><strong>学期:</strong></td>
										<td><gh:select id="term1" name="term"
												onclick="setTime()" dictionaryCode="CodeTerm"
												value="${examSub.term}" choose="N" style="width:53%" /></td>
									</tr>
									<tr>
										<td><strong>预约开始时间:</strong></td>
										<td><input type="text" id="YYstartTime" name="startTime"
											class="required" style="width: 50%"
											value='<fmt:formatDate  value="${examSub.startTime  }" pattern="yyyy-MM-dd HH:mm:ss"/>'
											onclick="WdatePicker({isShowWeek:true,dateFmt:'yyyy-MM-dd HH:mm:ss',maxDate:'#F{$dp.$D(\'endTime\')}'})" />
										</td>
										<td><strong>预约截止时间:</strong></td>
										<td><input type="text" id="YYendTime" name="endTime"
											class="required" style="width: 50%"
											value='<fmt:formatDate value="${examSub.endTime  }" pattern="yyyy-MM-dd HH:mm:ss"/>'
											onclick="WdatePicker({isShowWeek:true,dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:'#F{$dp.$D(\'startTime\')}'})" />
										</td>
									</tr>
									<tr>
										<td><strong>成绩录入开始时间:</strong></td>
										<td><input type="text" id="examinputStartTime"
											name="examinputStartTime" class="required" style="width: 50%"
											value='<fmt:formatDate value="${examSub.examinputStartTime  }" pattern="yyyy-MM-dd HH:mm:ss"/>'
											onclick="WdatePicker({isShowWeek:true,dateFmt:'yyyy-MM-dd HH:mm:ss',maxDate:'#F{$dp.$D(\'examinputEndTime\')}'})" />
										</td>
										<td><strong>成绩录入截止时间:</strong></td>
										<td><input type="text" id="examinputEndTime"
											name="examinputEndTime" class="required" style="width: 50%"
											value='<fmt:formatDate value="${examSub.examinputEndTime  }" pattern="yyyy-MM-dd HH:mm:ss"/>'
											onclick="WdatePicker({isShowWeek:true,dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:'#F{$dp.$D(\'examinputStartTime\')}'})" />
										</td>
									</tr>
									<tr>
										<td><strong>平时成绩录入开始时间:</strong></td>
										<td><input type="text" id="usualScoreInputStartTime"
											name="usualScoreInputStartTime" class="required"
											style="width: 50%"
											value='<fmt:formatDate value="${examSub.usualScoreInputStartTime  }" pattern="yyyy-MM-dd HH:mm:ss"/>'
											onclick="WdatePicker({isShowWeek:true,dateFmt:'yyyy-MM-dd HH:mm:ss',maxDate:'#F{$dp.$D(\'usualScoreInputEndTime\')}'})" />
										</td>
										<td><strong>平时成绩录入截止时间:</strong></td>
										<td><input type="text" id="usualScoreInputEndTime"
											name="usualScoreInputEndTime" class="required"
											style="width: 50%"
											value='<fmt:formatDate value="${examSub.usualScoreInputEndTime  }" pattern="yyyy-MM-dd HH:mm:ss"/>'
											onclick="WdatePicker({isShowWeek:true,dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:'#F{$dp.$D(\'usualScoreInputStartTime\')}'})" />
										</td>
									</tr>
									<tr>
										<td><strong>添加预约信息截止时间:</strong></td>
										<td><input type="text" id="examsubEndTime"
											name="examsubEndTime" class="required" style="width: 50%"
											value='<fmt:formatDate value="${examSub.examsubEndTime  }" pattern="yyyy-MM-dd HH:mm:ss"/>'
											onclick="WdatePicker({isShowWeek:true,dateFmt:'yyyy-MM-dd HH:mm:ss'})" />
										</td>
										<td><strong>预约状态:</strong></td>
										<td><gh:select name="examsubStatus"
												dictionaryCode="CodeExamSubscribeState"
												value="${examSub.examsubStatus}" choose="N"
												style="width:25%" /></td>
									</tr>
									<tr>
										<td><strong>是否发布座位:</strong></td>
										<td><gh:select name="isseatPublished"
												dictionaryCode="yesOrNo" value="${examSub.isseatPublished}"
												choose="N" style="width:25%" /></td>
										<td><strong>成绩类型:</strong></td>
										<td title="此设置影响新加考试课程的成绩类型"><c:choose>
												<c:when test="${ empty examSub.courseScoreType }">
													<gh:select name="courseScoreType"
														dictionaryCode="CodeCourseScoreStyle" value="11"
														style="width:25%" />
												</c:when>
												<c:when
													test="${ not empty examSub.courseScoreType and modifyScoreType eq 'Y' }">
													<gh:select name="courseScoreType"
														dictionaryCode="CodeCourseScoreStyle"
														value="${examSub.courseScoreType }" style="width:25%" />
												</c:when>
												<c:otherwise>
										${ghfn:dictCode2Val('CodeCourseScoreStyle',examSub.courseScoreType)}
									</c:otherwise>
											</c:choose></td>
									</tr>
									<tr>
										<td><strong>面授课程卷面分比例：</strong></td>
										<td title="此设置影响新加面授课程卷面分比例">
											<input name="facestudyScorePer" id="facestudyScorePer"
												class="required number" min="0" max="100"
												onblur="//checkFacestudyScorePerSum()"
												onKeyUp="value=value.replace(/[^\d]/g,'') "
												onbeforepaste="clipboardData.setData('text',clipboardData.getData('text').replace(/[^\d]/g,''))"
												style="width: 23%"
												value="<fmt:formatNumber value="${examSub.facestudyScorePer}" pattern="#"></fmt:formatNumber>" />%
										</td>
										<td><strong>网络课程卷面分比例：</strong></td>
										<td title="此设置影响新加网络课程卷面分比例">
											<input name="writtenScorePer" id="writtenScorePer"
												class="required number" min="0" max="100"
												onKeyUp="value=value.replace(/[^\d]/g,'') "
												onbeforepaste="clipboardData.setData('text',clipboardData.getData('text').replace(/[^\d]/g,''))"
												style="width: 23%"
												value="<fmt:formatNumber value="${examSub.writtenScorePer}" pattern="#"></fmt:formatNumber>" />%
										</td>
									</tr>
									<tr>
										<td><strong>面授课程平时考核分比例：</strong></td>
										<td title="此设置影响新加面授课程平时考核分比例">
											<input name="facestudyScorePer2" id="facestudyScorePer2"
												class="required number" min="0" max="100"
												onblur="//checkFacestudyScorePerSum()"
												onKeyUp="value=value.replace(/[^\d]/g,'') "
												onbeforepaste="clipboardData.setData('text',clipboardData.getData('text').replace(/[^\d]/g,''))"
												style="width: 23%"
												value="<fmt:formatNumber value="${examSub.facestudyScorePer2}" pattern="#"></fmt:formatNumber>" />%
										</td>
										<td><strong>网络课程平时考核分比例：</strong></td>
										<td title="此设置影响新加网络面授课程卷面分比例">
											<input name="netsidestudyScorePer"
												id="netsidestudyScorePer" class="required number" min="0"
												max="100" onKeyUp="value=value.replace(/[^\d]/g,'') "
												onbeforepaste="clipboardData.setData('text',clipboardData.getData('text').replace(/[^\d]/g,''))"
												style="width: 23%"
												value="<fmt:formatNumber value="${examSub.netsidestudyScorePer}" pattern="#"></fmt:formatNumber>" />%
										</td>
									</tr>
									<tr>
										<td><strong>考试类型：</strong></td>
										<td><gh:select name="examType"
												dictionaryCode="ExamResult" value="${examSub.examType}"
												choose="N" style="width:25%" /></td>
										<td></td>
										<td></td>
										<%-- 华教机考系统修改
							 <td><strong>考试类型：</strong></td>
							<td colspan="3"><gh:select name="examType" dictionaryCode="ExamResult" value="${examSub.examType}" choose="N" style="width:25%" />
							</td>
							 --%>
									</tr>
								</table>
							</div>
							<%-- <c:forEach items="${list_dict}" var="dict" varStatus="vs"> --%>
							<div>
								<table class="form" id="examinfoTab">
									<!--   -->
									<thead>
										<tr>
											<th colspan="7"><span class="buttonActive"><div
														class="buttonContent">
														<button type="button" onclick="delExaminfoTabwangsou('')">删除</button>
													</div></span> <span class="buttonActive"><div
														class="buttonContent">
														<button type="button"
															onclick="editExaminfoForNetCourseTabmianshou('examSub_examInfoBody1')">编辑考试课程</button>
													</div></span> <span class="buttonActive"><div
														class="buttonContent">
														<button type="button"
															onclick="addExaminfoForNetCourseTab()">新增考试课程</button>
													</div></span> <span class="buttonActive"><div
														class="buttonContent">
														<button type="button"
															onclick="examInfoConfingShowPage('0','examSub_examInfoBody1','2');">设置成绩比例</button>
													</div></span></th>
										</tr>
										<tr>
											<th style="width: 3%"><strong><input
													type="checkbox" name="checkall" id="check_all_examinfo"
													onclick="checkboxAll('#check_all_examinfo','c_id','#examSub_examInfoBody1')" /></strong></th>
											<th style="width: 15%"><strong>课程名称</strong></th>
											<th style="width: 6%; text-align: center;"><strong>考试形式</strong></th>
											<th style="width: 20%; text-align: center;"><strong>考试时间</strong></th>
											<th style="width: 20%; text-align: center;">成绩比例</th>
											<th style="width: 10%; text-align: center;"><strong>备注</strong></th>
											<th style="width: 10%; text-align: center;"><strong>考试课程类型</strong></th>
										</tr>
									</thead>
									<tbody id="examSub_examInfoBody1">
										<c:forEach items="${examSub.examInfo }" var="c" varStatus="vs">
											<c:if test="${c.examCourseType==0 }">
												<tr>
													<td><input type='checkbox' name='c_id'
														value='${c.resourceid }' /></td>
													<td>${c.course.courseName }</td>
													<td style="text-align: center;">${ghfn:dictCode2Val('CodeCourseExamType',c.course.examType)}</td>
													<td style="text-align: center;"><fmt:formatDate
															value="${c.examStartTime }"
															pattern="yyyy年MM月dd日 HH:mm:ss" />&nbsp;-&nbsp; <fmt:formatDate
															value="${c.examEndTime }" pattern="HH:mm:ss" /></td>
													<td style="text-align: center;" nowrap="nowrap">卷面成绩：<fmt:formatNumber
															value="${c.studyScorePer }" pattern="#" />% 网上成绩：<fmt:formatNumber
															value="${c.netsidestudyScorePer }" pattern="#" />% <%-- 网上学习：<fmt:formatNumber value="${c.facestudyScorePer3 }" pattern="#"/>% --%>
													</td>
													<td style="text-align: center;">${c.memo }</td>
													<td style="text-align: center;">${ghfn:dictCode2Val('CodeExamInfoCourseType',c.examCourseType)}</td>
												</tr>
											</c:if>
										</c:forEach>
									</tbody>
								</table>
							</div>
							<div>
								<table class="form" id="examinfoTab">
									<thead>
										<tr>
											<th colspan="7"><span class="buttonActive"><div
														class="buttonContent">
														<button type="button" onclick="delExaminfoTabwangsou('')">删除</button>
													</div></span> <span class="buttonActive"><div
														class="buttonContent">
														<button type="button"
															onclick="editExaminfoForNetCourseTabmianshou('examSub_examInfoBody2')">编辑考试课程</button>
													</div></span> <span class="buttonActive"><div
														class="buttonContent">
														<button type="button"
															onclick="addExaminfoForNetCourseTab()">新增考试课程</button>
													</div></span> <span class="buttonActive"><div
														class="buttonContent">
														<button type="button"
															onclick="examInfoConfingShowPage('1','examSub_examInfoBody2','3');">设置成绩比例</button>
													</div></span></th>
										</tr>
										<tr>
											<th style="width: 3%"><strong><input
													type="checkbox" name="checkall" id="check_all_examinfo"
													onclick="checkboxAll('#check_all_examinfo','c_id','#examSub_examInfoBody2')" /></strong></th>
											<th style="width: 15%"><strong>课程名称</strong></th>
											<th style="width: 6%; text-align: center;"><strong>考试形式</strong></th>
											<th style="width: 20%; text-align: center;"><strong>考试时间</strong></th>
											<th style="width: 20%; text-align: center;">成绩比例</th>
											<th style="width: 10%; text-align: center;"><strong>备注</strong></th>
											<th style="width: 10%; text-align: center;"><strong>考试课程类型</strong></th>
										</tr>
									</thead>
									<tbody id="examSub_examInfoBody2">
										<c:forEach items="${examSub.examInfo }" var="c" varStatus="vs">
											<c:if test="${c.examCourseType==1 }">
												<tr>
													<td><input type='checkbox' name='c_id'
														value='${c.resourceid }' /></td>
													<td>${c.course.courseName }</td>
													<td style="text-align: center;">${ghfn:dictCode2Val('CodeCourseExamType',c.course.examType)}</td>
													<td style="text-align: center;"><fmt:formatDate
															value="${c.examStartTime }"
															pattern="yyyy年MM月dd日 HH:mm:ss" />&nbsp;-&nbsp; <fmt:formatDate
															value="${c.examEndTime }" pattern="HH:mm:ss" /></td>
													<td style="text-align: center;" nowrap="nowrap">卷面：<fmt:formatNumber
															value="${c.facestudyScorePer }" pattern="#" />% 平时考核：<fmt:formatNumber
															value="${c.facestudyScorePer2 }" pattern="#" />%
													</td>
													<td style="text-align: center;">${c.memo }</td>
													<td style="text-align: center;">${ghfn:dictCode2Val('CodeExamInfoCourseType',c.examCourseType)}</td>
												</tr>
											</c:if>
										</c:forEach>
									</tbody>
								</table>
							</div>
							<%-- </c:forEach> --%>
						</div>
						<div class="tabsFooter">
							<div class="tabsFooterContent"></div>
						</div>
					</div>
				</div>
				<div class="formBar">
					<ul>
						<li><div class="buttonActive">
								<div class="buttonContent">
									<button type="button" onclick="checkForm()">提交</button>
								</div>
							</div></li>
						<li><div class="button">
								<div class="buttonContent">
									<button type="button" class="close"
										onclick="navTab.closeCurrentTab();">取消</button>
								</div>
							</div></li>
					</ul>
				</div>
			</form>
		</div>
	</div>
</body>
</html>

