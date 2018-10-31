<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

<title>在线帮助文章管理</title>
</head>
<body>
	<script type="text/javascript">	
jQuery(document).ready(function(){
	//生成数据树
	 $("#_helpArticleListTree").zTree({	expandSpeed: "",showLine: true,callback: {	click:	articleListTreeOnChange }	}, [${helpChannelTree}]);
		if($('._helpArticleLeftTree')){//如果有左侧树，则赋值为自适应高度		
			$('._helpArticleLeftTree').height($("#container .tabsPageContent").height()-5);
		}	
		articleListTreeOnChange();
	});
	
	function articleListTreeOnChange(event, treeId, treeNode) {		
		var channelId = "";
		if(treeNode){
			channelId = treeNode.id;
		}
		$("#_helpArticleListContent").loadUrl('${baseUrl}/edu3/portal/manage/helper/article/list.html?isSubPage=y&channelId='+channelId+"&rootId=${rootId}", {}, function(){
			$("#_helpArticleListContent").find("[layoutH]").layoutH();
		});
	}
	

</script>
	<div
		style="float: left; width: 19%; padding: 0; border: 0; margin: -1px;">
		<div class="_helpArticleLeftTree"
			style="display: block; overflow: auto; padding-bottom: 6px; border: solid 1px #CCC; line-height: 21px; width: 100%; height: auto;">
			<ul id="_helpArticleListTree" class="ztree"></ul>
		</div>
	</div>

	<div class="page" id="_helpArticleListContent"
		style="float: left; width: 81%; padding: 0; border: 0; margin: -1px;">
		<h3>请选择左边树节点</h3>
	</div>

</body>
</html>