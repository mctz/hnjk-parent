<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>缴费情况统计</title>
<style type="text/css">
.feeHeadHide th .gridCol {
	height: 0px;
}

#stufee_stat_pageContent2 th {
	text-align: center;
	vertical-align: middle;
	font-weight: bold;
}
</style>
<script type="text/javascript">
	function changeStatTypeText(objA){
		$("#studentfee_currentIndex2").val($(objA).attr("rel"));
	}
	(function($){
		$.fn.extend({_self_table:function(options){
			 return this.each(function(){
			 	var table = this;
			 	var tlength = $(table).width();
				var tparent = $(table).parent();
				var layoutH = $(this).attr("layoutH");
				var tableId = $(this).attr("id");
				var tWidths = [];
				var flength = tparent.innerWidth();			
				$(this).wrap("<div class='grid'></div>");
				var parent = $(table).parent();			
				parent.html($(table).html());
				var thead = parent.find("thead");
				thead.wrap("<div class='gridHeader'><div class='gridThead'><table id='"+tableId+"' style='width:" + (flength - 34) + "px;'></table></div></div>");			
				var lastH = $(">tr:last-child", thead);
				$(">th",lastH).each(function(){
					tWidths.push($(this).width());
				});	
				var allH = $(">tr", thead);
				$(">th",allH).each(function(){
					$(this).hoverClass("hover").html("<div class='gridCol'>"+ $(this).html() +"</div>");
				});					
				var tbody = parent.find(">tbody"); 
				tbody.wrap("<div class='gridScroller' layoutH='" + layoutH + "' style='width:" + flength + "px;'><div class='gridTbody'><table id='"+tableId+"' style='width:" + (flength - 34) + "px;'></table></div></div>");
				$("tr", tbody).each(function(){
					var $ftds = $(">td", this);
					var i = 0;
					for (var l = $ftds.size(); i < l; i++) {						
						var $ftd = $($ftds[i]);
						$ftd.html("<div>" + $ftd.html() + "</div>").width(tWidths[i]);							
					}					
					$(this).hoverClass().click(function(){
						$("tr",tbody).filter(".selected").removeClass("selected");
						$(this).addClass("selected");
					});
				});		
				setTimeout(function(){
					var scroller = $(".gridScroller", parent);
					scroller.scroll(function(event){
						var header = $(".gridThead", parent);
						if(scroller.scrollLeft() > 0){
							header.css("position", "relative");
							var scroll = scroller.scrollLeft();
							header.css("left", scroller.cssv("left") - scroll);
						}
						if(scroller.scrollLeft() == 0) {
							header.css("position", "relative");
							header.css("left", "0px");
						}
				        return false;
					});		
				},1);
				setInterval(function(){
					var flength = $(parent).parent().width();
					var brwidth = parseInt($(parent).parent().css("border-right-width"));
					if(isNaN(brwidth)) brwidth = 0;
					var blwidth = parseInt($(parent).parent().css("border-left-width"));
					if(isNaN(blwidth)) blwidth = 0;
					$(parent).width(flength - (brwidth + blwidth));
					$(".gridScroller", parent).width(flength - (brwidth + blwidth));
				}, 1000);
			 });
		}
		});
	})(jQuery);
	$(function (){	
		$("table._self_table")._self_table();
		_w_table_rowspan_subType(".stufee_merge_teachingType");
		_w_table_rowspan_subType(".stufee_merge_major");
		_w_table_rowspan_subType(".stufee_merge_classic");
		_w_table_rowspan_subType(".stufee_merge0");
		_w_table_rowspan_subType(".stufee_merge1");
		_w_table_rowspan_subType(".stufee_merge2");
		_w_table_rowspan_subType(".stufee_merge3");
	});
	//合并行
	function _w_table_rowspan_subType(_w_table_td_cls){   
		var _w_table_firsttd = "";   
	    var _w_table_currenttd = "";   
	    var _w_table_SpanNum = 0;   
	    $(_w_table_td_cls).each(function(i){   
	    	if(i==0){   
	            _w_table_firsttd = $(this);   
	            _w_table_SpanNum = 1;   
	        }else{   
	            _w_table_currenttd = $(this);   
	            if(_w_table_firsttd.html()==_w_table_currenttd.html()){   
	                _w_table_SpanNum++;   
	                _w_table_currenttd.remove();   
	                _w_table_firsttd.attr("rowSpan",_w_table_SpanNum).css("vertical-align", "middle");   
	            }else{   
	                _w_table_firsttd = $(this);   
	                _w_table_SpanNum = 1;   
	            }   
	        }   
	    });    
	} 
	//导出
	function exportStudentPaymentStat(){				
		var url = "${baseUrl}/edu3/finance/studentpayment/stat/export.html?"+$("#studnetfeeExportISearchForm2").serialize()+"&flag=export";
		downloadFileByIframe(url,"studnetpaymentstatExportIframe");
	}
