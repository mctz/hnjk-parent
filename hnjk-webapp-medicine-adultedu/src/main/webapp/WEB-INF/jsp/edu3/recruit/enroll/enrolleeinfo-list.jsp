<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>报名管理</title>

</head>
<body>
	<script type="text/javascript">
	
	
    //现场报名
	function enrollee_add(){
		var url = "${baseUrl}/edu3/recruit/enroll/enrolleeinfo-edit.html";
		navTab.openTab('RES_RECRUIT_ENROLLEE_EDIT', url, '现场报名');
	}
    //教学站现场报名
   // function brschool_enrollee_add(){
    //	var url = "${baseUrl}/edu3/recruit/enroll/enrolleeinfo-edit.html";
	//	navTab.openTab('RES_RECRUIT_BRSCHOOL_ENROLLEE_ADD', url, '现场报名');
    //}
	//修改学生报名信息
	function enrollee_modify(id,type){
		var url = "${baseUrl}/edu3/recruit/enroll/enrolleeinfo-edit.html";
		if(type==='')
			navTab.openTab('RES_RECRUIT_ENROLLEE_EDIT', url+'?resourceid='+id, '修改学生报名信息');
		else
			navTab.openTab('RES_RECRUIT_ENROLLEE_EDIT', url+'?resourceid='+id, '核对网上报名信息');
	}
	//重新报名
	//function enrollee_re(){
	//	var url = "${baseUrl}/edu3/recruit/enroll/reenrolleeinfo.html";
	//	if(isCheckOnlyone('resourceid','#enrolmanageBody')){
	//		navTab.openTab('RES_RECRUIT_ENROLLEE_RE', url+'?resourceid='+$("#enrolmanageBody input[@name='resourceid']:checked").val(), '重新报名');
	//	}			
	//}
	//老生申报新专业
	function enrollee_addNewMajor(){
		var url = "${baseUrl}/edu3/recruit/enroll/reenrolleeinfo.html";
		if(isCheckOnlyone('resourceid','#enrolmanageBody')){
			navTab.openTab('RES_RECRUIT_ENROLLEE_NEWMAJOR', url+'?act=addNewMajor&resourceid='+$("#enrolmanageBody input[@name='resourceid']:checked").val(), '老生申报新专业');
		}	
	}
	
	//删除报名信息
  	function enrollee_del(){
		var isCanDelete = true;
		//校验学生是否能删除  		
  		$("#enrolmanageBody input[@name='resourceid']:checked").each(function(){  			
  			if($(this).attr("title") == 'Y'){
  				alertMsg.warn("学生："+$(this).attr("enrolleename")+"已通过资格审核，不能删除！");
  				isCanDelete = false;
  				return false;
  			}          
        })        
  		if(isCanDelete){
  			pageBarHandle("您确定要将选择的学生信息删除吗？","${baseUrl}/edu3/recruit/enroll/delenrolleeinfo.html","#enrolmanageBody");
  		}
     	
	}
	
	//显示审核信息
	function showAuditStatus(enroolleId){
		jQuery("#"+enroolleId).show();	
	}
	//隐藏审核信息
	function hiddenAuditStatus(enroolleId){
		jQuery("#"+enroolleId).hide();	
	}
	//自定义导出报名信息
	function enrolleeInfoExport(){
	
		var url			 = "${baseUrl}/edu3/framework/filemanage/showcustomerexport.html";
		var branchSchool = jQuery("#eiinfo_brSchoolName").val();
		var recruitPlan  = jQuery("#_eiinfo_recruitPlan").val();
		var major		 = jQuery("#major").val();
		if(recruitPlan==""){
			alertMsg.warn("请选择招生批次!");
		 	return;
		}
		url				  =url+"?branchSchool="+branchSchool
		                  +"&recruitPlan="+recruitPlan
		                  +"&major="+major
		                  +"&excelModel=enrolleeInfo"
		                  +"&exportRequestPath=/edu3/recruit/enrolleeinfo/enrolleeinfo-export.html";
		                    
		$.pdialog.open(url,"RES_RECRUIT_ENROLLEE_EXPORT","自定义导出", {width:800, height:600});
	};

	//打印报名报表    
	function examExcusedPrint(){
		if(isChecked('resourceid','#enrolmanageBody')){
			if(isCheckOnlyone('resourceid','#enrolmanageBody')){
				var enrolleeinfoid =  $("#enrolmanageBody input[name='resourceid']:checked").val();				
				url = "${baseUrl}/edu3/recruit/enroll/printexamexcused-view.html?enrolleeinfoid="+enrolleeinfoid;
				$.pdialog.open(url,'RES_RECRUIT_ENROLLEE_PRINT_VIEW','打印预览',{height:600, width:800});
			}
		}else{
			$.pdialog.open("${baseUrl}/edu3/recruit/enroll/printexamexcused-condition.html","RES_RECRUIT_ENROLLEE_PRINT","选择条件",{height:100, width:150,mask:true});
		}
		
	}

	
	//打印报名汇总表
	function enrolleeinfoReportPrint(){
		var url = "${baseUrl}/edu3/recruit/enroll/print/statenrolleeinforeport-condition.html";
		$.pdialog.open(url,"RES_RECRUIT_ENROLLEE_STATREPORT_PRINT","条件选择", {width:800, height:400});
	}
	
	//导出报名汇总表
	function enrolleeinfoReportExport(){
		var url = "${baseUrl}/edu3/recruit/enroll/export/statenrolleeinfo-condition.html";
		$.pdialog.open(url,"RES_RECRUIT_ENROLLEE_STATREPORT_EXPORT_CONDITION","条件选择", {width:800, height:400});
	}
	
	//图片采集
	function enrolleeinfoPhotoCapture(){
		if(isCheckOnlyone('resourceid','#enrolmanageBody')){
			var url = "${baseUrl}/edu3/recruit/enroll/enrolleeinfo-webcam.html";
			$.pdialog.open(url+"?enrolleeinfoId="+$("#enrolmanageBody input[@name='resourceid']:checked").val(),"RES_RECRUIT_ENROLLEE_PHOTOCAPTURE","图像采集", {width:800, height:380});
			
		}			
		
	}
