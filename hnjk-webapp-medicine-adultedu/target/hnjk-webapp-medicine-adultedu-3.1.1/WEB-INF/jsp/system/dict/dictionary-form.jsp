<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>字典管理</title>
<script type="text/javascript">
    //验证字典编码唯一
    function validateOnly(){
    	var userid = jQuery("#resourceid");
    	var dictCode = jQuery("#dictCode");
    	if(dictCode.val()==""){ alertMsg.warn("请输入字典编码"); dictCode.focus() ;return false; }
    	var url = "${baseUrl}/edu3/framework/system/sys/dictionary/validateCode.html";
    	jQuery.post(url,{dictCode:dictCode.val(),resourceid:userid.val()},function(data){
    		if(data == "exist"){ alertMsg.warn("编码已存在!"); }else{alertMsg.correct("恭喜，此编码可用！") }
    	})
    }
    //新增子表
    function addChildDict(){
    	var selHtml01 = "<gh:select name='isUsedChild' dictionaryCode='yesOrNo' style='width:80%'/>";
    	var selHtml02 = "<gh:select name='isDefault' dictionaryCode='yesOrNo' style='width:80%'/>";
    	var rowNum = jQuery("#childDict").get(0).rows.length;
    	jQuery("#childDict").append("<tr><td width='5%'><q>"+rowNum+"</q><input type='checkbox' id='checkDictId' value=''><input type='hidden' name='dictChildId' value=''></td><td width='8%'>选项名:</td><td width='15%'><input type='text' name='dictChildName' value='' style='width:90%'/><td width='8%'>选项值:</td><td width='15%'><input type='text' name='dictChildValue' value='' style='width:90%'/></td>	<td width='8%'>是否可用:</td><td width='8%'>"+selHtml01+"</td><td width='8%'>默认值:</td><td width='8%'>"+selHtml02+"</td><td width='7%'>排序:</td><td width='8%'><input type='text' name='showOrder' value='"+rowNum+"' style='width:80%'/></td></tr>");
    }
    
    //删除子表
    function delChildDict(){
    	tab = jQuery("#childDict").get(0);
  		var ids = "";
		var idArray = new Array();
  		jQuery("input[id='checkDictId']:checkbox").each(function(index){
			if(jQuery(this).attr("checked")){
				ids = ids + jQuery(this).val() + ",";
				idArray.push(index);
			}
		})
		if(idArray.length<1){return;}
		if(window.confirm('确定删除该记录?')){
			var rowIndex;
			var nextDiff =0;
			for(j=0;j< idArray.length;j++){
				rowIndex = idArray[j]+1-nextDiff;
				tab.deleteRow(rowIndex);
				nextDiff++;
			}
			var url = "${baseUrl}/edu3/system/dictionary/delete.html";
			if(ids!=""){
				jQuery.get(url,{resourceid:ids})
			}
		}
    	jQuery("q").each(function(index){
    		jQuery(this).text(index+1);
    	})
    }
</script>
</head>
<body>
	<h2 class="contentTitle">编辑字典</h2>
	<div class="page">
		<div class="pageContent">
			<form method="post"
				action="${baseUrl}/edu3/system/dictionary/save.html"
				class="pageForm" onsubmit="return validateCallback(this);">
				<input type="hidden" id="resourceid" name="resourceid"
					value="${dict.resourceid }" />
				<div class="pageFormContent" layoutH="97">
					<table class="form">
						<tr>
							<td>字典编码:</td>
							<td><input type="text" id="dictCode" name="dictCode"
								style="width: 50%" value="${dict.dictCode }" class="required" />
								<span class="buttonActive" style="margin-left: 8px"><div
										class="buttonContent">
										<button type="button" onclick="validateOnly()">检查唯一性</button>
									</div></span></td>
							<td>所属模块:</td>
							<td><input type="text" name="module" style="width: 50%"
								value="${dict.module }" class="required" /></td>
						</tr>
						<tr>
							<td>字典名称:</td>
							<td><input type="text" name="dictName" style="width: 50%"
								value="${dict.dictName }" class="required" />
							<td>&nbsp;</td>
							<td>&nbsp;</td>
						</tr>
					</table>
					<table class="form" id="childDict">
						<tr>
							<td colspan="11" style="height: 50px;">
								<div class="formBar" style="height: 100%;">
									<label style="width: 80%;font-size: 14px;color: red;">
										<c:choose>
											<c:when test="${dict.dictCode eq 'CodeScoreChar' }">
												*此处排序号为字符成绩量化值
											</c:when>
											<c:when test="${dict.dictCode eq 'CodeFractionalGPA'}">
												*请按照从高到低的顺序填写分数及对应的绩点，可在分数前添加 “>”，“=”，“>=” 来确定分数范围
											</c:when>
											<c:when test="${dict.dictCode eq 'changeTypeToName'}">
												*此处选项名为学籍异动类别代码【CodeStudentStatusChange】，选项值用于处理“学籍异动申请表”二级标题
											</c:when>
											<c:when test="${dict.dictCode eq 'printStuchangeModel'}">
												*此处选项名为学籍异动类别代码【CodeStudentStatusChange】，选项值为“学籍异动申请表”模版文件名
											</c:when>
											<c:when test="${dict.dictCode eq 'CodePackageOffer'}">
												*添加录取通知书下载文件，选项名为文件类别，选项值为自定义文件名称，可在【录取管理】—>【设置录取查询时间】—>【上传录取文件】上传对应的文件
											</c:when>
											<c:when test="${dict.dictCode eq 'CodeScoreLevel'}">
												*用于处理等级成绩，将分数类型转为中文成绩
											</c:when>
											<c:when test="${dict.dictCode eq 'Code.WorkManage.position'}">
												*选项值说明：选项值为1和2的职位对应部门选项值1，3和4对应其它部门选项值
											</c:when>
											<c:otherwise></c:otherwise>
										</c:choose>
									</label>
									<ul style="height: 100%;">
										<li><div class="buttonActive">
												<div class="buttonContent">
													<button type="button" onclick="addChildDict()">新增一项</button>
												</div>
											</div></li>
										<li><div class="button">
												<div class="buttonContent">
													<button type="button" onclick="delChildDict()">删除</button>
												</div>
											</div></li>
									</ul>
								</div>
							</td>
						</tr>
						<c:forEach items="${dict.dictSets}" var="childDict" varStatus="vs">
							<tr>
								<td width="5%"><q>${vs.index+1}</q><input type="checkbox"
									id="checkDictId" value="${childDict.resourceid}"><input
									type="hidden" name="dictChildId"
									value="${childDict.resourceid}"></td>
								<td width="8%">选项名:</td>
								<td width="15%"><input type="text" name="dictChildName"
									value="${childDict.dictName }" style="width: 90%" />
								<td width="8%">选项值:</td>
								<td width="15%"><input type="text" name="dictChildValue"
									value="${childDict.dictValue }" style="width: 90%" /></td>
								<td width="8%">是否可用:</td>
								<td width="8%"><gh:select name="isUsedChild"
										value="${childDict.isUsed}" dictionaryCode="yesOrNo"
										style="width:80%" /></td>
								<td width="8%">默认值:</td>
								<td width="8%"><gh:select name="isDefault"
										value="${childDict.isDefault}" dictionaryCode="yesOrNo"
										style="width:80%" /></td>
								<td width="7%">排序:</td>
								<td width="8%"><input type="text" name="showOrder"
									value="${childDict.showOrder }" style="width: 80%" /></td>
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

</body>
</html>