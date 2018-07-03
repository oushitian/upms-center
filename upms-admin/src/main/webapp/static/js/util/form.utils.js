/**
 * Created by WIN7 on 2016/10/13.
 */
//form表单自动封装成json
$.fn.formToJson = function () {
    var o = {};
    var a = this.serializeArray();
    $.each(a, function () {
        if (o[this.name]) {
            if (!o[this.name].push) {
                o[this.name] = [o[this.name]];
            }
            o[this.name].push($.trim(this.value || ''));
        } else {
            o[this.name] = $.trim(this.value || '');
        }
    });
    return o;
};

$.fn.serializeObject = function () {
    return this.formToJson();
};


//封装json,并过滤key的前缀
$.fn.formToJsonWithFilter = function (prefix) {
    var o = {};
    var a = this.serializeArray();
    $.each(a, function () {
        o[this.name.replace(prefix,'')] = $.trim(this.value || '');
    });
    return o;
};

//带组别的表单封装成json,例如封装组别:sales,sales.isShow==1,结果为:{"isShow":"1"}
$.fn.formToJsonWithGroup = function (group) {
    var o = {};
    var a = this.serializeArray();
    $.each(a, function () {
        var name = this.name;
        var nameArray = name.split(".");
        var prefix = nameArray[0];
        if(nameArray.length<=1&&prefix!=group){
            return true;//下个循环
        }
        //取.后的字段名
        name = nameArray[1];
        if (o[name]) {
            if (!o[name].push) {
                o[name] = [o[name]];
            }
            o[name].push($.trim(this.value || ''));
        } else {
            o[name] = $.trim(this.value || '');
        }
    });
    return o;
};
//生成不带组别字段的json
$.fn.formToJsonWithoutGroup = function () {
    var o = {};
    var a = this.serializeArray();
    $.each(a, function () {
        var name = this.name;
        var nameArray = name.split(".");
        if(nameArray.length>1){
            return true;//下个循环
        }
        if (o[name]) {
            if (!o[name].push) {
                o[name] = [o[name]];
            }
            o[name].push($.trim(this.value || ''));
        } else {
            o[name] = $.trim(this.value || '');
        }
    });
    return o;
};


//自动替换文本带表达式$name$的字符为json中的对象值,obj为json对象
String.prototype.zRender = function (obj) {
    if ((obj + "") == "undefined") {
        obj = {};
    }
    return this.replace(/\$\w+\$/gi, function (matchs) {
        var returns = obj[matchs.replace(/\$/g, "")];
        return (returns + "") == "undefined" ? "" : returns;
    });
};


function setElementValue(name, value) {
    $("[name=" + name + "]").val(value);
}
function setTextboxValue(selector, value) {
    if(typeof(value)!='undefined' )
        $(selector).textbox("setValue", value);
}

function setSubCheckboxValue(parentEle, name, isChecked) {
    if (isChecked == 1) {
        $(parentEle).find("[name=" + name + "]").attr("checked", true);
    } else {
        $(parentEle).find("[name=" + name + "]").attr("checked", false);
    }
}
function setSubRadioValue(parentEle, name, value) {
    $(parentEle).find('input:radio[name="+name+"]').eq(value).attr("checked", true);
}

function setSubTextboxValue(parentEle, name, value) {
    $(parentEle).find("[name=" + name + "]").textbox("setValue", value);
}

function setSubElementValue(parentEle, name, value) {
    if(typeof(value)!='undefined' )
        $(parentEle).find("[name=" + name + "]").val(value);
}

function setSubElementData(parentEle, name,dataName, value) {
    if(typeof(value)!='undefined' ) {
        $(parentEle).find("[name=" + name + "]").data(dataName,value);
    }
}

//是否为空
function isBlank(str) {
    if (str == undefined || str == '' || str == null || str == NaN) {
        return true;
    }
    return false;
}

//正整数
function isPInt(str) {
    if (!(str)) {
        return false;
    }
    var g = /^[1-9]*[1-9][0-9]*$/;
    return g.test(str);
}
//整数
function isInt(str) {
    if (isBlank(str)) {
        return false;
    }
    var g = /^-?\d+$/;
    return g.test(str);
}


function isValidInt(str,min,max) {
    if(isInt(str)){
        if(parseInt(str)<min||parseInt(str)>max){
            return false;
        }else{
            return true;
        }
    }
    return false;
}

function isEn(str) {
    if (isBlank(str)) {
        return false;
    }
    var g = /^[A-Za-z, ]+$/;
    return g.test(str.trim());
}
