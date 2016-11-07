/**
 *
 */
package com.itee.exam.vo;

/**
 * @author moxin
 */
public class ExecuteResult<T> implements java.io.Serializable {

    private ResultCode resultCode;

    private T result;

    public ExecuteResult() {
    }

    public ExecuteResult(ResultCode resultCode) {
        this.resultCode = resultCode;
    }

    public ResultCode getResultCode() {
        return resultCode;
    }

    public void setResultCode(ResultCode resultCode) {
        this.resultCode = resultCode;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }
}
