<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>招生账号列表</title>

<script type="text/javascript">
	$(document).ready(function(){
		 $("#_brschoolAccoutListorgUnitTree").zTree({	expandSpeed: "",showLine: true,callback: {	click:	brschoolAccoutListTreeOnChange }	}, [${unitTree}]);
		if($('._brschoolAccoutLeftTree')){//如果有左侧树，则赋值为自适应高度		
			$('._brschoolAccoutLeftTree').height($("#container .tabsPageContent").height()-5);
		}
		brschoolAccoutListTreeOnChange();
	});
	

	function brschoolAccoutListTreeOnChange(event, treeId, treeNode) {		
		var unitId = "";
		if(treeNode){
			unitId = treeNode.id;
		}
		$("#_brschoolAccoutListContent").loadUrl('${baseUrl}/edu3/recruit/recruitmanage/brschoolaccount-list.html?isSubPage=y&unitId='+unitId, {}, function(){
			$("#_brschoolAccoutListContent").find("[layoutH]").layoutH();
		});
	}


</script>
</head>
<body>
	<div
		style="float: left; width: 19%; padding: 0; border: 0; margin: -1px;">
		<div class="_brschoolAccoutLeftTree"
			style="display: block; overflow: auto; padding-bottom: 6px; border: solid 1px #CCC; line-height: 21px; width: 100%; height: auto;">
			<ul id="_brschoolAccoutListorgUnitTree" class="ztree"></ul>
		</div>
	</div>
	<div class="page" id="_brschoolAccoutListContent"
		style="float: left; width: 81%; padding: 0; border: 0; margin: -1px;">
		<h3>请选择左边树节点</h3>
	</div>
</body>
</html>
