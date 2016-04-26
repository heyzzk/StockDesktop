/******************************************************************************
*
* Authors            : JackZheng (heyzzk@126.com)
*
********************************************************************************
* Copyright (c) 2016, JackZheng.
* All rights reserved.
*******************************************************************************
* REVISON HISTORY
*
* VERSION | DATE          | DESCRIPTION
* 1.0.0   | 2016/04/14    | First release v1.0.0. Finish basic functions.
* 1.1.0   | 2016/04/26    | Add author statement & hide code fuction
*******************************************************************************/

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import FileOperate.FileOperate;

//example
//s_sh000001
//s_sz002230

public class StockDesktop {
	
    JFrame jf;  
    JPanel jp;  
    static JTextField jtf[] = new JTextField[10];//support 10 stocks
    static JButton jbt[] = new JButton[10];
    static JCheckBox jcb = new JCheckBox("隐藏");
	static JTextArea jtas = null;
    
    static ArrayList globallist;

	static String filecode[] = new String[10];//code string read from file
	static int codeactive[] = {0,0,0,0,0,0,0,0,0,0};
	static String urlfinal = "";

    public StockDesktop() {  
        jf = new JFrame("Desktop");  
        Container contentPane = jf.getContentPane();  
        contentPane.setLayout(null); 
        jp = new JPanel();  
        
        for(int i=0;i<10;i++){
            jtf[i] = new JTextField();  
            jp.add(jtf[i]);
            contentPane.add(jtf[i]);
            jtf[i].setBounds(10,20+30*i,180,25);
            jtf[i].setText("null");
            
            jbt[i] = new JButton("+");  
            jp.add(jbt[i]);
            contentPane.add(jbt[i]);
            jbt[i].setBounds(200,20+30*i,45,25);
            jbt[i].addActionListener(new ButtonListen());
        }
        
        jcb.setBounds(10,20+30*10,52,25);
        contentPane.add(jcb);

		//statement
		jtas = new JTextArea(); 
		jtas.setBounds(10,20+30*11,220,125);
		jtas.setText("深市:s_sz+代码  如 深指:s_sz399001"+"\n"
					+"沪市:s_sh+代码  如 沪指:s_sh000001"+"\n"
					+"作者:heyzzk@126.com"+"\n"
					+"All Rights Reserved v1.1@20160426");
		//jts.setBackground(Color.gray);
		jtas.setEditable(false);
		contentPane.add(jtas);

        //main window setup
        jf.pack();
        jf.setLocation(20, 860);  
        jf.setSize(260, 300);
        jf.setVisible(true);
        jf.addWindowListener(new WindowAdapter() {  
            public void windowClosing(WindowEvent e) {  
                System.exit(0);  
            }  
        });
    }
    
    class ButtonListen implements ActionListener {
	    public void actionPerformed(ActionEvent e) {
	    	String[] tmp = e.toString().split(",");
	    	int btid=(Integer.parseInt(tmp[5])-20)/30;

	    	String getDate = jtf[btid].getText();

			if(codeactive[btid]==1){//active->null
				filecode[btid] = "null";
				codeactive[btid] = 0;
				jtf[btid].setText("null");
				jbt[btid].setText("+");
				System.out.println("ID="+btid+",delete stock");
			}else{//null->active
				filecode[btid] = jtf[btid].getText();
				codeactive[btid] = 1;
				jbt[btid].setText("-");
				System.out.println("ID="+btid+",add stock");
			}
			
			//filecode write to file
			final FileOperate fop = new FileOperate("StockDesktop.db");
			urlfinal = urlProcess(filecode);
			StringBuilder sb = new StringBuilder();
			System.out.println("btn:filecode.length="+filecode.length);
			for(int i=0;i<filecode.length;i++)
				sb.append(filecode[i]+";");
			fop.fileWrite(sb.toString());
	    }
    }

	public static void fileProcess(){
		String readfile=null;
		final FileOperate fop = new FileOperate("StockDesktop.db");
		boolean fileexist = fop.fileExist();
		System.out.println("fileExit="+fileexist);
		if(fileexist){
			readfile = fop.fileRead();
		}else{
			fop.fileWrite("null;null;null;null;null;null;null;null;null;null;");
			readfile = fop.fileRead();
		}
		
		System.out.println("readfile="+readfile);
		filecode = readfile.split(";");
		System.out.println("filecode length="+filecode.length);
		for(int i=0;i<filecode.length;i++) {
			if(!filecode[i].equals("null")){
				System.out.println("filecode["+i+"]="+filecode[i]);
				codeactive[i] = 1;
				jbt[i].setText("-");
			}
			System.out.print(codeactive[i]+",");
		}
		System.out.println("");

		System.out.println("init end");

	}
	
	public static void main(String[] args) {
		
		new StockDesktop();//for content view

		fileProcess();//read or init file
				
        TimerTask task = new TimerTask() {
            public void run() {  
				System.out.println("timer");

				//combine the code to url
				urlfinal = urlProcess(filecode);
        		System.out.println("urlfinal="+urlfinal);
				
				//get data from url
				globallist=getUrlData(urlfinal);

				//display
        		String[] stocks = (String[])globallist.toArray(new String[globallist.size()]);
        		for(int i=0;i<stocks.length;i++){
        			String[] tmp = stocks[i].split(",");
        			
        			if(!tmp[0].equals("null")){
        				if(jcb.isSelected())//hide code
        					jtf[i].setText(""+"    "+tmp[1]+"    "+tmp[3]+" ");
        				else
        					jtf[i].setText(tmp[0]+"    "+tmp[1]+"    "+tmp[3]+" ");
        			}
        		}
            }
        };
        Timer timer = new Timer();  
        long delay = 0;  
        long intevalPeriod = 1 * 1000;  
        timer.scheduleAtFixedRate(task, delay, intevalPeriod);
	}
	
	public static ArrayList getUrlData(String url){
		BufferedReader reader = null;
		ArrayList list = new ArrayList(); 
		try {
			//read data from url
			URL ur = null;
			ur = new URL(url);
			HttpURLConnection uc = (HttpURLConnection) ur.openConnection();
			reader = new BufferedReader(new InputStreamReader(ur.openStream(),"GBK"));
			
			//split datas into line
			String line;
			while ((line = reader.readLine()) != null)
			{
				String[] tmp = line.split("\"");
	        	if(!tmp[1].equals("")){//非空行
		        	String[] tmpb=tmp[1].split(",");
		        	if(Float.parseFloat(tmpb[1])!=0){//股价不为0
		        		list.add(tmp[1]);
		        	}else{//股价=0
		        		list.add(tmp[1]);
		        	}
	        	}else{//空行（退市？）
	        		list.add("null,0,0,0,0,0,0");
	        	}
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//String[] stocks = (String[])list.toArray(new String[list.size()]);
		
		return list;
	}
	
	public static String urlProcess(String input[]) {
		String url="http://hq.sinajs.cn/list=";
		StringBuilder sb = new StringBuilder();
		sb.append(url);

		for(int i=0;i<input.length;i++){
			if(input[i] == null){
				sb.append("null,");
			}else{
				sb.append(input[i]+",");
			}
		}
		
		return sb.toString();
	}
}

