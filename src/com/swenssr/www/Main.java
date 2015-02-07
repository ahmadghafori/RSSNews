//بسم الله الرحمن الرحیم

package com.swenssr.www;

import java.io.File;
import java.io.FileOutputStream;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.text.Html;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class Main extends Activity
{	
	private GA f=new GA(this);
	private int Type_Temp=-1;
	private ListView LS;
	private Cursor CU;
	private Image I=new Image(this);
	private Drawable[] dr;
	private Cursor CH,CI;
	
	String sql;
	String find;
	int Counetr_Heder=0;
	int counnews=0;
	int timersn=0;
	int timerheder=0;
	int Imgtimer=0;
	int tic=0;
	
	private Handler customHandler = new Handler();
	private Handler Heder = new Handler();
	private Handler Img_Timer = new Handler();
	
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//Change Layout Look Vertical
		
		setContentView(R.layout.activity_main);
		
		f.OpenSQLite("GA", "com.swenssr.www");
		
		startService(new Intent(this, RSSFEED.class));
		
		//==========================Load Public News=============================
		Cursor cursor=f.SelectSQLite("Select * From News_Public");
		int i=0;
		while (cursor.moveToNext())
		{
			if(i<4)
			{
				RSSReader.news[i][0]=cursor.getString(1).toString();
				RSSReader.news[i][1]=cursor.getString(2).toString();
			}
			i++;
		}
		//=======================================================================
		
		CH=f.SelectSQLite("Select * From News Where Type=1 And Don=0 And Urls!='1'");
		String  Path = Environment.getExternalStorageDirectory() + "/MyPhotos/";
		dr=f.Loadimage_Formmemory(CH, 5, null, Path, ".jpg");
		
		CI=f.SelectSQLite("Select * From News Where Don=1 And Urls!='1'");
		
		customHandler.postDelayed(timers,0);
		Heder.postDelayed(timers_Heder, 0);
		
		Img_Timer.postDelayed(IML, 0);
		
		timersn=10000;
		timerheder=10000;
		tic=10000;
	}
	 
	public boolean onKeyDown(int keyCode, KeyEvent event) 
	{
	    if (keyCode == KeyEvent.KEYCODE_BACK) 
	    {
	    	int t=f.Pop();
	        if(t!=-1)
	        {
	        	if(t==R.layout.ls)
	        	{
	        		f.Pop();
	        		Loadlist(Type_Temp);
	        	}
	        	else if(t==R.layout.activity_main)
	        	{
	        		setContentView(t);
	        		customHandler.postDelayed(timers,0);
	        		Counetr_Heder=0;
	        		Heder.postDelayed(timers_Heder, 0);
	        		timerheder=10000;
	        		timersn=10000;
	        	}
	        	else if(t==R.layout.finder)
	        	{
	        		setContentView(t);
	        		EditText ed=(EditText)findViewById(R.id.findertxt);
	        		ed.setText(find);
	        		FinderList();
	        	}
	        	else
	        	{
	        		setContentView(t);
	        	}
	        }
	        else
	        {
	    		AlertDialog.Builder builder = new AlertDialog.Builder(this);
	    		builder.setTitle("خروج");
	    		builder.setMessage("آیا از برنامه خارج میشوید");
	    		builder.setPositiveButton("بله", new DialogInterface.OnClickListener()
	    		{
	    			public void onClick(DialogInterface dialog, int which) 
	    			{
	    				System.exit(0);
	    			}
	    		}).setNegativeButton("خیر", new DialogInterface.OnClickListener() 
	    		{
	    			@Override
	    			public void onClick(DialogInterface dialog, int id) 
	    			{
	    				dialog.dismiss();
	    			}
	    		});
	    		AlertDialog alert = builder.create();
	    		alert.show();
	        }
	        return true;
	    }

	    return super.onKeyDown(keyCode, event);
	}
	
	public Runnable timers=new Runnable() //news timer public
	{
		@Override
		public void run() 
		{
			if(counnews>3)
			{
				counnews=0;
			}
			TextView t=(TextView)findViewById(R.id.textviewnews);
			t.setText(RSSReader.news[counnews][0]+RSSReader.news[counnews][1]);
			counnews++;
            customHandler.postDelayed(this, timersn);
		}
	};
	
	public Runnable timers_Heder=new Runnable() //news timer Head
	{
		@SuppressLint("NewApi")
		@Override
		public void run() 
		{
			if(Counetr_Heder<dr.length)
			{
				CH.moveToPosition(Counetr_Heder);
				ImageView img=(ImageView)findViewById(R.id.ImageHed);
				TextView txv=(TextView)findViewById(R.id.Hedtitel);
				img.setBackground(dr[Counetr_Heder]);
				txv.setText(CH.getString(3));
			}
			else
			{
				Counetr_Heder=-1;
			}
			Counetr_Heder++;
            Heder.postDelayed(this, timerheder);
		}
	};
	
	public Runnable IML=new Runnable()//Don image timer
	{
		@SuppressLint("NewApi")
		@Override
		public void run() 
		{
			if(Imgtimer<CI.getCount())
			{
				CI.moveToPosition(Imgtimer);
				if(CI.getInt(7)==1)
				{
					I.Config_Image_Downloader(R.drawable.ic_launcher, R.drawable.ic_launcher, R.drawable.ic_launcher);
					String tt=saveToSD(I.Get_Bitmap_Url(CI.getString(8)), Imgtimer);
					if(tt=="0")
					{
						tt=CI.getString(1);
					}
					f.RunSQLite("Update News Set Img='"+tt+"',Don=0 Where ID="+CI.getInt(0));
				}
			}
			else
			{
				Imgtimer=-1;
				Img_Timer.removeCallbacks(IML);
			}
			Imgtimer++;
			Img_Timer.postDelayed(this, tic);
		}
	};
	
	@SuppressLint("NewApi")
	public void Loadlist(int Type)
	{
		try
		{
			CU=f.SelectSQLite("Select * From News Where Type="+Type+" order by Uid DESC");
			if(CU.moveToNext())
			{
				final Drawable Image_Error = getResources().getDrawable( R.drawable.ic_launcher);
				final String  Path = Environment.getExternalStorageDirectory() + "/MyPhotos/";
				
				Drawable[] dr=f.Loadimage_Formmemory(CU, 5, Image_Error,Path ,".jpg");

				int[] ID_Item={3,1};
				int layout=R.layout.feed_rss_list;
				int[] Objects={R.id.title,R.id.date};
				String[] From={"A","B"};
				
				customHandler.removeCallbacks(timers);
				Heder.removeCallbacks(timers_Heder);
				f.Push(R.layout.activity_main);
				setContentView(R.layout.ls);
				
				LS=(ListView)findViewById(R.id.listView1);
				
				f.Fill_Listview(LS, CU, ID_Item, layout, Objects, From,dr,R.id.arrow);
				
				LS.setOnItemClickListener(new OnItemClickListener() 
				{
					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,int arg2, long arg3) 
					{
						try
						{
							CU.moveToPosition(arg2);
							f.Push(R.layout.ls);
							setContentView(R.layout.dec);
							
							TextView t=(TextView)findViewById(R.id.textView1);
							ImageView i=(ImageView)findViewById(R.id.Imagede);
							i.setBackground(f.Loadimage_Formmemory(Image_Error, Path+CU.getString(5)+".jpg"));
							
							if(CU.getString(5).equals("0")==true || CU.getString(5).equals(null)==true)
							{
								i.setVisibility(View.GONE);
							}
							t.setText(Html.fromHtml((String) CU.getString(4)).toString());
						}
						catch (ExceptionInInitializerError e)
						{
							f.showerror(e.getMessage().toString());
						}
					}
				});
			}
			else
			{
				f.Pop();
				f.showerror("اخبار", "اطلاعات خبری برای این بخش وجود ندارد");
			}
		}
		catch (Exception ex)
		{
			f.showerror(ex.getMessage().toString());
		}
	}
	
    public void Shere_SMS(View view)
    {	
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        final int position = LS.getPositionForView((View)view.getParent()); 
        CU.moveToPosition(position);
        intent.putExtra(Intent.EXTRA_TEXT,Html.fromHtml((String) CU.getString(4)).toString());
        startActivity(Intent.createChooser(intent, "Share this text via"));
    }
	
	@SuppressLint("NewApi")
	public void Button1(View view)
	{ 
		CU=f.SelectSQLite("Select * From News Where Type=1");
		if(CU.getCount()>0)
		{
			Type_Temp=1;
			Loadlist(1);
		}
		else
		{
    		chash("http://bcdm.ir/rss/?cat=43", 1);
		}
	}

	public void Button2(View view)
	{
		CU=f.SelectSQLite("Select * From News Where Type=2");
		if(CU.getCount()>0)
		{
			Type_Temp=2;
			Loadlist(2);
		}
		else
		{
    		chash("http://bcdm.ir/rss/?cat=41", 2);
		}
	}
	
	public void Button3(View view)
	{
		CU=f.SelectSQLite("Select * From News Where Type=3");
		if(CU.getCount()>0)
		{
			Type_Temp=3;
			Loadlist(3);
		}
		else
		{
    		chash("http://bcdm.ir/rss/?cat=40", 3);
		}
	}

	public void Button4(View view)
	{
		customHandler.removeCallbacks(timers);
		Heder.removeCallbacks(timers_Heder);
		f.Push(R.layout.activity_main);
		setContentView(R.layout.finder);
		
		FinderList("Select * From News Where Type=1 order by Uid DESC");
	}
	
	public void FinderList(String sqlcommand)
	{
		try
		{
			sql=sqlcommand;
			CU=f.SelectSQLite(sqlcommand);
			if(CU.moveToNext())
			{
				final Drawable Image_Error = getResources().getDrawable( R.drawable.ic_launcher);
				final String  Path = Environment.getExternalStorageDirectory() + "/MyPhotos/";
				
				Drawable[] dr=f.Loadimage_Formmemory(CU, 5, Image_Error,Path ,".jpg");

				int[] ID_Item={3,1};
				int layout=R.layout.feed_rss_list;
				int[] Objects={R.id.title,R.id.date};
				String[] From={"A","B"};
				
				LS=(ListView)findViewById(R.id.listView1);
				
				f.Fill_Listview(LS, CU, ID_Item, layout, Objects, From,dr,R.id.arrow);
				
				LS.setOnItemClickListener(new OnItemClickListener() 
				{
					@SuppressLint("NewApi")
					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,int arg2, long arg3) 
					{
						try
						{
							CU.moveToPosition(arg2);
							f.Push(R.layout.finder);
							setContentView(R.layout.dec);
							
							TextView t=(TextView)findViewById(R.id.textView1);
							ImageView i=(ImageView)findViewById(R.id.Imagede);
							i.setBackground(f.Loadimage_Formmemory(Image_Error, Path+CU.getString(5)+".jpg"));
							
							if(CU.getString(5).equals("0")==true || CU.getString(5).equals(null)==true)
							{
								i.setVisibility(View.GONE);
							}
							t.setText(Html.fromHtml((String) CU.getString(4)).toString());
						}
						catch (ExceptionInInitializerError e)
						{
							f.showerror(e.getMessage().toString());
						}
					}
				});
			}
			else
			{
				f.Pop();
				f.showerror("اخبار", "اطلاعات خبری وجود ندارد");
			}
		}
		catch (Exception ex)
		{
			f.showerror(ex.getMessage().toString());
		}
	}
	
	public void FinderList()
	{
		FinderList(sql);
	}
	
	public void Finder(View view)
	{
		EditText edittext=(EditText)findViewById(R.id.findertxt);
		if(edittext.getText().length()>3)
		{
			find=edittext.getText().toString().trim();
			FinderList("Select * From News Where Des like '%"+edittext.getText().toString().trim()+"%' order by Uid DESC");
		}
		else
		{
			f.showerror("جستجو", "تعداد کارکتر ها نا معتبر است");
		}
	}
	
	@SuppressLint("NewApi")
	public void NewsHed(View view)
	{
		try
		{	
			customHandler.removeCallbacks(timers);
			Heder.removeCallbacks(timers_Heder);
			
			Counetr_Heder--;
			if(Counetr_Heder<0 || Counetr_Heder>dr.length)
				Counetr_Heder=0;
			
			final Drawable Image_Error = getResources().getDrawable( R.drawable.ic_launcher);
			final String  Path = Environment.getExternalStorageDirectory() + "/MyPhotos/";
			
			if(CH.moveToPosition(Counetr_Heder))
			{
				f.Push(R.layout.activity_main);
				setContentView(R.layout.dec);
			
				TextView t=(TextView)findViewById(R.id.textView1);
				ImageView i=(ImageView)findViewById(R.id.Imagede);
				i.setBackground(f.Loadimage_Formmemory(Image_Error, Path+CH.getString(5)+".jpg"));
			
				if(CH.getString(5).equals("0")==true || CH.getString(5).equals(null)==true)
				{
					i.setVisibility(View.GONE);
				}
				t.setText(Html.fromHtml((String) CH.getString(4)).toString());
			}
		}
		catch (ExceptionInInitializerError e)
		{
			f.showerror(e.getMessage().toString());
		}
	}
	
	private void chash(String Url,int Type)
	{
		ProgressDialog.show(this, "بار گزاری", "صبر داشته باشید");
		RSSReader.main(this,Url);
		if(RSSReader.Size>0)
		{
			I.Config_Image_Downloader(R.drawable.splash, R.drawable.splash, R.drawable.splash);
			for(int i=0;i<RSSReader.Size;i++)
			{
				if(f.SelectSQLite("Select Uid From News Where Type="+Type+" And Uid="+RSSReader.Getid(i)).getCount()==0)
				{
					try
					{
    					String tt="0";
    					//if(RSSReader.Getimage(i).equals("http://www.bcdm.ir/images/bcdm_logo.jpg")==false)
    						 //tt=saveToSD(I.Get_Bitmap_Url(RSSReader.Getimage(i)),i);
						f.RunSQLite("Insert Into News(Uid,dates,Titel,Des,Img,Type,Don) Values('"+RSSReader.Getid(i)+"','"+RSSReader.Getdate(i)+"','"+RSSReader.Gettitle(i)+"','"+RSSReader.Getdes(i)+"','"+tt+"',"+Type+",'"+RSSReader.Getimage(i)+"')");
					}
					catch (Exception ex)
					{
						//f.RunSQLite("Insert Into News(Uid,dates,Titel,Des,Img,Type) Values('"+RSSReader.Getid(i)+"','"+RSSReader.Getdate(i)+"','"+RSSReader.Gettitle(i)+"','"+RSSReader.Getdes(i)+"','0',"+Type+")");
					}
				}
			}
			String id=RSSReader.Getid(RSSReader.Size-1);
			f.RunSQLite("Delete From News Where Type="+Type+" And Uid<"+id);
		}
	}
	
    public String saveToSD(Bitmap outputImage,int i)
    {
        File storagePath = new File(Environment.getExternalStorageDirectory() + "/MyPhotos/"); 
        storagePath.mkdirs(); 
        
        String temp=CI.getString(1);
        
        File myImage = new File(storagePath, temp + ".jpg");

        try 
        { 
            FileOutputStream out = new FileOutputStream(myImage); 
            outputImage.compress(Bitmap.CompressFormat.JPEG, 80, out); 
            out.flush();    
            out.close();
        } 
        catch (Exception e) 
        { 
        	temp="0";
            e.printStackTrace(); 
        }               
        return temp;
    }
}
