/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceclient.serializer;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author Toon
 */
public class Vector2fSerializer {

    Scanner scanner;
    ArrayList<String> messages;

    public void serialize(InputStream inputStream) {
        scanner = new Scanner(inputStream);
        messages = new ArrayList<String>();
        while (scanner.hasNext()) {
            messages.add(scanner.next());
        }
        System.out.println(scanner);

    }

}
