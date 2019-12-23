package nbgame.engine;

import nbgame.game.Statistic;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileDriver {
    private static final String FILE_NAME = "statistic.dat";
    private String path;

    public FileDriver() {
        createFile();
    }

    private void createFile() {
        path = System.getProperty("user.dir") + "\\" + FILE_NAME;
        File file = new File(path);

        try {
            file.createNewFile();
        } catch (IOException ignored) {}
    }

    public void writeResult(Statistic statistic) {
        List<Statistic> statistics = readAllResults();

        try (ObjectOutputStream ois = new ObjectOutputStream(new FileOutputStream(path))) {
            for(Statistic stat : statistics) {
                ois.writeObject(stat);
            }
            ois.writeObject(statistic);
        } catch (IOException ignored) {}
    }

    public List<Statistic> readAllResults() {
        List<Statistic> statistics = new ArrayList<>();
        Statistic statistic;
        File file = new File(path);

        if (file.length() > 0) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(path))) {
                statistic = readResult(ois);

                while (statistic != null) {
                    statistics.add(statistic);
                    statistic = readResult(ois);
                }

            } catch (IOException ignored) {}
        }

        return statistics;
    }

    private Statistic readResult(ObjectInputStream ois) {
        Statistic statistic = null;

        try {
            Object tmp = ois.readObject();
            if (tmp instanceof Statistic) {
                statistic = (Statistic) tmp;
            }
        } catch (IOException | ClassNotFoundException ex) {
            return null;
        }

        return statistic;
    }
}
