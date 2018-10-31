<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>组织单位管理</title>

<script type="text/javascript">
	$(document).ready(function(){
		//生成数据树
		$("#_orgUnitListTree").zTree({	expandSpeed: "",showLine: true,callback: {	click:	orgUnitListTreeOnChange }	}, [${unitTree}]);	
		if($('._orgUnitLeftTree')){//如果有左侧树，则赋值为自适应高度		
			$('._orgUnitLeftTree').height($("#container .tabsPageContent").height()-5);
		}
		orgUnitListTreeOnChange();
	});
	
	function orgUnitListTreeOnChange(event, treeId, treeNode) {				
		var unitId = "";
		if(treeNode){
			unitId = treeNode.id;
		}
		$("#_orgUnitListContent").loadUrl('${baseUrl}/edu3/system/org/unit/list.html?isSubPage=y&unitId='+unitId, {}, function(){
			$("#_orgUnitListContent").find("[layoutH]").layoutH();
		});
	}
	
</script>
</head>
<body>
	<div
		style="float: left; width: 25%; padding: 0; border: 0; margin: -1px;">
		<div class="_orgUnitLeftTree"
			style="display: block; overflow: auto; padding-bottom: 6px; border: solid 1px #CCC; line-height: 21px; width: 100%; height: auto;">
			<ul id="_orgUnitListTree" class="ztree"></ul>
		</div>
	</div>

	<div class="page" id="_orgUnitListContent"
		style="float: left; width: 75%; padding: 0; border: 0; margin: -1px;">
		<h3>请选择左边的树节点</h3>
	</div>
</body>
</html>