<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>选择组织单位</title>
<style>
</style>
</head>

<body>
	<script type="text/javascript">
    var zTree1;
	var setting;
	var checkedType = '${checkType}';	
	if(checkedType == 0){
		setting = {	checkable: true,checkStyle: "radio",checkRadioType: "all",callback: {	change:	zTreeOnChange }	};
	}else{
		setting = {	checkable: true,callback: {	change:	zTreeOnChange }	};
	}	


 var zNodes = [${unitTree}];

  $(document).ready(function(){					
		zTree1 = $("#orgUnitTree").zTree(setting, zNodes);			
	});
	
	function zTreeOnChange(event, treeId, treeNode) {
		var ov1 = $("#selectedIdValue").val();
		var ov2 = $("#selectedNameValue").val();
		$("#selectedIdValue").val(ov1+","+treeNode.id);
		$("#selectedNameValue").val(ov2+","+treeNode.name);
		//alert("[ treeId=" + treeId + ";tid=" + treeNode.id + "; Name=" + treeNode.name + " (节点Index = " + zTree1.getNodeIndex(treeNode) + ")");
	}
	
   function selectedOrgUnit(){
   		var ids =  $("#selectedIdValue").val();
   		if(ids!="") {
   			ids = ids.substring(1,ids.length);
   			$("#${idsN}").val(ids);
   		}
   		var names = $("#selectedNameValue").val();  
   		if(names!="") {
   			names = names.substring(1,names.length); 
   			$("#${namesN}").val(names);	
   		}	
   		
		// 关闭
		$("#closeBTN").click();   		
   }
  </script>




	<div class="formBar">
		<input type="hidden" value="" id="selectedIdValue"
			name="selectedIdValue" /> <input type="hidden" value=""
			id="selectedNameValue" name="selectedNameValue" />
		<ul>
			<li><div class="buttonActive" style="padding-right: 8px">
					<div class="buttonContent">
						<button type="button" onclick='selectedOrgUnit()'>确定</button>
					</div>
				</div></li>
			<li><div class="buttonActive" style="padding-right: 8px">
					<div class="buttonContent">
						<button type="button" class="close" id="closeBTN">取消</button>
					</div>
				</div></li>
		</ul>
	</div>
	<div class="leftTree_ztree"
		style="display: block; overflow: auto; height: 96%; width: 100%;">
		<div class="zTreeDemoBackground">
			<ul id="orgUnitTree" class="ztree"></ul>
		</div>
	</div>
</body>
</html>