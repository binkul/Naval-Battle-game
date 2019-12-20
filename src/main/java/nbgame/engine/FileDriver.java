package nbgame.engine;

import nbgame.game.Statistic;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileDriver {
    private static final String fileName = "statistic.dat";
    private final PathDriver pathDriver;
    private String path;

    public FileDriver() {
        this.pathDriver = new PathDriver();
        path = createPath();
        if (path.length() > 0) {
            createFile();
        }
    }

    private String createPath() {
        String result = pathDriver.getPicPath("result");
        if (result.length() > 0) {
            result = result.replace("file:/", "");
            result += "/" + fileName;
        }
        return result;
    }

    private void createFile() {
        File file = new File(path);

        try {
            file.createNewFile();
        } catch (IOException ex) {
            System.out.println("Problem with file creation: '" + path + "'. " + ex.getMessage());
        }
    }

    public void writeResult(Statistic statistic) {
        List<Statistic> statistics = readAllResults();
        ObjectOutputStream ois = null;

        try {
            ois = new ObjectOutputStream(new FileOutputStream(path));
            for(Statistic stat : statistics) {
                ois.writeObject(stat);
            }
            ois.writeObject(statistic);
        } catch (IOException ex) {
            System.out.println("Problem with output stream from file: " + path + "'. " + ex.getMessage());
        } finally {
            closeOutputFile(ois);
        }
    }

    public List<Statistic> readAllResults() {
        List<Statistic> statistics = new ArrayList<>();
        Statistic statistic;
        ObjectInputStream ois = null;
        File file = new File(path);

        if (file.length() > 0) {
            try {
                ois = new ObjectInputStream(new FileInputStream(path));
                statistic = readResult(ois);

                while (statistic != null) {
                    statistics.add(statistic);
                    statistic = readResult(ois);
                }

            } catch (IOException ex) {
                System.out.println("Problem with input stream from file: " + path + "'. " + ex.getMessage());
            } finally {
                closeInputFile(ois);
            }
        }

        return statistics;
    }

    private Statistic readResult(ObjectInputStream ois) {
        Statistic statistic = null;

        try {
            Object tmp = ois.readObject();
            if (tmp instanceof Statistic) {
                statistic = (Statistic)tmp;
            }
        } catch (IOException | ClassNotFoundException ex) {
            return null;
        }

        return statistic;
    }

    private void closeInputFile(ObjectInputStream ois) {
        try {
            if (ois != null) ois.close();
        } catch (IOException ex) {
            System.out.println("Problem with file closing: " + path + "'. " + ex.getMessage());
        }
    }

    private void closeOutputFile(ObjectOutputStream ois) {
        try {
            if (ois != null) ois.close();
        } catch (IOException ex) {
            System.out.println("Problem with file closing: " + path + "'. " + ex.getMessage());
        }
    }
}
