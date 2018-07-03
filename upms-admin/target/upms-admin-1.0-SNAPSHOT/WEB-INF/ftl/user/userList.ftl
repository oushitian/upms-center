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
    <script src="${static}/js/jquery.md5.js" type="text/javascript"></script>
</head>

<body>
<!--loading-->
<script src="${static}/js/page.loading.mask.js" type="text/javascript"></script>
<table id="dg" class="easyui-datagrid" style="width:100%;height:100%" data-options="
        rownumbers:true,
        singleSelect:true,
        url:'${ctx}/user/listData',
        method:'get',
        toolbar:'#tb',
        pagination:true,
        pageSize:20,
        striped:true,
        <#--操作按钮样式美化-->
        onLoadSuccess:function(){
            $('.button-edit,.button-delete,.button-privilege').linkbutton({});
        }">
    <thead>
    <tr>
        <th data-options="field:'userName',width:100">用户名</th>
        <th data-options="field:'email',width:160">邮箱</th>
        <th data-options="field:'ownRoles',width:300">拥有角色</th>
        <th data-options="field:'suppCode',width:80">供应商编号</th>
        <th data-options="field:'addTime',width:120,formatter:formatTime">添加时间</th>
        <th data-options="field:'lastLogin',width:120,formatter:formatTime">最后登录时间</th>
        <th data-options="field:'operate',width:380,align:'center',formatter:operate">操作</th>
    </tr>
    </thead>
</table>
<div id="tb">
    <form id="searchForm">
        <div>
            <input class="easyui-textbox" name="userName" data-options="label:'用户名'" style="width:200px;">
            &nbsp;&nbsp;&nbsp;&nbsp;
            <input class="easyui-textbox" name="roleName" data-options="label:'角色名'" style="width:200px;">
            &nbsp;&nbsp;&nbsp;&nbsp;
            <input class="easyui-textbox" name="email" data-options="label:'邮箱'" style="width:200px;">
            &nbsp;&nbsp;&nbsp;&nbsp;
            <select class="easyui-combobox" name="applicationId" data-options="label:'已授权应用',panelHeight:'auto',panelMaxHeight:200" style="width:280px;">
                <option value="">全部</option>
            <#list applicationList as application>
                <option value="${application.applicationId}">${application.appName}</option>
            </#list>
            </select>
            &nbsp;&nbsp;&nbsp;&nbsp;
            <select class="easyui-combobox" name="privilege" data-options="label:'私有权限',panelHeight:'auto',panelMaxHeight:200" style="width:150px;">
                <option value="">全部</option>
                <option value="1">有</option>
                <option value="0">无</option>
            </select>
            &nbsp;&nbsp;&nbsp;&nbsp;
            <a href="#" class="easyui-linkbutton" iconCls="icon-search" onclick="doSearch()">查 询</a>
        </div>
        <div style="margin-top: 15px">
            <#if permissionStrings?seq_contains("user:edit") > <a href="#" class="easyui-linkbutton" iconCls="icon-add" onclick="openAddDlg()" plain="true">新 增</a></#if>
            <#if permissionStrings?seq_contains("user:batchDelete") > <a href="#" class="easyui-linkbutton" iconCls="icon-remove" onclick="openBatchDelDlg()" plain="true">批量删除</a></#if>
        </div>
    </form>
</div>

<#--新增/编辑-->
<div id="addDlg" class="easyui-dialog"
     style="width:500px;height:550px;padding:10px" data-options="
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
        <input id="userId" name="userId" type="hidden"/>
        <div style="margin-bottom:10px;">
            <label class="label-top" style="text-align: right;width: 80px;display: inline-block;">用户名：</label>
            <input id="userName" name="userName" class="easyui-textbox" style="width: 200px;"
                   data-options="required:true,validType:'length[1,50]'"/>
        </div>
        <div style="margin-bottom:10px;">
            <label class="label-top" style="text-align: right;width: 80px;display: inline-block;">邮箱：</label>
            <input id="email" name="email" class="easyui-textbox" style="width: 200px;"
                   data-options="required:true,validType:'email'"/>
        </div>
        <div style="margin-bottom:10px;">
            <label class="label-top" style="text-align: right;width: 80px;display: inline-block;">供应商用户：</label>
            <input type="radio" name="isSuppUser" value="0"/>否
            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
            <input type="radio" name="isSuppUser" value="1"/>是
        </div>
        <div id="supplierDiv" style="margin-bottom:10px;">
            <label class="label-top" style="text-align: right;width: 80px;display: inline-block;">所属供应商：</label>
            <select id="suppCode" name="suppCode" class="easyui-combobox" style="width:200px;">
                <option value=""></option>
            <#list supplierMap?keys as key>
                <option value=${key}>${supplierMap[key]}</option>
            </#list>
            </select>
        </div>
        <div style="margin-bottom:10px;">
            <label class="label-top" style="text-align: right;width: 80px;display: inline-block;">角色：</label>
        </div>
        <div class="easyui-panel" style="margin-bottom:10px;padding:10px">
            <ul id="roleTree"></ul>
        </div>
    </form>
