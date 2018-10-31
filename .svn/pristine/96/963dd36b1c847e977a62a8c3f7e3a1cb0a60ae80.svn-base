<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
</head>
<body>
	<div class="page">
		<div class="pageContent">
			<form id="packageOfferUploadForm" class="pageForm" method="post"
				action="${baseUrl}/edu3/recruit/matriculate/uploadPackageOffer-save.html"
				onsubmit="return validateCallback(this);">
				<div class="pageFormContent" layoutH="97">
					<table class="form" id="packageOfferUploadTable">
						<c:forEach items="${dictionaries}" var="dic" varStatus="vs">
							<c:if test="${!fn:contains(dic.dictName,'录取通知书')}">
								<tr>
									<td width="20%">${dic.dictName }：</td>
									<td width="80%"><gh:upload hiddenInputName="${dic.dictCode }"
										uploadType="attach" baseStorePath="TheAdmissionNotice" formId="${dic.dictCode }"
										attachList="${dic.attachs }" formType="packageOfferUpload"
										uploadLimit="1" fileExt="doc|docx|pdf|xls|xlsx|jpg" isMulti="false" /></td>
								</tr>
							</c:if>
						</c:forEach>
						<tr>
							<td width="20%" style="height: 50px;">导入结果：</td>
							<td align="center" width="80%" id="packageOffer_importResult">
							</td>
						</tr>
					</table>
				</div>
				<div class="formBar">
					<ul>
						<li>
							<div class="buttonActive">
								<div class="buttonContent">
									<button type="submit">提交</button>
								</div>
							</div>
						</li>
						<li>
							<div class="button">
								<div class="buttonContent">
									<button type="button" class="close"
										onclick="$.pdialog.closeCurrent();">取消</button>
								</div>
							</div>
						</li>
					</ul>
				</div>
			</form>
		</div>
	</div>
</body>
</html>
