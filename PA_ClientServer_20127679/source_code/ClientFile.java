import java.nio.file.*;
import java.io.*;
import java.net.Socket;

public class ClientFile implements Runnable {
    public static WatchService ws;

    private static Client c;

    public ClientFile(Client c) {
        ClientFile.c = c;
    }

    public void run() {
        try {
            ws = FileSystems.getDefault().newWatchService();
            Path directory = Path.of(c.getDirectory());
            WatchKey wk = directory.register(ws, StandardWatchEventKinds.ENTRY_CREATE,
                                                StandardWatchEventKinds.ENTRY_DELETE,
                                                StandardWatchEventKinds.ENTRY_MODIFY);
            while (c.checkConnection()) {
                for (WatchEvent<?> event : wk.pollEvents()) {
                    WatchEvent<Path> pathEvent = (WatchEvent<Path>) event;
                    Path fileName = pathEvent.context();
                    WatchEvent.Kind<?> kind = event.kind();

                    if (kind == StandardWatchEventKinds.ENTRY_CREATE) {
                        String str = "ACTION: New file has been created : " + fileName;
                        c.sendToServer(str);
                    } else if (kind == StandardWatchEventKinds.ENTRY_DELETE) {
                        String str = "ACTION: A file has been deleted : " + fileName;
                        c.sendToServer(str);
                    } else if (kind == StandardWatchEventKinds.ENTRY_MODIFY) {
                        String str = "ACTION: A file has been modified : " + fileName;
                        c.sendToServer(str);
                    }
                }
                if (!wk.reset())
                    break;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}