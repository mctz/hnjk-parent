<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>组织单位管理</title>
</head>
<body>
	<script type="text/javascript">
   	$(document).ready(function(){
   		$("#unitForm #_unitType").bind("change", function(){
   			if($("#unitForm #_unitType").val() == 'brSchool'){
   				$("#_brschoolType").css({"display":"block"});
   			}else{
   				$("#_brschoolType").css({"display":"none"});
   			}
		  	
		});
   	});
   	$("#unitCode").change(function(){
   		var unitCodeInput = $("#unitCode").val();
   		$("#unitCode4StuNo").text(unitCodeInput)
   		.val(unitCodeInput);
   	});
    	
    function validateOnlyUnit(){
    	var userid = jQuery("#unitForm #resourceid");
    	var unitCode = jQuery("#unitForm #unitCode");    	
    	if(unitCode.val()==""){ alertMsg.warn("请输入组织单位编码"); unitCode.focus() ;return false; }
    	var url = "${baseUrl}/edu3/framework/system/orgunit/validateCode.html";
    	jQuery.post(url,{unitCode:unitCode.val(),resourceid:userid.val()},function(data){
    		if(data == "exist"){ alertMsg.warn("组织单位编码已存在!"); }else{ alertMsg.correct("恭喜，此组织单位编码可用！")}
    	})
    }
   
    function openSelectorUnitTree(){
 	var defaultValues = $("#unitForm #parentId").val();
 	orgUnitSelector('parentId','parentName','radio','${rootCode}',defaultValues);
 }
