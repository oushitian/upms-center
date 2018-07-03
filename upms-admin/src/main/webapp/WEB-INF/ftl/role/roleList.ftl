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
        url:'${ctx}/role/listData',
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
        <th data-options="field:'roleId',width:60,sortable:true">角色ID</th>
        <th data-options="field:'name',width:150">角色名</th>
        <th data-options="field:'appName',width:150">所属应用</th>
        <th data-options="field:'ownMenuGroups',width:200">拥有权限组</th>
        <th data-options="field:'gmtCreated',width:120,formatter:formatTime">添加时间</th>
        <th data-options="field:'gmtModified',width:120,formatter:formatTime">更新时间</th>
        <th data-options="field:'operate',width:300,align:'center',formatter:operate">操作</th>
    </tr>
    </thead>
</table>
<div id="tb">
    <form id="searchForm">
        <div>
            <input class="easyui-textbox" name="name" data-options="label:'角色名'" style="width:200px;">
            &nbsp;&nbsp;&nbsp;&nbsp;
            <select class="easyui-combobox" name="applicationId"
                    data-options="label:'所属应用',panelHeight:'auto',panelMaxHeight:200,onChange:function(newVal){showMenuGroup(newVal);}"
                    style="width:280px;">
                <option value="">全部</option>
            <#list applicationList as application>
                <option value="${application.applicationId}">${application.appName}</option>
            </#list>
            </select>
            &nbsp;&nbsp;&nbsp;&nbsp;
            <select id="menuGroupId" class="easyui-combobox" name="menuGroupId"
                    data-options="label:'拥有权限组',panelHeight:'auto',panelMaxHeight:200,valueField:'menuGroupId',textField:'name'"
                    style="width:250px;">
            </select>
            &nbsp;&nbsp;&nbsp;&nbsp;
            <a href="#" class="easyui-linkbutton" iconCls="icon-search" onclick="doSearch()">查 询</a>
        </div>
        <div style="margin-top: 15px">
        <#if permissionStrings?seq_contains("role:edit")><a href="#" class="easyui-linkbutton" iconCls="icon-add"
                                                            onclick="openAddDlg()" plain="true">新 增</a></#if>
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
        <div style="margin-bottom:10px;">
            <label class="label-top" style="text-align: right;width: 80px;display: inline-block;">角色名：</label>
            <input name="name" class="easyui-textbox" style="width: 200px;"
                   data-options="required:true,validType:'length[1,50]'"/>
        </div>
        <div style="margin-bottom:10px;">
            <label class="label-top" style="text-align: right;width: 80px;display: inline-block;">所属应用：</label>
            <select name="applicationId" class="easyui-combobox" style="width:200px;"
                    data-options="required:true,editable:false,onChange:function(newVal){showMenuGroupTree(newVal);},panelHeight:'auto',panelMaxHeight:200">
                <option value=""></option>
            <#list applicationList as application>
                <option value="${application.applicationId}">${application.appName}</option>
            </#list>
            </select>
        </div>
        <div style="margin-bottom:10px;">
            <label class="label-top" style="text-align: right;width: 80px;display: inline-block;">选择权限组：</label>
        </div>
        <div id="menuGroupTreeParent" class="easyui-panel" style="margin-bottom:10px;padding:10px">
            <ul id="menuGroupTree"></ul>
        </div>
        <div style="margin-bottom:10px;">
            <label class="label-top" style="text-align: right;width: 80px;display: inline-block;">选择维度值：</label>
        </div>
        <div id="dimensionTreeParent" class="easyui-panel" style="margin-bottom:10px;padding:10px">
            <ul id="dimensionTree"></ul>
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
                        edit();
                    }
                },{
                    text:'取消',
                    iconCls:'icon-cancel',
                    handler:function(){
                        $('#editDlg').dialog('close');
                    }
                }]">
    <form id="editForm">
        <input id="roleId" name="roleId" type="hidden"/>
        <div style="margin-bottom:10px;">
            <label class="label-top" style="text-align: right;width: 80px;display: inline-block;">角色名：</label>
            <input id="name" name="name" class="easyui-textbox" style="width: 200px;"
                   data-options="required:true,validType:'length[1,50]'"/>
        </div>
        <div style="margin-bottom:10px;">
            <label class="label-top" style="text-align: right;width: 80px;display: inline-block;">所属应用：</label>
            <select id="applicationId" name="applicationId" class="easyui-combobox" style="width:200px;"
                    data-options="required:true,editable:false,readonly:true">
                <option value=""></option>
            <#list applicationList as application>
                <option value="${application.applicationId}">${application.appName}</option>
            </#list>
            </select>
        </div>
        <div style="margin-bottom:10px;">
            <label class="label-top" style="text-align: right;width: 80px;display: inline-block;">选择权限组：</label>
        </div>
        <div id="menuGroupTreeParent_edit" class="easyui-panel" style="margin-bottom:10px;padding:10px">
            <ul id="menuGroupTree_edit"></ul>
        </div>
        <div style="margin-bottom:10px;">
            <label class="label-top" style="text-align: right;width: 80px;display: inline-block;">选择维度值：</label>
        </div>
        <div id="dimensionTreeParent_edit" class="easyui-panel" style="margin-bottom:10px;padding:10px">
            <ul id="dimensionTree_edit"></ul>
        </div>
    </form>
