package com.shopsphere.constants;

/**
 * Centralised API endpoint constants.
 * Follows SOA/REST conventions with versioned paths.
 */
public final class APIEndpoints {
    private APIEndpoints() {}

    // ── Auth Service ──────────────────────────────
    public static final String AUTH_LOGIN     = "/auth/login";
    public static final String AUTH_LOGOUT    = "/auth/logout";
    public static final String AUTH_REFRESH   = "/auth/refresh";
    public static final String AUTH_REGISTER  = "/auth/register";

    // ── User Service ──────────────────────────────
    public static final String USERS          = "/users";
    public static final String USER_BY_ID     = "/users/{id}";
    public static final String USER_PROFILE   = "/users/{id}/profile";
    public static final String USER_ORDERS    = "/users/{id}/orders";

    // ── Product Service ───────────────────────────
    public static final String PRODUCTS       = "/products";
    public static final String PRODUCT_BY_ID  = "/products/{id}";
    public static final String PRODUCT_SEARCH = "/products/search";
    public static final String CATEGORIES     = "/products/categories";

    // ── Order Service ─────────────────────────────
    public static final String ORDERS         = "/orders";
    public static final String ORDER_BY_ID    = "/orders/{id}";
    public static final String ORDER_STATUS   = "/orders/{id}/status";

    // ── Cart Service ──────────────────────────────
    public static final String CART           = "/cart";
    public static final String CART_ITEMS     = "/cart/items";
    public static final String CART_ITEM_ID   = "/cart/items/{itemId}";

    // ── Health & Infra ────────────────────────────
    public static final String HEALTH         = "/health";
    public static final String HEALTH_LIVE    = "/health/live";
    public static final String HEALTH_READY   = "/health/ready";
}
