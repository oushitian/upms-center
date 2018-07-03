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
        url:'${ctx}/menu/listData',
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
        <th data-options="field:'name',width:150">资源名</th>
        <th data-options="field:'typeName',width:60">资源类型</th>
        <th data-options="field:'appName',width:120">所属应用</th>
        <th data-options="field:'parentName',width:150">上级资源</th>
        <th data-options="field:'dimensionNames',width:150">数据维度</th>
        <th data-options="field:'url',width:250">链接</th>
        <th data-options="field:'permissionString',width:250">权限串</th>
        <th data-options="field:'displayOrder',width:40">排序号</th>
        <th data-options="field:'gmtCreated',width:120,formatter:formatTime">添加时间</th>
        <th data-options="field:'gmtModified',width:120,formatter:formatTime">更新时间</th>
        <th data-options="field:'operate',width:150,align:'center',formatter:operate">操作</th>
    </tr>
    </thead>
</table>
<div id="tb">
    <form id="searchForm">
        <div>
            <input class="easyui-textbox" name="name" data-options="label:'资源名'" style="width:200px;">
            &nbsp;&nbsp;&nbsp;&nbsp;
            <select class="easyui-combobox" name="type" data-options="label:'资源类型',panelHeight:'auto',panelMaxHeight:200" style="width:200px;">
                <option value="">全部</option>
                <option value="1">一级菜单</option>
                <option value="2">二级菜单</option>
                <option value="3">按钮</option>
            </select>
            &nbsp;&nbsp;&nbsp;&nbsp;
            <select class="easyui-combobox" name="applicationId" data-options="label:'所属应用',panelHeight:'auto',panelMaxHeight:200" style="width:280px;">
                <option value="">全部</option>
            <#list applicationList as application>
                <option value="${application.applicationId}">${application.appName}</option>
            </#list>
            </select>
            &nbsp;&nbsp;&nbsp;&nbsp;
            <input class="easyui-textbox" name="parentName" data-options="label:'上级资源'" style="width:200px;">
            &nbsp;&nbsp;&nbsp;&nbsp;
            <input class="easyui-textbox" name="url" data-options="label:'链接'" style="width:200px;">
            &nbsp;&nbsp;&nbsp;&nbsp;
            <a href="#" class="easyui-linkbutton" iconCls="icon-search" onclick="doSearch()">查 询</a>
        </div>
        <div style="margin-top: 15px">
            <#if permissionStrings?seq_contains("menu:edit")><a href="#" class="easyui-linkbutton" iconCls="icon-add" onclick="openAddDlg()" plain="true">新 增</a></#if>
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
                        save($('#addDlg'));
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
            <label class="label-top" style="text-align: right;width: 80px;display: inline-block;">应用系统：</label>
            <select id="applicationId" name="applicationId" class="easyui-combobox" style="width:200px;"
                    data-options="required:true,editable:false,onChange:function(){showType();},panelHeight:'auto',panelMaxHeight:200">
                <option value=""></option>
            <#list applicationList as application>
                <option value="${application.applicationId}">${application.appName}</option>
            </#list>
            </select>
        </div>
        <div class="type" style="margin-bottom:10px;">
            <label class="label-top" style="text-align: right;width: 80px;display: inline-block;">资源类型：</label>
            <select id="type" name="type" class="easyui-combobox" style="width:200px;"
                    data-options="required:true,editable:false,valueField:'typeId',textField:'typeName',onChange:function(newVal){showParent(newVal);},panelHeight:'auto',panelMaxHeight:200">
                <option value=""></option>
            </select>
        </div>
        <div class="resName" style="margin-bottom:10px;">
            <label class="label-top" style="text-align: right;width: 80px;display: inline-block;">资源名：</label>
            <input name="name" class="easyui-textbox" style="width: 200px;"
                   data-options="required:true,validType:'length[1,50]'"/>
        </div>
        <div style="margin-bottom:10px;">
            <label class="label-top" style="text-align: right;width: 80px;display: inline-block;">排序号：</label>
            <input name="displayOrder" class="easyui-numberspinner" style="width: 200px;"
                   data-options="spinAlign:'right',min:0">
            <a href="#" class="easyui-linkbutton easyui-tooltip" title="数值越大越靠后显示"
               data-options="plain:true,iconCls:'icon-help',position:'right'"></a>
        </div>
    </form>
