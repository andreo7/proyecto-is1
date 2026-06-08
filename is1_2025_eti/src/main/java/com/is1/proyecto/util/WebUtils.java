package com.is1.proyecto.util;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * Utilidades comunes para los controllers web.
 */
public final class WebUtils {

    private WebUtils() {}

    /**
     * Codifica un string para usarlo de forma segura como valor
     * de query parameter en una URL de redirección.
     * Convierte tildes, ñ, espacios y otros caracteres especiales.
     */
    public static String encode(String s) {
        if (s == null) return "";
        return URLEncoder.encode(s, StandardCharsets.UTF_8);
    }
}