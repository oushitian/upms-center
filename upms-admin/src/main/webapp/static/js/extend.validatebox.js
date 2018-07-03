$.extend($.fn.validatebox.defaults.rules, {
    CHS: {
        validator: function (value, param) {
            return /^[\u0391-\uFFE5]+$/.test(value);
        },
        message: '请输入汉字'
    },
    ZIP: {
        validator: function (value, param) {
            return /^[1-9]\d{5}$/.test(value);
        },
        message: '邮政编码不存在'
    },
    QQ: {
        validator: function (value, param) {
            return /^[1-9]\d{4,10}$/.test(value);
        },
        message: 'QQ号码不正确'
    },
    mobile: {
        validator: function (value, param) {
            return /^((\(\d{2,3}\))|(\d{3}\-))?13\d{9}$/.test(value);
        },
        message: '手机号码不正确'
    },
    loginName: {
        validator: function (value, param) {
            return /^[\u0391-\uFFE5\w]+$/.test(value);
        },
        message: '登录名称只允许汉字、英文字母、数字及下划线。'
    },
    safepass: {
        validator: function (value, param) {
            return safePassword(value);
        },
        message: '密码由字母和数字组成，至少6位'
    },
    equalTo: {
        validator: function (value, param) {
            return value == $(param[0]).val();
        },
        message: '两次输入的字符不一至'
    },
    number: {   //验证整数
        validator: function (value, param) {
            return /^\d+$/.test(value);
        },
        message: '请输入数字'
    },
    digit: {   //验证整数
        validator: function (value, param) {
            return /^\d+(\.\d+)?$/.test(value);
        },
        message: '请输入整数或小数'
    },
    currency : {// 验证货币
        validator : function(value) {
            var reg = /^[0-9]{1}\d*(\.\d{1,2})?$/;
            return reg.test(value) && value > 0;
        },
        message : '数字格式不正确,例: 123.78'
    },
    currencyNull: {// 验证货币,可以为空
        validator : function(value) {
            var reg = /^[0-9]{1}\d*(\.\d{1,2})?$/;
            return !value || (reg.test(value) && value >= 0);
        },
        message : '数字格式不正确,例: 123.78'
    },
    idcard: {
        validator: function (value, param) {
            return idCard(value);
        },
        message: '请输入正确的身份证号码'
    },
    valiName : { //验证物流公司名称是否有效
        validator : function(value, param) {
            var shippingId = ($('#shippingId').val());
            var bl=false;
            $.ajax({
                type:'POST',
                async:false,
                dateType:'json',
                url:'validatename',
                data:'shippingName='+value+"&shippingId="+shippingId,
                success:function(result){
                    bl=result.res;
                }
            });
            return bl;
        },
        message : '该物流公司名称已重复'
    },
    valiShortName : { //验证物流公司简称是否有效
        validator : function(value, param) {
            var shippingId = ($('#shippingId').val());
            var bl=false;
            $.ajax({
                type:'POST',
                async:false,
                dateType:'json',
                url:'validatename',
                data:'shippingName='+value+"&shippingId="+shippingId+"&type=shortname",
                success:function(result){
                    bl=result.res;
                }
            });
            return bl;
        },
        message : '该物流公司简称已重复'
    },
    valiAreaName : { //验证物流区域名称是否有效
        validator : function(value, param) {
            var areaId = $('#id').val();
            var shippingId = $('#shippingId').combobox('getValue');
            var bl=false;
            $.ajax({
                type:'POST',
                async:false,
                dateType:'json',
                url:'validatename',
                data:'areaName='+value+"&areaId="+areaId+"&shippingId="+shippingId,
                success:function(result){
                    bl=result.res;
                }
            });
            return bl;
        },
        message : '该物流公司简称已重复'
    },
    MTCN:{
        validator: function (value, param) {
            return /^\d+$/.test(value) && value.length == 10;
        },
        message: '请输入10位数字'
    },code: {
        validator: function (value, param) {
            return code(value);
        },
        message: '请输入字母和数字'
    }
//    ,dateCur: {
//        validator: function(value, param){
//            var today =new Date();
//            var curValue= value.replace("/","-")
//            var curDay = new Date(curValue.replace(/-/,"/"))
//
//            varify = today<curValue
//            return varify;
//        },
//        message: '开始时间不能小于当前时间！'
//    }
    ,dateGe: {
        validator: function(value, param){
            var startTime2 = $(param[0]).datebox('getValue');
            var d1 = $.fn.datebox.defaults.parser(startTime2);
            var d2 = $.fn.datebox.defaults.parser(value);
            varify=d2>=d1;
            return varify;

        },
        message: '结束时间不能小于开始时间！'
    }

     ,numberGt: {
        validator: function(value, param){
            var number1 = $(param[0]).textbox('getValue');
            if(isNaN(number1) || isNaN(value))return false;
            var n1 = parseFloat(number1);
            var n2 = parseFloat(value);
            if(!n1 && n2) return true;
            varify=n2>n1;
            return varify;

        },
        message: '必须大于 {1}！'
    },numberGe: {
        validator: function(value, param){
            var number1 = $(param[0]).textbox('getValue');
            if(isNaN(number1) || isNaN(value))return false;
            var n1 = parseFloat(number1);
            var n2 = parseFloat(value);
            if(!n1 && n2) return true;
            varify=n2>=n1;
            return varify;

        },
        message: '不能小于 {1} ！'
    },maxLength:{
        validator:function(value,param){
        var len=$.trim(value).length;
        return len<=param[0];
        },
        message:"最多输入{0}个字符"
    }
});

