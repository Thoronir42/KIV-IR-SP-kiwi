package cz.zcu.students.kiwi.TREC;

import cz.zcu.students.kiwi.IrJobSettings;

public class TrecJobSettings extends IrJobSettings {

    private String inputDir = "./TREC";
    private String outputDir = "./TREC-OUT";

    private TrecJobMode mode = TrecJobMode.TopicFile;

    private String trecCommand;
    private String trecRefFile;
    private boolean logToFile;

    public TrecJobSettings(String... args) {
        super();
        this.process(args);
    }

    public String getInputDir() {
        return inputDir;
    }

    public TrecJobSettings setInputDir(String inputDir) {
        this.inputDir = inputDir;
        return this;
    }

    public String getOutputDir() {
        return outputDir;
    }

    public TrecJobSettings setOutputDir(String outputDir) {
        this.outputDir = outputDir;
        return this;
    }

    public String getResultFile(String tag) {
        String filename = "results" + ((tag != null) ? "-" + tag : "") + ".txt";
        return getOutputDir() + "/" + filename;
    }

    public TrecJobMode getMode() {
        return mode;
    }

    public TrecJobSettings setMode(TrecJobMode mode) {
        this.mode = mode;
        return this;
    }

    public boolean isEvalEnabled() {
        if (this.trecCommand == null) {
            return false;
        }

        if (this.trecRefFile == null) {
            System.err.println("Trec eval command is set but relative file is missing");
            return false;
        }

        return true;
    }

    public TrecJobSettings setTrecEvalCommand(String trecCommand, String relativeFile) {
        this.trecCommand = trecCommand;
        this.trecRefFile = relativeFile;

        return this;
    }

    public String getTrecCommand() {
        return trecCommand;
    }

    public String getTrecRefFile() {
        return trecRefFile;
    }

    public TrecJobSettings setLogToFile(boolean logToFile) {
        this.logToFile = logToFile;
        return this;
    }

    public boolean getLogToFile() {
        return this.logToFile;
    }
}
