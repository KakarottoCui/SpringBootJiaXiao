//日期处理函数
function dateString(dateStr){

    if (typeof (dateStr)=='undefined'||dateStr==null){
        return '--'
    }
    var datetime=new Date(dateStr);
    var year = datetime.getFullYear();
    var month = datetime.getMonth() + 1 < 10 ? "0" + (datetime.getMonth() + 1) : datetime.getMonth() + 1;
    var date = datetime.getDate() < 10 ? "0" + datetime.getDate() : datetime.getDate();
    return year.toString()+ "-" + month.toString()+ "-" + date.toString();
}
//时间处理函数
function datetimeStr(dateStr){

    if (typeof (dateStr)=='undefined'){
        return '--'
    }
    var datetime=new Date(dateStr);
    var year = datetime.getFullYear();
    var month = datetime.getMonth() + 1 < 10 ? "0" + (datetime.getMonth() + 1) : datetime.getMonth() + 1;
    var date = datetime.getDate() < 10 ? "0" + datetime.getDate() : datetime.getDate();
    var hour = datetime.getHours()< 10 ? "0" + datetime.getHours() : datetime.getHours();
    var minute = datetime.getMinutes()< 10 ? "0" + datetime.getMinutes() : datetime.getMinutes();
    var second = datetime.getSeconds()< 10 ? "0" + datetime.getSeconds() : datetime.getSeconds();
    return year+ "-" + month+ "-" + date+" "+hour+":"+minute+":"+second;
}

function timerString(dateStr){
    var datetime=new Date(dateStr);
    var hour = datetime.getHours()< 10 ? "0" + datetime.getHours() : datetime.getHours();
    var minute = datetime.getMinutes()< 10 ? "0" + datetime.getMinutes() : datetime.getMinutes();
    return hour+":"+minute;
}


function getSex(str){

    if (str==0){
        return '男'
    }else {
        return "女"
    }

}


function shouimg() {

    $.ajax({
        url:"/login/getUserInfo",
        type:"post",
        cache:false,
        contentType:"application/json",
        datatype:"json",
        data:null,
        success:function(response) {
            $("#img").attr("src","../img/"+response.img);
            $("#usernameheader").text(response.name);
        }
        ,
        error:function(response){
            console.log("出错返回: " + response);
            console.log("出错数据: " + JSON.stringify(response));
        }
    });






}