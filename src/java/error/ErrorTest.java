package error;

import global.GlobalProperties;

public class ErrorTest {
    public static void main(String[] args) throws OpenHTMLException {
        new GlobalProperties();
        throw new OpenHTMLException(1);

    }
}
