package org.lhh.spongehome.common;

/**
 * 返回工具类
 */
public class ResultUtils {

    /**
     * 成功
     *
     * @param data 返回的数据
     * @param <T>  数据类型
     * @return 包含成功状态的响应对象
     */
    public static <T> BaseResponse<T> success(T data) {
        return new BaseResponse<>(0, data, "ok");
    }

    /**
     * 失败
     *
     * @param errorCode 错误码枚举
     * @return 包含错误状态的响应对象
     */
    public static BaseResponse error(ErrorCode errorCode) {
        if (errorCode == null) {
            throw new IllegalArgumentException("ErrorCode cannot be null");
        }
        // 确保消息不为空，使用默认值
        String message = errorCode.getMessage() != null ? errorCode.getMessage() : "Unknown error";
        return new BaseResponse<>(errorCode.getCode(), null, message);
    }

    /**
     * 失败
     *
     * @param code    错误码
     * @param message 错误消息
     * @return 包含错误状态的响应对象
     */
    public static BaseResponse error(int code, String message) {
        if (message == null || message.isEmpty()) {
            throw new IllegalArgumentException("Error message cannot be null or empty");
        }
        return new BaseResponse<>(code, null, message);
    }

    /**
     * 创建一个包含错误信息的响应对象
     * @param code
     * @param message
     * @param description
     * @return
     */

    public static BaseResponse error(int code, String message,String description) {
        if (message == null || message.isEmpty()|| description == null || description.isEmpty()) {
            throw new IllegalArgumentException("Error message cannot be null or empty");
        }
        return new BaseResponse<>(code, null, message,description);
    }

    /**
     * 失败
     *
     * @param errorCode 错误码枚举
     * @param message   自定义错误消息
     * @return 包含错误状态的响应对象
     */
    public static BaseResponse error(ErrorCode errorCode, String message ) {
        if (errorCode == null) {
            throw new IllegalArgumentException("ErrorCode cannot be null");
        }
        if (message == null || message.isEmpty()) {
            throw new IllegalArgumentException("Error message cannot be null or empty");
        }
        return new BaseResponse<>(errorCode.getCode(),  message ,errorCode.getDescription());
    }

    public static BaseResponse error(ErrorCode errorCode, String message,String  description ) {
        if (errorCode == null) {
            throw new IllegalArgumentException("ErrorCode cannot be null");
        }
        if (message == null || message.isEmpty()) {
            throw new IllegalArgumentException("Error message cannot be null or empty");
        }
        return new BaseResponse<>(errorCode.getCode(),  message,description);
    }


}