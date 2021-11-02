package com.example.miproyecto;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.miproyecto.Adapters.ProductAdapter;
import com.example.miproyecto.databinding.ActivityListProductsBinding;
import com.example.miproyecto.entities.ProductEntity;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ListProductsActivity<Products> extends AppCompatActivity {

    private ActivityListProductsBinding listProductsBinding;
    private FirebaseFirestore db;
    ArrayList<ProductEntity> productEntityArrayList;
    ProductAdapter productAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        listProductsBinding=ActivityListProductsBinding.inflate (getLayoutInflater ());
        View view = listProductsBinding.getRoot ();
        setContentView (view);
        //solo puede haber un setContentView. Arreglo que sea dinamico se llaman list
        db = FirebaseFirestore.getInstance();
        productEntityArrayList=new ArrayList<>();
        productAdapter = new ProductAdapter(this, productEntityArrayList, db);
        listProductsBinding.rvProducts.setHasFixedSize(true);
        listProductsBinding.rvProducts.setLayoutManager (new LinearLayoutManager (this));
        listProductsBinding.rvProducts.setAdapter (productAdapter);
        getProducts ();
    }
    public void getProducts(){
        db.collection ("products")
                .addSnapshotListener (new EventListener<QuerySnapshot> ()
                {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                     if(error != null){
                         Toast.makeText (getApplicationContext (),
                                 "failed to retrive data", Toast.LENGTH_LONG).show ();
                         return;
                     }
                     for(DocumentChange dc : value.getDocumentChanges ()){
                         if(dc.getType () == DocumentChange.Type.ADDED){

                             productEntityArrayList.add(dc.getDocument().toObject(ProductEntity.class));

                         }
                     }
                        productAdapter.notifyDataSetChanged ();
                    }
                });
    }
}