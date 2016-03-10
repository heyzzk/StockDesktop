import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import com.sun.org.apache.bcel.internal.generic.LLOAD;

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
	}

}