<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<style>
.MzTreeViewCell0 {
	border-bottom: 1px solid #CCCCCC;
	padding: 0px;
	margin: 0px;
}

.MzTreeViewCell1 {
	border-bottom: 1px solid #CCCCCC;
	border-left: 1px solid #CCCCCC;
	width: 200px;
	padding: 0px;
	margin: 0px;
	display: none;
}

#treeDIV input {
	vertical-align: middle;
}
</style>
</head>
<body>
	<script language="javaScript"
		src="${baseUrl}/jscript/mztree/MzTreeView12.js"></script>
	<script type="text/javascript">
		$(document).ready(function(){
			// 修改高度
			$("#treeDIV").css("height",parseInt($(".dialogContent").css("height"))-30);
			// 构造树形
			window.tree = new MzTreeView("tree");
			${htmlStr}
			tree.setIconPath("${baseUrl}/jscript/mztree/images/"); //可用相对路径
			tree.setURL("#");
			tree.wordLine = false;
			tree.setTarget("main");
			$("#treeDIV").html(tree.toString());
			tree.expandAll();		
		});
		
		function selectCm() {
			var es=document.getElementsByName("sel");
			var ids="",names="";
			for(var i=0;i<es.length;i++) {
				if (es[i].checked){
					ids+=es[i].value+",";
					names+=$(es[i]).next().html()+",";
				}
			}
			if(ids.length>0) ids = ids.substring(0,ids.length-1);
			$.ajax({
		 		url: '${baseUrl}/edu3/framework/system/authoriza/saveRoleResourceRef.html?roleId=${param.roleId}&resourceIds='+ids,
		 		type: 'POST',
		 		dataType: 'html', 	
		 		success: function(data){
		 			alertMsg.warn(data=="true"?"修改成功！":"修改失败！");
		  			$("#closeBTN").click();
				}
			});
		}
	</script>
	<div class="page">
		<div class="formBar">
			<ul>
				<li><div class="buttonActive" style="padding-right: 8px">
						<div class="buttonContent">
							<button type="submit" onclick='selectCm()'>确定</button>
						</div>
					</div></li>
				<li><div class="buttonActive" style="padding-right: 8px">
						<div class="buttonContent">
							<button type="submit" class="close" id="closeBTN">取消</button>
						</div>
					</div></li>
			</ul>
		</div>

		<div id="treeDIV" style="overflow: auto; width: 100%;"></div>
	</div>
</body>