</script>
	<div class="page">
		<div class="pageHeader">
			<form onsubmit="return navTabSearch(this);"
				action="${baseUrl }/edu3/recruit/enroll/enrolleeinfo-list.html"
				method="post">
				<div class="searchBar">
					<ul class="searchContent">
						<!-- <input type="hidden" id="eiinfo_brSchoolId" name="branchSchool" size="36" value="${condition['branchSchool']}"/> -->
						<input type="hidden" id="major" name="major"
							value="${condition['major']}" />
						<c:if test="${empty condition['brshSchool']}">
							<li><label>教学站：</label> <gh:brSchoolAutocomplete
									name="branchSchool" tabindex="1" id="eiinfo_brSchoolName"
									defaultValue="${condition['branchSchool']}" /></li>
						</c:if>
						<li style="width: 280px"><label>招生批次：</label> <gh:recruitPlanAutocomplete
								name="recruitPlan" tabindex="1" id="_eiinfo_recruitPlan"
								value="${condition['recruitPlan']}" style="width:180px" /></li>

						<li><label>准考证号：</label> <input type="text"
							name="examCertificateNo"
							value="${condition['examCertificateNo']}" /></li>
						<li><span class="tips">提示：更多查询条件请点击高级查询</span></li>

					</ul>
					<div class="subBar">
						<ul>
							<li><div class="buttonActive">
									<div class="buttonContent">
										<button type="submit">查询</button>
									</div>
								</div></li>
							<li><a class="button"
								href="${baseUrl }/edu3/recruit/enroll/enrolleeinfo-list.html?con=advance"
								target="dialog" width="600" height="400"
								rel="RES_RECRUIT_ENROLLEE_LIST" title="查询框"><span>高级查询</span></a></li>
						</ul>
					</div>
				</div>
			</form>
		</div>
		<div class="pageContent" layoutH="20">

			<gh:resAuth parentCode="RES_RECRUIT_ENROLLEE_LIST" pageType="list"></gh:resAuth>

			<table class="table" layouth="138">
				<thead>
					<tr>
						<th width="5%"><input type="checkbox" name="checkall"
							id="check_all_enrolmanage"
							onclick="checkboxAll('#check_all_enrolmanage','resourceid','#enrolmanageBody')" /></th>
						<th width="10%">招生批次</th>
						<th width="17%">教学站</th>
						<th width="17%">专业</th>
						<th width="8%">层次</th>
						<th width="8%">姓名</th>
						<th width="10%">准考证号</th>
						<th width="10%">证件号码</th>
						<th width="5%">报名类型</th>
						<th width="5%">审核状态</th>
						<th width="5%">操作</th>
					</tr>
				</thead>
				<tbody id="enrolmanageBody">
					<c:forEach items="${eilist.result}" var="enrolleeinfo"
						varStatus="vs">
						<tr>
							<td><input type="checkbox" name="resourceid"
								title="${enrolleeinfo.entranceflag }"
								enrolleename="${enrolleeinfo.studentBaseInfo.name }"
								value="${enrolleeinfo.resourceid }" autocomplete="off" /></td>
							<td align="left">${enrolleeinfo.recruitMajor.recruitPlan.recruitPlanname}</td>
							<td align="left">${enrolleeinfo.branchSchool.unitName }</td>
							<td align="left">${enrolleeinfo.recruitMajor.major.majorName }</td>
							<td align="left">${enrolleeinfo.recruitMajor.classic.classicName }</td>
							<td align="left">${enrolleeinfo.studentBaseInfo.name }</td>
							<td align="left">${enrolleeinfo.examCertificateNo }</td>
							<td align="left">${enrolleeinfo.studentBaseInfo.certNum }</td>
							<td align="left">${ghfn:dictCode2Val('CodeEnrolleeType',enrolleeinfo.enrolleeType)}</td>
							<td onmouseover="showAuditStatus('${enrolleeinfo.resourceid }');"
								onmouseout="hiddenAuditStatus('${enrolleeinfo.resourceid }');"
								align="left">

								<div id="${enrolleeinfo.resourceid }"
									style="background-color: #B8D0D6; position: absolute; text-align: left; display: none; width: 100px; height: 64px; border-style: solid; border-width: 1px; border-color: #D0D0D0;">
									<p>
										报名资格：
										<c:choose>
											<c:when test="${enrolleeinfo.signupFlag != null}">
												<font
													<c:if test="${enrolleeinfo.signupFlag eq 'Y'}">color="green"</c:if>
													<c:if test="${enrolleeinfo.signupFlag eq 'N'}">color="red"</c:if>>
													${ghfn:dictCode2Val('CodeAuditStatus',enrolleeinfo.signupFlag)}
												</font>
											</c:when>
											<c:otherwise>
												<font color="#183152">未审核</font>
											</c:otherwise>
										</c:choose>
									</p>
									<p>
										免试资格：
										<c:choose>
											<c:when
												test="${enrolleeinfo.isApplyNoexam == null  || enrolleeinfo.isApplyNoexam eq 'N'  }">
												<font color="#183152">未申请</font>
											</c:when>
											<c:when
												test="${enrolleeinfo.isApplyNoexam eq 'Y' && enrolleeinfo.noExamFlag!=null }">
												<font
													<c:if test="${enrolleeinfo.noExamFlag eq 'Y'}">color="green"</c:if>
													<c:if test="${enrolleeinfo.noExamFlag eq 'N'}">color="red"</c:if>>
													${ghfn:dictCode2Val('CodeAuditStatus',enrolleeinfo.noExamFlag )}
												</font>
											</c:when>
											<c:otherwise>
												<font color="#183152">未审核</font>
											</c:otherwise>
										</c:choose>
									</p>
									<p>
										入学资格：
										<c:choose>
											<c:when test="${enrolleeinfo.entranceflag != null}">
												<font
													<c:if test="${enrolleeinfo.entranceflag eq 'Y'}">color="green"</c:if>
													<c:if test="${enrolleeinfo.entranceflag eq 'N'}">color="red"</c:if>>
													${ghfn:dictCode2Val('CodeAuditStatus',enrolleeinfo.entranceflag )}
												</font>
											</c:when>
											<c:otherwise>
												<font color="#183152">未审核</font>
											</c:otherwise>
										</c:choose>
									</p>
								</div>
							</td>
							<td><c:if
									test="${enrolleeinfo.enrolleeType==0 and ghfn:hasAuth('RES_RECRUIT_ENROLLEE_EDIT')}">
									<a class="edit" href="#"
										onclick="enrollee_modify('${enrolleeinfo.resourceid}','')"><span>修改</span></a>
								</c:if> <c:if
									test="${enrolleeinfo.enrolleeType==1 and ghfn:hasAuth('RES_RECRUIT_ENROLLEE_CHECKINFO')}">
									<a class="icon" href="#"
										onclick="enrollee_modify('${enrolleeinfo.resourceid}','web');"><span>核对</span></a>
								</c:if></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<gh:page page="${eilist}"
				goPageUrl="${baseUrl }/edu3/recruit/enroll/enrolleeinfo-list.html"
				pageType="sys" condition="${condition}" />
		</div>
	</div>
</body>
</html>
