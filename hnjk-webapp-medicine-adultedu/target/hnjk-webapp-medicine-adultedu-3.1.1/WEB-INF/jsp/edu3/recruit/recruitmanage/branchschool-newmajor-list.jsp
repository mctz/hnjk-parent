<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>学习中习申报的新专业列表</title>

</head>
<body>
	<div class="page">
		<div class="pageHeader">
			<form id="brsNewMajorIntoRecruitMajorSearchForm"
				onsubmit="return navTabSearch(this);"
				action="${baseUrl }/edu3/recruit/recruitmanage/brSchoolnewmajor-list.html"
				method="post">
				<div class="searchBar">
					<ul class="searchContent">
						<c:if test="${empty condition['isBranchSchool'] }">
							<li style="width: 280px"><label style="width: 100px">教学站：</label>
								<gh:brSchoolAutocomplete name="brSchoolId" tabindex="1"
									id="branchschool-newmajor_brSchoolName"
									defaultValue="${condition['brSchoolId']}" style="width:120px" />
							</li>
						</c:if>
						<li style="width: 280px"><label>招生批次：</label> <gh:recruitPlanAutocomplete
								name="recruitPlanId" tabindex="2"
								id="_branchschool-newmajor_recruitPlan"
								value="${condition['recruitPlanId']}" style="width:180px" /> <font
							color="red">*</font></li>
						<li style="width: 280px"><label style="width: 120px">是否转入招生批次：</label>
							<select name="isIntoRecruitPlan">
								<option value="A">查看全部</option>
								<option value="Y"
									<c:if test="${condition['isIntoRecruitPlan'] eq 'Y' }"> selected="selected" </c:if>>是</option>
								<option value="N"
									<c:if test="${condition['isIntoRecruitPlan'] eq 'N' }"> selected="selected" </c:if>>否</option>
						</select></li>
					</ul>
					<div class="subBar">
						<ul style="width: 280px">
							<li style="float: right;"><div class="buttonActive">
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
			<div class="panelBar">
				<ul class="toolBar">
					<gh:resAuth parentCode="RES_RECRUIT_ADUIT_BRSCHOOLNEWMAJOR_LIST"
						pageType="list"></gh:resAuth>
				</ul>
			</div>

			<table id="brsNewMajorIntoRecruitMajorTable" class="table"
				layouth="138">

				<thead>
					<tr>
						<th width="4%"><input type="checkbox" name="checkall"
							id="check_all_brsnewmajor"
							onclick="checkboxAll('#check_all_brsnewmajor','resourceid','#brsNewMajorIntoRecruitMajorBody')" /></th>
						<th width="10%">招生批次</th>
						<th width="10%">教学站</th>
						<th width="7%">申报层次</th>
						<th width="18%">申报通过的专业名称</th>
						<th width="5%">专业方向</th>
						<th width="5%">招生范围</th>
						<th width="6%">学习形式</th>
						<th width="10%">办学地址</th>
						<th width="5%">指标数</th>
						<th width="5%">下限人数</th>
						<th width="5%">学制</th>
						<th width="10%">备注</th>
					</tr>
				</thead>
				<input id="brsNewMajorIntoRecruitMajor_recruitPlanId" type="hidden"
					name="recruitPlanId" value="${condition['recruitPlanId']}" />
				<tbody id="brsNewMajorIntoRecruitMajorBody">

					<c:forEach items="${brSchoolNewMajorList}" var="newMajor"
						varStatus="vs">
						<tr>
							<td align="left"><input type="checkbox" name="resourceid"
								value="${newMajor.resourceid}" autocomplete="off" /></td>
							<td align="left"
								title="${newMajor.branchSchoolPlan.recruitplan.recruitPlanname }">
								${newMajor.branchSchoolPlan.recruitplan.recruitPlanname }</td>
							<td align="left"
								title="${newMajor.branchSchoolPlan.branchSchool.unitName}">
								${newMajor.branchSchoolPlan.branchSchool.unitName}</td>
							<td align="left" title="${newMajor.classic.classicName}">
								${newMajor.classic.classicName}</td>
							<c:choose>
								<c:when test="${not empty newMajor.majorName}">
									<td align="left"><gh:selectModel bindValue="resourceid"
											displayValue="nationMajorName"
											modelClass="com.hnjk.edu.basedata.model.NationMajor"
											disabled="disabled" value=" ${newMajor.majorName}"
											style="width:200px" /></td>
								</c:when>
								<c:when test="${not empty newMajor.baseMajor}">
									<td align="left"><gh:selectModel bindValue="resourceid"
											displayValue="majorName"
											modelClass="com.hnjk.edu.basedata.model.Major"
											disabled="disabled" value="${newMajor.baseMajor}"
											style="width:200px" /></td>
								</c:when>
							</c:choose>
							<td align="left" title="${newMajor.dicrect}">${newMajor.dicrect}</td>
							<td align="left" title="${newMajor.scope}">${newMajor.scope}</td>
							<td align="left"
								title="${ghfn:dictCode2Val('CodeTeachingType',newMajor.teachingType)}">
								${ghfn:dictCode2Val('CodeTeachingType',newMajor.teachingType)}</td>
							<td align="left" title="${newMajor.address}">${newMajor.address}</td>
							<td align="left"><input name="limitNum"
								value="${newMajor.limitNum}" style="width: 40Px" size='20'
								min='0' onblur='checkLimitNum(this)' class='required number'
								<c:if test="${condition['isIntoRecruitPlan'] ne 'N' }"> disabled="disabled" </c:if> /></td>
							<td align="left"><input name="lowerNum"
								value="${newMajor.lowerNum}" style="width: 40Px" size='20'
								min='0' onblur='checkLowerNum(this)' class='required number'
								<c:if test="${condition['isIntoRecruitPlan'] ne 'N' }"> disabled="disabled" </c:if> /></td>
							<td align="left"><input name="studyperiod"
								style="width: 40Px" size='20' value="${newMajor.studyperiod}"
								min='0' onblur='checkStudyperIod(this)' class='required number'
								<c:if test="${condition['isIntoRecruitPlan'] ne 'N' }"> disabled="disabled" </c:if> /></td>
							<td align="left" title="${newMajor.memo}"><input name="memo"
								value="${newMajor.memo}" style="width: 60Px"
								<c:if test="${condition['isIntoRecruitPlan'] ne 'N' }"> disabled="disabled" </c:if> />
							</td>
						</tr>
					</c:forEach>


				</tbody>
			</table>
		</div>
	</div>
	<script type="text/javascript">

	//将教学站申报的新专业转为相应招生批次的招生专业
	function batchbaIntoRecruitPlan(){
		var url                = "${baseUrl }/edu3/recruit/recruitmanage/brSchoolnewmajor.html"
		var recruitPlanId      = $("#brsNewMajorIntoRecruitMajor_recruitPlanId").val();
		var recruitPlanName    = $("#brsNewMajorIntoRecruitMajorSearchForm select[name=recruitPlanId] option:selected").text();
		var isIntoRecruitPlan  = '${condition["isIntoRecruitPlan"]}';
		
		var limitNum 	       = new Array();
		var lowerNum 	       = new Array();
		var StudyperIod        = new Array();
		var brsNewMajorId      = new Array();
		//var examSubjectSet     = new Array();
		var memo               = new Array();
		
		//当是否转入招生批次为否的时候，才允许执行转入操作
		if("N"==isIntoRecruitPlan){
			
			if(""==recruitPlanId){
				alertMsg.warn("请选择一个招生批次，点击查询后再执行操作！");
				return false;
			}
			$("#brsNewMajorIntoRecruitMajorBody INPUT[name=resourceid]").each(function(ind){
				
				if($(this).attr("checked")===true){
					
					brsNewMajorId.push($(this).val());
					var li = $(this).parent().parent().parent().find("input[name=limitNum]").val();
					var lo = $(this).parent().parent().parent().find("input[name=lowerNum]").val();
					var si = $(this).parent().parent().parent().find("input[name=studyperiod]").val();
					//var ss = $(this).parent().parent().parent().find("select[name=examSubjectSet] option:selected").val();
					var me = $(this).parent().parent().parent().find("input[name=memo]").val();

					if(undefined == li || ""==li || li.isInteger()==false){
						$(this).parent().parent().parent().find("input[name=limitNum]").focus();
						alertMsg.warn("请正确填写指标数");
						return false;
					}
					if(undefined == lo || ""==lo || lo.isInteger()==false){
						$(this).parent().parent().parent().find("input[name=lowerNum]").focus();
						alertMsg.warn("请正确填写下限数");
						return false;
					}
					if(undefined == si || ""==si || si.isNumber()==false){
						$(this).parent().parent().parent().find("input[name=studyperiod]").focus();
						alertMsg.warn("请正确填写学制");
						return false;
					}
					/*if(undefined == ss || ""==ss ){
						$(this).parent().parent().parent().find("select[name=examSubjectSet]").focus();
						alertMsg.warn("请选择考试科目");
						return false;
					}*/
					if(undefined ==me){
						me   = "";
					}
					
					memo.push(me);
					limitNum.push(li);
					lowerNum.push(lo);
					StudyperIod.push(si);
					//examSubjectSet.push(ss);
				}
			});

			if(brsNewMajorId.length<=0){
				alertMsg.warn("请选择要转入招生批次的新专业");
				return false;	
			}
			if(limitNum.length.length<=0){
				alertMsg.warn("指标数不允许为空");
				return false;	
			}
			if(lowerNum.length.length<=0){
				alertMsg.warn("下限数不允许为空");
				return false;	
			}
			if(StudyperIod.length.length<=0){
				alertMsg.warn("学制不允许为空");
				return false;	
			}
			/*
			if(examSubjectSet.length.length<=0){
				alertMsg.warn("考试科目不允许为空");
				return false;	
			}*/
			alertMsg.confirm("确定要将所选教学站申报的新专业转为<font color='red'>"+recruitPlanName+"</font>的招生专业吗？", {
	            okCall: function(){
	               $.post(url, {recruitPlanId: recruitPlanId,brSchNewMajorId:brsNewMajorId.toString(),limitNum:limitNum.toString(),lowerNum:lowerNum.toString(),studyperiod:StudyperIod.toString(),memo:memo.toString()}, navTabAjaxDone, "json");
	            }
			});
			
			
		//当是否转入招生批次为是的时候，不允许执行转入操作	
		}else{
			alertMsg.warn("要执行转入操作，请先选择查询条件中的：<font color='red'>是否转入招生批次-->否</font>,点击查询后再操作！");
			return false;
		}
		
		
	}
	//检查指标数的格式
	function checkLimitNum(obj){
		var limitNum = $(obj).val();
		if(!limitNum.isInteger()){
			$(obj).attr("value","");
		}
	}
	//检查下限数的格式
	function checkLowerNum(obj){
		var lowerNum = $(obj).val();	
		var limitNum = $(obj).parent().parent().parent().find(" input[name=limitNum]").val();
		if(""==limitNum){
			$(obj).parent().parent().find(" input[name=limitNum]").focus();
		}
		if(lowerNum.isInteger()){
			if(parseInt(lowerNum)>=parseInt(limitNum)){
				$(obj).attr("value","").focus();
				alertMsg.warn("下限数不能大于指标数!");
			}
		}else{
			$(obj).attr("value","");
		}
		
	}
	//检查学制的格式
	function checkStudyperIod(obj){
		if(!$(obj).val().isNumber()){
			$(obj).attr("value","");
		}
	}
</script>
</body>
</html>