</div>

<#--查看数据权限-->
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
            <ul id="dimensionTree_view"></ul>
        </div>
    </form>
</div>

<#--分派应用许可-->
<div id="allocAppDlg" class="easyui-dialog" title="分派应用许可"
     style="width:500px;height:220px;padding:10px" data-options="
            cls:'dialog',
            resizable:true,
            closed:true,
            buttons: [{
                    text:'保存',
                    iconCls:'icon-ok',
                    handler:function(){
                        saveApp();
                    }
                },{
                    text:'取消',
                    iconCls:'icon-cancel',
                    handler:function(){
                        $('#allocAppDlg').dialog('close');
                    }
                }]">
    <form>
        <input id="roleId" name="roleId" type="hidden"/>
        <div style="margin-bottom:10px;">
            <label class="label-top" style="text-align: right;width: 120px;display: inline-block;">可操作的应用：</label>
            <select id="applicationId" name="applicationId" class="easyui-combobox" style="width:200px;height: 90px;"
                    data-options="multiple:true,multiline:true,editable:false,panelHeight:'auto',panelMaxHeight:200">
                <option value=""></option>
            <#list allApplicationList as application>
                <option value="${application.applicationId}">${application.appName}</option>
            </#list>
            </select>
        </div>
    </form>
</div>

