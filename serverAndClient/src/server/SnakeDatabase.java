package server;

import client.Snake;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class SnakeDatabase {

    private static final String FILE_PATH = "snakes.txt";

    private final List<Snake> snakes = new ArrayList<>();
    private final List<String[]> heads = new ArrayList<>();

    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    public SnakeDatabase() {
        loadFromFile();
    }

    public List<Snake> getAll() {
        return snakes;
    }

    public List<String[]> getAllHeads() {
        return heads;
    }

    public List<Snake> filterByAttribute(String value) {
        List<Snake> result = new ArrayList<>();
        for (Snake s : snakes) {
            if (s.getName().equals(value) ||
                s.getSize().equals(value) ||
                s.getColor().equals(value)) {
                result.add(s);
            }
        }
        return result;
    }

    public List<String[]> filterHeadByAttribute(String value) {
        List<String[]> result = new ArrayList<>();
        for (int i = 0; i < snakes.size(); i++) {
            if (snakes.get(i).getName().equals(value)) {
                result.add(heads.get(i));
            }
        }
        return result;
    }

    public boolean add(Snake snake, String[] head) {
        for (Snake s : snakes) {
            if (s.getName().equals(snake.getName()) &&
                s.getSize().equals(snake.getSize()) &&
                s.getColor().equals(snake.getColor())) {
                System.out.println(" Serpiente duplicada: " + snake);
                return false;
            }
        }
        snakes.add(snake);
        heads.add(head);
        System.out.println(" Añadiendo serpiente: " + snake);
        saveToFile();
        return true;
    }

    public boolean update(Snake updatedSnake) {
        for (int i = 0; i < snakes.size(); i++) {
            if (snakes.get(i).getName().equals(updatedSnake.getName())) {
                snakes.set(i, updatedSnake);
                saveToFile();
                return true;
            }
        }
        return false;
    }

    public boolean deleteByName(String name) {
        for (int i = 0; i < snakes.size(); i++) {
            if (snakes.get(i).getName().equals(name)) {
                snakes.remove(i);
                heads.remove(i);
                saveToFile();
                return true;
            }
        }
        return false;
    }

    private void loadFromFile() {
        lock.writeLock().lock();
        snakes.clear();
        heads.clear();

        File file = new File(FILE_PATH);
        if (!file.exists()) {
            System.out.println(" snakes.txt no encontrado. Se creará al guardar.");
            lock.writeLock().unlock();
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            boolean readingMetadata = false;

            // Leer serpientes
            while ((line = reader.readLine()) != null) {
                if (line.equals("metadata")) {
                    readingMetadata = true;
                    break;
                }
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    Snake snake = new Snake(parts[0], parts[1], parts[2]);
                    snakes.add(snake);
                }
            }

            // Leer headers
            while ((line = reader.readLine()) != null && readingMetadata) {
                int numHeaders = Integer.parseInt(line);
                String[] head = new String[numHeaders];
                for (int i = 0; i < numHeaders; i++) {
                    head[i] = reader.readLine();
                }
                heads.add(head);
            }

            System.out.println(" snakes.txt cargado con " + snakes.size() + " serpientes.");

        } catch (IOException | NumberFormatException e) {
            System.err.println(" Error cargando snakes.txt: " + e.getMessage());
        } finally {
            lock.writeLock().unlock();
        }
    }

    private void saveToFile() {
        lock.writeLock().lock();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            System.out.println(" Guardando serpientes en " + FILE_PATH);

            // Guardar serpientes
            for (Snake s : snakes) {
                writer.write(s.toString());
                writer.newLine();
            }

            // Escribir separador
            writer.write("metadata");
            writer.newLine();

            // Guardar headers
            for (String[] h : heads) {
                writer.write(Integer.toString(h.length));
                writer.newLine();
                for (String entry : h) {
                    writer.write(entry);
                    writer.newLine();
                }
            }

            System.out.println(" Guardado completado.");

        } catch (IOException e) {
            System.err.println(" Error al guardar: " + e.getMessage());
        } finally {
            lock.writeLock().unlock();
        }
    }
}
