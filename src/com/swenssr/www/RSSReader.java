//بسم الله الرحمن الرحیم

package com.swenssr.www;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class RSSReader 
{  
	
   @SuppressWarnings("unused")
   private static Context This;
   private static RSSReader instance = null;
   public  static List<Rss> myList;
   public  static int Size=0;
   private static URL rssURL;
   public  static String[][] news=new String[4][2];
  
   private RSSReader() {}  
  
   public static RSSReader getInstance() 
   {  
      if (instance == null)  
         instance = new RSSReader();  
      return instance;  
   }  
  
   public void setURL(URL url) 
   {  
      rssURL = url;  
   }  
  
   public static void writeFeed() 
   {  
      try 
      {  
         DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();  
         Document doc = builder.parse(rssURL.openStream());  
         NodeList items = doc.getElementsByTagName("item");  
         
         myList = new ArrayList<Rss>();
         Size=items.getLength();
         for (int i = 0; i < items.getLength(); i++) 
         {  
            Element item = (Element)items.item(i);  
            Rss m=new Rss();
            try{m.setTitel(getValue(item, "title").toString());}catch(Exception e){m.setTitel("");}
            try{m.setDescription(getValue(item, "description").toString());}catch(Exception e){m.setDescription("");}
            try{m.setImage(getAtrib(item, "media:content"));}catch(Exception e){m.setImage("");}
            try{m.setLink(getValue(item, "link").toString());}catch(Exception e){m.setLink("");}
            try{m.setDates(getValue(item, "pubDate").toString());}catch(Exception e){m.setDates("");}
            try{m.setID(getValue(item, "id").toString());}catch(Exception e){m.setID("");}
            myList.add(m);
         }  
      }
      catch (Exception e)
      { 
     	 //showerror("Error", e.getMessage().toString(), This);
         e.printStackTrace();  
      }  
   }  
  
   public static void showerror(String Titel,String Text,Context t)
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
   
   public static String getValue(Element parent, String nodeName) 
   {  
      return parent.getElementsByTagName(nodeName).item(0).getFirstChild().getNodeValue();  
   }  
   
   public static String getAtrib(Element parent, String nodeName) 
   {
	   NodeList mediacontentList = parent.getElementsByTagName(nodeName);
	   Element mediacontentElement = (Element) mediacontentList.item(0);
	   return mediacontentElement.getAttribute("url");  
   }  
  
   @SuppressWarnings("static-access")
   public static void main(Context t,String url) 
   {  
      try 
      {  
    	 This=t;
         RSSReader reader = RSSReader.getInstance();  
         reader.setURL(new URL(url));  
         reader.writeFeed();  
      }
      catch (Exception e) 
      { 
    	 //showerror("Error", e.getMessage().toString(), This);
         e.printStackTrace();  
      }  
   }  

   public static String Gettitle(int id)
   {
	   return myList.get(id).getTitel();
   }
   
   public static String Getdate(int id)
   {
	   return myList.get(id).getDates();
   }
   
   public static String Getdes(int id)
   {
	   return myList.get(id).getDescription();
   }
   
   public static String Getimage(int id)
   {
	   return myList.get(id).getImage();
   }
   
   public static String Getid(int id)
   {
	   return myList.get(id).getID();
   }
   
}