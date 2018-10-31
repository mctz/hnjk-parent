<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>组织单位管理</title>

<script type="text/javascript">
	$(document).ready(function(){
		$("#_branchschoolListTree").zTree({	expandSpeed: "",showLine: true,callback: {	click:	branchschoolListTreeOnChange }	}, [${unitTree}]);	
		if($('._branchschoolLeftTree')){//如果有左侧树，则赋值为自适应高度		
			$('._branchschoolLeftTree').height($("#container .tabsPageContent").height()-5);
		}
		branchschoolListTreeOnChange();
	});
	
	function branchschoolListTreeOnChange(event, treeId, treeNode) {		
		var unitId = "";
		var isChild = "n";	
		var isRoot = "n";
		if(treeNode){
			unitId = treeNode.id;
			if(null == treeNode.nodes){
				isChild = "y";
			}
			
			if(treeNode.level == '0'){
				isRoot = "y";
			}
		}
		$("#_branchschoolListContent").loadUrl('${baseUrl}/edu3/recruit/recruitmanage/orgunit/list.html?isSubPage=y&unitId='+unitId+'&isChild='+isChild+'&isRoot='+isRoot, {}, function(){
			$("#_branchschoolListContent").find("[layoutH]").layoutH();
		});
	}	
		
	
</script>
</head>
<body>
	<div
		style="float: left; width: 19%; padding: 0; border: 0; margin: -1px;">
		<div class="_branchschoolLeftTree"
			style="display: block; overflow: auto; padding-bottom: 6px; border: solid 1px #CCC; line-height: 21px; width: 100%; height: auto;">
			<ul id="_branchschoolListTree" class="ztree"></ul>
		</div>
	</div>

	<div class="page" id="_branchschoolListContent"
		style="float: left; width: 81%; padding: 0; border: 0; margin: -1px;">
		<h3>请选择左边树节点</h3>
	</div>
</body>
</html>