//بسم الله الرحمن الرحیم

package com.swenssr.www;

import java.io.File;
import java.io.InputStream;
import java.net.URL;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.decode.BaseImageDecoder;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.utils.StorageUtils;

public class Image 
{
	private Context This;
	//##################################Config_Image_Downloader##################################
	private final String directoryName = "News_GA";
	private File cacheDir;
	private ImageLoader imageLoader;
	private DisplayImageOptions options;
	private ImageLoaderConfiguration config;
	//###########################################################################################
	
	public Image(Context This)
	{
		this.This=This;
	}
	
	public void Config_Image_Downloader(int showImageOnLoading,int showImageForEmptyUri,int showImageOnFail)
	{
		cacheDir = StorageUtils.getOwnCacheDirectory(This, directoryName + "/Cache");
		imageLoader = ImageLoader.getInstance();
		//------------------------------------------------------------------------------------
		config = new ImageLoaderConfiguration.Builder(This)
		        .memoryCacheExtraOptions(480, 800) // default = device screen dimensions
		        .diskCacheExtraOptions(480, 800, null)
		        .threadPoolSize(3) // default
		        .threadPriority(Thread.NORM_PRIORITY - 2) // default
		        .tasksProcessingOrder(QueueProcessingType.FIFO) // default
		        .denyCacheImageMultipleSizesInMemory()
		        .memoryCache(new LruMemoryCache(2 * 1024 * 1024))
		        .memoryCacheSize(2 * 1024 * 1024)
		        .memoryCacheSizePercentage(13) // default
		        .diskCache(new UnlimitedDiscCache(cacheDir)) // chash memory
		        .diskCacheSize(50 * 1024 * 1024)
		        .diskCacheFileCount(100)
		        .diskCacheFileNameGenerator(new HashCodeFileNameGenerator()) // default
		        .imageDownloader(new BaseImageDownloader(This)) // default
		        .imageDecoder(new BaseImageDecoder(true)) // default
		        .defaultDisplayImageOptions(DisplayImageOptions.createSimple()) // default
		        .writeDebugLogs()
		        .build();
		//------------------------------------------------------------------------------------
		imageLoader.init(config);
		//------------------------------------------------------------------------------------
		 options = new DisplayImageOptions.Builder()
		 		.showImageOnLoading(showImageOnLoading) // resource or drawable
		 		.showImageForEmptyUri(showImageForEmptyUri) // resource or drawable
		 		.showImageOnFail(showImageOnFail) // resource or drawable
		 		.resetViewBeforeLoading(false)  // default
		 		.delayBeforeLoading(1000)
		 		.cacheInMemory(false) // default
		 		.cacheOnDisk(false) // default
		 		.considerExifParams(false) // default
		 		.imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2) // default
		 		.bitmapConfig(Bitmap.Config.ARGB_8888) // default
		 		.displayer(new SimpleBitmapDisplayer()) // default
		 		.handler(new Handler()) // default
		 		.build();
		//------------------------------------------------------------------------------------
	}

	public Bitmap Get_Bitmap_Url(String imageURI,int x,int y)
	{
		ImageSize targetSize = new ImageSize(x, y); // result Bitmap will be fit to this size
		Bitmap bmp = imageLoader.loadImageSync(imageURI, targetSize, options);
		return bmp;
	}
	
	public Bitmap Get_Bitmap_Url(String imageURI)
	{ 
		Bitmap bmp = imageLoader.loadImageSync(imageURI, options);
		return bmp;
	}
	
	@SuppressWarnings("deprecation")
	public Drawable Get_Drawable_Url(String imageURI)
	{
		Drawable drawable = new BitmapDrawable(Get_Bitmap_Url(imageURI));
		return drawable;
	}

	public Drawable LoadImageFromWebOperations(String url,String name) 
	{
	    try 
	    {
	        InputStream is = (InputStream) new URL(url).getContent();
	        Drawable d = Drawable.createFromStream(is, name);
	        return d;
	    } 
	    catch (Exception e) 
	    {
	        return null;
	    }
	}
	
	@SuppressWarnings("deprecation")
	public Drawable LoadImageFromDB(byte[] imgByte)
	{
		try
		{
			//byte[] imgByte=blob.getBytes(1, (int) blob.length());
		 	Bitmap b=BitmapFactory.decodeByteArray(imgByte, 0, imgByte.length);
		 	Drawable drawable = new BitmapDrawable(b);
		 	return drawable;
		}
		catch (Exception e) 
		{
			return null;
		}
	}
	
}
