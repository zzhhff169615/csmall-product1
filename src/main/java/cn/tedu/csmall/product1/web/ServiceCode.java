package cn.tedu.csmall.product1.web;



public enum ServiceCode {
    /**
     * 成功
     */
    OK(20000),
    /**
     * 错误：请求参数格式有误
     */
    ERROR_BAD_REQUEST(40000),
    /**
     * 错误：数据不存在
     */
    ERROR_NOT_FOUND(40400),
    /**
     * 错误：数据冲突
     */
    ERROR_CONFLICT(40900),
    /**
     * 错误：未通过认证，或未找到认证信息
     */
    ERROR_UNAUTHORIZED(40100),
    /**
     * 错误：未通过认证，因为账号被禁用
     */
    ERROR_UNAUTHORIZED_DISABLED(40101),
    /**
     * 错误：禁止访问，无此操作权限
     */
    ERROR_FORBIDDEN(40300),
    /**
     * 错误：插入数据错误
     */
    ERROR_INSERT(50000),
    /**
     * 错误：删除数据错误
     */
    ERROR_DELETE(50100),
    /**
     * 错误：修改数据错误
     */
    ERROR_UPDATE(50200),
    /**
     * 错误：JWT已过期
     */
    ERROR_JWT_EXPIRED(60000),
    /**
     * 错误：JWT格式错误
     */
    ERROR_JWT_MALFORMED(60100),
    /**
     * 错误：JWT验证签名失败
     */
    ERROR_JWT_SIGNATURE(60200),
    /**
     * 错误：未知错误
     */
    ERROR_UNKNOWN(99999);
    private Integer value;
    ServiceCode(Integer value){
        this.value = value;
    }
    public Integer getValue(){return value;}
}
