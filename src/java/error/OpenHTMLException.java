package error;

public class OpenHTMLException extends Exception {
    public OpenHTMLException(String message) {
        super(message);
    }
    public OpenHTMLException(int n) {
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
        );
    }
}
