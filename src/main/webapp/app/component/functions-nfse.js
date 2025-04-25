function exec(o, f) {
    setTimeout(function () {o.value = f(o.value)}, 1);
}

function valdate(v) {
    if (v == null)
        return v;
    var v2 = v.replace(/\D/g, "");
    if (v2.length > 8 || v2.length <= 0) {
        v = null;
    } else {
        var dia = v2.substring(0,2);
        var mes = v2.substring(2,4);
        if (dia <= 0 || dia > 31) {
            v = null;
        }
        if (mes <= 0 || mes > 12) {
            v = null;
        }
    }
    return v;
}

function mdata(v) {
    v = v.replace(/\D/g, "");                    //Remove tudo o que não é dígito
    v = v.replace(/(\d{2})(\d)/, "$1/$2");
    v = v.replace(/(\d{2})(\d)/, "$1/$2");

    v = v.replace(/(\d{2})(\d{2})$/, "$1$2");
    if (v.toString().length > 10) {
        v = v.replace(v, v.toString().substring(0, 10));
    }
    if (v.replace(/\D/g, "").length == 8)
        v = valdate(v);

    return v;
}

function somenteNumeros(event, campo, permiteNegativo, permiteZero, permitePositivo) {
    permiteNegativo = permiteNegativo != null ? permiteNegativo : false;
    permiteZero = permiteZero != null ? permiteZero : false;
    permitePositivo = permitePositivo != null ? permitePositivo : true;

    var tecla = event.which || event.charCode || event.keyCode || 0;

    if (permiteNegativo == true) {
        campo.addEventListener("blur", function () {
            if (campo.value.trim() == '-') {
                campo.value = '';
            }
            if (parseInt(campo.value) == 0) {
                campo.value = 0;
            }
        });
    }

    if (permiteZero == false) {
        campo.addEventListener("blur", function () {
            if (parseInt(campo.value.trim()) == 0) {
                campo.value = '';
            }
        })
    }

    if (permitePositivo == false) {
        campo.addEventListener("blur", function () {
            if (parseInt(campo.value.trim()) > 0) {
                campo.value = '';
            }
        })
    }

    if (tecla == 9) {
        return true;
    }

    if (permiteNegativo && (tecla == 45 || tecla == 109)) {
        var valor = campo.value;
        if (valor.startsWith('-')) {
            valor = valor.replace('-', '');
            campo.value = valor;
            event.preventDefault();
            return;
        }

        if (valor.indexOf('-') <= 0) {
            valor = '-'.concat(valor);
            campo.value = valor;
            event.preventDefault();
            return;
        }
    }

    if (permiteNegativo == false && (tecla == 45 || tecla == 109)) {
        event.preventDefault();
        return;
    }

    if ((tecla > 47 && tecla < 58) || tecla == 45) {
        return true;
    } else {
        if (tecla == 8 || tecla == 0) return true;
        else event.preventDefault();
    }
}