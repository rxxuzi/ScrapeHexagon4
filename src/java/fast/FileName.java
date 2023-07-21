package fast;

public class FileName {
    public static String translate(String name){
        return name
                .replaceAll(" ", "-")
                .replaceAll(",", "+")
                .replaceAll("\\\\", "-")
                .replaceAll(":" , "-");
    }
    public static String translate(int n){
        return String.valueOf(n)
                .replaceAll(" ", "-")
                .replaceAll(",", "+")
                .replaceAll("\\\\", "-")
                .replaceAll(":" , "-");
    }
}
