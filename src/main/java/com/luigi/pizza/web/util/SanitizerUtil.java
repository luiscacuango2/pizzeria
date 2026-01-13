package com.luigi.pizza.web.util; // <--- Verifica que esta línea sea idéntica

import org.springframework.web.util.HtmlUtils;
import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;

public class SanitizerUtil {

    public static String sanitize(String input) {
        if (input == null || input.isBlank()) {
            return input;
        }
        // 1. Limpieza profunda con Jsoup (Elimina scripts y tags no deseados)
        String clean = Jsoup.clean(input, Safelist.none());

        // 2. Escapado de caracteres HTML para mayor seguridad
        return HtmlUtils.htmlEscape(clean);
    }
}