import java.awt.FlowLayout;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class StockDesktop {
	
	public static void main(String[] args) {
		JFrame jf=new JFrame("界面");
		jf.setLayout(new FlowLayout());//布局设置
		jf.setLocationRelativeTo(null);//居中
		jf.setDefaultCloseOperation(jf.EXIT_ON_CLOSE);
		jf.setSize(400, 300);
		jf.setVisible(true);
		jf.add(new JLabel("账号"));//
		jf.add(new JTextField(10));//将文本框添加到界面
		jf.add(new JLabel("密码"));
		jf.add(new JTextField(10));
		jf.add(new JButton("登陆"));//创建并添加按钮
		getdata();
	}
	
	public static void getdata(){
		URL ur = null;
		BufferedReader reader = null;
		try {
			ur = new URL("http://hq.sinajs.cn/list=sh601006,sh601001");
			HttpURLConnection uc = (HttpURLConnection) ur.openConnection();
			reader = new BufferedReader(new InputStreamReader(ur.openStream(),"GBK"));
			String line;
			while ((line = reader.readLine()) != null)
			{
				System.out.print(line);
			}
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
