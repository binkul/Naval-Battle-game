package nbgame.engine;

public class PathDriver {

    public String getPath(String path) {
        ClassLoader loader = getClass().getClassLoader();
        Object object = loader.getResource(path);
        if (object != null) {
            return object.toString().replaceAll("%20", " ");
        } else {
            return "";
        }
    }
}
