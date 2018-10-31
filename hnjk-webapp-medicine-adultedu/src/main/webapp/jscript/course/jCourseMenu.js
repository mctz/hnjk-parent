(function($){
	$.fn.extend({jCourseMenu:function(options){
			var op = {level1Li:"li.level1",level1A:"li.level1 > h4",level2Ul:"ul.level2",level2A:"ul.level2 > li > h5",
					level3Ul:"ul.level3",level3A:"ul.level3 > li >a",
					hoveClass:"hove",hoveClass1:"hove1",hoveClass2:"hove2",curClass:"cur",curClass1:"cur1",curClass2:"cur2"};
			var state1,state2,state3;	
			return this.each(function(){
				var $this = $(this);
				$this.find(op.level1A).each(function(n){					
					$(this).mouseover(function (){
						if (state1!=n){	    
							$(this).addClass(op.hoveClass);					
					    }
					}).mouseout(function (){
						if (state1!=n){	    
							$(this).removeClass(op.hoveClass);					
					    }
					}).click(function (){
						//要判断是否有子菜单项
						var objUl=$(this).siblings(op.level2Ul);
						if (state1!=n){								
						    if(objUl.length>0){//如果存在二级菜单项，显示当前点击的二级菜单
						    	$this.find(op.level2Ul).hide();//隐藏所有二级菜单
						    	objUl.show();
						    	_showJMenu(objUl);
							}		
					        temp=n;
					    }
						$this.find(op.level1A).removeClass(op.hoveClass).removeClass(op.curClass);
						$(this).addClass(op.curClass);
					}).focus(function (){
						this.blur();
					});
				});
				//二级菜单
				$this.find(op.level2A).each(function(n){
					$(this).mouseover(function (){
						if (state2!=n){	    
							$(this).addClass(op.hoveClass1);					
					    }
					}).mouseout(function (){
						if (state2!=n){	    
							$(this).removeClass(op.hoveClass1);					
					    }
					}).click(function (){
						if (state2!=n){
							$this.find(op.level2A).removeClass(op.curClass1);
							state2=n;		
						    $(this).addClass(op.curClass1);
						}
					}).focus(function (){
						this.blur();
					});
				});
				//三级菜单
				$this.find(op.level3A).each(function(n){
					$(this).mouseover(function (){
						if (state3!=n){	    
							$(this).addClass(op.hoveClass2);					
					    }
					}).mouseout(function (){
						if (state3!=n){	    
							$(this).removeClass(op.hoveClass2);					
					    }
					}).focus(function (){
						this.blur();
					});
				});
			});	
			
			function _showJMenu(obj){
				var allhight=obj.find("li").length*26;//26为每个菜单项的高度，用来计算二级菜单的总高度。
				obj.height(1);
				var changeW=function(){ 	 		
				  var obj_h=obj.height();
				  if(obj_h<=allhight){ 
					  obj.height(obj_h+Math.ceil((allhight-obj_h)/10));
				  } else{ 
					  clearInterval(bw1);
				  }
			    };       
			    bw1= setInterval(changeW,40);//时间开关
			}
		}
	});
})(jQuery);