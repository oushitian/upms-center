/**
 *  页面加载等待页面
 *
 * @author Barry
 * @description 改造优化page.loading.mask.js ,引入请放入第一个<body>下面,参考index.tpl
 * @date 2017/7/05
 *
 */
(function () {
    /**
     * 传递函数给whenReady(),
     * 当文档解析完毕且为操作准备就绪时，函数将作为文档对象的方法调用。
     * DOMContentLoaded、readystatechange、load事件发生时会触发注册的函数，
     * 一旦文档准备就绪，所有函数都将被调用，任何传递给whenReady()的函数都将立即调用。
     */
    var whenReady = (function () {
        var funcs = []; // 文档还未解析完毕时，用于存放接收的函数的队列
        var ready = false; // 标志文档是否解析完毕

        // 当文档准备就绪时，调用事件处理程序
        function handler(e) {
            // 如果已经运行过一次，则返回
            if (ready) return;
            // 如果发生readystatechange事件，但其状态不是“complete”，则文档尚未准备好。
            if (e.type === "readystatechange" && document.readyState !== "complete") return;

            // 运行所有注册的函数
            for (var i = 0; i < funcs.length; i++) {
                funcs[i].call(document);
            }

            // 文档解析完毕，将ready标志设置为true，并清除所有注册的函数
            ready = true;
            funcs = null;
        }

        // 将接收到的函数funcs集合，以handler的方式注册到“load”事件上
        if (document.addEventListener) {
            document.addEventListener("DOMContentLoaded", handler, false);
            document.addEventListener("readystatechange", handler, false);
            window.addEventListener("load", handler, false);
        } else if (document.attachEvent) {
            document.attachEvent("onreadystatechange", handler);
            window.attachEvent("onload", handler);
        }

        // 返回 whenReady()
        return function whenReady(f) {
            if (ready) f.call(document); // 若文档准备完毕，则只需要运行它
            else funcs.push(f);   // 否则，加入队列等候
        };
    }());

    var _html = "<div style='position:absolute;left:0;width:100%;height:100%;top:0;background:#FFFFFF;opacity:1;filter:alpha(opacity=100);'><div class='loading-mask-default'>loading...</div></div>";
    var div = document.createElement('div');
    div.id = 'loading_mask';
    div.style.display = 'none';
    div.innerHTML = _html;

    //定义显示loadingMask方法
    var setLoadingMask = function (doc, div) {
        doc.body.appendChild(div);
        var mask = doc.getElementById('loading_mask');
        if (mask) {
            mask.style.display = "";
            // 不能使用window.onload 如果图片无法加载页面会一直loading
            // window.onload = function () {
            //     mask.remove();
            // };
            whenReady(function () {
                mask.style.display = "none";
            });
        }
    };
    // 设置父级,一个框架就一个父级配置
    if (window.JOLLY_CONFIG_PARENT) {
        setLoadingMask(document, div);
    } else {
        //判断页面子级是否有父级
        var parentMask = parent.document.getElementById('loading_mask');
        if (parentMask) {
            parentMask.style.display = "none";
        }
        setLoadingMask(document, div);
    }
})(window);