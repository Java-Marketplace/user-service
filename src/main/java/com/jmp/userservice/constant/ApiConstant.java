package com.jmp.userservice.constant;

public final class ApiConstant {

    private static final String[] WHITE_LIST_URLS = {
            "/error",
            "/swagger-ui/**",
            "/swagger-resources/**",
            "/swagger-ui.html",
            "/v2/api-docs/**",
            "/v3/api-docs/**",
            "/actuator/**",
            "/favicon.ico"
    };

    public static final String BASE_USER_URL = "/api/v1/users";

    public static String[] getWhiteListUrls() {
        return WHITE_LIST_URLS;
    }

    private ApiConstant() {}
}
