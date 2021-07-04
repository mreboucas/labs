package br.com.openmind.enumeration;

public enum EnumTopico {

    ECOMMERCE_SEND_EMAIL("ECOMMERCE_SEND_EMAIL"),
    ECOMMERCE_NEW_ORDER("ECOMMERCE_NEW_ORDER"),
    ALL_TOPICS("ECOMMERCE.*"),
    ECOMMERCE_ORDER_REJECTED("ECOMMERCE_ORDER_REJECTED"),

    ECOMMERCE_ORDER_APPROVED("ECOMMERCE_ORDER_APPROVED"),;


    private final String topico;

    EnumTopico(String topico) {
        this.topico = topico;
    }

    public String getTopico() {
        return topico;
    }

}
