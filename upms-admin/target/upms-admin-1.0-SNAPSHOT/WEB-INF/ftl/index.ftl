<!doctype html>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="edge"/>
    <title>首页</title>
    <link rel="stylesheet" href="${static}/insdep/easyui.css" type="text/css"/>
    <link rel="stylesheet" href="${static}/insdep/easyui_animation.css" type="text/css"/>
    <link rel="stylesheet" href="${static}/insdep/easyui_plus.css" type="text/css"/>
    <link rel="stylesheet" href="${static}/insdep/insdep_theme_default.css" type="text/css"/>
    <link rel="stylesheet" href="${static}/insdep/icon.css" type="text/css"/>
    <link rel="stylesheet" href="${static}/css/item.css" type="text/css"/>
    <link rel="stylesheet" href="${static}/plugin/font-awesome-4.7.0/css/font-awesome.min.css" type="text/css"/>
    <script src="${static}/js/jquery.min.js" type="text/javascript"></script>
    <script src="${static}/js/jquery.cookie.js" type="text/javascript"></script>
    <script src="${static}/js/jquery.easyui.1.5.min.js" type="text/javascript"></script>
    <script src="${static}/js/insdep/jquery.insdep-extend.min.js" type="text/javascript"></script>
    <script src="${static}/js/jquery.md5.js" type="text/javascript"></script>
    <script>
        window.JOLLY_CONFIG_PARENT = true;
    </script>
</head>

<body>
<!--loading-->
<script src="${static}/js/page.loading.mask.js" type="text/javascript"></script>
<div id="master-layout">
    <div data-options="region:'north',border:false,bodyCls:'theme-header-layout'">
        <div class="theme-navigate">
            <div class="left">
                <a href="#" class="easyui-linkbutton left-control-switch"><i class="fa fa-bars fa-lg"></i></a>
            </div>
            <div class="right">
                <a href="#" class="easyui-menubutton theme-navigate-more-button"
                   data-options="menu:'#more',hasDownArrow:false"></a>
                <div id="more" class="theme-navigate-more-panel">
                    <div><a href="${ctx}/logout?appKey=upms&token=${token}" plain="true">退出登录</a></div>
                    <div><a href="${ctx}/user/getPermitApp" plain="true">切换应用</a></div>
                  <!--  <div><a href="#" onclick="openChangePwdDlg()" plain="true">修改密码</a></div> -->
                </div>
            </div>
        </div>
    </div>

    <!--开始左侧菜单-->
    <div data-options="region:'west',border:false,bodyCls:'theme-left-layout'" style="width:200px;">
        <!--正常菜单-->
        <div class="theme-left-normal">
            <!--theme-left-switch 如果不需要缩进按钮，删除该对象即可-->
            <div class="left-control-switch theme-left-switch"><i class="fa fa-chevron-left fa-lg"></i></div>

            <!--start class="easyui-layout"-->
            <div class="easyui-layout" data-options="border:false,fit:true">
                <!--start region:'north'-->
                <div data-options="region:'north',border:false" style="height:120px;">
                    <!--start theme-left-user-panel-->
                    <div class="theme-left-user-panel">
                        <dl>
                            <dt>
                                <img src="${static}/insdep/images/portrait86x86.png" width="43" height="43">
                            </dt>
                            <dd>
                                <b class="badge-prompt">${user.userName}</b>
                            <#list roleList as role>
                                <span>${role.name}</span>
                            </#list>
                            </dd>
                        </dl>
                    </div>
                    <!--end theme-left-user-panel-->
                </div>
                <!--end region:'north'-->

                <!--start region:'center'-->
                <div data-options="region:'center',border:false">
                    <!--start easyui-accordion-->
                    <div class="easyui-accordion" data-options="border:false,fit:true">
                    <#list userMenus_ as e>
                        <div title="${e.name}">
                            <ul class="easyui-datalist" id="menu_datalist"
                                data-options="border:false,fit:true,rowStyler:function(){return 'cursor:pointer';},onClickRow:function(index,row){open(index,row);}">
                                <#list e.childrenMenus as child>
                                    <li value="${child.url}">${child.name}</li>
                                </#list>
                                <!--
                                <li value="/app/list">应用管理1</li>
                                <li value="/dataDimension/list">数据维度管理1</li>
                                <li value="/dataDimension/listDimensionValues">数据维度值管理1</li>
                                <li value="/menu/list">资源管理1</li>
                                <li value="/menuGroup/list">权限组管理1</li>
                                <li value="/role/list">角色管理1</li>
                                <li value="/log/list">操作日志1</li>
                                <li value="/user/list">用户管理1</li> -->
                            </ul>
                        </div>
                    </#list>
                    <#--<#list menus as it>-->
                    <#--<div title="${it.name}" style="overflow:auto;">-->
                    <#--<ul class="easyui-tree" id="menu_tree_${it.id}" data-options="-->
                    <#--onClick:function(node){-->
                    <#--if(node.attributes){-->
                    <#--Open(node.text,node.attributes.url);-->
                    <#--}-->
                    <#--}-->
                    <#--"></ul>-->
                    <#--</div>-->
                    <#--</#list>-->

                    </div>
                    <!--end easyui-accordion-->
                </div>
                <!--end region:'center'-->
            </div>
            <!--end class="easyui-layout"-->
        </div>
        <!--最小化菜单-->
        <div class="theme-left-minimal">
            <ul class="easyui-datalist" data-options="border:false,fit:true">
                <li><i class="fa fa-home fa-2x"></i>
                    <p>主题</p></li>
                <li><i class="fa fa-book fa-2x"></i>
                    <p>组件</p></li>
                <li><i class="fa fa-pencil fa-2x"></i>
                    <p>编辑</p></li>
                <li><i class="fa fa-cog fa-2x"></i>
                    <p>设置</p></li>
                <li><a class="left-control-switch"><i class="fa fa-chevron-right fa-2x"></i>
                    <p>打开</p></a></li>
            </ul>
        </div>
    </div>
    <!--结束左侧菜单-->

    <div data-options="region:'center',border:false" id="control"
         style="padding:20px; background:#fff;overflow: hidden">
        <iframe id="result" src="${ctx}/user/list" width="100%" height="100%" frameborder="no" border="0">
        </iframe>
    </div>
