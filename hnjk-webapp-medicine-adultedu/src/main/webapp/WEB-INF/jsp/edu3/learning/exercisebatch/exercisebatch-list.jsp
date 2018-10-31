<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>作业管理</title>
<script type="text/javascript">
	$(document).ready(function(){
		$("select[class*=flexselect]").flexselect();
	});
	//新增
	function addExerciseBatch(colType){
		if(!colType){
			colType = '1';
		}
		var url = "${baseUrl}/edu3/metares/exercise/unactiveexercise/input.html?colType="+colType;
		navTab.openTab('RES_METARES_EXERCISE_EXERCISEBATCH', url, '新增'+(colType=='1'?'客观题':'')+'作业');
	}
	
	//修改
	function editExerciseBatch(){
		var url = "${baseUrl}/edu3/metares/exercise/unactiveexercise/input.html";
		if(isCheckOnlyone('resourceid','#exerciseBatchBody')){
			navTab.openTab('RES_METARES_EXERCISE_EXERCISEBATCH', url+'?resourceid='+$("#exerciseBatchBody input[@name='resourceid']:checked").val(), '编辑作业');
		}			
	}
		
	//删除
	function removeExerciseBatch(){	
		pageBarHandle("您确定要删除这些记录吗？","${baseUrl}/edu3/metares/exercise/unactiveexercise/remove.html","#exerciseBatchBody");
	}
	//发布作业
	function statusExerciseBatch(type){
		pageBarHandle("您确定要"+(type==0?"取消":"")+"发布这些作业吗？","${baseUrl}/edu3/metares/exercise/unactiveexercise/status.html?type="+type,"#exerciseBatchBody");
	}
	//修改作业截止时间
	function endDateExerciseBatch(){
		if(isCheckOnlyone('resourceid','#exerciseBatchBody')){
			var startDate = $("#exerciseBatchBody input[@name='resourceid']:checked").attr('rel');
			var res = $("#exerciseBatchBody input[@name='resourceid']:checked").val();
			alertMsg.confirm("截止时间：<input type='text' id='_exerciseBatch_endDate' onFocus=\"WdatePicker({dateFmt:'yyyy-MM-dd',minDate:'"+startDate+"'})\">",{okCall:function (){
				var endDate = $("#_exerciseBatch_endDate").val();
				if(endDate!=""){		
					var res = $("#exerciseBatchBody input[@name='resourceid']:checked").val();
					$.post("${baseUrl}/edu3/metares/exercise/unactiveexercise/enddate/save.html",{resourceid:res,endDate:endDate},navTabAjaxDone,"json");
				} else {
					saveExerciseBatchEndDate();//重新选择时间
				}
			},okName:'设置'});	
		}	
	}