<script>
    $(function () {
        $("#menuGroupTree").tree({
            onCheck: function () {
                var nodes = $("#menuGroupTree").tree("getChecked");
                if (nodes.length > 0) {
                    var menuGroupIds = [];
                    for (var i = 0; i < nodes.length; i++) {
                        menuGroupIds.push(nodes[i].id);
                    }
                    $("#dimensionTreeParent").show();
                    $("#dimensionTree").tree({
                        url: "${ctx}/menu/getDimensionMenus?menuGroupIds=" + menuGroupIds.join(","),
                        method: "post",
                        animate: true,
                        lines: true,
                        checkbox: true
                    });
                } else {
                    $("#dimensionTree").empty();
                }
            }
        });

        $("#menuGroupTree_edit").tree({
            onLoadSuccess: function () {
                refreshEditTree($("#editForm").find("#roleId").val());
            },
            onCheck: function () {
                refreshEditTree("");
            }
        });
    });

    function refreshEditTree(roleId) {
        var nodes = $("#menuGroupTree_edit").tree("getChecked");
        if (nodes.length > 0) {
            var menuGroupIds = [];
            for (var i = 0; i < nodes.length; i++) {
                menuGroupIds.push(nodes[i].id);
            }
            $("#dimensionTree_edit").tree({
                url: "${ctx}/menu/getDimensionMenus?menuGroupIds=" + menuGroupIds.join(",") + "&roleId=" + roleId,
                method: "post",
                animate: true,
                lines: true,
                checkbox: true
            });
        } else {
            $("#dimensionTree_edit").empty();
        }
    }

    function openAddDlg() {
        $("#menuGroupTreeParent").hide();
        $("#menuGroupTree").empty();
        $("#dimensionTreeParent").hide();
        $("#dimensionTree").empty();
        $("#addForm").form("reset");
        $("#addDlg").dialog('open');
    }

    function openEditDlg(index) {
        var rows = $("#dg").datagrid("getRows");
        var $editForm = $("#editForm");
        $editForm.find("#roleId").val(rows[index].roleId);
        $editForm.find("#name").textbox("setValue", rows[index].name);
        $editForm.find("#applicationId").combobox("setValue", rows[index].applicationId);
        $editForm.find("#menuGroupTree_edit").tree({
            url: "${ctx}/menuGroup/getMenuGroups?applicationId=" + rows[index].applicationId + "&roleId=" + rows[index].roleId,
            method: "post",
            animate: true,
            lines: true,
            checkbox: true,
            onlyLeafCheck: true
        });
        $("#editDlg").dialog('open');
    }

    function openViewDlg(index) {
        var rows = $("#dg").datagrid("getRows");
        var roleId = rows[index].roleId;
        var name = rows[index].name;
        var menuGroupIds = rows[index].ownMenuGroupIds;
        $("#dimensionTree_view").tree({
            url: "${ctx}/menu/getDimensionMenus?menuGroupIds=" + menuGroupIds + "&roleId=" + roleId,
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
        $("#viewDlg").dialog({title: name + "的数据权限"}).dialog('open');
    }

    function openAllocAppDlg(index) {
        var rows = $("#dg").datagrid("getRows");
        $("#allocAppDlg").find("#roleId").val(rows[index].roleId);
        $("#allocAppDlg").find("#applicationId").combobox("setValues", rows[index].permitApplicationIds);
        $("#allocAppDlg").dialog('open');
    }

    function saveApp() {
        var applicationIds = $("#allocAppDlg").find("#applicationId").combobox("getValues");
        var roleId = $("#allocAppDlg").find("#roleId").val();
        $.post('${ctx}/role/savePermitApplication', {
            roleId: roleId,
            applicationIds: applicationIds.join(",")
        }, function (result) {
            if (result.code != 0) {
                return $.messager.alert('错误', result.msg, 'error');
            }
            $("#allocAppDlg").dialog("close");
            $("#dg").datagrid("reload");
        }, 'json');
    }

    function doSearch() {
        var params = $("#searchForm").formToJson();
        $("#dg").datagrid({queryParams: params});
    }

    function operate(value, row, index) {
        var str = "";
        str += "<#if permissionStrings?seq_contains("role:edit") ><a href='#' class='button-edit button-default' onclick='openEditDlg(" + index + ")'>编辑</a></#if>";
        if (row.appKey == "upms" || row.appKey == "UPMS") {
            str += "<#if permissionStrings?seq_contains("role:savePermitApplication") > <a href='#' class='button-edit button-info' onclick='openAllocAppDlg(" + index + ")'>分派应用许可</a></#if>";
        }
        str += "<#if permissionStrings?seq_contains("role:viewDataPermissions") > <a href='#' class='button-edit button-success' onclick='openViewDlg(" + index + ")'>查看数据权限</a></#if>";
        str += "<#if permissionStrings?seq_contains("role:delete")> <a href='#' class='button-delete button-danger' onclick='deleteRecord(" + row.roleId + ")'>删除</a></#if>";
        return str;
    }

    function formatTime(value, row, index) {
        return formatDate(value);
    }

    function save() {
        var $addForm = $("#addForm");
        var formValid = $addForm.form('validate');
        if (!formValid) return;
        var menuGroupNodes = $("#menuGroupTree").tree("getChecked");
        if (menuGroupNodes.length == 0)
            return $.messager.alert('提示', "请选择权限组", 'warning');
        var menuGroupIds = [];
        $.each(menuGroupNodes, function (index, node) {
            menuGroupIds.push(node.id);
        });
        var dimensionValueDetails = [];
        var dimensionNodes = $("#dimensionTree").tree("getChecked");
        if (dimensionNodes.length > 0) {
            $.each(dimensionNodes, function (index, node) {
                if (node.nodeType == 4) {
                    dimensionValueDetails.push(node.dimensionValueDetail);
                }
            });
        }
        var params = $addForm.formToJson();
        params.menuGroupIds = menuGroupIds.join(",");
        params.dimensionValueDetails = dimensionValueDetails.join(";");
        $.post('${ctx}/role/edit', params, function (result) {
            if (result.code != 0) {
                return $.messager.alert('错误', result.msg, 'error');
            }
            $("#addDlg").dialog("close");
            $("#dg").datagrid("reload");
        }, 'json');
    }

    function edit() {
        var $editForm = $("#editForm");
        var formValid = $editForm.form('validate');
        if (!formValid) return;
        var menuGroupNodes = $("#menuGroupTree_edit").tree("getChecked");
        if (menuGroupNodes.length == 0)
            return $.messager.alert('提示', "请选择权限组", 'warning');
        var menuGroupIds = [];
        $.each(menuGroupNodes, function (index, node) {
            menuGroupIds.push(node.id);
        });
        var dimensionValueDetails = [];
        var dimensionNodes = $("#dimensionTree_edit").tree("getChecked");
        if (dimensionNodes.length > 0) {
            $.each(dimensionNodes, function (index, node) {
                if (node.nodeType == 4) {
                    dimensionValueDetails.push(node.dimensionValueDetail);
                }
            });
        }
        var params = $editForm.formToJson();
        params.menuGroupIds = menuGroupIds.join(",");
        params.dimensionValueDetails = dimensionValueDetails.join(";");
        $.post('${ctx}/role/edit', params, function (result) {
            if (result.code != 0) {
                return $.messager.alert('错误', result.msg, 'error');
            }
            $("#editDlg").dialog("close");
            $("#dg").datagrid("reload");
        }, 'json');
    }

    function showMenuGroup(applicationId) {
        $.post('${ctx}/menuGroup/queryByApplicationId', {applicationId: applicationId}, function (result) {
            $("#menuGroupId").combobox('clear');//清除选中值
            if (result.code != 0) {
                return $.messager.alert('错误', result.msg, 'error');
            }
            var menuGroups = result.rows;
            if (menuGroups.length > 0)
                $("#menuGroupId").combobox('loadData', menuGroups);
            else
                $("#menuGroupId").combobox('loadData', {});//清空option
        }, 'json');
    }

    function deleteRecord(roleId) {
        $.messager.confirm("确认", "确认删除？", function (r) {
            if (r) {
                $.post('${ctx}/role/delete', {roleId: roleId}, function (result) {
                    if (result.code != 0) {
                        return $.messager.alert('错误', result.msg, 'error');
                    }
                    $("#dg").datagrid("reload");
                }, 'json');
            }
        });
    }

    function showMenuGroupTree(applicationId) {
        $("#menuGroupTreeParent").show();
        $("#dimensionTree").empty();
        $("#menuGroupTree").tree({
            url: "${ctx}/menuGroup/getMenuGroups?applicationId=" + applicationId,
            method: "post",
            animate: true,
            lines: true,
            checkbox: true,
            onlyLeafCheck: true
        });
    }

</script>
<!--第三方插件加载-->
<script src="${static}/js/extend.validatebox.js" type="text/javascript"></script>
<script src="${static}/js/util.js" type="text/javascript"></script>
<script src="${static}/js/util/form.utils.js" type="text/javascript"></script>
</body>
</html>