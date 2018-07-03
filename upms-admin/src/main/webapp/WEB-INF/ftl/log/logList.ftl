<!doctype html>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="edge"/>
    <link rel="stylesheet" href="${static}/insdep/easyui.css" type="text/css"/>
    <link rel="stylesheet" href="${static}/insdep/easyui_animation.css" type="text/css"/>
    <link rel="stylesheet" href="${static}/insdep/easyui_plus.css" type="text/css"/>
    <link rel="stylesheet" href="${static}/insdep/insdep_theme_default.css" type="text/css"/>
    <link rel="stylesheet" href="${static}/insdep/icon.css" type="text/css"/>
    <link rel="stylesheet" href="${static}/css/item.css" type="text/css"/>
    <link rel="stylesheet" href="${static}/plugin/font-awesome-4.7.0/css/font-awesome.min.css" type="text/css"/>
    <script src="${static}/js/jquery.min.js" type="text/javascript"></script>
    <script src="${static}/js/jquery.easyui.1.5.min.js" type="text/javascript"></script>
    <script src="${static}/js/insdep/jquery.insdep-extend.min.js" type="text/javascript"></script>
</head>
<body>
<script src="${static}/js/page.loading.mask.js" type="text/javascript"></script>
<table id="dg" class="easyui-datagrid" style="width:100%;height:100%" data-options="
        rownumbers:true,
        singleSelect:true,
        url:'${ctx}/log/listData',
        method:'get',
        toolbar:'#tb',
        pagination:true,
        pageSize:20,
        striped:true,
        <#--操作按钮样式美化-->
        onLoadSuccess:function(){
             $('.button-edit,.button-delete').linkbutton({});
        }">
    <thead>
    <tr>
        <th data-options="field:'userId',width:120">操作人</th>
        <th data-options="field:'gmtCreated',width:120,formatter:formatTime">操作日期</th>
        <th data-options="field:'ip',width:100">IP地址</th>
        <th data-options="field:'operName',width:120">操作功能</th>
        <th data-options="field:'content',width:1000">操作详细记录</th>
    </tr>
    </thead>
</table>
<div id="tb">
    <form id="searchForm">
        <div style="margin-bottom: 15px">
            <input class="easyui-textbox" name="userName" data-options="label:'操作人'" style="width:200px;">
            &nbsp;&nbsp;&nbsp;&nbsp;
            <input class="easyui-datetimebox" name="startTime" data-options="label:'开始时间',showSeconds:false,editable:false" style="width:250px;">
            &nbsp;&nbsp;&nbsp;&nbsp;
            <input class="easyui-datetimebox" name="endTime" data-options="label:'结束时间',showSeconds:false,editable:false" style="width:250px;">
            &nbsp;&nbsp;&nbsp;&nbsp;
            <select class="easyui-combobox" name="operName" data-options="label:'操作功能',panelHeight:'auto',panelMaxHeight:200,editable:false" style="width:230px;">
                <option value="">全部</option>
            <#list operates?keys as key>
                <option value=${key}>${operates[key]}</option>
            </#list>
            </select>
            &nbsp;&nbsp;&nbsp;&nbsp;
            <a href="#" class="easyui-linkbutton" iconCls="icon-search" onclick="doSearch()">查 询</a>
        </div>
    </form>
</div>
<script>

    function doSearch() {
        var params = $("#searchForm").formToJson();
        $("#dg").datagrid({queryParams: params});
    }

    function formatTime(value, row, index) {
        return formatDate(value);
    }

</script>
<!--第三方插件加载-->
<script src="${static}/js/extend.validatebox.js" type="text/javascript"></script>
<script src="${static}/js/util.js" type="text/javascript"></script>
<script src="${static}/js/util/form.utils.js" type="text/javascript"></script>

</body>
</html>