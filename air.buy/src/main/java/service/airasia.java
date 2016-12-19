package service;

import java.io.IOException;
import java.io.PrintStream;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class airasia
{
  public static void main(String[] args)
    throws IOException
  {
    airasia a = new airasia();
    Document doc = a.getHtml();
    boolean x = false;
    while (!x) {
      if (doc.html().contains("welcome"))
      {
        x = false;
        System.out.println("try again!");
        airasia b = new airasia();
        doc = b.getHtml();
      }
      else
      {
        System.out.println(doc.html());
        x = true;
      }
    }
    Elements div = doc.select("div[class=js_availability_container]");
    for (int i = 0; i < div.size(); i++)
    {
      Elements div2 = ((Element)div.get(i)).select("div[class=avail-fare-price]");
      for (int i2 = 0; i2 < div2.size(); i2++) {
        System.out.println(((Element)div2.get(i2)).text());
      }
    }
  }
  
  Document getHtml()
    throws IOException
  {
    Document doc = Jsoup.connect("https://booking.airasia.com/Flight/Select")
      .ignoreContentType(true)
      .followRedirects(true)
      .referrer("view-source:https://booking.airasia.com/Flight/Select")
      .userAgent("ozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; WOW64; Trident/5.0)")
      .data("mt_uuid", "6aa75829-44ae-4100-885b-28671f4bc269")
      .data("no_iframe", "1")
      .data("mt_adid", "139964")
      .data("o1", "TPE")
      .data("d1", "AKL")
      .data("culture", "zh-TW")
      .data("dd1", "2017-10-03")
      .data("dd2", "2017-11-05")
      .data("r", "true")
      .data("ADT", "1")
      .data("CHD", "0")
      .data("inl", "0")
      .data("s", "true")
      .data("mon", "true")
      .data("cc", "TWD")
      .data("c", "false")
      .timeout(10000)
      .post();
    
    return doc;
  }
}
