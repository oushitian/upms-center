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
        url:'${ctx}/menuGroup/listData',
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
        <th data-options="field:'name',width:200">权限组名称</th>
        <th data-options="field:'dataDimensions',width:200">数据维度</th>
        <th data-options="field:'appName',width:150">所属应用</th>
        <th data-options="field:'gmtCreated',width:120,formatter:formatTime">添加时间</th>
        <th data-options="field:'operate',width:200,align:'center',formatter:operate">操作</th>
    </tr>
    </thead>
</table>
<div id="tb">
    <form id="searchForm">
        <div>
            <input class="easyui-textbox" name="name" data-options="label:'权限组名称'" style="width:300px;">
            &nbsp;&nbsp;&nbsp;&nbsp;
            <select class="easyui-combobox" name="applicationId" data-options="label:'所属应用',panelHeight:'auto',panelMaxHeight:200" style="width:280px;">
                <option value="">全部</option>
            <#list applicationList as application>
                <option value="${application.applicationId}">${application.appName}</option>
            </#list>
            </select>
            &nbsp;&nbsp;&nbsp;&nbsp;
            <a href="#" class="easyui-linkbutton" iconCls="icon-search" onclick="doSearch()">查 询</a>
        </div>
        <div style="margin-top: 15px">
            <#if permissionStrings?seq_contains("menuGroup:edit")><a href="#" class="easyui-linkbutton" iconCls="icon-add" onclick="openAddDlg()" plain="true">新 增</a></#if>
        </div>
    </form>
</div>

<#--新增-->
<div id="addDlg" class="easyui-dialog" title="新增"
     style="width:500px;height:500px;padding:10px" data-options="
            cls:'dialog',
            resizable:true,
            closed:true,
            buttons: [{
                    text:'保存',
                    iconCls:'icon-ok',
                    handler:function(){
                        save($('#addDlg'),$('#menuTree'));
                    }
                },{
                    text:'取消',
                    iconCls:'icon-cancel',
                    handler:function(){
                        $('#addDlg').dialog('close');
                    }
                }]">
    <form id="addForm">
        <div style="margin-bottom:10px;">
            <label class="label-top" style="text-align: right;width: 80px;display: inline-block;">权限组名称：</label>
            <input name="name" class="easyui-textbox" style="width: 200px;"
                   data-options="required:true,validType:'length[1,50]'"/>
        </div>
        <div style="margin-bottom:10px;">
            <label class="label-top" style="text-align: right;width: 80px;display: inline-block;">所属应用：</label>
            <select name="applicationId" class="easyui-combobox" style="width:200px;"
                    data-options="required:true,editable:false,onChange:function(newVal){showMenus(newVal);},panelHeight:'auto',panelMaxHeight:200">
                <option value=""></option>
            <#list applicationList as application>
                <option value="${application.applicationId}">${application.appName}</option>
            </#list>
            </select>
        </div>
        <div style="margin-bottom:10px;">
            <label class="label-top" style="text-align: right;width: 80px;display: inline-block;">选择权限：</label>
        </div>
        <div id="menuTreeParent" class="easyui-panel" style="margin-bottom:10px;padding:10px">
            <ul id="menuTree"></ul>
        </div>
    </form>
</div>

<#--编辑-->
<div id="editDlg" class="easyui-dialog" title="编辑"
     style="width:500px;height:500px;padding:10px" data-options="
            cls:'dialog',
            resizable:true,
            closed:true,
            buttons: [{
                    text:'保存',
                    iconCls:'icon-ok',
                    handler:function(){
                        save($('#editDlg'),$('#menuTree_edit'));
                    }
                },{
                    text:'取消',
                    iconCls:'icon-cancel',
                    handler:function(){
                        $('#editDlg').dialog('close');
                    }
                }]">
    <form id="editForm">
        <input id="menuGroupId" name="menuGroupId" type="hidden"/>
        <div style="margin-bottom:10px;">
            <label class="label-top" style="text-align: right;width: 80px;display: inline-block;">权限组名称：</label>
            <input id="name" name="name" class="easyui-textbox" style="width: 200px;"
                   data-options="required:true,validType:'length[1,50]'"/>
        </div>
        <div style="margin-bottom:10px;">
            <label class="label-top" style="text-align: right;width: 80px;display: inline-block;">所属应用：</label>
            <select id="applicationId" name="applicationId" class="easyui-combobox" style="width:200px;"
                    data-options="readonly:true,editable:false">
                <option value=""></option>
            <#list applicationList as application>
                <option value="${application.applicationId}">${application.appName}</option>
            </#list>
            </select>
        </div>
        <div style="margin-bottom:10px;">
            <label class="label-top" style="text-align: right;width: 80px;display: inline-block;">选择权限：</label>
        </div>
        <div id="menuTreeParent_edit" class="easyui-panel" style="margin-bottom:10px;padding:10px">
            <ul id="menuTree_edit"></ul>
        </div>
    </form>
</div>

