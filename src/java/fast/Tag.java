package fast;

public class Tag {

    public static String translate(String url){

        return url.replaceAll(" ", "_")
                // "," -> "+"
                .replaceAll(",", "+")
                // "(" -> "%28"
                .replaceAll("\\(", "%28")
                // ")" -> "%29"
                .replaceAll("\\)", "%29")
                // ":" -> "%3A"
                .replaceAll(":", "%3A")
                // "/" -> "%2F"
                .replaceAll("/", "%2F")
                // "?" -> "%3F"
                .replaceAll("\\?", "%3F")
                // "&" -> "%26"
                .replaceAll("&", "%26")
                // "=" -> "%3D"
                .replaceAll("=", "%3D");
    }

    public static String find(String s) {
        return s.replaceAll(" ", "_");
    }
}
