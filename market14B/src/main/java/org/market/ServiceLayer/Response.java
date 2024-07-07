
package org.market.ServiceLayer;
public class Response<T> {

    private T value = null;
    private boolean error = false;
    private String Errormessage;

    public static <T> Response<T> successRes(T value) {
        Response<T> response = new Response<>();
        response.setValue(value);
        return response;
    }

    public static <T> Response<T> failRes(String message) {
        Response<T> response = new Response<>();
        response.setErrorMessage(message);
        response.setError(true);
        return response;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public boolean isError() {
        return error;
    }

    private void setError(boolean error) {
        this.error = error;
    }

    public String getErrorMessage() {
        return Errormessage;
    }

    private void setErrorMessage(String Errormessage) {
        this.Errormessage = Errormessage;
    }
}
