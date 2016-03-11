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

public class StockDesktop {
	class dataStruct{
		
	}
	
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
        		String stocks[]=getUrlData("http://hq.sinajs.cn/list=s_sh000001,s_sz002230,s_sz002736");
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
        
        ///////////debug////////////
        urlProcess(false, 2000, 500);
	}
	
	public static String[] getUrlData(String url){
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
				list.add(tmp[1]);
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		String[] stocks = (String[])list.toArray(new String[list.size()]);
		
		return stocks;
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
        System.out.println(sb);
        
		return sb.toString();
	}
}
