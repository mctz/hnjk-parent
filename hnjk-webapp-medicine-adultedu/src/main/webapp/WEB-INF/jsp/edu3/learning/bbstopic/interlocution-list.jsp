<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>随堂问答管理</title>
<script type="text/javascript">
	function goReply(bbsTopicId,courseId,status,type){
		/**	
		var url = "${baseUrl }/edu3/framework/isCourseTeacher.html";
		$.get(url,{courseId:courseId},function (isCourseTeacher){
			if(isCourseTeacher=='Y'){
				if(status==-1){
					alertMsg.warn("本帖已锁定，无法回复！");
				} else {
					if(type==0){
					var url = "${baseUrl}/edu3/metares/topicreply/bbsreply/input.html";
					navTab.openTab('RES_METARES_BBS_RREPLY_EDIT', url+"?from=interlocution&bbsTopicId="+bbsTopicId, '随堂问答回复');
					}
					else {
						var url = "${baseUrl}/edu3/framework/bbstopic/score/list.html?resourceid="+bbsTopicId;
						navTab.openTab('RES_METARES_BBS_TOPIC_SCORE', url, '帖子评分');	
					}
					
				}			
			} else {
				alertMsg.warn("当前用户不是课程老师，无法回答!");
			}
		});			
		**/
		if(status==-1){
			alertMsg.warn("本帖已锁定，无法回复！");
		} else {
			if(type==0){
			var url = "${baseUrl}/edu3/metares/topicreply/bbsreply/input.html";
			navTab.openTab('RES_METARES_BBS_RREPLY_EDIT', url+"?from=interlocution&bbsTopicId="+bbsTopicId, '随堂问答回复');
			}
			else {
				var orgUnitID = "${condition['orgUnitId']}";				
				var url = "${baseUrl}/edu3/framework/bbstopic/score/list.html?resourceid="+bbsTopicId+"&orgUnitID="+orgUnitID;
				navTab.openTab('RES_METARES_BBS_TOPIC_SCORE', url, '帖子评分');	
			}
		}		
	}	
	
	$(document).ready(function(){
		$("select[class*=flexselect]").flexselect();
	});

	
	//标记为FAQ
	function markFAQ(){
		//pageBarHandle("您确定要把这些问题标记为FAQ问题吗？","${baseUrl}/edu3/metares/topicreply/bbstopic/mark.html","#interlocutionBody");
		if(!isChecked('resourceid',"#interlocutionBody")){
 			alertMsg.warn('请选择一条要操作记录！');
			return false;
	 	}
		alertMsg.confirm("您确定要把这些问题标记为FAQ问题吗？<br/>关键字：<input type='text' id='faq_keywords' /><br/> <span style='color:green;'>(多个关键字以逗号隔开)</span>", {
				okCall: function(){//执行	
					var kws = $("#faq_keywords").val();
					var res = "";
					var k = 0;
					var num  = $("#interlocutionBody input[name='resourceid']:checked").size();
					$("#interlocutionBody input[@name='resourceid']:checked").each(function(){
	                        res+=$(this).val();
	                        if(k != num -1 ) res += ",";
	                        k++;
	                });
	                
					$.post("${baseUrl}/edu3/metares/topicreply/bbstopic/mark.html",{resourceid:res,keywords:kws}, navTabAjaxDone, "json");				
				}
		});
	}
	
	// 批量回复问题
	function batchReplyTopic() {
		var resourceIds = new Array();
		$("#interlocutionBody input[@name='resourceid']:checked").each(function(){
			resourceIds.push($(this).val());
		});
		if(resourceIds.length < 1){
			alertMsg.info("请选择一条记录！");
			return false;
		}
		var url = "${baseUrl }/edu3/metares/topicreply/bbstopic/batchReply.html";
		navTab.openTab('RES_METARES_BBS_TOPIC_REPLY', url+"?resourceIds="+resourceIds.toString(), '随堂问答批量回复');
	}
	//导出Excel
	function exportBbsTopicExcel(){
		$('#frame_exportBbsTopic').remove();
		var iframe = document.createElement("iframe");
		iframe.id = "frame_exportBbsTopic";
		
		var title         = $("#interlocution_title").val();
	//	var status 	  = $("#interlocution_status").val();
		var courseName 	  = $("#interlocution_courseName").val();
		var isAnswered 	  = $("#interlocution_isAnswered").val();
		var yearInfoId 	  = $("#interlocution_yearInfoId").val();
		var term = $("#interlocution_term").val();
		var classesId = $("#interlocution_list_classid").val(); 
		var orgUnitId		 = $("#interlocution_list_brSchoolName").val();
		var content		 = $("#interlocution_content").val();
		var fillinMan		 = $("#interlocution_fillinMan").val();
		var startLong		 = $("#interlocution_startLong").val();
		var endLong		 = $("#interlocution_endLong").val();
		iframe.src = "${baseUrl }/edu3/metares/bbsTopic/excel/export.html?title="
			+title+"&courseName="+courseName+"&isAnswered="+isAnswered+"&yearInfoId="+yearInfoId
			+"&term="+term+"&classesId="+classesId+"&orgUnitId="+orgUnitId				
			+"&content="+content+"&fillinMan="+fillinMan+"&startLong="+startLong+"&endLong="+endLong;
		//创建完成之后，添加到body中
		document.body.appendChild(iframe);
	}
