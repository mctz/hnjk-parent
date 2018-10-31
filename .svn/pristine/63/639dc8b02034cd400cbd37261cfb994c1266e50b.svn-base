<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>导出学生成绩</title>


</head>
<body>
	<div class="page">
		<div class="pageHeader" layouth="15">
			<form id="examResultExportForm" onsubmit="return dialogSearch(this);"
				action="${baseUrl }/edu3/teaching/examresults/export-genflie.html"
				method="post">
				<input name="examResultExportSubId" id="examResultExportSubId"
					type="hidden" value="${examSub.resourceid}" />
				<table width="100%">
					<thead>
						<tr height="20px">
							<td width="100" style="font-weight: bolder">批次名称：</td>
							<td colspan="2" style="text-align: left">${examSub.batchName }</td>
						</tr>
					</thead>
					<tbody>
						<c:if test="${attId != null }">
							<tr height="20px">
								<td colspan="3">该批次的成绩下载文件已生成！</td>
							</tr>
						</c:if>
						<c:if test="${logList !=null }">
							<tr height="20px">
								<td colspan="3" style="font-weight: bolder">操作信息：</td>
							</tr>
							<c:forEach items="${logList}" var="log">
								<tr height="10px">
									<td colspan="3"><c:if
											test="${log.optionType == 'examResultsExport' }">
						   		 		导出成绩人：${log.fillinMan } 导出时间：<fmt:formatDate
												value="${log.fillinDate }" pattern="yyyy-MM-dd HH:mm:ss" />
										</c:if> <c:if test="${log.optionType == 'examResultsImportChecked' }">
						   		 		导入成绩人：${log.fillinMan } 导入时间：<fmt:formatDate
												value="${log.fillinDate }" pattern="yyyy-MM-dd HH:mm:ss" />
										</c:if></td>
								</tr>
							</c:forEach>

						</c:if>
						<c:if test="${attId != null }">
							<tr height="30px">
								<td colspan="3"><a
									href="${baseUrl }/edu3/teaching/examresults/download-file.html?attId=${attId }">下载</a>成绩文件</td>
							</tr>
						</c:if>
						<tr>
							<td colspan="3"><HR width="100%" color="#B8D0D6" SIZE="1"></td>
						</tr>
						<tr height="20px">
							<td colspan="3">请选择要导出的成绩审核类型：</td>
						</tr>
						<tr height="30px">
							<td width="100" colspan="3"><INPUT type="checkbox" value="0"
								name="checkstatus" />保存&nbsp;&nbsp; <INPUT type="checkbox"
								value="1" name="checkstatus" />提交&nbsp;&nbsp; <INPUT
								type="checkbox" value="2" name="checkstatus" />审核未通过&nbsp; <INPUT
								type="checkbox" value="3" name="checkstatus" />审核通过&nbsp; <INPUT
								type="checkbox" value="4" name="checkstatus" checked="checked" />发布
							</td>
						</tr>
						<tr height="50px">
							<td width="100"></td>
							<td width="100"><c:choose>
									<c:when test="${attId !=null }">
										<a href="javascript:void(0)" class="button"
											onclick="doExamResultReExport('${attId}')"> <span>重新导出</span></a>
									</c:when>
									<c:otherwise>
										<a href="javascript:void(0)" class="button"
											onclick="doExamResultExport();"> <span>导出</span></a>
									</c:otherwise>
								</c:choose></td>
							<td><a href="javascript:void(0)" class="button"
								onclick="$.pdialog.closeCurrent();"> <span>取消</span></a></td>
						</tr>
					</tbody>
				</table>
			</form>
		</div>
	</div>
	<script type="text/javascript">	
	function doExamResultExport(){
		var path=$("#examResultExportForm").attr("action");
		$("#examResultExportForm").attr("action",path+"?type=export")
		$.pdialog.reload($("#examResultExportForm").attr("action"), $("#examResultExportForm").serializeArray());
	}
	function doExamResultReExport(attId){
		if(attId == ""){
			alertMsg.warn("缺少参数！");
			return false;
		}
		var path=$("#examResultExportForm").attr("action");
		$("#examResultExportForm").attr("action",path+"?type=reExport&attId="+attId)
		$.pdialog.reload($("#examResultExportForm").attr("action"), $("#examResultExportForm").serializeArray());
	}
</script>
</body>
</html>