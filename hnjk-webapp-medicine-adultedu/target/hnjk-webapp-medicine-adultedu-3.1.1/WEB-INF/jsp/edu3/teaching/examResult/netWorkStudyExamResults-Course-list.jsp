<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>在线录入成绩</title>
</head>
<body>
	<script type="text/javascript">
	//导出可编辑成绩单(卷面)
	function exportExamResultsTranscriptsWS(examInfoId,courseName,isAbnormityEnd,examType){
		var isAllowInputExamResults = "${isAllowInputExamResults}";
		var isAbnormityInput        = "${isAbnormityInput}";
		var examSubId     		    = "${examSub.resourceid}";
		var unitId	                = "${condition['unitId']}";
		//批次状态允许录入时，录入异常结束可录入、异常录入人员可录入，主讲老师非开卷/闭卷考试可录入
		if("Y"==isAllowInputExamResults || "normal"==type){
			var url = "${baseUrl }/edu3/teaching/transcripts/download.html?flag=1&examInfoId="+examInfoId+"&examSubId="+examSubId;
			alertMsg.confirm("确定下载《"+courseName+"》的成绩单？", {
				okCall: function(){
					downloadFileByIframe(url,'examInfoListForDownloadExportIframe');
				}
			});			
		}else{
			alertMsg.warn("当前不是成绩录入时间，不允许导出成绩单！");
			return false;
		}
	}
	//导出可编辑成绩单(平时)
	function exportExamResultsTranscriptsUS(examInfoId,courseName,isAbnormityEnd,examType){
		var isAllowInputUsualExamResults = "${isAllowInputUsualExamResults}";
		var isAbnormityInput        = "${isAbnormityInput}";
		var examSubId     		    = "${examSub.resourceid}";
		var unitId	                = "${condition['unitId']}";
		//批次状态允许录入时，录入异常结束可录入、异常录入人员可录入，主讲老师非开卷/闭卷考试可录入
		if("Y"==isAllowInputUsualExamResults ){
			var url = "${baseUrl }/edu3/teaching/transcripts/download.html?flag=1&examInfoId="+examInfoId+"&examSubId="+examSubId+"&type=normal&unitId="+unitId;
			alertMsg.confirm("确定下载《"+courseName+"》的成绩单？", {
				okCall: function(){
					downloadFileByIframe(url,'examInfoListForDownloadExportIframe');
				}
			});			
		}else{
			alertMsg.warn("当前不是成绩录入时间，不允许导出成绩单！");
			return false;
		}
	}
	//导入卷面成绩
	function importExamResultsTranscripts(examInfoId,courseName,isAbnormityEnd,examType,isMixTrue){
		var isAllowInputExamResults = "${isAllowInputExamResults}";
		var isAbnormityInput        = "${isAbnormityInput}";
		var examSubId     		    = "${examSub.resourceid}";
		var title = "卷面成绩导入";
		if("Y"==isMixTrue){
			title = "笔考成绩导入";
		}
		if("Y"==isAllowInputExamResults){
			var url = "${baseUrl}/edu3/teaching/transcripts/upload-showpage.html?examSubId="+examSubId+"&examInfoId="+examInfoId+"&flag=newtWorkStudyCourse&isMixTrue="+isMixTrue;
			$.pdialog.open(url,"RES_TEACHING_RESULT_MANAGE_IMPORT_RESULT_UNCHECK",courseName+title,{width:900, height:640,mask:true});	
		}else{
			alertMsg.warn("考试批次未关闭，或者当前不是成绩录入时间，不允许导入成绩！");
			return false;
		}
	}
	//在线录入卷面成绩
	function inputExamResults(examInfoId,courseName,isAbnormityEnd,examType,examTypeName,isReadOnly){
		var isAllowInputExamResults = "${isAllowInputExamResults}";
		var examSubId     		    = "${examSub.resourceid}";
		var isAbnormityInput        = "${isAbnormityInput}";		
		//var isTeacher               = "${isTeacher}";
		var isTeacher               = "Y";
		var unitId	                = "${condition['unitId']}";
		if("Y"==isAllowInputExamResults||'1'==isReadOnly){
			var url = "${baseUrl}/edu3/teaching/result/input-examresults-list.html?examSubId="+examSubId+"&examInfoId="+examInfoId+"&flag=newtWorkStudyCourse&isReadOnly="+isReadOnly+"&branchSchool="+unitId;
			navTab.openTab("RES_TEACHING_RESULT_MANAGE_RESULT_INPUT_RESULT",url,courseName+"卷面成绩录入");	
		}else{
			alertMsg.warn("考试批次未关闭，或者当前不是成绩录入时间，不允许录入成绩！");
			return false;
		}	
				
	}
	//在线录入平时成绩
	function inputUsExamResults(examInfoId,courseName,isAbnormityEnd,examType,examTypeName,isReadOnly){
		var isAllowInputUsualExamResults = "${isAllowInputUsualExamResults}";
		var examSubId     		    = "${examSub.resourceid}";		
		var isTeacher               = "${isTeacher}";
		var unitId	                = "${condition['unitId']}";
		if("Y"==isAllowInputUsualExamResults||'1'==isReadOnly){
			var url = "${baseUrl}/edu3/teaching/result/input-examresults-list.html?examSubId="+examSubId+"&examInfoId="+examInfoId+"&flag=newtWorkStudyCourse&type=normal&isReadOnly="+isReadOnly+"&branchSchool="+unitId;
			navTab.openTab("RES_TEACHING_RESULT_MANAGE_RESULT_INPUT_RESULT_US",url,courseName+"平时成绩录入");	
		}else{
			alertMsg.warn("当前不是平时成绩录入时间，不允许录入成绩！");
			return false;
		}	
	}
	//导入平时成绩
	function importExamResultsWrittenScoreTranscripts(examInfoId,courseName,isAbnormityEnd,examType){
		var isAllowInputExamResults = "${isAllowInputExamResults}";
		var isAbnormityInput        = "${isAbnormityInput}";
		var examSubId     		    = "${examSub.resourceid}";
		var url = "${baseUrl}/edu3/teaching/transcripts/upload-showpage.html?examSubId="+examSubId+"&examInfoId="+examInfoId+"&flag=newtWorkStudyCourse&type=normal";
		$.pdialog.open(url,"RES_TEACHING_RESULT_MANAGE_IMPORT_RESULT_UNCHECK_WRITTERNSCORE",courseName+"平时成绩导入",{width:900, height:640,mask:true});
		
	}
	//提交平时成绩
	function submitUsExamResults(examInfoId,courseName,flag){		

		var hasPermission = "${isBrSchool}";
		var unitId	                = "${condition['unitId']}";
		var url = "${baseUrl}/edu3/teaching/result/input-usexamresults-submit.html";
		if("Y"==hasPermission){
			alertMsg.confirm("您确定要提交<font color='red'>《"+courseName+"》</font>的平时成绩到<font color='red'>考务办</font>吗？提交后将<font color='red'>不能修改、录入</font>此课程的成绩。", {
				okCall: function(){
					jQuery.post(url,{resourceid:examInfoId,flag:flag,unitId:unitId},function(returnData){	
						var msg 	   		= returnData['message'];
					  	var statusCode 		=  returnData['statusCode'];
					  	if(statusCode==200){
					  		alertMsg.info(msg);
					  		navTab.reload($("#netWorkStudyExamResultsSearchForm").attr("action"), $("#netWorkStudyExamResultsSearchForm").serializeArray());
						}else{
							alertMsg.warn(msg);
						}
					},"json");
				}
			});	
		}else{
			alertMsg.warn("您没有提交《"+courseName+"》成绩 的权限");
			return false;
		}
	}
	//打印成绩
	function printExamResults(examInfoId,courseName,isAbnormityEnd,examType,force){
		var isAllowInputExamResults = "${isAllowInputExamResults}";
		var examSubId     		    = "${examSub.resourceid}";
		var isAbnormityInput        = "${isAbnormityInput}";			
		var unitId	                = "${condition['unitId']}";
		if("Y"==isAllowInputExamResults||'Y'==force){
			var url = "${baseUrl}/edu3/teaching/result/examresults-print-view.html?examSubId="+examSubId+"&examInfoId="+examInfoId+"&flag=newtWorkStudyCourse&branchSchool="+unitId;
			$.pdialog.open(url, "RES_TEACHING_RESULT_MANAGE_RESULT_PRINT_RESULT", courseName+"成绩打印", {width:800,height:600});
		}else{
			alertMsg.warn("考试批次未关闭，或者当前不是成绩录入时间，不允许打印总评成绩！");
			return false;
		}
	}
	//打印平时成绩
	function printExamResultsUS(examInfoId,courseName,isAbnormityEnd,examType,force){
		var isAllowInputUsualExamResults = "${isAllowInputUsualExamResults}";
		var isAbnormityInput        = "${isAbnormityInput}";
		var examSubId     		    = "${examSub.resourceid}";
		var unitId	                = "${condition['unitId']}";
		//批次状态允许录入时，录入异常结束可录入、异常录入人员可录入，主讲老师非开卷/闭卷考试可录入
		/*
		if("Y"==isAllowInputUsualExamResults ){*/
			var url = "${baseUrl}/edu3/teaching/result/examresults-print-view.html?examSubId="+examSubId+"&examInfoId="+examInfoId+"&flag=newtWorkStudyCourse&branchSchool="+unitId+"&form=us";
			
			$.pdialog.open(url, "RES_TEACHING_RESULT_MANAGE_RESULT_PRINT_RESULT", courseName+"成绩打印", {width:800,height:600});	
		/*
		}else{
			alertMsg.warn("当前不是成绩录入时间，不允许导出成绩单！");
			return false;
		}*/
	}
	//导出成绩
	function exportExamResults(examInfoId,courseName,isAbnormityEnd,examType,force){
		var isAllowInputExamResults = "${isAllowInputExamResults}";
		var examSubId     		    = "${examSub.resourceid}";
		var isAbnormityInput        = "${isAbnormityInput}";		
		var unitId	                = "${condition['unitId']}";
		if("Y"==isAllowInputExamResults||'Y'==force){
			var url = "${baseUrl}/edu3/teaching/result/examresults-print.html?examSubId="+examSubId+"&examInfoId="+examInfoId+"&flag=newtWorkStudyCourse&operatingType=export&branchSchool="+unitId;
			alertMsg.confirm("确定导出《"+courseName+"》的总评成绩单？", {
				okCall: function(){
					downloadFileByIframe(url,'exportCourseExamResultsForDownloadExportIframe');
				}
			});		
		}else{
			alertMsg.warn("考试批次未关闭，或者当前不是成绩录入时间，不允许导出总评成绩！");
			return false;
		}
	}
	//设置异常成绩状态
	function abnormityStatus(flag,examType,examInfoId,courseName){
		
		var isAbnormityInput        = "${isAbnormityInput}";	
		var isTeacher               = "${isTeacher}";
		var url            		    = "${baseUrl}/edu3/teaching/result/abnormity-status.html";
		var msg                     = flag=="Y"?"提交":"取消提交"
		
		alertMsg.confirm("确定要"+msg+"《"+courseName+"》的异常成绩？", {
			okCall: function(){
				jQuery.post(url,{examInfoId:examInfoId,status:flag},function(returnData){	
					var msg 	   		= returnData['message'];
				  	var statusCode 		=  returnData['statusCode'];
				  	if(statusCode==200){
				  		alertMsg.info(msg);
				  		navTab.reload($("#netWorkStudyExamResultsSearchForm").attr("action"), $("#netWorkStudyExamResultsSearchForm").serializeArray());
					}else{
						alertMsg.warn(msg);
					}
				},"json");
			}
		});	
	}
	//提交成绩
	function submitExamResults(examInfoId,courseName,flag){		

		var isTeacher = "${isTeacher}";
		var isBrSchool  = "${isBrSchool}"; 
		var unitId	                = "${condition['unitId']}";
		var url = "${baseUrl}/edu3/teaching/result/input-examresults-submit.html";
		if("Y"!=isBrSchool){
			alertMsg.confirm("您确定要提交<font color='red'>《"+courseName+"》</font>的成绩到<font color='red'>考务办</font>吗？提交后将<font color='red'>不能修改、录入</font>此课程的成绩。", {
				okCall: function(){
					jQuery.post(url,{resourceid:examInfoId,flag:flag,unitId:unitId},function(returnData){	
						var msg 	   		= returnData['message'];
					  	var statusCode 		=  returnData['statusCode'];
					  	if(statusCode==200){
					  		alertMsg.info(msg);
					  		navTab.reload($("#netWorkStudyExamResultsSearchForm").attr("action"), $("#netWorkStudyExamResultsSearchForm").serializeArray());
						}else{
							alertMsg.warn(msg);
						}
					},"json");
				}
			});	
		}else{
			alertMsg.warn("您没有提交《"+courseName+"》成绩 的权限");
			return false;
		}
	}