</div>

<#--分派私有权限-->
<div id="allocPrivilegeDlg" class="easyui-dialog" title="分派私有权限"
     style="width:500px;height:500px;padding:10px" data-options="
            cls:'dialog',
            resizable:true,
            closed:true,
            buttons: [{
                    text:'保存',
                    iconCls:'icon-ok',
                    handler:function(){
                        savePrivilege();
                    }
                },{
                    text:'取消',
                    iconCls:'icon-cancel',
                    handler:function(){
                        $('#allocPrivilegeDlg').dialog('close');
                    }
                }]">
    <form>
        <input id="userId" name="userId" type="hidden"/>
        <div class="easyui-panel" style="margin-bottom:10px;padding:10px">
            <ul id="menuTree"></ul>
        </div>
    </form>
</div>

<#--查看私有权限-->
<div id="viewPrivilegeDlg" class="easyui-dialog" title="查看私有权限"
     style="width:500px;height:500px;padding:10px" data-options="
            cls:'dialog',
            resizable:true,
            closed:true,
            buttons: [{
                    text:'取消',
                    iconCls:'icon-cancel',
                    handler:function(){
                        $('#viewPrivilegeDlg').dialog('close');
                    }
                }]">
    <form>
        <div class="easyui-panel" style="margin-bottom:10px;padding:10px">
            <ul id="menuTree_view"></ul>
        </div>
    </form>
</div>

<#--重置密码-->
<div id="resetPwdDlg" class="easyui-dialog" title="重置密码"
     style="width:400px;height:250px;padding:10px" data-options="
            cls:'dialog',
            resizable:true,
            closed:true,
            buttons: [{
                    text:'保存',
                    iconCls:'icon-ok',
                    handler:function(){
                        resetPwd();
                    }
                },{
                    text:'取消',
                    iconCls:'icon-cancel',
                    handler:function(){
                        $('#resetPwdDlg').dialog('close');
                    }
                }]">
    <form id="resetPwdForm">
        <input id="userId" name="userId" type="hidden"/>
        <div style="margin-bottom:10px;">
            <label class="label-top" style="text-align: right;width: 80px;display: inline-block;">用户名：</label>
            <input id="userName" class="easyui-textbox" style="width: 200px;"
                   data-options="readonly:true"/>
        </div>
        <div style="margin-bottom:10px;">
            <label class="label-top" style="text-align: right;width: 80px;display: inline-block;">新密码：</label>
            <input id="password" name="password" class="easyui-passwordbox" style="width: 200px;"
                   data-options="required:true,validType:'length[6,20]'"/>
        </div>
        <div style="margin-bottom:10px;">
            <label class="label-top" style="text-align: right;width: 80px;display: inline-block;">确认密码：</label>
            <input id="confirmPassword" class="easyui-passwordbox" style="width: 200px;"
                   data-options="required:true,validType:'length[6,20]'"/>
        </div>
    </form>
</div>

<#--批量删除-->
<div id="batchDelDlg" class="easyui-dialog" title="批量删除"
     style="width:400px;height:200px;padding:10px" data-options="
            cls:'dialog',
            resizable:true,
            closed:true,
            buttons: [{
                    text:'导入',
                    iconCls:'icon-ok',
                    handler:batchDel
                },{
                    text:'取消',
                    iconCls:'icon-cancel',
                    handler:function(){
                        $('#batchDelDlg').dialog('close');
                    }
                }]">
    <form enctype="multipart/form-data">
        <div style="margin-bottom: 20px">
            <a href="${static}/download/batchDelUserTemplate.xls" class="easyui-linkbutton" iconCls="icon-download">模版下载</a>
        </div>
        <input type="file" id="batchDelUserFile" name="batchDelUserFile"/>
    </form>
</div>

<#--批量删除结果弹出框-->
<div id="resultDlg" class="easyui-dialog" title="操作结果"
     style="width:500px;height:300px;padding: 10px;" data-options="
            cls:'dialog',
            resizable:true,
            closed:true">
    <div id="resultDiv">
    </div>
</div>

