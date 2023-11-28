package com.example.j_kost.DetailActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;

import com.example.j_kost.R;

public class MetodePembayaran extends AppCompatActivity {

    private ListView listViewMetodePembayaran;
    ImageView btnBack;
    private ArrayAdapter<String> adapter;
    private SearchView searchView;
    private String[] metodePembayaran = {
            "Bank BRI",
            "Bank BCA",
            "Bank BNI",
            "Bank Jatim",
            "Bank Mandiri",
            "Bank BSI",
            "Dana",
            "Shopeepay",
            "Ovo",
            "Link Aja"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_metode_pembayaran);

        btnBack = findViewById(R.id.btnBack);
        listViewMetodePembayaran = findViewById(R.id.listViewMetodePembayaran);
        searchView = findViewById(R.id.searchView);

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, metodePembayaran);
        listViewMetodePembayaran.setAdapter(adapter);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        listViewMetodePembayaran.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedValue = (String) adapterView.getItemAtPosition(i);
                Intent resultIntent = new Intent();
                resultIntent.putExtra("selectedValue", selectedValue);
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });

        // Mengatur fungsi pencarian
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });
    }
}