//将秒（1433217710）格式化为日期（2015-6-2 12:1:50）
function formatDateWithNoSeconds(value, row, index) {
    if (value != null && value != "") {
        var dateTime = new Date(value * 1000);
        var result = dateTime.getFullYear() + "-";
        if ((dateTime.getMonth() + 1) < 10) {
            result += "0" + (dateTime.getMonth() + 1) + "-";
        } else {
            result += (dateTime.getMonth() + 1) + "-";
        }
        if (dateTime.getDate() < 10) {
            result += "0" + dateTime.getDate() + " ";
        } else {
            result += dateTime.getDate() + " ";
        }

        if (dateTime.getHours() < 10) {
            result += "0" + dateTime.getHours() + ":";
        } else {
            result += dateTime.getHours() + ":";
        }
        if (dateTime.getMinutes() < 10) {
            result += "0" + dateTime.getMinutes() + "";
        } else {
            result += dateTime.getMinutes() + "";
        }
        return result;
    }
    return "";
}
function formatDate(value, row, index) {
    if (value != null && value != "") {
        var dateTime = new Date(value * 1000);
        var result = dateTime.getFullYear() + "-";
        if ((dateTime.getMonth() + 1) < 10) {
            result += "0" + (dateTime.getMonth() + 1) + "-";
        } else {
            result += (dateTime.getMonth() + 1) + "-";
        }
        if (dateTime.getDate() < 10) {
            result += "0" + dateTime.getDate() + " ";
        } else {
            result += dateTime.getDate() + " ";
        }

        if (dateTime.getHours() < 10) {
            result += "0" + dateTime.getHours() + ":";
        } else {
            result += dateTime.getHours() + ":";
        }

        if (dateTime.getMinutes() < 10) {
            result += "0" + dateTime.getMinutes() + ":";
        } else {
            result += dateTime.getMinutes() + ":";
        }

        if (dateTime.getSeconds() < 10) {
            result += "0" + dateTime.getSeconds();
        } else {
            result += dateTime.getSeconds();
        }
        return result;
    }
    return "";
}

/**
 * 获取当天00:00:00时间
 * @returns {number}
 */
function getTodayStartSecond() {
    var date = new Date();
    date.setHours(0);
    date.setMinutes(0);
    date.setSeconds(0);
    date.setMilliseconds(0);
    return date.getTime() / 1000;
}

/**
 * 获取偏移offset的日期,精确到天
 * @param offset
 * @returns Date
 */
function getDayOffset(offset) {
    return new Date((getTodayStartSecond()+offset*86400)*1000);
}


/**
 * 获取当天23:59:59时间
 * @returns {number}
 */
function getTodayEndSecond() {
    var date = new Date();
    date.setHours(23);
    date.setMinutes(59);
    date.setSeconds(59);
    date.setMilliseconds(0);
    return date.getTime() / 1000;
}

function getDayOffsexDaySecond(daySecond,offset){
    return daySecond + offset * 86400;
}

function getTodayOffsexDaySecond(offset){
    return getTodayStartSecond() + offset * 86400;
}

/**
 * 阿拉伯数字转罗马数字
 * @param arabic
 * @returns {string}
 */
function arabicToRoman(arabic){
    var alpha = [ 'I', 'V', 'X', 'L', 'C', 'D', 'M' ], roman = "", bit = 0;
    while (arabic > 0){
        var tempnum = arabic % 10;
        switch (tempnum){
            case 3:{
                roman = alpha[bit] + roman;
                tempnum--;
            }
            case 2:{
                roman = alpha[bit] + roman;
                tempnum--;
            }
            case 1:{
                roman = alpha[bit] + roman;
                break;
            }
            case 4:{
                roman = alpha[bit + 1] + roman;
                roman = alpha[bit] + roman;
                break;
            }
            case 8:{
                roman = alpha[bit] + roman;
                tempnum--;
            }
            case 7:{
                roman = alpha[bit] + roman;
                tempnum--;
            }
            case 6:{
                roman = alpha[bit] + roman;
                tempnum--;
            }
            case 5:{
                roman = alpha[bit + 1] + roman;
                break;
            }
            case 9:{
                roman = alpha[bit + 2] + roman;
                roman = alpha[bit] + roman;
                break;
            }
            default:{
                break;
            }
        }
        bit += 2;
        arabic = Math.floor(arabic / 10);
    }
    return roman;
}

Date.prototype.pattern = function (fmt) {
    var o = {
        "M+": this.getMonth() + 1, //月份
        "d+": this.getDate(), //日
        "h+": this.getHours() % 12 == 0 ? 12 : this.getHours() % 12, //小时
        "H+": this.getHours(), //小时
        "m+": this.getMinutes(), //分
        "s+": this.getSeconds(), //秒
        "q+": Math.floor((this.getMonth() + 3) / 3), //季度
        "S": this.getMilliseconds() //毫秒
    };
    var week = {
        "0": "/u65e5",
        "1": "/u4e00",
        "2": "/u4e8c",
        "3": "/u4e09",
        "4": "/u56db",
        "5": "/u4e94",
        "6": "/u516d"
    };
    if (/(y+)/.test(fmt)) {
        fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    }
    if (/(E+)/.test(fmt)) {
        fmt = fmt.replace(RegExp.$1, ((RegExp.$1.length > 1) ? (RegExp.$1.length > 2 ? "/u661f/u671f" : "/u5468") : "") + week[this.getDay() + ""]);
    }
    for (var k in o) {
        if (new RegExp("(" + k + ")").test(fmt)) {
            fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
        }
    }
    return fmt;
}

function trimArray(arr){
    return $.grep(arr,function(n,i){
        return n!=''
    });
}

function trimArrayExcluding(arr,excluding){
    return $.grep(arr,function(n,i){
        return n!=''&&(!excluding||(excluding&&n!=excluding));
    });
}

function isExcelFile(filePath){
    if (!filePath) {
        //$.messager.alert("提示", "请选择要导入的Excel文件!");
        return false;
    }
    if ((!/\.xls$/.test(filePath))&&(!/\.xlsx$/.test(filePath))) {
        //$.messager.alert("文件格式错误", "请选择Excel文件");
        return false;
    }
    return true;

}
