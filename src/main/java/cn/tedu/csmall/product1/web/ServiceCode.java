package cn.tedu.csmall.product1.web;



public enum ServiceCode {

    OK(20000),

    ERROR_BAD_REQUEST(40000),

    ERROR_NOT_FOUND(40400),

    ERROR_CONFLICT(40900),

    ERROR_UNAUTHORIZED(40100),

    ERROR_UNAUTHORIZED_DISABLED(40101),

    ERROR_FORBIDDEN(40300),

    ERROR_INSERT(50000),

    ERROR_DELETE(50100),

    ERROR_UPDATE(50200),

    ERROR_JWT_EXPIRED(60000),

    ERROR_JWT_MALFORMED(60100),

    ERROR_JWT_SIGNATURE(60200),

    ERROR_UNKNOWN(99999);
    private Integer value;
    ServiceCode(Integer value){
        this.value = value;
    }
    public Integer getValue(){return value;}
}
