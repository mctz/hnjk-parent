/**
 * 框架核心
 * @type 
 */
var DWZ = {
	// sbar: show sidebar
	keyCode: {ENTER: 13,ESC: 27,END: 35,HOME: 36,SHIFT: 16,TAB: 9,LEFT: 37,	RIGHT: 39,	UP: 38,	DOWN: 40,DELETE: 46, BACKSPACE:8},
	isOverAxis: function(x, reference, size) {
		//Determines when x coordinate is over "b" element axis
		return (x > reference) && (x < (reference + size));
	},
	isOver: function(y, x, top, left, height, width) {
		//Determines when x, y coordinates is over "b" element
		return this.isOverAxis(y, top, height) && this.isOverAxis(x, left, width);
	},
	ui:{sbar:true},
	frag:{}, //page fragment
	msg:{}, //alert message
	loginUrl:"", //session timeout
	jsonEval:function (data) {
		try{
			if ($.type(data) == 'string')
				return eval('(' + data + ')');
			else return data;
		} catch (e){
			return {};
		}
	},
	ajaxError:function (xhr, ajaxOptions, thrownError){
		alertMsg.error("抱歉，系统出错了！<br/>错误代码："+xhr.status+" - "+ajaxOptions+"<br/>请联系系统管理员。");
		//alert("Http status: "+xhr.status + " \najaxOptions: "+ ajaxOptions + " \nthrownError:"+thrownError);
	},
	ajaxDone:function (json){
		if(json.statusCode == 300) {
			if(json.message && alertMsg) alertMsg.error(json.message);
		} else if (json.statusCode == 301) {
			alertMsg.error(json.message, {okCall:function(){
				window.location = DWZ.loginUrl;
			}});
		} else {
			if(json.message && alertMsg) alertMsg.correct(json.message);
		};
	},
	init: function(pageFrag, options){
		var op = $.extend({loginUrl:"login.html", callback:null}, options);
		this.loginUrl = op.loginUrl;

		$.ajax({
			type:'GET',
			url:pageFrag,
			dataType:'xml',
			timeout: 50000,
			cache: false,
			error: function(){alert('Error loading XML document: ' + pageFrag);}, 
			success: function(xml){
				$(xml).find("_PAGE_").each(function(){
					var pageId = $(this).attr("id");
					if (pageId) DWZ.frag[pageId] = $(this).text();
				});
				
				$(xml).find("_MSG_").each(function(){
					var id = $(this).attr("id");
					if (id) DWZ.msg[id] = $(this).text();
				});
				
				if ($.isFunction(op.callback)) op.callback();
			}
		});
	}
};

