import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.lang.Integer;
import java.lang.Double;

/**
 * Driver class for Binary Trees.
 */

public class BinaryTreeDriver {
    private static boolean i = false;
    private static boolean d = false;
    private static boolean s = false;

    public static void main(String[] args) {

        Scanner read = new Scanner(System.in);
        BinaryTree bst = null;

        // checking for file i/o exception
        try {
            // using first (and only) vararg to create file
            String filename = args[0].trim();
            File file = new File(filename);
            //Creating Scanner instances to read file and user input
            Scanner scan = new Scanner(file);

            System.out.print("Enter list type (i - int, d - double, s - string): ");
            String input = read.nextLine().trim().toLowerCase();

            if (input.length() == 1) {
                char option = input.charAt(0);
                if (option == 'i') {
                    bst = new BinaryTree<Integer>();
                    // creating Binary Search Tree from input file
                    while (scan.hasNext()) {
                        bst.insert(Integer.parseInt(scan.next()));
                    }
                    i = true;
                } else if (option == 'd') {
                    bst = new BinaryTree<Double>();
                    while (scan.hasNext()) {
                        bst.insert(Double.parseDouble(scan.next()));
                    }
                    d = true;
                } else if (option == 's') {
                    bst = new BinaryTree<String>();
                    while (scan.hasNext()) {
                        bst.insert((scan.next()));
                    }
                    s = true;
                } else {
                    print("error");
                    System.exit(1);
                }
            }

        } catch(FileNotFoundException e) {
            System.err.println(e.getMessage());
        }

        print("commands");
        String command;
        if (bst != null) {
            do {
                System.out.print("Enter a command: ");
                command = read.nextLine().trim().toLowerCase();
                if (command.length() <= 2 && command.length() > 0) {
                    switch(command) {
                    case "i":
                        System.out.print("In-order: ");
                        bst.inOrder();
                        System.out.print("Enter an item to insert: ");
                        if (i) {
                            bst.insert(Integer.parseInt(read.next()));
                        } else if (d) {
                            bst.insert(Double.parseDouble(read.next()));
                        } else if (s) {
                            bst.insert(read.next());
                        } else {
                            print("error");
                            System.exit(1);
                        }
                        read.nextLine();
                        System.out.print("In-order: ");
                        bst.inOrder();
                        break;
                    case "d":
                        System.out.print("In-order: ");
                        bst.inOrder();
                        System.out.print("Enter an item to delete: ");
                        if (i) {
                            bst.delete(Integer.parseInt(read.next()));
                        } else if (d) {
                            bst.delete(Double.parseDouble(read.next()));
                        } else if (s) {
                            bst.delete(read.next());
                        } else {
                            print("error");
                            System.exit(1);
                        }
                        read.nextLine();
                        System.out.print("In-order: ");
                        bst.inOrder();
                        break;
                    case "p":
                        System.out.print("In-order: ");
                        bst.inOrder();
                        break;
                    case "s":
                        System.out.print("In-order: ");
                        bst.inOrder();
                        System.out.print("Enter an item to search: ");
                        if (i) {
                            bst.search(Integer.parseInt(read.next()));
                        } else if (d) {
                            bst.search(Double.parseDouble(read.next()));
                        } else if (s) {
                            bst.search(read.next());
                        } else {
                            print("error");
                            System.exit(1);
                        }
                        read.nextLine();
                        break;
                    case "l":
                        bst.getNumLeafNodes();
                        break;
                    case "sp":
                        bst.getSingleParents();
                        break;
                    case "c":
                        System.out.print("In-order: ");
                        bst.inOrder();
                        System.out.print("Enter an item: ");
                        if (i) {
                            bst.getCousins(Integer.parseInt(read.next()));
                        } else if (d) {
                            bst.getCousins(Double.parseDouble(read.next()));
                        } else if (s) {
                            bst.getCousins(read.next());
                        } else {
                            print("error");
                            System.exit(1);
                        }
                        read.nextLine();
                        break;
                    case "q":
                        System.out.println("Quitting program...");
                        System.exit(1);
                        break;
                    default:
                        print("error");
                    }
                } else {
                    print("error");
                    System.exit(1);
                }
            } while (command != "q");
        }

    } // main

    /**
     * Helper method to print info.
     */
    private static void print(String str) {
        if (str.equals("error")) {
            System.out.println("INVALID INPUT");
            System.out.println("exiting...");
        } else if(str.equals("commands")) {
            System.out.println("Commands:");
            System.out.println("'i' to Insert Item");
            System.out.println("'d' to Delete Item");
            System.out.println("'p' to Print Tree");
            System.out.println("'s' to Search Item");
            System.out.println("'l' to Count Leaf Nodes");
            System.out.println("'sp' to Find Single Parents");
            System.out.println("'c' to Find Cousins");
            System.out.println("'q' to Quit Program");
        }
    } //print

} // TreeDriver
