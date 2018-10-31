<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>网上学习时间设置</title>
<script type="text/javascript">
	//新增
	function addLearningTimeSetting(){
		navTab.openTab('RES_TEACHING_LEARNINGTIMESETTING_INPUT', '${baseUrl}/edu3/teaching/learningtimesetting/input.html', '新增网上学习时间');
	}
	
	//修改
	function modifyLearningTimeSetting(){	
		if(isCheckOnlyone('resourceid','#learningTimeSettingBody')){
			var url = "${baseUrl}/edu3/teaching/learningtimesetting/input.html?resourceid="+$("#learningTimeSettingBody input[@name='resourceid']:checked").val();
			navTab.openTab('RES_TEACHING_LEARNINGTIMESETTING_INPUT', url, '编辑网上学习时间');
		}			
	}
		
	//删除
	function removeLearningTimeSetting(){	
		pageBarHandle("您确定要删除这些记录吗？","${baseUrl}/edu3/teaching/learningtimesetting/remove.html","#learningTimeSettingBody");
	}
	//触发自动计算平时分
	function autoCaculate(){
		var url = "${baseUrl }/edu3/teaching/learningtimesetting/autoCalucate.html";
		navTab.openTab('RES_TEACHING_LEARNINGTIMESETTING_INPUT_11', url, '自动算分');
	}
	
	function autoCaculate_totalCret(){
		var url = "${baseUrl }/edu3/teaching/result/calculateTotalCreditHour.html?flag=auto";
		navTab.openTab('RES_TEACHING_LEARNINGTIMESETTING_INPUT_12', url, '自动算总学分');
	}
</script>
</head>
<body>
	<div class="page">
		<div class="pageHeader">
			<form onsubmit="return navTabSearch(this);"
				action="${baseUrl }/edu3/teaching/learningtimesetting/list.html"
				method="post">
				<div class="searchBar">
					<ul class="searchContent">
						<li><label>年度：</label> <gh:selectModel
								id="learningTimeSetting_yearInfoId" name="yearInfoId"
								bindValue="resourceid" displayValue="yearName"
								modelClass="com.hnjk.edu.basedata.model.YearInfo"
								value="${condition['yearInfoId']}" orderBy="firstYear desc"
								style="width:125px;" /></li>
						<li><label>学期：</label>
						<gh:select id="learningTimeSetting_term" name="term"
								value="${condition['term']}" dictionaryCode="CodeTerm" /></li>
					</ul>
					<div class="subBar">
						<ul>
							<li><div class="buttonActive">
									<div class="buttonContent">
										<button type="button" onclick="autoCaculate()">自动算平时分</button>
									</div>
								</div></li>
							<li><div class="buttonActive">
									<div class="buttonContent">
										<button type="button" onclick="autoCaculate_totalCret()">自动算总学分</button>
									</div>
								</div></li>
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
			<gh:resAuth parentCode="RES_TEACHING_LEARNINGTIMESETTING"
				pageType="list"></gh:resAuth>
			<table class="table" layouth="138">
				<thead>
					<tr>
						<th width="5%"><input type="checkbox" name="checkall"
							id="check_all_learningTimeSetting"
							onclick="checkboxAll('#check_all_learningTimeSetting','resourceid','#learningTimeSettingBody')" /></th>
						<th width="20%">年度</th>
						<th width="15%">学期</th>
						<th width="20%">开始时间</th>
						<th width="20%">截止时间</th>
						<th width="20%">备注</th>
					</tr>
				</thead>
				<tbody id="learningTimeSettingBody">
					<c:forEach items="${learningTimeSettingList.result}" var="setting"
						varStatus="vs">
						<tr>
							<td><input type="checkbox" name="resourceid"
								value="${setting.resourceid }" autocomplete="off" /></td>
							<td>${setting.yearInfo.yearName }</td>
							<td>${ghfn:dictCode2Val('CodeTerm',setting.term) }</td>
							<td><fmt:formatDate value="${setting.startTime }"
									pattern="yyyy-MM-dd HH:mm:ss" /></td>
							<td><fmt:formatDate value="${setting.endTime }"
									pattern="yyyy-MM-dd HH:mm:ss" /></td>
							<td>${setting.memo }</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<gh:page page="${learningTimeSettingList}"
				goPageUrl="${baseUrl }/edu3/teaching/learningtimesetting/list.html"
				pageType="sys" condition="${condition}" />
		</div>
	</div>
</body>
</html>