</script>
</head>
<body>
	<div class="page">
		<div class="pageHeader">
			<form onsubmit="return navTabSearch(this);"
				action="${baseUrl }/edu3/metares/exercise/unactive/list.html"
				method="post">
				<div class="searchBar">
					<ul class="searchContent">
						<li class="custom-li"><label>班级：</label> <gh:classesAutocomplete
								name="classesId" id="exercisebatch_list_classid" tabindex="1"
								displayType="code" defaultValue="${condition['classesId']}"
								exCondition="${classesCondition}" style="width:240px;"></gh:classesAutocomplete></li>
						<li><label>年度：</label>
						<gh:selectModel id="exerciseBatch_yearInfoId" name="yearInfoId"
								bindValue="resourceid" displayValue="yearName"
								modelClass="com.hnjk.edu.basedata.model.YearInfo" style="width:120px;"
								value="${condition['yearInfoId']}" orderBy="firstYear desc" /></li>
						<li><label>学期：</label>
						<gh:select id="exerciseBatch_term" name="term" style="width:120px;"
								value="${condition['term']}" dictionaryCode="CodeTerm" /></li>
					</ul>
					<ul class="searchContent">
						<li class="custom-li"><label>课程：</label> ${exercisebatchlistCourseSelect }</li>
						<%-- 
				<li>
					<label>作业标题：</label><input  type="text" name="colName" value="${condition['colName'] }"/>
											
				</li>
				<li>
					<label>作业类型：</label><gh:select name="colType" value="${condition['colType']}" dictionaryCode="CodeLearningColType"/>
				</li> --%>
						<li><label>作业状态：</label>
						<gh:select name="status" value="${condition['status']}" style="width:120px;"
								dictionaryCode="CodeExerciseBatchStatus" /></li>
					</ul>
					<ul style="margin-top: 8px">
						<li><span class="tips" style="padding: 2px;">点击作业标题可进入作业批阅界面</span></li>
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
			<gh:resAuth parentCode="RES_METARES_EXERCISE_UNACTIVE"
				pageType="eblist"></gh:resAuth>
			<table class="table" layouth="182">
				<thead>
					<tr>
						<th width="3%"><input type="checkbox" name="checkall"
							id="check_all_batch"
							onclick="checkboxAll('#check_all_batch','resourceid','#exerciseBatchBody')" /></th>
						<th width="8%">年度</th>
						<th width="5%">学期</th>
						<th width="8%">所属课程</th>
						<th width="9%">作业标题</th>
						<th width="5%">作业类型</th>
						<th width="8%">作业开始日期</th>
						<th width="8%">作业截止日期</th>
						<th width="5%">作业状态</th>
						<th width="5%">计分形式</th>
						<th width="5%">填写人姓名</th>
						<th width="8%">班级</th>
						<th width="5%">学生人数</th>
						<th width="5%">已提交</th>
						<th width="5%">已批改</th>
						<th width="5%">作业次数</th>
					</tr>
				</thead>
				<tbody id="exerciseBatchBody">
					<c:forEach items="${exerciseBatchList.result }" var="exercise"
						varStatus="vs">
						<tr>
							<td><input type="checkbox" name="resourceid"
								value="${exercise.resourceid }"
								rel="<fmt:formatDate value='${exercise.startDate }' pattern='yyyy-MM-dd'/>"
								autocomplete="off" /></td>
							<td>${exercise.yearInfo.yearName }</td>
							<td>${ghfn:dictCode2Val('CodeTerm',exercise.term) }</td>
							<td><a
								href="${baseUrl }/edu3/teaching/teachingcourse/view.html?courseId=${exercise.course.resourceid }"
								target="dialog" mask="true" width="700" height="400"
								title="${exercise.course.courseName }">${exercise.course.courseName }</a></td>
							<td><a
								href="${baseUrl}/edu3/teaching/exercisecheck/studentexercise/list.html?exerciseBatchId=${exercise.resourceid}&unitId=${unitId}&courseId=${exercise.course.resourceid}"
								target="navTab" rel="studentexercise"
								title="批改作业${exercise.colName }">${exercise.colName }</a></td>
							<td>${ghfn:dictCode2Val('CodeLearningColType',exercise.colType) }</td>
							<td><fmt:formatDate value="${exercise.startDate }"
									pattern="yyyy-MM-dd" /></td>
							<td><fmt:formatDate value="${exercise.endDate }"
									pattern="yyyy-MM-dd" /></td>
							<td>${ghfn:dictCode2Val('CodeExerciseBatchStatus',exercise.status) }</td>
							<td>
								${ghfn:dictCode2Val('CodeExerciseBatchScoringType',exercise.scoringType) }
							</td>
							<td><a
								href="${baseUrl }/edu3/framework/edumanager/view.html?userId=${exercise.fillinManId }"
								target="dialog" mask="true" width="700" height="400">${exercise.fillinMan }</a></td>
							<td title="${exercise.classesNames}">${exercise.classesNames}</td>
							<%-- <td>${exercise.orderCourseNum }</td> --%>
							<td>${empty resultMap[exercise.resourceid].totalNum ? 0 : resultMap[exercise.resourceid].totalNum }</td>
							<td>${empty resultMap[exercise.resourceid].handNum ? 0 : resultMap[exercise.resourceid].handNum }</td>
							<td>${empty resultMap[exercise.resourceid].correctNum ? 0 : resultMap[exercise.resourceid].correctNum }</td>
							<td>${exercise.exerciseNum }</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<gh:page page="${exerciseBatchList}"
				goPageUrl="${baseUrl}/edu3/metares/exercise/unactive/list.html"
				condition="${condition }" pageType="sys" />

		</div>
	</div>
</body>
</html>