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
        url:'${ctx}/dataDimension/listDimensionValueData',
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
        <th data-options="field:'dimensionDisplayName',width:150">所属维度</th>
        <th data-options="field:'displayName',width:150">维度值名称</th>
        <th data-options="field:'attributeValue',width:150">属性值</th>
        <th data-options="field:'gmtModified',width:120,formatter:formatTime">最后操作时间</th>
        <th data-options="field:'modifier',width:120">最后操作人</th>
        <th data-options="field:'dimensionOperate',width:150,align:'center',formatter:dimensionOperate">操作</th>
    </tr>
    </thead>
</table>
<div id="tb">
    <form id="searchForm">
        <div>
            <select class="easyui-combobox" name="dimensionId" data-options="label:'所属维度'" style="width:200px;">
                <option value="">全部</option>
            <#list dataDimensionList as dataDimensionn>
                <option value="${dataDimensionn.dimensionId}">${dataDimensionn.displayName}</option>
            </#list>
            </select>
            &nbsp;&nbsp;&nbsp;&nbsp;
            <input class="easyui-textbox" name="displayName" data-options="label:'维度值名称'" style="width:200px;">
            &nbsp;&nbsp;&nbsp;&nbsp;
            <a href="#" class="easyui-linkbutton" iconCls="icon-search" onclick="doSearch()">查 询</a>
        </div>
        <div style="margin-top: 15px">
            <#if permissionStrings?seq_contains("dataDimension:saveDimensionValue")><a href="#" class="easyui-linkbutton" iconCls="icon-add" onclick="openAddDlg()" plain="true">新 增</a></#if>
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
        <input id="recId" name="recId" type="hidden"/>
        <div style="margin-bottom:10px;">
            <label class="label-top" style="text-align: right;width: 100px;display: inline-block;">所属维度：</label>
            <select id="dimensionId" name="dimensionId" class="easyui-combobox" style="width:200px;"
                    data-options="required:true,editable:false,panelHeight:'auto',panelMaxHeight:200">
                <option value=""></option>
            <#list dataDimensionList as dataDimensionn>
                <option value="${dataDimensionn.dimensionId}">${dataDimensionn.displayName}</option>
            </#list>
            </select>
        </div>
        <div style="margin-bottom:10px;">
            <label class="label-top" style="text-align: right;width: 100px;display: inline-block;">维度值名称：</label>
            <input id="displayName" name="displayName" class="easyui-textbox" style="width: 200px;"
                   data-options="required:true,validType:'length[1,50]'"/>
        </div>
        <div style="margin-bottom:10px;">
            <label class="label-top" style="text-align: right;width: 100px;display: inline-block;">属性值：</label>
            <input id="attributeValue" name="attributeValue" class="easyui-textbox" style="width: 200px;"
                   data-options="required:true,validType:'length[1,50]'"/>
        </div>
    </form>
</div>

<script>
    var url;
    function openAddDlg() {
        $("#addForm").find("#recId").val("");
        $("#addForm").form("reset");
        $("#addDlg").dialog('open').dialog('setTitle', '新增数据维度值');
        url = '${ctx}/dataDimension/saveDimensionValue';
    }

    function openEditDlg(index) {
        $("#addForm").form("reset");
        var rows = $("#dg").datagrid("getRows");
        $("#addForm").find("#recId").val(rows[index].recId);
        $('#dimensionId').combobox('select',rows[index].dimensionId);
        $("#displayName").textbox("setValue", rows[index].displayName);
        $("#attributeValue").textbox("setValue", rows[index].attributeValue);
        url = '${ctx}/dataDimension/doModifyDimensionValue';
        $("#addDlg").dialog({title: "编辑数据维度"});
        $("#addDlg").dialog('open');
    }

    function doSearch() {
        var params = $("#searchForm").formToJson();
        $("#dg").datagrid({queryParams: params});
    }

    function dimensionOperate(value, row, index) {
        return "<#if permissionStrings?seq_contains("dataDimension:doModifyDimensionValue")> <a href='#' class='button-edit button-default' onclick='openEditDlg(" + index + ")'>编辑</a>  </#if>"+
        "<#if permissionStrings?seq_contains("dataDimension:deleteDimensionValue")><a href='#' class='button-delete button-danger' onclick='deleteDimensionValue(" + row.recId + ")'>删除</a> </#if>";

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

    function deleteDimensionValue(recId) {
        $.messager.confirm("确认", "确认删除？", function (r) {
            if (r) {
                $.post('${ctx}/dataDimension/deleteDimensionValue', {recId: recId}, function (result) {
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