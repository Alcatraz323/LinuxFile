
import java.io.*;
import java.text.*;public class EnuTask
{
	public static void run(final String root,Daemon d){
		new Thread(new Runnable(){

				@Override
				public void run()
				{
					File rootf=new File(root);
					EnuBean root_n=new EnuBean();
					root_n.setDepth(0);
					root_n.setSize(getDirSize(rootf));
					
					// TODO: Implement this method
				}
			});
	}
	interface Daemon{
		public void progress(EnuBean cur);
		public void onComplete(EnuBean res);
	}
	public static String getNetFileSizeDescription(long size) {  
        StringBuffer bytes = new StringBuffer();  
        DecimalFormat format = new DecimalFormat("###.0");  
        if (size >= 1024 * 1024 * 1024) {  
            double i = (size / (1024.0 * 1024.0 * 1024.0));  
            bytes.append(format.format(i)).append("GB");  
        }  
        else if (size >= 1024 * 1024) {  
            double i = (size / (1024.0 * 1024.0));  
            bytes.append(format.format(i)).append("MB");  
        }  
        else if (size >= 1024) {  
            double i = (size / (1024.0));  
            bytes.append(format.format(i)).append("KB");  
        }  
        else if (size < 1024) {  
            if (size <= 0) {  
                bytes.append("0B");  
            }  
            else {  
                bytes.append((int) size).append("B");  
            }  
        }  
        return bytes.toString();  
    }
	public static double getDirSize(File file) {     
        //判断文件是否存在     
        if (file.exists()) {     
            //如果是目录则递归计算其内容的总大小    
            if (file.isDirectory()) {     
                File[] children = file.listFiles();     
                double size = 0;     
                for (File f : children)     
                    size += getDirSize(f);     
                return size;     
            } else {//如果是文件则直接返回其大小,以“兆”为单位   
                double size = (double) file.length() / 1024 / 1024;        
                return size;     
            }     
        } else {     
            System.out.println("文件或者文件夹不存在，请检查路径是否正确！");     
            return 0.0;     
        }     
    }     
}
