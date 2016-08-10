function showTips(config) {
    //	mini-tips-danger
    //	mini-tips-success
    var c = {
        content: "<h2>提示信息</h2><p>成功！</p>",
        state: 'success',
        autoClose: true,
        x: 'center',
        y: 'center',
        timeout: 10 * 1000,
        open: function (e, c) {
            var i = c.timeout / 1000;

            function showTime() {
                e.find('.bottom').html('剩余时间' + i);
                i--;
                if (i >= 0) {
                    setTimeout(function () {
                        showTime();
                    }, 1000);
                }
            }
			if(c.autoClose){
				 showTime();
			}
        },
        close: function (e) {

        }
    };
    var nc = {};
    $.extend(true, nc, c, config);
    var windowWidth = document.documentElement.clientWidth;
    var windowHeight = document.documentElement.clientHeight;
    var tipClass = 'mini-tips-' + nc.state;
    var $tipsDiv = $('<div class=' + tipClass + '>' + nc.content
        + '<div class="bottom"></div></div>');
    var $modal = $('<div style="position: fixed;top:0;left:0;height: 100%;width: 100%;z-index: 99;background: #888;opacity: 0.7;"></div>');
    $('body').append($tipsDiv);
    $('body').append($modal);
    var top, left;
    left = '557px';
    //	left=((windowWidth / 2)-($tipsDiv.width()/2))+"px";
    console.info((windowWidth / 2) + '---' + $tipsDiv.width());
    $tipsDiv.css({
        'left': '42%',
        'top': '42%',
        'position': 'fixed',
        'padding': '3px 5px',
        'font-size': 12 + 'px',
        'z-index': '9999',
        'margin': '0 auto',
        'text-align': 'center',
        'width': 'auto'
    }).show("slow", function () {
        nc.open($tipsDiv, nc);
    });
    $tipsDiv.hide = function () {
        $tipsDiv.remove();
        $modal.remove();
    }
    if (c.autoClose) {
        setTimeout(function () {
            $tipsDiv.fadeOut("fast", function () {
                nc.close($tipsDiv, nc);
                $tipsDiv.remove();
                $modal.remove();
            });
        }, (nc.timeout));
    }

    return $tipsDiv;

}
//var template1="我是{0}，今年{1}了";
// var template2="我是{name}，今年{age}了";
// var result1=template1.format("loogn",22);
// var result2=template2.format({name:"loogn",age:22});
String.prototype.format = function (args) {
    var result = this;
    if (arguments.length > 0) {
        if (arguments.length == 1 && typeof (args) == "object") {
            for (var key in args) {
                if (args[key] != undefined) {
                    var reg = new RegExp("({" + key + "})", "g");
                    result = result.replace(reg, args[key]);
                }
            }
        }
        else {
            for (var i = 0; i < arguments.length; i++) {
                if (arguments[i] != undefined) {
                    var reg = new RegExp("({[" + i + "]})", "g");
                    result = result.replace(reg, arguments[i]);
                }
            }
        }
    }
    return result;
}