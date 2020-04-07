/**
 * 文章界面的所有操作
 *  |-阅读进度（阅读进度条，目录高亮）
 *  |-代码前面加入行号
 *  |-生成目录
 *  |-初始化图片点击效果并在图片下面添加上解释说明
 *  |-在代码的右上角显示代码的格式
 *  |-剪切板功能
 *  |-手机端目录
 *  |-链接新窗口打开
 *  |-启用分享插件
 *  Status: OK
 */
$(function () {

    let backTopButton = () => {
        // 获取顶部按钮
        let $backTop = $("#toolbar");
        // 窗口滚动的监听
        $(window).scroll(function () {
            ($(this).scrollTop() > window.innerHeight / 2) && $backTop.css({
                "display": "block" // 显示返回顶部按钮
            }) || $backTop.css({
                "display": "none" // 隐藏返回顶部按钮
            });
        });
        // 返回顶部动画效果
        $backTop.on('click', function () {
            $("html,body").animate({scrollTop: 0}, 300);
        });
    };
    backTopButton();

    /**
     * 阅读进度（阅读进度条和目录高亮功能）
     */
    function readProgress() {

        // 文章内容
        let $content = $("#content");
        // 阅读进度条
        // let $readProgressBar = $("#readProgress .read-progress-bar");
        // 文章标题
        let articleTitles = $("article h1[id],h2[id],h3[id],h4[id],h5[id],h6[id]");
        // 正在阅读的标题下标
        let readTitleIndex = 0;
        // 上次正在阅读的标题下标
        let lastReadTitleIndex = -1;
        /**
         * 目录的进度（高亮）
         */
        let changeCatalogProgress = () => {
            // 获取每个标题距离最上面的尺寸
            let articleTitleOffsetTops = articleTitles.map(function () {
                return $(this).offset().top;
            }).get();
            // 获取当前位置距离最上面的尺寸 （+24）目的是去除偏差
            let currentPageYOffset = window.pageYOffset + 100;
            // 当前正在阅读的标题的下标（以0开始）
            readTitleIndex = articleTitleOffsetTops.findIndex((value, index, arr) => {
                return (value <= currentPageYOffset && currentPageYOffset <= arr[index + 1]) ||
                    (index === arr.length - 1 && currentPageYOffset >= arr[arr.length - 1]) ||
                    (index === 0 && currentPageYOffset <= arr[0]);
            });
            // 如果上次阅读标题下标和本次阅读标题下标相等，或者标题的下标小于0跳过
            if (readTitleIndex === lastReadTitleIndex || readTitleIndex < 0) return;
            // 处理高亮样式
            $("#catalogs .catalog-item").each(function (index) {
                // 取到所有的目录中的a标签
                let link = $(this).find('a');
                if (index === readTitleIndex) {
                    // 当然激活目录元素相对于父元素（目录盒子.card-body）顶部的偏移量
                    let positionTop = $(this).position().top - $("#catalogBox .card-header").outerHeight();
                    // 目录盒子
                    let $catalogs = $("#catalogs");
                    // 目录盒子的高度
                    let catalogsContainerHeight = $catalogs.outerHeight();
                    // 如果被激活的目录已经不在盒子（在盒子外面隐藏了）中做如下操作
                    positionTop <= 0 && $catalogs.animate({ // 向上滚动滚动条
                        scrollTop: $catalogs.scrollTop() - catalogsContainerHeight
                    }, 300);
                    positionTop >= catalogsContainerHeight && $catalogs.animate({ // 向下滚动滚动条
                        scrollTop: $catalogs.scrollTop() + catalogsContainerHeight
                    }, 300);
                    link.addClass("catalog-active");
                    return;
                }
                link.removeClass("catalog-active");
            });
            lastReadTitleIndex = readTitleIndex;
        };


        // 目录的进度（高亮）
        changeCatalogProgress();

        $(window).on('scroll', function () {
            console.log("sdfsd")
            // 目录的进度（高亮）
            changeCatalogProgress();
        });
    }


    /**
     * 改变URL中的锚点
     * @param anchorId 锚点ID
     */
    let changeUrlAnchor = (anchorId) => {
        let location = window.location;
        let origin = location.origin;
        let pathname = location.pathname;
        let baseUrl = origin + pathname;
        window.history.replaceState(null, null, baseUrl + anchorId);
    };


    let addAnchorPoint = (where) => {
        // 获取所有的标题
        let titles = $(`${where} h1[id],h2[id],h3[id],h4[id],h5[id],h6[id]`);
        titles.each(function () {
            // 要添加到界面上的a标签
            let a = $(`<a id="user-content-${$(this).attr('id')}" class="anchor" aria-hidden="true" href="#${$(this).attr('id')}"><svg class="octicon octicon-link" viewBox="0 0 16 16" version="1.1" width="16" height="16" aria-hidden="true"><path fill-rule="evenodd" d="M4 9h1v1H4c-1.5 0-3-1.69-3-3.5S2.55 3 4 3h4c1.45 0 3 1.69 3 3.5 0 1.41-.91 2.72-2 3.25V8.59c.58-.45 1-1.27 1-2.09C10 5.22 8.98 4 8 4H4c-.98 0-2 1.22-2 2.5S3 9 4 9zm9-3h-1v1h1c1 0 2 1.22 2 2.5S13.98 12 13 12H9c-.98 0-2-1.22-2-2.5 0-.83.42-1.64 1-2.09V6.25c-1.09.53-2 1.84-2 3.25C6 11.31 7.55 13 9 13h4c1.45 0 3-1.69 3-3.5S14.5 6 13 6z"></path></svg></a>`);
            // 在Title上添加a标签
            $(this).prepend(a);
            // 为a标签添加点击事件
            a.on('click', function (event) {
                let titleId = $(this).attr("href");
                changeUrlAnchor(titleId);
                let offsetTop = $(titleId).offset().top - 20;
                $("html,body").animate({scrollTop: offsetTop}, 300);
                event.preventDefault(); // 阻止默认事件
            });
        });
        // 为每个title绑定点击事件
        titles.on('click', function (event) {
            changeUrlAnchor(`#${$(this).attr("id")}`);
            let offsetTop = $(this).offset().top - 20;
            $("html,body").animate({scrollTop: offsetTop}, 300);
            event.preventDefault();
        });
    };

    readProgress();
    addAnchorPoint("article");
});