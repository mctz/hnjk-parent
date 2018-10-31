<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>系统演示-前台组件演示</title>
</head>
<body>
	<script type="text/javascript">
var zTree1;
var setting;
setting = {			
		isSimpleData: true,
		treeNodeKey: "id",
		treeNodeParentKey: "pId",
		showLine: true,
		callback: {click:	_demoLeftTreeOnClick },
		root:{ 
			isRoot:true,
			nodes:[]
		}	
	};
	
zNodes =[		
 		{ id:3, pId:0, name:"组件演示", open:true},
 		{ id:31, pId:3, name:"UI组件演示", open:true},
 		{ id:311, pId:31, name:"树(tree)", "url":"${baseUrl}/edu3/system/demo/webplugin/ztree/show.html", "target":"ajax","rel":"rightContent"},
 		{ id:312, pId:31, name:"数据树(checkbox tree)", "url":"standardDemoForIe6.html", "target":"testIframe"},
 		{ id:313, pId:31, name:"数据树(radio tree)", "url":"standardDemoForIe6.html", "target":"testIframe"},
 		{ id:314, pId:31, name:"树型下拉(tree dropdown)", "url":"standardDemoForIe6.html", "target":"testIframe"},
 		{ id:315, pId:31, name:"表格(grid)", "url":"standardDemoForIe6.html", "target":"testIframe"},
 		{ id:316, pId:31, name:"图表(chart)", "url":"standardDemoForIe6.html", "target":"testIframe"},
 		{ id:317, pId:31, name:"多文件上传(muiltupload)", "url":"standardDemoForIe6.html", "target":"testIframe"},
 		{ id:318, pId:31, name:"日期(datepicker)", "url":"standardDemoForIe6.html", "target":"testIframe"},
 		{ id:319, pId:31, name:"表单验证(vilidate form)", "url":"standardDemoForIe6.html", "target":"testIframe"},
 		{ id:3110, pId:31, name:"窗口对话框(window)", "url":"standardDemoForIe6.html", "target":"testIframe"},
 		{ id:3111, pId:31, name:"提示框(alert)", "url":"standardDemoForIe6.html", "target":"testIframe"},
 		{ id:3112, pId:31, name:"标签页(tabs)", "url":"standardDemoForIe6.html", "target":"testIframe"},
 		{ id:3113, pId:31, name:"富编辑器(htmleditor)", "url":"standardDemoForIe6.html", "target":"testIframe"},
 		{ id:3114, pId:31, name:"按钮(button)", "url":"standardDemoForIe6.html", "target":"testIframe"},
 		
 		{ id:32, pId:3, name:"业务组件演示", open:true},
 		{ id:321, pId:32, name:"选取字典值", "url":"checkboxDemo.html", "target":"testIframe"},
 		{ id:322, pId:32, name:"组织树", "url":"radioDemo.html", "target":"testIframe"}, 		
 		{ id:323, pId:32, name:"Excel导出与导入", "url":"${baseUrl}/edu3/system/demo/webplugin/excel/showexport.html", "target":"ajax","rel":"rightContent"},
 		{ id:323, pId:32, name:"报表打印", "url":"${baseUrl}/edu3/system/demo/webplugin/print/show.html", "target":"ajax","rel":"rightContent"},
 		{ id:324, pId:32, name:"远程调用文档转换服务", "url":"${baseUrl}/edu3/demo/webplugin/docconvert.html","target":"ajax","rel":"rightContent"},
 		{ id:325, pId:32, name:"业务审计日志", "url":"${baseUrl}/edu3/demo/webplugin/history.html","target":"ajax","rel":"rightContent"},
 	];


jQuery(document).ready(function(){	
	setting.expandSpeed = ($.browser.msie && parseInt($.browser.version)<=6)?"":"fast";
	zTree1 = $("#_demotree").zTree(setting, zNodes);
		if($('._demoLeftTree')){//如果有左侧树，则赋值为自适应高度		
			$('._demoLeftTree').height($("#container .tabsPageContent").height()-5);
		}	
	})

function _demoLeftTreeOnClick(event, treeId, treeNode){
	//alert(treeId);
}	
</script>
	<div style="float: left; width: 19%">
		<div class="_demoLeftTree"
			style="display: block; overflow: auto; padding-bottom: 6px; border: solid 1px #CCC; line-height: 21px;">
			<div class="zTreeDemoBackground">
				<ul id="_demotree" class="ztree"></ul>
			</div>
		</div>
	</div>
	<div class="page" id="rightContent" style="float: left; width: 81%">
		<h1>请点击左边的目录树！</h1>
		<p>
			<a
				href="${baseUrl }/j_spring_security_switch_user?j_username=zs_leader">切换用户</a>
	</div>
</body>