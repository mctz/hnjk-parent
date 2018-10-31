<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>免考认定参数设定</title>
<script type="text/javascript">
 
    //选择用户
   	function selectTeacher(obj){
    	var tId = $(obj).parent().parent().parent().parent().find("td:has(input[name='dictChildName'])").attr("id");
		//alert($(obj).parent().parent().parent().parent().find("td:has(input[name='dictChildName']) ").attr("id"));
   		$.pdialog.open('${baseUrl }/edu3/teaching/noexamapply/config-choose-user.html?tId='+tId,'RES_TEACHING_RESULT_NOEXAM_SELECT_USER','选择老师',{mask:true,width:800,height:600});
   	}
  	//新增子表
    function addChildDict(){
    	var operatingFlag = "${operatingFlag}";
    	var info          = "";
    	var rowNum		  = jQuery("#configDict").get(0).rows.length;
    	var htmlStr       = "";
    	if("timeConfig"==operatingFlag){
    		info          = "时间：";
    		htmlStr       = "<tr><td width='10%'><q>"+info+rowNum+"</q><input type='checkbox' id='checkDictId' value=''><input type='hidden' name='dictChildId' value=''></td>"+
    						"<td width='15%'>开始时间</td><td width='30%'><input type='text'  name='dictChildValue1' value='' onClick='WdatePicker()'/></td>"+
    						"<td width='15%'>结束时间</td><td width='30%'><input type='text'  name='dictChildValue2' value='' onClick='WdatePicker()'/></td></tr>";
    	}
    
    	jQuery("#configDict").append(htmlStr);
    }
    
    //删除子表
    function delChildDict(){
    	var flag = "${flag}";
    	var operatingFlag = "${operatingFlag}";
    	
    	tab = jQuery("#configDict").get(0);
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
			var url = "${baseUrl}/edu3/teaching/noexamapply/config-del.html?flag="+flag+"&operatingFlag="+operatingFlag;
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
	<h2 class="contentTitle">参数设置</h2>
	<div class="page">
		<div class="pageContent">
			<form method="post"
				action="${baseUrl}/edu3/teaching/noexamapply/config-save.html"
				class="pageForm"
				onsubmit="return validateCallback(this,navTabAjaxDone);">

				<input type="hidden" name="resourceid" value="${dict.resourceid }" />
				<input type="hidden" name="flag" value="${flag }" /> <input
					type="hidden" name="operatingFlag" value="${operatingFlag }" />

				<div class="pageFormContent" layoutH="97">

					<table class="form" id="configDict">
						<c:choose>
							<c:when test="${operatingFlag eq 'userConfig' }">
								<tr>
									<td width="15%"><q>允许操作的老师</q> <input type="hidden"
										name="dictChildId" value="${childDict.resourceid}"></td>
									<td width="60%" id="teacherInfoTd"><span>${childDict.dictName }</span>
										<input type="hidden" " name="dictChildName"
										value="${childDict.dictName }" /> <input type="hidden"
										name="dictChildValue" value="${childDict.dictValue }" /></td>
									<td width="25%">
										<div class='buttonActive'>
											<div class='buttonContent'>
												<button type='button' onclick='selectTeacher(this)'>选择老师</button>
											</div>
										</div>
									</td>
								</tr>
							</c:when>
							<c:when test="${operatingFlag eq 'timeConfig' }">
								<tr>
									<td colspan="5" style="text-align: right">
										<div class="formBar">
											<ul>
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
								<c:forEach items="${childDict }" var="childDict" varStatus="vs">
									<tr>
										<td width="10%"><q>时间：${vs.index+1}</q> <input
											type="checkbox" id="checkDictId"
											value="${childDict.resourceid}"> <input type="hidden"
											name="dictChildId" value="${childDict.resourceid}"></td>
										<td width="15%">开始时间</td>
										<td width="30%"><input type="text" name="dictChildValue1"
											value="${childDict.dictName }" onClick="WdatePicker()" /></td>
										<td width="15%">结束时间</td>
										<td width="30%"><input type="text" name="dictChildValue2"
											value="${childDict.dictValue }" onClick="WdatePicker()" /></td>
									</tr>
								</c:forEach>
							</c:when>
						</c:choose>

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