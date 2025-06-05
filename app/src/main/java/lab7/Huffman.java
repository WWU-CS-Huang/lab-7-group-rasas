/*
 * Sydney Rasa
 * 6/5/25
 * Sorry I did this alone, I did not find a partner in class. 
 *  :) 
 */
package lab7;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.io.IOException;

public class Huffman {

   static class Node implements Comparable<Node> {
      char character;
      int frequency;
      Node leftNode;
      Node rightNode;

      Node(char character, int frequency) {
         this.character = character;
         this.frequency = frequency;
      }

      @Override
      public int compareTo(Node other) {
         return Integer.compare(this.frequency, other.frequency);
      }
   }

   public static Node createTree(String s) {
      Map<Character, Integer> frequencies = countFrequencies(s);

      PriorityQueue<Node> priorityQueue = new PriorityQueue<>();
      for (Map.Entry<Character, Integer> entry : frequencies.entrySet()) {
         priorityQueue.add(new Node(entry.getKey(), entry.getValue()));
      }

      while (priorityQueue.size() > 1) {
         Node leftNode = priorityQueue.poll();
         Node rightNode = priorityQueue.poll();

         Node parent = new Node('\0', leftNode.frequency + rightNode.frequency);
         parent.leftNode = leftNode;
         parent.rightNode = rightNode;

         priorityQueue.add(parent);
      }

      Node root = priorityQueue.poll();
      return root;

   }

   public static Map<Character, String> encode(Node root) {
      Map<Character, String> huffmanCode = new HashMap<>();
      buildCode(root, "", huffmanCode);
      return huffmanCode;

   }

   private static void buildCode(Node node, String code, Map<Character, String> huffmanCode) {
      if (node == null)
         return;

      if (node.leftNode == null && node.rightNode == null) {
         huffmanCode.put(node.character, code);
      }

      buildCode(node.leftNode, code + "0", huffmanCode);
      buildCode(node.rightNode, code + "1", huffmanCode);
   }

   public static Map<Character, Integer> countFrequencies(String s) {
      Map<Character, Integer> frequencies = new HashMap<>();

      for (int i = 0; i < s.length(); i++) {
         frequencies.put(s.charAt(i), frequencies.getOrDefault(s.charAt(i), 0) + 1);
      }

      return frequencies;
   }

   public static String decode(String encodedString, Node root) {
      StringBuilder decoded = new StringBuilder();
      Node current = root;

      for (int i = 0; i < encodedString.length(); i++) {
         char bit = encodedString.charAt(i);

         
         if (bit == '0') {
            current = current.leftNode;
         } else if (bit == '1') {
            current = current.rightNode;
         }

         
         if (current.leftNode == null && current.rightNode == null) {
            decoded.append(current.character);
            current = root;
         }
      }

      return decoded.toString();
   }

   public static void main(String[] args) throws FileNotFoundException {
      File file = new File(args[0]);
      Scanner scanner = new Scanner(file);
      
      StringBuilder sb = new StringBuilder();

      while (scanner.hasNextLine()) {
         sb.append(scanner.nextLine());
      }

      String s = sb.toString();
      
      if(s.length() <= 100){
         System.out.println("Input: " + s);
      }

      Node root = createTree(s);

      Map<Character, String> codeMap = encode(root);

      StringBuilder encodedString = new StringBuilder();
      for (char c : s.toCharArray()) {
         encodedString.append(codeMap.get(c));
      }
      if (s.length() <= 100) {
         System.out.println("Encoded String: " + encodedString.toString());
      }

      String decodedString = decode(encodedString.toString(), root);

      if (s.length() <= 100) {
         System.out.println("Decoded String: " + decodedString);
      }

      if (decodedString.equals(s)) {
         System.out.println("Decoded equals input: true");
      } else {
         System.out.println("Decoded equals input: false");
      }

      double compression = encodedString.length() / s.length() / 8.0;
      System.out.println("Compression Ratio: " + compression);
   }

}
