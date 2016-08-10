var Preview={
	width:70,
	init:function(){
		 this.setEvents();
		 this.loadImage();
		 $('.preview-body').data('w',this.width);
	},
	setEvents:function(){
		var me =this;
		$('#vertical').click(function(){
			$('.preview-body img').removeClass('horizontal').addClass('vertical');
		});
		$('#horizontal').click(function(){
			$('.preview-body img').removeClass('vertical').addClass('horizontal');
			
		});
		$('#min').click(function(){
			var w=$('.preview-body').data('w')-10;
			$('.preview-body').data('w',w);
			$('.preview-body img').css({
				width:w+"%"
			});
		});
		$('#max').click(function(){
			
			var w=$('.preview-body').data('w')+10;
			$('.preview-body').data('w',w);
			$('.preview-body img').css({
				width:w+"%"
			});
		});
		$('#reduction').click(function(){
			$('.preview-body img').css({
				width:me.width+"%"
			});
			$('.preview-body').data('w',me.width);
		});
	},
	loadImage:function(){
		var me =this;
		var tip=showTips({
			content: "<h2>提示信息</h2><p>正在加载...！</p>",
	        autoClose: false
		});
		$.ajax({
			type:'post',
            url: "/online-preview/preview-img.html",
            data : $.extend({
						"timed" : new Date().getTime()
					}, {
						fileId : fileId,
						relativePath:relativePath,
						fileName:fileName
					}),
            dataType: "json",
            timeout: 5*60*1000,
            error: function (XMLHttpRequest, textStatus, errorThrown) {
            	$('#preview-body').html("加载失败...");
            	tip.hide();
             },
            success: function (data, textStatus) {
            	if(data.success){
            		$.each(data.data,function(i,d){
	            		$('#preview-body').append('<img class="vertical"  alt="" src="'+d.imgPath+'">');
	            	});
            	}else{
            		$('#preview-body').html(data.message);
            	}
            	tip.hide();
            }
        });
	
	}
};
$(function(){
	Preview.init();
})