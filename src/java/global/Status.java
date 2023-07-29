package global;

public final class Status {
    private static int STATUS_CODE = -1;
    private static String STATUS_MESSAGE;
    private String STATUS_DETAIL;


    private static final String[] STATUS_TYPE = {
            "Success", "Fail", "Error"
    };
    public static void Error(String message){
        STATUS_CODE = 200;
        STATUS_MESSAGE = message;
    }

    /**
     * This method is used to set the status code.
     * <table>
     *     <thead>
     *         <tr>
     *             <td>Code</td><td>Type</td><td>Detail</td>
     *         </tr>
     *     </thead>
     *     <tbody>
     *       <tr><td>0</td><td>Success</td></tr>
     *       <tr><td>100</td><td>Fail</td><td>Fewer than required.</td></tr>
     *       <tr><td>101</td><td>Fail</td><td>Too many.</td></tr>
     *       <tr><td>102</td><td>Fail</td><td>No such word.</td></tr>
     *       <tr><td>103</td><td>Fail</td><td>No such file.</td></tr>
     *       <tr><td>104</td><td>Fail</td><td>No such directory.</td></tr>
     *       <tr><td>105</td><td>Fail</td><td>No such command.</td></tr>
     *       <tr><td>106</td><td>Fail</td><td>No such option.</td></tr>
     *     </tbody>
     * </table>
     *
     */
    public static void setStatusCode(int code){
        STATUS_CODE = code;
    }
    public int getCode() {
        return STATUS_CODE;
    }
    public void get(){

    }
    public void get(boolean b){

    }

    public void func(){

    }
}
