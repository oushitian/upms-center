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
        url:'${ctx}/app/listData',
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
        <th data-options="field:'appName',width:150">应用系统名称</th>
        <th data-options="field:'appKey',width:100">应用系统key</th>
        <th data-options="field:'domainName',width:250">应用系统链接</th>
        <th data-options="field:'description',width:150">描述</th>
        <th data-options="field:'gmtModified',width:120,formatter:formatTime">更新时间</th>
        <th data-options="field:'modifier',width:120">最后操作人</th>
            <th data-options="field:'operate',width:150,align:'center',formatter:operate">操作</th>
    </tr>
    </thead>
</table>
<div id="tb">

    <form id="searchForm">
        <div>
            <input class="easyui-textbox" name="appName" data-options="label:'应用系统名称'" style="width:200px;">
            &nbsp;&nbsp;&nbsp;&nbsp;
            <a href="#" class="easyui-linkbutton" iconCls="icon-search" onclick="doSearch()">查 询</a>
        </div>
        <div style="margin-top: 15px">
            <#if permissionStrings?seq_contains("app:save")>  <a href="#" class="easyui-linkbutton" iconCls="icon-add" onclick="openAddDlg()" plain="true">新 增</a></#if>
        </div>
    </form>
</div>

<#--新增-->
<div id="addDlg" class="easyui-dialog" title="新增"
     style="width:500px;height:auto;padding:10px" data-options="
            cls:'dialog',
            resizable:true,
            closed:true,
            buttons: [{
                    text:'保存',
                    iconCls:'icon-ok',
                    handler:function(){
                        save();
                    }
                },{
                    text:'取消',
                    iconCls:'icon-cancel',
                    handler:function(){
                        $('#addDlg').dialog('close');
                    }
                }]">
    <form id="addForm">
        <input id="applicationId" name="applicationId" type="hidden"/>
        <div style="margin-bottom:10px;">
            <label class="label-top" style="text-align: right;width: 100px;display: inline-block;">应用系统名称：</label>
            <input id="appName" name="appName" class="easyui-textbox" style="width: 200px;"
                   data-options="required:true,validType:'length[1,50]'"/>
        </div>
        <div style="margin-bottom:10px;">
            <label class="label-top" style="text-align: right;width: 100px;display: inline-block;">应用系统Key：</label>
            <input id="appKey" name="appKey" class="easyui-textbox" style="width: 200px;"
                   data-options="required:true,validType:'length[1,50]'"/>
        </div>
        <div style="margin-bottom:10px;">
            <label class="label-top" style="text-align: right;width: 100px;display: inline-block;">应用系统链接：</label>
            <input id="domainName" name="domainName" class="easyui-textbox" style="width: 200px;"
                   data-options="required:true,validType:'length[1,50]'"/>
        </div>
        <div style="margin-bottom:10px;">
            <label class="label-top" style="text-align: right;width: 100px;display: inline-block;">描述：</label>
            <input id="description" name="description" class="easyui-textbox" style="width: 200px;"
                   data-options="validType:'length[1,50]'"/>
        </div>
    </form>
</div>

<script>
    var url;
    function openAddDlg() {
        $("#addForm").find("#applicationId").val("");
        $("#addForm").find("#appKey").textbox({readonly:false});
        $("#addForm").form("reset");
        $("#addDlg").dialog('open').dialog('setTitle','新增应用');
        url = '${ctx}/app/save';
    }

    function openEditDlg(index) {
        $("#addForm").form("reset");
        $("#addForm").find("#appKey").textbox({readonly:true});
        var rows = $("#dg").datagrid("getRows");
        $("#addForm").find("#applicationId").val(rows[index].applicationId);
        $("#appName").textbox("setValue", rows[index].appName);
        $("#appKey").textbox("setValue", rows[index].appKey);
        $("#description").textbox("setValue", rows[index].description);
        $("#domainName").textbox("setValue", rows[index].domainName);
        url='${ctx}/app/doModify';
        $("#addDlg").dialog({title: "编辑应用"});
        $("#addDlg").dialog('open');
    }

    function doSearch() {
        var params = $("#searchForm").formToJson();
        $("#dg").datagrid({queryParams: params});
    }

    function operate(value, row, index) {
        return  " <#if permissionStrings?seq_contains("app:doModify")><a href='#' class='button-edit button-default' onclick='openEditDlg(" + index + ")'>编辑</a> </#if>"+
                        "<#if permissionStrings?seq_contains("app:delete")> <a href='#' class='button-delete button-danger' onclick='deleteApp(" + row.applicationId + ")'>删除</a> </#if>";
    }

    function formatTime(value, row, index) {
        return formatDate(value);
    }

    function save() {
        var $addForm = $("#addForm");
        var formValid = $addForm.form('validate');
        if (!formValid) return;
        var params = $addForm.formToJson();
        $.post(url, params, function (result) {
            if (result.code != 0) {
                return $.messager.alert('错误', result.msg, 'error');
            }
            $("#addDlg").dialog("close");
            $("#dg").datagrid("reload");
        }, 'json');
    }

    function deleteApp(applicationId) {
        $.messager.confirm("确认", "确认删除？", function (r) {
            if (r) {
                $.post('${ctx}/app/delete', {applicationId: applicationId}, function (result) {
                    if (result.code != 0) {
                        return $.messager.alert('错误', result.msg, 'error');
                    }
                    $("#dg").datagrid("reload");
                }, 'json');
            }
        });
    }



</script>
<!--第三方插件加载-->
<script src="${static}/js/extend.validatebox.js" type="text/javascript"></script>
<script src="${static}/js/util.js" type="text/javascript"></script>
<script src="${static}/js/util/form.utils.js" type="text/javascript"></script>

</body>
</html>