<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>角色权限管理</title>

<script type="text/javascript">
	
	jQuery(document).ready(function(){
		//生成数据树
		 var _resource_ztree = $("#_resourceListTree").zTree({	expandSpeed: "",showLine: true,callback: {	click:	resourceListTreeOnChange }	}, [${resourceTree}]);
		if($('._resourceLeftTree')){//如果有左侧树，则赋值为自适应高度		
			$('._resourceLeftTree').height($("#container .tabsPageContent").height()-5);
		}		
		
		resourceListTreeOnChange();
		_resource_ztree.expandAll(false);
	});
	
	
	
	function resourceListTreeOnChange(event, treeId, treeNode) {		
		var resid = "";		
		if(treeNode){
			resid = treeNode.id;
		}
		$("#_resourceListContent").loadUrl('${baseUrl}/edu3/system/authoriza/resource/list.html?isSubPage=y&resId='+resid, {}, function(){
			$("#_resourceListContent").find("[layoutH]").layoutH();
		});
	}
		
</script>
</head>
<body>
	<div
		style="float: left; width: 19%; padding: 0; border: 0; margin: -1px;">
		<div class="_resourceLeftTree"
			style="display: block; overflow: auto; padding-bottom: 6px; border: solid 1px #CCC; line-height: 21px; width: 100%; height: auto;">
			<ul id="_resourceListTree" class="ztree"></ul>
		</div>
	</div>

	<div class="page" id="_resourceListContent"
		style="float: left; width: 81%; padding: 0; border: 0; margin: -1px;">
		<h3>请选择右边的树节点</h3>
	</div>
</body>
</html>