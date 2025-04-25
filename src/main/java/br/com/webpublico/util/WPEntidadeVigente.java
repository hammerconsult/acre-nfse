package br.com.webpublico.util;

import org.joda.time.DateTime;

public abstract class WPEntidadeVigente {
    public abstract DateTime getInicioVigencia();

    public abstract DateTime getFinalVigencia();

    public boolean isVigente() {
        return isVigente(DateTime.now());
    }

    public boolean isVigente(DateTime comparar) {
        if (getInicioVigencia() != null && getFinalVigencia() != null) {
            if (comparar.isAfter(getInicioVigencia()) && comparar.isBefore(comparar)) {
                return true;
            }
        }
        if (getInicioVigencia() != null && getFinalVigencia() == null) {
            if (comparar.isAfter(getInicioVigencia())) {
                return true;
            }
        }
        return false;
    }
}
