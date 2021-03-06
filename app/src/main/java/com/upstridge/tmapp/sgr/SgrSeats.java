package com.upstridge.tmapp.sgr;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import com.upstridge.tmapp.adapters.CustomGridViewAdapter;
import com.upstridge.tmapp.models.Item;
import com.upstridge.tmapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import static com.upstridge.tmapp.config.Constants.BASE_URL;

/**
 * Created by Wango-PC on 6/8/2017.
 */

public class SgrSeats extends Activity implements AdapterView.OnItemClickListener
{
    GridView gridView;
    Button done;
    String type;
    ArrayList<Item> gridArray = new ArrayList<Item>();
    ArrayList<String> selected = new ArrayList<String>();
    CustomGridViewAdapter customGridAdapter;
    public Bitmap seatIcon,driverIcon,blankIcon,bookedIcon,vipIcon;
    public Bitmap seatSelect;

    String vehicle;
    String vehiclename;
    String destination;
    String date;
    String time;
    String origin;
    String arrival;
    String departure;
    String vip;
    String economic;
    String firstclassapply;
    String capacity;
    String organization;
    String traveltype;

    String seatname = "";
    List<String> vipArray = new ArrayList<String>();
    List<String> businessArray = new ArrayList<String>();
    List<String> economicArray = new ArrayList<String>();

    String url = BASE_URL + "android/seats.php";
    //String url = "http://admin.upstridge.co.ke/android/seats.php";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seats);

        driverIcon = BitmapFactory.decodeResource(this.getResources(), R.drawable.steering_icon);
        blankIcon = BitmapFactory.decodeResource(this.getResources(), R.drawable.ras1);

        seatIcon = BitmapFactory.decodeResource(this.getResources(), R.drawable.seat_layout_tab_nor_avl);
        bookedIcon = BitmapFactory.decodeResource(this.getResources(), R.drawable.seat_layout_tab_nor_bkd);
        vipIcon = BitmapFactory.decodeResource(this.getResources(), R.drawable.seat_layout_tab_nor_lad_avl);
        seatSelect = BitmapFactory.decodeResource(this.getResources(), R.drawable.seat_layout_screen_nor_std);

        Bundle bundle = getIntent().getExtras();
        vehicle = bundle.getString("vid");
        vehiclename = bundle.getString("vehicle");
        destination = bundle.getString("destination");
        date = bundle.getString("date");
        time = bundle.getString("time");
        origin = bundle.getString("origin");
        arrival = bundle.getString("arrival");
        departure = bundle.getString("departure");
        vip = bundle.getString("vip");
        economic = bundle.getString("economic");
        firstclassapply = bundle.getString("firstclassapply");
        capacity = bundle.getString("capacity");
        organization = bundle.getString("organization");
        traveltype = "SGR";

        //Toast.makeText(this,organization,Toast.LENGTH_SHORT).show();

