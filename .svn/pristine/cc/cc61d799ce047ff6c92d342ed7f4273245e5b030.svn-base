<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>用户管理</title>

<script type="text/javascript">

jQuery(document).ready(function(){
	//生成数据树
	 $("#_userListorgUnitTree").zTree({	expandSpeed: "",showLine: true,callback: {	click:	userListTreeOnChange }	}, [${unitTree}]);
		
		if($('._userLeftTree')){//如果有左侧树，则赋值为自适应高度		
			$('._userLeftTree').height($("#container .tabsPageContent").height()-5);
		}	
		userListTreeOnChange();
	});
	
	function userListTreeOnChange(event, treeId, treeNode) {		
		var unitId = "";
		if(treeNode){
			unitId = treeNode.id;
		}
		$("#_userListContent").loadUrl('${baseUrl}/edu3/system/org/user/list.html?isSubPage=y&unitId='+unitId, {}, function(){
			$("#_userListContent").find("[layoutH]").layoutH();
		});
	}

	
	
</script>

</head>
<body>
	<div
		style="float: left; width: 25%; padding: 0; border: 0; margin: -1px;">
		<div class="_userLeftTree"
			style="display: block; overflow: auto; padding-bottom: 6px; border: solid 1px #CCC; line-height: 21px; width: 100%; height: auto;">
			<ul id="_userListorgUnitTree" class="ztree"></ul>
		</div>
	</div>

	<div class="page" id="_userListContent"
		style="float: left; width: 75%; padding: 0; border: 0; margin: -1px;">
		<h3>请选择左边树节点</h3>
	</div>
</body>
</html>