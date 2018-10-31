<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>补考成绩管理</title>
	<style>
		th,td {
			text-align: center;
		}
	</style>
<script type="text/javascript">

	//打开页面或者点击查询（即加载页面执行）
	function examResultsManagerQueryBegin() {
		var defaultValue = "${condition['branchSchool_makeup']}";
		var schoolId = "";
		var isBrschool = "${isBrschool}";
		if(isBrschool==true || isBrschool=="true"){
			schoolId = defaultValue;
		}
		var gradeId = "${condition['gradeid_makeup']}";
		var classicId = "${condition['classic_makeup']}";
		
		var majorId = "${condition['major_makeup']}";
		var classesId = "${condition['classId_makeup']}";
		var selectIdsJson = "{unitId:'examResultsManagerForFaceMakeup_brSchoolName',gradeId:'examResultsManagerForFaceMakeup_gradeid',classicId:'examResultsManager_makeupForFaceTeachType_classic',majorId:'examResultsManagerForFaceMakeup_major',classesId:'classId'}";
		cascadeQuery("begin", defaultValue, schoolId, gradeId, classicId,"", majorId, classesId, selectIdsJson);
	}

	// 选择教学点
	function examResultsManagerQueryUnit() {
		var defaultValue = $("#examResultsManagerForFaceMakeup_brSchoolName").val();
		var selectIdsJson = "{gradeId:'examResultsManagerForFaceMakeup_gradeid',classicId:'examResultsManager_makeupForFaceTeachType_classic',majorId:'examResultsManagerForFaceMakeup_major',classesId:'classId'}";
		cascadeQuery("unit", defaultValue, "", "", "", "", "", "",selectIdsJson);
	}

	// 选择年级
	function examResultsManagerQueryGrade() {
		var defaultValue = $("#examResultsManagerForFaceMakeup_brSchoolName").val();
		var gradeId = $("#examResultsManagerForFaceMakeup_gradeid").val();
		var selectIdsJson = "{classicId:'examResultsManager_makeupForFaceTeachType_classic',teachingType:'id~teachingType',majorId:'examResultsManagerForFaceMakeup_major',classesId:'classId'}";
		cascadeQuery("grade", defaultValue, "", gradeId, "", "", "", "",selectIdsJson);
	}

	// 选择层次
	function examResultsManagerQueryClassic() {
		var defaultValue = $("#examResultsManagerForFaceMakeup_brSchoolName").val();
		var gradeId = $("#examResultsManagerForFaceMakeup_gradeid").val();
		var classicId = $("#examResultsManager_makeupForFaceTeachType_classic").val();
		var selectIdsJson = "{majorId:'examResultsManagerForFaceMakeup_major',classesId:'classId'}";
		cascadeQuery("classic", defaultValue, "", gradeId, classicId, "", "", "", selectIdsJson);
	}

	// 选择专业
	function examResultsManagerQueryMajor() {
		var defaultValue = $("#examResultsManagerForFaceMakeup_brSchoolName").val();
		var gradeId = $("#examResultsManagerForFaceMakeup_gradeid").val();
		var classicId = $("#examResultsManager_makeupForFaceTeachType_classic").val();

		var majorId = $("#examResultsManagerForFaceMakeup_major").val();
		var selectIdsJson = "{classesId:'classId'}";
		cascadeQuery("major", defaultValue, "", gradeId, classicId,"", majorId, "", selectIdsJson);
	}
	
	function ajaxFreshInMakeupExamByGrade(){
		var grade = $("#examResultsManagerForFaceMakeup_gradeid").val();
		if(''==grade){
			grade = "${condition['gradeId']}";
		}
		
		var brschool = $("#examResultsManagerForFaceMakeup_brSchoolName").val();
		if(''==brschool || brschool==null){
			brschool = "${condition['branchSchool']}";
		}
		
		var major_id = $("#examResultsManagerForFaceMakeup_major").val();
		if(''==major_id){
			major_id = "${condition['major']}";
		}
		
		var url = "${baseUrl}/edu3/teaching/result/ajaxRefreshClassesMakeup.html";
		$.ajax({
			type:'post',
			url:url,
			data:{grade:grade,brschool:brschool,major:major_id},
			dataType:"json",
			cache:false,
			error:DWZ.ajaxError,
			success:function(data){
				if(data['result']==300){
					alertMsg.error(data['msg']);
				}else{
					$("#examResults_makeup_classesid").html(data['classes']);
				}
			}
		}); 
		
		url = "${baseUrl}/edu3/teaching/result/ajaxRefreshMajorsMakeup.html";
		$.ajax({
			type:'post',
			url:url,
			data:{grade:grade,brschool:brschool},
			dataType:"json",
			cache:false,
			error:DWZ.ajaxError,
			success:function(data){
				if(data['result']==300){
					alertMsg.error(data['msg']);
				}else{
					$("#examResultsManagerForFaceMakeup_major").html(data['majorList']);
				}
			}
		}); 
	} 
	
	function ajaxFreshInMakeupExamByMajor(){
		var grade = $("#examResultsManagerForFaceMakeup_gradeid").val();
		if(''==grade){
			grade = "${condition['gradeId']}";
		}
		
		var brschool = $("#examResultsManagerForFaceMakeup_brSchoolName").val();
		if(''==brschool || brschool==null){
			brschool = "${condition['branchSchool']}";
		}
		
		var major_id = $("#examResultsManagerForFaceMakeup_major").val();
		if(''==major_id){
			major_id = "${condition['major']}";
		}
		var url = "${baseUrl}/edu3/teaching/result/ajaxRefreshClassesMakeup.html";
		$.ajax({
			type:'post',
			url:url,
			data:{grade:grade,brschool:brschool,major:major_id},
			dataType:"json",
			cache:false,
			error:DWZ.ajaxError,
			success:function(data){
				if(data['result']==300){
					alertMsg.error(data['msg']);
				}else{
					$("#examResults_makeup_classesid").html(data['classes']);
				}
			}
		});
		
		url = "${baseUrl}/edu3/teaching/result/ajaxRefreshMajorsMakeup.html";
		$.ajax({
			type:'post',
			url:url,
			data:{grade:grade,brschool:brschool,major:major_id},
			dataType:"json",
			cache:false,
			error:DWZ.ajaxError,
			success:function(data){
				if(data['result']==300){
					alertMsg.error(data['msg']);
				}else{
					$("#examResultsManagerForFaceMakeup_major").html(data['majorList']);
				}
			}
		}); 
	}
	
	function ajaxFreshMajorInMakeupExamByBrschool(){
		var grade = $("#examResultsManagerForFaceMakeup_gradeid").val();
		if(''==grade){
			grade = "${condition['gradeId']}";
		}
		var brschool = $("#examResultsManagerForFaceMakeup_brSchoolName").val();
		if(''==brschool || brschool==null){
			brschool = "${condition['branchSchool']}";
		}
	
		var url = "${baseUrl}/edu3/teaching/result/ajaxRefreshClassesMakeup.html";
		$.ajax({
			type:'post',
			url:url,
			data:{grade:grade,brschool:brschool},
			dataType:"json",
			cache:false,
			error:DWZ.ajaxError,
			success:function(data){
				if(data['result']==300){
					alertMsg.error(data['msg']);
				}else{
					$("#examResults_makeup_classesid").html(data['classes']);
				}
			}
		});
		
		url = "${baseUrl}/edu3/teaching/result/ajaxRefreshMajorsMakeup.html";
		$.ajax({
			type:'post',
			url:url,
			data:{grade:grade,brschool:brschool},
			dataType:"json",
			cache:false,
			error:DWZ.ajaxError,
			success:function(data){
				if(data['result']==300){
					alertMsg.error(data['msg']);
				}else{
					$("#examResultsManagerForFaceMakeup_major").html(data['majorList']);
				}
			}
		}); 
		
	}
	/* 撤销成绩 */
	function examResultsAuditManagerCancel(examInfoResourceId,courseName,classId){
		alertMsg.confirm("确认要将《<font color='red'>"+courseName+"</font>》成绩撤销提交吗？", {
			okCall: function(){
				var url = "${baseUrl}/edu3/teaching/result/input-examresults-cancelsubmit.html?examInfoResourceId="+examInfoResourceId + "&courseName=" + encodeURI(courseName)+"&classId="+classId;
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
							navTab.reload($("#examResultsManagerForFaceMakeupSearchForm").attr("action"), $("#examResultsManagerForFaceMakeupSearchForm").serializeArray());
						}
					}
				});
			}
		});
	}
	//撤销已发布成绩
	function canaelAuditPublishedExamResultsForFaceMakeup(){
		var examSubId     = "${condition['examSubId_makeup']}";
		var examSubStatus ="${examSub.examsubStatus}";
		var brSchool     = "${condition['branchSchool']}";
		var teachType = "${condition['teachType']}";
		var url = "${baseUrl}/edu3/teaching/result/examresults-batchAudit.html?examSubId="+examSubId+"&branchSchool="+brSchool+"&teachType="+teachType+"&auditType=cancel";
		var resIds = "";
		var gradeIds = "";
		var classics = "";
		var majorIds = "";
		var classesIds = "";
		var courseIds = "";
		var k = 0;
		var num = $("#examResultsManagerForFaceMakeupBody input[name='resourceid']:checked").size();
		if(num<=0){
			alertMsg.confirm("请您至少选择一条记录进行操作！");
			return;
		}
		$("#examResultsManagerForFaceMakeupBody input[name='resourceid']:checked").each(function(){
			var checekObj = $(this);
			resIds += checekObj.val();
    		gradeIds += checekObj.attr("gradeid");
    		classics += checekObj.attr("classic");
    		majorIds += checekObj.attr("majorid");
    		classesIds += checekObj.attr("classesid");
			courseIds += checekObj.attr("courseid");
	        if(k != num -1 ) {
	        	resIds += ",";
	    		gradeIds += ",";
	    		classics += ",";
	    		majorIds += ",";
	    		classesIds += ",";
				courseIds += ",";
	        }
	        k++;
	    });
		url+="&type=checked";
		alertMsg.confirm("确认要将<font color='red'>"+num+"</font>条记录撤销吗？", {
			okCall: function(){
				$.ajax({
					type:'POST',
					url:url,
					data:{'resIds':resIds,'courseIds':courseIds,'gradeIds':gradeIds,'majorIds':majorIds,'classics':classics,'classesIds':classesIds},
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
						}
						navTab.reload($("#examResultsManagerForFaceMakeupSearchForm").attr("action"), $("#examResultsManagerForFaceMakeupSearchForm").serializeArray());
					}
				});
			}
		});
	}
	</script>
