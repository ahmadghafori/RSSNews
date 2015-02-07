//بسم الله الرحمن الرحیم

package com.swenssr.www;

import java.io.File;
import java.io.FileOutputStream;

import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;

@TargetApi(Build.VERSION_CODES.ECLAIR)
public class MainActivity extends Activity 
{
	
    private static final int SPLASH_DURATION = 50;
    private Handler startMain;
    private boolean isBackButtonPressed;
    private GA f=new GA(this);
           
    public void chash_public_news()
    {
    	final Context t=this;
    	f.RunSQLite("Delete From News_Public");

    	try
    	{
    		RSSReader.main(t, "http://www.delgarm.com/rss/fees/coins");
			f.RunSQLite("Insert Into News_Public(ID,Title,Value) Values(1,'نرخ سکه تمام بهار آزادی','"+RSSReader.Gettitle(0).replace("\n", " ")+"')");
    	}
    	catch(Exception e)
    	{
    		f.RunSQLite("Insert Into News_Public(ID,Title,Value) Values(1,'نرخ سکه تمام بهار آزادی','"+" موجود نیست "+"')");
    	}
    	
    	try
    	{
    		RSSReader.main(t, "http://www.delgarm.com/rss/fees/dollar");
			f.RunSQLite("Insert Into News_Public(ID,Title,Value) Values(2,'قیمت دلار','"+RSSReader.Gettitle(0).replace("\n", " ")+"')");
    	}
    	catch(Exception e)
    	{
    		f.RunSQLite("Insert Into News_Public(ID,Title,Value) Values(2,'قیمت دلار','"+" موجود نیست "+"')");
    	}
    	
    	try
    	{
    		RSSReader.main(t, "http://www.delgarm.com/rss/fees/golden");
    		f.RunSQLite("Insert Into News_Public(ID,Title,Value) Values(3,'نرخ لحظه ای 1 گرم طلا 18 عیار','"+RSSReader.Gettitle(0).replace("\n", " ")+"')");
    	}
    	catch(Exception e)
    	{
    		f.RunSQLite("Insert Into News_Public(ID,Title,Value) Values(3,'نرخ لحظه ای 1 گرم طلا 18 عیار','"+" موجود نیست "+"')");
    	}
    	
    	try
    	{
    		RSSReader.main(t, "http://www.delgarm.com/rss/fees/euro");
			f.RunSQLite("Insert Into News_Public(ID,Title,Value) Values(4,'قیمت یورو','"+RSSReader.Gettitle(0).replace("\n", " ")+"')");
    	}
    	catch(Exception e)
    	{
    		f.RunSQLite("Insert Into News_Public(ID,Title,Value) Values(4,'قیمت یورو','"+" موجود نیست "+"')");
    	}
    }
    
    public void chash_khoshe_news()
    {
    	RSSReader.main(this,"http://bcdm.ir/rss/?cat=43");
    	if(RSSReader.Size>0)
    	{
    		for(int i=0;i<RSSReader.Size;i++)
    		{
    			if(f.SelectSQLite("Select Uid From News Where Type=1 And Uid="+RSSReader.Getid(i)).getCount()==0)
    			{
    				try
    				{
    					String D="1";
    					if(RSSReader.Getimage(i).equals("http://www.bcdm.ir/images/bcdm_logo.jpg")==false)
    						D=RSSReader.Getimage(i);
    					
    					f.RunSQLite("Insert Into News(Uid,dates,Titel,Des,Img,Type,Don,Urls) Values('"+RSSReader.Getid(i)+"','"+RSSReader.Getdate(i).trim()+"','"+RSSReader.Gettitle(i).trim()+"','"+RSSReader.Getdes(i).trim()+"','0','1',1,'"+D+"')");
    				}
    				catch (Exception ex)
    				{
    				}
    			}
    		}
    		String id=RSSReader.Getid(RSSReader.Size-1);
    		f.RunSQLite("Delete From News Where Type=1 And Uid<"+id);
    	}
    }
    
    public void chash_Sanat_news()
    {
    	RSSReader.main(this,"http://bcdm.ir/rss/?cat=41");
    	if(RSSReader.Size>0)
    	{
    		for(int i=0;i<RSSReader.Size;i++)
    		{
    			if(f.SelectSQLite("Select Uid From News Where Type=2 And Uid="+RSSReader.Getid(i)).getCount()==0)
    			{
    				try
    				{
        				String D="1";
        				if(RSSReader.Getimage(i).equals("http://www.bcdm.ir/images/bcdm_logo.jpg")==false)
        					D=RSSReader.Getimage(i);
        				
    					f.RunSQLite("Insert Into News(Uid,dates,Titel,Des,Img,Type,Don,Urls) Values('"+RSSReader.Getid(i)+"','"+RSSReader.Getdate(i).trim()+"','"+RSSReader.Gettitle(i).trim()+"','"+RSSReader.Getdes(i).trim()+"','0',2,1,'"+D+"')");
    				}
    				catch (Exception ex)
    				{
    				}
    			}
    		}
    		String id=RSSReader.Getid(RSSReader.Size-1);
    		f.RunSQLite("Delete From News Where Type=2 And Uid<"+id);
    	}
    }
    
    public void chash_Eghtesad_news()
    {
    	RSSReader.main(this,"http://bcdm.ir/rss/?cat=40");
    	if(RSSReader.Size>0)
		{
    		for(int i=0;i<RSSReader.Size;i++)
    		{
    			if(f.SelectSQLite("Select Uid From News Where Type=3 And Uid="+RSSReader.Getid(i)).getCount()==0)
    			{
    				try
    				{
        				String D="1";
        				if(RSSReader.Getimage(i).equals("http://www.bcdm.ir/images/bcdm_logo.jpg")==false)
        					D=RSSReader.Getimage(i);
        				
    					f.RunSQLite("Insert Into News(Uid,dates,Titel,Des,Img,Type,Don,Urls) Values('"+RSSReader.Getid(i)+"','"+RSSReader.Getdate(i)+"','"+RSSReader.Gettitle(i)+"','"+RSSReader.Getdes(i)+"','0',3,1,'"+D+"')");
    				}
    				catch (Exception ex)
    				{
    				}
    			}
    		}
    		String id=RSSReader.Getid(RSSReader.Size-1);
    		f.RunSQLite("Delete From News Where Type=3 And Uid<"+id);
		}
    }
    
    public void Start_chash()
    {	
    	f.OpenSQLite("GA", "com.swenssr.www");

    	//=======================================================================================================
    	
    	chash_public_news();
    	
    	chash_Eghtesad_news();
    	
    	chash_Sanat_news();
    	
    	chash_khoshe_news();
    	
    	finish(); 
    	Intent intent =new Intent(MainActivity.this, Main.class);
		MainActivity.this.startActivity(intent);
    }
    
    public String saveToSD(Bitmap outputImage,int i)
    {
        File storagePath = new File(Environment.getExternalStorageDirectory() + "/MyPhotos/"); 
        storagePath.mkdirs(); 
        
        String temp=RSSReader.Getid(i);

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
    
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        
        setContentView(R.layout.splash);
        startMain = new Handler();
        startMain.postDelayed(new Runnable() 
        {
            @Override
            public void run()  
            { 
               if (!isBackButtonPressed) 
               {
            	   Start_chash();
               }  
            }
        }, SPLASH_DURATION);
    }
 
    @Override
    public void onBackPressed()
    {
        isBackButtonPressed = true;
        super.onBackPressed();
    }   
}
