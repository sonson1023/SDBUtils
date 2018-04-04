package example.ssw.sdb;

import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
  
import com.hp.hpl.jena.sdb.SDBFactory;
import com.hp.hpl.jena.sdb.Store;
import com.hp.hpl.jena.sdb.StoreDesc;
import com.hp.hpl.jena.sdb.sql.SDBConnection;
import com.hp.hpl.jena.sdb.store.DatabaseType;
import com.hp.hpl.jena.sdb.store.LayoutType;
import com.hp.hpl.jena.sdb.store.StoreFactory;
import com.hp.hpl.jena.util.FileManager;
import com.hp.hpl.jena.rdf.model.*;

public class SDBUtils extends Throwable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8802308515054175253L;
	private static final String JDBC_DRIVER_CLASSNAME = "org.mariadb.jdbc.Driver";
	private static  String DB_HOST = "jdbc:mariadb://localhost:3306";
	private static  String DB_ID = "root";
	private static  String DB_PWD = "123qwe";
	private static  String DB_NAME = "testrioplestore";
	private static  String DB_URL = String.format("%s/%s", DB_HOST, DB_NAME);
	
	private static StoreDesc storeDesc = null;
	private static Store store = null;
	private static SDBConnection sdbConn;
	private static Connection jdbcConn = null;
	
	
		
	static{
		try{
			Class.forName(JDBC_DRIVER_CLASSNAME);	
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	 
	public static void init(String dbHost, String dbName, String dbID, String dbPwd){
		DB_ID = dbID;
//		DB_HOST = dbHost;
		DB_HOST = String.format("jdbc:mariadb://%s:3306", dbHost);
		DB_NAME = dbName;
		DB_ID = dbID;
		DB_PWD = dbPwd;
		DB_URL =  String.format("%s/%s", DB_HOST, DB_NAME);
	}

	
	public static void save(String filePath, String ns, String prefix){
		try{
			storeDesc = new StoreDesc(LayoutType.LayoutTripleNodesHash, DatabaseType.MySQL);

			jdbcConn = getConnection();
			if(jdbcConn == null){
				throw new Exception("jdbcConn is null");
			}
			sdbConn = new SDBConnection(jdbcConn);
			store = StoreFactory.create(storeDesc, sdbConn);
			
			long ms = System.currentTimeMillis();
			
			 Model model = SDBFactory.connectDefaultModel(store);

			InputStream is =FileManager.get().open(filePath);
			model.setNsPrefix(ns, prefix);
			// model.setNsPrefix("rdf",
			// "http://www.w3.org/1999/02/22-rdf-syntax-ns#");
			model.getResource("");
			model.read(is, null);
			System.out.println("END LOADING Time: " + (System.currentTimeMillis() - ms));
			 
			jdbcConn.close();
			sdbConn.close();
			store.close();

		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public static void save1(String filePath, String ns, String prefix){
		try{
			storeDesc = new StoreDesc(LayoutType.LayoutTripleNodesHash, DatabaseType.MySQL);

			jdbcConn = getConnection();
			if(jdbcConn == null){
				throw new Exception("jdbcConn is null");
			}
			sdbConn = new SDBConnection(jdbcConn);
			store = StoreFactory.create(storeDesc, sdbConn);
			
			long ms = System.currentTimeMillis();
			
			 Model model = SDBFactory.connectDefaultModel(store);

			InputStream is =FileManager.get().open(filePath);
			model.setNsPrefix(ns, prefix); 
			
			model.getResource("");
			model.read(is, ns);
			System.out.println("END LOADING : " + (System.currentTimeMillis() - ms));
			 
			jdbcConn.close();
			sdbConn.close();
			store.close();

		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public static void load(){
		try{
			storeDesc = new StoreDesc(LayoutType.LayoutTripleNodesHash, DatabaseType.MySQL);

			jdbcConn = getConnection();
			sdbConn = new SDBConnection(jdbcConn);
			store = StoreFactory.create(storeDesc, sdbConn);
		 
			Model baseModel = SDBFactory.connectDefaultModel(store);
			
			baseModel.write(System.out);
		
			jdbcConn.close();
			sdbConn.close();
			store.close();	
		}catch(Exception e){
			e.printStackTrace();
		}  
	}
	
	public static void load(OutputStream os){
		try{
			storeDesc = new StoreDesc(LayoutType.LayoutTripleNodesHash, DatabaseType.MySQL);

			jdbcConn = getConnection();
			sdbConn = new SDBConnection(jdbcConn);
			store = StoreFactory.create(storeDesc, sdbConn);
		 
			Model baseModel = SDBFactory.connectDefaultModel(store);
//			baseModel.write(os);
			baseModel.write(System.out);
			baseModel.commit();
			baseModel.close();
			
			jdbcConn.close();
			sdbConn.close();
			store.close();	
		}catch(Exception e){
			e.printStackTrace();
		}  
	}
	public static void clearTB(){
		String query1 = "DELETE FROM nodes";
		String query2 = "DELETE FROM prefixes";
		String query3 = "DELETE FROM quads";
		String query4 = "DELETE FROM triples";
		PreparedStatement ps = null;
		try{
			storeDesc = new StoreDesc(LayoutType.LayoutTripleNodesHash, DatabaseType.MySQL);

			jdbcConn = getConnection();
			sdbConn = new SDBConnection(jdbcConn);
			store = StoreFactory.create(storeDesc, sdbConn);
		 
//			Model baseModel = SDBFactory.connectDefaultModel(store);
//			baseModel.removeAll();
			
			ps = jdbcConn.prepareStatement(query1);
			ps.executeQuery();
		
			ps = jdbcConn.prepareStatement(query2);
			ps.executeQuery();
			
			ps = jdbcConn.prepareStatement(query3);
			ps.executeQuery();
			
			ps = jdbcConn.prepareStatement(query4);
			ps.executeQuery();
			System.out.println("remove all");
			jdbcConn.close();
			sdbConn.close();
			store.close();	
		}catch(Exception e){
			e.printStackTrace();
		}		
	}
	private static Connection getConnection() {
		try {
			return DriverManager.getConnection(DB_URL, DB_ID, DB_PWD);
		} catch (SQLException ex) {
			ex.printStackTrace();
//			throw new SDBException("SQL Exception while connecting to database: " + DB_URL + " : " + ex.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
//			throw new SDBException("SQL Exception while connecting to database: " + DB_URL + " : " + e.getMessage());
		}
		return null;
	}
}