</script>
	<div class="page">
		<div class="pageHeader">
			<c:choose>
				<c:when test="${type ne normal}">
					<form id="netWorkStudyExamResultsSearchForm"
						onsubmit="return navTabSearch(this);"
						action="${baseUrl}/edu3/teaching/examresults/networkstudy-course-list.html"
						method="post">
				</c:when>
				<c:when test="${type eq normal}">
					<form id="netWorkStudyExamResultsSearchForm"
						onsubmit="return navTabSearch(this);"
						action="${baseUrl}/edu3/teaching/examresults/networkstudy-course-list_us.html"
						method="post">
				</c:when>
			</c:choose>

			<input type="hidden" id="type" name="type"
				value="${condition['type']}" />
			<div class="searchBar">
				<ul class="searchContent">
					<li><label>考试批次：</label> <gh:selectModel
							id="netWorkStudyExamResults_ExamSub" name="examSubId"
							bindValue="resourceid" displayValue="batchName" style="width:55%"
							modelClass="com.hnjk.edu.teaching.model.ExamSub"
							value="${condition['examSubId']}" condition="batchType='exam'"
							orderBy="examinputStartTime desc" /> <font color="red">*</font></li>
					<li><label>课程：</label> <gh:courseAutocomplete name="courseId"
							tabindex="1" id="netWorkStudyExamResults_courseId"
							value="${condition['courseId']}" displayType="code"
							isFilterTeacher="Y"
							taskCondition="yearInfo.resourceid='${examSub.yearInfo.resourceid }',term='${examSub.term }'"
							style="width:55%" /></li>
					<li><label>考试编号：</label> <input
						id="netWorkStudyExamResults_examCourseCode" name="examCourseCode"
						value="${condition['examCourseCode'] }" style="width: 55%" /></li>
					<c:if test="${isadmin eq 'Y' }">
						<li><label>教学站：</label> <gh:brSchoolAutocomplete
								name="unitId" tabindex="2" id="teachingplanist_orgUnitId"
								displayType="code" defaultValue="${condition['unitId']}"
								style="width:53%" /></li>

					</c:if>
				</ul>
				<div class="subBar">
					<span style="line-height: 23px"> <font color="red"><c:choose>
								<c:when
									test="${condition['type'] eq 'normal' and timeStatus eq 1}">未到成绩录入时间</c:when>
								<c:when
									test="${condition['type'] ne 'normal' and timeStatus eq 2}">成绩录入时间已过</c:when>
							</c:choose></font> <font color="red"><c:choose>
								<c:when
									test="${condition['type'] eq 'normal' and timeStatus eq 1}">未到平时成绩录入时间</c:when>
								<c:when
									test="${condition['type'] eq 'normal' and timeStatus eq 2}">平时成绩录入时间已过</c:when>
							</c:choose></font>
					</span>
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
			<table class="table" layouth="111" width="100%">
				<thead>
					<tr>
						<!--  <th width="12%"  style="text-align: center;vertical-align: middle;">年度</th>-->
						<th width="6%" style="text-align: center; vertical-align: middle;">考试编号</th>
						<th width="14%"
							style="text-align: center; vertical-align: middle;">课程名称</th>
						<th width="6%" style="text-align: center; vertical-align: middle;">考核方式</th>
						<th width="6%" style="text-align: center; vertical-align: middle;">考试方式</th>
						<c:choose>
							<c:when test="${timeStatus eq 0}">
								<th width="6%"
									style="text-align: center; vertical-align: middle;">报考人数</th>
								<th width="8%"
									style="text-align: center; vertical-align: middle;">可编辑成绩数</th>
								<th width="8%" style="text-align: center;">导出可编辑成绩</th>
								<c:choose>
									<c:when test="${condition['type'] ne 'normal'}">
										<th width="8%" style="text-align: center;">导入卷面成绩</th>
										<th width="10%" style="text-align: center;"><c:choose>
												<c:when test="${isBrSchool ne 'Y'}">卷面成绩录入与提交</c:when>
												<c:otherwise>卷面成绩录入</c:otherwise>
											</c:choose></th>
										<%--<th width="12%"   style="text-align: center;">异常成绩状态</th> --%>
										<th width="10%" style="text-align: center;">导出与打印总评成绩</th>
									</c:when>
									<c:otherwise>
										<th width="8%" style="text-align: center;">导入平时成绩</th>
										<th width="10%" style="text-align: center;"><c:choose>
												<c:when test="${isBrSchool eq 'Y'}">平时成绩录入与提交</c:when>
												<c:otherwise>平时成绩录入</c:otherwise>
											</c:choose></th>
										<th width="10%" style="text-align: center;">打印平时成绩</th>
									</c:otherwise>
								</c:choose>
								<th width="4%" style="text-align: center;">查询</th>
							</c:when>
							<c:when test="${timeStatus eq 2 }">
								<th width="8%" style="text-align: center;">查询</th>
								<th width="10%" style="text-align: center;">导出与打印总评成绩</th>
							</c:when>
						</c:choose>
					</tr>
				</thead>
				<tbody id="netWorkStudyExamResultsBody">
					<c:forEach items="${page.result}" var="examInfo" varStatus="vs">
						<tr>
							<!--  <td  style="text-align: center;vertical-align: middle;" title="${examSub.yearInfo.yearName }${ghfn:dictCode2Val('CodeTermType',examSub.term)}">
			        		${examSub.yearInfo.yearName }${ghfn:dictCode2Val('CodeTermType',examSub.term)}
			        	</td>-->
							<td style="text-align: center; vertical-align: middle;">${examInfo.examCourseCode}</td>
							<td style="text-align: center; vertical-align: middle;">${examInfo.courseName}</td>
							<td style="text-align: center; vertical-align: middle;">统考</td>
							<td style="text-align: center; vertical-align: middle;"><c:choose>
									<c:when test="${examInfo.isMachineexam eq 'Y' }">机考</c:when>
									<c:otherwise>${ghfn:dictCode2Val('CodeCourseExamType',examInfo.examType)}</c:otherwise>
								</c:choose></td>
							<c:choose>
								<c:when test="${timeStatus eq 0}">
									<td style="text-align: center; vertical-align: middle;">${examInfo.orderNumber}</td>
									<td style="text-align: center; vertical-align: middle;"><c:choose>
											<c:when test="${examInfo.memo eq 0}">
												<font color="red" style="line-height: 21px;">${examInfo.memo}</font>
											</c:when>
											<c:otherwise>${examInfo.memo}</c:otherwise>
										</c:choose></td>
									<td style="text-align: center; vertical-align: middle;"><c:if
											test="${examInfo.memo ne 0}">
											<c:choose>
												<c:when test="${condition['type'] eq 'normal'}">
													<c:if test="${timeStatus eq 0 }">
														<a href="javaScript:void(0)"
															onclick="exportExamResultsTranscriptsUS('${examInfo.examInfoResourceId}','${examInfo.courseName}','${examInfo.isAbnormityEnd}','${examInfo.examType}')">导出</a>
													</c:if>
												</c:when>
												<c:otherwise>
													<a href="javaScript:void(0)"
														onclick="exportExamResultsTranscriptsWS('${examInfo.examInfoResourceId}','${examInfo.courseName}','${examInfo.isAbnormityEnd}','${examInfo.examType}')">导出</a>
												</c:otherwise>
											</c:choose>
										</c:if></td>

									<c:choose>
										<c:when test="${condition['type'] ne 'normal'}">
											<td style="text-align: center; vertical-align: middle;">
												<c:if test="${examInfo.memo ne 0}">
													<a href="javaScript:void(0)"
														onclick="importExamResultsTranscripts('${examInfo.examInfoResourceId}','${examInfo.courseName}','${examInfo.isAbnormityEnd}','${examInfo.examType}','${examInfo.isMachineexam}')">导入</a>
												</c:if>
											</td>
											<td style="text-align: center; vertical-align: middle;">
												<c:if test="${examInfo.memo ne 0}">
													<a href="javaScript:void(0)"
														onclick="inputExamResults('${examInfo.examInfoResourceId}','${examInfo.courseName}','${examInfo.isAbnormityEnd}','${examInfo.examType}','${ghfn:dictCode2Val('CodeCourseExamType',examInfo.examType)}',0)">录入</a>
													<%-- <c:if test="${isTeacher eq 'Y' and examInfo.isAbnormityEnd eq 'Y'}">--%>
													<c:if test="${isBrSchool ne 'Y' }">
			           			/ <a href="javaScript:void(0)"
															onclick="submitExamResults('${examInfo.examInfoResourceId}','${examInfo.courseName}','Y')"
															title="提交${examInfo.courseName}的成绩到考务办">提交</a>
													</c:if>
												</c:if>
											</td>
											<%-- 
			            <td style="text-align: center;vertical-align: middle;">
			            	<c:choose>
			            		<c:when test="${examInfo.isAbnormityEnd eq 'Y'}"><font color='green'>已提交</font></c:when>
			            		<c:otherwise><font color='red'>未提交</font></c:otherwise>
			            	</c:choose>
			            
			            	<c:if test="${(isAbnormityInput eq 'Y' and (examInfo.examType ==0 or  examInfo.examType ==1)) or (isTeacher eq 'Y' and  (examInfo.examType ==2 or  examInfo.examType ==3 or examInfo.examType ==6 ))}">
			            		(<a href="javaScript:void(0)"  onclick="abnormityStatus('Y','${examInfo.examType}','${examInfo.examInfoResourceId}','${examInfo.courseName}')" title="提交此课程的异常成绩状态，进入成绩录入环节">提交</a>/
			            		 <a href="javaScript:void(0)"  onclick="abnormityStatus('N','${examInfo.examType}','${examInfo.examInfoResourceId}','${examInfo.courseName}')" title="取消此课程已提交的异常成绩状态，继续录入异常成绩">取消提交</a>)
			            	</c:if>
			            	
			            </td>
			            --%>
											<td style="text-align: center; vertical-align: middle;">
												<a href="javaScript:void(0)"
												onclick="printExamResults('${examInfo.examInfoResourceId}','${examInfo.courseName}','${examInfo.isAbnormityEnd}','${examInfo.examType}','N')">查看打印</a>
												<a href="javaScript:void(0)"
												onclick="exportExamResults('${examInfo.examInfoResourceId}','${examInfo.courseName}','${examInfo.isAbnormityEnd}','${examInfo.examType}','N')">导出</a>
											</td>
										</c:when>
										<c:otherwise>
											<td style="text-align: center; vertical-align: middle;">
												<c:if test="${examInfo.memo ne 0 and timeStatus eq 0}">
													<a href="javaScript:void(0)"
														onclick="importExamResultsWrittenScoreTranscripts('${examInfo.examInfoResourceId}','${examInfo.courseName}','${examInfo.isAbnormityEnd}','${examInfo.examType}')">导入</a>
												</c:if>
											</td>
											<td style="text-align: center; vertical-align: middle;">
												<c:if test="${examInfo.memo ne 0 and timeStatus eq 0}">
													<a href="javaScript:void(0)"
														onclick="inputUsExamResults('${examInfo.examInfoResourceId}','${examInfo.courseName}','${examInfo.isAbnormityEnd}','${examInfo.examType}','${ghfn:dictCode2Val('CodeCourseExamType',examInfo.examType)}',0)">录入</a>
													<c:if test="${isBrSchool eq 'Y' }">
														<a href="javaScript:void(0)"
															onclick="submitUsExamResults('${examInfo.examInfoResourceId}','${examInfo.courseName}','Y')"
															title="提交${examInfo.courseName}的成绩到考务办">提交</a>
													</c:if>
												</c:if>
											</td>
											<td style="text-align: center; vertical-align: middle;">
												<a href="javaScript:void(0)"
												onclick="printExamResultsUS('${examInfo.examInfoResourceId}','${examInfo.courseName}','${examInfo.isAbnormityEnd}','${examInfo.examType}','N')">打印</a>
											</td>
										</c:otherwise>
									</c:choose>

									<td style="text-align: center; vertical-align: middle;"><c:choose>
											<c:when test="${condition['type'] ne 'normal'}">
												<a href="javaScript:void(0)"
													onclick="inputExamResults('${examInfo.examInfoResourceId}','${examInfo.courseName}','${examInfo.isAbnormityEnd}','${examInfo.examType}','${ghfn:dictCode2Val('CodeCourseExamType',examInfo.examType)}','1')">查询</a>
											</c:when>
											<c:otherwise>
												<a href="javaScript:void(0)"
													onclick="inputUsExamResults('${examInfo.examInfoResourceId}','${examInfo.courseName}','${examInfo.isAbnormityEnd}','${examInfo.examType}','${ghfn:dictCode2Val('CodeCourseExamType',examInfo.examType)}','1')">查询</a>
											</c:otherwise>
										</c:choose></td>
								</c:when>
								<c:when test="${timeStatus eq 2 }">
									<td style="text-align: center; vertical-align: middle;"><c:choose>
											<c:when test="${condition['type'] ne 'normal'}">
												<a href="javaScript:void(0)"
													onclick="inputExamResults('${examInfo.examInfoResourceId}','${examInfo.courseName}','${examInfo.isAbnormityEnd}','${examInfo.examType}','${ghfn:dictCode2Val('CodeCourseExamType',examInfo.examType)}','1')">查询</a>
											</c:when>
											<c:otherwise>
												<a href="javaScript:void(0)"
													onclick="inputUsExamResults('${examInfo.examInfoResourceId}','${examInfo.courseName}','${examInfo.isAbnormityEnd}','${examInfo.examType}','${ghfn:dictCode2Val('CodeCourseExamType',examInfo.examType)}','1')">查询</a>
											</c:otherwise>
										</c:choose></td>
									<td style="text-align: center; vertical-align: middle;"><a
										href="javaScript:void(0)"
										onclick="printExamResults('${examInfo.examInfoResourceId}','${examInfo.courseName}','${examInfo.isAbnormityEnd}','${examInfo.examType}','Y')">查看打印</a>
										<a href="javaScript:void(0)"
										onclick="exportExamResults('${examInfo.examInfoResourceId}','${examInfo.courseName}','${examInfo.isAbnormityEnd}','${examInfo.examType}','Y')">导出</a>
									</td>
								</c:when>
							</c:choose>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<c:choose>
				<c:when test="${type ne 'normal'}">
					<gh:page page="${page}" targetType="navTab"
						goPageUrl="${baseUrl }/edu3/teaching/examresults/networkstudy-course-list.html"
						pageType="sys" condition="${condition }" />
				</c:when>
				<c:when test="${type eq 'normal'}">
					<gh:page page="${page}" targetType="navTab"
						goPageUrl="${baseUrl }/edu3/teaching/examresults/networkstudy-course-list_us.html"
						pageType="sys" condition="${condition }" />
				</c:when>
			</c:choose>
		</div>
	</div>
</body>
</html>