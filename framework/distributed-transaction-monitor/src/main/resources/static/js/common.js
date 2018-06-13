function timeStamp2String (time){
    var datetime = new Date();
    datetime.setTime(time);
    var year = datetime.getFullYear();
    var month = datetime.getMonth() + 1;
    month = month < 10 ? "0"+month:month;
    var date = datetime.getDate();
    date = date < 10 ? "0"+date:date;
    var hour = datetime.getHours();
    hour = hour < 10 ? "0"+hour:hour;
    var minute = datetime.getMinutes();
    minute = minute < 10 ? "0"+minute:minute;
    var second = datetime.getSeconds();
    second = second < 10 ? "0"+second:second;
    //var mseconds = datetime.getMilliseconds();
    //return year + "-" + month + "-" + date+" "+hour+":"+minute+":"+second+"."+mseconds;
    return year + "-" + month + "-" + date+" "+hour+":"+minute+":"+second;
}

function isNull(arg1){
    return !arg1 && arg1!==0 && typeof arg1!=="boolean"?true:false;
}

function clearForm(form) {
    // input清空
    $(':input', form).each(function () {
        var type = this.type;
        var tag = this.tagName.toLowerCase(); // normalize case
        if (type == 'text' || type == 'password' || tag == 'textarea')
            this.value = "";
        // 多选checkboxes清空
        // select 下拉框清空
        else if (tag == 'select')
            this.selectedIndex = -1;
    });
};