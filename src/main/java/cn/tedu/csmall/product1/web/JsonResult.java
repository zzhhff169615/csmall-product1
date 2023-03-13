package cn.tedu.csmall.product1.web;

import cn.tedu.csmall.product1.ex.ServiceException;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class JsonResult<T> implements Serializable {
    @ApiModelProperty("业务状态码")
    private Integer state;
    @ApiModelProperty("提示文本")
    private String message;
    @ApiModelProperty("数据")
    private T data;
    public static JsonResult<Void> ok(){
        return ok(null);
    }
    public static <T> JsonResult<T> ok(T data){
        JsonResult<T> jsonResult = new JsonResult<>();
        jsonResult.state = ServiceCode.OK.getValue();
        jsonResult.data = data;
        return jsonResult;
    }
    public static JsonResult<Void> fail(ServiceException e){
        return fail(e.getServiceCode(),e.getMessage());
    }
    public static JsonResult<Void> fail(ServiceCode serviceCode,String message){
        JsonResult<Void> jsonResult = new JsonResult<>();
        jsonResult.state = serviceCode.getValue();
        jsonResult.message = message;
        return jsonResult;
    }
}
