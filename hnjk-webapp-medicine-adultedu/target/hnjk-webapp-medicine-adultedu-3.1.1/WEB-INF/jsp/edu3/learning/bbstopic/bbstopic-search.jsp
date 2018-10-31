<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>高级查询</title>

</head>
<body>
	<script type="text/javascript">
</script>
	<div class="page">
		<div class="pageContent">
			<form method="post"
				action="${baseUrl }/edu3/metares/topicreply/bbstopic/list.html"
				class="pageForm" onsubmit="return navTabSearch(this);">
				<div class="pageFormContent" layoutH="58">
					<div id="advanceSearch">
						<label>论坛版块：</label>
						<gh:bbsSectionSelect id="search_bbsSectionId" name="bbsSectionId"
							style="width: 40%;" value="${condition['bbsSectionId'] }" />
						<%--
					<select id="bbsSectionId" name="bbsSectionId" style="width: 40%;">
						<option value="" ${(empty condition['bbsSectionId'])?'selected':''}>选取所有版块...</option>											
						<c:forEach items="${parentBbsSections}" var="item">
							<c:forEach items="${item.value}" var="section" varStatus="vs">
								<c:choose>
									<c:when test="${vs.first }">
										<option value="${section.resourceid }" ${(condition['bbsSectionId']==section.resourceid)?'selected':''}>
										╋${section.sectionName }
										</option>
									</c:when>
									<c:otherwise>
										<option value="${section.resourceid }" ${(condition['bbsSectionId']==section.resourceid)?'selected':''}>
											<c:forEach begin="1" end="${section.sectionLevel }" step="1">&nbsp;&nbsp;</c:forEach>
											├${section.sectionName }
										</option>
									</c:otherwise>
								</c:choose>
							</c:forEach>
						</c:forEach>		
					</select>	
					 --%>
					</div>
					<div>
						<label>标题：</label> <input type="text" name="title"
							value="${condition['title'] }" />
					</div>
					<div>
						<label>帖子类型：</label>
						<gh:select name="topicType" value="${condition['topicType']}"
							dictionaryCode="CodeBbsTopicType" />
					</div>
					<div>
						<label>帖子状态：</label>
						<gh:select name="status" value="${condition['status']}"
							dictionaryCode="CodeBbsTopicStatus" />
					</div>
					<div>
						<label>发帖开始时间：</label> <input type="text"
							name="fillinDateStartStr"
							value="${condition['fillinDateStartStr']}" class="Wdate"
							id="tbeginTime"
							onFocus="WdatePicker({isShowWeek:true,maxDate:'#F{$dp.$D(\'tendTime\')}'})" />

					</div>
					<div>
						<label>发帖截止时间：</label> <input type="text" name="fillinDateEndStr"
							value="${condition['fillinDateEndStr']}" class="Wdate"
							id="tendTime"
							onFocus="WdatePicker({isShowWeek:true,minDate:'#F{$dp.$D(\'tbeginTime\')}'})" />
					</div>
					<div>
						<label>发帖人：</label> <input type="text" name="fillinMan"
							value="${condition['fillinMan']}" />
					</div>

					<div class="divider">divider</div>
					<div>
						<label>排序条件：</label> <select name="orderBy">
							<option value="">--请选择--</option>
							<option value="fillinDate">发帖时间</option>
							<option value="lastReplyDate">最后回帖时间</option>
							<option value="topicType">帖子类型</option>
							<option value="status">帖子状态</option>
						</select> <label class="radioButton"><input name="orderType"
							type="radio" value="ASC" />顺序</label> <label class="radioButton"><input
							name="orderType" type="radio" value="DESC" />倒序</label>
					</div>
				</div>
				<div class="formBar">
					<ul>
						<li><div class="buttonActive">
								<div class="buttonContent">
									<button type="submit">开始检索</button>
								</div>
							</div></li>
						<li><div class="button">
								<div class="buttonContent">
									<button type="reset">清空重输</button>
								</div>
							</div></li>
					</ul>
				</div>
			</form>
		</div>
	</div>
</body>
</html>
