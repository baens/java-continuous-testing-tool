package change;

import com.google.common.eventbus.EventBus;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class FileSystemChangeDetector {
    private final EventBus _eventBus;
    private final WatchKey _watchKey;
    private final WatchService _watchService;

    public FileSystemChangeDetector(String absolutePath) {
        _eventBus = new EventBus();

        try {
            FileSystem fileSystem = FileSystems.getDefault();
            _watchService = fileSystem.newWatchService();
            _watchKey = fileSystem.getPath(absolutePath).register(_watchService, StandardWatchEventKinds.ENTRY_CREATE);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void pump(){
        WatchKey key;

        try {
            key = _watchService.poll(1, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        if(key == null) return;

        _eventBus.post(new DetectedChange());

        key.reset();
    }

    public void register(Object o) {
        _eventBus.register(o);
    }
}
