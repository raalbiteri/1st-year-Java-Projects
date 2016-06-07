/**
 * CS2852- 031
 * Spring 2016
 * Lab 7 - Morse Code Decoder
 * Name: Raunel Albiter
 * Email: albiterri@msoe.edu
 * modified: 4/27/2016
 */

/**
 * This class is a BinaryTree built to hold MorseNodes
 * which contain contents related to a morse code such as
 * the symbol it represents and its code in dashes and dots
 * @param <E> element
 * @author albiterri
 * @version 1.0
 */
public class MorseTree<E> {

    private MorseNode<E> root;
    private String currentChar = "";
    private int charCount = 0;

    /**
     * Constructor that insure the MorseTree's root has
     * no contents at the beginning
     */
    public MorseTree() {

        this.root = new MorseNode<>(null, null);
    }

    /**
     * Used to populate the MorseTree with MorseNodes in the
     * correct position. Its position is based off of how far
     * down it goes left (dot) and right (dash).
     * @param symbol the representation of the morse code
     * @param morseCode morse code represented in either dashes or dots
     */
    public void add(E symbol, String morseCode) {

        charCount = 0;
        add(root, symbol, morseCode, morseCode);
    }

    private void add(MorseNode<E> localRoot, E symbol, String morseCode, String tempMorse) {

        final int MINIMUM_MORSE_LENGTH = 0;
        if(tempMorse.length() <= MINIMUM_MORSE_LENGTH) {
            throw new IllegalArgumentException("Cannot add in blank morse code. Fix your file!");
        }
        if(morseCode.length() - 1 == charCount) {
            if(morseCode.charAt(charCount) == '.' && localRoot.left == null) {
                localRoot.left = new MorseNode<>(symbol, tempMorse);
            } else if(morseCode.charAt(charCount) == '.' && localRoot.left != null) {
                localRoot.left.symbol = symbol;
                localRoot.left.morseCode = morseCode;
            } else if(morseCode.charAt(charCount) == '-' && localRoot.right == null) {
                localRoot.right = new MorseNode<>(symbol, tempMorse);
            } else if(morseCode.charAt(charCount) == '-' && localRoot.right != null) {
                localRoot.right.symbol = symbol;
                localRoot.right.morseCode = morseCode;
            }
        } else if(morseCode.charAt(charCount) == '.') {
            if(localRoot.left == null && morseCode.length() - 1 != charCount) {
                localRoot.left = new MorseNode<>(null, null);
                add(localRoot.left, symbol, morseCode.substring((charCount + 1),
                        morseCode.length()), tempMorse);
            } else {
                add(localRoot.left, symbol, morseCode.substring((charCount + 1),
                        morseCode.length()), tempMorse);
            }
        } else if(morseCode.charAt(charCount) == '-') {
            if(localRoot.right == null && morseCode.length() - 1 != charCount) {
                localRoot.right = new MorseNode<>(null, null);
                add(localRoot.right, symbol, morseCode.substring((charCount + 1),
                        morseCode.length()), tempMorse);
            } else {
                add(localRoot.right, symbol, morseCode.substring((charCount + 1),
                        morseCode.length()), tempMorse);
            }
        }
    }

    /**
     * Used to decode morse code by traversing the tree and
     * returning the symbol value of the MorseNode it matches.
     * @param morseCode morse code represented in either dashes or dots
     * @return the symbol representative of the morse code
     */
    public String decode(String morseCode) {

        if(morseCode.equals(".-.-.-")) {
            return "\n\n";
        }
        currentChar = "";
        charCount = 0;
        return decodeMorse(root, morseCode, morseCode);
    }

    private String decodeMorse(MorseNode<E> localRoot, String morseCode, String tempMorse) {

        if(currentChar.equals(tempMorse)) {
            return localRoot.symbol.toString();
        } else if(morseCode.charAt(charCount) == '.') {
            currentChar = currentChar.concat(".");
            return decodeMorse(localRoot.left, morseCode.substring((charCount + 1),
                    morseCode.length()), tempMorse);
        } else if(morseCode.charAt(charCount) == '-'){
            currentChar = currentChar.concat("-");
            return decodeMorse(localRoot.right, morseCode.substring((charCount + 1),
                    morseCode.length()), tempMorse);
        } else if(!morseCode.contains("-") || !morseCode.contains(".")){
            if(morseCode.equals("|")) {
                return " ";
            } else {
                throw new IllegalArgumentException();
            }
        } else {
            return null;
        }
    }

    /**
     * This class helps establish a MorseNode which the MorseTree
     * will be filled with. A MorseNode will contain morse code and
     * the symbols it represents.
     * @param <E> element
     */
    private static class MorseNode<E> {

        private E symbol;
        private String morseCode;
        private MorseNode<E> left;
        private MorseNode<E> right;

        private MorseNode(E symbol, String morseCode) {
            this.symbol = symbol;
            this.morseCode = morseCode;
            this.left = null;
            this.right = null;
        }
    }
}