</script>
	<h2 class="contentTitle">编辑单位</h2>
	<div class="page">
		<div class="pageContent">
			<form method="post" action="${baseUrl}/edu3/system/orgunit/save.html"
				class="pageForm" id="unitForm"
				onsubmit="return validateCallback(this);">
				<div class="pageFormContent" layoutH="97">
					<div class="tabs">
						<div class="tabsHeader">
							<div class="tabsHeaderContent">
								<ul>
									<li class="selected"><a href="#"><span>基本信息</span></a></li>
									<li><a href="#"><span>详细信息</span></a></li>
								</ul>
							</div>
						</div>
						<div class="tabsContent" style="height: 100%;">
							<div>
								<table class="form">
									<tr>
										<td style="width: 12%">组织单位编码:</td>
										<td style="width: 38%"><input type="text" id="unitCode"
											name="unitCode" style="width: 50%"
											value="${orgunit.unitCode }" class="required alphanumeric"
											alt="请输入字母下划线或数字的组合" /> <span class="buttonActive"
											style="margin-left: 8px"><div class="buttonContent">
													<button type="button" onclick="validateOnlyUnit()">检查唯一性</button>
												</div></span></td>
										<td style="width: 12%">组织单位名称:</td>
										<td style="width: 38%"><input type="text" name="unitName"
											style="width: 50%" value="${orgunit.unitName }"
											class="required" /></td>
									</tr>
									<tr>
										<td>组织单位简称:</td>
										<td><input type="text" name="unitShortName"
											style="width: 50%" value="${orgunit.unitShortName }"
											class="required" /></td>
										<td>父单位:</td>
										<td><input type="text" style="width: 50%" id="parentName"
											value="${parentOrg.unitShortName }" readonly="readonly" /> <input
											type="hidden" name="parentId" id="parentId"
											value="${parentOrg.resourceid }" /> <span
											class="buttonActive" style="margin-left: 8px">
												<div class="buttonContent">
													<button type="button" onclick="openSelectorUnitTree();">选择组织单位</button>
												</div>
										</span></td>
									</tr>
									<tr>
										<td>组织类型:</td>
										<td><select name="unitType" id="_unitType" size="1"
											style="width: 52%">
												<option value="localDepart"
													<c:if test="${orgunit.unitType eq 'localDepart'}">selected="selected"</c:if>>本部部门</option>
												<option value="virtulDepart"
													<c:if test="${orgunit.unitType eq 'virtulDepart'}">selected="selected"</c:if>>虚拟组织</option>
												<option value="brSchool"
													<c:if test="${orgunit.unitType eq 'brSchool'}">selected="selected"</c:if>>教学站</option>
										</select> 
											<input type="hidden" id="resourceid" name="resourceid" value="${orgunit.resourceid }" /> 
											<input type="hidden" id="isChild" name="isChild" value="${orgunit.isChild }" />
											</td>
										<td colspan="2">
											<div id="_brschoolType"
												<c:if test="${orgunit.unitType != 'brSchool' }"> style="display:none"</c:if>>
												<table width="100%">
													<tr>
														<td width="26%">教学站办学模式：</td>
														<td><gh:checkBox name="schoolType"
																dictionaryCode="CodeTeachingType"
																value="${orgunit.schoolType}" /></td>
													</tr>
												</table>
											</div>
											<c:if test="${empty orgunit.resourceid}"><input type="hidden"  name="schoolType" value="2,4" /> </c:if>
										</td>
									</tr>
									<tr>
										<td>组织单位描述:</td>
										<td><input type="text" name="unitDescript"
											style="width: 50%" value="${orgunit.unitDescript }" /></td>
										<td>状态：</td>
										<td><gh:select name="status" value="${orgunit.status}"
												dictionaryCode="CodeOrgUnitStatus" /></td>
									</tr>
									<tr>
										<td style="width: 12%">组织单位编码(用于编学号):</td>
										<td style="width: 38%"><input type="text"
											id="unitCode4StuNo" name="unitCode4StuNo" style="width: 50%"
											value="${orgunit.unitCode4StuNo }" class="required"
											alt="请输入字母下划线或数字的组合" /> <!-- 										<span class="buttonActive" style="margin-left:8px"><div class="buttonContent"><button type="button" onclick="validateOnlyUnit()">检查唯一性</button></div></span> -->
										</td>
									</tr>
								</table>
							</div>
							<div>
								<table class="form">
									<c:if test="${orgunit.unitType eq 'brSchool' }">
										<tr>
											<td style="width: 12%">是否允许开新专业:</td>
											<td style="width: 38%"><gh:select name="isAllowNewMajor"
													value="${orgunit.isAllowNewMajor}" dictionaryCode="yesOrNo"
													style="width:52%" /></td>
											<td style="width: 12%">专业数限制</td>
											<td style="width: 38%"><input type="text"
												name="limitMajorNum" style="width: 50%"
												value="${orgunit.limitMajorNum }" /><font color="red">0
													为不限制</font></td>
										</tr>
									</c:if>
									<tr>
										<td style="width: 12%">负责人姓名:</td>
										<td><input type="text" value="${orgunit.principal }"
											name="principal" style="width: 50%;"  /></td>
										<td style="width: 12%">负责人电话:</td>
										<td><input type="text" value="${orgunit.contectCall }"
											name="contectCall" style="width: 50%;"  /></td>
									</tr>
									<tr>
										<td style="width: 12%">教务员姓名:</td>
										<td><input type="text"
											value="${orgunit.orgUnitExtends['teachingMan'].exValue}"
											name="teachingMan" style="width: 50%;" /></td>
										<td style="width: 12%">教务员电话:</td>
										<td><input type="text"
											value="${orgunit.orgUnitExtends['teachingTel'].exValue }"
											name="teachingTel" style="width: 50%;" class="phone" /></td>
									</tr>
									<tr>
										<td style="width: 12%">教务员E-mail:</td>
										<td><input type="text"
											value="${orgunit.orgUnitExtends['teachingEmail'].exValue }"
											name="teachingEmail" style="width: 50%;" class="email" /></td>
										<td style="width: 12%">教务员QQ:</td>
										<td><input type="text"
											value="${orgunit.orgUnitExtends['teachingQQ'].exValue }"
											name="teachingQQ" style="width: 50%;" /></td>
									</tr>
									<tr>
										<td>专业主任姓名:</td>
										<td><input type="text"
											value="${orgunit.orgUnitExtends['majorDirector'].exValue }"
											name="majorDirector" style="width: 50%;" /></td>
										<td>报到时间:</td>
										<td><input type="text"
											value="${orgunit.reportTime }"
											name="reportTime" style="width: 50%;" /></td>
									</tr>
									<tr>
										<td>联系人</td>
										<td><input type="text" name="linkman" style="width: 50%"
											value="${orgunit.linkman }" /></td>
										<td>所属城市</td>
										<td><input type="text" name="localCity"
											style="width: 50%" value="${orgunit.localCity }" />&nbsp;</td>
									</tr>
									<tr>
										<td>邮编:</td>
										<td><input type="text" name="zipcode" style="width: 50%"
											value="${orgunit.zipcode }" /></td>
										<td>电子邮件</td>
										<td><input type="text" name="email" style="width: 50%"
											value="${orgunit.email }" />&nbsp;</td>
									</tr>
									<tr>
										<td>是否招生:</td>
										<td><gh:select name="isRecruit" value="${orgunit.isRecruit}" dictionaryCode="yesOrNo" /></td>
										<td>是否市内教学点：</td>
										<td><gh:select name="isLocal" value="${orgunit.isLocal}" dictionaryCode="yesOrNo" /></td>
									</tr>
									<tr>
										<td>地址:</td>
										<td colspan="3"><input type="text" name="address"
											style="width: 50%" value="${orgunit.address }" /></td>
									</tr>
									<tr>
										<td>报到地点:</td>
										<td colspan="3"><input type="text" name="reportSite"
											style="width: 50%" value="${orgunit.reportSite }" /></td>
									</tr>
	
								</table>
							</div>
						</div>
						<div class="tabsFooter">
							<div class="tabsFooterContent"></div>
						</div>

					</div>
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
</body>
</html>