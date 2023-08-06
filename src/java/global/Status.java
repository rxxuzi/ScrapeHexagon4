package global;

import java.util.HashMap;
import java.util.Map;

public final class Status {
    private static int STATUS_CODE = Integer.MAX_VALUE;
    private static String STATUS_MESSAGE;
    private String STATUS_DETAIL;

    private static final Map<Integer , String> response = new HashMap<>();

    Status(){
        response.put(0,"Success");
        response.put(101 , "Less Than Request");
        response.put(102 , "Page Not Found");
        response.put(103 , "Download Failed");
        response.put(201 , "Config Error");
        response.put(202 , "HTTP Connection Error");
        response.put(203 , "Malformed URL Error");
        response.put(204 , "InterruptedException Error");
    }


    private static final String[] STATUS_TYPE = {
            "Success", "Fail", "Error" , ""
    };
    public static void Error(String message){
        STATUS_CODE = 200;
        STATUS_MESSAGE = message;
    }

    private final static String[] FailStatus ={
        "Less Than Requests",

    };


    public static void setStatusCode(int code){
        STATUS_CODE = code;
    }
    public static int getCode() {
        return STATUS_CODE;
    }

    /**
     *
     * 番号に応じたStatus Messageを返します
     * @return
     *      <tr><td>0</td><td>Success</td></tr>
     *      <tr><td>1</td><td>Fail</td></tr>
     *      <tr><td>2</td><td>Error</td></tr>
     *      <tr><td>other</td><td>Nothing</td></tr>
     */
    public static String getStatusMessage(){
        return switch (STATUS_CODE / 100) {
            case 0 -> "SUCCESS";
            case 1 -> "FAIL";
            case 2 -> "ERROR";
            case 3 -> "Config Error!";
            default -> "";
        };
    }
    public void get(){

    }
    public void get(boolean b){

    }

    public void func(){

    }
}
