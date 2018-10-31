<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>选修课成绩</title>
<script type="text/javascript">
	$(document).ready(function(){
		electiveExamQueryBegin();
		//$("select[class*=flexselect]").flexselect();
	});

//打开页面或者点击查询（即加载页面执行）
 function electiveExamQueryBegin() {
	var defaultValue = "${condition['brSchoolId']}";
	var schoolId = "";
	var isBrschool = "${isBrschool}";
	if(isBrschool==true || isBrschool=="true"){
		schoolId = defaultValue;
	}
	var gradeId = "${condition['gradeId']}";
	var classicId = "${condition['classicId']}";
	var teachingType = "${condition['teachingType']}";
	var majorId = "${condition['majorId']}";
	var classesId = "${condition['classesId']}";
	var selectIdsJson = "{unitId:'electiveExam_school',gradeId:'electiveExam_grade',classicId:'electiveExam_classic',teachingType:'electiveExam_teachingType',majorId:'electiveExam_major',classesId:'electiveExam_classes'}";
	cascadeQuery("begin", defaultValue, schoolId, gradeId, classicId,teachingType, majorId, classesId, selectIdsJson);
}

</script>
</head>
<body>
	<div class="page">
		<div class="pageHeader">
			<form id="electiveExamForm" onsubmit="return validateTab(this);"
				method="post" action="${baseUrl }/edu3/teaching/examResult/electiveExam-list.html">
				<input name="fromPage" value="Y" type="hidden" />
				<div class="searchBar">
					<ul class="searchContent">
						<li class="custom-li"><label>教学点：</label> <span sel-id="electiveExam_school"
								sel-name="brSchoolId" sel-onchange="electiveExamQueryUnit()"
								sel-classs="flexselect"></span><font color="red">*</font></li>
						<li><label>考试批次：</label> <gh:selectModel
								id="electiveExam_ExamSub" name="examSubId"
								bindValue="resourceid" displayValue="batchName"
								style="width:120px" classCss="required"
								modelClass="com.hnjk.edu.teaching.model.ExamSub"
								value="${condition['examSubId']}"
								onchange="getCourseInfo()"
								condition="isDeleted=0 and batchType='exam' and examType='N'"
								orderBy="examinputStartTime desc" /><font color="red">*</font></li>
						<%-- <c:if test="${!isBrschool }">
							
						</c:if>
						<c:if test="${isBrschool}">
							<input type="hidden" name="brSchoolId" id="electiveExam_school"
								value="${condition['brSchoolId']}" />
						</c:if> --%>
						<li><label>年级：</label> <span sel-id="electiveExam_grade"
							sel-name="gradeId" sel-onchange="electiveExamQueryGrade()"
							sel-style="width: 120px"></span></li>
						<li><label>层次：</label> <span sel-id="electiveExam_classic"
							sel-name="classicId" sel-onchange="electiveExamQueryClassic()"
							sel-style="width: 120px"></span></li>
						<!-- <li><label>学习形式：</label> <span
							sel-id="electiveExam_teachingType" sel-name="teachingType"
							sel-onchange="electiveExamQueryTeachingType()"
							dictionaryCode="CodeTeachingType" sel-style="width: 120px"></span>
						</li> -->
					</ul>
					<ul class="searchContent">
						<li class="custom-li"><label>专业：</label> <span sel-id="electiveExam_major"
							sel-name="majorId" sel-onchange="electiveExamQueryMajor()"
							sel-classs="flexselect"></span></li>
						
						<li><label>学籍状态：</label> <gh:select id="stuStatus"
								name="stuStatus" value="${condition['stuStatus']}"
								dictionaryCode="CodeStudentStatus" style="width:120px" /></li>
						<li><label>学号：</label> <input id="electiveExam_studyNo"
							name="studyNo" value="${condition['studyNo'] }"
							style="width: 120px"></li>
						<li><label>姓名：</label> <input id="electiveExam_studentName"
							name="studentName" value="${condition['studentName'] }"
							style="width: 120px"></li>
					</ul>
					<ul class="searchContent">
						<li class="custom-li"><label>班级：</label> <span sel-id="electiveExam_classes"
							sel-name="classesId" sel-classs="flexselect" ></span></li>
						
						<li><label>成绩状态：</label>
							<select id="electiveExam_CheckStatus" name="checkStatus" style="width:120px;">
								<option value="">请选择</option>
								<option value="0" <c:if test="${condition['checkStatus'] eq '0'}"> selected="selected" </c:if>>保存</option>
								<option value="1" <c:if test="${condition['checkStatus'] eq '1'}"> selected="selected" </c:if>>提交</option>
								<option value="4" <c:if test="${condition['checkStatus'] eq '4'}"> selected="selected" </c:if>>发布</option>
								<option value="-1" <c:if test="${condition['checkStatus'] eq '-1'}"> selected="selected" </c:if>>非保存</option>
							</select>
							<%-- <gh:select id="electiveExam_CheckStatus" name="checkStatus"
								value="${condition['checkStatus'] }"
								dictionaryCode="CodeExamResultCheckStatus" style="width:120px;" /> --%>
						</li>
						<li class="custom-li"><label>课程名称：</label>
							<select id='electiveExam_courseId' name="courseId" style="width:200px;"><!-- flexselect -->
								${courseInfo }
							</select><font color="red">*</font>
							<%-- <gh:courseAutocomplete name="courseId"
								id="electiveExam_courseId" tabindex="1" displayType="code"
								style="width:200px" courseType="22"
								value="${condition['courseId']}" /> --%>
								
						</li>
						<div class="buttonActive">
							<div class="buttonContent">
								<button type="submit">查 询</button>
							</div>
						</div>
					</ul>
					<ul class="searchContent">
						<li class="custom-li"><label>打印时间：</label> <input type="text" style="width: 80px;"
							id="printDateBegin" name="printDateBegin" class="Wdate"
							value="${condition['printDateBegin']}"
							onfocus="WdatePicker({isShowWeek:true })" /> 至<input
							type="text" style="width: 80px;" id="printDateEnd"
							name="printDateEnd" class="Wdate"
							value="${condition['printDateEnd']}"
							onfocus="WdatePicker({isShowWeek:true })" />
						</li>
					</ul>
				</div>
			</form>
		</div>

		<div class="pageContent">
			<gh:resAuth parentCode="RES_TEACHING_ELECTIVEEXAMRESULTS_LIST"
				pageType="list"></gh:resAuth>
			<table class="table" layouth="188">
				<thead>
					<tr>
						<th width="3%"><input type="checkbox" name="checkall" id="check_all_electiveExamBody" 
							onclick="checkboxAll('#check_all_electiveExamBody','resourceid','#electiveExamBody')" /></th>
						<th width="12%">教学点</th>
						<th width="10%">专业</th>
						<th width="15%">班级</th>
						<th width="8%" style="text-align: center; vertical-align: middle;">考试批次</th>
						<th width="8%" style="text-align: center; vertical-align: middle;">学号</th>
						<th width="5%" style="text-align: center; vertical-align: middle;">姓名</th>
						<th width="8%">选修课</th>
						<th width="5%" style="text-align: center; vertical-align: middle;">成绩</th>
						<th width="5%" style="text-align: center; vertical-align: middle;">成绩状态</th>
						<th width="8%">打印日期</th>
					</tr>
				</thead>
				<tbody id="electiveExamBody">
					<c:forEach items="${page.result }" var="item" varStatus="vs">
						<tr>
							<td><input type="checkbox" name="resourceid"
								value="${item.resourceid}" checkStatus="${item.checkStatus }" autocomplete="off" /></td>
							<td>${item.studentInfo.branchSchool.unitName}</td>
							<td>${item.studentInfo.major.majorName}</td>
							<td>${item.studentInfo.classes.classname}</td>
							<td style="text-align: center; vertical-align: middle;">${item.examSub.batchName}</td>
							<td style="text-align: center; vertical-align: middle;">${item.studentInfo.studyNo}</td>
							<td style="text-align: center; vertical-align: middle;">${item.studentInfo.studentName}</td>
							<td>${item.course.courseName}</td>
							<td style="text-align: center; vertical-align: middle;">
							<c:choose>
								<c:when test="${item.examAbnormity eq '0'}">
									<c:choose>
										<c:when test="${item.courseScoreType eq '11'}">
											${item.integratedScore}
										</c:when>
										<c:when test="${item.courseScoreType eq '25'}">
											${ghfn:dictCode2Val('CodeScoreChar',item.integratedScore)}
										</c:when>
										<c:otherwise></c:otherwise>
									</c:choose>
								</c:when>
								<c:otherwise>
									${ghfn:dictCode2Val('CodeExamAbnormity',item.examAbnormity)}
								</c:otherwise>
							</c:choose>
							</td>
							<td <c:if test="${item.checkStatus eq '4'}">style='color: blue;text-align: center;'</c:if>
								<c:if test="${item.checkStatus eq '1'}">style='color: green;text-align: center;'</c:if>
								<c:if test="${item.checkStatus eq '0'}">style='color: red;text-align: center;'</c:if>>${ghfn:dictCode2Val('CodeExamResultCheckStatus',item.checkStatus)}</td>
							<td><fmt:formatDate value="${item.printDate }" pattern="yyyy-MM-dd"/></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<gh:page page="${page}"
				goPageUrl="${baseUrl}/edu3/teaching/examResult/electiveExam-list.html"
				condition="${condition }" pageType="sys" />
		</div>
	</div>
	<script type="text/javascript">
	// 选择教学点
	function electiveExamQueryUnit() {
		var defaultValue = $("#electiveExam_school").val();
		var selectIdsJson = "{gradeId:'electiveExam_grade',classicId:'electiveExam_classic',teachingType:'electiveExam_teachingType',majorId:'electiveExam_major',classesId:'electiveExam_classes'}";
		cascadeQuery("unit", defaultValue, "", "", "", "", "", "",selectIdsJson);
		getCourseInfo();
	}

	// 选择年级
	function electiveExamQueryGrade() {
		var defaultValue = $("#electiveExam_school").val();
		var gradeId = $("#electiveExam_grade").val();
		var selectIdsJson = "{classicId:'electiveExam_classic',teachingType:'electiveExam_teachingType',majorId:'electiveExam_major',classesId:'electiveExam_classes'}";
		cascadeQuery("grade", defaultValue, "", gradeId, "", "", "", "",selectIdsJson);
		getCourseInfo();
	}

	// 选择层次
	function electiveExamQueryClassic() {
		var defaultValue = $("#electiveExam_school").val();
		var gradeId = $("#electiveExam_grade").val();
		var classicId = $("#electiveExam_classic").val();
		var selectIdsJson = "{teachingType:'electiveExam_teachingType',majorId:'electiveExam_major',classesId:'electiveExam_classes'}";
		cascadeQuery("classic", defaultValue, "", gradeId, classicId, "", "", "", selectIdsJson);
		getCourseInfo()
	}

	// 选择学习形式
	function electiveExamQueryTeachingType() {
		var defaultValue = $("#electiveExam_school").val();
		var gradeId = $("#electiveExam_grade").val();
		var classicId = $("#electiveExam_classic").val();
		var teachingTypeId = $("#electiveExam_teachingType").val();
		var selectIdsJson = "{majorId:'electiveExam_major',classesId:'electiveExam_classes'}";
		cascadeQuery("teachingType", defaultValue, "", gradeId, classicId,teachingTypeId, "", "", selectIdsJson);
	}

	// 选择专业
	function electiveExamQueryMajor() {
		var defaultValue = $("#electiveExam_school").val();
		var gradeId = $("#electiveExam_grade").val();
		var classicId = $("#electiveExam_classic").val();
		var teachingTypeId = $("#electiveExam_teachingType").val();
		var majorId = $("#electiveExam_major").val();
		var selectIdsJson = "{classesId:'electiveExam_classes'}";
		cascadeQuery("major", defaultValue, "", gradeId, classicId,teachingTypeId, majorId, "", selectIdsJson);
		getCourseInfo();
	}

	/**
	 * 下载模版
	 */
	function downloadElectiveModel(){
		window.location.href="${baseUrl }/edu3/teaching/electiveExamResult/download-file.html"
	}

	/**
	 * 导入成绩
	 */
	function inportElectiveExam(){
		$.pdialog.open(baseUrl+"/edu3/teaching/electiveExamResult/inputScore.html", '导入选修课成绩成绩', {width: 600, height: 400});
	}

	/**
	 * 打印成绩单
	 */
	function printElectiveExamResult(){
		var brSchoolId = $("#electiveExam_school").val();
		var courseId = $("#electiveExam_courseId").val();
		var examSubId = $("#electiveExam_ExamSub").val(); 
		var checkStatus = $("#electiveExam_CheckStatus").val();
		var electiveExamId = [];
		if(brSchoolId==""){
			alertMsg.warn("请选择教学点！");
			return false;
		}
		if(examSubId==""){
			alertMsg.warn("请选择考试批次！");
			return false;
		}
		if(courseId==""){
			alertMsg.warn("请选择课程！");
			return false;
		}
		
		var message = "";
		/* $("#electiveExamBody input[@name='resourceid']:checked").each(function(){
			var checekObj = $(this);
			if(checekObj.attr("checkStatus")=='0'){
				message = "保存状态成绩不允许打印！";
			}
			electiveExamId.push(checekObj.val());
		}); */
		if(message!=""){
			alertMsg.warn(message);
			return false;
		}else if(electiveExamId.length==0){
			if(checkStatus=="0"){
				alertMsg.warn("请选择其它成绩状态查询条件再进行打印！");
				return false;
			}
		}
		var param = getUrlParam();
		var url = "${baseUrl}/edu3/teaching/electiveExamResult/printView.html?operatingType=print"+param+"&resourceids="+electiveExamId.join(',');
		$.pdialog.open(url, 'electiveExam_printView','打印预览', {width: 800, height: 600});
	}



	//成绩提交、撤销提交、成绩审核、撤销审核、删除
	function operateExamResult(type) {//type:submit 、 cancelSubmit、 audit 、 cancelAudit、delete
		var electiveExamId = [];
		var message = "";
		$("#electiveExamBody input[name='resourceid']:checked").each(function(){
			var checekObj = $(this);
			if(type=='audit'){
				if(checekObj.attr("checkStatus")!="1"){
					message = "非提交状态成绩，不允许审核！";
				}
			}else if(type=='cancelAudit'){
				if(checekObj.attr("checkStatus")!="4"){
					message = "非发布状态成绩，不允许撤销审核！";
				}
			}else if(type=='submit'){
                if(checekObj.attr("checkStatus")!='0'){
                    message = "非保存状态成绩，不允许提交！";
                }
            }else if(type=='cancelSubmit'){
                if(checekObj.attr("checkStatus")!='1'){
                    message = "非提交状态成绩，不允许撤销提交！";
                }
            }else if(type=='delete'){
                if(checekObj.attr("checkStatus")!='0'){
                    message = "非保存状态成绩，不允许删除！";
                }
			}
			electiveExamId.push(checekObj.val());
		});
		if(message!=""){
			alertMsg.warn(message);
			return false;
		}
		var info = "您确定要按照查询条件来进行下一步操作吗？";
		if(electiveExamId.length>0){
            info = "一共选择了"+electiveExamId.length+"个记录，您确定要进行下一步操作吗？";
		}
		var param = getUrlParam();
		var url = "${baseUrl}/edu3/teaching/electiveExamResult/operateExamResult.html?operatingType="+type+param;
        alertMsg.confirm(info, {
            okCall:function(){
                $.ajax({
                    type:'POST',
                    url:url,
                    data:{resourceid:electiveExamId.join(',')},
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
                            navTab.reload($("#electiveExamForm").attr("action"), $("#electiveExamForm").serializeArray());
                        }
                    }
                });
            }
        })
	}

	//编辑选修课成绩
	function editElectiveExamResult(){
		var electiveExamId = [];
		var message = "";
		var url = "${baseUrl}/edu3/teaching/electiveExamResult/editExamResult.html";
		$("#electiveExamBody input[name='resourceid']:checked").each(function(){
			var checekObj = $(this);
			if(checekObj.attr("checkStatus")!="0"){
				message = "请选择保存状态的成绩进行操作！";
			}
			electiveExamId.push(checekObj.val());
		});
		if(message!=""){
			alertMsg.warn(message);
			return false;
		}else if(electiveExamId.length==0){
			alertMsg.warn("请选择要操作的成绩！");
			return false;
		}else if(electiveExamId.length>1){
			alertMsg.warn("只能一个学生成绩进行修改！");
			return false;
		}
        navTab.openTab('RES_TEACHING_ELECTIVEEXAMRESULTS_INPUT', url+'?resourceid='+$("#electiveExamBody input[name='resourceid']:checked").val(), '编辑选修课成绩');

	}
	
	function getCourseInfo(){
		var param = getUrlParam();
		$.ajax({
			type:"post",
			url:baseUrl+"/edu3/teaching/electiveExamResult/getCourseInfo.html?1=1"+param,
			data:{},
			dataType:"json",
			success:function(data){
				if(data.statusCode==200){
					//$("select[class*=flexselect]").flexselect();
					$("#electiveExam_courseId").html(data.courseInfo);
					
				}else{
					alertMsg.warn(data.message);
				}
			}
		});
	}
	
	function getUrlParam(){
		var url = "";
		var examSubId = $("#electiveExam_ExamSub").val();
		var courseId = $("#electiveExam_courseId").val();
		var brSchoolId = $("#electiveExam_school").val();
		var gradeId = $("#electiveExam_grade").val();
		var classicId = $("#electiveExam_classic").val();
		var teachingType = $("#electiveExam_teachingType").val();
		var majorId = $("#electiveExam_major").val();
		var classesId = $("#electiveExam_classes").val();
		var stuStatus = $("#stuStatus").val();
		var studyNo = $("#electiveExam_studyNo").val();
		var studentName = $("#electiveExam_studentName").val();
		var checkStatus = $("#electiveExam_CheckStatus").val();
		var printDateBegin = $("#printDateBegin").val();
		var printDateEnd = $("#printDateEnd").val();
		
		if(examSubId!=""){
			url += "&examSubId="+examSubId;
		}
		if(courseId!=""){
			url += "&courseId="+courseId;
		}
		if(brSchoolId!=""){
			url += "&brSchoolId="+brSchoolId;
		}
		if(gradeId!=""){
			url += "&gradeId="+gradeId;
		}
		if(classicId!=""){
			url += "&classicId="+classicId;
		}
		if(teachingType!="" && teachingType!=undefined){
			url += "&teachingType="+teachingType;
		}
		if(majorId!=""){
			url += "&majorId="+majorId;
		}
		if(classesId!=""){
			url += "&classesId="+classesId;
		}
		if(studyNo!=""){
			url += "&studyNo="+studyNo;
		}
		if(studentName!=""){
			url += "&studentName="+studentName;
		}
		if(stuStatus!=""){
			url += "&stuStatus="+stuStatus;
		}
		if(checkStatus!=""){
			url += "&checkStatus="+checkStatus;
		}
		if(printDateBegin!=""){
			url += "&printDateBegin="+printDateBegin;
		}
		if(printDateBegin!=""){
			url += "&printDateEnd="+printDateEnd;
		}
		return url;
	}

	function validateTab(form){
		var examSubId = $("#electiveExam_ExamSub").val();
		var brSchoolId = $("#electiveExam_school").val();
		var gradeId = $("#electiveExam_grade").val();
		var classesId = $("#electiveExam_classes").val();
		var studyNo = $("#electiveExam_studyNo").val();
		if(examSubId=="" && brSchoolId=="" && gradeId=="" && studyNo==""){
			alertMsg.warn("请选择教学点或考试批次！");
			return false;
		}
		return navTabSearch(form);
	}
	</script>
</body>
</html>