<#--查看-->
<div id="viewDlg" class="easyui-dialog"
     style="width:500px;height:500px;padding:10px" data-options="
            cls:'dialog',
            resizable:true,
            closed:true,
            buttons: [{
                    text:'取消',
                    iconCls:'icon-cancel',
                    handler:function(){
                        $('#viewDlg').dialog('close');
                    }
                }]">
    <form>
        <div class="easyui-panel" style="margin-bottom:10px;padding:10px">
            <ul id="menuTree_view"></ul>
        </div>
    </form>
</div>

<script>
    function openAddDlg() {
        $("#menuTreeParent").hide();
        $("#menuTree").empty();
        $("#addForm").form("reset");
        $("#addDlg").dialog('open');
    }

    function openEditDlg(index) {
        var rows = $("#dg").datagrid("getRows");
        var menuGroupId = rows[index].menuGroupId;
        var applicationId = rows[index].applicationId;
        $("#menuGroupId").val(menuGroupId);
        $("#name").textbox("setValue", rows[index].name);
        $("#applicationId").combobox("setValue", applicationId);
        $("#menuTree_edit").tree({
            url: "${ctx}/menu/getMenus?applicationId=" + applicationId + "&menuGroupId=" + menuGroupId,
            method: "post",
            animate: true,
            lines: true,
            checkbox: true
        });
        $("#editDlg").dialog('open');
    }

    function openViewDlg(index) {
        var rows = $("#dg").datagrid("getRows");
        var menuGroupId = rows[index].menuGroupId;
        var applicationId = rows[index].applicationId;
        var name = rows[index].name;
        $("#menuTree_view").tree({
            url: "${ctx}/menu/getMenus?applicationId=" + applicationId + "&menuGroupId=" + menuGroupId,
            method: "post",
            animate: true,
            lines: true,
            checkbox: true,
            onLoadSuccess: function () {
//                checkbox只读
                $(this).find('span.tree-checkbox').unbind().click(function () {
                    return false;
                });
            }
        });
        $("#viewDlg").dialog({title: name + "的权限集"}).dialog('open');
    }

    function doSearch() {
        var params = $("#searchForm").formToJson();
        $("#dg").datagrid({queryParams: params});
    }

    function operate(value, row, index) {
        return " <#if permissionStrings?seq_contains("menuGroup:edit")><a href='#' class='button-edit button-default' onclick='openEditDlg(" + index + ")'>编辑</a> </#if>"+
            "<#if permissionStrings?seq_contains("menuGroup:viewPermissions")><a href='#' class='button-delete button-success' onclick='openViewDlg(" + index + ")'>查看权限集</a> </#if>"+
            "<#if permissionStrings?seq_contains("menuGroup:delete")><a href='#' class='button-delete button-danger' onclick='deleteRecord(" + row.menuGroupId + ")'>删除</a> </#if>";
    }

    function formatTime(value, row, index) {
        return formatDate(value);
    }

    function save(dlg, menuTree) {
        var $form = dlg.find("form");
        var formValid = $form.form('validate');
        if (!formValid) return;
        var nodes = menuTree.tree("getChecked");
        if (nodes.length == 0)
            return $.messager.alert('提示', "请选择菜单", 'warning');
        var menuIds = [];
        $.each(nodes, function (index, node) {
            pushMenuIds(node, menuTree, menuIds);
        });
        var params = $form.formToJson();
        params.menuIds = menuIds.join(",");
        $.post('${ctx}/menuGroup/edit', params, function (result) {
            if (result.code != 0) {
                return $.messager.alert('错误', result.msg, 'error');
            }
            dlg.dialog("close");
            $("#dg").datagrid("reload");
        }, 'json');
    }

    function pushMenuIds(node, menuTree, menuIds) {
        if ($.inArray(node.id, menuIds) < 0) {
            menuIds.push(node.id);
        }
        var parentNode = menuTree.tree('getParent', node.target);
        if (parentNode != null) {
            pushMenuIds(parentNode, menuTree, menuIds);//递归调用
        }
    }

    function deleteRecord(menuGroupId) {
        $.messager.confirm("确认", "确认删除？", function (r) {
            if (r) {
                $.post('${ctx}/menuGroup/delete', {menuGroupId: menuGroupId}, function (result) {
                    if (result.code != 0) {
                        return $.messager.alert('错误', result.msg, 'error');
                    }
                    $("#dg").datagrid("reload");
                }, 'json');
            }
        });
    }

    function showMenus(applicationId) {
        $("#menuTreeParent").show();
        $("#menuTree").tree({
            url: "${ctx}/menu/getMenus?applicationId=" + applicationId,
            method: "post",
            animate: true,
            lines: true,
            checkbox: true
        });
    }

</script>
<!--第三方插件加载-->
<script src="${static}/js/extend.validatebox.js" type="text/javascript"></script>
<script src="${static}/js/util.js" type="text/javascript"></script>
<script src="${static}/js/util/form.utils.js" type="text/javascript"></script>
</body>
</html>