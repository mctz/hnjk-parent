<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>系统演示-前台组件演示</title>
</head>
<body>
	<script type="text/javascript">
function validateDemoconvertForm(obj){
	return validateCallback(obj,_ajaxdone2);
}

function _ajaxdone2(json){
	DWZ.ajaxDone(json);
	if (json.statusCode == 200){
		$("#_previewText").html(json.text);
	}
}
</script>
	<h2 class="contentTitle">文档转换</h2>
	<div class="page">
		<div class="pageContent">
			<form method="post"
				action="${baseUrl}/edu3/demo/webplugin/dodocconvert.html"
				class="pageForm" onsubmit="return validateDemoconvertForm(this);">

				<div class="pageFormContent" layoutH="97">
					<div style="width: 98%; height: auto" id="_previewText"></div>
					<table class="form">
						<tr>
							<td width="30%">上传word文件：</td>
							<td><gh:upload hiddenInputName="attachId"
									uploadType="attach" fileExt="doc|" isStoreToDatabase="N"
									baseStorePath="users,${username },attachs"
									attachList="${attachList }" /></td>

						</tr>
					</table>
				</div>
				<div class="formBar">
					<ul>
						<li><div class="buttonActive">
								<div class="buttonContent">
									<button type="submit">提交</button>
								</div>
							</div></li>
						<li><div class="button">
								<div class="buttonContent">
									<button type="button" class="close"
										onclick="navTab.closeCurrentTab();">取消</button>
								</div>
							</div></li>
					</ul>
				</div>
			</form>
		</div>
	</div>
</body>