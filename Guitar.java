/**
 * CS2852- 031
 * Spring 2016
 * Lab 5 - Guitar Synthesizer
 * Name: Raunel Albiter
 * Email: albiterri@msoe.edu
 * modified: 4/18/2016
 */

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import javax.sound.sampled.LineUnavailableException;
import edu.msoe.taylor.audio.Note;
import edu.msoe.taylor.audio.SimpleAudio;


/**
 * The Guitar class generates guitar sounds based on user input.
 * 
 * In order to use this class correctly, one must create a Guitar
 * object, add all of the desired notes to the object, and then
 * call the play() method.  The play() method will generate a
 * list of samples for all of the notes to be played (by calling
 * an internal method (jaffeSmith())) and then send them to the
 * audio output stream.
 * @author t a y l o r@msoe.edu, albiterri@msoe.edu
 * @version 2016.04.18_2.2
 */
public class Guitar {
    /** 
     * Default sample rate in Hz 
     */
    private static final int DEFAULT_SAMPLE_RATE = 8000;
    
    /** 
     * Maximum sample rate in Hz
     */
    private static final int MAX_SAMPLE_RATE = 48000;
    
    /** 
     * Default decay rate
     */
    private static final float DEFAULT_DECAY_RATE = 0.99f;
    
    /**
     * Queue of notes 
     */
    private Queue<Note> notes = new LinkedList<>();
    
    /**
     *  Sample rate in samples per second 
     */
    private int sampleRate;
    
    /** 
     * Decay rate 
     */
    private float decayRate;
        
    /**
     * Constructs a new Guitar object with the default sample rate
     * and decay rate.
     */
    public Guitar() {

        this.sampleRate = DEFAULT_SAMPLE_RATE;
        this.decayRate = DEFAULT_DECAY_RATE;
    }
    
    /**
     * Constructs a new Guitar object with the specified parameters.
     * If an invalid sampleRate or decayRate is specified, the
     * default value will be used and an error message is sent to
     * System.err.
     * @param sampleRate sample rate (between 8000 Hz and 48000 Hz)
     * @param decayRate decay rate (between 0.0f and 1.0f)
     */
    public Guitar(int sampleRate, float decayRate) {

        if(sampleRate < DEFAULT_SAMPLE_RATE ||
                sampleRate > MAX_SAMPLE_RATE ||
                decayRate < 0.0f || decayRate > DEFAULT_DECAY_RATE) {

            System.err.print("Your rates either exceeded or are below the standard. Try " +
                    "another rate for either SampleRate or DecayRate");
        } else {
            this.sampleRate = sampleRate;
            this.decayRate = decayRate;
        }
    }
        
    /**
     * Adds the specified note to this Guitar.
     * @param note Note to be added.
     */
    public void addNote(Note note) {
        if(note != null) {
            notes.add(note);
        }
    }
    
    /**
     * Generates the audio samples for the notes listed in the
     * current Guitar object by calling the jaffeSmith algorithm and
     * sends the samples to the speakers.
     * @throws LineUnavailableException If audio line is unavailable.
     * @throws IOException If any other input/output problem is encountered.
     */
    public void play() throws LineUnavailableException, IOException {
        new SimpleAudio().play(jaffeSmith());
    }

    /**
     * Uses the Jaffe-Smith algorithm to generate the audio samples.
     * <br />Implementation note:<br />
     * Use Jaffe-Smith algorithm described on the assignment page
     * to generate a sequence of samples for each note in the list
     * of notes.
     * 
     * @return List of samples comprising the pluck sound(s).
     */
    private List<Float> jaffeSmith() {

        List<Float> samples = new LinkedList<>();
        final int MILLISECOND_TO_SECONDS = 1000;
        Random randSample = new Random();

        for(Note n: notes) {
            float samplesPerPeriod = sampleRate / n.getFrequency();
            float numberOfSamples = sampleRate * (n.getDuration() / MILLISECOND_TO_SECONDS);
            Queue<Float> periodSamples = new LinkedList<>();
            float previousSample = 0;
            for(int i = 0; i < samplesPerPeriod; i++) {
                float randValue = randSample.nextFloat() * 2 - 1;
                periodSamples.add(randValue);
            }
            for(int i = 0; i < numberOfSamples; i++) {

                float currentSample = periodSamples.remove();
                float newSample = decayRate*((previousSample + currentSample)/2);
                periodSamples.add(newSample);
                samples.add(newSample);
                previousSample = currentSample;
            }
        }
        return samples;
    }

    /**
     * Returns an array containing all the notes in this Guitar.
     * OPTIONAL
     * @return An array of Notes in the Guitar object.
     */
    public Note[] getNotes() {
        throw new UnsupportedOperationException("Optional method not implemented");
    }
    
    /**
     * Creates a new file and writes to that file.
     * OPTIONAL
     * @param file File to write to.
     * @throws IOException If any other input/output problem is encountered.
     */
    public void write(File file) throws IOException {
        throw new UnsupportedOperationException("Optional method not implemented");
    }
}
