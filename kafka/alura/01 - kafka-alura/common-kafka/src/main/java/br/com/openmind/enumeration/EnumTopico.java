package br.com.openmind.enumeration;

import java.util.Arrays;

public enum EnumTopico {

    ECOMMERCE_SEND_EMAIL("ECOMMERCE_SEND_EMAIL"),
    ECOMMERCE_NEW_ORDER("ECOMMERCE_NEW_ORDER"),
    ALL_TOPICS("ECOMMERCE.*"),
    ECOMMERCE_ORDER_REJECTED("ECOMMERCE_ORDER_REJECTED"),
    ECOMMERCE_ORDER_APPROVED("ECOMMERCE_ORDER_APPROVED"),
    USER_GENERATE_READING_REPORT("USER_GENERATE_READING_REPORT"),
    SEND_MESSAGE_TO_ALL_USERS("SEND_MESSAGE_TO_ALL_USERS"),
    ;

    private final String topico;

    EnumTopico(String topico) {
        this.topico = topico;
    }

    public String getTopico() {
        return topico;
    }

    public static EnumTopico findByName(String topico) {
        return Arrays.asList(EnumTopico.values()).stream().filter(topic -> topic.getTopico().equalsIgnoreCase(topico)).findAny().get();
    }

}
