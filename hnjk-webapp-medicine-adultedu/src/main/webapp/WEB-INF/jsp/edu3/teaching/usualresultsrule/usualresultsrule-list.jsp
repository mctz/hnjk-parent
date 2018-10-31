<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>学生平时分积分规则管理</title>
<script type="text/javascript">
	//$(function (){
	//	_w_table_rowspan("#usualResultsRuleTable",2);
	//});
</script>
</head>
<body>
	<div class="page">
		<div class="pageHeader">
			<form onsubmit="return navTabSearch(this);"
				action="${baseUrl }/edu3/teaching/usualresultsrule/list.html"
				method="post">
				<div class="searchBar">
					<ul class="searchContent">
						<li class="custom-li"><label>年度：</label>
						<gh:selectModel id="usualresultsrule_yearInfoId" name="yearInfoId"
								bindValue="resourceid" displayValue="yearName"
								style="width:130px"
								modelClass="com.hnjk.edu.basedata.model.YearInfo"
								value="${condition['yearInfoId']}" orderBy="firstYear desc" />
						</li>
						<li><label>学期：</label>
						<gh:select id="usualresultsrule_term" name="term"
								value="${condition['term']}" dictionaryCode="CodeTerm"
								style="width:130px" /></li>
					</ul>
					<ul class="searchContent">
						<li class="custom-li"><label>课程：</label> <gh:courseAutocomplete name="courseId"
								tabindex="1" id="usualresultsrule_courseId"
								value="${condition['courseId'] }" displayType="code"></gh:courseAutocomplete>
						</li>
						<li><label>课程编码：</label> <input type="text" name="courseCode"
							value="${condition['courseCode'] }" /></li>
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
			<gh:resAuth parentCode="RES_TEACHING_ESTAB_USUALRESULTSRULE"
				pageType="list"></gh:resAuth>
			<table class="table" layouth="161" id="usualResultsRuleTable">
				<thead>
					<tr>
						<th width="5%"><input type="checkbox" name="checkall"
							id="check_all_usualResultsRules"
							onclick="checkboxAll('#check_all_usualResultsRules','resourceid','#usualResultsRuleBody')" /></th>
						<th width="10%">年度学期</th>
						<th width="5%">课程编码</th>
						<th width="9%">课程名称</th>
						
						<th width="6%">随堂问答分比例</th>
						<th width="6%">随堂练习分比例</th>
						<th width="6%">作业练习分比例</th>
						<c:if test="${defaultRule eq 'N'}">
							<th width="6%">网络辅导分比例</th>
							<th width="5%">同步自测分比例</th>
							<th width="5%">实践及其他分比例</th>
							<th width="5%">面授考勤分比例</th>
						</c:if>
						<th width="5%">优秀帖满分个数</th>
						<th width="6%">随堂练习满分正确率</th>
						<%-- <th width="5%">规则版本</th> --%>
						<th width="5%">是否已使用</th>
						<th width="8%">创建时间</th>
						<th width="8%">备注</th>
					</tr>
				</thead>
				<tbody id="usualResultsRuleBody">
					<c:forEach items="${usualResultsRuleList.result}" var="u"
						varStatus="vs">
						<tr>
							<td><input type="checkbox" name="resourceid"
								value="${u.resourceid }" autocomplete="off" rel="${u.isUsed }" /></td>
							<td
								title="${u.yearInfo.yearName }${ghfn:dictCode2Val('CodeTerm',u.term) }">${u.yearInfo.yearName }${ghfn:dictCode2Val('CodeTerm',u.term) }</td>
							<td>${u.course.courseCode }</td>
							<td title="${u.course.courseName }">${u.course.courseName }</td>
							
							<td><fmt:formatNumber type="percent"
									value="${u.askQuestionResultPer/100 }" /></td>
							<td><fmt:formatNumber type="percent"
									value="${u.courseExamResultPer/100 }" /></td>
							<td><fmt:formatNumber type="percent"
									value="${u.exerciseResultPer/100 }" /></td>
							<c:if test="${defaultRule eq 'N'}">
								<td><fmt:formatNumber type="percent"
										value="${u.bbsResultPer/100 }" /></td>
								<td><fmt:formatNumber type="percent"
										value="${u.selftestResultPer/100 }" /></td>
								<td><fmt:formatNumber type="percent"
										value="${u.otherResultPer/100 }" /></td>
								<td><fmt:formatNumber type="percent"
										value="${u.faceResultPer/100 }" /></td>
							</c:if>
							<td>${u.bbsBestTopicNum }</td>
							<td><fmt:formatNumber type="percent"
									value="${u.courseExamCorrectPer/100 }" /></td>
							<%--  <td>${u.versionNum }</td> --%>
							<td>${ghfn:dictCode2Val('yesOrNo',u.isUsed) }</td>
							<td><fmt:formatDate value="${u.fillinDate }"
									pattern="yyyy-MM-dd" /></td>
							<td>${u.memo }</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>

			<gh:page page="${usualResultsRuleList}"
				goPageUrl="${baseUrl }/edu3/teaching/usualresultsrule/list.html"
				pageType="sys" condition="${condition}" />
		</div>
	</div>
	<script type="text/javascript">
	//新增
	function addUsualResultsRule(){
		navTab.openTab('RES_TEACHING_ESTAB_USUALRESULTSRULE_INPUT', '${baseUrl}/edu3/teaching/usualresultsrule/input.html', '新增学生平时分积分规则');
	}
	
	//修改
	function modifyUsualResultsRule(){
		var url = "${baseUrl }/edu3/teaching/usualresultsrule/input.html";
		if(isCheckOnlyone('resourceid','#usualResultsRuleBody')){
			//if($.trim($("#usualResultsRuleBody input[@name='resourceid']:checked").attr('rel'))=='Y'){
			//	alertMsg.warn("该规则正在使用中，不能更改！");
			//} else {
				navTab.openTab('RES_TEACHING_ESTAB_USUALRESULTSRULE_INPUT', url+'?resourceid='+$("#usualResultsRuleBody input[@name='resourceid']:checked").val(), '编辑学生平时分积分规则');
			//}			
		}			
	}
		
	//删除
	function removeUsualResultsRule(){	
		var isOk = true;
		$("#usualResultsRuleBody input[@name='resourceid']:checked").each(function (){
			if($.trim($(this).attr('rel'))=='Y'){
				isOk = false;
			}
		});
		if(isOk){
			pageBarHandle("您确定要删除这些记录吗？","${baseUrl}/edu3/teaching/usualresultsrule/remove.html","#usualResultsRuleBody");
		} else {
			alertMsg.warn("不能删除正在使用的规则！");
		}		
	}
	
	function initUsualResultsRule(){
		//navTab.openTab('RES_TEACHING_ESTAB_USUALRESULTSRULE_INIT', '${baseUrl}/edu3/teaching/usualresultsrule/init.html', '生成基本规则');
		$.pdialog.open("${baseUrl}/edu3/teaching/usualresultsrule/init.html",'chooseCourse','生成基本规则',{mask:true,height:500,width:750});
	}
</script>
</body>
</html>