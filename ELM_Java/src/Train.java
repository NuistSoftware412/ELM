import com.sun.net.ssl.SSLContext;


public class Train {
	public static void main(String args[]){
		if(args.length!=4){
			System.out.println("Four parameters needed!");
			System.exit(0);
		}
		String train_file = args[0];
		int n= Integer.parseInt(args[1]);
		String af = args[2];
		int ELM_Type=Integer.parseInt(args[3]);
		long startTime = System.nanoTime();
		if(ELM_Type==0){
			ELM_Regression elm = new ELM_Regression();
			elm.ELM_Train(train_file, n, af);
		}else{
			ELM_Classification elm = new ELM_Classification();
			elm.ELM_Train(train_file, ELM_Type, n, af);
		}
		long consumingTime = System.nanoTime() - startTime;
		System.out.println("Consumed Time=" + (double)consumingTime/1000000000);
	}
}
