import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.image.*;
import java.lang.System;
import java.lang.*;
import java.util.Timer;


class MainFrame extends JFrame
{
	//成员变量
	WindowForGetRect m_windowForGetRect;
	JButton m_btn_start,m_btn_setpos,m_btn_auto_set;
	JLabel m_Label;
	JSlider m_frequence;
	//Image m_Screen;
	BufferedImage m_image;
	int m_Map[][] = new int[15][23],m_px,m_py,m_screenH,m_screenW;
	int m_StdColor[] =new int[10],m_class_num;
	boolean m_Running=true;
	boolean m_HaveSet;
	//构造函数
	MainFrame()
	{
		super("打豆豆辅助工具");
		
		m_HaveSet=false;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setAlwaysOnTop(true);
		setSize(180+100,125);
		Container c = getContentPane();
		c.setLayout(new FlowLayout(FlowLayout.LEFT));
		
		this.m_btn_start = new JButton("开始");
		this.m_btn_auto_set = new JButton("自动设定");
		this.m_btn_setpos =new JButton("手动设定");
		this.m_Label = new JLabel("设定频率");
		this.m_frequence = new JSlider(50,1000,300);
		//this.m_frequence.createStandardLabels(250);
		this.m_frequence.setPaintLabels(true);
		this.m_frequence.setMinorTickSpacing (50);
		this.m_frequence.setMajorTickSpacing (200);
		this.m_frequence.setPaintTicks (true);
		this.m_frequence.putClientProperty("JSlider.isFilled",Boolean.TRUE);
		//this.m_frequence.setExtent(400);
		//
		c.add(this.m_btn_start);
		c.add(this.m_btn_auto_set);
		c.add(this.m_btn_setpos);
		c.add(this.m_Label);
		c.add(this.m_frequence);
		//c.add(this.m_Label);
		
		//this.setCursor(Cursor.CROSSHAIR_CURSOR);
		this.addKeyListener
		(
			new KeyAdapter()
			{
				public void keyPressed(KeyEvent e)
				{
					if(e.getKeyCode()==KeyEvent.VK_ESCAPE)
					{
						m_Running=false;
					}
				}
			}
		);
		this.m_btn_start.addActionListener
		(
			new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					DoStart();
				}
			}
		);
		this.m_btn_auto_set.addActionListener
		(
			new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					if(DoAutoSet())
					{
						JOptionPane.showMessageDialog(null,"自动获取成功","注意",JOptionPane.INFORMATION_MESSAGE);
						
						
					}else
					{
						JOptionPane.showMessageDialog(null,"自动获取失败","注意",JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		);
		this.m_btn_setpos.addActionListener
		(
			new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					
					DoSetPos();
				}
			}
		);
		
		//set position of window
		DoGetScreenSize();
		this.setLocation(this.m_screenW/2-90,this.m_screenH/2-140);
		
		setVisible(true);
	}
	
	void DoGetScreenSize()
	{	
			Toolkit tk = Toolkit.getDefaultToolkit();
			Dimension screenSize=tk.getScreenSize();
			this.m_screenH = (int)screenSize.getHeight();
			this.m_screenW = (int)screenSize.getWidth();
	}
	
	boolean DoAutoSet()
	{
		int StdW=583;
		int StdH=466;
		int i,j;
		
		Robot robot;
		try
		{
			robot = new Robot();
			BufferedImage image = robot.createScreenCapture(new Rectangle(0, 0,this.m_screenW,this.m_screenH));
			for(i=0;i<=this.m_screenW-StdW;++i)
			{
				for(j=0;j<=this.m_screenH-StdH;++j)
				{
					if(image.getRGB(i,j)==-1973791 && image.getRGB(i+583,j)==-1973791 && image.getRGB(i,j+465)==-1973791 && image.getRGB(i+583,j+465)==-1973791)
					{
						this.m_px = i;
						this.m_py = j+6;
						return true;
					}
				}
			}
			return false;
		}
		catch(AWTException e)
		{
			return false;
		}

	}
	//功能函数
	void DoSetPos()
	{
		
		Robot robot;
		try
		{
			robot = new Robot();
			BufferedImage image = robot.createScreenCapture(new Rectangle(0, 0,this.m_screenW,this.m_screenH));
			//JOptionPane.showMessageDialog(null,"11","d",JOptionPane.ERROR_MESSAGE,new ImageIcon(image));
			this.m_windowForGetRect = new WindowForGetRect(this,image);
		}
		catch(AWTException e)
		{
		}
	}
	
	void DoStart()
	{
		DoGetImg();
		//JOptionPane.showMessageDialog(null,"","d",JOptionPane.ERROR_MESSAGE,new ImageIcon(this.m_image));
		if(JOptionPane.YES_OPTION == JOptionPane.showConfirmDialog(this,"捕获是否合适？","捕获效果",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE,new ImageIcon(this.m_image)))
		{
			if(DoAnalysis())
			{
				m_Running=true;
				DoProc();
			}else
			{
				int i,j;
				for(i=0;i<15;i++)
				{
					for(j=0;j<23;j++)
					{
						System.out.printf("%d ",this.m_Map[i][j]);
					}
					System.out.printf("\n");
				}
			}
			
		}
		
	}
	void DoGetImg()
	{
		Robot robot;
		try
		{
			robot = new Robot();
			this.m_image = robot.createScreenCapture(new Rectangle(this.m_px+3,this.m_py+40,575,375));
		}
		catch(AWTException e)
		{
		}
	}

