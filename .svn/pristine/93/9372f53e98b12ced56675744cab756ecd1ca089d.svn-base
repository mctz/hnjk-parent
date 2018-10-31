<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator"
	prefix="decorator"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>${school}${schoolConnectName}- <decorator:title
		default="在线帮助" /></title>
<gh:loadCom components="bbs-defaultcss,jquery,ztree" />
<style type="text/css">
<!--
.STYLE1 {
	color: #FFFFFF;
	font-weight: bold;
}

.STYLE2 {
	color: #999999
}

#topicbody .nav_topic {
	height: 270px;
}
-->
</style>
<script type="text/javascript">
	var zTree1;
	var setting;
	setting = {			
			isSimpleData: true,
			expandSpeed: "",			
			treeNodeKey: "id",
			treeNodeParentKey: "pId",
			showLine: true,
			callback: {click:	_demoLeftTreeOnClick },
			root:{ 
				isRoot:true,
				nodes:[]
			}	
		};
	zNodes = ${jsonStr}; 
	
	jQuery(document).ready(function(){	
		setting.expandSpeed = ($.browser.msie && parseInt($.browser.version)<=6)?"":"fast";
			zTree1 = $("#_demotree").zTree(setting, zNodes);
			var node =zTree1.getNodeByParam("id", "${channelId}", null);
			zTree1.selectNode(node);
			var unit_height=20;
			$(".tree-node").each(function(){
				unit_height+=19;
			});
			$(".tabtitle #searchbody").attr("style","height:"+unit_height+"px;");
		})
		
	function _demoLeftTreeOnClick(event, treeId, treeNode){		
		return false;
	}
	//提交反馈
	function ajaxUpdateArticleResolveCount(){
		var url = "${baseUrl }/edu3/portal/manage/helper/article/resovle/update/ajax.html";
		jQuery.post(url,{articleResourceId:'${helpArticle.resourceid }',userfacebook:$("input[@name='userfacebook']:checked").val()});
		alert("您的提交已经成功，感谢的您的反馈。");  
	}
	//查询
	function searchHelpTopics(){
		var condition = $("#searchTopicKeyword").val();			
		if($.trim(condition)==""){
			alert("查询条件不能为空！");
		} else {
			window.open('${baseUrl}/portal/help/search.html?condition='+condition,'newwindow');
			
		}
	}	
	</script>
<decorator:head />
</head>

<body>
	<!-- top -->
	<UL id=header>
		<LI style="FLOAT: left"><IMG
			src="${baseUrl }/style/default/images/help_img.gif" alt="在线帮助中心"
			width="765" height="85"
			style="BORDER-TOP-WIDTH: 0px; BORDER-LEFT-WIDTH: 0px; BORDER-BOTTOM-WIDTH: 0px; BORDER-RIGHT-WIDTH: 0px">

		</LI>
		<LI
			style="MARGIN-TOP: 5px; FLOAT: left; MARGIN-LEFT: 50px; TEXT-ALIGN: center">
		<LI
			style="BORDER-RIGHT: #ffffff 1px solid; PADDING: 5px 0px 0px; BORDER-TOP: #ffffff 1px solid; MARGIN-TOP: 0px; BORDER-LEFT: #ffffff 1px solid; BORDER-BOTTOM: #ffffff 1px solid; LIST-STYLE-TYPE: none">
			<UL>
				<c:if test="${not empty course}">
					<c:set var="querys" value="?courseId=${course.resourceid}"></c:set>
					<c:set var="coursequerystr" value="&courseId=${course.resourceid}"></c:set>
				</c:if>
				<c:choose>
					<c:when test="${not empty user }">
						<DIV>
							欢迎您：<a
								href="${baseUrl }/edu3/learning/bbs/user.html?userid=${user.resourceid}${coursequerystr }"
								target="_blank" title="查看用户${user.username }的资料">${user.username }</a>
						</DIV>
						<DIV>
							<LI class=m_li_top style="DISPLAY: inline"></LI>
							<DIV class="submenu submunu_popup">
								<DIV class=menuitems></DIV>
							</DIV>
							<A
								href="${baseUrl }/edu3/learning/bbs/search.html?search=mine${coursequerystr }">我的主题</A>
							| <A
								href="${baseUrl }/edu3/learning/bbs/advancedSearch.html${querys}">搜索</A>
						</DIV>
					</c:when>
					<c:otherwise>
						<DIV>欢迎您：${ user.username}</DIV>
					</c:otherwise>
				</c:choose>
			</UL>
		</LI>
	</UL>
	<br />
	<!--导航-->
	<DIV id=topLayout>

		<DIV class=notice>
			<DIV
				style="PADDING-LEFT: 10px; FLOAT: left; WIDTH: auto; TEXT-ALIGN: left"
				class="STYLE1">最新公告：系统在线帮助中心上线了！</DIV>
		</DIV>
	</DIV>
	<!--搜索开始-->
	<DIV class=nav_top id=centerLayout31>
		<DIV class=searchbody id=searchbody
			style="text-align: center; BACKGROUND: #eff6fb; height: 30px; margin-bottom: 12px">
			<DL class=dis>
				<span style="font-weight: bold; font-size: 14px">帮助搜索：</span>
				<input type="text" id="searchTopicKeyword" name="keyword"
					style="height: 22px; width: 450px; BACKGROUND: #fff"
					value="${tags}"
					onkeydown="if(event.keyCode==13) {searchHelpTopics();return false;}" />
				<BUTTON type="button" onclick="searchHelpTopics();"
					style="height: 26px;">搜 索</BUTTON>
			</DL>
		</DIV>
		<!-- 左侧开始 -->
		<DIV id=left>
			<DIV class=tabtitle>
				<DIV class=search_bot id=search_bot>
					<P class=tab_search value="0">目录</P>
					<DIV style="CLEAR: both"></DIV>
				</DIV>
				<DIV class=searchbody id=searchbody style="height: 500px;">
					<DL class=dis>
						<div class="_demoLeftTree"
							style="display: block; height: auto; height: 500px; padding-bottom: 6px; line-height: 21px;">
							<div class="zTreeDemoBackground">
								<ul id="_demotree" class="ztree"></ul>
							</div>
						</div>
					</DL>
				</DIV>
			</DIV>
			<BR>
		</DIV>

		<!--主要内容区-->
		<DIV id=right></DIV>
		<DIV id=center1></DIV>
		<DIV id="center2">
			<decorator:body />
		</DIV>
	</DIV>

	<DIV style="MARGIN-TOP: 30px; MARGIN-BOTTOM: 10px"></DIV>
	<DIV class=copyright>
		<DIV id=stylemsg style="PADDING-RIGHT: 5px; COLOR: red"></DIV>
		<DIV>
			<DIV>
				Copyright &copy; 2001-2011 ${school}${schoolConnectName}All Rights
				Reserved <br />
				<gh:version />
			</DIV>
			<DIV>
				<BR>
			</DIV>
		</DIV>
	</DIV>
</body>
</html>