</script>
</head>
<body>
	<div class="page">
		<div class="pageHeader">
			<form onsubmit="return navTabSearch(this);"
				action="${baseUrl }/edu3/metares/interlocution/bbstopic/list.html"
				method="post">
				<div class="searchBar">
					<ul class="searchContent">
						<li class="custom-li"><label>教学点：</label> <gh:brSchoolAutocomplete
								name="orgUnitId" tabindex="1"
								id="interlocution_list_brSchoolName"
								defaultValue="${condition['orgUnitId']}" displayType="Code"
								style="width:240px;" /></li>
						<li><label>年度：</label> <gh:selectModel
								id="interlocution_yearInfoId" name="yearInfoId"
								bindValue="resourceid" displayValue="yearName"
								modelClass="com.hnjk.edu.basedata.model.YearInfo"
								value="${condition['yearInfoId']}" orderBy="firstYear desc"
								style="width:55%;" /></li>
						<li><label>学期：</label>
						<gh:select id="interlocution_term" name="term"
								value="${condition['term']}" dictionaryCode="CodeTerm"
								style="width:55%;" /></li>
						<li><label>问题状态：</label> <select
							id="interlocution_isAnswered" name="isAnswered"
							style="width: 100px;">
								<option value="">请选择</option>
								<option value="0"
									${(condition['isAnswered'] eq 0)?'selected':'' }>未答复</option>
								<option value="1"
									${(condition['isAnswered'] eq 1)?'selected':'' }>已答复</option>
						</select></li>
						
					</ul>
					<ul class="searchContent">
						<li class="custom-li"><label>班级：</label> <gh:classesAutocomplete
								name="classesId" id="interlocution_list_classid" tabindex="1"
								displayType="code" defaultValue="${condition['classesId']}"
								exCondition="${classesCondition}" style="width:240px;" /></li>
						
							<%-- 
					<input type="text" name="courseName" value="${condition['courseName'] }"/> --%>
							<%-- ${interlocutionlistCourseSelect } --%></li>
						<li><label>标题：</label><input id="interlocution_title"
							type="text" name="title" value="${condition['title'] }"
							style="width: 55%;" /></li>
						<li><label>内容：</label><input id="interlocution_content"
							type="text" name="content" value="${condition['content'] }"
							style="width: 55%;" /></li>
					</ul>
					<ul class="searchContent">
						<li class="custom-li"><label>所属课程：</label> <gh:courseAutocomplete
								name="courseName" tabindex="1" id="interlocution_courseName"
								value="${condition['courseName']}" displayType="code"
								isFilterTeacher="Y" style="width:240px;" /> <input type="hidden"
							value="${condition['courseName']}" id="interlocution_courseName">
						<li><label>回复时间段：</label><input class=""
							id="interlocution_startLong" type="text" name="startLong"
							value="${condition['startLong'] }" style="width: 20%;"
							onkeyup="if(this.value.length==1){this.value=this.value.replace(/[^1-9]/g,'')}else{this.value=this.value.replace(/\D/g,'')}"
							onafterpaste="if(this.value.length==1){this.value=this.value.replace(/[^1-9]/g,'')}else{this.value=this.value.replace(/\D/g,'')}" />
							h 至 <input id="interlocution_endLong" type="text" name="endLong"
							value="${condition['endLong'] }" style="width: 20%;"
							onkeyup="if(this.value.length==1){this.value=this.value.replace(/[^1-9]/g,'')}else{this.value=this.value.replace(/\D/g,'')}"
							onafterpaste="if(this.value.length==1){this.value=this.value.replace(/[^1-9]/g,'')}else{this.value=this.value.replace(/\D/g,'')}" /> h
						</li>
						<li><label>提问人：</label><input id="interlocution_fillinMan"
							type="text" name="fillinMan" value="${condition['fillinMan'] }"
							style="width: 55%;" /></li>
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
			<gh:resAuth parentCode="RES_METARES_BBS_INTERLOCUTION"
				pageType="list"></gh:resAuth>
			<table class="table" layouth="185">
				<thead>
					<tr>
						<th width="3%"><input type="checkbox" name="checkall"
							id="check_all_interlocution"
							onclick="checkboxAll('#check_all_interlocution','resourceid','#interlocutionBody')"
							disabled="disabled" /></th>
						<th width="8%">教学点</th>
						<th width="7%">课程</th>
						<th width="13%">章节点</th>
						<th width="16%">标题</th>
						<th width="4%">提问人</th>
						<th width="12%">班级</th>
						<th width="6%">提问时间</th>
						<th width="3%">状态</th>
						<th width="4%">FAQ</th>
						<th width="4%">有效贴</th>
						<th width="4%">回复时长</th>
						<th width="5%">第一次回复人</th>
						<th width="7%"></th>
					</tr>
				</thead>
				<tbody id="interlocutionBody">
					<c:forEach items="${bbsTopicListPage.result }" var="bbsTopic"
						varStatus="vs">

						<%-- <c:if test="${bbsTopic.hasCheckAuthority==true }"> --%>
						<tr>
							<td><c:if test="${bbsTopic.hasCheckAuthority==true }">
									<input type="checkbox" name="resourceid"
										value="${bbsTopic.resourceid }" autocomplete="off" />
								</c:if></td>
							<td>${bbsTopic.unitName}</td>
							<td>${bbsTopic.course.courseName}</td>
							<td>${bbsTopic.syllabus.syllabusName}</td>

							<td><a href="javascript:;"
								onclick="window.open('${baseUrl }/edu3/learning/bbs/topic.html?topicId=${bbsTopic.resourceid }&courseId=${bbsTopic.course.resourceid }','course_bbs')">${bbsTopic.title }</a></td>
							<td>${bbsTopic.fillinMan}</td>
							<td>${bbsTopic.classes.classname}</td>
							<td><fmt:formatDate value="${bbsTopic.fillinDate }"
									pattern="yyyy-MM-dd HH:mm" /></td>
							<td style="text-align: center;"><c:choose>
									<c:when test="${bbsTopic.isAnswered eq 1 }">
										<img src="${baseUrl }/themes/default/images/icon_ok.png"
											title="已答复" />
									</c:when>
									<c:otherwise>
										<img src="${baseUrl }/themes/default/images/icon_time.png"
											title="未解决" />
									</c:otherwise>
								</c:choose></td>
							<td><c:choose>
									<c:when test="${fn:contains(bbsTopic.tags,'FAQ') }">是</c:when>
									<c:otherwise>否</c:otherwise>
								</c:choose></td>
							<td><c:choose>
									<c:when test="${bbsTopic.scoreType eq '1' }">
										<label style="color: green;">是</label>
									</c:when>
									<c:otherwise>否</c:otherwise>
								</c:choose></td>
							<td>${bbsTopic.howLongReply}</td>
							<td>${bbsTopic.firstReplyAccount}</td>
							<td><c:if test="${bbsTopic.hasCheckAuthority==true }">
									<a href="javascript:;"
										onclick="goReply('${bbsTopic.resourceid }','${bbsTopic.course.resourceid}',${bbsTopic.status },1);"
										style="color: blue;">答应问题</a>
								</c:if></td>
						</tr>
						<%--  	</c:if> --%>
					</c:forEach>
				</tbody>
			</table>
			<gh:page page="${bbsTopicListPage}"
				goPageUrl="${baseUrl}/edu3/metares/interlocution/bbstopic/list.html"
				condition="${condition }" pageType="sys" />

		</div>
	</div>
</body>
</html>