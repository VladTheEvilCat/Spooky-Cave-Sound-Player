import javax.sound.sampled.*;
import java.net.URL;
import java.io.*;

/**
 * @author Vlad. J. Caso
 * @version Oct. 31, 2025
 */
public abstract class Main
{
    public static void main(String[] args) throws UnsupportedAudioFileException,IOException,LineUnavailableException
    {
        AudioInputStream inStream = AudioSystem.getAudioInputStream(Main.class.getResource("/Cave"+(new java.util.Random().nextInt(5)+1)+".wav"));
        Clip audioClip = AudioSystem.getClip();
        audioClip.open(inStream);
        audioClip.loop(0);
        audioClip.start();
        try {Thread.sleep(audioClip.getMicrosecondLength()/1000);}
        catch (InterruptedException e){e.printStackTrace();}
        audioClip.close();
        try {inStream.close();}
        catch (IOException f) {f.printStackTrace();}
        audioClip=null;
        inStream=null;
    }
}
