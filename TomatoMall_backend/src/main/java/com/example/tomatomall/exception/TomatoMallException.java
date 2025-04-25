package com.example.tomatomall.exception;

import lombok.Getter;

@Getter
public class TomatoMallException extends RuntimeException {
  private final String code;
  private final String msg;

  public TomatoMallException(String code, String msg){
    super(msg);
    this.code = code;
    this.msg = msg;
  }


    public static TomatoMallException usernameAlreadyExists(){
    return new TomatoMallException("400","用户已经存在!");
  }

  public static TomatoMallException userDoNotExist(){
    return new TomatoMallException("400","用户不存在!");
  }

  public static TomatoMallException notLogin(){
    return new TomatoMallException("400","未登录!");
  }

  public static TomatoMallException phoneOrPasswordError(){
    return new TomatoMallException("400","手机号或密码错误!");
  }

  public static TomatoMallException passwordIncorrectError() { return new TomatoMallException("400","密码错误!");}

  public static TomatoMallException productDoNotExist() { return new TomatoMallException("400","商品不存在!");}

  public static TomatoMallException addProductFail() { return new TomatoMallException("400","商品添加失败!");}

  public static TomatoMallException NoFile() { return new TomatoMallException("400","文件为空!");}

  public static TomatoMallException fileUploadFail() { return new TomatoMallException("400","文件上传失败!");}

  public static TomatoMallException orderDoNotExist() { return new TomatoMallException("400","订单不存在!");}
}
