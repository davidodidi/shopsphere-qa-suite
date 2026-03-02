package com.shopsphere.constants;

/**
 * HTTP status code constants for assertion readability.
 * Documents expected HTTP protocol responses per RFC 9110.
 */
public final class HTTPStatus {
    private HTTPStatus() {}

    // 2xx Success
    public static final int OK           = 200;
    public static final int CREATED      = 201;
    public static final int ACCEPTED     = 202;
    public static final int NO_CONTENT   = 204;

    // 3xx Redirection
    public static final int MOVED        = 301;
    public static final int FOUND        = 302;
    public static final int NOT_MODIFIED = 304;

    // 4xx Client Errors
    public static final int BAD_REQUEST  = 400;
    public static final int UNAUTHORIZED = 401;
    public static final int FORBIDDEN    = 403;
    public static final int NOT_FOUND    = 404;
    public static final int CONFLICT     = 409;
    public static final int UNPROCESSABLE= 422;
    public static final int TOO_MANY_REQ = 429;

    // 5xx Server Errors
    public static final int SERVER_ERROR = 500;
    public static final int BAD_GATEWAY  = 502;
    public static final int UNAVAILABLE  = 503;
}
