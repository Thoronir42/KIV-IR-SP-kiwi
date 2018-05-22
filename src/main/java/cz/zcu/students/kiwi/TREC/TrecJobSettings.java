package cz.zcu.students.kiwi.TREC;

import cz.zcu.students.kiwi.IrJobSettings;

public class TrecJobSettings extends IrJobSettings {

    private String inputDir = "./TREC";
    private String outputDir = "./TREC";

    private TrecJobMode mode = TrecJobMode.TopicFile;

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
        return getOutputDir() + "/ " + filename;
    }

    public TrecJobMode getMode() {
        return mode;
    }

    public TrecJobSettings setMode(TrecJobMode mode) {
        this.mode = mode;
        return this;
    }
}
