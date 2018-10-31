<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/common.jsp" %>
<head>
<style type="text/css">
.feeHeadHide th .gridCol {height:0px;}
#stufee_stat_pageContent2 th {text-align: center;vertical-align: middle;font-weight: bold;}
</style>
<script type="text/javascript">
	function changeStatTypeText(objA){
		$("#timePeriodView_currentIndex2").val($(objA).attr("rel"));
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
</script>
</head>
<body>	
	<div class="tabs" currentIndex=0>
		<div class="tabsHeader" id="teacherPageContent">
			<div class="tabsHeaderContent">
				<ul>
					<input type="hidden" id="timePeriodView_currentIndex2" name="currentIndex" value="${condition['currentIndex'] }"/>
					<c:forEach items="${timeList }" var="mapList" varStatus="vss">
						<li><a href="#" onclick="changeStatTypeText(this);" rel="${vss.index}">
						<span>
							<c:choose>
					        	<c:when test="${vss.index==0 }">可选老师</c:when>
					        	<c:when test="${vss.index==1 }">可选上课时间</c:when>
					        	<c:otherwise>可选课室</c:otherwise>
					        </c:choose>
						</span>
						</a></li>
					</c:forEach>
				</ul>
			</div>
		</div>
		<div class="tabsContent" style="height:100%;">				
			<c:forEach items="${timeList }" var="mapList">
				<table class="_self_table" layoutH="108">
					<thead>
					   <tr style="height: 35px">
						   <td style="text-align: center;vertical-align:middle;font-weight: bold;width: 18%">上课时间\星期</td>
						   <td style="text-align: center;font-weight: bold;width: 11%">星期一</td>
						   <td style="text-align: center;font-weight: bold;width: 11%">星期二</td>
						   <td style="text-align: center;font-weight: bold;width: 11%">星期三</td>
						   <td style="text-align: center;font-weight: bold;width: 11%">星期四</td>
						   <td style="text-align: center;font-weight: bold;width: 11%">星期五</td>
						   <td style="text-align: center;font-weight: bold;width: 11%">星期六</td>
						   <td style="text-align: center;font-weight: bold;width: 11%">星期日</td>
					   </tr>
				   	</thead>
				   	<tbody>
			     	<c:forEach items="${timeperiodList}" var="tp" varStatus="vs">
				        <tr>
				            <td style="text-align:center;vertical-align:middle;height:80px;width:18%">${tp.courseTimeName }</td>
				            <c:forEach items="${mapList}" var="timeMap">
				            	<td style="text-align:center;vertical-align:middle;" title="
				            		<c:forEach items="${timeMap}" var="map">
				            			<c:if test="${map.key eq tp.resourceid }">${map.value }">${map.value }</c:if>
				            		</c:forEach>
				           		</td>
				            </c:forEach>
				        </tr>
			       	</c:forEach>
			    	</tbody>
				</table>
			</c:forEach>
		</div>
	</div>
</body>