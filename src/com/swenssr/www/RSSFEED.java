//بسم الله الرحمن الرحیم

package com.swenssr.www;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;

public class RSSFEED extends Service
{
	private static final int timersn=10000;
	private Handler customHandler = new Handler();
	private Context This;
	private GA f=new GA(This);
	
	@Override
	public IBinder onBind(Intent arg0) 
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void onCreate() 
	{
		f.OpenSQLite("GA", "com.swenssr.www");
	}

	@Override
	public void onStart(Intent intent, int startId) 
	{
		customHandler.postDelayed(timers,timersn);
	}
	
	public Runnable timers=new Runnable() //news timer public
	{
		@Override
		public void run() 
		{
			if(chash_khoshe_news()>0)
			{
				f.Show_notification(R.drawable.ic_launcher, "نرم افزار خبر رسان", "خبرهای جدید سازمان خوشه بندی کسب و کار", "اخبار خوشه");
			}
            customHandler.postDelayed(this, timersn);
		}
	};
	
    public int chash_khoshe_news()
    {
    	int count=0;
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
    					
    					//f.RunSQLite("Insert Into News(Uid,dates,Titel,Des,Img,Type,Don,Urls) Values('"+RSSReader.Getid(i)+"','"+RSSReader.Getdate(i).trim()+"','"+RSSReader.Gettitle(i).trim()+"','"+RSSReader.Getdes(i).trim()+"','0','1',1,'"+D+"')");
    					
    					count++;
    				}
    				catch (Exception ex)
    				{
    				}
    			}
    		}
    		String id=RSSReader.Getid(RSSReader.Size-1);
    		f.RunSQLite("Delete From News Where Type=1 And Uid<"+id);
    	}
    	return count;
    }
	
	@Override
	public void onDestroy() 
	{
	}
	
}
