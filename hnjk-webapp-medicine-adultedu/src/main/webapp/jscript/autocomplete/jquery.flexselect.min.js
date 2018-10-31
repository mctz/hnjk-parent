(function(a){
	a.flexselect=function(b,c){this.init(b,c)};
	a.extend(a.flexselect.prototype,{
		settings:{
			allowMismatch:false,selectedClass:"flexselect_selected",dropdownClass:"flexselect_dropdown",
			inputIdTransform:function(b){return b+"_flexselect"},
			inputNameTransform:function(b){return},
			dropdownIdTransform:function(b){return b+"_flexselect_dropdown"}
		},
		select:null,input:null,hidden:null,dropdown:null,dropdownList:null,cache:[],results:[],lastAbbreviation:null,abbreviationBeforeFocus:null,selectedIndex:0,picked:false,dropdownMouseover:false,
		init:function(b,c){
			a.extend(this.settings,c);this.select=a(b);this.preloadCache();this.renderControls();this.wire()
		},preloadCache:function(){
			this.cache=this.select.children("option").map(function(){
				return{name:a.trim(a(this).text()),value:a(this).val(),score:0}
			})
		},renderControls:function(){
			var b=this.select.children("option:selected");
			/*console.log("----------------------------------------------------");
			console.log("b:"+b.val());*/
			this.hidden=a("<input type='hidden' />").attr({
				id:this.select.attr("id"),name:this.select.attr("name")
			}).addClass(this.select.attr("class")).val(b.val().replace(/<[^>].*?>/g,""));
			this.input=a("<input type='text' autocomplete='off' />").attr({
				id:this.settings.inputIdTransform(this.select.attr("id")),name:this.settings.inputNameTransform(this.select.attr("name")),
				accesskey:this.select.attr("accesskey"),tabindex:this.select.attr("tabindex"),style:this.select.attr("style")
				
			}).addClass(this.select.attr("class")).val(a.trim(b.text().replace(/<[^>].*?>/g,"")));
			this.dropdown=a("<div></div>").attr({id:this.settings.dropdownIdTransform(this.select.attr("id"))}).addClass(this.settings.dropdownClass);
			this.dropdownList=a("<ul></ul>");
			this.dropdown.append(this.dropdownList);
			this.select.after(this.input).after(this.hidden).remove();
			a("body").append(this.dropdown)
		},wire:function(selector){
			var b=this;this.input.click(function(){
				b.lastAbbreviation=null;b.focus()
			});
			this.input.mouseup(function(c){c.preventDefault()});
			this.input.focus(function(){b.abbreviationBeforeFocus=b.input.val();
			b.input.select();if(!b.picked){b.filterResults()}});
			this.input.blur(function(){if(!b.dropdownMouseover){b.hide();if(!b.picked){b.reset()}}});
			this.dropdownList.mouseover(function(c){if(c.target.tagName=="LI"){var d=b.dropdown.find("li");b.markSelected(d.index(a(c.target)))}});
			this.dropdownList.mouseleave(function(){b.markSelected(-1)});
			this.dropdownList.mouseup(function(c){b.pickSelected();b.select.change();b.focusAndHide();});
			this.dropdown.mouseover(function(c){b.dropdownMouseover=true});
			this.dropdown.mouseleave(function(c){b.dropdownMouseover=false});
			this.dropdown.mousedown(function(c){c.preventDefault()});
			this.input.keyup(function(c){switch(c.keyCode){case 13:c.preventDefault();b.pickSelected();b.select.change();b.focusAndHide();break;case 27:c.preventDefault();b.reset();b.focusAndHide();break;default:b.filterResults();break}});
			this.input.keydown(function(c){
				switch(c.keyCode){case 9:b.pickSelected();b.select.change();b.hide();break;case 33:c.preventDefault();b.markFirst();break;case 34:c.preventDefault();b.markLast();break;case 38:c.preventDefault();b.moveSelected(-1);break;case 40:c.preventDefault();b.moveSelected(1);break;case 13:case 27:c.preventDefault();c.stopPropagation();break}
			})
		},filterResults:function(){
			var c=this.input.val();if(c==this.lastAbbreviation){return}var b=[];a.each(this.cache,function(){this.score=LiquidMetal.score(this.name,c);if(this.score>0){b.push(this)}});this.results=b;this.sortResults();this.renderDropdown();this.markFirst();this.lastAbbreviation=c;this.picked=false
		},sortResults:function(){
			this.results.sort(function(d,c){return c.score-d.score})
		},renderDropdown:function(){
			var c=this.dropdown.outerWidth()-this.dropdown.innerWidth();var b=this.input.offset();
			this.dropdown.css({width:(this.input.outerWidth()-c)+"px",top:(b.top+this.input.outerHeight())+"px",left:b.left+"px"});var d=this.dropdownList.html("");a.each(this.results,function(){var e=a("<li/>");if(a.trim(this.name)==""){e.css({height:"20px"})}d.append(e.html(this.name))});this.dropdown.show()
		},markSelected:function(c){if(c>this.results.length){return}var b=this.dropdown.find("li");b.removeClass(this.settings.selectedClass);this.selectedIndex=c;if(c>=0){a(b[c]).addClass(this.settings.selectedClass)}}
		,pickSelected:function(){var b=this.results[this.selectedIndex];if(b){this.input.val(b.name.replace(/<[^>].*?>/g,""));this.hidden.val(b.value.replace(/<[^>].*?>/g,""));this.picked=true}else{if(this.settings.allowMismatch){this.hidden.val("")}else{this.reset()}}}
		,hide:function(){this.dropdown.hide();this.lastAbbreviation=null}
		,moveSelected:function(b){this.markSelected(this.selectedIndex+b)}
		,markFirst:function(){this.markSelected(0)}
		,markLast:function(){this.markSelected(this.results.length-1)}
		,reset:function(){this.input.val(this.abbreviationBeforeFocus.replace(/<[^>].*?>/g,""))}
		,focus:function(){this.input.focus()}
		,focusAndHide:function(){this.focus();this.hide()}
	});
	a.fn.flexselect=function(b){this.each(function(){if(this.tagName=="SELECT"){new a.flexselect(this,b)}});return this}
})(jQuery);
var LiquidMetal=function(){
	var c=0;var h=1;var f=0.8;var e=0.9;var a=0.85;
	return{
		score:function(j,n){
			if(n.length==0){return f}if(n.length>j.length){return c}var m=this.buildScoreArray(j,n);var l=0;for(var k in m){l+=m[k]}return(l/m.length)
		},buildScoreArray:function(o,t){var k=new Array(o.length);var m=o.toLowerCase();var s=t.toLowerCase().split("");var n=-1;var q=false;for(var l in s){var r=s[l];var p=m.indexOf(r,n+1);if(p<0){return d(k,c)}if(p==0){q=true}if(g(o,p)){k[p-1]=1;d(k,a,n+1,p-1)}else{if(b(o,p)){d(k,a,n+1,p)}else{d(k,c,n+1,p)}}k[p]=h;n=p}var j=q?e:f;d(k,j,n+1);return k}
	};
	function b(j,i){var k=j.charAt(i);return("A"<=k&&k<="Z")}function g(j,i){var k=j.charAt(i-1);return(k==" "||k=="\t")}function d(n,k,m,l){m=Math.max(m||0,0);l=Math.min(l||n.length,n.length);for(var j=m;j<l;j++){n[j]=k}return n}
}();
				