<script>

    $(function () {
        $(":radio[name='isSuppUser']").on('change', function () {
            var type = $(this).val();
            if (type == "0") {
                $("#suppCode").combobox({required: false}).combobox("clear");
                $("#supplierDiv").hide();
            }
            if (type == "1") {
                $("#suppCode").combobox({required: true}).combobox("clear");
                $("#supplierDiv").show();
            }
        });
    });

    function openAddDlg() {
        $("#addForm").form("reset");
        $("#addForm").find("#userId").val("");
        $("#addForm").find("#userName").textbox({readonly: false});
        $("#roleTree").tree({
            url: "${ctx}/role/getRoles",
            method: "post",
            animate: true,
            lines: true,
            checkbox: true,
            onlyLeafCheck: true
        });
        $(":radio[name='isSuppUser'][value='0']").attr({checked: true, disabled: false});
        $(":radio[name='isSuppUser'][value='1']").attr({checked: false, disabled: false});
        $("#suppCode").combobox({required: false, readonly: false}).combobox("clear");
        $("#supplierDiv").hide();
        $("#addDlg").dialog({title: "新增"});
        $("#addDlg").dialog('open');
    }

    function openEditDlg(index) {
        $("#addForm").form("reset");
        var rows = $("#dg").datagrid("getRows");
        $("#roleTree").tree({
            url: "${ctx}/role/getRoles?userId=" + rows[index].userId,
            method: "post",
            animate: true,
            lines: true,
            checkbox: true,
            onlyLeafCheck: true
        });
        $("#addForm").find("#userId").val(rows[index].userId);
        $("#addForm").find("#userName").textbox("setValue", rows[index].userName).textbox({readonly: true});
        $("#email").textbox("setValue", rows[index].email);
        var isSuppUser = rows[index].isSuppUser;
        if (isSuppUser == 0) {
            $(":radio[name='isSuppUser'][value='0']").attr({checked: true, disabled: true});
            $(":radio[name='isSuppUser'][value='1']").attr({checked: false, disabled: true});
            $("#suppCode").combobox({required: false}).combobox("clear");
            $("#supplierDiv").hide();
        } else {
            $(":radio[name='isSuppUser'][value='0']").attr({checked: false, disabled: true});
            $(":radio[name='isSuppUser'][value='1']").attr({checked: true, disabled: true});
            $("#suppCode").combobox({required: true, readonly: true}).combobox("setValue", rows[index].suppCode);
            $("#supplierDiv").show();
        }
        $("#addDlg").dialog({title: "编辑"});
        $("#addDlg").dialog('open');
    }

    function openResetPwdDlg(index) {
        $("#resetPwdForm").form("reset");
        var rows = $("#dg").datagrid("getRows");
        $("#resetPwdForm").find("#userId").val(rows[index].userId);
        $("#resetPwdForm").find("#userName").textbox("setValue", rows[index].userName);
        $("#resetPwdDlg").dialog('open');
    }

    function openBatchDelDlg() {
        $("#batchDelUserFile").val("");
        $("#batchDelDlg").dialog('open');
    }

    function batchDel() {
        if ($("#batchDelUserFile").val() == "") {
            return $.messager.alert('提示', '请先选择导入文件！', 'warning');
        }
        var formData = new FormData();
        formData.append("batchDelUserFile", $("#batchDelUserFile")[0].files[0]);
        $.ajax({
            url: '${ctx}/user/batchDelete',
            type: 'POST',
            data: formData,
            async: false,
            cache: false,
            contentType: false,
            processData: false,
            success: function (result) {
                if (result.code != 0) {
                    return $.messager.alert('错误', result.msg, 'error');
                }
                $("#resultDiv").html("<font color='red'>" + result.successCount + "</font>条数据操作成功，<font color='red'>" + result.failCount + "</font>条数据操作失败！<br/>");
                for (var i = 0; i < result.failList.length; i++) {
                    $("#resultDiv").append("<font color='red'>用户名：" + result.failList[i].userName + "，邮箱地址：" + result.failList[i].email + " 删除失败，原因：" + result.failList[i].failReason + "</font><br/>");
                }
                $("#resultDlg").dialog("open");
                $("#batchDelDlg").dialog("close");
                $("#dg").datagrid("reload");
            },
            error: function () {
                $.messager.alert('信息', '操作失败', 'error');
            }
        });
    }

    function resetPwd() {
        var $resetPwdForm = $("#resetPwdForm");
        var formValid = $resetPwdForm.form('validate');
        if (!formValid) return;
        var password = $("#password").passwordbox("getValue");
        var confirmPassword = $("#confirmPassword").passwordbox("getValue");
        if (password != confirmPassword) {
            return $.messager.alert('警告', '两次输入的密码不一致！', 'warning');
        }
        $("#password").passwordbox("setValue", $.md5(password));
        var params = $resetPwdForm.formToJson();
        $.post('${ctx}/user/resetPwd', params, function (result) {
            if (result.code != 0) {
                return $.messager.alert('错误', result.msg, 'error');
            }
            $("#resetPwdDlg").dialog("close");
            $("#dg").datagrid("reload");
        }, 'json');
    }

    function doSearch() {
        var params = $("#searchForm").formToJson();
        $("#dg").datagrid({queryParams: params});
    }

    function operate(value, row, index) {
        return   " <#if permissionStrings?seq_contains("user:edit")><a href='#' class='button-edit button-default' onclick='openEditDlg(" + index + ")'>编辑</a> </#if>"+
                    "<#if permissionStrings?seq_contains("user:saveUserPermissions")> <a href='#' class='button-privilege button-info' onclick='openAllocPrivilegeDlg(" + row.userId + ")'>分派私有权限</a> </#if>"+
                    "<#if permissionStrings?seq_contains("user:viewUserPermissions")> <a href='#' class='button-privilege button-success' onclick='openViewPrivilegeDlg(" + row.userId + ")'>查看私有权限</a> </#if>"+
                    "<#if permissionStrings?seq_contains("user:delete")> <a href='#' class='button-delete button-danger' onclick='deleteUser(" + row.userId + ")'>删除</a> </#if>"+
                    "<#if permissionStrings?seq_contains("user:resetPwd")> <a href='#' class='button-delete button-primary' onclick='openResetPwdDlg(" + index + ")'>重置密码</a> </#if>";
    }

    function formatTime(value, row, index) {
        return formatDate(value);
    }

    function openAllocPrivilegeDlg(userId) {
        $("#allocPrivilegeDlg").find("#userId").val(userId);
        $("#menuTree").tree({
            url: "${ctx}/menu/getMenus?userId=" + userId,
            method: "post",
            animate: true,
            lines: true,
            checkbox: true
        });
        $("#allocPrivilegeDlg").dialog('open');
    }

    function openViewPrivilegeDlg(userId) {
        $("#menuTree_view").tree({
            url: "${ctx}/menu/getMenus?userId=" + userId,
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
        $("#viewPrivilegeDlg").dialog('open');
    }

    function save() {
        var $addForm = $("#addForm");
        var formValid = $addForm.form('validate');
        if (!formValid) return;
        var nodes = $("#roleTree").tree("getChecked");
        if (nodes.length == 0)
            return $.messager.alert('提示', "请选择角色", 'warning');
        var roleIds = [];
        for (var i = 0; i < nodes.length; i++) {
            roleIds.push(nodes[i].id);
        }
        var params = $addForm.formToJson();
        params.roleIds = roleIds.join(",");
        $.post('${ctx}/user/edit', params, function (result) {
            if (result.code < 0) {
                return $.messager.alert('错误', result.msg, 'error');
            }
            if (result.code == 1) {
                $("#addDlg").dialog("close");
                $("#dg").datagrid("reload");
                return $.messager.alert('提示', "用户创建成功！该用户初始密码为" + result.msg + "，请注意保存！", 'success');
            }
            $("#addDlg").dialog("close");
            $("#dg").datagrid("reload");
        }, 'json');
    }

    function savePrivilege() {
        var $menuTree = $("#menuTree");
        var nodes = $menuTree.tree("getChecked");
        var menuIds = [];
        var dimensionValueDetails = [];
        if (nodes.length > 0) {
            $.each(nodes, function (index, node) {
                //选中的是维度值节点
                if (node.nodeType == 4) {
                    dimensionValueDetails.push(node.dimensionValueDetail);
                }
                pushMenuIds(node, $menuTree, menuIds);
            });
        }
        var userId = $("#allocPrivilegeDlg").find("#userId").val();
        $.post('${ctx}/user/saveUserPermissions', {
            userId: userId,
            menuIds: menuIds.join(","),
            dimensionValueDetails: dimensionValueDetails.join(";")
        }, function (result) {
            if (result.code != 0) {
                return $.messager.alert('错误', result.msg, 'error');
            }
            $("#allocPrivilegeDlg").dialog("close");
            $("#dg").datagrid("reload");
        }, 'json');
    }

    function pushMenuIds(node, menuTree, menuIds) {
        if (node.nodeType == 2) {//只保留节点类型为菜单的节点ID，也就是菜单ID
            if ($.inArray(node.id, menuIds) < 0) {
                menuIds.push(node.id);
            }
        }
        var parentNode = menuTree.tree('getParent', node.target);
        if (parentNode != null) {
            pushMenuIds(parentNode, menuTree, menuIds);//递归调用
        }
    }

    function deleteUser(userId) {
        $.messager.confirm("确认", "确认删除？", function (r) {
            if (r) {
                $.post('${ctx}/user/delete', {userId: userId}, function (result) {
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