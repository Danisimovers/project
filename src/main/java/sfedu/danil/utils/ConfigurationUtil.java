package sfedu.danil.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Configuration utility. Allows to get configuration properties from the
 * default configuration file
 *
 * @author Boris Jmailov
 */
public class ConfigurationUtil {

    private static final Properties configuration = new Properties();

    public ConfigurationUtil() {
    }

    private static Properties getConfiguration(String filePath) throws IOException {
        if(configuration.isEmpty()){
            loadConfiguration(filePath);
        }
        return configuration;
    }

    private static void loadConfiguration(String filePath) throws IOException{
        File nf = new File(filePath);
        InputStream in = new FileInputStream(nf);
        try {
            configuration.load(in);
        } catch (IOException ex) {
            throw new IOException(ex);
        } finally{
            in.close();
        }
    }

    public static String getConfigurationEntry(String key, String filePath) throws IOException{
        return getConfiguration(filePath).getProperty(key);
    }
}

