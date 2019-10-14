package data;

import java.io.BufferedReader;
import java.io.PrintWriter;

public class Participant {

    private int id;
    private BufferedReader bufferedReader;
    private PrintWriter printWriter;

    public Participant(int id, BufferedReader bufferedReader, PrintWriter printWriter) {
        this.id = id;
        this.bufferedReader = bufferedReader;
        this.printWriter = printWriter;

    }

    public int getId() {
        return this.id;
    }

    public BufferedReader getBufferedReader() {
        return this.bufferedReader;
    }

    public PrintWriter getPrintWriter() {
        return this.printWriter;
    }
}