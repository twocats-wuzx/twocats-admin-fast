const requiredValidate = function (value, item) {
    let othis = $(item);
    if (othis.attr('pause-verify')){
        return true
    }
    if (!value) {
        othis.addClass('error-form-item')
        othis.nextAll(".error-msg").html(othis.attr('lay-msg') || '必填项不能为空')
        return true
    }
    othis.removeClass('error-form-item')
    othis.nextAll(".error-msg").empty()
    return false
}

const emailValidate = function(value, item) {
    let othis = $(item);
    if (othis.attr('pause-verify') ){
        return true
    }
    const pattern = /^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+(.[a-zA-Z0-9_-])+/
    if (value && !pattern.test(value)) {
        othis.addClass('error-form-item')
        othis.nextAll(".error-msg").html(othis.attr('lay-msg') || '邮箱格式错误')
        return true
    }
    othis.removeClass('error-form-item')
    othis.nextAll(".error-msg").empty()
    return false
}

const phoneValidate = function phoneValidate(value, item) {
    let othis = $(item);
    if (othis.attr('pause-verify') ){
        return true
    }

    const pattern = /^1[3456789]\d{9}$/
    if (value && !pattern.test(value)) {
        othis.addClass('error-form-item')
        othis.nextAll(".error-msg").html(othis.attr('lay-msg') || '手机号格式错误')
        return true
    }
    othis.removeClass('error-form-item')
    othis.nextAll(".error-msg").empty()
    return false
}