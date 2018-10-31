<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>课程素材资源管理</title>
<gh:loadCom components="treeView" />
<script type="text/javascript">
	$(document).ready(function(){
		$("#materesourcetree").treeview({
			persist: "location",			
			unique: true
		});		
		if($('.materesourceLeftTree')){//如果有左侧树，则赋值为自适应高度		
			$('.materesourceLeftTree').height($("#container .tabsPageContent").height()-5);
		}
		
	});
	
	//左边树的链接
	function goCoursewareSelected(courseId,syllabusId){		
		var currentIndex = $('#materesourcecurrentIndex').val();
		var url = "${baseUrl}/edu3/framework/metares/list.html?syllabusId="+syllabusId+"&courseId="+courseId;
		
		$("#materesourcetree .itemtext").find("a").removeClass("selected");
		$("#materesourcetree span[nodeid='"+syllabusId+"']").find("a").addClass("selected");
		
		for(var i in [0,1,2,3]){
			$("#mateTab"+i).attr('href',url+"&currentIndex="+i);
		}
		$("#mateTab"+currentIndex).click();
	}	
	//增加
	function mateAddHandle(syllabusId,courseId,currentIndex,pageNum,pageSize){
		if(syllabusId == ""){
	 		alertMsg.warn("请选择左边的某个知识节点！");
	 		return false;
	 	}
		var mateurl = "${baseUrl}/edu3/framework/metares/list.html?syllabusId="+syllabusId+"&courseId="+courseId+"&currentIndex="+currentIndex;
	 	pageNum = pageNum || 1;
	 	pageSize = pageSize || 20;		
	 	mateurl += "&pageNum="+pageNum+"&pageSize="+pageSize;
		$("#mateTab"+currentIndex).attr('href',mateurl).click();
	}
	//修改
	function mateModifyHandle(syllabusId,courseId,bodyname,currentIndex,pageNum,pageSize){
		if(isCheckOnlyone('resourceid',bodyname)){
			var resourceid = $(bodyname+" input[@name='resourceid']:checked").val();
			var url = "${baseUrl}/edu3/framework/metares/list.html?syllabusId="+syllabusId+"&courseId="+courseId+"&resourceid="+resourceid+"&currentIndex="+currentIndex;
			pageNum = pageNum || 1;
		 	pageSize = pageSize || 20;		
		 	url += "&pageNum="+pageNum+"&pageSize="+pageSize;
			$("#mateTab"+currentIndex).attr('href',url).click();
		 }			
	}
	//删除
	function mateRemoveHandle(msg,postUrl,bodyname,myMateAjaxDone){
		if(!isChecked('resourceid',bodyname)){
 			alertMsg.warn('请选择一条要操作记录！');
			return false;
	 	}
		alertMsg.confirm(msg, {
				okCall: function(){//执行			
					var res = "";
					var k = 0;
					var num  = $(bodyname+" input[name='resourceid']:checked").size();
					$(bodyname+" input[@name='resourceid']:checked").each(function(){
	                        res+=$(this).val();
	                        if(k != num -1 ) res += ",";
	                        k++;
	                 });
	                
					$.post(postUrl,{resourceid:res}, myMateAjaxDone, "json");
				}
		});			
	}
	
	function myMateAjaxDone(json){
    	DWZ.ajaxDone(json);
	 	if (json.statusCode == 200){
	 		//$("#materesourcetree .itemtext").find("a").removeClass("selected");
			//$("#materesourcetree span[nodeid='"+json.syllabusId+"']").find("a").addClass("selected");
			
			var url = "${baseUrl}/edu3/framework/metares/list.html?syllabusId="+json.syllabusId+"&courseId="+json.courseId+"&currentIndex="+json.currentIndex;
	 		$("#mateTab"+json.currentIndex).attr('href',url).click();
	 	}
    }
</script>
</head>
<body>
	<div style="float: left; width: 19%">
		<div class="materesourceLeftTree"
			style="display: block; overflow: auto; padding-bottom: 6px; border: solid 1px #CCC; line-height: 21px;">
			<gh:treeview nodes="${materesourcetree}" id="materesourcetree"
				css="folder" expandLevel="100" />
		</div>
	</div>

	<div class="page" style="float: left; width: 81%">
		<div class="tabs" currentIndex="${condition['currentIndex'] }"
			eventtype="click">
			<div class="tabsHeader">
				<div class="tabsHeaderContent">
					<input type="hidden" id="materesourcecurrentIndex"
						value="${condition['currentIndex'] }">
					<ul>
						<li><a id="mateTab0"
							href="${baseUrl}/edu3/framework/metares/list.html?syllabusId=${condition['syllabusId'] }&courseId=${condition['courseId'] }&currentIndex=0"
							onclick="$('#materesourcecurrentIndex').val('0');" class="j-ajax"><span>学习目标</span></a></li>
						<li><a id="mateTab1"
							href="${baseUrl}/edu3/framework/metares/list.html?syllabusId=${condition['syllabusId'] }&courseId=${condition['courseId'] }&currentIndex=1"
							onclick="$('#materesourcecurrentIndex').val('1');" class="j-ajax"><span>学习材料</span></a></li>
						<li><a id="mateTab2"
							href="${baseUrl}/edu3/framework/metares/list.html?syllabusId=${condition['syllabusId'] }&courseId=${condition['courseId'] }&currentIndex=2"
							onclick="$('#materesourcecurrentIndex').val('2');" class="j-ajax"><span>随堂练习</span></a></li>
						<li><a id="mateTab3"
							href="${baseUrl}/edu3/framework/metares/list.html?syllabusId=${condition['syllabusId'] }&courseId=${condition['courseId'] }&currentIndex=3"
							onclick="$('#materesourcecurrentIndex').val('3');" class="j-ajax"><span>参考资料</span></a></li>
					</ul>
				</div>
			</div>
			<div class="tabsContent" style="height: 100%;">
				<!-- 1 -->
				<div id="mateTabContent0"></div>
				<!-- 2 -->
				<div id="mateTabContent1"></div>
				<!-- 3 -->
				<div id="mateTabContent2"></div>
				<!-- 4 -->
				<div id="mateTabContent3"></div>
			</div>
			<div class="tabsFooter">
				<div class="tabsFooterContent"></div>
			</div>
		</div>
	</div>
</body>
</html>