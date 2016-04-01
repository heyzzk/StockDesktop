package FileOperate;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class FileOperate {
    private static BufferedReader br = null;  
    private static BufferedWriter bw = null;  
    private static File file = null;  
    String filename = null;
    
    public FileOperate(String n) {
		// TODO Auto-generated constructor stub
    	filename = n;
	}
    
	public void fileWrite(String data){
		file = new File(filename);
        if (!file.exists() != false) {
            try {
                file.createNewFile();//不存在，新建
            } catch (IOException e) {  
                e.printStackTrace();  
            }  
        } else {//存在，清空后再写入
        }
        
        try {  
            bw = new BufferedWriter(new FileWriter(file));  
                bw.write(data);//输出字符串  
                bw.newLine();//换行  
                bw.flush();  
        } catch (Exception e) {  
            e.printStackTrace();  
        }
	}
	
	public String fileRead(){
		String b = null;
		try{
	        FileReader fr = new FileReader(filename);  
	        br = new BufferedReader(fr);  
	        //while ((b = br.readLine()) != null) {
	        //    System.out.println("filetest="+b);
	        //}  
	        b = br.readLine();
		}catch(Exception e){
			
		}finally {  
            try {  
                br.close();  
            } catch (IOException e) {  
                e.printStackTrace();  
            }  
        } 
		return b;
	}
}
