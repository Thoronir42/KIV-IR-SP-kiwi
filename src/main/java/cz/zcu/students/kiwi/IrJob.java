package cz.zcu.students.kiwi;

import org.apache.log4j.Logger;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public abstract class IrJob implements Runnable {
    protected static Logger log = Logger.getLogger(IrJob.class);

    protected static final DateFormat SDF = new SimpleDateFormat("yyyy-MM-dd_HH_mm_SS");

    protected boolean printExceptions = true;

    public boolean configureLogger() {
        return false;
    }

    public final void run() {
        String className = getClass().getSimpleName();

        try {
            if (execute()) {
                log.info(className + ": run complete");
            } else {
                throw new RuntimeException(className + " returned falsy value");
            }
        } catch (Exception ex) {
            log.warn(className + ": " + ex.toString());
            if (this.printExceptions) {
                ex.printStackTrace();
            }
        }
    }

    protected abstract boolean execute() throws Exception;

    protected boolean ensureDirectoriesExist(String... dirs) {
        for (String dirName : dirs) {
            File dir = new File(dirName);
            if (!dir.exists()) {
                boolean mkdirs = dir.mkdirs();
                if (!mkdirs) {
                    log.error("Output directory can't be created! Please either create it or change the STORAGE parameter.\nOutput directory: " + dir);
                    return false;
                }
                log.info("Output directory created: " + dir);
            }

        }

        return true;
    }


    public String time() {
        return SDF.format(System.currentTimeMillis());
    }
}
