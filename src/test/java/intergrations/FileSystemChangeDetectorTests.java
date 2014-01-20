package intergrations;

import change.DetectedChange;
import change.FileSystemChangeDetector;
import com.google.common.eventbus.Subscribe;
import com.google.common.io.Files;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

public class FileSystemChangeDetectorTests {
    private DetectionReceiver _receiver;
    private FileSystemChangeDetector _detector;
    private File _tmpDir;

    @Before
    public void setup(){
        _tmpDir = Files.createTempDir();
        _tmpDir.deleteOnExit();

        _receiver = new DetectionReceiver();

        _detector = new FileSystemChangeDetector(_tmpDir.getAbsolutePath());

        _detector.register(_receiver);
    }

    @Test
    public void default_noChangeEventFired() throws IOException {
        _detector.pump();

        assertThat(_receiver.hasReceivedChanged()).isFalse();
    }

    @Test
    public void fileCreated_changeEventFired() throws IOException, InterruptedException {
        new File(_tmpDir.getAbsolutePath()+"/test").createNewFile();

        _detector.pump();

        assertThat(_receiver.hasReceivedChanged()).isTrue();
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
