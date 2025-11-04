
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.net.URL;
import java.util.Random;
import java.io.*;

/**
 * Write a description of class Main here.
 *
 * @author Vlad. J. Caso
 * @version Oct. 22, 2025
 */
public abstract class Main
{
    public static final Random rand = new Random();
    
    public static void main(String[] args) throws UnsupportedAudioFileException,IOException,LineUnavailableException
    {
        AudioPlayer player;
        if(args.length==0){
            int I = rand.nextInt(23)+1;
            player = new AudioPlayer(Main.class.getResource("/Cave"+I+".wav"));
        } else {
            String fileName = "";
            if (args[0].equalsIgnoreCase("-d"))
                fileName="/Cave1.wav";
            else if (args[0].startsWith("/"))
                fileName=args[0];
            else if (args[0].startsWith("="))
                fileName="/Cave"+args[0].substring(1)+".wav";
            else if( args.length==2 && (args[0].equalsIgnoreCase("-random") || args[0].equalsIgnoreCase("-r"))) {
                int num = switch (args[1]) { // "Spooky-ness" level (0, 1, 2)
                    case "0" -> rand.nextInt(13)+1;
                    case "1" -> rand.nextInt(13,19)+1;
                    case "2" -> rand.nextInt(19,23)+1;
                    default -> rand.nextInt(13)+1;
                };
                fileName+="/Cave"+num+".wav";
            }
            player = new AudioPlayer(Main.class.getResource(fileName));
        }
        player.playSound();
    }

    private static class AudioPlayer {
        private AudioInputStream inStream;
        private Clip audioClip;

        /**
         * Constructor with sound file parameter.
         *
         * @param soundFile the path to the sound
         */
        public AudioPlayer(URL soundFile) throws LineUnavailableException, IOException, UnsupportedAudioFileException {
            this.inStream = AudioSystem.getAudioInputStream(soundFile);
            this.audioClip = AudioSystem.getClip();
            this.audioClip.open(this.inStream);
            this.audioClip.loop(0);
        }

        public void playSound() {
            this.audioClip.start();
            try {Thread.sleep(audioClip.getMicrosecondLength()/1000);}
            catch (InterruptedException e){e.printStackTrace();}
            // Cleanup!
            this.audioClip.close();
            try {this.inStream.close();}
            catch (IOException ioe) {ioe.printStackTrace();}
            this.audioClip=null;
            this.inStream=null;
        }
    }
}
