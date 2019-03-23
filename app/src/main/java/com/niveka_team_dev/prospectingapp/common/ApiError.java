package com.niveka_team_dev.prospectingapp.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class ApiError {
    private int code;
    private String message;
    private Throwable throwable;

    public static String message(int code){
        String me;
        if (code == 404)
            me = "/!\\ 404 ".concat("Api not found. try later");
        if (code == 500)
            me = "/!\\ 500 ".concat("Internal Server Error. Please contact technical staff.");
        if (code == 107)
            me = "/!\\ ERROR: General database error";
        if (code == 403)
            me = "/!\\ 403 ".concat("Forbidden: Invalid access token[Access denied]. try later");
        if (code == 405)
            me = "/!\\ 405 ".concat("Method not supported. Please contact technical staff.");
        if (code == 501)
            me = "/!\\ 501 ".concat("Unauthorized.");
        if (code == 504)
            me = "/!\\ 504 ".concat("Gateway Timeout.");
        if (code == 599)
            me = "/!\\ 599 ".concat("Network Connect Timeout Error.");
        if (code == 503)
            me = "/!\\/!\\/!\\ 503 ".concat("Application server is down for maintenance. Pleace a few moment.");
        if (code == 406)
            me = "/!\\ 406".concat("Not Acceptable. Please contact technical staff.");
        if (code == 412)
            me = "/!\\ 412 ".concat("Precondition Failed. Please contact technical staff.");
        if (code == 415)
            me = "/!\\ 412 ".concat("Precondition Failed. Please contact technical staff.");
        if (code == 412)
            me = "/!\\ 415 ".concat("Unsupported Media Type. Please contact technical staff.");
        if (code == 408)
            me = "/!\\ 408 ".concat("Request Timeout. Try later.");
        if (code == 414)
            me = "/!\\ 414 ".concat("Request-URI Too Long. Please contact technical staff.");
        if (code == 451)
            me = "/!\\ 451 ".concat("Unavailable For Legal Reasons. Please contact technical staff.");
        if (code == 444)
            me = "/!\\ 444 ".concat("Connection Closed Without Response. Please contact technical staff.");
        else
            me = "/!\\ 500 Internal Server Error";
        return me;
    }

    public ApiError(Throwable throwable) {
        this.throwable = throwable;
    }

    public ApiError(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public ApiError() {
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Throwable getThrowable() {
        return throwable;
    }

    public void setThrowable(Throwable throwable) {
        this.throwable = throwable;
    }

    public static String inThrawable(Throwable error){
        String me = "",errorType = "";
        if (error instanceof IOException) {
            errorType = "Timeout";
            me = String.valueOf(error.getCause());
        }
        else if (error instanceof IllegalStateException) {
            errorType = "ConversionError";
            me = String.valueOf(error.getCause());
        } else {
            errorType = "Other Error";
            me = String.valueOf(error.getLocalizedMessage());
        }
        return errorType.concat(": ").concat(me);
    }

    public static String serverMessage(String json){
        String me = "";
        ServerMessage sm = new ServerMessage();
        ObjectMapper mapper = new ObjectMapper();
        try {
            sm = mapper.readValue(json,ServerMessage.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (sm.getTitle().toLowerCase().equals("unauthorized"))
            return sm.getDetail();
        return sm.getTitle();
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ServerMessage{
        public String errorKey;
        public String type;
        public String entityName;
        public String title;
        public String message;
        public String status;
        public String params;
        public String detail;

        public String getDetail() {
            return detail;
        }

        public void setDetail(String detail) {
            this.detail = detail;
        }

        public ServerMessage() {
        }

        public String getErrorKey() {
            return errorKey;
        }

        public void setErrorKey(String errorKey) {
            this.errorKey = errorKey;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getEntityName() {
            return entityName;
        }

        public void setEntityName(String entityName) {
            this.entityName = entityName;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getParams() {
            return params;
        }

        public void setParams(String params) {
            this.params = params;
        }
    }
}
