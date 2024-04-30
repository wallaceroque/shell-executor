package com.wallace.demo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.*;

public class ShellCommandExecutor {

    public static void main(String[] args) {
        String command = "./shell_command.sh " + args[0]; // Comando do shell que você deseja executar

        try {
            executeShellCommand(command, 10); // Defina o tempo limite em segundos (por exemplo, 10 segundos)
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void executeShellCommand(final String command, final int timeoutInSeconds) throws IOException, InterruptedException {
        ProcessBuilder processBuilder = new ProcessBuilder(command.split(" "));
        final Process process = processBuilder.start();

        ExecutorService executor = Executors.newFixedThreadPool(2);
        Future<?> exitCodeFuture = executor.submit(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                return process.waitFor();
            }
        });
        Future<?> outputFuture = executor.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    printOutput(process.getInputStream());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        try {
            exitCodeFuture.get(timeoutInSeconds, TimeUnit.SECONDS);
            System.out.println("Comando executado com sucesso!");
        } catch (TimeoutException e) {
            process.destroy();
            System.err.println("Tempo limite atingido. O processo foi encerrado.");
        } catch (ExecutionException e) {
            System.err.println("Erro durante a execução do comando.");
            e.printStackTrace();
        } finally {
            executor.shutdownNow();
        }
    }

    private static void printOutput(InputStream inputStream) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
        }
        reader.close();
    }
}
