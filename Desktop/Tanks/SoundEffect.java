import java.io.*;
import java.net.URL;
import javax.sound.sampled.*;
/**
 * This enum encapsulates all the sound effects of a game, so as to separate the sound playing
 * codes from the game codes.<br/>
 * 1. Define all your sound effect names and the associated wave file.<br/>
 * 2. To play a specific sound, simply invoke SoundEffect.SOUND_NAME.play().<br/>
 * 3. You might optionally invoke the static method SoundEffect.init() to pre-load all the
 *    sound files, so that the play is not paused while loading the file for the first time.<br/>
 * 4. You can use the static variable SoundEffect.volume to mute the sound.
 * @author Erik Miller
 * @author Jay Sathe
 * @author Tristan Seroff
 * @author Christian Ciabatoni
 */
public enum SoundEffect
{
   SHOOT("Sounds/Shoot.wav"),   // explosion
   BOMBHIT("Sounds/BombHit.wav"),    // bomb hitting ground
   TANKDIE("Sounds/TankDie.wav"),	//tank exploding
   TANKHIT("Sounds/TankHit.wav"),	//tank hit by projectile
   MUSIC("Sounds/Music 1.wav");		// background music by Jared Keffer
   
   public static Volume volume = Volume.LOW;/**The volume of the sound clip*/
   
   private Clip clip;/**Each sound effect has its own clip, loaded with its own sound file*/
   
   /**
    * Nested class for specifying volume
    */
   public static enum Volume 
   {
      MUTE, LOW, MEDIUM, HIGH
   }
   
   /**
    * Constructs each element of the enum with its own sound file.
    * @param soundFileName the path of the soundfile
    */
   SoundEffect(String soundFileName) 
   {
      try 
      {
         // Use URL (instead of File) to read from disk and JAR.
         URL url = this.getClass().getClassLoader().getResource(soundFileName);
         // Set up an audio input stream piped from the sound file.
         AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(url);
         // Get a clip resource.
         clip = AudioSystem.getClip();
         // Open audio clip and load samples from the audio input stream.
         clip.open(audioInputStream);
      }
      catch (UnsupportedAudioFileException e) 
      {
         e.printStackTrace();
      }
      catch (IOException e) 
      {
         e.printStackTrace();
      }
      catch (LineUnavailableException e)
      {
         e.printStackTrace();
      }
   }
   
   /**
    * Plays the sound effect<p>
    * Play or Re-play the sound effect from the beginning, by rewinding.
    */
   public void play()
   {
      if (volume != Volume.MUTE)
      {
         if (clip.isRunning())
            clip.stop();   // Stop the player if it is still running
         clip.setFramePosition(0); // rewind to the beginning
         clip.start();     // Start playing
      }
   }
   
   /**
    * Loops the sound effect
    */
   public void loopMusic()
   {
	      if (volume != Volume.MUTE)
	      {
	         if (clip.isRunning())
	            clip.stop();   // Stop the player if it is still running
	         clip.setFramePosition(0); // rewind to the beginning
	         clip.loop(Clip.LOOP_CONTINUOUSLY);     // Start playing
	      }
	   }
   
   /**
    * Optional static method to pre-load all the sound files.
    */
   static void init()
   {
      values(); // calls the constructor for all the elements
   }
}