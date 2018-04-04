package example.ssw.sdb.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Properties;

import org.apache.log4j.Logger; 

public class SDBPropertiesLoader {
	 static Logger logger = Logger.getLogger(SDBPropertiesLoader.class);
	 
    private static String propertyFilePath="config.properties";
    private static Properties properties = new Properties();
    
    private static FileInputStream localInputStream;
    private static FileInputStream realInputStream;
   
    private static Hashtable<String, String> propMap = new Hashtable<>();
     
    public static String getProperty(String key){
    	String val = propMap.get(key);
    	return val;
    } 

    //프로퍼티 바꿀때 사용할 함수
    public static void setPropertis(String path){
        propertyFilePath = path; 
        loadProperty();
    }
    
    //스테틱으로 프로퍼티 로드
    static {
        loadProperty();
    }
    private static void loadProperty() {
        try {
            String DB_HOST;
            String DB_ID;
            String DB_PWD;
            String DB_NAME;
            String DB_URL;
            
            //프로퍼티파일 불러오기
            loadFileProperies();
            
            //불러온 프로퍼티를 변수에 할당
            DB_HOST = new String(properties.getProperty("DB_HOST").getBytes("ISO-8859-1"), "UTF-8");
            DB_ID = new String(properties.getProperty("DB_ID").getBytes("ISO-8859-1"), "UTF-8");
            DB_PWD = new String(properties.getProperty("DB_PWD").getBytes("ISO-8859-1"), "UTF-8");
            DB_NAME = new String(properties.getProperty("DB_NAME").getBytes("ISO-8859-1"), "UTF-8");
            DB_URL = String.format("%s/%s", DB_HOST, DB_NAME);
            
            propMap.put(Keys.DB_HOST, DB_HOST);  
            propMap.put(Keys.DB_ID, DB_ID);  
            propMap.put(Keys.DB_PWD, DB_PWD);  
            propMap.put(Keys.DB_NAME, DB_NAME);  
            propMap.put(Keys.DB_URL, DB_URL);  
            
        } catch (IOException e) {
            e.printStackTrace();
            logger.error(e);
        }
    }
    
    public static String getDBHost(){
    	return propMap.get(Keys.DB_HOST);
    }
    public static String getDBID(){
    	return propMap.get(Keys.DB_ID);
    }
    public static String getDBPwd(){
    	return propMap.get(Keys.DB_PWD);
    }
    public static String getDBName(){
    	return propMap.get(Keys.DB_NAME);
    }
    public static String getDBUrl(){
    	return propMap.get(Keys.DB_URL);
    }
     
    private static void loadFileProperies() throws FileNotFoundException, IOException {
        File propertiesFile = new File(propertyFilePath);
        if (propertiesFile.exists()) {
            loadPropertyFromFileInputStream();            //External files
        } else {
            loadPropertyFromResourceAsStream();            //Internal files (include in package)
        }
    }
    
    private static void loadPropertyFromFileInputStream() throws FileNotFoundException, IOException {
        localInputStream = new FileInputStream(propertyFilePath);
        properties.load(localInputStream);
    }
    
    private static void loadPropertyFromResourceAsStream() throws IOException {
        realInputStream = new FileInputStream("/"+propertyFilePath);
        properties.load(realInputStream);
    }
    
    public class Keys{ 
    	public static final String DB_HOST = "DB_HOST";
    	public static final String DB_PORT = "DB_PORT";
    	public static final String DB_PWD = "DB_PWD";
    	public static final String DB_ID="DB_ID";
    	public static final String DB_NAME="DB_NAME";
    	public static final String DB_URL="DB_URL";    	    
    }
}
 
 