import java.awt.BorderLayout;
import java.awt.Container;
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

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

//20160311
//SH:600000-603999:4000
//SZ:000001-002792,300001-300489:2792+488=3280
//0311 TODO:先获得数据，检查一遍，把活动的ID保存到数组，再用这数据拼接成URL，这样就只要2600+数据，节省时间。
//打印出消耗时间；
public class StockDesktop {

	
    JFrame jf;  
    JPanel jp;  
    static JTextField jtf1,jtf2,jtf3;

    public StockDesktop() {  
        jf = new JFrame("Desktop");  
        Container contentPane = jf.getContentPane();  
        contentPane.setLayout(null); 
        jp = new JPanel();  
        jtf1 = new JTextField();  
        jtf2 = new JTextField();  
        jtf3 = new JTextField();  
        jp.add(jtf1);
        jp.add(jtf2);
        jp.add(jtf3);
        contentPane.add(jtf1);
        contentPane.add(jtf2);
        contentPane.add(jtf3);
        jtf1.setBounds(10,20,180,25);
        jtf2.setBounds(10,50,180,25);
        jtf3.setBounds(10,80,180,25);

        jf.pack();
        jf.setLocation(20, 860);  
        jf.setSize(200, 160);
        jf.setVisible(true);
        jf.addWindowListener(new WindowAdapter() {  
            public void windowClosing(WindowEvent e) {  
                System.exit(0);  
            }  
        });  
    }
	
	public static void main(String[] args) {
		
		new StockDesktop();//for content view
        
		final DecimalFormat df = new DecimalFormat("0.00");
		
        TimerTask task = new TimerTask() {  
            public void run() {  
        		ArrayList list=getUrlData("http://hq.sinajs.cn/list=s_sh000001,s_sz002230,s_sz002736");
        		String[] stocks = (String[])list.toArray(new String[list.size()]);
        		for(int i=0;i<stocks.length;i++){
        			//System.out.println(stocks[i]);
        			String[] tmp = stocks[i].split(",");

        			//System.out.println(tmp[0]+","+tmp[3]+","+df.format(ratio));
        			if(i==0)jtf1.setText(tmp[0]+"    "+tmp[1]+"    "+tmp[3]+"%");
        			if(i==1)jtf2.setText(tmp[0]+"    "+tmp[1]+"    "+tmp[3]+"%");
        			if(i==2)jtf3.setText(tmp[0]+"    "+tmp[1]+"    "+tmp[3]+"%");
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
	        	if(!tmp[1].equals("")){//空行（退市？）
		        	String[] tmpb=tmp[1].split(",");
		        	if(Float.parseFloat(tmpb[1])!=0){//过滤掉停牌的（股价=0）
		        		list.add(tmp[1]);
		        	}
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
