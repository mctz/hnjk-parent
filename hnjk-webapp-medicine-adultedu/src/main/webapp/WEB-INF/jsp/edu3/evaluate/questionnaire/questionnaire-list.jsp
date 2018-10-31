<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>问卷列表</title>
</head>
<script type="text/javascript">

	$(document).ready(function() {
		questionnaireQueryBegin();
	});
	//打开页面或者点击查询（即加载页面执行）
	function questionnaireQueryBegin() {
		var defaultValue = "${condition['brSchool']}";
		var schoolId = "${linkageQuerySchoolId}";
		var classesId = "${condition['classesId']}";
		var selectIdsJson = "{unitId:'questionnaire_unitId',classesId:'questionnaire_classesid'}";
		cascadeQuery("begin", defaultValue, schoolId, "", "", "", "",
				classesId, selectIdsJson);
	}
	function questionnaireQueryUnit() {
		var defaultValue = $("#questionnaire_brSchool").val();
		var selectIdsJson = "{classesId:'questionnaire_classesid'}";
		cascadeQuery("unit", defaultValue, "", "", "", "", "", "",
				selectIdsJson);
	}

	//修改
	function editQuestionnaireTime() {
		var url = "${baseUrl}/edu3/teaching/quality/evaluation/questionnaire/editTime.html";
		
		if(!isChecked('resourceid','#QuestionnaireBody')){
 			alertMsg.warn('请选择一条要操作记录！');
			return false;
 		}
			
		var res = "";
		var k = 0;
		var num  = $("#QuestionnaireBody input[name='resourceid']:checked").size();
		$("#QuestionnaireBody input[@name='resourceid']:checked").each(function(){
                   res+=$(this).val();
                   if(k != num -1 && res!='') res += ",";
                   k++;
               })
        if(res==''){
        	alertMsg.warn("没有设置任课老师的课程，无法修改问卷有效时间！请先设置课程任课老师后再试");
        	return false;
        }
		alertMsg.confirm("开始时间：<input type='text' id='questionnaire_startDate'  onFocus=\"WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})\">"+
				"<br>结束时间：<input type='text' id='questionnaire_endDate'  onFocus=\"WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss' })\">",
				{okCall:function (){
			var startDate = $("#questionnaire_startDate").val();
			var endDate = $("#questionnaire_endDate").val();
			if(startDate!=""&& endDate!=""){					
				$.post(url,{resourceids:res,startDate:startDate,endDate:endDate},navTabAjaxDone,"json");
			} else {
				//重新选择时间
			}
		},okName:'设置'});	
	}
	

	//删除
	function cancelQuestionnaire() {
		_pageBarHandle(
				"您确定要取消发布这些记录吗？",
				"${baseUrl}/edu3/teaching/quality/evaluation/questionnaire/isPublish.html?operate=cancel",
				"#QuestionnaireBody");
	}
	function publishQuestionnaire() {
		_pageBarHandle(
				"您确定要发布这些记录吗？",
				"${baseUrl}/edu3/teaching/quality/evaluation/questionnaire/isPublish.html?operate=publish",
				"#QuestionnaireBody");
	}
	//查询条件约束检查
	function validateSearch(){
		var yearId = $("#questionnaire_yearInfoId").val();
		var term = $("#questionnaire_term").val();
		if(''==yearId||''==term){
			alertMsg.warn("年度和学期都不能为空!请先选择后再进行查询");
			return false;
		}
		return true;
	}
	function _pageBarHandle(msg,postUrl,bodyname){
		if(!isChecked('resourceid',bodyname)){
	 			alertMsg.warn('请选择一条要操作记录！');
				return false;
	 	}
		alertMsg.confirm(msg, {
				okCall: function(){//执行			
					var res = "";
					var k = 0;
					var num  = $(bodyname+" input[name='resourceid']:checked").size();
					$(bodyname+" input[@name='resourceid']:checked").each(function(){
	                        res+=$(this).val();
	                        if(k != num -1 && res!='') res += ",";
	                        k++;
	                    })	                
					if(res==''){
						alertMsg.warn("没有设置任课老师的课程，发布或取消发布！请先设置课程任课老师后再试");
						return false;
					}
					$.post(postUrl,{resourceid:res}, navTabAjaxDone, "json");
				}
		});			
	}
