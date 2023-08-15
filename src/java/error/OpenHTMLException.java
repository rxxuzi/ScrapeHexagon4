package error;

public class OpenHTMLException extends Exception {
    public OpenHTMLException(String message) {
        super(message);
    }
    public OpenHTMLException(int n , Throwable cause) {
        super(
            switch (n){
                //求められたファイル数より少ない
                case 1 -> "Fewer files than requested";
                //ページが存在しない
                case 2 -> "Page not found";
                //ネットワークエラーが生じた
                case 3 -> "Network error";
                default -> "Other";
            }
            ,
                cause
        );
    }
    public OpenHTMLException(int statusCode ) {
        super(
            switch (statusCode){
                //求められたファイル数より少ない
                case 1 -> "Fewer files than requested";
                //ページが存在しない
                case 2 -> "Page not found";
                //ネットワークエラーが生じた
                case 3 -> "Network error";
                default -> "Other";
            }
        );
    }
    public OpenHTMLException() {
        super();
    }
}
