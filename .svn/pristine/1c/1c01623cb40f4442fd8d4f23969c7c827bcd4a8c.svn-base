<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>招生限制管理表单</title>
<script type="text/javascript">
 </script>
</head>
<body>
	<h2 class="contentTitle">设置招生限制</h2>
	<div class="page">
		<div class="pageContent">
			<form method="post"
				action="${baseUrl}/edu3/recruit/recruitmanage/recruitlimit-save.html"
				class="pageForm" onsubmit="return validateCallback(this);">
				<div class="pageFormContent" layoutH="97">
					<table class="form" id="branchSchoolTable">
						<input type="hidden" name="resourceid"
							value="${school.resourceid }" />
						<tr>
							<td style="width: 12%">教学站编号:</td>
							<td style="width: 38%">${school.unitCode}</td>
							<td style="width: 12%">教学站名称:</td>
							<td style="width: 38%" colspan="2">${school.unitName}</td>
						</tr>
						<tr>
							<td style="width: 12%">允许招生指总标数:</td>
							<td style="width: 38%"><input type="text" id="limitMajorNum"
								name="limitMajorNum" size="40" value="${school.limitMajorNum}"
								min="0" class="required number" /></td>
							<td style="width: 12%">是否允许申请新专业:</td>
							<td style="width: 38%" colspan="2"><gh:select
									name="isAllowNewMajor" value="${school.isAllowNewMajor}"
									dictionaryCode="yesOrNo" classCss="required" /></td>
						</tr>

					</table>
					<br />
					<h2 class="contentTitle">允许的招生专业列表</h2>
					<input type="button" value="添加" onclick="addLimitMajor();" /> <input
						type="button" value="删除" onclick="delLimitMajor();" />
					<table class="form" id="brschMajorSettingTable">
						<thead>
							<tr>
								<td><input type="checkbox" name="checkall"
									id="check_all_brschool_limit_major"
									onclick="checkboxAll('#check_all_brschool_limit_major','brschMajorSettingId','#brschoolLimitMajorListBody')" />
								</td>
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
										checked="checked" /></td>
									<td>${setting.classic.classicName } <input type="hidden"
										value="${setting.classic.resourceid }" name="classic" />
									</td>
									<td>${setting.major.majorName } <input type="hidden"
										value="${setting.major.resourceid }" name="major" />
									</td>
									<td><input type="text" name="limitNum"
										value="${setting.limitNum }" /></td>
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
				<div class="formBar">
					<ul>
						<li>
							<div class="buttonActive">
								<div class="buttonContent">
									<button type="submit">提交</button>
								</div>
							</div>
						</li>
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
		var majorSelect = "<gh:selectModel id='major' name='major' bindValue='resourceid' displayValue='majorName' modelClass='com.hnjk.edu.basedata.model.Major'  /> ";
		var isOpenSelect= "<select name='isOpened'><option value='Y' selected='selected'>启用</option><option value='N'>未启用</option></select>";
		var classic     = "<gh:selectModel name='classic' bindValue='resourceid' displayValue='classicName' modelClass='com.hnjk.edu.basedata.model.Classic' />";
		var rowNum 		= jQuery("#brschMajorSettingTable").get(0).rows.length
		var trhtml      = "<tr><td><q>"+rowNum+"</q><input type='checkbox' checked='checked' name='brschMajorSettingId' value='' autocomplete='off' /></td><td>"+classic+"</td><td>"+majorSelect+"</td>";
			trhtml     += "<td><input type='text' name='limitNum' class='required number'/></td><td>"+isOpenSelect+"</td><td><textarea name='memo' rows='1' cols='10'></textarea></td></tr>";
		
		$("#brschoolLimitMajorListBody").append(trhtml);
		index = index+1;
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
</script>
</body>
</html>