</script>
<body>
	<div class="page">
		<div class="pageHeader">
			<form id="questionnaire_form" onsubmit="return navTabSearch(this);"
				action="${baseUrl }/edu3/teaching/quality/evaluation/questionnaire/list.html" method="post">
				<div class="searchBar">
					<ul class="searchContent">
						<li  style="width: 480px;"><label>教学点：</label> <span sel-id="questionnaire_unitId"
							sel-name="brSchool" sel-onchange="questionnaireQueryUnit()"
							sel-classs="flexselect" ></span></li>
						<li><label>年度：</label> <gh:selectModel
								id="questionnaire_yearInfoId" name="yearId"
								bindValue="resourceid" displayValue="yearName"
								modelClass="com.hnjk.edu.basedata.model.YearInfo"
								value="${condition['yearId']}" orderBy="yearName desc"
								style="width:125px" /><span style="color: red;">*</span></li>
						<li><label>学期：</label> <gh:select 
								id="questionnaire_term" name="term"
								dictionaryCode="CodeTerm" value="${condition['term']}"
								style="width:125px" 
								/><span style="color: red;">*</span></li>
						<li><label>课程：</label> <gh:courseAutocomplete name="courseId"
								tabindex="1" id="questionnaire_courseId"
								value="${condition['courseId']}" displayType="code"
								style="width:55%" /></li>
						

					</ul>
					<ul class="searchContent">
						<li style="width: 480px;"><label>班级：</label> <span sel-id="questionnaire_classesid"
							sel-name="classesId" sel-classs="flexselect" ></span></li>

						<li><label>教师姓名：</label> <input type="text" name="teacherName"
							value="${condition['teacherName']}" style="width: 125px" /></li>
						<li><label>是否发布：</label> <gh:select name="isPublish"
								dictionaryCode="yesOrNo" value="${condition['isPublish']}"
								style="width:125px" /></li>
						<li><label>是否已设老师：</label> <gh:select name="hasTeacher"
								dictionaryCode="yesOrNo" value="${condition['hasTeacher']}"
								style="width:125px" /></li>
					</ul>
						<ul class="searchContent">
						
						<li><label>课程类型：</label> <gh:select name="teachType"
								dictionaryCode="CodeCourseTeachType" value="${condition['teachType']}"
								style="width:125px" /></li>
					</ul>
					<div class="subBar">
						<ul>
							<li><div class="buttonActive">
									<div class="buttonContent">
										<button type="submit" onclick="return validateSearch();">查 询</button>
									</div>
								</div></li>
						</ul>
					</div>
				</div>
			</form>
		</div>
		<div class="pageContent">
			<gh:resAuth parentCode="RES_TEACHING_QUALITY_EVALUATION_QUESTIONNAIRE" pageType="list"></gh:resAuth>
			<table class="table" layouth="188">
				<thead>
					<tr>
						<th width="3%"><input type="checkbox" name="checkall"
							id="check_all_evaluation_question" onclick="checkboxAll('#check_all_evaluation_question','resourceid','#QuestionnaireBody')" 
							</th>
						<th width="15%">教学点</th>
						<th width="10%">班级名称</th>
						<th width="5%">教师姓名</th>
						<th width="6%">教师账号</th>
						<th width="10%">课程名称</th>
						<th width="5%">年度</th>
						<th width="5%">学期</th>
						<th width="4%">班级人数</th>
						<th width="4%">已收集问卷</th>
						<th width="4%">有效问卷</th>
						<th width="4%">有效采样率</th>	
						<th width="4%">课程类型</th>							
						<th width="8%">开始时间</th>
						<th width="8%">结束时间</th>
						<th width="4%">是否发布</th>					
					</tr>
				</thead>
				<tbody id="QuestionnaireBody">
					<c:forEach items="${page.result}" var="qn"
						varStatus="vs">
						<tr>
							<td><input type="checkbox" name="resourceid"
								value="${qn.resourceid }" autocomplete="off" /></td>
							<td>${qn.unitname }</td>
							<td>${qn.classesname }</td>
							<td>${qn.cnname }</td>
							<td>${qn.username }</td>
							<td>${qn.coursename }</td>
							<td>${qn.yearname }</td>							
							<td>${ghfn:dictCode2Val('CodeTerm',qn.term) }</td>
							<td>${qn.stuCount }</td>
							<td>${qn.sqCount }</td>
							<td>${qn.validCount }</td>
							<td>${qn.pervalid }</td>
							<td>${ghfn:dictCode2Val('CodeCourseTeachType',qn.teachType) }</td>
							<td><fmt:formatDate value="${qn.startTime }"
									pattern="yyyy-MM-dd HH:mm:ss" /></td>
							<td><fmt:formatDate value="${qn.endTime }"
									pattern="yyyy-MM-dd HH:mm:ss" /></td>
							<td><c:if test="${qn.isPublish eq 'N' }">
									<span style="color:red">${ghfn:dictCode2Val('yesOrNo',qn.isPublish) }</span>
								</c:if> <c:if test="${qn.isPublish eq 'Y' }">
										${ghfn:dictCode2Val('yesOrNo',qn.isPublish) }
									</c:if></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<gh:page page="${page}" targetType="navTab"
				goPageUrl="${baseUrl }/edu3/teaching/quality/evaluation/questionnaire/list.html"
				pageType="sys" condition="${condition}" />
		</div>
	</div>
</body>
</html>