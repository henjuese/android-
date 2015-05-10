package cn.bao.action.calculator;

import java.util.regex.Pattern;

import cn.bao.action.decimal.Counts;
import android.os.Bundle;
import android.app.Activity;
import android.content.DialogInterface;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.TextView;

public class MainActivity extends Activity {
	
	private TextView print;
	private TextView history;
	
	private static String fistNumber="0";//��һ�������ֵ
	private static String secondNumber="0";
	private static String threeNumber="0";
	private static String num="0";//��ʾ���
	private static String historynum="";//��ʷ��¼
	private static int flag=0;//����ۼ�
	public Counts take=null;
	public static int token=0;//���������������������һ��Ϊ׼
	public static int status=0;//������������ȼ�
	
	public Counts take2=null;//�������ȼ�ʱ����ʱ�洢
	
	//�Ӽ��˳�
	private int[] btidTake={R.id.chu,R.id.cheng,R.id.jian,R.id.jia};
	private Button[] buttonTake=new Button[btidTake.length];
	//0��9����
	private int[] btidNum={R.id.zero,R.id.one,R.id.two,R.id.three,R.id.foure,R.id.five,R.id.six,R.id.serven,R.id.eight,R.id.nine,R.id.dian};
	private Button[] buttons=new Button[btidNum.length];
	
	//c��del��=
	private int[] btc1={R.id.cleargo,R.id.deng,R.id.del};
	private Button[] btc1s=new Button[btc1.length];
	
	private GridLayout gly;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
/*		gly=(GridLayout) findViewById(R.id.gdl);
		gly.setBackgroundResource(R.drawable.jz);*/
		//��ʷ��¼
		history=(TextView) findViewById(R.id.history);
		//��������
		print=(TextView)findViewById(R.id.result);
		print.setText(num);
		//print.setEnabled(false);
		//0��9��ť�¼�
		GetNumber get=new GetNumber();
		for(int i=0;i<btidNum.length;i++){
			buttons[i]=(Button) findViewById(btidNum[i]);
			buttons[i].setOnClickListener(get);
		}
		//�������ť�¼�
		Compute cm=new Compute();
		for(int i=0;i<btidTake.length;i++){
			buttonTake[i]=(Button) findViewById(btidTake[i]);
			buttonTake[i].setOnClickListener(cm);
		}
		
		Button eq=(Button) findViewById(R.id.deng);
		eq.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(flag==0){
					secondNumber=print.getText().toString();
					if(null!=take){
						
						getGoodNum(v);
						
						ysRestul();
						//���������
						if(status==1&&(v.getId()==R.id.jia||v.getId()==R.id.jian||v.getId()==R.id.deng)){
							status=0;
							ysRestul();
						}
						flag=1;
						//print.setText(num);
						take=null;
						status=0;
					}
				}
			}
		});
		
		//����
		Button clear=(Button) findViewById(R.id.cleargo);
		clear.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				num="0";
				threeNumber=fistNumber=secondNumber=num;
				print.setText(num);
				flag=0;
				historynum="";
				history.setText(historynum);
			}
		});
		
		//����
		Button del=(Button) findViewById(R.id.del);
		del.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(num.length()>1){
					num=num.substring(0,num.length()-1);
				}else{
					num="0";
				}
				if(historynum.length()>1){
					historynum=historynum.substring(0,historynum.length()-1);
				}else{
					historynum="";
				}
				history.setText(historynum);
				print.setText(num);
			}
		});
		
		
	}
	
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	//��text��ֵ
	class GetNumber implements OnClickListener{
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if (flag == 1) {
				num = "0";
				historynum="";//����=�ź�Ҫ����ʷ��¼�����
			}
			if ("0".equals(num)||token==1) {//����һ����������¸�ֵ���ı���
				print.setText("");
				num = v.getId() == R.id.dian ? "0" : "";
			}
			String txt = ((Button) v).getText().toString();
			boolean s = Pattern.matches("-*(\\d+).?(\\d)*", num + txt);
			num = s ? (num + txt) : num;
			print.setText(num);
			flag=0;
			
			historynum+=txt;
			history.setText(historynum);
			token=0;
		}
	}
	
	//������
	class Compute implements OnClickListener{
		String fh="";
		@Override
		public void onClick(View v) {
			if(take==null){//û�������֮ǰ
				fistNumber=print.getText().toString();
			}else{
				//�ڰ�=֮ǰ����take��Ϊ��
				if(token==0){
					getGoodNum(v);
					ysRestul();
					//���������
					if(status==1&&(v.getId()==R.id.jia||v.getId()==R.id.jian)){
						status=0;
						ysRestul();
					}
				}
			}
			switch (v.getId()) {
			case R.id.jia:
				take = Counts.ADD;
				fh = "+";
				break;
			case R.id.jian:
				take = Counts.MINUS;
				fh = "-";
				break;
			case R.id.cheng:
				take = Counts.MULTIPLY;
				fh = "*";
				break;
			case R.id.chu:
				take = Counts.DIVIDE;
				fh = "��";
				break;
			}
			if(token==1){
				historynum=historynum.substring(0, historynum.length()-1);
			}
			historynum+=fh;
			history.setText(historynum);
			num="0";
			flag=0;			
			token=1;//����һ���������
		}
	}
	
	public void getGoodNum(View v){
		//�������ȼ�
		if((take==Counts.ADD||take==Counts.MINUS)&&(v.getId()==R.id.cheng||v.getId()==R.id.chu)){
			status=1;
			//�������ȼ�֮ǰ�Ĳ�����
			if(take==Counts.ADD){
				take2=Counts.ADD;
			}else if(take==Counts.MINUS){
				take2=Counts.MINUS;
			}
		}
	}
	
	public void ysRestul(){
		if(status==1){//�������������ȼ�
			if("0".equals(threeNumber)){
				threeNumber=print.getText().toString();
			}else{
				threeNumber=take.values(threeNumber, print.getText().toString());
				
			}
		}else{
			if(!"0".equals(threeNumber)){
				secondNumber=threeNumber;
				take=take2;
			}else{
				secondNumber=print.getText().toString();
			}
			num=take.values(fistNumber, secondNumber);
			fistNumber=num;
			threeNumber="0";
			print.setText(num);
			secondNumber="0";
		}
	}
	
	//��չԤ�������翪�����ȵ�
	class OnTake implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		//���β˵����������Ǹ�������Ǹ�����
		if(keyCode==KeyEvent.KEYCODE_MENU){
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	
}























