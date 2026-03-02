package com.shopsphere.constants;

public final class AppConstants {
    private AppConstants() {}

    // Timeouts
    public static final int DEFAULT_TIMEOUT_SEC  = 20;
    public static final int SHORT_TIMEOUT_SEC    = 5;
    public static final int LONG_TIMEOUT_SEC     = 60;
    public static final int PAGE_LOAD_TIMEOUT    = 30;

    // Test Tags
    public static final String TAG_SMOKE         = "@smoke";
    public static final String TAG_SANITY        = "@sanity";
    public static final String TAG_REGRESSION    = "@regression";
    public static final String TAG_E2E           = "@e2e";
    public static final String TAG_UAT           = "@uat";
    public static final String TAG_API           = "@api";
    public static final String TAG_MOBILE        = "@mobile";
    public static final String TAG_CONTRACT      = "@contract";

    // Test Data
    public static final String VALID_USER        = "standard_user";
    public static final String LOCKED_USER       = "locked_out_user";
    public static final String PROBLEM_USER      = "problem_user";
    public static final String PERF_GLITCH_USER  = "performance_glitch_user";
    public static final String DEFAULT_PASSWORD  = "secret_sauce";

    // UI Selectors fallback
    public static final String WAIT_FOR_ELEMENT  = "Waiting for element: {}";
}