jQuery.extend(String.prototype, {
	isPositiveInteger:function(){
		return (new RegExp(/^[1-9]\d*$/).test(this));
	},
	isInteger:function(){
		return (new RegExp(/^\d+$/).test(this));
	},
	isNumber: function(value, element) {
		return (new RegExp(/^-?(?:\d+|\d{1,3}(?:,\d{3})+)(?:\.\d+)?$/).test(this));
	},
	trim:function(){
		return this.replace(/(^\s*)|(\s*$)|\r|\n/g, "");
	},
	trans:function() {
		return this.replace(/&lt;/g, '<').replace(/&gt;/g,'>').replace(/&quot;/g, '"');
	},
	replaceAll:function(os, ns) {
		return this.replace(new RegExp(os,"gm"),ns);
	},
	skipChar:function(ch) {
		if (!this || this.length===0) {return '';}
		if (this.charAt(0)===ch) {return this.substring(1).skipChar(ch);}
		return this;
	},
	/**
	 * check if Valid password
	 */
	isValidPwd:function() {
		return (new RegExp(/^([_]|[a-zA-Z0-9]){6,32}$/).test(this)); 
	},
	/**
	 * check if Valid email
	 */
	isValidMail:function(){
		return(new RegExp(/^\w+((-\w+)|(\.\w+))*\@[A-Za-z0-9]+((\.|-)[A-Za-z0-9]+)*\.[A-Za-z0-9]+$/).test(this.trim()));
	},
	isSpaces:function() {
		for(var i=0; i<this.length; i+=1) {
			var ch = this.charAt(i);
			if (ch!=' '&& ch!="\n" && ch!="\t" && ch!="\r") {return false;}
		}
		return true;
	},
	isPhone:function() {
		return (new RegExp(/(^([0-9]{3,4}[-])?\d{3,8}(-\d{1,6})?$)|(^\([0-9]{3,4}\)\d{3,8}(\(\d{1,6}\))?$)|(^\d{3,8}$)/).test(this));
	},
	isURL:function(){
		return (new RegExp(/^(https?|ftp|mms|mailto|file|rtsp|rtp):\/\/(((([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\da-f]{2})|[!\$&'\(\)\*\+,;=]|:)*@)?(((\d|[1-9]\d|1\d\d|2[0-4]\d|25[0-5])\.(\d|[1-9]\d|1\d\d|2[0-4]\d|25[0-5])\.(\d|[1-9]\d|1\d\d|2[0-4]\d|25[0-5])\.(\d|[1-9]\d|1\d\d|2[0-4]\d|25[0-5]))|((([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.)+(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.?)(:\d*)?)(\/((([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\da-f]{2})|[!\$&'\(\)\*\+,;=]|:|@)+(\/(([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\da-f]{2})|[!\$&'\(\)\*\+,;=]|:|@)*)*)?)?(\?((([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\da-f]{2})|[!\$&'\(\)\*\+,;=]|:|@)|[\uE000-\uF8FF]|\/|\?)*)?(\#((([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\da-f]{2})|[!\$&'\(\)\*\+,;=]|:|@)|\/|\?)*)?$/i).test(this)); 
	}
});

/** 
 * You can use this map like this:
 * var myMap = new Map();
 * myMap.put("key","value");
 * var key = myMap.get("key");
 * myMap.remove("key");
 */
function Map(){

	this.elements = new Array();
	
	this.size = function(){
		return this.elements.length;
	}
	
	this.isEmpty = function(){
		return (this.elements.length < 1);
	}
	
	this.clear = function(){
		this.elements = new Array();
	}
	
	this.put = function(_key, _value){
		this.remove(_key);
		this.elements.push({key: _key, value: _value});
	}
	
	this.remove = function(_key){
		try {
			for (i = 0; i < this.elements.length; i++) {
				if (this.elements[i].key == _key) {
					this.elements.splice(i, 1);
					return true;
				}
			}
		} catch (e) {
			return false;
		}
		return false;
	}
	
	this.get = function(_key){
		try {
			for (i = 0; i < this.elements.length; i++) {
				if (this.elements[i].key == _key) { return this.elements[i].value; }
			}
		} catch (e) {
			return null;
		}
	}
	
	this.element = function(_index){
		if (_index < 0 || _index >= this.elements.length) { return null; }
		return this.elements[_index];
	}
	
	this.containsKey = function(_key){
		try {
			for (i = 0; i < this.elements.length; i++) {
				if (this.elements[i].key == _key) {
					return true;
				}
			}
		} catch (e) {
			return false;
		}
		return false;
	}
	
	this.values = function(){
		var arr = new Array();
		for (i = 0; i < this.elements.length; i++) {
			arr.push(this.elements[i].value);
		}
		return arr;
	}
	
	this.keys = function(){
		var arr = new Array();
		for (i = 0; i < this.elements.length; i++) {
			arr.push(this.elements[i].key);
		}
		return arr;
	}
}


(function($){
	$.fn.extend({
		loadUrl: function(url,data,callback){		
			var $this = $(this);
			$.ajax({
				type:'POST',
				url:url,
				cache: false,
				data:data,
				success: function(html){
					var json = DWZ.jsonEval(html);
					if (json.statusCode==301){
						alertMsg.error(DWZ.msg["sessionTimout"], {okCall:function(){
							window.location = "/render.do?method=login";
						}});
					} if (json.statusCode==300){
						if (json.message) alertMsg.error(json.message);
					} else {
						$this.html(html).initUI();
						if ($.isFunction(callback)) callback();
					}
				},
				error: DWZ.ajaxError
			});
		},
		initUI: function(){
			if($.isFunction(initUI)) initUI(this);
		},
		/**
		 * adjust component inner content box height
		 * @param {Object} content: content box jQuery Obj
		 */
		layoutH: function(content){
			var jBox = content || $("#container .tabsPageContent");
			var iTabsContentH = jBox.height();
			
			return this.each(function(){
				var iLayoutH = $(this).attr("layoutH") || 0;
				try {
					if(iLayoutH.indexOf("%")>0){//如果使用百分比
						var hstr = iLayoutH.substring(0,iLayoutH.length -1);
						iLayoutH = iTabsContentH - (iTabsContentH*(parseInt(iLayoutH)/100));
					}else{
						iLayoutH = parseInt(iLayoutH);
					}					
				} catch (e) {
					iLayoutH = 0
				}
				$(this).height(iTabsContentH - iLayoutH > 50 ? iTabsContentH - iLayoutH : 50);
				//alert("容器高度:"+jBox.height()+";layouH:"+iLayoutH+";实际高度:"+$(this).height());
			});
		},
		hoverClass: function(className){
			var _className = className || "hover";
			return this.each(function(){
				$(this).hover(function(){
					$(this).addClass(_className);
				},function(){
					$(this).removeClass(_className);
				});
			});
		},
		focusClass: function(className){
			var _className = className || "textInputFocus";
			return this.each(function(){
				$(this).focus(function(){
					$(this).addClass(_className);
				}).blur(function(){
					$(this).removeClass(_className);
				});
			});
		},
		inputAlert: function(){
			return this.each(function(){
				
				var $this = $(this);
				var altStr = $this.attr("alt");
				var isEmpty = function(){
					return (!$this.val() || $this.val() == altStr);
				}

				if (isEmpty()) $this.val(altStr).addClass("gray");
				$this.focus(function(){
					$this.removeClass("gray")
					if (isEmpty()) $this.val("");
				}).blur(function(){
					if (isEmpty()) $this.val(altStr).addClass("gray");
				});		
			});
		},
		isTag:function(tn) {
			if(!tn) return false;
			return $(this)[0].tagName.toLowerCase() == tn?true:false;
		}
	});	
})(jQuery);

/**
 * 兼容不同的浏览器居中scrollCenter
 **/
(function($){
	$.fn.extend({
		getWindowSize: function(){
			if ($.browser.opera) { return { width: window.innerWidth, height: window.innerHeight }; }
			return { width: $(window).width(), height: $(window).height() };
		},
		/**
		 * @param options
		 */		
		scrollCenter: function(options){
			// 扩展参数
			var op = $.extend({ z: 1000000, mode:"WH"}, options);			
			// 追加到 document.body 并设置其样式
			var windowSize = this.getWindowSize();
			if (op.mode == "W") {
				this.appendTo(document.body).css({
					'left': (windowSize.width - this.width()) / 2 + $(window).scrollLeft() + 'px'
				});
			} else if (op.model == "H"){
				this.appendTo(document.body).css({
					'top': (windowSize.height - this.height()) / 2 + $(window).scrollTop() + 'px'
				});	
			} else {
				this.appendTo(document.body).css({
				//	'position': 'absolute',
				//	'z-index': op.z,
					'top': (windowSize.height - this.height()) / 2 + $(window).scrollTop() + 'px',
					'left': (windowSize.width - this.width()) / 2 + $(window).scrollLeft() + 'px'
				});
			}
  		   // 保存当前位置参数
			var bodyScrollTop = $(document).scrollTop();
			var bodyScrollLeft = $(document).scrollLeft();
			var movedivTop = this.offset().top;
			var movedivLeft = this.offset().left;			
			var thisjQuery = this;			
			// 滚动事件
			$(window).scroll(function(e){
				var windowSize = thisjQuery.getWindowSize();
				var tmpBodyScrollTop = $(document).scrollTop();
				var tmpBodyScrollLeft = $(document).scrollLeft();				
				movedivTop += tmpBodyScrollTop - bodyScrollTop;
				movedivLeft += tmpBodyScrollLeft - bodyScrollLeft;
				bodyScrollTop = tmpBodyScrollTop;
				bodyScrollLeft = tmpBodyScrollLeft;
				// 以动画方式进行移动
				if (op.mode == "W") {
					thisjQuery.stop().animate({
						'left': movedivLeft + 'px'
					});
				} else if (op.mode == "H") {
					thisjQuery.stop().animate({
						'top': movedivTop + 'px'
					});
				} else {
					thisjQuery.stop().animate({
						'top': movedivTop + 'px',
						'left': movedivLeft + 'px'
					});
				}				
			});
			
			// 窗口大小重设事件
			$(window).resize(function(){
				var windowSize = thisjQuery.getWindowSize();
				movedivTop = (windowSize.height - thisjQuery.height()) / 2 + $(document).scrollTop();
				movedivLeft = (windowSize.width - thisjQuery.width()) / 2 + $(document).scrollLeft();				
				if (op.mode == "W") {
					thisjQuery.stop().animate({
						'left': movedivLeft + 'px'
					});
				} else if (op.mode == "H") {
					thisjQuery.stop().animate({
						'top': movedivTop + 'px'
					});
				} else {
					thisjQuery.stop().animate({
						'top': movedivTop + 'px',
						'left': movedivLeft + 'px'
					});
				}
				
			});			
			return this;
		}
	});
})(jQuery);

/**
 * Bar拖动
 */
(function($){
	$.fn.cssv = function(pre){
		var cssPre = $(this).css(pre);
		return cssPre.substring(0, cssPre.indexOf("px")) * 1;
	};
	$.fn.jBar = function(options){
		var op = $.extend({container:"#container", collapse:".collapse", toggleBut:".toggleCollapse div", sideBar:"#sidebar", sideBar2:"#sidebar_s", splitBar:"#splitBar", splitBar2:"#splitBarProxy"}, options);
		return this.each(function(){
			var jbar = this;
			var sbar = $(op.sideBar2, jbar);
			var bar = $(op.sideBar, jbar);
			$(op.toggleBut, bar).click(function(){
				DWZ.ui.sbar = false;
				$(op.splitBar).hide();
				var sbarwidth = sbar.cssv("left") + sbar.outerWidth();
				var barleft = sbarwidth - bar.outerWidth();
				var cleft = $(op.container).cssv("left") - (bar.outerWidth() - sbar.outerWidth());
				var cwidth = bar.outerWidth() - sbar.outerWidth() + $(op.container).outerWidth();
				$(op.container).animate({left: cleft,width: cwidth},50,function(){
					bar.animate({left: barleft}, 500, function(){
						bar.hide();
						sbar.show().css("left", -50).animate({left: 5}, 200);
					});
				});
				$(op.collapse,sbar).click(function(){
					var sbarwidth = sbar.cssv("left") + sbar.outerWidth();
					if(bar.is(":hidden")) {
						$(op.toggleBut, bar).hide();
						bar.show().animate({left: sbarwidth}, 500);
						$(op.container).click(_hideBar);
					} else {
						bar.animate({left: barleft}, 500, function(){
							bar.hide();
						});
					}
					function _hideBar() {
						$(op.container).unbind("click", _hideBar);
						if (!DWZ.ui.sbar) {
							bar.animate({left: barleft}, 500, function(){
								bar.hide();
							});
						}
					}
					return false;
				});
				return false;
			});
			$(op.toggleBut, sbar).click(function(){
				DWZ.ui.sbar = true;
				sbar.animate({left: -25}, 200, function(){				
					bar.show();
				});
				bar.animate({left: 5}, 800, function(){
					$(op.splitBar).show();
					$(op.toggleBut, bar).show();					
					var cleft = 5 + bar.outerWidth() + $(op.splitBar).outerWidth();
					var cwidth = $(op.container).outerWidth() - (cleft - $(op.container).cssv("left"));
					$(op.container).css({left: cleft,width: cwidth});
					$(op.collapse, sbar).unbind('click');

				});
				return false;
			});
			$(op.splitBar).mousedown(function(event){
				$(op.splitBar2).each(function(){
					var spbar2 = $(this);
					setTimeout(function(){spbar2.show();}, 100);
					spbar2.css({visibility: "visible",left: $(op.splitBar).css("left")});					
					spbar2.jDrag($.extend(options, {obj:$("#sidebar"), move:"horizontal", event:event,stop: function(){
						$(this).css("visibility", "hidden");
						var move = $(this).cssv("left") - $(op.splitBar).cssv("left");
						var sbarwidth = bar.outerWidth() + move;
						var cleft = $(op.container).cssv("left") + move;
						var cwidth = $(op.container).outerWidth() - move;
						bar.css("width", sbarwidth);
						$(op.splitBar).css("left", $(this).css("left"));
						$(op.container).css({left: cleft,width: cwidth});

					}}));
					return false;					
				});
			});
		});
	}
})(jQuery);

/**
 * 拖动事件 v1.4.3
 */
(function($){
	$.fn.jDrag = function(options){
		if (typeof options == 'string') {
			if (options == 'destroy') 
				return this.each(function(){
					$(this).unbind('mousedown', $.rwdrag.start);
					$.data(this, 'pp-rwdrag', null);
				});
		}
		return this.each(function(){
			var el = $(this);
			$.data($.rwdrag, 'pp-rwdrag', {
				options: $.extend({
					el: el,
					obj: el
				}, options)
			});
			if (options.event) 
				$.rwdrag.start(options.event);
			else {
				var select = options.selector;
				$(select, obj).bind('mousedown', $.rwdrag.start);
			}
		});
	};
	$.rwdrag = {
		start: function(e){
			var data = $.data(this, 'pp-rwdrag');
			var el = data.options.el[0];
			$.data(el, 'pp-rwdrag', {
				options: data.options
			});
			if (!$.rwdrag.current) {
				$.rwdrag.current = {
					el: el,
					oleft: parseInt(el.style.left) || 0,
					otop: parseInt(el.style.top) || 0,
					ox: e.pageX || e.screenX,
					oy: e.pageY || e.screenY
				};
				$(document).bind("mouseup", $.rwdrag.stop);
				$(document).bind("mousemove", $.rwdrag.drag);
			}
		},
		drag: function(e){
			if (!e)  var e = window.event;
			var current = $.rwdrag.current;
			var data = $.data(current.el, 'pp-rwdrag');
			var left = (current.oleft + (e.pageX || e.clientX) - current.ox);
			var top = (current.otop + (e.pageY || e.clientY) - current.oy);
			if (top < 1) top = 0;
			if (data.options.move == 'horizontal') {
				if ((data.options.minW && left >= $(data.options.obj).cssv("left") + data.options.minW) && (data.options.maxW && left <= $(data.options.obj).cssv("left") + data.options.maxW)) 
					current.el.style.left = left + 'px';
				else if (data.options.scop) {
					if (data.options.relObj) {
						if ((left - parseInt(data.options.relObj.style.left)) > data.options.cellMinW) {
							current.el.style.left = left + 'px';
						}
					} else 
						current.el.style.left = left + 'px';
				}
			} else if (data.options.move == 'vertical') {
					current.el.style.top = top + 'px';
			} else {
				var selector = data.options.selector ? $(data.options.selector, data.options.obj) : $(data.options.obj);
				if (left >= -selector.outerWidth() * 2 / 3 && top >= 0 && (left + selector.outerWidth() / 3 < $(window).width()) && (top + selector.outerHeight() < $(window).height())) {
					current.el.style.left = left + 'px';
					current.el.style.top = top + 'px';
				}
			}
			
			if (data.options.drag) {
				data.options.drag.apply(current.el, [current.el]);
			}
			
			return $.rwdrag.preventEvent(e);
		},
		stop: function(e){
			var current = $.rwdrag.current;
			var data = $.data(current.el, 'pp-rwdrag');
			$(document).unbind('mousemove', $.rwdrag.drag);
			$(document).unbind('mouseup', $.rwdrag.stop);
			if (data.options.stop) {
				data.options.stop.apply(current.el, [current.el]);
			}
			$.rwdrag.current = null;
			return $.rwdrag.preventEvent(e);
		},
		preventEvent:function(e){
			if (e.stopPropagation) e.stopPropagation();
			if (e.preventDefault) e.preventDefault();
			return false;			
		}
	};
})(jQuery);

/**
 * 数据树
 */
 (function($){
 	$.extend($.fn, {
		jTree:function(options) {
			var op = $.extend({checkFn:null, selected:"selected", exp:"expandable", coll:"collapsable", firstExp:"first_expandable", firstColl:"first_collapsable", lastExp:"last_expandable", lastColl:"last_collapsable", folderExp:"folder_expandable", folderColl:"folder_collapsable", endExp:"end_expandable", endColl:"end_collapsable",file:"file"}, options);
			return this.each(function(){
				var $this = $(this);
				var cnum = $this.children().length;
				$(">li", $this).each(function(index){
					var $li = $(this);
					var first = $li.prev()[0]?false:true;
					var last = $li.next()[0]?false:true; 
					$li.genTree({
						icon:$this.hasClass("treeFolder"),
						ckbox:$this.hasClass("treeCheck"),
						options: op,
						level: 0,
						exp:(cnum>1?(first?op.firstExp:(last?op.lastExp:op.exp)):op.endExp),
						coll:(cnum>1?(first?op.firstColl:(last?op.lastColl:op.coll)):op.endColl),
						showSub:(!$this.hasClass("collapse") && ($this.hasClass("expand") || (cnum>1?(first?true:false):true))),
						isLast:(cnum>1?(last?true:false):true)
					});
				});
				if($this.hasClass("treeCheck")){
					var checkFn = eval($this.attr("oncheck"));
					if(checkFn && $.isFunction(checkFn)) {
						$("div.ckbox", $this).each(function(){
							var ckbox = $(this);
							ckbox.click(function(){
								var checked = $(ckbox).hasClass("checked");
								var items = [];
								if(checked){
									var tnode = $(ckbox).parent().parent();
									var boxes = $("input", tnode);
									if(boxes.size() > 1) {
										$(boxes).each(function(){
											items[items.length] = {name:$(this).attr("name"), value:$(this).val()};
										});
									} else {
										items = {name:boxes.attr("name"), value:boxes.val()};
									}		
								}								
								checkFn({checked:checked, items:items});														
							});
						});
					}
				}
				$("a", $this).click(function(event){
					$("div." + op.selected, $this).removeClass(op.selected);
					$(this).parent().addClass(op.selected);
					event.stopPropagation();
					$(document).trigger("click");
					if (!$(this).attr("target")) return false;
				});
			});
		},
		subTree:function(op, level) {
			return this.each(function(){
				$(">li", this).each(function(){
					var $this = $(this);
					$this.data("last", ($this.next()[0]?false:true));
					$this.genTree({
						icon:op.icon,
						ckbox:op.ckbox,
						exp:$this.data("last")?op.options.lastExp:op.options.exp,
						coll:$this.data("last")?op.options.lastColl:op.options.coll,
						options:op.options,
						level:level,
						space:$this.data("last")?null:op.space,
						isLast:$this.data("last")
					});				
				});
			});
		},
		genTree:function(options) {
			var op = $.extend({icon:options.icon,ckbox:options.ckbox,exp:"", coll:"", showSub:false, level:0, options:null, isLast:false}, options);
			return this.each(function(){
				var node = $(this);
				var tree = $(">ul", node);
				if (tree.size()>0) {
					node.children(":first").wrap("<div></div>");
					$(">div", node).prepend("<div class='" + (op.showSub ? op.coll : op.exp) + "'></div>"+(op.ckbox ?"<div class='ckbox unchecked'></div>":"")+(op.icon?"<div class='"+ (op.showSub ? op.options.folderColl : op.options.folderExp) +"'></div>":""));
					op.showSub ? tree.show() : tree.hide();
					$(">div>div:first,>div>a", node).click(function(){
						var $this = $(this);
						var isA = $this.isTag('a');
						var $this = isA?$(">div>div", node).eq(op.level):$this;
						if (!isA || tree.is(":hidden")) {
							$this.toggleClass(op.exp).toggleClass(op.coll);
							if (op.icon) {
								$(">div>div:last", node).toggleClass(op.options.folderExp).toggleClass(op.options.folderColl);
							}
						}
						(tree.is(":hidden"))?tree.slideDown("fast"):(isA?"":tree.slideUp("fast"));
						return false;
					});
					addSpace(op.level, node);
					tree.subTree(op, op.level + 1);
				} else {
					node.children().wrap("<div></div>");			
					$(">div", node).prepend("<div class='node'></div>"+(op.ckbox?"<div class='ckbox unchecked'></div>":"")+(op.icon?"<div class='file'></div>":""));
					addSpace(op.level, node);
					if(op.isLast)$(node).addClass("last");
				}
				if (op.ckbox) node._check(op);
				$(">div",node).mouseover(function(){
					$(this).addClass("hover");
				}).mouseout(function(){
					$(this).removeClass("hover");
				});
				if($.browser.msie)
					$(">div",node).click(function(){
						$("a", this).trigger("click");
						return false;
					});
			});
			function addSpace(level,node) {
				if (level > 0) {					
					var parent = node.parent().parent();
					!parent.next()[0]?$(node).data("space","indent"):$(node).data("space","line");
					var plist = "<div class='" + $(node).data("space") + "'></div>";
					if (level > 1) {
						var next = $(">div>div", parent).filter(":first");
						while(level > 1){
							plist = "<div class='" + next.attr("class") + "'></div>" + plist;
							next = next.next();
							level--;
						}
					}
					$(">div", node).prepend(plist);
				}
			}
		},
		_check:function(op) {
			var node = $(this);
			var ckbox = $(">div>.ckbox", node);
			var $input = $("a", node);
			ckbox.append("<input type='checkbox' name='" + $input.attr("tname") + "' value='" + $input.attr("tvalue") + "' style='display:none;'/>")	
				 .each(function(){
					var $this = $(this);
					$this.click(function(){
						$this.data("checked", $this.hasClass("checked"))
							 .data("aClass",$this.data("checked")?"unchecked":"checked")
						     .data("rClass",$this.data("checked")?"checked":"unchecked")
							 .removeClass($this.data("rClass")).removeClass(!$this.data("checked")?"indeterminate":"").addClass($this.data("aClass"));
						$("input", $this).attr("checked", !$this.data("checked"));
						$(">ul", node).find("li").each(function(){
							var box = $("div.ckbox", this);
							box.removeClass($this.data("rClass")).removeClass(!$this.data("checked")?"indeterminate":"").addClass($this.data("aClass"))
							   .find("input").attr("checked", !$this.data("checked"));
						});
						$(node)._checkParent();
						return false;
					});
			});
		},
		_checkParent:function(){
			if($(this).parent().hasClass("tree")) return;
			var parent = $(this).parent().parent();
			var stree = $(">ul", parent);
			var ckbox = stree.find("div.ckbox").size();
			var ckboxed = stree.find("div.checked").size();
			var aClass = (ckboxed==ckbox?"checked":(ckboxed!=0?"indeterminate":"unchecked"));
			var rClass = (ckboxed==ckbox?"indeterminate":(ckboxed!=0?"checked":"indeterminate"));
			$(">div>.ckbox", parent).removeClass("unchecked").removeClass(rClass).addClass(aClass);
			parent._checkParent();
		}
	});
})(jQuery);

/**
 * 左侧菜单 v1.4.3
 * @type 
 */
(function($) {	
	var jmenus = new Map();
	// If the DWZ scope is not available, add it
	$.dwz = $.dwz || {};

	$(window).resize(function(){
		setTimeout(function(){
			for (var i=0; i<jmenus.size();i++){
				fillSpace(jmenus.element(i).key);
			}
		}, 100);
	});
	$.fn.extend({
		accordion: function(options, data) {

			var args = Array.prototype.slice.call(arguments, 1);

			return this.each(function() {
				if (options.fillSpace) jmenus.put(options.fillSpace, this);
				if (typeof options == "string") {
					var accordion = $.data(this, "dwz-accordion");
					accordion[options].apply(accordion, args);
				// INIT with optional options
				} else if (!$(this).is(".dwz-accordion"))
					$.data(this, "dwz-accordion", new $.dwz.accordion(this, options));
			});
		},
		/**
		 * deprecated, use accordion("activate", index) instead
		 * @param {Object} index
		 */
		activate: function(index) {
			return this.accordion("activate", index);
		}	
	});

	$.dwz.accordion = function(container, options) {
		
		// setup configuration
		this.options = options = $.extend({}, $.dwz.accordion.defaults, options);
		this.element = container;
		
		$(container).addClass("dwz-accordion");
		if ( options.navigation ) {
			var current = $(container).find("a").filter(options.navigationFilter);
			if ( current.length ) {
				if ( current.filter(options.header).length ) {
					options.active = current;
				} else {
					options.active = current.parent().parent().prev();
					current.addClass("current");
				}
			}
		}
		// calculate active if not specified, using the first header
		options.headers = $(container).find(options.header);
		options.active = findActive(options.headers, options.active);
		
		if ( options.fillSpace ) {
			fillSpace(options.fillSpace);		
		} else if ( options.autoheight ) {
			var maxHeight = 0;
			options.headers.next().each(function() {
				maxHeight = Math.max(maxHeight, $(this).outerHeight());
			}).height(maxHeight);
		}
		
		options.headers
			.not(options.active || "")
			.next()
			.hide();		
		options.active.find("h2").addClass(options.selectedClass);
		
		if (options.event)
			$(container).bind((options.event) + ".dwz-accordion", clickHandler);
	};

	$.dwz.accordion.prototype = {
		activate: function(index) {
			// call clickHandler with custom event
			clickHandler.call(this.element, {
				target: findActive( this.options.headers, index )[0]
			});
		},
		
		enable: function() {
			this.options.disabled = false;
		},
		disable: function() {
			this.options.disabled = true;
		},
		destroy: function() {
			this.options.headers.next().css("display", "");
			if ( this.options.fillSpace || this.options.autoheight ) {
				this.options.headers.next().css("height", "");
			}
			$.removeData(this.element, "dwz-accordion");
			$(this.element).removeClass("dwz-accordion").unbind(".dwz-accordion");
		}
	}

	function scopeCallback(callback, scope) {
		return function() {
			return callback.apply(scope, arguments);
		};
	}

	function completed(cancel) {
		// if removed while animated data can be empty
		if (!$.data(this, "dwz-accordion"))
			return;
		var instance = $.data(this, "dwz-accordion");
		var options = instance.options;
		options.running = cancel ? 0 : --options.running;
		if ( options.running )
			return;
		if ( options.clearStyle ) {
			options.toShow.add(options.toHide).css({
				height: "",
				overflow: ""
			});
		}
		$(this).triggerHandler("change.dwz-accordion", [options.data], options.change);
	}

	function fillSpace(key){
		var obj = jmenus.get(key);
		if (!obj) return;
		
		var parent = $(obj).parent();
		var height = parent.height() - (($(".accordionHeader", obj).size()) * ($(".accordionHeader:first-child", obj).outerHeight())) -2;

		var os = parent.children().not(obj);
		$.each(os, function(i){
			height -= $(os[i]).outerHeight();
		});
		$(".accordionContent",obj).height(height);
	}

	function toggle(toShow, toHide, data, clickedActive, down) {
		var options = $.data(this, "dwz-accordion").options;
		options.toShow = toShow;
		options.toHide = toHide;
		options.data = data;
		var complete = scopeCallback(completed, this);
		
		// count elements to animate
		options.running = toHide.size() == 0 ? toShow.size() : toHide.size();
		
		if ( options.animated ) {
			if ( !options.alwaysOpen && clickedActive ) {
				$.dwz.accordion.animations[options.animated]({
					toShow: jQuery([]),
					toHide: toHide,
					complete: complete,
					down: down,
					autoheight: options.autoheight
				});
			} else {
				$.dwz.accordion.animations[options.animated]({
					toShow: toShow,
					toHide: toHide,
					complete: complete,
					down: down,
					autoheight: options.autoheight
				});
			}
		} else {
			if ( !options.alwaysOpen && clickedActive ) {
				toShow.toggle();
			} else {
				toHide.hide();
				toShow.show();
			}
			complete(true);
		}
	}

	function clickHandler(event) {
		var options = $.data(this, "dwz-accordion").options;
		if (options.disabled)
			return false;
		
		// called only when using activate(false) to close all parts programmatically
		if ( !event.target && !options.alwaysOpen ) {
			options.active.find("h2").toggleClass(options.selectedClass);
			var toHide = options.active.next(),
				data = {
					instance: this,
					options: options,
					newHeader: jQuery([]),
					oldHeader: options.active,
					newContent: jQuery([]),
					oldContent: toHide
				},
				toShow = options.active = $([]);
			toggle.call(this, toShow, toHide, data );
			return false;
		}
		// get the click target
		var clicked = $(event.target);
		
		// due to the event delegation model, we have to check if one
		// of the parent elements is our actual header, and find that
		if ( clicked.parents(options.header).length )
			while ( !clicked.is(options.header) )
				clicked = clicked.parent();
		
		var clickedActive = clicked[0] == options.active[0];
		
		// if animations are still active, or the active header is the target, ignore click
		if (options.running || (options.alwaysOpen && clickedActive))
			return false;
		if (!clicked.is(options.header))
			return;

		// switch classes
		options.active.find("h2").toggleClass(options.selectedClass);
		if ( !clickedActive ) {
			clicked.find("h2").addClass(options.selectedClass);
		}

		// find elements to show and hide
		var toShow = clicked.next(),
			toHide = options.active.next(),
			//data = [clicked, options.active, toShow, toHide],
			data = {
				instance: this,
				options: options,
				newHeader: clicked,
				oldHeader: options.active,
				newContent: toShow,
				oldContent: toHide
			},
			down = options.headers.index( options.active[0] ) > options.headers.index( clicked[0] );
		
		options.active = clickedActive ? $([]) : clicked;
		toggle.call(this, toShow, toHide, data, clickedActive, down );

		return false;
	};

	function findActive(headers, selector) {
		return selector != undefined
			? typeof selector == "number"
				? headers.filter(":eq(" + selector + ")")
				: headers.not(headers.not(selector))
			: selector === false
				? $([])
				: headers.filter(":eq(0)");
	}

	$.extend($.dwz.accordion, {
		defaults: {
			selectedClass: "collapsable",
			alwaysOpen: true,
			animated: 'slide',
			event: "click",
			header: ".accordionHeader",
			autoheight: true,
			running: 0,
			navigationFilter: function() {
				return this.href.toLowerCase() == location.href.toLowerCase();
			}
		},
		animations: {
			slide: function(options, additions) {
				options = $.extend({
					easing: "swing",
					duration: 300
				}, options, additions);
				if ( !options.toHide.size() ) {
					options.toShow.animate({height: "show"}, options);
					return;
				}
				var hideHeight = options.toHide.height(),
					showHeight = options.toShow.height(),
					difference = showHeight / hideHeight;
				options.toShow.css({ height: 0}).show();
				options.toHide.filter(":hidden").each(options.complete).end().filter(":visible").animate({height:"hide"},{
					step: function(now) {
						var current = (hideHeight - now) * difference;
						if ($.browser.msie || $.browser.opera) {
							current = Math.ceil(current);
						}
						options.toShow.height( current );
					},
					duration: options.duration,
					easing: options.easing,
					complete: function() {
						if ( !options.autoheight ) {
							options.toShow.css({height:"auto"});
						}
						options.toShow.css({overflow:"auto"});
						options.complete();
					}
				});
			},
			bounceslide: function(options) {
				this.slide(options, {
					easing: options.down ? "bounceout" : "swing",
					duration: options.down ? 1000 : 200
				});
			},
			easeslide: function(options) {
				this.slide(options, {
					easing: "easeinout",
					duration: 700
				})
			}
		}
	});
})(jQuery);

/**
 * 消息提示
 * @type 
 */
var alertMsg = {
	_boxId: "#alertMsgBox",
	_bgId: "#alertBackground",
	_closeTimer: null,

	_types: {error:{type:"error", title:"错误"}, info:{type:"information", title:"提示"}, warn:{type:"warning", title:"警告"}, correct:{type:"correct", title:"成功"}, confirm:{type:"confirm", title:"确认提示"}},
	_butMsg: {ok:"确定", yes:"是", no:"否", cancel:"取消"},
	
	/**
	 * 
	 * @param {Object} type
	 * @param {Object} msg
	 * @param {Object} buttons [button1, button2]
	 */
	_open: function(type, msg, buttons){
		$(this._boxId).remove();
		var butsHtml = "";
		if (buttons) {
			for (var i = 0; i < buttons.length; i++) {
				var sRel = buttons[i].call ? "callback" : "";
				butsHtml += DWZ.frag["alertButFrag"].replace("#butMsg#", buttons[i].name).replace("#callback#", sRel);
			}
		}
		var boxHtml = DWZ.frag["alertBoxFrag"].replace("#type#", type.type).replace("#title#", type.title).replace("#message#", msg).replace("#butFragment#", butsHtml);
		$(boxHtml).appendTo("body").css({top:-$(this._boxId).height()+"px","z-index":"9999"}).animate({top:($(document).height()-200)/2 }, 10);
				
		if (this._closeTimer) {
			clearTimeout(this._closeTimer);
			this._closeTimer = null;
		}
		if (this._types.info == type || this._types.correct == type){
			this._closeTimer = setTimeout(function(){alertMsg.close()}, 3500);
		} else {
			$(this._bgId).show();
		}
		var jCallButs = $(this._boxId).find("[rel=callback]");
		for (var i = 0; i < buttons.length; i++) {
			jCallButs.eq(i).click(buttons[i].call);
		}
	},
	close: function(){
		$(this._boxId).animate({top:-$(this._boxId).height()}, 10, function(){
			$(this).remove();
		});
		$(this._bgId).hide();
	},
	error: function(msg, options) {
		this._alert(this._types.error, msg, options);
	},
	info: function(msg, options) {
		this._alert(this._types.info, msg, options);
	},
	warn: function(msg, options) {
		this._alert(this._types.warn, msg, options);
	},
	correct: function(msg, options) {
		this._alert(this._types.correct, msg, options);
	},
	_alert: function(type, msg, options) {
		var op = {okName:this._butMsg.ok, okCall:null};
		$.extend(op, options);
		var buttons = [
			{name:op.okName, call: op.okCall}
		];
		this._open(type, msg, buttons);
	},
	/**
	 * 
	 * @param {Object} msg
	 * @param {Object} options {okName, okCal, cancelName, cancelCall}
	 */
	confirm: function(msg, options) {
		var op = {okName:this._butMsg.ok, okCall:null, cancelName:this._butMsg.cancel, cancelCall:null};
		$.extend(op, options);
		var buttons = [
			{name:op.okName, call: op.okCall},
			{name:op.cancelName, call: op.cancelCall}
		];
		this._open(this._types.confirm, msg, buttons);
	},
	/**
	 * 扩展消息确认框，ok、cancel、close三个按按钮
	 * @param {Object} msg
	 * @param {Object} options {okName, okCal, cancelName, cancelCall}
	 */
	confirm2: function(msg, options) {
		var op = {okName:this._butMsg.ok, okCall:null, cancelName:this._butMsg.cancel, cancelCall:null,closeName:this._butMsg.cancel,closeCall:null};
		$.extend(op, options);
		var buttons = [
			{name:op.okName, call: op.okCall},
			{name:op.cancelName, call: op.cancelCall},
			{name:op.closeName, call: op.closeCall}
		];
		this._open(this._types.confirm, msg, buttons);
	}
};

/**
 * 右键菜单
 */
(function($){
	var menu, shadow, hash;
	$.fn.extend({
		contextMenu: function(id, options){
			var op = $.extend({
				    shadow : true,
				    bindings:{},
					ctrSub:null
				}, options
			);
			
			if (!menu) {
				menu = $('<div id="contextmenu"></div>').appendTo('body').hide();
			}
			if (!shadow) {
				shadow = $('<div id="contextmenuShadow"></div>').appendTo('body').hide();
			}
			
			hash = hash || [];
			hash.push({
				id : id,
				shadow: op.shadow,
				bindings: op.bindings || {},
				ctrSub: op.ctrSub
			});
			
			var index = hash.length - 1;
			$(this).bind('contextmenu', function(e) {
				display(index, this, e, op);
				return false;
			});
			return this;
		}
	});
	
	function display(index, trigger, e, options) {
		var cur = hash[index];

		var content = $(DWZ.frag[cur.id]);
		content.find('li').hoverClass();
	
		// Send the content to the menu
		menu.html(content);
	
		$.each(cur.bindings, function(id, func) {
			$("[rel='"+id+"']", menu).bind('click', function(e) {
				hide();
				func($(trigger), $("#"+cur.id));
			});
		});
		
		var posX = e.pageX;
		var posY = e.pageY;
		if ($(window).width() < posX + menu.width()) posX -= menu.width();
		if ($(window).height() < posY + menu.height()) posY -= menu.height();

		menu.css({'left':posX,'top':posY}).show();
		if (cur.shadow) shadow.css({width:menu.width(),height:menu.height(),left:posX+3,top:posY+3}).show();
		$(document).one('click', hide);
		
		if ($.isFunction(cur.ctrSub)) {cur.ctrSub($("#"+cur.id));}
	}
	
	function hide() {
		menu.hide();
		shadow.hide();
	}
})(jQuery);

/**
 * 导航Tab
 * @type 
 */
var navTab = {
	componentBox: null, // tab component. contain tabBox, prevBut, nextBut, panelBox
	_tabBox: null,
	_prevBut: null,
	_nextBut: null,
	_panelBox: null,
	_moreBut:null,
	_moreBox:null,
	_currentIndex: 0,
	
	_op: {id:"navTab", stTabBox:".navTab-tab", stPanelBox:".navTab-panel", mainTabId:"main", close$:"a.close", prevClass:"tabsLeft", nextClass:"tabsRight", stMore:".tabsMore", stMoreLi:"ul.tabsMoreList"},
	
	init: function(options){
		if ($.History) $.History.init("#container");
		var $this = this;
		$.extend(this._op, options);

		this.componentBox = $("#"+this._op.id);
		this._tabBox = this.componentBox.find(this._op.stTabBox);
		this._panelBox = this.componentBox.find(this._op.stPanelBox);
		this._prevBut = this.componentBox.find("."+this._op.prevClass);
		this._nextBut = this.componentBox.find("."+this._op.nextClass);
		this._moreBut = this.componentBox.find(this._op.stMore);
		this._moreBox = this.componentBox.find(this._op.stMoreLi);

		this._prevBut.click(function(event) {$this._scrollPrev()});
		this._nextBut.click(function(event) {$this._scrollNext()});
		this._moreBut.click(function(){
			$this._moreBox.show();
			return false;
		});
		$(document).click(function(){$this._moreBox.hide()});
		
		this._contextmenu(this._tabBox);
		
		this._init();
		this._ctrlScrollBut();
	},
	_init: function(){
		var $this = this;		
		this._getTabs().each(function(iTabIndex){
				$(this).unbind("click").click(function(event){
					$this._switchTab(iTabIndex);
				});
				$(this).find(navTab._op.close$).unbind("click").click(function(){
					$this._closeTab(iTabIndex);
				});
			});
		
	
		this._getMoreLi().each(function(iTabIndex){
			$(this).find(">a").unbind("click").click(function(event){
				$this._switchTab(iTabIndex);
			});
		});
		this._switchTab(this._currentIndex);
	},
	_contextmenu:function($obj){ // navTab右键菜单
		var $this = this;
		$obj.contextMenu('navTabCM', {
			bindings:{
				closeCurrent:function(t,m){
					var tabId = t.attr("tabid");
					if (tabId) $this.closeTab(tabId);
					else $this.closeCurrentTab();
				},
				closeOther:function(t,m){
					var index = $this._indexTabId(t.attr("tabid"));
					$this._closeOtherTab(index > 0 ? index : $this._currentIndex);
				},
				closeAll:function(t,m){
					$this.closeAllTab();
				}
			},
			ctrSub:function(m){
				var mCur = m.find("[rel='closeCurrent']");
				var mOther = m.find("[rel='closeOther']");
				var mAll = m.find("[rel='closeAll']");
				var $tabLi = $this._getTabs();
				if ($tabLi.size() < 2) {
					mCur.addClass("disabled");
					mOther.addClass("disabled");
					mAll.addClass("disabled");
				} else if ($this._currentIndex == 0) {
					mCur.addClass("disabled");
				} else if ($tabLi.size() == 2) {
					mOther.addClass("disabled");
				}
			}
		});
	},
	
	_getTabs: function(){
		//alert(this._tabBox);
		return this._tabBox.find("> li");
	},
	_getPanels: function(){
		return this._panelBox.find("> div");
	},
	_getMoreLi: function(){
		return this._moreBox.find("> li");
	},
	_getTab: function(tabid){
		var index = this._indexTabId(tabid);
		return this._getTabs().eq(index);
	},
	_getPanel: function(tabid){
		var index = this._indexTabId(tabid);
		return this._getPanels().eq(index);
	},
	_getTabsW: function(iStart, iEnd){
		return this._tabsW(this._getTabs().slice(iStart, iEnd));
	},
	_tabsW:function(jTabs){
		var iW = 0;
		jTabs.each(function(){
			iW += $(this).outerWidth(true);
		});
		return iW;
	},
	_indexTabId: function(tabid){
		if (!tabid) return -1;
		var iOpenIndex = -1;
		this._getTabs().each(function(index){
			if ($(this).attr("tabid") == tabid){iOpenIndex = index; return;}
		});
		return iOpenIndex;
	},
	_getLeft: function(){
		return this._tabBox.position().left;
	},
	_getScrollBarW: function(){
		return this.componentBox.width()-55;
	},
	
	_visibleStart: function(){
		var iLeft = this._getLeft(), iW = 0;
		var jTabs = this._getTabs();
		for (var i=0; i<jTabs.size(); i++){
			if (iW + iLeft >= 0) return i;
			iW += jTabs.eq(i).outerWidth(true);
		}
		return 0;
	},
	_visibleEnd: function(){
		var iLeft = this._getLeft(), iW = 0;
		var jTabs = this._getTabs();
		for (var i=0; i<jTabs.size(); i++){
			iW += jTabs.eq(i).outerWidth(true);
			if (iW + iLeft > this._getScrollBarW()) return i;
		}
		return jTabs.size();
	},
	_scrollPrev: function(){
		var iStart = this._visibleStart();
		if (iStart > 0){
			this._scrollTab(-this._getTabsW(0, iStart-1));
		}
	},
	_scrollNext: function(){
		var iEnd = this._visibleEnd();
		if (iEnd < this._getTabs().size()){
			this._scrollTab(-this._getTabsW(0, iEnd+1) + this._getScrollBarW());
		}	
	},
	_scrollTab: function(iLeft, isNext){
		var $this = this;
		this._tabBox.animate({ left: iLeft+'px' }, 200, function(){$this._ctrlScrollBut();});
	},
	_scrollCurrent: function(){ // auto scroll current tab
		var iW = this._tabsW(this._getTabs());
		if (iW <= this._getScrollBarW()){
			this._scrollTab(0);
		} else if (this._getLeft() < this._getScrollBarW() - iW){
			this._scrollTab(this._getScrollBarW()-iW);
		} else if (this._currentIndex < this._visibleStart()) {
			this._scrollTab(-this._getTabsW(0, this._currentIndex));
		} else if (this._currentIndex >= this._visibleEnd()) {
			this._scrollTab(this._getScrollBarW() - this._getTabs().eq(this._currentIndex).outerWidth(true) - this._getTabsW(0, this._currentIndex));
		}
	},
	_ctrlScrollBut: function(){
		var iW = this._tabsW(this._getTabs());
		if (this._getScrollBarW() > iW){
			this._prevBut.hide();
			this._nextBut.hide();
			this._tabBox.parent().removeClass("tabsPageHeaderMargin");
		} else {
			this._prevBut.show().removeClass("tabsLeftDisabled");
			this._nextBut.show().removeClass("tabsRightDisabled");
			this._tabBox.parent().addClass("tabsPageHeaderMargin");
			if (this._getLeft() >= 0){
				this._prevBut.addClass("tabsLeftDisabled");
			} else if (this._getLeft() <= this._getScrollBarW() - iW) {
				this._nextBut.addClass("tabsRightDisabled");
			} 
		}
	},
	
	_switchTab: function(iTabIndex){
		var jTabs = this._getTabs();		
		this._currentIndex = iTabIndex;
		jTabs.removeClass("selected").eq(iTabIndex).addClass("selected");
		this._getPanels().hide().eq(iTabIndex).show();
		this._scrollCurrent();
		
		this._getMoreLi().removeClass("selected").eq(iTabIndex).addClass("selected");
		
		this._reload(jTabs.eq(iTabIndex));
				
	},
			
	_closeTab: function(index){
		this._getTabs().eq(index).remove();
		this._getPanels().eq(index).remove();
		this._getMoreLi().eq(index).remove();
		if (this._currentIndex >= index) this._currentIndex--;
		this._init();
		this._scrollCurrent();
		this._reload(this._getTabs().eq(this._currentIndex));
	},
	closeTab: function(tabid){
		var index = this._indexTabId(tabid);
		if (index > 0) { this._closeTab(index); }
	},
	closeCurrentTab: function(){
		if (this._currentIndex > 0) {this._closeTab(this._currentIndex);}
	},
	closeAllTab: function(){
		this._getTabs().filter(":gt(0)").remove();
		this._getPanels().filter(":gt(0)").remove();
		this._getMoreLi().filter(":gt(0)").remove();
		this._currentIndex = 0;
		this._init();
		this._scrollCurrent();
	},
	_closeOtherTab: function(index){
		index = index || this._currentIndex;
		if (index > 0) {
			var str$ = ":eq("+index+")"
			this._getTabs().not(str$).filter(":gt(0)").remove();
			this._getPanels().not(str$).filter(":gt(0)").remove();
			this._getMoreLi().not(str$).filter(":gt(0)").remove();
			this._currentIndex = 1;
			this._init();
			this._scrollCurrent();
		} else {
			this.closeAllTab();
		}
	},

	_reload: function($tab){
		var flag = $tab.data("reloadFlag");
		var url = $tab.data("url");
		if (flag && url) {
			$tab.data("reloadFlag", null);
			$panel = this._getPanel($tab.data("tabid"));
			$panel.loadUrl(url, {}, function(){
				$panel.find("[layoutH]").layoutH();
			});
		}
	},
	reloadFlag: function(tabid){
		this._getTab(tabid).data("reloadFlag", 1);
	},
	reload: function(url, data, tabid){
		var $panel =  tabid ? this._getPanel(tabid) : this._getPanels().eq(this._currentIndex);
		
		if (!url) {
			$tab = tabid ? this._getTab(tabid) : this._getTabs().eq(this._currentIndex);
			url = $tab.data("url");
		}
		if (url) {
			$panel.loadUrl(url, data, function(){
				$panel.find("[layoutH]").layoutH();
			});
		}
	},
	getCurrentPanel: function() {
		return this._getPanels().eq(this._currentIndex);
	},
	openTab: function(tabid, url, title){ //if found tabid replade tab, else create a new tab.
		
		var iOpenIndex = this._indexTabId(tabid);

		if (iOpenIndex >= 0){
			var jTab = this._getTabs().eq(iOpenIndex);
			var stSpan = jTab.attr("tabid") == this._op.mainTabId ? "> a > span > span" : "> a > span";
			jTab.find(stSpan).text(title);
			var jPanel = this._getPanels().eq(iOpenIndex);
			jPanel.loadUrl(url, {}, function(){
				jPanel.find("[layoutH]").layoutH();
			});
			this._currentIndex = iOpenIndex;
		} else {
			var tabFrag = '<li tabid="#tabid#"><a href="javascript:" title="#title#"><span>#title#</span></a><a href="#" class="close">close</a></li>';
			this._tabBox.append(tabFrag.replace("#tabid#", tabid).replaceAll("#title#", title));
			this._panelBox.append('<div></div>');
			this._moreBox.append('<li><a href="javascript:" title="#title#">#title#</a></li>'.replaceAll("#title#", title));
			
			var jTabs = this._getTabs();
			var jPanel = this._getPanels().filter(":last");
			jPanel.loadUrl(url, {}, function(){
				jPanel.find("[layoutH]").layoutH();
			});
			this._currentIndex = jTabs.size() - 1;
			
			this._contextmenu(jTabs.filter(":last").hoverClass("hover"));
			
			if ($.History) {
				$.History.addHistory(tabid, function(tabid){
					var i = navTab._indexTabId(tabid);
					if (i >= 0) navTab._switchTab(i);
				}, tabid);
			}
		}
		
		this._init();
		this._scrollCurrent();
		
		this._getTabs().eq(this._currentIndex).data("url", url).data("tabid", tabid);
	}
};

/**
 * tab选项卡
 */
(function($){
	$.fn.extend({

		/**
		 * options: reverse[true, false], eventType[click, hover], currentIndex[default index 0]
		 * 			stTab[tabs selector], stTabPanel[tab panel selector]
		 * 			ajaxClass[ajax load], closeClass[close tab]
		 */ 
		tabs: function (options){
			var op = $.extend({reverse:false, eventType:"click", currentIndex:0, stTabHeader:"> .tabsHeader", stTab:">.tabsHeaderContent>ul", stTabPanel:"> .tabsContent", ajaxClass:"j-ajax", closeClass:"close", prevClass:"tabsLeft", nextClass:"tabsRight"}, options);
			
			return this.each(function(){
				initTab($(this));
			});
			
			function initTab(jT){
				var jSelector = jT.add($("> *", jT));
				var jTabHeader = $(op.stTabHeader, jSelector);
				var jTabs = $(op.stTab + " li", jTabHeader);
				var jGroups = $(op.stTabPanel + " > *", jSelector);
				var tabBox = $(op.stTab, jTabHeader);
				var prevBut = jTabHeader.find("."+op.prevClass);
				var nextBut = jTabHeader.find("."+op.nextClass);
				
				jTabs.unbind().find("a").unbind();
				
				var jTabOp={_componentBox:jT,_tabHeader:jTabHeader,_tabBox:tabBox,_tabs:jTabs,_prevBut:prevBut,_nextBut:nextBut};
				var $jTabTool = new JTabTool(jTabOp);
				prevBut.unbind().click(function(event) {$jTabTool._scrollPrev()});
				nextBut.unbind().click(function(event) {$jTabTool._scrollNext()});
				$jTabTool._scrollCurrent();
				
				jTabs.each(function(iTabIndex){
					if (op.currentIndex == iTabIndex) $(this).addClass("selected");
					else $(this).removeClass("selected");
					
					if (op.eventType == "hover") $(this).hover(function(event){switchTab(jT, iTabIndex)});
					else $(this).click(function(event){switchTab(jT, iTabIndex)});

					$("a", this).each(function(){
						if ($(this).hasClass(op.ajaxClass)) {
							$(this).click(function(event){
								var jGroup = jGroups.eq(iTabIndex);
								if (this.href) jGroup.loadUrl(this.href,{},function(){
									jGroup.find("[layoutH]").layoutH();
								});
								event.preventDefault();
							});
							if (op.currentIndex == iTabIndex) { $(this).trigger("click"); }
							
						} else if ($(this).hasClass(op.closeClass)) {
							$(this).click(function(event){
								jTabs.eq(iTabIndex).remove();
								jGroups.eq(iTabIndex).remove();
								if (iTabIndex == op.currentIndex) {
									op.currentIndex = (iTabIndex+1 < jTabs.size()) ? iTabIndex : iTabIndex - 1;
								} else if (iTabIndex < op.currentIndex){
									op.currentIndex = iTabIndex;
								}
								initTab(jT);
								return false;
							});
						}
					});
				});

				switchTab(jT, op.currentIndex);
			}
			
			function switchTab(jT, iTabIndex){
				var jSelector = jT.add($("> *", jT));
				var jTabHeader = $(op.stTabHeader, jSelector);
				var jTabs = $(op.stTab + " li", jTabHeader);
				var jGroups = $(op.stTabPanel + " > *", jSelector);
				
				var jTab = jTabs.eq(iTabIndex);
				var jGroup = jGroups.eq(iTabIndex);
				if (op.reverse && (jTab.hasClass("selected") )) {
					jTabs.removeClass("selected");
					jGroups.hide();
				} else {
					op.currentIndex = iTabIndex;
					jTabs.removeClass("selected");
					jTab.addClass("selected");
					
					jGroups.hide().eq(op.currentIndex).show();
				}
				
				if (!jGroup.attr("inited")){
					jGroup.attr("inited", 1000).find("input[type=text]").filter("[alt]").inputAlert();
				}
			}
			
			function JTabTool(jTabOp){
				this.jTabOp = jTabOp || {};
				this._getTabsW = function(iStart, iEnd){
					return this._tabsW(this.jTabOp._tabs.slice(iStart, iEnd));
				};
				this._tabsW = function(jTabs){
					var iW = 0;
					jTabs.each(function(){
						iW += $(this).outerWidth(true);
					});
					return iW;
				};
				this._getLeft = function(){
					return this.jTabOp._tabBox.position().left;
				};
				this._getScrollBarW = function(){
					return this.jTabOp._componentBox.width();
				};		
				this._visibleStart = function(){
					var iLeft = this._getLeft(), iW = 0;
					var jTabs = this.jTabOp._tabs;
					for (var i=0; i<jTabs.size(); i++){
						if (iW + iLeft >= 0) return i;
						iW += jTabs.eq(i).outerWidth(true);
					}
					return 0;
				};
				this._visibleEnd = function(){
					var iLeft = this._getLeft(), iW = 0;
					var jTabs = this.jTabOp._tabs;
					for (var i=0; i<jTabs.size(); i++){
						iW += jTabs.eq(i).outerWidth(true);
						if (iW + iLeft > this._getScrollBarW()) return i;
					}
					return jTabs.size();
				};
				this._scrollPrev = function(){
					var iStart = this._visibleStart();
					if (iStart > 0){
						this._scrollTab(-this._getTabsW(0, iStart-1));
					}
				};
				this._scrollNext = function(){					
					var iEnd = this._visibleEnd();					
					if (iEnd < this.jTabOp._tabs.size()){
						this._scrollTab(-this._getTabsW(0, iEnd+1) + this._getScrollBarW());
					}	
				};
				this._scrollTab = function(iLeft, isNext){
					var $this = this;
					this.jTabOp._tabBox.animate({ left: iLeft+'px' }, 200, function(){$this._ctrlScrollBut();});
				};
				this._ctrlScrollBut = function(){			
					var iW = this._tabsW(this.jTabOp._tabs);
					if (this._getScrollBarW() > iW){						
						this.jTabOp._prevBut.hide();
						this.jTabOp._nextBut.hide();
					} else {
						this.jTabOp._prevBut.show().removeClass("tabsLeftDisabled");
						this.jTabOp._nextBut.show().removeClass("tabsRightDisabled");				
						if (this._getLeft() >= 0){
							this.jTabOp._prevBut.addClass("tabsLeftDisabled");
						} else if (this._getLeft() <= this._getScrollBarW() - iW) {
							this.jTabOp._nextBut.addClass("tabsRightDisabled");
						} 
					}
				};
				this._scrollCurrent = function(){
					var iW = this._tabsW(this.jTabOp._tabs);
					if (iW <= this._getScrollBarW() || this._visibleStart() == 0){
						this._scrollTab(0);
					} else if (this._getLeft() < this._getScrollBarW() - iW){
						this._scrollTab(this._getScrollBarW()-iW);
					} 
				};		
			}			
		}	
		
	});	
})(jQuery);

/**
 * 最大化.最小化窗口
 */
(function($){
 	$.fn.extend({jresize:function(options) {
        if (typeof options == 'string') {
                if (options == 'destroy') 
					return this.each(function() {
							var dialog = this;		
							$("div[class^='resizable']",dialog).each(function() {
								$(this).hide();
							});
	                });
        }
		return this.each(function(){
			var dialog = $(this);			
			var resizable = $(".resizable");
			$("div[class^='resizable']",dialog).each(function() {
				var bar = this;
				$(bar).mousedown(function(event) {
					$.pdialog.switchDialog(dialog);
					$.resizeTool.start(resizable, dialog, event, $(bar).attr("tar"));
					return false;
				}).show();
			});
		});
	}});
	$.resizeTool = {
		start:function(resizable, dialog, e, target) {
			$.pdialog.initResize(resizable, dialog, target);
			$.data(resizable[0], 'layer-drag', {
				options: $.extend($.pdialog._op, {target:target, dialog:dialog,stop:$.resizeTool.stop})
			});
			$.layerdrag.start(resizable[0], e, $.pdialog._op);
		},
		stop:function(){
			var data = $.data(arguments[0], 'layer-drag');
			$.pdialog.resizeDialog(arguments[0], data.options.dialog, data.options.target);
			$("body").css("cursor", "");
			$(arguments[0]).hide();
		}
	};
	$.layerdrag = {  
		start:function(obj, e, options) {
			if (!$.layerdrag.current) {
				$.layerdrag.current = {
					el: obj,
					oleft: parseInt(obj.style.left) || 0,
					owidth: parseInt(obj.style.width) || 0,
					otop: parseInt(obj.style.top) || 0,
					oheight:parseInt(obj.style.height) || 0,
					ox: e.pageX || e.screenX,
					oy: e.pageY || e.clientY
				};
				$(document).bind('mouseup', $.layerdrag.stop);
				$(document).bind('mousemove', $.layerdrag.drag);
			}
			return $.layerdrag.preventEvent(e);
		},
        drag: function(e) {
                if (!e) var e = window.event;
                var current = $.layerdrag.current;
				var data = $.data(current.el, 'layer-drag');
				var lmove = (e.pageX || e.screenX) - current.ox;
				var tmove = (e.pageY || e.clientY) - current.oy;
				if((e.pageY || e.clientY) <= 0 || (e.pageY || e.clientY) >= ($(window).height() - $(".dialogHeader", $(data.options.dialog)).outerHeight())) return false;
				var target = data.options.target;	
				var width = current.owidth;	
				var height = current.oheight;		
				if (target != "n" && target != "s") {
					width += (target.indexOf("w") >= 0)?-lmove:lmove;
				}
				if (width >= 300) {
					if (target.indexOf("w") >= 0) {
						current.el.style.left = (current.oleft + lmove) + 'px';
					}
					if (target != "n" && target != "s") {
						current.el.style.width = width + 'px';
					}
				}
				if (target != "w" && target != "e") {
					height += (target.indexOf("n") >= 0)?-tmove:tmove;
				}
				if (height >= 250) {
					if (target.indexOf("n") >= 0) {
						current.el.style.top = (current.otop + tmove) + 'px';
					}
					if (target != "w" && target != "e") {
						current.el.style.height = height + 'px';
					}
				}
				return $.layerdrag.preventEvent(e);
        },     
        stop: function(e) {
                var current = $.layerdrag.current;
                var data = $.data(current.el, 'layer-drag');
				$(document).unbind('mousemove', $.layerdrag.drag);
				$(document).unbind('mouseup', $.layerdrag.stop);
                if (data.options.stop) {
                        data.options.stop.apply(current.el, [ current.el ]);
                }
                $.layerdrag.current = null;
				return $.layerdrag.preventEvent(e);
        },
		preventEvent:function(e) {
                if (e.stopPropagation) e.stopPropagation();
                if (e.preventDefault) e.preventDefault();
                return false;
		}
	};
})(jQuery);

/**
 * 弹出窗口
 */
(function($){
	$.pdialog = {
		_op:{height:300, width:500, minH:250, minW:300, total:20, max:false, mask:false, resizable:true, drawable:true, maxable:true,minable:true,fresh:true},
		_current:null,
		_zIndex:42,
		getCurrent:function(){
			return this._current;
		},
		reload:function(url, data){
			if (this._current) {
				var dialog = this._current;
				var jDContent = dialog.find(".dialogContent");
				jDContent.width($(dialog).width()-12);
				jDContent.loadUrl(url, data, function(){
					jDContent.find("[layoutH]").layoutH(jDContent);
//					$(".pageContent", dialog).width($(dialog).width()-14);
					$(".pageContent", dialog).width($(dialog).width()-26);
					$("button.close", dialog).click(function(){
						$.pdialog.close(dialog);
						return false;
					});
				});
			}
		},
		//打开一个层
		open:function(url, dlgid, title, options) {
			var op = $.extend({},$.pdialog._op, options);
			var dialog = $("body").data(dlgid);
			//重复打开一个层
			if(dialog) {
				if(dialog.is(":hidden")) {
					dialog.show();
				}
				dialog.find(".dialogHeader").find("h1").html(title);
				this.switchDialog(dialog);
				var jDContent = dialog.find(".dialogContent");
				jDContent.width($(dialog).width()-12);
				jDContent.loadUrl(url, {}, function(){
					jDContent.find("[layoutH]").layoutH(jDContent);
//					$(".pageContent", dialog).width($(dialog).width()-14);
					$(".pageContent", dialog).width($(dialog).width()-26);
					$("button.close").click(function(){
						$.pdialog.close(dialog);
						return false;
					});
				});
			} else {//打开一个全新的层

				$("body").append(DWZ.frag["dialogFrag"]);
				dialog = $(">.dialog:last-child", "body");				
				dialog.data("id",dlgid);
				//解决IE6层遮罩问题
				($.fn.bgiframe && dialog.bgiframe());
				
				dialog.find(".dialogHeader").find("h1").html(title);
				
				$(dialog).css("zIndex", ($.pdialog._zIndex+=2));
				$("div.shadow").css("zIndex", $.pdialog._zIndex - 3).show();
				$.pdialog._init(dialog, options);
				$(dialog).click(function(){
					$.pdialog.switchDialog(dialog);
				});
				if(op.resizable) dialog.jresize();
				if(op.drawable) dialog.dialogDrag();
				$("a.close", dialog).click(function(event){ 
					if(op.close){
						$.pdialog.closeCallBack(dialog,op);
					}else{
						$.pdialog.close(dialog);
					}
					return false;
				});
				if (op.maxable) {
					$("a.maximize", dialog).show().click(function(event){
						$.pdialog.switchDialog(dialog);
						$.pdialog.maxsize(dialog);
						dialog.jresize("destroy").dialogDrag("destroy");
						return false;
					});
				} else {
					$("a.maximize", dialog).hide();
				}
				$("a.restore", dialog).click(function(event){
					$.pdialog.restore(dialog);
					dialog.jresize().dialogDrag();
					return false;
				});
				if (op.minable) {
					$("a.minimize", dialog).show().click(function(event){
						$.pdialog.minimize(dialog);
						return false;
					});
				} else {
					$("a.minimize", dialog).hide();
				}
				$("div.dialogHeader a", dialog).mousedown(function(){
					return false;
				});
				$("div.dialogHeader", dialog).dblclick(function(){
					if($("a.restore",dialog).is(":hidden"))
						$("a.maximize",dialog).trigger("click");
					else
						$("a.restore",dialog).trigger("click");
				});
				if(op.max) {
//					$.pdialog.switchDialog(dialog);
					$.pdialog.maxsize(dialog);
					dialog.jresize("destroy").dialogDrag("destroy");
				}
				$("body").data(dlgid, dialog);
				$.pdialog._current = dialog;
				$.pdialog.attachShadow(dialog);
				//load data
				var jDContent = $(".dialogContent",dialog);
				jDContent.width($(dialog).width()-12);
				jDContent.loadUrl(url, {}, function(){
					jDContent.find("[layoutH]").layoutH(jDContent);
//					$(".pageContent", dialog).width($(dialog).width()-14);
					$(".pageContent", dialog).width($(dialog).width()-26);
					$("button.close").click(function(){
						$.pdialog.close(dialog);
						return false;
					});
				});			    
			}
			if (op.mask) {
				$(dialog).css("zIndex", 1000);
				$("a.minimize",dialog).hide();
				$(dialog).data("mask", true);
				$("#dialogBackground").show();
			}else {
				//add a task to task bar
				if(op.minable) $.taskBar.addDialog(dlgid,title);
			}				
		},
		/**
		 * 切换当前层
		 * @param {Object} dialog
		 */
		switchDialog:function(dialog) {
			var index = $(dialog).css("zIndex");
			$.pdialog.attachShadow(dialog);
			if($.pdialog._current) {
				var cindex = $($.pdialog._current).css("zIndex");
				$($.pdialog._current).css("zIndex", index);
				$(dialog).css("zIndex", cindex);
				$("div.shadow").css("zIndex", cindex - 1);
				$.pdialog._current = dialog;
			}
			$.taskBar.switchTask(dialog.data("id"));
		},
		/**
		 * 给当前层附上阴隐层
		 * @param {Object} dialog
		 */
		attachShadow:function(dialog) {
			var shadow = $("div.shadow");
			if(shadow.is(":hidden")) shadow.show();
			shadow.css({
				top: parseInt($(dialog)[0].style.top) - 2,
				left: parseInt($(dialog)[0].style.left) - 4,
				height: parseInt($(dialog).height()) + 8,
				width: parseInt($(dialog).width()) + 8,
				zIndex:parseInt($(dialog).css("zIndex")) - 1
			});
			$(".shadow_c", shadow).children().andSelf().each(function(){
				$(this).css("height", $(dialog).outerHeight() - 4);
			});
		},
		_init:function(dialog, options) {
			var op = $.extend({}, this._op, options);
			var height = op.height>op.minH?op.height:op.minH;
			var width = op.width>op.minW?op.width:op.minW;
			if(isNaN($(dialog).height()) || $(dialog).height() < height){
				$(dialog).height(height+"px");
				$(".dialogContent",dialog).height(height - $(".dialogHeader", dialog).outerHeight() - $(".dialogFooter", dialog).outerHeight() - 6);
			}
			if(isNaN($(dialog).css("width")) || $(dialog).width() < width) {
				$(dialog).width(width+"px");
			}
			
			var iTop = ($(window).height()-dialog.height())/2;
			dialog.css({
				left: ($(window).width()-dialog.width())/2,
				top: iTop > 0 ? iTop : 0
			});
		},
		/**
		 * 初始化半透明层
		 * @param {Object} resizable
		 * @param {Object} dialog
		 * @param {Object} target
		 */
		initResize:function(resizable, dialog,target) {
			$("body").css("cursor", target + "-resize");
			resizable.css({
				top: $(dialog).css("top"),
				left: $(dialog).css("left"),
				height:$(dialog).css("height"),
				width:$(dialog).css("width")
			});
			resizable.show();
		},
		/**
		 * 改变阴隐层
		 * @param {Object} target
		 * @param {Object} options
		 */
		repaint:function(target,options){
			var shadow = $("div.shadow");
			if(target != "w" && target != "e") {
				shadow.css("height", shadow.outerHeight() + options.tmove);
				$(".shadow_c", shadow).children().andSelf().each(function(){
					$(this).css("height", $(this).outerHeight() + options.tmove);
				});
			}
			if(target == "n" || target =="nw" || target == "ne") {
				shadow.css("top", options.otop - 2);
			}
			if(options.owidth && (target != "n" || target != "s")) {
				shadow.css("width", options.owidth + 8);
			}
			if(target.indexOf("w") >= 0) {
				shadow.css("left", options.oleft - 4);
			}
		},
		/**
		 * 改变左右拖动层的高度
		 * @param {Object} target
		 * @param {Object} tmove
		 * @param {Object} dialog
		 */
		resizeTool:function(target, tmove, dialog) {
			$("div[class^='resizable']", dialog).filter(function(){
				return $(this).attr("tar") == 'w' || $(this).attr("tar") == 'e';
			}).each(function(){
				$(this).css("height", $(this).outerHeight() + tmove);
			});
		},
		/**
		 * 改变原始层的大小
		 * @param {Object} obj
		 * @param {Object} dialog
		 * @param {Object} target
		 */
		resizeDialog:function(obj, dialog, target) {
			var oleft = parseInt(obj.style.left);
			var otop = parseInt(obj.style.top);
			var height = parseInt(obj.style.height);
			var width = parseInt(obj.style.width);
			if(target == "n" || target == "nw") {
				tmove = parseInt($(dialog).css("top")) - otop;
			} else {
				tmove = height - parseInt($(dialog).css("height"));
			}
			$(dialog).css({left:oleft,width:width,top:otop,height:height});
			$(".dialogContent", dialog).css("width", (width-12) + "px");
//			$(".pageContent", dialog).css("width", (width-14) + "px");
			$(".pageContent", dialog).css("width", (width-26) + "px");
			if (target != "w" && target != "e") {
				var content = $(".dialogContent", dialog);
				content.css({height:height - $(".dialogHeader", dialog).outerHeight() - $(".dialogFooter", dialog).outerHeight() - 6});
				content.find("[layoutH]").layoutH(content);
				$.pdialog.resizeTool(target, tmove, dialog);
			}
			$.pdialog.repaint(target, {oleft:oleft,otop: otop,tmove: tmove,owidth:width});
		},
		close:function(dialog) {
			if(typeof dialog == 'string') dialog = $("body").data(dialog);
			$(dialog).unbind("click").hide();
			$("div.dialogContent", dialog).html("");
			$("div.shadow").hide();
			if($(dialog).data("mask")){
				$("#dialogBackground").hide();
			} else{
				$.taskBar.closeDialog($(dialog).data("id"));
			}
		},
		closeCallBack:function(dialog,op) {
			var succes = true;
			console.log($.isFunction(op.close));
			if(op.close && $.isFunction(op.close)){
				
				var callBack= op.close;
				var param = op.param;
				if(param && param != ""){
					param = DWZ.jsonEval(param);
					succes = callBack(param);
				} else {
					succes = callBack();
				}
				if(!succes) return;
			}
			if(typeof dialog == 'string') dialog = $("body").data(dialog);
			$(dialog).unbind("click").hide();
			$("div.dialogContent", dialog).html("");
			$("div.shadow").hide();
			if($(dialog).data("mask")){
				$("#dialogBackground").hide();
			} else{
				$.taskBar.closeDialog($(dialog).data("id"));
			}
		},
		closeCurrent:function(){
			this.close($.pdialog._current);
		},
		maxsize:function(dialog) {
			$(dialog).data("original",{
				top:$(dialog).css("top"),
				left:$(dialog).css("left"),
				width:$(dialog).css("width"),
				height:$(dialog).css("height")
			});
			$("a.maximize",dialog).hide();
			$("a.restore",dialog).show();
//			var iContentW = $(window).width();
			var iContentW = $(window).width()-2;
			var iContentH = $(window).height() - 34;
			$(dialog).css({top:"0px",left:"0px",width:iContentW+"px",height:iContentH+"px"});
			$.pdialog._resizeContent(dialog,iContentW,iContentH);
		},
		restore:function(dialog) {
			var original = $(dialog).data("original");
			var dwidth = parseInt(original.width);
			var dheight = parseInt(original.height);
			$(dialog).css({
				top:original.top,
				left:original.left,
				width:dwidth,
				height:dheight
			});
			$.pdialog._resizeContent(dialog,dwidth,dheight);
			$("a.maximize",dialog).show();
			$("a.restore",dialog).hide();
			$.pdialog.attachShadow(dialog);
		},
		minimize:function(dialog){
			$(dialog).hide();
			$("div.shadow").hide();
			var task = $.taskBar.getTask($(dialog).data("id"));
			$(".resizable").css({
				top: $(dialog).css("top"),
				left: $(dialog).css("left"),
				height:$(dialog).css("height"),
				width:$(dialog).css("width")
			}).show().animate({top:$(window).height()-60,left:task.position().left,width:task.outerWidth(),height:task.outerHeight()},250,function(){
				$(this).hide();
				$.taskBar.inactive($(dialog).data("id"));
			});
		},
		_resizeContent:function(dialog,width,height) {
			var content = $(".dialogContent", dialog);
			content.css({width:(width-12) + "px",height:height - $(".dialogHeader", dialog).outerHeight() - $(".dialogFooter", dialog).outerHeight() - 6});
			content.find("[layoutH]").layoutH(content);
//			$(".pageContent", dialog).css("width", (width-14) + "px");
			$(".pageContent", dialog).css("width", (width-26) + "px");
		}
	};
})(jQuery);

/**
 * 弹出窗口拖动事件
 */
(function($){
	$.fn.dialogDrag = function(options){
        if (typeof options == 'string') {
                if (options == 'destroy') 
					return this.each(function() {
							var dialog = this;		
							$("div.dialogHeader", dialog).unbind("mousedown");
	                });
        }
		return this.each(function(){
			var dialog = $(this);
			$("div.dialogHeader", dialog).mousedown(function(e){
				$.pdialog.switchDialog(dialog);
				dialog.data("task",true);
				setTimeout(function(){
					if(dialog.data("task"))$.dialogDrag.start(dialog,e);
				},100);
				return false;
			}).mouseup(function(e){
				dialog.data("task",false);
				return false;
			});
		});
	};
	$.dialogDrag = {
		currId:null,
		_init:function(dialog) {
			this.currId = new Date().getTime();
			var shadow = $("#dialogProxy");
			if (!shadow.size()) {
				shadow = $(DWZ.frag["dialogProxy"]);
				$("body").append(shadow);
			}
			$("h1", shadow).html($(".dialogHeader h1", dialog).text());
		},
		start:function(dialog,event){
				this._init(dialog);
				var sh = $("#dialogProxy");
				sh.css({
					left: dialog.css("left"),
					top: dialog.css("top"),
					height: dialog.css("height"),
					width: dialog.css("width"),
					zIndex:parseInt(dialog.css("zIndex")) + 1
				}).show();
				$("div.dialogContent",sh).css("height",$("div.dialogContent",dialog).css("height"));
				sh.data("dialog",dialog);
				dialog.css({left:"-10000px",top:"-10000px"});
				$(".shadow").hide();				
				$(sh).jDrag({
					selector:".dialogHeader",
					stop: this.stop,
					event:event
				});
				return false;
		},
		stop:function(){
			var sh = $(arguments[0]);
			var dialog = sh.data("dialog");
			$(dialog).css({left:$(sh).css("left"),top:$(sh).css("top")});
			$.pdialog.attachShadow(dialog);
			$(sh).hide();
		}
	}
})(jQuery);

/**
 * 数据表格
 * @type 
 */
var showFileTimer = null;
try {
	

(function($){
	$.fn.extend({jTable:function(options){
		 return this.each(function(){
		 	var table = this;
		 	var tlength = $(table).width();
			//$.jTableTool.styles = [];
			var tableStyles = [];
			var tparent = $(table).parent();
			var layoutH = $(this).attr("layoutH");
			var tableId = $(this).attr("id");
			var flength = tparent.innerWidth();
			var padleft = parseInt(tparent.css("padding-left"));
			var padright = parseInt(tparent.css("padding-right"));
			var brwidth = parseInt(tparent.css("border-right-width"));
			if(isNaN(brwidth)) brwidth = 0;
			var blwidth = parseInt(tparent.css("border-left-width"));
			if(isNaN(blwidth)) blwidth = 0;
			var extraWidth = parseInt($(this).attr("extraWidth"));//表格加上额外的宽度,表格列数太多，加上横向滚动时用到
			if(!isNaN(extraWidth))	flength += extraWidth;
			var oldThs = $(table).find("thead>tr:last-child").find("th");
			for(var i = 0, l = oldThs.size(); i < l; i++) {
				var $th = $(oldThs[i]);
				var style = [];
//				style[0] = parseInt($th.width() * (flength - 34) / tlength) - 10;
				style[0] = parseInt($th.width() * (flength -24) / tlength) - 10;
				style[1] = $th.attr("align");
				tableStyles[tableStyles.length] = style;
			}
			$(this).wrap("<div class='grid'></div>");
			var parent = $(table).parent();			
			parent.html($(table).html());
			var thead = parent.find("thead");
//			thead.wrap("<div class='gridHeader'><div class='gridThead'><table id='"+tableId+"' style='width:" + (flength - 34) + "px;'></table></div></div>");			
			thead.wrap("<div class='gridHeader'><div class='gridThead'><table id='"+tableId+"' style='width:" + (flength-24) + "px;'></table></div></div>");			
			var firstH = $(">tr:first-child", thead);
			var lastH = $(">tr:last-child", thead);
			var ths = $(">th", lastH);
			$(">th",firstH).each(function(){
				$(this).html("<div class='gridCol'>"+ $(this).html() +"</div>");
			});			
			for(var i = 0, l = ths.size(); i<l; i++){
				var $th = $(ths[i]);
				$th.html("<div class='gridCol' title='"+$th.text()+"'>"+ $th.html() +"</div>");		
			}
			setTimeout(function(){
				for(var i = 0, l = ths.size(); i < l; i++) {
					var $th = $(ths[i]);
					var style = tableStyles[i];
					$th.addClass(style[1])
					   .removeAttr("align")
					   .hoverClass("hover")
					   .removeAttr("width")
					   .width(style[0]);
				}
			},1);
			setTimeout(function(){
				lastH.click(function(e){
					var $th = $(e.target).parent().parent();
					$("th",this).filter(".thSelected").removeClass("thSelected");
					$($th).addClass("thSelected");
					var cellNum = $.jTableTool.getCellNum($th);
					var tbody = $(".gridTbody", parent);
					$("table>tbody>tr", tbody).each(function(){
						var tds = $(">td",this);
						$(tds).filter(".tdSelected").removeClass("tdSelected");
						$(tds).eq(cellNum - 1).addClass("tdSelected");								
					});
				});			
			},1);
			var tbody = parent.find(">tbody"); 
//			tbody.wrap("<div class='gridScroller' layoutH='" + layoutH + "' style='width:" + (flength - (padleft + padright) - (brwidth + blwidth)) + "px;'><div class='gridTbody'><table id='"+tableId+"' style='width:" + (flength - 34) + "px;'></table></div></div>");
			tbody.wrap("<div class='gridScroller' layoutH='" + layoutH + "' style='width:" + (flength - (padleft + padright) - (brwidth + blwidth)) + "px;'><div class='gridTbody'><table id='"+tableId+"' style='width:" + (flength-24) + "px;'></table></div></div>");
			var ftr = $(">tr:first-child", tbody);										
			$("tr", tbody).each(function(){
				var $ftds = $(">td", this);
				var i = 0;
				for (var l = $ftds.size(); i < l; i++) {
					var $ftd = $($ftds[i]);
					$ftd.html("<div>" + $ftd.html() + "</div>");
					if(tableStyles[i]==null)continue;
					$ftd.addClass(tableStyles[i][1]);
				}		
				$(this).hoverClass().click(function(){
					$("tr",tbody).filter(".selected").removeClass("selected");
					$(this).addClass("selected");
					var sTarget = $(this).attr("target");
					if (sTarget)
						$("#"+sTarget).val($(this).attr("rel"));
				});
			});
			$(">td",ftr).each(function(i){
				if(tableStyles && tableStyles[i]){
					$(this).width(tableStyles[i][0]);
				}
			});			
			parent.append("<div class='resizeMarker' style='height:300px; left:57px;display:none;'></div><div class='resizeProxy' style='height:300px; left:377px;display:none;'></div>");
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
			setTimeout(function(){
				$(">tr", thead).each(function(){
					var tr = this;
					var subTitle = $(tr).next();
					$(">th", this).each(function(i){
						var th = this;
						$(th).mouseover(function(event){
							var offset = $.jTableTool.getOffset(th, event).offsetX;
							if($(th).outerWidth() - offset < 5) {
								$(th).css("cursor", "col-resize")
								    .mousedown(function(event){
									$(".resizeProxy", parent).show().css({
										left: $.jTableTool.getRight(th)- $(".gridScroller", parent).scrollLeft(),
										top:$.jTableTool.getTop(th),
										height:$.jTableTool.getHeight(th,parent),
										cursor:"col-resize"
									});
									$(".resizeMarker", parent).show().css({
											left: $.jTableTool.getLeft(th) + 1 - $(".gridScroller", parent).scrollLeft(),
											top: $.jTableTool.getTop(th),
											height:$.jTableTool.getHeight(th,parent)									
									});
									$(".resizeProxy", parent).jDrag($.extend(options, {scop:true, cellMinW:20, relObj:$(".resizeMarker", parent)[0],
											move: "horizontal",
											event:event,
											stop: function(){
												var pleft = $(".resizeProxy", parent).position().left;
												var mleft = $(".resizeMarker", parent).position().left;
												var move = pleft - mleft - $(th).outerWidth() - 9;

												var tbparent = $("table", parent);

												var cols = $.jTableTool.getColspan($(th));
												var cellNum = $.jTableTool.getCellNum($(th));
												var start = $.jTableTool.getStart($(th));
												var totalW = 0;
												var cellW = [];							
												if (subTitle[0]) {
													var $ths = $(">th", subTitle);
													for (var i = start - 1, j = 0; j < cols; j++) {
														var wd = $(">div", $ths.eq(i + j)).outerWidth();
														cellW[cellW.length] = wd;
														totalW += wd;
													}
													for (var i = start - 1, j = 0; j < cols; j++) {
														$ths.eq(i+j).css("width", cellW[j] + parseInt((cellW[j] * move / totalW).toFixed(0)));
													}
												} else {										
													var $div = $(">div", $(th));
													$(th).width($(th).width() + move);
												}																								
												var $lastH = $(">tr:last-child", thead);						
												var tbody = $(".gridTbody", parent);

												var $table = $(">table", tbody);
												$("tr", $table).each(function(index){
													var tds = $(">td", this);
												
													var $dcell = $(tds).eq(cellNum - 1);
													$dcell.css("width", $(">th",$lastH).eq(cellNum - 1).css("width"));
												});
												$(".resizeMarker,.resizeProxy", parent).hide();
											}
										}));
								});
							} else {
								$(th).css("cursor", "default");
								$(th).unbind("mousedown");
							}
							return false;
						});
					});
				});	
			},1);
			setTimeout(function(){//先执行一遍调整宽度,否则滚动条出现的突兀(1秒才后出现)
				var flength = $(parent).parent().width();
				var brwidth = parseInt($(parent).parent().css("border-right-width"));
				if(isNaN(brwidth)) brwidth = 0;
				var blwidth = parseInt($(parent).parent().css("border-left-width"));
				if(isNaN(blwidth)) blwidth = 0;
				$(parent).width(flength - (brwidth + blwidth));
				$(".gridScroller", parent).width(flength - (brwidth + blwidth));
			}, 1);
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
	$.jTableTool = {
		styles:[],
		 getLeft:function(obj) {
			var width = 0;
			$(obj).prevAll().each(function(){
				width += $(this).outerWidth();
			});
			return width - 1;
		},
		getRight:function(obj) {
			var width = 0;
			$(obj).prevAll().andSelf().each(function(){
				width += $(this).outerWidth();
			});
			return width - 1;
		},
		getTop:function(obj) {
			var height = 0;
			$(obj).parent().prevAll().each(function(){
				height += $(this).outerHeight();
			});
			return height;
		},
		getHeight:function(obj, parent) {
			var height = 0;
			var head = $(obj).parent();
			head.nextAll().andSelf().each(function(){
				height += $(this).outerHeight();
			});
			$(".gridTbody", parent).children().each(function(){
				height += $(this).outerHeight();
			});
			return height;
		},
		getCellNum:function(obj) {
			return $(obj).prevAll().andSelf().size();
		},
		getColspan:function(obj) {
			return $(obj).attr("colspan") || 1;
		},
		getStart:function(obj) {
			var start = 1;
			$(obj).prevAll().each(function(){
				start += parseInt($(this).attr("colspan") || 1);
			});
			return start;
		},
		getPageCoord:function(element){
			var coord = {x: 0, y: 0};
			while (element){
			    coord.x += element.offsetLeft;
			    coord.y += element.offsetTop;
			    element = element.offsetParent;
			}
			return coord;
		},
		getOffset:function(obj, evt){
			if($.browser.msie ) {
				var objset = $(obj).offset();
				var evtset = {
					offsetX:evt.pageX || evt.screenX,
					offsetY:evt.pageY || evt.screenY
				};
				var offset ={
			    	offsetX: evtset.offsetX - objset.left,
			    	offsetY: evtset.offsetY - objset.top
				};
				return offset;
			}
			var target = evt.target;
			if (target.offsetLeft == undefined){
			    target = target.parentNode;
			}
			var pageCoord = $.jTableTool.getPageCoord(target);
			var eventCoord ={
			    x: window.pageXOffset + evt.clientX,
			    y: window.pageYOffset + evt.clientY
			};
			var offset ={
			    offsetX: eventCoord.x - pageCoord.x,
			    offsetY: eventCoord.y - pageCoord.y
			};
			return offset;
		}
	}
})(jQuery);
} catch (e) {
	// TODO: handle exception
}

/**
 * 任务栏
 */
(function($){
	$.fn.extend({jTask:function(options){
		return this.each(function(){//定义栏中各个任务的操作
			var task = this;
			var id = $(task).attr("id");
			$(task).click(function(e){
				var dialog = $("body").data(id);
				if ($(task).hasClass("selected")) {
					$("a.minimize", dialog).trigger("click");
				} else {
					if (dialog.is(":hidden")) {
						$(".resizable").css({top: $(window).height()-60,left:$(task).position().left,height:$(task).outerHeight(),width:$(task).outerWidth()
						}).show().animate({top:$(dialog).css("top"),left: $(dialog).css("left"),width:$(dialog).css("width"),height:$(dialog).css("height")},250,function(){
							$(this).hide();
							$(dialog).show();
							$.pdialog.attachShadow(dialog);
						});
						$.taskBar.switchTask(id);
					} else
						$(dialog).trigger("click");
				}
				$.taskBar.scrollCurrent($(this));
				return false;
			});
			$("div.close", task).click(function(e){
				$.pdialog.close(id)
				return false;
			}).hoverClass("closeHover");
			
		});
	}
	});
	$.taskBar = {
		_taskBar:null,
		_taskBox:null,
		_prevBut:null,
		_nextBut:null,
		_op:{id:"taskbar", taskBox:"div.taskbarContent",prevBut:".taskbarLeft",prevDis:"taskbarLeftDisabled", nextBut:".taskbarRight",nextDis:"taskbarRightDisabled", selected:"selected",boxMargin:"taskbarMargin"},
		init:function(options) {
			var $this = this;
			$.extend(this._op, options);
			this._taskBar = $("#" + this._op.id);
			this._taskBox = this._taskBar.find(this._op.taskBox);
			this._taskList = this._taskBox.find(">ul");
			this._prevBut = this._taskBar.find(this._op.prevBut);
			this._nextBut = this._taskBar.find(this._op.nextBut);
			this._prevBut.click(function(e){$this.scrollLeft()});
			this._nextBut.click(function(e){$this.scrollRight()});
			
			// taskBar右键菜单
			this._contextmenu(this._taskBox);
		},

		_contextmenu:function(obj) {
			$(obj).contextMenu('dialogCM', {
				bindings:{
					closeCurrent:function(t,m){
						var obj = t.isTag("li")?t:$.taskBar._getCurrent();
						$("div.close", obj).trigger("click");
					},
					closeOther:function(t,m){
						var selector = t.isTag("li")?("#" +t.attr("id")):".selected";
						var tasks = $.taskBar._taskList.find(">li:not(:"+selector+")");
						tasks.each(function(i){
							$("div.close",tasks[i]).trigger("click");
						});
					},
					closeAll:function(t,m){
						var tasks = $.taskBar._getTasks();
						tasks.each(function(i){
							$("div.close",tasks[i]).trigger("click");
						});
					}
				},
				ctrSub:function(m){
					var mCur = m.find("[rel='closeCurrent']");
					var mOther = m.find("[rel='closeOther']");
					if(!$.taskBar._getCurrent()[0]) {
						mCur.addClass("disabled");
						mOther.addClass("disabled");
					} else {
						if($.taskBar._getTasks().size() == 1)
							mOther.addClass("disabled");
					}
				}
			});
		},
		_scrollCurrent:function(){
			var iW = this._tasksW(this._getTasks());
			if (iW > this._getTaskBarW()) {
				var $this = this;
				var lTask = $(">li:last-child", this._taskList);
				var left = this._getTaskBarW() - lTask.position().left - lTask.outerWidth(true);
				this._taskList.animate({
					left: left + 'px'
				}, 200, function(){
					$this._ctrlScrollBut();
				});
			} else {
				this._ctrlScrollBut();
			}
		},
		_getTaskBarW:function(){
			return this._taskBox.width()- (this._prevBut.is(":hidden")?this._prevBut.width()+2:0) - (this._nextBut.is(":hidden")?this._nextBut.width()+2:0);
		},
		_scrollTask:function(task){
			var $this = this;
			if(task.position().left + this._getLeft()+task.outerWidth() > this._getBarWidth()) {
				var left = this._getTaskBarW()- task.position().left  - task.outerWidth(true) - 2;
				this._taskList.animate({left: left + 'px'}, 200, function(){
					$this._ctrlScrollBut();
				});
			} else if(task.position().left + this._getLeft() < 0) {
				var left = this._getLeft()-(task.position().left + this._getLeft());
				this._taskList.animate({left: left + 'px'}, 200, function(){
					$this._ctrlScrollBut();
				});
			}
		},
		/**
		 * 控制左右移动按钮何时显示与隐藏
		 */
		_ctrlScrollBut:function(){
			var iW = this._tasksW(this._getTasks());
			if (this._getTaskBarW() > iW) {
				this._taskBox.removeClass(this._op.boxMargin);
				this._nextBut.hide();
				this._prevBut.hide();
				if(this._getTasks().eq(0)[0])this._scrollTask(this._getTasks().eq(0));
			} else {
				this._taskBox.addClass(this._op.boxMargin);
				this._nextBut.show().removeClass(this._op.nextDis);
				this._prevBut.show().removeClass(this._op.prevDis);
				if (this._getLeft() >= 0){
					this._prevBut.addClass(this._op.prevDis);
				}
				if (this._getLeft() <= this._getTaskBarW() - iW) {
					this._nextBut.addClass(this._op.nextDis);
				} 
			}
		},
		_getLeft: function(){
			return this._taskList.position().left;
		},
		/**
		 * 取得第一个完全显示在taskbar上的任务
		 */
		_visibleStart: function(){
			var iLeft = this._getLeft();
			var jTasks = this._getTasks();
			for (var i=0; i<jTasks.size(); i++){
				if (jTasks.eq(i).position().left + jTasks.eq(i).outerWidth(true) + iLeft >= 0) return jTasks.eq(i);
			}
			return jTasks.eq(0);
		},
		/**
		 * 取得最后一个完全显示在taskbar上的任务
		 */
		_visibleEnd: function(){
			var iLeft = this._getLeft();
			var jTasks = this._getTasks();
			for (var i=0; i<jTasks.size(); i++){
				if (jTasks.eq(i).position().left + jTasks.eq(i).outerWidth(true) + iLeft > this._getBarWidth()) return jTasks.eq(i);
			}
			return jTasks.eq(jTasks.size()-1);
		},
		/**
		 * 取得所有的任务
		 */
		_getTasks:function(){
			return this._taskList.find(">li");
		},
		/**
		 * 计算所传入的所有任务的宽度和
		 * @param {Object} jTasks
		 */
		_tasksW:function(jTasks){
			var iW = 0;
			jTasks.each(function(){
				iW += $(this).outerWidth(true);
			});
			return iW;
		},
		_getBarWidth: function() {
			return this._taskBar.innerWidth(true);
		},
		/**
		 * 在任务栏上新加一个任务
		 * @param {Object} id
		 * @param {Object} title
		 */
		addDialog: function(id, title){
			this.show();
			var task = $("#"+id,this._taskList);
			if (!task[0]) {
				var taskFrag = '<li id="#taskid#"><div class="taskbutton"><span>#title#</span></div><div class="close">Close</div></li>';
				this._taskList.append(taskFrag.replace("#taskid#", id).replace("#title#", title));
				task = $("#"+id,this._taskList);
				task.jTask();
			} else {
				$(">div>span", task).text(title);
			}
			this._contextmenu(task);
			this.switchTask(id);
			this._scrollTask(task);
		},
		/**
		 * 关闭一个任务
		 * @param {Object} id
		 */
		closeDialog: function(obj){
			var task = (typeof obj == 'string')? $("#"+obj, this._taskList):obj;
			task.remove();
			if(this._getTasks().size() == 0){
				this.hide();
			}			
			this._scrollCurrent();
		},
		/**
		 * 把任务变成不是当前的
		 * @param {Object} id
		 */
		inactive:function(id){
			$("#" + id, this._taskList).removeClass("selected");
		},
		/**
		 * 向左移一个任务
		 */
		scrollLeft: function(){
			var task = this._visibleStart();
			this._scrollTask(task);
		},
		/**
		 * 向右移一个任务
		 */
		scrollRight: function(){
			var task = this._visibleEnd();
			this._scrollTask(task);
		},
		/**
		 * 移出当前点击的任务
		 * @param {Object} task
		 */
		scrollCurrent:function(task){
			this._scrollTask(task);
		},
		/**
		 * 切换任务
		 * @param {Object} id
		 */
		switchTask:function(id) {
			this._getCurrent().removeClass("selected");
			this.getTask(id).addClass("selected");
		},
		_getCurrent:function() {
			return this._taskList.find(">.selected");
		},
		getTask:function(id) {
			return $("#" + id, this._taskList);
		},
		/**
		 * 显示任务栏
		 */
		show:function(){
			if (this._taskBar.is(":hidden")) {
				this._taskBar.css("top", $(window).height() - 34 + this._taskBar.outerHeight()).show();
				this._taskBar.animate({
					top: $(window).height() - this._taskBar.outerHeight()
				}, 500);
			}
		},
		/**
		 * 隐藏任务栏
		 */
		hide:function(){
			this._taskBar.animate({
				top: $(window).height() - 29 + this._taskBar.outerHeight(true)
			}, 500,function(){
				$.taskBar._taskBar.hide();
			});
		}
	}
})(jQuery);

/**
 * 分页
 */
(function($){
	$.fn.pagination = function(opts){
		var setting = {
			first$:"li.j-first", prev$:"li.j-prev", next$:"li.j-next", last$:"li.j-last", nums$:"li.j-num>a", jumpto$:"li.jumpto",
			pageNumFrag:'<li class="#liClass#"><a href="#">#pageNum#</a></li>'
		};
		return this.each(function(){
			var $this = $(this);
			var pc = new Pagination(opts);
			var interval = pc.getInterval();

			var pageNumFrag = '';
			for (var i=interval.start; i<interval.end;i++){
				pageNumFrag += setting.pageNumFrag.replaceAll("#pageNum#", i).replaceAll("#liClass#", i==pc.getCurrentPage() ? 'selected j-num' : 'j-num');
			}
			$this.html(DWZ.frag["pagination"].replaceAll("#pageNumFrag#", pageNumFrag).replaceAll("#currentPage#", pc.getCurrentPage())).find("li").hoverClass();

			var $first = $this.find(setting.first$);
			var $prev = $this.find(setting.prev$);
			var $next = $this.find(setting.next$);
			var $last = $this.find(setting.last$);
			
			if (pc.hasPrev()){
				$first.add($prev).find(">span").hide();
				_bindEvent($prev, pc.getCurrentPage()-1, pc.targetType(),pc.localArea());
				_bindEvent($first, 1, pc.targetType(),pc.localArea());
			} else {
				$first.add($prev).addClass("disabled").find(">a").hide();
			}
			
			if (pc.hasNext()) {
				$next.add($last).find(">span").hide();
				_bindEvent($next, pc.getCurrentPage()+1, pc.targetType(),pc.localArea());
				_bindEvent($last, pc.numPages(), pc.targetType(),pc.localArea());
			} else {
				$next.add($last).addClass("disabled").find(">a").hide();
			}

			$this.find(setting.nums$).each(function(i){
				_bindEvent($(this), i+interval.start, pc.targetType(),pc.localArea());
			});
			$this.find(setting.jumpto$).each(function(){
				
				var $this = $(this);
				var $inputBox = $this.find(":text");
				var $button = $this.find(":button");
				$button.click(function(event){
					
					if(opts.beforeForm&&opts.postBeforeForm=='Y'){//分页前提交指定的FORM
						$.ajax({
						   type: "POST",
						   url: jQuery("#"+opts.beforeForm).attr("action"),	
						   data:jQuery("#"+opts.beforeForm).serializeArray(),
						   dataType:"json",
						   global:false,
						   success: function(json){
							   if(json.statusCode == 300){
									alertMsg.warn(json.message);
							   }
						   }
						});
						//$.post(jQuery("#"+opts.beforeForm).attr("action"),jQuery("#"+opts.beforeForm).serializeArray());
					}
					
					var pageNum = $inputBox.val();					
					if (pageNum && pageNum.isPositiveInteger()) {
						if (pc.targetType() == "dialog") {
							dialogPageBreak({pageNum:pageNum});
						} else if(pc.targetType() == "navTab"){
							navTabPageBreak({pageNum:pageNum});
						}else if(pc.targetType() == "localArea"){//局部刷新							
							localAreaPageBreak(pc.localArea(),{pageNum:pageNum});
						}
					}
				});
				$inputBox.keyup(function(event){
					if (event.keyCode == DWZ.keyCode.ENTER) $button.click();
				});
			});
		});
		
		function _bindEvent(jTarget, pageNum, targetType,localArea){
			jTarget.bind("click", {pageNum:pageNum}, function(event){
				
				if(opts.beforeForm&&opts.postBeforeForm=='Y'){//分页前提交指定的FORM
					$.ajax({
					   type: "POST",
					   url: jQuery("#"+opts.beforeForm).attr("action"),	
					   data:jQuery("#"+opts.beforeForm).serializeArray(),
					   dataType:"json",
					   global:false,
					   success: function(json){
						   if(json.statusCode == 300){
								alertMsg.warn(json.message);
						   }
					   }
					});
					//$.post(jQuery("#"+opts.beforeForm).attr("action"),jQuery("#"+opts.beforeForm).serializeArray());
				}
				if (targetType == "dialog") {
					dialogPageBreak({pageNum:event.data.pageNum});
				} else if(targetType == "navTab"){
					navTabPageBreak({pageNum:event.data.pageNum});
				}else if(targetType == "localArea"){
					localAreaPageBreak(localArea,{pageNum:event.data.pageNum});
				}
				event.preventDefault();
			});
		}
	}

	var Pagination = function(opts) {
		this.opts = $.extend({
			targetType:"navTab",	// navTab, dialog,localArea
			localArea:"",//局部刷新区域的ID
			totalCount:0,
			pageSize:10,
			pageNumShown:10,
			currentPage:1,
			beforeForm:"",
			postBeforeForm:"",
			callback:function(){return false;}
		}, opts);
	}
	
	$.extend(Pagination.prototype, {
		targetType:function(){return this.opts.targetType},
		localArea:function(){return this.opts.localArea},
		numPages:function() {
			return Math.ceil(this.opts.totalCount/this.opts.pageSize);
		},
		getInterval:function(){
			var ne_half = Math.ceil(this.opts.pageNumShown/2);
			var np = this.numPages();
			var upper_limit = np - this.opts.pageNumShown;
			var start = this.getCurrentPage() > ne_half ? Math.max( Math.min(this.getCurrentPage() - ne_half, upper_limit), 0 ) : 0;
			var end = this.getCurrentPage() > ne_half ? Math.min(this.getCurrentPage()+ne_half, np) : Math.min(this.opts.pageNumShown, np);
			return {start:start+1, end:end+1};
		},
		getCurrentPage:function(){
			var currentPage = parseInt(this.opts.currentPage);
			if (isNaN(currentPage)) return 1;
			return currentPage;
		},
		hasPrev:function(){
			return this.getCurrentPage() > 1;
		},
		hasNext:function(){
			return this.getCurrentPage() < this.numPages();
		}
	});
})(jQuery);

/**
 * effects 事件 v1.4.3
 */
(function($){
	$.extend($.fn, {
		jBlindUp: function(options){
			var op = $.extend({duration: 500, easing: "swing", call: function(){}}, options);
			return this.each(function(){
				var $this = $(this);
				$(this).animate({height: 0}, {
					step: function(){},
					duration: op.duration,
					easing: op.easing,
					complete: function(){ 
						$this.css({display: "none"});
						op.call();
					}
				});
			});
		},
		jBlindDown: function(options){
			var op = $.extend({to:0, duration: 500,easing: "swing",call: function(){}}, options);
			return this.each(function(){
				var $this = $(this);
				var	fixedPanelHeight = (op.to > 0)?op.to:$.effect.getDimensions($this[0]).height;
				$this.animate({height: fixedPanelHeight}, {
					step: function(){},
					duration: op.duration,
					easing: op.easing,
					complete: function(){ 
					$this.css({display: ""});
					op.call(); }
				});
			});
		},
		jSlideUp:function(options) {
			var op = $.extend({to:0, duration: 500,easing: "swing",call: function(){}}, options);
			return this.each(function(){
				var $this = $(this);
				$this.wrapInner("<div></div>");
				var	fixedHeight = (op.to > 0)?op.to:$.effect.getDimensions($(">div",$this)[0]).height;
				$this.css({overflow:"visible",position:"relative"});
				$(">div",$this).css({position:"relative"}).animate({top: -fixedHeight}, {
					easing: op.easing,
					duration: op.duration,
					complete:function(){$this.html($(this).html());}
				});
				$this.animate({height: 0}, {
					duration: op.duration,
					easing: op.easing,
					complete: function(){ $this.css({display: "none", height:""}); op.call(); }
				});
			});
		},
		jSlideDown:function(options) {
			var op = $.extend({to:0, duration: 500,easing: "swing",call: function(){}}, options);
			return this.each(function(){
				var $this = $(this);
				var	fixedHeight = (op.to > 0)?op.to:$.effect.getDimensions($this[0]).height;
				$this.wrapInner("<div style=\"top:-" + fixedHeight + "px;\"></div>");
				$this.css({overflow:"visible",position:"relative", height:"0px"})
				.animate({height: fixedHeight}, {
					duration: op.duration,
					easing: op.easing,
					complete: function(){  $this.css({display: "", overflow:""}); op.call(); }
				});
				$(">div",$this).css({position:"relative"}).animate({top: 0}, {
					easing: op.easing,
					duration: op.duration,
					complete:function(){$this.html($(this).html());}
				});
			});
		}
	});
	$.effect = {
		getDimensions: function(element, displayElement){
			var dimensions = new $.effect.Rectangle;
			var displayOrig = $(element).css('display');
			var visibilityOrig = $(element).css('visibility');
			var isZero = $(element).height()==0?true:false;
			if ($(element).is(":hidden")) {
				$(element).css({visibility: 'hidden', display: 'block'});
				if(isZero)$(element).css("height","");
				if ($.browser.opera)
					refElement.focus();
			}
			dimensions.height = $(element).height();
			dimensions.width = $(element).width();
			if (displayOrig == 'none'){
				$(element).css({visibility: visibilityOrig, display: 'none'});
				if(isZero) if(isZero)$(element).css("height","0px");
			}
			return dimensions;
		}
	}
	$.effect.Rectangle = function(){
		this.width = 0;
		this.height = 0;
		this.unit = "px";
	}
})(jQuery);

/**
 * 面板
 */
(function($){
	$.extend($.fn, {
		jPanel:function(options){
			var op = $.extend({header:"panelHeader", headerC:"panelHeaderContent", content:"panelContent", coll:"collapsable", exp:"expandable", footer:"panelFooter", footerC:"panelFooterContent"}, options);
			return this.each(function(){
				var $panel = $(this);
				var close = $panel.hasClass("close");
				var collapse = $panel.hasClass("collapse");
				
				var $content = $(">div", $panel).addClass(op.content);				
				var title = $(">h1",$panel).wrap('<div class="'+op.header+'"><div class="'+op.headerC+'"></div></div>');
				if(collapse)$("<a href=\"\"></a>").addClass(close?op.exp:op.coll).insertAfter(title);

				var header = $(">div:first", $panel);
				var footer = $('<div class="'+op.footer+'"><div class="'+op.footerC+'"></div></div>').appendTo($panel);
				
				var defaultH = $panel.attr("defH")?$panel.attr("defH"):0;
				var minH = $panel.attr("minH")?$panel.attr("minH"):0;
				if (close) 
					$content.css({
						height: "0px",
						display: "none"
					});
				else {
					if (defaultH > 0) 
						$content.height(defaultH + "px");
					else if(minH > 0){
						$content.css("minHeight", minH+"px");
					}
				}
				if(!collapse) return;
				var $pucker = $("a", header);
				var inH = $content.innerHeight() - 6;
				if(minH > 0 && minH >= inH) defaultH = minH;
				else defaultH = inH;
				$pucker.click(function(){
					if($pucker.hasClass(op.exp)){
						$content.jBlindDown({to:defaultH,call:function(){
							$pucker.removeClass(op.exp).addClass(op.coll);
							if(minH > 0) $content.css("minHeight",minH+"px");
						}});
					} else { 
						if(minH > 0) $content.css("minHeight","");
						if(minH >= inH) $content.css("height", minH+"px");
						$content.jBlindUp({call:function(){
							$pucker.removeClass(op.coll).addClass(op.exp);
						}});
					}
					return false;
				});
			});
		}
	});
})(jQuery);     
/**
 * 面板拖动 v1.4.3
 */
(function($){
	var _op = {
		cursor: 'move', // selector 的鼠标手势
		sortBoxs: 'div.sortDrag', //拖动排序项父容器
		replace: true, //2个sortBox之间拖动替换
		items: '> *', //拖动排序项选择器
		selector: '', //拖动排序项用于拖动的子元素的选择器，为空时等于item
		zIndex: 1000
	};
	var sortDrag = {
		start:function($sortBox, $item, event, op){
			var $placeholder = this._createPlaceholder($item);
			var $helper = $item.clone();
			var position = $item.position();
			$helper.data('$sortBox', $sortBox).data('op', op).data('$item', $item).data('$placeholder', $placeholder);
			$helper.addClass('sortDragHelper').css({position:'absolute',top:position.top,left:position.left,zIndex:op.zIndex,width:$item.width()+'px',height:$item.height()+'px'}).jDrag({
				selector:op.selector,
				drag:this.drag,
				stop:this.stop,
				event:event
			});

			$item.before($placeholder).before($helper).hide();
			return false;
		},
		drag:function(){
			var $helper = $(arguments[0]), $sortBox = $helper.data('$sortBox'), $placeholder = $helper.data('$placeholder');
			var $items = $sortBox.find($helper.data('op')['items']).filter(':visible').filter(':not(.sortDragPlaceholder, .sortDragHelper)');
			var helperPos = $helper.position(), firstPos = $items.eq(0).position();

			var $overBox = sortDrag._getOverSortBox($helper);
			if ($overBox.length > 0 && $overBox[0] != $sortBox[0]){ //移动到其他容器
				$placeholder.appendTo($overBox);
				$helper.data('$sortBox', $overBox);
			} else {
				for (var i=0; i<$items.length; i++) {
					var $this = $items.eq(i), position = $this.position();
		
					if (helperPos.top > position.top + 10) {
						$this.after($placeholder);
					} else if (helperPos.top <= position.top) {
						$this.before($placeholder);
						break;
					}
				}
			}
		},
		stop:function(){
			var $helper = $(arguments[0]), $item = $helper.data('$item'), $placeholder = $helper.data('$placeholder');

			var position = $placeholder.position();
			$helper.animate({
				top: position.top + "px",
				left: position.left + "px"
			}, {
				complete: function(){
					if ($helper.data('op')['replace']){ //2个sortBox之间替换处理
						$srcBox = $item.parents(_op.sortBoxs+":first");
						$destBox = $placeholder.parents(_op.sortBoxs+":first");
						if ($srcBox[0] != $destBox[0]) { //判断是否移动到其他容器中
							$replaceItem = $placeholder.next();
							if ($replaceItem.size() > 0) {
								$replaceItem.insertAfter($item);
							}
						}
					}
					$item.insertAfter($placeholder).show();
					$placeholder.remove();
					$helper.remove();
				},
				duration: 300
			});
		},
		_createPlaceholder:function($item){
			return $('<'+$item[0].nodeName+' class="sortDragPlaceholder"/>').css({
				width:$item.outerWidth()+'px',
				height:$item.outerHeight()+'px',
				marginTop:$item.css('marginTop'),
				marginRight:$item.css('marginRight'),
				marginBottom:$item.css('marginBottom'),
				marginLeft:$item.css('marginLeft')
			});
		},
		_getOverSortBox:function($item){
			var itemPos = $item.position();
			var y = itemPos.top+($item.height()/2), x = itemPos.left+($item.width()/2);
			return $(_op.sortBoxs).filter(':visible').filter(function(){
				var $sortBox = $(this), sortBoxPos = $sortBox.position();
				return DWZ.isOver(y, x, sortBoxPos.top, sortBoxPos.left, $sortBox.height(), $sortBox.width());
			});
		}
	};
	
	$.fn.sortDrag = function(options){
				
		return this.each(function(){
			var op = $.extend({}, _op, options);
			var $sortBox = $(this);
			
			if ($sortBox.attr('selector')) op.selector = $sortBox.attr('selector');
			$sortBox.find(op.items).each(function(i){
				var $item = $(this), $selector = $item;
				if (op.selector) {
					$selector = $item.find(op.selector).css({cursor:op.cursor});
				}

				$selector.mousedown(function(event){
					sortDrag.start($sortBox, $item, event, op);
	
					event.preventDefault();
				});
			});
			
			
		});
	}
})(jQuery);

/**
 * ajax 历史记录
 */
(function($){
	$.extend({		
		History: {
			_hash: new Array(),
			_cont: undefined,
			_currentHash: "",
			_callback: undefined,
			init: function(cont, callback){
				$.History._cont = cont;
				$.History._callback = callback;
				var current_hash = location.hash.replace(/\?.*$/, '');
				$.History._currentHash = current_hash;
				if ($.browser.msie) {
					if ($.History._currentHash == '') {
						$.History._currentHash = '#';
					}
					$("body").prepend('<iframe id="jQuery_history" style="display: none;"></iframe>');
					var ihistory = $("#jQuery_history")[0];
					var iframe = ihistory.contentDocument || ihistory.contentWindow.document;
					iframe.open();
					iframe.close();
					iframe.location.hash = current_hash;
				}
				if ($.isFunction(this._callback)) 
					$.History._callback(current_hash.skipChar("#"));
				setInterval($.History._historyCheck, 100);
			},
			_historyCheck: function(){
				var current_hash = "";
				if ($.browser.msie) {
					var ihistory = $("#jQuery_history")[0];
					var iframe = ihistory.contentWindow;
					current_hash = iframe.location.hash.skipChar("#").replace(/\?.*$/, '');
				} else {
					current_hash = location.hash.skipChar('#').replace(/\?.*$/, '');
				}
//				if (!current_hash) {
//					if (current_hash != $.History._currentHash) {
//						$.History._currentHash = current_hash;
//						//TODO
//					}
//				} else {
					if (current_hash != $.History._currentHash) {
						$.History._currentHash = current_hash;
						$.History.loadHistory(current_hash);
					}
//				}
				
			},
			addHistory: function(hash, fun, args){
				$.History._currentHash = hash;
				var history = [hash, fun, args];
				$.History._hash.push(history);
				if ($.browser.msie) {
					var ihistory = $("#jQuery_history")[0];
					var iframe = ihistory.contentDocument || ihistory.contentWindow.document;
					iframe.open();
					iframe.close();
					iframe.location.hash = hash.replace(/\?.*$/, '');
					location.hash = hash.replace(/\?.*$/, '');
				} else {
					location.hash = hash.replace(/\?.*$/, '');
				}
			},
			loadHistory: function(hash){
				if ($.browser.msie) {
					location.hash = hash;
				}
				for (var i = 0; i < $.History._hash.length; i += 1) {
					if ($.History._hash[i][0] == hash) {
						$.History._hash[i][1]($.History._hash[i][2]);
						return;
					}
				}
			}
		}
	});
})(jQuery);

/**
 * 验证扩展
 */
(function($){
	$.validator.addMethod("alphanumeric", function(value, element) {
		return this.optional(element) || /^\w+$/i.test(value);
	}, "Letters, numbers or underscores only please");
	
	$.validator.addMethod("lettersonly", function(value, element) {
		return this.optional(element) || /^[a-z]+$/i.test(value);
	}, "Letters only please"); 
	
	$.validator.addMethod("phone", function(v, element) {//有效电话号码，中国大陆
	    v = v.replace(/\s+/g, ""); 
		return this.optional(element) || v.match(/(^[0-9]{3,4}\-[0-9]{3,8}$)|(^[0-9]{3,8}$)|(^\([0-9]{3,4}\)[0-9]{3,8}$)|(^[0-9]{3,4}[0-9]{3,8}$)|(^0{0,1}13[0-9]{9}$)/);
	}, "Please specify a valid phone number");
	
	$.validator.addMethod("mobile", function(v, element) {//有效手机，中国大陆
	    v = v.replace(/\s+/g, ""); 
		return this.optional(element) || v.match(/^0{0,1}1(3|4|5|7|8)[0-9]{9}$/);
	}, "Please specify a valid mobile number");
	
	$.validator.addMethod("ipaddress", function(v, element) {//合法IP
	    v = v.replace(/\s+/g, ""); 
		return this.optional(element) || v.match(/^(\d{1}|\d{2}|[0-1]\d{2}|2[0-4]\d|25[0-5])\.(\d{1}|\d{2}|[0-1]\d{2}|2[0-4]\d|25[0-5])\.(\d{1}|\d{2}|[0-1]\d{2}|2[0-4]\d|25[0-5])\.(\d{1}|\d{2}|[0-1]\d{2}|2[0-4]\d|25[0-5])$/);
	}, "Please specify a valid ip address");
	
	$.validator.addMethod("chinese", function(v, element) {//是否为中文
	    v = v.replace(/\s+/g, ""); 
		return this.optional(element) || v.match(/^[\u4E00-\u9FA5]{0,25}$/);
	}, "Please specify a valid chinese char");
	
	$.validator.addMethod("idnumber", function(v, element) {//是否合法身份证
	    v = v.replace(/\s+/g, ""); 
	    ////身份证正则表达式(15位)
		//isIDCard1=/^[1-9]\d{7}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])\d{3}$/;
		//身份证正则表达式(18位)
		//isIDCard2=/^[1-9]\d{5}[1-9]\d{3}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])\d{4}$/	  
	    
		return this.optional(element) || _idCardValidate(v);
	}, "Please specify a valid idnumber char");
	
	
	$.validator.addMethod("date1", function(v, element) {//日期格式 yyyy-MM-dd
	    v = v.replace(/\s+/g, ""); 
		return this.optional(element) || v.match(/^\d{4}\-\d{2}\-\d{2}$/);
	}, "Please specify a valid date format yyyy-MM-dd");
	
	$.validator.addMethod("date2", function(v, element) {//日期格式 yyyy-MM-dd HH:mm:ss
	    v = v.replace(/\s+/g, ""); 
		return this.optional(element) || v.match(/^\d{4}\-\d{2}\-\d{2}\s\d{2}:\d{2}:\d{2}$/);
	}, "Please specify a valid date format yyyy-MM-dd yyyy-MM-dd HH:mm:ss");
	
	$.validator.addMethod("postcode", function(v, element) {
	    v = v.replace(/\s+/g, ""); 
		return this.optional(element) || v.match(/^(\d){6}$/);//中国邮编
	}, "Please specify a valid postcode");
	
	$.validator.addMethod("maxTo",function(value, element, param) {				 
			var target = $(param).unbind(".validate-maxTo").bind("blur.validate-maxTo", function() {
				$(element).valid();
			});
			return value > target.val();			 
		},"必须小于");
	
	$.validator.addClassRules({
		alphanumeric: { alphanumeric: true },
		lettersonly: { lettersonly: true },
		phone: { phone: true },
		postcode: {postcode: true},
		mobile: {mobile:true},
		ipaddress:{ipaddress:true},
		chinese:{chinese:true},
		date1:{date1:true},
		date2:{date2:true},
		maxTo:{maxTo:true}
	});

})(jQuery);

/**
 * 验证中文提示
 */
(function($){
	$.extend($.validator.messages, {
		required: "必填字段",
		remote: "请修正该字段",
		email: "请输入正确格式的电子邮件",
		url: "请输入合法的网址",
		idnumber: "请输入合法的身份证号码",
		date: "请输入合法的日期",
		dateISO: "请输入合法的日期 (ISO).",
		number: "请输入合法的数字",
		digits: "只能输入整数",
		creditcard: "请输入合法的信用卡号",
		equalTo: "请再次输入相同的值",		
		
		accept: "请输入拥有合法后缀名的字符串",
		maxlength: $.validator.format("请输入长度为 {0}以内的字符"),
		minlength: $.validator.format("请输入长度最少是 {0} 的字符"),
		rangelength: $.validator.format("请输入长度介于 {0} 和 {1} 之间的字符"),
		range: $.validator.format("请输入介于 {0} 和 {1} 之间的值"),
		max: $.validator.format("请输入最大为 {0} 的值"),
		min: $.validator.format("请输入最小为 {0} 的值"),
		postcode: "请输入正确的邮编",
		alphanumeric: "必须为字母、数字、下划线",
		lettersonly: "必须是字母",
		phone: "请输入有效电话号码",
		mobile:"请输入有效手机号",
		ipaddress:"请输入有效IP地址",
		chinese:"请输入中文",
		date1:"请输入格式为'年-月-日'的日期格式",
		date2:"请输入格式为'年-月-日 小时:分钟:秒'的日期格式"
	});
	
	$.validator.setDefaults({errorElement:"span"});
})(jQuery);

/**
 * 样式
 */
(function($){
	$.fn.extend({
		theme: function(options){
			var op = $.extend({themeBase:"themes"}, options);
			var _themeHref =  baseUrl + "/"+op.themeBase + "/#theme#/style.css";
			return this.each(function(){
				var jThemeLi = $(this).find(">li[theme]");
				var setTheme = function(themeName){
					$("head").find("link[href$=style.css]").attr("href", _themeHref.replace("#theme#", themeName));
					jThemeLi.find(">div").removeClass("selected");
					jThemeLi.filter("[theme="+themeName+"]").find(">div").addClass("selected");
					
					if ($.isFunction($.cookie)) $.cookie("dwz_theme", themeName);
				}
				
				jThemeLi.each(function(index){
					$(this).click(function(){
						setTheme($(this).attr("theme"));
					});

				});
					
				if ($.isFunction($.cookie)){
					var themeName = $.cookie("dwz_theme");
					if (themeName) {
						setTheme(themeName);
					}
				}
				
			});
		}
	});
})(jQuery);

/**
 * 切换风格
 */
(function($){
	$.fn.switchEnv = function(){
		var op = {cities$:">ul>li", boxTitle$:">a>span"};
		return this.each(function(){
			var $this = $(this);
			$this.click(function(){
				if ($this.hasClass("selected")){
					_hide($this);
				} else {
					_show($this);
				}
				return false;
			});
			
//			$this.find(op.cities$).click(function(){
//				var $li = $(this);
//				$.post($li.find(">a").attr("href"), {}, function(html){
//					_hide($this);
//					$this.find(op.boxTitle$).html($li.find(">a").html());
//					navTab.closeAllTab();
//					$("#sidebar").html(html).initUI();
//				});
//				return false;
//			});
		});
	}
	
	function _show($box){
		$box.addClass("selected");
		$(document).bind("click",{box:$box}, _handler);
	}
	function _hide($box){
		$box.removeClass("selected");
		$(document).unbind("click", _handler);
	}
	
	function _handler(event){
		_hide(event.data.box)
	}
})(jQuery);


/**
 * 初始化环境
 */
function initEnv() {
	if ( $.browser.msie && /6.0/.test(navigator.userAgent) ) {
		try {
			document.execCommand("BackgroundImageCache", false, 1);	
		}catch(e){	
		}
	}
	initLayout();
	$(window).resize(function(){
		initLayout();
	});

	$("#leftside").jBar({minW:150, maxW:700});
	
	if ($.taskBar) $.taskBar.init();
	if (navTab) navTab.init();
	$("#switchEnvBox").switchEnv();//切换
	$("#fastEntryBox").switchEnv();//切换快速入口
	initUI();

	$("#taskbar li").hoverClass("hover");
	$("#taskbar li.selected").hoverClass("selectedHover");
	$("#taskbar .close").hoverClass("closeHover");
	$("#taskbar .restore").hoverClass("restoreHover");
	$("#taskbar .minimize").hoverClass("minimizeHover");
	$("#taskbar .taskbarLeft").hoverClass("taskbarLeftHover");
	$("#taskbar .taskbarRight").hoverClass("taskbarRightHover");
	
	// tab styles
	var jTabsPH = $("div.tabsPageHeader");
	jTabsPH.find(".tabsLeft").hoverClass("tabsLeftHover");
	jTabsPH.find(".tabsRight").hoverClass("tabsRightHover");
	jTabsPH.find(".tabsMore").hoverClass("tabsMoreHover");
	
	setTimeout(function(){
		var ajaxbg = $("#background,#progressBar");
		ajaxbg.hide();
		$(document).ajaxStart(function(){
			ajaxbg.show();
		}).ajaxStop(function(){
			ajaxbg.hide();
		});
	}, 500);
}
/**
 * 初始化布局
 */
function initLayout(){
	var iContentW = $(window).width() - (DWZ.ui.sbar ? $("#sidebar").width() + 10 : 29) - 5;
	var iContentH = $(window).height() - $("#header").height() - 29;

	$("#container").width(iContentW);
	$("#container .tabsPageContent").height(iContentH - 29).find("[layoutH]").layoutH();
	$("#sidebar, #sidebar_s .collapse, #splitBar, #splitBarProxy").height(iContentH - 2);
	$("#taskbar").css({top: iContentH + $("#header").height() + 5});
	$("#taskbar").width($(window).width());
}
/**
 * 初始化UI
 * @param {} jP
 */
function initUI(jP){
	var jParent = $(jP || document);

	//tables
	$("table.table", jParent).jTable();	
	// css tables
	$('table.list>tbody>tr', jParent).hover(function(){
		$(this).addClass('hover');
	}, function(){
		$(this).removeClass('hover');
	}).each(function(index){
		if (index % 2 == 1) $(this).addClass("trbg");
	});

	//auto bind tabs
	//	$("div.tabs", jParent).tabs({eventType:"hover"});
	//$("div.tabs", jParent).tabs({eventType:"click",curentIndex:"0"});
	$("div.tabs", jParent).each(function(){
		var $this = $(this);
		var options = {};
		options.currentIndex = $this.attr("currentIndex") || 0;
		options.eventType = $this.attr("eventType") || "click";
		$this.tabs(options);
	});

	$("ul.tree", jParent).jTree();
	$('div.accordion', jParent).accordion({fillSpace:true,alwaysOpen:true,active:0});
	//$("textarea.editor").xheditor({skin: 'vista'});
	
	// init styles
	$("input[type=text], input[type=password], textarea", jParent).addClass("textInput").focusClass("focus");

	$("input[readonly], textarea[readonly]", jParent).addClass("readonly");
	$("input[disabled=true], textarea[disabled=true]", jParent).addClass("disabled");

	$("input[type=text]").filter("[alt]").inputAlert();

	//Grid ToolBar
	$("div.panelBar li, div.panelBar", jParent).hoverClass("hover");
		
	//Button
	$("div.button", jParent).hoverClass("buttonHover");
	$("div.buttonActive", jParent).hoverClass("buttonActiveHover");
	
	//tabsPageHeader
	$("div.tabsHeader li, div.tabsPageHeader li, div.accordionHeader, div.accordion", jParent).hoverClass("hover");
	
	if ($.fn.sortDrag) $("div.sortDrag", jParent).sortDrag();
	$("div.panel", jParent).jPanel();

	//validate form
	$("form.required-validate", jParent).each(function(){
		$(this).validate({
			focusInvalid: false,
			focusCleanup: true,
			errorElement: "span",
			ignore:".ignore"
		});
	});
	
	/*
	if ($.fn.datepicker){
		$('input.date', jParent).each(function(){
			var $this = $(this);
			var opts = {};
			if ($this.attr("pattern")) opts.pattern = $this.attr("pattern");
			if ($this.attr("yearstart")) opts.yearstart = $this.attr("yearstart");
			if ($this.attr("yearend")) opts.yearend = $this.attr("yearend");
			$this.datepicker(opts);
		});
	}
	*/

	// navTab
	$("a[target=navTab]", jParent).each(function(){
		$(this).click(function(event){
			var $this = $(this);
			var title = $this.attr("title") || $this.text();
			var rel = $this.attr("rel") || "_blank";
			navTab.openTab(rel, $this.attr("href"), title);

			event.preventDefault();
		});
	});
	// navTabTodo
	$("a[target=navTabTodo]", jParent).each(function(){
		$(this).click(function(event){
			var $this = $(this);
			var title = $this.attr("title");
			if (title) {
				alertMsg.confirm(title, {
					okCall: function(){
						navTabTodo($this.attr("href"));
					}
				});
			} else {
				navTabTodo($this.attr("href"));
			}
			event.preventDefault();
		});
	});
	
	//dialogs
	$("a[target=dialog]", jParent).each(function(){
		$(this).click(function(event){
			var $this = $(this);
			var title = $this.attr("title") || $this.text();
			var rel = $this.attr("rel") || "_blank";
			var options = {};
			var w = $this.attr("width");
			var h = $this.attr("height");
			if (w) options.width = w;
			if (h) options.height = h;
			options.max = $this.attr("max");
			options.mask = $this.attr("mask");
			$.pdialog.open($this.attr("href"), rel, title, options);
			
			event.preventDefault();
		});
	});
	
	$("a[target=ajax]", jParent).each(function(){
		$(this).click(function(event){
			var $this = $(this);
			var rel = $this.attr("rel");
			if (rel) {
				var $rel = $("#"+rel); 
				$rel.loadUrl($this.attr("href"), {}, function(){
					$rel.find("[layoutH]").layoutH();
				});
			}

			event.preventDefault();
		});
	});
		
	$("div.pagination", jParent).each(function(){
		var $this = $(this);
		$this.pagination({
			targetType:$this.attr("targetType"),
			localArea:$this.attr("localArea"),
			totalCount:$this.attr("totalCount"),
			pageSize:$this.attr("pageSize"),
			pageNumShown:$this.attr("pageNumShown"),
			beforeForm:$this.attr("beforeForm"),
			postBeforeForm:$this.attr("postBeforeForm"),
			currentPage:$this.attr("currentPage")
		});
	});
}

/**
 * 普通表单验证回调函数
 * @param {} form
 * @param {} callback
 * @return {Boolean}
 */
function validateCallback(form, callback) {
	var $form = $(form);
	if (!$form.valid()) {
		alertMsg.error(DWZ.msg["validateFormError"]);
		return false; 
	}

	$.ajax({
		type:'POST',
		url:$form.attr("action"),
		data:$form.serializeArray(),
		dataType:"json",
		cache: false,
		success: callback || validateFormCallback,
		
		error: DWZ.ajaxError
	});
	return false;
}

function validateFormCallback(json){
	if(json.statusCode == 300) {
			if(json.message && alertMsg) alertMsg.error(json.message);
	} else if (json.statusCode == 301) {
			alertMsg.error(json.message, {okCall:function(){
				window.location = DWZ.loginUrl;
	}});
	}else if(json.statusCode==202){
		if(json.message && alertMsg) alertMsg.warn(json.message);
		if(json.reloadUrl){
			navTab.reload(json.reloadUrl);
		}
	} else if(json.statusCode == 201){//relogin
		if(json.message && alertMsg) alertMsg.correct(json.message);
		window.location = json.reloginUrl;
	} else {		
		if(json.message && alertMsg) alertMsg.correct(json.message);		
		navTab.reload(json.reloadUrl);
	};
}

/**
 * 带文件上传的ajax表单提交
 * @param {Object} form
 * @param {Object} callback
 */
function iframeCallback(form, callback){
	if(!$(form).valid()) {return false;}
	window.donecallback = callback || DWZ.ajaxDone;
	if ($("#callbackframe").size() == 0) {
		$("<iframe id='callbackframe' name='callbackframe' src='about:blank' style='display:none'></iframe>").appendTo("body");
	}
	form.target = "callbackframe";	
}

/**
 * navTabAjaxDone是DWZ框架中预定义的表单提交回调函数．
 * 服务器转回navTabId可以把那个navTab标记为reloadFlag=1, 下次切换到那个navTab时会重新载入内容. 
 * callbackType如果是closeCurrent就会关闭当前tab
 * navTabAjaxDone这个回调函数基本可以通用了，如果还有特殊需要也可以自定义回调函数.
 * 如果表单提交只提示操作是否成功, 就可以不指定回调函数. 框架会默认调用DWZ.ajaxDone()
 * <form action="/userAction?method=save" onsubmit="return validateCallback(this, navTabAjaxDone)">
 * 
 * form提交后返回json数据结构statusCode=200表示操作成功, 做页面跳转等操作. statusCode=300表示操作失败, 提示错误原因.
 * {"statusCode":"200", "message":"操作成功", "navTabId":"navNewsLi", "forwardUrl":"", "callbackType":"closeCurrent"}
 * {"statusCode":"300", "message":"操作失败"}
 */
function navTabAjaxDone(json){
	DWZ.ajaxDone(json);
	if (json.statusCode == 200){
		if(json.localArea){//局部刷新
			localAreaPageBreak(json.localArea);
		}else{
			if (json.navTabId){			
				navTab.reloadFlag(json.navTabId);
			} else {
				navTabPageBreak();			
			}
		}
		
		
		if ("closeCurrent" == json.callbackType) {
			setTimeout(function(){navTab.closeCurrentTab();}, 100);
		} else if ("forward" == json.callbackType) {			
			navTab.reload(json.forwardUrl);
		}
	}
}

/**
 * dialog上的表单提交回调函数
 * 服务器转回navTabId，可以重新载入指定的navTab. statusCode=200表示操作成功, 自动关闭当前dialog
 * 
 * form提交后返回json数据结构,json格式和navTabAjaxDone一致
 */
function dialogAjaxDone(json){
	DWZ.ajaxDone(json);
	if (json.statusCode == 200){
		if (json.navTabId){
			navTab.reload(null, {}, json.navTabId);
		}
		$.pdialog.closeCurrent();
	}
}


function navTabSearch(form){
	navTab.reload(form.action, $(form).serializeArray());
	return false;
}

function dialogSearch(form){
	$.pdialog.reload(form.action, $(form).serializeArray());
	return false;
}

function localAreaSearch(parent,form){//局部更新搜索
	$("#"+parent).loadUrl(form.action, $(form).serializeArray(), function(){
		$("#"+parent).find("[layoutH]").layoutH();
	});
	return false;
}

/**
 * 
 * @param {Object} args {pageNum:"",pageSize:"",orderField:""}
 * @param String formId 分页表单选择器，非必填项默认值是 "pagerForm"
 */
function _getPagerForm($parent, args) {
	var form = $("#pagerForm", $parent).get(0);

	if (form) {
		args = args || {};
		if(args["pageNum"])form.pageNum.value = args["pageNum"];
		if(args["pageSize"])form.pageSize.value = args["pageSize"];
		if(args["orderField"])form.orderField.value = args["orderField"];
	}
	
	return form;
}
function navTabPageBreak(args){		
	var form = _getPagerForm(navTab.getCurrentPanel(), args);
	if (form) navTab.reload(form.action, $(form).serializeArray());
}
function dialogPageBreak(args){
	var form = _getPagerForm($.pdialog.getCurrent(), args);
	if (form) $.pdialog.reload(form.action, $(form).serializeArray());
}

function localAreaPageBreak(parent,args){//局部区域的分页
	var form = $("#pagerForm", $("#"+parent)).get(0);

	if (form) {
		args = args || {};
		if(args["pageNum"])form.pageNum.value = args["pageNum"];
		if(args["pageSize"])form.pageSize.value = args["pageSize"];		
	}
	$("#"+parent).loadUrl(form.action, $(form).serializeArray(), function(){
		$("#"+parent).find("[layoutH]").layoutH();
	});
}

function navTabTodo(url, data){
	$.ajax({
		type:'POST',
		url:url,
		data:data,
		dataType:"json",
		cache: false,
		success: navTabAjaxDone,
		error: DWZ.ajaxError
	});
}

/**
 * checkbox全选/全不选
 * @param checkallId 全选checkbox ID，需要加#前置
 * @param checkboxitemName 子checkbox name
 */
function checkboxAll(checkallId,checkboxItemName,bodyName){
	$(bodyName+" INPUT[name='"+checkboxItemName+"']").each(function(ind){
		$(this).attr("checked",$(checkallId).attr("checked"));
	});
}
/**
 * 当子项中有一个没有被选中时,全选取消,否则为选中
 * @param {} checkallId 全选checkbox ID，需要加#前置
 * @param {} checkboxItemName 子checkbox name
 */
function uncheckboxAll(checkallId,checkboxItemName){
	var obj = $("input[name='"+checkboxItemName+"']");
	var chkFlag = true;
	for(var i=0;i<obj.length;i++){
		if(!obj[i].checked ){
			chkFlag = false;
			break;
		}
	}
	$(checkallId).attr('checked', chkFlag);
}

/**
 * 判断checkbox是否选中
 */
function isChecked(chkname,bodyname){	
	var obj = $(bodyname+" input[name='"+chkname+"']");
	for(var i=0;i<obj.length;i++){
		if(obj[i].checked ){
			return true;
		}
	}
	return false;
}

/**
 * 返回选择checkbox的个数
 * @param {} chkname
 */
function checkedNum(chkname,bodyname){
	var num = 0;
	if($(bodyname+" input[name='"+chkname+"']"))	num = $(bodyname+" input[name='"+chkname+"']:checked").size();
	return num;
}
/**
 * 只能选择一个
 * @param {} chkname
 */
function isCheckOnlyone(chkname,bodyname){
 	if(!isChecked(chkname,bodyname)){
 		alertMsg.warn('请选择一条要操作记录！');
		return false;
 	}
 	if(checkedNum(chkname,bodyname)>1){//只能编辑一条
		alertMsg.warn('只能选择一条记录操作！');
		return false;
	}	 	
 	return true;
}

/**
 * 页面工具栏辅助
 * @param {} msg
 * @param {} postUrl
 */
function pageBarHandle(msg,postUrl,bodyname){
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
                    })
                
				$.post(postUrl,{resourceid:res}, navTabAjaxDone, "json");
			}
	});			
}
/**
 * 组织单位选择器 rootCode - 组织根编码,checkType  0-radio,1-checkbox
 * @param {} idsN 赋值的INPUT ID name
 * @param {} namesN 赋值的INPUT NAME name
 * @param {} checkType radio,checkbox
 * @param {} rootCode
 * @param {} checkedValue
 */
function orgUnitSelector(idsN,namesN,checkType,rootCode,checkedValue){
	$.pdialog.open(baseUrl+'/edu3/framework/selector/org.html?idsN='+idsN+'&namesN='+namesN+'&parentUnitCode='+rootCode+'&checkBoxType='+checkType+'&checkedValues='+checkedValue,
					'selector',
					'选择组织单位',
					{mask:true,width:450,height:600}
					);
}

//函数说明：合并指定表格（表格id为_w_table_id）指定列（列数为_w_table_colnum）的相同文本的相邻单元格   
//参数说明：_w_table_id 为需要进行合并单元格的表格的id。如在HTMl中指定表格 id="data" ，此参数应为 #data    
//参数说明：_w_table_colnum 为需要合并单元格的所在列。为数字，从最左边第一列为1开始算起。 
//参数说明：_w_content_type 单元格比较内容,text-文本内容，html-html内容
function _w_table_rowspan(_w_table_id,_w_table_colnum,_w_content_type){   
    _w_table_firsttd = "";   
    _w_table_currenttd = "";   
    _w_table_SpanNum = 0;   
    _w_content_type = _w_content_type || "text";//默认为文本内容比较
    _w_table_Obj = $(_w_table_id + " tr td:nth-child(" + _w_table_colnum + ")");   
    _w_table_Obj.each(function(i){   
        if(i==0){   
            _w_table_firsttd = $(this);   
            _w_table_SpanNum = 1;   
        }else{   
            _w_table_currenttd = $(this);   
            if(_w_content_type=="text" && _w_table_firsttd.text()==_w_table_currenttd.text() || _w_content_type=="html" && _w_table_firsttd.html()==_w_table_currenttd.html()){   
                _w_table_SpanNum++;   
                _w_table_currenttd.hide(); //remove();   
                _w_table_firsttd.attr("rowSpan",_w_table_SpanNum).css({"vertical-align": "middle"}); //合并行,居中  
            }else{   
                _w_table_firsttd = $(this);   
                _w_table_SpanNum = 1;   
            }   
        }   
    });    
}   
  
//函数说明：合并指定表格（表格id为_w_table_id）指定行（行数为_w_table_rownum）的相同文本的相邻单元格   
//参数说明：_w_table_id 为需要进行合并单元格的表格id。如在HTMl中指定表格 id="data" ，此参数应为 #data    
//参数说明：_w_table_rownum 为需要合并单元格的所在行。其参数形式请参考jQuery中nth-child的参数。   
//          如果为数字，则从最左边第一行为1开始算起。   
//          "even" 表示偶数行   
//          "odd" 表示奇数行   
//          "3n+1" 表示的行数为1、4、7、10.......   
//参数说明：_w_table_maxcolnum 为指定行中单元格对应的最大列数，列数大于这个数值的单元格将不进行比较合并。   
//          此参数可以为空，为空则指定行的所有单元格要进行比较合并。   
function _w_table_colspan(_w_table_id,_w_table_rownum,_w_table_maxcolnum){   
    if(_w_table_maxcolnum == void 0){_w_table_maxcolnum=0;}   
    _w_table_firsttd = "";   
    _w_table_currenttd = "";   
    _w_table_SpanNum = 0;   
    $(_w_table_id + " tr:nth-child(" + _w_table_rownum + ")").each(function(i){   
        _w_table_Obj = $(this).children();   
        _w_table_Obj.each(function(i){   
            if(i==0){   
                _w_table_firsttd = $(this);   
                _w_table_SpanNum = 1;   
            }else if((_w_table_maxcolnum>0)&&(i>_w_table_maxcolnum)){   
                return "";   
            }else{   
                _w_table_currenttd = $(this);   
                if(_w_table_firsttd.text()==_w_table_currenttd.text()){   
                    _w_table_SpanNum++;   
                    _w_table_currenttd.hide(); //remove();   
                    _w_table_firsttd.attr("colSpan",_w_table_SpanNum);   
                }else{   
                    _w_table_firsttd = $(this);   
                    _w_table_SpanNum = 1;   
                }   
            }   
        });   
    });   
}

function _idCardValidate(idCard){
	var Wi         = [ 7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2, 1 ];// 加权因子
	var ValideCode = [ 1, 0, 10, 9, 8, 7, 6, 5, 4, 3, 2 ];				        // 身份证验证位值.10代表X
	var sum        = 0;
	var isValidityBrith = function(year,month,day){
		var temp_date  = new Date(year,parseFloat(month)-1,parseFloat(day));
		if(year.length == 2){
			var temp_year = temp_date.getYear();
		}else if(year.length == 4){
			var temp_year = temp_date.getFullYear();
		}else{
			//alert("身份证的出生年份不正确");
			return false;
		}
		if(temp_year != parseFloat(year)   
	        || temp_date.getMonth() != parseFloat(month) - 1   
	        || temp_date.getDate() != parseFloat(day)){   
	        
			//alert("身份证的出生日期不正确");
			return false;
	    }else{
	        return true;
	    }
	}
	
	idCard = idCard.replace(/ /g, "").replace(/(^\s*)|(\s*$)/g, ""); 
	if(idCard.length == 15){
		var year  =  idCard.substring(6,8);   
	    var month = idCard.substring(8,10);   
	    var day   = idCard.substring(10,12); 
	    return isValidityBrith(year,month,day);
	}
	if(idCard.length != 18){
		//alert("身份证的长度不正确！");
		return false;
	}
	var a_idCard = idCard.split("");
	if (a_idCard[17].toLowerCase() == 'x') a_idCard[17] = 10;
	for ( var i = 0; i < 17; i++) {
		sum += Wi[i] * a_idCard[i];
	}
	valCodePosition = sum % 11;			// 得到验证码所在位置
	if (a_idCard[17] != ValideCode[valCodePosition]){ 
		//alert("身份证的验证位不正确！");
		return false;
	}
	var year  =  idCard.substring(6,10);   
    var month = idCard.substring(10,12);   
    var day   = idCard.substring(12,14);

    return isValidityBrith(year,month,day);
}

/**
 * 切换用户
 * @param targetUsername
 */
function switchSecurityTargetUser(targetUsername){	
	alertMsg.confirm("确定要切换为用户"+targetUsername+"?", {
		okCall: function(){
					window.location = baseUrl+'/j_spring_security_switch_user?j_username='+targetUsername;
		}
	});
}

/**
 * 切换原用户
 * @param originalUsername
 */
function switchSecurityOriginalUser(originalUsername){
	window.location = baseUrl+'/j_spring_security_exit_user?j_username='+originalUsername;
}

/**
 * 导出或下载文件
 * url 下载或导出链接
 * iframeId 临时创建的iframe的id
 */
function downloadFileByIframe(url,iframeId){
	$('#'+iframeId).remove();
	var iframe = document.createElement("iframe");
	iframe.id = iframeId;
	iframe.src = url;
	iframe.style.display = "none";	
//	var bgbar = $("#background,#progressBar").show();//显示:数据加载中，请稍等...
//	if (iframe.attachEvent){
//	    iframe.attachEvent("onload", function(){//加载完毕 IE
//	        bgbar.hide();
//	    });
//	} else {
//		iframe.addEventListener("load",function(){
//	        bgbar.hide();
//	    },false);
//	    iframe.onload = function(){
//	    	bgbar.hide();
//	    };
//	}
	
	document.body.appendChild(iframe);
	
}

/**
 * 导出或下载文件
 * url 下载或导出链接
 * iframeId 临时创建的iframe的id
 * post请求
 */
function downloadFileByIframe(url,iframeId,post){
	$('#'+iframeId).remove();
	var iframe = document.createElement("iframe");
	iframe.id = iframeId;
	iframe.src = url;
	iframe.style.display = "none";	
	iframe.method = "post";	
	document.body.appendChild(iframe);
	
}

/**
 * 下载系统附件
 * @param attid 附件id
 */
function downloadAttachFile(attid){
	var url = baseUrl+"/edu3/framework/filemanage/download.html?id="+attid;
	var elemIF = document.createElement("iframe");  
	elemIF.src = url;  
	elemIF.style.display = "none";  
	document.body.appendChild(elemIF); 
}


/**	jQuery json2form Plugin 
 *	version: 1.0 (2011-03-01)
 *
 * 	Copyright (c) 2011, Crystal, shimingxy@163.com
 * 	Dual licensed under the MIT and GPL licenses:
 * 	http://www.opensource.org/licenses/mit-license.php
 * 	http://www.gnu.org/licenses/gpl.html
 * 	Date: 2011-03-01 rev 1
 */
 ;(function ($) {
$.json2form = $.json2form||{};
$.fn.json2form = function(config ) { 
	var config=$.extend({
			url		:null,
			elem	:this.attr("id"),
			type	:'POST'
		}, config || {});

	if(config.url){
		$.ajax({type: config.type,url: config.url,data:$.extend({json2form:config.elem},config.data||{}),dataType: "json",async: false,
			success: function(data){
				config.data=data;
			}
		});
	}
	
	if(!$("#"+config.elem).attr("loadedInit")){//init checkbox radio and select element ,label
		if(config.data.init){
			for (var elem in config.data.init){
				var arrayData=config.data.init[elem];
				if($("#"+config.elem+" input[name='"+elem+"']")){
					var elemType=$("#"+config.elem+" input[name='"+elem+"']").attr("type");
					var elemName=$("#"+config.elem+" input[name='"+elem+"']").attr("name");
					var initElem=$("#"+config.elem+" input[name='"+elem+"']");
					switch(elemType){
						case "checkbox":
						case "radio":
							for (var initelem in arrayData){
								initElem.after('<input type="'+elemType+'"  name="'+elemName+'" value="'+arrayData[initelem].value+'" />'+arrayData[initelem].display);
							}
							initElem.remove();
							break;
					}
				} 
				if($("#"+config.elem+" select[name='"+elem+"']")){
					for (var initelem in arrayData){
						$("#"+config.elem+" select[name='"+elem+"']").append("<option value='"+arrayData[initelem].value+"'>"+arrayData[initelem].display+"</option>");
					}
				}
			}
		}
		if(config.data.label){//label
			$("#"+config.elem+" label").each(function(){
				var labelFor=$(this).attr("for");
				if(config.data.label[labelFor]){
					$(this).html(config.data.label[labelFor]);
				}
			});
		}
	}
	
	if(config.data){//input text password hidden button reset submit checkbox radio select textarea
		$("#"+config.elem).find("input,select,textarea,table.form").each(function(){
			var elemType=$(this).attr("type")==undefined?this.type:$(this).attr("type");
			if($(this).is("table")) elemType = "table";//主从表单的数据
			var elemName=$(this).attr("name");
			var elemData=config.data[elemName];
			if(!$("#"+config.elem).attr("loadedInit")&&$(this).attr("loadurl")){
				switch(elemType){
					case "checkbox":
					case "radio":
					case "select":
					case "select-one":
					case "select-multiple":{
						var _this =this;
						$.ajax({type: config.type,url: $(this).attr("loadurl"),dataType: "json",async: false,success: function(data){	
							if(elemType=="select"||elemType=="select-one"||elemType=="select-multiple"){
								$(_this).empty();
							}
							for (var elem in data){
									if(elemType=="select"||elemType=="select-one"||elemType=="select-multiple"){
										$(_this).append("<option value='"+data[elem].value+"'>"+data[elem].display+"</option>");
									}else{
										$(_this).after('<input type="'+elemType+'"  name="'+elemName+'" value="'+data[elem].value+'" />'+data[elem].display);
									}
								}
								if(elemType=="checkbox"||elemType=="radio")$(_this).remove();
							}
						});
						break;
					}
				}
			}			
			if(elemData){
				var invokeElemVal = function ($obj,elemType,elemData){
					switch(elemType){
						case undefined:
						case "text":
						case "password":
						case "hidden":
						case "button":
						case "reset":
						case "textarea":
						case "submit":{
							if(typeof(elemData)=="string"){
								$obj.val(elemData.toUpperCase()=="NULL"?"":elemData);
							}else{
								$obj.val(elemData+"");
							}
							break;
						}
						case "checkbox":
						case "radio":{
							$obj.attr("checked","");
							if(elemData.constructor==Array){//checkbox multiple value is Array
								for (var elem in elemData){
									if(elemData[elem]==$obj.val()){
										$obj.attr("checked",true);
									}
								}
							}else{//radio or checkbox is a string single value
								if(elemData==$obj.val()){
									$obj.attr("checked",true);
								}
							}
							break;
						}
						case "select":
						case "select-one":
						case "select-multiple":{
							$obj.find("option:selected").attr("selected",false);
							if(elemData.constructor==Array){
								for (var elem in elemData){
									$obj.find("option[value='"+elemData[elem]+"']").attr("selected",true);
								}
							}else{
								$obj.find("option[value='"+elemData+"']").attr("selected",true);
							}
							break;
						}
						case "table":{//主从表单的数据处理
							var $tr = $obj.find("thead tr");//隐藏的表单列表模板
							for(var i=0;i<elemData.length;i++){
								var $ctr = $tr.clone();//克隆一份模板并绑定数据
								var elemTr = elemData[i];
								for(var elem in elemTr){
									var $oj = $ctr.find("[name='"+elem+"']");
									invokeElemVal($oj,$oj.attr("type")==undefined?$oj.get(0).type:$oj.attr("type"),elemTr[elem]);
								}
								$ctr.find(":disabled").attr("disabled",false).end().find("q").text(i+1);//表单元素改为启用状态，加上序号
								$obj.append($ctr);
							}
						}
					}
				};
				invokeElemVal($(this),elemType,elemData);
			}
		});
	}

	$("#"+config.elem).attr("loadedInit","true");//loadedInit is true,next invoke not need init checkbox radio and select element ,label
};
})(jQuery);
 
/**
 * jquery关键字高亮 v1.1
 */
 (function($){ 
	$.fn.extend({
		highlight: function(strings) {
 			
 			function findText(node, string) {
			  	if (node.nodeType == 3)
			   		 return searchText(node, string);		
			  	else if (node.nodeType == 1 && node.childNodes && !(/(script|style)/i.test(node.tagName))) {
			   		for (var i = 0; i < node.childNodes.length; ++i) {
			    		i += findText(node.childNodes[i], string);
			   		}
			  	}
			  	return 0;
  	
 			}
 
		   	function searchText(node, string){
		  		var position = node.data.toUpperCase().indexOf(string);
		   		if (position >= 0)
		    		return highlight(node, position, string);
		    	else
		    		return 0;
		  	}
  	
		  	 function highlight(node, position, string){
		 		var spannode = document.createElement('span');
		    	spannode.className = 'highlight';
		    	var middlebit = node.splitText(position);
		    	var endbit = middlebit.splitText(string.length);
		    	var middleclone = middlebit.cloneNode(true);
		    	spannode.appendChild(middleclone);
		    	middlebit.parentNode.replaceChild(spannode, middlebit);
		 		return 1;
		 	}
 
			 return this.each(function() {
			 	if(typeof strings == 'string')
			 		findText(this, strings.toUpperCase());	
			 	else
			 		for (var i = 0; i < strings.length; ++i) findText(this, strings[i].toUpperCase());	
			 });
        }
    }); 
})(jQuery);
 
 /** 根据json信息获取所有有关联动查询的select信息,返回的结果是json格式字符串，
  * 如：{unitInfo:{selId:aaa,selName:bbb...},...}
 **/
function getSelectInfo(selectIdsJson){
	var selJsonIds = eval("("+selectIdsJson+")");
	var selectInfo = "";
	if(selJsonIds){
		if(selJsonIds.unitId){
			var unitInfo = getInfoById(selJsonIds.unitId);
			selectInfo += "{\"unitInfo\":"+unitInfo;
		}
		if(selJsonIds.gradeId){
			var gradeInfo = getInfoById(selJsonIds.gradeId);
			if(selectInfo){
				selectInfo += ",\"gradeInfo\":"+gradeInfo;
			}else {
				selectInfo += "{\"gradeInfo\":"+gradeInfo;
			}
		}
		if(selJsonIds.classicId){
			var classicInfo = getInfoById(selJsonIds.classicId);
			if(selectInfo){
				selectInfo += ",\"classicInfo\":"+classicInfo;
			}else {
				selectInfo += "{\"classicInfo\":"+classicInfo;
			}
		}
		if(selJsonIds.teachingType){
			var teachingTypeInfo = getInfoById(selJsonIds.teachingType);
			if(selectInfo){
				selectInfo += ",\"teachingTypeInfo\":"+teachingTypeInfo;
			}else {
				selectInfo += "{\"teachingTypeInfo\":"+teachingTypeInfo;
			}
		}
		if(selJsonIds.majorId){
			var majorInfo = getInfoById(selJsonIds.majorId);
			if(selectInfo){
				selectInfo += ",\"majorInfo\":"+majorInfo;
			}else {
				selectInfo += "{\"majorInfo\":"+majorInfo;
			}
		}
		if(selJsonIds.classesId){
			var classesInfo = getInfoById(selJsonIds.classesId);
			if(selectInfo){
				selectInfo += ",\"classesInfo\":"+classesInfo;
			}else {
				selectInfo += "{\"classesInfo\":"+classesInfo;
			}
		}
	}
	
	if(selectInfo){
		selectInfo += "}";
	}
	return selectInfo;
}

/** 根据id获取select有关信息 **/
function getInfoById(selId){
	var selInfo = "{\"selId\":\""+selId+"\"";
	var selSpan = $("span[sel-id='"+selId+"']");
	if(selSpan){
		var selName = selSpan.attr("sel-name");
		if(selName){
			selInfo += ",\"selName\":\""+selName+"\"";
		}
		var selOnchange = selSpan.attr("sel-onchange");
		if(selOnchange){
			selInfo += ",\"selOnchange\":\""+selOnchange+"\"";
		}
		var selClass = selSpan.attr("sel-classs");
		if(selClass){
			selInfo += ",\"selClass\":\""+selClass+"\"";
		}
		var selStyle = selSpan.attr("sel-style");
		if(selStyle){
			selInfo += ",\"selStyle\":\""+selStyle+"\"";
		}
		var selPlaceholder = selSpan.attr("sel-placeholder");
		if(selPlaceholder){
			selInfo += ",\"selPlaceholder\":\""+selPlaceholder+"\"";
		}
	}
	selInfo += "}";
	return selInfo;
}


/**
*联动查询
*参数分别是：operate(操作范围[begin,unit,grade,classic,teachingType,major,classes]，不能为空),defaultValue(教学点默认值，可以为空),
*schoolId(固定某个教学点，可以为空)，gradeId（年级，可以为空）,classicId（层次，可以为空）,
*teachingType（学习形式，可以为空）,majorId（专业，可以为空）,classesId（班级，可以为空）
*selectIdsJson是联动查询显示选项的ID值（json格式），
*key是固定的【教学点：unitId,年级：gradeId,层次：classicId,学习形式：teachingType,专业：majorId,班级：classesId】
*填写格式如：{unitId:aaa,gradeId:bbb,...}
**/
function cascadeQuery(operate,defaultValue,schoolId,gradeId,classicId,teachingType,majorId,classesId,selectIdsJson) {
	// 操作范围
	if(!operate || operate==null){
		operate = "";
	}
	// 默认教学点
	if(!defaultValue || defaultValue==null){
		defaultValue = "";
	}
	// 固定某个教学点
	if(!schoolId || schoolId==null){
		schoolId = "";
	}
	// 年级
	if(!gradeId || gradeId==null){
		gradeId = "";
	}
	// 层次
	if(!classicId || classicId==null){
		classicId = "";
	}
	// 学习形式
	if(!teachingType || teachingType==null){
		teachingType = "";
	}
	// 专业
	if(!majorId || majorId==null){
		majorId = "";
	}
	// 班级
	if(!classesId || classesId==null){
		classesId = "";
	}
	// 将json字符串转换为json对象（select对应的ID）
	var _jsonIds = eval("("+selectIdsJson+")");
	var selectInfo = getSelectInfo(selectIdsJson);
	var url = baseUrl+"/edu3/teaching/linkageQuery/cascadeQuery.html";
	$.ajax({
		type:'post',
		url:url,
		data:{operate:operate,defaultValue:defaultValue,schoolId:schoolId,gradeId:gradeId,classicId:classicId,teachingType:teachingType,majorId:majorId,classesId:classesId,selectInfo:selectInfo},
		dataType:"json",
		cache:false,
		error:DWZ.ajaxError,
		success:function(data){
			if(data['statusCode']==200){
				if(_jsonIds.unitId){
					 $("span[sel-id='"+_jsonIds.unitId+"']").html(data.unitOptions);
					 $("span[sel-id='"+_jsonIds.unitId+"']").attr("title",data.unitTitle);
				}
				if(_jsonIds.gradeId){
					$("span[sel-id='"+_jsonIds.gradeId+"']").html(data.gradeOptions);
				}
				if(_jsonIds.classicId){
					$("span[sel-id='"+_jsonIds.classicId+"']").html(data.classicOptions);
				}
				if(_jsonIds.teachingType){
					$("span[sel-id='"+_jsonIds.teachingType+"']").html(data.teachingTypeOptions);
				}
				if(_jsonIds.majorId){
					$("span[sel-id='"+_jsonIds.majorId+"']").html(data.majorOptions);
					//$("span[sel-id='"+_jsonIds.majorId+"']").attr("title",data.majorTitle);
				}
				if(_jsonIds.classesId){
					$("span[sel-id='"+_jsonIds.classesId+"']").html(data.classesOptions);
					$("span[sel-id='"+_jsonIds.classesId+"']").attr("title",data.classesTitle);
				}
				// 自动补全插件
				$("select.flexselect").flexselect({sortBy:'name'});
			}else{
				alertMsg.error(data['message']);
			}
		}
	});
	var ajaxbg = $("#background,#progressBar");
	ajaxbg.hide();
}

/**
 * 验证是否手机号码
 */
function isPhoneLegal(phone) {
	return isChinaPhoneLegal(phone) || isHKPhoneLegal(phone);  
}
/**
 * 验证是否大陆手机号码
 */
function isChinaPhoneLegal(phone) {
	var flag = true;
	if( !(/^((13[0-9])|(15[^4])|(18[0-9])|(17[0-8])|(14[1-8])|(166)|(19[8-9]))[0-9]{8}$/.test(phone))){
		flag = false;
	}
	return flag;
}
/**
 * 验证是否港澳手机号码
 */
function isHKPhoneLegal(phone) {
	var flag = true;
	if( !(/^(5|6|8|9)\\d{7}$/.test(phone))){
		flag = false;
	}
	return flag;
}

(function ($){
	this.version = '@1.3';
	this.layer = {'width' : 200, 'height': 100};
	this.title = '信息提示';
	this.time = 4000;
	this.anims = {'type' : 'slide', 'speed' : 600};
	this.timer1 = null;
	this.inits = function(title, text){
		if($("#message").is("div")){ return; }
		$(document.body).prepend('<div id="message" style="border:#b9c9ef 1px solid;z-index:100;width:'+this.layer.width+'px;height:'+this.layer.height+'px;position:absolute; display:none;background:#cfdef4; bottom:0; right:0; overflow:hidden;"><div style="border:1px solid #fff;border-bottom:none;width:100%;height:25px;font-size:12px;overflow:hidden;color:#1f336b;"><span id="message_close" style="float:right;padding:5px 0 5px 0;width:16px;line-height:auto;color:red;font-size:12px;font-weight:bold;text-align:center;cursor:pointer;overflow:hidden;">×</span><div style="padding:5px 0 5px 5px;width:100px;line-height:18px;text-align:left;overflow:hidden;">'+title+'</div><div style="clear:both;"></div></div> <div style="padding-bottom:5px;border:1px solid #fff;border-top:none;width:100%;height:auto;font-size:12px;"><div id="message_content" style="margin:0 5px 0 5px;border:#b9c9ef 1px solid;padding:10px 0 10px 5px;font-size:12px;width:'+(this.layer.width-17)+'px;height:'+(this.layer.height-50)+'px;color:#1f336b;text-align:left;overflow:hidden;">'+text+'</div></div></div>');
		$("#message_close").click(function(){		
			setTimeout('this.close()', 1);
		});
		$("#message").hover(function(){
			clearTimeout(timer1);
			timer1 = null;
		},function(){
			timer1 = setTimeout('this.close()', time);
			//alert(timer1);
		});
	};
	this.show = function(title, text, time){
		if($("#message").is("div")){ return; }
		if(title==0 || !title)title = this.title;
		this.inits(title, text);
		if(time>=0)this.time = time;
		switch(this.anims.type){
			case 'slide':$("#message").slideDown(this.anims.speed);break;
			case 'fade':$("#message").fadeIn(this.anims.speed);break;
			case 'show':$("#message").show(this.anims.speed);break;
			default:$("#message").slideDown(this.anims.speed);break;
		}
		if($.browser.is=='chrome'){
			setTimeout(function(){
				$("#message").remove();
				this.inits(title, text);
				$("#message").css("display","block");
			},this.anims.speed-(this.anims.speed/5));
		}
		//$("#message").slideDown('slow');
		this.rmmessage(this.time);
	};

	this.lays = function(width, height){
		if($("#message").is("div")){ return; }
		if(width!=0 && width)this.layer.width = width;
		if(height!=0 && height)this.layer.height = height;
	}
	this.anim = function(type,speed){
		if($("#message").is("div")){ return; }
		if(type!=0 && type)this.anims.type = type;
		if(speed!=0 && speed){
			switch(speed){
				case 'slow' : ;break;
				case 'fast' : this.anims.speed = 200; break;
				case 'normal' : this.anims.speed = 400; break;
				default:					
					this.anims.speed = speed;
			}			
		}
	}
	this.rmmessage = function(time){
		if(time>0){
			timer1 = setTimeout('this.close()', time);
			//setTimeout('$("#message").remove()', time+1000);
		}
	};
	this.close = function(){
		switch(this.anims.type){
			case 'slide':$("#message").slideUp(this.anims.speed);break;
			case 'fade':$("#message").fadeOut(this.anims.speed);break;
			case 'show':$("#message").hide(this.anims.speed);break;
			default:$("#message").slideUp(this.anims.speed);break;
		};
		setTimeout('$("#message").remove();', this.anims.speed);
		this.original();	
	}

	this.original = function(){	
		this.layer = {'width' : 200, 'height': 100};
		this.title = '信息提示';
		this.time = 4000;
		this.anims = {'type' : 'slide', 'speed' : 600};

	};
    jQuery.messager = this;
    return jQuery;
});