// get the map of game
	boolean  DoAnalysis()
	{
		int i,j,k;
		this.m_class_num=0;
		// getColorCategory
		for(i=0;i<15;i++)
		{
			for(j=0;j<23;j++)
			{
				//System.out.printf("<%d,%d>\n",j*25+13,i*25+13);
				int color = this.m_image.getRGB(j*25+13,i*25+13);
				
				//System.out.printf("<%d,%d>=%d\n",i,j,color);
				
				if(color==0xffffffff || color==-1184275)
				{
					this.m_Map[i][j] = -1;
					continue;
				}
				for(k=0;k<this.m_class_num;++k)
				{
					if(equal(this.m_StdColor[k],color))
					{
						this.m_Map[i][j] = k;
						break;
					}
				}
				if(k==this.m_class_num)
				{
					this.m_Map[i][j] = k;
					this.m_StdColor[this.m_class_num++]=color;
					if(this.m_class_num>10)
						return false;
				}
			}
		}
		for(i=0;i<15;i++)
		{
			for(j=0;j<23;j++)
			{
				System.out.printf("%d ",this.m_Map[i][j]);
			}
			System.out.printf("\n");
		}
		return true;
	}
// test color  
	boolean equal(int ca,int cb)
	{
		return ca==cb;
	}

// test whether this point can be clicked
	boolean can(int i,int j)
	{
		int a=i,b=i,c=j,d=j;
		int c1,c2,c3,c4,t;
		int s[] = new int [10];
		
		while(this.m_Map[a][j]==-1 ) { if(a==0) break; --a;}
		while(this.m_Map[b][j]==-1 ) { if(b==14) break;++b;}
		while(this.m_Map[i][c]==-1 ) { if(c==0) break; --c;}
		while(this.m_Map[i][d]==-1 ) { if(d==22) break;++d;}
		
		c1=this.m_Map[a][j];
		c2=this.m_Map[b][j];
		c3=this.m_Map[i][c];
		c4=this.m_Map[i][d];
		
		if(c1>=0) s[c1]++;
		if(c2>=0) s[c2]++;
		if(c3>=0) s[c3]++;
		if(c4>=0) s[c4]++;
		
		for(t=0;t<10;++t)
		{
			if(s[t]==2)
			{
				if(c1==c2)
				{
					this.m_Map[a][j]=-1;
					this.m_Map[b][j]=-1;
				}
				if(c1==c3)
				{
					this.m_Map[a][j]=-1;
					this.m_Map[i][c]=-1;
				}
				if(c1==c4)
				{
					this.m_Map[a][j]=-1;
					this.m_Map[i][d]=-1;
				}
				if(c2==c3)
				{
					this.m_Map[b][j]=-1;
					this.m_Map[i][c]=-1;
				}
				if(c2==c4)
				{
					this.m_Map[b][j]=-1;
					this.m_Map[i][d]=-1;
				}
				if(c3==c4)
				{
					this.m_Map[i][c]=-1;
					this.m_Map[i][d]=-1;
				}
				return true;
			}
			if(s[t]==4)
			{
				this.m_Map[a][j]=-1;
				this.m_Map[b][j]=-1;
				this.m_Map[i][c]=-1;
				this.m_Map[i][d]=-1;
				return true;
			}
				
		}
		
		return false;
	}
