<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>组织单位管理</title>

</head>
<body>
	<script type="text/javascript">
	$(document).ready(function(){
		$("#brachschoolForm #_unitType").bind("change", function(){
			if($("#brachschoolForm #_unitType").val() == 'brSchool'){
				$("#_departType_brSchool").css({"display":"block","width":"100%"});
			}else{
				$("#_departType_brSchool").css({"display":"none"});
			}
	  	
	});
	});

    function validateOnlyUnit(){
    	var userid = jQuery("#brachschoolForm #resourceid");
    	var unitCode = jQuery("#brachschoolForm #unitCode");    	
    	if(unitCode.val()==""){ alertMsg.warn("请输入组织单位编码"); unitCode.focus() ;return false; }
    	var url = "${baseUrl}/edu3/framework/system/orgunit/validateCode.html";
    	jQuery.post(url,{unitCode:unitCode.val(),resourceid:userid.val()},function(data){
    		if(data == "exist"){ alertMsg.warn("组织单位编码已存在!"); }else{ alertMsg.correct("恭喜，此组织单位编码可用！")}
    	})
    }
    
   function openBranchschoolTree(){
 	var defaultValues = $("#parentId").val();
 	orgUnitSelector('parentId','parentName','radio','${rootCode}',defaultValues);
 }
