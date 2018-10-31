
var ImagePreview = function(file, img, options) {	
	this.file = $$(file);
	this.img = $$(img);	
	this._preload = null;
	this._data = "";
	this._upload = null;	
	var opt = this._setOptions(options);
	this.action = opt.action;
	this.timeout = opt.timeout;
	this.ratio = opt.ratio;
	this.maxWidth = opt.maxWidth;
	this.maxHeight = opt.maxHeight;
	this.onCheck = opt.onCheck;
	this.onShow = opt.onShow;
	this.onErr = opt.onErr;
	this._getData = this._getDataFun(opt.mode);
	this._show = opt.mode !== "filter" ? this._simpleShow : this._filterShow;
};

ImagePreview.MODE = $$B.ie7 || $$B.ie8 ? "filter" :
	$$B.firefox ? "domfile" :
	$$B.opera || $$B.chrome || $$B.safari ? "remote" : "simple";

ImagePreview.TRANSPARENT = $$B.ie7 || $$B.ie6 ?
	"mhtml:" + document.scripts[document.scripts.length - 1].getAttribute("src", 4) + "!blankImage" :
	"data:image/gif;base64,R0lGODlhAQABAIAAAP///wAAACH5BAEAAAAALAAAAAABAAEAAAICRAEAOw==";

ImagePreview.prototype = {
	_setOptions: function(options) {
    this.options = {
    	mode:		ImagePreview.MODE,
    	ratio:		0,		
    	maxWidth:	0,
    	maxHeight:	0,
    	onCheck:	function(){},
    	onShow:		function(){},
    	onErr:		function(){},
    	action:		undefined,
		timeout:	0
		};
    return $$.extend(this.options, options || {});
  },

  preview: function() {
	if ( this.file && false !== this.onCheck() ) {
		this._preview( this._getData() );
	}
  },
 
  _getDataFun: function(mode) {
	switch (mode) {
		case "filter" :
			return this._filterData;
		case "domfile" :
			return this._domfileData;
		case "remote" :
			return this._remoteData;
		case "simple" :
		default :
			return this._simpleData;}
  },
  _filterData: function() {
	this.file.select();
	try{
		return document.selection.createRange().text;
	} finally { document.selection.empty(); }
  },
  _domfileData: function() {
	return this.file.files[0].getAsDataURL();
  },
  _remoteData: function() {
	this._setUpload();
	this._upload && this._upload.upload();
  },
  _simpleData: function() {
	return this.file.value;
  },
  
  _setUpload: function() {
	if ( !this._upload && this.action !== undefined && typeof QuickUpload === "function" ) {
		var oThis = this;
		this._upload = new QuickUpload(this.file, {
			onReady: function(){
				this.action = oThis.action; this.timeout = oThis.timeout;
				var parameter = this.parameter;
				parameter.ratio = oThis.ratio;
				parameter.width = oThis.maxWidth;
				parameter.height = oThis.maxHeight;
			},
			onFinish: function(iframe){
				try{
					oThis._preview( iframe.contentWindow.document.body.innerHTML );
				}catch(e){ oThis._error("remote error"); }
			},
			onTimeout: function(){ oThis._error("timeout error"); }
		});
	}
  }, 
  _preview: function(data) {
	if ( !!data && data !== this._data ) {
		this._data = data; this._show();
	}
  },
  _simplePreload: function() {
	if ( !this._preload ) {
		var preload = this._preload = new Image(), oThis = this;
		preload.onload = function(){ oThis._imgShow( oThis._data, this.width, this.height ); };
		preload.onerror = function(){ oThis._error(); };
	}
  },
  _simpleShow: function() {
	this._simplePreload();
	this._preload.src = this._data;
  },

  _filterPreload: function() {
	if ( !this._preload ) {
		var preload = this._preload = document.createElement("div");	
		$$D.setStyle( preload, {
			width: "1px", height: "1px",
			visibility: "hidden", position: "absolute", left: "-9999px", top: "-9999px",
			filter: "progid:DXImageTransform.Microsoft.AlphaImageLoader(sizingMethod='image')"
		});
		
		var body = document.body; body.insertBefore( preload, body.childNodes[0] );
	}
  },

  _filterShow: function() {
	this._filterPreload();
	var preload = this._preload, data = this._data;
	try{
		preload.filters.item("DXImageTransform.Microsoft.AlphaImageLoader").src = data;
	}catch(e){ this._error("filter error"); return; }

	this.img.style.filter = "progid:DXImageTransform.Microsoft.AlphaImageLoader(sizingMethod='scale',src='" + data + "')";
	this._imgShow( ImagePreview.TRANSPARENT, preload.offsetWidth, preload.offsetHeight );
  },
  _imgShow: function(src, width, height) {
	var img = this.img, style = img.style,
		ratio = Math.max( 0, this.ratio ) || Math.min( 1,
				Math.max( 0, this.maxWidth ) / width  || 1,
				Math.max( 0, this.maxHeight ) / height || 1
			);

	style.width = Math.round( width * ratio ) + "px";
	style.height = Math.round( height * ratio ) + "px";

	img.src = src;
	this.onShow();
  },
  dispose: function() {
  	if ( this._upload ) {
		this._upload.dispose(); this._upload = null;
	}
	if ( this._preload ) {
		var preload = this._preload, parent = preload.parentNode;
		this._preload = preload.onload = preload.onerror = null;
		parent && parent.removeChild(preload);
	}
	
	this.file = this.img = null;
  },

_error: function(err) {
	this.onErr(err);
  }
}