        final CheckBookedSeats v = new CheckBookedSeats(this, url, date, time, destination, origin, traveltype);
        v.execute();

    }

    /*public void totalSeat(int n, List<String> seats)
    {
        gridArray.add(new Item(blankIcon,"" ));
        gridArray.add(new Item(blankIcon,"" ));
        gridArray.add(new Item(blankIcon,"" ));
        gridArray.add(new Item(driverIcon,"driver" ));

        //Toast.makeText(SgrSeats.this,"Vip Seats = "+String.valueOf(vipArray.size()),Toast.LENGTH_SHORT).show();
        //Toast.makeText(SgrSeats.this,"Business Seats = "+String.valueOf(businessArray.size()),Toast.LENGTH_SHORT).show();
        //Toast.makeText(SgrSeats.this,"Economy Seats = "+String.valueOf(economicArray.size()),Toast.LENGTH_SHORT).show();

        for (int i = 1; i <= n; ++i)
        {
            gridArray.add(new Item(seatIcon, "seat " + i));
            for(String seat : seats) {
                if (seat.contains("seat " + i)) {
                    gridArray.remove(i + 3);
                    gridArray.add(new Item(bookedIcon, "seat " + i));
                }
            }
            for(String vipseat : vipArray) {
                if (vipseat.contains("seat " + i)) {
                    gridArray.remove(i + 3);
                    gridArray.add(new Item(vipIcon, "seat " + i));
                }
            }
        }

    }*/

    public String seatName(String title)
    {
        return title;
    }

    public void seatSelected(int pos)
    {
        Item item = gridArray.get(pos);
        //seatname = seatName(item.getTitle());
        gridArray.remove(pos);
        gridArray.add(pos, new Item(seatSelect, "select"));

        customGridAdapter.notifyDataSetChanged();
    }

    public void seatDeselcted(int pos, Bitmap seatcompare, String title)
    {

        gridArray.remove(pos);
        int i = pos + 1;
        Item item = gridArray.get(i);
        //Toast.makeText(SgrSeats.this,item.getName(),Toast.LENGTH_SHORT).show();
        //Toast.makeText(SgrSeats.this,String.valueOf(pos),Toast.LENGTH_SHORT).show();
        //if(seatcompare == vipIcon){
            gridArray.add(pos, new Item(item.getIcon(), "seat "+(i-4)));
           // Toast.makeText(SgrSeats.this,"vip",Toast.LENGTH_SHORT).show();
        /*}
        if(seatcompare == seatIcon){
            gridArray.add(pos, new Item(seatIcon, "seat "+(i-4)));
            Toast.makeText(SgrSeats.this,"normal",Toast.LENGTH_SHORT).show();
        }*/
        customGridAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {

        Item item = gridArray.get(position);
        //item.setName(item.getTitle());
        String title = item.getName();

        //Toast.makeText(SgrSeats.this,title,Toast.LENGTH_SHORT).show();
        Bitmap seatcompare = item.getImage();
        if (seatcompare == seatIcon)
        {
            item.setIcon(seatIcon);
            //item.setName(seatIcon);
            seatSelected(position);
            selected.add(item.getTitle());
        }

        else if (seatcompare == vipIcon)
        {
            item.setIcon(vipIcon);
            seatSelected(position);
            selected.add(item.getTitle());
        }
        else if (seatcompare == blankIcon)
        {

        }
        else if (seatcompare == bookedIcon)
        {

        }
        else
        {
            seatDeselcted(position, item.getIcon(), item.getName());

        }


    }



    public class CheckBookedSeats extends AsyncTask<String, Integer, String> {

        Context c;
        String address;
        String date;
        String time;
        String destination;
        String origin;
        String type;
        ProgressDialog pd;

        public CheckBookedSeats(Context c, String address, String date, String time, String destination, String origin,String type){
            this.c = c;
            this.address = address;
            this.date = date;
            this.time = time;
            this.destination = destination;
            this.origin = origin;
            this.type=type;
        }



        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pd = new ProgressDialog(c);
            pd.setTitle("Fetch Data");
            pd.setMessage("Loading Seats...Please wait");
            pd.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String data = downloadData();
            return data;
        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            pd.dismiss();

            //Toast.makeText(c, s, Toast.LENGTH_LONG).show();
            if(s != null){
                SeatsParser p = new SeatsParser(c, s);
                p.execute();
            }else{
                Toast.makeText(c, "No seats available", Toast.LENGTH_SHORT).show();
            }
        }

        private String downloadData(){
            InputStream Is = null;
            String line = null;
            String prov = "";

            try{

                URL url = new URL(address);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("POST");
                con.setDoOutput(true);
                con.setDoInput(true);

                OutputStream outputStream = con.getOutputStream();

                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("date", "UTF-8")+"="+URLEncoder.encode(date,"UTF-8")+"&"+URLEncoder.encode("time", "UTF-8")+"="+URLEncoder.encode(time,"UTF-8")+"&"+URLEncoder.encode("destination","UTF-8")+"="+URLEncoder.encode(destination,"UTF-8")+"&"+URLEncoder.encode("origin", "UTF-8")+"="+URLEncoder.encode(origin,"UTF-8")+"&"+URLEncoder.encode("organization_id", "UTF-8")+"="+URLEncoder.encode(organization,"UTF-8")+"&"+URLEncoder.encode("type", "UTF-8")+"="+URLEncoder.encode(traveltype,"UTF-8");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = con.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
                //String result = "";

            /*while((line = bufferedReader.readLine()) != null){
                result += line;
            }*/


                //BufferedReader br = new BufferedReader(new InputStreamReader(Is));

                StringBuffer sb = new StringBuffer();

                if(bufferedWriter != null){
                    while((line = bufferedReader.readLine()) != null){
                        sb.append(line+"\n");
                    }
                }else{
                    return null;
                }

                bufferedReader.close();
                inputStream.close();
                con.disconnect();



                return sb.toString();

            }catch(MalformedURLException e){
                e.printStackTrace();
            }catch (IOException e){
                e.printStackTrace();
            }finally {
                if(Is != null){
                    try {
                        Is.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return null;
        }
    }



    public class SeatsParser  extends AsyncTask<Void, Integer, Integer> {

        Context c;
        String data;
        ProgressDialog pd;

        public SeatsParser(Context c, String data) {
            this.c = c;
            this.data = data;

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pd = new ProgressDialog(c);
            pd.setTitle("Parser");
            pd.setMessage("Completing...Please wait");
            pd.show();
        }

        @Override
        protected Integer doInBackground(Void... params) {

            return this.parse();
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);

            //Toast.makeText(c,integer,Toast.LENGTH_SHORT).show();

            if(integer == 1){

                /*StringBuilder builder = new StringBuilder();
                for (String seat : seats) {
                    builder.append(seat + ",");
                }
                Toast.makeText(c, String.valueOf(seats.size()), Toast.LENGTH_LONG).show();*/



                done = (Button)findViewById(R.id.doneButton);
                gridView = (GridView) findViewById(R.id.gridView1);
                gridView.setNumColumns(4);
                customGridAdapter = new CustomGridViewAdapter(c, R.layout.seatrow_grid, gridArray);
                gridView.setAdapter(customGridAdapter);
                gridView.setOnItemClickListener((AdapterView.OnItemClickListener) c);

                done.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if(selected.size() == 0){
                            Toast.makeText(SgrSeats.this,"Please select atleast one seat!",Toast.LENGTH_SHORT).show();
                        }else {

                            Intent i = new Intent(getApplicationContext(), SgrBooking.class);
                            Bundle b = new Bundle();
                            b.putString("destination", destination);
                            b.putString("date", date);
                            b.putString("time", time);
                            b.putString("vehicle", vehiclename);
                            b.putString("origin", origin);
                            b.putString("arrival", arrival);
                            b.putString("departure", departure);
                            b.putString("vip", vip);
                            b.putString("economic", economic);
                            b.putString("capacity", capacity);
                            b.putString("organization", organization);
                            b.putString("vid", vehicle);
                            b.putString("firstclassapply", firstclassapply);
                            b.putStringArrayList("seats", selected);
                            i.putExtras(b);
;                            startActivity(i);
                        }
                    }
                });
            }else{
                Toast.makeText(c,"No seats available",Toast.LENGTH_SHORT).show();
            }

            pd.dismiss();
        }


        private int parse() {
            try {

                gridArray.add(new Item(blankIcon,"" ));
                gridArray.add(new Item(blankIcon,"" ));
                gridArray.add(new Item(blankIcon,"" ));
                gridArray.add(new Item(driverIcon,"driver" ));

                JSONArray ja = new JSONArray(data);
                //JSONObject jo = new JSONObject(data);
                JSONObject seat = null;
               // JSONObject booked = null;

                //for (int i = 0; i < ja.length(); i++) {
                //JSONArray seatArray = new JSONArray(jo.getString("seat"));
                //JSONArray bookArray = new JSONArray(jo.getString("booked"));
                    //seatArray = seatArray.replace("[","").replace("]","");
                    //String sa = ja.getString("seat").replace("[","").replace("]","");
                   /* for (int k = 0; k < seatArray.length(); k++) {
                        seat = seatArray.getJSONObject(k);

                        if(seat.getInt("vip") == 1){
                            vipArray.add(seat.getString("seatno"));
                        }
                        if(seat.getInt("business") == 1){
                            businessArray.add(seat.getString("seatno"));
                        }
                        if(seat.getInt("economy") == 1){
                            economicArray.add(seat.getString("seatno"));
                        }*/

                       // int total = seat.getInt("total");

                        //for (int j = 1; j <= total; j++) {

                            //seats.add(seat.getString("seatno" + (j)));
                        //}

                    //}
                    //JSONArray bookArray = new JSONArray("booked");
                    for (int l = 0; l < ja.length(); l++) {
                        seat = ja.getJSONObject(l);

                        //totalSeat(Integer.parseInt(capacity), seats);
                        String seatno = seat.getString("seatno");
                        //seats.add(seatno);

                        if(seat.getInt("vip") == 1){
                            vipArray.add(seat.getString("seatno"));
                        }
                        if(seat.getInt("business") == 1){
                            businessArray.add(seat.getString("seatno"));
                        }
                        if(seat.getInt("economy") == 1){
                            economicArray.add(seat.getString("seatno"));
                        }



                        //Toast.makeText(SgrSeats.this,"Vip Seats = "+String.valueOf(vipArray.size()),Toast.LENGTH_SHORT).show();
                        //Toast.makeText(SgrSeats.this,"Business Seats = "+String.valueOf(businessArray.size()),Toast.LENGTH_SHORT).show();
                        //Toast.makeText(SgrSeats.this,"Economy Seats = "+String.valueOf(economicArray.size()),Toast.LENGTH_SHORT).show();

                        //for (int i = 1; i <= ja.length(); ++i)
                        //{
                       // gridArray.add(new Item(seatIcon, seatno));
                        if(seat.getString("status").equals("booked")){
                            gridArray.add(new Item(bookedIcon, seatno));
                        }else {
                            if (seat.getInt("vip") == 1) {
                               gridArray.add(new Item(vipIcon, seatno));
                            } else if (seat.getInt("business") == 1) {
                                gridArray.add(new Item(seatIcon, seatno));
                            } else if (seat.getInt("economy") == 1) {
                                gridArray.add(new Item(seatIcon, seatno));
                            }
                        }
                        //}

                        //int total = booked.getInt("total");

                        //for (int j = 1; j <= total; j++) {

                          //  seats.add(booked.getString("seatno" + (j)));
                       // }

                    }
              //  }
                return 1;
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return 0;
        }

    }



}