// perform mouse click
	void PerformClick(int i,int j)
	{
		Robot robot;
		try
		{
			robot = new Robot();
			robot.mouseMove(this.m_px+j*25+15,this.m_py+i*25+53);
			robot.mousePress(InputEvent.BUTTON1_MASK);
			robot.mouseRelease(InputEvent.BUTTON1_MASK);
		}
		catch(AWTException e)
		{
		}
		//System.out.println("0");
	}
//play game for me
	void DoProc()
	{
		int n=15*20,i=0,j=0;
		int flag=0;
		int fre=this.m_frequence.getValue();
		//loop
		while(m_Running)
		{	
			flag++;
			if(flag>23*15) break;
			
			if(this.m_Map[i][j]==-1)
			{
				if(can(i,j))
				{
					flag=0;
					n--;
					PerformClick(i,j);
					this.requestFocus(true);
					try
					{
						Thread.sleep(fre);
					}
					catch(InterruptedException e)
					{
					}
				}
			}
			// next p
			j+=1;
			if(j==23)
			{
				i+=1;
				j=0;
				if(i==15)
				{
					i=0;
				}
			}
		}
		//output left map
		for(i=0;i<15;i++)
		{
			for(j=0;j<23;j++)
			{
				System.out.printf("%d ",this.m_Map[i][j]);
			}
			System.out.printf("\n");
		}
	}
	
	
}


// window for get Rect to be Captured,Capture a ScreenShoot ,show it in the window
//user can set the position to be captured
class WindowForGetRect extends JFrame
{
	JLabel m_label;
	BufferedImage m_bf_img;
	MainFrame m_owner;
	
	WindowForGetRect(MainFrame own,BufferedImage img)
	{
		m_owner = own;
		m_bf_img =img;
		Container c = getContentPane();
		this.m_label = new JLabel(new ImageIcon(img));
		c.add(this.m_label);
		
		//Hide Window's Title
		setUndecorated(true);
				
		//FullScreen 
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice gd = ge.getDefaultScreenDevice();
		if(gd.isFullScreenSupported())
			gd.setFullScreenWindow(this);
		else
			System.out.println("Unsupported full screen.");
			
		//Set Cursor
		this.setCursor(Cursor.CROSSHAIR_CURSOR);
		
		//Add listener
		this.addKeyListener
		(
			new KeyAdapter()
			{
				public void keyPressed(KeyEvent e)
				{
					if(e.getKeyCode()==KeyEvent.VK_ESCAPE)
					{
						((WindowForGetRect)e.getSource()).dispose();
					}
				}
			}
		);
		
		this.addMouseListener
		(
			new MouseAdapter()
			{
				public void mousePressed(MouseEvent e)
				{
					//BufferedImage iimg= new BufferedImage(((WindowForGetRect)e.getSource()).img);
					//
					((WindowForGetRect)e.getSource()).m_owner.m_px = e.getX();
					((WindowForGetRect)e.getSource()).m_owner.m_py = e.getY();
					((WindowForGetRect)e.getSource()).dispose();
					//JOptionPane.showMessageDialog(null,""+((WindowForGetRect)e.getSource()).m_bf_img.getRGB(e.getX(),e.getY()),"注意",JOptionPane.ERROR_MESSAGE);
				}
			}
		);
	}
}

public class Main
{
	public static void main(String args[])
	{
		MainFrame m_mainFrame = new MainFrame();
	}
}

