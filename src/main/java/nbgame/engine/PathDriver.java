package nbgame.engine;

import nbgame.game.Statistic;

import java.util.List;

public class PathDriver {

    public String getPicPath(String imagePath) {
        String path = "";

        try {
            ClassLoader loader = getClass().getClassLoader();
            path = loader.getResource(imagePath).toString();
            path = path.replaceAll("%20", " ");
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return path;
    }
}
