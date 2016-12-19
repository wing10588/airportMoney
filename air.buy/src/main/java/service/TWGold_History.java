package service;

import java.io.IOException;
import java.io.PrintStream;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class TWGold_History
{
  public static void main(String[] args)
    throws IOException
  {
    Document doc = Jsoup.connect("http://rate.bot.com.tw/Pages/UIP005/UIP005INQ3.aspx?view=1&lang=zh-TW")
      .ignoreContentType(true)
      .followRedirects(true)
      .userAgent("Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:25.0) Gecko/20100101 Firefox/25.0")
      .referrer("view-source:http://rate.bot.com.tw/Pages/UIP005/UIP005INQ4.aspx?lang=zh-TW")
      .data("__VIEWSTATE", "/wEPDwUJNDg0NjU0OTI2ZBgCBR5fX0NvbnRyb2xzUmVxdWlyZVBvc3RCYWNrS2V5X18WCQUGUmFkaW81BQZSYWRpbzEFBlJhZGlvMgUGUmFkaW8zBQZSYWRpbzQFBWN0bDAyBQVjdGwwMwUFY3RsMDQFBWN0bDA1BQltdWx0aVRhYnMPD2QCAWSeGW0IAQRXUh8DW9ARNk3a72oo0w==")
      .data("term", "3")
      .data("year", "2014")
      .data("month", "4")
      .data("curcd", "TWD")
      .data("afterOrNot", "0")
      .data("Button1", "�d��")
      .data("__EVENTVALIDATION", "/wEWEAK1+MvVAwKOkoCCCwKMhc76CQLWssLjAgKf5L6bDQLL3LrjCgLW3JbjCgLS3JbjCgLR3JbjCgLM3JbjCgLl18nSCwLm1/nSCwKLts3CAQKUts3CAQKM54rGBgLWlM+bAvc+0B2WgWVZrmgRqen/5d9ubJqM")
      .timeout(10000)
      .post();
    
    Elements div = doc.select("tr[class=color1]");
    for (int i = 0; i < div.size(); i++)
    {
      Elements div2 = ((Element)div.get(i)).select("td[class=title]");
      for (int i2 = 0; i2 < div2.size(); i2++) {
        System.out.println(((Element)div2.get(i2)).text());
      }
    }
  }
}
