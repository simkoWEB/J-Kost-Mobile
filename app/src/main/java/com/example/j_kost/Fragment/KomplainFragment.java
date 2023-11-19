package com.example.j_kost.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.j_kost.R;

public class KomplainFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_komplain, container, false);

        final Spinner jenisKomplainSpinner = view.findViewById(R.id.jenis_komplain);
        final Spinner masalahKomplainSpinner = view.findViewById(R.id.masalah_komplain);

        ArrayAdapter<CharSequence> jenisKomplainAdapter = ArrayAdapter.createFromResource(getContext(), R.array.TipeKomplain, android.R.layout.simple_spinner_item);
        jenisKomplainAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        jenisKomplainSpinner.setAdapter(jenisKomplainAdapter);

        jenisKomplainSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Mendapatkan pilihan yang dipilih pada spinner jenis komplain
                String selectedTipeKomplain = parent.getItemAtPosition(position).toString();

                // Set adapter untuk spinner masalah komplain berdasarkan pilihan pada jenis komplain
                ArrayAdapter<CharSequence> masalahKomplainAdapter = null;
                switch (selectedTipeKomplain) {
                    case "Keamanan":
                        masalahKomplainAdapter = ArrayAdapter.createFromResource(getContext(), R.array.komplainKeamanan, android.R.layout.simple_spinner_item);
                        break;
                    case "Fasilitas":
                        masalahKomplainAdapter = ArrayAdapter.createFromResource(getContext(), R.array.komplainFasilitas, android.R.layout.simple_spinner_item);
                        break;
                    case "Kegaduhan":
                        masalahKomplainAdapter = ArrayAdapter.createFromResource(getContext(), R.array.komplainKegaduhan, android.R.layout.simple_spinner_item);
                        break;
                    // Handle jika memilih "Pilih tipe komplain" atau "Lainnya"
                    default:
                        masalahKomplainAdapter = ArrayAdapter.createFromResource(getContext(), R.array.defaultArray, android.R.layout.simple_spinner_item);
                        break;
                }

                // Set dropdown style
                if (masalahKomplainAdapter != null) {
                    masalahKomplainAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    masalahKomplainSpinner.setAdapter(masalahKomplainAdapter);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });
        return view;
    }
}