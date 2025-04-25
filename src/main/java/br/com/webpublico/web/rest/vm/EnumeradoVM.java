package br.com.webpublico.web.rest.vm;

/**
 * Created by romanini on 25/06/2018.
 */
public class EnumeradoVM {
    private String name;
    private String value;

    public EnumeradoVM() {
    }

    public EnumeradoVM(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
