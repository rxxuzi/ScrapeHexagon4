package fast;

public class FileName {
    public static String translate(String name){
        return name
                .replaceAll(" ", "-")
                .replaceAll(",", "+")
                .replaceAll("\\\\", "-")
                .replaceAll(":" , "-");
    }
}
