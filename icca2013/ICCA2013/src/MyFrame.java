import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.TitledBorder;
import java.text.*;
import java.util.*;
import java.io.*;
import java.text.*;
import ELM_.*;

public class MyFrame extends JFrame
	implements ActionListener,ListSelectionListener,MouseListener{

		//set file chooser
		private JFileChooser jfchooser = new JFileChooser();
		private JMenuItem OpenTraining = new JMenuItem("Open TrainingData File...");
		private JMenuItem OpenTesting = new JMenuItem("Open TestingData File...");

		//Files read from user
		private File fTrainingData_File;
		private File fTestingData_File;

		//set text fields for inputs
		private JTextField jtfTrainingData_File = new JTextField();
		private JTextField jtfTestingData_File = new JTextField();
		private JTextField jtfELM_Type = new JTextField();
		private JTextField jtfNumberofHiddenNeurons = new JTextField();

		//set list for activationfunction selection
		private String ActivationList[] = {"sigmoid","sine","hardlim"};
		private JList jlist = new JList(ActivationList);

		//set list for percentage of file to be extracted
		String Percentage[] = {"100%","50%","25%","12.5%","6.25%"};
		private JList jlistPercentage = new JList(Percentage);

		//set the array for corresponding percentage list
		private double PercentageFileList[] = {100,50,25,12.5,6.25};

		//set text fields for outputs
		private JTextField jtfTrainingTime = new JTextField();
		private JTextField jtfTestingTime = new JTextField();
		private JTextField jtfTrainingAccuracy = new JTextField();
		private JTextField jtfTestingAccuracy = new JTextField();

		//set text fields for adjust the graph
		private JTextField jtfScaleX = new JTextField();
		private JTextField jtfScaleY = new JTextField();

		//set the buttons
		private JButton jbtExecute = new JButton("Run the ELM Testing");
		private JButton jbtResetInputs = new JButton("Reset");
		private JButton jbtShowTraining = new JButton("Show Training Data File");
		private JButton jbtShowTesting = new JButton("Show Testing Data File");
		private JButton jbtShowOutput = new JButton("Show Output");
		private JButton jbtShowInOutput = new JButton("Show Input and Output");

		private JLabel jlbTrainingGraph = new JLabel("Training Data Graph");

		//Inputs from User
		private String TrainingData_File;
		private String TestingData_File;
		private int ELM_Type;
		private int NumberofHiddenNeurons;
		private String ActivationFunction = "";
		private double PercentageFile = 100;

		//Graphpanel
		GraphPanel p3 = new GraphPanel();

		//Outputs
		private double Output[];
		private double RegOutput[][];

		//Determine to Execute ELM function
		private boolean Execute;

		//set the Timer
		javax.swing.Timer timer;

		//determine to plot the graph
		//boolean plot;

	/**
		Execute main method to invoke GUI
	*/

	public static void main(String[] args) {

	MyFrame f1 = new MyFrame();
	f1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	f1.setTitle("Extreme Learning Machine");
	f1.setSize(1200,784);
	f1.setVisible(true);
	f1.MAIN();



	}

	/**
		Construct the Main GUI
	*/
	public void MAIN(){

	//initialize not to plot the graph
	//plot = false;

	//set dimension of text field, set one will affect all text fields within a same panel
	jtfTrainingData_File.setPreferredSize(new java.awt.Dimension(30, 10));
	jtfScaleX.setPreferredSize(new java.awt.Dimension(30, 10));


	//align text fields
	jtfTrainingData_File.setHorizontalAlignment(JTextField.RIGHT);
	jtfTestingData_File.setHorizontalAlignment(JTextField.RIGHT);
	jtfELM_Type.setHorizontalAlignment(JTextField.RIGHT);
	jtfNumberofHiddenNeurons.setHorizontalAlignment(JTextField.RIGHT);
	jtfTrainingTime.setHorizontalAlignment(JTextField.RIGHT);
	jtfTestingTime.setHorizontalAlignment(JTextField.RIGHT);
	jtfTrainingAccuracy.setHorizontalAlignment(JTextField.RIGHT);
	jtfTestingAccuracy.setHorizontalAlignment(JTextField.RIGHT);

	//Set the output textfields uneditable
	jtfTrainingTime.setEditable(false);
	jtfTestingTime.setEditable(false);
	jtfTrainingAccuracy.setEditable(false);
	jtfTestingAccuracy.setEditable(false);

	//set the Label for each text fields
	JLabel jlbTrainingData_File = new JLabel("Training Data File");
	JMenu jmuTrainingData_File = new JMenu("Training Data File");
	JMenu jmuTestingData_File = new JMenu("Testing Data File");
	JLabel jlbTestingData_File = new JLabel("Testing Data File");
	JLabel jlbELM_Type = new JLabel("ELM Type");
	JLabel jlbNumberofHiddenNeurons = new JLabel("Number of Hidden Neurons");
	JLabel jlbActivationFunction = new JLabel("Activation Function");
	JLabel jlbTrainingTime = new JLabel("Training Time");
	JLabel jlbTestingTime = new JLabel("Testing Time");
	JLabel jlbTrainingAccuracy = new JLabel("Training Accuracy");
	JLabel jlbTestingAccuracy = new JLabel("Testing Accuracy");
	JLabel jlbScaleX = new JLabel("Scale X");
	JLabel jlbScaleY = new JLabel("Scale Y");
	JLabel jlbPercentage = new JLabel("Extracted Percentage");

	//adjust the property of list selection

	//Only allowed to select one each time
	jlist.setSelectionMode(0);
	jlistPercentage.setSelectionMode(0);

	//Set visible row without scrollpane
	jlist.setVisibleRowCount(1);
	jlistPercentage.setVisibleRowCount(1);


	//form the first panel to store both input fields
	JPanel p1 = new JPanel();
	p1.setBorder(new TitledBorder("Enter the inputs for testing"));
	p1.setLayout(new GridLayout(4,4,5,5));
	p1.add(jlbTrainingData_File);
	p1.add(jtfTrainingData_File);
	p1.add(jlbTestingData_File);
	p1.add(jtfTestingData_File);
	p1.add(jlbELM_Type);
	p1.add(jtfELM_Type);
	p1.add(jlbNumberofHiddenNeurons);
	p1.add(jtfNumberofHiddenNeurons);
	p1.add(jlbActivationFunction);
	p1.add(new JScrollPane(jlist));
	p1.add(jlbPercentage);
	p1.add(new JScrollPane(jlistPercentage));
	p1.add(jbtExecute);
	p1.add(jbtResetInputs);


	//form the second panel to show outputs
	JPanel p2 = new JPanel();
	p2.setBorder(new TitledBorder("Outputs"));
	p2.setLayout(new GridLayout(2,4,5,5));
	p2.add(jlbTrainingTime);
	p2.add(jtfTrainingTime);
	p2.add(jlbTestingTime);
	p2.add(jtfTestingTime);
	p2.add(jlbTrainingAccuracy);
	p2.add(jtfTrainingAccuracy);
	p2.add(jlbTestingAccuracy);
	p2.add(jtfTestingAccuracy);

	//form the panel to plot the graph
	p3.setBorder(new TitledBorder("Graph"));

	p3.SetImage("image/mouse.gif");

	//form the panel to show some status


	//form the control panel of the graph
	JPanel p4 = new JPanel();
	p4.setBorder(new TitledBorder("Control Panel of Graph"));
	p4.setLayout(new GridLayout(8,1,5,5));
	jlbScaleX.setPreferredSize(new Dimension(30,10));
	p4.add(jlbScaleX);
	p4.add(jtfScaleX);
	p4.add(jlbScaleY);
	p4.add(jtfScaleY);
	p4.add(jbtShowTraining);
	p4.add(jbtShowTesting);
	p4.add(jbtShowOutput);
	p4.add(jbtShowInOutput);



	//show the panels
	Container container = getContentPane();
	container.add(p1,BorderLayout.NORTH);
	container.add(p2,BorderLayout.SOUTH);
	container.add(p3,BorderLayout.CENTER);
	container.add(p4,BorderLayout.EAST);

	//register listener
	jbtExecute.addActionListener(this);
	jbtResetInputs.addActionListener(this);
	jbtShowTraining.addActionListener(this);
	jbtShowTesting.addActionListener(this);
	jbtShowOutput.addActionListener(this);
	jbtShowInOutput.addActionListener(this);
	jtfTrainingData_File.addMouseListener(this);
	jtfTestingData_File.addMouseListener(this);
	jlist.addListSelectionListener(this);
	jlistPercentage.addListSelectionListener(this);

	setSize(1000,600);

	Execute = false;

	timer = new javax.swing.Timer(1000,ELM);





	}

	/**
		Override ActionEvent
		<br>Detect which button clicked and perform certain functions
	*/

	public void actionPerformed(ActionEvent e) {


		if(e.getSource() == jbtExecute){

			Execute = true;

			p3.SetImage("image/loading.gif");
			//plot = true;
			repaint();

			timer.start();

		} //end jbtExecute ELM

		else if(e.getSource() == jtfTrainingData_File) {

		}

		else if(e.getSource() == jbtResetInputs) {

		Reset();
		//plot = false;

		}// end jbtResetInputs

		else if(e.getSource() == jbtShowTraining) {

			//get the scales from users
			double ScaleX, ScaleY;
			ScaleX = Double.parseDouble(jtfScaleX.getText());
			ScaleY = Double.parseDouble(jtfScaleY.getText());

			p3.SetScale(ScaleX, ScaleY);

			p3.SetDataFile(fTrainingData_File,1);
			//plot = true;
			repaint();

		}

		else if(e.getSource() == jbtShowTesting) {

			//get the scales from users
			double ScaleX, ScaleY;
			ScaleX = Double.parseDouble(jtfScaleX.getText());
			ScaleY = Double.parseDouble(jtfScaleY.getText());

			p3.SetScale(ScaleX, ScaleY);

			p3.SetDataFile(fTestingData_File,3);
			repaint();

		}

		else if(e.getSource() == jbtShowOutput) {

			//get the scales from users
			double ScaleX, ScaleY;
			ScaleX = Double.parseDouble(jtfScaleX.getText());
			ScaleY = Double.parseDouble(jtfScaleY.getText());

			p3.SetScale(ScaleX, ScaleY);

			p3.SetRegOutput(RegOutput);

			p3.SetDataFile(fTestingData_File,0);

			repaint();

		}

		else if(e.getSource() == jbtShowInOutput) {

			System.out.println("Are u here >");

			//get the scales from users
			double ScaleX, ScaleY;
			ScaleX = Double.parseDouble(jtfScaleX.getText());
			ScaleY = Double.parseDouble(jtfScaleY.getText());

			p3.SetScale(ScaleX, ScaleY);

			p3.SetRegOutput(RegOutput);

			p3.SetDataFile(fTestingData_File,2);

			repaint();

		}


	}

 ActionListener ELM = new ActionListener() {
      public void actionPerformed(ActionEvent evt) {

			if(Execute)
			{

			//get the inputs
			String Input[] = {jtfTrainingData_File.getText(),jtfTestingData_File.getText(),
							jtfELM_Type.getText(),jtfNumberofHiddenNeurons.getText(),
							ActivationFunction};

			//declare error fields
			String Error[] = {"Training Data","Testing Data","ELM Type",
								"Number of Hidden Neurons","Activation Function"};

			try{

			//check if the fields is blank
			for(int i =0; i<Input.length; i++)
			{
			if(Input[i].length() ==0 )
			throw new IllegalArgumentException(Error[i]);
			}

			//check if the fields is filled with correct argument


			TrainingData_File = Input[0];
			TestingData_File = Input[1];
			ELM_Type = Integer.parseInt(Input[2]);
			NumberofHiddenNeurons = Integer.parseInt(Input[3]);

			//Run the ELM
			ELM el = new ELM();

			//set percentage of file to be extracted
			el.setPercentage(PercentageFile);


				try {
				Output = el.elm(fTrainingData_File,fTestingData_File,ELM_Type,NumberofHiddenNeurons,
								ActivationFunction);
				System.out.println("training file: " + fTrainingData_File);
				System.out.println("testing file: " + fTestingData_File);
				System.out.println("ELM_Type: " + ELM_Type);
				System.out.println("NumberofHiddenNeurons: " + NumberofHiddenNeurons);
				System.out.println("ActivationFunction: " + ActivationFunction);
				RegOutput = el.getOutputTY();
				System.out.println(RegOutput);

				}

				catch (Exception ex) {
				System.out.println(ex);
				}

			//show output
			showOutput();

			//reset the value to false
			Execute = false;

			//stop the timer
			timer.stop();

			//setImage
			p3.SetImage("image/mouse.gif");
			p3.DrawImage();
			repaint();
			//prompt out the message
			JOptionPane.showMessageDialog (null, "ELM computation is DONE!",
													 "Message Box", JOptionPane.INFORMATION_MESSAGE);


			}
			//handle exception (when the entered values is illegal)
			catch (NumberFormatException ex) {
						JOptionPane.showMessageDialog (null,
							"Both ELM Type and Number of HiddenNeurons fields must be integer!",
								 "Error Message", JOptionPane.ERROR_MESSAGE);
			Stop();
			}

			//handle exception (when fields is blank)
			catch (IllegalArgumentException str) {

			Stop();


			if(!str.equals("Activation Function"))
			JOptionPane.showMessageDialog (null, str + " field cannot be blank!",
							 "Error Message", JOptionPane.ERROR_MESSAGE);

			else
			JOptionPane.showMessageDialog (null, "Activation Function is not selected",
										 "Error Message", JOptionPane.ERROR_MESSAGE);

			System.out.println(str);

			}
		} //end of Execute


	}
};

	//reset all inputs
	void Reset(){

		jtfTrainingData_File.setText("");
		jtfTestingData_File.setText("");
		jtfELM_Type.setText("");
		jtfNumberofHiddenNeurons.setText("");
		jtfTrainingTime.setText("");
		jtfTestingTime.setText("");
		jtfTrainingAccuracy.setText("");
		jtfTestingAccuracy.setText("");
	}

	//stop running the ELM
	void Stop(){

		Execute = false;
		p3.SetImage("image/stop.gif");
		repaint();
	}


	/**
		Override ListSelectionEvent
		<br>Override valueChanged method
	*/

	public void valueChanged(ListSelectionEvent e) {


		if(e.getSource() == jlist) {
		//Get selected indices
		int index = jlist.getSelectedIndex();
		ActivationFunction = ActivationList[index];
		//System.out.println("Selected index : " + index);
		} //end jlist

		else if(e.getSource() == jlistPercentage) {
		//Get selected indices
		int index = jlistPercentage.getSelectedIndex();
		PercentageFile = PercentageFileList[index];
		}

	}

	/**
		Override MouseEvent
	*/

	public void mouseExited(MouseEvent e) {
	}

	/**
		Override MouseEvent
	*/

	public void mouseEntered(MouseEvent e) {
	}

	/**
		Override MouseEvent
		<br>Detect if the mouse click on the specified text field
	*/

	public void mouseClicked(MouseEvent e) {


		if(e.getSource() == jtfTrainingData_File)
		{
			ReadFile("TrainingData_File");

			//Update user interface.
		    jtfTrainingData_File.setText(fTrainingData_File.getName());

		}

		else if(e.getSource() == jtfTestingData_File)
		{
			ReadFile("TestingData_File");

			//Update user interface.
			jtfTestingData_File.setText(fTestingData_File.getName());
		}

	}

	/**
		Override MouseEvent
	*/

	public void mouseReleased(MouseEvent e) {
	}

	/**
		Override MouseEvent
	*/

	public void mousePressed(MouseEvent e) {
	}

	/**
		Override MouseEvent
	*/

	public void mouseDragged(MouseEvent e) {
	}

	void ReadFile(String fileName) {

		JFileChooser fileChooser = new JFileChooser();
		// fileChooser.setFileSelectionMode( JFileChooser.FILES_AND_DIRECTORIES);

        int retval = fileChooser.showOpenDialog( this );

		if (retval == JFileChooser.APPROVE_OPTION) {

		    //determine if it is TrainingData_File or TestingData_File
		    if(fileName.equals("TrainingData_File"))
		    fTrainingData_File = fileChooser.getSelectedFile();

		    else
		    fTestingData_File = fileChooser.getSelectedFile();

		}

	}

	void showOutput() {

	//re-format the data
	NumberFormat formatter = new DecimalFormat("0.0000");

	jtfTrainingTime.setText("" + formatter.format(Output[0]));
	jtfTestingTime.setText("" + formatter.format(Output[1]));
	jtfTrainingAccuracy.setText("" + formatter.format(Output[2]));
	jtfTestingAccuracy.setText("" + formatter.format(Output[3]));

	}


	//plot the graph
	class GraphPanel  extends JPanel {

		private double ScaleX = 1;
		private double ScaleY = 100;
		private File DataFile;
		private int IO = -1;
		private double[][] RegOutput;
		private Image images;

		protected void paintComponent(Graphics g){

		System.out.println("11");
		super.paintComponent(g);

		try {

		//center at coordinate (250,250)
		g.drawString("O",260,263);

		//draw two axis
		g.drawLine(5,250,550,250);
		g.drawLine(250,50,250,450);

		//draw arrows
		g.drawLine(550,250,540,260);
		g.drawLine(550,250,540,240);
		g.drawLine(250,50,260,70);
		g.drawLine(250,50,240,70);


		//draw the graph

		//plot training graph
		if(IO == 1)
		{
			g.setColor(Color.blue);
			Draw(g);
			showStatus(g,0);
		}
		//plot regression output graph
		else if(IO ==0)
		{
			g.setColor(Color.red);
			Draw2(g);
			showStatus(g,0);
		}
		//plot both testing and regression output graph
		else if(IO==2 || IO==4)
		{
			g.setColor(Color.blue);
			Draw(g);
			g.setColor(Color.red);
			Draw2(g);

			//set IO = 4
			IO =4;

			showStatus(g,50);
		}
		//plot testing graph
		else if (IO==3)
		{
			g.setColor(Color.blue);
			Draw(g);
			showStatus(g,0);
		}



		}
		catch (Exception ex) {
			System.out.println(ex);

		}




		}//end paintComponent

		/**
			Modify the scale for the graphs to be plotted
		@param ScaleX Modify the scale of X axis
		@param ScaleY Modify the scale of Y axis
		*/

		public void SetScale(double ScaleX, double ScaleY) {

		this.ScaleX = ScaleX*1;
		this.ScaleY = ScaleY*100;
		System.out.println("Scale is set in " + ScaleX + " and " + ScaleY );
		}//end SetScale

		/**
			Plot the Input graph( either training or testing)
			on the Graph Panel
		@param G Used to plot on the Graph Panel
		*/

		public void Draw(Graphics G)throws IOException {

			if(DataFile != null) {

			//call FileLoad class
			FileLoad file = new FileLoad(DataFile);

			double functX[][] = file.load(PercentageFile);

			//plot training graph
			if(IO==1){
				for(int i=0; i<functX.length; i++)
				G.drawString(".",(int)(functX[i][1]*ScaleX)+250,
	 	 		250-(int)(ScaleY*functX[i][0]));
			}

			//plot testing graph
			else{
				Polygon p = new Polygon();
				//reset polygon points
				p.reset();
				for(int i=0; i<functX.length; i++)
				p.addPoint((int)(functX[i][1]*ScaleX)+250,
	 	 		250-(int)(ScaleY*functX[i][0]));
	 	 		G.drawPolyline(p.xpoints,p.ypoints,p.npoints);
			}


			}//end if




		}//end Draw

		/**
			Plot the Regression Output graph on the Graph Panel
		@param G Used to plot on the Graph Panel
		*/

		public void Draw2(Graphics G)throws IOException {

			if(DataFile != null) {

			//call FileLoad class
			FileLoad file = new FileLoad(DataFile);

			double functX[][] = file.load(PercentageFile);

			//for(int i=0; i<functX.length; i++)
			//G.drawString(".",(int)(functX[i][1]*ScaleX)+250,
			//250-(int)(ScaleY*RegOutput[0][i]));

			//join the points with PolyLine
			Polygon pOutput = new Polygon();
			//reset the polygon points
			pOutput.reset();

			for(int i=0; i<functX.length; i++)
			pOutput.addPoint((int)(functX[i][1]*ScaleX)+250,
			250-(int)(ScaleY*RegOutput[0][i]));

			G.drawPolyline(pOutput.xpoints,pOutput.ypoints,pOutput.npoints);

			}//end if



		}

		void showStatus(Graphics G, int setoff_Y) {

			//set Font style and size
			//new Font(String name, int style, int size);

			Font font = new Font("Garamond",Font.BOLD,20);
			G.setFont(font);

			if(IO==1){
			G.drawString("Training Graph",550,50);
			G.fillOval(700,37,30,15);
			}
			else if(IO==3||IO==2){
			G.drawString("Testing Graph",550,50);
			G.fillOval(700,37,30,15);
			}
			else if(IO==0){
			G.drawString("Output Graph",550,50+setoff_Y);
			G.fillOval(700,37+setoff_Y,30,15);
			}
			else if(IO==4) {
			G.setColor(Color.blue);
			G.drawString("Testing Graph",550,50);
			G.fillOval(700,37,30,15);
			G.setColor(Color.red);
			G.drawString("Output Graph",550,50+setoff_Y);
			G.fillOval(700,37+setoff_Y,30,15);
			}



		}



		/**
			Decide what file data to be extracted and plotted on the
			GraphPane
		@param DataFile File to be read
		@param IO determine what graph to be plotted
			   <br>0: show output
			   <br>1: show input (either testing or training data)
			   <br>2: show both input and output
		*/

		public void SetDataFile(File DataFile, int IO) {

			this.DataFile = DataFile;
			this.IO = IO;
		}//end SetDataFile

		/**
			Set the RegressionOutput
		@param RegOutput Regression Output obtained in 2D array form
		*/

		public void SetRegOutput(double[][] RegOutput) {

			this.RegOutput = RegOutput;
		}

		/**
			Draw Image
		*/
		public void DrawImage(Graphics G) {

			G.drawImage(images,5,350,this);
		}

		/**
			Create Image icon from a local file name
		@param imageFileName directory where image icon stored
		*/
		public void SetImage(String imageFileName){

		images = new ImageIcon(imageFileName).getImage();


		//if(images != null)

		//System.out.println("INSIDE CHSNG");

		}

		public void DrawImage() {
			this.IO = 5;
		}







	}//end GraphPanel


}