/**
 * CS2852- 031
 * Spring 2016
 * Lab 7 - Morse Code Decoder
 * Name: Raunel Albiter
 * Email: albiterri@msoe.edu
 * modified: 4/27/2016
 */

import javax.swing.JOptionPane;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

/**
 * This class takes the input of an encoded file and decodes
 * it based off of what symbols and morse code that the MorseTree
 * is already populated with.
 * @author albiterri
 * @version 1.0
 */
public class MorseDecoder {

    private MorseTree<String> morseTree = new MorseTree<>();
    private String decodedMessage = "";
    private PrintWriter outputMessage = null;
    private String morseCode = "";

    /**
     * Loads in the morse code contents in order to populate the
     * MorseTree.
     * @param morseFile file containing the morse code to be put in tree
     */
    public void loadDecoder(File morseFile) {

        try(Scanner scanMorse = new Scanner(morseFile)) {
            while(scanMorse.hasNext()) {

                String symbol = scanMorse.next();
                String morseCode = scanMorse.next();
                morseTree.add(symbol, morseCode);
            }

        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(null, "Could not find file" + morseFile.toString() +
                    "Try another file", "File not Found", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Loads in the encoded file and decodes it to a file by referring
     * to the morse code and symbols in the MorseTree.
     * @param morseFile file containing the morse code to be decoded
     * @param outputFile file to be used to output symbol representation of decoded morse
     */
    public void decodeFile(File morseFile, File outputFile) {

        decodedMessage = "";
        Scanner scanMorse;

        try {
            scanMorse = new Scanner(morseFile);
            outputMessage = new PrintWriter(outputFile);
            decode(scanMorse);
        } catch (FileNotFoundException i) {
            JOptionPane.showMessageDialog(null, "Could not find file Try another file",
                    "File not Found", JOptionPane.ERROR_MESSAGE);
        }
        outputMessage.print(decodedMessage);
        outputMessage.close();
        System.out.println("Message Successfully Decoded!");
    }

    private void decode(Scanner scan) {

        while(scan.hasNext()) {

            try {
                morseCode = scan.next();
                decodedMessage = decodedMessage.concat(morseTree.decode(morseCode));
            } catch (IllegalArgumentException i) {
                System.out.println("Warning: skipping: " + morseCode);
            }
        }
        scan.close();
    }

    /**
     * Asks the user for the input and output files
     * in order to decode encoded symbols from morse code
     * @param args Ignored
     */
    public static void main(String[] args) {

        Scanner userInput = new Scanner(System.in);
        MorseDecoder morseDecoder = new MorseDecoder();
        File morseContent = new File("morsecode.txt");
        morseDecoder.loadDecoder(morseContent);
        System.out.println("Enter an input filename:");
        String inputFileName = userInput.next();
        File inputFile = new File(inputFileName);
        System.out.println("Enter an output filename:");
        String outputFileName = userInput.next();
        File outputFile = new File(outputFileName);
        morseDecoder.decodeFile(inputFile, outputFile);
    }
}
