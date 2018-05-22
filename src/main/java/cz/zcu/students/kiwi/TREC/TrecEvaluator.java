package cz.zcu.students.kiwi.TREC;

import org.apache.log4j.Logger;

import java.io.*;

public class TrecEvaluator {
    private static final Logger log = Logger.getLogger(TrecEvaluator.class);

    protected final String trecCommand;
    private final String trecReference;

    public TrecEvaluator(String trecCommand, String trecReference) {
        this.trecCommand = trecCommand;
        this.trecReference = trecReference;
    }


    public boolean runTrecEval(File output, String compareFile) {
        try {
            Process process = this.buildProcess(output, compareFile);

            return waitForProcess(process);
        } catch (IOException ex) {
            log.warn("Trec eval did not finish: " + ex.toString());
            return false;
        }
    }

    public boolean runTrecEval(OutputStream outputStream, String compareFile) {
        PrintStream ps = outputStream instanceof PrintStream ? (PrintStream) outputStream : new PrintStream(outputStream);

        try {
            Process process = this.buildProcess(null, compareFile);
            pipe(process.getInputStream(), ps);

            return waitForProcess(process);
        } catch (IOException ex) {
            log.warn("Trec eval did not finish: " + ex.toString());
            return false;
        }
    }

    protected String[] formatTrecCommand(String trecCommand, String trecReference, String compareFile) {
        return new String[]{trecCommand, trecReference, compareFile};
    }

    private Process buildProcess(File output, String compareFile) throws IOException {
        ProcessBuilder builder = new ProcessBuilder(formatTrecCommand(trecCommand, trecReference, compareFile));
        if (output != null) {
            builder.redirectOutput(ProcessBuilder.Redirect.appendTo(output));
        }

        log.info("exec( " + String.join(" ", builder.command()) + " )");
        return builder.start();
    }

    private boolean waitForProcess(Process process) throws IOException {
        try {
            int exitStatus = process.waitFor();
            log.info("Trec eval exited with status: " + exitStatus);

            return exitStatus == 0;
        } catch (InterruptedException ex) {
            throw new IOException("Trec eval did not finish", ex);
        }
    }

    private void pipe(InputStream in, PrintStream out) {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(in))) {
            String line;
            while ((line = br.readLine()) != null) {
                out.println(line);
            }
        } catch (IOException ex) {
            log.warn(ex);
        }
    }
}
