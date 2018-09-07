package net.kisacasi.bes_mid_rss;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class MainActivity extends AppCompatActivity {
    private ListView listView;
    private EditText et;
    private ProgressDialog progressDialog;
    private List<Model> modelList;
    private String arama="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        listView=findViewById(R.id.listView);
        new HaberServisiAsynTask().execute("https://www.mynet.com/haber/rss/sondakika");

    }
    public void onClickBtn(View v)
    {
        new HaberServisiAsynTaskButon().execute("https://www.mynet.com/haber/rss/sondakika");
    }
    class HaberServisiAsynTask extends AsyncTask<String,String,List<Model>>{

        @Override
        protected List<Model> doInBackground(String... params) {

            modelList=new ArrayList<Model>();
            HttpURLConnection baglanti=null;
            try {
                URL url=new URL(params[0]);
                baglanti= (HttpURLConnection) url.openConnection();
                int baglantiDurumu=baglanti.getResponseCode();
                if (baglantiDurumu==HttpURLConnection.HTTP_OK){

                    BufferedInputStream bufferedInputStream=new BufferedInputStream(baglanti.getInputStream());
                    publishProgress("Haberler Okunuyor...");
                    DocumentBuilderFactory documentBuilderFactory=DocumentBuilderFactory.newInstance();
                    DocumentBuilder documentBuilder=documentBuilderFactory.newDocumentBuilder();
                    Document document=documentBuilder.parse(bufferedInputStream);
                    NodeList haberNodeList=document.getElementsByTagName("item");
                    for (int i=0;i<haberNodeList.getLength();i++)
                    {
                        Element element= (Element) haberNodeList.item(i);
                        NodeList nodeListTitle=element.getElementsByTagName("title");
                        NodeList nodeListLink=element.getElementsByTagName("link");
                        NodeList nodeListDate=element.getElementsByTagName("pubDate");
                        NodeList nodeListCreator=element.getElementsByTagName("maincategory");
                        NodeList nodeListDescription=element.getElementsByTagName("ipimage");

                        String title=nodeListTitle.item(0).getFirstChild().getNodeValue();
                        String link=nodeListLink.item(0).getFirstChild().getNodeValue();
                        String date=nodeListDate.item(0).getFirstChild().getNodeValue();
                        String creator=nodeListCreator.item(0).getFirstChild().getNodeValue();
                        String description=nodeListDescription.item(0).getFirstChild().getNodeValue();

                        Model model=new Model();
                        model.setTitle(title);
                        model.setLink(link);
                        model.setDate(date);
                        model.setCreator(creator);

                        URL urlResim = new URL(description);
                        HttpURLConnection connection = (HttpURLConnection) urlResim.openConnection();
                        connection.setDoInput(true);
                        connection.connect();
                        InputStream input = connection.getInputStream();
                        Bitmap myBitmap = BitmapFactory.decodeStream(input);
                        model.setResim(myBitmap);

//                        Pattern p = Pattern.compile("(.*?)", Pattern.CASE_INSENSITIVE);
//                        Matcher m=p.matcher(description);
//                        String photoUrl=null;


//                        while (m.find()) {
//                            photoUrl = m.group(1);
//                            Bitmap bitmap = null;
//
//                            try {
//                                URL  urlResim = new URL(photoUrl.toString().trim());
//                                InputStream is = urlResim.openConnection().getInputStream();
//                                bitmap = BitmapFactory.decodeStream(is);
//
//                            } catch (MalformedURLException e) {
//                                e.printStackTrace();
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }
//
//
//                            model.setResim(bitmap);
//
//                        }
                        modelList.add(model);
                        publishProgress("Liste Güncelleniyor...");

                    }

                }else {
                    Toast.makeText(MainActivity.this,"İnternet Yoktur",Toast.LENGTH_LONG).show();
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            } catch (SAXException e) {
                e.printStackTrace();
            } finally {
                if (baglanti!=null)
                    baglanti.disconnect();
            }
            return modelList;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog=ProgressDialog.show(MainActivity.this,"Lütfen Bekleyiniz...","İşlem Yürütülüyor",true);

        }

        @Override
        protected void onPostExecute(List<Model> models) {
            super.onPostExecute(models);
            CustomAdapter adapter=new CustomAdapter(MainActivity.this,models);
            listView.setAdapter(adapter);
            progressDialog.cancel();

        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            progressDialog.setMessage(values[0]);
        }
    }
    class HaberServisiAsynTaskButon extends AsyncTask<String,String,List<Model>>{

        @Override
        protected List<Model> doInBackground(String... params) {

            et=findViewById(R.id.editAra);
            arama=et.getText().toString();
            modelList=new ArrayList<Model>();
            HttpURLConnection baglanti=null;
            try {
                URL url=new URL(params[0]);
                baglanti= (HttpURLConnection) url.openConnection();
                int baglantiDurumu=baglanti.getResponseCode();
                if (baglantiDurumu==HttpURLConnection.HTTP_OK){

                    BufferedInputStream bufferedInputStream=new BufferedInputStream(baglanti.getInputStream());
                    publishProgress("Haberler Okunuyor...");
                    DocumentBuilderFactory documentBuilderFactory=DocumentBuilderFactory.newInstance();
                    DocumentBuilder documentBuilder=documentBuilderFactory.newDocumentBuilder();
                    Document document=documentBuilder.parse(bufferedInputStream);
                    NodeList haberNodeList=document.getElementsByTagName("item");
                    for (int i=0;i<haberNodeList.getLength();i++)
                    {
                        Element element= (Element) haberNodeList.item(i);
                        NodeList nodeListTitle=element.getElementsByTagName("title");
                        NodeList nodeListLink=element.getElementsByTagName("link");
                        NodeList nodeListDate=element.getElementsByTagName("pubDate");
                        NodeList nodeListCreator=element.getElementsByTagName("maincategory");
                        NodeList nodeListDescription=element.getElementsByTagName("ipimage");

                        String title=nodeListTitle.item(0).getFirstChild().getNodeValue();
                        String link=nodeListLink.item(0).getFirstChild().getNodeValue();
                        String date=nodeListDate.item(0).getFirstChild().getNodeValue();
                        String creator=nodeListCreator.item(0).getFirstChild().getNodeValue();
                        String description=nodeListDescription.item(0).getFirstChild().getNodeValue();




                        //Pattern p = Pattern.compile(".*\\\\b"+arama+"\\\\b.*");
                        Pattern p = Pattern.compile(".*\\b"+arama+"\\b.*",Pattern.CASE_INSENSITIVE);
                        Matcher m = p.matcher(title);
                       // System.out.println(m.matches());
//                        if (m.matches()==true){
//
//                            Model model=new Model();
//                            model.setTitle(title);
//                            model.setLink(link);
//                            model.setDate(date);
//                            model.setCreator(creator);
//
//                            URL urlResim = new URL(description);
//                            HttpURLConnection connection = (HttpURLConnection) urlResim.openConnection();
//                            connection.setDoInput(true);
//                            connection.connect();
//                            InputStream input = connection.getInputStream();
//                            Bitmap myBitmap = BitmapFactory.decodeStream(input);
//                            model.setResim(myBitmap);
//                        }
                        if (m.matches()==true){
                            Model model=new Model();
                            model.setTitle(title);
                            model.setLink(link);
                            model.setDate(date);
                            model.setCreator(creator);

                            URL urlResim = new URL(description);
                            HttpURLConnection connection = (HttpURLConnection) urlResim.openConnection();
                            connection.setDoInput(true);
                            connection.connect();
                            InputStream input = connection.getInputStream();
                            Bitmap myBitmap = BitmapFactory.decodeStream(input);
                            model.setResim(myBitmap);

                            modelList.add(model);
                            //System.out.println(m.matches());


                        }





                        publishProgress("Liste Güncelleniyor...");

                    }

                }else {
                    Toast.makeText(MainActivity.this,"İnternet Yoktur",Toast.LENGTH_LONG).show();
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            } catch (SAXException e) {
                e.printStackTrace();
            } finally {
                if (baglanti!=null)
                    baglanti.disconnect();
            }
            return modelList;

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog=ProgressDialog.show(MainActivity.this,"Lütfen Bekleyiniz...","İşlem Yürütülüyor",true);

        }

        @Override
        protected void onPostExecute(List<Model> models) {
            super.onPostExecute(models);
            CustomAdapter adapter=new CustomAdapter(MainActivity.this,models);
            listView.setAdapter(adapter);
            progressDialog.cancel();

        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            progressDialog.setMessage(values[0]);
        }
    }

}
