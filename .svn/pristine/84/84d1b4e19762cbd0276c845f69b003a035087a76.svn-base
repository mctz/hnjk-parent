<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>消息管理</title>
<style>
.type {
	text-align: center;
	line-height: 28px;
}
</style>
<script type="text/javascript">
function viewStudentInfo(studyNo){
	var url = "${baseUrl}/edu3/framework/studentinfo/view.html";
	if(studyNo!=''){
		$.pdialog.open(url+'?studyNo='+studyNo, 'RES_SCHOOL_SCHOOLROLL_MANAGER_VIEW', '查看学籍', {width: 800, height: 600});
	}
}	
function auditConfirmMSG(){//审核
	var resourceid = [];
	var studyNo = [];
	$("#confirmMSGBody input[name='resourceid']:checked").each(function(){
		var checekObj = $(this);
		//alert(checekObj.toString());
		if(checekObj.attr("studyNo")!='' && checekObj.attr("status")!='Y'){
            studyNo.push(checekObj.attr("studyNo"));
			resourceid.push(checekObj.val());
		}
	});
	if(resourceid.length<=0){
		alertMsg.warn("必须勾选未审核的记录进行审核！");
		return false;
	}
	$.ajax({
		type:'POST',
		url:"${baseUrl}/edu3/schoolroll/loginConfirmMSG/audit.html",
		data:{studyNo:studyNo.toString(),resourceid:resourceid.toString()},
		dataType:"json",
		cache: false,
		error: DWZ.ajaxError,
		success: function(data){
			if(data.statusCode===200){
				alertMsg.correct(data.message);
			}else{
				alertMsg.error(data.message);
			}
		}
	});
}

function rollbackMSG() {
	alertMsg.warn("该功能暂未开放，请联系管理员！");
}

function exportMSG() {
    var url = "${baseUrl}/edu3/schoolroll/loginConfirmMSG/list.html?flag=export&"+$("#confirmMSGForm").serialize();
    downloadFileByIframe(url,'loginConfirmMSG_Ifram',"post");
}

function viewContent(resourceid) {
    var url = "${baseUrl}/edu3/schoolroll/loginConfirmMSG/view.html";
    $.pdialog.open(url+'?resourceid='+resourceid, 'RES_SCHOOL_SCHOOLROLL_MANAGER_VIEW', '查看修改内容', {width: 500, height: 300});
}
</script>
</head>
<body>

	<div class="page" >
		<div class="pageHeader">
			<form onsubmit="return navTabSearch(this);"
				action="${baseUrl }/edu3/schoolroll/loginConfirmMSG/list.html" method="post"
				id="confirmMSGForm">
				<div class="searchBar">
					<ul class="searchContent">
						<li><label>申请人：</label><input type="text" name="senderName"
							value="${condition['senderName']}" style="width: 50%" /></li>
						<li><label>审核结果：</label>
						<gh:select dictionaryCode="yesOrNo" name="status"
								value="${condition['status']}" style="width: 52%" /></li>
					</ul>
					<ul class="searchContent">
						<li class="custom-li"><label>申请时间：</label><input type="text" name="startDate"
							value="${condition['startDate']}" class="Wdate" id="msgStartDate"
							onFocus="WdatePicker({maxDate:'#F{$dp.$D(\'msgEndDate\')}',dateFmt:'yyyy-MM-dd'})"
							style="width: 80px;" /> 至：
							<input type="text" name="endDate"
							value="${condition['endDate']}" class="Wdate" id="msgEndDate"
							onFocus="WdatePicker({minDate:'#F{$dp.$D(\'msgStartDate\')}',dateFmt:'yyyy-MM-dd'})"
							style="width: 80px;" />
						</li>
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
			<gh:resAuth parentCode="RES_SCHOOL_SCHOOLROLL_CONFIRMMSG" pageType="list" />
			<table class="table" layouth="163">
				<thead>
					<tr>
						<th width="3%"><input type="checkbox" id="check_all_messages" name="checkall"
							onclick="checkboxAll('#check_all_messages','resourceid','#confirmMSGBody')" /></th>
						<th width="20%">标题</th>
						<th width="8%">申请人</th>
						<th width="12%">申请时间</th>
						<th width="30%">修改内容</th>
						<%--<th width="5%">是否阅读</th>--%>
						<th width="5%">审核通过</th>
					</tr>
				</thead>
				<tbody id="confirmMSGBody">
					<c:forEach items="${messagePage.result}" var="message"
						varStatus="s">
						<tr>
							<td><input type="checkbox" name="resourceid" value="${message.resourceid }"
									   studyNo="${message.studyNo }" status="${message.status}" autocomplete="off" /></td>
							<td>${message.msgTitle }</td>
							<td><a href="#" onclick="viewStudentInfo('${message.studyNo}')" title="点击查看"></a>${message.senderName }</td>
							<td><fmt:formatDate
									value="${message.sendTime }"
									pattern="yyyy-MM-dd HH:mm:ss" /></td>
							<c:if test="${fn:endsWith(message.content,'的学生')}">
								<td></td>
							</c:if>
							<c:if test="${ not fn:endsWith(message.content,'的学生')}">
								<td title="${fn:replace(message.title, ",", "&#13;&#10;")}" onclick="viewContent('${message.resourceid}')">${fn:replace(fn:replace(message.content,"green","red"),"<br/>"," ") }</td>
							</c:if>
							<%--<td>${ghfn:dictCode2Val('yesOrNo',message.isReply) }</td>--%>
							<td <c:if test="${message.status == 'Y'}">style='color:blue'</c:if>
								<c:if test="${message.status != 'Y'}">style='color:red'</c:if>
							>${ghfn:dictCode2Val('yesOrNo',message.status) }</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<gh:page page="${messagePage}"
				goPageUrl="${baseUrl }/edu3/schoolroll/loginConfirmMSG/list.html"
				condition="${condition }" pageType="sys" />
		</div>
	</div>
</body>