</div>

<#--编辑-->
<div id="editDlg" class="easyui-dialog" title="编辑"
     style="width:500px;height:auto;padding:10px" data-options="
            cls:'dialog',
            resizable:true,
            closed:true,
            buttons: [{
                    text:'保存',
                    iconCls:'icon-ok',
                    handler:function(){
                        save($('#editDlg'));
                    }
                },{
                    text:'取消',
                    iconCls:'icon-cancel',
                    handler:function(){
                        $('#editDlg').dialog('close');
                    }
                }]">
    <form id="editForm">
        <input id="menuId" name="menuId" type="hidden"/>
        <div style="margin-bottom:10px;">
            <label class="label-top" style="text-align: right;width: 80px;display: inline-block;">应用系统：</label>
            <select id="applicationId" name="applicationId" class="easyui-combobox" style="width:200px;"
                    data-options="readonly:true,editable:false">
                <option value=""></option>
            <#list applicationList as application>
                <option value="${application.applicationId}">${application.appName}</option>
            </#list>
            </select>
        </div>
        <div class="type" style="margin-bottom:10px;">
            <label class="label-top" style="text-align: right;width: 80px;display: inline-block;">资源类型：</label>
            <select id="type" name="type" class="easyui-combobox" style="width:200px;"
                    data-options="readonly:true,editable:false">
                <option value=""></option>
                <option value="1">一级菜单</option>
                <option value="2">二级菜单</option>
                <option value="3">按钮</option>
            </select>
        </div>
        <div class="resName" style="margin-bottom:10px;">
            <label class="label-top" style="text-align: right;width: 80px;display: inline-block;">资源名：</label>
            <input id="name" name="name" class="easyui-textbox" style="width: 200px;"
                   data-options="required:true,validType:'length[1,50]'"/>
        </div>
        <div style="margin-bottom:10px;">
            <label class="label-top" style="text-align: right;width: 80px;display: inline-block;">排序号：</label>
            <input id="displayOrder" name="displayOrder" class="easyui-numberspinner" style="width: 200px;"
                   data-options="spinAlign:'right',min:0">
            <a href="#" class="easyui-linkbutton easyui-tooltip" title="数值越大越靠后显示"
               data-options="plain:true,iconCls:'icon-help',position:'right'"></a>
        </div>
    </form>
</div>

