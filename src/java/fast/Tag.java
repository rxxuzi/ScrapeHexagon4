package fast;

public class Tag {


    public static String translate(String tagName){

        return tagName.replaceAll(" ", "_")
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

    public static String reverse(String s) {
        return s.replaceAll(" ", "_");
    }

    public static void main(String[] args) {
        String name = "\n" +
                "kafka_(honkai:_star_rail)";
        System.out.println(translate(name));
    }
}
