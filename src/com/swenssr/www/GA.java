//بسم الله الرحمن الرحیم

package com.swenssr.www;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;

public class GA
{
	private int[] Stack_int=new int[100000];
	private	int index_int;
	private SQLiteDatabase db;
	private Connection connect;
	private Context This;
	private View prompview;
	private AlertDialog alertdialog;
	private List<Map<String,String>> data=null;
	
	@SuppressLint("SimpleDateFormat")
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
	
	public SimpleAdapter AD;
	
	//-------------------------------Stack-------------------------------
	
	GA(Context This)
	{
		index_int=0;
		this.This=This;
	}
	
	public void Push(int id)
	{
		Stack_int[index_int]=id;
		index_int++;
	}
	
	public int Pop()
	{
		if(index_int>0)
		{
			index_int--;
			return Stack_int[index_int];
		}
		else
		{
			return -1;
		}
	}

	public void New_Stack()
	{
		index_int=0;
	}
	
	//-------------------------------SQLITE------------------------------
	
	public int OpenSQLite(String Database_Name,String Project_Name)
	{
		try
		{
			db = SQLiteDatabase.openDatabase("data/data/"+Project_Name+"/databases/"+Database_Name+".DB",null, SQLiteDatabase.OPEN_READWRITE);
			return 1;
		}
		catch (Exception e)
		{
			db=This.openOrCreateDatabase(Database_Name+".DB", 0, null);
			createSQLite();
			return 0;
		}
	}
	
	public Cursor SelectSQLite(String Command)
	{
		try
		{
			return  db.rawQuery(Command, null);
		}
		catch (Exception e)
		{
			showerror(e.getMessage().toString());
			return null;
		}
		
	}
	
	public void RunSQLite(String Command)
	{
		db.execSQL(Command);
	}

	private void createSQLite()
	{
		RunSQLite("CREATE TABLE News_Public (ID INTEGER, Title VARCHAR,Value VARCHAR)");
		RunSQLite("CREATE TABLE News (ID  INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL, Uid VARCHAR,dates VARCHAR,Titel VARCHAR,Des VARCHAR,Img VARCHAR,Type INTEGER,Don INTEGER,Urls VARCHAR)");
	}
	
	//-------------------------------SQLSERVER------------------------------
	
	@SuppressLint("NewApi")
	public void OpenSQLServer(String _user,String _pass,String _DB,String _server,String _instance,Context t)
	{
		StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
		Connection con=null;
		String ConnURL;
		try 
		{
			Class.forName("net.sourceforge.jtds.jdbc.Driver");
			//ConnURL="jdbc:jtds:sqlserver://192.168.1.6:1433;databaseName="+_DB+";user="+_user+";password="+_pass+";";
			//jdbc:jtds:sqlserver://MYPC:1433/Blog;instance=SQLEXPRESS;user=sa;password=s3cr3t
			ConnURL="jdbc:jtds:sqlserver://"+_server+"/"+_DB+";instance="+_instance+";user="+_user+";password="+_pass;
			con=DriverManager.getConnection(ConnURL);
		} 
		catch (SQLException e)
		{
			showerror("Open Sqlserver",e.getMessage());
			Log.e("ERROR", e.getMessage());
		}
		catch (ClassNotFoundException e)
		{
			showerror("Open Sqlserver",e.getMessage());
			Log.e("ERROR", e.getMessage());
		}
		catch (Exception e)
		{
			showerror("Open Sqlserver",e.getMessage());
			Log.e("ERROR", e.getMessage());
		}
		connect=con;
	}
	
	@SuppressLint("NewApi")
	public void OpenSQLServer(String _user,String _pass,String _DB,String _server,Context t)
	{
		StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
		Connection con=null;
		String ConnURL;
		try 
		{
			Class.forName("net.sourceforge.jtds.jdbc.Driver");
			ConnURL="jdbc:jtds:sqlserver://"+_server+":1433;databaseName="+_DB+";user="+_user+";password="+_pass+";";
			con=DriverManager.getConnection(ConnURL);
		} 
		catch (SQLException e)
		{
			showerror("Open Sqlserver",e.getMessage());
			Log.e("ERROR", e.getMessage());
		}
		catch (ClassNotFoundException e)
		{
			showerror("Open Sqlserver",e.getMessage());
			Log.e("ERROR", e.getMessage());
		}
		catch (Exception e)
		{
			showerror("Open Sqlserver",e.getMessage());
			Log.e("ERROR", e.getMessage());
		}
		connect=con;
	}
	
