<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>毕业论文交流区-学生主界面</title>
<script type="text/javascript">
	
	//浏览
	function _viewGruateMsg(msgid){
		var url ="${baseUrl}/edu3/teaching/graduateMsg/edit.html?resourceid="+msgid;
		navTab.openTab('RES_TEACHING_THESIS_MSG_VIEW',url,'毕业论文交流区');
	}
</script>
</head>
<body>

	<div class="page">
		<div class="tabs">
			<div class="tabsHeader">
				<div class="tabsHeaderContent">
					<ul>
						<li><a href="javascript:;"><span>毕业论文</span></a></li>

					</ul>
				</div>
			</div>
			<div class="tabsContent">
				<div>

					<div layoutH="42"
						style="float: left; display: block; overflow: auto; width: 18%; border: solid 1px #CCC; line-height: 21px; background: #fff">
						<ul class="tree treeFolder">
							<li><a href="javascript">毕业论文</a>
								<ul>
									<c:forEach items="${resList }" var="res">

										<li><a
											<c:if test="${res.resourcePath !='#' }">href="${baseUrl }${res.resourcePath}" target="ajax" rel="jbsxBox" </c:if>>${res.resourceName }</a></li>
									</c:forEach>

								</ul></li>

						</ul>
					</div>

					<div id="jbsxBox" style="margin-left: 19%;">
						<h2>请选择左边菜单</h2>
					</div>

				</div>

			</div>
			<div class="tabsFooter">
				<div class="tabsFooterContent"></div>
			</div>
		</div>
</body>
</html>