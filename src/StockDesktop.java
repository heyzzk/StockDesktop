import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import com.sun.org.apache.bcel.internal.generic.LLOAD;

public class StockDesktop {
	
	public static void main(String[] args) {
		JFrame jf=new JFrame("����");
		jf.setLayout(new FlowLayout());//��������
		jf.setLocationRelativeTo(null);//����
		jf.setDefaultCloseOperation(jf.EXIT_ON_CLOSE);
		jf.setSize(400, 300);
		jf.setVisible(true);
		jf.add(new JLabel("�˺�"));//
		jf.add(new JTextField(10));//���ı�����ӵ�����
		jf.add(new JLabel("����"));
		jf.add(new JTextField(10));
		jf.add(new JButton("��½"));//��������Ӱ�ť
	}

}