<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>选择知识节点</title>
<gh:loadCom components="ztree" />
</head>

<body>
	<script type="text/javascript">
    var zTree1;
	var setting = {checkable: true,checkType:{'Y':'s'},callback: {	change:	zTreeOnChange }	};
	
 	var zNodes = [${syllabusTree}];

  $(document).ready(function(){					
		zTree1 = $("#SyllabusTree").zTree(setting, zNodes);			
	});
	
	function zTreeOnChange(event, treeId, treeNode) {
		var treeNodes = zTree1.getCheckedNodes(true);
		var ids = "";
		var names = "";
		for (var i = 0; i < treeNodes.length; i++) {
			ids += treeNodes[i].id.split(',')[0]+","; 
			names += treeNodes[i].name+","; 
		}
		$("#selectedIdsValue").val(ids);
		$("#selectedNamesValue").val(names);
	}
	
   function selectedSyllabus(){
   		var ids =  $("#selectedIdsValue").val();
   		var names = $("#selectedNamesValue").val();
   		if(ids!="") {
   			ids = ids.substring(0,ids.length-1);
   		}
   		if(names!="") {
   			names = names.substring(0,names.length-1);    			
   		}
   		$("#${idsN}").val(ids);
   		$("#${namesN}").val(names);	
		$("#closeSyllabusBTN").click();   		
   }
  </script>




	<div class="formBar">
		<input type="hidden" value="" id="selectedIdsValue" /> <input
			type="hidden" value="" id="selectedNamesValue" />
		<ul>
			<li><div class="buttonActive" style="padding-right: 8px">
					<div class="buttonContent">
						<button type="button" onclick='selectedSyllabus()'>确定</button>
					</div>
				</div></li>
			<li><div class="buttonActive" style="padding-right: 8px">
					<div class="buttonContent">
						<button type="button" class="close" id="closeSyllabusBTN">取消</button>
					</div>
				</div></li>
		</ul>
	</div>
	<div class="leftTree_ztree"
		style="display: block; overflow: auto; height: 96%; width: 100%;">
		<div class="zTreeDemoBackground">
			<ul id="SyllabusTree" class="ztree"></ul>
		</div>
	</div>
	<div>&nbsp;&nbsp;&nbsp;&nbsp;</div>
</body>
</html>