</head>
<body>
	<div class="page">
		<div class="pageHeader" style="height: 75px;">
			<form id="examResultsManagerForFaceMakeupSearchForm"
				onsubmit="return submitValidate(this);"
				action="${baseUrl}/edu3/teaching/result/makeup_list.html"
				method="post">
				<%-- <input id="examResultsManagerForFaceTeachTypeSearchForm_pageNum"    type="hidden" name="pageNum"    value="${page.pageNum}"/>--%>
				<%/* <input type="hidden" name="pageNum" value="${page.pageNum }"/>*/%>
				<div class="searchBar">
					<ul class="searchContent">
						<%-- <c:if test="${!isBrschool }"> --%>
							<li class="custom-li"><label>教学点：</label> <span
								sel-id="examResultsManagerForFaceMakeup_brSchoolName"
								sel-name="branchSchool_makeup"
								sel-onchange="examResultsManagerQueryUnit()"
								sel-classs="flexselect"></span> <font
								color="red">*</font></li>
						<%-- </c:if>
						<c:if test="${isBrschool}">
							<input type="hidden" name="branchSchool_makeup"
								id="examResultsManagerForFaceMakeup_brSchoolName"
								value="${condition['branchSchool_makeup']}" />
						</c:if> --%>
						<li><label>考试批次：</label> <gh:selectModel
								id="examResultsManager_ExamSub" name="examSubId_makeup"
								bindValue="resourceid" displayValue="batchName"
								style="width:135px;"
								modelClass="com.hnjk.edu.teaching.model.ExamSub"
								value="${condition['examSubId_makeup']}"
								condition="isDeleted=0 and batchType='exam' and examType<>'N'"
								orderBy="examinputStartTime desc" /></li>
						<li><label>年级：</label> <span
							sel-id="examResultsManagerForFaceMakeup_gradeid"
							sel-name="gradeid_makeup"
							sel-onchange="examResultsManagerQueryGrade()"
							sel-style="width: 120px"></span></li>
						<li><label>层次：</label> <span
							sel-id="examResultsManager_makeupForFaceTeachType_classic"
							sel-name="classic_makeup"
							sel-onchange="examResultsManagerQueryClassic()"
							sel-classs="flexselect" sel-style="width: 100px"></span></li>
					</ul>
					<ul class="searchContent">
						<li class="custom-li"><label>专业：</label> <span
							sel-id="examResultsManagerForFaceMakeup_major"
							sel-name="major_makeup"
							sel-onchange="examResultsManagerQueryMajor()"
							sel-classs="flexselect"></span></li>
						<li><label>课程：</label> <gh:courseAutocomplete
								name="courseId_makeup" tabindex="1"
								id="examResultsManager_makeupForFaceTeachType_courseId"
								value="${condition['courseId_makeup']}" displayType="code"
								isFilterTeacher="Y"
								taskCondition="yearInfo.resourceid='${examSub.yearInfo.resourceid }',term='${examSub.term }'"
								style="width:130px;" /></li>
						<li><label>成绩状态：</label>
						<gh:select id="examResultsCheckStatus_makeup"
								name="checkStatus_makeup"
								value="${condition['checkStatus_makeup'] }"
								dictionaryCode="CodeExamResultCheckStatus" style="width:120px;" />
						</li>
					</ul>

					<ul class="searchContent">
						<li class="custom-li"><label>班级：</label> <span sel-id="classId"
							sel-name="classId_makeup" sel-classs="flexselect"
							></span></li>
						<li>
							教学形式：
							<gh:select id="courseTeachType_makeup" name="courseTeachType"
									   value="${condition['courseTeachType'] }"
									   dictionaryCode="CodeCourseTeachType" style="width:120px;" />
						</li>
						<div class="buttonActive" style="float: right">
							<div class="buttonContent">
								<button type="submit">查 询</button>
							</div>
						</div>
					</ul>
				</div>
			</form>
		</div>
		<gh:resAuth parentCode="RES_TEACHING_RESULT_MANAGE_MAKEUP"
			pageType="list"></gh:resAuth>
		<div class="pageContent">
			<table class="table" layouth="160" width="100%">
				<thead>
					<tr>
						<%-- <th width="7%" style="text-align: center;vertical-align: middle;">年度</th>--%>
						<th width="3%"><input
							type="checkbox" name="checkall"
							id="check_all_examResultManagerMakeup"
							onclick="checkboxAll('#check_all_examResultManagerMakeup','resourceid','#examResultsManagerForFaceMakeupBody')" /></th>
						<th width="8%">教学站</th>
						<th width="5%">年级</th>
						<th width="15%">班级</th>
						<th width="12%"style="text-align: center; vertical-align: middle;">专业</th>
						<th width="10%">课程</th>
						<th width="5%">教学形式</th>
						<%--<th width="6%" >补考人数</th> --%>
						<th width="8%">考试批次</th>
						<th width="5%">实考人数</th>
						<th width="8%">保/提/发</th>
						<th width="8%">成绩审核</th>
					</tr>
				</thead>
				<tbody id="examResultsManagerForFaceMakeupBody">
					<c:forEach items="${page.result}" var="planCourse" varStatus="vs">
						<tr>
							<td><input
								type="checkbox" name="resourceid"
								value="${planCourse.examInfoResourceId}"
								gradeid="${planCourse.gradeId }"
								classic="${planCourse.classicId}"
								majorid="${planCourse.majorId}"
								classesid="${planCourse.classId}"
								courseid="${planCourse.courseId}"
								teachType="${planCourse.teachType }"
								examSubId="${planCourse.examSubId}" autocomplete="off" /></td>
							<td title="${planCourse.unitShortName }">${planCourse.unitShortName }</td>
							<td title="${planCourse.gradeName }">${planCourse.gradeName }</td>
							<td title="${planCourse.classesName}">${planCourse.classesName}</td>
							<td title="${planCourse.majorName}">${planCourse.majorName}</td>
							<td title="${planCourse.courseCode}-${planCourse.courseName}">
								<a href="javaScript:void(0)" style="color: blue"
								onclick="showOperateRecord('${planCourse.classId }','${planCourse.classesName}','${planCourse.courseId}','${planCourse.courseName}','${planCourse.examSubId }')">${planCourse.courseName}</a>
							</td>
							<td title="${planCourse.teachType}">${ghfn:dictCode2Val('CodeCourseTeachType',planCourse.teachType)}</td>
							<td title="${planCourse.batchName}">${planCourse.batchName}</td>
							<%--<td style="text-align: center;vertical-align: middle;">
	 							${planCourse.headExam}
				            </td>  --%>
							<td title="实际参加补考的人数，不包括缺考、缓考">
								${planCourse.realExam}
							</td>
							<td title="保存状态${planCourse.checkstatus_save}人；提交状态${planCourse.checkstatus_submit}人；发布状态${planCourse.checkstatus_publish}人">
									${planCourse.checkstatus_save}/${planCourse.checkstatus_submit}/${planCourse.checkstatus_publish}
									<%--<c:if test="${planCourse.checkstatus_save != 0}"><label style="color: blue">保存</label>(${planCourse.checkstatus_save});</c:if>
									<c:if test="${planCourse.checkstatus_submit != 0}"><label style="color: green">提交</label>(${planCourse.checkstatus_submit});</c:if>
									<c:if test="${planCourse.checkstatus_publish != 0}"><label style="color: green">发布</label>(${planCourse.checkstatus_publish})</c:if>--%>
							</td>
							<td><a
								href="javaScript:void(0)" title="个别审核"
								onclick="examResultsAuditManagerForFaceMakeup('${planCourse.branchSchool}','${planCourse.gradeId}','${planCourse.classId }','${planCourse.majorId}','${planCourse.courseId}','${planCourse.courseName}','${planCourse.teachType}','single','${planCourse.examSubId}','${planCourse.resourceid}')">单审</a>&nbsp;&nbsp;
								<a href="javaScript:void(0)" style="color: green" title="全部通过（不包括保存状态的成绩）"
								onclick="examResultsAuditManagerForFaceMakeup('${planCourse.branchSchool}','${planCourse.gradeId}','${planCourse.classId }','${planCourse.majorId}','${planCourse.courseId}','${planCourse.courseName}','${planCourse.teachType}','all','${planCourse.examSubId}','${planCourse.resourceid}')">通过</a>&nbsp;&nbsp;
								<a href="javaScript:void(0)" style="color: red" title="撤销提交"
								onclick="examResultsAuditManagerCancel('${planCourse.examInfoResourceId}',' ${planCourse.courseName}','${planCourse.classId }')">撤回</a>
							</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<gh:page page="${page}" targetType="navTab"
				goPageUrl="${baseUrl }/edu3/teaching/result/makeup_list.html"
				pageType="sys" condition="${condition }" />
		</div>
	</div>
	<script type="text/javascript">
	$(document).ready(function(){	
		examResultsManagerQueryBegin();
		//$('._printExamResultsReviewForFaceCourse').poshytip({className: 'tip-yellowsimple',showTimeout: 1,alignTo: 'target',	alignX: 'left',	alignY: 'center',	offsetX: 5,allowTipHover: false});
	});

	//成绩审核
	function examResultsAuditManagerForFaceMakeup(brSchool,grade,classId,major,courseId,courseName,teachType,operatType,examSubId,examInfoId){
		//alert(brSchool + "--"+ courseId + "--" + classId);
		//var examSubId     = "${condition['examSubId_makeup']}";
		var examSubStatus ="${examSub.examsubStatus}";
		if(parseInt(examSubStatus) <= 2){
			alertMsg.warn('该考试批次未关闭，不允许审核成绩！');
		}else{
			if("all"==operatType){
				alertMsg.confirm("确认要将《<font color='red'>"+courseName+"</font>》所有成绩记录审核通过吗？", {
					okCall: function(){
						var url = "${baseUrl}/edu3/teaching/result/examresults-audit-pass-makeup.html?operatingType=all&examSubId="+examSubId+"&courseId="+courseId;
						url    += "&branchSchool="+brSchool+"&gradeId="+grade+"&major="+major+"&classId="+classId+"&examInfoId="+examInfoId;
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
									//navTab.reload($("#examResultsManagerForFaceTeachTypeSearchForm").attr("action"), $("#examResultsManagerForFaceTeachTypeSearchForm").serializeArray());
								}
							}
						});
					}
				});
			}else{
				var url = "${baseUrl}/edu3/teaching/result/audit-examresults-makeup-list.html?operatingType=single&examSubId="+examSubId+"&branchSchool="+brSchool;
				url    += "&gradeId="+grade+"&classId="+classId+"&major="+major+"&courseId="+courseId+"&examInfoId="+examInfoId;
				navTab.openTab("RES_TEACHING_RESULT_MANAGE_AUDIT_FORFACTCOURSE_LIST",url,courseName+"成绩审核");	
			}
			
		}
			
	}
	
	//全部审核
	function makupResultsAuditOfAll(){
		var examSubId     = "${condition['examSubId_makeup']}";
		var examSubStatus ="${examSub.examsubStatus}";
		var brSchool     = "${condition['branchSchool_makeup']}";
		var gradeId = "${condition['gradeid_makeup']}";
		var classic = "${condition['classic_makeup']}";
		var majorId = "${condition['major_makeup']}";
		var courseId = "${condition['courseId_makeup']}";
		var classesId = "${condition['classId_makeup']}";
		//var teachType = "${condition['teachType']}";
		
		var checkStatus = $("#examResultsCheckStatus").val();
		if(parseInt(examSubStatus) <= 2){
			alertMsg.warn('该考试预约批次未关闭，不允许审核成绩！');
		}else{
			var url = "${baseUrl}/edu3/teaching/result/examresults-batchAudit-pass-makeup.html?examSubId="+examSubId+"&branchSchool="+brSchool+"&teachType=facestudy";
			var resIds = "";
			var gradeIds = "";
			var majorIds = "";
			var classesIds = "";
			var courseIds = "";
			var examSubIds = "";
			var k = 0;
			var num = $("#examResultsManagerForFaceMakeupBody input[name='resourceid']:checked").size();
			if(num>0){//按照勾选条件审核
				$("#examResultsManagerForFaceMakeupBody input[@name='resourceid']:checked").each(function(){
					var checekObj = $(this);
					resIds += checekObj.val();
		    		gradeIds += checekObj.attr("gradeid");
		    		majorIds += checekObj.attr("majorid");
		    		classesIds += checekObj.attr("classesid");
					courseIds += checekObj.attr("courseid");
					examSubIds += checekObj.attr("examSubId");
			        if(k != num -1 ) {
			        	resIds += ",";
			    		gradeIds += ",";
			    		majorIds += ",";
			    		classesIds += ",";
						courseIds += ",";
						examSubIds += ",";
			        }
			        k++;
			    });
				url += "&type=checked&resIds="+resIds+"&gradeIds="+gradeIds+"&majorIds="+majorIds+"&classesIds="+classesIds+"&courseIds="+courseIds+"&examSubIds="+examSubIds;
			}else{//按照查询条件审核
				num = "${page.totalCount}";
				if(checkStatus!="" && checkStatus!=undefined){
					url +="&checkStatus="+checkStatus;
				}
				url += "&type=query&gradeId="+gradeId+"&classic="+classic+"&majorId="+majorId+"&classesId="+classesId+"&courseId="+courseId;
				
			}
			alertMsg.confirm("确认要将《<font color='red'>"+num+"</font>》条记录审核通过吗？", {
				okCall: function(){
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
								//navTab.reload($("#examResultsManagerForFaceTeachTypeSearchForm").attr("action"), $("#examResultsManagerForFaceTeachTypeSearchForm").serializeArray());
							}
						}
					});
				}
			});
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
			alertMsg.warn('该考试批次未关闭，不允许打印总评成绩！');
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
	function publishedExamResultsForFaceMakeup(brSchool,grade,classic,major,courseId,courseName,teachType){

		var examSubId         = "${examSub.resourceid}";
		var examSubStatus     = "${examSub.examsubStatus}";
		if(parseInt(examSubStatus) <= 2){
			alertMsg.warn('该考试批次未关闭，不允许发布成绩！');
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
								navTab.reload($("#examResultsManagerForFaceMakeupSearchForm").attr("action"), $("#examResultsManagerForFaceMakeupSearchForm").serializeArray());
							}
						}
					});
				}
			});
		}
	}
	

	function changeTeachingType(){
		var examResultsManagerForFaceTeachType_teachType = $("#examResultsManagerForFaceTeachType_teachType").val();
		navTab.openTab('RES_TEACHING_RESULT_MANAGE', '${baseUrl}/edu3/teaching/result/makeup_list.html?teachType='+examResultsManagerForFaceTeachType_teachType, '成绩审核');  
	}
	function submitValidate(obj){
		//var teachType = $("#examResultsManagerForFaceTeachType_teachType").val();
		//var brSchoolName = $("#examResultsManagerForFaceMakeup_brSchoolName").val();
		//var ExamSub = $("#examResultsManagerForFaceMakeup_ExamSub").val();
		var brSchoolName = $("#examResultsManagerForFaceMakeup_brSchoolName").val();
		/* if(''==ExamSub){
			alertMsg.warn("请选择考试批次！");
			return false;
		}else */ 

		if(''==brSchoolName && "${schoolCode}"!='10560'){
			alertMsg.warn("请选择教学点！");
			return false;
		}else{
			return navTabSearch(obj);
		}
	}
	function showOperateRecord(classesid,classesname,courseId,courseName,examsubId){
		var url = "${baseUrl}/edu3/teaching/result/operateRecord.html?classesid="+classesid+"&courseId="+courseId+"&examsubId="+examsubId;
		$.pdialog.open(url,"RES_TEACHING_OPERATE_RECORD",classesname+"\t【"+courseName+"】\t课程操作记录",{width:800, height:600});	
	}
</script>
</body>
</html>