	public void RunSQLServer(String Command,Context t)
	{
		try 
		{
			Statement statement=connect.createStatement();
			statement.execute(Command);
		} 
		catch (SQLException e) 
		{
			showerror(e.getMessage().toString());
		}
	}
	
	public void RunSQLServer(String Command)
	{
		RunSQLServer(Command,This);
	}
	
	public ResultSet SelectSQLServer(String COMANDOSQL,Context t)
	{
		ResultSet rs = null;
		try 
		{
			Statement statement=connect.createStatement();
			rs=statement.executeQuery(COMANDOSQL);
		}
		catch (Exception e)
		{
			showerror(e.getMessage());
		}
		return rs;
	}
	
	public ResultSet SelectSQLServer(String COMANDOSQL)
	{
		return SelectSQLServer(COMANDOSQL,This);
	}
	
	public int Rows_Count(String COMANDOSQL)
	{
		return Rows_Count(SelectSQLServer(COMANDOSQL));
	}
	
	public int Rows_Count(ResultSet RS)
	{
		int Count=0;
		try 
		{
			while(RS.next())
			{
				Count++;
			}
			RS.first();
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return Count;
	}
	
	//------------------------------Show------------------------------------
	
	public void showerror(String Titel,String Text,Context t)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(t);
		builder.setTitle(Titel);
		builder.setMessage(Text);
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface dialog, int which) 
			{
				dialog.dismiss();
			}
		});
		AlertDialog alert = builder.create();
		alert.show();
	}
	
	public void showerror(String  Titel,String Text)
	{
		showerror(Titel,Text,This);
	}
	
	public void showerror(String Text)
	{
		showerror("Error",Text,This);
	}
	
	public View Create(int layout)
	{
		LayoutInflater li=LayoutInflater.from(This);
		prompview=li.inflate(layout, null);
		AlertDialog.Builder alertdialogbuilder=new AlertDialog.Builder(This);
		alertdialogbuilder.setView(prompview);
		alertdialog=alertdialogbuilder.create();
		alertdialog.show();
		return prompview;
	}
	
	public void Closepage()
	{
		alertdialog.dismiss();
	}
	
	public View Loadpage(int layout)
	{
		LayoutInflater li=LayoutInflater.from(This);
		prompview=li.inflate(layout, null);
		AlertDialog.Builder alertdialogbuilder=new AlertDialog.Builder(This);
		alertdialogbuilder.setView(prompview);
		alertdialog=alertdialogbuilder.create();
		return prompview;
	}
	
	public void Showpage()
	{
		alertdialog.show();
	}
	
	public void Hidepage()
	{
		alertdialog.hide();
	}
	
	//-------------------------------List----------------------------------
	
	public SimpleAdapter Fill_Listview(ListView LS,ResultSet RS,int[] ID_Item,int layout,int[] Objects,String[] From)
	{
		try 
		{
			data = new ArrayList<Map<String,String>>();
			while(RS.next())
			{
				Map<String,String> datanum = new HashMap<String,String>();
				for(int i=0;i<ID_Item.length;i++)
				{
					datanum.put(From[i],RS.getString(ID_Item[i]));
				}
				data.add(datanum);
			}
			AD =new SimpleAdapter(This,data,layout,From,Objects);
			LS.setAdapter(AD);
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return AD;
	}
	
	public SimpleAdapter Fill_Listview(ListView LS,final Cursor CU,int[] ID_Item,int layout,int[] Objects,String[] From,final Drawable[] drawable,final int object_image)
	{
		try 
		{
		    ArrayList<Map<String,Object>> data1 = new ArrayList<Map<String,Object>>();
			for(int j=0;j<CU.getCount();j++)
			{
				CU.moveToPosition(j);
				Map<String,Object> datanum = new HashMap<String,Object>();
				for(int i=0;i<ID_Item.length;i++)
				{
					datanum.put(From[i],CU.getString(ID_Item[i]).toString());
				}
				data1.add(datanum);
			}
			AD =new SimpleAdapter(This,data1,layout,From,Objects)
			{
				@SuppressLint("NewApi")
				@Override
				public View getView(int position, View convertView,ViewGroup parent) 
				{
					Animation animFade = AnimationUtils.loadAnimation(This, R.anim.list);
					View v=super.getView(position, convertView, parent);
					v.setAnimation(animFade);
					try
					{
						ImageView im=(ImageView)v.findViewById(object_image);
						im.setBackground(drawable[position]);
					}
					catch (Exception e){}
					return super.getView(position, convertView, parent);
				}
			};
			LS.setAdapter(AD);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return AD;
	}
	
	public SimpleAdapter Fill_Listview(ListView LS,final Cursor CU,int[] ID_Item,int layout,int[] Objects,String[] From)
	{
		try 
		{
		    ArrayList<Map<String,Object>> data1 = new ArrayList<Map<String,Object>>();
		    for(int j=0;j<CU.getCount();j++)
			{
		    	CU.moveToPosition(j);
				Map<String,Object> datanum = new HashMap<String,Object>();
				for(int i=0;i<ID_Item.length;i++)
				{
					datanum.put(From[i],CU.getString(ID_Item[i]).toString());
				}
				data1.add(datanum);
			}
			AD =new SimpleAdapter(This,data1,layout,From,Objects);
			LS.setAdapter(AD);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return AD;
	}
	
	public void Fill_Listview(ListView LS,String[][] ST,int Number_Rows,int Number_Columns,int layout,int[] Objects,String[] From)
	{
		try 
		{
			data = new ArrayList<Map<String,String>>();
			for(int S=0;S<Number_Rows;S++)
			{
				if(ST[S][Number_Columns].toString().equals("1")==true)
				{
					Map<String,String> datanum = new HashMap<String,String>();
					for(int i=0;i<Number_Columns;i++)
					{
						datanum.put(From[i],ST[S][i]);
					}
					data.add(datanum);
				}
			}
			AD =new SimpleAdapter(This,data,layout,From,Objects);
			LS.setAdapter(AD);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			showerror(e.getMessage().toString());
		}
	}
		
	public void Remove_Item_Listview(int Index)
	{
		data.remove(Index);
		AD.notifyDataSetChanged(); 
	}
	
	//-----------------------------Spinner---------------------------------
	
	public SimpleAdapter Fill_Spinner(Spinner SP,ResultSet RS,int[] ID_Item,int layout,int[] Objects,String[] From)
	{
		try 
		{
			List<Map<String,String>> data=null;
			data = new ArrayList<Map<String,String>>();
			while(RS.next())
			{
				Map<String,String> datanum = new HashMap<String,String>();
				for(int i=0;i<ID_Item.length;i++)
				{
					datanum.put(From[i],RS.getString(ID_Item[i]));
				}
				data.add(datanum);
			}
			AD =new SimpleAdapter(This,data,layout,From,Objects);
			SP.setAdapter(AD);
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return AD;
	}
	
	//------------------------------PersianDate----------------------------
	
	public String todayShamsi() 
	{
		String curentDateandTime = sdf.format(new Date());
		String year = curentDateandTime.substring(0, 4);
		String month = curentDateandTime.substring(4, 6);
		String day = curentDateandTime.substring(6, 8);
		int Y = Integer.valueOf(year);
		int M = Integer.valueOf(month);
		int D = Integer.valueOf(day);
		return Shamsi(Y, M, D,0);
	}
	
	@SuppressWarnings("deprecation")
	public String Get_Times()
	{
		Calendar Cal = Calendar.getInstance(); 
		String Time="";
		int H=Cal.getTime().getHours();
		int M=Cal.getTime().getMinutes();
		
		if(H<10){Time="0"+Integer.toString(H);}else{Time=Integer.toString(H);}
		if(M<10){Time+=":0"+Integer.toString(M);}else{Time+=":"+Integer.toString(M);}	
		
		return Time;
	}
	
	public String IO_Date(String Date_Input,String Time_Input,String Date_Output,String Time_Output)
	{
		String Time="";
		
		Time_Input=Standard_Time(Time_Input);
		Time_Output=Standard_Time(Time_Output);
		
		try
		{
			int D=D_D_Hours(Date_Input,Date_Output)*60;
			int T=T_T_Minut(Time_Input, Time_Output);
		
			int Sum=D+T;
		
			if((Sum/60)<10)
			{
				Time="0"+Integer.toString((Sum/60));
			}
			else
			{
				Time=Integer.toString((Sum/60));
			}
		
			if((Sum%60)<10)
			{
				Time+=":0"+Integer.toString((Sum%60));
			}
			else
			{
				Time+=":"+Integer.toString((Sum%60));
			}
		}
		catch (Exception e)
		{
			showerror(e.getMessage().toString());
		}
		
		return Time;
	}
	
	public int T_T_Minut(String Time1,String Time2)
	{
		int[][] time=new int[2][2];
		int H,M; H=M=0;
		
		time[0][0]=Integer.parseInt(Time1.substring(0, 2));
		time[0][1]=Integer.parseInt(Time2.substring(0, 2));
		
		time[1][0]=Integer.parseInt(Time1.substring(3, 5));
		time[1][1]=Integer.parseInt(Time2.substring(3, 5));
		
		H=(time[0][1]-time[0][0])*60;
		
		M=time[1][1]-time[1][0];
		
		return H+M;
	}
	
	public int D_D_Hours(String Date1,String Date2)
	{
		int[][] date=new int[3][2];
		int Y,M,D;	Y=M=D=0;
		
		date[0][0]=Integer.parseInt(Date1.substring(0, 4));
		date[0][1]=Integer.parseInt(Date2.substring(0, 4));
		
		date[1][0]=Integer.parseInt(Date1.substring(5, 7));
		date[1][1]=Integer.parseInt(Date2.substring(5, 7));
		
		date[2][0]=Integer.parseInt(Date1.substring(8, 10));
		date[2][1]=Integer.parseInt(Date2.substring(8, 10));
		
		
		Y=(date[0][1]-date[0][0])*8640;
		M=(date[1][1]-date[1][0])*720;
		D=(date[2][1]-date[2][0])*24;
		
		return Y+M+D;
	}

	public int Minut(String Hours)
	{
		int H,M; H=M=0;	
		
		H=Integer.parseInt(Hours.substring(0, 1));
		M=Integer.parseInt(Hours.substring(3, 4));
		
		H=(H)*60;
		
		return H+M;
	}
	
	public String Hours(String Minu)
	{
		int T=Integer.parseInt(Minu);
		int H=T/60;
		int M=T%60;
		String Time="";
		if(H<10)
		{
			Time+="0"+Integer.toString(H);
		}
		else
		{
			Time=Integer.toString(H);
		}
		
		if(M<10)
		{
			Time+="0"+Integer.toString(M);
		}
		else
		{
			Time=Integer.toString(M);
		}
		
		return Time;
	}

	public String Standard_Time(String Time)
	{
		String H="";
		String M="";
		int i=0;
		while(Time.substring(i,i+1).equals(":")==false)
		{
			H+=Time.substring(i,i+1).toString();
			i++;
		}
		if(H.length()<2){H="0"+H;}
		
		i++;
		
		while(i<Time.length())
		{
			M+=Time.substring(i,i+1).toString();
			i++;
		}
		if(M.length()<2){M="0"+M;}
		
		return H+":"+M;
	}
	
	@SuppressLint("SimpleDateFormat")
	private String Shamsi(int Y, int M, int D,int status)
	{
		if (Y == 0)
			Y = 2000;
		if (Y < 100)
			Y = Y + 1900;
		if (Y == 2000)
		{
			if (M > 2)
			{
				SimpleDateFormat temp = new SimpleDateFormat("yyyyMMdd");
				String curentDateandTime = temp.format(new Date());
				String year = curentDateandTime.substring(0, 4);
				String month = curentDateandTime.substring(4, 6);
				String day = curentDateandTime.substring(6, 8);
				Y = Integer.valueOf(year);
				M = Integer.valueOf(month);
				D = Integer.valueOf(day);
			}
		}
		if (M < 3 || (M == 3 && D < 21))
			Y = Y - 622;
		else Y = Y - 621;
		{
			switch (M)
			{
				case 1: 
					if (D < 21)
					{
						M = 10;
						D = D + 10;
					}
					else
					{
						M = 11;
						D = D - 20;
					}
				break;
				case 2: 
					if (D < 20)
					{
						M = 11;
						D = D + 11;
					}
					else
					{
						M = 12;
						D = D - 19;
					}
					break;
					case 3:
						if (D < 21)
						{
							M = 12;
							D = D + 9;
						}
						else
						{
							M = 1;
							D = D - 20;
						}
					break;
					case 4:
						if (D < 21)
						{
							M = 1;
							D = D + 11;
						}
						else
						{
							M = 2; D = D - 20;
						}
					break;
					case 5:
						if (D < 22)
						{
							M = M - 3;
							D = D + 10;
						}
						else
						{
							M = M - 2;
							D = D - 21;
						}
					break;
					case 6:
						if (D < 22)
						{
							M = M - 3;
							D = D + 10;
						}
						else
						{
							M = M - 2;
							D = D - 21;
						}
						break;
					case 7:
						if (D < 23)
						{
							M = M - 3;
							D = D + 9;
						}
						else
						{
							M = M - 2;
							D = D - 22;
						}
					break;
					case 8:
						if (D < 23)
						{
							M = M - 3;
							D = D + 9;
						}
						else
						{
							M = M - 2;
							D = D - 22;
						}
					break;
					case 9:
						if (D < 23)
						{
							M = M - 3;
							D = D + 9;
						}
						else
						{
							M = M - 2;
							D = D - 22;
						}
					break;
					case 10:
						if (D < 23)
						{
							M = 7;
							D = D + 8;
						}
						else
						{
							M = 8;
							D = D - 22;
						}
					break;
					case 11:
						if (D < 22)
						{
							M = M - 3;
							D = D + 9;
						}
						else
						{
							M = M - 2;
							D = D - 21;
						}
					break;
					case 12:
						if (D < 22)
						{
							M = M - 3;
							D = D + 9;
						}
						else
						{
							M = M - 2;
							D = D - 21;
						}
					break;
				}
			}
			String month = "00";
			String day = "00";
			D = Integer.valueOf(D)+1;
			if (M < 10)
			{
				month = "0" + M;
			}
			else
			{
				month = String.valueOf(M);
			}
			if (D < 10)
			{
				day = "0" + D;
			}
			else
			{
				day = String.valueOf(D);
			}
			if(status==0)
				return String.valueOf(Y) + "/" + month + "/" + day;
			else if(status==1)
				return String.valueOf(Y) ;
			else if(status==2)
				return month;
			else
				return day;
		}
	
	//-------------------------------------Memory---------------------------------
	
	public Drawable[] Loadimage_Formmemory(Cursor CU,int IDColumn,Drawable Image_Error,String Path,String Type_Image)
	{
		Drawable[] drawable=new Drawable[CU.getCount()];
		for(int i=0;i<CU.getCount();i++)
		{
			CU.moveToPosition(i);
			String tt=CU.getString(IDColumn);
			if(tt.equals('0')==false && tt.equals(null)==false)
			{
				File imgFile = new  File(Path+tt+Type_Image);
				if(imgFile.exists())
				{
					Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
					@SuppressWarnings("deprecation")
					Drawable drawable_temp = new BitmapDrawable(myBitmap);
					drawable[i]=drawable_temp;
				}
				else
				{
					drawable[i]=Image_Error;
				}
			}
		}
		return drawable;
	}
	
	@SuppressWarnings("deprecation")
	public Drawable Loadimage_Formmemory(Drawable Image_Error,String Path)
	{
		Drawable drawable=null;
		File imgFile = new  File(Path);
		if(imgFile.exists())
		{
			Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
			drawable = new BitmapDrawable(myBitmap);
		}
		else
		{
			drawable=Image_Error;
		}
		return drawable;
	}

	//-------------------------------Notification----------------------------------
	
	@SuppressWarnings("deprecation")
	public void Show_notification(int Icon,String Titel,String Body,String Ticker)
	{
		int NOTIFICATION_ID=1;
        CharSequence tickerText = Ticker;
        long when = System.currentTimeMillis();
        CharSequence contentTitle = Titel;
        CharSequence contentText = Body;
        Intent notificationIntent = new Intent(This, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(This, 0, notificationIntent, 0);
        
        final Notification notification = new Notification(Icon, tickerText, when);
        notification.setLatestEventInfo(This, contentTitle, contentText, contentIntent);

        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        
        String ns = Context.NOTIFICATION_SERVICE;
        final NotificationManager mNotificationManager = (NotificationManager) This.getSystemService(ns);
               
        mNotificationManager.notify(NOTIFICATION_ID, notification);
	}
	
}
