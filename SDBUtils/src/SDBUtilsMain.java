import example.ssw.sdb.SDBUtils;

public class SDBUtilsMain {

	public static void main(String args[]){ 
		String prefix = "http://example.co.kr/ssw#";
		String ns = "ssw";
		String filePath = "data/example.rdf";
		
		SDBUtils.clearTB();
		SDBUtils.save(filePath, ns, prefix);
		SDBUtils.load();
		
	}

}
