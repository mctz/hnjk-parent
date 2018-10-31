<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>选择组织单位</title>
<gh:loadCom components="ztree" />
</head>

<body>
	<script type="text/javascript">
    var zTree1;
	var setting = {	
			checkable: true,
			checkStyle: "${checkBoxType}",
			checkRadioType: "all",
			callback: {	
				change:	zTreeOnChange 
			}	
	};//树形设置
	
 	var zNodes = [${unitTree}];

  $(document).ready(function(){					
		zTree1 = $("#orgUnitTree").zTree(setting, zNodes);			
	});
	
	function zTreeOnChange(event, treeId, treeNode) {
		var treeNodes = zTree1.getCheckedNodes(true);
		var ids = "";
		var names = "";
		for (var i = 0; i < treeNodes.length; i++) {
			ids += treeNodes[i].id+","; 
			names += treeNodes[i].name+","; 
		}
		$("#selectedIdValue").val(ids);
		$("#selectedNameValue").val(names);
	}
	
   function selectedOrgUnit(){
   		var codes =  $("#selectedIdValue").val();   		
   		if(codes!="") {
   			codes = codes.substring(0,codes.length-1);
   			if("${codesN}" != ""){
   				$("#${codesN}").val(codes);
   			}else if("${idsN}" !=""){
   				$("#${idsN}").val(codes);
   			}
   		}
   		var names = $("#selectedNameValue").val();  
   		if(names!="") {
   			names = names.substring(0,names.length-1); 
   			$("#${namesN}").val(names);	
   		}	
   		
		// 关闭
		$("#closeBTN").click();   		
   }
  </script>




	<div class="formBar">
		<input type="hidden" value="${checkedValues }" id="selectedIdValue"
			name="selectedIdValue" /> <input type="hidden"
			value="${checkedNames }" id="selectedNameValue"
			name="selectedNameValue" />
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