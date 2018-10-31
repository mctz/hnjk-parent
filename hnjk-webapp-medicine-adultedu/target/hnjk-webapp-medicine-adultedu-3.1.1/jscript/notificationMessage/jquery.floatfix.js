/*
 * jQuery floatFix plugin
 * Copyright 2009 (pkmaster|pk4321), http://www.lxasp.com
 * Date: 2009-8-23
 * Tested on jQuery v1.3.2
 */
;(function($) {
$.floatFix = $.floatFix || {version:'0.8.15'};
var floatFix = function(dom,opts) { 
	var me=$(dom);
	// public methods
	$.extend(this, {show: function() {showMine();},hide: function() {closeDiv();},toMin: function() {minDiv();},toMax: function() {maxDiv();},
		options: function() {
			return opts;
		}
	});
	
	var meMin = opts.minDomNode ? $(opts.minDomNode) : $('#'+me.attr('id')+opts.minPostfix);
	var btnmax,btnmin,btncls,
		hiding=false,showing=false,bMin = (meMin.length>0) ? true : false ;	
	function init(){
		$.each( [ me , meMin ], function(i, n){
			if (n.length>0) {
			n.hide();
			n.css({'position':'absolute','z-index':n.css('z-index')||opts.zIndex});			
			btnmax=$(opts.buttonMax, n );
			btnmin=$(opts.buttonMin, n );
			btncls=$(opts.buttonCls, n );			
			var c='click';
			btnmax.bind(c,null,maxDiv);
			btnmin.bind(c,null,minDiv);
			btncls.bind(c,null,closeDiv);
			}
		});
		if(bMin){
			meMin.hover(
				function(){ if(opts.hoverTurn)maxDiv(); },
				function(){}
			);
			me.hover(
				function(){},
				function(){ if(opts.hoverTurn)minDiv(); }
			);
		}
		if (opts.autoShow)showMine();
	
	};init();

	function moveAllMine(){
		moveToCorner(me);moveToCorner(meMin);
	}

	function unshow(){
		var w=$(window);
		w.unbind("resize", moveAllMine);
		w.unbind("scroll", moveAllMine);
		if (bMin) meMin.hide();
		me.hide();
	}
	
	function showMine(){
		var el,iz=opts.initSize;
		if (showing){
			unshow();
			showing=false;
		};
		if (iz=='min'){el=meMin}
		else if (iz=='max'||iz){el=me}
		if (el.length>0){
			if (opts.onBeforeShow(me,meMin,opts)){
				var w=$(window);
				w.bind("resize",null,moveAllMine);
				w.bind("scroll",null,moveAllMine);
				moveAllMine();
				opts.onAnimate(el,opts);
				showing=true;
				hiding=false;
				opts.onShow(me,meMin,opts);
			}
		};
	}
	//move to each 9 position !important
	function moveToCorner(el){
		if (el.length>0){
			var w = $(window),
				divWidth = el.outerWidth(),
				divHeight = el.outerHeight(),
				docHeight = w.height(),
				docWidth = w.width();

			var st= parseInt(document.body.scrollTop,10) || parseInt(document.documentElement.scrollTop,10),
				sl= parseInt(document.body.scrollLeft,10) || parseInt(document.documentElement.scrollLeft,10);

			var t7=st + opts.offsetTop,
				l7=sl + opts.offsetLeft,
				t3=docHeight - divHeight + t7,
				l3=docWidth - divWidth + l7,
				t5=parseInt(docHeight/2) - parseInt(divHeight/2) + t7,
				l5=parseInt(docWidth/2) - parseInt(divWidth/2) + l7,
				t,l

			switch(opts.position){
			case 7:
				t=t7;l=l7;
				break;

			case 8:
				t=t7;l=l5;
				break;

			case 9:
				t=t7;l=l3;
				break;

			case 4:
				t=t5;l=l7;
				break;

			case 5:
				t=t5;l=l5;
				break;

			case 6:
				t=t5;l=l3;
				break;

			case 1:
				t=t3;l=l7;
				break;

			case 2:
				t=t3;l=l5;
				break;
			
			case 3:	;
			default:
				t=t3;l=l3;
				break;
			}
			el.css({'top': t, 'left': l});
		}
	}
	function minDiv()
	{
		if (!hiding){
			if (bMin){
				me.hide();
				opts.onAnimate(meMin,opts);
				opts.onMin(me,meMin,opts);
			}
			else {
				hiding=true;
				unshow();
				showing=false;
			}
		}
	}
	function maxDiv()
	{
		if (!hiding){
			if (bMin) meMin.hide();
			opts.onAnimate(me,opts);
			opts.onMax(me,meMin,opts);
		}
	}
	function closeDiv()
	{
		if (opts.onBeforeHide(me,meMin,opts)){
			hiding=true;
			unshow();
			showing=false;
			opts.onHide(me,meMin,opts);
		}
	}



};//--]floatfix


$.fn.floatFix = function(conf) {

	// return existing instance
	var el = this.eq(typeof conf == 'number' ? conf : 0).data("floatFix");
	if (el) { return el; }

	// setup options
	var opts = {
		fx:{
			open:       'show', // can be 'show' or 'slideDown' or 'fadeIn'
			openSpeed:  ''
		},
		minPostfix:'_min',
		minDomNode:'',
		buttonMax:'.floatfix_max',
		buttonMin:'.floatfix_min',
		buttonCls:'.floatfix_cls',
		offsetLeft:0,
		offsetTop:0,
		position:3, //Settings like NUMBER KEYBOARD
		initSize:'max', //true=='max' / 'min'
		autoShow:true,
		hoverTurn:false,
		zIndex:9999,
		opacity:1,
		onBeforeShow:function(e,m,o){return true},
		onShow:function(e,m,o){},
		onBeforeHide:function(e,m,o){return true},
		onHide:function(e,m,o){},
		onMax:function(e,m,o){},
		onMin:function(e,m,o){},
		onAnimate:function(el,opts){
			el.hide()[opts.fx.open](opts.fx.openSpeed);
			el.css({opacity:opts.opacity});
		},
		api:false
	};

	$.extend(opts, conf);

	// install the plugin for each items in jQuery
	this.each(function() {
		el = new floatFix(this, opts);
		$(this).data("floatFix", el);
	});
	
	//api=true let users can immediate use the Public Methods
	return opts.api ? el: this;
};
	
})(jQuery);//--]jq plugin container
