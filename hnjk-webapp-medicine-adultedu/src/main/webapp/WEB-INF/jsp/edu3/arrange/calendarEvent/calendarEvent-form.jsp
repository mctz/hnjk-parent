<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>院历事件管理</title>
<script language="javascript">
 $(document).ready(function(){
	 $("#sCalendarEvent_content").val("${sCalendarEvent.content}");
 });
</script>
</head>
<body>
	<h2 class="contentTitle">${(empty sCalendarEvent.resourceid)?'新增':'编辑' }院历事件</h2>
	<div class="page">
	<div class="pageContent">	
	<form method="post" action="${baseUrl}/edu3/arrange/schoolCalendarEvent/save.html" class="pageForm" onsubmit="return validateCallback(this);" id="sCalendarEventForm">
		<input type="hidden" name="resourceid" value="${sCalendarEvent.resourceid }"/>     
		<div class="pageFormContent" layoutH="97">
			<table class="form" id="sCalendarEventTable">
				<tr>
					<td width="20%">所属院历：(只能选择未发布的院历)</td>
					<td width="30%"><select class="flexselect" name="schoolCalendarName" style="width:50%;">${option}</select></td>
					
					<td width="20%">事件名称：</td>
					<td width="30%"><input type="text" name="name" style="width:50%" value="${sCalendarEvent.name }" class="required"/></td>
				</tr>
				<tr>
					<td>事件内容：</td>
					<td colspan="3"><textarea id="sCalendarEvent_content" name="content" style="width:50%" cols="1" rows="3">${sCalendarEvent.content }</textarea></td>
				</tr>
				<tr>
					<td colspan="3"><label>时间段开始：</label><input type="text"  name="startDate" size="40" style="width:20%" value="<fmt:formatDate value="${sCalendarEvent.startDate }" pattern="yyyy-MM-dd HH:mm:ss" />" class="required" 
							 onFocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowWeek:true})"/><label>&nbsp;&nbsp;&nbsp;</label>
					<label>时间段结束：</label><input type="text"  name="endDate" size="40" style="width:20%" value="<fmt:formatDate value="${sCalendarEvent.endDate }" pattern="yyyy-MM-dd HH:mm:ss" />" class="required" 
							 onFocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowWeek:true})"/></td>
					<td><label>类型：</label><gh:select name="type" value="${sCalendarEvent.type}" dictionaryCode="CodeCalendarEvent" style="width:50%;" /></td>
				</tr>
				<tr>
					<td colspan="3"><label>时间段开始：</label><input type="text"  name="startDate2" size="40" style="width:20%" value="<fmt:formatDate value="${sCalendarEvent.startDate2 }" pattern="yyyy-MM-dd HH:mm:ss" />" 
							 onFocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowWeek:true})"/><label>&nbsp;&nbsp;&nbsp;</label>
					<label>时间段结束：</label><input type="text"  name="endDate2" size="40" style="width:20%" value="<fmt:formatDate value="${sCalendarEvent.endDate2 }" pattern="yyyy-MM-dd HH:mm:ss" />" 
							 onFocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowWeek:true})"/></td>
					<td><label>类型：</label><gh:select name="type2" value="${sCalendarEvent.type2}" dictionaryCode="CodeCalendarEvent" style="width:50%;" /></td>
				</tr>
				<tr>
					<td colspan="3"><label>时间段开始：</label><input type="text"  name="startDate3" size="40" style="width:20%" value="<fmt:formatDate value="${sCalendarEvent.startDate3 }" pattern="yyyy-MM-dd HH:mm:ss" />" 
							 onFocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowWeek:true})"/><label>&nbsp;&nbsp;&nbsp;</label>
					<label>时间段结束：</label><input type="text"  name="endDate3" size="40" style="width:20%" value="<fmt:formatDate value="${sCalendarEvent.endDate3 }" pattern="yyyy-MM-dd HH:mm:ss" />" 
							 onFocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowWeek:true})"/></td>
					<td><label>类型：</label><gh:select name="type3" value="${sCalendarEvent.type3}" dictionaryCode="CodeCalendarEvent" style="width:50%;" /></td>
				</tr>
				<tr>
				</tr>
			</table>
		</div> 
		<div class="formBar">
			<ul>
				<li><div class="buttonActive"><div class="buttonContent">
					<button type="submit">提交</button>
					</div></div></li>
					<li><div class="button"><div class="buttonContent"><button type="button" class="close" onclick="navTab.closeCurrentTab();">取消</button></div></div></li>
			</ul>
		</div>
	</form>
	</div>
	</div>	
</body>
</html>