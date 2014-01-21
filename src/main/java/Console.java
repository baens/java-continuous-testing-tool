import change.DetectedChange;
import change.FileSystemChangeDetector;
import com.google.common.eventbus.Subscribe;

import java.nio.file.Paths;

public class Console {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("Running");

        String absolutePath = Paths.get("").toAbsolutePath().toString();
        System.out.println(absolutePath);
        FileSystemChangeDetector detector = new FileSystemChangeDetector(
                absolutePath
        );
        detector.register(new Changer());

        while(true){
            detector.pump();
            Thread.yield();
        }
    }

    public static class Changer {
        @Subscribe
        public void change(DetectedChange e){
            System.out.println("A file has change");
        }
    }
}
