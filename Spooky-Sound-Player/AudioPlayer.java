
import java.io.*;
import javax.sound.sampled.*;
/**
 * Allows the playing of audio in either 'streaming' or 'preloaded' mode
 *
 * @author Vlad. J. Caso
 * @version Oct. 22, 2025
 */
public class AudioPlayer implements LineListener
{
    /**
     * Streaming mode - a sound can be played when the AudioPlayer object is instantiated, or, if no path is specified, as soon as a path is specified (via playSound method). After the audio clip ends, the clip object is closed.
     */
    public static final int STREAM = 0;
    
    /**
     * Preloaded mode - a sound can be loaded for later playback.
     */
    public static final int PRELOAD = 1;
    
    private int mode = -1;
    private File soundFile = null;
    private AudioInputStream inStream = null;
    private Clip audioClip = null;
    private long playerPos = -1;
    
    /**
     * Constructor with sound file path parameter: defaults to STREAM mode.
     * 
     * @param   soundPath   the path to the sound
     */
    public AudioPlayer(String soundPath) throws LineUnavailableException, IOException, UnsupportedAudioFileException
    {this(STREAM,soundPath);}
    
    /**
     * Constructor with sound file parameter
     * 
     * @param   soundFile   the path to the sound
     */
    public AudioPlayer(File soundFile) throws LineUnavailableException, IOException, UnsupportedAudioFileException
    {this(STREAM,soundFile);}
    
    /**
     * Constructor with mode and sound file path parameter.
     * 
     * @param   mode   the playback mode to instantiate in
     * @param   soundPath   the path to the sound
     */
    public AudioPlayer(int mode, String soundPath) throws LineUnavailableException, IOException, UnsupportedAudioFileException
    {this(mode,new File(soundPath));}
    
    /**
     * Constructor with mode and sound file parameter.
     * 
     * @param   mode   the playback mode to instantiate in
     * @param   soundFile   the path to the sound
     */
    public AudioPlayer(int mode, File soundFile) throws LineUnavailableException, IOException, UnsupportedAudioFileException
    {
        this.mode = mode;
        this.audioClip = AudioSystem.getClip();
        this.audioClip.addLineListener(this);
        this.playerPos = 0;
        if(mode==PRELOAD){
            this.loadSound(soundFile);
        }else if(mode==STREAM){
            this.playSound(soundFile);
        }
    }
    
    /**
     * Plays the current loaded sound
     */
    public void playSound()
    {
        this.audioClip.start();
        while(this.audioClip.isRunning()){
            this.playerPos = this.audioClip.getMicrosecondPosition();
        }
    }

    /**
     * Plays a sound file from the given string path
     * @param soundPath String path to the sound file
     * @throws LineUnavailableException
     * @throws IOException
     * @throws UnsupportedAudioFileException
     */
    public void playSound(String soundPath) throws LineUnavailableException, IOException, UnsupportedAudioFileException
    {playSound(new File(soundPath));}

    /**
     * Plays a sound file from the given sound File object
     * @param soundFile The sound file object
     * @throws LineUnavailableException
     * @throws IOException
     * @throws UnsupportedAudioFileException
     */
    public void playSound(File soundFile) throws LineUnavailableException, IOException, UnsupportedAudioFileException
    {
        this.inStream = AudioSystem.getAudioInputStream(soundFile.getCanonicalFile());
        this.audioClip.open(this.inStream);
        this.audioClip.setFramePosition(0);
        this.audioClip.start();
        while(this.audioClip.isRunning()){
            this.playerPos = this.audioClip.getMicrosecondPosition();
        }
    }
    
    public void loadSound(String fromPath) throws LineUnavailableException, IOException, UnsupportedAudioFileException {
        loadSound(new File(fromPath));
    }
    
    public void loadSound(File fromFile) throws LineUnavailableException, IOException, UnsupportedAudioFileException {
        this.soundFile = fromFile;
        this.inStream = AudioSystem.getAudioInputStream(this.soundFile.getCanonicalFile());
        this.audioClip.open(this.inStream);
        this.audioClip.setFramePosition(0);
    }
    
    public void update(LineEvent e) {
        System.out.println(e.getType());
        if(e.getType().equals(LineEvent.Type.STOP) && this.mode==STREAM){
            try
            {
                this.inStream.close();
            }
            catch (IOException ioe)
            {
                ioe.printStackTrace();
            }
            this.audioClip.close();
            this.audioClip = null;
            this.inStream = null;
            this.soundFile = null;
        }
    }
    
    //Getters & Setters

    /**
     * Gets the current playback mode
     *
     * @return  the playback mode, as an int
     */
    public int getMode(){return this.mode;}
    
    /**
     * Gets the current sound file, from which the path can be obtained
     *
     * @return      the sound file path
     */
    public File getSoundFile(){return this.soundFile;}
    
    /**
     * Gets the audio clip, for external control
     *
     * @return      the audio clip object
     */
    public Clip getAudioClip(){return this.audioClip;}
}
