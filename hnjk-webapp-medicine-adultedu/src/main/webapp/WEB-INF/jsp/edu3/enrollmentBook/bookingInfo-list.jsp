<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>招生预约报读信息</title>
<script type="text/javascript">
	//新增
	function addBookingInfo(){
		navTab.openTab('navTab', '${baseUrl}/edu3/enrollment/booking/edit.html', '新增招生预约报读信息');
	}
	
	//修改
	function modifyBookingInfo(){
		var url = "${baseUrl}/edu3/enrollment/booking/edit.html";
		if(isCheckOnlyone('resourceid','#bookingInfoBody')){
			navTab.openTab('_blank', url+'?resourceid='+$("#bookingInfoBody input[@name='resourceid']:checked").val(), '编辑招生预约报读信息');
		}			
	}
		
	//删除
	function deleteBookingInfo(){
		pageBarHandle("您确定要删除这些记录吗？","${baseUrl}/edu3/enrollment/booking/delete.html","#bookingInfoBody");
	}
	
	// 下载模板
	function downloadBookingTemplate() {
		var url = "${baseUrl}/edu3/enrollment/booking/downloadTemplate.html";
		downloadFileByIframe(url, "downloadBookingTemplate");
	}
	
	// 批量导入招生预约报读学生信息
	function batchImportBookingInfo() {
		var url = "${baseUrl}/edu3/enrollment/booking/batchImport-view.html";
		$.pdialog.open(url,"RES_ENROLLMENT_BOOKING_INFOLIST_BATCHIMPORT","批量导入招生预约报读学生",{width:700, height:550});
	}
	
	// 勾选或按查询条件导出
	function exportBookingInfo() {
		var url = "${baseUrl}/edu3/enrollment/booking/export.html";
		var unitId;
		var gradeId;
		var classicId;
		var majorId;
		var studentName;
		var certNum;
		var operatorName;
		var resourceids = new Array();
		$("#bookingInfoBody input[name='resourceid']:checked").each(function(){
			var $infoObj = $(this);
			resourceids.push($infoObj.val());
		});
		
		if(resourceids.length<1){
			unitId = $("#bookingInfo_list_unitId").val();
			gradeId = $("#bookingInfo_list_gradeId").val();
			classicId = $("#bookingInfo_list_classicId").val();
			majorId = $("#bookingInfo_list_majorId").val();
			studentName = $("#bookingInfo_list_studentName").val();
			certNum = $("#bookingInfo_list_certNum").val();
			operatorName = $("#bookingInfo_list_operatorName").val();
		}
		
		url +="?resourceids="+resourceids.join(",")+"&unitId="+unitId+"&gradeId="+gradeId+"&classicId="+classicId+"&majorId="+majorId
			 +"&studentName="+studentName+"&certNum="+certNum+"&operatorName="+operatorName
		
		downloadFileByIframe(url, "exportBookingInfoIframe");
	}
	
</script>
</head>
<body>

	<div class="page">
		<div class="pageHeader">
			<form onsubmit="return navTabSearch(this);"
				action="${baseUrl }/edu3/enrollment/booking/list.html" method="post">
				<div class="searchBar">
					<ul class="searchContent">
						<c:if test="${condition['isAdmin'] eq 'Y' }">
							<li>
								<label>教学点：</label>
								<gh:brSchoolAutocomplete name="unitId"  tabindex="2"
											id="bookingInfo_list_unitId" defaultValue="${condition['unitId']}" displayType="code" style="width:60%;" />
							</li>
						</c:if>
						<li>
							<label>年级：</label>
							<gh:selectModel name="gradeId" bindValue="resourceid" displayValue="gradeName" id="bookingInfo_list_gradeId"
											orderBy="yearInfo.firstYear desc,term desc,resourceid desc"
											modelClass="com.hnjk.edu.basedata.model.Grade" style="width:55%;"  value="${condition['gradeId']}" />
						</li>
						<li>
							<label>层次：</label>
							<gh:selectModel name="classicId" bindValue="resourceid" displayValue="classicName"
											value="${condition['classicId']}" style="width:55%;"  id="bookingInfo_list_classicId"
											modelClass="com.hnjk.edu.basedata.model.Classic" /> 
						</li>
						<li>
							<label>操作人：</label>
							<input type="text"  name="operatorName" style="width: 55%" value="${condition['operatorName']}" id="bookingInfo_list_operatorName"/>
						</li>
					</ul>
					<ul class="searchContent">
						<li>
							<label>专业：</label>
							<gh:selectModel name="majorId" bindValue="resourceid" id="bookingInfo_list_majorId"
											displayValue="majorCodeName"  modelClass="com.hnjk.edu.basedata.model.Major"
											value="${condition['majorId']}" orderBy="majorCode,majorName" style="width:60%;" />
						</li>
						<li>
							<label>学生姓名：</label>
							<input type="text"  name="studentName" style="width: 55%" value="${condition['studentName']}" id="bookingInfo_list_studentName"/>
						</li>
						<li>
							<label>证件号码：</label>
							<input type="text"  name="certNum" style="width: 55%" value="${condition['certNum']}"  id="bookingInfo_list_certNum"/>
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
			<gh:resAuth parentCode="RES_ENROLLMENT_BOOKING_INFOLIST" pageType="list"></gh:resAuth>
			<table id="bookingInfoTab" class="table" layouth="168">
				<thead>
					<tr>
						<th width="5%">
							<input type="checkbox" name="checkall"  id="check_all_bookingInfo"
								onclick="checkboxAll('#check_all_bookingInfo','resourceid','#bookingInfoBody')" />
						</th>
						<th width="18%">教学点</th>
						<th width="8%">年级</th>
						<th width="8%">层次</th>
						<th width="18%">专业</th>
						<th width="8%">姓名</th>
						<th width="15%">身份证号</th>
						<th width="12%">联系电话</th>
						<th width="8%">操作人</th>
					</tr>
				</thead>
				<tbody id="bookingInfoBody">
					<c:forEach items="${infoList.result}" var="info" varStatus="vs">
						<tr>
							<td>
								<input type="checkbox" name="resourceid" value="${info.resourceid }" autocomplete="off" />
							</td>
							<td>${info.unit.unitName }</td>
							<td>${info.grade.gradeName }</td>
							<td>${info.classic.classicName }</td>
							<td>${info.major.majorName }</td>
							<td>${info.studentName }</td>
							<td>${info.certNum }</td>
							<td>${info.phone }</td>
							<td>${info.operator.cnName }</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<gh:page page="${infoList}" goPageUrl="${baseUrl }/edu3/enrollment/booking/list.html" pageType="sys" condition="${condition}" />
		</div>
	</div>

</body>
</html>