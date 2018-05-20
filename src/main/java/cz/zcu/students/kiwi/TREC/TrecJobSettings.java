package cz.zcu.students.kiwi.TREC;

import cz.zcu.students.kiwi.IrJobSettings;

public class TrecJobSettings extends IrJobSettings {
    private String outputDir = "./TREC";

    public TrecJobSettings(String ...args) {
        super();
        this.process(args);
    }

    public String getOutputDir() {
        return outputDir;
    }

    public TrecJobSettings setOutputDir(String outputDir) {
        this.outputDir = outputDir;
        return this;
    }
}
