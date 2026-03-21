package com.crise.demoj.dto;

import lombok.Data;

@Data
public class ResultDto<T> {
    private Integer code;    // 状态码（10000成功，50000失败）
    private String msg;      // 提示信息
    private T data;          // 响应数据

    // 静态工具方法
    public static <T> ResultDto<T> success(T data) {
        ResultDto<T> result = new ResultDto<>();
        result.setCode(10000);
        result.setMsg("操作成功");
        result.setData(data);
        return result;
    }

    public static <T> ResultDto<T> error(String msg) {
        ResultDto<T> result = new ResultDto<>();
        result.setCode(50000);
        result.setMsg(msg);
        return result;
    }
}