</script>
	<h2 class="contentTitle">编辑单位</h2>
	<div class="page">
		<div class="pageContent">
			<form method="post"
				action="${baseUrl}/edu3/recruit/recruitmanage/orgunit/save.html"
				class="pageForm" id="brachschoolForm"
				onsubmit="return validateCallback(this);">
				<div class="pageFormContent" layoutH="97">
					<div class="tabs">
						<div class="tabsHeader">
							<div class="tabsHeaderContent">
								<ul>
									<li class="selected"><a href="#"><span>基本信息</span></a></li>
									<li><a href="#"><span>办学信息</span></a></li>
									<li><a href="#"><span>招生限制</span></a></li>
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
											type="hidden" id="parentId" name="parentId"
											value="${parentOrg.resourceid }" /> <span
											class="buttonActive" style="margin-left: 8px">
												<div class="buttonContent">
													<button type="button" onclick="openBranchschoolTree();">选择组织单位</button>
												</div>
										</span></td>
									</tr>
									<tr>
										<td>组织类型:</td>
										<td colspan="3">
											<!--  <input type="hidden" name="unitType" value="brSchool"/>-->
											<select name="unitType" size="1" id="_unitType"
											style="width: 20%">
												<option value="virtulDepart"
													<c:if test="${orgunit.unitType eq 'virtulDepart'}">selected="selected"</c:if>>虚拟单位</option>
												<option value="brSchool"
													<c:if test="${orgunit.unitType eq 'brSchool'}">selected="selected"</c:if>>教学站</option>
										</select> <input type="hidden" id="resourceid" name="resourceid"
											value="${orgunit.resourceid }" /> <input type="hidden"
											id="isChild" name="isChild" value="${orgunit.isChild }" /> <span
											class="tips">注意：虚拟组织是用来归类，而不是实际的教学站</span>
										</td>
									</tr>
								</table>
								<div id="_departType_brSchool" style="width: 100%">
									<table class="form">
										<tr>
											<td>状态：</td>
											<td><gh:select name="status" value="${orgunit.status}"
													dictionaryCode="CodeOrgUnitStatus" /></td>
											<td colspan="2">
												<table width="100%">
													<tr>
														<td width="26%">教学站办学模式：</td>
														<td><gh:checkBox name="schoolType"
																dictionaryCode="CodeTeachingType"
																value="${orgunit.schoolType}" /></td>
													</tr>
												</table>
											</td>
										</tr>
										<tr>
											<td>签约开始时间:</td>
											<td><input type="text" name="startDate"
												style="width: 50%" value="${orgunit.startDate }"
												class="date1" onFocus="WdatePicker({isShowWeek:true})" /></td>
											<td>签约到期时间:</td>
											<td><input type="text" name="endDate" style="width: 50%"
												value="${orgunit.endDate }" class="date1"
												onFocus="WdatePicker({isShowWeek:true})" /></td>
										</tr>
										<tr>
											<td>依托单位名称:</td>
											<td><input type="text" name="unitDescript"
												style="width: 50%" value="${orgunit.unitDescript }" /></td>
											<td>是否机考:</td>
											<td><input type="radio" " name="isMachineExam" value="Y"
												<c:if test="${orgunit.isMachineExam eq 'Y'}"> checked="checked"</c:if> />是
												<input type="radio" " name="isMachineExam" value="N"
												<c:if test="${orgunit.isMachineExam eq 'N' or empty orgunit.isMachineExam }"> checked="checked"</c:if> />否
											</td>
										</tr>
									</table>
								</div>
							</div>
							<div>
								<table class="form">
									<tr>
										<td style="width: 12%">负责人姓名:</td>
										<td><input type="text" value="${orgunit.principal }"
											name="principal" style="width: 50%;" class="required" /></td>
										<td style="width: 12%">负责人电话:</td>
										<td><input type="text" value="${orgunit.contectCall }"
											name="contectCall" style="width: 50%;" class="required phone" /></td>
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
										<td>专业主任姓名:</td>
										<td colspan="3"><input type="text"
											value="${orgunit.orgUnitExtends['majorDirector'].exValue }"
											name="majorDirector" style="width: 50%;" /></td>
									</tr>
									<tr>
										<td>地址:</td>
										<td colspan="3"><input type="text" name="address"
											style="width: 50%" value="${orgunit.address }" /></td>
									</tr>
								</table>
							</div>
							<div>
								<h2 class="contentTitle">允许招生的专业</h2>
								<table class="form">
									<tr>
										<td style="width: 12%">是否允许开新专业:</td>
										<td style="width: 38%"><gh:select name="isAllowNewMajor"
												value="${orgunit.isAllowNewMajor}" dictionaryCode="yesOrNo"
												style="width:52%" /></td>
										<td style="width: 12%">招生指标数：</td>
										<td style="width: 38%"><input type="text"
											name="limitMajorNum" style="width: 50%"
											value="${orgunit.limitMajorNum }" /><font color="red">0
												为不限制</font></td>
									</tr>
								</table>
								<div style="text-align: right">
									<input type="button" value="添加" onclick="addLimitMajor();" /> <input
										type="button" value="删除" onclick="delLimitMajor();" />
								</div>
								<table class="form" id="brschMajorSettingTable">
									<thead>
										<tr>
											<td>&nbsp;&nbsp;<input type="checkbox" name="checkall"
												id="check_all_brschool_limit_major"
												onclick="checkboxAll('#check_all_brschool_limit_major','brschMajorSettingId','#brschoolLimitMajorListBody'),enableAllSetting(this);"
												checked="checked" />
											</td>
											<td>办学模式</td>
											<td>层次</td>
											<td>专业</td>
											<td>指标数</td>
											<td>是否启用</td>
											<td>备注</td>
										</tr>
									</thead>
									<tbody id="brschoolLimitMajorListBody">
										<c:forEach items="${brschMajorSettingList }" var="setting"
											varStatus="vs">
											<tr>
												<td>${vs.index+1 }<input type="checkbox"
													name="brschMajorSettingId" value="${setting.resourceid}"
													checked="checked" onclick="enableThisSetting(this)" /></td>
												<td><gh:select id='teachingType${vs.index+1 }'
														name='teachingType' dictionaryCode='CodeTeachingType'
														classCss='required' value="${setting.schoolType }" /><font
													color="red">*</font></td>
												<td>${setting.classic.classicName } <input
													type="hidden" value="${setting.classic.resourceid }"
													name="classic" />
												</td>
												<td>${setting.major.majorName } <input type="hidden"
													value="${setting.major.resourceid }" name="major" />
												</td>
												<td><input type="text" name="limitNum"
													value="${setting.limitNum }" /><font color="red">*</font></td>
												<td><select name="isOpened">
														<option value=''>请选择</option>
														<option value='Y'
															<c:if test="${setting.isOpened eq 'Y' }">selected="selected"</c:if>>启用</option>
														<option value='N'
															<c:if test="${setting.isOpened eq 'N' }">selected="selected"</c:if>>未启用</option>
												</select></td>
												<td><textarea name="memo" rows="1" cols="10">${setting.memo } </textarea>
												</td>
											</tr>
										</c:forEach>
									</tbody>
								</table>
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
	<script type="text/javascript">
	
	var index = 0;
	//添加限制专业
	function addLimitMajor(){
		//var majorSelect = "<gh:selectModel id='major' name='major' bindValue='resourceid' displayValue='majorName' modelClass='com.hnjk.edu.basedata.model.Major'  /> ";
		var majorSelect = "<select  name='major'><option value=''>请选择</option></select><font color='red'>*</font>";
		var isOpenSelect= "<select name='isOpened'><option value='Y' selected='selected'>启用</option><option value='N'>未启用</option></select>";
		var classic     = "<gh:selectModel name='classic' bindValue='resourceid' displayValue='classicName' modelClass='com.hnjk.edu.basedata.model.Classic' onchange='getMajorForLimit(this)' /><font color='red'>*</font>";
		var rowNum 		= jQuery("#brschMajorSettingTable").get(0).rows.length
		var teachingType="<gh:select id='teachingType"+rowNum+"' name='teachingType'  dictionaryCode='CodeTeachingType'  classCss='required' onchange='teachingTypeChangeComm(this)'  /><font color='red'>*</font>"
		var trhtml      = "<tr><td><q>"+rowNum+"</q><input type='checkbox' checked='checked' name='brschMajorSettingId' value='' autocomplete='off' onclick='enableThisSetting(this)' /></td><td>"+teachingType+"</td><td>"+classic+"</td><td>"+majorSelect+"</td>";
			trhtml     += "<td><input type='text' name='limitNum' class='required number' value='0' /></td><td>"+isOpenSelect+"</td><td><input type='text' name='memo'/></td></tr>";
		
		$("#brschoolLimitMajorListBody").append(trhtml);
		index = index+1;
	}
	//办学模式Change事件
	function teachingTypeChangeComm(obj){
		$(obj).parent().parent().find("select[name=classic]").attr("value","");
		$(obj).parent().parent().find("select[name=major]").children().remove();
		$(obj).parent().parent().find("select[name=major]").append("<option value=''>请选择</option>");
	} 
	//删除限制专业
	function delLimitMajor(){
		tab = jQuery("#brschMajorSettingTable").get(0);
  		var ids = "";
		var idArray = new Array();
  		jQuery("input[name='brschMajorSettingId']:checkbox").each(function(index){
			if(jQuery(this).attr("checked")){
				ids = ids + jQuery(this).val() + ",";
				idArray.push(index);
			}
		})
		if(idArray.length<1){return;}
		alertMsg.confirm("确定删除该记录?",{
            okCall: function(){
				var rowIndex;
				var nextDiff =0;
				for(j=0;j< idArray.length;j++){
					rowIndex = idArray[j]+1-nextDiff;
					tab.deleteRow(rowIndex);
					nextDiff++;
				}
            }
		});
    	jQuery("q").each(function(index){
    		jQuery(this).text(index+1);
    	})
	}
	function getMajorForLimit(obj){
		var teachingType = $(obj).parent().parent().find("select[name=teachingType] option:selected").val();
		var classic      = $(obj).val();
		var ids          = new Array();
		if(""==teachingType){
			$(obj).parent().parent().find("select[name=teachingType]").focus();
			alertMsg.warn("请选择一个办学模式!")
		}else if(""==classic){
			$(obj).focus();
			alertMsg.warn("请选择一个层次!")
		}else{
			$("#brschoolLimitMajorListBody tr").each(function(ind){
				
				var t  = $(this).find("select[name=teachingType] option:selected").val();
				var c1 = $(this).find("input[name=classic]").val();
				var c2 = $(this).find("select[name=classic] option:selected").val();
				if(t===teachingType & c1===classic){
					ids.push($(this).find("input[name=major]").val());
				}else if(t===teachingType & c2===classic){
					if("" != $(this).find("select[name=major] option:selected").val()){
						ids.push($(this).find("select[name=major] option:selected").val());
					}
				}
			})
			var url = "${baseUrl}/edu3/framework/branschoollimit/getmajor.html"
			$.get(url,{exceptids:ids.toString()},function(data){
				var majorOption  = "<option value=''>请选择</option>";
				for(var i=0;i<data.length;i++){
					majorOption += "<option value='"+data[i].key+"' title='"+data[i].value+"'>"+data[i].name+"</option>"
				}
				$(obj).parent().parent().find("select[name=major]").html(majorOption);
			},"json")
		}
	}
	//启用/停用 所有招生专业限制
	function enableAllSetting(obj){
		if(obj.checked){
			$("#brschoolLimitMajorListBody input[name='brschMajorSettingId']").each(function(obj){
				var boxObj     = $(this);
				boxObj.checked = true;
				enableThisSetting(boxObj);
			});
		}else{
			$("#brschoolLimitMajorListBody input[name='brschMajorSettingId']").each(function(obj){
				var boxObj     = $(this);
				boxObj.checked = false;
				enableThisSetting(boxObj);
			});
		}
	}
	//启用/停用招生专业限制
	function enableThisSetting(obj){
		if(obj.checked){
			$(obj).parent().parent().find("select[name='teachingType']").attr("disabled","");
			$(obj).parent().parent().find("input[name='limitNum']").attr("disabled","");
			$(obj).parent().parent().find("select[name='isOpened']").attr("disabled","");
			
			//旧数据
			$(obj).parent().parent().find("input[name='classic']").attr("disabled","");
			//新增数据
			$(obj).parent().parent().find("select[name='classic']").attr("disabled","");
			//旧数据
			$(obj).parent().parent().find("input[name='major']").attr("disabled","");
			//新增数据
			$(obj).parent().parent().find("select[name='major']").attr("disabled","");
			//旧数据
			$(obj).parent().parent().find("textarea[name='memo']").attr("disabled","");
			//新增数据
			$(obj).parent().parent().find("iput[name='memo']").attr("disabled","");
			
		}else{
			
			$(obj).parent().parent().find("select[name='teachingType']").attr("disabled","disabled");
			$(obj).parent().parent().find("input[name='limitNum']").attr("disabled","disabled");
			$(obj).parent().parent().find("select[name='isOpened']").attr("disabled","disabled");
			
			//旧数据
			$(obj).parent().parent().find("input[name='classic']").attr("disabled","disabled");
			//新增数据
			$(obj).parent().parent().find("select[name='classic']").attr("disabled","disabled");
			//旧数据
			$(obj).parent().parent().find("input[name='major']").attr("disabled","disabled");
			//新增数据
			$(obj).parent().parent().find("select[name='major']").attr("disabled","disabled");
			//旧数据
			$(obj).parent().parent().find("textarea[name='memo']").attr("disabled","disabled");
			//新增数据
			$(obj).parent().parent().find("iput[name='memo']").attr("disabled","disabled");
		}
	}
</script>
</body>
</html>