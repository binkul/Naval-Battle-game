package nbgame.engine;

import java.util.Objects;

public class PathDriver {

    public String getPath(String imagePath) {
        String path;
        ClassLoader loader = getClass().getClassLoader();

        try {
            path = Objects.requireNonNull(loader.getResource(imagePath)).toString();
            path = path.replaceAll("%20", " ");
        } catch (NullPointerException ex) {
            path = "";
        }

        return path;
    }
}
