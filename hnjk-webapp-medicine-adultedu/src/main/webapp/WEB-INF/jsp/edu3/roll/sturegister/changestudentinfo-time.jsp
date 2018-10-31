<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>设置修改学籍信息开放时间</title>
<style type="text/css">
#my_studentInfo_stuFactFee td {
	text-align: center;
}

#my_studentInfo_stuFactFee th {
	text-align: center;
}
</style>
</head>
<body>
	<script type="text/javascript">
	function saveChangeForm(form){//保存回调 
		var openStartDate = $("#openStartDate").val();
		var openEndDate   = $("#openEndDate").val();
		var stus		  = $("#stus").val();
		var url			  = "${baseUrl}/edu3/register/studentinfo/saveDate.html?stus="+stus;
	    var param 		  = "&openStartDate="+openStartDate+"&openEndDate="+openEndDate;
		if(''==openStartDate){
			alertMsg.warn("您尚未设置开始时间。");
			return false; 
		}
		if(''==openEndDate){
			alertMsg.warn("您尚未设置结束时间。");
			return false; 
		}
		$.ajax({
			type:"post",
			url:url+param,
			dataType:"json",
			success:function(data){
			alertMsg.info("批量设置成功!");	
			}
		});
		$.pdialog.closeCurrent();
		return validateCallback(form,navTabAjaxDone);
	}
	
	function navTabAjaxDone(json){
		if(json.statusCode == "200"){
			alertMsg.correct(json.message);
		}else{
			alertMsg.error(json.message);
		}
	}

</script>
	<div class="page">
		<div class="pageContent">
			<form id="changeStuInfoTime" method="post"
				action="${baseUrl}/edu3/register/studentinfo/saveDate.html"
				onsubmit="return saveChangeForm(this);" class="pageForm">
				<div class="pageFormContent" layouth="21">
					<!-- tabs -->
					<div class="tabs">
						<div class="tabsContent" style="height: 100%;"
							items="${studentInfo }" var="studentInfo">
							<div class="tabs">
								<div class="tabsHeader">
									<div class="tabsHeaderContent">
										<ul>
											<li><a href="javascript:void(0)"><span>开放时间</span></a></li>
										</ul>
									</div>
								</div>
								<!-- 1 -->
								<div>
									<table id="change_student_info_table" class="form">
										<input type="hidden" name="resourceid"
											value="${studentInfo.resourceid }">
										<input type="hidden" name="stus" id="stus" value="${stus}">
										<tr>
											<td width="20%">开始时间:</td>
											<td width="30%"><input type="text"
												value="<fmt:formatDate value='${studentInfo.openStartDate}' pattern='yyyy-MM-dd HH:mm:ss'/>"
												name="openStartDate" id="openStartDateId" class="required"
												onFocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',maxDate:'#F{$dp.$D(\'openEndDateId\')}'})" /></td>
											<td width="20%">结束时间:</td>
											<td width="30%"><input
												value="<fmt:formatDate value='${studentInfo.openEndDate}' pattern='yyyy-MM-dd HH:mm:ss' />"
												type="text" name="openEndDate" id="openEndDateId"
												class="required" readonly="readonly"
												onFocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:'#F{$dp.$D(\'openStartDateId\')}'})" /></td>
										</tr>
									</table>
									<div class="formBar">
										<ul>
											<li><div class="buttonActive">
													<div class="buttonContent">
														<button type="submit">保存</button>
													</div>
												</div></li>
											<li><div class="button">
													<div class="buttonContent">
														<button type="button" class="close" href="#close">取消</button>
													</div>
												</div></li>
										</ul>
									</div>
								</div>
							</div>
						</div>
					</div>
					<!-- tabs end-->
				</div>
			</form>
		</div>
	</div>
</body>
</html>