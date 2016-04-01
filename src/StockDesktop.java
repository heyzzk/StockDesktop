import java.awt.BorderLayout;
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
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

import FileOperate.FileOperate;

//20160311
//SH:600000-603999:4000
//SZ:000001-002792,300001-300489:2792+488=3280
//0311 TODO:先获得数据，检查一遍，把活动的ID保存到数组，再用这数据拼接成URL，这样就只要2600+数据，节省时间。
//打印出消耗时间；
public class StockDesktop {
	
    JFrame jf;  
    JPanel jp;  
    static JTextField jtf[] = new JTextField[10];//支持10组数据
    static JButton jbt[] = new JButton[10];
    
    static ArrayList globallist;

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
            
            jbt[i] = new JButton();  
            jp.add(jbt[i]);
            contentPane.add(jbt[i]);
            jbt[i].setBounds(200,20+30*i,25,25);
            jbt[i].addActionListener(new ButtonListen());
        }

        //主窗口设置
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
	    	//System.out.println("你单击了按钮:"+btid);
	    	//jtf[btid].setText("null");
	    	String getDate = jtf[btid].getText();
	    	if(!getDate.equals("null")){
	    		//删除掉这个stock
	    		System.out.println("ID="+btid+",删除掉这个stock");
	    	}else{
	    		//为空，增加这个stock
	    		System.out.println("ID="+btid+",增加这个stock");
	    	}
	    	
	    	//重新拼凑URL并写入文件
	    }
    }
	
	public static void main(String[] args) {
		
		new StockDesktop();//for content view
		
		final FileOperate fop = new FileOperate("file");
        
		final DecimalFormat df = new DecimalFormat("0.00");
		
        TimerTask task = new TimerTask() {
            public void run() {  
        		globallist=getUrlData(fop.fileRead());
        		//System.out.println("长度"+list.size());
        		String[] stocks = (String[])globallist.toArray(new String[globallist.size()]);
        		for(int i=0;i<stocks.length;i++){
        			String[] tmp = stocks[i].split(",");
        			
        			if(!tmp[0].equals("null"))
        				jtf[i].setText(tmp[0]+"    "+tmp[1]+"    "+tmp[3]+"%");
        		}
            }
        };
        Timer timer = new Timer();  
        long delay = 0;  
        long intevalPeriod = 1 * 1000;  
        timer.scheduleAtFixedRate(task, delay, intevalPeriod);
        
        //20160311
		//SZ:000001-002792,300001-300489:2792+488=3280
        //SH:600000-603999:4000
        
        ///////////debug////////////
		//SZ
//        String urlSZ = urlProcess(false, 0, 700);//sz
//        ArrayList listSZ=getUrlData(urlSZ);
//        urlSZ = urlProcess(false, 700, 700);//sz
//        listSZ.addAll(getUrlData(urlSZ));
//        urlSZ = urlProcess(false, 1400, 700);//sz
//        listSZ.addAll(getUrlData(urlSZ));
//        urlSZ = urlProcess(false, 2100, 700);//sz
//        listSZ.addAll(getUrlData(urlSZ));
//        urlSZ = urlProcess(false, 300000, 700);//sz
//        listSZ.addAll(getUrlData(urlSZ));
//        System.out.println(listSZ.size());
//
//        //SH
//        String urlSH = urlProcess(true, 600000, 700);//sh
//        ArrayList listSH=getUrlData(urlSH);
//        urlSH = urlProcess(true, 600700, 700);//sh
//        listSH.addAll(getUrlData(urlSH));
//        urlSH = urlProcess(true, 601400, 700);//sh
//        listSH.addAll(getUrlData(urlSH));
//        urlSH = urlProcess(true, 602100, 700);//sh
//        listSH.addAll(getUrlData(urlSH));
//        urlSH = urlProcess(true, 602800, 700);//sh
//        listSH.addAll(getUrlData(urlSH));
//        urlSH = urlProcess(true, 603500, 700);//sh
//        listSH.addAll(getUrlData(urlSH));
//        System.out.println(listSH.size());
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
	
	public static String urlProcess(Boolean shanghai, int start, int length) {
		DecimalFormat df = new DecimalFormat("000000");
		StringBuilder sb = new StringBuilder();
		String url="http://hq.sinajs.cn/list=";
		String headsh=",s_sh";
		String headsz=",s_sz";
		
		sb.append(url);

        for (int i = start; i < start+length; i++) {
        	if(shanghai)
        		sb.append(headsh);
        	else
				sb.append(headsz);
        	sb.append(String.valueOf(df.format(i)));
        }
        
		return sb.toString();
	}
}
