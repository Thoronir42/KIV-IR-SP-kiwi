package cz.zcu.students.kiwi;

import cz.zcu.students.kiwi.TREC.TrecJob;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

public class IrMain {
    public static void main(String[] args) {
        Logger.getRootLogger().setLevel(Level.INFO);
        BasicConfigurator.configure();

        IrJob job = new TrecJob(args);
        job.configureLogger();

        // todo: specify job, parameters...

        job.run();
    }
}
