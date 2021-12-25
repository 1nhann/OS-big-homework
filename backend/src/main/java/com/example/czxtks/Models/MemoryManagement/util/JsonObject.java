//package com.example.czxtks.Models.MemoryManagement.util;
package com.example.czxtks.Models.MemoryManagement.util;
import lombok.Data;
import static com.example.czxtks.Models.MemoryManagement.util.Constants.*;

@Data
public class JsonObject<T> {
    private Integer code;
    private String msg;
    private T data;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public static JsonObject success(int async){
        JsonObject jsonObject = new JsonObject();
        jsonObject.code = OK_CODE;
        jsonObject.msg =OK_MSG ;
        jsonObject.data= async;
        return jsonObject;
    }

    public static JsonObject deFault(int async){
        JsonObject jsonObject  = new JsonObject();
        jsonObject.code = FAIL_CODE;
        jsonObject.msg = FAIL_MSG;
        jsonObject.data = -1 * async;
        return jsonObject;
    }

    public static JsonObject successFree(int async){
        JsonObject jsonObject = new JsonObject();
        jsonObject.code = OK_CODE;
        jsonObject.msg =OK_MSG ;
        jsonObject.data = async;
        return jsonObject;
    }

    public static JsonObject successStart(){
        JsonObject jsonObject = new JsonObject();
        jsonObject.code = OK_CODE;
        jsonObject.msg ="内存初始化完成" ;
        jsonObject.data = "finish";
        return jsonObject;
    }
}
