package com.upstridge.tmapp.sgr;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.upstridge.tmapp.R;
import com.upstridge.tmapp.adapters.CustomTrainAdapter;
import com.upstridge.tmapp.models.Trains;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.upstridge.tmapp.config.Constants.BASE_URL;

/**
 * Created by Wango-PC on 6/8/2017.
 */

public class SgrDetailsParser  extends AsyncTask<Void, Integer, Integer> {

    Context c;
    RecyclerView lv;
    String data;
    String date;
    String time;
    String destination;
    String origin;
    String type;
    String capacity;
    String vipprice;
    String ecprice;
    String arr;
    String dep;
    SearchView searchView;

    ArrayList<Trains> veh = new ArrayList<>();

    CustomTrainAdapter adapter;

    ProgressDialog pd;

    public SgrDetailsParser(Context c, String data, RecyclerView lv, String date, String time, String destination, String origin, SearchView searchView) {
        this.c = c;
        this.data = data;
        this.lv = lv;
        this.date = date;
        this.time = time;
        this.destination = destination;
        this.origin = origin;
        this.searchView = searchView;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        pd = new ProgressDialog(c);
        pd.setTitle("Parser");
        pd.setMessage("Parsing...Please wait");
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
            adapter = new CustomTrainAdapter(c,veh, destination, date, time, origin);
            /*final ArrayAdapter<String> adapter = new ArrayAdapter<String>(c,android.R.layout.simple_list_item_1,veh){

                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    View view =super.getView(position, convertView, parent);

                    TextView textView=(TextView) view.findViewById(android.R.id.text1);

                    textView.setTextColor(Color.WHITE);
                    textView.setTextSize(20);

                    return view;
                }
            };*/
            lv.setAdapter(adapter);

            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    int textlength = newText.length();
                    ArrayList<Trains> tempArrayList = new ArrayList<Trains>();
                    for(Trains c: veh){
                        if (textlength <= c.getName().length()) {
                            if (c.getName().toLowerCase().contains(newText.toString().toLowerCase())) {
                                tempArrayList.add(c);
                            }
                        }
                    }
                    adapter = new CustomTrainAdapter(c, tempArrayList, destination, date, time, origin);
                    lv.setAdapter(adapter);
                    //adapter.getFilter().filter(newText);
                    return false;
                }
            });

            /*lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String vehicle =((TextView)view.findViewById(R.id.hotelName)).getText().toString();
                    //arr =((TextView)view.findViewById(R.id.adults)).getText().toString().replace("Arrival : ","");
                    //dep =((TextView)view.findViewById(R.id.children)).getText().toString().replace("Departure : ","");
                    vipprice =((TextView)view.findViewById(R.id.availability)).getText().toString().replace("Vip Fare : KES ","");
                    ecprice =((TextView)view.findViewById(R.id.economicfare)).getText().toString().replace("Economic Fare : KES ","");
                    capacity =((TextView)view.findViewById(R.id.capacity)).getText().toString();
                    String organization =((TextView)view.findViewById(R.id.organization)).getText().toString();
                    String vid =((TextView)view.findViewById(R.id.vehicleid)).getText().toString();
                    String firstclassapply =((TextView)view.findViewById(R.id.firstclassapply)).getText().toString();

                    //Toast.makeText(c,arr+"-"+dep+"-"+vipprice+"-"+ecprice+"-"+capacity,Toast.LENGTH_LONG).show();

                    Intent i = new Intent(c, SgrSeats.class);
                    Bundle b = new Bundle();
                    b.putString("destination", destination);
                    b.putString("date", date);
                    b.putString("time", time);
                    b.putString("vehicle", vehicle);
                    b.putString("origin",origin);
                    b.putString("arrival", arr);
                    b.putString("departure", dep);
                    b.putString("vip", vipprice);
                    b.putString("economic", ecprice);
                    b.putString("capacity",capacity);
                    b.putString("organization", organization);
                    b.putString("vid", vid);
                    b.putString("firstclassapply", firstclassapply);
                    i.putExtras(b);
                    c.startActivity(i);
                }
            });*/

        }else{
            Toast.makeText(c,"No trains available",Toast.LENGTH_SHORT).show();
        }

        pd.dismiss();
    }


    private int parse() {
        try {

            JSONArray ja = new JSONArray(data);
            JSONObject jo = null;

            veh.clear();

            Trains train;

            for (int i = 0; i < ja.length(); i++) {
                jo = ja.getJSONObject(i);

                String name = jo.getString("name");
                String imageUrl = BASE_URL + "public/uploads/logo/"+jo.getString("logo");
                String route = jo.getString("oname") +" to "+ jo.getString("dname");
                String arrival = "Arrival : "+jo.getString("arrival");
                String departure = "Departure : "+jo.getString("departure");
                arr = jo.getString("arrival");
                dep = jo.getString("departure");
                type = jo.getString("type");
                capacity = jo.getString("capacity");
                String vipfare = "Vip Fare : KES "+jo.getString("firstclass");
                String economicfare = "Economic Fare : KES "+jo.getString("economic");
                vipprice = jo.getString("firstclass");
                ecprice = jo.getString("economic");
                capacity = jo.getString("capacity");
                String organization = jo.getString("organization_id");
                String vehicleid = jo.getString("vehicle_id");
                String firsclassapply = jo.getString("firstclass_apply");
                //String price = "VIP : KES "+jo.getString("firstclass") + " Economic : KES "+jo.getString("economic");

                train = new Trains();
                train.setName(name);
                train.setImageUrl(imageUrl);
                train.setRoute(route);
                //train.setArrival(arrival);
                //train.setDeparture(departure);
                train.setVipprice(vipfare);
                train.setEconomicfare(economicfare);
                train.setOrganization(organization);
                train.setVehicleid(vehicleid);
                train.setFirstclassapply(firsclassapply);
                train.setCapacity(capacity);
                veh.add(train);
            }
            return 1;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return 0;
    }

}

