


import java.io.File;

import logger.AccessLog;

public class DeleteReports{
	String path;
	public DeleteReports(String path){
		this.path=path;
		AccessLog.Log("PATH "+path);
		File f=new File(path);
		String fileList[] = f.list();
		for(int i=0;i<fileList.length;i++){
			AccessLog.Log("FILE NAME: "+i+" "+fileList[i]);
			AccessLog.Log("LAST MODIFIED DATE : "+i+" "+new File(fileList[i]).lastModified());
		}
	}
	
	
}