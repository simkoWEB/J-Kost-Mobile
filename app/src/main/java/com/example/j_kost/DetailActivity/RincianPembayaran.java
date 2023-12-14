package com.example.j_kost.DetailActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.j_kost.R;
import com.example.j_kost.Utils.MyPopUp;
import com.example.j_kost.Utils.NetworkUtils;
import com.saadahmedsoft.popupdialog.listener.OnDialogButtonClickListener;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class RincianPembayaran extends AppCompatActivity {

    ImageView btnBack;

//    getDataKost
    TextView rekeningTujuan;

//    getUser
    TextView namaUser;

//    getTransaksi
    TextView methodePembayaran, bulanPembayaran, transactionId;

//    get intent explicit
    TextView waktuTransaksi, total;

//    other
    TextView bayar, sisa;
    SharedPreferences sharedPreferences;
    private RequestQueue requestQueue;
    ImageView imageViewBukti;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rincian_pembayaran);

        requestQueue = Volley.newRequestQueue(RincianPembayaran.this);

        btnBack = findViewById(R.id.btnBack);

        transactionId = findViewById(R.id.idTransaksi);
        waktuTransaksi = findViewById(R.id.waktuTransaksi);
        rekeningTujuan = findViewById(R.id.rekeningTujuan);
        namaUser = findViewById(R.id.namaUser);
        methodePembayaran = findViewById(R.id.metodePembayaran);
        bulanPembayaran = findViewById(R.id.bulanPembayaran);
        imageViewBukti = findViewById(R.id.ivBukti);
        total = findViewById(R.id.total);
        bayar = findViewById(R.id.bayar);
        sisa = findViewById(R.id.sisa);

        sharedPreferences = RincianPembayaran.this.getApplicationContext().getSharedPreferences("userData", Context.MODE_PRIVATE);
        String idUser = sharedPreferences.getString("idUser", "");
        String kamarId = sharedPreferences.getString("nomorKamar", "");
        String idTransaksi = getIntent().getStringExtra("idTransaksi");

        //namaUser
        getDataUser(idUser);
        //rekening pemilik
        getRekening(kamarId);

//        Log.d("Get id transaksi", idTransaksi);
//        Log.d("Get id user", idUser);
        //metode, tglbayar, bulanPembayaran, hargatotal, bayar
        getDataTransaksi(idUser,idTransaksi);


        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void getDataTransaksi(String idUser, String idTransaksi){
        String url = "http://"+NetworkUtils.BASE_URL+"/PHP-MVC/public/GetDataMobile/getHistoryDetail/"+idUser+"/"+idTransaksi;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject data = response.getJSONObject("data");
                            String id = data.getString("id_transaksi");
                            String metodeBayar = data.getString("metode_pembayaran");
                            String tglBayar = data.getString("tggl_transaksi");
                            String buktiPembayaran = data.getString("foto_bukti_bayar");
                            int hargaTotal = data.getInt("harga");
                            int bayarTransaksi = data.getInt("bayar");

                            transactionId.setText(id);
                            methodePembayaran.setText(metodeBayar);
                            waktuTransaksi.setText(tglBayar);

                            String formattedHargaTotal = formatDec(hargaTotal);
                            String formattedBayarTransaksi = formatDec(bayarTransaksi);

                            total.setText("Rp. "+formattedHargaTotal);
                            bayar.setText("Rp. "+formattedBayarTransaksi);

                                loadImageToImageView(buktiPembayaran, imageViewBukti);


                            // Mengonversi format tanggal ke objek Calendar
                            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            Date date = dateFormat.parse(tglBayar);
                            Calendar calendar = Calendar.getInstance();
                            calendar.setTime(date);

                            // Mendapatkan nama bulan sesuai dengan tanggal
                            int month = calendar.get(Calendar.MONTH);
                            String namaBulan = new DateFormatSymbols().getMonths()[month];

                            // Gunakan nama bulan yang telah didapatkan
                            bulanPembayaran.setText(namaBulan);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (ParseException e) {
                            throw new RuntimeException(e);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        MyPopUp.showErrorDialog(RincianPembayaran.this, "Gagal menampilkan data", "Silahkan cek kembali koneksi anda", new OnDialogButtonClickListener() {
                            @Override
                            public void onDismissClicked(Dialog dialog) {
                                super.onDismissClicked(dialog);
                                dialog.dismiss();
                                finish();
                            }
                        });
                    }
                });

        requestQueue.add(jsonObjectRequest);
    }

    public static String getEndpointFromUrl(String url) {
        try {
            URI uri = new URI(url);
            String path = uri.getPath();
            String[] segments = path.split("/");
            return segments[segments.length - 1];
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void loadImageToImageView(String imageUrl, ImageView imageView) {
        String fullImageUrl = "http://" + NetworkUtils.BASE_URL + "/PHP-MVC/public/bukti_transfer/" + imageUrl;
        Picasso.get().load(fullImageUrl).into(imageView);
    }

    private void getRekening(String roomId){
        String url = "http://" + NetworkUtils.BASE_URL + "/PHP-MVC/public/GetDataMobile/getUserKost/" + roomId;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject data = response.getJSONObject("data");
                            String noRek = data.getString("no_rekening");

                            rekeningTujuan.setText(noRek);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        MyPopUp.showErrorDialog(RincianPembayaran.this, "Gagal menampilkan data", "Silahkan cek kembali koneksi anda", new OnDialogButtonClickListener() {
                            @Override
                            public void onDismissClicked(Dialog dialog) {
                                super.onDismissClicked(dialog);
                                dialog.dismiss();
                                finish();
                            }
                        });
                    }
                });

        requestQueue.add(jsonObjectRequest);
    }

    private void getDataUser(String userId){
        String apiUrl = "http://" + NetworkUtils.BASE_URL + "/PHP-MVC/public/GetDataMobile/getUserData/" + userId;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, apiUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            int code = jsonObject.getInt("code");
                            String status = jsonObject.getString("status");

                            if (code == 200 && status.equals("success")) {
                                JSONObject dataObject = jsonObject.getJSONObject("data");

                                String userName = dataObject.getString("Nama Penghuni");

                                namaUser.setText(userName);

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        MyPopUp.showErrorDialog(RincianPembayaran.this, "Gagal menampilkan data", "Silahkan cek kembali koneksi anda", new OnDialogButtonClickListener() {
                            @Override
                            public void onDismissClicked(Dialog dialog) {
                                super.onDismissClicked(dialog);
                                dialog.dismiss();
                                finish();
                            }
                        });
                    }
                });

        // Tambahkan request ke queue Volley
        Volley.newRequestQueue(RincianPembayaran.this).add(stringRequest);
    }

    public static String formatDec(int val) {
        return String.format("%,d", val).replace('.', ',');
    }
}