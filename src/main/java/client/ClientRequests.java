package client;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ClientRequests {


    public static Scanner sc = new Scanner(System.in);
    public static List<Path> filesToSend = new ArrayList<>();
    public static void connect() {
        System.out.println("Os seguintes arquivos serão enviados para o servidor: " + filesToSend);

        // criar o JSON
        // enviar para o peer

    }

    public static void list(String sendDir) {
        try {
            Stream<Path> path = Files.walk(Paths.get(sendDir));
            List<Path> result = path.filter(Files::isRegularFile)
                    .collect(Collectors.toList());

            for (int i = 0; i < result.size(); i++) {
                System.out.println("Index: " + i + "\tPath: " + result.get(i));
            }

            int choice;
            while (true) {
                System.out.println("Selecione um arquivo\n-1 para finalizar a seleção de arquivos");
                choice = sc.nextInt();
                try {
                    if (choice == -1) {
                        break;
                    }
                    if (choice == 99) {
                        filesToSend.addAll(result);
                    }
                    filesToSend.add(result.get(choice));
                    result.remove(choice);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            System.out.println("Seleção de arquivos finalizada.");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void index() {}
    public static void get() {}
}
