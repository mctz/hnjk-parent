<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/edu3/learning/bbssection/bbs-header.jsp"%>

<DIV id=topLayout>
	<!--导航-->
	<DIV class=notice>
		<DIV
			style="PADDING-LEFT: 10px; FLOAT: left; WIDTH: auto; TEXT-ALIGN: left"
			class="STYLE1">
			<A
				href="${baseUrl }/edu3/${course.isQualityResource eq 'Y' ? 'course':'learning' }/bbs/index.html${querys}"
				class="STYLE1"> <c:choose>
					<c:when test="${empty course}">广东学苑在线</c:when>
					<c:otherwise>${course.courseName }课程论坛</c:otherwise>
				</c:choose>
			</A> → 论坛搜索
		</DIV>
	</DIV>
</DIV>

<table cellspacing="1" cellpadding="3" align="center"
	class="tableborder1">
	<tbody>
		<tr>
			<th height="24" colspan="2">论坛搜索</th>
		</tr>
		<tr>
			<td valign="middle" align="left" colspan="2" class="tablebody1">
				<form id="searchform1" method="get"
					action="${baseUrl }/edu3/${course.isQualityResource eq 'Y' ? 'course':'learning' }/bbs/search.html"
					onsubmit="if($.trim(this.keyword.value)==''){alert('关键字不能为空！');return false;}return true;">
					<table cellspacing="0" cellpadding="0" align="center"
						style="width: 100%;">
						<tbody>
							<tr>
								<td width="20%" style="text-align: right;" valign="top">关键字：</td>
								<td valign="middle" colspan="2" class="tablebody1"><input
									type="text" name="keyword" size="40" /> <input type="hidden"
									name="courseId" value="${course.resourceid }" /></td>
							</tr>
							<tr>
								<td width="20%" style="text-align: right;" valign="top">搜索方式：</td>
								<td colspan="2" class="tablebody1"><input type="radio"
									checked="checked" value="title" name="sType">
									&nbsp;主题&nbsp;&nbsp; <input type="radio" value="fillinMan"
									name="sType"> &nbsp;作者&nbsp;&nbsp; <input type="radio"
									value="content" name="sType"> &nbsp;内容&nbsp;&nbsp; <input
									type="radio" value="titlecontent" name="sType">
									&nbsp;全文检索&nbsp;&nbsp;</td>
							</tr>
						</tbody>
					</table>
					<table cellspacing="0" cellpadding="0" align="center"
						style="width: 100%;">
						<tbody>
							<tr>
								<td width="20%"></td>
								<td colspan="2" class="tablebody1"><input type="submit"
									value="搜索" /> &nbsp;&nbsp;&nbsp;&nbsp;<input type="button"
									value="高级搜索" onclick="showAdvancedSearch();" /></td>
							</tr>
						</tbody>
					</table>
				</form> <script type="text/javascript">
						function showAdvancedSearch(){
							$('#searchform1').hide();
							$("#searchform2 input[name='keyword']").val($("#searchform1 input[name='keyword']").val());
							$('#searchform2').show();							
						}
					</script>
				<form id="searchform2" method="get"
					action="${baseUrl }/edu3/${course.isQualityResource eq 'Y' ? 'course':'learning' }/bbs/search.html"
					style="display: none;"
					onsubmit="if($.trim(this.keyword.value)==''){alert('关键字不能为空！');return false;}return true;">
					<table id="advancedsearch" cellspacing="0" cellpadding="0"
						align="center" style="width: 100%;">
						<tbody>
							<tr>
								<td width="20%" style="text-align: right;" valign="top">关键字：</td>
								<td valign="middle" colspan="2" class="tablebody1"><input
									type="text" name="keyword" size="40" /> <input type="hidden"
									name="courseId" value="${course.resourceid }" /></td>
							</tr>
							<tr>
								<td width="20%" style="text-align: right;" valign="top">搜索方式：</td>
								<td colspan="2" class="tablebody1"><input type="radio"
									checked="checked" value="title" name="sType">
									&nbsp;主题&nbsp;&nbsp; <input type="radio" value="fillinMan"
									name="sType"> &nbsp;作者&nbsp;&nbsp; <input type="radio"
									value="content" name="sType"> &nbsp;内容&nbsp;&nbsp; <input
									type="radio" value="titlecontent" name="sType">
									&nbsp;全文检索&nbsp;&nbsp;</td>
							</tr>
							<tr>
								<td width="20%" style="text-align: right;" valign="top">主题范围：</td>
								<td colspan="2" class="tablebody1"><label> <input
										type="radio" checked="checked" value="2" name="type">
										全部主题
								</label> <label> <input type="radio" value="1" name="type">
										精华主题
								</label> <label> <input type="radio" value="3" name="type">
										置顶主题
								</label></td>
							</tr>
							<tr>
								<td width="20%" style="text-align: right;" valign="top">搜索时间：</td>
								<td colspan="2" class="tablebody1">从<input type="text"
									name="start" class="Wdate"
									onFocus="WdatePicker({isShowWeek:true})" /> 到
									&nbsp;&nbsp;&nbsp;<input type="text" name="end" class="Wdate"
									onFocus="WdatePicker({isShowWeek:true})" /> <font>(默认到当前时间)</font>
								</td>
							</tr>
							<tr>
								<td width="20%" style="text-align: right;" valign="top">排序类型：</td>
								<td colspan="2" class="tablebody1"><select name="orderBy">
										<option selected="selected" value="lastReplyDate">
											回复时间</option>
										<option value="fillinDate">发布时间</option>
										<option value="replyCount">回复数量</option>
										<option value="clickCount">浏览次数</option>
								</select> <label> <input type="radio" value="asc" name="order">
										按升序排列
								</label> <label> <input type="radio" checked="checked"
										value="desc" name="order"> 按降序排列
								</label></td>
							</tr>
							<tr>
								<td width="20%" style="text-align: right;" valign="top">搜索范围：</td>
								<td colspan="2" class="tablebody1"><gh:bbsSectionSelect
										id="bbs_bbsSectionId" name="sectionId" size="10"
										style="width: 40%;" value="" multiple="multiple"
										isCourseSection="${(not empty course.resourceid)?'Y':'N' }" />
									<%-- 
										<select name="sectionId" multiple="multiple" size="10" style="width: 40%;">
											<option value="" selected="selected">
												选取所有版面
											</option>											
											<c:forEach items="${parentBbsSections}" var="item">
												<c:forEach items="${item.value}" var="section" varStatus="vs">
													<c:choose>
														<c:when test="${vs.first }">
															<option value="${section.resourceid }">
															╋${section.sectionName }
															</option>
														</c:when>
														<c:otherwise>
															<option value="${section.resourceid }">
																<c:forEach begin="1" end="${section.sectionLevel }" step="1">&nbsp;&nbsp;</c:forEach>
																├${section.sectionName }
															</option>
														</c:otherwise>
													</c:choose>
												</c:forEach>
											</c:forEach>		
										</select>
										 --%></td>
							</tr>
							<tr>
								<td width="20%"></td>
								<td colspan="2" class="tablebody1"><input type="submit"
									value="站内搜索" /></td>
							</tr>
						</tbody>
					</table>
				</form>
			</td>
		</tr>
	</tbody>
</table>

<BR>
<%@ include file="/WEB-INF/jsp/edu3/learning/bbssection/bbs-footer.jsp"%>