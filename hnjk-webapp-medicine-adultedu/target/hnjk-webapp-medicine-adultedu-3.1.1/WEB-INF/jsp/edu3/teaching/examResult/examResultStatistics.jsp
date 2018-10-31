<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>考试统计</title>

</head>
<body>
	<div class="page">
		<div class="pageHeader">
			<form id="examResultStatisticsForm"
				onsubmit="return submitValidate(this);"
				action="${baseUrl}/edu3/teaching/result/statistics.html"
				method="post">
				<%-- <input id="examResultStatisticsSearchForm_pageNum"    type="hidden" name="pageNum"    value="${page.pageNum}"/>--%>
				<%/* <input type="hidden" name="pageNum" value="${page.pageNum }"/>*/%>
				<div class="searchBar">
					<ul class="searchContent">
						<li><label>教学站:</label> <%/*<gh:brSchoolAutocomplete name="branchSchool" tabindex="1" id="examResultStatistics_brSchoolName" displayType="code" defaultValue="${condition['branchSchool']}"/><font color="red">*</font>>*/%>
							<select class="flexselect "
							id="examResultStatistics_brSchoolName" name="branchSchool"
							tabindex=1 style="width: 55%" onchange="erm_brschool_Major()">${unitOption}</select>
						</li>
						<li><label>考试批次：</label> <gh:selectModel
								id="examResultStatistics_ExamSub" name="examSubId"
								bindValue="resourceid" displayValue="batchName"
								style="width:55%"
								modelClass="com.hnjk.edu.teaching.model.ExamSub"
								value="${condition['examSubId']}"
								condition="isDeleted='0' and batchType='exam' and examType='N'"
								orderBy="examinputStartTime desc" /> <font color="red">*</font>
						</li>
					</ul>
					<ul class="searchContent">
						<li><label>课程：</label> <gh:courseAutocomplete
								name="courseId" tabindex="1" id="examResultStatistics_courseId"
								value="${condition['courseId']}" displayType="code"
								isFilterTeacher="Y"
								taskCondition="yearInfo.resourceid='${examSub.yearInfo.resourceid }',term='${examSub.term }'"
								style="width:55%" /></li>


						<li><label>年级：</label>
						<gh:selectModel id="examResultStatistics_gradeid" name="gradeid"
								bindValue="resourceid" displayValue="gradeName"
								style="width:55%" modelClass="com.hnjk.edu.basedata.model.Grade"
								orderBy="gradeName desc" value="${condition['gradeid']}" /></li>
						<li><label>层次：</label> <gh:selectModel
								id="examResultStatistics_classic" name="classic"
								bindValue="resourceid" displayValue="classicName"
								style="width:55%"
								modelClass="com.hnjk.edu.basedata.model.Classic"
								value="${condition['classic']}" /></li>
					</ul>
					<ul class="searchContent">
						<li><label>专业：</label> <%/*<gh:selectModel id="examResultStatistics_major" name="major" bindValue="resourceid" displayValue="majorCodeName" style="width:55%" 
							modelClass="com.hnjk.edu.basedata.model.Major" value="${condition['major']}" condition="isAdult='Y'" orderBy="majorCode" /> */%>
							<select class="flexselect" id=examResultStatistics_major
							name="major" tabindex=1 style="width: 55%">${majorOption}</select>

						</li>
						<li><label>班级:</label> <gh:classesAutocomplete name="classId"
								id="examResultManager_list2_classid" tabindex="1"
								displayType="code" defaultValue="${condition['classId']}"
								style="width:55%"></gh:classesAutocomplete></li>

						<div class="buttonActive" style="float: right">
							<div class="buttonContent">
								<button type="submit">查 询</button>
							</div>
						</div>
					</ul>
				</div>
			</form>
		</div>
		<div class="pageContent">
			<table class="table" layouth="138" width="100%">
				<thead>
					<tr>
						<th width="7%" style="text-align: center; vertical-align: middle;">考试批次</th>
						<th width="8%" style="text-align: center; vertical-align: middle;">教学站</th>
						<th width="6%" style="text-align: center; vertical-align: middle;">年级</th>
						<th width="6%" style="text-align: center; vertical-align: middle;">专业</th>
						<th width="6%" style="text-align: center; vertical-align: middle;">层次</th>
						<th width="6%" style="text-align: center; vertical-align: middle;">形式</th>
						<th width="12%"
							style="text-align: center; vertical-align: middle;">班级</th>
						<th width="12%"
							style="text-align: center; vertical-align: middle;">班主任</th>
						<th width="12%"
							style="text-align: center; vertical-align: middle;">课程</th>
						<th width="10%"
							style="text-align: center; vertical-align: middle;">课程编码</th>
					</tr>
				</thead>
				<tbody id="examResultStatisticsBody">
					<c:forEach items="${page.result}" var="results" varStatus="vs">
						<tr>
							<td style="text-align: center; vertical-align: middle;">
								${results['batchname'] }</td>
							<td style="text-align: center; vertical-align: middle;">
								${results['unitname'] }</td>
							<td style="text-align: center; vertical-align: middle;">
								${results['gradename'] }</td>
							<td style="text-align: center; vertical-align: middle;">
								${results['majorname'] }</td>
							<td style="text-align: center; vertical-align: middle;">
								${results['classicname'] }</td>
							<td style="text-align: center; vertical-align: middle;">
								${ghfn:dictCode2Val('CodeTeachingType',results['schooltype'])}</td>
							<td style="text-align: center; vertical-align: middle;">
								${results['classesname'] }</td>
							<td style="text-align: center; vertical-align: middle;">
								${results['classesmaster'] }</td>
							<td style="text-align: center; vertical-align: middle;">
								${results['coursename'] }</td>
							<td style="text-align: center; vertical-align: middle;">
								${results['coursecode'] }</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<gh:page page="${page}" targetType="navTab"
				goPageUrl="${baseUrl }/edu3/teaching/result/statistics.html"
				pageType="sys" condition="${condition }" />
		</div>
	</div>
	<script type="text/javascript">
	$(document).ready(function(){		
		//$('._printExamResultsReviewForFaceCourse').poshytip({className: 'tip-yellowsimple',showTimeout: 1,alignTo: 'target',	alignX: 'left',	alignY: 'center',	offsetX: 5,allowTipHover: false});
	});

	//成绩审核
	function examResultsAuditManagerForFaceTeachType(brSchool,grade,classic,major,courseId,courseName,teachType,operatType){
		
		var examSubId     = "${condition['examSubId']}";
		var examSubStatus ="${examSub.examsubStatus}";
		if(parseInt(examSubStatus) <= 2){
			alertMsg.warn('该考试预约批次未关闭，不允许审核成绩！');
		}else{
			if("all"==operatType){
				alertMsg.confirm("确认要将《<font color='red'>"+courseName+"</font>》所有成绩记录审核通过吗？", {
					okCall: function(){
						var url = "${baseUrl}/edu3/teaching/result/examresults-audit-pass.html?operatingType=all&examSubId="+examSubId+"&courseId="+courseId;
						url    += "&teachType="+teachType+"&branchSchool="+brSchool+"&gradeid="+grade+"&major="+major+"&classic="+classic;
						$.ajax({
							type:'POST',
							url:url,
							data:{},
							dataType:"json",
							cache: false,
							error: DWZ.ajaxError,
							success: function(resultData){
								var success  = resultData['success'];
								var msg      = resultData['msg'];
								if(success==false){
									alertMsg.warn(msg);
								}else{
									alertMsg.info(msg);
									navTabPageBreak();
									//navTab.reload($("#examResultStatisticsSearchForm").attr("action"), $("#examResultStatisticsSearchForm").serializeArray());
								}
							}
						});
					}
				});
			}else{
				var url = "${baseUrl}/edu3/teaching/result/audit-examresults-list.html?operatingType=single&examSubId="+examSubId+"&branchSchool="+brSchool;
				url    += "&gradeid="+grade+"&classic="+classic+"&major="+major+"&courseId="+courseId+"&teachType="+teachType;
				navTab.openTab("RES_TEACHING_RESULT_MANAGE_AUDIT_FORFACTCOURSE_LIST",url,courseName+"成绩审核");	
			}
			
		}
			
	}
	//打印、导出复审后的总评成绩
	function printExamResultsReviewForFaceCourse(brSchool,grade,classic,major,courseId,courseName,teachType,opreatingType){
		var examSubStatus     ="${examSub.examsubStatus}";
		var examSubId         = "${examSub.resourceid}";
		var url               = "${baseUrl}/edu3/teaching/result/examresultsreviewForFaceCourse-print-view.html";
		if("export"==opreatingType||"exportAll"==opreatingType){
			url    =  "${baseUrl}/edu3/teaching/result/examresultsreviewForFaceCourse-print.html";
		}
		url += "?examSubId=${examSub.resourceid}&courseId="+courseId+"&printType="+opreatingType;
		url   			 += "&teachType="+teachType+"&branchSchool="+brSchool+"&gradeid="+grade+"&major="+major+"&classic="+classic;
		
		if(parseInt(examSubStatus) <= 2&&"printAll"!=opreatingType&&"exportAll"!=opreatingType){
			alertMsg.warn('该考试预约批次未关闭，不允许打印总评成绩！');
		}else{
			if("export"==opreatingType||"exportAll"==opreatingType){
				alertMsg.confirm("确定导出《"+courseName+"》的总评成绩单？", {
					okCall: function(){
						downloadFileByIframe(url,'exportCourseExamResultsReviewForDownloadExportIframe');						
					}
				});
			}else{
				$.pdialog.open(url,"RES_TEACHING_RESULT_REVIEW_FOR_FACECOURSE","打印"+courseName+"总评成绩",{width:800, height:600});	
			}
			
		}
	}
	
	
	//发布成绩
	function publishedExamResultsForFaceTeachType(brSchool,grade,classic,major,courseId,courseName,teachType){

		var examSubId         = "${examSub.resourceid}";
		var examSubStatus     = "${examSub.examsubStatus}";
		if(parseInt(examSubStatus) <= 2){
			alertMsg.warn('该考试预约批次未关闭，不允许发布成绩！');
		}else{
			alertMsg.confirm("确认要将《<font color='red'>"+courseName+"</font>》的成绩发布吗？", {
				okCall: function(){
					var url = "${baseUrl}/edu3/teaching/result/publish-examresults-save.html";
					$.ajax({
						type:'POST',
						url:url,
						data:{examSubId:examSubId,courseId:courseId,branchSchool:brSchool,gradeid:grade,major:major,classic:classic,teachType:teachType},
						dataType:"json",
						cache: false,
						error: DWZ.ajaxError,
						success: function(resultData){
							var success  = resultData['success'];
							var msg      = resultData['msg'];
							if(success==false){
								alertMsg.warn(msg);
							}else{
								alertMsg.info(msg);
								navTab.reload($("#examResultStatisticsSearchForm").attr("action"), $("#examResultStatisticsSearchForm").serializeArray());
							}
						}
					});
				}
			});
		}

	}
	
	function erm_brschool_Major(){
		var unitId = $("#examResultStatistics_brSchoolName").val();
		var majorId = $("#examResultStatistics_major").val();
		var url = "${baseUrl}/edu3/teaching/teachinggrade/brschool_Major.html";
		$.ajax({
			type:'POST',
			url:url,
			data:{unitId:unitId,majorId:majorId},
			dataType:"json",
			cache: false,
			error: DWZ.ajaxError,
			success: function(data){
				if(data['result'] == 300){
					if(undefined!=data['msg']){
						alertMsg.warn(data['msg']);
					}
			    }else{
			    	$("#examResultStatistics_major").replaceWith("<select  class=\"flexselect textInput\" id=examResultStatistics_major name=\"major\" tabindex=1 style=width:55% >"+data['majorOption']+"</select>");
			    	$("#examResultStatistics_major_flexselect").remove();
			    	$("#examResultStatistics_major_flexselect_dropdown").remove();
			    	$("#examResultStatistics_major").flexselect();
				}
			}
		});
		
	}
	function changeTeachingType(){
		var examResultStatistics_teachType = $("#examResultStatistics_teachType").val();
		navTab.openTab('RES_TEACHING_RESULT_MANAGE', '${baseUrl}/edu3/teaching/result/list.html?teachType='+examResultStatistics_teachType, '成绩审核');  
	}
	function submitValidate(obj){
		//var brSchoolName = $("#examResultStatistics_brSchoolName").val();
		var ExamSub = $("#examResultStatistics_ExamSub").val();
		if(''==ExamSub){
			alertMsg.warn("请在查询前填写必填项。");
			return false;
		}else{
			return navTabSearch(obj);
		}
	}
	

	/* 撤销成绩 */
	function examResultsAuditManagerCancel2(brSchool,grade,classic,major,courseId,courseName,teachType){
		var examSubId     = "${condition['examSubId']}";
		var examSubStatus ="${examSub.examsubStatus}";
		var teachingtype = "${condition['teachType'] }";
		//alert(examSubId);
		//alert(examSubStatus);
		//alert(teachingtype);
		
		alertMsg.confirm("确认要将《<font color='red'>"+courseName+"</font>》成绩撤销提交吗？", {
			okCall: function(){
				var url = "${baseUrl}/edu3/teaching/result/input-examresults-cancelsubmit2.html?examSubId="+examSubId+"&courseId="+courseId;
				url    += "&teachType="+teachType+"&branchSchool="+brSchool+"&gradeid="+grade+"&major="+major+"&classic="+classic;
				//alert(url);
				$.ajax({
					type:'POST',
					url:url,
					data:{},
					dataType:"json",
					cache: false,
					error: DWZ.ajaxError,
					success: function(resultData){
						var success  = resultData['success'];
						var msg      = resultData['msg'];
						if(success==false){
							alertMsg.warn(msg);
						}else{
							alertMsg.info(msg);
							navTab.reload($("#examResultStatisticsSearchForm").attr("action"), $("#examResultStatisticsSearchForm").serializeArray());
						}
					}
				});
			}
		})
	}
	
</script>
</body>
</html>