$.fn.validatebox.defaults.rules.length.message = '长度必须为 {0} 到 {1}.';

/* 密码由字母和数字组成，至少6位 */
var safePassword = function (value) {
    return !(/^(([A-Z]*|[a-z]*|\d*|[-_\~!@#\$%\^&\*\.\(\)\[\]\{\}<>\?\\\/\'\"]*)|.{0,5})$|\s/.test(value));
};

var idCard = function (value) {
    if (value.length == 18 && 18 != value.length) return false;
    var number = value.toLowerCase();
    var d, sum = 0, v = '10x98765432', w = [7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2], a = '11,12,13,14,15,21,22,23,31,32,33,34,35,36,37,41,42,43,44,45,46,50,51,52,53,54,61,62,63,64,65,71,81,82,91';
    var re = number.match(/^(\d{2})\d{4}(((\d{2})(\d{2})(\d{2})(\d{3}))|((\d{4})(\d{2})(\d{2})(\d{3}[x\d])))$/);
    if (re == null || a.indexOf(re[1]) < 0) return false;
    if (re[2].length == 9) {
        number = number.substr(0, 6) + '19' + number.substr(6);
        d = ['19' + re[4], re[5], re[6]].join('-');
    } else d = [re[9], re[10], re[11]].join('-');
    if (!isDateTime.call(d, 'yyyy-MM-dd')) return false;
    for (var i = 0; i < 17; i++) sum += number.charAt(i) * w[i];
    return (re[2].length == 9 || number.charAt(17) == v.charAt(sum % 11));
};

var isDateTime = function (format, reObj) {
    format = format || 'yyyy-MM-dd';
    var input = this, o = {}, d = new Date();
    var f1 = format.split(/[^a-z]+/gi), f2 = input.split(/\D+/g), f3 = format.split(/[a-z]+/gi), f4 = input.split(/\d+/g);
    var len = f1.length, len1 = f3.length;
    if (len != f2.length || len1 != f4.length) return false;
    for (var i = 0; i < len1; i++) if (f3[i] != f4[i]) return false;
    for (var i = 0; i < len; i++) o[f1[i]] = f2[i];
    o.yyyy = s(o.yyyy, o.yy, d.getFullYear(), 9999, 4);
    o.MM = s(o.MM, o.M, d.getMonth() + 1, 12);
    o.dd = s(o.dd, o.d, d.getDate(), 31);
    o.hh = s(o.hh, o.h, d.getHours(), 24);
    o.mm = s(o.mm, o.m, d.getMinutes());
    o.ss = s(o.ss, o.s, d.getSeconds());
    o.ms = s(o.ms, o.ms, d.getMilliseconds(), 999, 3);
    if (o.yyyy + o.MM + o.dd + o.hh + o.mm + o.ss + o.ms < 0) return false;
    if (o.yyyy < 100) o.yyyy += (o.yyyy > 30 ? 1900 : 2000);
    d = new Date(o.yyyy, o.MM - 1, o.dd, o.hh, o.mm, o.ss, o.ms);
    var reVal = d.getFullYear() == o.yyyy && d.getMonth() + 1 == o.MM && d.getDate() == o.dd && d.getHours() == o.hh && d.getMinutes() == o.mm && d.getSeconds() == o.ss && d.getMilliseconds() == o.ms;
    return reVal && reObj ? d : reVal;
    function s(s1, s2, s3, s4, s5) {
        s4 = s4 || 60, s5 = s5 || 2;
        var reVal = s3;
        if (s1 != undefined && s1 != '' || !isNaN(s1)) reVal = s1 * 1;
        if (s2 != undefined && s2 != '' && !isNaN(s2)) reVal = s2 * 1;
        return (reVal == s1 && s1.length != s5 || reVal > s4) ? -10000 : reVal;
    }
};

//仅输入字母及数字
var code = function (value) {
    return /^[0-9a-zA-Z]*$/g.test(value);
};

