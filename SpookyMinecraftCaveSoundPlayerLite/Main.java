
import javax.sound.sampled.*;
import java.net.URL;
import java.util.Random;
import java.io.*;

/**
 * Write a description of class Main here.
 *
 * @author Vlad. J. Caso
 * @version Oct. 31, 2025
 */
public abstract class Main
{
    public static Random rand = new Random();
    
    public static void main(String[] args) throws UnsupportedAudioFileException,java.io.IOException,LineUnavailableException
    {
        AudioPlayer player;
        int I = rand.nextInt(5)+1;
        player = new AudioPlayer(Main.class.getResource("/sounds/Cave"+I+".wav"));
        player.playSound();

    }

    private static class AudioPlayer implements LineListener {
        private AudioInputStream inStream = null;
        private Clip audioClip = null;

        /**
         * Constructor with sound file parameter.
         *
         * @param soundFile the path to the sound
         */
        public AudioPlayer(URL soundFile) throws LineUnavailableException, IOException, UnsupportedAudioFileException {
            this.inStream = AudioSystem.getAudioInputStream(soundFile);
            this.audioClip = AudioSystem.getClip();
            this.audioClip.addLineListener(this);
            this.audioClip.open(this.inStream);
            this.audioClip.loop(0);
        }

        public void playSound() {
            this.audioClip.setFramePosition(0);
            this.audioClip.start();
            long pos = 0;
            while(this.audioClip.isRunning()){pos = this.audioClip.getMicrosecondPosition();}

        }

        public void update(LineEvent e) {
            System.out.println(e.getType());
            if (e.getType().equals(LineEvent.Type.STOP)) {
                System.out.println("Close");
                this.audioClip.close();
                try {this.inStream.close();}
                catch (IOException f) {f.printStackTrace();}
                this.audioClip=null;
                this.inStream=null;
            }
        }
    }
}
