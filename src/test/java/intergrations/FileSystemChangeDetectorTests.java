package intergrations;

import change.DetectedChange;
import change.FileSystemChangeDetector;
import com.google.common.eventbus.Subscribe;
import com.google.common.io.Files;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

public class FileSystemChangeDetectorTests {
    @Test
    public void default_noChangeEventFired() throws IOException {
        File tmpDir = Files.createTempDir();
        tmpDir.deleteOnExit();

        DetectionReceiver receiver = new DetectionReceiver();

        FileSystemChangeDetector detector = new FileSystemChangeDetector(tmpDir.getAbsolutePath());

        detector.register(receiver);

        detector.pump();

        assertThat(receiver.hasReceivedChanged()).isFalse();
    }

    @Test
    public void fileCreated_changeEventFired() throws IOException, InterruptedException {
        File tmpDir = Files.createTempDir();
        tmpDir.deleteOnExit();

        DetectionReceiver receiver = new DetectionReceiver();

        FileSystemChangeDetector detector = new FileSystemChangeDetector(tmpDir.getAbsolutePath());

        detector.register(receiver);

        File file = new File(tmpDir.getAbsolutePath()+"/test");
        file.createNewFile();

        detector.pump();

        assertThat(receiver.hasReceivedChanged()).isTrue();
    }

    class DetectionReceiver {
        private boolean _hasReceivedChanged;

        public boolean hasReceivedChanged(){
            return _hasReceivedChanged;
        }

        @Subscribe
        public void change(DetectedChange e){
            System.out.println("....");
            _hasReceivedChanged = true;
        }
    }
}
