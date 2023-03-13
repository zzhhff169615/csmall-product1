package cn.tedu.csmall.product1.ex;

import cn.tedu.csmall.product1.web.ServiceCode;
import lombok.Getter;

@Getter
public class ServiceException extends RuntimeException{

    private ServiceCode serviceCode;
    public ServiceException(ServiceCode serviceCode,String message){
        super(message);
        this.serviceCode = serviceCode;
    }
}
