package fr.fuzeblocks.homeplugin.debug;



public class DebugManager {
    /*
    private boolean active = true;
    private final File file;

    public DebugManager(File file) {
        this.file = file;
    }

    public DebugManager() {
        Optional<File> file = findFile();
        if (file.isPresent()) {
            this.file = file.get();
        } else {
            this.file = FileManager.createFile(new File(HomePlugin.getPlugin(HomePlugin.class).getDataFolder() + "/logs/","debug_1.log"));
        }
    }

    private Optional<File> findFile() {
        File folder = HomePlugin.getPlugin(HomePlugin.class).getDataFolder();
        File dir = new File(folder + "/logs/");
        if (dir.isDirectory()) {
            int number = 0;
            for (File file : dir.listFiles()) {
                if (file != null) {
                  String[] parts = file.getName().split("_");
                  number = Integer.parseInt(parts[1]);
                }
            }
               return Optional.of(new File(folder + "/logs/","debug_" + number + ".log"));
        }
        return Optional.empty();
    }

    public void log(String log) {
        if (!active) return;
        try {
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(log + "\n");
            fileWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public File getFile() {
        return file;
    }

     */
}
