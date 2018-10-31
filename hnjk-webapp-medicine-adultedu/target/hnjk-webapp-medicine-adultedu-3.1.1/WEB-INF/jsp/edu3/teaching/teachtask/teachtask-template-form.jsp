<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>教学任务书模板</title>
</head>
<body>
	<h2 class="contentTitle">教学任务书模板</h2>
	<div class="page">
		<div class="pageContent">
			<form method="post"
				action="${baseUrl}/edu3/teaching/teachtask/template/save.html"
				class="pageForm" onsubmit="return validateCallback(this);">
				<input type="hidden" name="resourceid"
					value="${teachTask.resourceid }" />
				<div class="pageFormContent" layoutH="97">
					<table class="form">
						<tr>
							<td width="20%">年度:</td>
							<td width="30%"><gh:selectModel name="yearInfoId"
									bindValue="resourceid" displayValue="yearName"
									style="width:60%"
									modelClass="com.hnjk.edu.basedata.model.YearInfo"
									value="${teachTask.yearInfo.resourceid}"
									orderBy="firstYear desc" classCss="required" /> <span
								style="color: red;">*</span></td>
							<td width="20%">学期:</td>
							<td width="30%"><gh:select name="term"
									value="${teachTask.term}" dictionaryCode="CodeTerm"
									style="width:60%" classCss="required" /><span
								style="color: red;">*</span></td>
						</tr>
					</table>
					<table class="form">
						<tr>
							<td><span class="buttonActive"><div
										class="buttonContent">
										<button type="button" onclick="_addTaskTemplateDetail()">
											新 增</button>
									</div></span> <span class="buttonActive"><div class="buttonContent">
										<button type="button" onclick="_delTaskTemplateDetail()">
											删 除</button>
									</div></span></td>
						</tr>
					</table>
					<table class="form" id="_teachTaskTemplateDetailBody">
						<tr>
							<td style="width: 4%"><input type="checkbox" name="checkall"
								id="check_all_teachTaskTemplateDetail"
								onclick="checkboxAll('#check_all_teachTaskTemplateDetail','c_id','#_teachTaskTemplateDetailBody')" /></td>
							<td style="width: 4%">序号</td>
							<td style="width: 12%">开始时间</td>
							<td style="width: 12%">结束时间</td>
							<td style="width: 10%">教学活动</td>
							<td style="width: 39%">教学任务内容</td>
							<td style="width: 7%">是否允许修改</td>
							<td style="width: 12%">预警时间</td>
						</tr>
						<c:forEach items="${teachTask.teachTaskDetails }" var="c"
							varStatus="vs">
							<tr>
								<td><input type='checkbox' name='c_id'
									value='${c.resourceid }' autocomplete="off"> <input
									type="hidden" name='tk_id' value='${c.resourceid }'></td>
								<td><input type="text" name="showOrder"
									value="${c.showOrder }" class="required" style='width: 90%' /></td>
								<td><input type='text' name='startTime'
									value="<fmt:formatDate value='${c.startTime }' pattern='yyyy-MM-dd HH:mm:ss'/>"
									style='width: 95%' class='required'
									onFocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" /></td>
								<td><input type='text' name='endTime'
									value="<fmt:formatDate value='${c.endTime }' pattern='yyyy-MM-dd HH:mm:ss'/>"
									style='width: 95%' class='required'
									onFocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" /></td>
								<td><gh:select name='taskType'
										dictionaryCode='CodeTaskType' classCss='required'
										style='width:90%' value='${c.taskType }' /><span
									style="color: red;">*</span></td>
								<td><textarea rows='5' name='taskContent'
										style='width: 95%' class='required'>${c.taskContent }</textarea></td>
								<td><gh:select name='isAllowModify'
										dictionaryCode='yesOrNo' classCss='required' style='width:90%'
										value='${c.isAllowModify }' /><span style="color: red;">*</span></td>
								<td><input type='text' name='warningTime'
									value="<fmt:formatDate value="${c.warningTime }" pattern='yyyy-MM-dd HH:mm:ss'/>"
									style='width: 95%'
									onFocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" /></td>
							</tr>
						</c:forEach>
					</table>
				</div>
				<div class="formBar">
					<ul>
						<li><div class="buttonActive">
								<div class="buttonContent">
									<button type="submit">提交</button>
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
	<script type="text/javascript">	   
    function _addTaskTemplateDetail(){    	
    	var rowNum = jQuery("#_teachTaskTemplateDetailBody").get(0).rows.length;
    	var html = "<tr><td><input type='checkbox' name='c_id' value=''><input type='hidden' name='tk_id' value=''></td>"+
    		"<td><input type='text' name='showOrder' value='"+rowNum+"' style='width:90%'/></td>"+
    		"<td><input type='text' name='startTime' value='' style='width:95%' class='required' onFocus=\"WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})\"></td>"+
			"<td><input type='text' name='endTime' value='' style='width:95%' class='required' onFocus=\"WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})\"></td>"+
			"<td><gh:select name='taskType' dictionaryCode='CodeTaskType' style='width:90%' value=''/><span style='color: red;'>*</span></td>"+
			"<td><textarea rows='5' name='taskContent' style='width:95%' class='required'></textarea></td>"+
			"<td><gh:select name='isAllowModify' dictionaryCode='yesOrNo' style='width:90%' value=''/><span style='color: red;'>*</span></td>"+
			"<td><input type='text' name='warningTime' value='' style='width:95%' onFocus=\"WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})\"></td>"+
			"</tr>";
		jQuery("#_teachTaskTemplateDetailBody").append(html);
    }
    
    function _delTaskTemplateDetail(){
    	var ids = new Array();
    	$("#_teachTaskTemplateDetailBody input[name='c_id']:checked").each(function(ind){		  	
		  	ids.push($(this).val());
		  	$(this).parent().parent().remove();
		});
		if(ids.length>0){
			var url = "${baseUrl}/edu3/teaching/teachtask/deleteDetail.html";
			$.get(url, {c_id:ids.toString()});
		}
		$("#_teachTaskTemplateDetailBody input[name=showOrder]").each(function(ind){    		
    		$(this).val(ind+1);    		   	
    	});
    }
</script>
</body>
</html>