</script>
</head>
<body>
	<div class="page">
		<div class="pageHeader">
			<form id="studnetfeeExportISearchForm2"
				onsubmit="return navTabSearch(this);"
				action="${baseUrl }/edu3/finance/studentpayment/stat/list.html"
				method="post">
				<input type="hidden" id="studentfee_currentIndex2"
					name="currentIndex" value="${condition['currentIndex'] }" />
				<div class="searchBar">
					<ul class="searchContent">
						<li><label>年度：</label> <gh:selectModel id="stat_yearInfoId"
								name="yearInfoId" bindValue="resourceid" displayValue="yearName"
								modelClass="com.hnjk.edu.basedata.model.YearInfo"
								value="${condition['yearInfoId']}" orderBy="yearName desc"
								style="width:125px" /></li>
						<c:if test="${!isBrschool }">
							<li style="width: 400px;"><label>教学点：</label> <gh:selectModel name="branchSchool"
									bindValue="resourceid" displayValue="unitName"
									modelClass="com.hnjk.security.model.OrgUnit"
									value="${condition['branchSchool']}" style="width:300px" /></li>
						</c:if>
						<c:if test="${isBrschool}">
							<input type="hidden" name="branchSchool"
								id="schoolCalendar_branchSchool"
								value="${condition['branchSchool']}" />
						</c:if>
						<%-- <li>
					<label>自然年度：</label>
					<select id="studentfee_year" name="year" style="width: 120px;">
					<option value="0">全部</option>
					<c:forEach items="${yearList }" var="cyear">
					<option value="${cyear }" <c:if test="${condition['year'] eq cyear }">selected="selected"</c:if>>${cyear }</option>
					</c:forEach>
					</select>
				</li> --%>
					</ul>
					<div class="subBar">
						<ul>
							<li><div class="buttonActive">
									<div class="buttonContent">
										<button type="submit">查 询</button>
									</div>
								</div></li>
						</ul>
					</div>
				</div>
			</form>
		</div>
		<div class="pageContent" id="stufee_stat_pageContent2">
			<gh:resAuth parentCode="RES_FINANCE_STUDENTPAYMENT_STAT"
				pageType="list" />
			<div class="tabs"
				<c:if test="${not empty condition['currentIndex'] }">currentIndex="${condition['currentIndex'] }"</c:if>>
				<div class="tabsHeader">
					<div class="tabsHeaderContent">
						<ul>
							<c:forEach items="${statTypes }" var="statType" varStatus="vss">
								<li><a href="#" onclick="changeStatTypeText(this);"
									rel="${vss.index}"> <span> <c:choose>
												<c:when test="${statType eq 'teachingType' }">办学模式</c:when>
												<c:when test="${statType eq 'major' }">专业</c:when>
												<c:when test="${statType eq 'classic' }">层次</c:when>
												<c:otherwise>学习中心</c:otherwise>
											</c:choose>
									</span>
								</a></li>
							</c:forEach>
						</ul>
					</div>
				</div>
				<div class="tabsContent" style="height: 100%;">
					<c:forEach items="${statTypes }" var="statType">
						<div style="width: 100%">
							<table class="_self_table" layoutH="168">
								<thead>
									<tr>
										<th width="7%" rowspan="2">自然年度</th>
										<th width="14%" rowspan="2"
											<c:if test="${statType eq 'teachingType' }">colspan="1"</c:if>>
											<c:choose>
												<c:when test="${statType eq 'teachingType' }">办学模式</c:when>
												<c:when test="${statType eq 'major' }">专业</c:when>
												<c:when test="${statType eq 'classic' }">层次</c:when>
												<c:otherwise>学习中心</c:otherwise>
											</c:choose>
										</th>
										<th width="15%" colspan="2">应缴费用</th>
										<th width="15%" colspan="2">完费</th>
										<th width="14%" colspan="2">减免费用</th>
										<th width="14%" colspan="2">部分费用</th>
										<th width="14%" colspan="2">欠费</th>
										<th width="7%" rowspan="2">完费率</th>
									</tr>
									<tr>
										<th width="7%">人数</th>
										<th width="8%">金额</th>
										<th width="7%">人数</th>
										<th width="8%">金额</th>
										<th width="7%">人数</th>
										<th width="7%">金额</th>
										<th width="7%">人数</th>
										<th width="7%">金额</th>
										<th width="7%">人数</th>
										<th width="7%">金额</th>
									</tr>
									<tr class="feeHeadHide">
										<th width="7%"></th>
										<%--  <c:choose>
					            	<c:when test="${statType eq 'teachingType' }">
					            	<th width="6%"> </th>
						        	<th width="8%"> </th>
					            	</c:when>
					            	<c:otherwise><th width="14%"> </th></c:otherwise>
					            </c:choose>		 --%>
										<th width="14%"></th>
										<th width="7%"></th>
										<th width="8%"></th>
										<th width="7%"></th>
										<th width="8%"></th>
										<th width="7%"></th>
										<th width="7%"></th>
										<th width="7%"></th>
										<th width="7%"></th>
										<th width="7%"></th>
										<th width="7%"></th>
										<th width="7%"></th>
									</tr>
								</thead>
								<tbody>
									<c:forEach items="${statListMap[statType] }" var="stat"
										varStatus="vs">
										<tr>
											<td
												<c:if test="${not vs.last }">class="stufee_merge_${statType }"</c:if>
												style="text-align: center;" title="${stat.firstyear }">${stat.firstyear }</td>
											<c:choose>
												<c:when test="${statType eq 'teachingType' }">

													<c:if test="${not vs.last }">
														<td class="stufee_merge0">${ghfn:dictCode2Val('CodeTeachingType',stat.statType) }</td>
													</c:if>

													<c:if test="${vs.last }">
														<td class="stufee_merge0">${stat.statType }</td>
													</c:if>

												</c:when>
												<c:otherwise>
													<td title="${stat.statType }">${stat.statType }</td>
												</c:otherwise>
											</c:choose>

											<td class="right" title="${stat.allfeeCount }">${stat.allfeeCount }&nbsp;</td>
											<td class="right" title="${stat.recpayfee }"><fmt:formatNumber
													value="${stat.recpayfee }" pattern="#,#00" />&nbsp;</td>
											<td class="right" title="${stat.fullfeeCount }">${stat.fullfeeCount }&nbsp;</td>
											<td class="right" title="${stat.fullfeeSum }"><fmt:formatNumber
													value="${stat.fullfeeSum }" pattern="#,#00" />&nbsp;</td>
											<td class="right" title="${stat.deratefeeCount }">${stat.deratefeeCount }&nbsp;</td>
											<td class="right" title="${stat.deratefeeSum }">${stat.deratefeeSum }&nbsp;</td>
											<td class="right" title="${stat.partfeeCount }">${stat.partfeeCount }&nbsp;</td>
											<td class="right" title="${stat.partfeeSum }">${stat.partfeeSum }&nbsp;</td>
											<td class="right" title="${stat.owefeeCount }">${stat.owefeeCount }&nbsp;</td>
											<td class="right" title="${stat.owefeeSum }"><fmt:formatNumber
													value="${stat.owefeeSum }" pattern="#,#00" />&nbsp;</td>
											<td class="right"
												<c:if test="${stat.fullfeePer ge 1.0 }">style="color: blue;"</c:if>><fmt:formatNumber
													type="percent" pattern="##0.0%" value="${stat.fullfeePer }" />
											</td>
										</tr>
									</c:forEach>
								</tbody>
							</table>
						</div>
					</c:forEach>
				</div>
				<div class="tabsFooter">
					<div class="tabsFooterContent"></div>
				</div>
			</div>
		</div>
	</div>
</body>
</html>