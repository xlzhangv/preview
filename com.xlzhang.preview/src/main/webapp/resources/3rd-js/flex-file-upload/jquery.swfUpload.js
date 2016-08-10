/**
 * @license jQuery swfUpload plugin v1.0.0 09/12/2010
 *          
 * params:{
 * 			label:'上传',
 * 			autoUpload':false//是否自动上传,
 * 			maxFileSize:最大字节数,
 			url:'',//上传请求
 			otherParams:{}//附加参数json对象
 * },
 * fileFilters:[{
 * 		describe:描述，
 * 		extension:'*.jpg;*.gif;*.png'
 * }]
 * 	onError
 * 错误代码:
 * 		-1 未选择文件
 * 		1 文件类型不匹配
 * 		2 上传文件大小超过限制
 *
 *     <b>Styles样式信息</b>
 *    borderColor:"0xAAB3B3"
 *    color:"0x0B333C"
 *    cornerRadius:"4"
 *    disabledColor:"0xAAB3B3"
 *    disabledIcon:"null"
 *    disabledSkin:"mx.skins.halo.ButtonSkin"
 *    downIcon:"null"
 *    downSkin:"mx.skins.halo.ButtonSkin"
 *    fillAlphas:"[0.6, 0.4]"
 *    fillColors:"[0xE6EEEE, 0xFFFFFF]"
 *    focusAlpha:"0.5"
 *    focusRoundedCorners"tl tr bl br"
 *    fontAntiAliasType:"advanced"
 *    fontFamily:"Verdana"
 *    fontGridFitType:"pixel"
 *    fontSharpness:"0"
 *    fontSize:"10"
 *    fontStyle:"normal|italic"
 *    fontThickness:"0"
 *    fontWeight:"bold|normal"
 *    highlightAlphas:"[0.3, 0.0]"
 *    horizontalGap:"2"
 *    icon:"null"
 *    kerning:"false|true"
 *    leading:"2"
 *    letterSpacing:"0"
 *    overIcon:"null"
 *    overSkin:"mx.skins.halo.ButtonSkin"
 *    paddingBottom:"0"
 *    paddingLeft:"0"
 *    paddingRight:"0"
 *    paddingTop:"0"
 *    repeatDelay:"500"
 *    repeatInterval:"35"
 *    selectedDisabledIcon:"null"
 *    selectedDisabledSkin:"mx.skins.halo.ButtonSkin"
 *    selectedDownIcon:"null"
 *    selectedDownSkin:"mx.skins.halo.ButtonSkin"
 *    selectedOverIcon:"null"
 *    selectedOverSkin:"mx.skins.halo.ButtonSkin"
 *    selectedUpIcon:"null"
 *    selectedUpSkin:"mx.skins.halo.ButtonSkin"
 *    skin:"mx.skins.halo.ButtonSkin"
 *    textAlign:"center|left|right"
 *    textDecoration:"none|underline"
 *    textIndent:"0"
 *    textRollOverColor:"0x2B333C"
 *    textSelectedColor:"0x000000"
 *    upIcon:"null"
 *    upSkin:"mx.skins.halo.ButtonSkin"
 *    verticalGap:"2"
 *
 */

(function($) {
	$.fn.swfUpload = function(options) {
		var me = this;
		// 拷贝对象
		function objCopy(result, source) {
			for ( var key in source) {
				var copy = source[key];
				if (source === copy)
					continue;// 如window.window === window，会陷入死循环，需要处理一下
				if (is(copy, "Object")) {
					result[key] = arguments.callee(result[key] || {}, copy);
				} else if (is(copy, "Array")) {
					result[key] = arguments.callee(result[key] || [], copy);
				} else {
					result[key] = copy;
				}
			}

			return result;

			function is(obj, type) {
				var toString = Object.prototype.toString;
				return (type === "Null" && obj === null)
						|| (type === "Undefined" && obj === undefined)
						|| toString.call(obj).slice(8, -1) === type;
			}

		}
		var $swfUpload = {
			$element : me,
			"width" : '100%',
			"height" : '100%',
			"swffile" : "FileUpload.swf",
			"quality" : 85,
			"params" : {},
			"btnStyle" : undefined,
			"fileFilters" : undefined,
			"debug" : function() {
			},
			"onError" : function() {
			},
			'onLoad' : function() {
			},
			"onSubmitUpload" : function() {
			},
			"onSelect" : function() {
			},
			"onProgress" : function() {
			},
			'onSuccess' : function() {
			}
		};
		me.$swfUpload=$swfUpload;
		if (typeof options === "object") {
			$swfUpload=objCopy($swfUpload,options);
			$swfUpload['isReady'] = function() {
				return true;
			}
		}
		window["$swfUpload"] = $swfUpload;
		this.css({
			position : 'relative'
		});
		var getObjectId = function() {
			if (!me.objectId) {
				me.objectId = 'XwebcamXobjectX' + new Date().getTime()
						+ Math.round(Math.random() * 1222000);
			}
			return me.objectId;
		}
		
		var buildFlash = function() {
			var _params = "";
			for ( var p in $swfUpload.params) {
				_params += p + "=" + $swfUpload.params[p] + "&"
			}
			_params = _params.substring(0, _params.length - 1);
			var source = '<object id="' + getObjectId()
					+ '" type="application/x-shockwave-flash" data="'
					+ $swfUpload["swffile"] + '" width="' + $swfUpload["width"]
					+ '" height="' + $swfUpload["height"] + '">'
					+ '<param name="movie" value="' + $swfUpload["swffile"]
					+ '" />' + '<param name="allowFullScreen" value="true" />'
					+ '<param name="FlashVars" value="' + _params + '" />'
					+ '<param name="allowScriptAccess" value="always" />'
					+ '</object>';
			me.empty();
			me.loadDiv = $('<div>');
			me.loadDiv.css({
				height : $swfUpload["height"],
				width : $swfUpload["width"],
				position : 'absolute',
				'background-color' : '#B2B5B7'
			});
			me.append(me.loadDiv);
			me.append(source);

		}

		me.bindEvents = function() {
			if(me.intervalId){
				clearInterval(me.intervalId);
			}
			
			buildFlash();
			// 搭建js与flash互通的环境
			function thisMovie(movieName) {
				if (navigator.appName.indexOf("Microsoft") != -1) {
					return window[movieName];
				} else {
					return document[movieName];
				}
			}

			function loadSwf() {
				var cam = thisMovie(me.objectId);
				if (cam && cam["isLoadFinish"]) {
					if (cam["isLoadFinish"]()) {
						clearInterval(me.intervalId);
						me.loadDiv.hide();
					}

					/* Simple callback methods are not allowed :-/ */
					me["onSubmitUpload"] = function() {
						cam.submitUpload();
					}
					if ($swfUpload.btnStyle) {
						cam.setBtnStyle($swfUpload.btnStyle);
					}
					if ($swfUpload.fileFilters) {
						cam.setFileFilter($swfUpload.fileFilters);
					}
					if($swfUpload.params.otherParams){
						cam.setOtherParams($swfUpload.params.otherParams);
					}
					cam['initUrl']($swfUpload.params.url);
					$swfUpload["onLoad"](me, cam);
				}
			}
			me.intervalId = setInterval(function() {
				try {
					loadSwf();
				} catch (e) {
					console.log(e);
				}
			}, 10);
		}
		me.bindEvents();
		return me;
	}
})(jQuery);