<script>
    function openAddDlg() {
        var $addForm = $("#addForm");
        $addForm.find(".url").remove();
        $addForm.find(".parentId").remove();
        $addForm.find(".dimensionId").remove();
        $addForm.form("reset");
        $addForm.find("#type").combobox('loadData', {});//清空option
        $("#addDlg").dialog('open');
    }

    function openEditDlg(index) {
        var $editForm = $("#editForm");
        $editForm.find(".url").remove();
        $editForm.find(".parentId").remove();
        $editForm.find(".dimensionId").remove();
        var rows = $("#dg").datagrid("getRows");
        var type = rows[index].type;
        var applicationId = rows[index].applicationId;
        var menuId = rows[index].menuId;
        if (type != 1) {
            //显示url文本框
            var urlDiv = "<div class='url' style='margin-bottom:10px;'><label class='label-top' style='text-align: right;width: 80px;display: inline-block;'>链接：</label> <input id='url' name='url' class='easyui-textbox' style='width: 200px;' data-options='required:true,validType:\"length[1,255]\"'/></div>";
            $editForm.find(".resName").after(urlDiv);
            $.parser.parse($editForm.find(".url"));//样式渲染
        }
        $editForm.find("#menuId").val(menuId);
        $editForm.find("#applicationId").combobox("setValue", applicationId);
        $editForm.find("#type").combobox("setValue", type);
        $editForm.find("#name").textbox("setValue", rows[index].name);
        $editForm.find("#url").textbox("setValue", rows[index].url);
        $editForm.find("#displayOrder").textbox("setValue", rows[index].displayOrder);
        $.post('${ctx}/menu/getParentMenuByType', {
            type: type,
            applicationId: applicationId
        }, function (result) {
            if (result.code != 0) {
                return $.messager.alert('错误', result.msg, 'error');
            }
            var parents = result.rows;
            if (parents.length > 0) {
                var str = "<div class='parentId' style='margin-bottom:10px;'><label class='label-top' style='text-align: right;width: 80px;display: inline-block;'>上级资源：</label> <select id='parentId' name='parentId' class='easyui-combobox' style='width:200px;' data-options='required:true,editable:false,panelHeight:\"auto\",panelMaxHeight:200'><option value=''></option>";
                for (var i = 0; i < parents.length; i++) {
                    str += "<option value=" + parents[i].menuId + ">" + parents[i].name + "</option>";
                }
                str += "</select></div>";
                $editForm.find(".type").after(str);
                $.parser.parse($editForm.find(".parentId"));//样式渲染
                $editForm.find("#parentId").combobox("setValue", rows[index].parentId);//回显
            }
        }, 'json');

        //显示数据维度下拉框
        if (type == 3) {
            $.post('${ctx}/dataDimension/getDataDimensions', {}, function (result) {
                if (result.code != 0) {
                    return $.messager.alert('错误', result.msg, 'error');
                }
                var dimensions = result.rows;
                if (dimensions.length > 0) {
                    var str = "<div class='dimensionId' style='margin-bottom:10px;'><label class='label-top' style='text-align: right;width: 80px;display: inline-block;'>数据维度：</label> <select id='dimensionIds' class='easyui-combobox' style='width:200px;' data-options='multiple:true,editable:false,panelHeight:\"auto\",panelMaxHeight:200'><option value=''></option>";
                    for (var i = 0; i < dimensions.length; i++) {
                        str += "<option value=" + dimensions[i].dimensionId + ">" + dimensions[i].displayName + "</option>";
                    }
                    str += "</select> <a href='#' class='easyui-linkbutton easyui-tooltip' title='可多选' data-options='plain:true,iconCls:\"icon-help\",position:\"right\"'></a></div>";
                    $editForm.find(".url").after(str);
                    $.parser.parse($editForm.find(".dimensionId"));//样式渲染
                    //下拉框回显
                    $.post('${ctx}/dataDimension/getDataDimensions', {menuId: menuId}, function (r) {
                        var arr = r.rows;
                        if (arr.length > 0) {
                            var ids = [];
                            $.each(arr, function (index, item) {
                                ids.push(item.dimensionId);
                            });
                            $editForm.find("#dimensionIds").combobox("setValues", ids);//回显
                        }
                    }, 'json');
                }
            }, 'json');
        }
        $("#editDlg").dialog('open');
    }

    function doSearch() {
        var params = $("#searchForm").formToJson();
        $("#dg").datagrid({queryParams: params});
    }

    function operate(value, row, index) {
       return "<#if permissionStrings?seq_contains("menu:edit")><a href='#' class='button-edit button-default' onclick='openEditDlg(" + index + ")'>编辑</a></#if>" +
              "<#if permissionStrings?seq_contains("menu:delete")> <a href='#' class='button-delete button-danger' onclick='deleteRecord(" + row.menuId + ")'>删除</a></#if>";

    }

    function formatTime(value, row, index) {
        return formatDate(value);
    }

    function save(dlg) {
        var $form = dlg.find("form");
        var formValid = $form.form('validate');
        if (!formValid) return;
        var params = $form.formToJson();
        var dimensionIds = [];
        if ($form.find("#dimensionIds").length > 0) {
            var arr = $form.find("#dimensionIds").combobox("getValues");
            $.each(arr, function (index, item) {
                if (item != "") dimensionIds.push(item);
            });
        }
        params.dimensionIds = dimensionIds.join(",");
        $.post('${ctx}/menu/edit', params, function (result) {
            if (result.code != 0) {
                return $.messager.alert('错误', result.msg, 'error');
            }
            dlg.dialog("close");
            $("#dg").datagrid("reload");
        }, 'json');
    }

    function showType() {
        var $addForm = $("#addForm");
        $addForm.find(".url").remove();
        $addForm.find(".parentId").remove();//删除上级资源下拉框
        $addForm.find(".dimensionId").remove();
        $addForm.find("#type").combobox('clear');//清除选中值
        $addForm.find("#type").combobox('loadData', [{typeId: 1, typeName: "一级菜单"}, {typeId: 2, typeName: "二级菜单"}, {
            typeId: 3,
            typeName: "按钮"
        }]);
    }

    function showParent(type) {
        var $addForm = $("#addForm");
        $addForm.find(".url").remove();
        $addForm.find(".parentId").remove();
        $addForm.find(".dimensionId").remove();
        if (type == 1 || type == "") return;//一级菜单没有上级资源
        var applicationId = $addForm.find("#applicationId").combobox("getValue");
        $.post('${ctx}/menu/getParentMenuByType', {type: type, applicationId: applicationId}, function (result) {
            if (result.code != 0) {
                return $.messager.alert('错误', result.msg, 'error');
            }
            var parents = result.rows;
            var str = "<div class='parentId' style='margin-bottom:10px;'><label class='label-top' style='text-align: right;width: 80px;display: inline-block;'>上级资源：</label> <select name='parentId' class='easyui-combobox' style='width:200px;' data-options='required:true,editable:false,panelHeight:\"auto\",panelMaxHeight:200'><option value=''></option>";
            for (var i = 0; i < parents.length; i++) {
                str += "<option value=" + parents[i].menuId + ">" + parents[i].name + "</option>";
            }
            str += "</select></div>";
            $addForm.find(".type").after(str);
            $.parser.parse($addForm.find(".parentId"));//样式渲染
        }, 'json');

        //显示url文本框
        var urlDiv = "<div class='url' style='margin-bottom:10px;'><label class='label-top' style='text-align: right;width: 80px;display: inline-block;'>链接：</label> <input name='url' class='easyui-textbox' style='width: 200px;' data-options='required:true,validType:\"length[1,255]\"'/></div>";
        $addForm.find(".resName").after(urlDiv);
        $.parser.parse($addForm.find(".url"));//样式渲染

        //显示数据维度下拉框
        if (type == 3) {
            $.post('${ctx}/dataDimension/getDataDimensions', {}, function (result) {
                if (result.code != 0) {
                    return $.messager.alert('错误', result.msg, 'error');
                }
                var dimensions = result.rows;
                if (dimensions.length > 0) {
                    var str = "<div class='dimensionId' style='margin-bottom:10px;'><label class='label-top' style='text-align: right;width: 80px;display: inline-block;'>数据维度：</label> <select id='dimensionIds' class='easyui-combobox' style='width:200px;' data-options='multiple:true,editable:false,panelHeight:\"auto\",panelMaxHeight:200'><option value=''></option>";
                    for (var i = 0; i < dimensions.length; i++) {
                        str += "<option value=" + dimensions[i].dimensionId + ">" + dimensions[i].displayName + "</option>";
                    }
                    str += "</select> <a href='#' class='easyui-linkbutton easyui-tooltip' title='可多选' data-options='plain:true,iconCls:\"icon-help\",position:\"right\"'></a></div>";
                    $addForm.find(".url").after(str);
                    $.parser.parse($addForm.find(".dimensionId"));//样式渲染
                }
            }, 'json');
        }
    }

    function deleteRecord(menuId) {
        $.messager.confirm("确认", "确认删除？", function (r) {
            if (r) {
                $.post('${ctx}/menu/delete', {menuId: menuId}, function (result) {
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