<#--新增/编辑-->
    <div id="changePwdDlg" class="easyui-dialog"
         style="width:480px;height:390px;padding:10px" data-options="
            cls:'dialog',
            resizable:true,
            closed:true,
            buttons: [{
                    text:'保存',
                    iconCls:'icon-ok',
                    handler:function(){
                        changePwd();
                    }
                },{
                    text:'取消',
                    iconCls:'icon-cancel',
                    handler:function(){
                        $('#changePwdDlg').dialog('close');
                    }
                }]">
        <form id="changePwdForm">
            <div style="margin-bottom:10px;">
                <label class="label-top" style="text-align: right;width: 80px;display: inline-block;">用户名：</label>
                <label class="label-top" style="text-align: right;width: 80px;display: inline-block;">${user.userName}</label>
            </div>
            <div style="margin-bottom:10px;">
                <label class="label-top" style="text-align: right;width: 80px;display: inline-block;">旧密码：</label>
                <input id="password" name="password" class="easyui-textbox" style="width: 200px;"
                       data-options="required:true,validType:'password'"/>
            </div>
            <div style="margin-bottom:10px;">
                <label class="label-top" style="text-align: right;width: 80px;display: inline-block;">新密码：</label>
                <input id="newPassword" name="newPassword" class="easyui-textbox" style="width: 200px;"
                       data-options="required:true,validType:'password'"/>
            </div>
        </form>
    </div>


</div>

<script>
    var url;
    $(function () {
        /*布局部分*/
        $('#master-layout').layout({
            fit: true/*布局框架全屏*/
        });
        /*右侧菜单控制部分*/
        var left_control_status = true;
        var left_control_panel = $("#master-layout").layout("panel", 'west');

        $(".left-control-switch").on("click", function () {
            if (left_control_status) {
                left_control_panel.panel('resize', {width: 70});
                left_control_status = false;
                $(".theme-left-normal").hide();
                $(".theme-left-minimal").show();
            } else {
                left_control_panel.panel('resize', {width: 200});
                left_control_status = true;
                $(".theme-left-normal").show();
                $(".theme-left-minimal").hide();
            }
            $("#master-layout").layout('resize', {width: '100%'})
        });
        /*右侧菜单控制结束*/
        /*$("#open-layout").on("click",function(){
                var option = {
                    "region":"west",
                    "split":true,
                    "title":"title",
                    "width":180
                };
                $('#master-layout').layout('add', option);
                auth_token

        });*/
    });
    function open(index, row) {
        $('#result').attr('src', '${ctx}' + row.value);
    }
    function openChangePwdDlg() {
        $("#changePwdForm").form("reset");
        $("#changePwdDlg").dialog('open').dialog('setTitle','修改密码');
        url = '${ctx}/authenticationAccess/password';
    }
    function changePwd() {
        var $changePwdForm = $("#changePwdForm");
        var formValid =   $changePwdForm.form('validate');
        if (!formValid) return;
        var params = $changePwdForm.formToJson();
        $.post(url, params, function (result) {
            if (result.code != 0) {
                return $.messager.alert('错误', result.msg, 'error');
            }
            $("#changePwdDlg").dialog("close");
        }, 'json');
    }
</script>
<!--第三方插件加载-->
<script src="${static}/js/extend.validatebox.js" type="text/javascript"></script>
<script src="${static}/js/util.js" type="text/javascript"></script>
<script src="${static}/js/util/form.utils.js" type="text/javascript"></script>
</body>
</html>
