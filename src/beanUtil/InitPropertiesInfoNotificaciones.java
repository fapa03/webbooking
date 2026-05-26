package beanUtil;
import java.io.InputStream;
import java.io.InputStream;
import java.util.Properties;

import logger.AccessLog;

public class InitPropertiesInfoNotificaciones {
    private static Properties properties;

    /**
     * Constructor sin parametros
     */
    private InitPropertiesInfoNotificaciones() {
    }

    /**
     * Método que regresa un objeto properties singleton con todos los querys de
     * la aplicacion
     *
     * @return Objeto properties con querys de aplicacion
     */
    public static Properties getInstance() {
        try {
            initSessionFactory();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return properties;
    }

    /**
     * Método que que regresa un objeto properties no singleton con todos los
     * querys de la aplicacion
     *
     * @return Objeto properties con querys de aplicacion
     */
    public static Properties getReloadInstance() {
        try {
            properties = null;
            initSessionFactory();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return properties;
    }

    /**
     * Método que regresa el query en base a la clave solicitada
     *
     * @param key es la clave para obtener el query
     * @return query con la clave
     */
    public String getValue(String key) {
        return properties.getProperty(key);
    }

    /**
     * Método que lee el archivo querys.Properties y los almacena en una
     * variable singleton
     */
    private static synchronized void initSessionFactory() throws Exception {
    	InputStream is = null;
        try {
            if (properties == null) {
                is = InitPropertiesInfoNotificaciones.class.getClassLoader().getResourceAsStream("infoNotificacion.properties");
                properties = new Properties();
                properties.load(is);
                is.close();
                is = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                is.close();
                is = null;
            }
        }
    }
}