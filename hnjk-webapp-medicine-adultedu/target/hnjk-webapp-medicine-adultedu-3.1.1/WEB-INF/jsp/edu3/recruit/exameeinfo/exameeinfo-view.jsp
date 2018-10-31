<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>考生信息查看</title>
<style type="text/css">
#exameeInfoViewForm td {
	padding: 0;
}
</style>
</head>
<body>
	<div class="page">
		<div class="pageContent">
			<div class="pageFormContent" layoutH="25">
				<table class="form" id="exameeInfoViewForm">
					<tr>
						<td colspan="4">
							<table cellpadding="0" cellspacing="0" border="0" width="100%">
								<tr>
									<td rowspan="6" width="14%"
										style="text-align: center; vertical-align: middle;"><c:choose>
											<c:when test="${not empty exameeInfo.mainPhotoPath}">
												<img
													src="${rootUrl}/common/students/${exameeInfo.mainPhotoPath}"
													width="105" height="150" alt="照 片 " />
											</c:when>
											<c:when test="${not empty exameeInfo.backPhotoPath}">
												<img
													src="${rootUrl}/common/students/${exameeInfo.backPhotoPath}"
													width="105" height="150" alt="照 片 " />
											</c:when>
											<c:otherwise>
												<img src="${baseUrl}/themes/default/images/img_preview.png"
													width="100" height="140" />
											</c:otherwise>
										</c:choose></td>
									<td>考生号：</td>
									<td colspan="3">${exameeInfo.KSH }</td>
								</tr>
								<tr>
									<td>准考证号：</td>
									<td colspan="3">${exameeInfo.ZKZH }</td>
								</tr>
								<tr>
									<td width="15%">姓名：</td>
									<td width="28%">${exameeInfo.XM }</td>
									<td width="15%">外语语种：</td>
									<td width="28%">
										${ghfn:dictCode2Val('CodeForeignLanguage',exameeInfo.WYYZDM) }</td>
								</tr>
								<tr>
									<td>出生日期：</td>
									<td><fmt:formatDate value="${exameeInfo.CSRQ }"
											pattern="yyyy-MM-dd" /></td>
									<td>性别：</td>
									<td>${ghfn:dictCode2Val('CodeSex',exameeInfo.XBDM) }</td>
								</tr>
								<tr>
									<td>身份证号：</td>
									<td colspan="3">${exameeInfo.SFZH }</td>
								</tr>
								<tr>
									<td>考生特征标志：</td>
									<td colspan="3">
										${ghfn:dictCode2Val('CodeCharacteristic',exameeInfo.KSTZBZ) }</td>
								</tr>
							</table>
						</td>
					</tr>
					<tr>
						<td width="20%">招生类别：</td>
						<td width="30%">
							${ghfn:dictCode2Val('CodeRecruitType',exameeInfo.ZSLBDM) }</td>
						<td width="20%">民族：</td>
						<td width="30%">
							${ghfn:dictCode2Val('CodeNation',exameeInfo.MZDM) }</td>
					</tr>
					<tr>
						<td>政治面貌：</td>
						<td>${ghfn:dictCode2Val('CodePolitics',exameeInfo.ZZMMDM) }</td>
						<td>报考专业属性：</td>
						<td>
							${ghfn:dictCode2Val('CodeMajorAttribute',exameeInfo.BKZYSXDM) }</td>
					</tr>
					<tr>
						<td>邮政编码：</td>
						<td>${exameeInfo.YZBM }</td>
						<td>联系电话：</td>
						<td>${exameeInfo.LXDH }</td>
					</tr>
					<tr>
						<td>文化程度：</td>
						<td>
							${ghfn:dictCode2Val('CodeEducationalLevel',exameeInfo.WHCDDM) }</td>
						<td>专业类别：</td>
						<td>
							${ghfn:dictCode2Val('CodeMajorCatogery',exameeInfo.ZYLBDM) }</td>
					</tr>
					<tr>
						<td>工作年月：</td>
						<td><fmt:formatDate value="${exameeInfo.GZRQ }"
								pattern="yyyy-MM-dd" /></td>
						<td>毕业日期：</td>
						<td><fmt:formatDate value="${exameeInfo.BYRQ }"
								pattern="yyyy-MM-dd" /></td>
					</tr>
					<tr>
						<td>毕业学校：</td>
						<td colspan="3">${exameeInfo.BYXX }</td>
					</tr>
					<tr>
						<td>录取通知书邮寄地址：</td>
						<td colspan="3">${exameeInfo.TXDZ }</td>
					</tr>
					<tr>
						<td colspan="4">
							<table cellpadding="0" cellspacing="0" border="0" width="100%">
								<c:forEach begin="0" end="14" step="3" var="i">
									<tr>
										<c:forEach begin="${i }" end="${i+2 }" var="j">
											<c:if test="${j eq 0 }">
												<td width="7%" rowspan="5"
													style="text-align: center; vertical-align: middle;">成<br />绩<br />信<br />息
												</td>
											</c:if>
											<td width="20%">
												${ghfn:dictCode2Val('CodeExameeInfoScore',exameeInfoScoreList[j].scoreCode) }</td>
											<td width="11%"><fmt:formatNumber
													value="${exameeInfoScoreList[j].scoreValue }"
													pattern="####.#" /></td>
										</c:forEach>
									</tr>
								</c:forEach>
							</table>
						</td>
					</tr>
					<tr>
						<td colspan="4">
							<table cellpadding="0" cellspacing="0" border="0" width="100%">
								<c:forEach begin="0" end="5" step="2" var="i">
									<tr>
										<c:forEach begin="${i }" end="${i+1 }" var="j">
											<c:if test="${j eq 0 }">
												<td width="7%" rowspan="5"
													style="text-align: center; vertical-align: middle;">志<br />愿<br />信<br />息
												</td>
											</c:if>
											<td width="46.5%">${exameeInfoWishList[j].ZYBM }
												${exameeInfoWishList[j].ZYMC }</td>
										</c:forEach>
									</tr>
								</c:forEach>
							</table>
						</td>
					</tr>
					<tr>
						<td width="20%">录取专业：</td>
						<td width="40%">${exameeInfo.LQZY } ${exameeInfo.LQZYMC }</td>
						<td width="20%">层次：</td>
						<td width="20%">${exameeInfo.CCMC }</td>
					</tr>
					<tr>
						<td width="20%">学习形式：</td>
						<td width="40%">
							${ghfn:dictCode2Val('CodeTeachingType',exameeInfo.XXXSDM) }</td>
						<td width="20%">学制：</td>
						<td width="20%"><fmt:formatNumber value="${exameeInfo.XZ }"
								pattern="##.#" /></td>
					</tr>
				</table>
			</div>
			</form>
		</div>
	</div>
</body>
</html>